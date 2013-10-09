package codechicken.obfuscator;

import java.util.LinkedList;
import java.util.List;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import codechicken.lib.asm.ObfMapping;

import static org.objectweb.asm.tree.AbstractInsnNode.*;

public class ConstantObfuscator implements Opcodes
{
    public ObfRemapper obf;
    public List<ObfMapping> descCalls = new LinkedList<ObfMapping>();
    public List<ObfMapping> classCalls = new LinkedList<ObfMapping>();
    
    public ConstantObfuscator(ObfRemapper obf, String[] a_classCalls, String[] a_descCalls)
    {
        this.obf = obf;
        for(String callDesc : a_classCalls)
            classCalls.add(ObfMapping.fromDesc(callDesc));
        
        for(String callDesc : a_descCalls)
            descCalls.add(ObfMapping.fromDesc(callDesc));
    }

    public void transform(ClassNode cnode)
    {
        for(MethodNode method : cnode.methods)
            for(AbstractInsnNode insn = method.instructions.getFirst(); insn != null; insn = insn.getNext())
                obfuscateInsnSeq(insn);
    }
    
    private void obfuscateInsnSeq(AbstractInsnNode insn)
    {
        if(matchesClass(insn))
        {
            LdcInsnNode node1 = (LdcInsnNode) insn;
            node1.cst = obf.map((String) node1.cst);
        }
        if(matchesDesc(insn))
        {
            LdcInsnNode node1 = (LdcInsnNode) insn;
            LdcInsnNode node2 = (LdcInsnNode) node1.getNext();
            LdcInsnNode node3 = (LdcInsnNode) node2.getNext();
            ObfMapping mapping = new ObfMapping((String) node1.cst, (String) node2.cst, (String) node3.cst).map(obf);
            node1.cst = mapping.s_owner;
            node2.cst = mapping.s_name;
            node3.cst = mapping.s_desc;
        }
    }
    
    private boolean matchesClass(AbstractInsnNode insn)
    {
        if(insn.getType() != LDC_INSN) return false;
        insn = insn.getNext();
        if(insn == null || insn.getType() != METHOD_INSN) return false;
        for(ObfMapping m : classCalls)
            if(m.matches((MethodInsnNode) insn))
                return true;
        return false;
    }
    
    private boolean matchesDesc(AbstractInsnNode insn)
    {
        if(insn.getType() != LDC_INSN) return false;
        insn = insn.getNext();
        if(insn == null || insn.getType() != LDC_INSN) return false;
        insn = insn.getNext();
        if(insn == null || insn.getType() != LDC_INSN) return false;
        insn = insn.getNext();
        if(insn == null || insn.getType() != METHOD_INSN) return false;
        for(ObfMapping m : descCalls)
            if(m.matches((MethodInsnNode) insn))
                return true;
        return false;
    }
}
