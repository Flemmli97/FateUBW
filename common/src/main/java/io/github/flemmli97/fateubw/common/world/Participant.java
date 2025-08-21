package io.github.flemmli97.fateubw.common.world;

import io.github.flemmli97.fateubw.api.entity.ServantLike;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class Participant<T extends Mob & ServantLike<T>> {

    /**
     * UUID for this participant. If it's a player will be the players uuid, otherwise the servants
     */
    private final ParticipantId uuid;

    private ResourceKey<Level> levelCache;
    private WeakReference<T> servant;

    public Participant(ServantLike<T> servant, @Nullable Player player) {
        this(servant.get(), player != null ? player.getUUID() : null);
    }

    public Participant(ServantLike<T> servant, @Nullable UUID player) {
        this(servant.get(), player);
    }

    private Participant(T servant, @Nullable UUID player) {
        this.uuid = new ParticipantId(player != null ? player : servant.getUUID(), servant.getUUID());
        this.servant = new WeakReference<>(servant);
    }

    public Participant(CompoundTag tag) {
        this.uuid = new ParticipantId(tag.getUUID("UUID"), tag.getUUID("Servant"));
        if (tag.contains("CachedLevel")) {
            this.levelCache = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString("CachedLevel")));
        }
    }

    public boolean isPlayerParticipant() {
        return !this.uuid.participant().equals(this.uuid.servant());
    }

    public UUID getId() {
        return this.uuid.participant();
    }

    @Nullable
    public ServerPlayer getAsPlayer(MinecraftServer server) {
        if (!this.isPlayerParticipant())
            return null;
        return server.getPlayerList().getPlayer(this.uuid.participant());
    }

    @SuppressWarnings("unchecked")
    public T getServant(MinecraftServer server) {
        T servant = this.servant == null ? null : this.servant.get();
        if (servant == null || !servant.isAlive()) {
            if (this.levelCache != null) {
                ServerLevel level = server.getLevel(this.levelCache);
                if (level != null) {
                    Entity entity = EntityUtils.findFromUUID(Entity.class, level, this.uuid.servant());
                    if (entity instanceof ServantLike) {
                        this.servant = new WeakReference<>((T) entity);
                        return this.servant.get();
                    }
                }
            }
            for (ServerLevel level : server.getAllLevels()) {
                Entity entity = EntityUtils.findFromUUID(BaseServant.class, level, this.uuid.servant());
                if (entity instanceof ServantLike) {
                    this.servant = new WeakReference<>((T) entity);
                    this.levelCache = entity.level().dimension();
                    break;
                }
            }
        }
        return servant;
    }

    public boolean valid(MinecraftServer server) {
        T servant = this.getServant(server);
        return servant != null && servant.isAlive();
    }

    private ResourceKey<Level> cachedLevel() {
        T servant = this.servant == null ? null : this.servant.get();
        return servant != null ? servant.level().dimension() : null;
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Participant<?> participant))
            return false;
        return this.getId().equals(participant.getId());
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("UUID", this.uuid.participant());
        tag.putUUID("Servant", this.uuid.servant());
        ResourceKey<Level> cache = this.cachedLevel();
        if (cache != null)
            tag.putString("CachedLevel", cache.location().toString());
        return tag;
    }

    private record ParticipantId(UUID participant, UUID servant) {

    }
}
