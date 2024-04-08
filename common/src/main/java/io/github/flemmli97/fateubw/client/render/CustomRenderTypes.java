package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.client.CustomRenderTypesHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.function.Consumer;

public class CustomRenderTypes extends RenderType {

    private static ShaderInstance CORRUPTED_SHADER_INSTANCE;
    public static final ShaderStateShard CORRUPTED_SHADER = new ShaderStateShard(() -> CORRUPTED_SHADER_INSTANCE);
    public static final TransparencyStateShard CORRUPTED_OVERLAY_TRANSPARENCY = new TransparencyStateShard("fateubw:corrupted_overlay_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.SRC_COLOR);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static final ResourceLocation CORRUPTED_TEXTURE = new ResourceLocation(Fate.MODID, "textures/misc/corrupted_overlay.png");

    public static final RenderType TRANSLUCENTCOLOR = CustomRenderTypesHelper.createType("fateubw:translucent_color", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, CustomRenderTypesHelper.createBuilder().setWriteMaskState(COLOR_DEPTH_WRITE).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setOutputState(WEATHER_TARGET).setShaderState(RENDERTYPE_LIGHTNING_SHADER).createCompositeState(false));
    public static RenderType CORRUPTED_OVERLAY = CustomRenderTypesHelper.createType("fateubw:corrupted_overlay", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(CORRUPTED_SHADER).setTextureState(new RenderStateShard.TextureStateShard(CORRUPTED_TEXTURE, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(CORRUPTED_OVERLAY_TRANSPARENCY).setTexturingState(GLINT_TEXTURING).createCompositeState(false));

    private static boolean init;

    public static void registerShader(ShaderRegister register) {
        try {
            register.register(new ResourceLocation(Fate.MODID, "rendertype_corrupted"), DefaultVertexFormat.POSITION_TEX,
                    shaderInstance -> CustomRenderTypes.CORRUPTED_SHADER_INSTANCE = shaderInstance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addRendertype(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> map) {
        if (init)
            return;
        init = true;
        map.computeIfAbsent(CustomRenderTypes.CORRUPTED_OVERLAY, e -> new BufferBuilder(CustomRenderTypes.CORRUPTED_OVERLAY.bufferSize()));
    }

    private CustomRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }

    public interface ShaderRegister {

        void register(ResourceLocation id, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoad) throws IOException;
    }
}
