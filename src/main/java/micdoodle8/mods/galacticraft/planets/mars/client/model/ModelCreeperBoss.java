package micdoodle8.mods.galacticraft.planets.mars.client.model;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCreeperBoss;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelCreeperBoss extends EntityModel<EntityCreeperBoss>
{
    RendererModel headMain;
    RendererModel bodyMain;
    RendererModel rightLegFront;
    RendererModel leftLegFront;
    RendererModel rightLeg;
    RendererModel leftLeg;
    RendererModel oxygenTank;
    RendererModel headLeft;
    RendererModel headRight;
    RendererModel neckRight;
    RendererModel neckLeft;

    public ModelCreeperBoss()
    {
        this(0.0F);
    }

    public ModelCreeperBoss(float scale)
    {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.neckRight = new RendererModel(this, 16, 20);
        this.neckRight.mirror = true;
        this.neckRight.addBox(-2.5F, -9F, -1.5F, 5, 9, 3, scale);
        this.neckRight.setRotationPoint(-3F, 10F, 0F);
        this.neckRight.setTextureSize(128, 64);
        this.neckRight.mirror = true;
        this.setRotation(this.neckRight, 0F, 0F, -1.169371F);
        this.neckRight.mirror = false;
        this.neckLeft = new RendererModel(this, 16, 20);
        this.neckLeft.addBox(-2.5F, -9F, -1.5F, 5, 9, 3, scale);
        this.neckLeft.setRotationPoint(3F, 10F, 0F);
        this.neckLeft.setTextureSize(128, 64);
        this.neckLeft.mirror = true;
        this.setRotation(this.neckLeft, 0F, 0F, 1.169371F);
        this.headMain = new RendererModel(this, 0, 0);
        this.headMain.addBox(-4F, -8F, -4F, 8, 8, 8, scale);
        this.headMain.setRotationPoint(0F, 6F, 0F);
        this.headMain.setTextureSize(128, 64);
        this.headMain.mirror = true;
        this.setRotation(this.headMain, 0F, 0F, 0F);
        this.bodyMain = new RendererModel(this, 16, 16);
        this.bodyMain.addBox(-4F, 0F, -2F, 8, 12, 4, scale);
        this.bodyMain.setRotationPoint(0F, 6F, 0F);
        this.bodyMain.setTextureSize(128, 64);
        this.bodyMain.mirror = true;
        this.setRotation(this.bodyMain, 0F, 0F, 0F);
        this.rightLegFront = new RendererModel(this, 0, 16);
        this.rightLegFront.addBox(-2F, 0F, -2F, 4, 6, 4, scale);
        this.rightLegFront.setRotationPoint(-2F, 18F, -4F);
        this.rightLegFront.setTextureSize(128, 64);
        this.rightLegFront.mirror = true;
        this.setRotation(this.rightLegFront, 0F, 0F, 0F);
        this.leftLegFront = new RendererModel(this, 0, 16);
        this.leftLegFront.addBox(-2F, 0F, -2F, 4, 6, 4, scale);
        this.leftLegFront.setRotationPoint(2F, 18F, -4F);
        this.leftLegFront.setTextureSize(128, 64);
        this.leftLegFront.mirror = true;
        this.setRotation(this.leftLegFront, 0F, 0F, 0F);
        this.rightLeg = new RendererModel(this, 0, 16);
        this.rightLeg.addBox(0F, 0F, -2F, 4, 6, 4, scale);
        this.rightLeg.setRotationPoint(-4F, 18F, 4F);
        this.rightLeg.setTextureSize(128, 64);
        this.rightLeg.mirror = true;
        this.setRotation(this.rightLeg, 0F, 0F, 0F);
        this.leftLeg = new RendererModel(this, 0, 16);
        this.leftLeg.addBox(-2F, 0F, -2F, 4, 6, 4, scale);
        this.leftLeg.setRotationPoint(2F, 18F, 4F);
        this.leftLeg.setTextureSize(128, 64);
        this.leftLeg.mirror = true;
        this.setRotation(this.leftLeg, 0F, 0F, 0F);
        this.oxygenTank = new RendererModel(this, 40, 0);
        this.oxygenTank.addBox(-5F, -9F, -5F, 10, 10, 10, scale);
        this.oxygenTank.setRotationPoint(0F, 6F, 0F);
        this.oxygenTank.setTextureSize(128, 64);
        this.oxygenTank.mirror = true;
        this.setRotation(this.oxygenTank, 0F, 0F, 0F);
        this.headLeft = new RendererModel(this, 0, 0);
        this.headLeft.addBox(1F, -9F, -4F, 8, 8, 8, scale);
        this.headLeft.setRotationPoint(3F, 6F, 0.1F);
        this.headLeft.setTextureSize(128, 64);
        this.headLeft.mirror = true;
        this.setRotation(this.headLeft, 0F, 0F, 0.7853982F);
        this.headRight = new RendererModel(this, 0, 0);
        this.headRight.addBox(-9F, -9F, -4F, 8, 8, 8, scale);
        this.headRight.setRotationPoint(-3F, 6F, -0.1F);
        this.headRight.setTextureSize(128, 64);
        this.headRight.mirror = true;
        this.setRotation(this.headRight, 0F, 0F, -0.7853982F);
    }

    @Override
    public void render(EntityCreeperBoss creeper, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(creeper, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(creeper, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        if (creeper.headsRemaining > 2)
        {
            this.headLeft.render(scale);
            this.neckLeft.render(scale);
            this.headRight.render(scale);
            this.neckRight.render(scale);
            this.headMain.render(scale);
            this.oxygenTank.render(scale);
        }
        else if (creeper.headsRemaining > 1)
        {
            this.headRight.render(scale);
            this.neckRight.render(scale);
            this.headMain.render(scale);
            this.oxygenTank.render(scale);
        }
        else if (creeper.headsRemaining > 0)
        {
            this.headMain.render(scale);
            this.oxygenTank.render(scale);
        }

        this.bodyMain.render(scale);
        this.rightLegFront.render(scale);
        this.leftLegFront.render(scale);
        this.rightLeg.render(scale);
        this.leftLeg.render(scale);
    }

    private void setRotation(RendererModel model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(EntityCreeperBoss entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        this.headMain.rotateAngleY = netHeadYaw / Constants.RADIANS_TO_DEGREES;
        this.headMain.rotateAngleX = headPitch / Constants.RADIANS_TO_DEGREES;
        this.oxygenTank.rotateAngleY = netHeadYaw / Constants.RADIANS_TO_DEGREES;
        this.oxygenTank.rotateAngleX = headPitch / Constants.RADIANS_TO_DEGREES;
        this.rightLegFront.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLegFront.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2F * limbSwingAmount;
        this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2F * limbSwingAmount;
        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2F * limbSwingAmount;
    }
}
