package codechicken.lib.asm;

import java.io.IOException;

import net.minecraft.launchwrapper.LaunchClassLoader;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import com.google.common.base.Objects;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ObfMapping
{
    /**
     * CCC will deal with this.
     */
    public static Remapper runtimeMapper = FMLDeobfuscatingRemapper.INSTANCE;
    public static Remapper mcpMapper = null;
    
    public static final boolean obfuscated;
    
    static
    {
        boolean obf = true;
        try
        {
            obf = ((LaunchClassLoader)ObfMapping.class.getClassLoader()).getClassBytes("net.minecraft.world.World") == null;
        }
        catch(IOException iox) {}
        obfuscated = obf;
    }
    
    public String s_owner;
    public String s_name;
    public String s_desc;
    
    public boolean runtime;
    
    public ObfMapping(String owner)
    {
        this(owner, "", "");
    }
    
    public ObfMapping(String owner, String name, String desc)
    {
        this.s_owner = owner;
        this.s_name = name;
        this.s_desc = desc;

        if(s_owner.contains("."))
            throw new IllegalArgumentException(s_owner);
        
        if(mcpMapper != null)
            map(mcpMapper);
    }
    
    public ObfMapping(ObfMapping descmap, String subclass)
    {
        this(subclass, descmap.s_name, descmap.s_desc);
    }

    public static ObfMapping fromDesc(String s)
    {
        int lastDot = s.lastIndexOf('.');
        if(lastDot < 0)
            return new ObfMapping(s, "", "");
        int sep = s.indexOf('(');//methods
        int sep_end = sep;
        if(sep < 0) {
            sep = s.indexOf(' ');//some stuffs
            sep_end = sep+1;
        }
        if(sep < 0) {
            sep = s.indexOf(':');//fields
            sep_end = sep+1;
        }
        if(sep < 0)
            return new ObfMapping(s.substring(0, lastDot), s.substring(lastDot + 1), "");

        return new ObfMapping(s.substring(0, lastDot), s.substring(lastDot+1, sep), s.substring(sep_end));
    }
            
    public ObfMapping subclass(String subclass)
    {
        return new ObfMapping(this, subclass);
    }

    public boolean matches(MethodNode node)
    {
        return s_name.equals(node.name) && s_desc.equals(node.desc);
    }
    
    public boolean matches(MethodInsnNode node)
    {
        return s_owner.equals(node.owner) && s_name.equals(node.name) && s_desc.equals(node.desc);
    }

    public AbstractInsnNode toInsn(int opcode)
    {
        if(isClass())
            return new TypeInsnNode(opcode, s_owner);
        else if(isMethod())
            return new MethodInsnNode(opcode, s_owner, s_name, s_desc);
        else
            return new FieldInsnNode(opcode, s_owner, s_name, s_desc);
    }
    
    public void visitTypeInsn(MethodVisitor mv, int opcode)
    {
        mv.visitTypeInsn(opcode, s_owner);
    }
    
    public void visitMethodInsn(MethodVisitor mv, int opcode)
    {
        mv.visitMethodInsn(opcode, s_owner, s_name, s_desc);
    }

    public void visitFieldInsn(MethodVisitor mv, int opcode)
    {
        mv.visitFieldInsn(opcode, s_owner, s_name, s_desc);
    }

    public boolean isClass(String name)
    {
        return name.replace('.', '/').equals(s_owner);
    }

    public boolean matches(String name, String desc)
    {
        return s_name.equals(name) && s_desc.equals(desc);
    }

    public boolean matches(FieldNode node)
    {
        return s_name.equals(node.name) && s_desc.equals(node.desc);
    }
    
    public boolean matches(FieldInsnNode node)
    {
        return s_owner.equals(node.owner) && s_name.equals(node.name) && s_desc.equals(node.desc);
    }

    public String javaClass()
    {
        return s_owner.replace('/', '.');
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof ObfMapping))
            return false;
        
        ObfMapping desc = (ObfMapping)obj;
        return s_owner.equals(desc.s_owner) && s_name.equals(desc.s_name) && s_desc.equals(desc.s_desc);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hashCode(s_desc, s_name, s_owner);
    }
    
    @Override
    public String toString()
    {
        if(s_name.length() == 0)
            return "["+s_owner+"]";
        if(s_desc.length() == 0)
            return "["+s_owner+"."+s_name+"]";
        return "["+(isMethod() ? methodDesc() : fieldDesc())+"]";
    }

    public String methodDesc()
    {
        return s_owner+"."+s_name+s_desc;
    }

    public String fieldDesc()
    {
        return s_owner+"."+s_name+":"+s_desc;
    }
    
    public boolean isClass()
    {
        return s_name.length() == 0;
    }
    
    public boolean isMethod()
    {
        return s_desc.contains("(");
    }
    
    public boolean isField()
    {
        return !isClass() && !isMethod();
    }
    
    public ObfMapping map(Remapper mapper)
    {
        if(isMethod())
            s_name = mapper.mapMethodName(s_owner, s_name, s_desc);
        else if(isField())
            s_name = mapper.mapFieldName(s_owner, s_name, s_desc);
        
        s_owner = mapper.mapType(s_owner);
        
        if(isMethod())
            s_desc = mapper.mapMethodDesc(s_desc);
        else if(s_desc.length() > 0)
            s_desc = mapper.mapDesc(s_desc);
        
        return this;
    }
    
    public ObfMapping toRuntime()
    {
        if(!runtime)
            map(runtimeMapper);
        
        runtime = true;
        return this;
    }
    
    public ObfMapping copy()
    {
        return new ObfMapping(s_owner, s_name, s_desc);
    }
}
