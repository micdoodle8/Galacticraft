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

	public void doRender(float f, boolean useParentTransformations)
	{
		reset();
		super.doRender(f, useParentTransformations);
	}

	public void preTransform(float factor, boolean push)
	{
		super.preTransform(factor, push);

		int i = _i++ % 2;
		GL11.glTranslatef(0.375F * (i * 2 - 1), 0.0F, 0.0F);
		GL11.glTranslatef(0.0F, -0.375F, 0.0F);
		GL11.glScalef(1.333333F, 1.333333F, 1.333333F);
	}

	public boolean canBeRandomBoxSource()
	{
		return false;
	}
}
