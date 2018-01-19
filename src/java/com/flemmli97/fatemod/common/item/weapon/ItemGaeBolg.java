package com.flemmli97.fatemod.common.item.weapon;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityGaeBolg;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGaeBolg extends ClassSpear {

    public static ToolMaterial gaebolg_mat = EnumHelper.addToolMaterial("gaebolg_mat", 0, 900, 0.0F, 3.5F, 14);

    public ItemGaeBolg() {
    	super(gaebolg_mat, -1.5F);
    	setUnlocalizedName("gae_bolg");
    	setCreativeTab(Fate.customTab);
    	GameRegistry.register(this, new ResourceLocation(Fate.MODID, "gae_bolg"));
    }
    
    @Override
	public ActionResult <ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote)
		{
			EntityGaeBolg gaeBolg = new EntityGaeBolg(world, player);			     
			gaeBolg.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0, 1.5F, 0);	
			IPlayer mana = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(player.capabilities.isCreativeMode)
			{
				world.spawnEntityInWorld(gaeBolg);
			}
			else 
			{
				if (mana.useMana(player, 15))
				{
					world.spawnEntityInWorld(gaeBolg);
					stack.stackSize--;
				}
				else
				{
					player.addChatMessage(new TextComponentString(TextFormatting.AQUA + "You don't have enough mana"));
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
    
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
