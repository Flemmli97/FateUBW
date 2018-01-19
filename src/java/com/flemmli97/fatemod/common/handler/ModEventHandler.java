package com.flemmli97.fatemod.common.handler;

import java.util.List;

import javax.annotation.Nullable;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.gui.ManaBar;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.item.IExtendedReach;
import com.flemmli97.fatemod.network.MessageExtendedHit;
import com.flemmli97.fatemod.network.MessageMana;
import com.flemmli97.fatemod.network.MessagePlayerServant;
import com.flemmli97.fatemod.network.PacketHandler;
import com.flemmli97.fatemod.proxy.ClientProxy;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
//TODO death capabilites
public class ModEventHandler {
	
	public static final ResourceLocation PlayerCap = new ResourceLocation(Fate.MODID, "playerCap");
	private	GrailWarPlayerTracker tracker=null;

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
		    event.addCapability(PlayerCap, new PlayerCapProvider());
        }
    }
    
    @SubscribeEvent
    public void initTracker(WorldEvent.Load e)
    {
    		if(e.getWorld()!=null && !e.getWorld().isRemote)
    			this.tracker = GrailWarPlayerTracker.get(e.getWorld());
    }
    
    //or PlayerLoggedInEvent
    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer && !event.getEntity().worldObj.isRemote)
        {
			IPlayer cap = event.getEntity().getCapability(PlayerCapProvider.PlayerCap, null);
			PacketHandler.sendTo(new MessageMana(cap), (EntityPlayerMP) event.getEntity());
        }
    }

    @SubscribeEvent
    public void extendedReach(PlayerInteractEvent.LeftClickEmpty event)
    {
    	ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
    	if(stack!=null)
    	if(stack.getItem() instanceof IExtendedReach)
    	{
    		IExtendedReach item = (IExtendedReach) stack.getItem();
	    	RayTraceResult result = ModEventHandler.calculateEntityFromLook(event.getEntityPlayer(), item.getRange());
	    	if(result!=null)
	    	{
	    		PacketHandler.sendToServer(new MessageExtendedHit(result.entityHit.getUniqueID().toString()));
	    	}
    	}
    }

    @SubscribeEvent
	public void onEatEvent(LivingEntityUseItemEvent.Finish event){
	    	ItemStack stack = event.getItem();
	    	if(stack!=null && stack.getItem() instanceof ItemFood)
	    	{
	    		ItemFood item = (ItemFood) stack.getItem();
	    		float foodValue = item.getHealAmount(stack);
	    		if (event.getEntityLiving() instanceof EntityPlayer && !event.getEntity().worldObj.isRemote)
	    		{
	    			IPlayer cap = event.getEntityLiving().getCapability(PlayerCapProvider.PlayerCap, null);
	    			float mana = cap.getMana()+foodValue*5;
	    			if(mana>100)
	    			{
	    				mana=100;
	    			}
	    			cap.setMana((EntityPlayer) event.getEntityLiving(), mana);
	    		}
	    	}
    }
    
    @SubscribeEvent
    public void updateGrailWar(WorldTickEvent event)
    {
    		if(event.phase == Phase.END && !event.world.isRemote)
    		{
	    	  	if(this.tracker !=null)
	    	  	{
	    	  		if(this.tracker.joinTicker>0)
	    	  		{
		    	  		this.tracker.joinTicker--;
		    	  		if(this.tracker.joinTicker==0)
		    	  		{
		    	  			this.tracker.startGrailWar(event.world);
		    	  		}
	    	  		}
	    	  		if(this.tracker.winningDelay>0)
	    	  		{
	    	  			this.tracker.winningDelay--;
	    	  			if(this.tracker.winningDelay==0)
		    	  		{
		    	  			EntityPlayer player = this.tracker.getWinningPlayer();
		    	  			this.tracker.removePlayer(player);
		    	  			EntityItem holyGrail = new EntityItem(player.worldObj, player.posX+event.world.rand.nextInt(9)-4, player.posY, player.posZ+ event.world.rand.nextInt(9)-4, new ItemStack(ModItems.grail));
		    	  			holyGrail.lifespan = 6000;
		    	  			holyGrail.setOwner(player.getName());
		    	  			holyGrail.setEntityInvulnerable(true);
		    	  			event.world.spawnEntityInWorld(holyGrail);
		    	  		}
	    	  		}
	    	  	}
    		}
    }
    
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void keyEvent(KeyInputEvent event)
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		World world = Minecraft.getMinecraft().theWorld;
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
	
	public static RayTraceResult calculateEntityFromLook(EntityPlayer player, float reach)
	{
		RayTraceResult result = null;
		if(player.worldObj!=null)
		{
			Entity entity = null;
            Vec3d posVec = player.getPositionEyes(1);
			Vec3d look = player.getLook(1);
            Vec3d rangeVec = posVec.addVector(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach);
            Vec3d hitVec = null;
            List<Entity> list = player.worldObj.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().addCoord(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach).expand(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
            {
                public boolean apply(@Nullable Entity entity)
                {
                    return entity != null && entity.canBeCollidedWith();
                }
            }));
            for(int i = 0; i < list.size(); ++i)
            {
            	Entity entity1 = (Entity)list.get(i);
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz((double)entity1.getCollisionBorderSize());
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(posVec, rangeVec);
                if (raytraceresult != null)
                {
                    double d3 = posVec.distanceTo(raytraceresult.hitVec);

                    if (d3 < reach)
                    {
                        entity = entity1;
                        hitVec = raytraceresult.hitVec;
                    }
                }
            }
            if(entity!=null)
            {
            	result = new RayTraceResult(entity, hitVec);
            }
		}
		return result;
	}
}
