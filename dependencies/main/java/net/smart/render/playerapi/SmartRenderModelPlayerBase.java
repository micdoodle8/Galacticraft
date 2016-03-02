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

	@Override
	public SmartRenderModel getRenderModel()
	{
		if(model == null)
			model = new SmartRenderModel(modelPlayer, this, modelPlayer.bipedBody, modelPlayer.bipedCloak, modelPlayer.bipedHead, modelPlayer.bipedEars, modelPlayer.bipedHeadwear, modelPlayer.bipedRightArm, modelPlayer.bipedLeftArm, modelPlayer.bipedRightLeg, modelPlayer.bipedLeftLeg);
		return model;
	}

	@Override
	public void render(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().render(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
	}

	@Override
	public void superRender(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		super.render(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
	}

	@Override
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

	@Override
	public void setRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor, Entity entity)
	{
		getRenderModel().setRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor, entity);
	}

	@Override
	public void superSetRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor, Entity entity)
	{
		super.setRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor, entity);
	}

	@Override
	public void renderCloak(float f)
	{
		getRenderModel().renderCloak(f);
	}

	@Override
	public void superRenderCloak(float factor)
	{
		super.renderCloak(factor);
	}

	@Override
	public ModelRenderer getRandomModelBox(Random random)
	{
		return getRenderModel().getRandomBox(random);
	}

	@Override public ModelRenderer getOuter() { return getRenderModel().bipedOuter; }
	@Override public ModelRenderer getTorso() { return getRenderModel().bipedTorso; }
	@Override public ModelRenderer getBody() { return getRenderModel().bipedBody; }
	@Override public ModelRenderer getBreast() { return getRenderModel().bipedBreast; }
	@Override public ModelRenderer getNeck() { return getRenderModel().bipedNeck; }
	@Override public ModelRenderer getHead() { return getRenderModel().bipedHead; }
	@Override public ModelRenderer getHeadwear() { return getRenderModel().bipedHeadwear; }
	@Override public ModelRenderer getRightShoulder() { return getRenderModel().bipedRightShoulder; }
	@Override public ModelRenderer getRightArm() { return getRenderModel().bipedRightArm; }
	@Override public ModelRenderer getLeftShoulder() { return getRenderModel().bipedLeftShoulder; }
	@Override public ModelRenderer getLeftArm() { return getRenderModel().bipedLeftArm; }
	@Override public ModelRenderer getPelvic() { return getRenderModel().bipedPelvic; }
	@Override public ModelRenderer getRightLeg() { return getRenderModel().bipedRightLeg; }
	@Override public ModelRenderer getLeftLeg() { return getRenderModel().bipedLeftLeg; }
	@Override public ModelRenderer getEars() { return getRenderModel().bipedEars; }
	@Override public ModelRenderer getCloak() { return getRenderModel().bipedCloak; }

	@Override
	public void animateHeadRotation(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateHeadRotation", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateHeadRotation(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateHeadRotation(viewHorizontalAngelOffset, viewVerticalAngelOffset);
	}

	@Override
	public void animateSleeping(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateSleeping", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateSleeping(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateSleeping();
	}

	@Override
	public void animateArmSwinging(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateArmSwinging", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateArmSwinging(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateArmSwinging(totalHorizontalDistance, currentHorizontalSpeed);
	}

	@Override
	public void animateRiding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateRiding", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateRiding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateRiding();
	}

	@Override
	public void animateLeftArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateLeftArmItemHolding", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateLeftArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateLeftArmItemHolding();
	}

	@Override
	public void animateRightArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateRightArmItemHolding", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateRightArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateRightArmItemHolding();
	}

	@Override
	public void animateWorkingBody(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateWorkingBody", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateWorkingBody(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateWorkingBody();
	}

	@Override
	public void animateWorkingArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateWorkingArms", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateWorkingArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateWorkingArms();
	}

	@Override
	public void animateSneaking(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateSneaking", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateSneaking(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateSneaking();
	}

	@Override
	public void animateArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateArms", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateArms(totalTime);
	}

	@Override
	public void animateBowAiming(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		modelPlayerAPI.dynamic("animateBowAiming", new Object[]{ totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateBowAiming(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor)
	{
		getRenderModel().animateBowAiming(totalTime);
	}
}