package com.flemmli97.fate.common.grail;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TruceHandler extends WorldSavedData {

    private static final String identifier = "TruceData";

    private Map<UUID, Set<UUID>> truceMap = Maps.newHashMap();
    private Map<UUID, Set<UUID>> pendingRequests = Maps.newHashMap();

    public TruceHandler(){
        this(identifier);
    }
    private TruceHandler(String identifier) {
        super(identifier);
    }

    public static TruceHandler get(World world) {
        return world.getServer().getOverworld().getSavedData().getOrCreate(TruceHandler::new, identifier);
    }

    public boolean sendRequest(PlayerEntity from, UUID to){
        return false;
    }

    public boolean hasRequestFrom(UUID from, UUID to){
        return false;
    }

    public Set<UUID> pending(PlayerEntity player){
        return null;
    }

    public void accept(PlayerEntity player, UUID request){

    }

    public Set<UUID> get(UUID player){
        return null;
    }

    public void disband(PlayerEntity player, UUID uuid){

    }

    public void disbandAll(PlayerEntity player){

    }

    public void reset(){

    }

    @Override
    public void read(CompoundNBT p_76184_1_) {

    }

    @Override
    public CompoundNBT write(CompoundNBT p_189551_1_) {
        return null;
    }
}
