package com.flemmli97.fatemod.client.gui;

import java.io.IOException;
import java.util.Random;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.GrailWarPlayerTracker;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.fatemod.network.MessageGui;
import com.flemmli97.fatemod.network.MessagePlayerServant;
import com.flemmli97.fatemod.network.PacketHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class CommandGui extends GuiScreen {
	
	public EntityPlayer guiPlayer;
	public EntityServant servant;
	private GuiButton attack,aggressive,normal,defensive,useNP,move,follow,stay,protect,truce,request,accept,remove,kill,forfeit,back,call;
	private int currentPage = 0;
	private Random rand = new Random();
	private int command1 = rand.nextInt(3);
	private int command2 = rand.nextInt(3);
	private int command3 = rand.nextInt(3);
	private GuiPlayerButton[] playerButton;
	
	private final static ResourceLocation guiBackGround = new ResourceLocation(LibReference.MODID + ":textures/gui/command_gui_1.png");
	private final static ResourceLocation guiTruce = new ResourceLocation(LibReference.MODID + ":textures/gui/command_gui_2.png");
	
	public CommandGui(EntityPlayer player)
	{
		guiPlayer = player;	
		PacketHandler.sendToServer(new MessageGui(1));
		PacketHandler.sendToServer(new MessageGui(2));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		IPlayer capSync = guiPlayer.getCapability(PlayerCapProvider.PlayerCap, null);
		if(capSync.getServant()!=null)
		{
			float mouseXNew =(float)((this.width - 200) / 2 + 51) - mouseX;
			float mouseYNew = (float)((this.height - 180) / 2 + 75 - 50) - mouseY;
			GuiInventory.drawEntityOnScreen(this.width/2 -50, this.height / 2 -20, 29, mouseXNew, mouseYNew, capSync.getServant());
		}
		this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
		if(currentPage!=3)
		{
			this.mc.getTextureManager().bindTexture(guiBackGround);
			this.drawTexturedModalRect(this.width/2 -100, this.height / 2 - 100, 0, 0, 200, 210);
			this.drawCommand(capSync.getCommandSeals());

			this.mc.fontRenderer.drawString("§4Name:", this.width/2 -90, this.height / 2 - 5, 1);
			this.mc.fontRenderer.drawString("§4Damage:", this.width/2 -90, this.height / 2 +15, 1);
			this.mc.fontRenderer.drawString("§4Armor:", this.width/2 -90, this.height / 2 + 35, 1);
			this.mc.fontRenderer.drawString("§4Nobel Phantasm:", this.width/2 -90, this.height / 2 + 55, 1);
			if(capSync.getServant()!=null)
			{
				this.mc.fontRenderer.drawString(I18n.format(capSync.getServant().getName()), this.width/2 -90, this.height / 2 +5, 1);
				this.mc.fontRenderer.drawString(String.valueOf(capSync.getServant().props().strength()), this.width/2 -90, this.height / 2 +25, 1);
				this.mc.fontRenderer.drawString(String.valueOf(capSync.getServant().props().armor()), this.width/2 -90, this.height / 2 + 45, 1);
				if(capSync.getServant().nobelPhantasm()!=null)
					this.mc.fontRenderer.drawString(capSync.getServant().nobelPhantasm(), this.width/2 -90, this.height / 2 + 65, 1);
			}
		}
		else
		{
			this.mc.getTextureManager().bindTexture(guiTruce);
			this.drawTexturedModalRect(this.width/2 -100, this.height / 2 - 100, 0, 0, 200, 210);
			this.drawCommand(capSync.getCommandSeals());
		}
		super.drawScreen(mouseX, mouseY, partialTicks);

	}
	
	private void drawCommand(int amount)
	{
		int[] command = new int[] {command1, command2, command3};
		for(int i = 0; i < amount; i++)
		{
			switch(command[i])
			{
				case 0:
					this.drawTexturedModalRect(this.width/2 +105, this.height / 2 - 100+35*i, 226, 0, 30, 30);
					break;
				case 1:
					this.drawTexturedModalRect(this.width/2 +105, this.height / 2 - 100+35*i, 226, 32, 30, 30);
					break;
				case 2:
					this.drawTexturedModalRect(this.width/2 +105, this.height / 2 - 100+35*i, 226, 63, 30, 30);
					break;
				case 3: 
					this.drawTexturedModalRect(this.width/2 +105, this.height / 2 - 100+35*i, 226, 94, 30, 30);
					break;
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
	    return false;
	}
	
	@Override
	public void initGui() {
		this.buttonList.clear();
		this.labelList.clear();
		GrailWarPlayerTracker track = GrailWarPlayerTracker.get(this.mc.world);
		if(currentPage == 0)
		{
			this.buttonList.add(attack = new GuiButton(0, this.width / 2+10 , this.height / 2 - 82, 80, 20, "Attack"));
			this.buttonList.add(move = new GuiButton(0, this.width / 2+10 , this.height / 2 - 52, 80, 20, "Movement"));
			this.buttonList.add(truce = new GuiButton(0, this.width / 2+10 , this.height / 2 - 22, 80, 20, "Truce (WIP)"));
			this.buttonList.add(kill = new GuiButton(0, this.width / 2+10 , this.height / 2 + 8, 80, 20, "Kill Servant"));
			this.buttonList.add(forfeit = new GuiButton(0, this.width / 2+10 , this.height / 2 + 38, 80, 20, "Give up"));
		}
		else if (currentPage == 1)
		{
			this.buttonList.add(back = new GuiButton(0, this.width / 2+10 , this.height / 2 - 82, 80, 20, "Back"));
			this.buttonList.add(aggressive = new GuiButton(0, this.width / 2+10 , this.height / 2 - 52, 80, 20, "Aggressive"));
			this.buttonList.add(normal = new GuiButton(0, this.width / 2+10, this.height / 2 - 22, 80, 20, "Normal"));
			this.buttonList.add(defensive = new GuiButton(0, this.width / 2+10 , this.height / 2 + 8, 80, 20, "Defensive"));
		}
		else if (currentPage == 2)
		{
			this.buttonList.add(back = new GuiButton(0, this.width / 2+10, this.height / 2 -82, 80, 20, "Back"));
			this.buttonList.add(follow = new GuiButton(0, this.width / 2+10 , this.height / 2 -52, 80, 20, "Follow"));
			this.buttonList.add(stay = new GuiButton(0, this.width / 2+10 , this.height / 2 - 22, 80, 20, "Stay"));
			this.buttonList.add(protect = new GuiButton(0, this.width / 2+10 , this.height / 2 + 8, 80, 20, "Protect"));
			this.buttonList.add(call = new GuiButton(0, this.width / 2+10 , this.height / 2 + 38, 80, 20, "Call"));
		}
		else if (currentPage == 3)
		{
			if(track!=null&& track.playerCount()>0)
			{
				this.playerButton = new GuiPlayerButton[track.playerCount()];
				for(int i = 0;i<track.playerCount();i++)
				{
					EntityPlayer player = track.getPlayer(i);
					if(player!=null)
					{
						String name = player.getName();				
						this.buttonList.add(this.playerButton[i] = new GuiPlayerButton(i, this.width / 2+10, this.height / 2 -82+(i)*20, 80, 20, name, player.getUniqueID().toString()));
					}
				}
			}
			this.buttonList.add(back = new GuiButton(0, this.width / 2-90, this.height / 2 -5, 80, 20, "Back"));
			this.buttonList.add(request = new GuiButton(0, this.width / 2-90 , this.height / 2 +25, 80, 20, "Request Truce"));
			this.buttonList.add(accept = new GuiButton(0, this.width / 2-90 , this.height / 2 +45, 80, 20, "Accept"));
			this.buttonList.add(remove = new GuiButton(0, this.width / 2-90 , this.height / 2 +65, 80, 20, "Remove"));
			request.enabled=false;
			accept.enabled=false;
			remove.enabled=false;
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == this.attack) {
			this.currentPage = 1;
			this.initGui();
	    }
		else if (button == this.move)
		{
			this.currentPage = 2;
			this.initGui();
		}
		else if (button == this.truce)
		{
			this.currentPage = 3;
			this.initGui();
		}
		else if (button == this.kill)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(7));
			//IPlayer capSync = guiPlayer.getCapability(PlayerCapProvider.PlayerCap, null);
			//capSync.setServant(Minecraft.getMinecraft().player, null);
		}
		else if (button == this.forfeit)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(8));
			PacketHandler.sendToServer(new MessageGui(2));
		}
		if(button == this.aggressive)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(1));
		}
		else if(button == this.normal)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(0));
		}
		else if(button == this.defensive)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(2));
		}
		else if(button == this.useNP)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(3));
		}
		if(button == this.follow)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(4));	
		}
		else if(button == this.stay)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(5));	
		}
		else if(button == this.protect)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(6));
		}
		else if(button == this.call)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(9));
		}
		if(button == this.request)
		{
			
		}
		else if(button == this.accept)
		{
			
		}
		else if(button == this.remove)
		{
			//IPlayer capSync = guiPlayer.getCapability(PlayerCapProvider.PlayerCap, null);
			//capSync.removePlayerTruce(player);
			
			
		}
		else if(button instanceof GuiPlayerButton)
		{
			GuiPlayerButton theButton = (GuiPlayerButton) button;
			if(!theButton.selected)
			{
				request.enabled=true;
				accept.enabled=true;
				remove.enabled=true;
				theButton.selected=true;
			}
			else
			{
				request.enabled=false;
				accept.enabled=false;
				remove.enabled=false;
				theButton.selected=false;
			}
		}
		if(button == this.back)
		{
			this.currentPage = 0;
			this.initGui();
		}
	}
	
	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
        {
            this.mc.player.closeScreen();
        }
    }
	
}
