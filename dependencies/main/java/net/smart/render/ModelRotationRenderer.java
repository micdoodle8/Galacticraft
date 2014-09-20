package net.smart.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import java.lang.reflect.*;
import java.nio.FloatBuffer;

import net.minecraft.client.model.*;

import net.smart.utilities.*;

public class ModelRotationRenderer extends ModelRenderer
{
	protected final static float RadiantToAngle = SmartRenderContext.RadiantToAngle;
	protected final static float Whole = SmartRenderContext.Whole;
	protected final static float Half = SmartRenderContext.Half;

	public ModelRotationRenderer(ModelBase modelBase, int i, int j, ModelRotationRenderer baseRenderer)
	{
		super(modelBase, i, j);
		rotationOrder = XYZ;
		compiled = false;

		base = baseRenderer;
		if(base != null)
			base.addChild(this);

		scaleX = 1.0F;
		scaleY = 1.0F;
		scaleZ = 1.0F;

		fadeEnabled = false;
	}

	public void render(float f)
	{
		if((!ignoreRender && !ignoreBase) || forceRender)
			doRender(f, ignoreBase);
	}

	public void renderIgnoreBase(float f)
	{
		if(ignoreBase)
			doRender(f, false);
	}

	public void doRender(float f, boolean useParentTransformations)
	{
		if(!preRender(f))
			return;
		preTransforms(f, true, useParentTransformations);
		GL11.glCallList(displayList);
		if (childModels != null)
			for (int i = 0; i < childModels.size(); i++)
				((ModelRenderer)childModels.get(i)).render(f);
		postTransforms(f, true, useParentTransformations);
	}

	public boolean preRender(float f)
	{
		if(isHidden)
			return false;

		if(!showModel)
			return false;

		if(!compiled)
			UpdateCompiled();

		if(!compiled)
		{
			Reflect.Invoke(_compileDisplayList, this, f);
			UpdateDisplayList();
			compiled = true;
		}

		return true;
	}

	public void preTransforms(float f, boolean push, boolean useParentTransformations)
	{
		if(base != null && !ignoreBase && useParentTransformations)
			base.preTransforms(f, push, true);
		preTransform(f, push);
	}

	public void preTransform(float f, boolean push)
	{
		if(rotateAngleX != 0.0F || rotateAngleY != 0.0F || rotateAngleZ != 0.0F || ignoreSuperRotation)
		{
			if(push)
				GL11.glPushMatrix();

			GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);

			if (ignoreSuperRotation)
			{
				buffer.rewind();
				GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buffer);
				buffer.get(array);

				GL11.glLoadIdentity();
				GL11.glTranslatef(array[12] / array[15], array[13] / array[15], array[14] / array[15]);
			}

			rotate(rotationOrder, rotateAngleX, rotateAngleY, rotateAngleZ);

