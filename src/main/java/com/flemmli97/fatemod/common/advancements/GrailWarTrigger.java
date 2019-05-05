package com.flemmli97.fatemod.common.advancements;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flemmli97.fatemod.common.lib.LibReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class GrailWarTrigger implements ICriterionTrigger<GrailWarTrigger.Instance>{

    private static final ResourceLocation ID = new ResourceLocation(LibReference.MODID, "altar");
    private final Map<PlayerAdvancements, GrailWarTrigger.Listeners> listeners = Maps.newHashMap();

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<GrailWarTrigger.Instance> listener) {
		GrailWarTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);

        if (listeners == null)
        {
        	listeners = new GrailWarTrigger.Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, listeners);
        }

        listeners.add(listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<GrailWarTrigger.Instance> listener) {
		GrailWarTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);

        if (listeners != null)
        {
        	listeners.remove(listener);

            if (listeners.isEmpty())
            {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
		this.listeners.remove(playerAdvancementsIn);		
	}

	@Override
	public GrailWarTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new GrailWarTrigger.Instance(JsonUtils.getBoolean(json, "joining"));
	}
	
	public void trigger(EntityPlayerMP player, boolean joining)
    {
		GrailWarTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());

        if (listeners != null)
        {
        	listeners.trigger(joining);
        }
    }
	
	public static class Instance extends AbstractCriterionInstance
    {
		private final boolean join;
		
		public Instance(boolean join) {
			super(ID);
			this.join=join;
		}
		
		public boolean test(boolean join)
		{
			return join==this.join;
		}
    }

	static class Listeners
	{
		private final PlayerAdvancements playerAdvancements;
        private final Set<ICriterionTrigger.Listener<GrailWarTrigger.Instance>> listeners = Sets.<ICriterionTrigger.Listener<GrailWarTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn)
        {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty()
        {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<GrailWarTrigger.Instance> listener)
        {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<GrailWarTrigger.Instance> listener)
        {
            this.listeners.remove(listener);
        }

        public void trigger(boolean joining)
        {
        	List<ICriterionTrigger.Listener<GrailWarTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<GrailWarTrigger.Instance> listener : this.listeners)
            {
                if (((GrailWarTrigger.Instance)listener.getCriterionInstance()).test(joining))
                {
                	if (list == null)
                		list = Lists.<ICriterionTrigger.Listener<GrailWarTrigger.Instance>>newArrayList();
                	list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<GrailWarTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }
	}
}
