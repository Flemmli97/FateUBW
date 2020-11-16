package com.flemmli97.fate.client.gui;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.capability.IPlayer;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.fate.common.utils.EnumServantUpdate;
import com.flemmli97.fate.common.utils.Utils;
import com.flemmli97.fate.common.world.GrailWarHandler;
import com.flemmli97.fate.network.C2SMessageGui;
import com.flemmli97.fate.network.C2SServantCommand;
import com.flemmli97.fate.network.PacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class CommandGui extends Screen {

	private Pages currentPage = Pages.MENU;
	private int trucePage = 0;
	private Random rand = new Random();
	private int command1 = this.rand.nextInt(3);
	private int command2 = this.rand.nextInt(3);
	private int command3 = this.rand.nextInt(3);
	//private GuiPlayerButton[] playerButton;

	private final static ResourceLocation guiBackGround = new ResourceLocation(Fate.MODID, "textures/gui/command_gui_1.png");
	private final static ResourceLocation guiTruce = new ResourceLocation(Fate.MODID, "textures/gui/command_gui_2.png");

	public CommandGui() {
		super(new TranslationTextComponent("fate.gui.command"));
		PacketHandler.sendToServer(new C2SMessageGui(C2SMessageGui.Type.ALL));
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		PlayerEntity player = this.getMinecraft().player;
		IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap).orElse(null);
		EntityServant servant = capSync.getServant(player);
		if (capSync == null)
			return;
		if (this.currentPage != Pages.TRUCE) {
			this.getMinecraft().getTextureManager().bindTexture(guiBackGround);
			this.drawTexture(stack, this.width / 2 - 100, this.height / 2 - 100, 0, 0, 201, 210);
			this.drawCommand(stack, capSync.getCommandSeals());

			this.getMinecraft().fontRenderer.draw(stack, "§4Name:", this.width / 2 - 90, this.height / 2 - 5, 1);
			this.getMinecraft().fontRenderer.draw(stack, "§4Damage:", this.width / 2 - 90, this.height / 2 + 15, 1);
			this.getMinecraft().fontRenderer.draw(stack, "§4Armor:", this.width / 2 - 90, this.height / 2 + 35, 1);
			this.getMinecraft().fontRenderer.draw(stack, "§4Nobel Phantasm:", this.width / 2 - 90, this.height / 2 + 55, 1);
			if (servant != null) {
				this.getMinecraft().fontRenderer.draw(stack, servant.getRealName(), this.width / 2 - 90, this.height / 2 + 5, 1);
				this.getMinecraft().fontRenderer.draw(stack, String.valueOf(servant.props().strength()), this.width / 2 - 90, this.height / 2 + 25, 1);
				this.getMinecraft().fontRenderer.draw(stack, String.valueOf(servant.props().armor()), this.width / 2 - 90, this.height / 2 + 45, 1);
				this.getMinecraft().fontRenderer.draw(stack, servant.nobelPhantasm(), this.width / 2 - 90, this.height / 2 + 65, 1);
			}
		} else {
			this.getMinecraft().getTextureManager().bindTexture(guiTruce);
			this.drawTexture(stack, this.width / 2 - 100, this.height / 2 - 100, 0, 0, 201, 210);
			this.drawCommand(stack, capSync.getCommandSeals());
		}
		if (servant != null) {
			float mouseXNew = (float) ((this.width - 200) / 2 + 51) - mouseX;
			float mouseYNew = (float) ((this.height - 180) / 2 + 75 - 50) - mouseY;
			InventoryScreen.drawEntity(this.width / 2 - 50, this.height / 2 - 20, 29, mouseXNew, mouseYNew, servant);
		}
		super.render(stack, mouseX, mouseY, partialTicks);
	}

	private void drawCommand(MatrixStack stack, int amount) {
		int[] command = new int[]{this.command1, this.command2, this.command3};
		for (int i = 0; i < amount; i++) {
			switch (command[i]) {
				case 0:
					this.drawTexture(stack, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 226, 0, 30, 30);
					break;
				case 1:
					this.drawTexture(stack, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 226, 32, 30, 30);
					break;
				case 2:
					this.drawTexture(stack, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 226, 63, 30, 30);
					break;
				case 3:
					this.drawTexture(stack, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 226, 94, 30, 30);
					break;
			}
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	protected void init() {
		super.init();
		if (this.currentPage == Pages.MENU) {
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
					, new TranslationTextComponent("fate.gui.command.attack"), b -> {
				this.currentPage = Pages.ATTACK;
				this.init(this.client, this.width, this.height);
			}));
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 52, 80, 20
					, new TranslationTextComponent("fate.gui.command.movement"), b -> {
				this.currentPage = Pages.MOVEMENT;
				this.init(this.client, this.width, this.height);
			}));
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 22, 80, 20
					, new TranslationTextComponent("fate.gui.command.truce"), b -> {
				this.currentPage = Pages.TRUCE;
				this.init(this.client, this.width, this.height);
			}));
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 + 8, 80, 20
					, new TranslationTextComponent("fate.gui.command.kill"), b -> {
				PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.KILL));
				PacketHandler.sendToServer(new C2SMessageGui(C2SMessageGui.Type.SERVANT));
			}));
			EntityServant servant = Utils.getServant(this.getMinecraft().player);
			if (servant != null) {
				if (servant.specialCommands() != null)
					this.addButton(new Button(this.width / 2 + 10, this.height / 2 + 38, 80, 20
							, new TranslationTextComponent("fate.gui.command.special"), b -> {
						this.currentPage = Pages.SPECIAL;
						this.init(this.client, this.width, this.height);
					}));
			}
		} else if (this.currentPage == Pages.ATTACK) {
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
					, new TranslationTextComponent("fate.gui.command.back"), b -> {
				this.currentPage = Pages.MENU;
				this.trucePage = 0;
				this.init(this.client, this.width, this.height);
			}));
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 52, 80, 20
					, new TranslationTextComponent("fate.gui.command.aggressive"), b -> {
				PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.AGGRESSIVE));
			}));
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 22, 80, 20
					, new TranslationTextComponent("fate.gui.command.normal"), b -> {
				PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.NORMAL));
			}));
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 + 8, 80, 20
					, new TranslationTextComponent("fate.gui.command.defensive"), b -> {
				PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.DEFENSIVE));
			}));
		} else if (this.currentPage == Pages.MOVEMENT) {
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
					, new TranslationTextComponent("fate.gui.command.back"), b -> {
				this.currentPage = Pages.MENU;
				this.trucePage = 0;
				this.init(this.client, this.width, this.height);
			}));
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 52, 80, 20
					, new TranslationTextComponent("fate.gui.command.follow"), b -> {
				PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.FOLLOW));
			}));
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 22, 80, 20
					, new TranslationTextComponent("fate.gui.command.stay"), b -> {
				PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.STAY));
			}));
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 + 8, 80, 20
					, new TranslationTextComponent("fate.gui.command.protect"), b -> {
				PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.GUARD));
			}));
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 + 38, 80, 20
					, new TranslationTextComponent("fate.gui.command.call"), b -> {
				PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.TELEPORT));
			}));
		} else if (this.currentPage == Pages.SPECIAL) {
			this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
					, new TranslationTextComponent("fate.gui.command.back"), b -> {
				this.currentPage = Pages.MENU;
				this.trucePage = 0;
				this.init(this.client, this.width, this.height);
			}));
			EntityServant servant = Utils.getServant(this.getMinecraft().player);
			if (servant != null)
				for (int i = 0; i < servant.specialCommands().length; i++)
					this.addButton(new ButtonSpecial(this.width / 2 + 10, this.height / 2 - 52, 80, 20, servant.specialCommands()[i]));
		} else if (this.currentPage == Pages.TRUCE) {
			/*Set<Entry<UUID, String>> players = GrailWarHandler.get(this.mc.world).players();
			if(GrailWarHandler.get(this.mc.world).containsPlayer(player))
			{
				this.playerButton = new GuiPlayerButton[players.size()-1];
				int i = 0;
				for(Entry<UUID, String> entry : players)
				{					
					if(!entry.getKey().equals(player.getUniqueID()))
					{
						this.playerButton[i] = 
								new GuiPlayerButton(i,this.width / 2+4, this.height / 2 -82+(i%7)*20, entry.getValue(), entry.getKey().toString(), State.getState(player, entry.getKey()));
						++i;
						
					}
				}
				for(int j = 0; j < 7; j++)
				{
					int index = this.trucePage*7+j;
					if(index<this.playerButton.length)
					{
						this.addButton(this.playerButton[index]);
					}
				}
			}
			this.addButton(this.next=new ButtonTrucePage(0,this.width / 2+48,this.height / 2+62,true));
			this.addButton(this.prev=new ButtonTrucePage(0,this.width / 2+4,this.height / 2+62,false));

			this.addButton(this.back = new Button(this.width / 2-90, this.height / 2 -5, 80, 20, "Back"));
			this.addButton(this.request = new GuiStringButton(0, this.width / 2-90 , this.height / 2 +25, 80, 20, "Request Truce"));
			this.addButton(this.accept = new GuiStringButton(0, this.width / 2-90 , this.height / 2 +45, 80, 20, "Accept"));
			this.addButton(this.remove = new GuiStringButton(0, this.width / 2-90 , this.height / 2 +65, 80, 20, "Remove"));
			this.request.enabled=false;
			this.accept.enabled=false;
			this.remove.enabled=false;*/
		}
	}

	/*
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == this.attack) {
			this.currentPage = Pages.ATTACK;
			this.initGui();
		}
		if (button == this.move) {
			this.currentPage = Pages.MOVEMENT;
			this.initGui();
		}
		if (button == this.truce) {
			this.currentPage = Pages.TRUCE;
			this.initGui();
		}
		if (button == this.special) {
			this.currentPage = Pages.SPECIAL;
			this.initGui();
		}
		if (button == this.kill) {
			PacketHandler.sendToServer(new MessagePlayerServant(7));
			PacketHandler.sendToServer(new MessageGui(2));
		}
		else if (button == this.forfeit)
		{
			PacketHandler.sendToServer(new MessagePlayerServant(8));
		}
		if (button == this.aggressive) {
			PacketHandler.sendToServer(new MessagePlayerServant(1));
		}
		if (button == this.normal) {
			PacketHandler.sendToServer(new MessagePlayerServant(0));
		}
		if (button == this.defensive) {
			PacketHandler.sendToServer(new MessagePlayerServant(2));
		}
		if (button == this.useNP) {
			PacketHandler.sendToServer(new MessagePlayerServant(3));
		}
		if (button == this.follow) {
			PacketHandler.sendToServer(new MessagePlayerServant(4));
		}
		if (button == this.stay) {
			PacketHandler.sendToServer(new MessagePlayerServant(5));
		}
		if (button == this.protect) {
			PacketHandler.sendToServer(new MessagePlayerServant(6));
		} else if (button == this.call) {
			PacketHandler.sendToServer(new MessagePlayerServant(9));
		}
		if (button == this.request) {
			GuiStringButton req = (GuiStringButton) button;
			if (!req.getSavedString().isEmpty()) {
				PacketHandler.sendToServer(new MessageTruce(0, req.getSavedString()));
				this.initGui();
			}
		}
		if (button == this.accept) {
			GuiStringButton req = (GuiStringButton) button;
			if (!req.getSavedString().isEmpty()) {
				PacketHandler.sendToServer(new MessageTruce(1, req.getSavedString()));
				this.initGui();
			}
		}
		if (button == this.remove) {
			GuiStringButton req = (GuiStringButton) button;
			if (!req.getSavedString().isEmpty()) {
				PacketHandler.sendToServer(new MessageTruce(2, req.getSavedString()));
				this.initGui();
			}
		}
		if (button instanceof GuiPlayerButton) {
			GuiPlayerButton theButton = (GuiPlayerButton) button;
			if (!theButton.selected) {
				this.request.enabled = theButton.getState() == State.NONE;
				this.accept.enabled = theButton.getState() == State.PENDING;
				this.remove.enabled = theButton.getState() == State.TRUCE;
				theButton.selected = !(theButton.getState() == State.REQUESTED);
				switch (theButton.getState()) {
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
			} else {
				this.request.enabled = false;
				this.accept.enabled = false;
				this.remove.enabled = false;
				this.request.setSavedString("");
				this.accept.setSavedString("");
				this.remove.setSavedString("");
				theButton.selected = false;
			}
		}
		if (button instanceof ButtonSpecial) {
			((ButtonSpecial) button).onPressed();
		}
		if (button == this.next) {
			if (this.playerButton != null && this.trucePage < this.playerButton.length / 7) {
				++this.trucePage;
				this.initGui();
			}
		}
		if (button == this.prev) {
			if (this.trucePage > 0) {
				--this.trucePage;
				this.initGui();
			}
		}
		if (button == this.back) {
			this.currentPage = Pages.MENU;
			this.trucePage = 0;
			this.initGui();
		}
	}	*/

	/*public void updatePlayerButtons()
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
	}*/

	private enum Pages {
		MENU,
		MOVEMENT,
		ATTACK,
		SPECIAL,
		TRUCE;
	}
}