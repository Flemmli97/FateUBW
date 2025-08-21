package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.client.ClientRegister;
import io.github.flemmli97.fateubw.client.model.ServantModel;
import io.github.flemmli97.fateubw.common.entity.summons.HassanClone;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.tenshilib.client.render.layer.ItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderHassanCopy extends MobRenderer<HassanClone, ServantModel<HassanClone>> {

    public static final ResourceLocation TEXTURES = ClientRegister.servantTexture(FateEntities.HASSAN);

    public RenderHassanCopy(EntityRendererProvider.Context ctx) {
        super(ctx, new ServantModel<>(ClientRegister.servantLocation(FateEntities.HASSAN)), 0.5F);
        this.addLayer(new ItemLayer<>(this, ctx.getItemInHandRenderer()));
        this.addLayer(new CustomHeadLayer<>(this, ctx.getModelSet(), ctx.getItemInHandRenderer()));
    }

    @Override
    protected float getFlipDegrees(HassanClone livingEntity) {
        return 0;
    }

    @Override
    public ResourceLocation getTextureLocation(HassanClone servant) {
        return TEXTURES;
    }
}
