package api.player.model;

import org.objectweb.asm.*;

public final class ModelPlayerConstructorVisitor extends MethodVisitor
{
	private final boolean isObfuscated;

	public ModelPlayerConstructorVisitor(MethodVisitor paramMethodVisitor, boolean isObfuscated)
	{
		super(262144, paramMethodVisitor);
		this.isObfuscated = isObfuscated;
	}

	public void visitMethodInsn(int opcode, String owner, String name, String desc)
	{
		if(isObfuscated && name.equals("<init>") && owner.equals("net/minecraft/client/model/ModelBiped"))
			owner = "bhm";
		super.visitMethodInsn(opcode, owner, name, desc);
		if(name.equals("<init>") && owner.equals(isObfuscated ? "bhm" : "net/minecraft/client/model/ModelBiped"))
		{
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitVarInsn(Opcodes.ALOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "create", "(Lapi/player/model/IModelPlayerAPI;FLjava/lang/String;)Lapi/player/model/ModelPlayerAPI;");
			mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayer", "modelPlayerAPI", "Lapi/player/model/ModelPlayerAPI;");

			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitIntInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "beforeLocalConstructing", "(Lapi/player/model/IModelPlayerAPI;F)V");
		}
	}

	public void visitInsn(int opcode)
	{
		if(opcode == Opcodes.RETURN)
		{
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitIntInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "afterLocalConstructing", "(Lapi/player/model/IModelPlayerAPI;F)V");
		}
		super.visitInsn(opcode);
	}
}
