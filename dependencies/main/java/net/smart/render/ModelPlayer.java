package net.smart.render;

import java.util.*;

import net.minecraft.client.model.*;
import net.minecraft.entity.*;

public class ModelPlayer extends ModelBiped implements IModelPlayer
{
	private final SmartRenderModel model;

	public ModelPlayer(float f)
	{
		super(f);

		model = new SmartRenderModel(f, this, this, bipedBody, bipedCloak, bipedHead, bipedEars, bipedHeadwear, bipedRightArm, bipedLeftArm, bipedRightLeg, bipedLeftLeg);
	}

	public void render(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.render(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
	}

	public void superRender(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		super.render(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
	}

	public SmartRenderModel getRenderModel()
	{
		return model;
	}

	public void initialize(ModelRenderer bipedBody, ModelRenderer bipedCloak, ModelRenderer bipedHead, ModelRenderer bipedEars, ModelRenderer bipedHeadwear, ModelRenderer bipedRightArm, ModelRenderer bipedLeftArm, ModelRenderer bipedRightLeg, ModelRenderer bipedLeftLeg)
	{
		this.bipedBody = bipedBody;
		this.bipedCloak = bipedCloak;
		this.bipedHead = bipedHead;
		this.bipedEars = bipedEars;
		this.bipedHeadwear = bipedHeadwear;
		this.bipedRightArm = bipedRightArm;
		this.bipedLeftArm = bipedLeftArm;
		this.bipedRightLeg = bipedRightLeg;
		this.bipedLeftLeg = bipedLeftLeg;
	}

	public void setRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor, Entity entity)
	{
		model.setRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor, entity);
	}

	public void superSetRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor, Entity entity)
	{
		super.setRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor, entity);
	}

	public void renderCloak(float f)
	{
		model.renderCloak(f);
	}

	public void superRenderCloak(float f)
	{
		super.renderCloak(f);
	}

	public ModelRenderer getRandomModelBox(Random random)
    {
		return model.getRandomBox(random);
	}

	public ModelRenderer getOuter() { return model.bipedOuter; }
	public ModelRenderer getTorso() { return model.bipedTorso; }
	public ModelRenderer getBody() { return model.bipedBody; }
	public ModelRenderer getBreast() { return model.bipedBreast; }
	public ModelRenderer getNeck() { return model.bipedNeck; }
	public ModelRenderer getHead() { return model.bipedHead; }
	public ModelRenderer getHeadwear() { return model.bipedHeadwear; }
	public ModelRenderer getRightShoulder() { return model.bipedRightShoulder; }
	public ModelRenderer getRightArm() { return model.bipedRightArm; }
	public ModelRenderer getLeftShoulder() { return model.bipedLeftShoulder; }
	public ModelRenderer getLeftArm() { return model.bipedLeftArm; }
	public ModelRenderer getPelvic() { return model.bipedPelvic; }
	public ModelRenderer getRightLeg() { return model.bipedRightLeg; }
	public ModelRenderer getLeftLeg() { return model.bipedLeftLeg; }
	public ModelRenderer getEars() { return model.bipedEars; }
	public ModelRenderer getCloak() { return model.bipedCloak; }

	public void animateHeadRotation(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateHeadRotation(viewHorizontalAngelOffset, viewVerticalAngelOffset);
	}

	public void animateSleeping(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateSleeping();
	}

	public void animateArmSwinging(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateArmSwinging(totalHorizontalDistance, currentHorizontalSpeed);
	}

	public void animateRiding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateRiding();
	}

	public void animateLeftArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateLeftArmItemHolding();
	}

	public void animateRightArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateRightArmItemHolding();
	}

	public void animateWorkingBody(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateWorkingBody();
	}

	public void animateWorkingArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateWorkingArms();
	}

	public void animateSneaking(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateSneaking();
	}

	public void animateArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateArms(totalTime);
	}

	public void animateBowAiming(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateBowAiming(totalTime);
	}
}