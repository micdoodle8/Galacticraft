package codechicken.core.asm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

import static codechicken.core.launch.CodeChickenCorePlugin.logger;

public class DelegatedTransformer implements IClassTransformer
{
    private static ArrayList<IClassTransformer> delegatedTransformers;
    private static Method m_defineClass;
    private static Field f_cachedClasses;
    
    public DelegatedTransformer()
    {
        delegatedTransformers = new ArrayList<IClassTransformer>();
        try
        {
            m_defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
            m_defineClass.setAccessible(true);
            f_cachedClasses = LaunchClassLoader.class.getDeclaredField("cachedClasses");
            f_cachedClasses.setAccessible(true);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        if (bytes == null) return null;
        for(IClassTransformer trans : delegatedTransformers)
            bytes = trans.transform(name, tname, bytes);
        return bytes;
    }

    public static void addTransformer(String transformer, JarFile jar, File jarFile)
    {
        logger.debug("Adding CCTransformer: " + transformer);
        try
        {
            byte[] bytes;
            bytes = Launch.classLoader.getClassBytes(transformer);
            
            if(bytes == null)
            {
                String resourceName = transformer.replace('.', '/')+".class";
                ZipEntry entry = jar.getEntry(resourceName);
                if(entry == null)
                    throw new Exception("Failed to add transformer: "+transformer+". Entry not found in jar file "+jarFile.getName());
                
                bytes = readFully(jar.getInputStream(entry));
            }
            
            defineDependancies(bytes, jar, jarFile);
            Class<?> clazz = defineClass(transformer, bytes);
            
            if(!IClassTransformer.class.isAssignableFrom(clazz))
                throw new Exception("Failed to add transformer: "+transformer+" is not an instance of IClassTransformer");
            
            IClassTransformer classTransformer;
            try
            {
                classTransformer = (IClassTransformer) clazz.getDeclaredConstructor(File.class).newInstance(jarFile);
            }
            catch(NoSuchMethodException nsme)
            {
                classTransformer = (IClassTransformer) clazz.newInstance();
            }
            delegatedTransformers.add(classTransformer);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }        
    }
    
    private static void defineDependancies(byte[] bytes, JarFile jar, File jarFile) throws Exception
    {
        defineDependancies(bytes, jar, jarFile, new Stack<String>());
    }

    private static void defineDependancies(byte[] bytes, JarFile jar, File jarFile, Stack<String> depStack) throws Exception
    {
        ClassReader reader = new ClassReader(bytes);
        DependancyLister lister = new DependancyLister(Opcodes.ASM4);
        reader.accept(lister, 0);
        
        depStack.push(reader.getClassName());
        
        for(String dependancy : lister.getDependancies())
        {
            if(depStack.contains(dependancy))
                continue;
            
            try
            {
                Launch.classLoader.loadClass(dependancy.replace('/', '.'));
            }
            catch(ClassNotFoundException cnfe)
            {
                ZipEntry entry = jar.getEntry(dependancy+".class");
                if(entry == null)
                    throw new Exception("Dependency "+dependancy+" not found in jar file "+jarFile.getName());
                
                byte[] depbytes = readFully(jar.getInputStream(entry));
                defineDependancies(depbytes, jar, jarFile, depStack);

                logger.debug("Defining dependancy: "+dependancy);
                
                defineClass(dependancy.replace('/', '.'), depbytes);
            }
        }
        
        depStack.pop();
    }

    private static Class<?> defineClass(String classname, byte[] bytes) throws Exception
    {
        Class<?> clazz = (Class<?>) m_defineClass.invoke(Launch.classLoader, classname, bytes, 0, bytes.length);
        ((Map<String, Class<?>>)f_cachedClasses.get(Launch.classLoader)).put(classname, clazz);
        return clazz;
    }

    public static byte[] readFully(InputStream stream) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(stream.available());
        int r;
        while ((r = stream.read()) != -1)
        {
            bos.write(r);
        }

        return bos.toByteArray();
    }
}
