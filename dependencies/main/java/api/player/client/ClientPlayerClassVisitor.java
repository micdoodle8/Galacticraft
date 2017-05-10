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
	private boolean hadLocalDisplayGUIBrewingStand;
	private boolean hadLocalDisplayGUIChest;
	private boolean hadLocalDisplayGUIDispenser;
	private boolean hadLocalDisplayGUIEditSign;
	private boolean hadLocalDisplayGUIEnchantment;
	private boolean hadLocalDisplayGUIFurnace;
	private boolean hadLocalDisplayGUIWorkbench;
	private boolean hadLocalDropOneItem;
	private boolean hadLocalDropPlayerItem;
	private boolean hadLocalDropPlayerItemWithRandomChoice;
	private boolean hadLocalFall;
	private boolean hadLocalGetAIMoveSpeed;
	private boolean hadLocalGetBedOrientationInDegrees;
	private boolean hadLocalGetBrightness;
	private boolean hadLocalGetBrightnessForRender;
	private boolean hadLocalGetCurrentPlayerStrVsBlock;
	private boolean hadLocalGetCurrentPlayerStrVsBlockForge;
	private boolean hadLocalGetDistanceSq;
	private boolean hadLocalGetDistanceSqToEntity;
	private boolean hadLocalGetFOVMultiplier;
	private boolean hadLocalGetHurtSound;
	private boolean hadLocalGetItemIcon;
	private boolean hadLocalGetSleepTimer;
	private boolean hadLocalHandleLavaMovement;
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
	private boolean hadLocalSleepInBedAt;
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

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		String[] newInterfaces = new String[interfaces.length + 1];
		for(int i=0; i<interfaces.length; i++)
			newInterfaces[i] = interfaces[i];
		newInterfaces[interfaces.length] = "api/player/client/IClientPlayerAPI";
		super.visit(version, access, name, signature, superName, newInterfaces);
	}

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

		if(name.equals(isObfuscated ? "a" : "addStat") && desc.equals(isObfuscated ? "(Lph;I)V" : "(Lnet/minecraft/stats/StatBase;I)V"))
		{
			hadLocalAddStat = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAddStat", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "attackEntityFrom") && desc.equals(isObfuscated ? "(Lro;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z"))
		{
			hadLocalAttackEntityFrom = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAttackEntityFrom", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "r" : "attackEntityPlayerSPEntityWithCurrentItem") && desc.equals(isObfuscated ? "(Lsa;)V" : "(Lnet/minecraft/entity/Entity;)V"))
		{
			hadLocalAttackTargetEntityWithCurrentItem = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAttackTargetEntityWithCurrentItem", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "aE" : "canBreatheUnderwater") && desc.equals("()Z"))
		{
			hadLocalCanBreatheUnderwater = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanBreatheUnderwater", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "canHarvestBlock") && desc.equals(isObfuscated ? "(Laji;)Z" : "(Lnet/minecraft/block/Block;)Z"))
		{
			hadLocalCanHarvestBlock = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanHarvestBlock", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "canPlayerEdit") && desc.equals(isObfuscated ? "(IIIILadd;)Z" : "(IIIILnet/minecraft/item/ItemStack;)Z"))
		{
			hadLocalCanPlayerEdit = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanPlayerEdit", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "g_" : "canTriggerWalking") && desc.equals("()Z"))
		{
			hadLocalCanTriggerWalking = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanTriggerWalking", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "k" : "closeScreen") && desc.equals("()V"))
		{
			hadLocalCloseScreen = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCloseScreen", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "d" : "damageEntity") && desc.equals(isObfuscated ? "(Lro;F)V" : "(Lnet/minecraft/util/DamageSource;F)V"))
		{
			hadLocalDamageEntity = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDamageEntity", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "func_146098_a") && desc.equals(isObfuscated ? "(Laov;)V" : "(Lnet/minecraft/tileentity/TileEntityBrewingStand;)V"))
		{
			hadLocalDisplayGUIBrewingStand = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIBrewingStand", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "displayGUIChest") && desc.equals(isObfuscated ? "(Lrb;)V" : "(Lnet/minecraft/inventory/IInventory;)V"))
		{
			hadLocalDisplayGUIChest = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIChest", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "func_146102_a") && desc.equals(isObfuscated ? "(Lapb;)V" : "(Lnet/minecraft/tileentity/TileEntityDispenser;)V"))
		{
			hadLocalDisplayGUIDispenser = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIDispenser", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "func_146100_a") && desc.equals(isObfuscated ? "(Laor;)V" : "(Lnet/minecraft/tileentity/TileEntity;)V"))
		{
			hadLocalDisplayGUIEditSign = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIEditSign", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "displayGUIEnchantment") && desc.equals("(IIILjava/lang/String;)V"))
		{
			hadLocalDisplayGUIEnchantment = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIEnchantment", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "func_146101_a") && desc.equals(isObfuscated ? "(Lapg;)V" : "(Lnet/minecraft/tileentity/TileEntityFurnace;)V"))
		{
			hadLocalDisplayGUIFurnace = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIFurnace", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "b" : "displayGUIWorkbench") && desc.equals("(III)V"))
		{
			hadLocalDisplayGUIWorkbench = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIWorkbench", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "dropOneItem") && desc.equals(isObfuscated ? "(Z)Lxk;" : "(Z)Lnet/minecraft/entity/item/EntityItem;"))
		{
			hadLocalDropOneItem = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropOneItem", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "dropPlayerItemWithRandomChoice") && desc.equals(isObfuscated ? "(Ladd;Z)Lxk;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;"))
		{
			hadLocalDropPlayerItem = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropPlayerItem", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "func_146097_a") && desc.equals(isObfuscated ? "(Ladd;ZZ)Lxk;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;"))
		{
			hadLocalDropPlayerItemWithRandomChoice = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropPlayerItemWithRandomChoice", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "b" : "fall") && desc.equals("(F)V"))
		{
			hadLocalFall = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localFall", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "bl" : "getAIMoveSpeed") && desc.equals("()F"))
		{
			hadLocalGetAIMoveSpeed = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetAIMoveSpeed", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "bK" : "getBedOrientationInDegrees") && desc.equals("()F"))
		{
			hadLocalGetBedOrientationInDegrees = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBedOrientationInDegrees", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "d" : "getBrightness") && desc.equals("(F)F"))
		{
			hadLocalGetBrightness = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBrightness", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "c" : "getBrightnessForRender") && desc.equals("(F)I"))
		{
			hadLocalGetBrightnessForRender = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBrightnessForRender", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "getCurrentPlayerStrVsBlock") && desc.equals(isObfuscated ? "(Laji;Z)F" : "(Lnet/minecraft/block/Block;Z)F"))
		{
			hadLocalGetCurrentPlayerStrVsBlock = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetCurrentPlayerStrVsBlock", desc, signature, exceptions);
		}

		if(name.equals("getBreakSpeed") && desc.equals(isObfuscated ? "(Laji;ZI)F" : "(Lnet/minecraft/block/Block;ZI)F"))
		{
			hadLocalGetCurrentPlayerStrVsBlockForge = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetCurrentPlayerStrVsBlockForge", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "e" : "getDistanceSq") && desc.equals("(DDD)D"))
		{
			hadLocalGetDistanceSq = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetDistanceSq", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "f" : "getDistanceSqToEntity") && desc.equals(isObfuscated ? "(Lsa;)D" : "(Lnet/minecraft/entity/Entity;)D"))
		{
			hadLocalGetDistanceSqToEntity = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetDistanceSqToEntity", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "t" : "getFOVMultiplier") && desc.equals("()F"))
		{
			hadLocalGetFOVMultiplier = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetFOVMultiplier", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "aT" : "getHurtSound") && desc.equals("()Ljava/lang/String;"))
		{
			hadLocalGetHurtSound = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetHurtSound", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "b" : "getItemIcon") && desc.equals(isObfuscated ? "(Ladd;I)Lrf;" : "(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/util/IIcon;"))
		{
			hadLocalGetItemIcon = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetItemIcon", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "bM" : "getSleepTimer") && desc.equals("()I"))
		{
			hadLocalGetSleepTimer = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetSleepTimer", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "P" : "handleLavaMovement") && desc.equals("()Z"))
		{
			hadLocalHandleLavaMovement = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localHandleLavaMovement", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "N" : "handleWaterMovement") && desc.equals("()Z"))
		{
			hadLocalHandleWaterMovement = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localHandleWaterMovement", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "f" : "heal") && desc.equals("(F)V"))
		{
			hadLocalHeal = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localHeal", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "aa" : "isEntityInsideOpaqueBlock") && desc.equals("()Z"))
		{
			hadLocalIsEntityInsideOpaqueBlock = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsEntityInsideOpaqueBlock", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "M" : "isInWater") && desc.equals("()Z"))
		{
			hadLocalIsInWater = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsInWater", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "isInsideOfMaterial") && desc.equals(isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/material/Material;)Z"))
		{
			hadLocalIsInsideOfMaterial = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsInsideOfMaterial", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "h_" : "isOnLadder") && desc.equals("()Z"))
		{
			hadLocalIsOnLadder = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsOnLadder", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "bm" : "isPlayerSleeping") && desc.equals("()Z"))
		{
			hadLocalIsPlayerSleeping = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsPlayerSleeping", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "an" : "isSneaking") && desc.equals("()Z"))
		{
			hadLocalIsSneaking = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsSneaking", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "ao" : "isSprinting") && desc.equals("()Z"))
		{
			hadLocalIsSprinting = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsSprinting", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "bj" : "jump") && desc.equals("()V"))
		{
			hadLocalJump = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localJump", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "knockBack") && desc.equals(isObfuscated ? "(Lsa;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V"))
		{
			hadLocalKnockBack = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localKnockBack", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "d" : "moveEntity") && desc.equals("(DDD)V"))
		{
			hadLocalMoveEntity = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveEntity", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "e" : "moveEntityWithHeading") && desc.equals("(FF)V"))
		{
			hadLocalMoveEntityWithHeading = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveEntityWithHeading", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "moveFlying") && desc.equals("(FFF)V"))
		{
			hadLocalMoveFlying = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveFlying", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "onDeath") && desc.equals(isObfuscated ? "(Lro;)V" : "(Lnet/minecraft/util/DamageSource;)V"))
		{
			hadLocalOnDeath = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnDeath", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "e" : "onLivingUpdate") && desc.equals("()V"))
		{
			hadLocalOnLivingUpdate = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnLivingUpdate", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "onKillEntity") && desc.equals(isObfuscated ? "(Lsv;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V"))
		{
			hadLocalOnKillEntity = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnKillEntity", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "onStruckByLightning") && desc.equals(isObfuscated ? "(Lxh;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V"))
		{
			hadLocalOnStruckByLightning = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnStruckByLightning", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "h" : "onUpdate") && desc.equals("()V"))
		{
			hadLocalOnUpdate = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnUpdate", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "func_145780_a") && desc.equals(isObfuscated ? "(IIILaji;)V" : "(IIILnet/minecraft/block/Block;)V"))
		{
			hadLocalPlayStepSound = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localPlayStepSound", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "j" : "func_145771_j") && desc.equals("(DDD)Z"))
		{
			hadLocalPushOutOfBlocks = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localPushOutOfBlocks", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "rayTrace") && desc.equals(isObfuscated ? "(DF)Lazu;" : "(DF)Lnet/minecraft/util/MovingObjectPosition;"))
		{
			hadLocalRayTrace = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localRayTrace", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "readEntityFromNBT") && desc.equals(isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V"))
		{
			hadLocalReadEntityFromNBT = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localReadEntityFromNBT", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "bH" : "respawnPlayer") && desc.equals("()V"))
		{
			hadLocalRespawnPlayer = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localRespawnPlayer", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "B" : "setDead") && desc.equals("()V"))
		{
			hadLocalSetDead = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetDead", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "n" : "setPlayerSPHealth") && desc.equals("(F)V"))
		{
			hadLocalSetPlayerSPHealth = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetPlayerSPHealth", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "setPositionAndRotation") && desc.equals("(DDDFF)V"))
		{
			hadLocalSetPositionAndRotation = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetPositionAndRotation", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "b" : "setSneaking") && desc.equals("(Z)V"))
		{
			hadLocalSetSneaking = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetSneaking", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "c" : "setSprinting") && desc.equals("(Z)V"))
		{
			hadLocalSetSprinting = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetSprinting", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "sleepInBedAt") && desc.equals(isObfuscated ? "(III)Lza;" : "(III)Lnet/minecraft/entity/player/EntityPlayer$EnumStatus;"))
		{
			hadLocalSleepInBedAt = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSleepInBedAt", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "ba" : "swingItem") && desc.equals("()V"))
		{
			hadLocalSwingItem = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSwingItem", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "bq" : "updateEntityActionState") && desc.equals("()V"))
		{
			hadLocalUpdateEntityActionState = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localUpdateEntityActionState", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "ab" : "updateRidden") && desc.equals("()V"))
		{
			hadLocalUpdateRidden = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localUpdateRidden", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "wakeUpPlayer") && desc.equals("(ZZZ)V"))
		{
			hadLocalWakeUpPlayer = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localWakeUpPlayer", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "b" : "writeEntityToNBT") && desc.equals(isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V"))
		{
			hadLocalWriteEntityToNBT = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localWriteEntityToNBT", desc, signature, exceptions);
		}

		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	public void visitEnd()
	{
		MethodVisitor mv;

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "addExhaustion", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "addExhaustion", "(Lapi/player/client/IClientPlayerAPI;F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realAddExhaustion", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "addExhaustion", "(F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superAddExhaustion", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "addExhaustion", "(F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalAddExhaustion)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAddExhaustion", "(F)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "addExhaustion", "(F)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "k" : "addMovementStat", "(DDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "addMovementStat", "(Lapi/player/client/IClientPlayerAPI;DDD)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realAddMovementStat", "(DDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "k" : "addMovementStat", "(DDD)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superAddMovementStat", "(DDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "k" : "addMovementStat", "(DDD)V");
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
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "k" : "addMovementStat", "(DDD)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "addStat", "" + (isObfuscated ? "(Lph;I)V" : "(Lnet/minecraft/stats/StatBase;I)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "addStat", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lph;I" : "Lnet/minecraft/stats/StatBase;I") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realAddStat", "" + (isObfuscated ? "(Lph;I)V" : "(Lnet/minecraft/stats/StatBase;I)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "addStat", "" + (isObfuscated ? "(Lph;I)V" : "(Lnet/minecraft/stats/StatBase;I)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superAddStat", "" + (isObfuscated ? "(Lph;I)V" : "(Lnet/minecraft/stats/StatBase;I)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "addStat", "" + (isObfuscated ? "(Lph;I)V" : "(Lnet/minecraft/stats/StatBase;I)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalAddStat)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAddStat", "" + (isObfuscated ? "(Lph;I)V" : "(Lnet/minecraft/stats/StatBase;I)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "addStat", "" + (isObfuscated ? "(Lph;I)V" : "(Lnet/minecraft/stats/StatBase;I)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "attackEntityFrom", "" + (isObfuscated ? "(Lro;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "attackEntityFrom", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lro;F" : "Lnet/minecraft/util/DamageSource;F") + ")Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realAttackEntityFrom", "" + (isObfuscated ? "(Lro;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "attackEntityFrom", "" + (isObfuscated ? "(Lro;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z") + "");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superAttackEntityFrom", "" + (isObfuscated ? "(Lro;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "attackEntityFrom", "" + (isObfuscated ? "(Lro;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z") + "");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalAttackEntityFrom)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAttackEntityFrom", "" + (isObfuscated ? "(Lro;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "attackEntityFrom", "" + (isObfuscated ? "(Lro;F)Z" : "(Lnet/minecraft/util/DamageSource;F)Z") + "");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "r" : "attackEntityPlayerSPEntityWithCurrentItem", "" + (isObfuscated ? "(Lsa;)V" : "(Lnet/minecraft/entity/Entity;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "attackTargetEntityWithCurrentItem", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lsa;" : "Lnet/minecraft/entity/Entity;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realAttackTargetEntityWithCurrentItem", "" + (isObfuscated ? "(Lsa;)V" : "(Lnet/minecraft/entity/Entity;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "r" : "attackEntityPlayerSPEntityWithCurrentItem", "" + (isObfuscated ? "(Lsa;)V" : "(Lnet/minecraft/entity/Entity;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superAttackTargetEntityWithCurrentItem", "" + (isObfuscated ? "(Lsa;)V" : "(Lnet/minecraft/entity/Entity;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "r" : "attackEntityPlayerSPEntityWithCurrentItem", "" + (isObfuscated ? "(Lsa;)V" : "(Lnet/minecraft/entity/Entity;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalAttackTargetEntityWithCurrentItem)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localAttackTargetEntityWithCurrentItem", "" + (isObfuscated ? "(Lsa;)V" : "(Lnet/minecraft/entity/Entity;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "r" : "attackEntityPlayerSPEntityWithCurrentItem", "" + (isObfuscated ? "(Lsa;)V" : "(Lnet/minecraft/entity/Entity;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "aE" : "canBreatheUnderwater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "canBreatheUnderwater", "(Lapi/player/client/IClientPlayerAPI;)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realCanBreatheUnderwater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aE" : "canBreatheUnderwater", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superCanBreatheUnderwater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aE" : "canBreatheUnderwater", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalCanBreatheUnderwater)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanBreatheUnderwater", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aE" : "canBreatheUnderwater", "()Z");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "canHarvestBlock", "" + (isObfuscated ? "(Laji;)Z" : "(Lnet/minecraft/block/Block;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "canHarvestBlock", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Laji;" : "Lnet/minecraft/block/Block;") + ")Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realCanHarvestBlock", "" + (isObfuscated ? "(Laji;)Z" : "(Lnet/minecraft/block/Block;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "canHarvestBlock", "" + (isObfuscated ? "(Laji;)Z" : "(Lnet/minecraft/block/Block;)Z") + "");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superCanHarvestBlock", "" + (isObfuscated ? "(Laji;)Z" : "(Lnet/minecraft/block/Block;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "canHarvestBlock", "" + (isObfuscated ? "(Laji;)Z" : "(Lnet/minecraft/block/Block;)Z") + "");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalCanHarvestBlock)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanHarvestBlock", "" + (isObfuscated ? "(Laji;)Z" : "(Lnet/minecraft/block/Block;)Z") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "canHarvestBlock", "" + (isObfuscated ? "(Laji;)Z" : "(Lnet/minecraft/block/Block;)Z") + "");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "canPlayerEdit", "" + (isObfuscated ? "(IIIILadd;)Z" : "(IIIILnet/minecraft/item/ItemStack;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitVarInsn(Opcodes.ILOAD, 4);
		mv.visitVarInsn(Opcodes.ALOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "canPlayerEdit", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "IIIILadd;" : "IIIILnet/minecraft/item/ItemStack;") + ")Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realCanPlayerEdit", "" + (isObfuscated ? "(IIIILadd;)Z" : "(IIIILnet/minecraft/item/ItemStack;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitVarInsn(Opcodes.ILOAD, 4);
		mv.visitVarInsn(Opcodes.ALOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "canPlayerEdit", "" + (isObfuscated ? "(IIIILadd;)Z" : "(IIIILnet/minecraft/item/ItemStack;)Z") + "");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superCanPlayerEdit", "" + (isObfuscated ? "(IIIILadd;)Z" : "(IIIILnet/minecraft/item/ItemStack;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitVarInsn(Opcodes.ILOAD, 4);
		mv.visitVarInsn(Opcodes.ALOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "canPlayerEdit", "" + (isObfuscated ? "(IIIILadd;)Z" : "(IIIILnet/minecraft/item/ItemStack;)Z") + "");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalCanPlayerEdit)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanPlayerEdit", "" + (isObfuscated ? "(IIIILadd;)Z" : "(IIIILnet/minecraft/item/ItemStack;)Z") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitVarInsn(Opcodes.ILOAD, 4);
			mv.visitVarInsn(Opcodes.ALOAD, 5);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "canPlayerEdit", "" + (isObfuscated ? "(IIIILadd;)Z" : "(IIIILnet/minecraft/item/ItemStack;)Z") + "");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "g_" : "canTriggerWalking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "canTriggerWalking", "(Lapi/player/client/IClientPlayerAPI;)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realCanTriggerWalking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "g_" : "canTriggerWalking", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superCanTriggerWalking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "g_" : "canTriggerWalking", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalCanTriggerWalking)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCanTriggerWalking", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "g_" : "canTriggerWalking", "()Z");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "k" : "closeScreen", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "closeScreen", "(Lapi/player/client/IClientPlayerAPI;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realCloseScreen", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "k" : "closeScreen", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superCloseScreen", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "k" : "closeScreen", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalCloseScreen)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localCloseScreen", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "k" : "closeScreen", "()V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "d" : "damageEntity", "" + (isObfuscated ? "(Lro;F)V" : "(Lnet/minecraft/util/DamageSource;F)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "damageEntity", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lro;F" : "Lnet/minecraft/util/DamageSource;F") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDamageEntity", "" + (isObfuscated ? "(Lro;F)V" : "(Lnet/minecraft/util/DamageSource;F)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "d" : "damageEntity", "" + (isObfuscated ? "(Lro;F)V" : "(Lnet/minecraft/util/DamageSource;F)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDamageEntity", "" + (isObfuscated ? "(Lro;F)V" : "(Lnet/minecraft/util/DamageSource;F)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "damageEntity", "" + (isObfuscated ? "(Lro;F)V" : "(Lnet/minecraft/util/DamageSource;F)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDamageEntity)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDamageEntity", "" + (isObfuscated ? "(Lro;F)V" : "(Lnet/minecraft/util/DamageSource;F)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "damageEntity", "" + (isObfuscated ? "(Lro;F)V" : "(Lnet/minecraft/util/DamageSource;F)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "func_146098_a", "" + (isObfuscated ? "(Laov;)V" : "(Lnet/minecraft/tileentity/TileEntityBrewingStand;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "displayGUIBrewingStand", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Laov;" : "Lnet/minecraft/tileentity/TileEntityBrewingStand;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDisplayGUIBrewingStand", "" + (isObfuscated ? "(Laov;)V" : "(Lnet/minecraft/tileentity/TileEntityBrewingStand;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "func_146098_a", "" + (isObfuscated ? "(Laov;)V" : "(Lnet/minecraft/tileentity/TileEntityBrewingStand;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDisplayGUIBrewingStand", "" + (isObfuscated ? "(Laov;)V" : "(Lnet/minecraft/tileentity/TileEntityBrewingStand;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_146098_a", "" + (isObfuscated ? "(Laov;)V" : "(Lnet/minecraft/tileentity/TileEntityBrewingStand;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDisplayGUIBrewingStand)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIBrewingStand", "" + (isObfuscated ? "(Laov;)V" : "(Lnet/minecraft/tileentity/TileEntityBrewingStand;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_146098_a", "" + (isObfuscated ? "(Laov;)V" : "(Lnet/minecraft/tileentity/TileEntityBrewingStand;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "displayGUIChest", "" + (isObfuscated ? "(Lrb;)V" : "(Lnet/minecraft/inventory/IInventory;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "displayGUIChest", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lrb;" : "Lnet/minecraft/inventory/IInventory;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDisplayGUIChest", "" + (isObfuscated ? "(Lrb;)V" : "(Lnet/minecraft/inventory/IInventory;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "displayGUIChest", "" + (isObfuscated ? "(Lrb;)V" : "(Lnet/minecraft/inventory/IInventory;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDisplayGUIChest", "" + (isObfuscated ? "(Lrb;)V" : "(Lnet/minecraft/inventory/IInventory;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "displayGUIChest", "" + (isObfuscated ? "(Lrb;)V" : "(Lnet/minecraft/inventory/IInventory;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDisplayGUIChest)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIChest", "" + (isObfuscated ? "(Lrb;)V" : "(Lnet/minecraft/inventory/IInventory;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "displayGUIChest", "" + (isObfuscated ? "(Lrb;)V" : "(Lnet/minecraft/inventory/IInventory;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "func_146102_a", "" + (isObfuscated ? "(Lapb;)V" : "(Lnet/minecraft/tileentity/TileEntityDispenser;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "displayGUIDispenser", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lapb;" : "Lnet/minecraft/tileentity/TileEntityDispenser;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDisplayGUIDispenser", "" + (isObfuscated ? "(Lapb;)V" : "(Lnet/minecraft/tileentity/TileEntityDispenser;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "func_146102_a", "" + (isObfuscated ? "(Lapb;)V" : "(Lnet/minecraft/tileentity/TileEntityDispenser;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDisplayGUIDispenser", "" + (isObfuscated ? "(Lapb;)V" : "(Lnet/minecraft/tileentity/TileEntityDispenser;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_146102_a", "" + (isObfuscated ? "(Lapb;)V" : "(Lnet/minecraft/tileentity/TileEntityDispenser;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDisplayGUIDispenser)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIDispenser", "" + (isObfuscated ? "(Lapb;)V" : "(Lnet/minecraft/tileentity/TileEntityDispenser;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_146102_a", "" + (isObfuscated ? "(Lapb;)V" : "(Lnet/minecraft/tileentity/TileEntityDispenser;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "func_146100_a", "" + (isObfuscated ? "(Laor;)V" : "(Lnet/minecraft/tileentity/TileEntity;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "displayGUIEditSign", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Laor;" : "Lnet/minecraft/tileentity/TileEntity;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDisplayGUIEditSign", "" + (isObfuscated ? "(Laor;)V" : "(Lnet/minecraft/tileentity/TileEntity;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "func_146100_a", "" + (isObfuscated ? "(Laor;)V" : "(Lnet/minecraft/tileentity/TileEntity;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDisplayGUIEditSign", "" + (isObfuscated ? "(Laor;)V" : "(Lnet/minecraft/tileentity/TileEntity;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_146100_a", "" + (isObfuscated ? "(Laor;)V" : "(Lnet/minecraft/tileentity/TileEntity;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDisplayGUIEditSign)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIEditSign", "" + (isObfuscated ? "(Laor;)V" : "(Lnet/minecraft/tileentity/TileEntity;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_146100_a", "" + (isObfuscated ? "(Laor;)V" : "(Lnet/minecraft/tileentity/TileEntity;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "displayGUIEnchantment", "(IIILjava/lang/String;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitVarInsn(Opcodes.ALOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "displayGUIEnchantment", "(Lapi/player/client/IClientPlayerAPI;IIILjava/lang/String;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDisplayGUIEnchantment", "(IIILjava/lang/String;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitVarInsn(Opcodes.ALOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "displayGUIEnchantment", "(IIILjava/lang/String;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDisplayGUIEnchantment", "(IIILjava/lang/String;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitVarInsn(Opcodes.ALOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "displayGUIEnchantment", "(IIILjava/lang/String;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDisplayGUIEnchantment)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIEnchantment", "(IIILjava/lang/String;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitVarInsn(Opcodes.ALOAD, 4);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "displayGUIEnchantment", "(IIILjava/lang/String;)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "func_146101_a", "" + (isObfuscated ? "(Lapg;)V" : "(Lnet/minecraft/tileentity/TileEntityFurnace;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "displayGUIFurnace", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lapg;" : "Lnet/minecraft/tileentity/TileEntityFurnace;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDisplayGUIFurnace", "" + (isObfuscated ? "(Lapg;)V" : "(Lnet/minecraft/tileentity/TileEntityFurnace;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "func_146101_a", "" + (isObfuscated ? "(Lapg;)V" : "(Lnet/minecraft/tileentity/TileEntityFurnace;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDisplayGUIFurnace", "" + (isObfuscated ? "(Lapg;)V" : "(Lnet/minecraft/tileentity/TileEntityFurnace;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_146101_a", "" + (isObfuscated ? "(Lapg;)V" : "(Lnet/minecraft/tileentity/TileEntityFurnace;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDisplayGUIFurnace)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIFurnace", "" + (isObfuscated ? "(Lapg;)V" : "(Lnet/minecraft/tileentity/TileEntityFurnace;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_146101_a", "" + (isObfuscated ? "(Lapg;)V" : "(Lnet/minecraft/tileentity/TileEntityFurnace;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "b" : "displayGUIWorkbench", "(III)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "displayGUIWorkbench", "(Lapi/player/client/IClientPlayerAPI;III)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDisplayGUIWorkbench", "(III)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "b" : "displayGUIWorkbench", "(III)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDisplayGUIWorkbench", "(III)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "displayGUIWorkbench", "(III)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDisplayGUIWorkbench)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDisplayGUIWorkbench", "(III)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "displayGUIWorkbench", "(III)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "dropOneItem", "" + (isObfuscated ? "(Z)Lxk;" : "(Z)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "dropOneItem", "(Lapi/player/client/IClientPlayerAPI;Z)" + (isObfuscated ? "Lxk;" : "Lnet/minecraft/entity/item/EntityItem;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDropOneItem", "" + (isObfuscated ? "(Z)Lxk;" : "(Z)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "dropOneItem", "" + (isObfuscated ? "(Z)Lxk;" : "(Z)Lnet/minecraft/entity/item/EntityItem;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDropOneItem", "" + (isObfuscated ? "(Z)Lxk;" : "(Z)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "dropOneItem", "" + (isObfuscated ? "(Z)Lxk;" : "(Z)Lnet/minecraft/entity/item/EntityItem;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDropOneItem)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropOneItem", "" + (isObfuscated ? "(Z)Lxk;" : "(Z)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "dropOneItem", "" + (isObfuscated ? "(Z)Lxk;" : "(Z)Lnet/minecraft/entity/item/EntityItem;") + "");
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "dropPlayerItemWithRandomChoice", "" + (isObfuscated ? "(Ladd;Z)Lxk;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "dropPlayerItem", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Ladd;Z" : "Lnet/minecraft/item/ItemStack;Z") + ")" + (isObfuscated ? "Lxk;" : "Lnet/minecraft/entity/item/EntityItem;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDropPlayerItem", "" + (isObfuscated ? "(Ladd;Z)Lxk;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "dropPlayerItemWithRandomChoice", "" + (isObfuscated ? "(Ladd;Z)Lxk;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDropPlayerItem", "" + (isObfuscated ? "(Ladd;Z)Lxk;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "dropPlayerItemWithRandomChoice", "" + (isObfuscated ? "(Ladd;Z)Lxk;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDropPlayerItem)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropPlayerItem", "" + (isObfuscated ? "(Ladd;Z)Lxk;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "dropPlayerItemWithRandomChoice", "" + (isObfuscated ? "(Ladd;Z)Lxk;" : "(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;") + "");
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "func_146097_a", "" + (isObfuscated ? "(Ladd;ZZ)Lxk;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "dropPlayerItemWithRandomChoice", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Ladd;ZZ" : "Lnet/minecraft/item/ItemStack;ZZ") + ")" + (isObfuscated ? "Lxk;" : "Lnet/minecraft/entity/item/EntityItem;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realDropPlayerItemWithRandomChoice", "" + (isObfuscated ? "(Ladd;ZZ)Lxk;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "func_146097_a", "" + (isObfuscated ? "(Ladd;ZZ)Lxk;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superDropPlayerItemWithRandomChoice", "" + (isObfuscated ? "(Ladd;ZZ)Lxk;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_146097_a", "" + (isObfuscated ? "(Ladd;ZZ)Lxk;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalDropPlayerItemWithRandomChoice)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localDropPlayerItemWithRandomChoice", "" + (isObfuscated ? "(Ladd;ZZ)Lxk;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_146097_a", "" + (isObfuscated ? "(Ladd;ZZ)Lxk;" : "(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;") + "");
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "b" : "fall", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "fall", "(Lapi/player/client/IClientPlayerAPI;F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realFall", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "b" : "fall", "(F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superFall", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "fall", "(F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalFall)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localFall", "(F)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "fall", "(F)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "bl" : "getAIMoveSpeed", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getAIMoveSpeed", "(Lapi/player/client/IClientPlayerAPI;)F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetAIMoveSpeed", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bl" : "getAIMoveSpeed", "()F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetAIMoveSpeed", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bl" : "getAIMoveSpeed", "()F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetAIMoveSpeed)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetAIMoveSpeed", "()F", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bl" : "getAIMoveSpeed", "()F");
			mv.visitInsn(Opcodes.FRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "bK" : "getBedOrientationInDegrees", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getBedOrientationInDegrees", "(Lapi/player/client/IClientPlayerAPI;)F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetBedOrientationInDegrees", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bK" : "getBedOrientationInDegrees", "()F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetBedOrientationInDegrees", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bK" : "getBedOrientationInDegrees", "()F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetBedOrientationInDegrees)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBedOrientationInDegrees", "()F", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bK" : "getBedOrientationInDegrees", "()F");
			mv.visitInsn(Opcodes.FRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "d" : "getBrightness", "(F)F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getBrightness", "(Lapi/player/client/IClientPlayerAPI;F)F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetBrightness", "(F)F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "d" : "getBrightness", "(F)F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetBrightness", "(F)F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "getBrightness", "(F)F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetBrightness)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBrightness", "(F)F", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "getBrightness", "(F)F");
			mv.visitInsn(Opcodes.FRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "c" : "getBrightnessForRender", "(F)I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getBrightnessForRender", "(Lapi/player/client/IClientPlayerAPI;F)I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetBrightnessForRender", "(F)I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "c" : "getBrightnessForRender", "(F)I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetBrightnessForRender", "(F)I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "c" : "getBrightnessForRender", "(F)I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetBrightnessForRender)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetBrightnessForRender", "(F)I", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "c" : "getBrightnessForRender", "(F)I");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "getCurrentPlayerStrVsBlock", "" + (isObfuscated ? "(Laji;Z)F" : "(Lnet/minecraft/block/Block;Z)F") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getCurrentPlayerStrVsBlock", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Laji;Z" : "Lnet/minecraft/block/Block;Z") + ")F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetCurrentPlayerStrVsBlock", "" + (isObfuscated ? "(Laji;Z)F" : "(Lnet/minecraft/block/Block;Z)F") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "getCurrentPlayerStrVsBlock", "" + (isObfuscated ? "(Laji;Z)F" : "(Lnet/minecraft/block/Block;Z)F") + "");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetCurrentPlayerStrVsBlock", "" + (isObfuscated ? "(Laji;Z)F" : "(Lnet/minecraft/block/Block;Z)F") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "getCurrentPlayerStrVsBlock", "" + (isObfuscated ? "(Laji;Z)F" : "(Lnet/minecraft/block/Block;Z)F") + "");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetCurrentPlayerStrVsBlock)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetCurrentPlayerStrVsBlock", "" + (isObfuscated ? "(Laji;Z)F" : "(Lnet/minecraft/block/Block;Z)F") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "getCurrentPlayerStrVsBlock", "" + (isObfuscated ? "(Laji;Z)F" : "(Lnet/minecraft/block/Block;Z)F") + "");
			mv.visitInsn(Opcodes.FRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "getBreakSpeed", "" + (isObfuscated ? "(Laji;ZI)F" : "(Lnet/minecraft/block/Block;ZI)F") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getCurrentPlayerStrVsBlockForge", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Laji;ZI" : "Lnet/minecraft/block/Block;ZI") + ")F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetCurrentPlayerStrVsBlockForge", "" + (isObfuscated ? "(Laji;ZI)F" : "(Lnet/minecraft/block/Block;ZI)F") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", "getBreakSpeed", "" + (isObfuscated ? "(Laji;ZI)F" : "(Lnet/minecraft/block/Block;ZI)F") + "");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetCurrentPlayerStrVsBlockForge", "" + (isObfuscated ? "(Laji;ZI)F" : "(Lnet/minecraft/block/Block;ZI)F") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", "getBreakSpeed", "" + (isObfuscated ? "(Laji;ZI)F" : "(Lnet/minecraft/block/Block;ZI)F") + "");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetCurrentPlayerStrVsBlockForge)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetCurrentPlayerStrVsBlockForge", "" + (isObfuscated ? "(Laji;ZI)F" : "(Lnet/minecraft/block/Block;ZI)F") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", "getBreakSpeed", "" + (isObfuscated ? "(Laji;ZI)F" : "(Lnet/minecraft/block/Block;ZI)F") + "");
			mv.visitInsn(Opcodes.FRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "e" : "getDistanceSq", "(DDD)D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getDistanceSq", "(Lapi/player/client/IClientPlayerAPI;DDD)D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetDistanceSq", "(DDD)D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "e" : "getDistanceSq", "(DDD)D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetDistanceSq", "(DDD)D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "e" : "getDistanceSq", "(DDD)D");
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
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "e" : "getDistanceSq", "(DDD)D");
			mv.visitInsn(Opcodes.DRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "f" : "getDistanceSqToEntity", "" + (isObfuscated ? "(Lsa;)D" : "(Lnet/minecraft/entity/Entity;)D") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getDistanceSqToEntity", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lsa;" : "Lnet/minecraft/entity/Entity;") + ")D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetDistanceSqToEntity", "" + (isObfuscated ? "(Lsa;)D" : "(Lnet/minecraft/entity/Entity;)D") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "f" : "getDistanceSqToEntity", "" + (isObfuscated ? "(Lsa;)D" : "(Lnet/minecraft/entity/Entity;)D") + "");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetDistanceSqToEntity", "" + (isObfuscated ? "(Lsa;)D" : "(Lnet/minecraft/entity/Entity;)D") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "f" : "getDistanceSqToEntity", "" + (isObfuscated ? "(Lsa;)D" : "(Lnet/minecraft/entity/Entity;)D") + "");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetDistanceSqToEntity)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetDistanceSqToEntity", "" + (isObfuscated ? "(Lsa;)D" : "(Lnet/minecraft/entity/Entity;)D") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "f" : "getDistanceSqToEntity", "" + (isObfuscated ? "(Lsa;)D" : "(Lnet/minecraft/entity/Entity;)D") + "");
			mv.visitInsn(Opcodes.DRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "t" : "getFOVMultiplier", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getFOVMultiplier", "(Lapi/player/client/IClientPlayerAPI;)F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetFOVMultiplier", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "t" : "getFOVMultiplier", "()F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetFOVMultiplier", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "t" : "getFOVMultiplier", "()F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetFOVMultiplier)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetFOVMultiplier", "()F", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "t" : "getFOVMultiplier", "()F");
			mv.visitInsn(Opcodes.FRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "aT" : "getHurtSound", "()Ljava/lang/String;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getHurtSound", "(Lapi/player/client/IClientPlayerAPI;)Ljava/lang/String;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetHurtSound", "()Ljava/lang/String;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aT" : "getHurtSound", "()Ljava/lang/String;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetHurtSound", "()Ljava/lang/String;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aT" : "getHurtSound", "()Ljava/lang/String;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetHurtSound)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetHurtSound", "()Ljava/lang/String;", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aT" : "getHurtSound", "()Ljava/lang/String;");
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "b" : "getItemIcon", "" + (isObfuscated ? "(Ladd;I)Lrf;" : "(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/util/IIcon;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getItemIcon", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Ladd;I" : "Lnet/minecraft/item/ItemStack;I") + ")" + (isObfuscated ? "Lrf;" : "Lnet/minecraft/util/IIcon;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetItemIcon", "" + (isObfuscated ? "(Ladd;I)Lrf;" : "(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/util/IIcon;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "b" : "getItemIcon", "" + (isObfuscated ? "(Ladd;I)Lrf;" : "(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/util/IIcon;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetItemIcon", "" + (isObfuscated ? "(Ladd;I)Lrf;" : "(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/util/IIcon;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "getItemIcon", "" + (isObfuscated ? "(Ladd;I)Lrf;" : "(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/util/IIcon;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetItemIcon)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetItemIcon", "" + (isObfuscated ? "(Ladd;I)Lrf;" : "(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/util/IIcon;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "getItemIcon", "" + (isObfuscated ? "(Ladd;I)Lrf;" : "(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/util/IIcon;") + "");
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "bM" : "getSleepTimer", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getSleepTimer", "(Lapi/player/client/IClientPlayerAPI;)I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetSleepTimer", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bM" : "getSleepTimer", "()I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetSleepTimer", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bM" : "getSleepTimer", "()I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetSleepTimer)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localGetSleepTimer", "()I", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bM" : "getSleepTimer", "()I");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "P" : "handleLavaMovement", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "handleLavaMovement", "(Lapi/player/client/IClientPlayerAPI;)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realHandleLavaMovement", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "P" : "handleLavaMovement", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superHandleLavaMovement", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "P" : "handleLavaMovement", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalHandleLavaMovement)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localHandleLavaMovement", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "P" : "handleLavaMovement", "()Z");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "N" : "handleWaterMovement", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "handleWaterMovement", "(Lapi/player/client/IClientPlayerAPI;)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realHandleWaterMovement", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "N" : "handleWaterMovement", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superHandleWaterMovement", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "N" : "handleWaterMovement", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalHandleWaterMovement)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localHandleWaterMovement", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "N" : "handleWaterMovement", "()Z");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "f" : "heal", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "heal", "(Lapi/player/client/IClientPlayerAPI;F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realHeal", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "f" : "heal", "(F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superHeal", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "f" : "heal", "(F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalHeal)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localHeal", "(F)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "f" : "heal", "(F)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "aa" : "isEntityInsideOpaqueBlock", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isEntityInsideOpaqueBlock", "(Lapi/player/client/IClientPlayerAPI;)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsEntityInsideOpaqueBlock", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aa" : "isEntityInsideOpaqueBlock", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsEntityInsideOpaqueBlock", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aa" : "isEntityInsideOpaqueBlock", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsEntityInsideOpaqueBlock)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsEntityInsideOpaqueBlock", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "aa" : "isEntityInsideOpaqueBlock", "()Z");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "M" : "isInWater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isInWater", "(Lapi/player/client/IClientPlayerAPI;)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsInWater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "M" : "isInWater", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsInWater", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "M" : "isInWater", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsInWater)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsInWater", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "M" : "isInWater", "()Z");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "isInsideOfMaterial", "" + (isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/material/Material;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isInsideOfMaterial", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lawt;" : "Lnet/minecraft/block/material/Material;") + ")Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsInsideOfMaterial", "" + (isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/material/Material;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "isInsideOfMaterial", "" + (isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/material/Material;)Z") + "");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsInsideOfMaterial", "" + (isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/material/Material;)Z") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "isInsideOfMaterial", "" + (isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/material/Material;)Z") + "");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsInsideOfMaterial)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsInsideOfMaterial", "" + (isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/material/Material;)Z") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "isInsideOfMaterial", "" + (isObfuscated ? "(Lawt;)Z" : "(Lnet/minecraft/block/material/Material;)Z") + "");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "h_" : "isOnLadder", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isOnLadder", "(Lapi/player/client/IClientPlayerAPI;)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsOnLadder", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "h_" : "isOnLadder", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsOnLadder", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "h_" : "isOnLadder", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsOnLadder)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsOnLadder", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "h_" : "isOnLadder", "()Z");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "bm" : "isPlayerSleeping", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isPlayerSleeping", "(Lapi/player/client/IClientPlayerAPI;)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsPlayerSleeping", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bm" : "isPlayerSleeping", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsPlayerSleeping", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bm" : "isPlayerSleeping", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsPlayerSleeping)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsPlayerSleeping", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bm" : "isPlayerSleeping", "()Z");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "an" : "isSneaking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isSneaking", "(Lapi/player/client/IClientPlayerAPI;)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsSneaking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "an" : "isSneaking", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsSneaking", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "an" : "isSneaking", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsSneaking)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsSneaking", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "an" : "isSneaking", "()Z");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "ao" : "isSprinting", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "isSprinting", "(Lapi/player/client/IClientPlayerAPI;)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realIsSprinting", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ao" : "isSprinting", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superIsSprinting", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "ao" : "isSprinting", "()Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalIsSprinting)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localIsSprinting", "()Z", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "ao" : "isSprinting", "()Z");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "bj" : "jump", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "jump", "(Lapi/player/client/IClientPlayerAPI;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realJump", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bj" : "jump", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superJump", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bj" : "jump", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalJump)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localJump", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bj" : "jump", "()V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "knockBack", "" + (isObfuscated ? "(Lsa;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "knockBack", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lsa;FDD" : "Lnet/minecraft/entity/Entity;FDD") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realKnockBack", "" + (isObfuscated ? "(Lsa;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "knockBack", "" + (isObfuscated ? "(Lsa;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superKnockBack", "" + (isObfuscated ? "(Lsa;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "knockBack", "" + (isObfuscated ? "(Lsa;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalKnockBack)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localKnockBack", "" + (isObfuscated ? "(Lsa;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitVarInsn(Opcodes.DLOAD, 3);
			mv.visitVarInsn(Opcodes.DLOAD, 5);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "knockBack", "" + (isObfuscated ? "(Lsa;FDD)V" : "(Lnet/minecraft/entity/Entity;FDD)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "d" : "moveEntity", "(DDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "moveEntity", "(Lapi/player/client/IClientPlayerAPI;DDD)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realMoveEntity", "(DDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "d" : "moveEntity", "(DDD)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superMoveEntity", "(DDD)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "moveEntity", "(DDD)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalMoveEntity)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveEntity", "(DDD)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.DLOAD, 1);
			mv.visitVarInsn(Opcodes.DLOAD, 3);
			mv.visitVarInsn(Opcodes.DLOAD, 5);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "d" : "moveEntity", "(DDD)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "e" : "moveEntityWithHeading", "(FF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "moveEntityWithHeading", "(Lapi/player/client/IClientPlayerAPI;FF)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realMoveEntityWithHeading", "(FF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "e" : "moveEntityWithHeading", "(FF)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superMoveEntityWithHeading", "(FF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "e" : "moveEntityWithHeading", "(FF)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalMoveEntityWithHeading)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveEntityWithHeading", "(FF)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "e" : "moveEntityWithHeading", "(FF)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "moveFlying", "(FFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "moveFlying", "(Lapi/player/client/IClientPlayerAPI;FFF)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realMoveFlying", "(FFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "moveFlying", "(FFF)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superMoveFlying", "(FFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "moveFlying", "(FFF)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalMoveFlying)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localMoveFlying", "(FFF)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitVarInsn(Opcodes.FLOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "moveFlying", "(FFF)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "onDeath", "" + (isObfuscated ? "(Lro;)V" : "(Lnet/minecraft/util/DamageSource;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "onDeath", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lro;" : "Lnet/minecraft/util/DamageSource;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realOnDeath", "" + (isObfuscated ? "(Lro;)V" : "(Lnet/minecraft/util/DamageSource;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "onDeath", "" + (isObfuscated ? "(Lro;)V" : "(Lnet/minecraft/util/DamageSource;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superOnDeath", "" + (isObfuscated ? "(Lro;)V" : "(Lnet/minecraft/util/DamageSource;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "onDeath", "" + (isObfuscated ? "(Lro;)V" : "(Lnet/minecraft/util/DamageSource;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalOnDeath)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnDeath", "" + (isObfuscated ? "(Lro;)V" : "(Lnet/minecraft/util/DamageSource;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "onDeath", "" + (isObfuscated ? "(Lro;)V" : "(Lnet/minecraft/util/DamageSource;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "e" : "onLivingUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "onLivingUpdate", "(Lapi/player/client/IClientPlayerAPI;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realOnLivingUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "e" : "onLivingUpdate", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superOnLivingUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "e" : "onLivingUpdate", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalOnLivingUpdate)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnLivingUpdate", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "e" : "onLivingUpdate", "()V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "onKillEntity", "" + (isObfuscated ? "(Lsv;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "onKillEntity", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lsv;" : "Lnet/minecraft/entity/EntityLivingBase;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realOnKillEntity", "" + (isObfuscated ? "(Lsv;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "onKillEntity", "" + (isObfuscated ? "(Lsv;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superOnKillEntity", "" + (isObfuscated ? "(Lsv;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "onKillEntity", "" + (isObfuscated ? "(Lsv;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalOnKillEntity)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnKillEntity", "" + (isObfuscated ? "(Lsv;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "onKillEntity", "" + (isObfuscated ? "(Lsv;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "onStruckByLightning", "" + (isObfuscated ? "(Lxh;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "onStruckByLightning", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Lxh;" : "Lnet/minecraft/entity/effect/EntityLightningBolt;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realOnStruckByLightning", "" + (isObfuscated ? "(Lxh;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "onStruckByLightning", "" + (isObfuscated ? "(Lxh;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superOnStruckByLightning", "" + (isObfuscated ? "(Lxh;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "onStruckByLightning", "" + (isObfuscated ? "(Lxh;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalOnStruckByLightning)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnStruckByLightning", "" + (isObfuscated ? "(Lxh;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "onStruckByLightning", "" + (isObfuscated ? "(Lxh;)V" : "(Lnet/minecraft/entity/effect/EntityLightningBolt;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "h" : "onUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "onUpdate", "(Lapi/player/client/IClientPlayerAPI;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realOnUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "h" : "onUpdate", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superOnUpdate", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "h" : "onUpdate", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalOnUpdate)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localOnUpdate", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "h" : "onUpdate", "()V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "func_145780_a", "" + (isObfuscated ? "(IIILaji;)V" : "(IIILnet/minecraft/block/Block;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitVarInsn(Opcodes.ALOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "playStepSound", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "IIILaji;" : "IIILnet/minecraft/block/Block;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realPlayStepSound", "" + (isObfuscated ? "(IIILaji;)V" : "(IIILnet/minecraft/block/Block;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitVarInsn(Opcodes.ALOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "func_145780_a", "" + (isObfuscated ? "(IIILaji;)V" : "(IIILnet/minecraft/block/Block;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superPlayStepSound", "" + (isObfuscated ? "(IIILaji;)V" : "(IIILnet/minecraft/block/Block;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitVarInsn(Opcodes.ALOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_145780_a", "" + (isObfuscated ? "(IIILaji;)V" : "(IIILnet/minecraft/block/Block;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalPlayStepSound)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localPlayStepSound", "" + (isObfuscated ? "(IIILaji;)V" : "(IIILnet/minecraft/block/Block;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitVarInsn(Opcodes.ALOAD, 4);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "func_145780_a", "" + (isObfuscated ? "(IIILaji;)V" : "(IIILnet/minecraft/block/Block;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "j" : "func_145771_j", "(DDD)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "pushOutOfBlocks", "(Lapi/player/client/IClientPlayerAPI;DDD)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realPushOutOfBlocks", "(DDD)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "j" : "func_145771_j", "(DDD)Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superPushOutOfBlocks", "(DDD)Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "j" : "func_145771_j", "(DDD)Z");
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
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "j" : "func_145771_j", "(DDD)Z");
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "rayTrace", "" + (isObfuscated ? "(DF)Lazu;" : "(DF)Lnet/minecraft/util/MovingObjectPosition;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "rayTrace", "(Lapi/player/client/IClientPlayerAPI;DF)" + (isObfuscated ? "Lazu;" : "Lnet/minecraft/util/MovingObjectPosition;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realRayTrace", "" + (isObfuscated ? "(DF)Lazu;" : "(DF)Lnet/minecraft/util/MovingObjectPosition;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "rayTrace", "" + (isObfuscated ? "(DF)Lazu;" : "(DF)Lnet/minecraft/util/MovingObjectPosition;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superRayTrace", "" + (isObfuscated ? "(DF)Lazu;" : "(DF)Lnet/minecraft/util/MovingObjectPosition;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "rayTrace", "" + (isObfuscated ? "(DF)Lazu;" : "(DF)Lnet/minecraft/util/MovingObjectPosition;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalRayTrace)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localRayTrace", "" + (isObfuscated ? "(DF)Lazu;" : "(DF)Lnet/minecraft/util/MovingObjectPosition;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.DLOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "rayTrace", "" + (isObfuscated ? "(DF)Lazu;" : "(DF)Lnet/minecraft/util/MovingObjectPosition;") + "");
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "readEntityFromNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "readEntityFromNBT", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Ldh;" : "Lnet/minecraft/nbt/NBTTagCompound;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realReadEntityFromNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "readEntityFromNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superReadEntityFromNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "readEntityFromNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalReadEntityFromNBT)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localReadEntityFromNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "readEntityFromNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "bH" : "respawnPlayer", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "respawnPlayer", "(Lapi/player/client/IClientPlayerAPI;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realRespawnPlayer", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bH" : "respawnPlayer", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superRespawnPlayer", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bH" : "respawnPlayer", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalRespawnPlayer)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localRespawnPlayer", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bH" : "respawnPlayer", "()V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "B" : "setDead", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "setDead", "(Lapi/player/client/IClientPlayerAPI;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetDead", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "B" : "setDead", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetDead", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "B" : "setDead", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetDead)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetDead", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "B" : "setDead", "()V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "n" : "setPlayerSPHealth", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "setPlayerSPHealth", "(Lapi/player/client/IClientPlayerAPI;F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetPlayerSPHealth", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "n" : "setPlayerSPHealth", "(F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetPlayerSPHealth", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "n" : "setPlayerSPHealth", "(F)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetPlayerSPHealth)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetPlayerSPHealth", "(F)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "n" : "setPlayerSPHealth", "(F)V");
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
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "setPositionAndRotation", "(Lapi/player/client/IClientPlayerAPI;DDDFF)V");
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
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "setPositionAndRotation", "(DDDFF)V");
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
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "setPositionAndRotation", "(DDDFF)V");
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
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "setPositionAndRotation", "(DDDFF)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "b" : "setSneaking", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "setSneaking", "(Lapi/player/client/IClientPlayerAPI;Z)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetSneaking", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "b" : "setSneaking", "(Z)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetSneaking", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "setSneaking", "(Z)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetSneaking)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetSneaking", "(Z)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "setSneaking", "(Z)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "c" : "setSprinting", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "setSprinting", "(Lapi/player/client/IClientPlayerAPI;Z)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetSprinting", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "c" : "setSprinting", "(Z)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetSprinting", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "c" : "setSprinting", "(Z)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetSprinting)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSetSprinting", "(Z)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "c" : "setSprinting", "(Z)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "sleepInBedAt", "" + (isObfuscated ? "(III)Lza;" : "(III)Lnet/minecraft/entity/player/EntityPlayer$EnumStatus;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "sleepInBedAt", "(Lapi/player/client/IClientPlayerAPI;III)" + (isObfuscated ? "Lza;" : "Lnet/minecraft/entity/player/EntityPlayer$EnumStatus;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSleepInBedAt", "" + (isObfuscated ? "(III)Lza;" : "(III)Lnet/minecraft/entity/player/EntityPlayer$EnumStatus;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "sleepInBedAt", "" + (isObfuscated ? "(III)Lza;" : "(III)Lnet/minecraft/entity/player/EntityPlayer$EnumStatus;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSleepInBedAt", "" + (isObfuscated ? "(III)Lza;" : "(III)Lnet/minecraft/entity/player/EntityPlayer$EnumStatus;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "sleepInBedAt", "" + (isObfuscated ? "(III)Lza;" : "(III)Lnet/minecraft/entity/player/EntityPlayer$EnumStatus;") + "");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSleepInBedAt)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSleepInBedAt", "" + (isObfuscated ? "(III)Lza;" : "(III)Lnet/minecraft/entity/player/EntityPlayer$EnumStatus;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "sleepInBedAt", "" + (isObfuscated ? "(III)Lza;" : "(III)Lnet/minecraft/entity/player/EntityPlayer$EnumStatus;") + "");
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "ba" : "swingItem", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "swingItem", "(Lapi/player/client/IClientPlayerAPI;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSwingItem", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ba" : "swingItem", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSwingItem", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "ba" : "swingItem", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSwingItem)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localSwingItem", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "ba" : "swingItem", "()V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "bq" : "updateEntityActionState", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "updateEntityActionState", "(Lapi/player/client/IClientPlayerAPI;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realUpdateEntityActionState", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bq" : "updateEntityActionState", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superUpdateEntityActionState", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bq" : "updateEntityActionState", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalUpdateEntityActionState)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localUpdateEntityActionState", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "bq" : "updateEntityActionState", "()V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "ab" : "updateRidden", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "updateRidden", "(Lapi/player/client/IClientPlayerAPI;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realUpdateRidden", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ab" : "updateRidden", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superUpdateRidden", "()V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "ab" : "updateRidden", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalUpdateRidden)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localUpdateRidden", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "ab" : "updateRidden", "()V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "a" : "wakeUpPlayer", "(ZZZ)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "wakeUpPlayer", "(Lapi/player/client/IClientPlayerAPI;ZZZ)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realWakeUpPlayer", "(ZZZ)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "wakeUpPlayer", "(ZZZ)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superWakeUpPlayer", "(ZZZ)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "wakeUpPlayer", "(ZZZ)V");
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
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "a" : "wakeUpPlayer", "(ZZZ)V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "b" : "writeEntityToNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "writeEntityToNBT", "(Lapi/player/client/IClientPlayerAPI;" + (isObfuscated ? "Ldh;" : "Lnet/minecraft/nbt/NBTTagCompound;") + ")V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realWriteEntityToNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "b" : "writeEntityToNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superWriteEntityToNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "writeEntityToNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalWriteEntityToNBT)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "localWriteEntityToNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "blg" : "net/minecraft/client/entity/AbstractClientPlayer", isObfuscated ? "b" : "writeEntityToNBT", "" + (isObfuscated ? "(Ldh;)V" : "(Lnet/minecraft/nbt/NBTTagCompound;)V") + "");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getAddedToChunkField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ag" : "addedToChunk", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setAddedToChunkField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ag" : "addedToChunk", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getArrowHitTimerField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "av" : "arrowHitTimer", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setArrowHitTimerField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "av" : "arrowHitTimer", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getAttackTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aB" : "attackTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setAttackTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aB" : "attackTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getAttackedAtYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "az" : "attackedAtYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setAttackedAtYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "az" : "attackedAtYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getAttackingPlayerField", isObfuscated ? "()Lyz;" : "()Lnet/minecraft/entity/player/EntityPlayer;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aR" : "attackingPlayer", isObfuscated ? "Lyz;" : "Lnet/minecraft/entity/player/EntityPlayer;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setAttackingPlayerField", isObfuscated ? "(Lyz;)V" : "(Lnet/minecraft/entity/player/EntityPlayer;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aR" : "attackingPlayer", isObfuscated ? "Lyz;" : "Lnet/minecraft/entity/player/EntityPlayer;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getBoundingBoxField", isObfuscated ? "()Lazt;" : "()Lnet/minecraft/util/AxisAlignedBB;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "C" : "boundingBox", isObfuscated ? "Lazt;" : "Lnet/minecraft/util/AxisAlignedBB;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getCameraPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aJ" : "cameraPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setCameraPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aJ" : "cameraPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getCameraYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bs" : "cameraYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setCameraYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bs" : "cameraYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getCapabilitiesField", isObfuscated ? "()Lyw;" : "()Lnet/minecraft/entity/player/PlayerCapabilities;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bE" : "capabilities", isObfuscated ? "Lyw;" : "Lnet/minecraft/entity/player/PlayerCapabilities;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setCapabilitiesField", isObfuscated ? "(Lyw;)V" : "(Lnet/minecraft/entity/player/PlayerCapabilities;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bE" : "capabilities", isObfuscated ? "Lyw;" : "Lnet/minecraft/entity/player/PlayerCapabilities;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getChunkCoordXField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ah" : "chunkCoordX", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setChunkCoordXField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ah" : "chunkCoordX", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getChunkCoordYField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ai" : "chunkCoordY", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setChunkCoordYField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ai" : "chunkCoordY", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getChunkCoordZField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aj" : "chunkCoordZ", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setChunkCoordZField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aj" : "chunkCoordZ", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDataWatcherField", isObfuscated ? "()Lte;" : "()Lnet/minecraft/entity/DataWatcher;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "af" : "dataWatcher", isObfuscated ? "Lte;" : "Lnet/minecraft/entity/DataWatcher;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDataWatcherField", isObfuscated ? "(Lte;)V" : "(Lnet/minecraft/entity/DataWatcher;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "af" : "dataWatcher", isObfuscated ? "Lte;" : "Lnet/minecraft/entity/DataWatcher;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDeadField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aT" : "dead", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDeadField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aT" : "dead", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDeathTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aA" : "deathTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDeathTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aA" : "deathTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDimensionField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ap" : "dimension", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDimensionField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ap" : "dimension", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDistanceWalkedModifiedField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "P" : "distanceWalkedModified", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDistanceWalkedModifiedField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "P" : "distanceWalkedModified", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getDistanceWalkedOnStepModifiedField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "Q" : "distanceWalkedOnStepModified", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setDistanceWalkedOnStepModifiedField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "Q" : "distanceWalkedOnStepModified", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getEntityAgeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aU" : "entityAge", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setEntityAgeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aU" : "entityAge", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getEntityCollisionReductionField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "Y" : "entityCollisionReduction", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setEntityCollisionReductionField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "Y" : "entityCollisionReduction", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getEntityUniqueIDField", "()Ljava/util/UUID;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ar" : "entityUniqueID", "Ljava/util/UUID;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setEntityUniqueIDField", "(Ljava/util/UUID;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ar" : "entityUniqueID", "Ljava/util/UUID;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getExperienceField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bH" : "experience", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setExperienceField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bH" : "experience", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getExperienceLevelField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bF" : "experienceLevel", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setExperienceLevelField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bF" : "experienceLevel", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getExperienceTotalField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bG" : "experienceTotal", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setExperienceTotalField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bG" : "experienceTotal", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getFallDistanceField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "R" : "fallDistance", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setFallDistanceField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "R" : "fallDistance", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_110154_aXField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aW" : "field_110154_aX", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_110154_aXField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aW" : "field_110154_aX", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_70135_KField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "J" : "field_70135_K", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_70135_KField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "J" : "field_70135_K", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_70741_aBField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aZ" : "field_70741_aB", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_70741_aBField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aZ" : "field_70741_aB", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_70763_axField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aY" : "field_70763_ax", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_70763_axField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aY" : "field_70763_ax", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_70764_awField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aX" : "field_70764_aw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_70764_awField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aX" : "field_70764_aw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_70768_auField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aV" : "field_70768_au", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_70768_auField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aV" : "field_70768_au", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_70769_aoField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aK" : "field_70769_ao", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_70769_aoField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aK" : "field_70769_ao", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_70770_apField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aL" : "field_70770_ap", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_70770_apField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aL" : "field_70770_ap", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71079_bUField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bC" : "field_71079_bU", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71079_bUField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bC" : "field_71079_bU", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71082_cxField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cc" : "field_71082_cx", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71082_cxField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cc" : "field_71082_cx", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71085_bRField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bz" : "field_71085_bR", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71085_bRField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bz" : "field_71085_bR", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71089_bVField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bD" : "field_71089_bV", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71089_bVField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bD" : "field_71089_bV", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71091_bMField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bu" : "field_71091_bM", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71091_bMField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bu" : "field_71091_bM", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71094_bPField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bx" : "field_71094_bP", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71094_bPField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bx" : "field_71094_bP", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71095_bQField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "by" : "field_71095_bQ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71095_bQField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "by" : "field_71095_bQ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71096_bNField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bv" : "field_71096_bN", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71096_bNField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bv" : "field_71096_bN", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71097_bOField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bw" : "field_71097_bO", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71097_bOField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bw" : "field_71097_bO", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71160_ciField", isObfuscated ? "()Lqm;" : "()Lnet/minecraft/util/MouseFilter;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bT" : "field_71160_ci", isObfuscated ? "Lqm;" : "Lnet/minecraft/util/MouseFilter;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71160_ciField", isObfuscated ? "(Lqm;)V" : "(Lnet/minecraft/util/MouseFilter;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bT" : "field_71160_ci", isObfuscated ? "Lqm;" : "Lnet/minecraft/util/MouseFilter;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71161_cjField", isObfuscated ? "()Lqm;" : "()Lnet/minecraft/util/MouseFilter;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bU" : "field_71161_cj", isObfuscated ? "Lqm;" : "Lnet/minecraft/util/MouseFilter;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71161_cjField", isObfuscated ? "(Lqm;)V" : "(Lnet/minecraft/util/MouseFilter;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bU" : "field_71161_cj", isObfuscated ? "Lqm;" : "Lnet/minecraft/util/MouseFilter;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getField_71162_chField", isObfuscated ? "()Lqm;" : "()Lnet/minecraft/util/MouseFilter;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bS" : "field_71162_ch", isObfuscated ? "Lqm;" : "Lnet/minecraft/util/MouseFilter;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setField_71162_chField", isObfuscated ? "(Lqm;)V" : "(Lnet/minecraft/util/MouseFilter;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bS" : "field_71162_ch", isObfuscated ? "Lqm;" : "Lnet/minecraft/util/MouseFilter;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getFireResistanceField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ab" : "fireResistance", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setFireResistanceField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ab" : "fireResistance", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getFishEntityField", isObfuscated ? "()Lxe;" : "()Lnet/minecraft/entity/projectile/EntityFishHook;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bK" : "fishEntity", isObfuscated ? "Lxe;" : "Lnet/minecraft/entity/projectile/EntityFishHook;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setFishEntityField", isObfuscated ? "(Lxe;)V" : "(Lnet/minecraft/entity/projectile/EntityFishHook;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bK" : "fishEntity", isObfuscated ? "Lxe;" : "Lnet/minecraft/entity/projectile/EntityFishHook;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getFlyToggleTimerField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bq" : "flyToggleTimer", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setFlyToggleTimerField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bq" : "flyToggleTimer", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getFoodStatsField", isObfuscated ? "()Lzr;" : "()Lnet/minecraft/util/FoodStats;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bp" : "foodStats", isObfuscated ? "Lzr;" : "Lnet/minecraft/util/FoodStats;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setFoodStatsField", isObfuscated ? "(Lzr;)V" : "(Lnet/minecraft/util/FoodStats;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bp" : "foodStats", isObfuscated ? "Lzr;" : "Lnet/minecraft/util/FoodStats;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getForceSpawnField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "n" : "forceSpawn", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setForceSpawnField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "n" : "forceSpawn", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHeightField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "N" : "height", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHeightField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "N" : "height", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHorseJumpPowerField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bO" : "horseJumpPower", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHorseJumpPowerField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bO" : "horseJumpPower", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHorseJumpPowerCounterField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "horseJumpPowerCounter", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHorseJumpPowerCounterField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "a" : "horseJumpPowerCounter", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHurtResistantTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ad" : "hurtResistantTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHurtResistantTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ad" : "hurtResistantTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getHurtTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ax" : "hurtTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setHurtTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ax" : "hurtTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIgnoreFrustumCheckField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ak" : "ignoreFrustumCheck", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIgnoreFrustumCheckField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ak" : "ignoreFrustumCheck", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInPortalField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "an" : "inPortal", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInPortalField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "an" : "inPortal", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInWaterField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ac" : "inWater", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInWaterField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ac" : "inWater", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInventoryField", isObfuscated ? "()Lyx;" : "()Lnet/minecraft/entity/player/InventoryPlayer;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bm" : "inventory", isObfuscated ? "Lyx;" : "Lnet/minecraft/entity/player/InventoryPlayer;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInventoryField", isObfuscated ? "(Lyx;)V" : "(Lnet/minecraft/entity/player/InventoryPlayer;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bm" : "inventory", isObfuscated ? "Lyx;" : "Lnet/minecraft/entity/player/InventoryPlayer;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getInventoryContainerField", isObfuscated ? "()Lzs;" : "()Lnet/minecraft/inventory/Container;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bn" : "inventoryContainer", isObfuscated ? "Lzs;" : "Lnet/minecraft/inventory/Container;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setInventoryContainerField", isObfuscated ? "(Lzs;)V" : "(Lnet/minecraft/inventory/Container;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bn" : "inventoryContainer", isObfuscated ? "Lzs;" : "Lnet/minecraft/inventory/Container;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsAirBorneField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "al" : "isAirBorne", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsAirBorneField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "al" : "isAirBorne", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsCollidedField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "G" : "isCollided", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsCollidedField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "G" : "isCollided", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsCollidedHorizontallyField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "E" : "isCollidedHorizontally", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsCollidedHorizontallyField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "E" : "isCollidedHorizontally", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsCollidedVerticallyField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "F" : "isCollidedVertically", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsCollidedVerticallyField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "F" : "isCollidedVertically", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsDeadField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "K" : "isDead", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsDeadField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "K" : "isDead", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsImmuneToFireField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ae" : "isImmuneToFire", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsImmuneToFireField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ae" : "isImmuneToFire", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsInWebField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "I" : "isInWeb", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsInWebField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "I" : "isInWeb", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsJumpingField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bc" : "isJumping", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsJumpingField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bc" : "isJumping", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsSwingInProgressField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "at" : "isSwingInProgress", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsSwingInProgressField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "at" : "isSwingInProgress", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getJumpMovementFactorField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aQ" : "jumpMovementFactor", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setJumpMovementFactorField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aQ" : "jumpMovementFactor", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastDamageField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bb" : "lastDamage", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastDamageField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bb" : "lastDamage", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastTickPosXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "S" : "lastTickPosX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastTickPosXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "S" : "lastTickPosX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastTickPosYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "T" : "lastTickPosY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastTickPosYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "T" : "lastTickPosY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLastTickPosZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "U" : "lastTickPosZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLastTickPosZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "U" : "lastTickPosZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLimbSwingField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aG" : "limbSwing", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLimbSwingField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aG" : "limbSwing", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLimbSwingAmountField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aF" : "limbSwingAmount", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLimbSwingAmountField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aF" : "limbSwingAmount", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMaxHurtResistantTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aH" : "maxHurtResistantTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMaxHurtResistantTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aH" : "maxHurtResistantTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMaxHurtTimeField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ay" : "maxHurtTime", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMaxHurtTimeField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ay" : "maxHurtTime", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMcField", isObfuscated ? "()Lbao;" : "()Lnet/minecraft/client/Minecraft;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "d" : "mc", isObfuscated ? "Lbao;" : "Lnet/minecraft/client/Minecraft;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMcField", isObfuscated ? "(Lbao;)V" : "(Lnet/minecraft/client/Minecraft;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "d" : "mc", isObfuscated ? "Lbao;" : "Lnet/minecraft/client/Minecraft;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMotionXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "v" : "motionX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMotionXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "v" : "motionX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMotionYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "w" : "motionY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMotionYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "w" : "motionY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMotionZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "x" : "motionZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMotionZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "x" : "motionZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMoveForwardField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "be" : "moveForward", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMoveForwardField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "be" : "moveForward", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMoveStrafingField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bd" : "moveStrafing", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMoveStrafingField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bd" : "moveStrafing", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMovementInputField", isObfuscated ? "()Lbli;" : "()Lnet/minecraft/util/MovementInput;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "c" : "movementInput", isObfuscated ? "Lbli;" : "Lnet/minecraft/util/MovementInput;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMovementInputField", isObfuscated ? "(Lbli;)V" : "(Lnet/minecraft/util/MovementInput;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "c" : "movementInput", isObfuscated ? "Lbli;" : "Lnet/minecraft/util/MovementInput;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getMyEntitySizeField", isObfuscated ? "()Lse;" : "()Lnet/minecraft/entity/Entity$EnumEntitySize;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "as" : "myEntitySize", isObfuscated ? "Lse;" : "Lnet/minecraft/entity/Entity$EnumEntitySize;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setMyEntitySizeField", isObfuscated ? "(Lse;)V" : "(Lnet/minecraft/entity/Entity$EnumEntitySize;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "as" : "myEntitySize", isObfuscated ? "Lse;" : "Lnet/minecraft/entity/Entity$EnumEntitySize;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getNewPosRotationIncrementsField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bg" : "newPosRotationIncrements", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setNewPosRotationIncrementsField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bg" : "newPosRotationIncrements", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getNewPosXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bh" : "newPosX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setNewPosXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bh" : "newPosX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getNewPosYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bi" : "newPosY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setNewPosYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bi" : "newPosY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getNewPosZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bj" : "newPosZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setNewPosZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bj" : "newPosZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getNewRotationPitchField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bl" : "newRotationPitch", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setNewRotationPitchField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bl" : "newRotationPitch", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getNewRotationYawField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bk" : "newRotationYaw", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setNewRotationYawField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bk" : "newRotationYaw", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getNoClipField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "X" : "noClip", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setNoClipField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "X" : "noClip", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getOnGroundField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "D" : "onGround", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setOnGroundField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "D" : "onGround", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getOpenContainerField", isObfuscated ? "()Lzs;" : "()Lnet/minecraft/inventory/Container;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bo" : "openContainer", isObfuscated ? "Lzs;" : "Lnet/minecraft/inventory/Container;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setOpenContainerField", isObfuscated ? "(Lzs;)V" : "(Lnet/minecraft/inventory/Container;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bo" : "openContainer", isObfuscated ? "Lzs;" : "Lnet/minecraft/inventory/Container;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPlayerLocationField", isObfuscated ? "()Lr;" : "()Lnet/minecraft/util/ChunkCoordinates;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bB" : "playerLocation", isObfuscated ? "Lr;" : "Lnet/minecraft/util/ChunkCoordinates;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPlayerLocationField", isObfuscated ? "(Lr;)V" : "(Lnet/minecraft/util/ChunkCoordinates;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bB" : "playerLocation", isObfuscated ? "Lr;" : "Lnet/minecraft/util/ChunkCoordinates;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPortalCounterField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ao" : "portalCounter", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPortalCounterField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ao" : "portalCounter", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPosXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "s" : "posX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPosXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "s" : "posX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPosYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "t" : "posY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPosYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "t" : "posY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPosZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "u" : "posZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPosZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "u" : "posZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevCameraPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aI" : "prevCameraPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevCameraPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aI" : "prevCameraPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevCameraYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "br" : "prevCameraYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevCameraYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "br" : "prevCameraYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevDistanceWalkedModifiedField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "O" : "prevDistanceWalkedModified", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevDistanceWalkedModifiedField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "O" : "prevDistanceWalkedModified", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevHealthField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aw" : "prevHealth", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevHealthField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aw" : "prevHealth", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevLimbSwingAmountField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aE" : "prevLimbSwingAmount", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevLimbSwingAmountField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aE" : "prevLimbSwingAmount", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevPosXField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "p" : "prevPosX", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevPosXField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "p" : "prevPosX", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevPosYField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "q" : "prevPosY", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevPosYField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "q" : "prevPosY", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevPosZField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "r" : "prevPosZ", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevPosZField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "r" : "prevPosZ", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRenderArmPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bL" : "prevRenderArmPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRenderArmPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bL" : "prevRenderArmPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRenderArmYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "i" : "prevRenderArmYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRenderArmYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "i" : "prevRenderArmYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRenderYawOffsetField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aN" : "prevRenderYawOffset", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRenderYawOffsetField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aN" : "prevRenderYawOffset", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRotationPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "B" : "prevRotationPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRotationPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "B" : "prevRotationPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRotationYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "A" : "prevRotationYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRotationYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "A" : "prevRotationYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevRotationYawHeadField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aP" : "prevRotationYawHead", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevRotationYawHeadField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aP" : "prevRotationYawHead", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevSwingProgressField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aC" : "prevSwingProgress", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevSwingProgressField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aC" : "prevSwingProgress", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPrevTimeInPortalField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bN" : "prevTimeInPortal", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPrevTimeInPortalField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bN" : "prevTimeInPortal", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getPreventEntitySpawningField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "k" : "preventEntitySpawning", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setPreventEntitySpawningField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "k" : "preventEntitySpawning", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRandField", "()Ljava/util/Random;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "Z" : "rand", "Ljava/util/Random;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRandField", "(Ljava/util/Random;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "Z" : "rand", "Ljava/util/Random;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRandomYawVelocityField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bf" : "randomYawVelocity", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRandomYawVelocityField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bf" : "randomYawVelocity", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRecentlyHitField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aS" : "recentlyHit", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRecentlyHitField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aS" : "recentlyHit", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRenderArmPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "h" : "renderArmPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRenderArmPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "h" : "renderArmPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRenderArmYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "g" : "renderArmYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRenderArmYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "g" : "renderArmYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRenderDistanceWeightField", "()D", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "j" : "renderDistanceWeight", "D");
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRenderDistanceWeightField", "(D)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "j" : "renderDistanceWeight", "D");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRenderYawOffsetField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aM" : "renderYawOffset", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRenderYawOffsetField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aM" : "renderYawOffset", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRiddenByEntityField", isObfuscated ? "()Lsa;" : "()Lnet/minecraft/entity/Entity;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "l" : "riddenByEntity", isObfuscated ? "Lsa;" : "Lnet/minecraft/entity/Entity;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRiddenByEntityField", isObfuscated ? "(Lsa;)V" : "(Lnet/minecraft/entity/Entity;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "l" : "riddenByEntity", isObfuscated ? "Lsa;" : "Lnet/minecraft/entity/Entity;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRidingEntityField", isObfuscated ? "()Lsa;" : "()Lnet/minecraft/entity/Entity;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "m" : "ridingEntity", isObfuscated ? "Lsa;" : "Lnet/minecraft/entity/Entity;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRidingEntityField", isObfuscated ? "(Lsa;)V" : "(Lnet/minecraft/entity/Entity;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "m" : "ridingEntity", isObfuscated ? "Lsa;" : "Lnet/minecraft/entity/Entity;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRotationPitchField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "z" : "rotationPitch", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRotationPitchField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "z" : "rotationPitch", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRotationYawField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "y" : "rotationYaw", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRotationYawField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "y" : "rotationYaw", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRotationYawHeadField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aO" : "rotationYawHead", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRotationYawHeadField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aO" : "rotationYawHead", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getScoreValueField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ba" : "scoreValue", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setScoreValueField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ba" : "scoreValue", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getServerPosXField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bZ" : "serverPosX", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setServerPosXField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bZ" : "serverPosX", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getServerPosYField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ca" : "serverPosY", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setServerPosYField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "ca" : "serverPosY", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getServerPosZField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cb" : "serverPosZ", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setServerPosZField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "cb" : "serverPosZ", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSleepingField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bA" : "sleeping", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSleepingField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bA" : "sleeping", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSpeedInAirField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bJ" : "speedInAir", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSpeedInAirField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bJ" : "speedInAir", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSpeedOnGroundField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bI" : "speedOnGround", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSpeedOnGroundField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bI" : "speedOnGround", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSprintToggleTimerField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "e" : "sprintToggleTimer", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSprintToggleTimerField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "e" : "sprintToggleTimer", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSprintingTicksLeftField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "f" : "sprintingTicksLeft", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSprintingTicksLeftField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "f" : "sprintingTicksLeft", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getStepHeightField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "W" : "stepHeight", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setStepHeightField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "W" : "stepHeight", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSwingProgressField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aD" : "swingProgress", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSwingProgressField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aD" : "swingProgress", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSwingProgressIntField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "au" : "swingProgressInt", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSwingProgressIntField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "au" : "swingProgressInt", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTeleportDirectionField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aq" : "teleportDirection", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTeleportDirectionField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aq" : "teleportDirection", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTicksExistedField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aa" : "ticksExisted", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTicksExistedField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "aa" : "ticksExisted", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTimeInPortalField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bM" : "timeInPortal", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTimeInPortalField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bM" : "timeInPortal", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTimeUntilPortalField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "am" : "timeUntilPortal", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTimeUntilPortalField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "am" : "timeUntilPortal", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getVelocityChangedField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "H" : "velocityChanged", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setVelocityChangedField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "H" : "velocityChanged", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getWidthField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "M" : "width", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setWidthField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "M" : "width", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getWorldObjField", isObfuscated ? "()Lahb;" : "()Lnet/minecraft/world/World;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "o" : "worldObj", isObfuscated ? "Lahb;" : "Lnet/minecraft/world/World;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setWorldObjField", isObfuscated ? "(Lahb;)V" : "(Lnet/minecraft/world/World;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "o" : "worldObj", isObfuscated ? "Lahb;" : "Lnet/minecraft/world/World;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getXpCooldownField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bt" : "xpCooldown", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setXpCooldownField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "bt" : "xpCooldown", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getYOffsetField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "L" : "yOffset", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setYOffsetField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "L" : "yOffset", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getYSizeField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "V" : "ySize", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setYSizeField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", isObfuscated ? "V" : "ySize", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getClientPlayerBase", "(Ljava/lang/String;)Lapi/player/client/ClientPlayerBase;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getClientPlayerBase", "(Lapi/player/client/IClientPlayerAPI;Ljava/lang/String;)Lapi/player/client/ClientPlayerBase;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getClientPlayerBaseIds", "()Ljava/util/Set;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "getClientPlayerBaseIds", "(Lapi/player/client/IClientPlayerAPI;)Ljava/util/Set;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dynamic", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/client/ClientPlayerAPI", "dynamic", "(Lapi/player/client/IClientPlayerAPI;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getClientPlayerAPI", "()Lapi/player/client/ClientPlayerAPI;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, isObfuscated ? "blk" : "net/minecraft/client/entity/EntityPlayerSP", "clientPlayerAPI", "Lapi/player/client/ClientPlayerAPI;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getEntityPlayerSP", isObfuscated ? "()Lblk;" : "()Lnet/minecraft/client/entity/EntityPlayerSP;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		cv.visitField(Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, "clientPlayerAPI", "Lapi/player/client/ClientPlayerAPI;", null, null);
	}
}
