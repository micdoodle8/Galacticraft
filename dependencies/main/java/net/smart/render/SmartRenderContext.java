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

import cpw.mods.fml.client.registry.RenderingRegistry;

import net.minecraft.client.entity.*;
import net.minecraft.client.renderer.entity.*;

public abstract class SmartRenderContext extends SmartRenderUtilities
{
	public static void registerRenderers()
	{
		registerRenderers(net.smart.render.RenderPlayer.class);
	}

	public static void registerRenderers(Class<?> type)
	{
		Render render;
		try
		{
			render = (Render)type.newInstance();
		}
		catch (Exception e)
		{
			return;
		}

		RenderingRegistry.registerEntityRenderingHandler(EntityPlayerSP.class, render);
		RenderingRegistry.registerEntityRenderingHandler(EntityOtherPlayerMP.class, render);
		render.setRenderManager(RenderManager.instance);
	}
}