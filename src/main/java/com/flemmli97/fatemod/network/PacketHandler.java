package com.flemmli97.fatemod.network;

import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(LibReference.MODID);
	
	public static final void registerPackets() {
		int id = 0;
		dispatcher.registerMessage(MessageMana.Handler.class, MessageMana.class, id++, Side.CLIENT);
		dispatcher.registerMessage(MessagePlayerServant.Handler.class, MessagePlayerServant.class, id++, Side.SERVER);
		dispatcher.registerMessage(MessageServantSync.Handler.class, MessageServantSync.class, id++, Side.CLIENT);
		dispatcher.registerMessage(MessageGui.Handler.class, MessageGui.class, id++, Side.SERVER);
		dispatcher.registerMessage(MessageWarTracker.Handler.class, MessageWarTracker.class, id++, Side.CLIENT);
		dispatcher.registerMessage(MessageAltarUpdate.Handler.class, MessageAltarUpdate.class, id++, Side.CLIENT);
		dispatcher.registerMessage(MessageCommandSeals.Handler.class, MessageCommandSeals.class, id++, Side.CLIENT);
		dispatcher.registerMessage(MessageTruce.Handler.class, MessageTruce.class, id++, Side.SERVER);
	}
	
	public static final void sendTo(IMessage message, EntityPlayerMP player) {
		dispatcher.sendTo(message, player);
	}
	
	public static void sendToAll(IMessage message) {
		dispatcher.sendToAll(message);
	}

	public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		dispatcher.sendToAllAround(message, point);
	}

	public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
		sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}

	public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
		sendToAllAround(message, player.world.provider.getDimension(), player.posX, player.posY, player.posZ, range);
	}
	
	public static final void sendToAllAround(IMessage message, TileEntity tileEntity, double range) {
		sendToAllAround(message, tileEntity.getWorld().provider.getDimension(), tileEntity.getPos().getX() + 0.5D, tileEntity.getPos().getY() + 0.5D, tileEntity.getPos().getZ() + 0.5D, range);
	}

	public static final void sendToDimension(IMessage message, int dimensionId) {
		dispatcher.sendToDimension(message, dimensionId);
	}

	public static final void sendToServer(IMessage message) {
		dispatcher.sendToServer(message);
	}
	
	public static final Packet<?> getPacket(IMessage message) {
		return dispatcher.getPacketFrom(message);
	}

}
