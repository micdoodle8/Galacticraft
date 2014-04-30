package codechicken.lib.asm;

import org.objectweb.asm.tree.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.tree.AbstractInsnNode.*;

public class ASMReader
{
    public static Map<String, Integer> opCodes = new HashMap<String, Integer>();
    public static byte[] TYPE;

    static {
        opCodes.put("NOP", NOP);
        opCodes.put("ACONST_NULL", ACONST_NULL);
        opCodes.put("ICONST_M1", ICONST_M1);
        opCodes.put("ICONST_0", ICONST_0);
        opCodes.put("ICONST_1", ICONST_1);
        opCodes.put("ICONST_2", ICONST_2);
        opCodes.put("ICONST_3", ICONST_3);
        opCodes.put("ICONST_4", ICONST_4);
        opCodes.put("ICONST_5", ICONST_5);
        opCodes.put("LCONST_0", LCONST_0);
        opCodes.put("LCONST_1", LCONST_1);
        opCodes.put("FCONST_0", FCONST_0);
        opCodes.put("FCONST_1", FCONST_1);
        opCodes.put("FCONST_2", FCONST_2);
        opCodes.put("DCONST_0", DCONST_0);
        opCodes.put("DCONST_1", DCONST_1);
        opCodes.put("BIPUSH", BIPUSH);
        opCodes.put("SIPUSH", SIPUSH);
        opCodes.put("LDC", LDC);
        opCodes.put("ILOAD", ILOAD);
        opCodes.put("LLOAD", LLOAD);
        opCodes.put("FLOAD", FLOAD);
        opCodes.put("DLOAD", DLOAD);
        opCodes.put("ALOAD", ALOAD);
        opCodes.put("IALOAD", IALOAD);
        opCodes.put("LALOAD", LALOAD);
        opCodes.put("FALOAD", FALOAD);
        opCodes.put("DALOAD", DALOAD);
        opCodes.put("AALOAD", AALOAD);
        opCodes.put("BALOAD", BALOAD);
        opCodes.put("CALOAD", CALOAD);
        opCodes.put("SALOAD", SALOAD);
        opCodes.put("ISTORE", ISTORE);
        opCodes.put("LSTORE", LSTORE);
        opCodes.put("FSTORE", FSTORE);
        opCodes.put("DSTORE", DSTORE);
        opCodes.put("ASTORE", ASTORE);
        opCodes.put("IASTORE", IASTORE);
        opCodes.put("LASTORE", LASTORE);
        opCodes.put("FASTORE", FASTORE);
        opCodes.put("DASTORE", DASTORE);
        opCodes.put("AASTORE", AASTORE);
        opCodes.put("BASTORE", BASTORE);
        opCodes.put("CASTORE", CASTORE);
        opCodes.put("SASTORE", SASTORE);
        opCodes.put("POP", POP);
        opCodes.put("POP2", POP2);
        opCodes.put("DUP", DUP);
        opCodes.put("DUP_X1", DUP_X1);
        opCodes.put("DUP_X2", DUP_X2);
        opCodes.put("DUP2", DUP2);
        opCodes.put("DUP2_X1", DUP2_X1);
        opCodes.put("DUP2_X2", DUP2_X2);
        opCodes.put("SWAP", SWAP);
        opCodes.put("IADD", IADD);
        opCodes.put("LADD", LADD);
        opCodes.put("FADD", FADD);
        opCodes.put("DADD", DADD);
        opCodes.put("ISUB", ISUB);
        opCodes.put("LSUB", LSUB);
        opCodes.put("FSUB", FSUB);
        opCodes.put("DSUB", DSUB);
        opCodes.put("IMUL", IMUL);
        opCodes.put("LMUL", LMUL);
        opCodes.put("FMUL", FMUL);
        opCodes.put("DMUL", DMUL);
        opCodes.put("IDIV", IDIV);
        opCodes.put("LDIV", LDIV);
        opCodes.put("FDIV", FDIV);
        opCodes.put("DDIV", DDIV);
        opCodes.put("IREM", IREM);
        opCodes.put("LREM", LREM);
        opCodes.put("FREM", FREM);
        opCodes.put("DREM", DREM);
        opCodes.put("INEG", INEG);
        opCodes.put("LNEG", LNEG);
        opCodes.put("FNEG", FNEG);
        opCodes.put("DNEG", DNEG);
        opCodes.put("ISHL", ISHL);
        opCodes.put("LSHL", LSHL);
        opCodes.put("ISHR", ISHR);
        opCodes.put("LSHR", LSHR);
        opCodes.put("IUSHR", IUSHR);
        opCodes.put("LUSHR", LUSHR);
        opCodes.put("IAND", IAND);
        opCodes.put("LAND", LAND);
        opCodes.put("IOR", IOR);
        opCodes.put("LOR", LOR);
        opCodes.put("IXOR", IXOR);
        opCodes.put("LXOR", LXOR);
        opCodes.put("IINC", IINC);
        opCodes.put("I2L", I2L);
        opCodes.put("I2F", I2F);
        opCodes.put("I2D", I2D);
        opCodes.put("L2I", L2I);
        opCodes.put("L2F", L2F);
        opCodes.put("L2D", L2D);
        opCodes.put("F2I", F2I);
        opCodes.put("F2L", F2L);
        opCodes.put("F2D", F2D);
        opCodes.put("D2I", D2I);
        opCodes.put("D2L", D2L);
        opCodes.put("D2F", D2F);
        opCodes.put("I2B", I2B);
        opCodes.put("I2C", I2C);
        opCodes.put("I2S", I2S);
        opCodes.put("LCMP", LCMP);
        opCodes.put("FCMPL", FCMPL);
        opCodes.put("FCMPG", FCMPG);
        opCodes.put("DCMPL", DCMPL);
        opCodes.put("DCMPG", DCMPG);
        opCodes.put("IFEQ", IFEQ);
        opCodes.put("IFNE", IFNE);
        opCodes.put("IFLT", IFLT);
        opCodes.put("IFGE", IFGE);
        opCodes.put("IFGT", IFGT);
        opCodes.put("IFLE", IFLE);
        opCodes.put("IF_ICMPEQ", IF_ICMPEQ);
        opCodes.put("IF_ICMPNE", IF_ICMPNE);
        opCodes.put("IF_ICMPLT", IF_ICMPLT);
        opCodes.put("IF_ICMPGE", IF_ICMPGE);
        opCodes.put("IF_ICMPGT", IF_ICMPGT);
        opCodes.put("IF_ICMPLE", IF_ICMPLE);
        opCodes.put("IF_ACMPEQ", IF_ACMPEQ);
        opCodes.put("IF_ACMPNE", IF_ACMPNE);
        opCodes.put("GOTO", GOTO);
        opCodes.put("JSR", JSR);
        opCodes.put("RET", RET);
        opCodes.put("TABLESWITCH", TABLESWITCH);
        opCodes.put("LOOKUPSWITCH", LOOKUPSWITCH);
        opCodes.put("IRETURN", IRETURN);
        opCodes.put("LRETURN", LRETURN);
        opCodes.put("FRETURN", FRETURN);
        opCodes.put("DRETURN", DRETURN);
        opCodes.put("ARETURN", ARETURN);
        opCodes.put("RETURN", RETURN);
        opCodes.put("GETSTATIC", GETSTATIC);
        opCodes.put("PUTSTATIC", PUTSTATIC);
        opCodes.put("GETFIELD", GETFIELD);
        opCodes.put("PUTFIELD", PUTFIELD);
        opCodes.put("INVOKEVIRTUAL", INVOKEVIRTUAL);
        opCodes.put("INVOKESPECIAL", INVOKESPECIAL);
        opCodes.put("INVOKESTATIC", INVOKESTATIC);
        opCodes.put("INVOKEINTERFACE", INVOKEINTERFACE);
        opCodes.put("INVOKEDYNAMIC", INVOKEDYNAMIC);
        opCodes.put("NEW", NEW);
        opCodes.put("NEWARRAY", NEWARRAY);
        opCodes.put("ANEWARRAY", ANEWARRAY);
        opCodes.put("ARRAYLENGTH", ARRAYLENGTH);
        opCodes.put("ATHROW", ATHROW);
        opCodes.put("CHECKCAST", CHECKCAST);
        opCodes.put("INSTANCEOF", INSTANCEOF);
        opCodes.put("MONITORENTER", MONITORENTER);
        opCodes.put("MONITOREXIT", MONITOREXIT);
        opCodes.put("MULTIANEWARRAY", MULTIANEWARRAY);
        opCodes.put("IFNULL", IFNULL);
        opCodes.put("IFNONNULL", IFNONNULL);

        //derived from classWriter, mapped to AbstractInsnNode
        TYPE = new byte[200];
        String s = "AAAAAAAAAAAAAAAABBJ__CCCCC____________________AAAAAAAACC"
                + "CCC____________________AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                + "AAAAAAAAAAAAAAAAAKAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHCLMAA"
                + "AAAAEEEEFFFFGDBDAADDAA_NHH";
        for (int i = 0; i < s.length(); i++)
            TYPE[i] = (byte) (s.charAt(i) - 'A');
    }

