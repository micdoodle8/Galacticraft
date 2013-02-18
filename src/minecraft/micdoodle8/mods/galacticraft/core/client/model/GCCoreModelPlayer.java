package micdoodle8.mods.galacticraft.core.client.model;

import java.util.List;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiTankRefill;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModelPlayerAPI;
import net.minecraft.src.ModelPlayerBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.client.FMLClientHandler;

public class GCCoreModelPlayer extends ModelPlayerBase
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
    
	public GCCoreModelPlayer(ModelPlayerAPI mpapi)
	{
		super(mpapi);
	}

	@Override
    public void afterLocalConstructing(float var1)
    {
		this.oxygenMask = new ModelRenderer(this.modelPlayer, 32, 2);
        this.oxygenMask.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 1);
        this.oxygenMask.setRotationPoint(0.0F, 0.0F + 0.0F, 0.0F);
		
        this.parachute[0] = new ModelRenderer(this.modelPlayer, 0, 0).setTextureSize(512, 256);
        this.parachute[0].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, var1);
        this.parachute[0].setRotationPoint(15.0F, 4.0F, 0.0F);
        this.parachute[1] = new ModelRenderer(this.modelPlayer, 0, 42).setTextureSize(512, 256);
        this.parachute[1].addBox(-20.0F, -45.0F, -20.0F, 40, 2, 40, var1);
        this.parachute[1].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachute[2] = new ModelRenderer(this.modelPlayer, 0, 0).setTextureSize(512, 256);
        this.parachute[2].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, var1);
        this.parachute[2].setRotationPoint(11F, -11, 0.0F);

        this.parachuteStrings[0] = new ModelRenderer(this.modelPlayer, 100, 0).setTextureSize(512, 256);
        this.parachuteStrings[0].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
        this.parachuteStrings[0].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[1] = new ModelRenderer(this.modelPlayer, 100, 0).setTextureSize(512, 256);
        this.parachuteStrings[1].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
        this.parachuteStrings[1].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[2] = new ModelRenderer(this.modelPlayer, 100, 0).setTextureSize(512, 256);
        this.parachuteStrings[2].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
        this.parachuteStrings[2].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[3] = new ModelRenderer(this.modelPlayer, 100, 0).setTextureSize(512, 256);
        this.parachuteStrings[3].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
        this.parachuteStrings[3].setRotationPoint(0.0F, 0.0F, 0.0F);

		this.tubes[0][0] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[0][0].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][0].setRotationPoint(2F, 3F, 5.8F);
		this.tubes[0][0].setTextureSize(128, 64);
		this.tubes[0][0].mirror = true;
		this.tubes[0][1] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[0][1].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][1].setRotationPoint(2F, 2F, 6.8F);
		this.tubes[0][1].setTextureSize(128, 64);
		this.tubes[0][1].mirror = true;
		this.tubes[0][2] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[0][2].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][2].setRotationPoint(2F, 1F, 6.8F);
		this.tubes[0][2].setTextureSize(128, 64);
		this.tubes[0][2].mirror = true;
		this.tubes[0][3] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[0][3].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][3].setRotationPoint(2F, 0F, 6.8F);
		this.tubes[0][3].setTextureSize(128, 64);
		this.tubes[0][3].mirror = true;
		this.tubes[0][4] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[0][4].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][4].setRotationPoint(2F, -1F, 6.8F);
		this.tubes[0][4].setTextureSize(128, 64);
		this.tubes[0][4].mirror = true;
		this.tubes[0][5] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[0][5].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][5].setRotationPoint(2F, -2F, 5.8F);
		this.tubes[0][5].setTextureSize(128, 64);
		this.tubes[0][5].mirror = true;
		this.tubes[0][6] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[0][6].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][6].setRotationPoint(2F, -3F, 4.8F);
		this.tubes[0][6].setTextureSize(128, 64);
		this.tubes[0][6].mirror = true;

		this.tubes[1][0] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[1][0].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][0].setRotationPoint(-2F, 3F, 5.8F);
		this.tubes[1][0].setTextureSize(128, 64);
		this.tubes[1][0].mirror = true;
		this.tubes[1][1] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[1][1].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][1].setRotationPoint(-2F, 2F, 6.8F);
		this.tubes[1][1].setTextureSize(128, 64);
		this.tubes[1][1].mirror = true;
		this.tubes[1][2] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[1][2].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][2].setRotationPoint(-2F, 1F, 6.8F);
		this.tubes[1][2].setTextureSize(128, 64);
		this.tubes[1][2].mirror = true;
		this.tubes[1][3] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[1][3].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][3].setRotationPoint(-2F, 0F, 6.8F);
		this.tubes[1][3].setTextureSize(128, 64);
		this.tubes[1][3].mirror = true;
		this.tubes[1][4] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[1][4].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][4].setRotationPoint(-2F, -1F, 6.8F);
		this.tubes[1][4].setTextureSize(128, 64);
		this.tubes[1][4].mirror = true;
		this.tubes[1][5] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[1][5].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][5].setRotationPoint(-2F, -2F, 5.8F);
		this.tubes[1][5].setTextureSize(128, 64);
		this.tubes[1][5].mirror = true;
		this.tubes[1][6] = new ModelRenderer(this.modelPlayer, 0, 0);
		this.tubes[1][6].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][6].setRotationPoint(-2F, -3F, 4.8F);
		this.tubes[1][6].setTextureSize(128, 64);
		this.tubes[1][6].mirror = true;
		
		this.greenOxygenTanks[0] = new ModelRenderer(this.modelPlayer, 4, 0);
		this.greenOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.greenOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
		this.greenOxygenTanks[0].mirror = true;
		this.greenOxygenTanks[1] = new ModelRenderer(this.modelPlayer, 4, 0);
		this.greenOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.greenOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
		this.greenOxygenTanks[1].mirror = true;
		
		this.orangeOxygenTanks[0] = new ModelRenderer(this.modelPlayer, 16, 0);
		this.orangeOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.orangeOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
		this.orangeOxygenTanks[0].mirror = true;
		this.orangeOxygenTanks[1] = new ModelRenderer(this.modelPlayer, 16, 0);
		this.orangeOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.orangeOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
		this.orangeOxygenTanks[1].mirror = true;
		
		this.redOxygenTanks[0] = new ModelRenderer(this.modelPlayer, 28, 0);
		this.redOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.redOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
		this.redOxygenTanks[0].mirror = true;
		this.redOxygenTanks[1] = new ModelRenderer(this.modelPlayer, 28, 0);
		this.redOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.redOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
		this.redOxygenTanks[1].mirror = true;
		
    	super.afterLocalConstructing(var1);
    }

    @Override
	public void afterRender(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
    	final Class<?> entityClass = EntityClientPlayerMP.class;
    	final Render render = RenderManager.instance.getEntityClassRenderObject(entityClass);
    	final ModelBiped modelBipedMain = ((RenderPlayer)render).getModelBipedMainField();
    	
    	if (var1 instanceof EntityPlayer && this.modelPlayer == modelBipedMain)
    	{
        	final EntityPlayer player = (EntityPlayer)var1;
        	final int j;
        	
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

    		FMLClientHandler.instance().getClient().renderEngine.bindTexture(FMLClientHandler.instance().getClient().renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/entities/player.png"));
    		
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
        		FMLClientHandler.instance().getClient().renderEngine.bindTexture(FMLClientHandler.instance().getClient().renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/entities/parachute/" + ClientProxyCore.parachuteTextures.get(player.username) + ".png"));

            	this.parachute[0].render(var7);
            	this.parachute[1].render(var7);
            	this.parachute[2].render(var7);

            	this.parachuteStrings[0].render(var7);
            	this.parachuteStrings[1].render(var7);
            	this.parachuteStrings[2].render(var7);
            	this.parachuteStrings[3].render(var7);
        	}

        	this.loadDownloadableImageTexture(var1.skinUrl, var1.getTexture());
        	
        	super.afterRender(var1, var2, var3, var4, var5, var6, var7);
    	}
    	else
    	{
        	super.afterRender(var1, var2, var3, var4, var5, var6, var7);
    	}
    }

    protected boolean loadDownloadableImageTexture(String par1Str, String par2Str)
    {
        final RenderEngine var3 = RenderManager.instance.renderEngine;
        final int var4 = var3.getTextureForDownloadableImage(par1Str, par2Str);

        if (var4 >= 0)
        {
            var3.bindTexture(var4);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
	public void afterSetRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7)
    {
    	
    	float speedModifier = 0.0F;
    	
    	if (var7.onGround)
    	{
    		speedModifier = 0.1162F;
    	}
    	else
    	{
    		speedModifier = 0.1162F * 2;
    	}
    	
    	if (!var7.onGround && var7.worldObj.provider instanceof IGalacticraftWorldProvider)
    	{
            this.modelPlayer.bipedLeftLeg.rotateAngleX = MathHelper.cos(var1 * speedModifier + (float)Math.PI) * 1.45F * var2;
            this.modelPlayer.bipedRightLeg.rotateAngleX = MathHelper.cos(var1 * speedModifier) * 1.45F * var2;
            this.modelPlayer.bipedRightArm.rotateAngleX += MathHelper.cos(var1 * (speedModifier / 2) + (float)Math.PI) * 4.0F * var2 * 0.5F;
            this.modelPlayer.bipedLeftArm.rotateAngleX += MathHelper.cos(var1 * (speedModifier / 2)) * 4.0F * var2 * 0.5F;
            this.modelPlayer.bipedRightArm.rotateAngleY += -MathHelper.cos(var1 * 0.1162F) * 0.2F;
            this.modelPlayer.bipedLeftArm.rotateAngleY += -MathHelper.cos(var1 * 0.1162F) * 0.2F;
            this.modelPlayer.bipedRightArm.rotateAngleZ += (float) (5 * (Math.PI / 180));
            this.modelPlayer.bipedLeftArm.rotateAngleZ += (float) (-5 * (Math.PI / 180));
    	}
    	
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
        	this.modelPlayer.bipedLeftArm.rotateAngleX += (float) Math.PI;
        	this.modelPlayer.bipedLeftArm.rotateAngleZ += (float) Math.PI / 10;
        	this.modelPlayer.bipedRightArm.rotateAngleX += (float) Math.PI;
        	this.modelPlayer.bipedRightArm.rotateAngleZ -= (float) Math.PI / 10;
    	}
    	
        float var8;
        float var9;

        if (this.modelPlayer.onGround > -9990.0F)
        {
            var8 = this.modelPlayer.onGround;
            this.modelPlayer.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var8) * (float)Math.PI * 2.0F) * 0.2F;
            this.modelPlayer.bipedRightArm.rotationPointZ = MathHelper.sin(this.modelPlayer.bipedBody.rotateAngleY) * 5.0F;
            this.modelPlayer.bipedRightArm.rotationPointX = -MathHelper.cos(this.modelPlayer.bipedBody.rotateAngleY) * 5.0F;
            this.modelPlayer.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.modelPlayer.bipedBody.rotateAngleY) * 5.0F;
            this.modelPlayer.bipedLeftArm.rotationPointX = MathHelper.cos(this.modelPlayer.bipedBody.rotateAngleY) * 5.0F;
            this.modelPlayer.bipedRightArm.rotateAngleY += this.modelPlayer.bipedBody.rotateAngleY;
            this.modelPlayer.bipedLeftArm.rotateAngleY += this.modelPlayer.bipedBody.rotateAngleY;
            this.modelPlayer.bipedLeftArm.rotateAngleX += this.modelPlayer.bipedBody.rotateAngleY;
            var8 = 1.0F - this.modelPlayer.onGround;
            var8 *= var8;
            var8 *= var8;
            var8 = 1.0F - var8;
            var9 = MathHelper.sin(var8 * (float)Math.PI);
            float var10 = MathHelper.sin(this.modelPlayer.onGround * (float)Math.PI) * -(this.modelPlayer.bipedHead.rotateAngleX - 0.7F) * 0.75F;
            this.modelPlayer.bipedRightArm.rotateAngleX = (float)((double)this.modelPlayer.bipedRightArm.rotateAngleX - ((double)var9 * 1.2D + (double)var10));
            this.modelPlayer.bipedRightArm.rotateAngleY += this.modelPlayer.bipedBody.rotateAngleY * 2.0F;
            this.modelPlayer.bipedRightArm.rotateAngleZ = MathHelper.sin(this.modelPlayer.onGround * (float)Math.PI) * -0.4F;
        }

    	final EntityPlayer player = (EntityPlayer)var7;
