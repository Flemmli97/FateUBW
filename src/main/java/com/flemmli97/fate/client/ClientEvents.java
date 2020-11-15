package com.flemmli97.fate.client;

import com.flemmli97.fate.Fate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Fate.MODID, value = Dist.CLIENT)
public class ClientEvents {

    /*
    @SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void keyEvent(KeyInputEvent event)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		World world = Minecraft.getMinecraft().world;
		if(ClientProxy.command.isPressed())
		{
        	player.openGui(Fate.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
		if(ClientProxy.special.isPressed())
		{
			PacketHandler.sendToServer(new MessagePlayerServant(3));
		}
		if(ClientProxy.boost.isPressed())
		{
			PacketHandler.sendToServer(new MessagePlayerServant(10));
		}
		if(ClientProxy.target.isPressed())
		{
			PacketHandler.sendToServer(new MessagePlayerServant(11));
		}
    }

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderManaBar(RenderGameOverlayEvent.Post event)
	{
		if (event.isCancelable() || event.getType() != ElementType.EXPERIENCE)return;
		{
			new ManaBar(Minecraft.getMinecraft());
		}
	}
     */
}
