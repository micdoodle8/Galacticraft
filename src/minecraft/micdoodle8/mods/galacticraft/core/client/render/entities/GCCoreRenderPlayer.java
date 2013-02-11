package micdoodle8.mods.galacticraft.core.client.render.entities;

import java.util.HashMap;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraft.src.RenderPlayerBase;

public class GCCoreRenderPlayer extends RenderPlayerBase
{
	public GCCoreRenderPlayer(RenderPlayerAPI var1) 
	{
		super(var1);
	}

    public void renderPlayer(EntityPlayer var1, double var2, double var4, double var6, float var8, float var9)
    {
    	super.renderPlayer(var1, var2, var4, var6, var8, var9);
    }
}
