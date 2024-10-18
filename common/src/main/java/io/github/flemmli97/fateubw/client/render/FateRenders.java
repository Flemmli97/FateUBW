package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.mixin.RenderTypeAccessor;
import io.github.flemmli97.tenshilib.client.CustomRenderTypesHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class FateRenders extends RenderType {

    private static ShaderInstance CORRUPTED_SHADER_INSTANCE;
    private static ShaderInstance CLIPPED_SHADER_INSTANCE;
    private static ShaderInstance PULSING_TEXT_SHADER;

    public static final ShaderStateShard CORRUPTED_SHADER = new ShaderStateShard(() -> CORRUPTED_SHADER_INSTANCE);
    public static final ShaderStateShard CLIPPED_SHADER = new ShaderStateShard(() -> CLIPPED_SHADER_INSTANCE);
    public static final ShaderStateShard BLOOM_SHADER = new ShaderStateShard(() -> PULSING_TEXT_SHADER);

    public static final TransparencyStateShard CORRUPTED_OVERLAY_TRANSPARENCY = new TransparencyStateShard("fateubw:corrupted_overlay_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.SRC_COLOR);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static final ResourceLocation CORRUPTED_TEXTURE = new ResourceLocation(Fate.MODID, "textures/misc/corrupted_overlay.png");

    public static final RenderType TRANSLUCENTCOLOR = CustomRenderTypesHelper.createType("fateubw:translucent_color", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, CustomRenderTypesHelper.createBuilder().setWriteMaskState(COLOR_DEPTH_WRITE).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setOutputState(WEATHER_TARGET).setShaderState(RENDERTYPE_LIGHTNING_SHADER).createCompositeState(false));
    public static final RenderType CORRUPTED_OVERLAY = CustomRenderTypesHelper.createType("fateubw:corrupted_overlay", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(CORRUPTED_SHADER).setTextureState(new RenderStateShard.TextureStateShard(CORRUPTED_TEXTURE, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(CORRUPTED_OVERLAY_TRANSPARENCY).setTexturingState(GLINT_TEXTURING).createCompositeState(false));

    private static final BiFunction<RenderType, Vector4f, RenderType> CLIPPED = (wrapped, plane) ->
            new RenderType("rendertype_clipped", wrapped.format(), wrapped.mode(), wrapped.bufferSize(),
                    wrapped.affectsCrumbling(), ((RenderTypeAccessor) wrapped).getSortOnUpload(), () -> {
                wrapped.setupRenderState();
                CLIPPED_SHADER.setupRenderState();
                Uniform uniform = CLIPPED_SHADER_INSTANCE.getUniform("ClippingPlane");
                if (uniform != null) {
                    uniform.set(plane);
                }
            }, () -> {
                wrapped.clearRenderState();
                CLIPPED_SHADER.clearRenderState();
            }) {
            };

    private static final Function<ResourceLocation, RenderType> TRANSLUCENT_BLOOM_TEX = Util.memoize((resourceLocation) -> {
        CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(BLOOM_SHADER).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setLightmapState(LIGHTMAP).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).createCompositeState(true);
        return CustomRenderTypesHelper.createType("fateubw:entity_translucent_bloom", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, compositeState);
    });

    private static boolean init;

    public static void registerShader(ShaderRegister register) {
        try {
            register.register(new ResourceLocation(Fate.MODID, "rendertype_corrupted"), DefaultVertexFormat.POSITION_TEX,
                    shaderInstance -> FateRenders.CORRUPTED_SHADER_INSTANCE = shaderInstance);
            register.register(new ResourceLocation(Fate.MODID, "rendertype_clipped"), DefaultVertexFormat.NEW_ENTITY,
                    shaderInstance -> FateRenders.CLIPPED_SHADER_INSTANCE = shaderInstance);
            register.register(new ResourceLocation(Fate.MODID, "pulsing_entity_text"), DefaultVertexFormat.NEW_ENTITY,
                    shaderInstance -> FateRenders.PULSING_TEXT_SHADER = shaderInstance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addRendertype(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> map) {
        if (init)
            return;
        init = true;
        map.computeIfAbsent(FateRenders.CORRUPTED_OVERLAY, e -> new BufferBuilder(FateRenders.CORRUPTED_OVERLAY.bufferSize()));
    }

    public static RenderType getClippedRendertype(RenderType origin, Vector4f clippingPlane) {
        return CLIPPED.apply(origin, clippingPlane);
    }

    public static RenderType getPulsingEntityText(ResourceLocation texture) {
        return TRANSLUCENT_BLOOM_TEX.apply(texture);
    }

    public static Vector4f createClippingPlane(Vector3f normal, Entity from, float offset) {
        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 viewPos = cam.getPosition();
        viewPos = viewPos.subtract(from.position());
        double dist = (normal.x() * viewPos.x() + normal.y() * viewPos.y() + normal.z() * viewPos.z()) / Math.sqrt(normal.x() * normal.x() + normal.y() * normal.y() + normal.z() * normal.z());
        return new Vector4f(normal.x(), normal.y(), normal.z(), (float) dist + offset);
    }

    private FateRenders(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }

    public interface ShaderRegister {

        void register(ResourceLocation id, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoad) throws IOException;
    }
}
