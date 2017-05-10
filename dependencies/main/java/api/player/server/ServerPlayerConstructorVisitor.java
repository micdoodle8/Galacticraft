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

package api.player.server;

import org.objectweb.asm.*;

public final class ServerPlayerConstructorVisitor extends MethodVisitor
{
	private final boolean isObfuscated;

	public ServerPlayerConstructorVisitor(MethodVisitor paramMethodVisitor, boolean isObfuscated)
	{
		super(262144, paramMethodVisitor);
		this.isObfuscated = isObfuscated;
	}

	public void visitMethodInsn(int opcode, String owner, String name, String desc)
	{
		super.visitMethodInsn(opcode, owner, name, desc);
		if(name.equals("<init>") && owner.equals(isObfuscated ? "yz" : "net/minecraft/entity/player/EntityPlayer"))
		{
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/server/ServerPlayerAPI", "create", "(Lapi/player/server/IServerPlayerAPI;)Lapi/player/server/ServerPlayerAPI;");
			mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "mw" : "net/minecraft/entity/player/EntityPlayerMP", "serverPlayerAPI", "Lapi/player/server/ServerPlayerAPI;");

			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitIntInsn(Opcodes.ALOAD, 1);
			mv.visitIntInsn(Opcodes.ALOAD, 2);
			mv.visitIntInsn(Opcodes.ALOAD, 3);
			mv.visitIntInsn(Opcodes.ALOAD, 4);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/server/ServerPlayerAPI", "beforeLocalConstructing", "(Lapi/player/server/IServerPlayerAPI;Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/WorldServer;Lcom/mojang/authlib/GameProfile;Lnet/minecraft/server/management/ItemInWorldManager;)V");
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
			mv.visitIntInsn(Opcodes.ALOAD, 4);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/server/ServerPlayerAPI", "afterLocalConstructing", "(Lapi/player/server/IServerPlayerAPI;Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/WorldServer;Lcom/mojang/authlib/GameProfile;Lnet/minecraft/server/management/ItemInWorldManager;)V");
		}
		super.visitInsn(opcode);
	}
}
