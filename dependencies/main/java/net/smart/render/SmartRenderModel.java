package net.smart.render;

import java.util.*;

import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.*;

public class SmartRenderModel extends SmartRenderContext
{
	public IModelPlayer imp;
	public ModelBiped mp;

	public SmartRenderModel(float f, ModelBiped mp, IModelPlayer imp, ModelRenderer originalBipedBody, ModelRenderer originalBipedCloak, ModelRenderer originalBipedHead, ModelRenderer originalBipedEars, ModelRenderer originalBipedHeadwear, ModelRenderer originalBipedRightArm, ModelRenderer originalBipedLeftArm, ModelRenderer originalBipedRightLeg, ModelRenderer originalBipedLeftLeg)
	{
		this.imp = imp;
		this.mp = mp;

		mp.boxList.clear();

		bipedOuter = create(-1, -1, null);
		bipedOuter.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedOuter.fadeEnabled = true;

		bipedTorso = create(16, 16, bipedOuter);
		bipedTorso.setRotationPoint(0.0F, 0.0F, 0.0F);

		bipedBody = create(16, 16, bipedTorso, originalBipedBody);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

		bipedBreast = create(-1, -1, bipedTorso);
		bipedBreast.setRotationPoint(0.0F, 0.0F, 0.0F);

		bipedNeck = create(-1, -1, bipedBreast);
		bipedNeck.setRotationPoint(0.0F, 0.0F, 0.0F);

		bipedCloak = new ModelCapeRenderer(mp, 0, 0, bipedBreast, bipedOuter);
		copy(bipedCloak, originalBipedCloak);
		bipedCloak.setRotationPoint(0.0F, 0.0F, 2.0F);

		bipedHead = create(0, 0, bipedNeck, originalBipedHead);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);

		bipedEars = new ModelEarsRenderer(mp, 24, 0, bipedHead);
		copy(bipedCloak, originalBipedEars);
		bipedEars.setRotationPoint(0.0F, 0.0F, 0.0F);

