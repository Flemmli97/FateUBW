package com.flemmli97.fatemod.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

public class CustomDataPacket extends DataSerializers{
    
    public static final DataSerializer<Double> Double = new DataSerializer<Double>()
    {

        @Override
        public void write(PacketBuffer buf, Double value)
        {
            buf.writeDouble(value);
        }

        @Override
        public Double read(PacketBuffer buf) throws java.io.IOException
        {
            return buf.readDouble();
        }

        @Override
        public DataParameter<Double> createKey(int id)
        {
            return new DataParameter<Double>(id, this);
        }
		@Override
		public Double copyValue(Double value) {
			return value;
		}

    };
    
    public static final void registerData()
    {
    	registerSerializer(Double);
    }
}
