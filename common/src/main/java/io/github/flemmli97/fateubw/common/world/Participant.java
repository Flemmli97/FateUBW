package io.github.flemmli97.fateubw.common.world;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class Participant {

    /**
     * UUID for this participant. If its a player will be the players uuid, otherwise the servants
     */
    private final UUID uuid;
    private final UUID linkedUuid;

    private ResourceKey<Level> levelCache;
    private WeakReference<BaseServant> servant;

    public Participant(BaseServant servant, @Nullable Player player) {
        this.uuid = player != null ? player.getUUID() : servant.getUUID();
        this.linkedUuid = servant.getUUID();
        this.servant = new WeakReference<>(servant);
    }

    public Participant(CompoundTag tag) {
        this.uuid = tag.getUUID("UUID");
        this.linkedUuid = tag.getUUID("Linked");
        if (tag.contains("CachedLevel")) {
            this.levelCache = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(tag.getString("CachedLevel")));
        }
    }

    public boolean isPlayerParticipant() {
        return !this.linkedUuid.equals(this.uuid);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public BaseServant getServant(MinecraftServer server) {
        BaseServant servant = this.servant == null ? null : this.servant.get();
        if (servant == null || !servant.isAlive()) {
            boolean player = this.isPlayerParticipant();
            if (this.levelCache != null) {
                ServerLevel level = server.getLevel(this.levelCache);
                if (level != null) {
                    servant = EntityUtil.findFromUUID(BaseServant.class, level, player ? this.uuid : this.linkedUuid);
                    if (servant != null)
                        this.servant = new WeakReference<>(servant);
                }
            } else {
                for (ServerLevel level : server.getAllLevels()) {
                    servant = EntityUtil.findFromUUID(BaseServant.class, level, player ? this.uuid : this.linkedUuid);
                    if (servant != null) {
                        this.servant = new WeakReference<>(servant);
                        break;
                    }
                }
            }
        }
        return servant;
    }

    private ResourceKey<Level> cachedLevel() {
        BaseServant servant = this.servant == null ? null : this.servant.get();
        return servant != null ? servant.level.dimension() : null;
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Participant participant))
            return false;
        return this.uuid.equals(participant.linkedUuid);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("UUID", this.uuid);
        tag.putUUID("Linked", this.linkedUuid);
        ResourceKey<Level> cache = this.cachedLevel();
        if (cache != null)
            tag.putString("CachedLevel", cache.location().toString());
        return tag;
    }
}
