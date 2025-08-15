package io.github.flemmli97.fateubw.client.model;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedea;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.resources.ResourceLocation;

public class MedeaModel<T extends EntityMedea> extends ServantModel<T> {

    public static final ResourceLocation LOCATION = Fate.modRes("servant/medea");

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

    public void toggleCloak(boolean show) {
        this.cloak.visible = show;
        this.cloakBack.visible = show;
        this.hat.visible = show;
    }
}
