package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedea;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelMedea<T extends EntityMedea & IAnimated> extends ModelServant<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "medea"), "main");

    private final ModelPartHandler.ModelPartExtended cloak;
    private final ModelPartHandler.ModelPartExtended hat;
    private final ModelPartHandler.ModelPartExtended cloakBack;

    public ModelMedea(ModelPart root) {
        super(root, "medea");
        this.cloak = this.model.getPart("Cloak");
        this.hat = this.model.getPart("Hat");
        this.cloakBack = this.model.getPart("CloakBackLayer");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition CloakBackLayer = Body.addOrReplaceChild("CloakBackLayer", CubeListBuilder.create().texOffs(30, 111).addBox(-4.0F, 0.0F, 1.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, -3.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Hat = Head.addOrReplaceChild("Hat", CubeListBuilder.create().texOffs(72, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition LeftArmDown = LeftArm.addOrReplaceChild("LeftArmDown", CubeListBuilder.create().texOffs(40, 26).mirror().addBox(-4.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 36).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(3.0F, 4.0F, 0.0F));

        PartDefinition LeftItem = LeftArmDown.addOrReplaceChild("LeftItem", CubeListBuilder.create(), PartPose.offset(-2.0F, 3.0F, 0.0F));

        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(24, 42).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition RightArmDown = RightArm.addOrReplaceChild("RightArmDown", CubeListBuilder.create().texOffs(0, 48).addBox(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 16).addBox(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-3.0F, 4.0F, 0.0F));

        PartDefinition RightItem = RightArmDown.addOrReplaceChild("RightItem", CubeListBuilder.create(), PartPose.offset(2.0F, 3.0F, 0.0F));

        PartDefinition LeftLeg = Body.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(16, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition LeftLegDown = LeftLeg.addOrReplaceChild("LeftLegDown", CubeListBuilder.create().texOffs(56, 26).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 56).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition RightLeg = Body.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(56, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(56, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition RightLegDown = RightLeg.addOrReplaceChild("RightLegDown", CubeListBuilder.create().texOffs(48, 56).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 58).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition Cloak = Body.addOrReplaceChild("Cloak", CubeListBuilder.create().texOffs(0, 68).addBox(-9.0F, -1.0F, -5.0F, 18.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 75).addBox(-8.0F, 0.0F, 0.0F, 16.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 75).addBox(8.0F, 0.0F, -5.0F, 1.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(48, 67).addBox(-9.0F, 0.0F, -5.0F, 1.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 105).addBox(-8.0F, 0.0F, -5.0F, 16.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

        PartDefinition Cloak2 = Cloak.addOrReplaceChild("Cloak2", CubeListBuilder.create().texOffs(34, 93).addBox(-9.0F, 0.0F, -5.0F, 1.0F, 12.0F, 6.0F, new CubeDeformation(-0.001F))
                .texOffs(0, 92).addBox(-8.0F, 0.0F, 0.0F, 16.0F, 12.0F, 1.0F, new CubeDeformation(-0.001F))
                .texOffs(48, 93).addBox(8.0F, 0.0F, -5.0F, 1.0F, 12.0F, 6.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition RidingLegs = partdefinition.addOrReplaceChild("RidingLegs", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition LeftLeg2 = RidingLegs.addOrReplaceChild("LeftLeg2", CubeListBuilder.create().texOffs(16, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftLegDown2 = LeftLeg2.addOrReplaceChild("LeftLegDown2", CubeListBuilder.create().texOffs(56, 26).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 56).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition RightLeg2 = RidingLegs.addOrReplaceChild("RightLeg2", CubeListBuilder.create().texOffs(56, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(56, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-4.0F, 0.0F, 0.0F));

        PartDefinition RightLegDown2 = RightLeg2.addOrReplaceChild("RightLegDown2", CubeListBuilder.create().texOffs(48, 56).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 58).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition LeftItemDetached = partdefinition.addOrReplaceChild("LeftItemDetached", CubeListBuilder.create(), PartPose.offset(6.0F, 9.0F, 0.0F));

        PartDefinition RightItemDetached = partdefinition.addOrReplaceChild("RightItemDetached", CubeListBuilder.create(), PartPose.offset(-6.0F, 9.0F, -2.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void toggleCloak(boolean show) {
        this.cloak.visible = show;
        this.cloakBack.visible = show;
        this.hat.visible = show;
    }
}
