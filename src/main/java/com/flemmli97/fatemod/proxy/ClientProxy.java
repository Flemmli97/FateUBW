package com.flemmli97.fatemod.proxy;

import org.lwjgl.input.Keyboard;

import com.flemmli97.fatemod.client.gui.CommandGui;
import com.flemmli97.fatemod.client.render.MultiItemColor;
import com.flemmli97.fatemod.client.render.servant.RenderServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.init.ModRender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy {
	
	public static KeyBinding command = new KeyBinding("fate.command", Keyboard.KEY_H, "key.categories.misc");
	public static KeyBinding special = new KeyBinding("fate.special", Keyboard.KEY_V, "key.categories.misc");
	public static KeyBinding boost = new KeyBinding("fate.boost", Keyboard.KEY_G, "key.categories.misc");

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        ModRender.registerRenderers();
        ModRender.registerParticles();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        MultiItemColor color = new MultiItemColor();
		ItemColors itemcolors = FMLClientHandler.instance().getClient().getItemColors();
		itemcolors.registerItemColorHandler(color, ModItems.spawnEgg);
        ClientRegistry.registerKeyBinding(command);
        ClientRegistry.registerKeyBinding(special);
        ClientRegistry.registerKeyBinding(boost);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        
    }
    
    @Override
	public IThreadListener getListener(MessageContext ctx) {
    	return ctx.side.isClient() ? Minecraft.getMinecraft() : super.getListener(ctx);
	}

	@Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {

     return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
    }
	
	@Override
    public String entityName(EntityServant servant)
    {
		if(!RenderServant.showIdentity(servant))
			return I18n.format("servant.unknown");
    	return super.entityName(servant);
    }
	
	@Override
    public void updateGuiTruce()
    {
    	Gui gui = Minecraft.getMinecraft().currentScreen;
    	if(gui instanceof CommandGui)
    	{
    		((CommandGui)gui).updatePlayerButtons();
    	}
    }
}