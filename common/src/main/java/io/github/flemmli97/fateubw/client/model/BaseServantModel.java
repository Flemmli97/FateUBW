package io.github.flemmli97.fateubw.client.model;

import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public abstract class BaseServantModel<T extends Entity> extends EntityModel<T> implements IArmModel, HeadedModel, IPreRenderUpdate<T>, ExtendedModel {

    protected BaseServantModel() {
        this(RenderType::entityCutoutNoCull);
    }

    protected BaseServantModel(Function<ResourceLocation, RenderType> function) {
        super(function);
    }
}
