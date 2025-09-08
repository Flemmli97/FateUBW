package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.utils.Utils;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class ArcherArrow extends AbstractArrow {

    public ArcherArrow(EntityType<? extends ArcherArrow> type, Level level) {
        super(type, level);
    }

    public ArcherArrow(Level level, LivingEntity shootingEntity, ItemStack weapon) {
        super(FateEntities.ARCHER_ARROW.get(), shootingEntity, level, Items.ARROW.getDefaultInstance(), weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    public ItemStack getPickupItemStackOrigin() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Utils.runWithInvulTimer(this.getOwner(), result.getEntity(), this::damageTarget, this.getOwner() instanceof Player ? 20 : 0);
    }

    protected boolean damageTarget(Entity target) {
        if (target == this.getOwner())
            return false;
        double speed = this.getDeltaMovement().length();
        double damage = this.getBaseDamage();
        Entity owner = this.getOwner();
        DamageSource damageSource = FateDamageTypes.indirect(FateDamageTypes.ARCHER_NORMAL, this, owner == null ? this : owner);
        if (this.getWeaponItem() != null && this.level() instanceof ServerLevel serverLevel) {
            damage = EnchantmentHelper.modifyDamage(serverLevel, this.getWeaponItem(), target, damageSource, (float) damage);
        }
        damage = Mth.clamp(speed * damage, 0.0F, Double.MAX_VALUE);
        if (this.isCritArrow()) {
            int crit = this.random.nextInt((Mth.ceil(damage) / 2 + 2));
            damage = Math.min(crit + damage, Double.MAX_VALUE);
            if (Double.isInfinite(damage))
                damage = Double.MAX_VALUE;
        }
        if (owner instanceof LivingEntity living) {
            living.setLastHurtMob(target);
        }
        int firePre = target.getRemainingFireTicks();
        if (target.hurt(damageSource, (float) damage)) {
            if (this.isOnFire()) {
                target.igniteForSeconds(5.0F);
            }
            if (target instanceof LivingEntity livingTarget) {
                if (!this.level().isClientSide) {
                    livingTarget.setArrowCount(livingTarget.getArrowCount() + 1);
                }
                if (this.level() instanceof ServerLevel serverLevel2) {
                    EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel2, livingTarget, damageSource, this.getWeaponItem());
                }
                this.doPostHurtEffects(livingTarget);
                if (livingTarget != owner && livingTarget instanceof Player && owner instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer) owner).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
            }
            this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.discard();
            return true;
        }
        target.setRemainingFireTicks(firePre);
        this.deflect(ProjectileDeflection.REVERSE, target, this.getOwner(), false);
        this.setDeltaMovement(this.getDeltaMovement().scale(0.2));
        if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7) {
            this.discard();
        }
        return false;
    }
}