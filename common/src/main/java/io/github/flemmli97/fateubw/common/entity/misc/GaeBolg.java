package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.servant.EntityCuchulainn;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
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

public class GaeBolg extends EntityProjectile {

    public GaeBolg(EntityType<? extends EntityProjectile> type, Level world) {
        super(type, world);
    }

    public GaeBolg(Level world, LivingEntity shooter) {
        super(ModEntities.gaebolg.get(), world, shooter);
    }

    @Override
    public int livingTickMax() {
        return 100;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult res) {
        res.getEntity().hurt(CustomDamageSource.gaeBolg(this, this.getOwner()), Config.Common.gaeBolgDmg);
        if (res.getEntity() instanceof LivingEntity && (!(res.getEntity() instanceof Player) || !((Player) res.getEntity()).getAbilities().invulnerable)) {
            for (MobEffectInstance effect : Config.Common.gaeBolgEffect.potions()) {
                //The no jump effect from jump boost doesnt seem to affect entities
                if (effect.getEffect() != MobEffects.JUMP || res.getEntity() instanceof Player)
                    ((LivingEntity) res.getEntity()).addEffect(effect);
            }
        }
        this.kill();
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.kill();
    }

    @Override
    public void remove(RemovalReason removalReason) {
        if (!this.level.isClientSide) {
            if (this.getOwner() instanceof Player) {
                Player player = (Player) this.getOwner();
                if (!player.isCreative()) {
                    ItemEntity gaeBolg = new ItemEntity(this.level, this.getOwner().getX(), this.getOwner().getY(), this.getOwner().getZ(), new ItemStack(ModItems.gaebolg.get()));
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