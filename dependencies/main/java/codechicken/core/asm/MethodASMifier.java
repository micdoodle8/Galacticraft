package codechicken.core.asm;

import java.io.File;
import java.io.PrintWriter;

import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.TraceMethodVisitor;

import codechicken.core.launch.CodeChickenCorePlugin;
import codechicken.lib.asm.ObfMapping;

import static org.objectweb.asm.Opcodes.*;

public class MethodASMifier extends ClassVisitor
{
    PrintWriter printWriter;
    ObfMapping method;
    Printer asmifier;

    public MethodASMifier(ObfMapping method, Printer printer, PrintWriter printWriter) {
        super(ASM4);
        this.method = method;
        this.printWriter = printWriter;
        asmifier = printer;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (method.matches(name, desc)) {
            Printer localPrinter = asmifier.visitMethod(access, name, desc, signature, exceptions);
            return new TraceMethodVisitor(null, localPrinter);
        }

        return null;
    }

    @Override
    public void visitEnd() {
        asmifier.visitClassEnd();
        asmifier.print(printWriter);
        super.visitEnd();
    }

    public static void printMethod(ObfMapping method, Printer printer, File toFile) {
        try {
            printMethod(method, Launch.classLoader.getClassBytes(method.javaClass()), printer, toFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printMethod(ObfMapping method, byte[] bytes, Printer printer, File toFile) {
        try {
            if (!toFile.getParentFile().exists())
                toFile.getParentFile().mkdirs();
            if (!toFile.exists())
                toFile.createNewFile();

            PrintWriter printWriter = new PrintWriter(toFile);

            ClassVisitor cv = new MethodASMifier(method, printer, printWriter);
            ClassReader cr = new ClassReader(bytes);
            cr.accept(cv, 0);

            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