			GL11.glScalef(scaleX, scaleY, scaleZ);
			GL11.glTranslatef(offsetX, offsetY, offsetZ);
		}
		else if(rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F || scaleX != 1.0F || scaleY != 1.0F || scaleZ != 1.0F || offsetX != 0.0F || offsetY != 0.0F || offsetZ != 0.0F)
		{
			GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);
			GL11.glScalef(scaleX, scaleY, scaleZ);
			GL11.glTranslatef(offsetX, offsetY, offsetZ);
		}
	}

	private static void rotate(int rotationOrder, float rotateAngleX, float rotateAngleY, float rotateAngleZ)
	{
		if((rotationOrder == ZXY) && rotateAngleY != 0.0F)
			GL11.glRotatef(rotateAngleY * RadiantToAngle, 0.0F, 1.0F, 0.0F);

		if((rotationOrder == YXZ) && rotateAngleZ != 0.0F)
			GL11.glRotatef(rotateAngleZ * RadiantToAngle, 0.0F, 0.0F, 1.0F);

		if((rotationOrder == YZX || rotationOrder == YXZ || rotationOrder == ZXY || rotationOrder == ZYX) && rotateAngleX != 0.0F)
			GL11.glRotatef(rotateAngleX * RadiantToAngle, 1.0F, 0.0F, 0.0F);

		if((rotationOrder == XZY || rotationOrder == ZYX) && rotateAngleY != 0.0F)
			GL11.glRotatef(rotateAngleY * RadiantToAngle, 0.0F, 1.0F, 0.0F);

		if((rotationOrder == XYZ || rotationOrder == XZY || rotationOrder == YZX || rotationOrder == ZXY || rotationOrder == ZYX ) && rotateAngleZ != 0.0F)
			GL11.glRotatef(rotateAngleZ * RadiantToAngle, 0.0F, 0.0F, 1.0F);

		if((rotationOrder == XYZ || rotationOrder == YXZ || rotationOrder == YZX) && rotateAngleY != 0.0F)
			GL11.glRotatef(rotateAngleY * RadiantToAngle, 0.0F, 1.0F, 0.0F);

		if((rotationOrder == XYZ || rotationOrder == XZY) && rotateAngleX != 0.0F)
			GL11.glRotatef(rotateAngleX * RadiantToAngle, 1.0F, 0.0F, 0.0F);
	}

	public void postTransform(float f, boolean pop)
	{
		if(rotateAngleX != 0.0F || rotateAngleY != 0.0F || rotateAngleZ != 0.0F || ignoreSuperRotation)
		{
			if(pop)
				GL11.glPopMatrix();
		}
		else if(rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F || scaleX != 1.0F || scaleY != 1.0F || scaleZ != 1.0F || offsetX != 0.0F || offsetY != 0.0F || offsetZ != 0.0F)
		{
			GL11.glTranslatef(-offsetX, -offsetY, -offsetZ);
			GL11.glScalef(1F / scaleX, 1F / scaleY, 1F / scaleZ);
			GL11.glTranslatef(-rotationPointX * f, -rotationPointY * f, -rotationPointZ * f);
		}
	}

	public void postTransforms(float f, boolean pop, boolean useParentTransformations)
	{
		postTransform(f, pop);
		if(base != null && !ignoreBase && useParentTransformations)
			base.postTransforms(f, pop, true);
	}

	public void reset()
	{
		rotationOrder = XYZ;

		scaleX = 1.0F;
		scaleY = 1.0F;
		scaleZ = 1.0F;

		rotationPointX = 0F;
		rotationPointY = 0F;
		rotationPointZ = 0F;

		rotateAngleX = 0F;
		rotateAngleY = 0F;
		rotateAngleZ = 0F;

		ignoreBase = false;
		ignoreSuperRotation = false;
		forceRender = false;

		offsetX = 0;
		offsetY = 0;
		offsetZ = 0;

		fadeOffsetX = false;
		fadeOffsetY = false;
		fadeOffsetZ = false;
		fadeRotateAngleX = false;
		fadeRotateAngleY = false;
		fadeRotateAngleZ = false;
		fadeRotationPointX = false;
		fadeRotationPointY = false;
		fadeRotationPointZ = false;

		previous = null;
	}

	public void renderWithRotation(float f)
	{
		boolean update = !compiled;
		super.renderWithRotation(f);
		if(update)
			UpdateLocals();
	}

	public void postRender(float f)
	{
		boolean update = !compiled;
		if(!preRender(f))
			return;
		if(update)
			UpdateLocals();
		preTransforms(f, false, true);
	}

	private void UpdateLocals()
	{
		UpdateCompiled();
		if(compiled)
			UpdateDisplayList();
	}

	private void UpdateCompiled()
	{
		compiled = (Boolean)Reflect.GetField(_compiled, this);
	}

	private void UpdateDisplayList()
	{
		displayList = (Integer)Reflect.GetField(_displayList, this);
	}

	private static Field _compiled = Reflect.GetField(ModelRenderer.class, SmartRenderInstall.ModelRenderer_compiled);
	private static Method _compileDisplayList = Reflect.GetMethod(ModelRenderer.class, SmartRenderInstall.ModelRenderer_compileDisplayList, float.class);
	private static Field _displayList = Reflect.GetField(ModelRenderer.class, SmartRenderInstall.ModelRenderer_displayList);

	protected ModelRotationRenderer base;

	public boolean ignoreRender;
	public boolean forceRender;

	public boolean compiled;
	public int displayList;
	public int rotationOrder;

	public float scaleX;
	public float scaleY;
	public float scaleZ;

	public boolean ignoreBase;
	public boolean ignoreSuperRotation;

	public float offsetX;
	public float offsetY;
	public float offsetZ;

	public static int XYZ = 0;
	public static int XZY = 1;
	public static int YXZ = 2;
	public static int YZX = 3;
	public static int ZXY = 4;
	public static int ZYX = 5;

	public boolean fadeEnabled;

	public boolean fadeOffsetX;
	public boolean fadeOffsetY;
	public boolean fadeOffsetZ;
	public boolean fadeRotateAngleX;
	public boolean fadeRotateAngleY;
	public boolean fadeRotateAngleZ;
	public boolean fadeRotationPointX;
	public boolean fadeRotationPointY;
	public boolean fadeRotationPointZ;

	public RendererData previous;

	public void fadeStore(float totalTime)
	{
		if(previous != null)
		{
			previous.offsetX = offsetX;
			previous.offsetY = offsetY;
			previous.offsetZ = offsetZ;
			previous.rotateAngleX = rotateAngleX;
			previous.rotateAngleY = rotateAngleY;
			previous.rotateAngleZ = rotateAngleZ;
			previous.rotationPointX = rotationPointX;
			previous.rotationPointY = rotationPointY;
			previous.rotationPointZ = rotationPointZ;
			previous.totalTime = totalTime;
		}
	}

	public void fadeIntermediate(float totalTime)
	{
		if(previous != null && totalTime - previous.totalTime <= 2F)
		{
			offsetX = GetIntermediatePosition(previous.offsetX, offsetX, fadeOffsetX, previous.totalTime, totalTime);
			offsetY = GetIntermediatePosition(previous.offsetY, offsetY, fadeOffsetY, previous.totalTime, totalTime);
			offsetZ = GetIntermediatePosition(previous.offsetZ, offsetZ, fadeOffsetZ, previous.totalTime, totalTime);

			rotateAngleX = GetIntermediateAngle(previous.rotateAngleX, rotateAngleX, fadeRotateAngleX, previous.totalTime, totalTime);
			rotateAngleY = GetIntermediateAngle(previous.rotateAngleY, rotateAngleY, fadeRotateAngleY, previous.totalTime, totalTime);
			rotateAngleZ = GetIntermediateAngle(previous.rotateAngleZ, rotateAngleZ, fadeRotateAngleZ, previous.totalTime, totalTime);

			rotationPointX = GetIntermediatePosition(previous.rotationPointX, rotationPointX, fadeRotationPointX, previous.totalTime, totalTime);
			rotationPointY = GetIntermediatePosition(previous.rotationPointY, rotationPointY, fadeRotationPointY, previous.totalTime, totalTime);
			rotationPointZ = GetIntermediatePosition(previous.rotationPointZ, rotationPointZ, fadeRotationPointZ, previous.totalTime, totalTime);
		}
	}

	public boolean canBeRandomBoxSource()
	{
		return true;
	}

	private float GetIntermediatePosition(float prevPosition, float shouldPosition, boolean fade, float lastTotalTime, float totalTime)
	{
		if(!fade || shouldPosition == prevPosition)
			return shouldPosition;

		return prevPosition + (shouldPosition - prevPosition) * (totalTime - lastTotalTime) * 0.2F;
	}

	private float GetIntermediateAngle(float prevAngle, float shouldAngle, boolean fade, float lastTotalTime, float totalTime)
	{
		if(!fade || shouldAngle == prevAngle)
			return shouldAngle;

		while(prevAngle >= Whole) prevAngle -= Whole;
		while(prevAngle < 0F) prevAngle += Whole;

		while(shouldAngle >= Whole) shouldAngle -= Whole;
		while(shouldAngle < 0F) shouldAngle += Whole;

		if(shouldAngle > prevAngle && (shouldAngle - prevAngle) > Half)
			prevAngle += Whole;

		if(shouldAngle < prevAngle && (prevAngle - shouldAngle) > Half)
			shouldAngle += Whole;

		return prevAngle + (shouldAngle - prevAngle) * (totalTime - lastTotalTime) * 0.2F;
	}

	private static FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
	private static float[] array = new float[16];
}
