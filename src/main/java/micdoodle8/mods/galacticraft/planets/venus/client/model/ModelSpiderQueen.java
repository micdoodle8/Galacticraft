package micdoodle8.mods.galacticraft.planets.venus.client.model;

import micdoodle8.mods.galacticraft.core.client.model.ModelRendererGC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSpiderQueen extends ModelBase
{
    ModelRendererGC head;
    ModelRendererGC body;
    ModelRendererGC[] leg1 = new ModelRendererGC[3];
    ModelRendererGC[] leg2 = new ModelRendererGC[3];
    ModelRendererGC[] leg3 = new ModelRendererGC[3];
    ModelRendererGC[] leg4 = new ModelRendererGC[3];
    ModelRendererGC[] leg5 = new ModelRendererGC[3];
    ModelRendererGC[] leg6 = new ModelRendererGC[3];
    ModelRendererGC[] leg7 = new ModelRendererGC[3];
    ModelRendererGC[] leg8 = new ModelRendererGC[3];
    ModelRendererGC rearEnd;
    ModelRendererGC rearBack;
    ModelRendererGC rearLeft;
    ModelRendererGC rearRight;

    private float legLength0;

    public ModelSpiderQueen()
    {
        textureWidth = 64;
        textureHeight = 64;

        head = new ModelRendererGC(this, 32, 4);
        head.addBox(-4F, -4F, -8F, 8, 8, 8);
        head.setRotationPoint(0F, 20F, -3F);
        head.setTextureSize(64, 64);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
        body = new ModelRendererGC(this, 0, 0);
        body.addBox(-3F, -3F, -3F, 6, 6, 10);
        body.setRotationPoint(0F, 20F, 0F);
        body.setTextureSize(64, 64);
        body.mirror = true;
        setRotation(body, 0F, 0F, 0F);

        leg1[0] = new ModelRendererGC(this, 28, 0);
        leg1[0].addBox(0F, -1F, -1F, 10, 2, 2);
        leg1[0].setRotationPoint(-3F, 20F, 4F);
        leg1[0].setTextureSize(64, 64);
        leg1[0].mirror = true;
        setRotation(leg1[0], 0F, 3.631943F, -0.7330383F);
        leg1[0].mirror = true;
        leg1[1] = new ModelRendererGC(this, 28, 0);
        leg1[1].addBox(0F, -1F, -1F, 8, 2, 2);
        leg1[1].setRotationPoint(-9.7F, 13.2F, 7.6F);
        leg1[1].setTextureSize(64, 64);
        leg1[1].mirror = true;
        setRotation(leg1[1], 0F, 3.631937F, 0.3823201F);
        leg1[2] = new ModelRendererGC(this, 28, 0);
        leg1[2].addBox(0F, -1F, -1F, 8, 2, 2);
        leg1[2].setRotationPoint(-16.4F, 16.2F, 11.1F);
        leg1[2].setTextureSize(64, 64);
        leg1[2].mirror = true;
        setRotation(leg1[2], 0F, 3.631937F, 1.461656F);

        leg2[0] = new ModelRendererGC(this, 28, 0);
        leg2[0].addBox(0F, -1F, -1F, 10, 2, 2);
        leg2[0].setRotationPoint(3F, 20F, 4F);
        leg2[0].setTextureSize(64, 64);
        leg2[0].mirror = true;
        setRotation(leg2[0], 0F, -0.4903446F, -0.7330383F);
        leg2[1] = new ModelRendererGC(this, 28, 0);
        leg2[1].addBox(0F, -1F, -1F, 8, 2, 2);
        leg2[1].setRotationPoint(9.666667F, 13.2F, 7.6F);
        leg2[1].setTextureSize(64, 64);
        leg2[1].mirror = true;
        setRotation(leg2[1], 0F, -0.4903503F, 0.3823201F);
        leg2[2] = new ModelRendererGC(this, 28, 0);
        leg2[2].addBox(0F, -1F, -1F, 8, 2, 2);
        leg2[2].setRotationPoint(16.4F, 16.2F, 11.1F);
        leg2[2].setTextureSize(64, 64);
        leg2[2].mirror = true;
        setRotation(leg2[2], 0F, -0.4903503F, 1.461656F);

        leg3[0] = new ModelRendererGC(this, 28, 0);
        leg3[0].addBox(0F, -1F, -1F, 10, 2, 2);
        leg3[0].setRotationPoint(-3F, 20F, 2F);
        leg3[0].setTextureSize(64, 64);
        leg3[0].mirror = true;
        setRotation(leg3[0], 0F, 3.335237F, -0.7330383F);
        leg3[1] = new ModelRendererGC(this, 28, 0);
        leg3[1].addBox(0F, -1F, -1F, 8, 2, 2);
        leg3[1].setRotationPoint(-10.7F, 13.2F, 3.6F);
        leg3[1].setTextureSize(64, 64);
        leg3[1].mirror = true;
        setRotation(leg3[1], 0F, 3.335231F, 0.3823201F);
        leg3[1].mirror = false;
        leg3[2] = new ModelRendererGC(this, 28, 0);
        leg3[2].addBox(0F, -1F, -1F, 8, 2, 2);
        leg3[2].setRotationPoint(-17.6F, 16.2F, 4.9F);
        leg3[2].setTextureSize(64, 64);
        leg3[2].mirror = true;
        setRotation(leg3[2], 0F, 3.335231F, 1.461656F);

        leg4[0] = new ModelRendererGC(this, 28, 0);
        leg4[0].addBox(0F, -1F, -1F, 10, 2, 2);
        leg4[0].setRotationPoint(3F, 20F, 2F);
        leg4[0].setTextureSize(64, 64);
        leg4[0].mirror = true;
        setRotation(leg4[0], 0F, -0.1936386F, -0.7330383F);
        leg4[1] = new ModelRendererGC(this, 28, 0);
        leg4[1].addBox(0F, -1F, -1F, 8, 2, 2);
        leg4[1].setRotationPoint(10.66667F, 13.2F, 3.6F);
        leg4[1].setTextureSize(64, 64);
        leg4[1].mirror = true;
        setRotation(leg4[1], 0F, -0.1936443F, 0.3823201F);
        leg4[2] = new ModelRendererGC(this, 28, 0);
        leg4[2].addBox(0F, -1F, -1F, 8, 2, 2);
        leg4[2].setRotationPoint(17.6F, 16.2F, 4.9F);
        leg4[2].setTextureSize(64, 64);
        leg4[2].mirror = true;
        setRotation(leg4[2], 0F, -0.1936443F, 1.461656F);

        leg5[0] = new ModelRendererGC(this, 28, 0);
        leg5[0].addBox(0F, -1F, -1F, 10, 2, 2);
        leg5[0].setRotationPoint(-3F, 20F, 0.5F);
        leg5[0].setTextureSize(64, 64);
        leg5[0].mirror = true;
        setRotation(leg5[0], 0F, 2.7838F, -0.7330383F);
        leg5[1] = new ModelRendererGC(this, 28, 0);
        leg5[1].addBox(0F, -1F, -1F, 8, 2, 2);
        leg5[1].setRotationPoint(-10F, 13.2F, -2.2F);
        leg5[1].setTextureSize(64, 64);
        leg5[1].mirror = true;
        setRotation(leg5[1], 0F, 2.783794F, 0.3823201F);
        leg5[1].mirror = false;
        leg5[2] = new ModelRendererGC(this, 28, 0);
        leg5[2].addBox(0F, -1F, -1F, 8, 2, 2);
        leg5[2].setRotationPoint(-16.5F, 16.2F, -4.7F);
        leg5[2].setTextureSize(64, 64);
        leg5[2].mirror = true;
        setRotation(leg5[2], 0F, 2.783794F, 1.461656F);

        leg6[0] = new ModelRendererGC(this, 28, 0);
        leg6[0].addBox(0F, -1F, -1F, 10, 2, 2);
        leg6[0].setRotationPoint(3F, 20F, 0.5F);
        leg6[0].setTextureSize(64, 64);
        leg6[0].mirror = true;
        setRotation(leg6[0], 0F, 0.3648668F, -0.7330383F);
        leg6[1] = new ModelRendererGC(this, 28, 0);
        leg6[1].addBox(0F, -1F, -1F, 8, 2, 2);
        leg6[1].setRotationPoint(10F, 13.2F, -2.2F);
        leg6[1].setTextureSize(64, 64);
        leg6[1].mirror = true;
        setRotation(leg6[1], 0F, 0.3648611F, 0.3823201F);
        leg6[2] = new ModelRendererGC(this, 28, 0);
        leg6[2].addBox(0F, -1F, -1F, 8, 2, 2);
        leg6[2].setRotationPoint(16.53333F, 16.2F, -4.7F);
        leg6[2].setTextureSize(64, 64);
        leg6[2].mirror = true;
        setRotation(leg6[2], 0F, 0.3648611F, 1.461656F);

        leg7[0] = new ModelRendererGC(this, 28, 0);
        leg7[0].addBox(0F, -1F, -1F, 10, 2, 2);
        leg7[0].setRotationPoint(-3F, 20F, -2F);
        leg7[0].setTextureSize(64, 64);
        leg7[0].mirror = true;
        setRotation(leg7[0], 0F, 2.495821F, -0.7330383F);
        leg7[1] = new ModelRendererGC(this, 28, 0);
        leg7[1].addBox(0F, -1F, -1F, 8, 2, 2);
        leg7[1].setRotationPoint(-8.8F, 13.33333F, -6.466667F);
        leg7[1].setTextureSize(64, 64);
        leg7[1].mirror = true;
        setRotation(leg7[1], 0F, 2.495821F, 0.4206553F);
        leg7[2] = new ModelRendererGC(this, 28, 0);
        leg7[2].addBox(0F, -1F, -1F, 8, 2, 2);
        leg7[2].setRotationPoint(-14.5F, 16.66667F, -10.9F);
        leg7[2].setTextureSize(64, 64);
        leg7[2].mirror = true;
        setRotation(leg7[2], 0F, 2.495821F, 1.201406F);

        leg8[0] = new ModelRendererGC(this, 28, 0);
        leg8[0].addBox(0F, -1F, -1F, 10, 2, 2);
        leg8[0].setRotationPoint(3F, 20F, -2F);
        leg8[0].setTextureSize(64, 64);
        leg8[0].mirror = true;
        setRotation(leg8[0], 0F, 0.6615727F, -0.7330383F);
        leg8[1] = new ModelRendererGC(this, 28, 0);
        leg8[1].addBox(0F, -1F, -1F, 8, 2, 2);
        leg8[1].setRotationPoint(8.8F, 13.33333F, -6.466667F);
        leg8[1].setTextureSize(64, 64);
        leg8[1].mirror = true;
        setRotation(leg8[1], 0F, 0.6368846F, 0.4206553F);
        leg8[2] = new ModelRendererGC(this, 28, 0);
        leg8[2].addBox(0F, -1F, -1F, 8, 2, 2);
        leg8[2].setRotationPoint(14.53333F, 16.66667F, -10.9F);
        leg8[2].setTextureSize(64, 64);
        leg8[2].mirror = true;
        setRotation(leg8[2], 0F, 0.6615671F, 1.201406F);

        rearEnd = new ModelRendererGC(this, 0, 41);
        rearEnd.addBox(-6F, -7F, -6F, 12, 11, 12);
        rearEnd.setRotationPoint(0F, 20F, 13F);
        rearEnd.setTextureSize(64, 64);
        rearEnd.mirror = true;
        setRotation(rearEnd, 0F, 0F, 0F);
        rearBack = new ModelRendererGC(this, 26, 31);
        rearBack.addBox(-5F, -6F, 6F, 10, 9, 1);
        rearBack.setRotationPoint(0F, 20F, 13F);
        rearBack.setTextureSize(64, 64);
        rearBack.mirror = true;
        setRotation(rearBack, 0F, 0F, 0F);
        rearLeft = new ModelRendererGC(this, 0, 22);
        rearLeft.addBox(6F, -6F, -5F, 1, 9, 10);
        rearLeft.setRotationPoint(0F, 20F, 13F);
        rearLeft.setTextureSize(64, 64);
        rearLeft.mirror = true;
        setRotation(rearLeft, 0F, 0F, 0F);
        rearRight = new ModelRendererGC(this, 0, 22);
        rearRight.addBox(-7F, -6F, -5F, 1, 9, 10);
        rearRight.setRotationPoint(0F, 20F, 13F);
        rearRight.setTextureSize(64, 64);
        rearRight.mirror = true;
        setRotation(rearRight, 0F, 0F, 0F);

        for (int i = 0; i < 2; ++i)
        {
            this.convertToChild(this.leg1[i], this.leg1[i + 1]);
            this.convertToChild(this.leg2[i], this.leg2[i + 1]);
            this.convertToChild(this.leg3[i], this.leg3[i + 1]);
            this.convertToChild(this.leg4[i], this.leg4[i + 1]);
            this.convertToChild(this.leg5[i], this.leg5[i + 1]);
            this.convertToChild(this.leg6[i], this.leg6[i + 1]);
            this.convertToChild(this.leg7[i], this.leg7[i + 1]);
            this.convertToChild(this.leg8[i], this.leg8[i + 1]);
        }

        this.legLength0 = this.leg1[0].cubeList.get(0).posX2 - this.leg1[0].cubeList.get(0).posX1;
    }

    private void convertToChild(ModelRendererGC parent, ModelRendererGC child)
    {
//        // move child rotation point to be relative to parent
//        child.rotationPointX -= parent.rotationPointX;
//        child.rotationPointY -= parent.rotationPointY;
//        child.rotationPointZ -= parent.rotationPointZ;
//        // make rotations relative to parent
//        child.rotateAngleX -= parent.rotateAngleX;
//        child.rotateAngleY -= parent.rotateAngleY;
//        child.rotateAngleZ -= parent.rotateAngleZ;
//        // create relationship
//        parent.addChild(child);
    }

    private void setRotation(ModelRendererGC model, float x, float y, float z)
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
        head.render(f5);
        body.render(f5);
        for (int i = 0; i < 3; ++i)
        {
            this.leg1[i].render(f5);
            this.leg2[i].render(f5);
            this.leg3[i].render(f5);
            this.leg4[i].render(f5);
            this.leg5[i].render(f5);
            this.leg6[i].render(f5);
            this.leg7[i].render(f5);
            this.leg8[i].render(f5);
        }
        rearEnd.render(f5);
        rearBack.render(f5);
        rearLeft.render(f5);
        rearRight.render(f5);
    }

    private void copyLegAngles(float length, ModelRendererGC parent, ModelRendererGC child)
    {
        child.rotationPointX = parent.rotationPointX + length * (MathHelper.cos(parent.rotateAngleZ) * MathHelper.cos(parent.rotateAngleY));
        child.rotationPointY = parent.rotationPointY + length * (MathHelper.sin(parent.rotateAngleZ));
        child.rotationPointZ = parent.rotationPointZ + (length) * (-MathHelper.sin(parent.rotateAngleY) * MathHelper.cos(parent.rotateAngleZ));
    }

    private void copyLeftToRight(ModelRendererGC left, ModelRendererGC right)
    {
        right.rotateAngleX = left.rotateAngleX;
        right.rotateAngleY = -left.rotateAngleY;
        right.rotateAngleZ = left.rotateAngleZ;
    }

    private void copyLeg1LeftToRight(ModelRendererGC left, ModelRendererGC right)
    {
        right.rotateAngleX = left.rotateAngleX;
        right.rotateAngleY = (float) (Math.PI - left.rotateAngleY);
        right.rotateAngleZ = left.rotateAngleZ;
    }

    @Override
    public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6, Entity entityIn)
    {
        float movement = f1;
        float increment = -1.0F;
        float offset = -0.4903446F;
        this.leg2[0].rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.leg2[0].rotateAngleZ = -0.7330383F;
        offset = -0.1936386F;
        movement += increment;
        this.leg4[0].rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.leg4[0].rotateAngleZ = -0.7330383F;
        offset = 0.3648668F;
        movement += increment;
        this.leg6[0].rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.leg6[0].rotateAngleZ = -0.7330383F;
        offset = 0.6615727F;
        movement += increment;
        this.leg8[0].rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.leg8[0].rotateAngleZ = -0.7330383F;

        float updist = -0.5F;

        // Move legs up if they are being moved forward. dx/dy of cos(movement) is -sin(movement)
        movement = f1;
        if (-MathHelper.sin(movement) * 0.2 > 0.0F)
        {
            this.leg2[0].rotateAngleZ = -0.7330383F + -MathHelper.sin(movement) * updist;
        }
        movement += increment;
        if (-MathHelper.sin(movement) * 0.2 > 0.0F)
        {
            this.leg4[0].rotateAngleZ = -0.7330383F + -MathHelper.sin(movement) * updist;
        }
        movement += increment;
        if (-MathHelper.sin(movement) * 0.2 > 0.0F)
        {
            this.leg6[0].rotateAngleZ = -0.7330383F + -MathHelper.sin(movement) * updist;
        }
        movement += increment;
        if (-MathHelper.sin(movement) * 0.2 > 0.0F)
        {
            this.leg8[0].rotateAngleZ = -0.7330383F + -MathHelper.sin(movement) * updist;
        }

        for (int i = 1; i < 3; ++i)
        {
            this.leg1[i].rotateAngleY = this.leg1[0].rotateAngleY;
            this.leg2[i].rotateAngleY = this.leg2[0].rotateAngleY;
            this.leg3[i].rotateAngleY = this.leg3[0].rotateAngleY;
            this.leg4[i].rotateAngleY = this.leg4[0].rotateAngleY;
            this.leg5[i].rotateAngleY = this.leg5[0].rotateAngleY;
            this.leg6[i].rotateAngleY = this.leg6[0].rotateAngleY;
            this.leg7[i].rotateAngleY = this.leg7[0].rotateAngleY;
            this.leg8[i].rotateAngleY = this.leg8[0].rotateAngleY;

            this.leg1[i].rotateAngleX = this.leg1[0].rotateAngleX;
            this.leg2[i].rotateAngleX = this.leg2[0].rotateAngleX;
            this.leg3[i].rotateAngleX = this.leg3[0].rotateAngleX;
            this.leg4[i].rotateAngleX = this.leg4[0].rotateAngleX;
            this.leg5[i].rotateAngleX = this.leg5[0].rotateAngleX;
            this.leg6[i].rotateAngleX = this.leg6[0].rotateAngleX;
            this.leg7[i].rotateAngleX = this.leg7[0].rotateAngleX;
            this.leg8[i].rotateAngleX = this.leg8[0].rotateAngleX;

//            this.leg1[i].rotateAngleZ = this.leg1[0].rotateAngleZ;
//            this.leg2[i].rotateAngleZ = this.leg2[0].rotateAngleZ;
//            this.leg3[i].rotateAngleZ = this.leg3[0].rotateAngleZ;
//            this.leg4[i].rotateAngleZ = this.leg4[0].rotateAngleZ;
//            this.leg5[i].rotateAngleZ = this.leg5[0].rotateAngleZ;
//            this.leg6[i].rotateAngleZ = this.leg6[0].rotateAngleZ;
//            this.leg7[i].rotateAngleZ = this.leg7[0].rotateAngleZ;
//            this.leg8[i].rotateAngleZ = this.leg8[0].rotateAngleZ;
        }

        for (int i = 0; i < 1; ++i)
        {
            this.copyLeg1LeftToRight(this.leg2[i], this.leg1[i]);
            this.copyLeg1LeftToRight(this.leg4[i], this.leg3[i]);
            this.copyLeg1LeftToRight(this.leg6[i], this.leg5[i]);
            this.copyLeg1LeftToRight(this.leg8[i], this.leg7[i]);
        }
        for (int i = 1; i < 2; ++i)
        {
            float length1a = 10.0F;
            float length1b = 10.0F;
            this.copyLegAngles(length1a, this.leg1[0], this.leg1[1]);
            this.copyLegAngles(length1b, this.leg2[0], this.leg2[1]);
            this.copyLegAngles(length1a, this.leg3[0], this.leg3[1]);
            this.copyLegAngles(length1b, this.leg4[0], this.leg4[1]);
            this.copyLegAngles(length1a, this.leg5[0], this.leg5[1]);
            this.copyLegAngles(length1b, this.leg6[0], this.leg6[1]);
            this.copyLegAngles(length1a, this.leg7[0], this.leg7[1]);
            this.copyLegAngles(length1b, this.leg8[0], this.leg8[1]);

            float length2a = 8.0F;
            float length2b = 8.0F;
            this.copyLegAngles(length2a, this.leg1[1], this.leg1[2]);
            this.copyLegAngles(length2b, this.leg2[1], this.leg2[2]);
            this.copyLegAngles(length2a, this.leg3[1], this.leg3[2]);
            this.copyLegAngles(length2b, this.leg4[1], this.leg4[2]);
            this.copyLegAngles(length2a, this.leg5[1], this.leg5[2]);
            this.copyLegAngles(length2b, this.leg6[1], this.leg6[2]);
            this.copyLegAngles(length2a, this.leg7[1], this.leg7[2]);
            this.copyLegAngles(length2b, this.leg8[1], this.leg8[2]);
        }
//        EntityJuicer juicer = (EntityJuicer) entityIn;
//        super.setRotationAngles(f1, f2, f3, f4, f5, f6, entityIn);
//        float movement = f1;
//        float increment = -1.0F;
//        float offset = 0.5F;
//        this.legLeftFront1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
//        this.legLeftFront1.rotateAngleZ = (float) (-Math.PI / 3.0F);
//        this.legLeftFront2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
//        this.legLeftFront2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
//        offset = 0.05F;
//        movement += increment;
//        this.legLeftMidFront1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
//        this.legLeftMidFront1.rotateAngleZ = (float) (-Math.PI / 3.0F);
//        this.legLeftMidFront2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
//        this.legLeftMidFront2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
//        offset = -0.1F;
//        movement += increment;
//        this.legLeftMidBack1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
//        this.legLeftMidBack1.rotateAngleZ = (float) (-Math.PI / 3.0F);
//        this.legLeftMidBack2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
//        this.legLeftMidBack2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
//        offset = -0.5F;
//        movement += increment;
//        this.legLeftBack1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
//        this.legLeftBack1.rotateAngleZ = (float) (-Math.PI / 3.0F);
//        this.legLeftBack2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
//        this.legLeftBack2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
//
//        this.copyLegAngles(legLeftFront1, legLeftFront2);
//        this.copyLegAngles(legLeftMidFront1, legLeftMidFront2);
//        this.copyLegAngles(legLeftMidBack1, legLeftMidBack2);
//        this.copyLegAngles(legLeftBack1, legLeftBack2);
//        this.copyLegAngles(legRightFront1, legRightFront2);
//        this.copyLegAngles(legRightMidFront1, legRightMidFront2);
//        this.copyLegAngles(legRightMidBack1, legRightMidBack2);
//        this.copyLegAngles(legRightBack1, legRightBack2);
//
//        this.copyLeg1LeftToRight(legLeftFront1, legRightFront1);
//        this.copyLeg1LeftToRight(legLeftMidFront1, legRightMidFront1);
//        this.copyLeg1LeftToRight(legLeftMidBack1, legRightMidBack1);
//        this.copyLeg1LeftToRight(legLeftBack1, legRightBack1);
//        this.copyLeftToRight(legLeftFront2, legRightFront2);
//        this.copyLeftToRight(legLeftMidFront2, legRightMidFront2);
//        this.copyLeftToRight(legLeftMidBack2, legRightMidBack2);
//        this.copyLeftToRight(legLeftBack2, legRightBack2);
    }
}
