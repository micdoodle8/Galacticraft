package micdoodle8.mods.galacticraft.core.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelAluminumWire extends ModelBase
{
    // fields
    ModelRenderer middle;
    ModelRenderer right;
    ModelRenderer left;
    ModelRenderer back;
    ModelRenderer front;
    ModelRenderer top;
    ModelRenderer bottom;

    public ModelAluminumWire()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.middle = new ModelRenderer(this, 0, 0);
        this.middle.addBox(-1F, -1F, -1F, 4, 4, 4);
        this.middle.setRotationPoint(-1F, 15F, -1F);
        this.middle.setTextureSize(64, 32);
        this.middle.mirror = true;
        this.setRotation(this.middle, 0F, 0F, 0F);
        this.right = new ModelRenderer(this, 21, 0);
        this.right.addBox(0F, 0F, 0F, 6, 4, 4);
        this.right.setRotationPoint(2F, 14F, -2F);
        this.right.setTextureSize(64, 32);
        this.right.mirror = true;
        this.setRotation(this.right, 0F, 0F, 0F);
        this.left = new ModelRenderer(this, 21, 0);
        this.left.addBox(0F, 0F, 0F, 6, 4, 4);
        this.left.setRotationPoint(-8F, 14F, -2F);
        this.left.setTextureSize(64, 32);
        this.left.mirror = true;
        this.setRotation(this.left, 0F, 0F, 0F);
        this.back = new ModelRenderer(this, 0, 11);
        this.back.addBox(0F, 0F, 0F, 4, 4, 6);
        this.back.setRotationPoint(-2F, 14F, 2F);
        this.back.setTextureSize(64, 32);
        this.back.mirror = true;
        this.setRotation(this.back, 0F, 0F, 0F);
        this.front = new ModelRenderer(this, 0, 11);
        this.front.addBox(0F, 0F, 0F, 4, 4, 6);
        this.front.setRotationPoint(-2F, 14F, -8F);
        this.front.setTextureSize(64, 32);
        this.front.mirror = true;
        this.setRotation(this.front, 0F, 0F, 0F);
        this.top = new ModelRenderer(this, 21, 11);
        this.top.addBox(0F, 0F, 0F, 4, 6, 4);
        this.top.setRotationPoint(-2F, 8F, -2F);
        this.top.setTextureSize(64, 32);
        this.top.mirror = true;
        this.setRotation(this.top, 0F, 0F, 0F);
        this.bottom = new ModelRenderer(this, 21, 11);
        this.bottom.addBox(0F, 0F, 0F, 4, 6, 4);
        this.bottom.setRotationPoint(-2F, 18F, -2F);
        this.bottom.setTextureSize(64, 32);
        this.bottom.mirror = true;
        this.setRotation(this.bottom, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.renderMiddle();
        this.renderBottom();
        this.renderTop();
        this.renderLeft();
        this.renderRight();
        this.renderBack();
        this.renderFront();
    }

    public void renderMiddle()
    {
        this.middle.render(0.0625F);
    }

    public void renderBottom()
    {
        this.bottom.render(0.0625F);
    }

    public void renderTop()
    {
        this.top.render(0.0625F);
    }

    public void renderLeft()
    {
        this.left.render(0.0625F);
    }

    public void renderRight()
    {
        this.right.render(0.0625F);
    }

    public void renderBack()
    {
        this.back.render(0.0625F);
    }

    public void renderFront()
    {
        // this.Front.render(0.0625F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float x, float y, float z, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(x, y, z, f3, f4, f5, entity);
    }
}
