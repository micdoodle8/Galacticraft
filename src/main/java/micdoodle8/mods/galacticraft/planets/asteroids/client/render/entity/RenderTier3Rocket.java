package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTier3Rocket extends Render
{
	private ResourceLocation rocketTexture;

	protected IModelCustom rocketModelObj;

	public RenderTier3Rocket(IModelCustom spaceshipModel, String textureDomain, String texture)
	{
		this.rocketModelObj = spaceshipModel;
        this.rocketTexture = new ResourceLocation(textureDomain, "textures/model/" + texture + ".png");
        this.shadowSize = 2F;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
        return this.rocketTexture;
	}

	public void renderSpaceship(EntitySpaceshipBase entity, double par2, double par4, double par6, float par8, float par9)
	{
		GL11.glPushMatrix();
		final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9 + 180;
		final float var25 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9 + 45;

		GL11.glTranslatef((float) par2, (float) par4 - 0.4F, (float) par6);
		GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
		final float var28 = entity.rollAmplitude - par9;
		float var30 = entity.shipDamage - par9;

		if (var30 < 0.0F)
		{
			var30 = 0.0F;
		}

		if (var28 > 0.0F)
		{
			final float i = entity.getLaunched() ? (5 - MathHelper.floor_double(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
			GL11.glRotatef(MathHelper.sin(var28) * var28 * i * par9, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(MathHelper.sin(var28) * var28 * i * par9, 1.0F, 0.0F, 1.0F);
		}

		this.bindEntityTexture(entity);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		GL11.glScalef(0.9F, 0.9F, 0.9F);

        this.rocketModelObj.renderOnly("Boosters", "Rocket");
        Vector3 teamColor = ClientUtil.updateTeamColor(FMLClientHandler.instance().getClient().thePlayer.getCommandSenderName(), true);
        GL11.glColor3f(teamColor.floatX(), teamColor.floatY(), teamColor.floatZ());
        this.rocketModelObj.renderPart("NoseCone");

        if (FMLClientHandler.instance().getClient().thePlayer.ticksExisted / 10 % 2 < 1)
        {
            GL11.glColor3f(1, 0, 0);
        }
        else
        {
            GL11.glColor3f(0, 1, 0);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.rocketModelObj.renderPart("Cube");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glColor3f(1, 1, 1);

		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderSpaceship((EntitySpaceshipBase) par1Entity, par2, par4, par6, par8, par9);
	}
}
