package com.flemmli97.fatemod.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

public class CustomDataPacket extends DataSerializers{
	
	public static final DataSerializer<ItemStack> ITEM_STACK = new DataSerializer<ItemStack>()
    {
        public void write(PacketBuffer buf, ItemStack value)
        {
            buf.writeItemStackToBuffer((ItemStack)value);
        }
        public ItemStack read(PacketBuffer buf) throws java.io.IOException
        {
            return buf.readItemStackFromBuffer();
        }
        public DataParameter<ItemStack> createKey(int id)
        {
            return new DataParameter<ItemStack>(id, this);
        }

    };
    
    public static final DataSerializer<Double> Double = new DataSerializer<Double>()
    {
        public void write(PacketBuffer buf, Double value)
        {
            buf.writeDouble(value);
        }
        public Double read(PacketBuffer buf) throws java.io.IOException
        {
            return buf.readDouble();
        }
        public DataParameter<Double> createKey(int id)
        {
            return new DataParameter<Double>(id, this);
        }

    };
    
    public static final void registerData()
    {
    	registerSerializer(ITEM_STACK);
    	registerSerializer(Double);
    }
}
