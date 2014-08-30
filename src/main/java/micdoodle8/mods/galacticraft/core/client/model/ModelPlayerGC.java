package micdoodle8.mods.galacticraft.core.client.model;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Loader;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Method;
import java.util.List;

public class ModelPlayerGC extends ModelBiped
{
    public static final ResourceLocation oxygenMaskTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/oxygen.png");
	public static final ResourceLocation playerTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/player.png");
    public static final ResourceLocation frequencyModuleTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/frequencyModule.png");

	public ModelRenderer[] parachute = new ModelRenderer[3];
	public ModelRenderer[] parachuteStrings = new ModelRenderer[4];
	public ModelRenderer[][] tubes = new ModelRenderer[2][7];
	public ModelRenderer[] greenOxygenTanks = new ModelRenderer[2];
	public ModelRenderer[] orangeOxygenTanks = new ModelRenderer[2];
	public ModelRenderer[] redOxygenTanks = new ModelRenderer[2];
	public ModelRenderer oxygenMask;

    private boolean usingParachute;

	private IModelCustom frequencyModule;

	private static boolean crossbowModLoaded = false;

	static
	{
		ModelPlayerGC.crossbowModLoaded = Loader.isModLoaded("CrossbowMod2");
	}

	public ModelPlayerGC(float var1)
	{
		super(var1);

		this.oxygenMask = new ModelRenderer(this, 0, 0);
		this.oxygenMask.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 1);
		this.oxygenMask.setRotationPoint(0.0F, 0.0F + 0.0F, 0.0F);

