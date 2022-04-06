package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.minions.LesserMonster;
import io.github.flemmli97.fateubw.common.entity.servant.ai.GillesAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityGilles extends BaseServant {

    public final GillesAttackGoal attackAI = new GillesAttackGoal(this, 16);

    private static final AnimatedAction rangedAttack = new AnimatedAction(32, 25, "cast");
    private static final AnimatedAction npAttack = new AnimatedAction(20, 0, "np");
    private static final AnimatedAction[] anims = {rangedAttack, npAttack};

    private final AnimationHandler<EntityGilles> animationHandler = new AnimationHandler<>(this, anims);

    public EntityGilles(EntityType<? extends EntityGilles> entityType, Level world) {
        super(entityType, world, LibEntities.gilles + ".hogou");
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.grimoire.get()));
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
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void attackWithNP() {
        if (!this.level.isClientSide) {
            //EntityMonster minion = new EntityMonster(this.world, this);
            //this.world.spawnEntity(minion);
            //minion.setAttackTarget(this.getAttackTarget());
        }
    }

    public void attackWithRangedAttack(LivingEntity target) {
        if (!this.level.isClientSide) {
            if (this.level.getEntitiesOfClass(LesserMonster.class, this.getBoundingBox().inflate(16), monster -> monster.getOwnerUUID().equals(this.getUUID())).size() < Config.Common.gillesMinionAmount) {
                LesserMonster minion = new LesserMonster(this.level, this);
                BlockPos pos = RayTraceUtils.randomPosAround(this.level, minion, this.blockPosition(), 9, true, this.getRandom());
                if (pos != null) {
                    minion.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Mth.wrapDegrees(this.level.random.nextFloat() * 360.0F), 0.0F);
                    this.level.addFreshEntity(minion);
                    minion.setTarget(this.getTarget());
                    this.revealServant();
                }
            }
        }
    }
}
