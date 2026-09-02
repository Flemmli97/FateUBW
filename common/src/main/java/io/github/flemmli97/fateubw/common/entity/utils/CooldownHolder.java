package io.github.flemmli97.fateubw.common.entity.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Optional;
import java.util.function.BooleanSupplier;

public class CooldownHolder {

    private final Entity entity;
    private final NumberProvider provider;
    private final BooleanSupplier requirement;

    private int cooldown;

    public CooldownHolder(Entity entity, NumberProvider provider, BooleanSupplier requirement) {
        this.entity = entity;
        this.provider = provider;
        this.requirement = requirement;
    }

    public boolean canUse() {
        return this.cooldown <= 0;
    }

    public void tick() {
        if (this.requirement != null && !this.requirement.getAsBoolean())
            return;
        --this.cooldown;
    }

    public void use() {
        this.cooldown = this.provider.getInt(this.createContext());
    }

    private LootContext createContext() {
        return new LootContext.Builder(new LootParams.Builder((ServerLevel) this.entity.level())
                .withParameter(LootContextParams.ORIGIN, this.entity.position())
                .withParameter(LootContextParams.THIS_ENTITY, this.entity)
                .create(LootContextParamSets.COMMAND))
                .create(Optional.empty());
    }
}
