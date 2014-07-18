package codechicken.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.tree.ClassNode;

import codechicken.core.launch.CodeChickenCorePlugin;
import codechicken.lib.asm.ASMHelper;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.relauncher.CoreModManager;

public class ClassDiscoverer
{
    public IStringMatcher matcher;
    public String[] superclasses;
    public ArrayList<Class<?>> classes;
    public ModClassLoader modClassLoader;

    public ClassDiscoverer(IStringMatcher matcher, Class<?>... superclasses) {
        this.matcher = matcher;
        this.superclasses = new String[superclasses.length];
        for (int i = 0; i < superclasses.length; i++)
            this.superclasses[i] = superclasses[i].getName().replace('.', '/');

        classes = new ArrayList<Class<?>>();
        modClassLoader = (ModClassLoader) Loader.instance().getModClassLoader();
    }

    public ClassDiscoverer(Class<?>... superclasses) {
        this(new IStringMatcher()
        {
            public boolean matches(String test) {return true;}
        }, superclasses);
    }

    public ArrayList<Class<?>> findClasses() {
        try {
            findClasspathMods();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    private void checkAddClass(String resource) {
        try {
            String classname = resource.replace(".class", "").replace("\\", ".").replace("/", ".");
            byte[] bytes = Launch.classLoader.getClassBytes(classname);
            if (bytes == null)
                return;

            ClassNode cnode = ASMHelper.createClassNode(bytes);
            for (String superclass : superclasses)
                if (!cnode.interfaces.contains(superclass) && !cnode.superName.equals(superclass))
                    return;

            addClass(classname);
        } catch (IOException e) {
            CodeChickenCorePlugin.logger.error("Unable to load class: " + resource, e);
        }
    }

    private void addClass(String classname) {
        try {
            Class<?> class1 = Class.forName(classname, true, modClassLoader);
            classes.add(class1);
        } catch (Throwable t) {
            CodeChickenCorePlugin.logger.error("Unable to load class: " + classname, t);
        }
    }

    private void findClasspathMods() {
        List<String> knownLibraries = ImmutableList.<String>builder()
                .addAll(modClassLoader.getDefaultLibraries())
                .addAll(CoreModManager.getLoadedCoremods()).build();
        File[] minecraftSources = modClassLoader.getParentSources();
        HashSet<String> searchedSources = new HashSet<String>();
        for (File minecraftSource : minecraftSources) {
            if (searchedSources.contains(minecraftSource.getAbsolutePath()))
                continue;
            searchedSources.add(minecraftSource.getAbsolutePath());

            if (minecraftSource.isFile()) {
                if (!knownLibraries.contains(minecraftSource.getName())) {
                    FMLLog.fine("Found a minecraft related file at %s, examining for codechicken classes", minecraftSource.getAbsolutePath());
                    try {
                        readFromZipFile(minecraftSource);
                    } catch (Exception e) {
                        CodeChickenCorePlugin.logger.error("Failed to scan " + minecraftSource.getAbsolutePath() + ", the zip file is invalid", e);
                    }
                }
            } else if (minecraftSource.isDirectory()) {
                FMLLog.fine("Found a minecraft related directory at %s, examining for codechicken classes", minecraftSource.getAbsolutePath());
                readFromDirectory(minecraftSource, minecraftSource);
            }
        }
    }

    private void readFromZipFile(File file) throws IOException {
        FileInputStream fileinputstream = new FileInputStream(file);
        ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
        do {
            ZipEntry zipentry = zipinputstream.getNextEntry();
            if (zipentry == null) {
                break;
            }
            String fullname = zipentry.getName().replace('\\', '/');
            int pos = fullname.lastIndexOf('/');
            String name = pos == -1 ? fullname : fullname.substring(pos + 1);
            if (!zipentry.isDirectory() && matcher.matches(name)) {
                checkAddClass(fullname);
            }
        }
        while (true);
        fileinputstream.close();
    }

    private void readFromDirectory(File directory, File basedirectory) {
        for (File child : directory.listFiles()) {
            if (child.isDirectory()) {
                readFromDirectory(child, basedirectory);
            } else if (child.isFile() && matcher.matches(child.getName())) {
                String fullname = CommonUtils.getRelativePath(basedirectory, child);
                checkAddClass(fullname);
            }
        }
    }
}
