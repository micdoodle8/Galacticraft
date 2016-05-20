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

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;

public class RenderPlayer extends net.minecraft.client.renderer.entity.RenderPlayer implements IRenderPlayer
{
	public RenderPlayer()
	{
		render = new SmartRenderRender(this);
	}

	@Override
	public IModelPlayer createModel(ModelBiped existing, float f)
	{
		return new ModelPlayer(f);
	}

	@Override
	public void initialize(ModelBiped modelBipedMain, ModelBiped modelArmorChestplate, ModelBiped modelArmor, float shadowSize)
	{
		this.mainModel = modelBipedMain;
		this.shadowSize = shadowSize;

		this.modelBipedMain = modelBipedMain;
		this.modelArmorChestplate = modelArmorChestplate;
		this.modelArmor = modelArmor;
	}

	@Override
	public void doRender(AbstractClientPlayer entityplayer, double d, double d1, double d2, float f, float renderPartialTicks)
	{
		render.renderPlayer(entityplayer, d, d1, d2, f, renderPartialTicks);
	}

	@Override
	public void superRenderPlayer(AbstractClientPlayer entityplayer, double d, double d1, double d2, float f, float renderPartialTicks)
	{
		super.doRender(entityplayer, d, d1, d2, f, renderPartialTicks);
	}

	@Override
	public void renderFirstPersonArm(EntityPlayer entityPlayer)
	{
		render.drawFirstPersonHand(entityPlayer);
	}

	@Override
	public void superDrawFirstPersonHand(EntityPlayer entityPlayer)
	{
		super.renderFirstPersonArm(entityPlayer);
	}

	@Override
	protected void rotateCorpse(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2)
	{
		render.rotatePlayer(entityplayer, totalTime, actualRotation, f2);
	}

	@Override
	public void superRotatePlayer(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2)
	{
		super.rotateCorpse(entityplayer, totalTime, actualRotation, f2);
	}

	@Override
	protected void preRenderCallback(AbstractClientPlayer entityplayer, float f)
	{
		render.renderSpecials(entityplayer, f);
	}

	@Override
	public void superRenderSpecials(AbstractClientPlayer entityplayer, float f)
	{
		super.preRenderCallback(entityplayer, f);
	}

	@Override
	protected float handleRotationFloat(EntityLivingBase entityliving, float f)
	{
		render.beforeHandleRotationFloat(entityliving, f);
		float result = super.handleRotationFloat(entityliving, f);
		render.afterHandleRotationFloat(entityliving, f);
		return result;
	}

	@Override
	public RenderManager getRenderManager()
	{
		return renderManager;
	}

	@Override
	public ModelBiped getModelBipedMain()
	{
		return (ModelBiped)mainModel;
	}

	@Override
	public ModelBiped getModelArmorChestplate()
	{
		return modelArmorChestplate;
	}

	@Override
	public ModelBiped getModelArmor()
	{
		return modelArmor;
	}

	public IModelPlayer getRenderModelBipedMain()
	{
		return (ModelPlayer)getModelBipedMain();
	}

	public IModelPlayer getRenderModelArmorChestplate()
	{
		return (ModelPlayer)getModelArmorChestplate();
	}

	public IModelPlayer getRenderModelArmor()
	{
		return (ModelPlayer)getModelArmor();
	}

	@Override
	public IModelPlayer[] getRenderModels()
	{
		if(allIModelPlayers == null)
			allIModelPlayers = new IModelPlayer[] { getRenderModelBipedMain(), getRenderModelArmorChestplate(), getRenderModelArmor() };
		return allIModelPlayers;
	}

	private IModelPlayer[] allIModelPlayers;

	private final SmartRenderRender render;
}