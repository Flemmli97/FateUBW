package io.github.flemmli97.fateubw;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Fate {

    public static final String MODID = "fateubw";
    public static final Logger LOGGER = LogManager.getLogger(Fate.MODID);

    public static ResourceLocation modRes(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
