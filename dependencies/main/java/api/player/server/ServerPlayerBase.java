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

public abstract class ServerPlayerBase
{
	public ServerPlayerBase(ServerPlayerAPI playerAPI)
	{
		this.internalServerPlayerAPI = playerAPI;
		this.playerAPI = playerAPI.player;
		this.player = playerAPI.player.getEntityPlayerMP();
	}

	public void beforeBaseAttach(boolean onTheFly)
	{
	}

	public void afterBaseAttach(boolean onTheFly)
	{
	}

	public void beforeLocalConstructing(net.minecraft.server.MinecraftServer paramMinecraftServer, net.minecraft.world.WorldServer paramWorldServer, com.mojang.authlib.GameProfile paramGameProfile, net.minecraft.server.management.PlayerInteractionManager paramPlayerInteractionManager)
	{
	}

	public void afterLocalConstructing(net.minecraft.server.MinecraftServer paramMinecraftServer, net.minecraft.world.WorldServer paramWorldServer, com.mojang.authlib.GameProfile paramGameProfile, net.minecraft.server.management.PlayerInteractionManager paramPlayerInteractionManager)
	{
	}

	public void beforeBaseDetach(boolean onTheFly)
	{
	}

	public void afterBaseDetach(boolean onTheFly)
	{
	}

	public Object dynamic(String key, Object[] parameters)
	{
		return internalServerPlayerAPI.dynamicOverwritten(key, parameters, this);
	}

	public final int hashCode()
	{
		return super.hashCode();
	}

	public void beforeAddExhaustion(float paramFloat)
	{
	}

	public void addExhaustion(float paramFloat)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenAddExhaustion(this);

