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