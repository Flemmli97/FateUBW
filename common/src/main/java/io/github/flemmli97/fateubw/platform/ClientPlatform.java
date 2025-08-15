package io.github.flemmli97.fateubw.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.tenshilib.loader.LoaderInitializer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public interface ClientPlatform {

    ClientPlatform INSTANCE = LoaderInitializer.getImplInstance(ClientPlatform.class,
            "io.github.flemmli97.fateubw.fabric.platform.ClientPlatformImpl",
            "io.github.flemmli97.fateubw.neoforge.platform.ClientPlatformImpl");

    void renderModelList(ItemRenderer renderer, BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer);
}
