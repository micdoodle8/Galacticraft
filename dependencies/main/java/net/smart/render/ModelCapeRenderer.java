// ==================================================================
// This file is part of Smart Render.
//
// Smart Render is free software: you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// Smart Render is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Smart Render. If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

package net.smart.render;

import net.minecraft.client.model.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;

import org.lwjgl.opengl.GL11;

public class ModelCapeRenderer extends ModelSpecialRenderer
{
	public ModelCapeRenderer(ModelBase modelBase, int i, int j, ModelRotationRenderer baseRenderer, ModelRotationRenderer outerRenderer)
	{
		super(modelBase, i, j, baseRenderer);
		outer = outerRenderer;
	}

	public void beforeRender(EntityPlayer entityplayer, float factor)
	{
		this.entityplayer = entityplayer;
		this.setFactor = factor;

		super.beforeRender(true);
	}

	@Override
	public void preTransform(float factor, boolean push)
	{
		super.preTransform(factor, push);

		double d = (entityplayer.field_71091_bM + (entityplayer.field_71094_bP - entityplayer.field_71091_bM) * setFactor) - (entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * setFactor);
		double d1 = (entityplayer.field_71096_bN + (entityplayer.field_71095_bQ - entityplayer.field_71096_bN) * setFactor) - (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * setFactor);
		double d2 = (entityplayer.field_71097_bO + (entityplayer.field_71085_bR - entityplayer.field_71097_bO) * setFactor) - (entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * setFactor);
		float f1 = entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * setFactor;
		double d3 = MathHelper.sin((f1 * 3.141593F) / 180F);
		double d4 = -MathHelper.cos((f1 * 3.141593F) / 180F);
		float f2 = (float)d1 * 10F;
		if(f2 < -6F)
		{
			f2 = -6F;
		}
		if(f2 > 32F)
		{
			f2 = 32F;
		}
		float f3 = (float)(d * d3 + d2 * d4) * 100F;
		float f4 = (float)(d * d4 - d2 * d3) * 100F;
		if(f3 < 0.0F)
		{
			f3 = 0.0F;
		}
		float f5 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * setFactor;
		f2 += MathHelper.sin((entityplayer.prevDistanceWalkedModified + (entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified) * setFactor) * 6F) * 32F * f5;

		float localAngle = 6F + f3 / 2.0F + f2;
		float localAngleMax = Math.max(70.523F - outer.rotateAngleX * RadiantToAngle, 6F);
		float realLocalAngle = Math.min(localAngle, localAngleMax);

		GL11.glRotatef(realLocalAngle, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(f4 / 2.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(-f4 / 2.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
	}

	@Override
	public boolean canBeRandomBoxSource()
	{
		return false;
	}

	private final ModelRotationRenderer outer;
	private EntityPlayer entityplayer;
	private float setFactor;
}