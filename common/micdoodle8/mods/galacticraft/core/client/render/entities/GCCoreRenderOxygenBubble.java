package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelOxygenBubble;
import micdoodle8.mods.galacticraft.core.entities.ISizeable;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreRenderOxygenBubble.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreRenderOxygenBubble extends Render
{
	private static final ResourceLocation oxygenBubbleTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/bubble.png");

	private final GCCoreModelOxygenBubble oxygenBubbleModel = new GCCoreModelOxygenBubble();

	private final float colorRed;
	private final float colorGreen;
	private final float colorBlue;

	public GCCoreRenderOxygenBubble(float red, float green, float blue)
	{
		this.colorRed = red;
		this.colorGreen = green;
		this.colorBlue = blue;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return GCCoreRenderOxygenBubble.oxygenBubbleTexture;
	}

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
	{
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float) d0, (float) d1, (float) d2);

		this.bindEntityTexture(entity);

		GL11.glEnable(GL11.GL_BLEND);

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(this.colorRed, this.colorGreen, this.colorBlue, 1.0F);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDepthMask(false);
		GL11.glScalef(((ISizeable) entity).getSize(), ((ISizeable) entity).getSize(), ((ISizeable) entity).getSize());

		this.oxygenBubbleModel.render(entity, (float) d0, (float) d1, (float) d2, 0, 0, 1.0F);

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
