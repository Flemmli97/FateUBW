package com.flemmli97.fatemod.common.commands;


import java.util.ArrayList;
import java.util.List;

import com.flemmli97.fatemod.common.handler.GrailWarHandler;
import com.google.common.collect.Lists;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class GrailWarReset implements ICommand{

	private final List<String> aliases = new ArrayList<String>();

	public GrailWarReset()
	{
		this.aliases.add("grailWarReset");
	}
	
	@Override
	public String getName() {
		return "grailWarReset";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/grailWarReset";
	}

	@Override
	public List<String> getAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		GrailWarHandler.get(server.getEntityWorld()).reset(server.getEntityWorld());
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		return Lists.newArrayList();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}
}
