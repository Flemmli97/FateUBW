package io.github.flemmli97.fateubw.common.entity.servant;


import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.minions.HassanClone;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EntityHassan extends BaseServant {

    public static final AnimatedAction MELEE_1 = new AnimatedAction(0.48, 0.36, "slash_1");
    public static final AnimatedAction MELEE_1_2 = new AnimatedAction(0.48, 0.36, "slash_1_2");
    public static final AnimatedAction MELEE_2 = new AnimatedAction(0.48, 0.36, "slash_2");
    public static final AnimatedAction MELEE_2_2 = new AnimatedAction(0.48, 0.36, "slash_2_2");
    public static final AnimatedAction STAB = new AnimatedAction(0.68, 0.36, "stab");
    public static final AnimatedAction STAB_2 = new AnimatedAction(0.92, 0.44, "top_stab");
    public static final AnimatedAction THROW = new AnimatedAction(0.96, 0.32, "dagger_throw");

    private static final AnimatedAction DUPE = new AnimatedAction(1.4, 0.84, "dupe");
    public static final AnimatedAction SUMMON = new AnimatedAction(2., 0, "summon");
    private static final AnimatedAction[] ANIMS = {MELEE_1, MELEE_1_2, MELEE_2, MELEE_2_2, STAB, STAB_2, THROW, DUPE, SUMMON};

    private static final byte SMOKE = 64;

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityHassan>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityHassan>(EntityHassan.MELEE_1)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .chain(GoalAttackAction.<EntityHassan>chainBuilder(EntityHassan.MELEE_1_2)
                            .chain(List.of(new GoalAttackAction.ChainedAction<>(EntityHassan.MELEE_1_2, e -> 0),
                                    new GoalAttackAction.ChainedAction<>(EntityHassan.MELEE_2_2, e -> 0)))
                            .withPredicate(e -> e.getRandom().nextFloat() < 0.7))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 11),
            WeightedEntry.wrap(new GoalAttackAction<EntityHassan>(EntityHassan.MELEE_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .chain(GoalAttackAction.<EntityHassan>chainBuilder(EntityHassan.MELEE_2_2)
                            .chain(List.of(new GoalAttackAction.ChainedAction<>(EntityHassan.MELEE_2_2, e -> 0),
                                    new GoalAttackAction.ChainedAction<>(EntityHassan.MELEE_1_2, e -> 0)))
                            .withPredicate(e -> e.getRandom().nextFloat() < 0.7))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 11),
            WeightedEntry.wrap(new GoalAttackAction<EntityHassan>(EntityHassan.STAB)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityHassan>(EntityHassan.STAB_2)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityHassan>(EntityHassan.THROW)
                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
                    .withCondition(((goal, target, previous) -> goal.distanceToTargetSq > 25 || goal.attacker.getRandom().nextFloat() < 0.5))
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 14, 1.1))), 12),
            WeightedEntry.wrap(new GoalAttackAction<EntityHassan>(EntityHassan.DUPE)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition((goal, target, prev) -> Utils.<EntityHassan>npCheck().test(goal, target, prev) && goal.attacker.gatherCopies().isEmpty())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(4, 6, 1.1))), 15)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityHassan>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 2)
    );

    public final AnimatedAttackGoal<EntityHassan> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityHassan> animationHandler = new AnimationHandler<>(this, ANIMS).setAnimationChangeCons(anim -> {
        if (!SUMMON.is(anim)) {
            if (!this.offHandCache.isEmpty()) {
                this.setItemInHand(InteractionHand.OFF_HAND, this.offHandCache);
                this.offHandCache = ItemStack.EMPTY;
            }
            if (!this.mainHandCache.isEmpty()) {
                this.setItemInHand(InteractionHand.MAIN_HAND, this.mainHandCache);
                this.mainHandCache = ItemStack.EMPTY;
            }
        }
    });

    private ItemStack mainHandCache = ItemStack.EMPTY;
    private ItemStack offHandCache = ItemStack.EMPTY;

    private final Vector4f summonColor = new Vector4f(28 / 255f, 29 / 255f, 31 / 255f, 0.8f);

    private final Set<UUID> copies = new HashSet<>();

    public EntityHassan(EntityType<? extends EntityHassan> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ASSASSIN_DAGGER.get()));
    }

    @Override
    public boolean canUseNP() {
        return super.canUseNP() && this.gatherCopies().isEmpty();
    }

    @Override
    public AnimationHandler<EntityHassan> getAnimationHandler() {
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
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(DUPE)) {
            if (anim.isAtTick(0.72)) {
                this.level.broadcastEntityEvent(this, SMOKE);
            }
            if (anim.canAttack()) {
                if (!this.forcedNP)
                    this.useMana(this.props().hogouMana());
                this.summonClones();
                this.forcedNP = false;
            }
        } else if (anim.is(THROW)) {
            if (anim.canAttack()) {
                this.throwItem(true);
            } else if (anim.isAtTick(0.84)) {
                this.throwItem(false);
            }
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() + 0.4;
        double length = 1;
        if (anim.is(MELEE_1, MELEE_2_2)) {
            width += 1.3;
            length += 0.7;
        }
        if (anim.is(MELEE_2, MELEE_1_2)) {
            width += 1.1;
            length += 0.6;
        }
        if (anim.is(STAB)) {
            length += 1.1;
        }
        if (anim.is(STAB_2)) {
            width += 0.2;
            length += 0.8;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    public void removeCopy(HassanClone copy) {
        this.copies.remove(copy.getUUID());
    }

    public boolean addCopy(HassanClone copy) {
        if (this.copies.size() < Config.Common.hassanCopies) {
            this.copies.add(copy.getUUID());
            return true;
        }
        return false;
    }

    public List<HassanClone> gatherCopies() {
        ArrayList<HassanClone> list = new ArrayList<>();
        for (HassanClone e : this.level.getEntitiesOfClass(HassanClone.class, this.getBoundingBox().inflate(32))) {
            if (this.copies.contains(e.getUUID())) {
                e.setOriginal(this);
                list.add(e);
            }
        }
        return list;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == SMOKE) {
            for (int i = 0; i < 32; i++) {
                this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        this.getX((this.getRandom().nextDouble() * 2 - 1) * 1.2), this.getY(this.getRandom().nextDouble() * 1.2), this.getZ((this.getRandom().nextDouble() * 2 - 1) * 1.2),
                        this.getRandom().nextGaussian() * 0.02, this.getRandom().nextGaussian() * 0.02, this.getRandom().nextGaussian() * 0.02);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.dead && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void summonClones() {
        if (this.gatherCopies().isEmpty()) {
            this.copies.clear();
            for (int i = 0; i < Config.Common.hassanCopies; i++) {
                HassanClone hassan = new HassanClone(this.level, this);
                hassan.moveTo(this.getX(), this.getY(), this.getZ(), Mth.wrapDegrees(this.level.random.nextFloat() * 360.0F), 0.0F);
                hassan.finalizeSpawn((ServerLevelAccessor) this.level, this.level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                this.level.addFreshEntity(hassan);
                this.addCopy(hassan);
            }
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 300, 1, true, false));
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 2, true, false));
            for (Mob mob : this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(32))) {
                if (mob.getTarget() == this)
                    mob.setTarget(null);
            }
            this.revealServant();
        }
    }

    public void throwItem(boolean main) {
        ThrownItemEntity item = new ThrownItemEntity(this.level, this);
        item.setWeapon(this.getWeaponToThrowAndReplace(main));
        if (this.getTarget() != null) {
            item.shootAtEntity(this.getTarget(), 1.2f, 7 - this.level.getDifficulty().getId() * 2);
        } else {
            item.shootFromRotation(this, this.getXRot() + 5, this.getYRot(), 0.0F, 1.2f, 1.0F);
        }
        this.playSound(SoundEvents.FISHING_BOBBER_THROW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(item);
    }

    private ItemStack getWeaponToThrowAndReplace(boolean main) {
        ItemStack weapon;
        if (!main) {
            if (!this.getOffhandItem().isEmpty()) {
                this.offHandCache = this.getOffhandItem();
                this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                weapon = this.offHandCache;
            } else
                weapon = this.getMainHandItem().isEmpty() ? this.mainHandCache.copy() : this.getMainHandItem();
        } else {
            this.mainHandCache = this.getMainHandItem();
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            weapon = this.mainHandCache;
        }
        return weapon.isEmpty() ? new ItemStack(ModItems.ASSASSIN_DAGGER.get()) : weapon.copy();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ListTag copies = new ListTag();
        this.copies.forEach(hassan -> copies.add(NbtUtils.createUUID(hassan)));
        tag.put("Copies", copies);
        tag.put("MainHandCache", this.mainHandCache.save(new CompoundTag()));
        tag.put("OffHandCache", this.offHandCache.save(new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        tag.getList("Copies", Tag.TAG_INT_ARRAY).forEach(nbt -> this.copies.add(NbtUtils.loadUUID(nbt)));
        this.mainHandCache = ItemStack.of(tag.getCompound("MainHandCache"));
        this.offHandCache = ItemStack.of(tag.getCompound("OffHandCache"));
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