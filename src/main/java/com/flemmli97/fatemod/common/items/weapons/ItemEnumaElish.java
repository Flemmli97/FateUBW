package com.flemmli97.fatemod.common.items.weapons;

import javax.annotation.Nullable;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.common.util.EnumHelper;

public class ItemEnumaElish extends ItemSword{

    public static ToolMaterial enumaelish_mat = EnumHelper.addToolMaterial("enumaelish_mat", 0, 1500, 0.0F, 4.0F, 10);

    public ItemEnumaElish() {
    	super(enumaelish_mat);
    	this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "enuma_elish"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
    
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new CapProvider();
    }

    private static class CapProvider implements ICapabilityProvider
    {
    	private final IAnimationStateMachine asm = Fate.proxy.getASM(new ResourceLocation(LibReference.MODID, "asms/item/enuma_elish.json"));
    	@Override
    	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    		return capability==CapabilityAnimation.ANIMATION_CAPABILITY;
    	}

    	@Override
    	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    		return this.hasCapability(capability, facing) ? CapabilityAnimation.ANIMATION_CAPABILITY.cast(this.asm) : null;
    	}
    }
}
