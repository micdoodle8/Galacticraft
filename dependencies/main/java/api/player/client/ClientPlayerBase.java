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

	public void beforeLocalConstructing(net.minecraft.client.Minecraft paramMinecraft, net.minecraft.world.World paramWorld, net.minecraft.util.Session paramSession, int paramInt)
	{
	}

	public void afterLocalConstructing(net.minecraft.client.Minecraft paramMinecraft, net.minecraft.world.World paramWorld, net.minecraft.util.Session paramSession, int paramInt)
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

	public void beforeCanPlayerEdit(int paramInt1, int paramInt2, int paramInt3, int paramInt4, net.minecraft.item.ItemStack paramItemStack)
	{
	}

	public boolean canPlayerEdit(int paramInt1, int paramInt2, int paramInt3, int paramInt4, net.minecraft.item.ItemStack paramItemStack)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenCanPlayerEdit(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localCanPlayerEdit(paramInt1, paramInt2, paramInt3, paramInt4, paramItemStack);
		else if(overwritten != this)
			_result = overwritten.canPlayerEdit(paramInt1, paramInt2, paramInt3, paramInt4, paramItemStack);
		else
			_result = false;

		return _result;
	}

	public void afterCanPlayerEdit(int paramInt1, int paramInt2, int paramInt3, int paramInt4, net.minecraft.item.ItemStack paramItemStack)
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

	public void beforeDisplayGUIBrewingStand(net.minecraft.tileentity.TileEntityBrewingStand paramTileEntityBrewingStand)
	{
	}

	public void displayGUIBrewingStand(net.minecraft.tileentity.TileEntityBrewingStand paramTileEntityBrewingStand)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDisplayGUIBrewingStand(this);

		if(overwritten == null)
			playerAPI.localDisplayGUIBrewingStand(paramTileEntityBrewingStand);
		else if(overwritten != this)
			overwritten.displayGUIBrewingStand(paramTileEntityBrewingStand);

	}

	public void afterDisplayGUIBrewingStand(net.minecraft.tileentity.TileEntityBrewingStand paramTileEntityBrewingStand)
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

	public void beforeDisplayGUIDispenser(net.minecraft.tileentity.TileEntityDispenser paramTileEntityDispenser)
	{
	}

	public void displayGUIDispenser(net.minecraft.tileentity.TileEntityDispenser paramTileEntityDispenser)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDisplayGUIDispenser(this);

		if(overwritten == null)
			playerAPI.localDisplayGUIDispenser(paramTileEntityDispenser);
		else if(overwritten != this)
			overwritten.displayGUIDispenser(paramTileEntityDispenser);

	}

	public void afterDisplayGUIDispenser(net.minecraft.tileentity.TileEntityDispenser paramTileEntityDispenser)
	{
	}

	public void beforeDisplayGUIEditSign(net.minecraft.tileentity.TileEntity paramTileEntity)
	{
	}

	public void displayGUIEditSign(net.minecraft.tileentity.TileEntity paramTileEntity)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDisplayGUIEditSign(this);

		if(overwritten == null)
			playerAPI.localDisplayGUIEditSign(paramTileEntity);
		else if(overwritten != this)
			overwritten.displayGUIEditSign(paramTileEntity);

	}

	public void afterDisplayGUIEditSign(net.minecraft.tileentity.TileEntity paramTileEntity)
	{
	}

	public void beforeDisplayGUIEnchantment(int paramInt1, int paramInt2, int paramInt3, String paramString)
	{
	}

	public void displayGUIEnchantment(int paramInt1, int paramInt2, int paramInt3, String paramString)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDisplayGUIEnchantment(this);

		if(overwritten == null)
			playerAPI.localDisplayGUIEnchantment(paramInt1, paramInt2, paramInt3, paramString);
		else if(overwritten != this)
			overwritten.displayGUIEnchantment(paramInt1, paramInt2, paramInt3, paramString);

	}

	public void afterDisplayGUIEnchantment(int paramInt1, int paramInt2, int paramInt3, String paramString)
	{
	}

	public void beforeDisplayGUIFurnace(net.minecraft.tileentity.TileEntityFurnace paramTileEntityFurnace)
	{
	}

	public void displayGUIFurnace(net.minecraft.tileentity.TileEntityFurnace paramTileEntityFurnace)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDisplayGUIFurnace(this);

		if(overwritten == null)
			playerAPI.localDisplayGUIFurnace(paramTileEntityFurnace);
		else if(overwritten != this)
			overwritten.displayGUIFurnace(paramTileEntityFurnace);

	}

	public void afterDisplayGUIFurnace(net.minecraft.tileentity.TileEntityFurnace paramTileEntityFurnace)
	{
	}

	public void beforeDisplayGUIWorkbench(int paramInt1, int paramInt2, int paramInt3)
	{
	}

	public void displayGUIWorkbench(int paramInt1, int paramInt2, int paramInt3)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDisplayGUIWorkbench(this);

		if(overwritten == null)
			playerAPI.localDisplayGUIWorkbench(paramInt1, paramInt2, paramInt3);
		else if(overwritten != this)
			overwritten.displayGUIWorkbench(paramInt1, paramInt2, paramInt3);

	}

	public void afterDisplayGUIWorkbench(int paramInt1, int paramInt2, int paramInt3)
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

	public void beforeDropPlayerItem(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean)
	{
	}

	public net.minecraft.entity.item.EntityItem dropPlayerItem(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDropPlayerItem(this);

		net.minecraft.entity.item.EntityItem _result;
		if(overwritten == null)
			_result = playerAPI.localDropPlayerItem(paramItemStack, paramBoolean);
		else if(overwritten != this)
			_result = overwritten.dropPlayerItem(paramItemStack, paramBoolean);
		else
			_result = null;

		return _result;
	}

	public void afterDropPlayerItem(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean)
	{
	}

	public void beforeDropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean1, boolean paramBoolean2)
	{
	}

	public net.minecraft.entity.item.EntityItem dropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean1, boolean paramBoolean2)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenDropPlayerItemWithRandomChoice(this);

		net.minecraft.entity.item.EntityItem _result;
		if(overwritten == null)
			_result = playerAPI.localDropPlayerItemWithRandomChoice(paramItemStack, paramBoolean1, paramBoolean2);
		else if(overwritten != this)
			_result = overwritten.dropPlayerItemWithRandomChoice(paramItemStack, paramBoolean1, paramBoolean2);
		else
			_result = null;

		return _result;
	}

	public void afterDropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean1, boolean paramBoolean2)
	{
	}

	public void beforeFall(float paramFloat)
	{
	}

	public void fall(float paramFloat)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenFall(this);

		if(overwritten == null)
			playerAPI.localFall(paramFloat);
		else if(overwritten != this)
			overwritten.fall(paramFloat);

	}

	public void afterFall(float paramFloat)
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

	public void beforeGetCurrentPlayerStrVsBlock(net.minecraft.block.Block paramBlock, boolean paramBoolean)
	{
	}

	public float getCurrentPlayerStrVsBlock(net.minecraft.block.Block paramBlock, boolean paramBoolean)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetCurrentPlayerStrVsBlock(this);

		float _result;
		if(overwritten == null)
			_result = playerAPI.localGetCurrentPlayerStrVsBlock(paramBlock, paramBoolean);
		else if(overwritten != this)
			_result = overwritten.getCurrentPlayerStrVsBlock(paramBlock, paramBoolean);
		else
			_result = 0;

		return _result;
	}

	public void afterGetCurrentPlayerStrVsBlock(net.minecraft.block.Block paramBlock, boolean paramBoolean)
	{
	}

	public void beforeGetCurrentPlayerStrVsBlockForge(net.minecraft.block.Block paramBlock, boolean paramBoolean, int paramInt)
	{
	}

	public float getCurrentPlayerStrVsBlockForge(net.minecraft.block.Block paramBlock, boolean paramBoolean, int paramInt)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetCurrentPlayerStrVsBlockForge(this);

		float _result;
		if(overwritten == null)
			_result = playerAPI.localGetCurrentPlayerStrVsBlockForge(paramBlock, paramBoolean, paramInt);
		else if(overwritten != this)
			_result = overwritten.getCurrentPlayerStrVsBlockForge(paramBlock, paramBoolean, paramInt);
		else
			_result = 0;

		return _result;
	}

	public void afterGetCurrentPlayerStrVsBlockForge(net.minecraft.block.Block paramBlock, boolean paramBoolean, int paramInt)
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

	public void beforeGetFOVMultiplier()
	{
	}

	public float getFOVMultiplier()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetFOVMultiplier(this);

		float _result;
		if(overwritten == null)
			_result = playerAPI.localGetFOVMultiplier();
		else if(overwritten != this)
			_result = overwritten.getFOVMultiplier();
		else
			_result = 0;

		return _result;
	}

	public void afterGetFOVMultiplier()
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

	public void beforeGetItemIcon(net.minecraft.item.ItemStack paramItemStack, int paramInt)
	{
	}

	public net.minecraft.util.IIcon getItemIcon(net.minecraft.item.ItemStack paramItemStack, int paramInt)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenGetItemIcon(this);

		net.minecraft.util.IIcon _result;
		if(overwritten == null)
			_result = playerAPI.localGetItemIcon(paramItemStack, paramInt);
		else if(overwritten != this)
			_result = overwritten.getItemIcon(paramItemStack, paramInt);
		else
			_result = null;

		return _result;
	}

	public void afterGetItemIcon(net.minecraft.item.ItemStack paramItemStack, int paramInt)
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

	public void beforeHandleLavaMovement()
	{
	}

	public boolean handleLavaMovement()
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenHandleLavaMovement(this);

		boolean _result;
		if(overwritten == null)
			_result = playerAPI.localHandleLavaMovement();
		else if(overwritten != this)
			_result = overwritten.handleLavaMovement();
		else
			_result = false;

		return _result;
	}

	public void afterHandleLavaMovement()
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

	public void beforePlayStepSound(int paramInt1, int paramInt2, int paramInt3, net.minecraft.block.Block paramBlock)
	{
	}

	public void playStepSound(int paramInt1, int paramInt2, int paramInt3, net.minecraft.block.Block paramBlock)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenPlayStepSound(this);

		if(overwritten == null)
			playerAPI.localPlayStepSound(paramInt1, paramInt2, paramInt3, paramBlock);
		else if(overwritten != this)
			overwritten.playStepSound(paramInt1, paramInt2, paramInt3, paramBlock);

	}

	public void afterPlayStepSound(int paramInt1, int paramInt2, int paramInt3, net.minecraft.block.Block paramBlock)
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

	public void beforeSleepInBedAt(int paramInt1, int paramInt2, int paramInt3)
	{
	}

	public net.minecraft.entity.player.EntityPlayer.EnumStatus sleepInBedAt(int paramInt1, int paramInt2, int paramInt3)
	{
		ClientPlayerBase overwritten = internalClientPlayerAPI.GetOverwrittenSleepInBedAt(this);

		net.minecraft.entity.player.EntityPlayer.EnumStatus _result;
		if(overwritten == null)
			_result = playerAPI.localSleepInBedAt(paramInt1, paramInt2, paramInt3);
		else if(overwritten != this)
			_result = overwritten.sleepInBedAt(paramInt1, paramInt2, paramInt3);
		else
			_result = null;

		return _result;
	}

	public void afterSleepInBedAt(int paramInt1, int paramInt2, int paramInt3)
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
