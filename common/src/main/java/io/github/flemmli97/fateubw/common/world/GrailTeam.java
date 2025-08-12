package io.github.flemmli97.fateubw.common.world;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class GrailTeam {

    private static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);

    public static final Codec<GrailTeam> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.STRING.fieldOf("name").forGetter(d -> d.name),
                    UUID_CODEC.fieldOf("id").forGetter(d -> d.id),
                    UUID_CODEC.fieldOf("creator").forGetter(d -> d.creator),
                    Codec.unboundedMap(UUID_CODEC, CodecUtils.stringEnumCodec(TeamPosition.class, null)).fieldOf("players").forGetter(d -> d.players),
                    Codec.unboundedMap(UUID_CODEC, CodecUtils.stringEnumCodec(TeamStatus.class, null)).fieldOf("allies").forGetter(d -> d.allies)
            ).apply(instance, GrailTeam::new));

    private String name;
    private final UUID id;

    private final UUID creator;

    private final Map<UUID, TeamPosition> players = new HashMap<>();
    private final Map<UUID, TeamStatus> allies = new HashMap<>();

    public GrailTeam(String name, UUID creator) {
        this.name = name;
        this.creator = creator;
        this.id = UUID.randomUUID();
    }

    private GrailTeam(String name, UUID id, UUID creator, Map<UUID, TeamPosition> players, Map<UUID, TeamStatus> allies) {
        this.name = name;
        this.id = id;
        this.creator = creator;
        this.players.putAll(players);
        this.allies.putAll(allies);
    }

    public static GameProfile readProfile(FriendlyByteBuf buf) {
        return new GameProfile(buf.readBoolean() ? buf.readUUID() : null, buf.readBoolean() ? buf.readUtf() : null);
    }

    public static void writeProfile(FriendlyByteBuf buf, GameProfile profile) {
        buf.writeBoolean(profile.getId() != null);
        if (profile.getId() != null)
            buf.writeUUID(profile.getId());
        buf.writeBoolean(profile.getName() != null);
        if (profile.getName() != null)
            buf.writeUtf(profile.getName());
    }

    public String getName() {
        return this.name;
    }

    protected boolean rename(Player source, String name) {
        if (!this.isAdmin(source.getUUID())) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.permission.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        this.name = name;
        return true;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getCreator() {
        return this.creator;
    }

    public boolean isAdmin(UUID player) {
        TeamPosition position = this.getPosition(player);
        return position == TeamPosition.CREATOR || position == TeamPosition.ADMIN;
    }

    public boolean isMember(UUID player) {
        return this.getPosition(player).isInTeam();
    }

    public Collection<UUID> members() {
        List<UUID> members = new ArrayList<>();
        members.add(this.creator);
        members.addAll(this.players.keySet());
        return members;
    }

    public TeamPosition getPosition(UUID player) {
        if (this.getCreator().equals(player))
            return TeamPosition.CREATOR;
        return this.players.getOrDefault(player, TeamPosition.NONE);
    }

    public boolean isAlliedTo(GrailTeam other) {
        if (other.equals(this))
            return true;
        return this.getAllyStatus(other) == TeamStatus.ALLY;
    }

    public TeamStatus getAllyStatus(GrailTeam other) {
        return this.allies.getOrDefault(other.getId(), TeamStatus.NONE);
    }

    protected boolean addPlayer(UUID player) {
        if (this.getPosition(player) != TeamPosition.INVITED)
            return false;
        this.players.put(player, TeamPosition.MEMBER);
        return true;
    }

    protected boolean removePlayer(Player source, UUID uuid) {
        if (!this.isAdmin(source.getUUID()) && !source.getUUID().equals(uuid)) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.permission.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        Player player = source.getServer().getPlayerList().getPlayer(uuid);
        if (player != null) {
            player.sendSystemMessage(Component.translatable("fateubw.chat.team.kicked", source.getName(), this.getName()).withStyle(ChatFormatting.RED));
        }
        return this.players.remove(uuid) != null;
    }

    protected boolean givePerms(Player source, UUID uuid) {
        if (!this.isAdmin(source.getUUID())) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.permission.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        if (!this.isMember(uuid) || source.getServer() == null)
            return false;
        this.players.put(uuid, TeamPosition.ADMIN);
        Player player = source.getServer().getPlayerList().getPlayer(uuid);
        Component target;
        if (player != null) {
            target = player.getName();
            player.sendSystemMessage(Component.translatable("fateubw.chat.team.promote", source.getName())
                    .withStyle(ChatFormatting.GOLD));
        } else {
            target = source.getServer().getProfileCache().get(uuid).map(p -> Component.literal(p.getName())).orElse(null);
        }
        if (target != null)
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.promote.user", target)
                    .withStyle(ChatFormatting.GRAY));
        return true;
    }

    protected boolean revokePerms(Player source, UUID uuid) {
        if (!this.isAdmin(source.getUUID())) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.permission.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        if (source.getUUID().equals(uuid)) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.self.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        if (source.getServer() == null)
            return false;
        this.players.put(uuid, TeamPosition.MEMBER);
        Player player = source.getServer().getPlayerList().getPlayer(uuid);
        Component target;
        if (player != null) {
            target = player.getName();
            player.sendSystemMessage(Component.translatable("fateubw.chat.team.demote", source.getName())
                    .withStyle(ChatFormatting.RED));
        } else {
            target = source.getServer().getProfileCache().get(uuid).map(p -> Component.literal(p.getName())).orElse(null);
        }
        if (target != null)
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.demote.user", target)
                    .withStyle(ChatFormatting.GRAY));
        return true;
    }

    protected boolean invite(Player source, UUID uuid) {
        if (!this.isAdmin(source.getUUID())) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.permission.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        if (source.getServer() == null || this.isMember(uuid))
            return false;
        this.players.put(uuid, TeamPosition.INVITED);
        Player player = source.getServer().getPlayerList().getPlayer(uuid);
        Component target;
        if (player != null) {
            target = player.getName();
            player.sendSystemMessage(Component.translatable("fateubw.chat.team.invite.received", this.getName())
                    .withStyle(ChatFormatting.GREEN));
        } else {
            target = source.getServer().getProfileCache().get(uuid).map(p -> Component.literal(p.getName())).orElse(null);
        }
        if (target != null)
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.invite.sent", target)
                    .withStyle(ChatFormatting.GRAY));
        return true;
    }

    protected boolean removeInvite(Player source, UUID uuid) {
        if (!this.isAdmin(source.getUUID()) && !source.getUUID().equals(uuid)) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.permission.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        return this.players.remove(uuid, TeamPosition.INVITED);
    }

    protected boolean addAlly(Player source, GrailTeam team) {
        if (!this.isAdmin(source.getUUID())) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.permission.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        if (source.getServer() == null)
            return false;
        TeamStatus status = this.getAllyStatus(team);
        TeamStatus other = team.getAllyStatus(this);
        if (status != TeamStatus.INCOMING_REQUEST || other != TeamStatus.OUTGOING_REQUEST) {
            if (status != TeamStatus.ALLY && other != TeamStatus.ALLY) {
                this.allies.remove(team.getId());
                team.allies.remove(this.getId());
                return true;
            }
            return false;
        }
        this.allies.put(team.getId(), TeamStatus.ALLY);
        team.allies.put(this.getId(), TeamStatus.ALLY);
        this.notifyAdmins(this, source.getServer(), player -> player.sendSystemMessage(Component.translatable("fateubw.chat.team.alliance.start", this.getName(), team.getName()).withStyle(ChatFormatting.GOLD)));
        this.notifyAdmins(team, source.getServer(), player -> player.sendSystemMessage(Component.translatable("fateubw.chat.team.alliance.start", this.getName(), team.getName()).withStyle(ChatFormatting.GOLD)));
        return true;
    }

    protected boolean removeAlly(Player source, GrailTeam team) {
        if (!this.isAdmin(source.getUUID())) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.permission.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        if (source.getServer() == null)
            return false;
        this.allies.remove(team.getId());
        team.allies.remove(this.getId());
        team.notifyAdmins(this, source.getServer(), player -> player.sendSystemMessage(Component.translatable("fateubw.chat.team.alliance.dissolved.with", source.getName(), team.getName()).withStyle(ChatFormatting.RED)));
        source.sendSystemMessage(Component.translatable("fateubw.chat.team.alliance.dissolved", source.getName(), this.getName())
                .withStyle(ChatFormatting.GRAY));
        return true;
    }

    protected boolean requestAlliance(Player source, GrailTeam team) {
        if (!this.isAdmin(source.getUUID())) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.permission.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        if (source.getServer() == null || this.getAllyStatus(team) != TeamStatus.NONE)
            return false;
        this.allies.put(team.getId(), TeamStatus.OUTGOING_REQUEST);
        team.allies.put(this.getId(), TeamStatus.INCOMING_REQUEST);
        team.notifyAdmins(team, source.getServer(), player -> player.sendSystemMessage(Component.translatable("fateubw.chat.team.alliance.received", this.getName()).withStyle(ChatFormatting.GREEN)));
        source.sendSystemMessage(Component.translatable("fateubw.chat.team.alliance.sent", team.getName())
                .withStyle(ChatFormatting.GRAY));
        return true;
    }

    protected boolean removeRequest(Player source, GrailTeam team) {
        if (!this.isAdmin(source.getUUID())) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.permission.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        TeamStatus status = this.getAllyStatus(team);
        if (status != TeamStatus.OUTGOING_REQUEST)
            return false;
        this.allies.remove(team.getId());
        team.allies.remove(this.getId());
        return true;
    }

    protected boolean denyRequest(Player source, GrailTeam team) {
        if (!this.isAdmin(source.getUUID())) {
            source.sendSystemMessage(Component.translatable("fateubw.chat.team.permission.no").withStyle(ChatFormatting.DARK_RED));
            return false;
        }
        if (source.getServer() == null)
            return false;
        TeamStatus status = this.getAllyStatus(team);
        if (status != TeamStatus.INCOMING_REQUEST)
            return false;
        this.allies.remove(team.getId());
        team.allies.remove(this.getId());
        this.notifyAdmins(team, source.getServer(), player -> player.sendSystemMessage(Component.translatable("fateubw.chat.team.alliance.denied", this.getName())
                .withStyle(ChatFormatting.RED)));
        return true;
    }

    protected void onDisband(TeamHandler teamHandler) {
        teamHandler.getTeams(this)
                .forEach(other -> other.allies.remove(this.getId()));
    }

    private void notifyAdmins(GrailTeam team, MinecraftServer server, Consumer<ServerPlayer> consumer) {
        for (UUID member : team.members()) {
            TeamPosition pos = this.getPosition(member);
            if (pos != TeamPosition.ADMIN && pos != TeamPosition.CREATOR)
                continue;
            ServerPlayer player = server.getPlayerList().getPlayer(member);
            if (player != null) {
                consumer.accept(player);
            }
        }
    }

    public GrailTeam.ShortTeamInfo getInfo() {
        return this.getInfo(null);
    }

    public GrailTeam.ShortTeamInfo getInfo(@Nullable Player player) {
        return new GrailTeam.ShortTeamInfo(this.getName(), this.getId(), this.getCreator(), player != null && this.isAdmin(player.getUUID()));
    }

    public record ClientTeamInfo(Optional<ShortTeamInfo> team, List<Pair<GameProfile, TeamPosition>> players,
                                 List<Pair<ShortTeamInfo, TeamStatus>> others, List<ShortTeamInfo> invites) {

        public static ClientTeamInfo create(ServerPlayer player, TeamHandler handler) {
            GrailTeam team = handler.getTeamFor(player);
            List<Pair<GameProfile, TeamPosition>> players = new ArrayList<>();
            if (team != null) {
                for (ServerPlayer others : player.getServer().getPlayerList().getPlayers()) {
                    if (handler.getTeamFor(others) != null || team.players.containsKey(others.getUUID()))
                        continue;
                    players.add(Pair.of(others.getGameProfile(), TeamPosition.NONE));
                }
                for (Map.Entry<UUID, TeamPosition> entry : team.players.entrySet()) {
                    player.getServer().getProfileCache().get(entry.getKey()).ifPresent(prof -> players.add(Pair.of(prof, entry.getValue())));
                }
                player.getServer().getProfileCache().get(team.getCreator()).ifPresent(prof -> players.add(Pair.of(prof, TeamPosition.CREATOR)));
                players.sort(Comparator.comparing(p -> p.getFirst().getName()));
            }
            List<Pair<ShortTeamInfo, TeamStatus>> others = team == null ? List.of() : handler.getTeams(team)
                    .stream()
                    .map(other -> Pair.of(other.getInfo(), team.getAllyStatus(other))).toList();
            return new ClientTeamInfo(Optional.ofNullable(team != null ? team.getInfo(player) : null),
                    players, others, handler.fetchInvitesFor(player));
        }

        public static ClientTeamInfo read(FriendlyByteBuf buf) {
            return new ClientTeamInfo(buf.readBoolean() ? Optional.of(new ShortTeamInfo(buf)) : Optional.empty(),
                    buf.readList(b -> Pair.of(GrailTeam.readProfile(b), b.readEnum(TeamPosition.class))),
                    buf.readList(b -> Pair.of(new ShortTeamInfo(b), b.readEnum(TeamStatus.class))),
                    buf.readList(ShortTeamInfo::new));
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeBoolean(this.team.isPresent());
            this.team.ifPresent(inf -> inf.write(buf));
            buf.writeCollection(this.players, (b, p) -> {
                GrailTeam.writeProfile(b, p.getFirst());
                b.writeEnum(p.getSecond());
            });
            buf.writeCollection(this.others, (b, p) -> {
                p.getFirst().write(b);
                b.writeEnum(p.getSecond());
            });
            buf.writeCollection(this.invites, (b, inf) -> inf.write(b));
        }
    }

    public record ShortTeamInfo(String name, UUID id, UUID creator,
                                boolean admin) implements Comparable<ShortTeamInfo> {

        public ShortTeamInfo(FriendlyByteBuf buf) {
            this(buf.readUtf(), buf.readUUID(), buf.readUUID(), buf.readBoolean());
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeUtf(this.name());
            buf.writeUUID(this.id());
            buf.writeUUID(this.creator());
            buf.writeBoolean(this.admin());
        }

        @Override
        public int compareTo(@NotNull GrailTeam.ShortTeamInfo o) {
            return this.name().compareTo(o.name());
        }

        @Override
        public @NotNull String toString() {
            return this.name();
        }
    }

    public enum TeamPosition {
        NONE,
        INVITED,
        MEMBER,
        ADMIN,
        CREATOR;

        public boolean isInTeam() {
            return this == MEMBER || this == ADMIN || this == CREATOR;
        }
    }

    public enum TeamStatus {
        NONE,
        OUTGOING_REQUEST,
        INCOMING_REQUEST,
        ALLY
    }
}
