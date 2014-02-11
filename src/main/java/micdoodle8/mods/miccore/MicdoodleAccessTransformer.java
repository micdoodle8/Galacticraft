package micdoodle8.mods.miccore;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class MicdoodleAccessTransformer extends AccessTransformer
{
    private static MicdoodleAccessTransformer instance;
    private static List<String> mapFileList = new LinkedList<String>();

    public MicdoodleAccessTransformer() throws IOException
    {
        super();
        MicdoodleAccessTransformer.instance = this;

        for (final String file : MicdoodleAccessTransformer.mapFileList)
        {
            this.readMapFile(file);
        }

        MicdoodleAccessTransformer.mapFileList = null;
    }

    public static void addTransformerMap(String mapFile)
    {
        if (MicdoodleAccessTransformer.instance == null)
        {
            MicdoodleAccessTransformer.mapFileList.add(mapFile);
        }
        else
        {
            MicdoodleAccessTransformer.instance.readMapFile(mapFile);
        }
    }

    private void readMapFile(String mapFile)
    {
        System.out.println("[Micdoodle8Core]: Adding AccessTransformer: " + mapFile);

        try
        {
            final Method parentMapFile = AccessTransformer.class.getDeclaredMethod("readMapFile", String.class);
            parentMapFile.setAccessible(true);
            parentMapFile.invoke(this, mapFile);
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
