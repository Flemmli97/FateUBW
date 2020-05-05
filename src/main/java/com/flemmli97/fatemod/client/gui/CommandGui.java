package com.flemmli97.fatemod.client.gui;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.flemmli97.fatemod.client.gui.GuiPlayerButton.State;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.GrailWarHandler;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.fatemod.network.MessageGui;
import com.flemmli97.fatemod.network.MessagePlayerServant;
import com.flemmli97.fatemod.network.MessageTruce;
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
	private GuiButton attack,aggressive,normal,defensive,useNP,move,follow,stay,protect,truce,kill,back,call,special;
	private ButtonTrucePage next, prev;
	private GuiStringButton accept, remove, request;
	private Pages currentPage = Pages.MENU;
	private int trucePage = 0;
	private Random rand = new Random();
	private int command1 = this.rand.nextInt(3);
	private int command2 = this.rand.nextInt(3);
	private int command3 = this.rand.nextInt(3);
	private GuiPlayerButton[] playerButton;

	private final static ResourceLocation guiBackGround = new ResourceLocation(LibReference.MODID, "textures/gui/command_gui_1.png");
	private final static ResourceLocation guiTruce = new ResourceLocation(LibReference.MODID, "textures/gui/command_gui_2.png");
	
	public CommandGui(EntityPlayer player)
	{
		this.guiPlayer = player;	
		PacketHandler.sendToServer(new MessageGui(1));
		PacketHandler.sendToServer(new MessageGui(2));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		IPlayer capSync = this.guiPlayer.getCapability(PlayerCapProvider.PlayerCap, null);
		if(capSync.getServant(this.guiPlayer)!=null)
		{
			float mouseXNew =(float)((this.width - 200) / 2 + 51) - mouseX;
			float mouseYNew = (float)((this.height - 180) / 2 + 75 - 50) - mouseY;
			GuiInventory.drawEntityOnScreen(this.width/2 -50, this.height / 2 -20, 29, mouseXNew, mouseYNew, capSync.getServant(this.guiPlayer));
		}
		this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
		if(this.currentPage !=Pages.TRUCE)
		{
			this.mc.getTextureManager().bindTexture(guiBackGround);
			this.drawTexturedModalRect(this.width/2 -100, this.height / 2 - 100, 0, 0, 201, 210);
			this.drawCommand(capSync.getCommandSeals());

			this.mc.fontRenderer.drawString("§4Name:", this.width/2 -90, this.height / 2 - 5, 1);
			this.mc.fontRenderer.drawString("§4Damage:", this.width/2 -90, this.height / 2 +15, 1);
			this.mc.fontRenderer.drawString("§4Armor:", this.width/2 -90, this.height / 2 + 35, 1);
			this.mc.fontRenderer.drawString("§4Nobel Phantasm:", this.width/2 -90, this.height / 2 + 55, 1);
			if(capSync.getServant(this.guiPlayer)!=null)
			{
				this.mc.fontRenderer.drawString(I18n.format(capSync.getServant(this.guiPlayer).getName()), this.width/2 -90, this.height / 2 +5, 1);
				this.mc.fontRenderer.drawString(String.valueOf(capSync.getServant(this.guiPlayer).props().strength()), this.width/2 -90, this.height / 2 +25, 1);
				this.mc.fontRenderer.drawString(String.valueOf(capSync.getServant(this.guiPlayer).props().armor()), this.width/2 -90, this.height / 2 + 45, 1);
				this.mc.fontRenderer.drawString(capSync.getServant(this.guiPlayer).nobelPhantasm(), this.width/2 -90, this.height / 2 + 65, 1);
			}
		}
		else
		{
			this.mc.getTextureManager().bindTexture(guiTruce);
			this.drawTexturedModalRect(this.width/2 -100, this.height / 2 - 100, 0, 0, 201, 210);
			this.drawCommand(capSync.getCommandSeals());
		}
		super.drawScreen(mouseX, mouseY, partialTicks);

	}
	
	private void drawCommand(int amount)
	{
		int[] command = new int[] {this.command1, this.command2, this.command3};
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
		if(this.currentPage == Pages.MENU)
		{
			this.buttonList.add(this.attack = new GuiButton(0, this.width / 2+10 , this.height / 2 - 82, 80, 20, "Attack"));
			this.buttonList.add(this.move = new GuiButton(0, this.width / 2+10 , this.height / 2 - 52, 80, 20, "Movement"));
			this.buttonList.add(this.truce = new GuiButton(0, this.width / 2+10 , this.height / 2 - 22, 80, 20, "Truce"));
			this.buttonList.add(this.kill = new GuiButton(0, this.width / 2+10 , this.height / 2 + 8, 80, 20, "Kill Servant"));
			if(this.guiPlayer.getCapability(PlayerCapProvider.PlayerCap, null).getServant(this.guiPlayer)!=null)
			{
				if(this.guiPlayer.getCapability(PlayerCapProvider.PlayerCap, null).getServant(this.guiPlayer).specialCommands()!=null)
					this.buttonList.add(this.special = new GuiButton(0, this.width / 2+10 , this.height / 2 + 38, 80, 20, "Special"));
			}
		}
		else if (this.currentPage == Pages.ATTACK)
		{
			this.buttonList.add(this.back = new GuiButton(0, this.width / 2+10 , this.height / 2 - 82, 80, 20, "Back"));
			this.buttonList.add(this.aggressive = new GuiButton(0, this.width / 2+10 , this.height / 2 - 52, 80, 20, "Aggressive"));
			this.buttonList.add(this.normal = new GuiButton(0, this.width / 2+10, this.height / 2 - 22, 80, 20, "Normal"));
			this.buttonList.add(this.defensive = new GuiButton(0, this.width / 2+10 , this.height / 2 + 8, 80, 20, "Defensive"));
		}
		else if (this.currentPage == Pages.MOVEMENT)
		{
			this.buttonList.add(this.back = new GuiButton(0, this.width / 2+10, this.height / 2 -82, 80, 20, "Back"));
			this.buttonList.add(this.follow = new GuiButton(0, this.width / 2+10 , this.height / 2 -52, 80, 20, "Follow"));
			this.buttonList.add(this.stay = new GuiButton(0, this.width / 2+10 , this.height / 2 - 22, 80, 20, "Stay"));
			this.buttonList.add(this.protect = new GuiButton(0, this.width / 2+10 , this.height / 2 + 8, 80, 20, "Protect"));
			this.buttonList.add(this.call = new GuiButton(0, this.width / 2+10 , this.height / 2 + 38, 80, 20, "Call"));
		}
		else if(this.currentPage == Pages.SPECIAL)
		{
			this.buttonList.add(this.back = new GuiButton(0, this.width / 2+10, this.height / 2 -82, 80, 20, "Back"));
			EntityServant servant = this.guiPlayer.getCapability(PlayerCapProvider.PlayerCap, null).getServant(this.guiPlayer);
			for(int i = 0; i < servant.specialCommands().length; i++)
				this.buttonList.add(new ButtonSpecial(0, this.width / 2+10 , this.height / 2 -52, 80, 20, servant.specialCommands()[i]));
		}
		else if (this.currentPage == Pages.TRUCE)
		{
			Set<Entry<UUID, String>> players = GrailWarHandler.get(this.mc.world).players();
			if(GrailWarHandler.get(this.mc.world).containsPlayer(this.guiPlayer))
			{
				this.playerButton = new GuiPlayerButton[players.size()-1];
				int i = 0;
				for(Entry<UUID, String> entry : players)
				{					
					if(!entry.getKey().equals(this.guiPlayer.getUniqueID()))
					{
						this.playerButton[i] = 
								new GuiPlayerButton(i,this.width / 2+4, this.height / 2 -82+(i%7)*20, entry.getValue(), entry.getKey().toString(), State.getState(this.guiPlayer, entry.getKey()));
						++i;
						
					}
				}
				for(int j = 0; j < 7; j++)
				{
					int index = this.trucePage*7+j;
					if(index<this.playerButton.length)
					{
						this.buttonList.add(this.playerButton[index]);
					}
				}
			}
			this.buttonList.add(this.next=new ButtonTrucePage(0,this.width / 2+48,this.height / 2+62,true));
			this.buttonList.add(this.prev=new ButtonTrucePage(0,this.width / 2+4,this.height / 2+62,false));

			this.buttonList.add(this.back = new GuiButton(0, this.width / 2-90, this.height / 2 -5, 80, 20, "Back"));
			this.buttonList.add(this.request = new GuiStringButton(0, this.width / 2-90 , this.height / 2 +25, 80, 20, "Request Truce"));
			this.buttonList.add(this.accept = new GuiStringButton(0, this.width / 2-90 , this.height / 2 +45, 80, 20, "Accept"));
			this.buttonList.add(this.remove = new GuiStringButton(0, this.width / 2-90 , this.height / 2 +65, 80, 20, "Remove"));
			this.request.enabled=false;
			this.accept.enabled=false;
			this.remove.enabled=false;
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == this.attack) {
			this.currentPage = Pages.ATTACK;
			this.initGui();
	    }
		if (button == this.move)
		{
			this.currentPage = Pages.MOVEMENT;
			this.initGui();
		}
		if (button == this.truce)
		{
			this.currentPage = Pages.TRUCE;
			this.initGui();
		}
		if(button == this.special)
		{
			this.currentPage = Pages.SPECIAL;
			this.initGui();
		}
		if (button == this.kill)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(7));
			PacketHandler.sendToServer(new MessageGui(2));
		}
		/*else if (button == this.forfeit)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(8));
		}*/
		if(button == this.aggressive)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(1));
		}
		if(button == this.normal)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(0));
		}
		if(button == this.defensive)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(2));
		}
		if(button == this.useNP)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(3));
		}
		if(button == this.follow)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(4));	
		}
		if(button == this.stay)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(5));	
		}
		if(button == this.protect)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(6));
		}
		else if(button == this.call)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(9));
		}
		if(button == this.request)
		{
			GuiStringButton req = (GuiStringButton) button;
			if(!req.getSavedString().isEmpty())
			{
				PacketHandler.sendToServer(new MessageTruce(0, req.getSavedString()));
				this.initGui();
			}
		}
		if(button == this.accept)
		{
			GuiStringButton req = (GuiStringButton) button;
			if(!req.getSavedString().isEmpty())
			{
				PacketHandler.sendToServer(new MessageTruce(1, req.getSavedString()));
				this.initGui();
			}
		}
		if(button == this.remove)
		{
			GuiStringButton req = (GuiStringButton) button;
			if(!req.getSavedString().isEmpty())
			{
				PacketHandler.sendToServer(new MessageTruce(2, req.getSavedString()));
				this.initGui();
			}
		}
		if(button instanceof GuiPlayerButton)
		{
			GuiPlayerButton theButton = (GuiPlayerButton) button;
			if(!theButton.selected)
			{
                this.request.enabled=theButton.getState()==State.NONE;
                this.accept.enabled=theButton.getState()==State.PENDING;
                this.remove.enabled=theButton.getState()==State.TRUCE;
				theButton.selected=!(theButton.getState()==State.REQUESTED);
				switch(theButton.getState())
				{
				case NONE:
                    this.request.setSavedString(theButton.getUUID());
					break;
				case PENDING:
                    this.accept.setSavedString(theButton.getUUID());
					break;
				case TRUCE:
                    this.remove.setSavedString(theButton.getUUID());
					break;
				default:
					break;	
				}
			}
			else
			{
                this.request.enabled=false;
                this.accept.enabled=false;
                this.remove.enabled=false;
                this.request.setSavedString("");
                this.accept.setSavedString("");
                this.remove.setSavedString("");
				theButton.selected=false;
			}
		}
		if(button instanceof ButtonSpecial)
		{
			((ButtonSpecial)button).onPressed();
		}
		if(button==this.next)
		{
			if(this.playerButton!=null && this.trucePage<this.playerButton.length/7)
			{
				++this.trucePage;
				this.initGui();
			}
		}
		if(button==this.prev)
		{
			if(this.trucePage>0)
			{
				--this.trucePage;
				this.initGui();
			}
		}
		if(button == this.back)
		{
			this.currentPage = Pages.MENU;
			this.trucePage=0;
			this.initGui();
		}
	}	
	
	/**
	 * Just stops from processing buttons if one was pressed already.
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (mouseButton == 0)
        {
            for (int i = 0; i < this.buttonList.size(); ++i)
            {
                GuiButton guibutton = this.buttonList.get(i);
                
                if (guibutton.mousePressed(this.mc, mouseX, mouseY))
                {
                    net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre event = new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre(this, guibutton, this.buttonList);
                    if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
                        break;
                    guibutton = event.getButton();
                    this.selectedButton = guibutton;
                    guibutton.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(guibutton);
                    if (this.equals(this.mc.currentScreen))
                        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post(this, event.getButton(), this.buttonList));
                    return;
                }
            }
        }
    }
	
	public void updatePlayerButtons()
	{
		if(this.playerButton==null)
			return;
		for(GuiPlayerButton button : this.playerButton)
		{
			button.refreshState();
			button.selected=false;
		}
        this.request.enabled=false;
        this.accept.enabled=false;
        this.remove.enabled=false;
        this.request.setSavedString("");
        this.accept.setSavedString("");
        this.remove.setSavedString("");
	}
	
	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
        {
            this.mc.player.closeScreen();
        }
    }
	
	private static enum Pages
	{
		MENU,
		MOVEMENT,
		ATTACK,
		SPECIAL,
		TRUCE;
	}
}
