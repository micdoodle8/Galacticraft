package api.player.forge;

import java.util.*;

import net.minecraft.launchwrapper.*;

import api.player.model.*;
import api.player.render.*;

public class RenderPlayerAPITransformer implements IClassTransformer
{
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if(transformedName.equals(RenderPlayerClassVisitor.targetClassName))
		{
			Stack<String> models = new Stack<String>();
			models.push(ModelPlayerClassVisitor.deobfuscatedClassReference + ":armor");
			models.push(ModelPlayerClassVisitor.deobfuscatedClassReference + ":chestplate");
			models.push(ModelPlayerClassVisitor.deobfuscatedClassReference + ":main");

			Map<String, Stack<String>> renderConstructorReplacements = new Hashtable<String, Stack<String>>();
			renderConstructorReplacements.put(ModelPlayerClassVisitor.obfuscatedSuperClassReference, models);
			renderConstructorReplacements.put(ModelPlayerClassVisitor.deobfuscateSuperClassReference, models);

			return RenderPlayerClassVisitor.transform(bytes, RenderPlayerAPIPlugin.isObfuscated, renderConstructorReplacements);
		}
		else if(transformedName.equals(ModelPlayerClassVisitor.targetClassName))
			return ModelPlayerClassVisitor.transform(bytes, RenderPlayerAPIPlugin.isObfuscated);
		else
			return bytes;
	}
}