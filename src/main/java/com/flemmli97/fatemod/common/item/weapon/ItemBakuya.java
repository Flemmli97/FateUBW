package com.flemmli97.fatemod.common.item.weapon;

import com.flemmli97.fatemod.Fate;
import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBakuya extends ItemSword{

    public static ToolMaterial bakuya_mat = EnumHelper.addToolMaterial("bakuya_mat", 0, 650, 0.0F, 1.0F, 24);

	public ItemBakuya() {
    	super(bakuya_mat);
    	setUnlocalizedName("bakuya");
    	setCreativeTab(Fate.customTab);
    	GameRegistry.register(this, new ResourceLocation(Fate.MODID, "bakuya"));
    }
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.remove(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.0, 0));
		}

		return multimap;
	}
	
	/*@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target,
			EnumHand hand) {
		if(!playerIn.worldObj.isRemote)
		{
			if(hand == EnumHand.OFF_HAND && EnumHand.MAIN_HAND != null)
			{
				if(playerIn.getHeldItemMainhand().getItem() == ModItems.kanshou )
				{
					target.attackEntityFrom(DamageSource.causePlayerDamage(playerIn), 5.0F);
					System.out.println("test");
				}
			}
		}
		return super.itemInteractionForEntity(stack, playerIn, target, hand);
	}*/

	@SideOnly(Side.CLIENT)
	    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }
}
