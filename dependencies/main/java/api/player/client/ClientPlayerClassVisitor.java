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

import java.io.*;

import org.objectweb.asm.*;

public final class ClientPlayerClassVisitor extends ClassVisitor
{
	public static final String targetClassName = "net.minecraft.client.entity.EntityPlayerSP";

	private boolean hadLocalAddExhaustion;
	private boolean hadLocalAddMovementStat;
	private boolean hadLocalAddStat;
	private boolean hadLocalAttackEntityFrom;
	private boolean hadLocalAttackTargetEntityWithCurrentItem;
	private boolean hadLocalCanBreatheUnderwater;
	private boolean hadLocalCanHarvestBlock;
	private boolean hadLocalCanPlayerEdit;
	private boolean hadLocalCanTriggerWalking;
	private boolean hadLocalCloseScreen;
	private boolean hadLocalDamageEntity;
	private boolean hadLocalDisplayGui;
	private boolean hadLocalDisplayGUIChest;
	private boolean hadLocalDropItem;
	private boolean hadLocalDropOneItem;
	private boolean hadLocalDropPlayerItemWithRandomChoice;
	private boolean hadLocalFall;
	private boolean hadLocalGetAIMoveSpeed;
	private boolean hadLocalGetBedOrientationInDegrees;
	private boolean hadLocalGetBrightness;
	private boolean hadLocalGetBrightnessForRender;
	private boolean hadLocalGetBreakSpeed;
	private boolean hadLocalGetDistanceSq;
	private boolean hadLocalGetDistanceSqToEntity;
	private boolean hadLocalGetFovModifier;
	private boolean hadLocalGetHurtSound;
	private boolean hadLocalGetName;
	private boolean hadLocalGetSleepTimer;
	private boolean hadLocalHandleWaterMovement;
	private boolean hadLocalHeal;
	private boolean hadLocalIsEntityInsideOpaqueBlock;
	private boolean hadLocalIsInWater;
	private boolean hadLocalIsInsideOfMaterial;
	private boolean hadLocalIsOnLadder;
	private boolean hadLocalIsPlayerSleeping;
	private boolean hadLocalIsSneaking;
	private boolean hadLocalIsSprinting;
	private boolean hadLocalJump;
	private boolean hadLocalKnockBack;
	private boolean hadLocalMoveEntity;
	private boolean hadLocalMoveEntityWithHeading;
	private boolean hadLocalMoveFlying;
	private boolean hadLocalOnDeath;
	private boolean hadLocalOnLivingUpdate;
	private boolean hadLocalOnKillEntity;
	private boolean hadLocalOnStruckByLightning;
	private boolean hadLocalOnUpdate;
	private boolean hadLocalPlayStepSound;
	private boolean hadLocalPushOutOfBlocks;
	private boolean hadLocalRayTrace;
	private boolean hadLocalReadEntityFromNBT;
	private boolean hadLocalRespawnPlayer;
	private boolean hadLocalSetDead;
	private boolean hadLocalSetPlayerSPHealth;
	private boolean hadLocalSetPositionAndRotation;
	private boolean hadLocalSetSneaking;
	private boolean hadLocalSetSprinting;
	private boolean hadLocalTrySleep;
	private boolean hadLocalSwingItem;
	private boolean hadLocalUpdateEntityActionState;
	private boolean hadLocalUpdateRidden;
	private boolean hadLocalWakeUpPlayer;
	private boolean hadLocalWriteEntityToNBT;

	public static byte[] transform(byte[] bytes, boolean isObfuscated)
	{
		try
		{
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			ClassReader cr = new ClassReader(in);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ClientPlayerClassVisitor p = new ClientPlayerClassVisitor(cw, isObfuscated);

			cr.accept(p, 0);

			byte[] result = cw.toByteArray();
			in.close();
			return result;
		}
		catch(IOException ioe)
		{
			throw new RuntimeException(ioe);
		}
	}

	private final boolean isObfuscated;

