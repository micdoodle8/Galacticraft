package micdoodle8.mods.galacticraft.asm;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class GalacticraftAccessTransformer extends AccessTransformer
{
	private static GalacticraftAccessTransformer instance;
	private static List<String> mapFileList = new LinkedList<String>();

	public GalacticraftAccessTransformer() throws IOException
	{
		super();
		GalacticraftAccessTransformer.instance = this;

		for(final String file : GalacticraftAccessTransformer.mapFileList)
		{
			this.readMapFile(file);
		}

		GalacticraftAccessTransformer.mapFileList = null;
	}

	public static void addTransformerMap(String mapFile)
	{
		if(GalacticraftAccessTransformer.instance == null)
		{
			GalacticraftAccessTransformer.mapFileList.add(mapFile);
		}
		else
		{
			GalacticraftAccessTransformer.instance.readMapFile(mapFile);
		}
	}

	private void readMapFile(String mapFile)
	{
		System.out.println("[GCCoreTransformer]: Adding AccessTransformer: " + mapFile);

		try
		{
			final Method parentMapFile = AccessTransformer.class.getDeclaredMethod("readMapFile", String.class);
			parentMapFile.setAccessible(true);
			parentMapFile.invoke(this, mapFile);
		}
		catch(final Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
