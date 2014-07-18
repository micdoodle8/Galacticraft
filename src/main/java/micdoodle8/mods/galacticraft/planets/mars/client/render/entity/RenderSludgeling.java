package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelSludgeling;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySludgeling;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderSludgeling extends RenderLiving
{
	private static final ResourceLocation sludgelingTexture = new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/model/sludgeling.png");

	public RenderSludgeling()
	{
		super(new ModelSludgeling(), 0.3F);
	}

	protected ResourceLocation func_110779_a(EntitySludgeling par1EntityArrow)
	{
		return RenderSludgeling.sludgelingTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.func_110779_a((EntitySludgeling) par1Entity);
	}

	public void renderSludgeling(EntitySludgeling sludgeling, double par2, double par4, double par6, float par8, float par9)
	{
		super.doRender(sludgeling, par2, par4, par6, par8, par9);
	}

	@Override
	protected float getDeathMaxRotation(EntityLivingBase par1EntityLiving)
	{
		return 180.0F;
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3)
	{
		return -1;
	}

	@Override
	public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderSludgeling((EntitySludgeling) par1EntityLiving, par2, par4, par6, par8, par9);
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderSludgeling((EntitySludgeling) par1Entity, par2, par4, par6, par8, par9);
	}
}
