package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/**
 * CaladBolg - Undefined
 * Created using Tabula 6.0.0
 */
public class ModelCaladBolg extends EntityModel<CaladBolg> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "caladbolg"), "main");

    protected final ModelPartHandler model;

    private final ModelPartHandler.ModelPartExtended guard;
    private final ModelPartHandler.ModelPartExtended guard2;
    private final ModelPartHandler.ModelPartExtended guard3;
    private final ModelPartHandler.ModelPartExtended guard4;

    public ModelCaladBolg(ModelPart root) {
        this.model = new ModelPartHandler(root.getChild("base"), "base");
        this.guard = this.model.getPart("guard");
        this.guard2 = this.model.getPart("guard2");
        this.guard3 = this.model.getPart("guard3");
        this.guard4 = this.model.getPart("guard4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(21, 9).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 9.0F));

        PartDefinition blade = base.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -23.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-1.0F, -1.0F, -18.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-1.5F, -1.5F, -10.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -1.0F));

        PartDefinition guard = base.addOrReplaceChild("guard", CubeListBuilder.create().texOffs(12, 1).addBox(-1.475F, -3.0F, -1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.75F, -1.0F, -0.8727F, 0.0F, 0.0F));

        PartDefinition guard2 = base.addOrReplaceChild("guard2", CubeListBuilder.create().texOffs(20, 1).addBox(-1.475F, -1.0F, -1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.75F, -1.0F, 0.8727F, 0.0F, 0.0F));

        PartDefinition guard3 = base.addOrReplaceChild("guard3", CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, -1.475F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, 0.0F, -1.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition guard4 = base.addOrReplaceChild("guard4", CubeListBuilder.create().texOffs(10, 30).addBox(-3.0F, -1.475F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, 0.0F, -1.0F, 0.0F, 0.8727F, 0.0F));

        return LayerDefinition.create(meshdefinition, 35, 34);
    }

    @Override
    public void setupAnim(CaladBolg bolg, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float speed = (float) bolg.getDeltaMovement().length();
        if (speed < 1)
            speed = 1;
        if (speed > 2)
            speed = 2;
        this.model.getMainPart().setScale(speed, speed, speed);
        this.guard.xRot -= MathUtils.degToRad(25);
        this.guard2.xRot += MathUtils.degToRad(25);
        this.guard3.yRot -= MathUtils.degToRad(25);
        this.guard4.yRot += MathUtils.degToRad(25);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(matrixStack, buffer, packedLight, packedOverlay);
    }
}
