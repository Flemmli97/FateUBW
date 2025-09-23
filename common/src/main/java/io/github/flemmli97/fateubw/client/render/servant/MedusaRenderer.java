package io.github.flemmli97.fateubw.client.render.servant;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientRegister;
import io.github.flemmli97.fateubw.client.model.ServantModel;
import io.github.flemmli97.fateubw.client.render.ServantRenderer;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.servant.Medusa;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MedusaRenderer<T extends BaseServant> extends ServantRenderer<T, ServantModel<T>> {

    public static final ResourceLocation OPEN_EYES = Fate.modRes("textures/entity/servant/" + FateEntities.MEDUSA.getID().getPath() + "_open.png");

    public MedusaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ServantModel<>(ClientRegister.servantLocation(FateEntities.MEDUSA)), ClientRegister.servantTexture(FateEntities.MEDUSA), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(T servant) {
        AnimationState anim = servant.getAnimationHandler().getAnimation();
        if (anim != null && anim.is(Medusa.EYE) && anim.isPast("open") && !anim.isPast("close")) {
            return OPEN_EYES;
        }
        return super.getTextureLocation(servant);
    }
}
