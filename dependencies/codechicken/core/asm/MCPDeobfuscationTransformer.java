package codechicken.core.asm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFileChooser;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import codechicken.core.launch.CodeChickenCorePlugin;
import codechicken.lib.asm.ASMHelper;
import codechicken.lib.asm.CC_ClassWriter;
import codechicken.lib.asm.ObfMapping;
import codechicken.lib.config.ConfigFile;
import codechicken.lib.config.ConfigTag;
import codechicken.obfuscator.IHeirachyEvaluator;
import codechicken.obfuscator.ObfuscationRun;
import codechicken.obfuscator.ObfuscationMap.ObfuscationEntry;
import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import cpw.mods.fml.common.asm.transformers.DeobfuscationTransformer;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class MCPDeobfuscationTransformer implements IClassTransformer, Opcodes, IHeirachyEvaluator
{
    private static ObfuscationRun run;
    private static List<String> excludedPackages = new LinkedList<String>();
    private static MCPDeobfuscationTransformer instance = new MCPDeobfuscationTransformer();
    
    private static boolean activated;

    private static Field f_transformers;
    private static Field f_modifiers;
    private static Field f_Modifier_name;
    private static Field f_Modifier_desc;
    
    static
    {
        try
        {
            f_transformers = LaunchClassLoader.class.getDeclaredField("transformers");
            f_modifiers = AccessTransformer.class.getDeclaredField("modifiers");
            Class<?> c_Modifier = Class.forName(AccessTransformer.class.getName()+"$Modifier", false, 
                    CodeChickenCorePlugin.cl);
            f_Modifier_name = c_Modifier.getDeclaredField("name");
            f_Modifier_desc = c_Modifier.getDeclaredField("desc");
            
            f_transformers.setAccessible(true);
            f_modifiers.setAccessible(true);
            f_Modifier_name.setAccessible(true);
            f_Modifier_desc.setAccessible(true);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private static Object get(Field f, Object inst)
    {
        try
        {
            return f.get(inst);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private static void set(Field f, Object inst, Object value)
    {
        try
        {
            f.set(inst, value);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private static List<IClassTransformer> getTransformers()
    {
        return (List<IClassTransformer>) get(f_transformers, CodeChickenCorePlugin.cl);
    }
    
    public static void load()
    {
        ConfigFile config = CodeChickenCoreModContainer.config;
        File mcDir = CodeChickenCorePlugin.minecraftDir;
        
        if(config.getTag("dev.deobfuscate")
                .setComment("set to true to completely deobfuscate mcp names")
                .getBooleanValue(!ObfMapping.obfuscated))
        {
            run = new ObfuscationRun(false, getConfDir(mcDir, config), 
                    ObfuscationRun.fillDefaults(new HashMap<String, String>()));
            run.obf.setHeirachyEvaluator(instance);
            run.setQuiet().parseMappings();
            for(String pkg : run.config.get("excludedPackages").split(";"))
                excludedPackages.add(pkg);
            
            ObfMapping.runtimeMapper = run.obfMapper;
            if(ObfMapping.obfuscated)
            {
                run.setSeargeConstants();
                getTransformers().add(instance);
            }
            else
            {
                ObfMapping.mcpMapper = run.obfMapper;
                getTransformers().add(0, instance);//insert transformer as first.
            }
        }
    }

    private static File getConfDir(File mcDir, ConfigFile config)
    {
        ConfigTag tag = config.getTag("dev.mappingDir");
        if(tag.value != null)
            return new File(tag.getValue());
        
        File test = new File(mcDir, "../conf");
        if(new File(test, "methods.csv").exists())
        {
            tag.setValue(test.getPath());
            return test;
        }
        
        for(int i = 0; i < 3; i++)
        {
            JFileChooser fc = new JFileChooser(mcDir);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setDialogTitle("Select an mcp conf dir for the deobfuscator.");
            int ret = fc.showDialog(null, "Select");
            if(ret != JFileChooser.APPROVE_OPTION)
                continue;
            
            File selected = fc.getSelectedFile();
            if(selected.isFile() || !new File(selected, "methods.csv").exists())
                continue;
            
            tag.setValue(selected.getPath());
            return selected;
        }

        throw new RuntimeException("Failed to select mappings directory, set it manually in the config");
    }
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if(name.equals("cpw.mods.fml.common.Loader"))
        {
            bytes = injectCallback(bytes);
            activated = true;
        }
        
        if(!activated || bytes == null)
            return bytes;
        
        ClassNode cnode = ASMHelper.createClassNode(bytes, ClassReader.EXPAND_FRAMES);
        ClassWriter cw = new CC_ClassWriter(0, true);
        run.remap(cnode, cw);
        return cw.toByteArray();
    }
    
    private byte[] injectCallback(byte[] bytes)
    {
        ClassNode cnode = ASMHelper.createClassNode(bytes);
        MethodNode mnode = ASMHelper.findMethod(new ObfMapping(cnode.name, "<clinit>", "()V"), cnode);
        mnode.instructions.insert(new MethodInsnNode(INVOKESTATIC, "codechicken/core/asm/MCPDeobfuscationTransformer", "loadCallback", "()V"));
        return ASMHelper.createBytes(cnode, 0);
    }

    public static void loadCallback()
    {
        if(ObfMapping.obfuscated)
        {
            //move ourselves to the end
            List<IClassTransformer> transformers = getTransformers();
            for(Iterator<IClassTransformer> iterator = transformers.iterator(); iterator.hasNext(); )
            {
                IClassTransformer t = iterator.next();
                if(t == instance || t instanceof DeobfuscationTransformer)
                    iterator.remove();
            }
            transformers.add(instance);
        }
        else
        {
            //remap access transformers
            for(IClassTransformer t : getTransformers())
                if(t instanceof AccessTransformer)
                    remapAccessTransformer(t);
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void remapAccessTransformer(IClassTransformer t)
    {
        Multimap<String, ?> modifiers = (Multimap<String, ?>) get(f_modifiers, t);
        Multimap<String, Object> t_modifiers = HashMultimap.create();
        for(Entry<String, ?> entry : modifiers.entries())
        {
            Object mod = entry.getValue();
            String m_owner = entry.getKey().replace('.', '/');
            String m_name = (String)get(f_Modifier_name, mod);
            String m_desc = (String)get(f_Modifier_desc, mod);
            ObfMapping map;
            if(m_name.equals("*"))
            {
                map = new ObfMapping(m_owner);
                map.s_name = m_name;
                map.s_desc = m_desc;
            }
            else
            {
                map = new ObfMapping(m_owner, m_name, m_desc);
            }

            set(f_Modifier_name, mod, map.s_name);
            set(f_Modifier_desc, mod, map.s_desc);
            t_modifiers.put(map.javaClass(), mod);
        }
        set(f_modifiers, t, t_modifiers);
    }

    @Override
    public List<String> getParents(ObfuscationEntry desc)
    {
        try
        {
            String name = ObfMapping.obfuscated ? desc.obf.s_owner : desc.mcp.s_owner;
            name = name.replace('/', '.');
            byte[] bytes = CodeChickenCorePlugin.cl.getClassBytes(name);
            if(bytes != null)
                return ObfuscationRun.getParents(ASMHelper.createClassNode(bytes));
        }
        catch(IOException e)
        {
        }
        return null;
    }
    
    @Override
    public boolean isLibClass(ObfuscationEntry desc)
    {
        String name = desc.srg.s_owner;
        for(String p : excludedPackages)
            if(name.startsWith(p))
                return true;
        
        return false;
    }

    public static String unmap(String name)
    {
        if(run == null) 
            return null;
        ObfuscationEntry e = run.obf.lookupMcpClass(name);
        if(e == null)
            return null;
        return e.obf.s_owner;
    }

    public static MCPDeobfuscationTransformer instance()
    {
        return instance;
    }
}
