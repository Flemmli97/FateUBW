package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.common.advancements.GrailWarTrigger;
import io.github.flemmli97.fateubw.platform.Platform;

public class AdvancementRegister {

    public static GrailWarTrigger grailWarTrigger;

    public static void init() {
        grailWarTrigger = Platform.INSTANCE.registerCriteriaTrigger(new GrailWarTrigger());
    }
}
