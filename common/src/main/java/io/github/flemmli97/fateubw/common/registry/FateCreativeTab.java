package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FateCreativeTab {

    private static final List<Supplier<ItemStack>> CONTENTS = new ArrayList<>();

    public static final LoaderRegister<CreativeModeTab> TABS = LoaderRegistryAccess.INSTANCE.of(Registries.CREATIVE_MODE_TAB, Fate.MODID);

    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> TAB = TABS.register("tab", () -> Platform.INSTANCE.tabBuilder()
            .icon(() -> new ItemStack(FateItems.RANDOM_ICON.get()))
            .displayItems((params, output) -> CONTENTS.forEach(s -> output.accept(s.get()))).build());

    public static synchronized void addToTab(RegistryEntrySupplier<Item, ?> entry) {
        CONTENTS.add(() -> new ItemStack(entry.get()));
    }
}
