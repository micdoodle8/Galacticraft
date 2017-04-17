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

import net.minecraft.launchwrapper.*;

import api.player.client.*;
import api.player.server.*;

public class PlayerAPITransformer implements IClassTransformer
{
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if(transformedName.equals(ClientPlayerClassVisitor.targetClassName))
			return ClientPlayerClassVisitor.transform(bytes, PlayerAPIPlugin.isObfuscated);
		else if(transformedName.equals(ServerPlayerClassVisitor.targetClassName))
			return ServerPlayerClassVisitor.transform(bytes, PlayerAPIPlugin.isObfuscated);
		else
			return bytes;
	}
}