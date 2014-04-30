package codechicken.lib.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.objectweb.asm.tree.AbstractInsnNode.*;

/**
 * A section of an InsnList, may become invalid if the insn list is modified
 */
public class InsnListSection implements Iterable<AbstractInsnNode>
{
    private class InsnListSectionIterator implements Iterator<AbstractInsnNode>
    {
        int i = 0;

        @Override
        public boolean hasNext() {
            return i < size();
        }

        @Override
        public AbstractInsnNode next() {
            return get(i++);
        }

        @Override
        public void remove() {
            InsnListSection.this.remove(--i);
        }
    }

    public InsnList list;
    public int start;
    public int end;

    public InsnListSection(InsnList list, int start, int end) {
        this.list = list;
        this.start = start;
        this.end = end;
    }

    public InsnListSection(InsnList list, AbstractInsnNode first, AbstractInsnNode last) {
        this(list, list.indexOf(first), list.indexOf(last)+1);
    }

    public InsnListSection(InsnList list) {
        this(list, 0, list.size());
    }

    public InsnListSection() {
        this(new InsnList());
    }

    public void accept(MethodVisitor mv) {
        for(AbstractInsnNode insn : this)
            insn.accept(mv);
    }

    public AbstractInsnNode getFirst() {
        return size() == 0 ? null : list.get(start);
    }

    public AbstractInsnNode getLast() {
        return size() == 0 ? null : list.get(end - 1);
    }

    public int size() {
        return end - start;
    }

    public AbstractInsnNode get(int i) {
        return list.get(start + i);
    }

    public void set(int i, AbstractInsnNode insn) {
        list.set(get(i), insn);
    }

    public void remove(int i) {
        list.remove(get(i));
        end--;
    }

    public void replace(AbstractInsnNode location, AbstractInsnNode insn) {
        list.set(location, insn);
    }

    public void add(AbstractInsnNode insn) {
        list.add(insn);
        end++;
    }

    public void insertBefore(InsnList insns) {
        int s = insns.size();
        if(this.list.size() == 0)
            list.insert(insns);
        else
            list.insertBefore(list.get(start), insns);
        start+=s;
        end+=s;
    }

    public void insert(InsnList insns) {
        if(end == 0)
            list.insert(insns);
        else
            list.insert(list.get(end-1), insns);
    }

    public void replace(InsnList insns) {
        int s = insns.size();
        remove();
        insert(insns);
        end = start+s;
    }

    public void remove() {
        while(end != start)
            remove(0);
    }

    public void setLast(AbstractInsnNode last) {
        end = list.indexOf(last)+1;
    }

    public void setFirst(AbstractInsnNode first) {
        start = list.indexOf(first);
    }

    public InsnListSection drop(int n) {
        return slice(n, size());
    }

    public InsnListSection take(int n) {
        return slice(0, n);
    }

    public InsnListSection slice(int start, int end) {
        return new InsnListSection(list, this.start+start, this.start+end);
    }

    /**
     * Removes leading and trailing labels and line number nodes that don't affect control flow
     * @return this
     */
    public InsnListSection trim(Set<LabelNode> controlFlowLabels) {
        while(start < end && !InsnComparator.insnImportant(getFirst(), controlFlowLabels))
            start++;

        while(start < end && !InsnComparator.insnImportant(getLast(), controlFlowLabels))
            end--;

        return this;
    }

    public String toString() {
        Textifier t = new Textifier();
        accept(new TraceMethodVisitor(t));
        StringWriter sw = new StringWriter();
        t.print(new PrintWriter(sw));
        return sw.toString();
    }

    public void println() {
        System.out.println(toString());
    }

    public HashMap<LabelNode,LabelNode> identityLabelMap() {
        HashMap<LabelNode, LabelNode> labelMap = new HashMap<LabelNode, LabelNode>();
        for (AbstractInsnNode insn : this)
            switch(insn.getType()) {
                case LABEL:
                    labelMap.put((LabelNode) insn, (LabelNode) insn);
                    break;
                case JUMP_INSN:
                    labelMap.put(((JumpInsnNode)insn).label, ((JumpInsnNode)insn).label);
                    break;
                case LOOKUPSWITCH_INSN:
                    LookupSwitchInsnNode linsn = (LookupSwitchInsnNode)insn;
                    labelMap.put(linsn.dflt, linsn.dflt);
                    for(LabelNode label : linsn.labels)
                        labelMap.put(label, label);
                    break;
                case TABLESWITCH_INSN:
                    TableSwitchInsnNode tinsn = (TableSwitchInsnNode)insn;
                    labelMap.put(tinsn.dflt, tinsn.dflt);
                    for(LabelNode label : tinsn.labels)
                        labelMap.put(label, label);
                    break;
                case FRAME:
                    FrameNode fnode = (FrameNode)insn;
                    if(fnode.local != null)
                        for(Object o : fnode.local)
                            if(o instanceof LabelNode)
                                labelMap.put((LabelNode) o, (LabelNode) o);
                    if(fnode.stack != null)
                        for(Object o : fnode.stack)
                            if(o instanceof LabelNode)
                                labelMap.put((LabelNode) o, (LabelNode) o);
                    break;
            }

        return labelMap;
    }

    public Map<LabelNode, LabelNode> cloneLabels() {
        Map<LabelNode, LabelNode> labelMap = identityLabelMap();
        for(Entry<LabelNode, LabelNode> entry : labelMap.entrySet())
            entry.setValue(new LabelNode());

        return labelMap;
    }

    public InsnListSection copy() {
        return copy(cloneLabels());
    }

    public InsnListSection copy(Map<LabelNode, LabelNode> labelMap) {
        InsnListSection copy = new InsnListSection();
        for(AbstractInsnNode insn : this)
            copy.add(insn.clone(labelMap));

        return copy;
    }

    @Override
    public Iterator<AbstractInsnNode> iterator() {
        return new InsnListSectionIterator();
    }
}