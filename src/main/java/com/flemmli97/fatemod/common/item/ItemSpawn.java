package com.flemmli97.fatemod.common.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.render.MultiItemColor;
import com.flemmli97.fatemod.common.entity.servant.SpawnEntityCustomList;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.GrailWarPlayerTracker;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpawn extends Item{
	
	ItemColors color = Minecraft.getMinecraft().getItemColors();
	MultiItemColor icolor;
	
	public ItemSpawn()
	{
		this.setHasSubtypes(true);
		this.setCreativeTab(Fate.customTab);
		this.setUnlocalizedName("spawnEgg");
		GameRegistry.register(this, new ResourceLocation(Fate.MODID, "spawn_egg"));
	}

    @Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.GOLD + "Rename to \"Summon\" to spawn as your servant");
	}

	public String getItemStackDisplayName(ItemStack stack)
    {
        String s = ("" + I18n.format(this.getUnlocalizedName() + ".name")).trim();
		String entityName = getEntityIdFromItem(stack);

        if (entityName != null)
        {
            s = s + " " + I18n.format("entity." + entityName  + ".name");
        }

        return s;
    }
    
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else if (!player.canPlayerEdit(pos.offset(facing), facing, stack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = world.getBlockState(pos);

            if (iblockstate.getBlock() == Blocks.MOB_SPAWNER)
            {
                TileEntity tileentity = world.getTileEntity(pos);

                if (tileentity instanceof TileEntityMobSpawner)
                {
                    MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
                    mobspawnerbaselogic.setEntityName(getEntityIdFromItem(stack));
                    tileentity.markDirty();
                    world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

                    if (!player.capabilities.isCreativeMode)
                    {
                        --stack.stackSize;
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            pos = pos.offset(facing);
            double d0 = 0.0D;

            if (facing == EnumFacing.UP && iblockstate instanceof BlockFence)
            {
                d0 = 0.5D;
            }

            Entity entity = spawnCreature(world, getEntityIdFromItem(stack), (double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D);

            if (entity != null)
            {
                if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                {
                	if("Summon".equals(stack.getDisplayName()))
                	{
                		IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
            			if(cap.getServant()==null)
            			{
                			cap.setServant((EntityServant) entity);
                			((EntityServant) entity).setOwner(player);
                			//((EntityServant) entity).wasSummoned=true;
                			GrailWarPlayerTracker track = GrailWarPlayerTracker.get(world);
                			track.addPlayer(player);  
            			}
            			else
            			{
            				player.addChatMessage(new TextComponentString(TextFormatting.RED + "You already have a servant, spawned a masterless one"));
            			}
                	}
                	else
                    entity.setCustomNameTag(stack.getDisplayName());
                }

                applyItemEntityDataToEntity(world, player, stack, entity);

                if (!player.capabilities.isCreativeMode)
                {
                    --stack.stackSize;
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }

    public static void applyItemEntityDataToEntity(World entityWorld, @Nullable EntityPlayer player, ItemStack stack, @Nullable Entity targetEntity)
    {
        MinecraftServer minecraftserver = entityWorld.getMinecraftServer();

        if (minecraftserver != null && targetEntity != null)
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (nbttagcompound != null && nbttagcompound.hasKey("EntityTag", 10))
            {
                if (!entityWorld.isRemote && targetEntity.ignoreItemEntityData() && (player == null || !minecraftserver.getPlayerList().canSendCommands(player.getGameProfile())))
                {
                    return;
                }

                NBTTagCompound nbttagcompound1 = targetEntity.writeToNBT(new NBTTagCompound());
                UUID uuid = targetEntity.getUniqueID();
                nbttagcompound1.merge(nbttagcompound.getCompoundTag("EntityTag"));
                targetEntity.setUniqueId(uuid);
                targetEntity.readFromNBT(nbttagcompound1);
            }
        }
    }

    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (world.isRemote)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
        }
        else
        {
            RayTraceResult raytraceresult = this.rayTrace(world, player, true);

            if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if (!(world.getBlockState(blockpos).getBlock() instanceof BlockLiquid))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
                }
                else if (world.isBlockModifiable(player, blockpos) && player.canPlayerEdit(blockpos, raytraceresult.sideHit, stack))
                {
                    Entity entity = spawnCreature(world, getEntityIdFromItem(stack), (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D);

                    if (entity == null)
                    {
                        return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
                    }
                    else
                    {
                        if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                        {
                        	if("Summon".equals(stack.getDisplayName()))
                        	{
                    			IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
                    			if(cap.getServant()==null)
                    			{
	                    			cap.setServant((EntityServant) entity);
	                    			((EntityServant) entity).setOwner(player);
	                    			GrailWarPlayerTracker track = GrailWarPlayerTracker.get(world);
	                    			track.addPlayer(player);  
                    			}
                    			else
                    			{
                    				player.addChatMessage(new TextComponentString(TextFormatting.RED + "You already have a servant, spawned a masterless one"));
                    			}
                        	}
                        	else
                            entity.setCustomNameTag(stack.getDisplayName());
                        }

                        applyItemEntityDataToEntity(world, player, stack, entity);

                        if (!player.capabilities.isCreativeMode)
                        {
                            --stack.stackSize;
                        }

                        player.addStat(StatList.getObjectUseStats(this));
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
                    }
                }
                else
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
                }
            }
            else
            {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
            }
        }
    }

    @Nullable

    public static Entity spawnCreature(World worldIn, @Nullable String entityID, double x, double y, double z)
    {
        if (entityID != null && SpawnEntityCustomList.entityEggs.containsKey(entityID))
        {
            Entity entity = null;

            for (int i = 0; i < 1; ++i)
            {
                entity = SpawnEntityCustomList.createEntityByName(entityID, worldIn);

                if (entity instanceof EntityLivingBase)
                {
                    EntityLiving entityliving = (EntityLiving)entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
                    worldIn.spawnEntityInWorld(entity);
                    entityliving.playLivingSound();
                }
            }

            return entity;
        }
        else
        {
            return null;
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        for (SpawnEntityCustomList.EntityEggInfo eggEntity  : SpawnEntityCustomList.entityEggs.values())
        {            
        	ItemStack stack = new ItemStack(itemIn, 1);
        	applyEntityIdToItemStack(stack, eggEntity.spawnedID);
            subItems.add(stack);
        }
    }

    /**
     * APplies the given entity ID to the given ItemStack's NBT data.
     */
    public static void applyEntityIdToItemStack(ItemStack stack, String entityId)
    {
        NBTTagCompound nbttagcompound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setString("id", entityId);
        nbttagcompound.setTag("EntityTag", nbttagcompound1);
        stack.setTagCompound(nbttagcompound);
    }

    @Nullable

    /**
     * Gets the entity ID associated with a given ItemStack in its NBT data.
     */
    public static String getEntityIdFromItem(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound == null)
        {
            return null;
        }
        else if (!nbttagcompound.hasKey("EntityTag", 10))
        {
            return null;
        }
        else
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("EntityTag");
            return !nbttagcompound1.hasKey("id", 8) ? null : nbttagcompound1.getString("id");
        }
    }
       
	 @SideOnly(Side.CLIENT)
	    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }
}
