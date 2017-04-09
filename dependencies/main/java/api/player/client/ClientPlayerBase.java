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

public abstract class ClientPlayerBase
{
	public ClientPlayerBase(ClientPlayerAPI playerAPI)
	{
		this.internalClientPlayerAPI = playerAPI;
		this.playerAPI = playerAPI.player;
		this.player = playerAPI.player.getEntityPlayerSP();
	}

	public void beforeBaseAttach(boolean onTheFly)
	{
	}

	public void afterBaseAttach(boolean onTheFly)
	{
	}

	public void beforeLocalConstructing(net.minecraft.client.Minecraft paramMinecraft, net.minecraft.world.World paramWorld, net.minecraft.client.network.NetHandlerPlayClient paramNetHandlerPlayClient, net.minecraft.stats.StatFileWriter paramStatFileWriter)
	{
	}

	public void afterLocalConstructing(net.minecraft.client.Minecraft paramMinecraft, net.minecraft.world.World paramWorld, net.minecraft.client.network.NetHandlerPlayClient paramNetHandlerPlayClient, net.minecraft.stats.StatFileWriter paramStatFileWriter)
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
		return internalClientPlayerAPI.dynamicOverwritten(key, parameters, this);
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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenAddExhaustion(this);

		if(overwritten == null)
			playerAPI.localAddExhaustion(paramFloat);
		else if(overwritten != this)
			overwritten.addExhaustion(paramFloat);

	}

	public void afterAddExhaustion(float paramFloat)
	{
	}

	public void beforeAddMovementStat(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void addMovementStat(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenAddMovementStat(this);

		if(overwritten == null)
			playerAPI.localAddMovementStat(paramDouble1, paramDouble2, paramDouble3);
		else if(overwritten != this)
			overwritten.addMovementStat(paramDouble1, paramDouble2, paramDouble3);

	}

	public void afterAddMovementStat(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void beforeAddStat(net.minecraft.stats.StatBase paramStatBase, int paramInt)
	{
	}

	public void addStat(net.minecraft.stats.StatBase paramStatBase, int paramInt)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenAddStat(this);

		if(overwritten == null)
			playerAPI.localAddStat(paramStatBase, paramInt);
		else if(overwritten != this)
			overwritten.addStat(paramStatBase, paramInt);

	}

	public void afterAddStat(net.minecraft.stats.StatBase paramStatBase, int paramInt)
	{
	}

	public void beforeAttackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
	}

	public boolean attackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenAttackEntityFrom(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenAttackTargetEntityWithCurrentItem(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenCanBreatheUnderwater(this);

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

	public void beforeCanHarvestBlock(net.minecraft.block.Block paramBlock)
	{
	}

	public boolean canHarvestBlock(net.minecraft.block.Block paramBlock)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenCanHarvestBlock(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localCanHarvestBlock(paramBlock);
		else if(overwritten != this)
			_result = overwritten.canHarvestBlock(paramBlock);
		else
			_result = false;

		return _result;
	}

	public void afterCanHarvestBlock(net.minecraft.block.Block paramBlock)
	{
	}

	public void beforeCanPlayerEdit(net.minecraft.util.BlockPos paramBlockPos, net.minecraft.util.EnumFacing paramEnumFacing, net.minecraft.item.ItemStack paramItemStack)
	{
	}

	public boolean canPlayerEdit(net.minecraft.util.BlockPos paramBlockPos, net.minecraft.util.EnumFacing paramEnumFacing, net.minecraft.item.ItemStack paramItemStack)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenCanPlayerEdit(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localCanPlayerEdit(paramBlockPos, paramEnumFacing, paramItemStack);
		else if(overwritten != this)
			_result = overwritten.canPlayerEdit(paramBlockPos, paramEnumFacing, paramItemStack);
		else
			_result = false;

		return _result;
	}

	public void afterCanPlayerEdit(net.minecraft.util.BlockPos paramBlockPos, net.minecraft.util.EnumFacing paramEnumFacing, net.minecraft.item.ItemStack paramItemStack)
	{
	}

	public void beforeCanTriggerWalking()
	{
	}

	public boolean canTriggerWalking()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenCanTriggerWalking(this);

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

	public void beforeCloseScreen()
	{
	}

	public void closeScreen()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenCloseScreen(this);

		if(overwritten == null)
			playerAPI.localCloseScreen();
		else if(overwritten != this)
			overwritten.closeScreen();

	}

	public void afterCloseScreen()
	{
	}

	public void beforeDamageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
	}

	public void damageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDamageEntity(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDisplayGui(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDisplayGUIChest(this);

		if(overwritten == null)
			playerAPI.localDisplayGUIChest(paramIInventory);
		else if(overwritten != this)
			overwritten.displayGUIChest(paramIInventory);

	}

	public void afterDisplayGUIChest(net.minecraft.inventory.IInventory paramIInventory)
	{
	}

	public void beforeDropItem(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean1, boolean paramBoolean2)
	{
	}

	public net.minecraft.entity.item.EntityItem dropItem(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean1, boolean paramBoolean2)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDropItem(this);

		net.minecraft.entity.item.EntityItem _result;
		if(overwritten == null)
			_result = playerAPI.localDropItem(paramItemStack, paramBoolean1, paramBoolean2);
		else if(overwritten != this)
			_result = overwritten.dropItem(paramItemStack, paramBoolean1, paramBoolean2);
		else
			_result = null;

		return _result;
	}

	public void afterDropItem(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean1, boolean paramBoolean2)
	{
	}

	public void beforeDropOneItem(boolean paramBoolean)
	{
	}

	public net.minecraft.entity.item.EntityItem dropOneItem(boolean paramBoolean)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDropOneItem(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDropPlayerItemWithRandomChoice(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenFall(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetAIMoveSpeed(this);

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

	public void beforeGetBedOrientationInDegrees()
	{
	}

	public float getBedOrientationInDegrees()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetBedOrientationInDegrees(this);

		float _result;
		if(overwritten == null)
			_result = playerAPI.localGetBedOrientationInDegrees();
		else if(overwritten != this)
			_result = overwritten.getBedOrientationInDegrees();
		else
			_result = 0;

		return _result;
	}

	public void afterGetBedOrientationInDegrees()
	{
	}

	public void beforeGetBrightness(float paramFloat)
	{
	}

	public float getBrightness(float paramFloat)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetBrightness(this);

		float _result;
		if(overwritten == null)
			_result = playerAPI.localGetBrightness(paramFloat);
		else if(overwritten != this)
			_result = overwritten.getBrightness(paramFloat);
		else
			_result = 0;

		return _result;
	}

	public void afterGetBrightness(float paramFloat)
	{
	}

	public void beforeGetBrightnessForRender(float paramFloat)
	{
	}

	public int getBrightnessForRender(float paramFloat)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetBrightnessForRender(this);

		int _result;
		if(overwritten == null)
			_result = playerAPI.localGetBrightnessForRender(paramFloat);
		else if(overwritten != this)
			_result = overwritten.getBrightnessForRender(paramFloat);
		else
			_result = 0;

		return _result;
	}

	public void afterGetBrightnessForRender(float paramFloat)
	{
	}

	public void beforeGetBreakSpeed(net.minecraft.block.state.IBlockState paramIBlockState, net.minecraft.util.BlockPos paramBlockPos)
	{
	}

	public float getBreakSpeed(net.minecraft.block.state.IBlockState paramIBlockState, net.minecraft.util.BlockPos paramBlockPos)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetBreakSpeed(this);

		float _result;
		if(overwritten == null)
			_result = playerAPI.localGetBreakSpeed(paramIBlockState, paramBlockPos);
		else if(overwritten != this)
			_result = overwritten.getBreakSpeed(paramIBlockState, paramBlockPos);
		else
			_result = 0;

		return _result;
	}

	public void afterGetBreakSpeed(net.minecraft.block.state.IBlockState paramIBlockState, net.minecraft.util.BlockPos paramBlockPos)
	{
	}

	public void beforeGetDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public double getDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetDistanceSq(this);

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

	public void beforeGetDistanceSqToEntity(net.minecraft.entity.Entity paramEntity)
	{
	}

	public double getDistanceSqToEntity(net.minecraft.entity.Entity paramEntity)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetDistanceSqToEntity(this);

		double _result;
		if(overwritten == null)
			_result = playerAPI.localGetDistanceSqToEntity(paramEntity);
		else if(overwritten != this)
			_result = overwritten.getDistanceSqToEntity(paramEntity);
		else
			_result = 0;

		return _result;
	}

	public void afterGetDistanceSqToEntity(net.minecraft.entity.Entity paramEntity)
	{
	}

	public void beforeGetFovModifier()
	{
	}

	public float getFovModifier()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetFovModifier(this);

		float _result;
		if(overwritten == null)
			_result = playerAPI.localGetFovModifier();
		else if(overwritten != this)
			_result = overwritten.getFovModifier();
		else
			_result = 0;

		return _result;
	}

	public void afterGetFovModifier()
	{
	}

	public void beforeGetHurtSound()
	{
	}

	public java.lang.String getHurtSound()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetHurtSound(this);

		java.lang.String _result;
		if(overwritten == null)
			_result = playerAPI.localGetHurtSound();
		else if(overwritten != this)
			_result = overwritten.getHurtSound();
		else
			_result = null;

		return _result;
	}

	public void afterGetHurtSound()
	{
	}

	public void beforeGetName()
	{
	}

	public java.lang.String getName()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetName(this);

		java.lang.String _result;
		if(overwritten == null)
			_result = playerAPI.localGetName();
		else if(overwritten != this)
			_result = overwritten.getName();
		else
			_result = null;

		return _result;
	}

	public void afterGetName()
	{
	}

	public void beforeGetSleepTimer()
	{
	}

	public int getSleepTimer()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetSleepTimer(this);

		int _result;
		if(overwritten == null)
			_result = playerAPI.localGetSleepTimer();
		else if(overwritten != this)
			_result = overwritten.getSleepTimer();
		else
			_result = 0;

		return _result;
	}

	public void afterGetSleepTimer()
	{
	}

	public void beforeHandleWaterMovement()
	{
	}

	public boolean handleWaterMovement()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenHandleWaterMovement(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localHandleWaterMovement();
		else if(overwritten != this)
			_result = overwritten.handleWaterMovement();
		else
			_result = false;

		return _result;
	}

	public void afterHandleWaterMovement()
	{
	}

	public void beforeHeal(float paramFloat)
	{
	}

	public void heal(float paramFloat)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenHeal(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenIsEntityInsideOpaqueBlock(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenIsInWater(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenIsInsideOfMaterial(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenIsOnLadder(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenIsPlayerSleeping(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenIsSneaking(this);

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

	public void beforeIsSprinting()
	{
	}

	public boolean isSprinting()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenIsSprinting(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localIsSprinting();
		else if(overwritten != this)
			_result = overwritten.isSprinting();
		else
			_result = false;

		return _result;
	}

	public void afterIsSprinting()
	{
	}

	public void beforeJump()
	{
	}

	public void jump()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenJump(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenKnockBack(this);

		if(overwritten == null)
			playerAPI.localKnockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);
		else if(overwritten != this)
			overwritten.knockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);

	}

	public void afterKnockBack(net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2)
	{
	}

	public void beforeMoveEntity(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void moveEntity(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenMoveEntity(this);

		if(overwritten == null)
			playerAPI.localMoveEntity(paramDouble1, paramDouble2, paramDouble3);
		else if(overwritten != this)
			overwritten.moveEntity(paramDouble1, paramDouble2, paramDouble3);

	}

	public void afterMoveEntity(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void beforeMoveEntityWithHeading(float paramFloat1, float paramFloat2)
	{
	}

	public void moveEntityWithHeading(float paramFloat1, float paramFloat2)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenMoveEntityWithHeading(this);

		if(overwritten == null)
			playerAPI.localMoveEntityWithHeading(paramFloat1, paramFloat2);
		else if(overwritten != this)
			overwritten.moveEntityWithHeading(paramFloat1, paramFloat2);

	}

	public void afterMoveEntityWithHeading(float paramFloat1, float paramFloat2)
	{
	}

	public void beforeMoveFlying(float paramFloat1, float paramFloat2, float paramFloat3)
	{
	}

	public void moveFlying(float paramFloat1, float paramFloat2, float paramFloat3)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenMoveFlying(this);

		if(overwritten == null)
			playerAPI.localMoveFlying(paramFloat1, paramFloat2, paramFloat3);
		else if(overwritten != this)
			overwritten.moveFlying(paramFloat1, paramFloat2, paramFloat3);

	}

	public void afterMoveFlying(float paramFloat1, float paramFloat2, float paramFloat3)
	{
	}

	public void beforeOnDeath(net.minecraft.util.DamageSource paramDamageSource)
	{
	}

	public void onDeath(net.minecraft.util.DamageSource paramDamageSource)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenOnDeath(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenOnLivingUpdate(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenOnKillEntity(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenOnStruckByLightning(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenOnUpdate(this);

		if(overwritten == null)
			playerAPI.localOnUpdate();
		else if(overwritten != this)
			overwritten.onUpdate();

	}

	public void afterOnUpdate()
	{
	}

	public void beforePlayStepSound(net.minecraft.util.BlockPos paramBlockPos, net.minecraft.block.Block paramBlock)
	{
	}

	public void playStepSound(net.minecraft.util.BlockPos paramBlockPos, net.minecraft.block.Block paramBlock)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenPlayStepSound(this);

		if(overwritten == null)
			playerAPI.localPlayStepSound(paramBlockPos, paramBlock);
		else if(overwritten != this)
			overwritten.playStepSound(paramBlockPos, paramBlock);

	}

	public void afterPlayStepSound(net.minecraft.util.BlockPos paramBlockPos, net.minecraft.block.Block paramBlock)
	{
	}

	public void beforePushOutOfBlocks(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public boolean pushOutOfBlocks(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenPushOutOfBlocks(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localPushOutOfBlocks(paramDouble1, paramDouble2, paramDouble3);
		else if(overwritten != this)
			_result = overwritten.pushOutOfBlocks(paramDouble1, paramDouble2, paramDouble3);
		else
			_result = false;

		return _result;
	}

	public void afterPushOutOfBlocks(double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void beforeRayTrace(double paramDouble, float paramFloat)
	{
	}

	public net.minecraft.util.MovingObjectPosition rayTrace(double paramDouble, float paramFloat)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenRayTrace(this);

		net.minecraft.util.MovingObjectPosition _result;
		if(overwritten == null)
			_result = playerAPI.localRayTrace(paramDouble, paramFloat);
		else if(overwritten != this)
			_result = overwritten.rayTrace(paramDouble, paramFloat);
		else
			_result = null;

		return _result;
	}

	public void afterRayTrace(double paramDouble, float paramFloat)
	{
	}

	public void beforeReadEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
	}

	public void readEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenReadEntityFromNBT(this);

		if(overwritten == null)
			playerAPI.localReadEntityFromNBT(paramNBTTagCompound);
		else if(overwritten != this)
			overwritten.readEntityFromNBT(paramNBTTagCompound);

	}

	public void afterReadEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
	}

	public void beforeRespawnPlayer()
	{
	}

	public void respawnPlayer()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenRespawnPlayer(this);

		if(overwritten == null)
			playerAPI.localRespawnPlayer();
		else if(overwritten != this)
			overwritten.respawnPlayer();

	}

	public void afterRespawnPlayer()
	{
	}

	public void beforeSetDead()
	{
	}

	public void setDead()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenSetDead(this);

		if(overwritten == null)
			playerAPI.localSetDead();
		else if(overwritten != this)
			overwritten.setDead();

	}

	public void afterSetDead()
	{
	}

	public void beforeSetPlayerSPHealth(float paramFloat)
	{
	}

	public void setPlayerSPHealth(float paramFloat)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenSetPlayerSPHealth(this);

		if(overwritten == null)
			playerAPI.localSetPlayerSPHealth(paramFloat);
		else if(overwritten != this)
			overwritten.setPlayerSPHealth(paramFloat);

	}

	public void afterSetPlayerSPHealth(float paramFloat)
	{
	}

	public void beforeSetPositionAndRotation(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
	}

	public void setPositionAndRotation(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenSetPositionAndRotation(this);

		if(overwritten == null)
			playerAPI.localSetPositionAndRotation(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
		else if(overwritten != this)
			overwritten.setPositionAndRotation(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

	}

	public void afterSetPositionAndRotation(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
	}

	public void beforeSetSneaking(boolean paramBoolean)
	{
	}

	public void setSneaking(boolean paramBoolean)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenSetSneaking(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenSetSprinting(this);

		if(overwritten == null)
			playerAPI.localSetSprinting(paramBoolean);
		else if(overwritten != this)
			overwritten.setSprinting(paramBoolean);

	}

	public void afterSetSprinting(boolean paramBoolean)
	{
	}

	public void beforeTrySleep(net.minecraft.util.BlockPos paramBlockPos)
	{
	}

	public net.minecraft.entity.player.EntityPlayer.EnumStatus trySleep(net.minecraft.util.BlockPos paramBlockPos)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenTrySleep(this);

		net.minecraft.entity.player.EntityPlayer.EnumStatus _result;
		if(overwritten == null)
			_result = playerAPI.localTrySleep(paramBlockPos);
		else if(overwritten != this)
			_result = overwritten.trySleep(paramBlockPos);
		else
			_result = null;

		return _result;
	}

	public void afterTrySleep(net.minecraft.util.BlockPos paramBlockPos)
	{
	}

	public void beforeSwingItem()
	{
	}

	public void swingItem()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenSwingItem(this);

		if(overwritten == null)
			playerAPI.localSwingItem();
		else if(overwritten != this)
			overwritten.swingItem();

	}

	public void afterSwingItem()
	{
	}

	public void beforeUpdateEntityActionState()
	{
	}

	public void updateEntityActionState()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenUpdateEntityActionState(this);

		if(overwritten == null)
			playerAPI.localUpdateEntityActionState();
		else if(overwritten != this)
			overwritten.updateEntityActionState();

	}

	public void afterUpdateEntityActionState()
	{
	}

	public void beforeUpdateRidden()
	{
	}

	public void updateRidden()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenUpdateRidden(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenWakeUpPlayer(this);

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
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenWriteEntityToNBT(this);

		if(overwritten == null)
			playerAPI.localWriteEntityToNBT(paramNBTTagCompound);
		else if(overwritten != this)
			overwritten.writeEntityToNBT(paramNBTTagCompound);

	}

	public void afterWriteEntityToNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
	}

	protected final net.minecraft.client.entity.EntityPlayerSP player;
	protected final IClientPlayer playerAPI;
	private final ClientPlayerAPI internalClientPlayerAPI;
}
