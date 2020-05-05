package com.flemmli97.fatemod.client.gui;

import java.util.UUID;

import com.flemmli97.fatemod.common.handler.TruceMapHandler;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;


public class GuiPlayerButton extends GuiButton{

	public boolean selected;
	private String uuid;
	protected static final ResourceLocation guiStuff = new ResourceLocation(LibReference.MODID, "textures/gui/player_button.png");
	private State state;
	public GuiPlayerButton(int buttonId, int x, int y, String buttonText, String uuid, State state) {
		super(buttonId, x, y, 89, 20, buttonText);
		this.uuid=uuid;
		this.state=state;
	}
	
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(guiStuff);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.enabled?2:this.getHoverState(this.hovered);
            int state = 0;
            switch(this.state)
            {
				case NONE: state = 0;
					break;
				case PENDING: state = 1;
					break;
				case REQUESTED: state = 2;
					break;
				case TRUCE: state = 3;
					break;
            }
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, state*40 + i * 20-20, this.width, this.height);
            //this.drawTexturedModalRect(this.x + this.width / 2, this.y, 256 - this.width / 2, state*20+i * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (this.packedFGColour != 0)
            {
                j = this.packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
    }
	
	public String getUUID()
	{
		return this.uuid;
	}
	
	public State getState()
	{
		return this.state;
	}
	
	public void refreshState()
	{
		this.state=State.getState(Minecraft.getMinecraft().player, UUID.fromString(this.uuid));
	}
	
	public static enum State
	{
		NONE,
		PENDING,
		REQUESTED,
		TRUCE;
		
		public static State getState(EntityPlayer player, UUID uuid)
		{
			TruceMapHandler truces = TruceMapHandler.get(player.world);
			if(truces.getRequests(player).contains(uuid))
				return PENDING;
			if(truces.hasRequest(uuid, player))
				return REQUESTED;
			if(truces.playerTruces(player).contains(uuid))
				return TRUCE;
			return NONE;
		}
	}
}
