package io.github.flemmli97.fate.common.entity.servant;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.entity.EntityLesserMonster;
import io.github.flemmli97.fate.common.entity.servant.ai.GillesAttackGoal;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityGilles extends EntityServant {

    public final GillesAttackGoal attackAI = new GillesAttackGoal(this, 16);

    private static final AnimatedAction rangedAttack = new AnimatedAction(32, 25, "cast");
    private static final AnimatedAction npAttack = new AnimatedAction(20, 0, "np");
    private static final AnimatedAction[] anims = {rangedAttack, npAttack};

    private final AnimationHandler<EntityGilles> animationHandler = new AnimationHandler<>(this, anims);

    public EntityGilles(EntityType<? extends EntityGilles> entityType, World world) {
        super(entityType, world, "Prelati's Spellbook");
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.grimoire.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        return anim.getID().equals(rangedAttack.getID());
    }

    @Override
    public AnimationHandler<EntityGilles> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public int attackCooldown(AnimatedAction anim) {
        return 90;
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attackAI);
        else
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (!this.canUseNP && !this.getShouldBeDead() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void attackWithNP() {
        if (!this.world.isRemote) {
            //EntityMonster minion = new EntityMonster(this.world, this);
            //this.world.spawnEntity(minion);
            //minion.setAttackTarget(this.getAttackTarget());
        }
    }

    public void attackWithRangedAttack(LivingEntity target) {
        if (!this.world.isRemote) {
            if (this.world.getEntitiesWithinAABB(EntityLesserMonster.class, this.getBoundingBox().grow(16), monster -> monster.getOwnerUUID().equals(this.getUniqueID())).size() < Config.Common.gillesMinionAmount) {
                EntityLesserMonster minion = new EntityLesserMonster(this.world, this);
                BlockPos pos = RayTraceUtils.randomPosAround(this.world, minion, this.getPosition(), 9, true, this.getRNG());
                if (pos != null) {
                    minion.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, MathHelper.wrapDegrees(this.world.rand.nextFloat() * 360.0F), 0.0F);
                    this.world.addEntity(minion);
                    minion.setAttackTarget(this.getAttackTarget());
                    this.revealServant();
                }
            }
        }
    }
}
