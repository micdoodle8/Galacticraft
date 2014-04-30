package codechicken.lib.asm;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;

import java.util.*;
import java.util.Map.Entry;

import static org.objectweb.asm.tree.AbstractInsnNode.*;

public class ASMBlock
{
    public InsnListSection list;
    private BiMap<String, LabelNode> labels;

    public ASMBlock(InsnListSection list, BiMap<String, LabelNode> labels) {
        this.list = list;
        this.labels = labels;
    }

    public ASMBlock(InsnListSection list) {
        this(list, HashBiMap.<String, LabelNode>create());
    }

    public ASMBlock(InsnList list) {
        this(new InsnListSection(list));
    }

    public ASMBlock() {
        this(new InsnListSection());
    }

    public LabelNode getOrAdd(String s) {
        LabelNode l = get(s);
        if (l == null)
            labels.put(s, l = new LabelNode());
        return l;
    }

    public LabelNode get(String s) {
        return labels.get(s);
    }

    public void replaceLabels(Map<LabelNode, LabelNode> labelMap, Set<LabelNode> usedLabels) {
        for (AbstractInsnNode insn : list)
            switch (insn.getType()) {
                case LABEL:
                    AbstractInsnNode insn2 = insn.clone(labelMap);
                    if (insn2 == insn)//identity mapping
                        continue;
                    if (usedLabels.contains(insn2))
                        throw new IllegalStateException("LabelNode cannot be a part of two InsnLists");
                    list.replace(insn, insn2);
                    break;
                case JUMP_INSN:
                case FRAME:
                case LOOKUPSWITCH_INSN:
                case TABLESWITCH_INSN:
                    list.replace(insn, insn.clone(labelMap));
            }

        for(Entry<LabelNode, LabelNode> entry : labelMap.entrySet()) {
            String key = labels.inverse().get(entry.getKey());
            if(key != null)
                labels.put(key, entry.getValue());
        }
    }

    public void replaceLabels(Map<LabelNode, LabelNode> labelMap) {
        replaceLabels(labelMap, Collections.EMPTY_SET);
    }

    public void replaceLabel(String s, LabelNode l) {
        LabelNode old = get(s);
        if (old != null)
            replaceLabels(ImmutableMap.of(old, l));
    }

    /**
     * Pulls all common labels from other into this
     * @return this
     */
    public ASMBlock mergeLabels(ASMBlock other) {
        if(labels.isEmpty() || other.labels.isEmpty())
            return this;

        //common labels, give them our nodes
        HashMap<LabelNode, LabelNode> labelMap = list.identityLabelMap();
        for(Entry<String, LabelNode> entry : other.labels.entrySet()) {
            LabelNode old = labels.get(entry.getKey());
            if(old != null)
                labelMap.put(old, entry.getValue());
        }
        HashSet<LabelNode> usedLabels = new HashSet<LabelNode>();
        for (AbstractInsnNode insn = other.list.list.getFirst(); insn != null; insn = insn.getNext())
            if(insn.getType() == LABEL)
                usedLabels.add((LabelNode) insn);

        replaceLabels(labelMap, usedLabels);
        return this;
    }

    /**
     * Like mergeLabels but pulls insns from other list into this so LabelNodes can be transferred
     * @return this
     */
    public ASMBlock pullLabels(ASMBlock other) {
        other.list.remove();
        return mergeLabels(other);
    }

    public ASMBlock copy() {
        BiMap<String, LabelNode> labels = HashBiMap.create();
        Map<LabelNode, LabelNode> labelMap = list.cloneLabels();

        for(Entry<String, LabelNode> entry : this.labels.entrySet())
            labels.put(entry.getKey(), labelMap.get(entry.getValue()));

        return new ASMBlock(list.copy(labelMap), labels);
    }

    public ASMBlock applyLabels(InsnListSection list2) {
        if(labels.isEmpty())
            return new ASMBlock(list2);

        Set<LabelNode> cFlowLabels1 = labels.values();
        Set<LabelNode> cFlowLabels2 = InsnComparator.getControlFlowLabels(list2);
        ASMBlock block = new ASMBlock(list2);

        HashMap<LabelNode, LabelNode> labelMap = new HashMap<LabelNode, LabelNode>();

        for(int i = 0, k = 0; i < list.size() && k < list2.size(); ) {
            AbstractInsnNode insn1 = list.get(i);
            if(!InsnComparator.insnImportant(insn1, cFlowLabels1)) {
                i++;
                continue;
            }

            AbstractInsnNode insn2 = list2.get(k);
            if(!InsnComparator.insnImportant(insn2, cFlowLabels2)) {
                k++;
                continue;
            }

            if(insn1.getOpcode() != insn2.getOpcode())
                throw new IllegalArgumentException("Lists do not match:\n"+list+"\n\n"+list2);

            switch(insn1.getType()) {
                case LABEL:
                    labelMap.put((LabelNode) insn1, (LabelNode) insn2);
                    break;
                case JUMP_INSN:
                    labelMap.put(((JumpInsnNode) insn1).label, ((JumpInsnNode) insn2).label);
                    break;
            }
            i++; k++;
        }

        for(Entry<String, LabelNode> entry : labels.entrySet())
            block.labels.put(entry.getKey(), labelMap.get(entry.getValue()));

        return block;
    }

    public InsnList rawListCopy() {
        return list.copy().list;
    }
}