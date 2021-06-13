package io.github.flemmli97.fate.common.registry;

import io.github.flemmli97.fate.common.advancements.GrailWarTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class AdvancementRegister {

    public static GrailWarTrigger grailWarTrigger;

    public static void init() {
        grailWarTrigger = CriteriaTriggers.register(new GrailWarTrigger());
    }
}
