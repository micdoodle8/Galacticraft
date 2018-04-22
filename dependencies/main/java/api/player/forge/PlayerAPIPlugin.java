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
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License and the GNU General Public License along with Player API.
// If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

package api.player.forge;

import java.util.*;

import net.minecraftforge.fml.relauncher.*;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions("api.player.forge")
public class PlayerAPIPlugin implements IFMLLoadingPlugin
{
	public static String Version = "1.0";

	public static boolean isObfuscated;

	@Override
    public String[] getASMTransformerClass()
	{
		return new String[] { "api.player.forge.PlayerAPITransformer" };
	}

	@Override
    public String getModContainerClass()
	{
		return "api.player.forge.PlayerAPIContainer";
	}

	@Override
    public String getSetupClass()
	{
		return null;
	}

	@Override
    public void injectData(Map<String, Object> data)
	{
		isObfuscated = (Boolean)data.get("runtimeDeobfuscationEnabled");
	}

	@Override
    public String getAccessTransformerClass()
	{
		return null;
	}
}
