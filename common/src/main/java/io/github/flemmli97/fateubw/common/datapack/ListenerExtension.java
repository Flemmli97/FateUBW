package io.github.flemmli97.fateubw.common.datapack;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface ListenerExtension extends PreparableReloadListener {

    ResourceLocation id();

    void insertRegistryAccess(HolderLookup.Provider provider);
}
