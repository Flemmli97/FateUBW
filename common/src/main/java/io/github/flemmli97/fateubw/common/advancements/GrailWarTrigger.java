package io.github.flemmli97.fateubw.common.advancements;

import com.google.gson.JsonObject;
import io.github.flemmli97.fateubw.Fate;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GrailWarTrigger extends SimpleCriterionTrigger<GrailWarTrigger.Instance> {

    private static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "altar");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public GrailWarTrigger.Instance createInstance(JsonObject obj, EntityPredicate.Composite and, DeserializationContext parser) {
        boolean joining = obj.has("joining") && obj.get("joining").getAsBoolean();
        return new GrailWarTrigger.Instance(joining, and);
    }

    public void trigger(ServerPlayer player, boolean joining) {
        this.trigger(player, inst -> inst.test(joining));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final boolean join;

        public Instance(boolean join, EntityPredicate.Composite pred) {
            super(ID, pred);
            this.join = join;
        }

        public boolean test(boolean join) {
            return join == this.join;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            JsonObject obj = super.serializeToJson(conditions);
            obj.addProperty("joining", this.join);
            return obj;
        }
    }
}