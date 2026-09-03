package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class SpearProjectile extends BaseProjectile {

    public SpearProjectile(EntityType<? extends SpearProjectile> type, Level level) {
        super(type, level);
    }

    public SpearProjectile(Level level, LivingEntity shooter) {
        super(FateEntities.SPEAR.get(), level, shooter);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.15f;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        DamageSource source = FateDamageTypes.indirect(FateDamageTypes.SPEAR, this, this.getOwner());
        AttributeInstance inst;
        float dmg = this.getOwner() instanceof LivingEntity living && (inst = living.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)) != null ?
                (float) inst.getValue() : 7;
        boolean res = result.getEntity().hurt(source, dmg);
        this.discard();
        return res;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.discard();
    }
}
