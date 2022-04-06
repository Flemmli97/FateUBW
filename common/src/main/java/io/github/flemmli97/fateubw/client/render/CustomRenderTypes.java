package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.flemmli97.tenshilib.client.CustomRenderTypesHelper;
import net.minecraft.client.renderer.RenderType;

public class CustomRenderTypes extends RenderType {

    public static final RenderType TRANSLUCENTCOLOR = CustomRenderTypesHelper.createType("fate:translucent_color", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, CustomRenderTypesHelper.createBuilder().setWriteMaskState(COLOR_DEPTH_WRITE).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setOutputState(WEATHER_TARGET).setShaderState(RENDERTYPE_LIGHTNING_SHADER).createCompositeState(false));

    private CustomRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }
}
