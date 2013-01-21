package micdoodle8.mods.galacticraft.core.client.model;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.src.ModelPlayerAPI;
import net.minecraft.src.ModelPlayerBase;

public class GCCoreModelPlayer extends ModelPlayerBase
{
	public boolean usingParachute = false;
	
    public ModelRenderer[] parachute = new ModelRenderer[3];
    public ModelRenderer[] parachuteStrings = new ModelRenderer[4];
    public ModelRenderer[][] tubes = new ModelRenderer[2][7];
    public ModelRenderer[] oxygenTanks = new ModelRenderer[2];
    
	public GCCoreModelPlayer(ModelPlayerAPI mpapi) 
	{
		super(mpapi);
	}

	@Override
    public void afterLocalConstructing(float var1) 
    {
        this.parachute[0] = new ModelRenderer(this.modelPlayer, 0, 0);
        this.parachute[0].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, var1);
        this.parachute[0].setRotationPoint(15.0F, 4.0F, 0.0F);
        this.parachute[1] = new ModelRenderer(this.modelPlayer, 0, 0);
        this.parachute[1].addBox(-20.0F, -45.0F, -20.0F, 40, 2, 40, var1);
        this.parachute[1].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachute[2] = new ModelRenderer(this.modelPlayer, 0, 0);
        this.parachute[2].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, var1);
        this.parachute[2].setRotationPoint(11F, -11, 0.0F);

        this.parachuteStrings[0] = new ModelRenderer(this.modelPlayer, 0, 0);
        this.parachuteStrings[0].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
        this.parachuteStrings[0].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[1] = new ModelRenderer(this.modelPlayer, 0, 0);
        this.parachuteStrings[1].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
        this.parachuteStrings[1].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[2] = new ModelRenderer(this.modelPlayer, 0, 0);
        this.parachuteStrings[2].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
        this.parachuteStrings[2].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[3] = new ModelRenderer(this.modelPlayer, 0, 0);
        this.parachuteStrings[3].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
        this.parachuteStrings[3].setRotationPoint(0.0F, 0.0F, 0.0F);

		this.tubes[0][0] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[0][0].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][0].setRotationPoint(2F, 3F, 5.8F);
		this.tubes[0][0].setTextureSize(128, 64);
		this.tubes[0][0].mirror = true;
		this.tubes[0][1] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[0][1].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][1].setRotationPoint(2F, 2F, 6.8F);
		this.tubes[0][1].setTextureSize(128, 64);
		this.tubes[0][1].mirror = true;
		this.tubes[0][2] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[0][2].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][2].setRotationPoint(2F, 1F, 6.8F);
		this.tubes[0][2].setTextureSize(128, 64);
		this.tubes[0][2].mirror = true;
		this.tubes[0][3] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[0][3].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][3].setRotationPoint(2F, 0F, 6.8F);
		this.tubes[0][3].setTextureSize(128, 64);
		this.tubes[0][3].mirror = true;
		this.tubes[0][4] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[0][4].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][4].setRotationPoint(2F, -1F, 6.8F);
		this.tubes[0][4].setTextureSize(128, 64);
		this.tubes[0][4].mirror = true;
		this.tubes[0][5] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[0][5].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][5].setRotationPoint(2F, -2F, 5.8F);
		this.tubes[0][5].setTextureSize(128, 64);
		this.tubes[0][5].mirror = true;
		this.tubes[0][6] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[0][6].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[0][6].setRotationPoint(2F, -3F, 4.8F);
		this.tubes[0][6].setTextureSize(128, 64);
		this.tubes[0][6].mirror = true;


		this.tubes[1][0] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[1][0].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][0].setRotationPoint(-2F, 3F, 5.8F);
		this.tubes[1][0].setTextureSize(128, 64);
		this.tubes[1][0].mirror = true;
		this.tubes[1][1] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[1][1].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][1].setRotationPoint(-2F, 2F, 6.8F);
		this.tubes[1][1].setTextureSize(128, 64);
		this.tubes[1][1].mirror = true;
		this.tubes[1][2] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[1][2].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][2].setRotationPoint(-2F, 1F, 6.8F);
		this.tubes[1][2].setTextureSize(128, 64);
		this.tubes[1][2].mirror = true;
		this.tubes[1][3] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[1][3].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][3].setRotationPoint(-2F, 0F, 6.8F);
		this.tubes[1][3].setTextureSize(128, 64);
		this.tubes[1][3].mirror = true;
		this.tubes[1][4] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[1][4].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][4].setRotationPoint(-2F, -1F, 6.8F);
		this.tubes[1][4].setTextureSize(128, 64);
		this.tubes[1][4].mirror = true;
		this.tubes[1][5] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[1][5].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][5].setRotationPoint(-2F, -2F, 5.8F);
		this.tubes[1][5].setTextureSize(128, 64);
		this.tubes[1][5].mirror = true;
		this.tubes[1][6] = new ModelRenderer(this.modelPlayer, 48, 30);
		this.tubes[1][6].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
		this.tubes[1][6].setRotationPoint(-2F, -3F, 4.8F);
		this.tubes[1][6].setTextureSize(128, 64);
		this.tubes[1][6].mirror = true;
		
		this.oxygenTanks[0] = new ModelRenderer(this.modelPlayer, 48, 20);
		this.oxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.oxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
		this.oxygenTanks[0].setTextureSize(128, 64);
		this.oxygenTanks[0].mirror = true;
		this.oxygenTanks[1] = new ModelRenderer(this.modelPlayer, 48, 20);
		this.oxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
		this.oxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
		this.oxygenTanks[1].setTextureSize(128, 64);
		this.oxygenTanks[1].mirror = true;
		
    	super.afterLocalConstructing(var1);
    }

    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
    	if (usingParachute)
    	{
    		FMLClientHandler.instance().getClient().renderEngine.bindTexture(FMLClientHandler.instance().getClient().renderEngine.getTexture("/terrain.png"));
    		
        	this.parachute[0].render(var7);
        	this.parachute[1].render(var7);
        	this.parachute[2].render(var7);
        	
        	this.parachuteStrings[0].render(var7);
        	this.parachuteStrings[1].render(var7);
        	this.parachuteStrings[2].render(var7);
        	this.parachuteStrings[3].render(var7);
    	}

