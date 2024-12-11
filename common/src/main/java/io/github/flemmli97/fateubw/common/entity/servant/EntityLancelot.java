package io.github.flemmli97.fateubw.common.entity.servant;


import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.servant.ai.LancelotAttackAI;
import io.github.flemmli97.fateubw.common.items.weapons.ClassSpear;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class EntityLancelot extends BaseServant {

    public static final String CORRUPTED_ITEM = Fate.MODID + ":Corrupted";

    public static final AnimatedAction MELEE_1 = new AnimatedAction(0.64, 0.52, "long_sword_1");
    public static final AnimatedAction MELEE_1_VAR_1 = new AnimatedAction(0.64, 0.52, "long_sword_1_2");
    public static final AnimatedAction MELEE_1_VAR_2 = new AnimatedAction(0.6, 0.24, "long_sword_1_3");
    public static final AnimatedAction MELEE_2 = new AnimatedAction(0.56, 0.4, "horizontal_slash");
    public static final AnimatedAction JUMP = AnimatedAction.builder((int) Math.ceil(0.32 * 20), "jump").infinite().build();
    public static final AnimatedAction JUMP_LAND = new AnimatedAction(0.72, 0.24, "jump_land");
    public static final AnimatedAction TRIDENT = new AnimatedAction(0.88, 0.52, "trident");
    public static final AnimatedAction BOW = new AnimatedAction(1, 0.76, "bow");
    public static final AnimatedAction CROSSBOW = new AnimatedAction(1.64, 1.2, "crossbow");
    public static final AnimatedAction STAB = new AnimatedAction(0.68, 0.32, "stab");
    public static final AnimatedAction GUN_SMALL = new AnimatedAction(0.8, 0.48, "gun_small");
    public static final AnimatedAction GUN_BIG = new AnimatedAction(1.08, 0.4, "gun_big");

    public static final AnimatedAction SUMMON = new AnimatedAction(2., 0, "summon");
    public static final AnimatedAction[] ANIMS = {MELEE_1, MELEE_1_VAR_1, MELEE_1_VAR_2, MELEE_2, JUMP, JUMP_LAND, TRIDENT, BOW, STAB, CROSSBOW, GUN_SMALL, GUN_BIG, SUMMON};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityLancelot>>> ATTACKS = List.of(
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.MELEE_1)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
//                    .chain(GoalAttackAction.<EntityLancelot>chainBuilder(EntityLancelot.MELEE_1_VAR_1)
//                            .chain(EntityLancelot.MELEE_1_VAR_2).withPredicate(e -> e.getRandom().nextFloat() < 0.5))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 11),
//            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.MELEE_2)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetAttackRunner<>(1))), 11),
            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.JUMP)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    //.withCondition(((goal, target, previous) -> goal.distanceToTargetSq > 25))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 7))), 13),
            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.BOW)
                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
                    .withCondition((goal, target, previous) -> goal.attacker.canUseAttack(EntityLancelot.BOW))
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 14, 1.1))), 9),
            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.TRIDENT)
                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
                    .withCondition((goal, target, previous) -> goal.attacker.canUseAttack(EntityLancelot.TRIDENT))
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 14, 1.1))), 9),
            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.CROSSBOW)
                    .cooldown(e -> e.getRandom().nextInt(15) + 10)
                    .withCondition((goal, target, previous) -> goal.attacker.canUseAttack(EntityLancelot.CROSSBOW))
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 14, 1.1))), 9),
            WeightedEntry.wrap(new GoalAttackAction<EntityLancelot>(EntityLancelot.STAB)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .withCondition((goal, target, previous) -> goal.attacker.canUseAttack(EntityLancelot.STAB))
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(2, 4))), 10)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityLancelot>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 6),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 2)
    );

    public final AnimatedAttackGoal<EntityLancelot> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityLancelot> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeCons(anim -> {
                if (anim != null) {
                    this.inventorySlotForAttack = this.inventorySlotFor(anim);
                    this.swapWithInventory(false);
                } else {
                    this.swapWithInventory(true);
                }
            });

    private final Vector4f summonColor = new Vector4f(28 / 255f, 28 / 255f, 33 / 255f, 0.7f);

    private final SimpleContainer inventory = new SimpleContainer(5);
    private final SimpleContainer swapped = new SimpleContainer(1);
    private int inventorySlotForAttack = -1;
    private int pickupDelay;

    public EntityLancelot(EntityType<? extends EntityLancelot> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
    }

    @Override
    public AnimationHandler<EntityLancelot> getAnimationHandler() {
        return this.animationHandler;
    }

    public boolean canUseAttack(AnimatedAction anim) {
        return this.inventorySlotFor(anim) != -1;
    }

    private int searchInv(Predicate<ItemStack> predicate) {
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            if (predicate.test(this.inventory.getItem(i)))
                return i;
        }
        return -1;
    }

    private int inventorySlotFor(AnimatedAction anim) {
        Predicate<ItemStack> pred = stack -> !stack.isEmpty();
        if (anim.is(EntityLancelot.TRIDENT)) {
            pred = stack -> stack.getItem() instanceof TridentItem;
        } else if (anim.is(EntityLancelot.BOW)) {
            pred = stack -> stack.getItem() instanceof BowItem;
        }
        if (anim.is(EntityLancelot.CROSSBOW)) {
            pred = stack -> stack.getItem() instanceof CrossbowItem;
        }
        if (anim.is(EntityLancelot.STAB)) {
            pred = stack -> stack.getItem() instanceof TridentItem || stack.getItem() instanceof ClassSpear
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
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attack);
        else
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ARONDIGHT.get()));
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (this.isPassenger())
            return this.getVehicle().hurt(damageSource, damage);

        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return this.preAttackEntityFrom(damageSource, damage);
        } else {
            if (!(damageSource.getEntity() instanceof BaseServant))
                damage *= 0.5;

            if (damageSource.isProjectile() && !damageSource.isBypassArmor()) {
                boolean blocked = false;
                if (!this.level.isClientSide) {
                    if (this.getRandom().nextFloat() < Config.Common.lancelotReflectChance && damageSource.getDirectEntity() != null
                            && !(damageSource.getDirectEntity() instanceof LivingEntity)) {
                        this.reflectProjectile(damageSource.getDirectEntity());
                        blocked = true;
                        this.level.playSound(null, this.blockPosition(), SoundEvents.ANVIL_PLACE, SoundSource.NEUTRAL, 1, 1);
                    } else if (this.projectileBlockChance(damageSource, damage)) {
                        this.level.playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 1, 1);
                        blocked = true;
                    }
                    if (blocked && damageSource.getDirectEntity() != null)
                        damageSource.getDirectEntity().remove(RemovalReason.KILLED);
                }
                return !blocked;
            }
            return this.preAttackEntityFrom(damageSource, Math.min(50, damage));
        }
    }

    @Override
    public void tick() {
        super.tick();
        --this.pickupDelay;
        if (this.level.isClientSide) {
            for (int x = 0; x < 2; x++) {
                this.level.addParticle(
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
    public boolean equipItemIfPossible(ItemStack stack) {
        if (this.pickupDelay > 0)
            return false;
        EquipmentSlot equipmentSlot = Mob.getEquipmentSlotForItem(stack);
        boolean special = this.specialWeapons(stack);
        if (special) {
            equipmentSlot = EquipmentSlot.OFFHAND;
        }
        ItemStack current = this.getItemBySlot(equipmentSlot);
        if (current.getItem() == ModItems.ARONDIGHT.get() && equipmentSlot == EquipmentSlot.MAINHAND)
            return false;
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
            this.equipEventAndSound(stack);
            this.revealServant();
            return true;
        }
        return false;
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
        return LancelotAttackAI.getFor(stack) != null
                || stack.getItem() instanceof ClassSpear
                || stack.getItem().getDescriptionId().contains("spear")
                || stack.getItem() instanceof ArrowItem || stack.getItem() instanceof FireworkRocketItem;
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack candidate, ItemStack existing) {
        boolean better = ItemUtils.isItemBetter(candidate, existing);
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
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(JUMP)) {
            LivingEntity target = this.getTarget();
            if (anim.isAtTick(0.12)) {
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.position().add(this.getLookAngle());
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(0.16);
                if (dir.lengthSqr() > 4.5 * 4.5) {
                    dir.normalize().scale(4.5);
                }
                this.setDeltaMovement(dir.add(0, 0.9, 0));
            }
            if (anim.isPastTick(0.12)) {
                this.fallDistance = 0;
                if (anim.isPastTick(anim.getLength())) {
                    if (this.isOnGround()) {
                        this.getAnimationHandler().setAnimation(JUMP_LAND);
                    }
                }
                // Stuck check. Or e.g. if in water
                if (anim.isPastTick(6.0) && (!this.getFeetBlockState().is(Blocks.AIR) || !this.getBlockStateOn().is(Blocks.AIR))) {
                    this.getAnimationHandler().setAnimation(JUMP_LAND);
                }
            }
        } else if (anim.is(CROSSBOW, BOW, TRIDENT)) {
            LivingEntity target = this.getTarget();
            if (anim.isAtTick(0.2))
                this.startUsingItem(this.toUseHand());
            if (anim.canAttack()) {
                if (target != null && this.getSensing().hasLineOfSight(target)) {
                    LancelotAttackAI.ItemAI ai = LancelotAttackAI.getFor(this.getUseItem());
                    if (ai != null) {
                        this.useItemRemaining = 1;
                        this.lookAtNow(target, 360, 90);
                        this.releaseUsingItem();
                        boolean used = ai.attack(this, target, this.getUsedItemHand());
                        if (used) {
                            this.setItemInHand(this.getUsedItemHand(), ItemStack.EMPTY);
                        }
                    }
                }
                this.stopUsingItem();
            }
        } else {
            boolean step = anim.is(MELEE_1) && anim.isAtTick(0.28) ||
                    anim.is(MELEE_1_VAR_1) && anim.isAtTick(0.2) ||
                    anim.is(MELEE_1_VAR_2) && anim.isAtTick(0.08);
            if (step) {
                Vec3 dir = Utils.fromRelativeVector(this, new Vec3(0, 0, 1)).scale(anim.is(MELEE_1_VAR_2) ? 0.25 : 0.3);
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
            if (anim.canAttack() && anim.is(JUMP_LAND)) {
                S2CScreenShake.sendAround(this, 10, 8, 3);
                this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0f, 0.9f);
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        if (anim.is(JUMP_LAND)) {
            double width = this.getBbWidth() + 2;
            return new AABB(-width * 0.5, -0.02, -width * 0.3, width * 0.5, this.getBbHeight() * 0.5, width * 0.7);
        }
        double width = this.getBbWidth() + 0.4;
        double length = 1;
        if (anim.is(STAB)) {
            length += 1.3;
        }
        if (anim.is(MELEE_1, MELEE_1_VAR_1, MELEE_1_VAR_2)) {
            width += 1.25;
            length += 0.95;
        }
        if (anim.is(MELEE_2)) {
            width += 1.3;
            length += 0.7;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        if (stack.getItem() != ModItems.ARONDIGHT.get())
            stack.getOrCreateTag().putBoolean(CORRUPTED_ITEM, true);
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
        if (stack.hasTag())
            stack.getTag().remove(CORRUPTED_ITEM);
        return super.spawnAtLocation(stack);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Inventory", this.inventory.createTag());
        tag.put("Swapped", this.swapped.createTag());
        tag.putInt("SelectedSlot", this.inventorySlotForAttack);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.inventory.fromTag(tag.getList("Inventory", Tag.TAG_COMPOUND));
        this.swapped.fromTag(tag.getList("Swapped", Tag.TAG_COMPOUND));
        this.inventorySlotForAttack = tag.getInt("SelectedSlot");
    }

    @Override
    public String[] specialCommands() {
        return new String[]{ModEntities.LANCELOT.getID() + ".drop"};
    }

    @Override
    public void doSpecialCommand(String s) {
        if (s.equals(ModEntities.LANCELOT.getID() + ".drop")) {
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
        Entity e = oldProjectile.getType().create(this.level);
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
            this.level.addFreshEntity(e);
        }
    }

    private void shootProj(Entity e, double dirX, double dirY, double dirZ, float vel, float acc) {
        Vec3 dir = new Vec3(dirX, dirY, dirZ).normalize().add(this.random.nextGaussian() * 0.0075F * acc, this.random.nextGaussian() * 0.0075F * acc, this.random.nextGaussian() * 0.0075F * acc).scale(vel);
        e.setDeltaMovement(dir);
        float[] xYRot = MathsHelper.XYRotFrom(dir);
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
        return this.toUseHand() == InteractionHand.OFF_HAND
                || (this.inventorySlotFor(STAB) == 1);
    }

    @Override
    protected AnimatedAction getSummonAnimation() {
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