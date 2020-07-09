package micdoodle8.mods.galacticraft.planets.mars.client.model;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.ResourceLocation;

public class ModelBalloonParachute extends EntityModel<EntityLandingBalloons>
{
    private static final ResourceLocation grayParachuteTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/gray.png");

    public RendererModel[] parachute = new RendererModel[3];
    public RendererModel[] parachuteStrings = new RendererModel[4];

    public ModelBalloonParachute()
    {
        this(0.0F);
    }

    public ModelBalloonParachute(float par1)
    {
        super();

        this.parachute[0] = new RendererModel(this, 0, 0).setTextureSize(512, 256);
        this.parachute[0].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, par1);
        this.parachute[0].setRotationPoint(15.0F, 4.0F, 0.0F);
        this.parachute[1] = new RendererModel(this, 0, 42).setTextureSize(512, 256);
        this.parachute[1].addBox(-20.0F, -45.0F, -20.0F, 40, 2, 40, par1);
        this.parachute[1].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachute[2] = new RendererModel(this, 0, 0).setTextureSize(512, 256);
        this.parachute[2].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, par1);
        this.parachute[2].setRotationPoint(11F, -11, 0.0F);

        this.parachuteStrings[0] = new RendererModel(this, 100, 0).setTextureSize(512, 256);
        this.parachuteStrings[0].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[0].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[1] = new RendererModel(this, 100, 0).setTextureSize(512, 256);
        this.parachuteStrings[1].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[1].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[2] = new RendererModel(this, 100, 0).setTextureSize(512, 256);
        this.parachuteStrings[2].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[2].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[3] = new RendererModel(this, 100, 0).setTextureSize(512, 256);
        this.parachuteStrings[3].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[3].setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(EntityLandingBalloons entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        renderAll();
    }

    public void renderAll()
    {
        Minecraft.getInstance().textureManager.bindTexture(ModelBalloonParachute.grayParachuteTexture);

        int i;

        for (i = 0; i < this.parachute.length; i++)
        {
            this.parachute[i].render(0.0625F);
        }

        for (i = 0; i < this.parachuteStrings.length; i++)
        {
            this.parachuteStrings[i].render(0.0625F);
        }

        this.parachute[0].rotateAngleY = 0 / Constants.RADIANS_TO_DEGREES;
        this.parachute[2].rotateAngleY = -(0 / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[0].rotateAngleY = 0 / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[1].rotateAngleY = 0 / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[2].rotateAngleY = -(0 / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[3].rotateAngleY = -(0 / Constants.RADIANS_TO_DEGREES);

        this.parachute[0].setRotationPoint(-5.85F, -11.0F, 2.0F);
        this.parachute[1].setRotationPoint(9F, -7F, 2.0F);
        this.parachute[2].setRotationPoint(-2.15F, 4.0F, 2.0F);
        this.parachute[0].rotateAngleZ = 210F / Constants.RADIANS_TO_DEGREES;
        this.parachute[1].rotateAngleZ = 180F / Constants.RADIANS_TO_DEGREES;
        this.parachute[2].rotateAngleZ = -(210F / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[0].rotateAngleZ = (155F + 180F) / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[0].rotateAngleX = 23F / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[0].setRotationPoint(9.0F, 3.0F, 2.0F);
        this.parachuteStrings[1].rotateAngleZ = (155F + 180F) / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[1].rotateAngleX = -(23F / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[1].setRotationPoint(9.0F, 3.0F, 2.0F);

        this.parachuteStrings[2].rotateAngleZ = -((155F + 180F) / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[2].rotateAngleX = 23F / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[2].setRotationPoint(9.0F, 3.0F, 2.0F);
        this.parachuteStrings[3].rotateAngleZ = -((155F + 180F) / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[3].rotateAngleX = -(23F / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[3].setRotationPoint(9.0F, 3.0F, 2.0F);
    }

    public void renderParachute()
    {
    }
}
