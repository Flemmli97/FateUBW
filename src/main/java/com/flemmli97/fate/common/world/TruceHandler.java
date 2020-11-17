package com.flemmli97.fate.common.world;

import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TruceHandler extends WorldSavedData {

    private static final String identifier = "TruceData";

    private SetMultimap<UUID, UUID> truceMap = HashMultimap.create();
    private SetMultimap<UUID, UUID> pendingRequests = HashMultimap.create();

    public TruceHandler() {
        this(identifier);
    }

    private TruceHandler(String identifier) {
        super(identifier);
    }

    public static TruceHandler get(ServerWorld world) {
        return world.getServer().getOverworld().getSavedData().getOrCreate(TruceHandler::new, identifier);
    }

    public boolean sendRequest(ServerPlayerEntity from, UUID to) {
        if (this.pendingRequests.put(to, from.getUniqueID())) {
            this.markDirty();
            PlayerEntity player = from.world.getPlayerByUuid(to);
            GameProfile rec = player != null ? player.getGameProfile() : from.getServer().getPlayerProfileCache().getProfileByUUID(to);
            from.sendMessage(new TranslationTextComponent("chat.truce.send", rec.getName()).formatted(TextFormatting.GOLD), Util.NIL_UUID);
            if (player != null)
                player.sendMessage(new TranslationTextComponent("chat.truce.request", from.getName()).formatted(TextFormatting.GOLD), Util.NIL_UUID);
            return true;
        }
        return false;
    }

    public boolean hasRequestFrom(UUID from, UUID to) {
        return this.pendingRequests.get(to).contains(from);
    }

    public Set<UUID> pending(PlayerEntity player) {
        return this.pendingRequests.get(player.getUniqueID());
    }

    public Set<UUID> outgoingRequests(PlayerEntity player) {
        return this.pendingRequests.asMap().entrySet().stream().filter(e->e.getValue().contains(player.getUniqueID()))
                .map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public void accept(ServerPlayerEntity player, UUID request) {
        if (this.pendingRequests.get(player.getUniqueID()).contains(request)) {
            this.pendingRequests.remove(player.getUniqueID(), request);
            this.truceMap.put(player.getUniqueID(), request);
            this.truceMap.put(request, player.getUniqueID());
            PlayerEntity other = player.world.getPlayerByUuid(request);
            GameProfile rec = other != null ? player.getGameProfile() : player.getServer().getPlayerProfileCache().getProfileByUUID(request);
            player.sendMessage(new TranslationTextComponent("chat.truce.accept", rec.getName()).formatted(TextFormatting.GOLD), Util.NIL_UUID);
            player.getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap -> {
                if (cap.getServant(player) != null)
                    cap.getServant(player).setAttackTarget(null);
            });
            if (other != null) {
                other.sendMessage(new TranslationTextComponent("chat.truce.requestsuccess", player.getName(), TextFormatting.GOLD), Util.NIL_UUID);
                other.getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap -> {
                    if (cap.getServant(other) != null)
                        cap.getServant(other).setAttackTarget(null);
                });
            }
            this.markDirty();
        }
    }

    public void disband(PlayerEntity player, UUID uuid) {
        if (this.truceMap.get(player.getUniqueID()).contains(uuid)) {
            this.truceMap.remove(player.getUniqueID(), uuid);
            this.truceMap.remove(uuid, player.getUniqueID());
            this.markDirty();
        }
    }

    public void disbandAll(PlayerEntity player) {
        for (UUID a : this.truceMap.get(player.getUniqueID())) {
            this.truceMap.remove(a, player.getUniqueID());
        }
        this.truceMap.removeAll(player.getUniqueID());
        this.markDirty();
    }

    public Set<UUID> get(UUID player) {
        return truceMap.get(player);
    }

    @Override
    public void read(CompoundNBT nbt) {
        CompoundNBT requests = nbt.getCompound("Requests");
        for (String s : requests.keySet()) {
            ListNBT list = requests.getList(s, Constants.NBT.TAG_INT_ARRAY);
            list.forEach(value -> this.pendingRequests.put(UUID.fromString(s), NBTUtil.readUniqueId(value)));
        }
        CompoundNBT truce = nbt.getCompound("Truce");
        for (String s : truce.keySet()) {
            ListNBT list = truce.getList(s, Constants.NBT.TAG_INT_ARRAY);
            list.forEach(value -> this.truceMap.put(UUID.fromString(s), NBTUtil.readUniqueId(value)));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT requests = new CompoundNBT();
        this.pendingRequests.asMap().forEach((uuid, set) -> {
            ListNBT list = new ListNBT();
            set.forEach(u -> list.add(NBTUtil.fromUuid(u)));
            requests.put(uuid.toString(), list);
        });
        compound.put("Requests", requests);
        CompoundNBT truce = new CompoundNBT();
        this.truceMap.asMap().forEach((uuid, set) -> {
            ListNBT list = new ListNBT();
            set.forEach(u -> list.add(NBTUtil.fromUuid(u)));
            truce.put(uuid.toString(), list);
        });
        compound.put("Truce", truce);
        return compound;
    }
}