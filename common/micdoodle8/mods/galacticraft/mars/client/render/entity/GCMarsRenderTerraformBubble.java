package micdoodle8.mods.galacticraft.mars.client.render.entity;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelOxygenBubble;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityTerraformBubble;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsRenderTerraformBubble.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCMarsRenderTerraformBubble extends Render
{
	private static final ResourceLocation bubbleTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/bubble.png");

	private final GCCoreModelOxygenBubble bubbleModel = new GCCoreModelOxygenBubble();

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return GCMarsRenderTerraformBubble.bubbleTexture;
	}

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
	{
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float) d0, (float) d1, (float) d2);

		this.bindEntityTexture(entity);

		GL11.glEnable(GL11.GL_BLEND);

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0.1F, 1, 0.1F, 1.0F);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDepthMask(false);
		GL11.glScaled(((GCMarsEntityTerraformBubble) entity).getSize(), ((GCMarsEntityTerraformBubble) entity).getSize(), ((GCMarsEntityTerraformBubble) entity).getSize());

		this.bubbleModel.render(entity, (float) d0, (float) d1, (float) d2, 0, 0, 1.0F);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glDepthMask(true);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthFunc(GL11.GL_LEQUAL);

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
