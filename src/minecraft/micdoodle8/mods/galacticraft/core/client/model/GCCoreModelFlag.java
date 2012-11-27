package micdoodle8.mods.galacticraft.core.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.src.Entity;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.StringUtils;

public class GCCoreModelFlag extends ModelBase
{
	ModelRenderer base;
	ModelRenderer pole;
	ModelRenderer flag;
	ModelRenderer picSide1;
	ModelRenderer picSide2;

	public GCCoreModelFlag()
	{
		textureWidth = 128;
		textureHeight = 64;
		base = new ModelRenderer(this, 4, 0);
		base.addBox(-1.5F, 0F, -1.5F, 3, 1, 3);
		base.setRotationPoint(0F, 23F, 0F);
		base.setTextureSize(128, 64);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		pole = new ModelRenderer(this, 0, 0);
		pole.addBox(-0.5F, -40F, -0.5F, 1, 40, 1);
		pole.setRotationPoint(0F, 23F, 0F);
		pole.setTextureSize(128, 64);
		pole.mirror = true;
		setRotation(pole, 0F, 0F, 0F);
		flag = new ModelRenderer(this, 88, 0);
		flag.addBox(0F, 0F, 0F, 20, 12, 0);
		flag.setRotationPoint(0.5F, -16F, 0F);
		flag.setTextureSize(128, 64);
		flag.mirror = true;
		setRotation(flag, 0F, 0F, 0F);
		picSide1 = new ModelRenderer(this, 24, 0);
		picSide1.addBox(0F, 0F, 0F, 32, 32, 0);
		picSide1.setRotationPoint(0.5F, -16F, 0.1F);
		picSide1.setTextureSize(128, 64);
		picSide1.mirror = true;
		setRotation(picSide1, 0F, 0F, 0F);
		picSide2 = new ModelRenderer(this, 24, 0);
		picSide2.addBox(0F, 0F, 0F, 32, 32, 0);
		picSide2.setRotationPoint(0.5F, -16F, -0.1F);
		picSide2.setTextureSize(128, 64);
		picSide2.mirror = true;
		setRotation(picSide2, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(entity, f, f1, f2, f3, f4, f5);
		base.render(f5);
		pole.render(f5);
		flag.render(f5);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		this.loadDownloadableImageTexture("https://minotar.net/helm/" + StringUtils.stripControlCodes(FMLClientHandler.instance().getClient().thePlayer.username) + "/32.png", FMLClientHandler.instance().getClient().thePlayer.getTexture());
		picSide1.render(f5);
		picSide2.render(f5);
	}
	
    protected boolean loadDownloadableImageTexture(String par1Str, String par2Str)
    {
        RenderEngine var3 = FMLClientHandler.instance().getClient().renderEngine;
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

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}