		if(overwritten == null)
			playerAPI.localAddExhaustion(paramFloat);
		else if(overwritten != this)
			overwritten.addExhaustion(paramFloat);

	}

	public void afterAddExhaustion(float paramFloat)
	{
	}

	public void beforeAddExperience(int paramInt)
	{
	}

	public void addExperience(int paramInt)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenAddExperience(this);

		if(overwritten == null)
			playerAPI.localAddExperience(paramInt);
		else if(overwritten != this)
			overwritten.addExperience(paramInt);

	}

	public void afterAddExperience(int paramInt)
	{
	}

	public void beforeAddExperienceLevel(int paramInt)
	{
	}

	public void addExperienceLevel(int paramInt)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenAddExperienceLevel(this);

		if(overwritten == null)
			playerAPI.localAddExperienceLevel(paramInt);
		else if(overwritten != this)
			overwritten.addExperienceLevel(paramInt);

	}

	public void afterAddExperienceLevel(int paramInt)
	{
	}

	public void beforeAddMovementStat(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void addMovementStat(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenAddMovementStat(this);

		if(overwritten == null)
			playerAPI.localAddMovementStat(paramDouble1, paramDouble2, paramDouble3);
		else if(overwritten != this)
			overwritten.addMovementStat(paramDouble1, paramDouble2, paramDouble3);

	}

	public void afterAddMovementStat(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void beforeAttackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
	}

	public boolean attackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenAttackEntityFrom(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localAttackEntityFrom(paramDamageSource, paramFloat);
		else if(overwritten != this)
			_result = overwritten.attackEntityFrom(paramDamageSource, paramFloat);
		else
			_result = false;

		return _result;
	}

	public void afterAttackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
	}

	public void beforeAttackTargetEntityWithCurrentItem(net.minecraft.entity.Entity paramEntity)
	{
	}

	public void attackTargetEntityWithCurrentItem(net.minecraft.entity.Entity paramEntity)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenAttackTargetEntityWithCurrentItem(this);

		if(overwritten == null)
			playerAPI.localAttackTargetEntityWithCurrentItem(paramEntity);
		else if(overwritten != this)
			overwritten.attackTargetEntityWithCurrentItem(paramEntity);

	}

	public void afterAttackTargetEntityWithCurrentItem(net.minecraft.entity.Entity paramEntity)
	{
	}

	public void beforeCanBreatheUnderwater()
	{
	}

	public boolean canBreatheUnderwater()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenCanBreatheUnderwater(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localCanBreatheUnderwater();
		else if(overwritten != this)
			_result = overwritten.canBreatheUnderwater();
		else
			_result = false;

		return _result;
	}

	public void afterCanBreatheUnderwater()
	{
	}

	public void beforeCanHarvestBlock(net.minecraft.block.state.IBlockState paramIBlockState)
	{
	}

	public boolean canHarvestBlock(net.minecraft.block.state.IBlockState paramIBlockState)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenCanHarvestBlock(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localCanHarvestBlock(paramIBlockState);
		else if(overwritten != this)
			_result = overwritten.canHarvestBlock(paramIBlockState);
		else
			_result = false;

		return _result;
	}

	public void afterCanHarvestBlock(net.minecraft.block.state.IBlockState paramIBlockState)
	{
	}

	public void beforeCanPlayerEdit(net.minecraft.util.math.BlockPos paramBlockPos, net.minecraft.util.EnumFacing paramEnumFacing, net.minecraft.item.ItemStack paramItemStack)
	{
	}

	public boolean canPlayerEdit(net.minecraft.util.math.BlockPos paramBlockPos, net.minecraft.util.EnumFacing paramEnumFacing, net.minecraft.item.ItemStack paramItemStack)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenCanPlayerEdit(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localCanPlayerEdit(paramBlockPos, paramEnumFacing, paramItemStack);
		else if(overwritten != this)
			_result = overwritten.canPlayerEdit(paramBlockPos, paramEnumFacing, paramItemStack);
		else
			_result = false;

		return _result;
	}

	public void afterCanPlayerEdit(net.minecraft.util.math.BlockPos paramBlockPos, net.minecraft.util.EnumFacing paramEnumFacing, net.minecraft.item.ItemStack paramItemStack)
	{
	}

	public void beforeCanTriggerWalking()
	{
	}

	public boolean canTriggerWalking()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenCanTriggerWalking(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localCanTriggerWalking();
		else if(overwritten != this)
			_result = overwritten.canTriggerWalking();
		else
			_result = false;

		return _result;
	}

	public void afterCanTriggerWalking()
	{
	}

	public void beforeClonePlayer(net.minecraft.entity.player.EntityPlayerMP paramEntityPlayerMP, boolean paramBoolean)
	{
	}

	public void clonePlayer(net.minecraft.entity.player.EntityPlayerMP paramEntityPlayerMP, boolean paramBoolean)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenClonePlayer(this);

		if(overwritten == null)
			playerAPI.localClonePlayer(paramEntityPlayerMP, paramBoolean);
		else if(overwritten != this)
			overwritten.clonePlayer(paramEntityPlayerMP, paramBoolean);

	}

	public void afterClonePlayer(net.minecraft.entity.player.EntityPlayerMP paramEntityPlayerMP, boolean paramBoolean)
	{
	}

	public void beforeDamageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
	}

	public void damageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenDamageEntity(this);

		if(overwritten == null)
			playerAPI.localDamageEntity(paramDamageSource, paramFloat);
		else if(overwritten != this)
			overwritten.damageEntity(paramDamageSource, paramFloat);

	}

	public void afterDamageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
	}

	public void beforeDisplayGui(net.minecraft.world.IInteractionObject paramIInteractionObject)
	{
	}

	public void displayGui(net.minecraft.world.IInteractionObject paramIInteractionObject)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenDisplayGui(this);

		if(overwritten == null)
			playerAPI.localDisplayGui(paramIInteractionObject);
		else if(overwritten != this)
			overwritten.displayGui(paramIInteractionObject);

	}

	public void afterDisplayGui(net.minecraft.world.IInteractionObject paramIInteractionObject)
	{
	}

	public void beforeDisplayGUIChest(net.minecraft.inventory.IInventory paramIInventory)
	{
	}

	public void displayGUIChest(net.minecraft.inventory.IInventory paramIInventory)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenDisplayGUIChest(this);

		if(overwritten == null)
			playerAPI.localDisplayGUIChest(paramIInventory);
		else if(overwritten != this)
			overwritten.displayGUIChest(paramIInventory);

	}

	public void afterDisplayGUIChest(net.minecraft.inventory.IInventory paramIInventory)
	{
	}

	public void beforeDropOneItem(boolean paramBoolean)
	{
	}

	public net.minecraft.entity.item.EntityItem dropOneItem(boolean paramBoolean)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenDropOneItem(this);

		net.minecraft.entity.item.EntityItem _result;
		if(overwritten == null)
			_result = playerAPI.localDropOneItem(paramBoolean);
		else if(overwritten != this)
			_result = overwritten.dropOneItem(paramBoolean);
		else
			_result = null;

		return _result;
	}

	public void afterDropOneItem(boolean paramBoolean)
	{
	}

	public void beforeDropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean)
	{
	}

	public net.minecraft.entity.item.EntityItem dropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenDropPlayerItemWithRandomChoice(this);

		net.minecraft.entity.item.EntityItem _result;
		if(overwritten == null)
			_result = playerAPI.localDropPlayerItemWithRandomChoice(paramItemStack, paramBoolean);
		else if(overwritten != this)
			_result = overwritten.dropPlayerItemWithRandomChoice(paramItemStack, paramBoolean);
		else
			_result = null;

		return _result;
	}

	public void afterDropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean)
	{
	}

	public void beforeFall(float paramFloat1, float paramFloat2)
	{
	}

	public void fall(float paramFloat1, float paramFloat2)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenFall(this);

		if(overwritten == null)
			playerAPI.localFall(paramFloat1, paramFloat2);
		else if(overwritten != this)
			overwritten.fall(paramFloat1, paramFloat2);

	}

	public void afterFall(float paramFloat1, float paramFloat2)
	{
	}

	public void beforeGetAIMoveSpeed()
	{
	}

	public float getAIMoveSpeed()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenGetAIMoveSpeed(this);

		float _result;
		if(overwritten == null)
			_result = playerAPI.localGetAIMoveSpeed();
		else if(overwritten != this)
			_result = overwritten.getAIMoveSpeed();
		else
			_result = 0;

		return _result;
	}

	public void afterGetAIMoveSpeed()
	{
	}

	public void beforeGetBreakSpeed(net.minecraft.block.state.IBlockState paramIBlockState, net.minecraft.util.math.BlockPos paramBlockPos)
	{
	}

	public float getBreakSpeed(net.minecraft.block.state.IBlockState paramIBlockState, net.minecraft.util.math.BlockPos paramBlockPos)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenGetBreakSpeed(this);

		float _result;
		if(overwritten == null)
			_result = playerAPI.localGetBreakSpeed(paramIBlockState, paramBlockPos);
		else if(overwritten != this)
			_result = overwritten.getBreakSpeed(paramIBlockState, paramBlockPos);
		else
			_result = 0;

		return _result;
	}

	public void afterGetBreakSpeed(net.minecraft.block.state.IBlockState paramIBlockState, net.minecraft.util.math.BlockPos paramBlockPos)
	{
	}

	public void beforeGetDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public double getDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenGetDistanceSq(this);

		double _result;
		if(overwritten == null)
			_result = playerAPI.localGetDistanceSq(paramDouble1, paramDouble2, paramDouble3);
		else if(overwritten != this)
			_result = overwritten.getDistanceSq(paramDouble1, paramDouble2, paramDouble3);
		else
			_result = 0;

		return _result;
	}

	public void afterGetDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void beforeGetBrightness()
	{
	}

	public float getBrightness()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenGetBrightness(this);

		float _result;
		if(overwritten == null)
			_result = playerAPI.localGetBrightness();
		else if(overwritten != this)
			_result = overwritten.getBrightness();
		else
			_result = 0;

		return _result;
	}

	public void afterGetBrightness()
	{
	}

	public void beforeGetEyeHeight()
	{
	}

	public float getEyeHeight()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenGetEyeHeight(this);

		float _result;
		if(overwritten == null)
			_result = playerAPI.localGetEyeHeight();
		else if(overwritten != this)
			_result = overwritten.getEyeHeight();
		else
			_result = 0;

		return _result;
	}

	public void afterGetEyeHeight()
	{
	}

	public void beforeHeal(float paramFloat)
	{
	}

	public void heal(float paramFloat)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenHeal(this);

		if(overwritten == null)
			playerAPI.localHeal(paramFloat);
		else if(overwritten != this)
			overwritten.heal(paramFloat);

	}

	public void afterHeal(float paramFloat)
	{
	}

	public void beforeIsEntityInsideOpaqueBlock()
	{
	}

	public boolean isEntityInsideOpaqueBlock()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenIsEntityInsideOpaqueBlock(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localIsEntityInsideOpaqueBlock();
		else if(overwritten != this)
			_result = overwritten.isEntityInsideOpaqueBlock();
		else
			_result = false;

		return _result;
	}

	public void afterIsEntityInsideOpaqueBlock()
	{
	}

	public void beforeIsInWater()
	{
	}

	public boolean isInWater()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenIsInWater(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localIsInWater();
		else if(overwritten != this)
			_result = overwritten.isInWater();
		else
			_result = false;

		return _result;
	}

	public void afterIsInWater()
	{
	}

	public void beforeIsInsideOfMaterial(net.minecraft.block.material.Material paramMaterial)
	{
	}

	public boolean isInsideOfMaterial(net.minecraft.block.material.Material paramMaterial)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenIsInsideOfMaterial(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localIsInsideOfMaterial(paramMaterial);
		else if(overwritten != this)
			_result = overwritten.isInsideOfMaterial(paramMaterial);
		else
			_result = false;

		return _result;
	}

	public void afterIsInsideOfMaterial(net.minecraft.block.material.Material paramMaterial)
	{
	}

	public void beforeIsOnLadder()
	{
	}

	public boolean isOnLadder()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenIsOnLadder(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localIsOnLadder();
		else if(overwritten != this)
			_result = overwritten.isOnLadder();
		else
			_result = false;

		return _result;
	}

	public void afterIsOnLadder()
	{
	}

	public void beforeIsPlayerSleeping()
	{
	}

	public boolean isPlayerSleeping()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenIsPlayerSleeping(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localIsPlayerSleeping();
		else if(overwritten != this)
			_result = overwritten.isPlayerSleeping();
		else
			_result = false;

		return _result;
	}

	public void afterIsPlayerSleeping()
	{
	}

	public void beforeIsSneaking()
	{
	}

	public boolean isSneaking()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenIsSneaking(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localIsSneaking();
		else if(overwritten != this)
			_result = overwritten.isSneaking();
		else
			_result = false;

		return _result;
	}

	public void afterIsSneaking()
	{
	}

	public void beforeJump()
	{
	}

	public void jump()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenJump(this);

		if(overwritten == null)
			playerAPI.localJump();
		else if(overwritten != this)
			overwritten.jump();

	}

	public void afterJump()
	{
	}

	public void beforeKnockBack(net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2)
	{
	}

	public void knockBack(net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenKnockBack(this);

		if(overwritten == null)
			playerAPI.localKnockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);
		else if(overwritten != this)
			overwritten.knockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);

	}

	public void afterKnockBack(net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2)
	{
	}

	public void beforeMountEntity(net.minecraft.entity.Entity paramEntity, boolean paramBoolean)
	{
	}

	public boolean mountEntity(net.minecraft.entity.Entity paramEntity, boolean paramBoolean)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenMountEntity(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localMountEntity(paramEntity, paramBoolean);
		else if(overwritten != this)
			_result = overwritten.mountEntity(paramEntity, paramBoolean);
		else
			_result = false;

		return _result;
	}

	public void afterMountEntity(net.minecraft.entity.Entity paramEntity, boolean paramBoolean)
	{
	}

	public void beforeMoveEntity(net.minecraft.entity.MoverType paramMoverType, double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void moveEntity(net.minecraft.entity.MoverType paramMoverType, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenMoveEntity(this);

		if(overwritten == null)
			playerAPI.localMoveEntity(paramMoverType, paramDouble1, paramDouble2, paramDouble3);
		else if(overwritten != this)
			overwritten.moveEntity(paramMoverType, paramDouble1, paramDouble2, paramDouble3);

	}

	public void afterMoveEntity(net.minecraft.entity.MoverType paramMoverType, double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void beforeMoveEntityWithHeading(float paramFloat1, float paramFloat2, float paramFloat3)
	{
	}

	public void moveEntityWithHeading(float paramFloat1, float paramFloat2, float paramFloat3)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenMoveEntityWithHeading(this);

		if(overwritten == null)
			playerAPI.localMoveEntityWithHeading(paramFloat1, paramFloat2, paramFloat3);
		else if(overwritten != this)
			overwritten.moveEntityWithHeading(paramFloat1, paramFloat2, paramFloat3);

	}

	public void afterMoveEntityWithHeading(float paramFloat1, float paramFloat2, float paramFloat3)
	{
	}

	public void beforeMoveFlying(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
	{
	}

	public void moveFlying(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenMoveFlying(this);

		if(overwritten == null)
			playerAPI.localMoveFlying(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
		else if(overwritten != this)
			overwritten.moveFlying(paramFloat1, paramFloat2, paramFloat3, paramFloat4);

	}

	public void afterMoveFlying(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
	{
	}

	public void beforeOnDeath(net.minecraft.util.DamageSource paramDamageSource)
	{
	}

	public void onDeath(net.minecraft.util.DamageSource paramDamageSource)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenOnDeath(this);

		if(overwritten == null)
			playerAPI.localOnDeath(paramDamageSource);
		else if(overwritten != this)
			overwritten.onDeath(paramDamageSource);

	}

	public void afterOnDeath(net.minecraft.util.DamageSource paramDamageSource)
	{
	}

	public void beforeOnLivingUpdate()
	{
	}

	public void onLivingUpdate()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenOnLivingUpdate(this);

		if(overwritten == null)
			playerAPI.localOnLivingUpdate();
		else if(overwritten != this)
			overwritten.onLivingUpdate();

	}

	public void afterOnLivingUpdate()
	{
	}

	public void beforeOnKillEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
	}

	public void onKillEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenOnKillEntity(this);

		if(overwritten == null)
			playerAPI.localOnKillEntity(paramEntityLivingBase);
		else if(overwritten != this)
			overwritten.onKillEntity(paramEntityLivingBase);

	}

	public void afterOnKillEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
	}

	public void beforeOnStruckByLightning(net.minecraft.entity.effect.EntityLightningBolt paramEntityLightningBolt)
	{
	}

	public void onStruckByLightning(net.minecraft.entity.effect.EntityLightningBolt paramEntityLightningBolt)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenOnStruckByLightning(this);

		if(overwritten == null)
			playerAPI.localOnStruckByLightning(paramEntityLightningBolt);
		else if(overwritten != this)
			overwritten.onStruckByLightning(paramEntityLightningBolt);

	}

	public void afterOnStruckByLightning(net.minecraft.entity.effect.EntityLightningBolt paramEntityLightningBolt)
	{
	}

	public void beforeOnUpdate()
	{
	}

	public void onUpdate()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenOnUpdate(this);

		if(overwritten == null)
			playerAPI.localOnUpdate();
		else if(overwritten != this)
			overwritten.onUpdate();

	}

	public void afterOnUpdate()
	{
	}

	public void beforeOnUpdateEntity()
	{
	}

	public void onUpdateEntity()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenOnUpdateEntity(this);

		if(overwritten == null)
			playerAPI.localOnUpdateEntity();
		else if(overwritten != this)
			overwritten.onUpdateEntity();

	}

	public void afterOnUpdateEntity()
	{
	}

	public void beforeReadEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
	}

	public void readEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenReadEntityFromNBT(this);

		if(overwritten == null)
			playerAPI.localReadEntityFromNBT(paramNBTTagCompound);
		else if(overwritten != this)
			overwritten.readEntityFromNBT(paramNBTTagCompound);

	}

	public void afterReadEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
	}

	public void beforeSetDead()
	{
	}

	public void setDead()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenSetDead(this);

		if(overwritten == null)
			playerAPI.localSetDead();
		else if(overwritten != this)
			overwritten.setDead();

	}

	public void afterSetDead()
	{
	}

	public void beforeSetEntityActionState(float paramFloat1, float paramFloat2, boolean paramBoolean1, boolean paramBoolean2)
	{
	}

	public void setEntityActionState(float paramFloat1, float paramFloat2, boolean paramBoolean1, boolean paramBoolean2)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenSetEntityActionState(this);

		if(overwritten == null)
			playerAPI.localSetEntityActionState(paramFloat1, paramFloat2, paramBoolean1, paramBoolean2);
		else if(overwritten != this)
			overwritten.setEntityActionState(paramFloat1, paramFloat2, paramBoolean1, paramBoolean2);

	}

	public void afterSetEntityActionState(float paramFloat1, float paramFloat2, boolean paramBoolean1, boolean paramBoolean2)
	{
	}

	public void beforeSetPosition(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void setPosition(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenSetPosition(this);

		if(overwritten == null)
			playerAPI.localSetPosition(paramDouble1, paramDouble2, paramDouble3);
		else if(overwritten != this)
			overwritten.setPosition(paramDouble1, paramDouble2, paramDouble3);

	}

	public void afterSetPosition(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void beforeSetSneaking(boolean paramBoolean)
	{
	}

	public void setSneaking(boolean paramBoolean)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenSetSneaking(this);

		if(overwritten == null)
			playerAPI.localSetSneaking(paramBoolean);
		else if(overwritten != this)
			overwritten.setSneaking(paramBoolean);

	}

	public void afterSetSneaking(boolean paramBoolean)
	{
	}

	public void beforeSetSprinting(boolean paramBoolean)
	{
	}

	public void setSprinting(boolean paramBoolean)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenSetSprinting(this);

		if(overwritten == null)
			playerAPI.localSetSprinting(paramBoolean);
		else if(overwritten != this)
			overwritten.setSprinting(paramBoolean);

	}

	public void afterSetSprinting(boolean paramBoolean)
	{
	}

	public void beforeSwingItem(net.minecraft.util.EnumHand paramEnumHand)
	{
	}

	public void swingItem(net.minecraft.util.EnumHand paramEnumHand)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenSwingItem(this);

		if(overwritten == null)
			playerAPI.localSwingItem(paramEnumHand);
		else if(overwritten != this)
			overwritten.swingItem(paramEnumHand);

	}

	public void afterSwingItem(net.minecraft.util.EnumHand paramEnumHand)
	{
	}

	public void beforeUpdateEntityActionState()
	{
	}

	public void updateEntityActionState()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenUpdateEntityActionState(this);

		if(overwritten == null)
			playerAPI.localUpdateEntityActionState();
		else if(overwritten != this)
			overwritten.updateEntityActionState();

	}

	public void afterUpdateEntityActionState()
	{
	}

	public void beforeUpdatePotionEffects()
	{
	}

	public void updatePotionEffects()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenUpdatePotionEffects(this);

		if(overwritten == null)
			playerAPI.localUpdatePotionEffects();
		else if(overwritten != this)
			overwritten.updatePotionEffects();

	}

	public void afterUpdatePotionEffects()
	{
	}

	public void beforeUpdateRidden()
	{
	}

	public void updateRidden()
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenUpdateRidden(this);

		if(overwritten == null)
			playerAPI.localUpdateRidden();
		else if(overwritten != this)
			overwritten.updateRidden();

	}

	public void afterUpdateRidden()
	{
	}

	public void beforeWakeUpPlayer(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
	{
	}

	public void wakeUpPlayer(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenWakeUpPlayer(this);

		if(overwritten == null)
			playerAPI.localWakeUpPlayer(paramBoolean1, paramBoolean2, paramBoolean3);
		else if(overwritten != this)
			overwritten.wakeUpPlayer(paramBoolean1, paramBoolean2, paramBoolean3);

	}

	public void afterWakeUpPlayer(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
	{
	}

	public void beforeWriteEntityToNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
	}

	public void writeEntityToNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
		ServerPlayerBase overwritten = internalServerPlayerAPI.GetOverwrittenWriteEntityToNBT(this);

		if(overwritten == null)
			playerAPI.localWriteEntityToNBT(paramNBTTagCompound);
		else if(overwritten != this)
			overwritten.writeEntityToNBT(paramNBTTagCompound);

	}

	public void afterWriteEntityToNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
	}

	protected final net.minecraft.entity.player.EntityPlayerMP player;
	protected final IServerPlayer playerAPI;
	private final ServerPlayerAPI internalServerPlayerAPI;
}
