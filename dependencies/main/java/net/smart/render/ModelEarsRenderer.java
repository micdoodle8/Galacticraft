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

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.*;

public class ModelEarsRenderer extends ModelSpecialRenderer
{
	private int _i = 0;

	public ModelEarsRenderer(ModelBase modelBase, int i, int j, ModelRotationRenderer baseRenderer)
	{
		super(modelBase, i, j, baseRenderer);
	}

	public void beforeRender()
	{
		super.beforeRender(true);
	}

	@Override
	public void doRender(float f, boolean useParentTransformations)
	{
		reset();
		super.doRender(f, useParentTransformations);
	}

	@Override
	public void preTransform(float factor, boolean push)
	{
		super.preTransform(factor, push);

		int i = _i++ % 2;
		GL11.glTranslatef(0.375F * (i * 2 - 1), 0.0F, 0.0F);
		GL11.glTranslatef(0.0F, -0.375F, 0.0F);
		GL11.glScalef(1.333333F, 1.333333F, 1.333333F);
	}

	@Override
	public boolean canBeRandomBoxSource()
	{
		return false;
	}
}