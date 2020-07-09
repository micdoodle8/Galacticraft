package micdoodle8.mods.galacticraft.core.client.model;

import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedEnderman;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EndermanModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelEvolvedEnderman extends BipedModel<EntityEvolvedEnderman>
{
    public boolean isCarrying;
    public boolean isAttacking;
    RendererModel oxygenMask;
    RendererModel tank1;
    RendererModel tank2;
    RendererModel tube1;
    RendererModel tube2;
    RendererModel tube3;
    RendererModel tube4;
    RendererModel tube5;
    RendererModel tube6;
    RendererModel tube7;
    RendererModel tube8;
    RendererModel tube9;
    RendererModel tube10;
    RendererModel tube11;
    RendererModel tube12;
    RendererModel tube13;
    RendererModel tube14;

    public ModelEvolvedEnderman()
    {
        super(0.0F/*, -14.0F, 64, 64*/);
        float f1 = -14.0F;
        this.bipedHeadwear = new RendererModel(this, 0, 16);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F - 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
        this.bipedBody = new RendererModel(this, 32, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
        this.bipedRightArm = new RendererModel(this, 56, 0);
        this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, 0.0F);
        this.bipedRightArm.setRotationPoint(-3.0F, 2.0F + f1, 0.0F);
        this.bipedLeftArm = new RendererModel(this, 56, 0);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, 0.0F);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + f1, 0.0F);
        this.bipedRightLeg = new RendererModel(this, 56, 0);
        this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, 0.0F);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + f1, 0.0F);
        this.bipedLeftLeg = new RendererModel(this, 56, 0);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, 0.0F);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + f1, 0.0F);

        this.oxygenMask = new RendererModel(this, 0, 44);
        this.oxygenMask.addBox(-5F, -9F, -5F, 10, 10, 10);
        this.oxygenMask.setRotationPoint(0F, -14F, 0F);

        this.tank1 = new RendererModel(this, 52, 54);
        this.tank1.addBox(0F, 0F, 0F, 3, 7, 3);
        this.tank1.setRotationPoint(0F, -11F, 2F);

        this.tank2 = new RendererModel(this, 52, 54);
        this.tank2.addBox(0F, 0F, 0F, 3, 7, 3);
        this.tank2.setRotationPoint(-3F, -11F, 2F);

        this.tube1 = new RendererModel(this, 44, 62);
        this.tube1.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube1.setRotationPoint(-2F, -10.5F, 5F);

        this.tube2 = new RendererModel(this, 44, 62);
        this.tube2.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube2.setRotationPoint(-2F, -11F, 6F);

        this.tube3 = new RendererModel(this, 44, 62);
        this.tube3.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube3.setRotationPoint(-2F, -12F, 7F);

        this.tube4 = new RendererModel(this, 44, 62);
        this.tube4.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube4.setRotationPoint(-2F, -13F, 7F);

        this.tube5 = new RendererModel(this, 44, 62);
        this.tube5.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube5.setRotationPoint(-2F, -14F, 7F);

        this.tube6 = new RendererModel(this, 44, 62);
        this.tube6.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube6.setRotationPoint(-2F, -15F, 6F);

        this.tube7 = new RendererModel(this, 44, 62);
        this.tube7.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube7.setRotationPoint(-2F, -16F, 5F);

        this.tube8 = new RendererModel(this, 44, 62);
        this.tube8.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube8.setRotationPoint(1F, -10.5F, 5F);

        this.tube9 = new RendererModel(this, 44, 62);
        this.tube9.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube9.setRotationPoint(1F, -11F, 6F);

        this.tube10 = new RendererModel(this, 44, 62);
        this.tube10.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube10.setRotationPoint(1F, -12F, 7F);

        this.tube11 = new RendererModel(this, 44, 62);
        this.tube11.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube11.setRotationPoint(1F, -13F, 7F);

        this.tube12 = new RendererModel(this, 44, 62);
        this.tube12.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube12.setRotationPoint(1F, -14F, 7F);

        this.tube13 = new RendererModel(this, 44, 62);
        this.tube13.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube13.setRotationPoint(1F, -15F, 6F);

        this.tube14 = new RendererModel(this, 44, 62);
        this.tube14.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube14.setRotationPoint(1F, -16F, 5F);
    }

    @Override
    public void setRotationAngles(EntityEvolvedEnderman enderman, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.setRotationAngles(enderman, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.bipedHead.showModel = true;
        float f6 = -14.0F;
        this.bipedBody.rotateAngleX = 0.0F;
        this.bipedBody.rotationPointY = f6;
        this.bipedBody.rotationPointZ = -0.0F;
        this.bipedRightLeg.rotateAngleX -= 0.0F;
        this.bipedLeftLeg.rotateAngleX -= 0.0F;
        this.bipedRightArm.rotateAngleX = (float) (this.bipedRightArm.rotateAngleX * 0.5D);
        this.bipedLeftArm.rotateAngleX = (float) (this.bipedLeftArm.rotateAngleX * 0.5D);
        this.bipedRightLeg.rotateAngleX = (float) (this.bipedRightLeg.rotateAngleX * 0.5D);
        this.bipedLeftLeg.rotateAngleX = (float) (this.bipedLeftLeg.rotateAngleX * 0.5D);
        float f7 = 0.4F;

        if (this.bipedRightArm.rotateAngleX > f7)
        {
            this.bipedRightArm.rotateAngleX = f7;
        }
        if (this.bipedLeftArm.rotateAngleX > f7)
        {
            this.bipedLeftArm.rotateAngleX = f7;
        }
        if (this.bipedRightArm.rotateAngleX < -f7)
        {
            this.bipedRightArm.rotateAngleX = -f7;
        }
        if (this.bipedLeftArm.rotateAngleX < -f7)
        {
            this.bipedLeftArm.rotateAngleX = -f7;
        }
        if (this.bipedRightLeg.rotateAngleX > f7)
        {
            this.bipedRightLeg.rotateAngleX = f7;
        }
        if (this.bipedLeftLeg.rotateAngleX > f7)
        {
            this.bipedLeftLeg.rotateAngleX = f7;
        }
        if (this.bipedRightLeg.rotateAngleX < -f7)
        {
            this.bipedRightLeg.rotateAngleX = -f7;
        }
        if (this.bipedLeftLeg.rotateAngleX < -f7)
        {
            this.bipedLeftLeg.rotateAngleX = -f7;
        }

        if (this.isCarrying)
        {
            this.bipedRightArm.rotateAngleX = -0.5F;
            this.bipedLeftArm.rotateAngleX = -0.5F;
            this.bipedRightArm.rotateAngleZ = 0.05F;
            this.bipedLeftArm.rotateAngleZ = -0.05F;
        }

        this.bipedRightArm.rotationPointZ = 0.0F;
        this.bipedLeftArm.rotationPointZ = 0.0F;
        this.bipedRightLeg.rotationPointZ = 0.0F;
        this.bipedLeftLeg.rotationPointZ = 0.0F;
        this.bipedRightLeg.rotationPointY = 9.0F + f6;
        this.bipedLeftLeg.rotationPointY = 9.0F + f6;
        this.bipedHead.rotationPointZ = -0.0F;
        this.bipedHead.rotationPointY = f6 + 1.0F;
        this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX;
        this.bipedHeadwear.rotationPointY = this.bipedHead.rotationPointY;
        this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;

        this.oxygenMask.rotationPointX = this.bipedHead.rotationPointX;
        this.oxygenMask.rotationPointY = this.bipedHead.rotationPointY;
        this.oxygenMask.rotationPointZ = this.bipedHead.rotationPointZ;
        this.oxygenMask.rotateAngleX = this.bipedHead.rotateAngleX;
        this.oxygenMask.rotateAngleY = this.bipedHead.rotateAngleY;
        this.oxygenMask.rotateAngleZ = this.bipedHead.rotateAngleZ;

        if (this.isAttacking)
        {
            float f8 = 1.0F;
            this.bipedHead.rotationPointY -= f8 * 5.0F;
            this.oxygenMask.rotationPointY -= f8 * 5.0F;
        }
    }

    @Override
    public void render(EntityEvolvedEnderman entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.oxygenMask.render(scale);
        this.tank1.render(scale);
        this.tank2.render(scale);
        this.tube1.render(scale);
        this.tube2.render(scale);
        this.tube3.render(scale);
        this.tube4.render(scale);
        this.tube5.render(scale);
        this.tube6.render(scale);
        this.tube7.render(scale);
        this.tube8.render(scale);
        this.tube9.render(scale);
        this.tube10.render(scale);
        this.tube11.render(scale);
        this.tube12.render(scale);
        this.tube13.render(scale);
        this.tube14.render(scale);
    }
}