package com.flemmli97.fate.common.commands;

import com.flemmli97.fate.common.world.GrailWarHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.world.server.ServerWorld;

public class CommandHandler {

    public static void reg(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("fate")
                .then(Commands.literal("reset").requires(src -> src.hasPermissionLevel(2)).executes(CommandHandler::resetWar))
        );
    }

    private static int resetWar(CommandContext<CommandSource> ctx) {
        ServerWorld world = ctx.getSource().getWorld();
        GrailWarHandler.get(world).reset(world);
        return Command.SINGLE_SUCCESS;
    }
}