    public static Map<String, ASMBlock> loadResource(String res) {
        return loadResource(ASMHelper.class.getResourceAsStream(res), res);
    }

    public static Map<String, ASMBlock> loadResource(InputStream in, String res) {
        HashMap<String, ASMBlock> blocks = new HashMap<String, ASMBlock>();
        String current = "unnamed";
        ASMBlock block = new ASMBlock();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = r.readLine()) != null) {
                {
                    int hpos = line.indexOf('#');
                    if (hpos >= 0) line = line.substring(0, hpos);
                }
                line = line.trim();
                if (line.length() == 0) continue;
                if (line.startsWith("list ")) {
                    if (block.list.size() > 0) blocks.put(current, block);
                    current = line.substring(5);
                    block = new ASMBlock();
                    continue;
                }

                try {
                    AbstractInsnNode insn = null;
                    String[] split = line.replace(" : ", ":").split(" ");
                    Integer i_opcode = opCodes.get(split[0]);
                    if (i_opcode == null) {
                        if (split[0].equals("LINENUMBER"))
                            insn = new LineNumberNode(Integer.parseInt(split[1]), block.getOrAdd(split[2]));
                        else if (split[0].startsWith("L"))
                            insn = block.getOrAdd(split[0]);
                        else
                            throw new Exception("Unknown opcode " + split[0]);
                    } else {
                        int opcode = i_opcode;
                        switch (TYPE[opcode]) {
                            case INSN:
                                insn = new InsnNode(opcode);
                                break;
                            case INT_INSN:
                                insn = new IntInsnNode(opcode, Integer.parseInt(split[1]));
                                break;
                            case VAR_INSN:
                                insn = new VarInsnNode(opcode, Integer.parseInt(split[1]));
                                break;
                            case TYPE_INSN:
                                insn = new ObfMapping(split[1]).toClassloading().toInsn(opcode);
                                break;
                            case FIELD_INSN:
                            case METHOD_INSN:
                                StringBuilder sb = new StringBuilder();
                                for (int i = 1; i < split.length; i++)
                                    sb.append(split[i]);
                                insn = ObfMapping.fromDesc(sb.toString()).toClassloading().toInsn(opcode);
                                break;
                            case INVOKE_DYNAMIC_INSN:
                                throw new Exception("Found INVOKEDYNAMIC while reading");
                            case JUMP_INSN:
                                insn = new JumpInsnNode(opcode, block.getOrAdd(split[1]));
                                break;
                            case LDC_INSN:
                                String cst = split[1];
                                if(cst.equals("*"))
                                    insn = new LdcInsnNode(null);
                                else if (cst.endsWith("\""))
                                    insn = new LdcInsnNode(cst.substring(1, cst.length() - 1));
                                else if (cst.endsWith("L"))
                                    insn = new LdcInsnNode(Long.valueOf(cst.substring(0, cst.length() - 1)));
                                else if (cst.endsWith("F"))
                                    insn = new LdcInsnNode(Float.valueOf(cst.substring(0, cst.length() - 1)));
                                else if (cst.endsWith("D"))
                                    insn = new LdcInsnNode(Double.valueOf(cst.substring(0, cst.length() - 1)));
                                else if (cst.contains("."))
                                    insn = new LdcInsnNode(Double.valueOf(cst));
                                else
                                    insn = new LdcInsnNode(Integer.valueOf(cst));
                                break;
                            case IINC_INSN:
                                insn = new IincInsnNode(opcode, Integer.parseInt(split[1]));
                                break;
                            case LABEL:
                                throw new Exception("Use L# for labels");
                            case TABLESWITCH_INSN:
                            case LOOKUPSWITCH_INSN:
                                throw new Exception("I don't know how to deal with this insn type");
                            case MULTIANEWARRAY_INSN:
                                insn = new MultiANewArrayInsnNode(split[1], Integer.parseInt(split[2]));
                                break;
                            case FRAME:
                                throw new Exception("Use ClassWriter.COMPUTE_FRAMES");
                        }
                    }

                    if (insn != null)
                        block.list.add(insn);
                } catch (Exception e) {
                    System.err.println("Error while reading ASM Block " +
                            current + " from " + res + ", line: " + line);
                    e.printStackTrace();
                }
            }

            r.close();
            if (block.list.size() > 0) blocks.put(current, block);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read ASM resource: " + res, e);
        }
        return blocks;
    }
}
