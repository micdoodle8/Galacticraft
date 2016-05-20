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

import net.minecraft.client.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.player.*;

public interface IRenderPlayer
{
	IModelPlayer createModel(ModelBiped existing, float f);

	void initialize(ModelBiped modelBipedMain, ModelBiped modelArmorChestplate, ModelBiped modelArmor, float shadowSize);

	void superRenderPlayer(AbstractClientPlayer entityplayer, double d, double d1, double d2, float f, float renderPartialTicks);

	void superDrawFirstPersonHand(EntityPlayer entityPlayer);

	void superRotatePlayer(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2);

	void superRenderSpecials(AbstractClientPlayer entityplayer, float f);

	RenderManager getRenderManager();

	ModelBiped getModelBipedMain();

	ModelBiped getModelArmorChestplate();

	ModelBiped getModelArmor();

	IModelPlayer[] getRenderModels();
}