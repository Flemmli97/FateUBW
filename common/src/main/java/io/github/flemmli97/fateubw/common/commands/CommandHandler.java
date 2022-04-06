package io.github.flemmli97.fateubw.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.integration.AstralSorcery;
import io.github.flemmli97.fateubw.common.loot.GrailLootTable;
import io.github.flemmli97.fateubw.common.loot.entry.AttributeEntry;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.Collection;

public class CommandHandler {

    public static SuggestionProvider<CommandSourceStack> GRAILLOOTSUGGESTION = (ctx, builder) -> SharedSuggestionProvider.suggestResource(DatapackHandler.getAllTables().stream(), builder);

    public static void reg(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fate")
                .then(Commands.literal("reset").requires(src -> src.hasPermission(2)).executes(CommandHandler::resetWar)
                        .then(Commands.literal("astral").then(Commands.argument("players", EntityArgument.players()).executes(CommandHandler::resetAstralPoints)))
                        .then(Commands.literal("attributes").then(Commands.argument("players", EntityArgument.players()).executes(CommandHandler::resetAttributes))))
                .then(Commands.literal("loot").requires(src -> src.hasPermission(2))
                        .then(Commands.argument("id", ResourceLocationArgument.id()).suggests(GRAILLOOTSUGGESTION)
                                .then(Commands.argument("players", EntityArgument.players()).executes(CommandHandler::giveLoot))))
        );
    }

    private static int resetWar(CommandContext<CommandSourceStack> ctx) {
        GrailWarHandler.get(ctx.getSource().getServer()).reset(ctx.getSource().getLevel());
        return Command.SINGLE_SUCCESS;
    }

    private static int giveLoot(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "id");
        GrailLootTable loot = DatapackHandler.getLootTable(id).orElse(null);
        if (loot == null) {
            ctx.getSource().sendSuccess(new TranslatableComponent("fate.command.loot.give", players), false);
            return 0;
        }
        players.forEach(loot::give);
        return Command.SINGLE_SUCCESS;
    }

    private static int resetAttributes(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
        Collection<Attribute> attributes = PlatformUtils.INSTANCE.attributes().values();
        players.forEach(player -> attributes.forEach(att -> {
            AttributeInstance inst = player.getAttribute(att);
            if (inst != null)
                inst.removeModifier(AttributeEntry.attributeUUID);
        }));
        ctx.getSource().sendSuccess(new TranslatableComponent("fate.command.attributes.reset", players), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int resetAstralPoints(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (Fate.astralSorcery) {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
            players.forEach(AstralSorcery::resetPerkPoints);
            ctx.getSource().sendSuccess(new TranslatableComponent("fate.command.astral.reset", players), false);
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }
}
