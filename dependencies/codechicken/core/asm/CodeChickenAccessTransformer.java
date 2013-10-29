package codechicken.core.asm;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableBiMap;

import codechicken.lib.asm.ObfMapping;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class CodeChickenAccessTransformer extends AccessTransformer
{
    private static CodeChickenAccessTransformer instance;
    private static List<String> mapFileList = new LinkedList<String>();
    
    private static boolean makeAllPublic;
    private static Field f_classNameBiMap;
    private static Object emptyMap = ImmutableBiMap.of();
    
    public CodeChickenAccessTransformer() throws IOException
    {
        super();
        instance = this;
        for(String file : mapFileList)
            readMapFile(file);
        mapFileList = null;
        loadPublicConfig();
    }
    
    private void loadPublicConfig() {
        if(ObfMapping.obfuscated)
            return;
        
       makeAllPublic = CodeChickenCoreModContainer.config.getTag("dev.runtimePublic")
                .setComment("Enabling this setting will make all minecraft classes public at runtime in MCP just as they are in modloader." +
        		"\nYou should ONLY use this when you are testing with a mod that relies on runtime publicity and doesn't include access transformers." +
        		"\nSuch mods are doing the wrong thing and should be fixed.")
		.getBooleanValue(false);
       
       if(!makeAllPublic)
            return;
        
        try
        {
            f_classNameBiMap = FMLDeobfuscatingRemapper.class.getDeclaredField("classNameBiMap");
            f_classNameBiMap.setAccessible(true);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void addTransformerMap(String mapFile)
    {
        if(instance == null)
            mapFileList.add(mapFile);
        else
            instance.readMapFile(mapFile);
    }
    
    private void readMapFile(String mapFile)
    {
        System.out.println("Adding Accesstransformer map: "+mapFile);
        try
        {
            Method parentMapFile = AccessTransformer.class.getDeclaredMethod("readMapFile", String.class);
            parentMapFile.setAccessible(true);
            parentMapFile.invoke(this, mapFile);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        boolean setPublic = makeAllPublic && name.startsWith("net.minecraft.");
        if(setPublic)
            setClassMap(name);
        bytes = super.transform(name, transformedName, bytes);
        if(setPublic)
            restoreClassMap();
        return bytes;
    }

    private void restoreClassMap() {
        try
        {
            f_classNameBiMap.set(FMLDeobfuscatingRemapper.INSTANCE, emptyMap);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void setClassMap(String name) {
        try
        {
            f_classNameBiMap.set(FMLDeobfuscatingRemapper.INSTANCE, ImmutableBiMap.of(name.replace('.', '/'), ""));
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
