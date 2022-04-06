package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ModelEA extends Model {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "ea"), "main");

    private final ModelPart handle;
    private final ModelPart blade1;
    private final ModelPart blade2;
    private final ModelPart blade3;
    private final ModelPart blade;

    public ModelEA(ModelPart root) {
        super(RenderType::entitySolid);
        this.handle = root.getChild("handle");
        this.blade = root.getChild("blade");
        this.blade1 = this.blade.getChild("blade1");
        this.blade2 = this.blade.getChild("blade2");
        this.blade3 = this.blade.getChild("blade3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition handle = partdefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(0, 3).addBox(0.0F, 2.0F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 0).mirror().addBox(0.0F, -10.0F, -1.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 16.0F, 1.0F));

        PartDefinition handleEnd = handle.addOrReplaceChild("handleEnd", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 5.4F, -1.2F, -0.3927F, 0.0F, 0.0F));

        PartDefinition guard = handle.addOrReplaceChild("guard", CubeListBuilder.create().texOffs(0, 13).mirror().addBox(-0.5F, 0.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition guardTop = guard.addOrReplaceChild("guardTop", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-1.0F, -1.0F, 0.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.2F, -1.8F, 0.3927F, 0.0F, 0.0F));

        PartDefinition guardBottom = guard.addOrReplaceChild("guardBottom", CubeListBuilder.create().texOffs(10, 13).mirror().addBox(-1.0F, -1.0F, 0.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.0F, -2.5F, -0.3927F, 0.0F, 0.0F));

        PartDefinition blade = partdefinition.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(-1.0F, 16.0F, 1.0F));

        PartDefinition blade1 = blade.addOrReplaceChild("blade1", CubeListBuilder.create().texOffs(8, 3).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -7.5F, -0.5F, 0.0F, -0.7854F, 0.0F));

        PartDefinition blade2 = blade.addOrReplaceChild("blade2", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, -4.5F, -0.5F));

        PartDefinition blade3 = blade.addOrReplaceChild("blade3", CubeListBuilder.create().texOffs(8, 8).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -1.5F, -0.5F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 24, 24);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.handle.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.blade.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public void setBase(boolean flag) {
        this.blade.visible = !flag;
        this.handle.visible = flag;
    }

    public void spinBlade(int tick, float partialTicks) {
        float angle = (tick + partialTicks) * 10 % 360;
        this.blade1.yRot = MathUtils.degToRad(angle);
        this.blade2.yRot = -MathUtils.degToRad(angle);
        this.blade3.yRot = MathUtils.degToRad(angle + 90);
    }
}
