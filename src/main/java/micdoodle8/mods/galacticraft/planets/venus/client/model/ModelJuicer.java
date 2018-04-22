package micdoodle8.mods.galacticraft.planets.venus.client.model;

import micdoodle8.mods.galacticraft.planets.venus.entities.EntityJuicer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelJuicer extends ModelBase
{
    private ModelRenderer body1;
    private ModelRenderer body2;
    private ModelRenderer head;
    private ModelRenderer legRightFront1;
    private ModelRenderer legRightFront2;
    private ModelRenderer legLeftFront1;
    private ModelRenderer legLeftFront2;
    private ModelRenderer legRightMidFront1;
    private ModelRenderer legRightMidFront2;
    private ModelRenderer legLeftMidFront1;
    private ModelRenderer legLeftMidFront2;
    private ModelRenderer legRightMidBack1;
    private ModelRenderer legRightMidBack2;
    private ModelRenderer legLeftMidBack1;
    private ModelRenderer legLeftMidBack2;
    private ModelRenderer legRightBack1;
    private ModelRenderer legRightBack2;
    private ModelRenderer legLeftBack1;
    private ModelRenderer legLeftBack2;
    private ModelRenderer back;
    private ModelRenderer tail0;
    private ModelRenderer tail1;
    private ModelRenderer stinger;

    private float legLength0;

    public ModelJuicer()
    {
        textureWidth = 64;
        textureHeight = 32;

        body1 = new ModelRenderer(this, 24, 22);
        body1.addBox(-3F, -1F, -4F, 6, 2, 8);
        body1.setRotationPoint(0F, 0F, 0F);
        body1.setTextureSize(64, 32);
        body1.mirror = true;
        setRotation(body1, 0F, 0F, 0F);
        body2 = new ModelRenderer(this, 0, 22);
        body2.addBox(-2.5F, -1.5F, -3.5F, 5, 3, 7);
        body2.setRotationPoint(0F, 0F, 0F);
        body2.setTextureSize(64, 32);
        body2.mirror = true;
        setRotation(body2, 0F, 0F, 0F);

        head = new ModelRenderer(this, 52, 0);
        head.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
        head.setRotationPoint(0F, -1F, -4F);
        head.setTextureSize(64, 32);
        head.mirror = true;
        setRotation(head, -0.3717861F, 0F, 0F);

        legRightFront1 = new ModelRenderer(this, 44, 0);
        legRightFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        legRightFront1.setRotationPoint(-2.5F, 0F, -3F);
        legRightFront1.setTextureSize(64, 32);
        legRightFront1.mirror = true;
        setRotation(legRightFront1, 0F, 2.453788F, 1.115358F);
        legRightFront2 = new ModelRenderer(this, 30, 0);
        legRightFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        legRightFront2.setRotationPoint(-3.5F, -3F, -4F);
        legRightFront2.setTextureSize(64, 32);
        legRightFront2.mirror = true;
        setRotation(legRightFront2, 0F, 2.825574F, -0.8551081F);

        legLeftFront1 = new ModelRenderer(this, 44, 0);
        legLeftFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        legLeftFront1.setRotationPoint(2.5F, 0F, -3F);
        legLeftFront1.setTextureSize(64, 32);
        legLeftFront1.mirror = true;
        setRotation(legLeftFront1, 0F, 0.7807508F, -1.115358F);
        legLeftFront2 = new ModelRenderer(this, 30, 0);
        legLeftFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        legLeftFront2.setRotationPoint(3.5F, -3F, -4F);
        legLeftFront2.setTextureSize(64, 32);
        legLeftFront2.mirror = true;
        setRotation(legLeftFront2, 0F, 0.7063936F, 0.8551081F);

        legRightMidFront1 = new ModelRenderer(this, 44, 0);
        legRightMidFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        legRightMidFront1.setRotationPoint(-2.5F, 0F, -1F);
        legRightMidFront1.setTextureSize(64, 32);
        legRightMidFront1.mirror = true;
        setRotation(legRightMidFront1, 0F, 2.93711F, 1.115358F);
        legRightMidFront2 = new ModelRenderer(this, 30, 0);
        legRightMidFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        legRightMidFront2.setRotationPoint(-3.5F, -3F, -1F);
        legRightMidFront2.setTextureSize(64, 32);
        legRightMidFront2.mirror = true;
        setRotation(legRightMidFront2, 0F, 3.011467F, -0.8551081F);

        legLeftMidFront1 = new ModelRenderer(this, 44, 0);
        legLeftMidFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        legLeftMidFront1.setRotationPoint(2.5F, 0F, -1F);
        legLeftMidFront1.setTextureSize(64, 32);
        legLeftMidFront1.mirror = true;
        setRotation(legLeftMidFront1, 0F, 0.1858931F, -1.115358F);
        legLeftMidFront2 = new ModelRenderer(this, 30, 0);
        legLeftMidFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        legLeftMidFront2.setRotationPoint(3.5F, -3F, -1F);
        legLeftMidFront2.setTextureSize(64, 32);
        legLeftMidFront2.mirror = true;
        setRotation(legLeftMidFront2, 0F, 0.3346075F, 0.8551081F);

        legRightMidBack1 = new ModelRenderer(this, 44, 0);
        legRightMidBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        legRightMidBack1.setRotationPoint(-2.5F, 0F, 1F);
        legRightMidBack1.setTextureSize(64, 32);
        legRightMidBack1.mirror = true;
        setRotation(legRightMidBack1, 0F, -3.030057F, 1.115358F);
        legRightMidBack2 = new ModelRenderer(this, 30, 0);
        legRightMidBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        legRightMidBack2.setRotationPoint(-3.5F, -3F, 1F);
        legRightMidBack2.setTextureSize(64, 32);
        legRightMidBack2.mirror = true;
        setRotation(legRightMidBack2, 0F, -2.974289F, -0.8551081F);

        legLeftMidBack1 = new ModelRenderer(this, 44, 0);
        legLeftMidBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        legLeftMidBack1.setRotationPoint(2.5F, 0F, 1F);
        legLeftMidBack1.setTextureSize(64, 32);
        legLeftMidBack1.mirror = true;
        setRotation(legLeftMidBack1, 0F, -0.0371786F, -1.115358F);
        legLeftMidBack2 = new ModelRenderer(this, 30, 0);
        legLeftMidBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        legLeftMidBack2.setRotationPoint(3.5F, -3F, 1F);
        legLeftMidBack2.setTextureSize(64, 32);
        legLeftMidBack2.mirror = true;
        setRotation(legLeftMidBack2, 0F, -0.1487144F, 0.8551081F);

        legRightBack1 = new ModelRenderer(this, 44, 0);
        legRightBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        legRightBack1.setRotationPoint(-2.5F, 0F, 3F);
        legRightBack1.setTextureSize(64, 32);
        legRightBack1.mirror = true;
        setRotation(legRightBack1, 0F, -2.658271F, 1.115358F);
        legRightBack2 = new ModelRenderer(this, 30, 0);
        legRightBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        legRightBack2.setRotationPoint(-3.5F, -3F, 4F);
        legRightBack2.setTextureSize(64, 32);
        legRightBack2.mirror = true;
        setRotation(legRightBack2, 0F, -2.788396F, -0.8551081F);

        legLeftBack1 = new ModelRenderer(this, 44, 0);
        legLeftBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        legLeftBack1.setRotationPoint(2.5F, 0F, 3F);
        legLeftBack1.setTextureSize(64, 32);
        legLeftBack1.mirror = true;
        setRotation(legLeftBack1, 0F, -0.3346075F, -1.115358F);
        legLeftBack2 = new ModelRenderer(this, 30, 0);
        legLeftBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        legLeftBack2.setRotationPoint(3.5F, -3F, 3F);
        legLeftBack2.setTextureSize(64, 32);
        legLeftBack2.mirror = true;
        setRotation(legLeftBack2, 0F, -0.5205006F, 0.8551081F);

        back = new ModelRenderer(this, 0, 16);
        back.addBox(-1.5F, -0.5F, -2.5F, 3, 1, 5);
        back.setRotationPoint(0F, -1.5F, 0F);
        back.setTextureSize(64, 32);
        back.mirror = true;
        setRotation(back, 0F, 0F, 0F);

        tail0 = new ModelRenderer(this, 42, 17);
        tail0.addBox(-0.5F, -0.5F, 0F, 1, 1, 4);
        tail0.setRotationPoint(0F, -2F, 0F);
        tail0.setTextureSize(64, 32);
        tail0.mirror = true;
        setRotation(tail0, 0.5205006F, 0F, 0F);
        tail1 = new ModelRenderer(this, 42, 12);
        tail1.addBox(-0.5F, 0F, 0F, 1, 1, 4);
        tail1.setRotationPoint(0F, -3F, 3F);
        tail1.setTextureSize(64, 32);
        tail1.mirror = true;
        setRotation(tail1, 2.659407F, 0F, 0F);
        stinger = new ModelRenderer(this, 48, 9);
        stinger.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        stinger.setRotationPoint(0F, -5.2F, -1.133333F);
        stinger.setTextureSize(64, 32);
        stinger.mirror = true;
        setRotation(stinger, 1.487144F, 0F, 0F);

        convertToChild(legLeftFront1, legLeftFront2);
        convertToChild(legLeftMidFront1, legLeftMidFront2);
        convertToChild(legLeftMidBack1, legLeftMidBack2);
        convertToChild(legLeftBack1, legLeftBack2);
        convertToChild(legRightFront1, legRightFront2);
        convertToChild(legRightMidFront1, legRightMidFront2);
        convertToChild(legRightMidBack1, legRightMidBack2);
        convertToChild(legRightBack1, legRightBack2);

        convertToChild(tail0, tail1);
        convertToChild(tail1, stinger);

        this.legLength0 = this.legLeftFront1.cubeList.get(0).posX2 - this.legLeftFront1.cubeList.get(0).posX1;
    }

    private void convertToChild(ModelRenderer parent, ModelRenderer child)
    {
        // move child rotation point to be relative to parent
        child.rotationPointX -= parent.rotationPointX;
        child.rotationPointY -= parent.rotationPointY;
        child.rotationPointZ -= parent.rotationPointZ;
        // make rotations relative to parent
        child.rotateAngleX -= parent.rotateAngleX;
        child.rotateAngleY -= parent.rotateAngleY;
        child.rotateAngleZ -= parent.rotateAngleZ;
        // create relationship
        parent.addChild(child);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        body1.render(f5);
        body2.render(f5);
        head.render(f5);
        legRightMidBack1.render(f5);
        legRightFront1.render(f5);
        legRightMidFront1.render(f5);
        legRightBack1.render(f5);
        legLeftBack1.render(f5);
        legLeftMidBack1.render(f5);
        legLeftMidFront1.render(f5);
        legLeftFront1.render(f5);
        back.render(f5);
        tail0.render(f5);
    }

    private void copyLegAngles(ModelRenderer parent, ModelRenderer child)
    {
        child.rotationPointX = this.legLength0 * (parent.rotationPointX < 0.0F ? -1.0F : 1.0F) * (MathHelper.sin(parent.rotateAngleX) + MathHelper.cos(parent.rotateAngleY));
        child.rotationPointY = 0.0F;
        child.rotationPointZ = 0.0F;
    }

    private void copyLeftToRight(ModelRenderer left, ModelRenderer right)
    {
        right.rotateAngleX = left.rotateAngleX;
        right.rotateAngleY = -left.rotateAngleY;
        right.rotateAngleZ = left.rotateAngleZ;
    }

    private void copyLeg1LeftToRight(ModelRenderer left, ModelRenderer right)
    {
        right.rotateAngleX = left.rotateAngleX;
        right.rotateAngleY = (float) (Math.PI - left.rotateAngleY);
        right.rotateAngleZ = -left.rotateAngleZ;
    }

    @Override
    public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6, Entity entityIn)
    {
        EntityJuicer juicer = (EntityJuicer) entityIn;
        super.setRotationAngles(f1, f2, f3, f4, f5, f6, entityIn);
        float movement = f1;
        float increment = -1.0F;
        float offset = 0.5F;
        this.legLeftFront1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.legLeftFront1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        this.legLeftFront2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        this.legLeftFront2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
        offset = 0.05F;
        movement += increment;
        this.legLeftMidFront1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.legLeftMidFront1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        this.legLeftMidFront2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        this.legLeftMidFront2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
        offset = -0.1F;
        movement += increment;
        this.legLeftMidBack1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.legLeftMidBack1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        this.legLeftMidBack2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        this.legLeftMidBack2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
        offset = -0.5F;
        movement += increment;
        this.legLeftBack1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.legLeftBack1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        this.legLeftBack2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        this.legLeftBack2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);

        this.copyLegAngles(legLeftFront1, legLeftFront2);
        this.copyLegAngles(legLeftMidFront1, legLeftMidFront2);
        this.copyLegAngles(legLeftMidBack1, legLeftMidBack2);
        this.copyLegAngles(legLeftBack1, legLeftBack2);
        this.copyLegAngles(legRightFront1, legRightFront2);
        this.copyLegAngles(legRightMidFront1, legRightMidFront2);
        this.copyLegAngles(legRightMidBack1, legRightMidBack2);
        this.copyLegAngles(legRightBack1, legRightBack2);

        this.copyLeg1LeftToRight(legLeftFront1, legRightFront1);
        this.copyLeg1LeftToRight(legLeftMidFront1, legRightMidFront1);
        this.copyLeg1LeftToRight(legLeftMidBack1, legRightMidBack1);
        this.copyLeg1LeftToRight(legLeftBack1, legRightBack1);
        this.copyLeftToRight(legLeftFront2, legRightFront2);
        this.copyLeftToRight(legLeftMidFront2, legRightMidFront2);
        this.copyLeftToRight(legLeftMidBack2, legRightMidBack2);
        this.copyLeftToRight(legLeftBack2, legRightBack2);

        this.tail0.rotationPointY = -1.5F;
        this.tail0.offsetY = 0.0F;
//        this.tail0.rotateAngleX = MathHelper.cos(movement) * 0.2F + 0.5205006F;
        this.tail0.rotateAngleX = juicer.attackingPlayer != null ? 0.52F : 0.1F;
        this.tail0.rotateAngleY = 0.0F;
        this.tail0.rotateAngleZ = 0.0F;
        this.tail1.rotateAngleX = juicer.attackingPlayer != null ? 2.659407F : 2.7F;
        this.tail1.rotationPointZ = 4 * (MathHelper.sin(this.tail0.rotateAngleZ) + MathHelper.cos(this.tail0.rotateAngleY));
        this.tail1.rotationPointY = 0.5F;
        this.tail1.rotationPointX = 0.0F;
        this.stinger.rotateAngleX = -this.tail0.rotateAngleX - (this.tail0.rotateAngleX - 2.659407F) - 0.5205006F;
        this.stinger.rotationPointZ = 4 * (MathHelper.sin(this.tail1.rotateAngleZ) + MathHelper.cos(this.tail1.rotateAngleY));
        this.stinger.rotationPointY = 0.5F;
        this.stinger.rotationPointX = 0.0F;
    }
}
