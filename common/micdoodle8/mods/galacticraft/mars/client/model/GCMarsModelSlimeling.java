package micdoodle8.mods.galacticraft.mars.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class GCMarsModelSlimeling extends ModelBase
{
    ModelRenderer tail3;
    ModelRenderer tail2;
    ModelRenderer tail1;
    ModelRenderer bodyMain;
    ModelRenderer neck;
    ModelRenderer head;

    public GCMarsModelSlimeling(float scale)
    {
        textureWidth = 256;
        textureHeight = 128;

        head = new ModelRenderer(this, 196, 25);
        head.addBox(-4.5F, -15.7F, 0.5F, 9, 9, 9);
        head.setRotationPoint(0F, 0F, 0F);
        head.setTextureSize(256, 128);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
        tail3 = new ModelRenderer(this, 0, 25);
        tail3.addBox(-3.5F, 1F, -17F, 7, 5, 7);
        tail3.setRotationPoint(0F, 0F, 0F);
        tail3.setTextureSize(256, 128);
        tail3.mirror = true;
        setRotation(tail3, 0F, 0F, 0F);
        tail2 = new ModelRenderer(this, 28, 25);
        tail2.addBox(-4.5F, -1F, -15F, 9, 7, 9);
        tail2.setRotationPoint(0F, 0F, 0F);
        tail2.setTextureSize(256, 128);
        tail2.mirror = true;
        setRotation(tail2, 0F, 0F, 0F);
        tail1 = new ModelRenderer(this, 64, 25);
        tail1.addBox(-5.5F, -3F, -11F, 11, 9, 10);
        tail1.setRotationPoint(0F, 0F, 0F);
        tail1.setTextureSize(256, 128);
        tail1.mirror = true;
        setRotation(tail1, 0F, 0F, 0F);
        bodyMain = new ModelRenderer(this, 106, 25);
        bodyMain.addBox(-6F, -6F, -6F, 12, 12, 12);
        bodyMain.setRotationPoint(0F, 0F, 0F);
        bodyMain.setTextureSize(256, 128);
        bodyMain.mirror = true;
        setRotation(bodyMain, 0F, 0F, 0F);
        neck = new ModelRenderer(this, 154, 25);
        neck.addBox(-5.5F, -10.5F, -3F, 11, 11, 10);
        neck.setRotationPoint(0F, 0F, 0F);
        neck.setTextureSize(256, 128);
        neck.mirror = true;
        setRotation(neck, 0F, 0F, 0F);
        
        if (scale > 0)
        {
            head = new ModelRenderer(this, 156, 0);
            head.addBox(-3.5F, -14.7F, 1.5F, 7, 7, 7);
            head.setRotationPoint(0F, 0F, 0F);
            head.setTextureSize(256, 128);
            head.mirror = true;
            setRotation(head, 0F, 0F, 0F);
            neck = new ModelRenderer(this, 122, 0);
            neck.addBox(-4.5F, -9.5F, -2F, 9, 9, 8);
            neck.setRotationPoint(0F, 0F, 0F);
            neck.setTextureSize(256, 128);
            neck.mirror = true;
            setRotation(neck, 0F, 0F, 0F);
            bodyMain = new ModelRenderer(this, 82, 0);
            bodyMain.addBox(-5F, -5F, -5F, 10, 10, 10);
            bodyMain.setRotationPoint(0F, 0F, 0F);
            bodyMain.setTextureSize(256, 128);
            bodyMain.mirror = true;
            setRotation(bodyMain, 0F, 0F, 0F);
            tail1 = new ModelRenderer(this, 48, 0);
            tail1.addBox(-4.5F, -2F, -10F, 9, 7, 8);
            tail1.setRotationPoint(0F, 0F, 0F);
            tail1.setTextureSize(256, 128);
            tail1.mirror = true;
            setRotation(tail1, 0F, 0F, 0F);
            tail2 = new ModelRenderer(this, 20, 0);
            tail2.addBox(-3.5F, 0F, -14F, 7, 5, 7);
            tail2.setRotationPoint(0F, 0F, 0F);
            tail2.setTextureSize(256, 128);
            tail2.mirror = true;
            setRotation(tail2, 0F, 0F, 0F);
            tail3 = new ModelRenderer(this, 0, 0);
            tail3.addBox(-2.5F, 2F, -16F, 5, 3, 5);
            tail3.setRotationPoint(0F, 0F, 0F);
            tail3.setTextureSize(256, 128);
            tail3.mirror = true;
            setRotation(tail3, 0F, 0F, 0F);
        }

        this.bodyMain.addChild(this.tail1);
        this.neck.addChild(this.head);
        this.tail1.addChild(this.tail2);
        this.tail2.addChild(this.tail3);
        this.bodyMain.addChild(this.neck);
    }

    @Override
    public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        super.render(entity, par2, par3, par4, par5, par6, par7);
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
        bodyMain.render(par7);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) 
    {
        this.tail1.rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 0.2F * par2;
        this.tail2.rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 0.2F * par2;
        this.tail3.rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 0.2F * par2;
        this.tail1.offsetZ = MathHelper.cos(0.5F * par1 * 0.6662F) * 0.2F * par2;
        this.tail2.offsetZ = MathHelper.cos(0.5F * par1 * 0.6662F) * 0.2F * par2;
        this.tail3.offsetZ = MathHelper.cos(0.5F * par1 * 0.6662F) * 0.2F * par2;
        this.neck.offsetZ = -MathHelper.cos(0.5F * par1 * 0.6662F) * 0.1F * par2;
        this.head.offsetZ = -MathHelper.cos(0.5F * par1 * 0.6662F) * 0.1F * par2;
    }
}
