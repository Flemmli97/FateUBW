package io.github.flemmli97.fateubw.fabric.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CriteriaTriggers.class)
public interface CriteriaTriggerAccessor {

    @Invoker("register")
    static <T extends CriterionTrigger<?>> T registerCriteria(T criterion) {
        throw new IllegalStateException();
    }
}
