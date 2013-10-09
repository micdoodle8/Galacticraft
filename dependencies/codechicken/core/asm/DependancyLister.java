package codechicken.core.asm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DependancyLister extends ClassVisitor
{
    private static Pattern classdesc = Pattern.compile("L(.+?);");
    
    private class DependancyMethodLister extends MethodVisitor
    {
        public DependancyMethodLister(int api)
        {
            super(api);
        }
        
        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc)
        {
            dependDesc(desc);
        }
        
        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
        {
            dependDesc(desc);
        }
        
        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc)
        {
            depend(owner);
            dependDesc(desc);
        }
    }
    
    private HashSet<String> dependancies = new HashSet<String>();

    public DependancyLister(int api)
    {
        super(api);
    }
    
    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
    {
        dependDesc(desc);
        return null;
    }
    
    private void dependDesc(String desc)
    {
        Matcher match = classdesc.matcher(desc);
        while(match.find())
        {
            String s = match.group();
            depend(s.substring(1, s.length()-1));
        }
    }

    private void depend(String classname)
    {
        dependancies.add(classname);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
    {
        dependDesc(desc);
        return new DependancyMethodLister(Opcodes.ASM4);
    }
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
    {
        depend(superName);
        if(interfaces != null)
            for(String interfacename : interfaces)
                depend(interfacename);
    }
    
    public List<String> getDependancies()
    {
        return new ArrayList<String>(dependancies);
    }
}
