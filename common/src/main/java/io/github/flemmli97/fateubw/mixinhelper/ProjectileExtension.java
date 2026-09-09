package io.github.flemmli97.fateubw.mixinhelper;

import net.minecraft.world.entity.Entity;

public interface ProjectileExtension {

    Entity fateubw$targetedBy();

    void fateubw$setTargetedBy(Entity entity);
}
