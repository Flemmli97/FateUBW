package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.minions.LesserMonster;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import net.minecraft.client.Minecraft;
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
 * Starfish Demons - Black_Saturn
 * Created using Tabula 7.0.0
 */
public class ModelStarfishDemon<T extends LesserMonster> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "starfish"), "main");

    private final ModelPartHandler model;
    private final BlockBenchAnimations anim;

    public ModelStarfishDemon(ModelPart root) {
        this.model = new ModelPartHandler(root.getChild("MouthBottom"), "MouthBottom");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(Fate.MODID, "starfish"));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition MouthBottom = partdefinition.addOrReplaceChild("MouthBottom", CubeListBuilder.create().texOffs(0, 3).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 1.5708F, 0.0F, 0.5236F));

        PartDefinition MouthSide1 = MouthBottom.addOrReplaceChild("MouthSide1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -3.0F));

        PartDefinition TentacleP1S1 = MouthSide1.addOrReplaceChild("TentacleP1S1", CubeListBuilder.create().texOffs(16, 10).mirror().addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.1F, -0.5236F, 0.0F, 0.0F));

        PartDefinition TentacleP2S1 = TentacleP1S1.addOrReplaceChild("TentacleP2S1", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.6F, 0.8727F, 0.0F, 0.0F));

        PartDefinition TentacleP3S1 = TentacleP2S1.addOrReplaceChild("TentacleP3S1", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.5F, -1.5708F, 0.0F, 0.0F));

        PartDefinition MouthSide2 = MouthBottom.addOrReplaceChild("MouthSide2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.6F, 0.0F, -1.5F, 0.0F, -1.0472F, 0.0F));

        PartDefinition TentacleP1S2 = MouthSide2.addOrReplaceChild("TentacleP1S2", CubeListBuilder.create().texOffs(16, 10).mirror().addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.1F, -0.5236F, 0.0F, 0.0F));

        PartDefinition TentacleP2S2 = TentacleP1S2.addOrReplaceChild("TentacleP2S2", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.6F, 0.8727F, 0.0F, 0.0F));

        PartDefinition TentacleP3S2 = TentacleP2S2.addOrReplaceChild("TentacleP3S2", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.5F, -1.5708F, 0.0F, 0.0F));

        PartDefinition MouthSide3 = MouthBottom.addOrReplaceChild("MouthSide3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.6F, 0.0F, 1.5F, 0.0F, -2.0944F, 0.0F));

        PartDefinition TentacleP1S3 = MouthSide3.addOrReplaceChild("TentacleP1S3", CubeListBuilder.create().texOffs(16, 10).mirror().addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.1F, -0.1745F, 0.0F, 0.0F));

        PartDefinition TentacleP2S3 = TentacleP1S3.addOrReplaceChild("TentacleP2S3", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.6F, -0.3491F, 0.0F, 0.0F));

        PartDefinition TentacleP3S3 = TentacleP2S3.addOrReplaceChild("TentacleP3S3", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition MouthSide4 = MouthBottom.addOrReplaceChild("MouthSide4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition TentacleP1S4 = MouthSide4.addOrReplaceChild("TentacleP1S4", CubeListBuilder.create().texOffs(16, 10).mirror().addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.1F, -0.1745F, 0.0F, 0.0F));

        PartDefinition TentacleP2S4 = TentacleP1S4.addOrReplaceChild("TentacleP2S4", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.6F, -0.3491F, 0.0F, 0.0F));

        PartDefinition TentacleP3S4 = TentacleP2S4.addOrReplaceChild("TentacleP3S4", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition MouthSide5 = MouthBottom.addOrReplaceChild("MouthSide5", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.6F, 0.0F, 1.5F, 0.0F, -4.1888F, 0.0F));

        PartDefinition TentacleP1S5 = MouthSide5.addOrReplaceChild("TentacleP1S5", CubeListBuilder.create().texOffs(16, 10).mirror().addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.1F, -0.1745F, 0.0F, 0.0F));

        PartDefinition TentacleP2S5 = TentacleP1S5.addOrReplaceChild("TentacleP2S5", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.6F, -0.3491F, 0.0F, 0.0F));

        PartDefinition TentacleP3S5 = TentacleP2S5.addOrReplaceChild("TentacleP3S5", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition MouthSide6 = MouthBottom.addOrReplaceChild("MouthSide6", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.6F, 0.0F, -1.5F, 0.0F, -5.236F, 0.0F));

        PartDefinition TentacleP1S6 = MouthSide6.addOrReplaceChild("TentacleP1S6", CubeListBuilder.create().texOffs(16, 10).mirror().addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.1F, -0.1745F, 0.0F, 0.0F));

        PartDefinition TentacleP2S6 = TentacleP1S6.addOrReplaceChild("TentacleP2S6", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.6F, -0.3491F, 0.0F, 0.0F));

        PartDefinition TentacleP3S6 = TentacleP2S6.addOrReplaceChild("TentacleP3S6", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.5F, -0.5236F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(T monster, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        AnimatedAction anim = monster.getAnimationHandler().getAnimation();
        if (anim != null)
            this.anim.doAnimation(this, anim.getID(), anim.getTick(), partialTicks);
        else if (monster.getDeltaMovement().x != 0 || monster.getDeltaMovement().z != 0)
            this.anim.doAnimation(this, "walk", monster.tickCount, partialTicks);
        else
            this.anim.doAnimation(this, "idle", monster.tickCount, partialTicks);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }
}
