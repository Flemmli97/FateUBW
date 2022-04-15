package io.github.flemmli97.fateubw.common.world;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.mojang.authlib.GameProfile;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TruceHandler extends SavedData {

    private static final String identifier = "TruceData";

    private final SetMultimap<UUID, UUID> truceMap = HashMultimap.create();
    private final SetMultimap<UUID, UUID> pendingRequests = HashMultimap.create();

    public TruceHandler() {
    }

    private TruceHandler(CompoundTag tag) {
        this.load(tag);
    }

    public static TruceHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(TruceHandler::new, TruceHandler::new, identifier);
    }

    public boolean sendRequest(ServerPlayer from, UUID to) {
        if (this.pendingRequests.put(to, from.getUUID())) {
            this.setDirty();
            Player player = from.level.getPlayerByUUID(to);
            GameProfile rec = player != null ? player.getGameProfile() : from.getServer().getProfileCache().get(to).get();
            if (rec == null)
                return false;
            from.sendMessage(new TranslatableComponent("chat.truce.send", rec.getName()).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
            if (player != null)
                player.sendMessage(new TranslatableComponent("chat.truce.request", from.getName()).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
            return true;
        }
        return false;
    }

    public boolean hasRequestFrom(UUID from, UUID to) {
        return this.pendingRequests.get(to).contains(from);
    }

    public Set<UUID> pending(Player player) {
        return this.pendingRequests.get(player.getUUID());
    }

    public Set<UUID> outgoingRequests(Player player) {
        return this.pendingRequests.asMap().entrySet().stream().filter(e -> e.getValue().contains(player.getUUID()))
                .map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public void accept(ServerPlayer player, UUID request) {
        if (this.pendingRequests.get(player.getUUID()).contains(request)) {
            this.pendingRequests.remove(player.getUUID(), request);
            this.truceMap.put(player.getUUID(), request);
            this.truceMap.put(request, player.getUUID());
            Player other = player.level.getPlayerByUUID(request);
            GameProfile rec = other != null ? player.getGameProfile() : player.getServer().getProfileCache().get(request).get();
            if (rec == null)
                return;
            player.sendMessage(new TranslatableComponent("chat.truce.accept", rec.getName()).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                if (data.getServant(player) != null)
                    data.getServant(player).setTarget(null);
            });
            if (other != null) {
                other.sendMessage(new TranslatableComponent("chat.truce.requestsuccess", player.getName(), ChatFormatting.GOLD), Util.NIL_UUID);
                Platform.INSTANCE.getPlayerData(other).ifPresent(cap -> {
                    if (cap.getServant(other) != null)
                        cap.getServant(other).setTarget(null);
                });
            }
            this.setDirty();
        }
    }

    public void disband(Player player, UUID uuid) {
        if (this.truceMap.get(player.getUUID()).contains(uuid)) {
            this.truceMap.remove(player.getUUID(), uuid);
            this.truceMap.remove(uuid, player.getUUID());
            Player other = player.level.getPlayerByUUID(uuid);
            GameProfile rec = other != null ? player.getGameProfile() : player.getServer().getProfileCache().get(uuid).get();
            if (rec == null)
                return;
            player.sendMessage(new TranslatableComponent("chat.truce.disband", rec.getName()).withStyle(ChatFormatting.RED), Util.NIL_UUID);
            if (other != null)
                other.sendMessage(new TranslatableComponent("chat.truce.disband", player.getName(), ChatFormatting.RED), Util.NIL_UUID);
            this.setDirty();
        }
    }

    public void disbandAll(Player player) {
        for (UUID a : this.truceMap.get(player.getUUID())) {
            this.truceMap.remove(a, player.getUUID());
        }
        this.truceMap.removeAll(player.getUUID());
        this.setDirty();
    }

    public Set<UUID> get(UUID player) {
        return this.truceMap.get(player);
    }

    public void load(CompoundTag nbt) {
        CompoundTag requests = nbt.getCompound("Requests");
        for (String s : requests.getAllKeys()) {
            ListTag list = requests.getList(s, Tag.TAG_INT_ARRAY);
            list.forEach(value -> this.pendingRequests.put(UUID.fromString(s), NbtUtils.loadUUID(value)));
        }
        CompoundTag truce = nbt.getCompound("Truce");
        for (String s : truce.getAllKeys()) {
            ListTag list = truce.getList(s, Tag.TAG_INT_ARRAY);
            list.forEach(value -> this.truceMap.put(UUID.fromString(s), NbtUtils.loadUUID(value)));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        CompoundTag requests = new CompoundTag();
        this.pendingRequests.asMap().forEach((uuid, set) -> {
            ListTag list = new ListTag();
            set.forEach(u -> list.add(NbtUtils.createUUID(u)));
            requests.put(uuid.toString(), list);
        });
        compound.put("Requests", requests);
        CompoundTag truce = new CompoundTag();
        this.truceMap.asMap().forEach((uuid, set) -> {
            ListTag list = new ListTag();
            set.forEach(u -> list.add(NbtUtils.createUUID(u)));
            truce.put(uuid.toString(), list);
        });
        compound.put("Truce", truce);
        return compound;
    }
}