package io.github.flemmli97.fate.client.render;

import com.flemmli97.tenshilib.mixin.RenderStateAccessors;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class CustomRenderTypes {

    protected static final RenderState.TransparencyState TRANSLUCENT_SUB = new RenderState.TransparencyState("fate:translucent_sub_transparency", RenderSystem::enableBlend, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static final RenderType TRANSLUCENTCOLOR = RenderType.makeType("fate:translucent_color", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, RenderType.State.getBuilder().writeMask(RenderStateAccessors.allMask()).transparency(TRANSLUCENT_SUB).target(RenderStateAccessors.weatherTarget()).shadeModel(RenderStateAccessors.enableShade()).build(false));

}
