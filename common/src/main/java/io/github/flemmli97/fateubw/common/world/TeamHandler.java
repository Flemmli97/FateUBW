package io.github.flemmli97.fateubw.common.world;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.network.S2CTeamGuiData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeamHandler extends SavedData {

    private static final String IDENTIFIER = "FateGrailTeams";
    private static final SavedData.Factory<TeamHandler> FACTORY = new Factory<>(TeamHandler::new, TeamHandler::new, DataFixTypes.LEVEL);

    private final Map<UUID, GrailTeam> teams = new HashMap<>();
    private final Map<UUID, UUID> teamsByPlayer = new HashMap<>();

    private final Map<UUID, WeakReference<ServerPlayer>> listeners = new HashMap<>();

    private TeamHandler() {
    }

    private TeamHandler(CompoundTag tag, HolderLookup.Provider provider) {
        this.load(tag);
    }

    public static TeamHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, IDENTIFIER);
    }

    @Nullable
    public GrailTeam getTeamFor(Player player) {
        return this.getTeamFor(player.getUUID());
    }

    @Nullable
    private GrailTeam getTeamFor(UUID player) {
        UUID team = this.teamsByPlayer.get(player);
        if (team != null)
            return this.teams.get(team);
        return null;
    }

    @Nullable
    public GrailTeam getTeamFor(Entity entity) {
        if (entity instanceof Player player) {
            return this.getTeamFor(player);
        }
        if (entity instanceof OwnableEntity ownable) {
            if (ownable.getOwnerUUID() == null) {
                return null;
            }
            Entity owner = ownable.getOwner();
            if (owner != null)
                return this.getTeamFor(owner);
            return this.getTeamFor(ownable.getOwnerUUID());
        }
        return null;
    }

    public List<GrailTeam> getTeams(GrailTeam except) {
        return this.teams.values().stream().filter(name -> !name.equals(except)).sorted(Comparator.comparing(GrailTeam::getName)).toList();
    }

    public boolean areAllies(Entity entity, Entity other) {
        Team team = entity.getTeam();
        if (team != null && team.isAlliedTo(other.getTeam()))
            return true;
        GrailTeam first = this.getTeamFor(entity);
        GrailTeam second = this.getTeamFor(other);
        if (first != null && second != null)
            return first.isAlliedTo(second);
        return false;
    }

    public void createTeam(Player player, String name) {
        UUID uuid = player.getUUID();
        if (this.teamsByPlayer.containsKey(uuid)) {
            player.sendSystemMessage(Component.translatable("fateubw.chat.team.player.exist", player.getName()).withStyle(ChatFormatting.RED));
            return;
        }
        GrailTeam team = new GrailTeam(name, uuid);
        this.teams.put(team.getId(), team);
        this.teamsByPlayer.put(uuid, team.getId());
        this.onTeamChange(this.listeners.keySet());
    }

    public void joinTeam(Player player, UUID teamID) {
        UUID uuid = player.getUUID();
        if (this.teamsByPlayer.containsKey(uuid)) {
            player.sendSystemMessage(Component.translatable("fateubw.chat.team.player.exist", player.getName()).withStyle(ChatFormatting.RED));
            return;
        }
        GrailTeam team = this.teams.get(teamID);
        if (team == null) {
            player.sendSystemMessage(Component.translatable("fateubw.chat.team.missing", teamID.toString()).withStyle(ChatFormatting.RED));
            return;
        }
        if (team.addPlayer(uuid)) {
            this.teamsByPlayer.put(uuid, team.getId());
            this.fetchInvitesFor(player)
                    .forEach(inf -> this.denyInvite(player, inf.id()));
            this.onTeamChange(team.members());
        }
    }

    public void removeFromTeam(Player player, UUID target) {
        UUID team = this.teamsByPlayer.get(player.getUUID());
        if (team != null)
            this.removeFromTeam(player, target, team);
    }

    public void removeFromTeam(Player source, UUID player, UUID teamID) {
        GrailTeam team = this.teams.get(teamID);
        if (team == null) {
            return;
        }
        if (source.getUUID().equals(player) && team.getCreator().equals(player)) {
            team.onDisband(this);
            this.teamsByPlayer.remove(player);
            this.teams.remove(team.getId());
            this.onTeamChange(this.listeners.keySet());
        } else if (team.removePlayer(source, player)) {
            this.teamsByPlayer.remove(player);
            this.onTeamChange(team.members());
            this.onTeamChange(List.of(player));
        }
    }

    /**
     * Get all teams that invited the given player
     */
    public List<GrailTeam.ShortTeamInfo> fetchInvitesFor(Player player) {
        return this.teams.values().stream().filter(t -> t.getPosition(player.getUUID()) == GrailTeam.TeamPosition.INVITED)
                .map(GrailTeam::getInfo).sorted().toList();
    }

    /**
     * Get all teams that requested an alliance with the given team
     */
    public List<GrailTeam.ShortTeamInfo> fetchRequestsFor(Player player, GrailTeam team) {
        if (team == null)
            return List.of();
        return this.teams.values().stream().filter(t -> !t.equals(team) && t.isAdmin(player.getUUID()) && t.getAllyStatus(team) == GrailTeam.TeamStatus.INCOMING_REQUEST)
                .map(GrailTeam::getInfo).sorted().toList();
    }

    public void rename(Player source, String newName) {
        GrailTeam team = this.getTeamFor(source);
        if (team == null) {
            return;
        }
        if (team.rename(source, newName)) {
            this.onTeamChange(team.members());
        }
    }

    public void givePerms(Player source, UUID player) {
        GrailTeam team = this.getTeamFor(source);
        if (team == null) {
            return;
        }
        if (team.givePerms(source, player)) {
            this.onTeamChange(team.members());
        }
    }

    public void revokePerms(Player source, UUID player) {
        GrailTeam team = this.getTeamFor(source);
        if (team == null) {
            return;
        }
        if (team.revokePerms(source, player)) {
            this.onTeamChange(team.members());
        }
    }

    public void retractInvite(Player source, UUID player) {
        GrailTeam team = this.getTeamFor(source);
        if (team == null) {
            return;
        }
        if (team.removeInvite(source, player)) {
            this.onTeamChange(team.members());
            this.onTeamChange(List.of(player));
        }
    }

    public void invite(Player source, UUID player) {
        GrailTeam team = this.getTeamFor(source);
        if (team == null) {
            return;
        }
        if (team.invite(source, player)) {
            this.onTeamChange(team.members());
            this.onTeamChange(List.of(player));
        }
    }

    public void denyInvite(Player player, UUID teamID) {
        GrailTeam team = this.teams.get(teamID);
        if (team == null) {
            return;
        }
        if (team.removeInvite(player, player.getUUID())) {
            this.onTeamChange(team.members());
            this.onTeamChange(List.of(player.getUUID()));
        }
    }

    public void requestAlliance(Player source, UUID teamID) {
        GrailTeam team = this.getTeamFor(source);
        GrailTeam other = this.teams.get(teamID);
        if (team == null || other == null)
            return;
        if (team.requestAlliance(source, other)) {
            this.onTeamChange(team.members());
            this.onTeamChange(other.members());
        }
    }

    public void retractRequest(Player source, UUID teamID) {
        GrailTeam team = this.getTeamFor(source);
        GrailTeam other = this.teams.get(teamID);
        if (team == null || other == null)
            return;
        if (team.removeRequest(source, other)) {
            this.onTeamChange(team.members());
            this.onTeamChange(other.members());
        }
    }

    public void acceptAlliance(Player source, UUID teamID) {
        GrailTeam team = this.getTeamFor(source);
        GrailTeam other = this.teams.get(teamID);
        if (team == null || other == null)
            return;
        if (team.addAlly(source, other)) {
            this.onTeamChange(team.members());
            this.onTeamChange(other.members());
        }
    }

    public void denyAlliance(Player player, UUID teamID) {
        GrailTeam team = this.getTeamFor(player);
        GrailTeam other = this.teams.get(teamID);
        if (team == null || other == null)
            return;
        if (team.denyRequest(player, other)) {
            this.onTeamChange(team.members());
            this.onTeamChange(other.members());
        }
    }

    public void dissolveAlliance(Player player, UUID teamID) {
        GrailTeam team = this.getTeamFor(player);
        GrailTeam other = this.teams.get(teamID);
        if (team == null || other == null)
            return;
        if (team.removeAlly(player, other)) {
            this.onTeamChange(team.members());
            this.onTeamChange(other.members());
        }
    }

    public void listenChanges(ServerPlayer player) {
        this.listeners.put(player.getUUID(), new WeakReference<>(player));
    }

    public void removeListener(Player player) {
        this.listeners.remove(player.getUUID());
    }

    private void onTeamChange(Collection<UUID> players) {
        players.forEach(uuid -> {
            WeakReference<ServerPlayer> player = this.listeners.get(uuid);
            if (player != null && player.get() != null) {
                S2CTeamGuiData.sendTeamData(player.get(), false);
            }
        });
        this.setDirty();
    }

    public void load(CompoundTag nbt) {
        ListTag teams = nbt.getList("Teams", Tag.TAG_COMPOUND);
        teams.forEach(tag -> {
            try {
                GrailTeam team = GrailTeam.CODEC.parse(NbtOps.INSTANCE, tag).getOrThrow();
                this.teams.put(team.getId(), team);
                team.members().forEach(player -> this.teamsByPlayer.put(player, team.getId()));
            } catch (Exception e) {
                Fate.LOGGER.error("Unable to load team from tag {}", tag, e);
            }
        });
    }

    @Override
    public CompoundTag save(CompoundTag compound, HolderLookup.Provider provider) {
        ListTag teams = new ListTag();
        this.teams.values().forEach(team -> {
            try {
                teams.add(GrailTeam.CODEC.encodeStart(NbtOps.INSTANCE, team).getOrThrow());
            } catch (Exception e) {
                Fate.LOGGER.error("Unable to save team {} with id {}", team.getId(), team.getName(), e);
            }
        });
        compound.put("Teams", teams);
        return compound;
    }
}
