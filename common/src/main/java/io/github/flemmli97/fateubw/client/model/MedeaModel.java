package io.github.flemmli97.fateubw.client.model;

import io.github.flemmli97.fateubw.client.ClientRegister;
import io.github.flemmli97.fateubw.common.entity.servant.Medea;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.resources.ResourceLocation;

public class MedeaModel<T extends Medea> extends ServantModel<T> {

    public static final ResourceLocation LOCATION = ClientRegister.servantLocation(FateEntities.MEDEA);

    private ModelPartsContainer.ModelPartExtended cloak;
    private ModelPartsContainer.ModelPartExtended hat;
    private ModelPartsContainer.ModelPartExtended cloakBack;

    public MedeaModel() {
        super(LOCATION);
    }

    @Override
    protected void modelReloadListener(ModelPartsContainer model) {
        this.cloak = model.getPart("Cloak");
        this.hat = model.getPart("Hat");
        this.cloakBack = model.getPart("CloakBackLayer");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.hat.visible = entity.getHealth() > entity.getMaxHealth() * 0.5;
    }

    public void toggleCloak(boolean show) {
        this.cloak.visible = show;
        this.cloakBack.visible = show;
        this.hat.visible = show;
    }
}
