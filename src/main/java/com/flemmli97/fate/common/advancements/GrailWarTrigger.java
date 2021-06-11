package com.flemmli97.fate.common.advancements;

import com.flemmli97.fate.Fate;
import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class GrailWarTrigger extends AbstractCriterionTrigger<GrailWarTrigger.Instance> {

    private static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "altar");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public GrailWarTrigger.Instance deserializeTrigger(JsonObject obj, EntityPredicate.AndPredicate and, ConditionArrayParser parser) {
        boolean joining = obj.has("joining") && obj.get("joining").getAsBoolean();
        return new GrailWarTrigger.Instance(joining, and);
    }

    public void trigger(ServerPlayerEntity player, boolean joining) {
        this.triggerListeners(player, inst -> inst.test(joining));
    }

    public static class Instance extends CriterionInstance {

        private final boolean join;

        public Instance(boolean join, EntityPredicate.AndPredicate pred) {
            super(ID, pred);
            this.join = join;
        }

        public boolean test(boolean join) {
            return join == this.join;
        }
    }
}