package api.player.forge;

import java.util.*;
import com.google.common.eventbus.*;
import cpw.mods.fml.common.*;

public class PlayerAPIContainer extends DummyModContainer
{
	public PlayerAPIContainer()
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

		meta.modId = "PlayerAPI";
		meta.name = "Player API";
		meta.version = "1.0";
		meta.description = "Player API for Minecraft Forge";
		meta.url = "http://www.minecraftforum.net/topic/738498-";
		meta.authorList = Arrays.asList(new String[] { "Divisor" });

		return meta;
	}
}
