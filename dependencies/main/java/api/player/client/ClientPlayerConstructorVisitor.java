// ==================================================================
// This file is part of Player API.
//
// Player API is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// Player API is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License and the GNU General Public License along with Player API.
// If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

package api.player.client;

import org.objectweb.asm.*;

public final class ClientPlayerConstructorVisitor extends MethodVisitor
{
	private final boolean isObfuscated;

	public ClientPlayerConstructorVisitor(MethodVisitor paramMethodVisitor, boolean isObfuscated)
	{
		super(262144, paramMethodVisitor);
		this.isObfuscated = isObfuscated;
	}

	public void visitMethodInsn(int opcode, String owner, String name, String desc)
	{
		super.visitMethodInsn(opcode, owner, name, desc);
		if(name.equals("<init>") && owner.equals(isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer"))
		{
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "create", "(Lapi/player/client/IClientPlayerAPI;)Lapi/player/client/ClientPlayerAPI;");
			mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", "clientPlayerAPI", "Lapi/player/client/ClientPlayerAPI;");

			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitIntInsn(Opcodes.ALOAD, 1);
			mv.visitIntInsn(Opcodes.ALOAD, 2);
			mv.visitIntInsn(Opcodes.ALOAD, 3);
			mv.visitIntInsn(Opcodes.ILOAD, 4);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "beforeLocalConstructing", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/client/Minecraft;Lnet/minecraft/world/World;Lnet/minecraft/util/Session;I)V");
		}
	}

	public void visitInsn(int opcode)
	{
		if(opcode == Opcodes.RETURN)
		{
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitIntInsn(Opcodes.ALOAD, 1);
			mv.visitIntInsn(Opcodes.ALOAD, 2);
			mv.visitIntInsn(Opcodes.ALOAD, 3);
			mv.visitIntInsn(Opcodes.ILOAD, 4);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "afterLocalConstructing", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/client/Minecraft;Lnet/minecraft/world/World;Lnet/minecraft/util/Session;I)V");
		}
		super.visitInsn(opcode);
	}
}
