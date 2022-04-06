package io.github.flemmli97.fateubw.common.world;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServantTracker {

    private final Set<BaseServant> servants = new HashSet<>();
    private Map<ChunkPos, ResourceKey<Level>> pos;

    public void add(BaseServant entity) {
        this.servants.add(entity);
    }

    public void remove(BaseServant entity) {
        this.servants.remove(entity);
    }

    public void onLoad(ServerLevel world) {
        if (this.pos != null) {
            this.pos.forEach((c, r) -> {
                if (world.dimension().equals(r))
                    world.getChunkSource().addRegionTicket(TicketType.UNKNOWN, c, 1, c);
                else {
                    ServerLevel w = world.getServer().getLevel(r);
                    w.getChunkSource().addRegionTicket(TicketType.UNKNOWN, c, 1, c);
                }
            });
            this.pos = null;
        }
    }

    public CompoundTag onSave(CompoundTag nbt) {
        CompoundTag tag = new CompoundTag();
        this.servants.forEach(servant -> tag.putString("L:" + ChunkPos.asLong(servant.blockPosition()),
                servant.level.dimension().location().toString()));
        nbt.put("ToLoadChunks", tag);
        return nbt;
    }

    public void load(CompoundTag nbt) {
        this.pos = new HashMap<>();
        CompoundTag tag = nbt.getCompound("ToLoadChunks");
        tag.getAllKeys().forEach(cl -> this.pos.put(new ChunkPos(Long.parseLong(cl.substring(2))), ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(tag.getString(cl)))));
    }
}
