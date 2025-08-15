package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

import java.util.function.Predicate;

public class EntityLancelot extends BaseServant {

    public static final String CORRUPTED_ITEM = Fate.MODID + ":Corrupted";

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

//    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityLancelot>>> ATTACKS = List.of(
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.TWO_HAND_1)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .chain(GoalAttackAction.<EntityLancelot>chainBuilder(EntityLancelot.TWO_HAND_2, 2, 0.24f, 1)
//                            .or(EntityLancelot.TWO_HAND_3, 2, 0.24f, 1).withChance(0.5f))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 11),
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.TWO_HAND_2)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .chain(GoalAttackAction.<EntityLancelot>chainBuilder(EntityLancelot.TWO_HAND_1, 2, 0.24f, 1)
//                            .chain(EntityLancelot.ONE_HAND_1).withChance(0.3f))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 11),
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.TWO_HAND_4)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .chain(GoalAttackAction.<EntityLancelot>chainBuilder(EntityLancelot.TWO_HAND_2)
//                            .or(EntityLancelot.TWO_HAND_3, 2, 0.24f, 1).withChance(0.5f))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 11),
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.ONE_HAND_1)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 10)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 11),
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.STAB_1)
//                    .cooldown(e -> e.getRandom().nextInt(20) + 8)
//                    .withCondition((goal, target, previous) -> goal.attacker.canUseAttack(EntityLancelot.STAB_1))
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(2, 4))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.JUMP)
//                    .cooldown(e -> e.getRandom().nextInt(25) + 15)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 7))), 4),
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.JUMP)
//                    .cooldown(e -> e.getRandom().nextInt(25) + 15)
//                    .withCondition(((goal, target, previous) -> goal.distanceToTargetSq > 25))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 7))), 13),
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.BOW)
//                    .cooldown(e -> e.getRandom().nextInt(25) + 10)
//                    .withCondition((goal, target, previous) -> goal.attacker.canUseAttack(EntityLancelot.BOW))
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 14, 1.1))), 9),
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.TRIDENT)
//                    .cooldown(e -> e.getRandom().nextInt(25) + 10)
//                    .withCondition((goal, target, previous) -> goal.attacker.canUseAttack(EntityLancelot.TRIDENT))
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 14, 1.1))), 9),
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.CROSSBOW)
//                    .cooldown(e -> e.getRandom().nextInt(25) + 10)
//                    .withCondition((goal, target, previous) -> goal.attacker.canUseAttack(EntityLancelot.CROSSBOW))
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 14, 1.1))), 9)
//    );
//    public static final List<WeightedEntry.Wrapper<IdleAction<EntityLancelot>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 6),
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 2)
//    );
//
//    public final AnimatedAttackGoal<EntityLancelot> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityLancelot> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (anim != null) {
                    this.inventorySlotForAttack = this.inventorySlotFor(anim);
                    this.swapWithInventory(false);
                } else {
                    this.swapWithInventory(true);
                }
                return false;
            });

    private final Vector4f summonColor = new Vector4f(28 / 255f, 28 / 255f, 33 / 255f, 0.7f);

    private final SimpleContainer inventory = new SimpleContainer(5);
    private final SimpleContainer swapped = new SimpleContainer(1);
    private int inventorySlotForAttack = -1;
    private int pickupDelay;

    public EntityLancelot(EntityType<? extends EntityLancelot> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
    }

    @Override
    public AnimationHandler<EntityLancelot> getAnimationHandler() {
        return this.animationHandler;
    }

    public boolean canUseAttack(AnimationDefinition anim) {
        return this.inventorySlotFor(anim) != -1;
    }

    private int searchInv(Predicate<ItemStack> predicate) {
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            if (predicate.test(this.inventory.getItem(i)))
                return i;
        }
        return -1;
    }

    private int inventorySlotFor(AnimationDefinition anim) {
        Predicate<ItemStack> pred = stack -> !stack.isEmpty();
        if (anim.is(EntityLancelot.TRIDENT)) {
            pred = stack -> stack.getItem() instanceof TridentItem;
        } else if (anim.is(EntityLancelot.BOW)) {
            pred = stack -> stack.getItem() instanceof BowItem;
        }
        if (anim.is(EntityLancelot.CROSSBOW)) {
            pred = stack -> stack.getItem() instanceof CrossbowItem;
        }
        if (anim.is(EntityLancelot.STAB_1)) {
            pred = stack -> stack.getItem() instanceof TridentItem || stack.is(FateTags.Items.SPEARS)
                    || stack.getItem().getDescriptionId().contains("spear");
        }
        if (pred.test(this.getMainHandItem()))
            return 0;
        if (pred.test(this.getOffhandItem()))
            return 1;
        int slot = this.searchInv(pred);
        return slot != -1 ? slot + 2 : -1;
    }

    private void swapWithInventory(boolean back) {
        if (this.inventorySlotForAttack > 1) {
            int slot = this.inventorySlotForAttack - 2;
            if (back) {
                this.setItemInHand(InteractionHand.OFF_HAND, this.swapped.getItem(0));
                this.swapped.setItem(0, ItemStack.EMPTY);
            } else if (!this.inventory.getItem(slot).isEmpty()) {
                ItemStack current = this.getItemInHand(InteractionHand.OFF_HAND);
                this.swapped.setItem(0, current);
                this.setItemInHand(InteractionHand.OFF_HAND, this.inventory.getItem(slot).copy());
            }
        }
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
    public boolean hurt(DamageSource damageSource, float damage) {
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
    public void tick() {
        super.tick();
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
    public boolean canPickUpLoot() {
        return true;
    }

    @Override
    public ItemStack equipItemIfPossible(ItemStack stack) {
        if (this.pickupDelay > 0)
            return ItemStack.EMPTY;
        Equipable equipable = Equipable.get(stack);
        EquipmentSlot equipmentSlot = equipable != null ? equipable.getEquipmentSlot() : EquipmentSlot.MAINHAND;
        boolean special = this.specialWeapons(stack);
        if (special) {
            equipmentSlot = EquipmentSlot.OFFHAND;
        }
        ItemStack current = this.getItemBySlot(equipmentSlot);
        if (current.getItem() == FateItems.ARONDIGHT.get() && equipmentSlot == EquipmentSlot.MAINHAND)
            return ItemStack.EMPTY;
        boolean bl = !special;
        int slot = -1;
        if (!bl) {
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                InventorySearchResult res = this.canReplaceInventoryItem(stack, this.inventory.getItem(i));
                bl = res == InventorySearchResult.REPLACABLE;
                if (res == InventorySearchResult.ABORT)
                    break;
                if (bl) {
                    slot = i;
                    current = this.inventory.getItem(i);
                    break;
                }
            }
        }
        if (slot == -1) {
            bl = current.isEmpty() || this.canReplaceCurrentItem(stack, current);
        }
        if (bl && this.canHoldItem(stack)) {
            double d = slot == -1 ? this.getEquipmentDropChance(equipmentSlot) : 1;
            if (!current.isEmpty() && Math.max(this.random.nextFloat() - 0.1f, 0.0f) < d) {
                this.spawnAtLocation(current);
            }
            if (slot == -1)
                this.setItemSlotAndDropWhenKilled(equipmentSlot, stack);
            else {
                this.inventory.setItem(slot, stack);
            }
//            this.equipso(stack);
            this.revealServant();
            return stack;
        }
        return ItemStack.EMPTY;
    }

    protected InventorySearchResult canReplaceInventoryItem(ItemStack candidate, ItemStack existing) {
        if (!this.specialWeapons(candidate)) {
            return InventorySearchResult.ABORT;
        }
        if (candidate.getItem() instanceof BowItem && existing.getItem() instanceof BowItem) {
            return this.canReplaceCurrentItem(candidate, existing) ? InventorySearchResult.REPLACABLE : InventorySearchResult.ABORT;
        }
        if (candidate.getItem() instanceof TridentItem && existing.getItem() instanceof TridentItem) {
            return this.canReplaceCurrentItem(candidate, existing) ? InventorySearchResult.REPLACABLE : InventorySearchResult.ABORT;
        }
        if (candidate.getItem() instanceof CrossbowItem && existing.getItem() instanceof CrossbowItem) {
            return this.canReplaceCurrentItem(candidate, existing) ? InventorySearchResult.REPLACABLE : InventorySearchResult.ABORT;
        }
        if (candidate.getItem() instanceof ArrowItem && existing.getItem() instanceof ArrowItem) {
            return candidate.getCount() > existing.getCount() ? InventorySearchResult.REPLACABLE : InventorySearchResult.ABORT;
        }
        if (candidate.getItem() instanceof FireworkRocketItem && existing.getItem() instanceof FireworkRocketItem) {
            return candidate.getCount() > existing.getCount() ? InventorySearchResult.REPLACABLE : InventorySearchResult.ABORT;
        }
        return existing.isEmpty() ? InventorySearchResult.REPLACABLE : InventorySearchResult.CONTINUE;
    }

    protected boolean specialWeapons(ItemStack stack) {
        return false;
//        return LancelotAttackAI.getFor(stack) != null
//                || stack.getItem() instanceof SpearItem
//                || stack.getItem().getDescriptionId().contains("spear")
//                || stack.getItem() instanceof ArrowItem || stack.getItem() instanceof FireworkRocketItem;
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack candidate, ItemStack existing) {
        boolean better = ItemUtils.isItemBetter(this, null, candidate, existing);
        if (!better) {
            // No way to actually tell if item is better so we check for simply enchantments
            if (candidate.getItem() instanceof TridentItem && existing.getItem() instanceof TridentItem) {
                better = candidate.isEnchanted() && !existing.isEnchanted();
            }
            if (candidate.getItem() instanceof CrossbowItem && existing.getItem() instanceof CrossbowItem) {
                better = candidate.isEnchanted() && !existing.isEnchanted();
            }
        }
        return better;
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(JUMP)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt("jump")) {
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.position().add(this.getLookAngle());
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(0.16);
                if (dir.lengthSqr() > 4.5 * 4.5) {
                    dir.normalize().scale(4.5);
                }
                this.setDeltaMovement(dir.add(0, 0.9, 0));
            }
            if (anim.isPast("jump")) {
                this.fallDistance = 0;
                if (anim.done(0)) {
                    if (this.onGround()) {
                        this.getAnimationHandler().setAnimation(JUMP_LAND);
                    }
                }
                // Stuck check. Or e.g. if in water
                if (anim.isPast(6.0) && (!this.getInBlockState().is(Blocks.AIR) || !this.getBlockStateOn().is(Blocks.AIR))) {
                    this.getAnimationHandler().setAnimation(JUMP_LAND);
                }
            }
        } else if (anim.is(CROSSBOW, BOW, TRIDENT)) {
            LivingEntity target = this.getTarget();
            if (anim.isAt(0.2))
                this.startUsingItem(this.toUseHand());
            if (anim.isAt("attack")) {
                if (target != null && this.getSensing().hasLineOfSight(target)) {
//                    LancelotAttackAI.ItemAI ai = LancelotAttackAI.getFor(this.getUseItem());
//                    if (ai != null) {
//                        this.useItemRemaining = 1;
//                        this.lookAtNow(target, 360, 90);
//                        this.releaseUsingItem();
//                        boolean used = ai.attack(this, target, this.getUsedItemHand());
//                        if (used) {
//                            this.setItemInHand(this.getUsedItemHand(), ItemStack.EMPTY);
//                        }
//                    }
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
    public AABB attackBB(AnimationState anim) {
        if (anim.is(JUMP_LAND)) {
            double width = this.getBbWidth() + 2;
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
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        if (stack.getItem() != FateItems.ARONDIGHT.get())
            stack.set(FateDataComponents.CORRUPTED_ITEM.get(), Unit.INSTANCE);
        super.setItemSlot(slot, stack);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            this.spawnAtLocation(this.inventory.getItem(i));
        }
        this.inventory.clearContent();
    }

    @Override
    public ItemEntity spawnAtLocation(ItemStack stack) {
        stack.remove(FateDataComponents.CORRUPTED_ITEM.get());
        return super.spawnAtLocation(stack);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Inventory", this.inventory.createTag(this.registryAccess()));
        tag.put("Swapped", this.swapped.createTag(this.registryAccess()));
        tag.putInt("SelectedSlot", this.inventorySlotForAttack);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.inventory.fromTag(tag.getList("Inventory", Tag.TAG_COMPOUND), this.registryAccess());
        this.swapped.fromTag(tag.getList("Swapped", Tag.TAG_COMPOUND), this.registryAccess());
        this.inventorySlotForAttack = tag.getInt("SelectedSlot");
    }

    @Override
    public String[] specialCommands() {
        return new String[]{FateEntities.LANCELOT.getID() + ".drop"};
    }

    @Override
    public void doSpecialCommand(String s) {
        if (s.equals(FateEntities.LANCELOT.getID() + ".drop")) {
            this.swapWithInventory(true);
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                this.spawnAtLocation(this.inventory.getItem(i));
            }
            this.inventory.clearContent();
            this.pickupDelay = 100;
        }
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

    protected InteractionHand toUseHand() {
        if (this.getAnimationHandler().isCurrent(CROSSBOW))
            return this.getMainHandItem().getItem() instanceof CrossbowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if (this.getAnimationHandler().isCurrent(TRIDENT))
            return this.getMainHandItem().getItem() instanceof TridentItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if (this.getAnimationHandler().isCurrent(BOW))
            return this.getMainHandItem().getItem() instanceof BowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        return !this.getMainHandItem().isEmpty() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    @Override
    public boolean flipAnimation() {
        return this.toUseHand() == InteractionHand.OFF_HAND;
//                || (this.inventorySlotFor(STAB_1) == 1);
    }

    @Override
    protected String getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }

    @Override
    public ItemStack getProjectile(ItemStack weaponStack) {
        if (weaponStack.getItem() instanceof ProjectileWeaponItem weapon) {
            Predicate<ItemStack> predicate = weapon.getSupportedHeldProjectiles();
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            if (itemstack.isEmpty()) {
                int slot = this.searchInv(predicate);
                if (slot != -1) {
                    itemstack = this.inventory.getItem(slot);
                }
            }
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        }
        return super.getProjectile(weaponStack);
    }

    protected enum InventorySearchResult {
        REPLACABLE,
        CONTINUE,
        ABORT
    }
}