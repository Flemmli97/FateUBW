package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.HopliteModel;
import io.github.flemmli97.fateubw.common.entity.summons.Hoplite;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderHoplite<T extends Hoplite> extends MobRenderer<T, HopliteModel<T>> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/hoplite.png");
    public static final ResourceLocation TEX_IRON = Fate.modRes("textures/entity/hoplite_iron.png");

    public RenderHoplite(EntityRendererProvider.Context ctx) {
        super(ctx, new HopliteModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Hoplite hoplite) {
        return hoplite.hasIronArmor() ? TEX_IRON : TEX;
    }
}
