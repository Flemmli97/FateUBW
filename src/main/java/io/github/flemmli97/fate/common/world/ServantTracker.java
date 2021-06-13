package io.github.flemmli97.fate.common.world;

import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServantTracker {

    private Set<EntityServant> servants = new HashSet<>();
    private Map<ChunkPos, RegistryKey<World>> pos;

    public void add(EntityServant entity) {
        this.servants.add(entity);
    }

    public void remove(EntityServant entity) {
        this.servants.remove(entity);
    }

    public void onLoad(ServerWorld world) {
        if (this.pos != null) {
            this.pos.forEach((c, r) -> {
                if (world.getDimensionKey().equals(r))
                    world.getChunkProvider().registerTicket(TicketType.UNKNOWN, c, 1, c);
                else {
                    ServerWorld w = world.getServer().getWorld(r);
                    w.getChunkProvider().registerTicket(TicketType.UNKNOWN, c, 1, c);
                }
            });
            this.pos = null;
        }
    }

    public CompoundNBT onSave(CompoundNBT nbt) {
        CompoundNBT tag = new CompoundNBT();
        this.servants.forEach(servant -> tag.putString("L:" + ChunkPos.asLong(servant.chunkCoordX, servant.chunkCoordZ),
                servant.world.getDimensionKey().getLocation().toString()));
        nbt.put("ToLoadChunks", tag);
        return nbt;
    }

    public void load(CompoundNBT nbt) {
        this.pos = new HashMap<>();
        CompoundNBT tag = nbt.getCompound("ToLoadChunks");
        tag.keySet().forEach(cl -> this.pos.put(new ChunkPos(Long.parseLong(cl.substring(2))), RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(tag.getString(cl)))));
    }
}
