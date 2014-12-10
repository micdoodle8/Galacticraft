package net.smart.render.playerapi;

import api.player.model.*;
import api.player.render.*;

import net.smart.render.*;

public abstract class SmartRender
{
	public static final String ID = SmartRenderInfo.ModName;

	public static void register()
	{
		RenderPlayerAPI.register(ID, SmartRenderRenderPlayerBase.class);
		ModelPlayerAPI.register(ID, SmartRenderModelPlayerBase.class);
	}

	public static SmartRenderRenderPlayerBase getPlayerBase(net.minecraft.client.renderer.entity.RenderPlayer renderPlayer)
	{
		return (SmartRenderRenderPlayerBase)((IRenderPlayerAPI)renderPlayer).getRenderPlayerBase(ID);
	}

	public static SmartRenderModelPlayerBase getPlayerBase(api.player.model.ModelPlayer renderPlayer)
	{
		return (SmartRenderModelPlayerBase)((IModelPlayerAPI)renderPlayer).getModelPlayerBase(ID);
	}
}