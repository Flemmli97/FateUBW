package io.github.flemmli97.fateubw;

import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Fate {

    public static final String MODID = "fateubw";
    public static final Logger logger = LogManager.getLogger(Fate.MODID);

    public static boolean astralSorcery;

    public static final CreativeModeTab TAB = PlatformUtils.INSTANCE.tab(new ResourceLocation(Fate.MODID, "tab"), () -> new ItemStack(ModItems.randomIcon.get()));
}
