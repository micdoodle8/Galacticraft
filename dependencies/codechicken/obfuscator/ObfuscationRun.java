package codechicken.obfuscator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.tree.ClassNode;

import com.google.common.base.Function;

public class ObfuscationRun implements ILogStreams
{
    public final ObfDirection obfDir;
    public final ObfuscationMap obf;
    public final ObfRemapper obfMapper;
    public final ConstantObfuscator cstMappper;
    
    public File confDir;
    public Map<String, String> config;
    
    private PrintStream out = System.out;
    private PrintStream err = System.err;
    private PrintStream quietStream = new PrintStream(DummyOutputStream.instance);
    
    private boolean verbose;
    private boolean quiet;

    public boolean clean;
    private long startTime;
    private boolean finished;
    
    public ObfuscationRun(boolean obfuscate, File confDir, Map<String, String> config)
    {
        obfDir = new ObfDirection().setObfuscate(obfuscate);
        this.confDir = confDir;
        this.config = config;
        
        obf = new ObfuscationMap().setLog(this);
        obfMapper = new ObfRemapper(obf, obfDir);
        cstMappper = new ConstantObfuscator(obfMapper, 
                config.get("classConstantCalls").split(","), 
                config.get("descConstantCalls").split(","));
    }
    
    public ObfuscationRun setClean()
    {
        clean = true;
        return this;
    }
    
    public ObfuscationRun setVerbose()
    {
        verbose = true;
        return this;
    }
    
    public ObfuscationRun setQuiet()
    {
        quiet = true;
        return this;
    }
    
    public ObfuscationRun setOut(PrintStream p)
    {
        out = p;
        return this;
    }
    
    public PrintStream out()
    {
        return quiet ? quietStream : out;
    }
    
    public PrintStream fine()
    {
        return verbose ? out : quietStream;
    }
    
    public ObfuscationRun setErr(PrintStream p)
    {
        err = p;
        return this;
    }
    
    public PrintStream err()
    {
        return quiet ? quietStream : err;
    }

    public ObfuscationRun setSearge()
    {
        obfDir.setSearge(true);
        return this;
    }

    public ObfuscationRun setSeargeConstants()
    {
        obfDir.setSeargeConstants(true);
        return this;
    }
    
    public void start()
    {
        startTime = System.currentTimeMillis();
    }
    
    public static Map<String, String> fillDefaults(Map<String, String> config)
    {
        if(!config.containsKey("excludedPackages"))
            config.put("excludedPackages", "java/;sun/;javax/;scala/;" +
                    "argo/;org/lwjgl/;org/objectweb/;org/bouncycastle/;com/google/");
        if(!config.containsKey("ignore"))
            config.put("ignore", ".");
        if(!config.containsKey("classConstantCalls"))
            config.put("classConstantCalls", 
                    "codechicken/lib/asm/ObfMapping.<init>(Ljava/lang/String;)V," +
                    "codechicken/lib/asm/ObfMapping.subclass(Ljava/lang/String;)Lcodechicken/lib/asm/ObfMapping;," +
                    "codechicken/lib/asm/ObfMapping.<init>(Lcodechicken/lib/asm/ObfMapping;Ljava/lang/String;)V");
        if(!config.containsKey("descConstantCalls"))
            config.put("descConstantCalls", 
                    "codechicken/lib/asm/ObfMapping.<init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V," +
                    "org/objectweb/asm/MethodVisitor.visitFieldInsn(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V," +
                    "org/objectweb/asm/tree/MethodNode.visitFieldInsn(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V," +
                    "org/objectweb/asm/MethodVisitor.visitMethodInsn(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V," +
                    "org/objectweb/asm/tree/MethodNode.visitMethodInsn(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V," +
                    "org/objectweb/asm/tree/MethodInsnNode.<init>(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V," +
                    "org/objectweb/asm/tree/FieldInsnNode.<init>(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
        
        return config;
    }

    public static void processLines(File file, Function<String, Void> function)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null)
                function.apply(line);
            reader.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void processFiles(File dir, Function<File, Void> function, boolean recursive)
    {
        for(File file : dir.listFiles())
            if(file.isDirectory() && recursive)
                processFiles(file, function, recursive);
            else
                function.apply(file);
    }
    
    public static void deleteDir(File directory, boolean remove)
    {
        if(!directory.exists())
        {
            if(!remove)directory.mkdirs();
            return;
        }
        for(File file : directory.listFiles())
        {
            if(file.isDirectory())
            {
                deleteDir(file, true);
            }
            else
            {
                if(!file.delete())
                    throw new RuntimeException("Delete Failed: "+file);
            }
        }
        if(remove)
        {
            if(!directory.delete())
                throw new RuntimeException("Delete Failed: "+directory);
        }
    }
    
    public long startTime()
    {
        return startTime;
    }

    public void remap(ClassNode cnode, ClassVisitor cv)
    {
        cstMappper.transform(cnode);
        cnode.accept(new RemappingClassAdapter(cv, obfMapper));
    }

    public static List<String> getParents(ClassNode cnode)
    {
        List<String> parents = new LinkedList<String>();
        if(cnode.superName != null)
            parents.add(cnode.superName);
        
        for(String s_interface : cnode.interfaces)
            parents.add(s_interface);
        
        return parents;
    }

    public void finish(boolean errored)
    {
        long millis = System.currentTimeMillis()-startTime;
        out().println((errored ? "Errored after" : "Done in ")+new DecimalFormat("0.00").format(millis/1000D)+"s");
        finished = true;
    }
    
    public boolean finished()
    {
        return finished;
    }

    public void parseMappings()
    {
        obf.parseMappings(confDir);
    }
}