		this.parachute[0] = new ModelRenderer(this, 0, 0).setTextureSize(512, 256);
		this.parachute[0].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, var1);
		this.parachute[0].setRotationPoint(15.0F, 4.0F, 0.0F);
		this.parachute[1] = new ModelRenderer(this, 0, 42).setTextureSize(512, 256);
		this.parachute[1].addBox(-20.0F, -45.0F, -20.0F, 40, 2, 40, var1);
		this.parachute[1].setRotationPoint(0.0F, 0.0F, 0.0F);
		this.parachute[2] = new ModelRenderer(this, 0, 0).setTextureSize(512, 256);
		this.parachute[2].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, var1);
		this.parachute[2].setRotationPoint(11F, -11, 0.0F);

		this.parachuteStrings[0] = new ModelRenderer(this, 100, 0).setTextureSize(512, 256);
		this.parachuteStrings[0].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
		this.parachuteStrings[0].setRotationPoint(0.0F, 0.0F, 0.0F);
		this.parachuteStrings[1] = new ModelRenderer(this, 100, 0).setTextureSize(512, 256);
		this.parachuteStrings[1].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
		this.parachuteStrings[1].setRotationPoint(0.0F, 0.0F, 0.0F);
		this.parachuteStrings[2] = new ModelRenderer(this, 100, 0).setTextureSize(512, 256);
		this.parachuteStrings[2].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
		this.parachuteStrings[2].setRotationPoint(0.0F, 0.0F, 0.0F);
		this.parachuteStrings[3] = new ModelRenderer(this, 100, 0).setTextureSize(512, 256);
		this.parachuteStrings[3].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
		this.parachuteStrings[3].setRotationPoint(0.0F, 0.0F, 0.0F);

		this.tubes[0][0] = new ModelRenderer(this, 0, 0);
		this.tubes[0][0].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][0].setRotationPoint(2F, 3F, 5.8F);
		this.tubes[0][0].setTextureSize(128, 64);
		this.tubes[0][0].mirror = true;
		this.tubes[0][1] = new ModelRenderer(this, 0, 0);
		this.tubes[0][1].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][1].setRotationPoint(2F, 2F, 6.8F);
		this.tubes[0][1].setTextureSize(128, 64);
		this.tubes[0][1].mirror = true;
		this.tubes[0][2] = new ModelRenderer(this, 0, 0);
		this.tubes[0][2].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][2].setRotationPoint(2F, 1F, 6.8F);
		this.tubes[0][2].setTextureSize(128, 64);
		this.tubes[0][2].mirror = true;
		this.tubes[0][3] = new ModelRenderer(this, 0, 0);
		this.tubes[0][3].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][3].setRotationPoint(2F, 0F, 6.8F);
		this.tubes[0][3].setTextureSize(128, 64);
		this.tubes[0][3].mirror = true;
		this.tubes[0][4] = new ModelRenderer(this, 0, 0);
		this.tubes[0][4].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][4].setRotationPoint(2F, -1F, 6.8F);
		this.tubes[0][4].setTextureSize(128, 64);
		this.tubes[0][4].mirror = true;
		this.tubes[0][5] = new ModelRenderer(this, 0, 0);
		this.tubes[0][5].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][5].setRotationPoint(2F, -2F, 5.8F);
		this.tubes[0][5].setTextureSize(128, 64);
		this.tubes[0][5].mirror = true;
		this.tubes[0][6] = new ModelRenderer(this, 0, 0);
		this.tubes[0][6].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][6].setRotationPoint(2F, -3F, 4.8F);
		this.tubes[0][6].setTextureSize(128, 64);
		this.tubes[0][6].mirror = true;

		this.tubes[1][0] = new ModelRenderer(this, 0, 0);
		this.tubes[1][0].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][0].setRotationPoint(-2F, 3F, 5.8F);
		this.tubes[1][0].setTextureSize(128, 64);
		this.tubes[1][0].mirror = true;
		this.tubes[1][1] = new ModelRenderer(this, 0, 0);
		this.tubes[1][1].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][1].setRotationPoint(-2F, 2F, 6.8F);
		this.tubes[1][1].setTextureSize(128, 64);
		this.tubes[1][1].mirror = true;
		this.tubes[1][2] = new ModelRenderer(this, 0, 0);
		this.tubes[1][2].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][2].setRotationPoint(-2F, 1F, 6.8F);
		this.tubes[1][2].setTextureSize(128, 64);
		this.tubes[1][2].mirror = true;
		this.tubes[1][3] = new ModelRenderer(this, 0, 0);
		this.tubes[1][3].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][3].setRotationPoint(-2F, 0F, 6.8F);
		this.tubes[1][3].setTextureSize(128, 64);
		this.tubes[1][3].mirror = true;
		this.tubes[1][4] = new ModelRenderer(this, 0, 0);
		this.tubes[1][4].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][4].setRotationPoint(-2F, -1F, 6.8F);
		this.tubes[1][4].setTextureSize(128, 64);
		this.tubes[1][4].mirror = true;
		this.tubes[1][5] = new ModelRenderer(this, 0, 0);
		this.tubes[1][5].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][5].setRotationPoint(-2F, -2F, 5.8F);
		this.tubes[1][5].setTextureSize(128, 64);
		this.tubes[1][5].mirror = true;
		this.tubes[1][6] = new ModelRenderer(this, 0, 0);
		this.tubes[1][6].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][6].setRotationPoint(-2F, -3F, 4.8F);
		this.tubes[1][6].setTextureSize(128, 64);
		this.tubes[1][6].mirror = true;

		this.greenOxygenTanks[0] = new ModelRenderer(this, 4, 0);
		this.greenOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.greenOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
		this.greenOxygenTanks[0].mirror = true;
		this.greenOxygenTanks[1] = new ModelRenderer(this, 4, 0);
		this.greenOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.greenOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
		this.greenOxygenTanks[1].mirror = true;

		this.orangeOxygenTanks[0] = new ModelRenderer(this, 16, 0);
		this.orangeOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.orangeOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
		this.orangeOxygenTanks[0].mirror = true;
		this.orangeOxygenTanks[1] = new ModelRenderer(this, 16, 0);
		this.orangeOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.orangeOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
		this.orangeOxygenTanks[1].mirror = true;

		this.redOxygenTanks[0] = new ModelRenderer(this, 28, 0);
		this.redOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.redOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
		this.redOxygenTanks[0].mirror = true;
		this.redOxygenTanks[1] = new ModelRenderer(this, 28, 0);
		this.redOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.redOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
		this.redOxygenTanks[1].mirror = true;

		this.frequencyModule = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/frequencyModule.obj"));
	}

	@Override
	public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
	{
		final Class<?> entityClass = GCEntityClientPlayerMP.class;
		final Render render = RenderManager.instance.getEntityClassRenderObject(entityClass);
		final ModelBiped modelBipedMain = ((RenderPlayer) render).modelBipedMain;

		this.usingParachute = false;
        boolean wearingMask = false;
        boolean wearingGear = false;
        boolean wearingLeftTankGreen = false;
        boolean wearingLeftTankOrange = false;
        boolean wearingLeftTankRed = false;
        boolean wearingRightTankGreen = false;
        boolean wearingRightTankOrange = false;
        boolean wearingRightTankRed = false;
        boolean wearingFrequencyModule = false;

		final EntityPlayer player = (EntityPlayer) var1;
		PlayerGearData gearData = ClientProxyCore.playerItemData.get(player.getCommandSenderName());

		if (gearData != null)
		{
			usingParachute = gearData.getParachute() != null;
			wearingMask = gearData.getMask() > -1;
			wearingGear = gearData.getGear() > -1;
			wearingLeftTankGreen = gearData.getLeftTank() == 0;
			wearingLeftTankOrange = gearData.getLeftTank() == 1;
			wearingLeftTankRed = gearData.getLeftTank() == 2;
			wearingRightTankGreen = gearData.getRightTank() == 0;
			wearingRightTankOrange = gearData.getRightTank() == 1;
			wearingRightTankRed = gearData.getRightTank() == 2;
			wearingFrequencyModule = gearData.getFrequencyModule() > -1;
		}

		this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);

		if (var1 instanceof AbstractClientPlayer && this.equals(modelBipedMain))
		{
			if (gearData != null)
			{
				if (wearingMask)
				{
					FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerGC.oxygenMaskTexture);
					GL11.glPushMatrix();
					GL11.glScalef(1.05F, 1.05F, 1.05F);
					this.oxygenMask.render(var7);
					GL11.glScalef(1F, 1F, 1F);
					GL11.glPopMatrix();
				}

				//

				if (wearingFrequencyModule)
				{
					FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerGC.frequencyModuleTexture);
					GL11.glPushMatrix();
					GL11.glRotatef(180, 1, 0, 0);

					GL11.glRotatef((float) (this.bipedHeadwear.rotateAngleY * (-180.0F / Math.PI)), 0, 1, 0);
					GL11.glRotatef((float) (this.bipedHeadwear.rotateAngleX * (180.0F / Math.PI)), 1, 0, 0);
					GL11.glScalef(0.3F, 0.3F, 0.3F);
					GL11.glTranslatef(-1.1F, 1.2F, 0);
					this.frequencyModule.renderPart("Main");
					GL11.glTranslatef(0, 1.2F, 0);
					GL11.glRotatef((float) (Math.sin(var1.ticksExisted * 0.05) * 50.0F), 1, 0, 0);
					GL11.glRotatef((float) (Math.cos(var1.ticksExisted * 0.1) * 50.0F), 0, 1, 0);
					GL11.glTranslatef(0, -1.2F, 0);
					this.frequencyModule.renderPart("Radar");
					GL11.glPopMatrix();
				}

				//

				FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerGC.playerTexture);

				if (wearingGear)
				{
					for (int i = 0; i < 7; i++)
					{
						for (int k = 0; k < 2; k++)
						{
							this.tubes[k][i].render(var7);
						}
					}
				}

				//

				if (wearingLeftTankRed)
				{
					this.redOxygenTanks[0].render(var7);
				}

				//

				if (wearingLeftTankOrange)
				{
					this.orangeOxygenTanks[0].render(var7);
				}

				//

				if (wearingLeftTankGreen)
				{
					this.greenOxygenTanks[0].render(var7);
				}

				//

				if (wearingRightTankRed)
				{
					this.redOxygenTanks[1].render(var7);
				}

				//

				if (wearingRightTankOrange)
				{
					this.orangeOxygenTanks[1].render(var7);
				}

				//

				if (wearingRightTankGreen)
				{
					this.greenOxygenTanks[1].render(var7);
				}

				//

				if (usingParachute)
				{
					FMLClientHandler.instance().getClient().renderEngine.bindTexture(gearData.getParachute());

					this.parachute[0].render(var7);
					this.parachute[1].render(var7);
					this.parachute[2].render(var7);

					this.parachuteStrings[0].render(var7);
					this.parachuteStrings[1].render(var7);
					this.parachuteStrings[2].render(var7);
					this.parachuteStrings[3].render(var7);
				}
			}

			FMLClientHandler.instance().getClient().renderEngine.bindTexture(((AbstractClientPlayer) player).getLocationSkin());
		}

		super.render(var1, var2, var3, var4, var5, var6, var7);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
	{
		final EntityPlayer player = (EntityPlayer) par7Entity;
		final ItemStack currentItemStack = player.inventory.getCurrentItem(); 

		this.bipedHead.rotateAngleY = par4 / 57.29578F;
		this.bipedHead.rotateAngleX = par5 / 57.29578F;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		this.oxygenMask.rotateAngleY = this.bipedHead.rotateAngleY;
		this.oxygenMask.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;

		//Slower arm and leg motion if airborne in Galacticraft Dimensions
		if (!par7Entity.onGround && par7Entity.worldObj.provider instanceof IGalacticraftWorldProvider && !(currentItemStack != null && currentItemStack.getItem() instanceof IHoldableItem))
		{
			float speedModifier = 0.1162F * 2;
	
			float angularSwingArm = MathHelper.cos(par1 * (speedModifier / 2));
			float angularSwingLeg = MathHelper.cos(par1 * speedModifier);
			this.bipedRightArm.rotateAngleX = - angularSwingArm * 4.0F * par2 * 0.5F;
			this.bipedLeftArm.rotateAngleX = angularSwingArm * 4.0F * par2 * 0.5F;
			this.bipedRightArm.rotateAngleZ = (float) (5 * (Math.PI / 180));
			this.bipedLeftArm.rotateAngleZ = (float) (-5 * (Math.PI / 180));
			this.bipedRightLeg.rotateAngleX = angularSwingLeg * 1.45F * par2;
			this.bipedLeftLeg.rotateAngleX = -angularSwingLeg * 1.45F * par2;
		}
		else
		//Normal arm and leg motion
		{
			float angularSwingArm = MathHelper.cos(par1 * 0.6662F);
			float angularSwingLeg = angularSwingArm;
			this.bipedRightArm.rotateAngleX = - angularSwingArm * 2.0F * par2 * 0.5F;
			this.bipedLeftArm.rotateAngleX = angularSwingArm * 2.0F * par2 * 0.5F;
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightLeg.rotateAngleX = angularSwingLeg * 1.4F * par2;
			this.bipedLeftLeg.rotateAngleX = -angularSwingLeg * 1.4F * par2;
		}

		if (this.isRiding)
		{
			float thirtySixDegrees = (float) Math.PI / 5F;
			this.bipedRightArm.rotateAngleX += -thirtySixDegrees;
			this.bipedLeftArm.rotateAngleX += -thirtySixDegrees;
			this.bipedRightLeg.rotateAngleX = -thirtySixDegrees * 2F;
			this.bipedLeftLeg.rotateAngleX = -thirtySixDegrees * 2F;
			this.bipedRightLeg.rotateAngleY = thirtySixDegrees / 2F;
			this.bipedLeftLeg.rotateAngleY = -thirtySixDegrees / 2F;
		}

		if (this.heldItemLeft != 0)
		{
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - (float) Math.PI / 10F * this.heldItemLeft;
		}

		if (this.heldItemRight != 0)
		{
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - (float) Math.PI / 10F * this.heldItemRight;
		}

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedLeftArm.rotateAngleY = 0.0F;
		float f6;
		float f7;

		if (this.onGround > -9990.0F)
		{
			f6 = this.onGround;
			this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * (float) Math.PI * 2.0F) * 0.2F;
			this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedLeftArm.rotationPointZ = -this.bipedRightArm.rotationPointZ;
			this.bipedLeftArm.rotationPointX = -this.bipedRightArm.rotationPointX;
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
			f6 = 1.0F - this.onGround;
			f6 *= f6;
			f6 *= f6;
			f6 = 1.0F - f6;
			f7 = MathHelper.sin(f6 * (float) Math.PI);
			final float f8 = MathHelper.sin(this.onGround * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
			this.bipedRightArm.rotateAngleX = (float) (this.bipedRightArm.rotateAngleX - (f7 * 1.2D + f8));
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
			this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float) Math.PI) * -0.4F;
		}

		if (this.isSneak)
		{
			this.bipedBody.rotateAngleX = 0.5F;
			this.bipedRightArm.rotateAngleX += 0.4F;
			this.bipedLeftArm.rotateAngleX += 0.4F;
			this.bipedRightLeg.rotationPointZ = 4.0F;
			this.bipedLeftLeg.rotationPointZ = 4.0F;
			this.bipedRightLeg.rotationPointY = 9.0F;
			this.bipedLeftLeg.rotationPointY = 9.0F;
			this.bipedHead.rotationPointY = 1.0F;
			this.bipedHeadwear.rotationPointY = 1.0F;
		}
		else
		{
			this.bipedBody.rotateAngleX = 0.0F;
			this.bipedRightLeg.rotationPointZ = 0.1F;
			this.bipedLeftLeg.rotationPointZ = 0.1F;
			this.bipedRightLeg.rotationPointY = 12.0F;
			this.bipedLeftLeg.rotationPointY = 12.0F;
			this.bipedHead.rotationPointY = 0.0F;
			this.bipedHeadwear.rotationPointY = 0.0F;
		}

		if (this.aimedBow)
		{
			f6 = 0.0F;
			f7 = 0.0F;
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F) + this.bipedHead.rotateAngleY;
			this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
			this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
		}
		
		float partialAngleZ = MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		float partialAngleX = MathHelper.sin(par3 * 0.067F) * 0.05F;
		this.bipedRightArm.rotateAngleZ += partialAngleZ;
		this.bipedLeftArm.rotateAngleZ -= partialAngleZ;
		this.bipedRightArm.rotateAngleX += partialAngleX;
		this.bipedLeftArm.rotateAngleX -= partialAngleX;

		//Render parachute
		if (usingParachute)
		{
			this.parachute[0].rotateAngleZ = (float) (30F * (Math.PI / 180F));
			this.parachute[2].rotateAngleZ = (float) -(30F * (Math.PI / 180F));
			this.parachuteStrings[0].rotateAngleZ = (float) (155F * (Math.PI / 180F));
			this.parachuteStrings[0].rotateAngleX = (float) (23F * (Math.PI / 180F));
			this.parachuteStrings[0].setRotationPoint(-9.0F, -7.0F, 2.0F);
			this.parachuteStrings[1].rotateAngleZ = (float) (155F * (Math.PI / 180F));
			this.parachuteStrings[1].rotateAngleX = (float) -(23F * (Math.PI / 180F));
			this.parachuteStrings[1].setRotationPoint(-9.0F, -7.0F, 2.0F);
			this.parachuteStrings[2].rotateAngleZ = (float) -(155F * (Math.PI / 180F));
			this.parachuteStrings[2].rotateAngleX = (float) (23F * (Math.PI / 180F));
			this.parachuteStrings[2].setRotationPoint(9.0F, -7.0F, 2.0F);
			this.parachuteStrings[3].rotateAngleZ = (float) -(155F * (Math.PI / 180F));
			this.parachuteStrings[3].rotateAngleX = (float) -(23F * (Math.PI / 180F));
			this.parachuteStrings[3].setRotationPoint(9.0F, -7.0F, 2.0F);
			this.bipedLeftArm.rotateAngleX += (float) Math.PI;
			this.bipedLeftArm.rotateAngleZ += (float) Math.PI / 10;
			this.bipedRightArm.rotateAngleX += (float) Math.PI;
			this.bipedRightArm.rotateAngleZ -= (float) Math.PI / 10;
		}

		//Hold rockets and other items above the player's head
		if (currentItemStack != null && currentItemStack.getItem() instanceof IHoldableItem && !(par7Entity.ridingEntity instanceof EntityAutoRocket) && !(par7Entity.ridingEntity instanceof EntityLander))
		{
			IHoldableItem holdableItem = (IHoldableItem) currentItemStack.getItem();

			if (holdableItem.shouldHoldLeftHandUp(player))
			{
				this.bipedLeftArm.rotateAngleX = 0;
				this.bipedLeftArm.rotateAngleZ = 0;

				this.bipedLeftArm.rotateAngleX += (float) Math.PI + 0.3;
				this.bipedLeftArm.rotateAngleZ += (float) Math.PI / 10;
			}

			if (holdableItem.shouldHoldRightHandUp(player))
			{
				this.bipedRightArm.rotateAngleX = 0;
				this.bipedRightArm.rotateAngleZ = 0;

				this.bipedRightArm.rotateAngleX += (float) Math.PI + 0.3;
				this.bipedRightArm.rotateAngleZ -= (float) Math.PI / 10;
			}

			if (holdableItem.shouldCrouch(player))
			{
				this.bipedBody.rotateAngleX = 0.5F;
				this.bipedRightLeg.rotationPointZ = 4.0F;
				this.bipedLeftLeg.rotationPointZ = 4.0F;
				this.bipedRightLeg.rotationPointY = 9.0F;
				this.bipedLeftLeg.rotationPointY = 9.0F;
				this.bipedHead.rotationPointY = 1.0F;
				this.bipedHeadwear.rotationPointY = 1.0F;
			}
		}

		//Render the Oxygen Tanks etc
		this.greenOxygenTanks[0].rotateAngleX = this.bipedBody.rotateAngleX;
		this.greenOxygenTanks[0].rotateAngleY = this.bipedBody.rotateAngleY;
		this.greenOxygenTanks[0].rotateAngleZ = this.bipedBody.rotateAngleZ;
		this.greenOxygenTanks[1].rotateAngleX = this.bipedBody.rotateAngleX;
		this.greenOxygenTanks[1].rotateAngleY = this.bipedBody.rotateAngleY;
		this.greenOxygenTanks[1].rotateAngleZ = this.bipedBody.rotateAngleZ;
		this.orangeOxygenTanks[0].rotateAngleX = this.bipedBody.rotateAngleX;
		this.orangeOxygenTanks[0].rotateAngleY = this.bipedBody.rotateAngleY;
		this.orangeOxygenTanks[0].rotateAngleZ = this.bipedBody.rotateAngleZ;
		this.orangeOxygenTanks[1].rotateAngleX = this.bipedBody.rotateAngleX;
		this.orangeOxygenTanks[1].rotateAngleY = this.bipedBody.rotateAngleY;
		this.orangeOxygenTanks[1].rotateAngleZ = this.bipedBody.rotateAngleZ;
		this.redOxygenTanks[0].rotateAngleX = this.bipedBody.rotateAngleX;
		this.redOxygenTanks[0].rotateAngleY = this.bipedBody.rotateAngleY;
		this.redOxygenTanks[0].rotateAngleZ = this.bipedBody.rotateAngleZ;
		this.redOxygenTanks[1].rotateAngleX = this.bipedBody.rotateAngleX;
		this.redOxygenTanks[1].rotateAngleY = this.bipedBody.rotateAngleY;
		this.redOxygenTanks[1].rotateAngleZ = this.bipedBody.rotateAngleZ;

		//Attempt compatibility with SmartMoving mod
		try
		{
			if (Class.forName("mod_SmartMoving") != null || Class.forName("net.minecraft.src.mod_SmartMoving") != null)
			{
				super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
				return;
			}
		}
		catch (final ClassNotFoundException e1)
		{
		}

		final List<?> l = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getBoundingBox(player.posX - 20, 0, player.posZ - 20, player.posX + 20, 200, player.posZ + 20));

		for (int i = 0; i < l.size(); i++)
		{
			final Entity e = (Entity) l.get(i);

			if (e instanceof EntityTier1Rocket)
			{
				final EntityTier1Rocket ship = (EntityTier1Rocket) e;

				if (ship.riddenByEntity != null && !((EntityPlayer) ship.riddenByEntity).getGameProfile().getName().equals(player.getGameProfile().getName()) && (ship.getLaunched() || ship.timeUntilLaunch < 390))
				{
					this.bipedRightArm.rotateAngleZ -= (float) (Math.PI / 8) + MathHelper.sin(par3 * 0.9F) * 0.2F;
					this.bipedRightArm.rotateAngleX = (float) Math.PI;
				}
			}
		}

		if (ModelPlayerGC.crossbowModLoaded)
		{
			try
			{
				Class<?> c = Class.forName("micdoodle8.mods.crossbowmod.client.ClientProxy");

				Method m = c.getMethod("bipedRotationHook", ModelBiped.class, Entity.class, float.class);

				m.invoke(null, this, par7Entity, par3);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
