package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.servant.EntityCuchulainn;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.fateubw.common.utils.Utils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class GaeBolg extends BaseProjectile {

    public GaeBolg(EntityType<? extends GaeBolg> type, Level level) {
        super(type, level);
    }

    public GaeBolg(Level level, LivingEntity shooter) {
        super(FateEntities.GAEBOLG.get(), level, shooter);
    }

    @Override
    public int livingTickMax() {
        return 100;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult res) {
        res.getEntity().hurt(CustomDamageSource.gaeBolg(this, this.getOwner()), Utils.magicDamage(this.getOwner()) + CommonConfig.gaeBolgDmg);
        if (res.getEntity() instanceof LivingEntity && (!(res.getEntity() instanceof Player) || !((Player) res.getEntity()).getAbilities().invulnerable)) {
            for (MobEffectInstance effect : CommonConfig.gaeBolgEffect.potions()) {
                //The no jump effect from jump boost doesnt seem to affect entities
                if (effect.getEffect() != MobEffects.JUMP || res.getEntity() instanceof Player)
                    ((LivingEntity) res.getEntity()).addEffect(effect);
            }
        }
        this.discard();
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.discard();
    }

    @Override
    public void remove(RemovalReason removalReason) {
        if (!this.level.isClientSide) {
            if (this.getOwner() instanceof Player player) {
                if (!player.isCreative()) {
                    ItemEntity gaeBolg = new ItemEntity(this.level, this.getOwner().getX(), this.getOwner().getY(), this.getOwner().getZ(), new ItemStack(FateItems.GAEBOLG.get()));
                    gaeBolg.setPickUpDelay(0);
                    player.level.addFreshEntity(gaeBolg);
                    player.playSound(SoundEvents.ITEM_PICKUP, 0.8f, 1);
                }
            } else if (this.getOwner() instanceof EntityCuchulainn) {
                ((EntityCuchulainn) this.getOwner()).retrieveGaeBolg();
            }
        }
        super.remove(removalReason);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.01F;
    }
}