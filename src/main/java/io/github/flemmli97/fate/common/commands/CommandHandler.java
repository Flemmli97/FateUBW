package io.github.flemmli97.fate.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.flemmli97.fate.common.datapack.DatapackHandler;
import io.github.flemmli97.fate.common.world.GrailWarHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.Collection;

public class CommandHandler {

    public static SuggestionProvider<CommandSource> GRAILLOOTSUGGESTION = (ctx, builder) -> ISuggestionProvider.func_212476_a(DatapackHandler.getAllTables().stream(), builder);

    public static void reg(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("fate")
                .then(Commands.literal("reset").requires(src -> src.hasPermissionLevel(2)).executes(CommandHandler::resetWar))
                .then(Commands.literal("loot").requires(src -> src.hasPermissionLevel(2))
                        .then(Commands.argument("id", ResourceLocationArgument.resourceLocation()).suggests(GRAILLOOTSUGGESTION)
                                .then(Commands.argument("players", EntityArgument.players()).executes(CommandHandler::giveLoot))))
        );
    }

    private static int resetWar(CommandContext<CommandSource> ctx) {
        ServerWorld world = ctx.getSource().getWorld();
        GrailWarHandler.get(world).reset(world);
        return Command.SINGLE_SUCCESS;
    }

    private static int giveLoot(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgument.getPlayers(ctx, "players");
        ResourceLocation id = ResourceLocationArgument.getResourceLocation(ctx, "id");
        DatapackHandler.getLootTable(id).ifPresent(loot -> {
            players.forEach(player -> loot.give(player));
        });
        return Command.SINGLE_SUCCESS;
    }
}
