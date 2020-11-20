package com.flemmli97.fate.common.registry;

import com.flemmli97.fate.common.advancements.GrailWarTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class AdvancementRegister {

    public static GrailWarTrigger grailWarTrigger;

    public static void init() {
        grailWarTrigger = CriteriaTriggers.register(new GrailWarTrigger());
    }
}
