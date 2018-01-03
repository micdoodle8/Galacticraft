package micdoodle8.mods.galacticraft.core.client.model;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelEvolvedSkeletonBoss extends ModelBase
{
    private ModelRenderer upperHead;
    private ModelRenderer pelvis;
    private ModelRenderer sternum;
    private ModelRenderer rightLeg;
    private ModelRenderer rightArm;
    private ModelRenderer spine;
    private ModelRenderer leftArm;
    private ModelRenderer leftLeg;
    private ModelRenderer leftFrontBotRib;
    private ModelRenderer leftFrontTopRib;
    private ModelRenderer leftFront2ndRib;
    private ModelRenderer leftFront3rdRib;
    private ModelRenderer leftSideBotRib;
    private ModelRenderer leftSide3rdRib;
    private ModelRenderer leftSide2ndRib;
    private ModelRenderer leftSideTopRib;
    private ModelRenderer rightSideTopRib;
    private ModelRenderer rightSide2ndRib;
    private ModelRenderer rightSide3rdRib;
    private ModelRenderer rightSideBotRib;
    private ModelRenderer rightFrontBotRib;
    private ModelRenderer rightFront3rdRib;
    private ModelRenderer rightFront2ndRib;
    private ModelRenderer rightFrontTopRib;
    private ModelRenderer leftBackTopRib;
    private ModelRenderer leftBack2ndRib;
    private ModelRenderer leftBack3rdRib;
    private ModelRenderer leftBackBotRib;
    private ModelRenderer rightBackBotRib;
    private ModelRenderer rightBack3rdRib;
    private ModelRenderer rightBack2ndRib;
    private ModelRenderer rightBackTopRib;

    public ModelEvolvedSkeletonBoss()
    {
        this.textureWidth = 128;
        this.textureHeight = 128;

        float halfPI = Constants.halfPI;

        this.upperHead = new ModelRenderer(this, 0, 16);
        this.upperHead.addBox(-4F, -8F, -6F, 8, 8, 8);
        this.upperHead.setRotationPoint(0F, -24F, 6F);
        this.upperHead.setTextureSize(64, 32);
        this.upperHead.mirror = true;
        this.setRotation(this.upperHead, 0.122173F, 0F, 0F);
        this.pelvis = new ModelRenderer(this, 32, 19);
        this.pelvis.addBox(-6F, 0F, -3F, 12, 5, 5);
        this.pelvis.setRotationPoint(0F, -2F, 5F);
        this.pelvis.setTextureSize(64, 32);
        this.pelvis.mirror = true;
        this.setRotation(this.pelvis, 0F, 0F, 0F);
        this.sternum = new ModelRenderer(this, 0, 0);
        this.sternum.addBox(-1.5F, 0F, -1F, 3, 9, 1);
        this.sternum.setRotationPoint(0F, -21F, 2F);
        this.sternum.setTextureSize(64, 32);
        this.sternum.mirror = true;
        this.setRotation(this.sternum, 0F, 0F, 0F);
        this.rightLeg = new ModelRenderer(this, 56, 33);
        this.rightLeg.mirror = true;
        this.rightLeg.addBox(-2F, 0F, -2F, 3, 26, 3);
        this.rightLeg.setRotationPoint(-5F, 0F, 5F);
        this.rightLeg.setTextureSize(64, 32);
        this.rightLeg.mirror = true;
        this.setRotation(this.rightLeg, 0F, 0F, 0F);
        this.rightLeg.mirror = false;
        this.rightArm = new ModelRenderer(this, 56, 33);
        this.rightArm.addBox(-2F, -2F, -1.5F, 3, 24, 3);
        this.rightArm.setRotationPoint(-8F, -20F, 5F);
        this.rightArm.setTextureSize(64, 32);
        this.rightArm.mirror = true;
        this.setRotation(this.rightArm, 0F, 0F, 0F);
        this.spine = new ModelRenderer(this, 32, 33);
        this.spine.addBox(-1.5F, 0F, -1F, 3, 22, 2);
        this.spine.setRotationPoint(0F, -24F, 6F);
        this.spine.setTextureSize(64, 32);
        this.spine.mirror = true;
        this.setRotation(this.spine, 0F, 0F, 0F);
        this.leftArm = new ModelRenderer(this, 56, 33);
        this.leftArm.addBox(-1F, -2F, -1.5F, 3, 24, 3);
        this.leftArm.setRotationPoint(8F, -20F, 5F);
        this.leftArm.setTextureSize(64, 32);
        this.leftArm.mirror = true;
        this.setRotation(this.leftArm, 0F, 0F, 0F);
        this.leftLeg = new ModelRenderer(this, 56, 33);
        this.leftLeg.addBox(-2F, 0F, -2F, 3, 26, 3);
        this.leftLeg.setRotationPoint(6F, 0F, 5F);
        this.leftLeg.setTextureSize(64, 32);
        this.leftLeg.mirror = true;
        this.setRotation(this.leftLeg, 0F, 0F, 0F);
        this.leftFrontBotRib = new ModelRenderer(this, 0, 0);
        this.leftFrontBotRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftFrontBotRib.setRotationPoint(7F, -13F, 2F);
        this.leftFrontBotRib.setTextureSize(64, 32);
        this.leftFrontBotRib.mirror = true;
        this.setRotation(this.leftFrontBotRib, 0F, -halfPI, 0F);
        this.leftFrontTopRib = new ModelRenderer(this, 0, 0);
        this.leftFrontTopRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftFrontTopRib.setRotationPoint(7F, -22F, 2F);
        this.leftFrontTopRib.setTextureSize(64, 32);
        this.leftFrontTopRib.mirror = true;
        this.setRotation(this.leftFrontTopRib, 0F, -halfPI, 0F);
        this.leftFront2ndRib = new ModelRenderer(this, 0, 0);
        this.leftFront2ndRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftFront2ndRib.setRotationPoint(7F, -19F, 2F);
        this.leftFront2ndRib.setTextureSize(64, 32);
        this.leftFront2ndRib.mirror = true;
        this.setRotation(this.leftFront2ndRib, 0F, -halfPI, 0F);
        this.leftFront3rdRib = new ModelRenderer(this, 0, 0);
        this.leftFront3rdRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftFront3rdRib.setRotationPoint(7F, -16F, 2F);
        this.leftFront3rdRib.setTextureSize(64, 32);
        this.leftFront3rdRib.mirror = true;
        this.setRotation(this.leftFront3rdRib, 0F, -halfPI, 0F);
        this.leftSideBotRib = new ModelRenderer(this, 0, 0);
        this.leftSideBotRib.addBox(-1F, 0F, -6F, 1, 2, 6);
        this.leftSideBotRib.setRotationPoint(7F, -13F, 7F);
        this.leftSideBotRib.setTextureSize(64, 32);
        this.leftSideBotRib.mirror = true;
        this.setRotation(this.leftSideBotRib, 0F, 0F, 0F);
        this.leftSide3rdRib = new ModelRenderer(this, 0, 0);
        this.leftSide3rdRib.addBox(-1F, 0F, -6F, 1, 2, 6);
        this.leftSide3rdRib.setRotationPoint(7F, -16F, 7F);
        this.leftSide3rdRib.setTextureSize(64, 32);
        this.leftSide3rdRib.mirror = true;
        this.setRotation(this.leftSide3rdRib, 0F, 0F, 0F);
        this.leftSide2ndRib = new ModelRenderer(this, 0, 0);
        this.leftSide2ndRib.addBox(-1F, 0F, -6F, 1, 2, 6);
        this.leftSide2ndRib.setRotationPoint(7F, -19F, 7F);
        this.leftSide2ndRib.setTextureSize(64, 32);
        this.leftSide2ndRib.mirror = true;
        this.setRotation(this.leftSide2ndRib, 0F, 0F, 0F);
        this.leftSideTopRib = new ModelRenderer(this, 0, 0);
        this.leftSideTopRib.addBox(-1F, 0F, -6F, 1, 2, 6);
        this.leftSideTopRib.setRotationPoint(7F, -22F, 7F);
        this.leftSideTopRib.setTextureSize(64, 32);
        this.leftSideTopRib.mirror = true;
        this.setRotation(this.leftSideTopRib, 0F, 0F, 0F);
        this.rightSideTopRib = new ModelRenderer(this, 0, 0);
        this.rightSideTopRib.addBox(0F, 0F, -6F, 1, 2, 6);
        this.rightSideTopRib.setRotationPoint(-7F, -22F, 7F);
        this.rightSideTopRib.setTextureSize(64, 32);
        this.rightSideTopRib.mirror = true;
        this.setRotation(this.rightSideTopRib, 0F, 0F, 0F);
        this.rightSide2ndRib = new ModelRenderer(this, 0, 0);
        this.rightSide2ndRib.addBox(0F, 0F, -6F, 1, 2, 6);
        this.rightSide2ndRib.setRotationPoint(-7F, -19F, 7F);
        this.rightSide2ndRib.setTextureSize(64, 32);
        this.rightSide2ndRib.mirror = true;
        this.setRotation(this.rightSide2ndRib, 0F, 0F, 0F);
        this.rightSide3rdRib = new ModelRenderer(this, 0, 0);
        this.rightSide3rdRib.addBox(0F, 0F, -6F, 1, 2, 6);
        this.rightSide3rdRib.setRotationPoint(-7F, -16F, 7F);
        this.rightSide3rdRib.setTextureSize(64, 32);
        this.rightSide3rdRib.mirror = true;
        this.setRotation(this.rightSide3rdRib, 0F, 0F, 0F);
        this.rightSideBotRib = new ModelRenderer(this, 0, 0);
        this.rightSideBotRib.addBox(0F, 0F, -6F, 1, 2, 6);
        this.rightSideBotRib.setRotationPoint(-7F, -13F, 7F);
        this.rightSideBotRib.setTextureSize(64, 32);
        this.rightSideBotRib.mirror = true;
        this.setRotation(this.rightSideBotRib, 0F, 0F, 0F);
        this.rightFrontBotRib = new ModelRenderer(this, 0, 0);
        this.rightFrontBotRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightFrontBotRib.setRotationPoint(-7F, -13F, 2F);
        this.rightFrontBotRib.setTextureSize(64, 32);
        this.rightFrontBotRib.mirror = true;
        this.setRotation(this.rightFrontBotRib, 0F, halfPI, 0F);
        this.rightFront3rdRib = new ModelRenderer(this, 0, 0);
        this.rightFront3rdRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightFront3rdRib.setRotationPoint(-7F, -16F, 2F);
        this.rightFront3rdRib.setTextureSize(64, 32);
        this.rightFront3rdRib.mirror = true;
        this.setRotation(this.rightFront3rdRib, 0F, halfPI, 0F);
        this.rightFront2ndRib = new ModelRenderer(this, 0, 0);
        this.rightFront2ndRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightFront2ndRib.setRotationPoint(-7F, -19F, 2F);
        this.rightFront2ndRib.setTextureSize(64, 32);
        this.rightFront2ndRib.mirror = true;
        this.setRotation(this.rightFront2ndRib, 0F, halfPI, 0F);
        this.rightFrontTopRib = new ModelRenderer(this, 0, 0);
        this.rightFrontTopRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightFrontTopRib.setRotationPoint(-7F, -22F, 2F);
        this.rightFrontTopRib.setTextureSize(64, 32);
        this.rightFrontTopRib.mirror = true;
        this.setRotation(this.rightFrontTopRib, 0F, halfPI, 0F);
        this.leftBackTopRib = new ModelRenderer(this, 0, 0);
        this.leftBackTopRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftBackTopRib.setRotationPoint(7F, -22F, 7F);
        this.leftBackTopRib.setTextureSize(64, 32);
        this.leftBackTopRib.mirror = true;
        this.setRotation(this.leftBackTopRib, 0F, -halfPI, 0F);
        this.leftBack2ndRib = new ModelRenderer(this, 0, 0);
        this.leftBack2ndRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftBack2ndRib.setRotationPoint(7F, -19F, 7F);
        this.leftBack2ndRib.setTextureSize(64, 32);
        this.leftBack2ndRib.mirror = true;
        this.setRotation(this.leftBack2ndRib, 0F, -halfPI, 0F);
        this.leftBack3rdRib = new ModelRenderer(this, 0, 0);
        this.leftBack3rdRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftBack3rdRib.setRotationPoint(7F, -16F, 7F);
        this.leftBack3rdRib.setTextureSize(64, 32);
        this.leftBack3rdRib.mirror = true;
        this.setRotation(this.leftBack3rdRib, 0F, -halfPI, 0F);
        this.leftBackBotRib = new ModelRenderer(this, 0, 0);
        this.leftBackBotRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftBackBotRib.setRotationPoint(7F, -13F, 7F);
        this.leftBackBotRib.setTextureSize(64, 32);
        this.leftBackBotRib.mirror = true;
        this.setRotation(this.leftBackBotRib, 0F, -halfPI, 0F);
        this.rightBackBotRib = new ModelRenderer(this, 0, 0);
        this.rightBackBotRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightBackBotRib.setRotationPoint(-7F, -13F, 7F);
        this.rightBackBotRib.setTextureSize(64, 32);
        this.rightBackBotRib.mirror = true;
        this.setRotation(this.rightBackBotRib, 0F, halfPI, 0F);
        this.rightBack3rdRib = new ModelRenderer(this, 0, 0);
        this.rightBack3rdRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightBack3rdRib.setRotationPoint(-7F, -16F, 7F);
        this.rightBack3rdRib.setTextureSize(64, 32);
        this.rightBack3rdRib.mirror = true;
        this.setRotation(this.rightBack3rdRib, 0F, halfPI, 0F);
        this.rightBack2ndRib = new ModelRenderer(this, 0, 0);
        this.rightBack2ndRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightBack2ndRib.setRotationPoint(-7F, -19F, 7F);
        this.rightBack2ndRib.setTextureSize(64, 32);
        this.rightBack2ndRib.mirror = true;
        this.setRotation(this.rightBack2ndRib, 0F, halfPI, 0F);
        this.rightBackTopRib = new ModelRenderer(this, 0, 0);
        this.rightBackTopRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightBackTopRib.setRotationPoint(-7F, -22F, 7F);
        this.rightBackTopRib.setTextureSize(64, 32);
        this.rightBackTopRib.mirror = true;
        this.setRotation(this.rightBackTopRib, 0F, halfPI, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.upperHead.render(f5);
        this.pelvis.render(f5);
        this.sternum.render(f5);
        this.rightLeg.render(f5);
        this.rightArm.render(f5);
        this.spine.render(f5);
        this.leftArm.render(f5);
        this.leftLeg.render(f5);
        this.leftFrontBotRib.render(f5);
        this.leftFrontTopRib.render(f5);
        this.leftFront2ndRib.render(f5);
        this.leftFront3rdRib.render(f5);
        this.leftSideBotRib.render(f5);
        this.leftSide3rdRib.render(f5);
        this.leftSide2ndRib.render(f5);
        this.leftSideTopRib.render(f5);
        this.rightSideTopRib.render(f5);
        this.rightSide2ndRib.render(f5);
        this.rightSide3rdRib.render(f5);
        this.rightSideBotRib.render(f5);
        this.rightFrontBotRib.render(f5);
        this.rightFront3rdRib.render(f5);
        this.rightFront2ndRib.render(f5);
        this.rightFrontTopRib.render(f5);
        this.leftBackTopRib.render(f5);
        this.leftBack2ndRib.render(f5);
        this.leftBack3rdRib.render(f5);
        this.leftBackBotRib.render(f5);
        this.rightBackBotRib.render(f5);
        this.rightBack3rdRib.render(f5);
        this.rightBack2ndRib.render(f5);
        this.rightBackTopRib.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity e)
    {
        final float floatPI = 3.1415927F;

        final EntitySkeletonBoss boss = (EntitySkeletonBoss) e;
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, e);
        this.upperHead.rotateAngleY = par4 / Constants.RADIANS_TO_DEGREES;
        this.upperHead.rotateAngleX = par5 / Constants.RADIANS_TO_DEGREES;
        this.rightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + floatPI) * 2.0F * par2 * 0.5F;
        this.leftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
        this.rightArm.rotateAngleZ = 0.0F;
        this.leftArm.rotateAngleZ = 0.0F;
        this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
        this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + floatPI) * 1.4F * par2;
        this.rightLeg.rotateAngleY = 0.0F;
        this.leftLeg.rotateAngleY = 0.0F;

        if (this.isRiding)
        {
            this.rightArm.rotateAngleX += -(floatPI / 5F);
            this.leftArm.rotateAngleX += -(floatPI / 5F);
            this.rightLeg.rotateAngleX = -(floatPI * 2F / 5F);
            this.leftLeg.rotateAngleX = -(floatPI * 2F / 5F);
            this.rightLeg.rotateAngleY = floatPI / 10F;
            this.leftLeg.rotateAngleY = -(floatPI / 10F);
        }

        this.rightArm.rotateAngleY = par4 / Constants.RADIANS_TO_DEGREES;
        this.leftArm.rotateAngleY = par4 / Constants.RADIANS_TO_DEGREES;
        float var7;
        float var8;

        if (this.swingProgress > -9990.0F)
        {
            var7 = this.swingProgress;
            this.spine.rotateAngleY = MathHelper.sin(MathHelper.sqrt(var7) * Constants.twoPI) * 0.2F;

            this.rightArm.rotationPointZ = MathHelper.sin(this.spine.rotateAngleY) * 5.0F;
            this.rightArm.rotationPointX = -MathHelper.cos(this.spine.rotateAngleY) * 5.0F;
            this.leftArm.rotationPointZ = -MathHelper.sin(this.spine.rotateAngleY) * 5.0F;
            this.leftArm.rotationPointX = MathHelper.cos(this.spine.rotateAngleY) * 5.0F;
            this.rightArm.rotateAngleY += this.spine.rotateAngleY;
            this.leftArm.rotateAngleY += this.spine.rotateAngleY;
            this.leftArm.rotateAngleX += this.spine.rotateAngleY;
            var7 = 1.0F - this.swingProgress;
            var7 *= var7;
            var7 *= var7;
            var7 = 1.0F - var7;
            var8 = MathHelper.sin(var7 * floatPI);
            final float var9 = MathHelper.sin(this.swingProgress * floatPI) * -(this.upperHead.rotateAngleX - 0.7F) * 0.75F;
            this.rightArm.rotateAngleX = (float) (this.rightArm.rotateAngleX - (var8 * 1.2D + var9));
            this.rightArm.rotateAngleY += this.spine.rotateAngleY * 2.0F;
            this.rightArm.rotateAngleZ = MathHelper.sin(this.swingProgress * floatPI) * -0.4F;
        }

        final float f6 = MathHelper.sin(this.swingProgress * floatPI);
        final float f7 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * floatPI);
        this.rightArm.rotateAngleZ = 0.0F;
        this.leftArm.rotateAngleZ = 0.0F;
        this.rightArm.rotateAngleY = 0.5F * (par4 / Constants.RADIANS_TO_DEGREES) + -(0.1F - f6 * 0.6F);
        this.leftArm.rotateAngleY = 0.5F * (par4 / Constants.RADIANS_TO_DEGREES) + 0.1F - f6 * 0.6F;
        this.rightArm.rotateAngleX = -(floatPI / 2F);
        this.leftArm.rotateAngleX = -(floatPI / 2F);
        this.rightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
        this.leftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
        this.rightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.leftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.rightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
        this.leftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

        if (((EntitySkeletonBoss) e).deathTicks > 0)
        {
            this.leftArm.rotateAngleX = -(floatPI / 2F) + (float) (Math.pow(((EntitySkeletonBoss) e).deathTicks, 2) / 5.0F) / 3.0F / Constants.RADIANS_TO_DEGREES;
            this.rightArm.rotateAngleX = -(floatPI / 2F) + (float) (Math.pow(((EntitySkeletonBoss) e).deathTicks, 2) / 5.0F) / 2.0F / Constants.RADIANS_TO_DEGREES;
        }

        if (boss.throwTimer + boss.postThrowDelay > 0)
        {
            this.rightArm.rotateAngleX -= MathHelper.cos((boss.throwTimer + boss.postThrowDelay) * 0.05F) * 1.2F + 0.05F;
            this.leftArm.rotateAngleX -= MathHelper.cos((boss.throwTimer + boss.postThrowDelay) * 0.05F) * 1.2F + 0.05F;
        }
    }

    public void postRenderArm(float scale, ItemCameraTransforms.TransformType type)
    {
        if (type == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
        {
            this.rightArm.postRender(scale);
        }
        else
        {
            this.leftArm.postRender(scale);
        }
    }
}
