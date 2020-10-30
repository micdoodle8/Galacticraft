package micdoodle8.mods.galacticraft.core.client.model.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.Model;

public class ModelSolarPanel extends Model
{
    ModelRenderer panelMain;
    ModelRenderer sideHorizontal0;
    ModelRenderer sideVertical0;
    ModelRenderer sideVertical2;
    ModelRenderer sideVertical1;
    ModelRenderer sideHorizontal1;
    ModelRenderer sideHorizontal3;
    ModelRenderer sideHorizontal2;
    ModelRenderer pole;

    public ModelSolarPanel()
    {
        super(RenderType::getEntitySolid);
        this.textureWidth = 256;
        this.textureHeight = 128;
        this.panelMain = new ModelRenderer(this, 0, 0);
        this.panelMain.addBox(-23F, -0.5F, -23F, 46, 1, 46);
        this.panelMain.setRotationPoint(0F, 0F, 0F);
        this.panelMain.setTextureSize(256, 128);
        this.panelMain.mirror = true;
        this.setRotation(this.panelMain, 0F, 0F, 0F);
        this.sideHorizontal0 = new ModelRenderer(this, 0, 48);
        this.sideHorizontal0.addBox(-24F, -1.111F, -23F, 1, 1, 46);
        this.sideHorizontal0.setRotationPoint(0F, 0F, 0F);
        this.sideHorizontal0.setTextureSize(256, 128);
        this.sideHorizontal0.mirror = true;
        this.setRotation(this.sideHorizontal0, 0F, 0F, 0F);
        this.sideVertical0 = new ModelRenderer(this, 94, 48);
        this.sideVertical0.addBox(-24F, -1.1F, 23F, 48, 1, 1);
        this.sideVertical0.setRotationPoint(0F, 0F, 0F);
        this.sideVertical0.setTextureSize(256, 128);
        this.sideVertical0.mirror = true;
        this.setRotation(this.sideVertical0, 0F, 0F, 0F);
        this.sideVertical2 = new ModelRenderer(this, 94, 48);
        this.sideVertical2.addBox(-24F, -1.1F, -24F, 48, 1, 1);
        this.sideVertical2.setRotationPoint(0F, 0F, 0F);
        this.sideVertical2.setTextureSize(256, 128);
        this.sideVertical2.mirror = true;
        this.setRotation(this.sideVertical2, 0F, 0F, 0F);
        this.sideVertical1 = new ModelRenderer(this, 94, 48);
        this.sideVertical1.addBox(-24F, -1.1F, -0.5F, 48, 1, 1);
        this.sideVertical1.setRotationPoint(0F, 0F, 0F);
        this.sideVertical1.setTextureSize(256, 128);
        this.sideVertical1.mirror = true;
        this.setRotation(this.sideVertical1, 0F, 0F, 0F);
        this.sideHorizontal1 = new ModelRenderer(this, 0, 48);
        this.sideHorizontal1.addBox(-9F, -1.111F, -23F, 1, 1, 46);
        this.sideHorizontal1.setRotationPoint(0F, 0F, 0F);
        this.sideHorizontal1.setTextureSize(256, 128);
        this.sideHorizontal1.mirror = true;
        this.setRotation(this.sideHorizontal1, 0F, 0F, 0F);
        this.sideHorizontal3 = new ModelRenderer(this, 0, 48);
        this.sideHorizontal3.addBox(23F, -1.111F, -23F, 1, 1, 46);
        this.sideHorizontal3.setRotationPoint(0F, 0F, 0F);
        this.sideHorizontal3.setTextureSize(256, 128);
        this.sideHorizontal3.mirror = true;
        this.setRotation(this.sideHorizontal3, 0F, 0F, 0F);
        this.sideHorizontal2 = new ModelRenderer(this, 0, 48);
        this.sideHorizontal2.addBox(8F, -1.111F, -23F, 1, 1, 46);
        this.sideHorizontal2.setRotationPoint(0F, 0F, 0F);
        this.sideHorizontal2.setTextureSize(256, 128);
        this.sideHorizontal2.mirror = true;
        this.setRotation(this.sideHorizontal2, 0F, 0F, 0F);
        this.pole = new ModelRenderer(this, 94, 50);
        this.pole.addBox(-1.5F, 0.0F, -1.5F, 3, 24, 3);
        this.pole.setRotationPoint(0F, 0F, 0F);
        this.pole.setTextureSize(256, 128);
        this.pole.mirror = true;
        this.setRotation(this.pole, 0F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void render(MatrixStack matStack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        renderSolarPanel(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        renderPole(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderSolarPanel(MatrixStack matStack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.panelMain.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideHorizontal0.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideVertical0.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideVertical1.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideVertical2.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideHorizontal1.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideHorizontal2.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideHorizontal3.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderPole(MatrixStack matStack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.pole.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
