package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.misc.MagicBeam;
import io.github.flemmli97.fateubw.common.entity.misc.MagicBufCircle;
import io.github.flemmli97.fateubw.common.entity.servant.ai.MedeaAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityMedea extends BaseServant {

    public final MedeaAttackGoal attackAI = new MedeaAttackGoal(this, 16);

    private static final AnimatedAction npAttack = new AnimatedAction(20, 0, "np");
    private static final AnimatedAction ranged = new AnimatedAction(30, 5, "beam");

    private static final AnimatedAction[] anims = {ranged, npAttack};

    private final AnimationHandler<EntityMedea> animationHandler = new AnimationHandler<>(this, anims);

    private int circleDelay;

    private static final int[][] castOffsets = {{-2, 0}, {-1, 0}, {1, 0}, {0, 1}, {0, 2}};

    public EntityMedea(EntityType<? extends BaseServant> entityType, Level world) {
        super(entityType, world, LibEntities.medea + ".hogou");
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.staff.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.RANGED)
            return anim.getID().equals(ranged.getID());
        return type == AttackType.NP && anim.getID().equals(npAttack.getID());
    }

    @Override
    public AnimationHandler<EntityMedea> getAnimationHandler() {
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
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void buff() {
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1, 2, true, false));
        if (!this.hasEffect(MobEffects.REGENERATION))
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, 1, true, false));
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1, 2, true, false));
    }

    public void attackWithNP() {

    }

    public void attackWithRangedAttack(LivingEntity target) {
        int strength = 0;
        MobEffectInstance eff = this.getEffect(MobEffects.DAMAGE_BOOST);
        if (eff != null)
            strength = eff.getAmplifier();
        for (int i = 0; i < 3; i++) {
            MagicBeam beam = new MagicBeam(this.level, this, target, strength);
            Vec3 side = MathUtils.rotate(MathUtils.normalY, MathUtils.normalX, -this.getYRot() * Mth.DEG_TO_RAD);
            int[] offset = castOffsets[this.random.nextInt(castOffsets.length)];
            Vec3 area = this.position().add(side.scale(offset[0])).add(0, this.getBbHeight() + offset[1], 0);
            beam.setPos(area.x, area.y, area.z);
            beam.setRotationTo(target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(), 0f);
            this.level.addFreshEntity(beam);
        }
        this.revealServant();
    }

    public void makeCircle() {
        if (!this.level.isClientSide && this.circleDelay == 0) {
            this.level.addFreshEntity(new MagicBufCircle(this.level, this, Config.Common.medeaCircleRange));
            this.circleDelay = Config.Common.medeaCircleSpan;
            if (this.getOwner() != null)
                this.getOwner().sendMessage(new TranslatableComponent("chat.medea.circle.spawn"), Util.NIL_UUID);
        }
    }

    @Override
    public String[] specialCommands() {
        return new String[]{LibEntities.medea + ".circle"};
    }

    @Override
    public void doSpecialCommand(String s) {
        if (s.equals(LibEntities.medea + ".circle"))
            this.makeCircle();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("CircleDelay", this.circleDelay);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.circleDelay = tag.getInt("CircleDelay");
    }

    public enum PegasusState {
        RECHARGE,
        FLYING,
        CHARGING,
        FLYCHARGING
    }
}
