package io.github.flemmli97.fate.client.model;

import com.flemmli97.tenshilib.common.utils.MathUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelEA extends Model {

    private final ModelRenderer base;
    private final ModelRenderer handle;
    private final ModelRenderer handleEnd;
    private final ModelRenderer blade;
    private final ModelRenderer blade1;
    private final ModelRenderer blade2;
    private final ModelRenderer blade3;
    private final ModelRenderer backbone;
    private final ModelRenderer guard;
    private final ModelRenderer guardTop;
    private final ModelRenderer guardBottom;

    public ModelEA() {
        super(RenderType::getEntitySolid);
        this.textureWidth = 24;
        this.textureHeight = 24;

        this.base = new ModelRenderer(this);
        this.base.setRotationPoint(-1.0F, 16.0F, 1.0F);


        this.handle = new ModelRenderer(this);
        this.handle.setRotationPoint(0.5F, 5.4F, -1.2F);
        this.base.addChild(this.handle);
        this.handle.setTextureOffset(0, 3).addBox(-0.5F, -3.4F, 0.2F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        this.handleEnd = new ModelRenderer(this);
        this.handleEnd.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handle.addChild(this.handleEnd);
        this.setRotationAngle(this.handleEnd, -0.3927F, 0.0F, 0.0F);
        this.handleEnd.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        this.blade = new ModelRenderer(this);
        this.blade.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base.addChild(this.blade);


        this.blade1 = new ModelRenderer(this);
        this.blade1.setRotationPoint(0.5F, -7.5F, -0.5F);
        this.blade.addChild(this.blade1);
        this.setRotationAngle(this.blade1, 0.0F, -0.7854F, 0.0F);
        this.blade1.setTextureOffset(8, 3).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        this.blade2 = new ModelRenderer(this);
        this.blade2.setRotationPoint(0.5F, -4.5F, -0.5F);
        this.blade.addChild(this.blade2);
        this.blade2.setTextureOffset(0, 8).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);

        this.blade3 = new ModelRenderer(this);
        this.blade3.setRotationPoint(0.5F, -1.5F, -0.5F);
        this.blade.addChild(this.blade3);
        this.setRotationAngle(this.blade3, 0.0F, -0.7854F, 0.0F);
        this.blade3.setTextureOffset(8, 8).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        this.backbone = new ModelRenderer(this);
        this.backbone.setRotationPoint(0.5F, -6.5F, -0.5F);
        this.blade.addChild(this.backbone);
        this.backbone.setTextureOffset(16, 0).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 7.0F, 1.0F, 0.0F, true);

        this.guard = new ModelRenderer(this);
        this.guard.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base.addChild(this.guard);
        this.guard.setTextureOffset(0, 13).addBox(-0.5F, 0.0F, -1.5F, 2.0F, 2.0F, 3.0F, 0.0F, true);

        this.guardTop = new ModelRenderer(this);
        this.guardTop.setRotationPoint(0.0F, 1.2F, -1.8F);
        this.guard.addChild(this.guardTop);
        this.setRotationAngle(this.guardTop, 0.3927F, 0.0F, 0.0F);
        this.guardTop.setTextureOffset(0, 18).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 1.0F, 4.0F, 0.0F, true);

        this.guardBottom = new ModelRenderer(this);
        this.guardBottom.setRotationPoint(0.0F, 2.0F, -2.5F);
        this.guard.addChild(this.guardBottom);
        this.setRotationAngle(this.guardBottom, -0.3927F, 0.0F, 0.0F);
        this.guardBottom.setTextureOffset(10, 13).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 1.0F, 4.0F, 0.0F, true);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.base.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setBase(boolean flag) {
        this.backbone.showModel = flag;
        this.blade1.showModel = !flag;
        this.blade2.showModel = !flag;
        this.blade3.showModel = !flag;
        this.handle.showModel = flag;
        this.guard.showModel = flag;
    }

    public void spinBlade(int tick, float partialTicks) {
        float angle = (tick + partialTicks) * 10 % 360;
        this.blade1.rotateAngleY = MathUtils.degToRad(angle);
        this.blade2.rotateAngleY = -MathUtils.degToRad(angle);
        this.blade3.rotateAngleY = MathUtils.degToRad(angle + 90);
    }
}
