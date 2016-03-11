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

import java.lang.reflect.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.*;
import cpw.mods.fml.common.gameevent.TickEvent.*;
import net.smart.render.statistics.*;
import net.smart.utilities.*;

@Mod(modid = "SmartRender", name = "Smart Render", version = "2.1", dependencies = "required-after:PlayerAPI@[1.3,)")
public class SmartRenderMod
{
	private static boolean addRenderer = true;

	private boolean hasRenderer = false;

	public static void doNotAddRenderer()
	{
		addRenderer = false;
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		if(!FMLCommonHandler.instance().getSide().isClient())
			return;

		hasRenderer = Loader.isModLoaded("RenderPlayerAPI");

		net.smart.render.statistics.playerapi.SmartStatistics.register();

		if(hasRenderer)
		{
			Class<?> type = Reflect.LoadClass(SmartRenderMod.class, new Name("net.smart.render.playerapi.SmartRender"), true);
			Method method = Reflect.GetMethod(type, new Name("register"));
			Reflect.Invoke(method, null);
		}

		if(!hasRenderer && addRenderer)
			SmartRenderContext.registerRenderers(null);

		net.smart.render.statistics.playerapi.SmartStatisticsFactory.initialize();

		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	@SuppressWarnings({ "static-method" })
	public void tickStart(ClientTickEvent event)
	{
		SmartStatisticsContext.onTickInGame();
	}
}