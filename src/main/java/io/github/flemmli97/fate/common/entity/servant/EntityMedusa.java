package io.github.flemmli97.fate.common.entity.servant;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.fate.common.entity.servant.ai.MedusaAttackGoal;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityMedusa extends EntityServant {

    public final MedusaAttackGoal attackAI = new MedusaAttackGoal(this);

    private static final AnimatedAction npAttack = new AnimatedAction(20, 0, "np");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, npAttack};

    private final AnimationHandler<EntityMedusa> animationHandler = new AnimationHandler<>(this, anims);

    public EntityMedusa(EntityType<? extends EntityServant> entityType, World world) {
        super(entityType, world, "medusa.hogou");
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.medusaDagger.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals("np");
        return anim.getID().equals("vanilla");
    }

    @Override
    public AnimationHandler<EntityMedusa> getAnimationHandler() {
        return this.animationHandler;
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

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.getRidingEntity() != null && this.getRidingEntity() instanceof LivingEntity) {
            LivingEntity mount = (LivingEntity) this.getRidingEntity();
            return mount.attackEntityFrom(damageSource, damage);
        }
        return super.attackEntityFrom(damageSource, damage);
    }

    public void attackWithNP() {
        /*if(!this.world.isRemote)
        {
            EntityPegasus peg = new EntityPegasus(this.world);
            peg.setPosition(this.posX, this.posY, this.posZ);
            for(int x=0;x < 5;x++)
            {
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.posX, this.posY, this.posZ, true));
            }
            List<Entity> list =this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(5, 3.0D, 5));
            for(int x = 0; x< list.size();x++)
            {
                if(list.get(x) instanceof EntityLivingBase)
                {
                    EntityLivingBase ent = (EntityLivingBase) list.get(x);
                    ent.knockBack(this, 5.0F, MathHelper.sin(this.rotationYaw * 0.017453292F), (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                }
            }
            this.world.spawnEntity(peg);
            this.startRiding(peg);
            this.revealServant();
        }*/
    }
}
