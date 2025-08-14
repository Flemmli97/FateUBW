package io.github.flemmli97.fateubw.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.loot.GrailLootTable;
import io.github.flemmli97.fateubw.common.loot.entry.AttributeEntry;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.mixin.AttributeMapAccessor;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class CommandHandler {

    public static SuggestionProvider<CommandSourceStack> GRAILLOOTSUGGESTION = (ctx, builder) -> SharedSuggestionProvider.suggestResource(DatapackHandler.getAllTables().stream(), builder);

    public static void reg(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(Fate.MODID)
                .then(Commands.literal("reset").requires(src -> src.hasPermission(2)).executes(CommandHandler::resetWar)
                        .then(Commands.literal("attributes").then(Commands.argument("players", EntityArgument.players()).executes(CommandHandler::resetAttributes))))
                .then(Commands.literal("start").requires(src -> src.hasPermission(2)).executes(CommandHandler::startWar))
                .then(Commands.literal("loot").requires(src -> src.hasPermission(2))
                        .then(Commands.argument("id", ResourceLocationArgument.id()).suggests(GRAILLOOTSUGGESTION)
                                .then(Commands.argument("players", EntityArgument.players()).executes(CommandHandler::giveLoot))))
                .then(Commands.literal("command_spell").requires(src -> src.hasPermission(2))
                        .then(Commands.literal("set")
                                .then(Commands.argument("players", EntityArgument.players())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1)).executes(ctx -> CommandHandler.modifyCommandspell(ctx, CommnandMode.SET)))))
                        .then(Commands.literal("give")
                                .then(Commands.argument("players", EntityArgument.players())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1)).executes(ctx -> CommandHandler.modifyCommandspell(ctx, CommnandMode.ADD)))))
                        .then(Commands.literal("take")
                                .then(Commands.argument("players", EntityArgument.players())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1)).executes(ctx -> CommandHandler.modifyCommandspell(ctx, CommnandMode.TAKE))))))
        );
    }

    private static int startWar(CommandContext<CommandSourceStack> ctx) {
        if (!GrailWarHandler.get(ctx.getSource().getServer()).forceStartGrailWar()) {
            ctx.getSource().sendFailure(Component.translatable("fateubw.command.war.start.fail"));
            return 0;
        }
        ctx.getSource().sendSuccess(() -> Component.translatable("fateubw.command.war.start"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int resetWar(CommandContext<CommandSourceStack> ctx) {
        GrailWarHandler.get(ctx.getSource().getServer()).reset(true);
        return Command.SINGLE_SUCCESS;
    }

    private static int giveLoot(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "id");
        GrailLootTable loot = DatapackHandler.getLootTable(id).orElse(null);
        if (loot == null) {
            ctx.getSource().sendSuccess(() -> Component.translatable("fateubw.command.loot.none", id.toString()), false);
            return 0;
        }
        players.forEach(loot::give);
        if (players.size() == 1) {
            ctx.getSource().sendSuccess(() -> Component.translatable("fateubw.command.loot.give.single", players.iterator().next().getDisplayName(), id.toString()), false);
        } else {
            ctx.getSource().sendSuccess(() -> Component.translatable("fateubw.command.loot.give", players.iterator().next().getDisplayName(), id.toString()), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int resetAttributes(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
        players.forEach(player -> ((AttributeMapAccessor) player.getAttributes()).getAttributes()
                .forEach((att, inst) -> inst.removeModifier(AttributeEntry.ATTRIBUTE_UUID)));
        ctx.getSource().sendSuccess(() -> Component.translatable("fateubw.command.attributes.reset", players), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int modifyCommandspell(CommandContext<CommandSourceStack> ctx, CommnandMode mode) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        players.forEach(player -> {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            int count = switch (mode) {
                case SET -> amount;
                case TAKE -> data.getCommandSeals() - amount;
                case ADD -> data.getCommandSeals() + amount;
            };
            data.setCommandSeals(player, count);
        });
        switch (mode) {
            case SET ->
                    ctx.getSource().sendSuccess(() -> Component.translatable("fateubw.command.spells.set", players, amount), false);
            case TAKE ->
                    ctx.getSource().sendSuccess(() -> Component.translatable("fateubw.command.spells.take", players, amount), false);
            case ADD ->
                    ctx.getSource().sendSuccess(() -> Component.translatable("fateubw.command.spells.add", players, amount), false);
        }
        return players.size();
    }

    private enum CommnandMode {
        SET,
        TAKE,
        ADD
    }
}
