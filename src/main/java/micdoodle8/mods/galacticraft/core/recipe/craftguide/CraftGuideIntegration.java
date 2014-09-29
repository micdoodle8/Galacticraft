package micdoodle8.mods.galacticraft.core.recipe.craftguide;

import java.lang.reflect.Method;

public class CraftGuideIntegration
{
	
	public static void register()
	{
		try {
			Class c = Class.forName("uristqwerty.CraftGuide.ReflectionAPI");
			Method m = c.getMethod("registerAPIObject", Object.class);
			m.invoke(null, new CraftGuideCompressorRecipes());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
