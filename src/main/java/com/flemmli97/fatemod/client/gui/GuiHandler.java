package com.flemmli97.fatemod.client.gui;

import com.flemmli97.fatemod.client.gui.CommandGui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	public static final int Command_Gui = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == Command_Gui)
		{
			return new CommandGui(player);
		}
		return null;
	}
	
	
}
