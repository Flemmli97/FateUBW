package io.github.flemmli97.fateubw.common.entity.servant;


import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.common.entity.minions.Gordius;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class EntityIskander extends BaseServant {

    public static final AnimatedAction MELEE_1 = new AnimatedAction(0.56, 0.4, "horizontal_slash");
    public static final AnimatedAction MELEE_2 = new AnimatedAction(0.48, 0.4, "slash_1");
    public static final AnimatedAction MELEE_3 = new AnimatedAction(0.48, 0.4, "slash_2");
    public static final AnimatedAction MELEE_4 = new AnimatedAction(0.44, 0.36, "vertical_slash");

    public static final AnimatedAction MOUNT_STAND_MELEE_1 = new AnimatedAction(0.48, 0.4, "slash_mounted_standing_1");
    public static final AnimatedAction MOUNT_STAND_MELEE_2 = new AnimatedAction(0.48, 0.4, "slash_mounted_standing_2");
    public static final AnimatedAction MOUNT_MELEE_1 = new AnimatedAction(0.48, 0.4, "slash_mounted_1");
    public static final AnimatedAction MOUNT_MELEE_2 = new AnimatedAction(0.48, 0.4, "slash_mounted_2");

    private static final AnimatedAction CHARIOT = new AnimatedAction(1.48, 0.68, "chariot_summon");
    public static final AnimatedAction SUMMON = new AnimatedAction(2., 0, "summon");
    private static final AnimatedAction[] ANIMS = {MELEE_1, MELEE_2, MELEE_3, MELEE_4, CHARIOT, SUMMON};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityIskander>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MELEE_1)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MELEE_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MELEE_3)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityIskander>(EntityIskander.MELEE_4)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityIskander>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 2)
    );

    public final AnimatedAttackGoal<EntityIskander> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityIskander> animationHandler = new AnimationHandler<>(this, ANIMS);
    private final Vector4f summonColor = new Vector4f(112 / 255f, 23 / 255f, 21 / 255f, 0.7f);

    public EntityIskander(EntityType<? extends EntityIskander> entityType, Level level) {
        super(entityType, level);
        this.canUseNP = true;
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.KUPRIOTS.get()));
    }

    @Override
    public AnimationHandler<EntityIskander> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attack);
        else
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return super.hurt(damageSource, damage);
        } else if (this.isPassenger()) {
            this.getVehicle().hurt(damageSource, damage);
        }
        return super.hurt(damageSource, damage);
    }

    public boolean attackWithNP() {
        if (this.isPassenger() || this.level.isClientSide)
            return false;
        Gordius wheel = ModEntities.GORDIUS_WHEEL.get().create(this.level);
        wheel.setPos(this.getX(), this.getY(), this.getZ());
        this.level.addFreshEntity(wheel);
        this.boardingCooldown = 0;
        this.startRiding(wheel);
        //spawn lightning
        for (int i = 0; i < 5; i++) {
            LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level);
            lightningboltentity.moveTo(this.getX() + this.random.nextGaussian() * 2, this.getY(), this.getZ() + this.random.nextGaussian() * 2);
            lightningboltentity.setVisualOnly(true);
            this.level.addFreshEntity(lightningboltentity);
        }
        this.revealServant();
        return true;
    }

    @Override
    public boolean attacksFromMount() {
        return false;
    }

    @Override
    protected AnimatedAction getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }
}
