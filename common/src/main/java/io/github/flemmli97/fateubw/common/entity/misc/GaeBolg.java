package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.servant.Cuchulainn;
import io.github.flemmli97.fateubw.common.entity.utils.EntityTrailHandler;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.Utils;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class GaeBolg extends BaseProjectile {

    private Entity target;
    private final EntityTrailHandler trailHandler = new EntityTrailHandler(this, 8);

    public GaeBolg(EntityType<? extends GaeBolg> type, Level level) {
        super(type, level);
    }

    public GaeBolg(Level level, LivingEntity shooter) {
        super(FateEntities.GAEBOLG.get(), level, shooter);
    }

    @Override
    public int livingTickMax() {
        return 80;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    @Override
    public void tick() {
        if (this.firstTick) {
            this.trailHandler.tick();
        }
        super.tick();
        if (!this.level().isClientSide) {
            if (this.target != null) {
                Vec3 dist = this.target.position().add(0, this.target.getBbHeight() * 0.5, 0).subtract(this.position());
                int changeFreq = dist.lengthSqr() > 36 ? 6 : dist.lengthSqr() > 9 ? 4 : 2;
                if (this.tickCount % changeFreq == 0) {
                    this.homeTarget(dist);
                }
            }
        }
        this.trailHandler.tick();
    }

    protected void homeTarget(Vec3 dist) {
        Vec3 delta = this.getDeltaMovement();
        Vec3 motion = dist.normalize().scale(delta.length());
        this.setDeltaMovement(motion);
        double f = Math.sqrt(horizontalMag(motion));
        this.setYRot((float) (Mth.atan2(motion.x, motion.z) * (double) (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(motion.y, f) * (double) (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    protected float motionReduction(boolean inWater) {
        return 1;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult res) {
        if (this.target != null && res.getEntity() != this.target)
            return false;
        float health = res.getEntity() instanceof LivingEntity living ? living.getHealth() : 0;
        res.getEntity().hurt(FateDamageTypes.indirect(FateDamageTypes.GAE_BOLG, this, this.getOwner()),
                Utils.magicDamage(this.getOwner()) + CommonConfig.gaeBolgDmg + health * 0.1f);
        if (res.getEntity() instanceof LivingEntity living && (!(res.getEntity() instanceof Player player) || !player.getAbilities().invulnerable)) {
            for (MobEffectInstance effect : CommonConfig.gaeBolgEffect.potions()) {
                living.addEffect(effect);
            }
        }
        this.discard();
        return true;
    }

    @Override
    public void moveEntity() {
        if (this.level().isClientSide) {
            // Do client side detection too for block bounce
            this.doCollision();
        }
        super.moveEntity();
    }

    @Override
    protected void onBlockHit(BlockHitResult hit) {
        if (this.tickCount < 40) {
            Vec3 newMot;
            Vec3 mot = this.getDeltaMovement();
            Direction dir = hit.getDirection();
            switch (dir) {
                case DOWN, UP -> newMot = new Vec3(mot.x(), -mot.y(), mot.z());
                case WEST, EAST -> newMot = new Vec3(-mot.x(), mot.y(), mot.z());
                default -> newMot = new Vec3(mot.x(), mot.y(), -mot.z());
            }
            this.setPos(hit.getLocation().add(dir.getStepX() * 0.5, dir.getStepY() * 0.5, dir.getStepZ() * 0.5));
            this.setDeltaMovement(newMot);
            return;
        }
        if (!this.level().isClientSide)
            this.discard();
    }

    @Override
    public void remove(RemovalReason removalReason) {
        if (!this.level().isClientSide) {
            if (this.getOwner() instanceof Player player) {
                if (!player.isCreative()) {
                    ItemEntity gaeBolg = new ItemEntity(this.level(), this.getOwner().getX(), this.getOwner().getY(), this.getOwner().getZ(), new ItemStack(FateItems.GAEBOLG.get()));
                    gaeBolg.setPickUpDelay(0);
                    player.level().addFreshEntity(gaeBolg);
                    player.playSound(SoundEvents.ITEM_PICKUP, 0.8f, 1);
                }
            } else if (this.getOwner() instanceof Cuchulainn) {
                ((Cuchulainn) this.getOwner()).retrieveGaeBolg();
            }
        }
        super.remove(removalReason);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.01F;
    }

    public TrailPositions trailPositions() {
        return this.trailHandler.getPositions();
    }
}