	public ClientPlayerClassVisitor(ClassVisitor classVisitor, boolean isObfuscated)
	{
		super(262144, classVisitor);
		this.isObfuscated = isObfuscated;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		String[] newInterfaces = new String[interfaces.length + 1];
		for(int i=0; i<interfaces.length; i++)
			newInterfaces[i] = interfaces[i];
		newInterfaces[interfaces.length] = "api/player/client/IClientPlayerAPI";
		super.visit(version, access, name, signature, superName, newInterfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		if(name.equals("<init>"))
			return new ClientPlayerConstructorVisitor(super.visitMethod(access, name, desc, signature, exceptions), isObfuscated);

		if(name.equals(isObfuscated ? "a" : "addExhaustion") && desc.equals("(F)V"))
		{
			hadLocalAddExhaustion = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAddExhaustion", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "k" : "addMovementStat") && desc.equals("(DDD)V"))
		{
			hadLocalAddMovementStat = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAddMovementStat", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "addStat") && desc.equals(isObfuscated ? "(Lqo;I)V" : "(Lnet/minecraft/stats/StatBase;I)V"))
		{
			hadLocalAddStat = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAddStat", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "attackEntityFrom") && desc.equals(isObfuscated ? "(Lur;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z"))
		{
			hadLocalAttackEntityFrom = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAttackEntityFrom", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "f" : "attackEntityPlayerSPEntityWithCurrentItem") && desc.equals(isObfuscated ? "(Lvg;)V" : "(Lnet/minecraft/entity/Entity;)V"))
		{
			hadLocalAttackTargetEntityWithCurrentItem = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAttackTargetEntityWithCurrentItem", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "bN" : "canBreatheUnderwater") && desc.equals("()Z"))
		{
			hadLocalCanBreatheUnderwater = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanBreatheUnderwater", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "c" : "canHarvestBlock") && desc.equals(isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/state/IBlockState;)Z"))
		{
			hadLocalCanHarvestBlock = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanHarvestBlock", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "canPlayerEdit") && desc.equals(isObfuscated ? "(Let;Lfa;Laip;)Z" : "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/item/ItemStack;)Z"))
		{
			hadLocalCanPlayerEdit = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanPlayerEdit", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "ak" : "canTriggerWalking") && desc.equals("()Z"))
		{
			hadLocalCanTriggerWalking = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanTriggerWalking", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "p" : "closeScreen") && desc.equals("()V"))
		{
			hadLocalCloseScreen = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCloseScreen", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "d" : "damageEntity") && desc.equals(isObfuscated ? "(Lur;F)V" : "(Lnet/minecraft/util/DamageSource;F)V"))
		{
			hadLocalDamageEntity = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDamageEntity", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "displayGui") && desc.equals(isObfuscated ? "(Luc;)V" : "(Lnet/minecraft/world/IInteractionObject;)V"))
		{
			hadLocalDisplayGui = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGui", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "displayGUIChest") && desc.equals(isObfuscated ? "(Ltv;)V" : "(Lnet/minecraft/inventory/IInventory;)V"))
		{
			hadLocalDisplayGUIChest = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIChest", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "dropItem") && desc.equals(isObfuscated ? "(Laip;ZZ)Lacl;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;"))
		{
			hadLocalDropItem = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropItem", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "dropItem") && desc.equals(isObfuscated ? "(Z)Lacl;" : "(Z)Lnet/minecraft/entity/item/EntityItem;"))
		{
			hadLocalDropOneItem = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropOneItem", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "dropItem") && desc.equals(isObfuscated ? "(Laip;Z)Lacl;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;"))
		{
			hadLocalDropPlayerItemWithRandomChoice = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropPlayerItemWithRandomChoice", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "e" : "fall") && desc.equals("(FF)V"))
		{
			hadLocalFall = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localFall", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "cy" : "getAIMoveSpeed") && desc.equals("()F"))
		{
			hadLocalGetAIMoveSpeed = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetAIMoveSpeed", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "db" : "getBedOrientationInDegrees") && desc.equals("()F"))
		{
			hadLocalGetBedOrientationInDegrees = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBedOrientationInDegrees", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "aw" : "getBrightness") && desc.equals("()F"))
		{
			hadLocalGetBrightness = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBrightness", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "av" : "getBrightnessForRender") && desc.equals("()I"))
		{
			hadLocalGetBrightnessForRender = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBrightnessForRender", desc, signature, exceptions);
		}

		if(name.equals("getDigSpeed") && desc.equals(isObfuscated ? "(Lawt;Let;)F" : "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)F"))
		{
			hadLocalGetBreakSpeed = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBreakSpeed", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "d" : "getDistanceSq") && desc.equals("(DDD)D"))
		{
			hadLocalGetDistanceSq = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetDistanceSq", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "h" : "getDistanceSqToEntity") && desc.equals(isObfuscated ? "(Lvg;)D" : "(Lnet/minecraft/entity/Entity;)D"))
		{
			hadLocalGetDistanceSqToEntity = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetDistanceSqToEntity", desc, signature, exceptions);
		}

		if(name.equals("getFovModifier") && desc.equals("()F"))
		{
			hadLocalGetFovModifier = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetFovModifier", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "d" : "getHurtSound") && desc.equals(isObfuscated ? "(Lur;)Lqe;" : "(Lnet/minecraft/util/DamageSource;)Lnet/minecraft/util/SoundEvent;"))
		{
			hadLocalGetHurtSound = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetHurtSound", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "h_" : "getName") && desc.equals("()Ljava/lang/String;"))
		{
			hadLocalGetName = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetName", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "dd" : "getSleepTimer") && desc.equals("()I"))
		{
			hadLocalGetSleepTimer = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetSleepTimer", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "aq" : "handleWaterMovement") && desc.equals("()Z"))
		{
			hadLocalHandleWaterMovement = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localHandleWaterMovement", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "b" : "heal") && desc.equals("(F)V"))
		{
			hadLocalHeal = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localHeal", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "aD" : "isEntityInsideOpaqueBlock") && desc.equals("()Z"))
		{
			hadLocalIsEntityInsideOpaqueBlock = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsEntityInsideOpaqueBlock", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "ao" : "isInWater") && desc.equals("()Z"))
		{
			hadLocalIsInWater = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsInWater", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "isInsideOfMaterial") && desc.equals(isObfuscated ? "(Lbcz;)Z" : "(Lnet/minecraft/block/material/Material;)Z"))
		{
			hadLocalIsInsideOfMaterial = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsInsideOfMaterial", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "m_" : "isOnLadder") && desc.equals("()Z"))
		{
			hadLocalIsOnLadder = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsOnLadder", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "cz" : "isPlayerSleeping") && desc.equals("()Z"))
		{
			hadLocalIsPlayerSleeping = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsPlayerSleeping", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "aU" : "isSneaking") && desc.equals("()Z"))
		{
			hadLocalIsSneaking = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsSneaking", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "aV" : "isSprinting") && desc.equals("()Z"))
		{
			hadLocalIsSprinting = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsSprinting", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "cu" : "jump") && desc.equals("()V"))
		{
			hadLocalJump = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localJump", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "knockBack") && desc.equals(isObfuscated ? "(Lvg;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V"))
		{
			hadLocalKnockBack = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localKnockBack", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "move") && desc.equals(isObfuscated ? "(Lvv;DDD)V" : "(Lnet/minecraft/entity/MoverType;DDD)V"))
		{
			hadLocalMoveEntity = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveEntity", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "travel") && desc.equals("(FFF)V"))
		{
			hadLocalMoveEntityWithHeading = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveEntityWithHeading", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "b" : "moveRelative") && desc.equals("(FFFF)V"))
		{
			hadLocalMoveFlying = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveFlying", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "onDeath") && desc.equals(isObfuscated ? "(Lur;)V" : "(Lnet/minecraft/util/DamageSource;)V"))
		{
			hadLocalOnDeath = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnDeath", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "n" : "onLivingUpdate") && desc.equals("()V"))
		{
			hadLocalOnLivingUpdate = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnLivingUpdate", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "b" : "onKillEntity") && desc.equals(isObfuscated ? "(Lvp;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V"))
		{
			hadLocalOnKillEntity = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnKillEntity", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "onStruckByLightning") && desc.equals(isObfuscated ? "(Laci;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V"))
		{
			hadLocalOnStruckByLightning = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnStruckByLightning", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "B_" : "onUpdate") && desc.equals("()V"))
		{
			hadLocalOnUpdate = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnUpdate", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "playStepSound") && desc.equals(isObfuscated ? "(Let;Laow;)V" : "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
		{
			hadLocalPlayStepSound = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localPlayStepSound", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "i" : "pushOutOfBlocks") && desc.equals("(DDD)Z"))
		{
			hadLocalPushOutOfBlocks = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localPushOutOfBlocks", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "rayTrace") && desc.equals(isObfuscated ? "(DF)Lbhc;" : "(DF)Lnet/minecraft/util/math/RayTraceResult;"))
		{
			hadLocalRayTrace = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localRayTrace", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "readEntityFromNBT") && desc.equals(isObfuscated ? "(Lfy;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V"))
		{
			hadLocalReadEntityFromNBT = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localReadEntityFromNBT", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "cY" : "respawnPlayer") && desc.equals("()V"))
		{
			hadLocalRespawnPlayer = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localRespawnPlayer", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "X" : "setDead") && desc.equals("()V"))
		{
			hadLocalSetDead = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetDead", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "o" : "setPlayerSPHealth") && desc.equals("(F)V"))
		{
			hadLocalSetPlayerSPHealth = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetPlayerSPHealth", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "setPositionAndRotation") && desc.equals("(DDDFF)V"))
		{
			hadLocalSetPositionAndRotation = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetPositionAndRotation", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "e" : "setSneaking") && desc.equals("(Z)V"))
		{
			hadLocalSetSneaking = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetSneaking", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "f" : "setSprinting") && desc.equals("(Z)V"))
		{
			hadLocalSetSprinting = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetSprinting", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "trySleep") && desc.equals(isObfuscated ? "(Let;)Laed$a;" : "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/player/EntityPlayer$SleepResult;"))
		{
			hadLocalTrySleep = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localTrySleep", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "swingArm") && desc.equals(isObfuscated ? "(Lub;)V" : "(Lnet/minecraft/util/EnumHand;)V"))
		{
			hadLocalSwingItem = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSwingItem", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "cA" : "updateEntityActionState") && desc.equals("()V"))
		{
			hadLocalUpdateEntityActionState = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localUpdateEntityActionState", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "aE" : "updateRidden") && desc.equals("()V"))
		{
			hadLocalUpdateRidden = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localUpdateRidden", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "wakeUpPlayer") && desc.equals("(ZZZ)V"))
		{
			hadLocalWakeUpPlayer = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localWakeUpPlayer", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "b" : "writeEntityToNBT") && desc.equals(isObfuscated ? "(Lfy;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V"))
		{
			hadLocalWriteEntityToNBT = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localWriteEntityToNBT", desc, signature, exceptions);
		}

		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	@Override
	public void visitEnd()
	{
		MethodVisitor mv;

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "addExhaustion", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "addExhaustion", "(Lapi/player/client/IClientPlayerAPI;F)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realAddExhaustion", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "addExhaustion", "(F)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superAddExhaustion", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "addExhaustion", "(F)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalAddExhaustion)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAddExhaustion", "(F)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "addExhaustion", "(F)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "k" : "addMovementStat", "(DDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "addMovementStat", "(Lapi/player/client/IClientPlayerAPI;DDD)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realAddMovementStat", "(DDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "k" : "addMovementStat", "(DDD)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superAddMovementStat", "(DDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "k" : "addMovementStat", "(DDD)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalAddMovementStat)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAddMovementStat", "(DDD)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.DLOAD, 1);
			mv.visitVarInsn(Opcodes.DLOAD, 3);
			mv.visitVarInsn(Opcodes.DLOAD, 5);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "k" : "addMovementStat", "(DDD)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "addStat", "" + (isObfuscated ? "(Lqo;I)V" : "(Lnet/minecraft/stats/StatBase;I)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "addStat", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/stats/StatBase;I)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realAddStat", "(Lnet/minecraft/stats/StatBase;I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "addStat", "" + (isObfuscated ? "(Lqo;I)V" : "(Lnet/minecraft/stats/StatBase;I)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superAddStat", "(Lnet/minecraft/stats/StatBase;I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "addStat", "" + (isObfuscated ? "(Lqo;I)V" : "(Lnet/minecraft/stats/StatBase;I)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalAddStat)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAddStat", "(Lnet/minecraft/stats/StatBase;I)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "addStat", "" + (isObfuscated ? "(Lqo;I)V" : "(Lnet/minecraft/stats/StatBase;I)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "attackEntityFrom", "" + (isObfuscated ? "(Lur;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "attackEntityFrom", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/util/DamageSource;F)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realAttackEntityFrom", "(Lnet/minecraft/util/DamageSource;F)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "attackEntityFrom", "" + (isObfuscated ? "(Lur;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z") + "", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superAttackEntityFrom", "(Lnet/minecraft/util/DamageSource;F)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "attackEntityFrom", "" + (isObfuscated ? "(Lur;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z") + "", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalAttackEntityFrom)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAttackEntityFrom", "(Lnet/minecraft/util/DamageSource;F)Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "attackEntityFrom", "" + (isObfuscated ? "(Lur;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z") + "", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "f" : "attackEntityPlayerSPEntityWithCurrentItem", "" + (isObfuscated ? "(Lvg;)V" : "(Lnet/minecraft/entity/Entity;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "attackTargetEntityWithCurrentItem", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/entity/Entity;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realAttackTargetEntityWithCurrentItem", "(Lnet/minecraft/entity/Entity;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "f" : "attackEntityPlayerSPEntityWithCurrentItem", "" + (isObfuscated ? "(Lvg;)V" : "(Lnet/minecraft/entity/Entity;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superAttackTargetEntityWithCurrentItem", "(Lnet/minecraft/entity/Entity;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "f" : "attackEntityPlayerSPEntityWithCurrentItem", "" + (isObfuscated ? "(Lvg;)V" : "(Lnet/minecraft/entity/Entity;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalAttackTargetEntityWithCurrentItem)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAttackTargetEntityWithCurrentItem", "(Lnet/minecraft/entity/Entity;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "f" : "attackEntityPlayerSPEntityWithCurrentItem", "" + (isObfuscated ? "(Lvg;)V" : "(Lnet/minecraft/entity/Entity;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "bN" : "canBreatheUnderwater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "canBreatheUnderwater", "(Lapi/player/client/IClientPlayerAPI;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realCanBreatheUnderwater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bN" : "canBreatheUnderwater", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superCanBreatheUnderwater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bN" : "canBreatheUnderwater", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalCanBreatheUnderwater)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanBreatheUnderwater", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bN" : "canBreatheUnderwater", "()Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "c" : "canHarvestBlock", "" + (isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/state/IBlockState;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "canHarvestBlock", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/block/state/IBlockState;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realCanHarvestBlock", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "c" : "canHarvestBlock", "" + (isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/state/IBlockState;)Z") + "", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superCanHarvestBlock", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "c" : "canHarvestBlock", "" + (isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/state/IBlockState;)Z") + "", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalCanHarvestBlock)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanHarvestBlock", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "c" : "canHarvestBlock", "" + (isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/state/IBlockState;)Z") + "", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "canPlayerEdit", "" + (isObfuscated ? "(Let;Lfa;Laip;)Z" : "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/item/ItemStack;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitVarInsn(Opcodes.ALOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "canPlayerEdit", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/item/ItemStack;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realCanPlayerEdit", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/item/ItemStack;)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitVarInsn(Opcodes.ALOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "canPlayerEdit", "" + (isObfuscated ? "(Let;Lfa;Laip;)Z" : "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/item/ItemStack;)Z") + "", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superCanPlayerEdit", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/item/ItemStack;)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitVarInsn(Opcodes.ALOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "canPlayerEdit", "" + (isObfuscated ? "(Let;Lfa;Laip;)Z" : "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/item/ItemStack;)Z") + "", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalCanPlayerEdit)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanPlayerEdit", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/item/ItemStack;)Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ALOAD, 2);
			mv.visitVarInsn(Opcodes.ALOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "canPlayerEdit", "" + (isObfuscated ? "(Let;Lfa;Laip;)Z" : "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/item/ItemStack;)Z") + "", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "ak" : "canTriggerWalking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "canTriggerWalking", "(Lapi/player/client/IClientPlayerAPI;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realCanTriggerWalking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ak" : "canTriggerWalking", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superCanTriggerWalking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "ak" : "canTriggerWalking", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalCanTriggerWalking)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanTriggerWalking", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "ak" : "canTriggerWalking", "()Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "p" : "closeScreen", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "closeScreen", "(Lapi/player/client/IClientPlayerAPI;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realCloseScreen", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "p" : "closeScreen", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superCloseScreen", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "p" : "closeScreen", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalCloseScreen)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCloseScreen", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "p" : "closeScreen", "()V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "d" : "damageEntity", "" + (isObfuscated ? "(Lur;F)V" : "(Lnet/minecraft/util/DamageSource;F)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "damageEntity", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/util/DamageSource;F)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDamageEntity", "(Lnet/minecraft/util/DamageSource;F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "d" : "damageEntity", "" + (isObfuscated ? "(Lur;F)V" : "(Lnet/minecraft/util/DamageSource;F)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDamageEntity", "(Lnet/minecraft/util/DamageSource;F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "damageEntity", "" + (isObfuscated ? "(Lur;F)V" : "(Lnet/minecraft/util/DamageSource;F)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDamageEntity)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDamageEntity", "(Lnet/minecraft/util/DamageSource;F)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "damageEntity", "" + (isObfuscated ? "(Lur;F)V" : "(Lnet/minecraft/util/DamageSource;F)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "displayGui", "" + (isObfuscated ? "(Luc;)V" : "(Lnet/minecraft/world/IInteractionObject;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "displayGui", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/world/IInteractionObject;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDisplayGui", "(Lnet/minecraft/world/IInteractionObject;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "displayGui", "" + (isObfuscated ? "(Luc;)V" : "(Lnet/minecraft/world/IInteractionObject;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDisplayGui", "(Lnet/minecraft/world/IInteractionObject;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "displayGui", "" + (isObfuscated ? "(Luc;)V" : "(Lnet/minecraft/world/IInteractionObject;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDisplayGui)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGui", "(Lnet/minecraft/world/IInteractionObject;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "displayGui", "" + (isObfuscated ? "(Luc;)V" : "(Lnet/minecraft/world/IInteractionObject;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "displayGUIChest", "" + (isObfuscated ? "(Ltv;)V" : "(Lnet/minecraft/inventory/IInventory;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "displayGUIChest", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/inventory/IInventory;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDisplayGUIChest", "(Lnet/minecraft/inventory/IInventory;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "displayGUIChest", "" + (isObfuscated ? "(Ltv;)V" : "(Lnet/minecraft/inventory/IInventory;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDisplayGUIChest", "(Lnet/minecraft/inventory/IInventory;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "displayGUIChest", "" + (isObfuscated ? "(Ltv;)V" : "(Lnet/minecraft/inventory/IInventory;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDisplayGUIChest)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIChest", "(Lnet/minecraft/inventory/IInventory;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "displayGUIChest", "" + (isObfuscated ? "(Ltv;)V" : "(Lnet/minecraft/inventory/IInventory;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Laip;ZZ)Lacl;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "dropItem", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/item/ItemStack;ZZ)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDropItem", "(Lnet/minecraft/item/ItemStack;ZZ)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Laip;ZZ)Lacl;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDropItem", "(Lnet/minecraft/item/ItemStack;ZZ)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Laip;ZZ)Lacl;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDropItem)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropItem", "(Lnet/minecraft/item/ItemStack;ZZ)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Laip;ZZ)Lacl;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;") + "", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Z)Lacl;" : "(Z)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "dropOneItem", "(Lapi/player/client/IClientPlayerAPI;Z)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDropOneItem", "(Z)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Z)Lacl;" : "(Z)Lnet/minecraft/entity/item/EntityItem;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDropOneItem", "(Z)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Z)Lacl;" : "(Z)Lnet/minecraft/entity/item/EntityItem;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDropOneItem)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropOneItem", "(Z)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Z)Lacl;" : "(Z)Lnet/minecraft/entity/item/EntityItem;") + "", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Laip;Z)Lacl;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "dropPlayerItemWithRandomChoice", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/item/ItemStack;Z)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDropPlayerItemWithRandomChoice", "(Lnet/minecraft/item/ItemStack;Z)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Laip;Z)Lacl;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDropPlayerItemWithRandomChoice", "(Lnet/minecraft/item/ItemStack;Z)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Laip;Z)Lacl;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDropPlayerItemWithRandomChoice)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropPlayerItemWithRandomChoice", "(Lnet/minecraft/item/ItemStack;Z)" + (isObfuscated ? "Lacl;" : "Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "dropItem", "" + (isObfuscated ? "(Laip;Z)Lacl;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;") + "", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "e" : "fall", "(FF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "fall", "(Lapi/player/client/IClientPlayerAPI;FF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realFall", "(FF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "e" : "fall", "(FF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superFall", "(FF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "e" : "fall", "(FF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalFall)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localFall", "(FF)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "e" : "fall", "(FF)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "cy" : "getAIMoveSpeed", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getAIMoveSpeed", "(Lapi/player/client/IClientPlayerAPI;)F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetAIMoveSpeed", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cy" : "getAIMoveSpeed", "()F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetAIMoveSpeed", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "cy" : "getAIMoveSpeed", "()F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetAIMoveSpeed)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetAIMoveSpeed", "()F", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "cy" : "getAIMoveSpeed", "()F", false);
			mv.visitInsn(Opcodes.FRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "db" : "getBedOrientationInDegrees", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getBedOrientationInDegrees", "(Lapi/player/client/IClientPlayerAPI;)F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetBedOrientationInDegrees", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "db" : "getBedOrientationInDegrees", "()F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetBedOrientationInDegrees", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "db" : "getBedOrientationInDegrees", "()F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetBedOrientationInDegrees)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBedOrientationInDegrees", "()F", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "db" : "getBedOrientationInDegrees", "()F", false);
			mv.visitInsn(Opcodes.FRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "aw" : "getBrightness", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getBrightness", "(Lapi/player/client/IClientPlayerAPI;)F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetBrightness", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aw" : "getBrightness", "()F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetBrightness", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aw" : "getBrightness", "()F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetBrightness)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBrightness", "()F", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aw" : "getBrightness", "()F", false);
			mv.visitInsn(Opcodes.FRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "av" : "getBrightnessForRender", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getBrightnessForRender", "(Lapi/player/client/IClientPlayerAPI;)I", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetBrightnessForRender", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "av" : "getBrightnessForRender", "()I", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetBrightnessForRender", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "av" : "getBrightnessForRender", "()I", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetBrightnessForRender)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBrightnessForRender", "()I", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "av" : "getBrightnessForRender", "()I", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "getDigSpeed", "" + (isObfuscated ? "(Lawt;Let;)F" : "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)F") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getBreakSpeed", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetBreakSpeed", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", "getDigSpeed", "" + (isObfuscated ? "(Lawt;Let;)F" : "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)F") + "", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetBreakSpeed", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", "getDigSpeed", "" + (isObfuscated ? "(Lawt;Let;)F" : "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)F") + "", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetBreakSpeed)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBreakSpeed", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)F", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ALOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", "getDigSpeed", "" + (isObfuscated ? "(Lawt;Let;)F" : "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)F") + "", false);
			mv.visitInsn(Opcodes.FRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "d" : "getDistanceSq", "(DDD)D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getDistanceSq", "(Lapi/player/client/IClientPlayerAPI;DDD)D", false);
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetDistanceSq", "(DDD)D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "d" : "getDistanceSq", "(DDD)D", false);
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetDistanceSq", "(DDD)D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "getDistanceSq", "(DDD)D", false);
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetDistanceSq)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetDistanceSq", "(DDD)D", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.DLOAD, 1);
			mv.visitVarInsn(Opcodes.DLOAD, 3);
			mv.visitVarInsn(Opcodes.DLOAD, 5);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "getDistanceSq", "(DDD)D", false);
			mv.visitInsn(Opcodes.DRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "h" : "getDistanceSqToEntity", "" + (isObfuscated ? "(Lvg;)D" : "(Lnet/minecraft/entity/Entity;)D") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getDistanceSqToEntity", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/entity/Entity;)D", false);
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetDistanceSqToEntity", "(Lnet/minecraft/entity/Entity;)D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "h" : "getDistanceSqToEntity", "" + (isObfuscated ? "(Lvg;)D" : "(Lnet/minecraft/entity/Entity;)D") + "", false);
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetDistanceSqToEntity", "(Lnet/minecraft/entity/Entity;)D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "h" : "getDistanceSqToEntity", "" + (isObfuscated ? "(Lvg;)D" : "(Lnet/minecraft/entity/Entity;)D") + "", false);
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetDistanceSqToEntity)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetDistanceSqToEntity", "(Lnet/minecraft/entity/Entity;)D", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "h" : "getDistanceSqToEntity", "" + (isObfuscated ? "(Lvg;)D" : "(Lnet/minecraft/entity/Entity;)D") + "", false);
			mv.visitInsn(Opcodes.DRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "getFovModifier", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getFovModifier", "(Lapi/player/client/IClientPlayerAPI;)F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetFovModifier", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", "getFovModifier", "()F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetFovModifier", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", "getFovModifier", "()F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetFovModifier)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetFovModifier", "()F", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", "getFovModifier", "()F", false);
			mv.visitInsn(Opcodes.FRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "d" : "getHurtSound", "" + (isObfuscated ? "(Lur;)Lqe;" : "(Lnet/minecraft/util/DamageSource;)Lnet/minecraft/util/SoundEvent;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getHurtSound", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/util/DamageSource;)" + (isObfuscated ? "Lqe;" : "Lnet/minecraft/util/SoundEvent;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetHurtSound", "(Lnet/minecraft/util/DamageSource;)" + (isObfuscated ? "Lqe;" : "Lnet/minecraft/util/SoundEvent;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "d" : "getHurtSound", "" + (isObfuscated ? "(Lur;)Lqe;" : "(Lnet/minecraft/util/DamageSource;)Lnet/minecraft/util/SoundEvent;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetHurtSound", "(Lnet/minecraft/util/DamageSource;)" + (isObfuscated ? "Lqe;" : "Lnet/minecraft/util/SoundEvent;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "getHurtSound", "" + (isObfuscated ? "(Lur;)Lqe;" : "(Lnet/minecraft/util/DamageSource;)Lnet/minecraft/util/SoundEvent;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetHurtSound)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetHurtSound", "(Lnet/minecraft/util/DamageSource;)" + (isObfuscated ? "Lqe;" : "Lnet/minecraft/util/SoundEvent;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "getHurtSound", "" + (isObfuscated ? "(Lur;)Lqe;" : "(Lnet/minecraft/util/DamageSource;)Lnet/minecraft/util/SoundEvent;") + "", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "h_" : "getName", "()Ljava/lang/String;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getName", "(Lapi/player/client/IClientPlayerAPI;)Ljava/lang/String;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetName", "()Ljava/lang/String;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "h_" : "getName", "()Ljava/lang/String;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetName", "()Ljava/lang/String;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "h_" : "getName", "()Ljava/lang/String;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetName)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetName", "()Ljava/lang/String;", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "h_" : "getName", "()Ljava/lang/String;", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "dd" : "getSleepTimer", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getSleepTimer", "(Lapi/player/client/IClientPlayerAPI;)I", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetSleepTimer", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "dd" : "getSleepTimer", "()I", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetSleepTimer", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "dd" : "getSleepTimer", "()I", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetSleepTimer)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetSleepTimer", "()I", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "dd" : "getSleepTimer", "()I", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "aq" : "handleWaterMovement", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "handleWaterMovement", "(Lapi/player/client/IClientPlayerAPI;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realHandleWaterMovement", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aq" : "handleWaterMovement", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superHandleWaterMovement", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aq" : "handleWaterMovement", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalHandleWaterMovement)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localHandleWaterMovement", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aq" : "handleWaterMovement", "()Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "b" : "heal", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "heal", "(Lapi/player/client/IClientPlayerAPI;F)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realHeal", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "b" : "heal", "(F)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superHeal", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "heal", "(F)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalHeal)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localHeal", "(F)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "heal", "(F)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "aD" : "isEntityInsideOpaqueBlock", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isEntityInsideOpaqueBlock", "(Lapi/player/client/IClientPlayerAPI;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsEntityInsideOpaqueBlock", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aD" : "isEntityInsideOpaqueBlock", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsEntityInsideOpaqueBlock", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aD" : "isEntityInsideOpaqueBlock", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsEntityInsideOpaqueBlock)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsEntityInsideOpaqueBlock", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aD" : "isEntityInsideOpaqueBlock", "()Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "ao" : "isInWater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isInWater", "(Lapi/player/client/IClientPlayerAPI;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsInWater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ao" : "isInWater", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsInWater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "ao" : "isInWater", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsInWater)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsInWater", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "ao" : "isInWater", "()Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "isInsideOfMaterial", "" + (isObfuscated ? "(Lbcz;)Z" : "(Lnet/minecraft/block/material/Material;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isInsideOfMaterial", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/block/material/Material;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsInsideOfMaterial", "(Lnet/minecraft/block/material/Material;)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "isInsideOfMaterial", "" + (isObfuscated ? "(Lbcz;)Z" : "(Lnet/minecraft/block/material/Material;)Z") + "", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsInsideOfMaterial", "(Lnet/minecraft/block/material/Material;)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "isInsideOfMaterial", "" + (isObfuscated ? "(Lbcz;)Z" : "(Lnet/minecraft/block/material/Material;)Z") + "", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsInsideOfMaterial)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsInsideOfMaterial", "(Lnet/minecraft/block/material/Material;)Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "isInsideOfMaterial", "" + (isObfuscated ? "(Lbcz;)Z" : "(Lnet/minecraft/block/material/Material;)Z") + "", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "m_" : "isOnLadder", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isOnLadder", "(Lapi/player/client/IClientPlayerAPI;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsOnLadder", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "m_" : "isOnLadder", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsOnLadder", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "m_" : "isOnLadder", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsOnLadder)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsOnLadder", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "m_" : "isOnLadder", "()Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "cz" : "isPlayerSleeping", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isPlayerSleeping", "(Lapi/player/client/IClientPlayerAPI;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsPlayerSleeping", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cz" : "isPlayerSleeping", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsPlayerSleeping", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "cz" : "isPlayerSleeping", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsPlayerSleeping)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsPlayerSleeping", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "cz" : "isPlayerSleeping", "()Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "aU" : "isSneaking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isSneaking", "(Lapi/player/client/IClientPlayerAPI;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsSneaking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aU" : "isSneaking", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsSneaking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aU" : "isSneaking", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsSneaking)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsSneaking", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aU" : "isSneaking", "()Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "aV" : "isSprinting", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isSprinting", "(Lapi/player/client/IClientPlayerAPI;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsSprinting", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aV" : "isSprinting", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsSprinting", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aV" : "isSprinting", "()Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsSprinting)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsSprinting", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aV" : "isSprinting", "()Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "cu" : "jump", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "jump", "(Lapi/player/client/IClientPlayerAPI;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realJump", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cu" : "jump", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superJump", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "cu" : "jump", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalJump)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localJump", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "cu" : "jump", "()V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "knockBack", "" + (isObfuscated ? "(Lvg;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "knockBack", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/entity/Entity;FDD)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realKnockBack", "(Lnet/minecraft/entity/Entity;FDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "knockBack", "" + (isObfuscated ? "(Lvg;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superKnockBack", "(Lnet/minecraft/entity/Entity;FDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "knockBack", "" + (isObfuscated ? "(Lvg;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalKnockBack)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localKnockBack", "(Lnet/minecraft/entity/Entity;FDD)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitVarInsn(Opcodes.DLOAD, 3);
			mv.visitVarInsn(Opcodes.DLOAD, 5);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "knockBack", "" + (isObfuscated ? "(Lvg;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "move", "" + (isObfuscated ? "(Lvv;DDD)V" : "(Lnet/minecraft/entity/MoverType;DDD)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 2);
		mv.visitVarInsn(Opcodes.DLOAD, 4);
		mv.visitVarInsn(Opcodes.DLOAD, 6);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "moveEntity", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/entity/MoverType;DDD)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realMoveEntity", "(Lnet/minecraft/entity/MoverType;DDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 2);
		mv.visitVarInsn(Opcodes.DLOAD, 4);
		mv.visitVarInsn(Opcodes.DLOAD, 6);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "move", "" + (isObfuscated ? "(Lvv;DDD)V" : "(Lnet/minecraft/entity/MoverType;DDD)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superMoveEntity", "(Lnet/minecraft/entity/MoverType;DDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 2);
		mv.visitVarInsn(Opcodes.DLOAD, 4);
		mv.visitVarInsn(Opcodes.DLOAD, 6);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "move", "" + (isObfuscated ? "(Lvv;DDD)V" : "(Lnet/minecraft/entity/MoverType;DDD)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalMoveEntity)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveEntity", "(Lnet/minecraft/entity/MoverType;DDD)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.DLOAD, 2);
			mv.visitVarInsn(Opcodes.DLOAD, 4);
			mv.visitVarInsn(Opcodes.DLOAD, 6);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "move", "" + (isObfuscated ? "(Lvv;DDD)V" : "(Lnet/minecraft/entity/MoverType;DDD)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "travel", "(FFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "moveEntityWithHeading", "(Lapi/player/client/IClientPlayerAPI;FFF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realMoveEntityWithHeading", "(FFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "travel", "(FFF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superMoveEntityWithHeading", "(FFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "travel", "(FFF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalMoveEntityWithHeading)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveEntityWithHeading", "(FFF)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitVarInsn(Opcodes.FLOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "travel", "(FFF)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "b" : "moveRelative", "(FFFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "moveFlying", "(Lapi/player/client/IClientPlayerAPI;FFFF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realMoveFlying", "(FFFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "b" : "moveRelative", "(FFFF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superMoveFlying", "(FFFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "moveRelative", "(FFFF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalMoveFlying)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveFlying", "(FFFF)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitVarInsn(Opcodes.FLOAD, 3);
			mv.visitVarInsn(Opcodes.FLOAD, 4);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "moveRelative", "(FFFF)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "onDeath", "" + (isObfuscated ? "(Lur;)V" : "(Lnet/minecraft/util/DamageSource;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "onDeath", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/util/DamageSource;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realOnDeath", "(Lnet/minecraft/util/DamageSource;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "onDeath", "" + (isObfuscated ? "(Lur;)V" : "(Lnet/minecraft/util/DamageSource;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superOnDeath", "(Lnet/minecraft/util/DamageSource;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "onDeath", "" + (isObfuscated ? "(Lur;)V" : "(Lnet/minecraft/util/DamageSource;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalOnDeath)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnDeath", "(Lnet/minecraft/util/DamageSource;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "onDeath", "" + (isObfuscated ? "(Lur;)V" : "(Lnet/minecraft/util/DamageSource;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "n" : "onLivingUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "onLivingUpdate", "(Lapi/player/client/IClientPlayerAPI;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realOnLivingUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "n" : "onLivingUpdate", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superOnLivingUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "n" : "onLivingUpdate", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalOnLivingUpdate)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnLivingUpdate", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "n" : "onLivingUpdate", "()V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "b" : "onKillEntity", "" + (isObfuscated ? "(Lvp;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "onKillEntity", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/entity/EntityLivingBase;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realOnKillEntity", "(Lnet/minecraft/entity/EntityLivingBase;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "b" : "onKillEntity", "" + (isObfuscated ? "(Lvp;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superOnKillEntity", "(Lnet/minecraft/entity/EntityLivingBase;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "onKillEntity", "" + (isObfuscated ? "(Lvp;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalOnKillEntity)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnKillEntity", "(Lnet/minecraft/entity/EntityLivingBase;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "onKillEntity", "" + (isObfuscated ? "(Lvp;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "onStruckByLightning", "" + (isObfuscated ? "(Laci;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "onStruckByLightning", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/entity/effect/EntityLightningBolt;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realOnStruckByLightning", "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "onStruckByLightning", "" + (isObfuscated ? "(Laci;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superOnStruckByLightning", "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "onStruckByLightning", "" + (isObfuscated ? "(Laci;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalOnStruckByLightning)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnStruckByLightning", "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "onStruckByLightning", "" + (isObfuscated ? "(Laci;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "B_" : "onUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "onUpdate", "(Lapi/player/client/IClientPlayerAPI;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realOnUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "B_" : "onUpdate", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superOnUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "B_" : "onUpdate", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalOnUpdate)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnUpdate", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "B_" : "onUpdate", "()V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "playStepSound", "" + (isObfuscated ? "(Let;Laow;)V" : "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "playStepSound", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realPlayStepSound", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "playStepSound", "" + (isObfuscated ? "(Let;Laow;)V" : "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superPlayStepSound", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "playStepSound", "" + (isObfuscated ? "(Let;Laow;)V" : "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalPlayStepSound)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localPlayStepSound", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ALOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "playStepSound", "" + (isObfuscated ? "(Let;Laow;)V" : "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "i" : "pushOutOfBlocks", "(DDD)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "pushOutOfBlocks", "(Lapi/player/client/IClientPlayerAPI;DDD)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realPushOutOfBlocks", "(DDD)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "i" : "pushOutOfBlocks", "(DDD)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superPushOutOfBlocks", "(DDD)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "i" : "pushOutOfBlocks", "(DDD)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalPushOutOfBlocks)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localPushOutOfBlocks", "(DDD)Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.DLOAD, 1);
			mv.visitVarInsn(Opcodes.DLOAD, 3);
			mv.visitVarInsn(Opcodes.DLOAD, 5);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "i" : "pushOutOfBlocks", "(DDD)Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "rayTrace", "" + (isObfuscated ? "(DF)Lbhc;" : "(DF)Lnet/minecraft/util/math/RayTraceResult;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "rayTrace", "(Lapi/player/client/IClientPlayerAPI;DF)" + (isObfuscated ? "Lbhc;" : "Lnet/minecraft/util/math/RayTraceResult;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realRayTrace", "(DF)" + (isObfuscated ? "Lbhc;" : "Lnet/minecraft/util/math/RayTraceResult;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "rayTrace", "" + (isObfuscated ? "(DF)Lbhc;" : "(DF)Lnet/minecraft/util/math/RayTraceResult;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superRayTrace", "(DF)" + (isObfuscated ? "Lbhc;" : "Lnet/minecraft/util/math/RayTraceResult;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "rayTrace", "" + (isObfuscated ? "(DF)Lbhc;" : "(DF)Lnet/minecraft/util/math/RayTraceResult;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalRayTrace)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localRayTrace", "(DF)" + (isObfuscated ? "Lbhc;" : "Lnet/minecraft/util/math/RayTraceResult;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.DLOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "rayTrace", "" + (isObfuscated ? "(DF)Lbhc;" : "(DF)Lnet/minecraft/util/math/RayTraceResult;") + "", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "readEntityFromNBT", "" + (isObfuscated ? "(Lfy;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "readEntityFromNBT", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/nbt/NBTTagCompound;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realReadEntityFromNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "readEntityFromNBT", "" + (isObfuscated ? "(Lfy;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superReadEntityFromNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "readEntityFromNBT", "" + (isObfuscated ? "(Lfy;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalReadEntityFromNBT)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localReadEntityFromNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "readEntityFromNBT", "" + (isObfuscated ? "(Lfy;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "cY" : "respawnPlayer", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "respawnPlayer", "(Lapi/player/client/IClientPlayerAPI;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realRespawnPlayer", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cY" : "respawnPlayer", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superRespawnPlayer", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "cY" : "respawnPlayer", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalRespawnPlayer)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localRespawnPlayer", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "cY" : "respawnPlayer", "()V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "X" : "setDead", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "setDead", "(Lapi/player/client/IClientPlayerAPI;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetDead", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "X" : "setDead", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetDead", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "X" : "setDead", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetDead)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetDead", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "X" : "setDead", "()V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "o" : "setPlayerSPHealth", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "setPlayerSPHealth", "(Lapi/player/client/IClientPlayerAPI;F)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetPlayerSPHealth", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "o" : "setPlayerSPHealth", "(F)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetPlayerSPHealth", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "o" : "setPlayerSPHealth", "(F)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetPlayerSPHealth)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetPlayerSPHealth", "(F)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "o" : "setPlayerSPHealth", "(F)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "setPositionAndRotation", "(DDDFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitVarInsn(Opcodes.FLOAD, 7);
		mv.visitVarInsn(Opcodes.FLOAD, 8);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "setPositionAndRotation", "(Lapi/player/client/IClientPlayerAPI;DDDFF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetPositionAndRotation", "(DDDFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitVarInsn(Opcodes.FLOAD, 7);
		mv.visitVarInsn(Opcodes.FLOAD, 8);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "setPositionAndRotation", "(DDDFF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetPositionAndRotation", "(DDDFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitVarInsn(Opcodes.FLOAD, 7);
		mv.visitVarInsn(Opcodes.FLOAD, 8);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "setPositionAndRotation", "(DDDFF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetPositionAndRotation)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetPositionAndRotation", "(DDDFF)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.DLOAD, 1);
			mv.visitVarInsn(Opcodes.DLOAD, 3);
			mv.visitVarInsn(Opcodes.DLOAD, 5);
			mv.visitVarInsn(Opcodes.FLOAD, 7);
			mv.visitVarInsn(Opcodes.FLOAD, 8);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "setPositionAndRotation", "(DDDFF)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "e" : "setSneaking", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "setSneaking", "(Lapi/player/client/IClientPlayerAPI;Z)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetSneaking", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "e" : "setSneaking", "(Z)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetSneaking", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "e" : "setSneaking", "(Z)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetSneaking)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetSneaking", "(Z)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "e" : "setSneaking", "(Z)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "f" : "setSprinting", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "setSprinting", "(Lapi/player/client/IClientPlayerAPI;Z)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetSprinting", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "f" : "setSprinting", "(Z)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetSprinting", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "f" : "setSprinting", "(Z)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetSprinting)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetSprinting", "(Z)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "f" : "setSprinting", "(Z)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "trySleep", "" + (isObfuscated ? "(Let;)Laed$a;" : "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/player/EntityPlayer$SleepResult;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "trySleep", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/util/math/BlockPos;)" + (isObfuscated ? "Laed$a;" : "Lnet/minecraft/entity/player/EntityPlayer$SleepResult;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realTrySleep", "(Lnet/minecraft/util/math/BlockPos;)" + (isObfuscated ? "Laed$a;" : "Lnet/minecraft/entity/player/EntityPlayer$SleepResult;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "trySleep", "" + (isObfuscated ? "(Let;)Laed$a;" : "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/player/EntityPlayer$SleepResult;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superTrySleep", "(Lnet/minecraft/util/math/BlockPos;)" + (isObfuscated ? "Laed$a;" : "Lnet/minecraft/entity/player/EntityPlayer$SleepResult;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "trySleep", "" + (isObfuscated ? "(Let;)Laed$a;" : "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/player/EntityPlayer$SleepResult;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalTrySleep)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localTrySleep", "(Lnet/minecraft/util/math/BlockPos;)" + (isObfuscated ? "Laed$a;" : "Lnet/minecraft/entity/player/EntityPlayer$SleepResult;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "trySleep", "" + (isObfuscated ? "(Let;)Laed$a;" : "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/player/EntityPlayer$SleepResult;") + "", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "swingArm", "" + (isObfuscated ? "(Lub;)V" : "(Lnet/minecraft/util/EnumHand;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "swingItem", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/util/EnumHand;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSwingItem", "(Lnet/minecraft/util/EnumHand;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "swingArm", "" + (isObfuscated ? "(Lub;)V" : "(Lnet/minecraft/util/EnumHand;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSwingItem", "(Lnet/minecraft/util/EnumHand;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "swingArm", "" + (isObfuscated ? "(Lub;)V" : "(Lnet/minecraft/util/EnumHand;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSwingItem)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSwingItem", "(Lnet/minecraft/util/EnumHand;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "swingArm", "" + (isObfuscated ? "(Lub;)V" : "(Lnet/minecraft/util/EnumHand;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "cA" : "updateEntityActionState", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "updateEntityActionState", "(Lapi/player/client/IClientPlayerAPI;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realUpdateEntityActionState", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cA" : "updateEntityActionState", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superUpdateEntityActionState", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "cA" : "updateEntityActionState", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalUpdateEntityActionState)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localUpdateEntityActionState", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "cA" : "updateEntityActionState", "()V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "aE" : "updateRidden", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "updateRidden", "(Lapi/player/client/IClientPlayerAPI;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realUpdateRidden", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aE" : "updateRidden", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superUpdateRidden", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aE" : "updateRidden", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalUpdateRidden)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localUpdateRidden", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aE" : "updateRidden", "()V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "wakeUpPlayer", "(ZZZ)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "wakeUpPlayer", "(Lapi/player/client/IClientPlayerAPI;ZZZ)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realWakeUpPlayer", "(ZZZ)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "wakeUpPlayer", "(ZZZ)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superWakeUpPlayer", "(ZZZ)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "wakeUpPlayer", "(ZZZ)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalWakeUpPlayer)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localWakeUpPlayer", "(ZZZ)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "wakeUpPlayer", "(ZZZ)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "b" : "writeEntityToNBT", "" + (isObfuscated ? "(Lfy;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "writeEntityToNBT", "(Lapi/player/client/IClientPlayerAPI;Lnet/minecraft/nbt/NBTTagCompound;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realWriteEntityToNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "b" : "writeEntityToNBT", "" + (isObfuscated ? "(Lfy;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superWriteEntityToNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "writeEntityToNBT", "" + (isObfuscated ? "(Lfy;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalWriteEntityToNBT)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localWriteEntityToNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bua" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "writeEntityToNBT", "" + (isObfuscated ? "(Lfy;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLEFT_SHOULDER_ENTITYField", isObfuscated ? "()Lmy;" : "()Lnet/minecraft/network/datasync/DataParameter;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bt" : "LEFT_SHOULDER_ENTITY", isObfuscated ? "Lmy;" : "Lnet/minecraft/network/datasync/DataParameter;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRIGHT_SHOULDER_ENTITYField", isObfuscated ? "()Lmy;" : "()Lnet/minecraft/network/datasync/DataParameter;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bu" : "RIGHT_SHOULDER_ENTITY", isObfuscated ? "Lmy;" : "Lnet/minecraft/network/datasync/DataParameter;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getActiveHandField", isObfuscated ? "()Lub;" : "()Lnet/minecraft/util/EnumHand;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cs" : "activeHand", isObfuscated ? "Lub;" : "Lnet/minecraft/util/EnumHand;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setActiveHandField", isObfuscated ? "(Lub;)V" : "(Lnet/minecraft/util/EnumHand;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cs" : "activeHand", isObfuscated ? "Lub;" : "Lnet/minecraft/util/EnumHand;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getActiveItemStackField", isObfuscated ? "()Laip;" : "()Lnet/minecraft/item/ItemStack;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bo" : "activeItemStack", isObfuscated ? "Laip;" : "Lnet/minecraft/item/ItemStack;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setActiveItemStackField", isObfuscated ? "(Laip;)V" : "(Lnet/minecraft/item/ItemStack;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bo" : "activeItemStack", isObfuscated ? "Laip;" : "Lnet/minecraft/item/ItemStack;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getActiveItemStackUseCountField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bp" : "activeItemStackUseCount", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setActiveItemStackUseCountField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bp" : "activeItemStackUseCount", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getAddedToChunkField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aa" : "addedToChunk", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setAddedToChunkField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aa" : "addedToChunk", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getArrowHitTimerField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ax" : "arrowHitTimer", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setArrowHitTimerField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ax" : "arrowHitTimer", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getAttackedAtYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aA" : "attackedAtYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setAttackedAtYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aA" : "attackedAtYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getAttackingPlayerField", isObfuscated ? "()Laed;" : "()Lnet/minecraft/entity/player/EntityPlayer;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aS" : "attackingPlayer", isObfuscated ? "Laed;" : "Lnet/minecraft/entity/player/EntityPlayer;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setAttackingPlayerField", isObfuscated ? "(Laed;)V" : "(Lnet/minecraft/entity/player/EntityPlayer;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aS" : "attackingPlayer", isObfuscated ? "Laed;" : "Lnet/minecraft/entity/player/EntityPlayer;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getAutoJumpEnabledField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cu" : "autoJumpEnabled", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setAutoJumpEnabledField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cu" : "autoJumpEnabled", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getAutoJumpRequiredField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cv" : "autoJumpTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setAutoJumpRequiredField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cv" : "autoJumpTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getCachedUniqueIdStringField", "()Ljava/lang/String;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ar" : "cachedUniqueIdString", "Ljava/lang/String;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setCachedUniqueIdStringField", "(Ljava/lang/String;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ar" : "cachedUniqueIdString", "Ljava/lang/String;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getCameraPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aK" : "cameraPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setCameraPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aK" : "cameraPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getCameraYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bC" : "cameraYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setCameraYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bC" : "cameraYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getCapabilitiesField", isObfuscated ? "()Laeb;" : "()Lnet/minecraft/entity/player/PlayerCapabilities;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bO" : "capabilities", isObfuscated ? "Laeb;" : "Lnet/minecraft/entity/player/PlayerCapabilities;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setCapabilitiesField", isObfuscated ? "(Laeb;)V" : "(Lnet/minecraft/entity/player/PlayerCapabilities;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bO" : "capabilities", isObfuscated ? "Laeb;" : "Lnet/minecraft/entity/player/PlayerCapabilities;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getChasingPosXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bH" : "chasingPosX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setChasingPosXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bH" : "chasingPosX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getChasingPosYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bI" : "chasingPosY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setChasingPosYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bI" : "chasingPosY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getChasingPosZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bJ" : "chasingPosZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setChasingPosZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bJ" : "chasingPosZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getChunkCoordXField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ab" : "chunkCoordX", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setChunkCoordXField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ab" : "chunkCoordX", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getChunkCoordYField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ac" : "chunkCoordY", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setChunkCoordYField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ac" : "chunkCoordY", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getChunkCoordZField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ad" : "chunkCoordZ", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setChunkCoordZField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ad" : "chunkCoordZ", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDataWatcherField", isObfuscated ? "()Lnb;" : "()Lnet/minecraft/network/datasync/EntityDataManager;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "Y" : "dataManager", isObfuscated ? "Lnb;" : "Lnet/minecraft/network/datasync/EntityDataManager;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDataWatcherField", isObfuscated ? "(Lnb;)V" : "(Lnet/minecraft/network/datasync/EntityDataManager;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "Y" : "dataManager", isObfuscated ? "Lnb;" : "Lnet/minecraft/network/datasync/EntityDataManager;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDeadField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aU" : "dead", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDeadField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aU" : "dead", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDeathTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aB" : "deathTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDeathTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aB" : "deathTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDimensionField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "am" : "dimension", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDimensionField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "am" : "dimension", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDistanceWalkedModifiedField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "J" : "distanceWalkedModified", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDistanceWalkedModifiedField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "J" : "distanceWalkedModified", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDistanceWalkedOnStepModifiedField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "K" : "distanceWalkedOnStepModified", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDistanceWalkedOnStepModifiedField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "K" : "distanceWalkedOnStepModified", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getEnderChestField", isObfuscated ? "()Lagm;" : "()Lnet/minecraft/inventory/InventoryEnderChest;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bw" : "enderChest", isObfuscated ? "Lagm;" : "Lnet/minecraft/inventory/InventoryEnderChest;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setEnderChestField", isObfuscated ? "(Lagm;)V" : "(Lnet/minecraft/inventory/InventoryEnderChest;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bw" : "enderChest", isObfuscated ? "Lagm;" : "Lnet/minecraft/inventory/InventoryEnderChest;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getEntityCollisionReductionField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "R" : "entityCollisionReduction", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setEntityCollisionReductionField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "R" : "entityCollisionReduction", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getEntityUniqueIDField", "()Ljava/util/UUID;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aq" : "entityUniqueID", "Ljava/util/UUID;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setEntityUniqueIDField", "(Ljava/util/UUID;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aq" : "entityUniqueID", "Ljava/util/UUID;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getExperienceField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bR" : "experience", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setExperienceField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bR" : "experience", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getExperienceLevelField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bP" : "experienceLevel", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setExperienceLevelField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bP" : "experienceLevel", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getExperienceTotalField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bQ" : "experienceTotal", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setExperienceTotalField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bQ" : "experienceTotal", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getFallDistanceField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "L" : "fallDistance", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setFallDistanceField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "L" : "fallDistance", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getFirstUpdateField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "W" : "firstUpdate", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setFirstUpdateField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "W" : "firstUpdate", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getFishEntityField", isObfuscated ? "()Lacf;" : "()Lnet/minecraft/entity/projectile/EntityFishHook;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bU" : "fishEntity", isObfuscated ? "Lacf;" : "Lnet/minecraft/entity/projectile/EntityFishHook;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setFishEntityField", isObfuscated ? "(Lacf;)V" : "(Lnet/minecraft/entity/projectile/EntityFishHook;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bU" : "fishEntity", isObfuscated ? "Lacf;" : "Lnet/minecraft/entity/projectile/EntityFishHook;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getFlagsField", isObfuscated ? "()Lmy;" : "()Lnet/minecraft/network/datasync/DataParameter;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "Z" : "FLAGS", isObfuscated ? "Lmy;" : "Lnet/minecraft/network/datasync/DataParameter;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getFlyToggleTimerField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bA" : "flyToggleTimer", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setFlyToggleTimerField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bA" : "flyToggleTimer", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getFoodStatsField", isObfuscated ? "()Lafp;" : "()Lnet/minecraft/util/FoodStats;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bz" : "foodStats", isObfuscated ? "Lafp;" : "Lnet/minecraft/util/FoodStats;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setFoodStatsField", isObfuscated ? "(Lafp;)V" : "(Lnet/minecraft/util/FoodStats;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bz" : "foodStats", isObfuscated ? "Lafp;" : "Lnet/minecraft/util/FoodStats;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getForceSpawnField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "k" : "forceSpawn", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setForceSpawnField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "k" : "forceSpawn", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getGlowingField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "as" : "glowing", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setGlowingField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "as" : "glowing", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHandActiveField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cr" : "handActive", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHandActiveField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cr" : "handActive", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHandStatesField", isObfuscated ? "()Lmy;" : "()Lnet/minecraft/network/datasync/DataParameter;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "at" : "HAND_STATES", isObfuscated ? "Lmy;" : "Lnet/minecraft/network/datasync/DataParameter;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHasValidHealthField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cn" : "hasValidHealth", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHasValidHealthField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cn" : "hasValidHealth", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHeightField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "H" : "height", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHeightField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "H" : "height", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHorseJumpPowerField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cq" : "horseJumpPower", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHorseJumpPowerField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cq" : "horseJumpPower", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHorseJumpPowerCounterField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cp" : "horseJumpPowerCounter", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHorseJumpPowerCounterField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cp" : "horseJumpPowerCounter", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHundredEightyField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ba" : "unused180", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHundredEightyField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ba" : "unused180", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHurtResistantTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "V" : "hurtResistantTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHurtResistantTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "V" : "hurtResistantTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHurtTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ay" : "hurtTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHurtTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ay" : "hurtTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIdleTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aV" : "idleTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIdleTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aV" : "idleTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIgnoreFrustumCheckField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ah" : "ignoreFrustumCheck", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIgnoreFrustumCheckField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ah" : "ignoreFrustumCheck", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInPortalField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ak" : "inPortal", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInPortalField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ak" : "inPortal", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInWaterField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "U" : "inWater", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInWaterField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "U" : "inWater", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInterpTargetXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bj" : "interpEntityPlayerSPX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInterpTargetXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bj" : "interpEntityPlayerSPX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInterpTargetYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bk" : "interpEntityPlayerSPY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInterpTargetYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bk" : "interpEntityPlayerSPY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInterpTargetYawField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bm" : "interpEntityPlayerSPYaw", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInterpTargetYawField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bm" : "interpEntityPlayerSPYaw", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInterpTargetZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bl" : "interpEntityPlayerSPZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInterpTargetZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bl" : "interpEntityPlayerSPZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInventoryField", isObfuscated ? "()Laec;" : "()Lnet/minecraft/entity/player/InventoryPlayer;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bv" : "inventory", isObfuscated ? "Laec;" : "Lnet/minecraft/entity/player/InventoryPlayer;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInventoryField", isObfuscated ? "(Laec;)V" : "(Lnet/minecraft/entity/player/InventoryPlayer;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bv" : "inventory", isObfuscated ? "Laec;" : "Lnet/minecraft/entity/player/InventoryPlayer;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInventoryContainerField", isObfuscated ? "()Lafr;" : "()Lnet/minecraft/inventory/Container;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bx" : "inventoryContainer", isObfuscated ? "Lafr;" : "Lnet/minecraft/inventory/Container;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInventoryContainerField", isObfuscated ? "(Lafr;)V" : "(Lnet/minecraft/inventory/Container;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bx" : "inventoryContainer", isObfuscated ? "Lafr;" : "Lnet/minecraft/inventory/Container;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsAirBorneField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ai" : "isAirBorne", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsAirBorneField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ai" : "isAirBorne", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsCollidedField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "C" : "isCollided", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsCollidedField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "C" : "isCollided", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsCollidedHorizontallyField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "A" : "isCollidedHorizontally", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsCollidedHorizontallyField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "A" : "isCollidedHorizontally", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsCollidedVerticallyField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "B" : "isCollidedVertically", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsCollidedVerticallyField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "B" : "isCollidedVertically", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsDeadField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "F" : "isDead", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsDeadField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "F" : "isDead", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsElytraFlyingField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cw" : "wasFallFlying", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsElytraFlyingField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cw" : "wasFallFlying", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsImmuneToFireField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "X" : "isImmuneToFire", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsImmuneToFireField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "X" : "isImmuneToFire", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsInWebField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "E" : "isInWeb", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsInWebField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "E" : "isInWeb", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsJumpingField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bd" : "isJumping", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsJumpingField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bd" : "isJumping", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsSwingInProgressField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "au" : "isSwingInProgress", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsSwingInProgressField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "au" : "isSwingInProgress", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getJumpMovementFactorField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aR" : "jumpMovementFactor", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setJumpMovementFactorField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aR" : "jumpMovementFactor", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastDamageField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bc" : "lastDamage", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastDamageField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bc" : "lastDamage", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastPortalPosField", isObfuscated ? "()Let;" : "()Lnet/minecraft/util/math/BlockPos;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "an" : "lastPortalPos", isObfuscated ? "Let;" : "Lnet/minecraft/util/math/BlockPos;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastPortalPosField", isObfuscated ? "(Let;)V" : "(Lnet/minecraft/util/math/BlockPos;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "an" : "lastPortalPos", isObfuscated ? "Let;" : "Lnet/minecraft/util/math/BlockPos;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastPortalVecField", isObfuscated ? "()Lbhe;" : "()Lnet/minecraft/util/math/Vec3d;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ao" : "lastPortalVec", isObfuscated ? "Lbhe;" : "Lnet/minecraft/util/math/Vec3d;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastPortalVecField", isObfuscated ? "(Lbhe;)V" : "(Lnet/minecraft/util/math/Vec3d;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ao" : "lastPortalVec", isObfuscated ? "Lbhe;" : "Lnet/minecraft/util/math/Vec3d;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastReportedPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ci" : "lastReportedPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastReportedPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ci" : "lastReportedPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastReportedPosXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ce" : "lastReportedPosX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastReportedPosXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ce" : "lastReportedPosX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastReportedPosYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cf" : "lastReportedPosY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastReportedPosYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cf" : "lastReportedPosY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastReportedPosZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cg" : "lastReportedPosZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastReportedPosZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cg" : "lastReportedPosZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastReportedYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ch" : "lastReportedYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastReportedYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ch" : "lastReportedYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastTickPosXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "M" : "lastTickPosX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastTickPosXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "M" : "lastTickPosX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastTickPosYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "N" : "lastTickPosY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastTickPosYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "N" : "lastTickPosY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastTickPosZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "O" : "lastTickPosZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastTickPosZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "O" : "lastTickPosZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLimbSwingField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aH" : "limbSwing", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLimbSwingField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aH" : "limbSwing", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLimbSwingAmountField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aG" : "limbSwingAmount", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLimbSwingAmountField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aG" : "limbSwingAmount", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMainHandField", isObfuscated ? "()Lmy;" : "()Lnet/minecraft/network/datasync/DataParameter;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bs" : "MAIN_HAND", isObfuscated ? "Lmy;" : "Lnet/minecraft/network/datasync/DataParameter;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMaxHurtResistantTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aI" : "maxHurtResistantTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMaxHurtResistantTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aI" : "maxHurtResistantTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMaxHurtTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "az" : "maxHurtTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMaxHurtTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "az" : "maxHurtTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMcField", isObfuscated ? "()Lbib;" : "()Lnet/minecraft/client/Minecraft;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "f" : "mc", isObfuscated ? "Lbib;" : "Lnet/minecraft/client/Minecraft;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMcField", isObfuscated ? "(Lbib;)V" : "(Lnet/minecraft/client/Minecraft;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "f" : "mc", isObfuscated ? "Lbib;" : "Lnet/minecraft/client/Minecraft;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMotionXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "s" : "motionX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMotionXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "s" : "motionX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMotionYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "t" : "motionY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMotionYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "t" : "motionY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMotionZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "u" : "motionZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMotionZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "u" : "motionZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMoveForwardField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bg" : "moveForward", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMoveForwardField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bg" : "moveForward", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMoveStrafingField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "be" : "moveStrafing", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMoveStrafingField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "be" : "moveStrafing", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMoveVerticalField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bf" : "moveVertical", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMoveVerticalField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bf" : "moveVertical", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMovedDistanceField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aY" : "movedDistance", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMovedDistanceField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aY" : "movedDistance", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMovementInputField", isObfuscated ? "()Lbub;" : "()Lnet/minecraft/util/MovementInput;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "e" : "movementInput", isObfuscated ? "Lbub;" : "Lnet/minecraft/util/MovementInput;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMovementInputField", isObfuscated ? "(Lbub;)V" : "(Lnet/minecraft/util/MovementInput;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "e" : "movementInput", isObfuscated ? "Lbub;" : "Lnet/minecraft/util/MovementInput;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getNewPosRotationIncrementsField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bi" : "newPosRotationIncrements", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setNewPosRotationIncrementsField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bi" : "newPosRotationIncrements", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getNewPosXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bn" : "interpEntityPlayerSPPitch", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setNewPosXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bn" : "interpEntityPlayerSPPitch", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getNoClipField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "Q" : "noClip", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setNoClipField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "Q" : "noClip", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getOnGroundField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "z" : "onGround", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setOnGroundField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "z" : "onGround", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getOnGroundSpeedFactorField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aX" : "onGroundSpeedFactor", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setOnGroundSpeedFactorField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aX" : "onGroundSpeedFactor", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getOpenContainerField", isObfuscated ? "()Lafr;" : "()Lnet/minecraft/inventory/Container;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "by" : "openContainer", isObfuscated ? "Lafr;" : "Lnet/minecraft/inventory/Container;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setOpenContainerField", isObfuscated ? "(Lafr;)V" : "(Lnet/minecraft/inventory/Container;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "by" : "openContainer", isObfuscated ? "Lafr;" : "Lnet/minecraft/inventory/Container;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPermissionLevelField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cd" : "permissionLevel", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPermissionLevelField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cd" : "permissionLevel", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPlayerLocationField", isObfuscated ? "()Let;" : "()Lnet/minecraft/util/math/BlockPos;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bL" : "bedLocation", isObfuscated ? "Let;" : "Lnet/minecraft/util/math/BlockPos;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPlayerLocationField", isObfuscated ? "(Let;)V" : "(Lnet/minecraft/util/math/BlockPos;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bL" : "bedLocation", isObfuscated ? "Let;" : "Lnet/minecraft/util/math/BlockPos;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPlayerModelFlagField", isObfuscated ? "()Lmy;" : "()Lnet/minecraft/network/datasync/DataParameter;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "br" : "PLAYER_MODEL_FLAG", isObfuscated ? "Lmy;" : "Lnet/minecraft/network/datasync/DataParameter;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPortalCounterField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "al" : "portalCounter", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPortalCounterField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "al" : "portalCounter", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPosXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "p" : "posX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPosXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "p" : "posX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPosYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "q" : "posY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPosYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "q" : "posY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPosZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "r" : "posZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPosZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "r" : "posZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPositionUpdateTicksField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cm" : "positionUpdateTicks", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPositionUpdateTicksField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cm" : "positionUpdateTicks", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevCameraPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aJ" : "prevCameraPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevCameraPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aJ" : "prevCameraPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevCameraYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bB" : "prevCameraYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevCameraYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bB" : "prevCameraYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevChasingPosXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bE" : "prevChasingPosX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevChasingPosXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bE" : "prevChasingPosX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevChasingPosYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bF" : "prevChasingPosY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevChasingPosYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bF" : "prevChasingPosY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevChasingPosZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bG" : "prevChasingPosZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevChasingPosZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bG" : "prevChasingPosZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevDistanceWalkedModifiedField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "I" : "prevDistanceWalkedModified", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevDistanceWalkedModifiedField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "I" : "prevDistanceWalkedModified", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevLimbSwingAmountField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aF" : "prevLimbSwingAmount", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevLimbSwingAmountField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aF" : "prevLimbSwingAmount", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevMovedDistanceField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aZ" : "prevMovedDistance", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevMovedDistanceField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aZ" : "prevMovedDistance", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevOnGroundField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cj" : "prevOnGround", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevOnGroundField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cj" : "prevOnGround", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevOnGroundSpeedFactorField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aW" : "prevOnGroundSpeedFactor", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevOnGroundSpeedFactorField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aW" : "prevOnGroundSpeedFactor", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevPosXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "m" : "prevPosX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevPosXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "m" : "prevPosX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevPosYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "n" : "prevPosY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevPosYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "n" : "prevPosY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevPosZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "o" : "prevPosZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevPosZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "o" : "prevPosZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRenderArmPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bY" : "prevRenderArmPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRenderArmPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bY" : "prevRenderArmPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRenderArmYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bX" : "prevRenderArmYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRenderArmYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bX" : "prevRenderArmYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRenderYawOffsetField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aO" : "prevRenderYawOffset", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRenderYawOffsetField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aO" : "prevRenderYawOffset", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRotationPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "y" : "prevRotationPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRotationPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "y" : "prevRotationPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRotationYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "x" : "prevRotationYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRotationYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "x" : "prevRotationYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRotationYawHeadField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aQ" : "prevRotationYawHead", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRotationYawHeadField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aQ" : "prevRotationYawHead", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevSwingProgressField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aC" : "prevSwingProgress", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevSwingProgressField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aC" : "prevSwingProgress", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevTimeInPortalField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ca" : "prevTimeInPortal", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevTimeInPortalField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ca" : "prevTimeInPortal", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPreventEntitySpawningField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "i" : "preventEntitySpawning", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPreventEntitySpawningField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "i" : "preventEntitySpawning", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRandField", "()Ljava/util/Random;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "S" : "rand", "Ljava/util/Random;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRandField", "(Ljava/util/Random;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "S" : "rand", "Ljava/util/Random;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRandomNumberBetweenOneHundredthAndTwoHundredthField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aM" : "randomUnused1", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRandomNumberBetweenOneHundredthAndTwoHundredthField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aM" : "randomUnused1", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRandomNumberBetweenZeroAndTwelveThousandThreeHundredNinetyEightField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aL" : "randomUnused2", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRandomNumberBetweenZeroAndTwelveThousandThreeHundredNinetyEightField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aL" : "randomUnused2", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRandomYawVelocityField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bh" : "randomYawVelocity", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRandomYawVelocityField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bh" : "randomYawVelocity", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRecentlyHitField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aT" : "recentlyHit", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRecentlyHitField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aT" : "recentlyHit", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRecipeBookField", isObfuscated ? "()Lql;" : "()Lnet/minecraft/stats/RecipeBook;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cc" : "recipeBook", isObfuscated ? "Lql;" : "Lnet/minecraft/stats/RecipeBook;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRenderArmPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bW" : "renderArmPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRenderArmPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bW" : "renderArmPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRenderArmYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bV" : "renderArmYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRenderArmYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bV" : "renderArmYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRenderOffsetXField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bM" : "renderOffsetX", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRenderOffsetXField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bM" : "renderOffsetX", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRenderOffsetYField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cx" : "renderOffsetY", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRenderOffsetYField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cx" : "renderOffsetY", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRenderOffsetZField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bN" : "renderOffsetZ", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRenderOffsetZField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bN" : "renderOffsetZ", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRenderYawOffsetField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aN" : "renderYawOffset", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRenderYawOffsetField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aN" : "renderYawOffset", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRideCooldownField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "j" : "rideCooldown", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRideCooldownField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "j" : "rideCooldown", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRotationPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "w" : "rotationPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRotationPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "w" : "rotationPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRotationYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "v" : "rotationYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRotationYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "v" : "rotationYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRotationYawHeadField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aP" : "rotationYawHead", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRotationYawHeadField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aP" : "rotationYawHead", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRowingBoatField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ct" : "rowingBoat", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRowingBoatField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ct" : "rowingBoat", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getScoreValueField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bb" : "scoreValue", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setScoreValueField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bb" : "scoreValue", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSendQueueField", isObfuscated ? "()Lbrz;" : "()Lnet/minecraft/client/network/NetHandlerPlayClient;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "d" : "connection", isObfuscated ? "Lbrz;" : "Lnet/minecraft/client/network/NetHandlerPlayClient;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getServerBrandField", "()Ljava/lang/String;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "co" : "serverBrand", "Ljava/lang/String;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setServerBrandField", "(Ljava/lang/String;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "co" : "serverBrand", "Ljava/lang/String;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getServerPosXField", "()J", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ae" : "serverPosX", "J");
		mv.visitInsn(Opcodes.LRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setServerPosXField", "(J)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.LLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ae" : "serverPosX", "J");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getServerPosYField", "()J", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "af" : "serverPosY", "J");
		mv.visitInsn(Opcodes.LRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setServerPosYField", "(J)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.LLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "af" : "serverPosY", "J");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getServerPosZField", "()J", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ag" : "serverPosZ", "J");
		mv.visitInsn(Opcodes.LRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setServerPosZField", "(J)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.LLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ag" : "serverPosZ", "J");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getServerSneakStateField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ck" : "serverSneakState", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setServerSneakStateField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ck" : "serverSneakState", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getServerSprintStateField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cl" : "serverSprintState", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setServerSprintStateField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cl" : "serverSprintState", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSleepingField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bK" : "sleeping", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSleepingField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bK" : "sleeping", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSpeedInAirField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bT" : "speedInAir", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSpeedInAirField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bT" : "speedInAir", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSprintToggleTimerField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "g" : "sprintToggleTimer", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSprintToggleTimerField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "g" : "sprintToggleTimer", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSprintingTicksLeftField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "h" : "sprintingTicksLeft", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSprintingTicksLeftField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "h" : "sprintingTicksLeft", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getStatWriterField", isObfuscated ? "()Lqt;" : "()Lnet/minecraft/stats/StatisticsManager;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cb" : "statWriter", isObfuscated ? "Lqt;" : "Lnet/minecraft/stats/StatisticsManager;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getStepHeightField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "P" : "stepHeight", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setStepHeightField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "P" : "stepHeight", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSwingProgressField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aD" : "swingProgress", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSwingProgressField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aD" : "swingProgress", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSwingProgressIntField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aw" : "swingProgressInt", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSwingProgressIntField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aw" : "swingProgressInt", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSwingingHandField", isObfuscated ? "()Lub;" : "()Lnet/minecraft/util/EnumHand;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "av" : "swingingHand", isObfuscated ? "Lub;" : "Lnet/minecraft/util/EnumHand;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSwingingHandField", isObfuscated ? "(Lub;)V" : "(Lnet/minecraft/util/EnumHand;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "av" : "swingingHand", isObfuscated ? "Lub;" : "Lnet/minecraft/util/EnumHand;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTeleportDirectionField", isObfuscated ? "()Lfa;" : "()Lnet/minecraft/util/EnumFacing;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ap" : "teleportDirection", isObfuscated ? "Lfa;" : "Lnet/minecraft/util/EnumFacing;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTeleportDirectionField", isObfuscated ? "(Lfa;)V" : "(Lnet/minecraft/util/EnumFacing;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ap" : "teleportDirection", isObfuscated ? "Lfa;" : "Lnet/minecraft/util/EnumFacing;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTicksElytraFlyingField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bq" : "ticksElytraFlying", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTicksElytraFlyingField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bq" : "ticksElytraFlying", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTicksExistedField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "T" : "ticksExisted", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTicksExistedField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "T" : "ticksExisted", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTicksSinceLastSwingField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aE" : "ticksSinceLastSwing", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTicksSinceLastSwingField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aE" : "ticksSinceLastSwing", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTimeInPortalField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bZ" : "timeInPortal", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTimeInPortalField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bZ" : "timeInPortal", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTimeUntilPortalField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aj" : "timeUntilPortal", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTimeUntilPortalField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aj" : "timeUntilPortal", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getVelocityChangedField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "D" : "velocityChanged", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setVelocityChangedField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "D" : "velocityChanged", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getWidthField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "G" : "width", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setWidthField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "G" : "width", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getWorldObjField", isObfuscated ? "()Lamu;" : "()Lnet/minecraft/world/World;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "l" : "world", isObfuscated ? "Lamu;" : "Lnet/minecraft/world/World;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setWorldObjField", isObfuscated ? "(Lamu;)V" : "(Lnet/minecraft/world/World;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "l" : "world", isObfuscated ? "Lamu;" : "Lnet/minecraft/world/World;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getXpCooldownField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bD" : "xpCooldown", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setXpCooldownField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bD" : "xpCooldown", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getXpSeedField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bS" : "xpSeed", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setXpSeedField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bS" : "xpSeed", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getClientPlayerBase", "(Ljava/lang/String;)Lapi/player/client/ClientPlayerBase;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getClientPlayerBase", "(Lapi/player/client/IClientPlayerAPI;Ljava/lang/String;)Lapi/player/client/ClientPlayerBase;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getClientPlayerBaseIds", "()Ljava/util/Set;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getClientPlayerBaseIds", "(Lapi/player/client/IClientPlayerAPI;)Ljava/util/Set;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dynamic", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "dynamic", "(Lapi/player/client/IClientPlayerAPI;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getClientPlayerAPI", "()Lapi/player/client/ClientPlayerAPI;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "bud" : "net/minecraft/client/entity/EntityPlayerSP", "clientPlayerAPI", "Lapi/player/client/ClientPlayerAPI;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getEntityPlayerSP", isObfuscated ? "()Lbud;" : "()Lnet/minecraft/client/entity/EntityPlayerSP;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		cv.visitField(Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, "clientPlayerAPI", "Lapi/player/client/ClientPlayerAPI;", null, null);
	}
}
