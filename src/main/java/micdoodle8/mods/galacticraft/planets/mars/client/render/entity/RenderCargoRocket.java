package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



@SideOnly(Side.CLIENT)
public class RenderCargoRocket extends Render
{
	private static final ResourceLocation cargoRocketTexture = new ResourceLocation(MarsModule.TEXTURE_DOMAIN, "textures/model/cargoRocket.png");

	protected IModelCustom rocketModel;

	public RenderCargoRocket(IModelCustom model)
	{
		this.shadowSize = 0.5F;
		this.rocketModel = model;
	}

	protected ResourceLocation func_110779_a(EntityCargoRocket par1EntityArrow)
	{
		return RenderCargoRocket.cargoRocketTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.func_110779_a((EntityCargoRocket) par1Entity);
	}

	public void renderBuggy(EntityCargoRocket entity, double par2, double par4, double par6, float par8, float par9)
	{
		GL11.glPushMatrix();
		final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
		GL11.glTranslatef((float) par2, (float) par4, (float) par6);
		GL11.glScalef(0.4F, 0.4F, 0.4F);
		GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);

		this.bindTexture(RenderCargoRocket.cargoRocketTexture);
		this.rocketModel.renderAll();

		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderBuggy((EntityCargoRocket) par1Entity, par2, par4, par6, par8, par9);
	}
}
