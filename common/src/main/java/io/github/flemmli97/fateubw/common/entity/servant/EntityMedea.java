package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.misc.MagicBeam;
import io.github.flemmli97.fateubw.common.entity.misc.MagicBufCircle;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
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
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityMedea extends BaseServant {

    private static final AnimatedAction NP_ATTACK = AnimatedAction.builder(20, "np").build();
    private static final AnimatedAction RANGED = AnimatedAction.builder(30, "beam").marker("attack", 5).build();

    private static final AnimatedAction[] ANIMS = {RANGED, NP_ATTACK};


    private final AnimationHandler<EntityMedea> animationHandler = new AnimationHandler<>(this, ANIMS);

    private int circleDelay;

    private static final int[][] castOffsets = {{-2, 0}, {-1, 0}, {1, 0}, {0, 1}, {0, 2}};

    public EntityMedea(EntityType<? extends BaseServant> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STAFF.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.RANGED)
            return anim.getID().equals(RANGED.getID());
        return type == AttackType.NP && anim.getID().equals(NP_ATTACK.getID());
    }

    @Override
    public AnimationHandler<EntityMedea> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
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
            Vec3 side = MathUtils.rotate(MathUtils.NORMAL_Y, MathUtils.NORMAL_X, -this.getYRot() * Mth.DEG_TO_RAD);
            int[] offset = castOffsets[this.random.nextInt(castOffsets.length)];
            Vec3 area = this.position().add(side.scale(offset[0])).add(0, this.getBbHeight() + offset[1], 0);
            beam.setPos(area.x, area.y, area.z);
            beam.setRotationTo(target, 0);
            this.level.addFreshEntity(beam);
        }
        this.revealServant();
    }

    public void makeCircle() {
        if (!this.level.isClientSide && this.circleDelay == 0) {
            this.level.addFreshEntity(new MagicBufCircle(this.level, this, Config.Common.medeaCircleRange));
            this.circleDelay = Config.Common.medeaCircleSpan;
            if (this.getOwner() != null)
                this.getOwner().sendMessage(new TranslatableComponent("fateubw.chat.medea.circle.spawn"), Util.NIL_UUID);
        }
    }


    @Override
    public PathNavigation getNavigation() {
        return this.navigation;
    }

    @Override
    public String[] specialCommands() {
        return new String[]{ModEntities.MEDEA.getID() + ".circle"};
    }

    @Override
    public void doSpecialCommand(String s) {
        if (s.equals(ModEntities.MEDEA.getID() + ".circle"))
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
