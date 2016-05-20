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

import java.util.*;

import net.minecraft.client.model.*;
import net.minecraft.entity.*;

public class ModelPlayer extends ModelBiped implements IModelPlayer
{
	private final SmartRenderModel model;

	public ModelPlayer(float f)
	{
		super(f);

		model = new SmartRenderModel(this, this, bipedBody, bipedCloak, bipedHead, bipedEars, bipedHeadwear, bipedRightArm, bipedLeftArm, bipedRightLeg, bipedLeftLeg);
	}

	@Override
	public void render(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.render(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
	}

	@Override
	public void superRender(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		super.render(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
	}

	@Override
	public SmartRenderModel getRenderModel()
	{
		return model;
	}

	@Override
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

	@Override
	public void setRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor, Entity entity)
	{
		model.setRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor, entity);
	}

	@Override
	public void superSetRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor, Entity entity)
	{
		super.setRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor, entity);
	}

	@Override
	public void renderCloak(float f)
	{
		model.renderCloak(f);
	}

	@Override
	public void superRenderCloak(float f)
	{
		super.renderCloak(f);
	}

	@Override
	public ModelRenderer getRandomModelBox(Random random)
	{
		return model.getRandomBox(random);
	}

	@Override public ModelRenderer getOuter() { return model.bipedOuter; }
	@Override public ModelRenderer getTorso() { return model.bipedTorso; }
	@Override public ModelRenderer getBody() { return model.bipedBody; }
	@Override public ModelRenderer getBreast() { return model.bipedBreast; }
	@Override public ModelRenderer getNeck() { return model.bipedNeck; }
	@Override public ModelRenderer getHead() { return model.bipedHead; }
	@Override public ModelRenderer getHeadwear() { return model.bipedHeadwear; }
	@Override public ModelRenderer getRightShoulder() { return model.bipedRightShoulder; }
	@Override public ModelRenderer getRightArm() { return model.bipedRightArm; }
	@Override public ModelRenderer getLeftShoulder() { return model.bipedLeftShoulder; }
	@Override public ModelRenderer getLeftArm() { return model.bipedLeftArm; }
	@Override public ModelRenderer getPelvic() { return model.bipedPelvic; }
	@Override public ModelRenderer getRightLeg() { return model.bipedRightLeg; }
	@Override public ModelRenderer getLeftLeg() { return model.bipedLeftLeg; }
	@Override public ModelRenderer getEars() { return model.bipedEars; }
	@Override public ModelRenderer getCloak() { return model.bipedCloak; }

	@Override
	public void animateHeadRotation(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateHeadRotation(viewHorizontalAngelOffset, viewVerticalAngelOffset);
	}

	@Override
	public void animateSleeping(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateSleeping();
	}

	@Override
	public void animateArmSwinging(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateArmSwinging(totalHorizontalDistance, currentHorizontalSpeed);
	}

	@Override
	public void animateRiding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateRiding();
	}

	@Override
	public void animateLeftArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateLeftArmItemHolding();
	}

	@Override
	public void animateRightArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateRightArmItemHolding();
	}

	@Override
	public void animateWorkingBody(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateWorkingBody();
	}

	@Override
	public void animateWorkingArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateWorkingArms();
	}

	@Override
	public void animateSneaking(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateSneaking();
	}

	@Override
	public void animateArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateArms(totalTime);
	}

	@Override
	public void animateBowAiming(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		model.animateBowAiming(totalTime);
	}
}