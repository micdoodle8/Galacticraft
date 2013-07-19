package micdoodle8.mods.galacticraft.core.client.model.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GCCoreModelSolarPanel extends ModelBase
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

    public GCCoreModelSolarPanel()
    {
        this(0.0F);
    }

    public GCCoreModelSolarPanel(float var1)
    {
        this.textureWidth = 256;
        this.textureHeight = 128;
        panelMain = new ModelRenderer(this, 0, 0);
        panelMain.addBox(-23F, -0.5F, -23F, 46, 1, 46);
        panelMain.setRotationPoint(0F, 0F, 0F);
        panelMain.setTextureSize(256, 128);
        panelMain.mirror = true;
        setRotation(panelMain, 0F, 0F, 0F);
        sideHorizontal0 = new ModelRenderer(this, 0, 48);
        sideHorizontal0.addBox(-24F, -1.111F, -23F, 1, 1, 46);
        sideHorizontal0.setRotationPoint(0F, 0F, 0F);
        sideHorizontal0.setTextureSize(256, 128);
        sideHorizontal0.mirror = true;
        setRotation(sideHorizontal0, 0F, 0F, 0F);
        sideVertical0 = new ModelRenderer(this, 94, 48);
        sideVertical0.addBox(-24F, -1.1F, 23F, 48, 1, 1);
        sideVertical0.setRotationPoint(0F, 0F, 0F);
        sideVertical0.setTextureSize(256, 128);
        sideVertical0.mirror = true;
        setRotation(sideVertical0, 0F, 0F, 0F);
        sideVertical2 = new ModelRenderer(this, 94, 48);
        sideVertical2.addBox(-24F, -1.1F, -24F, 48, 1, 1);
        sideVertical2.setRotationPoint(0F, 0F, 0F);
        sideVertical2.setTextureSize(256, 128);
        sideVertical2.mirror = true;
        setRotation(sideVertical2, 0F, 0F, 0F);
        sideVertical1 = new ModelRenderer(this, 94, 48);
        sideVertical1.addBox(-24F, -1.1F, -0.5F, 48, 1, 1);
        sideVertical1.setRotationPoint(0F, 0F, 0F);
        sideVertical1.setTextureSize(256, 128);
        sideVertical1.mirror = true;
        setRotation(sideVertical1, 0F, 0F, 0F);
        sideHorizontal1 = new ModelRenderer(this, 0, 48);
        sideHorizontal1.addBox(-9F, -1.111F, -23F, 1, 1, 46);
        sideHorizontal1.setRotationPoint(0F, 0F, 0F);
        sideHorizontal1.setTextureSize(256, 128);
        sideHorizontal1.mirror = true;
        setRotation(sideHorizontal1, 0F, 0F, 0F);
        sideHorizontal3 = new ModelRenderer(this, 0, 48);
        sideHorizontal3.addBox(23F, -1.111F, -23F, 1, 1, 46);
        sideHorizontal3.setRotationPoint(0F, 0F, 0F);
        sideHorizontal3.setTextureSize(256, 128);
        sideHorizontal3.mirror = true;
        setRotation(sideHorizontal3, 0F, 0F, 0F);
        sideHorizontal2 = new ModelRenderer(this, 0, 48);
        sideHorizontal2.addBox(8F, -1.111F, -23F, 1, 1, 46);
        sideHorizontal2.setRotationPoint(0F, 0F, 0F);
        sideHorizontal2.setTextureSize(256, 128);
        sideHorizontal2.mirror = true;
        setRotation(sideHorizontal2, 0F, 0F, 0F);
        pole = new ModelRenderer(this, 94, 50);
        pole.addBox(-1.5F, 0.0F, -1.5F, 3, 24, 3);
        pole.setRotationPoint(0F, 0F, 0F);
        pole.setTextureSize(256, 128);
        pole.mirror = true;
        setRotation(pole, 0F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void renderPanel()
    {
        panelMain.render(0.0625F);
        sideHorizontal0.render(0.0625F);
        sideVertical0.render(0.0625F);
        sideVertical2.render(0.0625F);
        sideVertical1.render(0.0625F);
        sideHorizontal1.render(0.0625F);
        sideHorizontal3.render(0.0625F);
        sideHorizontal2.render(0.0625F);
    }

    public void renderPole()
    {
        pole.render(0.0625F);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
    }
}
