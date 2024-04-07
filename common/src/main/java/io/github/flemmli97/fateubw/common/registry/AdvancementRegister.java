package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.common.advancements.GrailWarTrigger;
import io.github.flemmli97.fateubw.platform.Platform;

public class AdvancementRegister {

    public static GrailWarTrigger GRAIL_WAR_TRIGGER;

    public static void init() {
        GRAIL_WAR_TRIGGER = Platform.INSTANCE.registerCriteriaTrigger(new GrailWarTrigger());
    }
}
