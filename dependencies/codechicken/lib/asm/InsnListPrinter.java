package codechicken.lib.asm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.Textifier;

import codechicken.lib.asm.InstructionComparator.InsnListSection;

public class InsnListPrinter extends Textifier
{
    private boolean buildingLabelMap = false;
    
    public void visitInsnList(InsnList list)
    {
        text.clear();
        if(labelNames == null)
            labelNames = new HashMap<Label, String>();
        else
            labelNames.clear();
        
        buildingLabelMap = true;
        for(AbstractInsnNode insn = list.getFirst(); insn != null; insn = insn.getNext())
            if(insn.getType() == 8)
                visitLabel(((LabelNode)insn).getLabel());
        
        text.clear();
        buildingLabelMap = false;

        for(AbstractInsnNode insn = list.getFirst(); insn != null; insn = insn.getNext())
            _visitInsn(insn);
    }

    public void visitInsnList(InsnListSection subsection)
    {
        text.clear();
        if(labelNames == null)
            labelNames = new HashMap<Label, String>();
        else
            labelNames.clear();
        
        buildingLabelMap = true;
        {
            AbstractInsnNode insn = subsection.first;
            while(true)
            {
                if(insn.getType() == 8)
                    visitLabel(((LabelNode)insn).getLabel());
                if(insn == subsection.last)
                    break;
                insn = insn.getNext();
            }
        }            
        
        text.clear();
        buildingLabelMap = false;

        {
            AbstractInsnNode insn = subsection.first;
            while(true)
            {
                _visitInsn(insn);
                if(insn == subsection.last)
                    break;
                insn = insn.getNext();
            }
        }
    }

    public void visitInsn(AbstractInsnNode insn)
    {
        text.clear();
        if(labelNames == null)
            labelNames = new HashMap<Label, String>();
        else
            labelNames.clear();
        
        _visitInsn(insn);
    }
    
    private void _visitInsn(AbstractInsnNode insn)
    {
        switch(insn.getType())
        {
            case 0:
                visitInsn(insn.getOpcode());
                break;
            case 1:
                IntInsnNode iinsn = (IntInsnNode)insn;
                visitIntInsn(iinsn.getOpcode(), iinsn.operand);
                break;
            case 2:
                VarInsnNode vinsn = (VarInsnNode)insn;
                visitVarInsn(vinsn.getOpcode(), vinsn.var);
                break;
            case 3:
                TypeInsnNode tinsn = (TypeInsnNode)insn;
                visitTypeInsn(tinsn.getOpcode(), tinsn.desc);
                break;
            case 4:
                FieldInsnNode finsn = (FieldInsnNode)insn;
                visitFieldInsn(finsn.getOpcode(), finsn.owner, finsn.name, finsn.desc);
                break;
            case 5:
                MethodInsnNode minsn = (MethodInsnNode)insn;
                visitMethodInsn(minsn.getOpcode(), minsn.owner, minsn.name, minsn.desc);
                break;
            case 6:
                InvokeDynamicInsnNode idinsn = (InvokeDynamicInsnNode)insn;
                visitInvokeDynamicInsn(idinsn.name, idinsn.desc, idinsn.bsm, idinsn.bsmArgs);
                break;
            case 7:
                JumpInsnNode jinsn = (JumpInsnNode)insn;
                visitJumpInsn(jinsn.getOpcode(), jinsn.label.getLabel());
                break;
            case 8:
                LabelNode linsn = (LabelNode)insn;
                visitLabel(linsn.getLabel());
                break;
            case 9:
                LdcInsnNode ldcinsn = (LdcInsnNode)insn;
                visitLdcInsn(ldcinsn.cst);
                break;
            case 10:
                IincInsnNode iiinsn = (IincInsnNode)insn;
                visitIincInsn(iiinsn.var, iiinsn.incr);
                break;
            case 11:
                TableSwitchInsnNode tsinsn = (TableSwitchInsnNode)insn;
                Label[] tslables = new Label[tsinsn.labels.size()];
                for(int i = 0; i < tslables.length; i++)
                    tslables[i] = tsinsn.labels.get(i).getLabel();              
                visitTableSwitchInsn(tsinsn.min, tsinsn.max, tsinsn.dflt.getLabel(), tslables);
                break;
            case 12:
                LookupSwitchInsnNode lsinsn = (LookupSwitchInsnNode)insn;
                Label[] lslables = new Label[lsinsn.labels.size()];
                for(int i = 0; i < lslables.length; i++)
                    lslables[i] = lsinsn.labels.get(i).getLabel();
                int[] lskeys = new int[lsinsn.keys.size()];
                for(int i = 0; i < lskeys.length; i++)
                    lskeys[i] = lsinsn.keys.get(i);
                visitLookupSwitchInsn(lsinsn.dflt.getLabel(), lskeys, lslables);
                break;
            case 13:
                MultiANewArrayInsnNode ainsn = (MultiANewArrayInsnNode)insn;
                visitMultiANewArrayInsn(ainsn.desc, ainsn.dims);
                break;
            case 14:
                FrameNode fnode = (FrameNode)insn;
                switch(fnode.type)
                {
                    case -1:
                    case 0:
                      visitFrame(fnode.type, fnode.local.size(), fnode.local.toArray(), fnode.stack.size(), fnode.stack.toArray());
                      break;
                    case 1:
                      visitFrame(fnode.type, fnode.local.size(), fnode.local.toArray(), 0, null);
                      break;
                    case 2:
                      visitFrame(fnode.type, fnode.local.size(), null, 0, null);
                      break;
                    case 3:
                      visitFrame(fnode.type, 0, null, 0, null);
                      break;
                    case 4:
                      visitFrame(fnode.type, 0, null, 1, fnode.stack.toArray());
                }
                break;
            case 15:
                LineNumberNode lnode = (LineNumberNode)insn;
                visitLineNumber(lnode.line, lnode.start.getLabel());
                break;
        }
    }
    
    @Override
    public void visitLabel(Label label)
    {
        if(!buildingLabelMap && !labelNames.containsKey(label))
        {
            labelNames.put(label, "LEXT"+labelNames.size());
        }
        super.visitLabel(label);
    }

    public String textString()
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        print(pw);
        pw.flush();
        return sw.toString();
    }
}
