package io.github.flemmli97.fate.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.datapack.DatapackHandler;
import io.github.flemmli97.fate.common.integration.AstralSorcery;
import io.github.flemmli97.fate.common.loot.GrailLootTable;
import io.github.flemmli97.fate.common.loot.entry.AttributeEntry;
import io.github.flemmli97.fate.common.world.GrailWarHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;

public class CommandHandler {

    public static SuggestionProvider<CommandSource> GRAILLOOTSUGGESTION = (ctx, builder) -> ISuggestionProvider.func_212476_a(DatapackHandler.getAllTables().stream(), builder);

    public static void reg(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("fate")
                .then(Commands.literal("reset").requires(src -> src.hasPermissionLevel(2)).executes(CommandHandler::resetWar)
                        .then(Commands.literal("astral").then(Commands.argument("players", EntityArgument.players()).executes(CommandHandler::resetAstralPoints)))
                        .then(Commands.literal("attributes").then(Commands.argument("players", EntityArgument.players()).executes(CommandHandler::resetAttributes))))
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
        GrailLootTable loot = DatapackHandler.getLootTable(id).orElse(null);
        if (loot == null) {
            ctx.getSource().sendFeedback(new TranslationTextComponent("fate.command.loot.give", players), false);
            return 0;
        }
        players.forEach(loot::give);
        return Command.SINGLE_SUCCESS;
    }

    private static int resetAttributes(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgument.getPlayers(ctx, "players");
        Collection<Attribute> attributes = ForgeRegistries.ATTRIBUTES.getValues();
        players.forEach(player -> attributes.forEach(att -> {
            ModifiableAttributeInstance inst = player.getAttribute(att);
            if (inst != null)
                inst.removeModifier(AttributeEntry.attributeUUID);
        }));
        ctx.getSource().sendFeedback(new TranslationTextComponent("fate.command.attributes.reset", players), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int resetAstralPoints(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        if (Fate.astralSorcery) {
            Collection<ServerPlayerEntity> players = EntityArgument.getPlayers(ctx, "players");
            players.forEach(AstralSorcery::resetPerkPoints);
            ctx.getSource().sendFeedback(new TranslationTextComponent("fate.command.astral.reset", players), false);
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }
}
