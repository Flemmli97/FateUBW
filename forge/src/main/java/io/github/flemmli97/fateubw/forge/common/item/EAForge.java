package io.github.flemmli97.fateubw.forge.common.item;

import io.github.flemmli97.fateubw.common.items.weapons.ItemEA;
import io.github.flemmli97.fateubw.forge.client.ClientItemRenderer;
import io.github.flemmli97.fateubw.forge.common.capability.ItemStackCap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EAForge extends ItemEA {

    public EAForge(Properties props) {
        super(props);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(ClientItemRenderer.ea());
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ItemStackCap();
    }
}
