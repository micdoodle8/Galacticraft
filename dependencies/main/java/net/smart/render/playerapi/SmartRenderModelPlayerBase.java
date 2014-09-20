package net.smart.render.playerapi;

import java.util.*;

import net.minecraft.client.model.*;
import net.minecraft.entity.*;

import api.player.model.*;

import net.smart.render.*;
import net.smart.render.IModelPlayer;

public class SmartRenderModelPlayerBase extends ModelPlayerBase implements IModelPlayer
{
	private SmartRenderModel model;

	public SmartRenderModelPlayerBase(ModelPlayerAPI modelplayerapi)
	{
		super(modelplayerapi);
	}

	public SmartRenderModel getRenderModel()
	{
		if(model == null)
			model = new SmartRenderModel(modelPlayerAPI.getExpandParameter(), modelPlayer, this, modelPlayer.bipedBody, modelPlayer.bipedCloak, modelPlayer.bipedHead, modelPlayer.bipedEars, modelPlayer.bipedHeadwear, modelPlayer.bipedRightArm, modelPlayer.bipedLeftArm, modelPlayer.bipedRightLeg, modelPlayer.bipedLeftLeg);
		return model;
	}

	public void render(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().render(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
	}

	public void superRender(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		super.render(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
	}

	public void initialize(ModelRenderer bipedBody, ModelRenderer bipedCloak, ModelRenderer bipedHead, ModelRenderer bipedEars, ModelRenderer bipedHeadwear, ModelRenderer bipedRightArm, ModelRenderer bipedLeftArm, ModelRenderer bipedRightLeg, ModelRenderer bipedLeftLeg)
	{
		modelPlayer.bipedBody = bipedBody;
		modelPlayer.bipedCloak = bipedCloak;
		modelPlayer.bipedHead = bipedHead;
		modelPlayer.bipedEars = bipedEars;
		modelPlayer.bipedHeadwear = bipedHeadwear;
		modelPlayer.bipedRightArm = bipedRightArm;
		modelPlayer.bipedLeftArm = bipedLeftArm;
		modelPlayer.bipedRightLeg = bipedRightLeg;
		modelPlayer.bipedLeftLeg = bipedLeftLeg;
	}

	public void setRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor, Entity entity)
	{
		getRenderModel().setRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor, entity);
	}

	public void superSetRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor, Entity entity)
	{
		super.setRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor, entity);
	}

	public void renderCloak(float f)
	{
		getRenderModel().renderCloak(f);
	}

	public void superRenderCloak(float factor)
	{
		super.renderCloak(factor);
	}

	public ModelRenderer getRandomModelBox(Random random)
    {
		return getRenderModel().getRandomBox(random);
	}

	public ModelRenderer getOuter() { return getRenderModel().bipedOuter; }
	public ModelRenderer getTorso() { return getRenderModel().bipedTorso; }
	public ModelRenderer getBody() { return getRenderModel().bipedBody; }
	public ModelRenderer getBreast() { return getRenderModel().bipedBreast; }
	public ModelRenderer getNeck() { return getRenderModel().bipedNeck; }
	public ModelRenderer getHead() { return getRenderModel().bipedHead; }
	public ModelRenderer getHeadwear() { return getRenderModel().bipedHeadwear; }
	public ModelRenderer getRightShoulder() { return getRenderModel().bipedRightShoulder; }
	public ModelRenderer getRightArm() { return getRenderModel().bipedRightArm; }
	public ModelRenderer getLeftShoulder() { return getRenderModel().bipedLeftShoulder; }
	public ModelRenderer getLeftArm() { return getRenderModel().bipedLeftArm; }
	public ModelRenderer getPelvic() { return getRenderModel().bipedPelvic; }
	public ModelRenderer getRightLeg() { return getRenderModel().bipedRightLeg; }
	public ModelRenderer getLeftLeg() { return getRenderModel().bipedLeftLeg; }
	public ModelRenderer getEars() { return getRenderModel().bipedEars; }
	public ModelRenderer getCloak() { return getRenderModel().bipedCloak; }

	public void animateHeadRotation(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateHeadRotation", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateHeadRotation(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateHeadRotation(viewHorizontalAngelOffset, viewVerticalAngelOffset);
	}

	public void animateSleeping(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateSleeping", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateSleeping(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateSleeping();
	}

	public void animateArmSwinging(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateArmSwinging", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateArmSwinging(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateArmSwinging(totalHorizontalDistance, currentHorizontalSpeed);
	}

	public void animateRiding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateRiding", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateRiding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateRiding();
	}

	public void animateLeftArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateLeftArmItemHolding", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateLeftArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateLeftArmItemHolding();
	}

	public void animateRightArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateRightArmItemHolding", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateRightArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateRightArmItemHolding();
	}

	public void animateWorkingBody(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateWorkingBody", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateWorkingBody(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateWorkingBody();
	}

	public void animateWorkingArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateWorkingArms", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateWorkingArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateWorkingArms();
	}

	public void animateSneaking(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateSneaking", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateSneaking(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateSneaking();
	}

	public void animateArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateArms", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateArms(totalTime);
	}

	public void animateBowAiming(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateBowAiming", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateBowAiming(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateBowAiming(totalTime);
	}
}