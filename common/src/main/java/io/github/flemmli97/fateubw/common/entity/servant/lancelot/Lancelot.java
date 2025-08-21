package io.github.flemmli97.fateubw.common.entity.servant.lancelot;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.LeapInDirection;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.joml.Vector4f;

import java.util.function.Predicate;

public class Lancelot extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String TWO_HAND_1 = BUILDER.add("two_hand_1", AnimationsBuilder.definition(0.78)
            .marker("attack", 0.64).marker("step", 0.64));
    public static final String TWO_HAND_2 = BUILDER.add("two_hand_2", AnimationsBuilder.definition(0.7)
            .marker("attack", 0.52).marker("step", 0.52));
    public static final String TWO_HAND_3 = BUILDER.add("two_hand_3", AnimationsBuilder.definition(0.7)
            .marker("attack", 0.48).marker("step", 0.48));
    public static final String TWO_HAND_4 = BUILDER.add("two_hand_4", AnimationsBuilder.definition(0.7)
            .marker("attack", 0.56).marker("step", 0.56));
    public static final String ONE_HAND_1 = BUILDER.add("one_hand_1", AnimationsBuilder.definition(0.56)
            .marker("attack", 0.4).marker("step", 0.48));
    public static final String STAB_1 = BUILDER.add("stab_1", AnimationsBuilder.definition(0.76).marker("attack", 0.44));
    public static final String JUMP = BUILDER.add("jump", AnimationsBuilder.definition(0.36).marker("jump", 0.2).infinite());
    public static final String JUMP_LAND = BUILDER.add("jump_land", AnimationsBuilder.definition(0.8).marker("attack", 0.24));
    public static final String TRIDENT = BUILDER.add("trident", AnimationsBuilder.definition(1.08).marker("attack", 0.76));
    public static final String BOW = BUILDER.add("bow", AnimationsBuilder.definition(1.24).marker("attack", 1));
    public static final String CROSSBOW = BUILDER.add("crossbow", AnimationsBuilder.definition(1.88).marker("attack", 1.4));
    public static final String GUN_SMALL = BUILDER.add("gun_small", AnimationsBuilder.definition(0.8).marker("attack", 0.48));
    public static final String GUN_BIG = BUILDER.add("gun_big", AnimationsBuilder.definition(1.28).marker("attack", 0.4));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Lancelot> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (anim != null) {
                    this.swapWithInventory(anim.animation());
                } else {
                    this.swapWithInventory(null);
                }
                return false;
            });

    private final Vector4f summonColor = new Vector4f(28 / 255f, 28 / 255f, 33 / 255f, 0.7f);

    private final LancelotInventory inventory = new LancelotInventory(this);
    private final SimpleContainer swapped = new SimpleContainer(1);
    private int pickupDelay;

    public Lancelot(EntityType<? extends Lancelot> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.ARONDIGHT.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<Lancelot>create()
                .start(BehaviourUtils.of(AnimationPlayHolder.<Lancelot>builder(TWO_HAND_1)
                        .start(TWO_HAND_2, 2, 0.24f, 1)
                        .start(TWO_HAND_3, 2, 0.24f, 1)
                        .chainChance(0.5f).build())).play(BehaviourUtils.cooldownedPlay(true, 20, 30))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(BehaviourUtils.of(AnimationPlayHolder.<Lancelot>builder(TWO_HAND_2)
                        .start(TWO_HAND_1, 2, 0.24f, 1).chain(ONE_HAND_1)
                        .chainChance(0.5f).build())).play(BehaviourUtils.cooldownedPlay(true, 20, 30))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(BehaviourUtils.of(AnimationPlayHolder.<Lancelot>builder(TWO_HAND_4)
                        .start(TWO_HAND_2, 2, 0.24f, 1)
                        .start(TWO_HAND_3, 2, 0.24f, 1)
                        .chainChance(0.5f).build())).play(BehaviourUtils.cooldownedPlay(true, 20, 30))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(ONE_HAND_1).play(BehaviourUtils.cooldownedPlay(true, 20, 30))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(5)
                .start(STAB_1).play(BehaviourUtils.cooldownedPlay(true, 20, 30))
                .condition(entity -> entity.canUseAttack(STAB_1))
                .prepare(new SetWalkTargetWithinDist<Lancelot>().min(2).max(4)).prepareOptional(BehaviourUtils.moveTo())
                .end(5)
                .start(JUMP).play(BehaviourUtils.cooldownedPlay(false, 20, 30))
                .prepare(new SetWalkTargetToAttackTarget<Lancelot>().speedMod((m, e) -> 1.1f)
                        .closeEnoughDist(BehaviourUtils.closeEnough(9))).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(2)
                .start(JUMP).play(BehaviourUtils.cooldownedPlay(false, 20, 25))
                .condition(BehaviourUtils.ifFurtherThan(8))
                .prepare(new SetWalkTargetToAttackTarget<Lancelot>().speedMod((m, e) -> 1.1f)
                        .closeEnoughDist(BehaviourUtils.closeEnough(9))).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(12)
                .start(BOW).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(11), 25, 35))
                .condition(entity -> entity.canUseAttack(BOW))
                .prepare(new LeapInDirection<Lancelot>().shouldLeap((owner, target) -> owner.distanceToSqr(target) < 49)
                        .horizontalDirection((owner, target) -> LeapInDirection.createBackwardsVec(owner.position(), target.position()).scale(1.3f)))
                .end(3)
                .start(BOW).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(11), 25, 35))
                .condition(entity -> entity.canUseAttack(BOW))
                .prepare(new SetWalkTargetWithinDist<Lancelot>()
                        .min(5).max(10).speedMod(1.1f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(2)
                .start(CROSSBOW).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(15), 25, 35))
                .condition(entity -> entity.canUseAttack(CROSSBOW))
                .prepare(new LeapInDirection<Lancelot>().shouldLeap((owner, target) -> owner.distanceToSqr(target) < 49)
                        .horizontalDirection((owner, target) -> LeapInDirection.createBackwardsVec(owner.position(), target.position()).scale(1.3f)))
                .end(3)
                .start(CROSSBOW).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(15), 25, 35))
                .condition(entity -> entity.canUseAttack(CROSSBOW))
                .prepare(new SetWalkTargetWithinDist<Lancelot>()
                        .min(5).max(10).speedMod(1.1f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(2)
                .start(TRIDENT).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(11), 25, 35))
                .condition(entity -> entity.canUseAttack(TRIDENT))
                .prepare(new LeapInDirection<Lancelot>().shouldLeap((owner, target) -> owner.distanceToSqr(target) < 49)
                        .horizontalDirection((owner, target) -> LeapInDirection.createBackwardsVec(owner.position(), target.position()).scale(1.3f)))
                .end(3)
                .start(TRIDENT).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(11), 25, 35))
                .condition(entity -> entity.canUseAttack(TRIDENT))
                .prepare(new SetWalkTargetWithinDist<Lancelot>()
                        .min(5).max(10).speedMod(1.1f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(2)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(6, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(3, new SetWalkTargetAwayFromTarget<BaseServant>()
                        .speedMod(1.1f)
                        .radius(7), BehaviourUtils.moveTo()).build();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        --this.pickupDelay;
        if (this.level().isClientSide) {
            for (int x = 0; x < 2; x++) {
                this.level().addParticle(
                        ParticleTypes.LARGE_SMOKE,
                        this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(),
                        this.getY() + this.random.nextDouble() * this.getBbHeight(),
                        this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(),
                        this.random.nextGaussian() * 0.02D,
                        this.random.nextGaussian() * 0.02D,
                        this.random.nextGaussian() * 0.02D);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Inventory", this.inventory.save(this.registryAccess()));
        tag.put("Swapped", this.swapped.createTag(this.registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.inventory.load(tag.getCompound("Inventory"), this.registryAccess());
        this.swapped.fromTag(tag.getList("Swapped", Tag.TAG_COMPOUND), this.registryAccess());
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(JUMP)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt("jump")) {
                Vec3 dir;
                if (target != null) {
                    dir = target.position().subtract(this.position());
                    dir = new Vec3(dir.x(), 0, dir.z());
                    if (dir.lengthSqr() > 26 * 26)
                        dir = dir.normalize().scale(26 * 0.14);
                    else
                        dir = dir.scale(0.14);
                } else {
                    dir = this.getLookAngle().scale(0.75);
                }
                this.setDeltaMovement(dir.x(), 1, dir.z());
            }
            if (anim.isPast("jump")) {
                this.fallDistance = 0;
                if (anim.done(0)) {
                    if (this.onGround()) {
                        this.getAnimationHandler().setAnimation(this.getAnimationHandler().get(JUMP_LAND),
                                0, AnimationHandler.FALLBACK_TRANSIT_TIME, 0);
                    }
                }
                // Stuck check. Or e.g. if in water
                if (anim.isPast(6.0) && (!this.getInBlockState().is(Blocks.AIR) || !this.getBlockStateOn().is(Blocks.AIR))) {
                    this.getAnimationHandler().setAnimation(this.getAnimationHandler().get(JUMP_LAND),
                            0, AnimationHandler.FALLBACK_TRANSIT_TIME, 0);
                }
            }
        } else if (anim.is(CROSSBOW, BOW, TRIDENT)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt(0.2)) {
                InteractionHand hand = this.toUseHand();
                Pair<ResourceLocation, LancelotUseHandler> handler = LancelotAttackAI.getFor(this.getItemInHand(hand));
                if (handler != null) {
                    handler.getSecond().startUse(this, target, hand);
                }
            }
            if (anim.isAt("attack")) {
                if (target != null && this.getSensing().hasLineOfSight(target)) {
                    Pair<ResourceLocation, LancelotUseHandler> handler = LancelotAttackAI.getFor(this.getUseItem());
                    if (handler != null) {
                        this.useItemRemaining = 1;
                        handler.getSecond().use(this, target, this.getUsedItemHand());
                    }
                }
                this.stopUsingItem();
            }
        } else {
            boolean step = anim.is(TWO_HAND_1) && anim.isAt(0.28) ||
                    anim.is(TWO_HAND_2) && anim.isAt(0.2) ||
                    anim.is(TWO_HAND_3) && anim.isAt(0.08);
            if (step) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(anim.is(TWO_HAND_3) ? 0.25 : 0.3);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            if (anim.isAt("attack") && anim.is(JUMP_LAND)) {
                S2CScreenShake.sendAround(this, 10, 8, 3);
                this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1.0f, 0.9f);
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(JUMP_LAND)) {
            return new OrientedBoundingBox(this.attackBB(anim), this.getYRot(), 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        if (anim.is(JUMP_LAND)) {
            double width = this.getBbWidth() + 4;
            return new AABB(-width * 0.5, -0.02, -width * 0.3, width * 0.5, this.getBbHeight() * 0.5, width * 0.7);
        }
        double width = this.getBbWidth() + 0.3;
        double length = 1;
        if (anim.is(TWO_HAND_1, TWO_HAND_2)) {
            width += 1.1;
            length += 0.8;
        }
        if (anim.is(TWO_HAND_3, TWO_HAND_4)) {
            width += 1.3;
            length += 0.7;
        }
        if (anim.is(ONE_HAND_1)) {
            width += 1.5;
            length += 0.75;
        }
        if (anim.is(STAB_1)) {
            width += 0.1;
            length += 1.6;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public AnimationHandler<Lancelot> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return super.hurt(damageSource, damage);
        if (this.isPassenger())
            return this.getVehicle().hurt(damageSource, damage);
        if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !this.level().isClientSide) {
            if (damageSource.is(DamageTypeTags.IS_PROJECTILE) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
                if (this.getRandom().nextFloat() < this.props().getConfig(ServantExtraData.LANCELOT_REFLECT_CHANCE) && damageSource.getDirectEntity() != null
                        && !(damageSource.getDirectEntity() instanceof LivingEntity)) {
                    this.reflectProjectile(damageSource.getDirectEntity());
                    this.level().playSound(null, this.blockPosition(), SoundEvents.ANVIL_PLACE, SoundSource.NEUTRAL, 1, 1);
                    return false;
                }
            }
        }
        return super.hurt(damageSource, damage);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.ARONDIGHT.get()));
        }
    }

    @Override
    public boolean canPickUpLoot() {
        return true;
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        if (itemEntity.hasPickUpDelay())
            return;
        super.pickUpItem(itemEntity);
    }

    @Override
    public ItemStack equipItemIfPossible(ItemStack stack) {
        if (this.pickupDelay > 0)
            return ItemStack.EMPTY;
        ItemStack res = this.inventory.insert(stack);
        if (res != null) {
            if (!res.isEmpty()) {
                this.spawnAtLocation(res);
            }
            return stack;
        }
        Equipable equipable = Equipable.get(stack);
        EquipmentSlot equipmentSlot = equipable != null ? equipable.getEquipmentSlot() : EquipmentSlot.MAINHAND;
        ItemStack current = this.getItemBySlot(equipmentSlot);
        if (current.getItem() == FateItems.ARONDIGHT.get() && equipmentSlot == EquipmentSlot.MAINHAND)
            return ItemStack.EMPTY;
        if (this.canHoldItem(stack) && this.canReplaceCurrentItem(stack, current)) {
            if (!current.isEmpty()) {
                this.spawnAtLocation(current);
            }
            this.setItemSlotAndDropWhenKilled(equipmentSlot, stack);
            this.revealServant();
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack candidate, ItemStack existing) {
        return ItemUtils.isItemBetter(this, null, candidate, existing);
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        if (stack.getItem() != FateItems.ARONDIGHT.get())
            stack.set(FateDataComponents.CORRUPTED_ITEM.get(), Unit.INSTANCE);
        super.setItemSlot(slot, stack);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        for (ItemStack stack : this.inventory) {
            this.spawnAtLocation(stack);
        }
        this.inventory.clearContent();
    }

    @Override
    public ItemEntity spawnAtLocation(ItemStack stack) {
        stack.remove(FateDataComponents.CORRUPTED_ITEM.get());
        return super.spawnAtLocation(stack);
    }

    public boolean canUseAttack(String animation) {
        if (animation.equals(Lancelot.TRIDENT)) {
            return this.checkHands(s -> LancelotAttackAI.get(LancelotAttackAI.TRIDENT).matches(s)) ||
                    !this.inventory.get(LancelotAttackAI.TRIDENT).isEmpty();
        }
        if (animation.equals(Lancelot.BOW)) {
            return this.checkHands(s -> LancelotAttackAI.get(LancelotAttackAI.BOW).matches(s))
                    || !this.inventory.get(LancelotAttackAI.BOW).isEmpty();
        }
        if (animation.equals(Lancelot.CROSSBOW)) {
            return this.checkHands(s -> LancelotAttackAI.get(LancelotAttackAI.CROSSBOW).matches(s))
                    || !this.inventory.get(LancelotAttackAI.CROSSBOW).isEmpty();
        }
        if (animation.equals(Lancelot.STAB_1)) {
            return this.checkHands(s -> s.is(FateTags.Items.SPEARS))
                    || !this.inventory.get(LancelotAttackAI.TRIDENT).isEmpty()
                    || !this.inventory.get(LancelotInventory.SPEAR).isEmpty();
        }
        return !this.getMainHandItem().isEmpty() || !this.getOffhandItem().isEmpty();
    }

    private boolean checkHands(Predicate<ItemStack> stack) {
        return stack.test(this.getMainHandItem()) || stack.test(this.getOffhandItem());
    }

    private void swapWithInventory(String animation) {
        if (this.level().isClientSide)
            return;
        if (animation == null)
            this.inventory.swapItems(null);
        else if (animation.equals(BOW)) {
            if (!this.checkHands(s -> LancelotAttackAI.get(LancelotAttackAI.BOW).matches(s))) {
                this.inventory.swapItems(LancelotAttackAI.BOW);
            }
        } else if (animation.equals(CROSSBOW)) {
            if (!this.checkHands(s -> LancelotAttackAI.get(LancelotAttackAI.CROSSBOW).matches(s))) {
                this.inventory.swapItems(LancelotAttackAI.CROSSBOW);
            }
        } else if (animation.equals(TRIDENT)) {
            if (!this.checkHands(s -> LancelotAttackAI.get(LancelotAttackAI.TRIDENT).matches(s))) {
                this.inventory.swapItems(LancelotAttackAI.TRIDENT);
            }
        } else if (animation.equals(STAB_1)) {
            if (!this.checkHands(s -> s.is(FateTags.Items.SPEARS)) &&
                    !this.inventory.swapItems(LancelotInventory.SPEAR)) {
                this.inventory.swapItems(LancelotAttackAI.TRIDENT);
            }
        }
    }

    protected InteractionHand toUseHand() {
        if (this.getAnimationHandler().isCurrent(CROSSBOW))
            return this.getMainHandItem().getItem() instanceof CrossbowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if (this.getAnimationHandler().isCurrent(TRIDENT))
            return this.getMainHandItem().getItem() instanceof TridentItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if (this.getAnimationHandler().isCurrent(BOW))
            return this.getMainHandItem().getItem() instanceof BowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if (this.getAnimationHandler().isCurrent(STAB_1))
            return this.getMainHandItem().is(FateTags.Items.SPEARS) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        return !this.getMainHandItem().isEmpty() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    @Override
    public ItemStack getProjectile(ItemStack weaponStack) {
        if (weaponStack.getItem() instanceof ProjectileWeaponItem weapon) {
            Predicate<ItemStack> predicate = weapon.getSupportedHeldProjectiles();
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            if (itemstack.isEmpty()) {
                ItemStack arrow = this.inventory.get(LancelotInventory.ARROWS);
                if (predicate.test(arrow))
                    return arrow;
                ItemStack firework = this.inventory.get(LancelotInventory.FIREWORKS);
                if (predicate.test(firework))
                    return firework;
            }
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        }
        return super.getProjectile(weaponStack);
    }

    private void reflectProjectile(Entity oldProjectile) {
        CompoundTag old = new CompoundTag();
        oldProjectile.saveWithoutId(old);
        old.remove("UUID");
        if (old.contains("Owner"))
            old.putUUID("Owner", this.getUUID());
        Entity e = oldProjectile.getType().create(this.level());
        if (e instanceof Projectile) {
            e.load(old);
            float velocity = (float) (e.getDeltaMovement().length() * 0.7);
            if (this.getTarget() != null) {
                LivingEntity target = this.getTarget();
                Vec3 dir = new Vec3(target.getX() - e.getX(), (target.getY() + target.getEyeHeight()) - e.getY(), target.getZ() - e.getZ());
                this.shootProj(e, dir.x, dir.y, dir.z, velocity, 1);
            } else {
                this.shootProj(e, -e.getDeltaMovement().x, -e.getDeltaMovement().y, -e.getDeltaMovement().z, velocity, 1);
            }
            this.level().addFreshEntity(e);
        }
    }

    private void shootProj(Entity e, double dirX, double dirY, double dirZ, float vel, float acc) {
        Vec3 dir = new Vec3(dirX, dirY, dirZ).normalize().add(this.random.nextGaussian() * 0.0075F * acc, this.random.nextGaussian() * 0.0075F * acc, this.random.nextGaussian() * 0.0075F * acc).scale(vel);
        e.setDeltaMovement(dir);
        float[] xYRot = MathsHelper.YXRotFrom(dir);
        float targetYRot = xYRot[0];
        float targetXRot = xYRot[1];
        e.setYRot(targetYRot);
        e.setXRot(targetXRot);
        e.yRotO = e.getYRot();
        e.xRotO = e.getXRot();
    }

    @Override
    public String[] specialCommands() {
        return new String[]{FateEntities.LANCELOT.getID() + ".drop"};
    }

    @Override
    public void doSpecialCommand(ServerPlayer sender, String id) {
        if (id.equals(FateEntities.LANCELOT.getID() + ".drop")) {
            this.swapWithInventory(null);
            for (ItemStack stack : this.inventory) {
                this.spawnAtLocation(stack);
            }
            this.inventory.clearContent();
            this.pickupDelay = 100;
        }
    }

    @Override
    public boolean flipAnimation() {
        return this.toUseHand() == InteractionHand.OFF_HAND;
    }

    @Override
    protected String getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }
}