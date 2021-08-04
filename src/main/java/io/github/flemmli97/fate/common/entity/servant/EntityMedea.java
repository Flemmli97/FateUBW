package io.github.flemmli97.fate.common.entity.servant;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.entity.EntityCasterCircle;
import io.github.flemmli97.fate.common.entity.EntityMagicBeam;
import io.github.flemmli97.fate.common.entity.servant.ai.MedeaAttackGoal;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityMedea extends EntityServant {

    public final MedeaAttackGoal attackAI = new MedeaAttackGoal(this, 16);

    private static final AnimatedAction npAttack = new AnimatedAction(20, 0, "np");
    private static final AnimatedAction ranged = new AnimatedAction(30, 5, "beam");

    private static final AnimatedAction[] anims = {ranged, npAttack};

    private final AnimationHandler<EntityMedea> animationHandler = new AnimationHandler<>(this, anims);

    private int circleDelay;

    private static final int[][] castOffsets = {{-1, 0}, {1, 0}, {0, 2}};

    public EntityMedea(EntityType<? extends EntityServant> entityType, World world) {
        super(entityType, world, "medea.hogou");
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.staff.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        return false;
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
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (!this.canUseNP && !this.getShouldBeDead() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void buff() {
        this.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1, 2, true, false));
        if (!this.isPotionActive(Effects.REGENERATION))
            this.addPotionEffect(new EffectInstance(Effects.REGENERATION, 50, 1, true, false));
        this.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1, 2, true, false));
    }

    public void attackWithNP() {

    }

    public void attackWithRangedAttack(LivingEntity target) {
        int strength = 0;
        EffectInstance eff = this.getActivePotionEffect(Effects.STRENGTH);
        if (eff != null)
            strength = eff.getAmplifier();
        EntityMagicBeam beam = new EntityMagicBeam(this.world, this, target, strength);
        beam.setRotationTo(target.getPosX(), target.getPosY(), target.getPosZ(), 0.8f);


        Vector3d look = this.getLookVec();
        Vector3d vert = new Vector3d(0, 1, 0).rotatePitch(this.rotationPitch);
        Vector3d hor = look.crossProduct(vert).normalize();
        int[] offset = castOffsets[this.rand.nextInt(castOffsets.length)];
        Vector3d area = this.getPositionVec().add(hor.scale(offset[0])).add(vert.scale(offset[1]));
        beam.setPosition(area.x, area.y + this.getEyeHeight(), area.z);

        this.world.addEntity(beam);
        this.revealServant();
    }

    public void makeCircle() {
        if (!this.world.isRemote && this.circleDelay == 0) {
            this.world.addEntity(new EntityCasterCircle(this.world, this, Config.Common.medeaCircleRange));
            this.circleDelay = Config.Common.medeaCircleSpan;
            if (this.getOwner() != null)
                this.getOwner().sendMessage(new TranslationTextComponent("chat.medea.circle.spawn"), Util.DUMMY_UUID);
        }
    }

    @Override
    public String[] specialCommands() {
        return new String[]{"medea.circle"};
    }

    @Override
    public void doSpecialCommand(String s) {
        if (s.equals("medea.circle"))
            this.makeCircle();
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        tag.putInt("CircleDelay", this.circleDelay);
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        this.circleDelay = tag.getInt("CircleDelay");
    }

    public enum PegasusState {
        RECHARGE,
        FLYING,
        CHARGING,
        FLYCHARGING
    }
}
