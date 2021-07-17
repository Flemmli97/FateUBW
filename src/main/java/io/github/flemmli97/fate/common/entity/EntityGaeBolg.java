package io.github.flemmli97.fate.common.entity;

import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.entity.servant.EntityCuchulainn;
import io.github.flemmli97.fate.common.registry.ModEntities;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.CustomDamageSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class EntityGaeBolg extends EntityProjectile {

    public EntityGaeBolg(EntityType<? extends EntityProjectile> type, World world) {
        super(type, world);
    }

    public EntityGaeBolg(World world, LivingEntity shooter) {
        super(ModEntities.gaebolg.get(), world, shooter);
    }

    @Override
    public int livingTickMax() {
        return 100;
    }

    @Override
    protected boolean entityRayTraceHit(EntityRayTraceResult res) {
        res.getEntity().attackEntityFrom(CustomDamageSource.gaeBolg(this, this.getOwner()), Config.Common.gaeBolgDmg);
        if (res.getEntity() instanceof LivingEntity && (!(res.getEntity() instanceof PlayerEntity) || !((PlayerEntity) res.getEntity()).abilities.disableDamage)) {
            for (EffectInstance effect : Config.Common.gaeBolgEffect.potions()) {
                //The no jump effect from jump boost doesnt seem to affect entities
                if (effect.getPotion() != Effects.JUMP_BOOST || res.getEntity() instanceof PlayerEntity)
                    ((LivingEntity) res.getEntity()).addPotionEffect(effect);
            }
        }
        this.remove();
        return true;
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult blockRayTraceResult) {
        this.remove();
    }

    @Override
    public void remove() {
        if (!this.world.isRemote) {
            if (this.getOwner() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) this.getOwner();
                if (!player.isCreative()) {
                    ItemEntity gaeBolg = new ItemEntity(this.world, this.getOwner().getPosX(), this.getOwner().getPosY(), this.getOwner().getPosZ(), new ItemStack(ModItems.gaebolg.get()));
                    gaeBolg.setPickupDelay(0);
                    player.world.addEntity(gaeBolg);
                    player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.8f, 1);
                }
            } else if (this.getOwner() instanceof EntityCuchulainn) {
                ((EntityCuchulainn) this.getOwner()).retrieveGaeBolg();
            }
        }
        super.remove();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.01F;
    }
}