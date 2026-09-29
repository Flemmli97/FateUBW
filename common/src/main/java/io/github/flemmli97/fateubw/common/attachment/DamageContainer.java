package io.github.flemmli97.fateubw.common.attachment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

public class DamageContainer {

    private final LivingEntity entity;
    private final Object2IntMap<UUID> lastDamages = new Object2IntOpenHashMap<>();

    public DamageContainer(LivingEntity entity) {
        this.entity = entity;
    }

    public void recordDamage(Entity entity) {
        this.lastDamages.put(entity.getUUID(), this.entity.tickCount);
    }

    public HurtState canHurtThis(Entity entity, int invulnerability, boolean shared) {
        if (this.entity.invulnerableTime <= 10) {
            return HurtState.VANILLA;
        }
        int last = this.lastDamages.getOrDefault(entity.getUUID(), 0);
        if (last == 0) {
            if (this.entity.invulnerableTime <= 10) {
                return HurtState.VANILLA;
            }
        }
        int invulnerableTime = shared ? (20 - this.entity.invulnerableTime) : (this.entity.tickCount - last);
        return invulnerableTime >= invulnerability ? HurtState.ALLOW : HurtState.DENY;
    }

    public void tick() {
        this.lastDamages.object2IntEntrySet().removeIf((entry) -> (this.entity.tickCount - entry.getIntValue()) >= 20);
    }

    public enum HurtState {
        DENY,
        ALLOW,
        VANILLA
    }
}
