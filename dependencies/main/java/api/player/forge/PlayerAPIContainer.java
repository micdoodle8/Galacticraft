// ==================================================================
// This file is part of Player API.
// 
// Player API is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
// 
// Player API is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License and the GNU General Public License along with Player API.
// If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

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
		meta.version = PlayerAPIPlugin.Version;
		meta.description = "Player API for Minecraft Forge";
		meta.url = "http://www.minecraftforum.net/topic/738498-";
		meta.authorList = Arrays.asList(new String[] { "Divisor" });

		return meta;
	}
}