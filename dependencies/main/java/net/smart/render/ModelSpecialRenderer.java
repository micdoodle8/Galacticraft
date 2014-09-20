package net.smart.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.*;

public class ModelSpecialRenderer extends ModelRotationRenderer
{
	public boolean doPopPush;

	public ModelSpecialRenderer(ModelBase modelBase, int i, int j, ModelRotationRenderer baseRenderer)
	{
		super(modelBase, i, j, baseRenderer);
		ignoreRender = true;
	}

	public void beforeRender(boolean popPush)
	{
		doPopPush = popPush;
		ignoreRender = false;
	}

	public void doRender(float f, boolean useParentTransformations)
	{
		if(doPopPush)
		{
			GL11.glPopMatrix();
			GL11.glPushMatrix();
		}
		super.doRender(f, true);
	}

	public void afterRender()
	{
		ignoreRender = true;
		doPopPush = false;
	}
}
