package com.flemmli97.fatemod.common.item.weapon;

import java.util.List;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityArcherArrow;
import com.flemmli97.fatemod.common.entity.EntityCaladBolg;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEmiyaBow extends ItemBow{
	
	private boolean charged = false;
	
	public ItemEmiyaBow() {
    	super();
    	setUnlocalizedName("archer_bow");
    	setCreativeTab(Fate.customTab);
    	GameRegistry.register(this, new ResourceLocation(Fate.MODID, "archer_bow"));
    }
	   
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if(this.charged)
		{
			tooltip.add(TextFormatting.AQUA + "Charged");
		}
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		 if(!charged && !entityLiving.worldObj.isRemote)
		 {
			EntityPlayer entityplayer = (EntityPlayer)entityLiving;
			IPlayer mana = entityplayer.getCapability(PlayerCapProvider.PlayerCap, null);
			if(mana.useMana(entityplayer, 80)|| entityplayer.capabilities.isCreativeMode)
         	{
				charged = true;
         	}
		 }
		 return super.onEntitySwing(entityLiving, stack);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if(charged)
		{
			this.spawnCaladBolg(worldIn, entityLiving, stack, timeLeft);
		}
		else
		{
			this.spawnNormalArrow(stack, worldIn, entityLiving, timeLeft);
		}
	}
	
	public void spawnCaladBolg(World world, EntityLivingBase entityLiving, ItemStack stack, int timeLeft)
	{
		EntityCaladBolg bolg = new EntityCaladBolg(world, entityLiving);
		if(!world.isRemote)
		{
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
			float f = getArrowVelocity(i);
            if ((double)f >= 0.1D)
            {
	            bolg.setHeadingFromThrower(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0, f*2, 0);
	            world.spawnEntityInWorld(bolg);
	            charged=false;
            }
		}
	}
	
	public void spawnNormalArrow(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            boolean flag = entityplayer.capabilities.isCreativeMode;
			IPlayer mana = entityplayer.getCapability(PlayerCapProvider.PlayerCap, null);

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            if (i < 0) return;

            if (mana.getMana() > 5 || flag)
            {

                float f = getArrowVelocity(i);

                if ((double)f >= 0.1D)
                {
                	EntityArcherArrow arrow = new EntityArcherArrow(worldIn, entityplayer);
                    if (!worldIn.isRemote)
                    {             
                        arrow.setAim(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);                     

                        if (f == 1.0F)
                        {
                        	arrow.setIsCritical(true);
                        }
                        
                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                        if (j > 0)
                        {
                        	arrow.setDamage(arrow.getDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                        if (k > 0)
                        {
                        	arrow.setKnockbackStrength(k);
                        }

                        stack.damageItem(1, entityplayer);

                        worldIn.spawnEntityInWorld(arrow);
                        if(!flag)
                        mana.useMana(entityplayer, 5);
                    }

                    worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    entityplayer.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
	}
	
	

	@Override
	public boolean hasEffect(ItemStack stack) {
		return this.charged ||super.hasEffect(stack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
			IPlayer mana = playerIn.getCapability(PlayerCapProvider.PlayerCap, null);
			if (playerIn.capabilities.isCreativeMode || mana.getMana()>10 || charged)
	        {
				playerIn.setActiveHand(hand);
	            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	        }
			else
			{
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
			}
	}

	@SideOnly(Side.CLIENT)
	    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }

}
