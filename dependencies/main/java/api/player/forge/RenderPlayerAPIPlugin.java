package api.player.forge;

import java.util.*;
import cpw.mods.fml.relauncher.*;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions("api.player.forge")
public class RenderPlayerAPIPlugin implements IFMLLoadingPlugin
{
	public static boolean isObfuscated;

	public String[] getASMTransformerClass()
	{
		return new String[] { "api.player.forge.RenderPlayerAPITransformer" };
	}

	public String getModContainerClass()
	{
		return "api.player.forge.RenderPlayerAPIContainer";
	}

	public String getSetupClass()
	{
		return null;
	}

	public void injectData(Map<String, Object> data)
	{
		isObfuscated = (Boolean)data.get("runtimeDeobfuscationEnabled");
	}

	public String getAccessTransformerClass()
	{
		return null;
	}
}
