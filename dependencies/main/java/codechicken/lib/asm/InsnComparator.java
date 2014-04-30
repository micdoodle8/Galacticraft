package codechicken.lib.asm;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.objectweb.asm.tree.*;

import java.util.*;

import static org.objectweb.asm.tree.AbstractInsnNode.*;

public class InsnComparator
{
    public static boolean varInsnEqual(VarInsnNode insn1, VarInsnNode insn2) {
        return insn1.var == -1 || insn2.var == -1 || insn1.var == insn2.var;
    }

    public static boolean methodInsnEqual(MethodInsnNode insn1, MethodInsnNode insn2) {
        return insn1.owner.equals(insn2.owner) && insn1.name.equals(insn2.name) && insn1.desc.equals(insn2.desc);
    }

    public static boolean fieldInsnEqual(FieldInsnNode insn1, FieldInsnNode insn2) {
        return insn1.owner.equals(insn2.owner) && insn1.name.equals(insn2.name) && insn1.desc.equals(insn2.desc);
    }

    public static boolean ldcInsnEqual(LdcInsnNode insn1, LdcInsnNode insn2) {
        return insn1.cst == null || insn2.cst == null || insn1.cst.equals(insn2.cst);
    }

    public static boolean typeInsnEqual(TypeInsnNode insn1, TypeInsnNode insn2) {
        return insn1.desc.equals("*") || insn2.desc.equals("*") || insn1.desc.equals(insn2.desc);
    }

    public static boolean iincInsnEqual(IincInsnNode node1, IincInsnNode node2) {
        return node1.var == node2.var && node1.incr == node2.incr;
    }

    public static boolean intInsnEqual(IntInsnNode node1, IntInsnNode node2) {
        return node1.operand == -1 || node2.operand == -1 || node1.operand == node2.operand;
    }

    public static boolean insnEqual(AbstractInsnNode node1, AbstractInsnNode node2) {
        if (node1.getOpcode() != node2.getOpcode())
            return false;

        switch (node2.getType()) {
            case VAR_INSN:
                return varInsnEqual((VarInsnNode) node1, (VarInsnNode) node2);
            case TYPE_INSN:
                return typeInsnEqual((TypeInsnNode) node1, (TypeInsnNode) node2);
            case FIELD_INSN:
                return fieldInsnEqual((FieldInsnNode) node1, (FieldInsnNode) node2);
            case METHOD_INSN:
                return methodInsnEqual((MethodInsnNode) node1, (MethodInsnNode) node2);
            case LDC_INSN:
                return ldcInsnEqual((LdcInsnNode) node1, (LdcInsnNode) node2);
            case IINC_INSN:
                return iincInsnEqual((IincInsnNode) node1, (IincInsnNode) node2);
            case INT_INSN:
                return intInsnEqual((IntInsnNode) node1, (IntInsnNode) node2);
            default:
                return true;
        }
    }

    public static boolean insnImportant(AbstractInsnNode insn, Set<LabelNode> controlFlowLabels) {
        switch(insn.getType()) {
            case LINE:
            case FRAME:
                return false;
            case LABEL:
                return controlFlowLabels.contains(insn);
            default:
                return true;
        }
    }

    public static Set<LabelNode> getControlFlowLabels(InsnListSection list) {
        return getControlFlowLabels(list.list);
    }

    public static Set<LabelNode> getControlFlowLabels(InsnList list) {
        HashSet<LabelNode> controlFlowLabels = new HashSet<LabelNode>();
        for (AbstractInsnNode insn = list.getFirst(); insn != null; insn = insn.getNext()) {
            switch (insn.getType()) {
                case JUMP_INSN:
                    JumpInsnNode jinsn = (JumpInsnNode) insn;
                    controlFlowLabels.add(jinsn.label);
                    break;
                case TABLESWITCH_INSN:
                    TableSwitchInsnNode tsinsn = (TableSwitchInsnNode) insn;
                    controlFlowLabels.add(tsinsn.dflt);
                    for (LabelNode label : tsinsn.labels)
                        controlFlowLabels.add(label);
                    break;
                case LOOKUPSWITCH_INSN:
                    LookupSwitchInsnNode lsinsn = (LookupSwitchInsnNode) insn;
                    controlFlowLabels.add(lsinsn.dflt);
                    for (LabelNode label : lsinsn.labels)
                        controlFlowLabels.add(label);
                    break;
            }
        }
        return controlFlowLabels;
    }

    public static InsnList getImportantList(InsnList list) {
        return getImportantList(new InsnListSection(list)).list;
    }

    public static InsnListSection getImportantList(InsnListSection list) {
        if (list.size() == 0)
            return list;

        Set<LabelNode> controlFlowLabels = getControlFlowLabels(list);
        Map<LabelNode, LabelNode> labelMap = Maps.asMap(controlFlowLabels, new Function<LabelNode, LabelNode>()
        {
            @Override
            public LabelNode apply(LabelNode input) {
                return input;
            }
        });

        InsnListSection importantNodeList = new InsnListSection();
        for(AbstractInsnNode insn : list)
            if (insnImportant(insn, controlFlowLabels))
                importantNodeList.add(insn.clone(labelMap));

        return importantNodeList;
    }

    public static List<InsnListSection> find(InsnListSection haystack, InsnListSection needle) {
        Set<LabelNode> controlFlowLabels = getControlFlowLabels(haystack);
        LinkedList<InsnListSection> list = new LinkedList<InsnListSection>();
        for (int start = 0; start <= haystack.size() - needle.size(); start++) {
            InsnListSection section = matches(haystack.drop(start), needle, controlFlowLabels);
            if (section != null) {
                list.add(section);
                start = section.end-1;
            }
        }

        return list;
    }

    public static List<InsnListSection> find(InsnList haystack, InsnListSection needle) {
        return find(new InsnListSection(haystack), needle);
    }

    public static InsnListSection matches(InsnListSection haystack, InsnListSection needle, Set<LabelNode> controlFlowLabels) {
        int h = 0, n = 0;
        for (; h < haystack.size() && n < needle.size(); h++) {
            AbstractInsnNode insn = haystack.get(h);
            if(!insnImportant(insn, controlFlowLabels))
                continue;

            if (!insnEqual(haystack.get(h), needle.get(n)))
                return null;
            n++;
        }
        if (n != needle.size())
            return null;

        return haystack.take(h);
    }

    public static InsnListSection findOnce(InsnListSection haystack, InsnListSection needle) {
        List<InsnListSection> list = find(haystack, needle);
        if(list.size() != 1)
            throw new RuntimeException("Needle found " + list.size() + " times in Haystack:\n" + haystack + "\n\n" + needle);

        return list.get(0);
    }

    public static InsnListSection findOnce(InsnList haystack, InsnListSection needle) {
        return findOnce(new InsnListSection(haystack), needle);
    }

    public static List<InsnListSection> findN(InsnListSection haystack, InsnListSection needle) {
        List<InsnListSection> list = find(haystack, needle);
        if(list.isEmpty())
            throw new RuntimeException("Needle not found in Haystack:\n" + haystack + "\n\n" + needle);

        return list;
    }

    public static List<InsnListSection> findN(InsnList haystack, InsnListSection needle) {
        return findN(new InsnListSection(haystack), needle);
    }
}
