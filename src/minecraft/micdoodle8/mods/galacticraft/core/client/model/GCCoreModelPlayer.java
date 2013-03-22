package micdoodle8.mods.galacticraft.core.client.model;

import java.util.List;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderPlayer;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSpaceship;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class GCCoreModelPlayer extends ModelBiped
{
    public ModelRenderer[] parachute = new ModelRenderer[3];
    public ModelRenderer[] parachuteStrings = new ModelRenderer[4];
    public ModelRenderer[][] tubes = new ModelRenderer[2][7];
    public ModelRenderer[] greenOxygenTanks = new ModelRenderer[2];
    public ModelRenderer[] orangeOxygenTanks = new ModelRenderer[2];
    public ModelRenderer[] redOxygenTanks = new ModelRenderer[2];
    public ModelRenderer oxygenMask;

	boolean usingParachute = false;
	boolean wearingMask = false;
	boolean wearingGear = false;
	boolean wearingLeftTankRed = false;
	boolean wearingLeftTankOrange = false;
	boolean wearingLeftTankGreen = false;
	boolean wearingRightTankRed = false;
	boolean wearingRightTankOrange = false;
	boolean wearingRightTankGreen = false;

    public GCCoreModelPlayer(float var1)
    {
    	super(var1);
    	
		this.oxygenMask = new ModelRenderer(this, 32, 2);
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
    }

    @Override
	public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
    	final Class<?> entityClass = GCCorePlayerBaseClient.class;
    	final Render render = RenderManager.instance.getEntityClassRenderObject(entityClass);
    	final ModelBiped modelBipedMain = ((GCCoreRenderPlayer)render).getModel();
    	
    	if (var1 instanceof EntityPlayer && this.equals(modelBipedMain))
    	{
        	final EntityPlayer player = (EntityPlayer)var1;
        	boolean changed = false;

            for (final String name : ClientProxyCore.playersUsingParachutes)
            {
    			if (player.username.equals(name))
    			{
    				this.usingParachute = true;
    				changed = true;
    			}
            }

            if (!changed || ClientProxyCore.parachuteTextures.get(player.username).equals("none"))
            {
            	this.usingParachute = false;
            }

            //

    		FMLClientHandler.instance().getClient().renderEngine.func_98187_b("/micdoodle8/mods/galacticraft/core/client/entities/player.png");

            changed = false;

            for (final String name : ClientProxyCore.playersWithOxygenMask)
            {
    			if (player.username.equals(name))
    			{
    				this.wearingMask = true;
    				changed = true;
    			}
            }

            if (!changed)
            {
            	this.wearingMask = false;
            }

			if (this.wearingMask)
			{
				this.oxygenMask.render(var7);
			}

			//

            changed = false;

            for (final String name : ClientProxyCore.playersWithOxygenGear)
            {
    			if (player.username.equals(name))
    			{
    				this.wearingGear = true;
    				changed = true;
    			}
            }

            if (!changed)
            {
            	this.wearingGear = false;
            }

			if (this.wearingGear)
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

            changed = false;

            for (final String name : ClientProxyCore.playersWithOxygenTankLeftRed)
            {
    			if (player.username.equals(name))
    			{
    				this.wearingLeftTankRed = true;
    				changed = true;
    			}
            }

            if (!changed)
            {
            	this.wearingLeftTankRed = false;
            }

			if (this.wearingLeftTankRed)
			{
				this.redOxygenTanks[0].render(var7);
			}

			//

            changed = false;

            for (final String name : ClientProxyCore.playersWithOxygenTankLeftOrange)
            {
    			if (player.username.equals(name))
    			{
    				this.wearingLeftTankOrange = true;
    				changed = true;
    			}
            }

            if (!changed)
            {
            	this.wearingLeftTankOrange = false;
            }

			if (this.wearingLeftTankOrange)
			{
				this.orangeOxygenTanks[0].render(var7);
			}

			//

            changed = false;

            for (final String name : ClientProxyCore.playersWithOxygenTankLeftGreen)
            {
    			if (player.username.equals(name))
    			{
    				this.wearingLeftTankGreen = true;
    				changed = true;
    			}
            }

            if (!changed)
            {
            	this.wearingLeftTankGreen = false;
            }

			if (this.wearingLeftTankGreen)
			{
				this.greenOxygenTanks[0].render(var7);
			}

			//

            changed = false;

            for (final String name : ClientProxyCore.playersWithOxygenTankRightRed)
            {
    			if (player.username.equals(name))
    			{
    				this.wearingRightTankRed = true;
    				changed = true;
    			}
            }

            if (!changed)
            {
            	this.wearingRightTankRed = false;
            }

			if (this.wearingRightTankRed)
			{
				this.redOxygenTanks[1].render(var7);
			}

			//

            changed = false;

            for (final String name : ClientProxyCore.playersWithOxygenTankRightOrange)
            {
    			if (player.username.equals(name))
    			{
    				this.wearingRightTankOrange = true;
    				changed = true;
    			}
            }

            if (!changed)
            {
            	this.wearingRightTankOrange = false;
            }

			if (this.wearingRightTankOrange)
			{
				this.orangeOxygenTanks[1].render(var7);
			}

			//

            changed = false;

            for (final String name : ClientProxyCore.playersWithOxygenTankRightGreen)
            {
    			if (player.username.equals(name))
    			{
    				this.wearingRightTankGreen = true;
    				changed = true;
    			}
            }

            if (!changed)
            {
            	this.wearingRightTankGreen = false;
            }

			if (this.wearingRightTankGreen)
			{
				this.greenOxygenTanks[1].render(var7);
			}

			//

        	if (this.usingParachute)
        	{
        		FMLClientHandler.instance().getClient().renderEngine.func_98187_b("/micdoodle8/mods/galacticraft/core/client/entities/parachute/" + ClientProxyCore.parachuteTextures.get(player.username) + ".png");

            	this.parachute[0].render(var7);
            	this.parachute[1].render(var7);
            	this.parachute[2].render(var7);

            	this.parachuteStrings[0].render(var7);
            	this.parachuteStrings[1].render(var7);
            	this.parachuteStrings[2].render(var7);
            	this.parachuteStrings[3].render(var7);
        	}

        	this.loadDownloadableImageTexture(var1.skinUrl, var1.getTexture());
    	}
    	
    	super.render(var1, var2, var3, var4, var5, var6, var7);
    }

    protected boolean loadDownloadableImageTexture(String par1Str, String par2Str)
    {
        final RenderEngine var3 = RenderManager.instance.renderEngine;
        final int var4 = var3.getTextureForDownloadableImage(par1Str, par2Str);

        if (var4 >= 0)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
    	this.bipedHead.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.bipedHead.rotateAngleX = par5 / (180F / (float)Math.PI);
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 2.0F * par2 * 0.5F;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;

        if (this.isRiding)
        {
            this.bipedRightArm.rotateAngleX += -((float)Math.PI / 5F);
            this.bipedLeftArm.rotateAngleX += -((float)Math.PI / 5F);
            this.bipedRightLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.bipedLeftLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.bipedRightLeg.rotateAngleY = (float)Math.PI / 10F;
            this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
        }

        if (this.heldItemLeft != 0)
        {
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - (float)Math.PI / 10F * this.heldItemLeft;
        }

        if (this.heldItemRight != 0)
        {
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - (float)Math.PI / 10F * this.heldItemRight;
        }

        this.bipedRightArm.rotateAngleY = 0.0F;
        this.bipedLeftArm.rotateAngleY = 0.0F;
        float f6;
        float f7;

        if (this.onGround > -9990.0F)
        {
            f6 = this.onGround;
            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI * 2.0F) * 0.2F;
            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
            f6 = 1.0F - this.onGround;
            f6 *= f6;
            f6 *= f6;
            f6 = 1.0F - f6;
            f7 = MathHelper.sin(f6 * (float)Math.PI);
            float f8 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
            this.bipedRightArm.rotateAngleX = (float)(this.bipedRightArm.rotateAngleX - (f7 * 1.2D + f8));
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
            this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
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

        this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

        if (this.aimedBow)
        {
            f6 = 0.0F;
            f7 = 0.0F;
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F) + this.bipedHead.rotateAngleY;
            this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
            this.bipedRightArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
            this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
            this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
            this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
        }
        
    	if (!((EntityPlayer) par7Entity).onGround && ((EntityPlayer) par7Entity).worldObj.provider instanceof IGalacticraftWorldProvider && !(((EntityPlayer) par7Entity).inventory.getCurrentItem() != null && ((EntityPlayer) par7Entity).inventory.getCurrentItem().getItem() instanceof GCCoreItemSpaceship))
    	{
    		this.bipedHead.rotateAngleY = par4 / (180F / (float)Math.PI);
            this.bipedHead.rotateAngleX = par5 / (180F / (float)Math.PI);
            this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
            this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
            this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 2.0F * par2 * 0.5F;
            this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
            this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
            this.bipedRightLeg.rotateAngleY = 0.0F;
            this.bipedLeftLeg.rotateAngleY = 0.0F;

        	float speedModifier = 0.0F;

        	if (par7Entity.onGround)
        	{
        		speedModifier = 0.1162F;
        	}
        	else
        	{
        		speedModifier = 0.1162F * 2;
        	}

            this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * speedModifier + (float)Math.PI) * 1.45F * par2;
            this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * speedModifier) * 1.45F * par2;
            this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * (speedModifier / 2) + (float)Math.PI) * 4.0F * par2 * 0.5F;
            this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * (speedModifier / 2)) * 4.0F * par2 * 0.5F;
            this.bipedRightArm.rotateAngleY = -MathHelper.cos(par1 * 0.1162F) * 0.2F;
            this.bipedLeftArm.rotateAngleY = -MathHelper.cos(par1 * 0.1162F) * 0.2F;
            this.bipedRightArm.rotateAngleZ = (float) (5 * (Math.PI / 180));
            this.bipedLeftArm.rotateAngleZ = (float) (-5 * (Math.PI / 180));

            if (this.isRiding)
            {
                this.bipedRightArm.rotateAngleX += -((float)Math.PI / 5F);
                this.bipedLeftArm.rotateAngleX += -((float)Math.PI / 5F);
                this.bipedRightLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
                this.bipedLeftLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
                this.bipedRightLeg.rotateAngleY = (float)Math.PI / 10F;
                this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
            }

            if (this.heldItemLeft != 0)
            {
                this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - (float)Math.PI / 10F * this.heldItemLeft;
            }

            if (this.heldItemRight != 0)
            {
                this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - (float)Math.PI / 10F * this.heldItemRight;
            }

            this.bipedRightArm.rotateAngleY = 0.0F;
            this.bipedLeftArm.rotateAngleY = 0.0F;
            float var8;
            float var9;

            if (this.onGround > -9990.0F)
            {
                var8 = this.onGround;
                this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var8) * (float)Math.PI * 2.0F) * 0.2F;
                this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
                this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
                this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
                var8 = 1.0F - this.onGround;
                var8 *= var8;
                var8 *= var8;
                var8 = 1.0F - var8;
                var9 = MathHelper.sin(var8 * (float)Math.PI);
                final float var10 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
                this.bipedRightArm.rotateAngleX = (float)(this.bipedRightArm.rotateAngleX - (var9 * 1.2D + var10));
                this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
                this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
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

            this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

            if (this.aimedBow)
            {
                var8 = 0.0F;
                var9 = 0.0F;
                this.bipedRightArm.rotateAngleZ = 0.0F;
                this.bipedLeftArm.rotateAngleZ = 0.0F;
                this.bipedRightArm.rotateAngleY = -(0.1F - var8 * 0.6F) + this.bipedHead.rotateAngleY;
                this.bipedLeftArm.rotateAngleY = 0.1F - var8 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
                this.bipedRightArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
                this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
                this.bipedRightArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
                this.bipedLeftArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
                this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
                this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
                this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
                this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
            }
    	}
    	
    	boolean holdingSpaceship = false;

        this.oxygenMask.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.oxygenMask.rotateAngleX = par5 / (180F / (float)Math.PI);

    	if (this.usingParachute)
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

        if (par7Entity instanceof EntityPlayer)
        {
        	final EntityPlayer player = (EntityPlayer) par7Entity;

        	if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof GCCoreItemSpaceship)
        	{
        		holdingSpaceship = true;
        		

            	this.bipedLeftArm.rotateAngleX = 0;
            	this.bipedLeftArm.rotateAngleZ = 0;
            	this.bipedRightArm.rotateAngleX = 0;
            	this.bipedRightArm.rotateAngleZ = 0;

            	this.bipedLeftArm.rotateAngleX += (float) Math.PI + 0.3;
            	this.bipedLeftArm.rotateAngleZ += (float) Math.PI / 10;
            	this.bipedRightArm.rotateAngleX += (float) Math.PI + 0.3;
            	this.bipedRightArm.rotateAngleZ -= (float) Math.PI / 10;

                this.bipedBody.rotateAngleX = 0.5F;
                this.bipedRightLeg.rotationPointZ = 4.0F;
                this.bipedLeftLeg.rotationPointZ = 4.0F;
                this.bipedRightLeg.rotationPointY = 9.0F;
                this.bipedLeftLeg.rotationPointY = 9.0F;
                this.bipedHead.rotationPointY = 1.0F;
                this.bipedHeadwear.rotationPointY = 1.0F;
        	}
        }

    	try
    	{
			if (EntityCreeper.class.forName("mod_SmartMoving") != null || EntityCreeper.class.forName("net.minecraft.src.mod_SmartMoving") != null)
			{
				super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
				return;
			}
		}
    	catch (final ClassNotFoundException e1)
    	{
		}

    	final EntityPlayer player = (EntityPlayer)par7Entity;

		final List l = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getAABBPool().getAABB(player.posX - 20, 0, player.posZ - 20, player.posX + 20, 200, player.posZ + 20));

		for (int i = 0; i < l.size(); i++)
		{
			final Entity e = (Entity) l.get(i);

			if (e instanceof GCCoreEntitySpaceship)
			{
				final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship)e;

				if (ship.riddenByEntity != null && !((EntityPlayer)ship.riddenByEntity).username.equals(player.username) && (ship.getLaunched() == 1 || ship.getTimeUntilLaunch() < 390))
				{
			    	this.bipedRightArm.rotateAngleZ -= (float) (Math.PI / 8) + MathHelper.sin(par3 * 0.9F) * 0.2F;
			    	this.bipedRightArm.rotateAngleX = (float) Math.PI;
				}
			}
		}
    }

//    @Override
//	public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7)
//    {
    	
//
//    	super.afterSetRotationAngles(var1, var2, var3, var4, var5, var6, var7);
//    }

    @Override
	public void renderCloak(float var1)
    {
    	super.renderCloak(var1);
    }
}