		bipedHeadwear = create(32, 0, bipedHead, originalBipedHeadwear);
		bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);

		bipedRightShoulder = create(40, 16, bipedBreast);
		bipedRightShoulder.setRotationPoint(-5F, 2.0F, 0.0F);

		bipedRightArm = create(40, 16, bipedRightShoulder, originalBipedRightArm);

		bipedLeftShoulder = create(-1, -1, bipedBreast);
		bipedLeftShoulder.mirror = true;
		bipedLeftShoulder.setRotationPoint(5F, 2.0F, 0.0F);

		bipedLeftArm = create(40, 16, bipedLeftShoulder, originalBipedLeftArm);

		bipedPelvic = create(-1, -1, bipedTorso);
		bipedPelvic.setRotationPoint(0.0F, 12.0F, 0.0F);

		bipedRightLeg = create(0, 16, bipedPelvic, originalBipedRightLeg);
		bipedRightLeg.setRotationPoint(-2F, 0.0F, 0.0F);

		bipedLeftLeg = create(0, 16, bipedPelvic, originalBipedLeftLeg);
		bipedLeftLeg.setRotationPoint(2.0F, 0.0F, 0.0F);

		imp.initialize(bipedBody, bipedCloak, bipedHead, bipedEars, bipedHeadwear, bipedRightArm, bipedLeftArm, bipedRightLeg, bipedLeftLeg);
	}

	private ModelRotationRenderer create(int i, int j, ModelRotationRenderer base)
	{
		return new ModelRotationRenderer(mp, i, j, base);
	}

	private ModelRotationRenderer create(int i, int j, ModelRotationRenderer base, ModelRenderer original)
	{
		ModelRotationRenderer local = create(i, j, base);
		copy(local, original);
		return local;
	}

	private void copy(ModelRotationRenderer local, ModelRenderer original)
	{
		if(original.childModels != null)
			for(Object childModel : original.childModels)
				local.addChild((ModelRenderer)childModel);
		if(original.cubeList != null)
			for(Object cube : original.cubeList)
				local.cubeList.add(cube);
		local.mirror = original.mirror;
	}

	public void render(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		bipedBody.ignoreRender = bipedHead.ignoreRender = bipedHeadwear.ignoreRender = bipedRightArm.ignoreRender = bipedLeftArm.ignoreRender = bipedRightLeg.ignoreRender = bipedLeftLeg.ignoreRender = true;
		imp.superRender(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
		bipedBody.ignoreRender = bipedHead.ignoreRender = bipedHeadwear.ignoreRender = bipedRightArm.ignoreRender = bipedLeftArm.ignoreRender = bipedRightLeg.ignoreRender = bipedLeftLeg.ignoreRender = false;

		bipedOuter.render(factor);

		bipedOuter.renderIgnoreBase(factor);
		bipedTorso.renderIgnoreBase(factor);
		bipedBody.renderIgnoreBase(factor);
		bipedBreast.renderIgnoreBase(factor);
		bipedNeck.renderIgnoreBase(factor);
		bipedHead.renderIgnoreBase(factor);
		bipedHeadwear.renderIgnoreBase(factor);
		bipedRightShoulder.renderIgnoreBase(factor);
		bipedRightArm.renderIgnoreBase(factor);
		bipedLeftShoulder.renderIgnoreBase(factor);
		bipedLeftArm.renderIgnoreBase(factor);
		bipedPelvic.renderIgnoreBase(factor);
		bipedRightLeg.renderIgnoreBase(factor);
		bipedLeftLeg.renderIgnoreBase(factor);
	}

	public void setRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor, Entity entity)
	{
		reset();

		if(firstPerson || isInventory)
		{
			bipedBody.ignoreBase = true;
			bipedHead.ignoreBase = true;
			bipedHeadwear.ignoreBase = true;
			bipedEars.ignoreBase = true;
			bipedCloak.ignoreBase = true;
			bipedRightArm.ignoreBase = true;
			bipedLeftArm.ignoreBase = true;
			bipedRightLeg.ignoreBase = true;
			bipedLeftLeg.ignoreBase = true;

			bipedBody.forceRender = firstPerson;
			bipedHead.forceRender = firstPerson;
			bipedHeadwear.forceRender = firstPerson;
			bipedEars.forceRender = firstPerson;
			bipedCloak.forceRender = firstPerson;
			bipedRightArm.forceRender = firstPerson;
			bipedLeftArm.forceRender = firstPerson;
			bipedRightLeg.forceRender = firstPerson;
			bipedLeftLeg.forceRender = firstPerson;

			bipedRightArm.setRotationPoint(-5F, 2.0F, 0.0F);
			bipedLeftArm.setRotationPoint(5F, 2.0F, 0.0F);
			bipedRightLeg.setRotationPoint(-2F, 12F, 0.0F);
			bipedLeftLeg.setRotationPoint(2.0F, 12F, 0.0F);

			imp.superSetRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor, entity);
	 		return;
		}

		if(isSleeping)
		{
			prevOuterRenderData.rotateAngleX = 0;
			prevOuterRenderData.rotateAngleY = 0;
			prevOuterRenderData.rotateAngleZ = 0;
		}

		bipedOuter.previous = prevOuterRenderData;

		bipedOuter.rotateAngleY = actualRotation / RadiantToAngle;
		bipedOuter.fadeRotateAngleY = !(entity.ridingEntity instanceof EntityPig);

		imp.animateHeadRotation(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);

		if(isSleeping)
			imp.animateSleeping(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);

		imp.animateArmSwinging(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);

		if(mp.isRiding)
			imp.animateRiding(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);

		if(mp.heldItemLeft != 0)
			imp.animateLeftArmItemHolding(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);

		if(mp.heldItemRight != 0)
			imp.animateRightArmItemHolding(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);

		if(mp.onGround > -9990F)
		{
			imp.animateWorkingBody(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
			imp.animateWorkingArms(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
		}

		if(mp.isSneak)
			imp.animateSneaking(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);

		imp.animateArms(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);

		if(mp.aimedBow)
			imp.animateBowAiming(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);

		if(bipedOuter.previous != null && !bipedOuter.fadeRotateAngleX)
			bipedOuter.previous.rotateAngleX = bipedOuter.rotateAngleX;

		if(bipedOuter.previous != null && !bipedOuter.fadeRotateAngleY)
			bipedOuter.previous.rotateAngleY = bipedOuter.rotateAngleY;

		bipedOuter.fadeIntermediate(totalTime);
		bipedOuter.fadeStore(totalTime);

		bipedCloak.ignoreBase = false;
		bipedCloak.rotateAngleX = Sixtyfourth;
	}

	public void animateHeadRotation(float viewHorizontalAngelOffset, float viewVerticalAngelOffset)
	{
		bipedNeck.ignoreBase = true;
		bipedHead.rotateAngleY = (actualRotation + viewHorizontalAngelOffset) / RadiantToAngle;
		bipedHead.rotateAngleX = viewVerticalAngelOffset / RadiantToAngle;
	}

	public void animateSleeping()
	{
		bipedNeck.ignoreBase = false;
		bipedHead.rotateAngleY = 0F;
		bipedHead.rotateAngleX = Eighth;
		bipedTorso.rotationPointZ = -17F;
	}

	public void animateArmSwinging(float totalHorizontalDistance, float currentHorizontalSpeed)
	{
		bipedRightArm.rotateAngleX = MathHelper.cos(totalHorizontalDistance * 0.6662F + Half) * 2.0F * currentHorizontalSpeed * 0.5F;
		bipedLeftArm.rotateAngleX = MathHelper.cos(totalHorizontalDistance * 0.6662F) * 2.0F * currentHorizontalSpeed * 0.5F;

		bipedRightLeg.rotateAngleX = MathHelper.cos(totalHorizontalDistance * 0.6662F) * 1.4F * currentHorizontalSpeed;
		bipedLeftLeg.rotateAngleX = MathHelper.cos(totalHorizontalDistance * 0.6662F + Half) * 1.4F * currentHorizontalSpeed;
	}

	public void animateRiding()
	{
		bipedRightArm.rotateAngleX += -0.6283185F;
		bipedLeftArm.rotateAngleX += -0.6283185F;
		bipedRightLeg.rotateAngleX = -1.256637F;
		bipedLeftLeg.rotateAngleX = -1.256637F;
		bipedRightLeg.rotateAngleY = 0.3141593F;
		bipedLeftLeg.rotateAngleY = -0.3141593F;
	}

	public void animateLeftArmItemHolding()
	{
		bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F - 0.3141593F * mp.heldItemLeft;
	}

	public void animateRightArmItemHolding()
	{
		bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F - 0.3141593F * mp.heldItemRight;
	}

	public void animateWorkingBody()
	{
		float angle = MathHelper.sin(MathHelper.sqrt_float(mp.onGround) * Whole) * 0.2F;
		bipedBreast.rotateAngleY = bipedBody.rotateAngleY += angle;
		bipedBreast.rotationOrder = bipedBody.rotationOrder = ModelRotationRenderer.YXZ;
		bipedLeftArm.rotateAngleX += angle;
	}

	public void animateWorkingArms()
	{
		float f6 = 1.0F - mp.onGround;
		f6 = 1.0F - f6 * f6 * f6;
		float f7 = MathHelper.sin(f6 * Half);
		float f8 = MathHelper.sin(mp.onGround * Half) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
		bipedRightArm.rotateAngleX -= f7 * 1.2D + f8;
		bipedRightArm.rotateAngleY += MathHelper.sin(MathHelper.sqrt_float(mp.onGround) * Whole) * 0.4F;
		bipedRightArm.rotateAngleZ -= MathHelper.sin(mp.onGround * Half) * 0.4F;
	}

	public void animateSneaking()
	{
		bipedTorso.rotateAngleX += 0.5F;
		bipedRightLeg.rotateAngleX += -0.5F;
		bipedLeftLeg.rotateAngleX += -0.5F;
		bipedRightArm.rotateAngleX += -0.1F;
		bipedLeftArm.rotateAngleX += -0.1F;

		bipedPelvic.offsetY = -0.137F;
		bipedPelvic.offsetZ = -0.051F;

		bipedBreast.offsetY = -0.014F;
		bipedBreast.offsetZ = -0.057F;

		bipedNeck.offsetY = 0.0621F;
	}

	public void animateArms(float totalTime)
	{
		bipedRightArm.rotateAngleZ += MathHelper.cos(totalTime * 0.09F) * 0.05F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(totalTime * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(totalTime * 0.067F) * 0.05F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(totalTime * 0.067F) * 0.05F;
	}

	public void animateBowAiming(float totalTime)
	{
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.0F;
		bipedRightArm.rotateAngleY = -0.1F + bipedHead.rotateAngleY - bipedOuter.rotateAngleY;
		bipedLeftArm.rotateAngleY = 0.1F + bipedHead.rotateAngleY + 0.4F - bipedOuter.rotateAngleY;
		bipedRightArm.rotateAngleX = -1.570796F + bipedHead.rotateAngleX;
		bipedLeftArm.rotateAngleX = -1.570796F + bipedHead.rotateAngleX;
		bipedRightArm.rotateAngleZ += MathHelper.cos(totalTime * 0.09F) * 0.05F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(totalTime * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(totalTime * 0.067F) * 0.05F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(totalTime * 0.067F) * 0.05F;
	}

	public void reset()
	{
		bipedOuter.reset();
		bipedTorso.reset();
		bipedBody.reset();
		bipedBreast.reset();
		bipedNeck.reset();
		bipedHead.reset();
		bipedHeadwear.reset();
		bipedEars.reset();
		bipedCloak.reset();
		bipedRightShoulder.reset();
		bipedRightArm.reset();
		bipedLeftShoulder.reset();
		bipedLeftArm.reset();
		bipedPelvic.reset();
		bipedRightLeg.reset();
		bipedLeftLeg.reset();

		bipedRightShoulder.setRotationPoint(-5F, 2.0F, 0.0F);
		bipedLeftShoulder.setRotationPoint(5F, 2.0F, 0.0F);
		bipedPelvic.setRotationPoint(0.0F, 12.0F, 0.0F);
		bipedRightLeg.setRotationPoint(-2F, 0.0F, 0.0F);
		bipedLeftLeg.setRotationPoint(2.0F, 0.0F, 0.0F);
		bipedCloak.setRotationPoint(0.0F, 0.0F, 2.0F);
	}

	public void renderCloak(float f)
	{
		attemptToCallRenderCape = true;
		if(!disabled)
			imp.superRenderCloak(f);
	}

	public ModelRenderer getRandomBox(Random par1Random)
    {
		List boxList = mp.boxList;
		int size = boxList.size();
		int renderersWithBoxes = 0;

		for(int i=0; i<size; i++)
		{
			ModelRenderer renderer = (ModelRenderer)boxList.get(i);
			if(canBeRandomBoxSource(renderer))
				renderersWithBoxes++;
		}

		if(renderersWithBoxes != 0)
		{
			int random = par1Random.nextInt(renderersWithBoxes);
			renderersWithBoxes = -1;
	
			for(int i=0; i<size; i++)
			{
				ModelRenderer renderer = (ModelRenderer)boxList.get(i);
				if(canBeRandomBoxSource(renderer))
					renderersWithBoxes++;
				if(renderersWithBoxes == random)
					return renderer;
			}
		}

		return null;
	}

	private static boolean canBeRandomBoxSource(ModelRenderer renderer)
	{
		return renderer.cubeList != null && renderer.cubeList.size() > 0 && (!(renderer instanceof ModelRotationRenderer) || ((ModelRotationRenderer)renderer).canBeRandomBoxSource());
	}

	public boolean isInventory;

	public int scaleArmType;
	public int scaleLegType;

	public float totalVerticalDistance;
	public float currentVerticalSpeed;
	public float totalDistance;
	public float currentSpeed;

	public double distance;
	public double verticalDistance;
	public double horizontalDistance;
	public float currentCameraAngle;
	public float currentVerticalAngle;
	public float currentHorizontalAngle;

	public float actualRotation;
	public float forwardRotation;
	public float workingAngle;

	public ModelRotationRenderer bipedOuter;
	public ModelRotationRenderer bipedTorso;
	public ModelRotationRenderer bipedBody;
	public ModelRotationRenderer bipedBreast;
	public ModelRotationRenderer bipedNeck;
	public ModelRotationRenderer bipedHead;
	public ModelRotationRenderer bipedHeadwear;
	public ModelRotationRenderer bipedRightShoulder;
	public ModelRotationRenderer bipedRightArm;
	public ModelRotationRenderer bipedLeftShoulder;
	public ModelRotationRenderer bipedLeftArm;
	public ModelRotationRenderer bipedPelvic;
	public ModelRotationRenderer bipedRightLeg;
	public ModelRotationRenderer bipedLeftLeg;
	public ModelEarsRenderer bipedEars;
	public ModelCapeRenderer bipedCloak;


	public boolean disabled;
	public boolean attemptToCallRenderCape;
	public RendererData prevOuterRenderData;
	public boolean isSleeping;
	public boolean firstPerson;
}