//
//    	if (player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntitySpaceship)
//    	{
//    		GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;
//
////        	this.modelPlayer.bipedBody.rotateAngleX = ship.rotationYaw % 360;
//        	this.modelPlayer.bipedBody.rotateAngleX = (float) (ship.rotationYaw * (Math.PI / 180F));
//        	this.modelPlayer.bipedBody.rotateAngleZ = (float) (ship.rotationPitch * (Math.PI / 180F));
//        	this.modelPlayer.bipedHead.rotateAngleX = (float) (ship.rotationYaw * (Math.PI / 180F));
//        	this.modelPlayer.bipedHead.rotateAngleZ = (float) (ship.rotationPitch * (Math.PI / 180F));
//        	this.modelPlayer.bipedLeftArm.rotateAngleX = (float) (ship.rotationYaw * (Math.PI / 180F));
//        	this.modelPlayer.bipedLeftArm.rotateAngleZ = (float) (ship.rotationPitch * (Math.PI / 180F));
//        	this.modelPlayer.bipedRightArm.rotateAngleX = (float) (ship.rotationYaw * (Math.PI / 180F));
//        	this.modelPlayer.bipedRightArm.rotateAngleZ = (float) (ship.rotationPitch * (Math.PI / 180F));
//        	this.modelPlayer.bipedRightLeg.rotateAngleX = (float) (ship.rotationYaw * (Math.PI / 180F));
//        	this.modelPlayer.bipedRightLeg.rotateAngleZ = (float) (ship.rotationPitch * (Math.PI / 180F));
//        	this.modelPlayer.bipedLeftLeg.rotateAngleX = (float) (ship.rotationYaw * (Math.PI / 180F));
//        	this.modelPlayer.bipedLeftLeg.rotateAngleZ = (float) (ship.rotationPitch * (Math.PI / 180F));
//    	}
    	
    	this.greenOxygenTanks[0].rotateAngleX = this.modelPlayer.bipedBody.rotateAngleX;
    	this.greenOxygenTanks[0].rotateAngleY = this.modelPlayer.bipedBody.rotateAngleY;
    	this.greenOxygenTanks[0].rotateAngleZ = this.modelPlayer.bipedBody.rotateAngleZ;
    	this.greenOxygenTanks[1].rotateAngleX = this.modelPlayer.bipedBody.rotateAngleX;
    	this.greenOxygenTanks[1].rotateAngleY = this.modelPlayer.bipedBody.rotateAngleY;
    	this.greenOxygenTanks[1].rotateAngleZ = this.modelPlayer.bipedBody.rotateAngleZ;
    	this.orangeOxygenTanks[0].rotateAngleX = this.modelPlayer.bipedBody.rotateAngleX;
    	this.orangeOxygenTanks[0].rotateAngleY = this.modelPlayer.bipedBody.rotateAngleY;
    	this.orangeOxygenTanks[0].rotateAngleZ = this.modelPlayer.bipedBody.rotateAngleZ;
    	this.orangeOxygenTanks[1].rotateAngleX = this.modelPlayer.bipedBody.rotateAngleX;
    	this.orangeOxygenTanks[1].rotateAngleY = this.modelPlayer.bipedBody.rotateAngleY;
    	this.orangeOxygenTanks[1].rotateAngleZ = this.modelPlayer.bipedBody.rotateAngleZ;
    	this.redOxygenTanks[0].rotateAngleX = this.modelPlayer.bipedBody.rotateAngleX;
    	this.redOxygenTanks[0].rotateAngleY = this.modelPlayer.bipedBody.rotateAngleY;
    	this.redOxygenTanks[0].rotateAngleZ = this.modelPlayer.bipedBody.rotateAngleZ;
    	this.redOxygenTanks[1].rotateAngleX = this.modelPlayer.bipedBody.rotateAngleX;
    	this.redOxygenTanks[1].rotateAngleY = this.modelPlayer.bipedBody.rotateAngleY;
    	this.redOxygenTanks[1].rotateAngleZ = this.modelPlayer.bipedBody.rotateAngleZ;
    	
    	if (!(FMLClientHandler.instance().getClient().currentScreen instanceof GuiInventory) && !(FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiTankRefill))
    	{
            this.oxygenMask.rotationPointX = this.modelPlayer.bipedHeadwear.rotationPointX;
            this.oxygenMask.rotationPointY = this.modelPlayer.bipedHeadwear.rotationPointY;
            this.oxygenMask.rotationPointZ = this.modelPlayer.bipedHeadwear.rotationPointZ;
            this.oxygenMask.rotateAngleX = this.modelPlayer.bipedHeadwear.rotateAngleX;
            this.oxygenMask.rotateAngleY = this.modelPlayer.bipedHeadwear.rotateAngleY;
            this.oxygenMask.rotateAngleZ = this.modelPlayer.bipedHeadwear.rotateAngleZ;
    	}
    	
		final List l = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(player.posX - 20, 0, player.posZ - 20, player.posX + 20, 200, player.posZ + 20));
	
		for (int i = 0; i < l.size(); i++)
		{
			final Entity e = (Entity) l.get(i);
			
			if (e instanceof GCCoreEntitySpaceship)
			{
				final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship)e;
				
				if (ship.riddenByEntity != null && !((EntityPlayer)ship.riddenByEntity).username.equals(player.username) && (ship.getLaunched() == 1 || ship.getTimeUntilLaunch() < 390))
				{
			    	this.modelPlayer.bipedRightArm.rotateAngleZ -= (float) (Math.PI / 8) + MathHelper.sin(var3 * 0.9F) * 0.2F;
			    	this.modelPlayer.bipedRightArm.rotateAngleX = (float) Math.PI;
				}
			}
		}
		
    	super.afterSetRotationAngles(var1, var2, var3, var4, var5, var6, var7);
    }

    public void renderCloak(float var1)
    {
    	super.renderCloak(var1);
    }
}
