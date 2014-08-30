package api.player.forge;

import java.util.*;
import com.google.common.eventbus.*;
import cpw.mods.fml.common.*;

public class RenderPlayerAPIContainer extends DummyModContainer
{
	public RenderPlayerAPIContainer()
	{
		super(createMetadata());
	}

	public boolean registerBus(EventBus bus, LoadController controller)
	{
		return true;
	}

	private static ModMetadata createMetadata()
	{
		ModMetadata meta = new ModMetadata();

		meta.modId = "RenderPlayerAPI";
		meta.name = "Render Player API";
		meta.version = "1.0";
		meta.description = "Render Player API for Minecraft Forge";
		meta.url = "http://www.minecraftforum.net/topic/1261354-";
		meta.authorList = Arrays.asList(new String[] { "Divisor" });

		return meta;
	}
}
