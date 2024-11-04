package io.github.flemmli97.fateubw.common.world;

import com.google.common.collect.ImmutableSet;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TruceHandler extends SavedData {

    private static final String IDENTIFIER = "FateTruceData";

    private final Map<UUID, Set<UUID>> truceMap = new HashMap<>();
    private final Map<UUID, Set<UUID>> pendingRequests = new HashMap<>();

    public TruceHandler() {
    }

    private TruceHandler(CompoundTag tag) {
        this.load(tag);
    }

    public static TruceHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(TruceHandler::new, TruceHandler::new, IDENTIFIER);
    }

    public boolean sendRequest(ServerPlayer from, UUID to) {
        if (this.pendingRequests.computeIfAbsent(to, o -> new HashSet<>())
                .add(from.getUUID())) {
            this.setDirty();
            Player player = from.level.getPlayerByUUID(to);
            GameProfile rec = player != null ? player.getGameProfile() : from.getServer().getProfileCache().get(to).orElse(null);
            if (rec == null)
                return false;
            from.sendMessage(new TranslatableComponent("fateubw.chat.truce.send", rec.getName()).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
            if (player != null)
                player.sendMessage(new TranslatableComponent("fateubw.chat.truce.request", from.getName()).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
            return true;
        }
        return false;
    }

    public boolean hasRequestFrom(UUID from, UUID to) {
        return this.pendingRequests.get(to).contains(from);
    }

    public Set<UUID> pending(Player player) {
        Set<UUID> set = this.pendingRequests.get(player.getUUID());
        return set == null ? Set.of() : ImmutableSet.copyOf(set);
    }

    public Set<UUID> outgoingRequests(Player player) {
        return this.pendingRequests.entrySet().stream().filter(e -> e.getValue().contains(player.getUUID()))
                .map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public void accept(ServerPlayer player, UUID request) {
        Set<UUID> pending = this.pendingRequests.get(player.getUUID());
        if (pending != null && pending.contains(request)) {
            pending.remove(request);
            this.truceMap.computeIfAbsent(player.getUUID(), o -> new HashSet<>())
                    .add(request);
            this.truceMap.computeIfAbsent(request, o -> new HashSet<>())
                    .add(player.getUUID());
            ServerPlayer other = player.getServer().getPlayerList().getPlayer(request);
            GameProfile rec = other != null ? player.getGameProfile() : player.getServer().getProfileCache().get(request).orElse(null);
            if (rec == null)
                return;
            player.sendMessage(new TranslatableComponent("fateubw.chat.truce.accept", rec.getName()).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
            // Reset the servants targeting so it doesn't target other players servant
            GrailWarHandler tracker = GrailWarHandler.get(player.getServer());
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                if (tracker.getServant(player) != null)
                    tracker.getServant(player).setTarget(null);
            });
            if (other != null) {
                other.sendMessage(new TranslatableComponent("fateubw.chat.truce.requestsuccess", player.getName(), ChatFormatting.GOLD), Util.NIL_UUID);
                Platform.INSTANCE.getPlayerData(other).ifPresent(data -> {
                    if (tracker.getServant(other) != null)
                        tracker.getServant(other).setTarget(null);
                });
            }
            this.setDirty();
        }
    }

    public void disband(Player player, UUID uuid) {
        Set<UUID> truces = this.truceMap.get(player.getUUID());
        if (truces != null && truces.contains(uuid)) {
            truces.remove(uuid);
            Set<UUID> truces2 = this.truceMap.get(uuid);
            if (truces2 != null)
                truces2.remove(player.getUUID());
            Player other = player.level.getPlayerByUUID(uuid);
            GameProfile rec = other != null ? player.getGameProfile() : player.getServer().getProfileCache().get(uuid).orElse(null);
            if (rec == null)
                return;
            player.sendMessage(new TranslatableComponent("fateubw.chat.truce.disband", rec.getName()).withStyle(ChatFormatting.RED), Util.NIL_UUID);
            if (other != null)
                other.sendMessage(new TranslatableComponent("fateubw.chat.truce.disband", player.getName(), ChatFormatting.RED), Util.NIL_UUID);
            this.setDirty();
        }
    }

    public void disbandAll(Player player) {
        Set<UUID> truce = this.truceMap.remove(player.getUUID());
        if (truce != null) {
            for (UUID a : truce) {
                Set<UUID> truces = this.truceMap.get(a);
                if (truces != null)
                    truces.remove(player.getUUID());
            }
        }
        this.truceMap.remove(player.getUUID());
        this.setDirty();
    }

    public Set<UUID> get(UUID player) {
        return this.truceMap.getOrDefault(player, Set.of());
    }

    public void load(CompoundTag nbt) {
        CompoundTag requests = nbt.getCompound("Requests");
        for (String s : requests.getAllKeys()) {
            ListTag list = requests.getList(s, Tag.TAG_INT_ARRAY);
            Set<UUID> players = new HashSet<>();
            list.forEach(value -> players.add(NbtUtils.loadUUID(value)));
            this.pendingRequests.put(UUID.fromString(s), players);
        }
        CompoundTag truce = nbt.getCompound("Truce");
        for (String s : truce.getAllKeys()) {
            ListTag list = truce.getList(s, Tag.TAG_INT_ARRAY);
            Set<UUID> players = new HashSet<>();
            list.forEach(value -> players.add(NbtUtils.loadUUID(value)));
            this.truceMap.put(UUID.fromString(s), players);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        CompoundTag requests = new CompoundTag();
        this.pendingRequests.forEach((uuid, set) -> {
            ListTag list = new ListTag();
            set.forEach(u -> list.add(NbtUtils.createUUID(u)));
            requests.put(uuid.toString(), list);
        });
        compound.put("Requests", requests);
        CompoundTag truce = new CompoundTag();
        this.truceMap.forEach((uuid, set) -> {
            ListTag list = new ListTag();
            set.forEach(u -> list.add(NbtUtils.createUUID(u)));
            truce.put(uuid.toString(), list);
        });
        compound.put("Truce", truce);
        return compound;
    }
}