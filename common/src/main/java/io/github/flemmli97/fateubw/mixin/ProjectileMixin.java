package io.github.flemmli97.fateubw.mixin;

import io.github.flemmli97.fateubw.mixinhelper.ProjectileExtension;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Projectile.class)
public class ProjectileMixin implements ProjectileExtension {

    @Unique
    private Entity fateubw$targetedBy;

    @Override
    public Entity fateubw$targetedBy() {
        return this.fateubw$targetedBy;
    }

    @Override
    public void fateubw$setTargetedBy(Entity entity) {
        this.fateubw$targetedBy = entity;
    }
}