//		FMLClientHandler.instance().getClient().renderEngine.bindTexture(FMLClientHandler.instance().getClient().renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/entities/skeleton.png"));
		
    	for (int i = 0; i < 7; i++)
    	{
    		for (int j = 0; j < 2; j++)
    		{
            	this.tubes[j][i].render(var7);
    		}
    	}
    	
    	this.oxygenTanks[0].render(var7);
    	this.oxygenTanks[1].render(var7);
    	
    	this.loadDownloadableImageTexture(var1.skinUrl, var1.getTexture());
    	
    	super.render(var1, var2, var3, var4, var5, var6, var7);
    }

    protected boolean loadDownloadableImageTexture(String par1Str, String par2Str)
    {
        RenderEngine var3 = RenderManager.instance.renderEngine;
        int var4 = var3.getTextureForDownloadableImage(par1Str, par2Str);

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

    public void afterSetRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) 
    {
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
        	this.modelPlayer.bipedLeftArm.rotateAngleX += (float) (Math.PI);
        	this.modelPlayer.bipedLeftArm.rotateAngleZ += (float) (Math.PI) / 10;
        	this.modelPlayer.bipedRightArm.rotateAngleX += (float) (Math.PI);
        	this.modelPlayer.bipedRightArm.rotateAngleZ -= (float) (Math.PI) / 10;
    	}
    	
    	this.oxygenTanks[0].rotateAngleX = this.modelPlayer.bipedBody.rotateAngleX;
    	this.oxygenTanks[0].rotateAngleY = this.modelPlayer.bipedBody.rotateAngleY;
    	this.oxygenTanks[0].rotateAngleZ = this.modelPlayer.bipedBody.rotateAngleZ;
    	this.oxygenTanks[1].rotateAngleX = this.modelPlayer.bipedBody.rotateAngleX;
    	this.oxygenTanks[1].rotateAngleY = this.modelPlayer.bipedBody.rotateAngleY;
    	this.oxygenTanks[1].rotateAngleZ = this.modelPlayer.bipedBody.rotateAngleZ;
    }
}
