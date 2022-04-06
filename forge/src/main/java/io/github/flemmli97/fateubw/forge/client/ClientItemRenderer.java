package io.github.flemmli97.fateubw.forge.client;

import io.github.flemmli97.fateubw.client.render.item.RenderEAItem;
import io.github.flemmli97.fateubw.client.render.item.RenderExcaliburItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.IItemRenderProperties;

public class ClientItemRenderer {

    public static IItemRenderProperties excalibur() {
        return new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return new RenderExcaliburItem(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
            }
        };
    }

    public static IItemRenderProperties ea() {
        return new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return new RenderEAItem(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
            }
        };
    }
}
