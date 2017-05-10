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

import java.util.*;

public final class ServerPlayerBaseSorting
{
	private String[] beforeLocalConstructingSuperiors = null;
	private String[] beforeLocalConstructingInferiors = null;
	private String[] afterLocalConstructingSuperiors = null;
	private String[] afterLocalConstructingInferiors = null;

	private Map<String, String[]> dynamicBeforeSuperiors = null;
	private Map<String, String[]> dynamicBeforeInferiors = null;
	private Map<String, String[]> dynamicOverrideSuperiors = null;
	private Map<String, String[]> dynamicOverrideInferiors = null;
	private Map<String, String[]> dynamicAfterSuperiors = null;
	private Map<String, String[]> dynamicAfterInferiors = null;

	private String[] beforeAddExhaustionSuperiors = null;
	private String[] beforeAddExhaustionInferiors = null;
	private String[] overrideAddExhaustionSuperiors = null;
	private String[] overrideAddExhaustionInferiors = null;
	private String[] afterAddExhaustionSuperiors = null;
	private String[] afterAddExhaustionInferiors = null;

	private String[] beforeAddExperienceSuperiors = null;
	private String[] beforeAddExperienceInferiors = null;
	private String[] overrideAddExperienceSuperiors = null;
	private String[] overrideAddExperienceInferiors = null;
	private String[] afterAddExperienceSuperiors = null;
	private String[] afterAddExperienceInferiors = null;

	private String[] beforeAddExperienceLevelSuperiors = null;
	private String[] beforeAddExperienceLevelInferiors = null;
	private String[] overrideAddExperienceLevelSuperiors = null;
	private String[] overrideAddExperienceLevelInferiors = null;
	private String[] afterAddExperienceLevelSuperiors = null;
	private String[] afterAddExperienceLevelInferiors = null;

	private String[] beforeAddMovementStatSuperiors = null;
	private String[] beforeAddMovementStatInferiors = null;
	private String[] overrideAddMovementStatSuperiors = null;
	private String[] overrideAddMovementStatInferiors = null;
	private String[] afterAddMovementStatSuperiors = null;
	private String[] afterAddMovementStatInferiors = null;

	private String[] beforeAttackEntityFromSuperiors = null;
	private String[] beforeAttackEntityFromInferiors = null;
	private String[] overrideAttackEntityFromSuperiors = null;
	private String[] overrideAttackEntityFromInferiors = null;
	private String[] afterAttackEntityFromSuperiors = null;
	private String[] afterAttackEntityFromInferiors = null;

	private String[] beforeAttackTargetEntityWithCurrentItemSuperiors = null;
	private String[] beforeAttackTargetEntityWithCurrentItemInferiors = null;
	private String[] overrideAttackTargetEntityWithCurrentItemSuperiors = null;
	private String[] overrideAttackTargetEntityWithCurrentItemInferiors = null;
	private String[] afterAttackTargetEntityWithCurrentItemSuperiors = null;
	private String[] afterAttackTargetEntityWithCurrentItemInferiors = null;

	private String[] beforeCanBreatheUnderwaterSuperiors = null;
	private String[] beforeCanBreatheUnderwaterInferiors = null;
	private String[] overrideCanBreatheUnderwaterSuperiors = null;
	private String[] overrideCanBreatheUnderwaterInferiors = null;
	private String[] afterCanBreatheUnderwaterSuperiors = null;
	private String[] afterCanBreatheUnderwaterInferiors = null;

	private String[] beforeCanHarvestBlockSuperiors = null;
	private String[] beforeCanHarvestBlockInferiors = null;
	private String[] overrideCanHarvestBlockSuperiors = null;
	private String[] overrideCanHarvestBlockInferiors = null;
	private String[] afterCanHarvestBlockSuperiors = null;
	private String[] afterCanHarvestBlockInferiors = null;

	private String[] beforeCanPlayerEditSuperiors = null;
	private String[] beforeCanPlayerEditInferiors = null;
	private String[] overrideCanPlayerEditSuperiors = null;
	private String[] overrideCanPlayerEditInferiors = null;
	private String[] afterCanPlayerEditSuperiors = null;
	private String[] afterCanPlayerEditInferiors = null;

	private String[] beforeCanTriggerWalkingSuperiors = null;
	private String[] beforeCanTriggerWalkingInferiors = null;
	private String[] overrideCanTriggerWalkingSuperiors = null;
	private String[] overrideCanTriggerWalkingInferiors = null;
	private String[] afterCanTriggerWalkingSuperiors = null;
	private String[] afterCanTriggerWalkingInferiors = null;

	private String[] beforeClonePlayerSuperiors = null;
	private String[] beforeClonePlayerInferiors = null;
	private String[] overrideClonePlayerSuperiors = null;
	private String[] overrideClonePlayerInferiors = null;
	private String[] afterClonePlayerSuperiors = null;
	private String[] afterClonePlayerInferiors = null;

	private String[] beforeDamageEntitySuperiors = null;
	private String[] beforeDamageEntityInferiors = null;
	private String[] overrideDamageEntitySuperiors = null;
	private String[] overrideDamageEntityInferiors = null;
	private String[] afterDamageEntitySuperiors = null;
	private String[] afterDamageEntityInferiors = null;

	private String[] beforeDisplayGUIChestSuperiors = null;
	private String[] beforeDisplayGUIChestInferiors = null;
	private String[] overrideDisplayGUIChestSuperiors = null;
	private String[] overrideDisplayGUIChestInferiors = null;
	private String[] afterDisplayGUIChestSuperiors = null;
	private String[] afterDisplayGUIChestInferiors = null;

	private String[] beforeDisplayGUIDispenserSuperiors = null;
	private String[] beforeDisplayGUIDispenserInferiors = null;
	private String[] overrideDisplayGUIDispenserSuperiors = null;
	private String[] overrideDisplayGUIDispenserInferiors = null;
	private String[] afterDisplayGUIDispenserSuperiors = null;
	private String[] afterDisplayGUIDispenserInferiors = null;

	private String[] beforeDisplayGUIFurnaceSuperiors = null;
	private String[] beforeDisplayGUIFurnaceInferiors = null;
	private String[] overrideDisplayGUIFurnaceSuperiors = null;
	private String[] overrideDisplayGUIFurnaceInferiors = null;
	private String[] afterDisplayGUIFurnaceSuperiors = null;
	private String[] afterDisplayGUIFurnaceInferiors = null;

	private String[] beforeDisplayGUIWorkbenchSuperiors = null;
	private String[] beforeDisplayGUIWorkbenchInferiors = null;
	private String[] overrideDisplayGUIWorkbenchSuperiors = null;
	private String[] overrideDisplayGUIWorkbenchInferiors = null;
	private String[] afterDisplayGUIWorkbenchSuperiors = null;
	private String[] afterDisplayGUIWorkbenchInferiors = null;

	private String[] beforeDropOneItemSuperiors = null;
	private String[] beforeDropOneItemInferiors = null;
	private String[] overrideDropOneItemSuperiors = null;
	private String[] overrideDropOneItemInferiors = null;
	private String[] afterDropOneItemSuperiors = null;
	private String[] afterDropOneItemInferiors = null;

	private String[] beforeDropPlayerItemSuperiors = null;
	private String[] beforeDropPlayerItemInferiors = null;
	private String[] overrideDropPlayerItemSuperiors = null;
	private String[] overrideDropPlayerItemInferiors = null;
	private String[] afterDropPlayerItemSuperiors = null;
	private String[] afterDropPlayerItemInferiors = null;

	private String[] beforeFallSuperiors = null;
	private String[] beforeFallInferiors = null;
	private String[] overrideFallSuperiors = null;
	private String[] overrideFallInferiors = null;
	private String[] afterFallSuperiors = null;
	private String[] afterFallInferiors = null;

	private String[] beforeGetAIMoveSpeedSuperiors = null;
	private String[] beforeGetAIMoveSpeedInferiors = null;
	private String[] overrideGetAIMoveSpeedSuperiors = null;
	private String[] overrideGetAIMoveSpeedInferiors = null;
	private String[] afterGetAIMoveSpeedSuperiors = null;
	private String[] afterGetAIMoveSpeedInferiors = null;

	private String[] beforeGetCurrentPlayerStrVsBlockSuperiors = null;
	private String[] beforeGetCurrentPlayerStrVsBlockInferiors = null;
	private String[] overrideGetCurrentPlayerStrVsBlockSuperiors = null;
	private String[] overrideGetCurrentPlayerStrVsBlockInferiors = null;
	private String[] afterGetCurrentPlayerStrVsBlockSuperiors = null;
	private String[] afterGetCurrentPlayerStrVsBlockInferiors = null;

	private String[] beforeGetCurrentPlayerStrVsBlockForgeSuperiors = null;
	private String[] beforeGetCurrentPlayerStrVsBlockForgeInferiors = null;
	private String[] overrideGetCurrentPlayerStrVsBlockForgeSuperiors = null;
	private String[] overrideGetCurrentPlayerStrVsBlockForgeInferiors = null;
	private String[] afterGetCurrentPlayerStrVsBlockForgeSuperiors = null;
	private String[] afterGetCurrentPlayerStrVsBlockForgeInferiors = null;

	private String[] beforeGetDistanceSqSuperiors = null;
	private String[] beforeGetDistanceSqInferiors = null;
	private String[] overrideGetDistanceSqSuperiors = null;
	private String[] overrideGetDistanceSqInferiors = null;
	private String[] afterGetDistanceSqSuperiors = null;
	private String[] afterGetDistanceSqInferiors = null;

	private String[] beforeGetBrightnessSuperiors = null;
	private String[] beforeGetBrightnessInferiors = null;
	private String[] overrideGetBrightnessSuperiors = null;
	private String[] overrideGetBrightnessInferiors = null;
	private String[] afterGetBrightnessSuperiors = null;
	private String[] afterGetBrightnessInferiors = null;

	private String[] beforeGetEyeHeightSuperiors = null;
	private String[] beforeGetEyeHeightInferiors = null;
	private String[] overrideGetEyeHeightSuperiors = null;
	private String[] overrideGetEyeHeightInferiors = null;
	private String[] afterGetEyeHeightSuperiors = null;
	private String[] afterGetEyeHeightInferiors = null;

	private String[] beforeHealSuperiors = null;
	private String[] beforeHealInferiors = null;
	private String[] overrideHealSuperiors = null;
	private String[] overrideHealInferiors = null;
	private String[] afterHealSuperiors = null;
	private String[] afterHealInferiors = null;

	private String[] beforeIsEntityInsideOpaqueBlockSuperiors = null;
	private String[] beforeIsEntityInsideOpaqueBlockInferiors = null;
	private String[] overrideIsEntityInsideOpaqueBlockSuperiors = null;
	private String[] overrideIsEntityInsideOpaqueBlockInferiors = null;
	private String[] afterIsEntityInsideOpaqueBlockSuperiors = null;
	private String[] afterIsEntityInsideOpaqueBlockInferiors = null;

	private String[] beforeIsInWaterSuperiors = null;
	private String[] beforeIsInWaterInferiors = null;
	private String[] overrideIsInWaterSuperiors = null;
	private String[] overrideIsInWaterInferiors = null;
	private String[] afterIsInWaterSuperiors = null;
	private String[] afterIsInWaterInferiors = null;

	private String[] beforeIsInsideOfMaterialSuperiors = null;
	private String[] beforeIsInsideOfMaterialInferiors = null;
	private String[] overrideIsInsideOfMaterialSuperiors = null;
	private String[] overrideIsInsideOfMaterialInferiors = null;
	private String[] afterIsInsideOfMaterialSuperiors = null;
	private String[] afterIsInsideOfMaterialInferiors = null;

	private String[] beforeIsOnLadderSuperiors = null;
	private String[] beforeIsOnLadderInferiors = null;
	private String[] overrideIsOnLadderSuperiors = null;
	private String[] overrideIsOnLadderInferiors = null;
	private String[] afterIsOnLadderSuperiors = null;
	private String[] afterIsOnLadderInferiors = null;

	private String[] beforeIsPlayerSleepingSuperiors = null;
	private String[] beforeIsPlayerSleepingInferiors = null;
	private String[] overrideIsPlayerSleepingSuperiors = null;
	private String[] overrideIsPlayerSleepingInferiors = null;
	private String[] afterIsPlayerSleepingSuperiors = null;
	private String[] afterIsPlayerSleepingInferiors = null;

	private String[] beforeIsSneakingSuperiors = null;
	private String[] beforeIsSneakingInferiors = null;
	private String[] overrideIsSneakingSuperiors = null;
	private String[] overrideIsSneakingInferiors = null;
	private String[] afterIsSneakingSuperiors = null;
	private String[] afterIsSneakingInferiors = null;

	private String[] beforeJumpSuperiors = null;
	private String[] beforeJumpInferiors = null;
	private String[] overrideJumpSuperiors = null;
	private String[] overrideJumpInferiors = null;
	private String[] afterJumpSuperiors = null;
	private String[] afterJumpInferiors = null;

	private String[] beforeKnockBackSuperiors = null;
	private String[] beforeKnockBackInferiors = null;
	private String[] overrideKnockBackSuperiors = null;
	private String[] overrideKnockBackInferiors = null;
	private String[] afterKnockBackSuperiors = null;
	private String[] afterKnockBackInferiors = null;

	private String[] beforeMountEntitySuperiors = null;
	private String[] beforeMountEntityInferiors = null;
	private String[] overrideMountEntitySuperiors = null;
	private String[] overrideMountEntityInferiors = null;
	private String[] afterMountEntitySuperiors = null;
	private String[] afterMountEntityInferiors = null;

	private String[] beforeMoveEntitySuperiors = null;
	private String[] beforeMoveEntityInferiors = null;
	private String[] overrideMoveEntitySuperiors = null;
	private String[] overrideMoveEntityInferiors = null;
	private String[] afterMoveEntitySuperiors = null;
	private String[] afterMoveEntityInferiors = null;

	private String[] beforeMoveEntityWithHeadingSuperiors = null;
	private String[] beforeMoveEntityWithHeadingInferiors = null;
	private String[] overrideMoveEntityWithHeadingSuperiors = null;
	private String[] overrideMoveEntityWithHeadingInferiors = null;
	private String[] afterMoveEntityWithHeadingSuperiors = null;
	private String[] afterMoveEntityWithHeadingInferiors = null;

	private String[] beforeMoveFlyingSuperiors = null;
	private String[] beforeMoveFlyingInferiors = null;
	private String[] overrideMoveFlyingSuperiors = null;
	private String[] overrideMoveFlyingInferiors = null;
	private String[] afterMoveFlyingSuperiors = null;
	private String[] afterMoveFlyingInferiors = null;

	private String[] beforeOnDeathSuperiors = null;
	private String[] beforeOnDeathInferiors = null;
	private String[] overrideOnDeathSuperiors = null;
	private String[] overrideOnDeathInferiors = null;
	private String[] afterOnDeathSuperiors = null;
	private String[] afterOnDeathInferiors = null;

	private String[] beforeOnLivingUpdateSuperiors = null;
	private String[] beforeOnLivingUpdateInferiors = null;
	private String[] overrideOnLivingUpdateSuperiors = null;
	private String[] overrideOnLivingUpdateInferiors = null;
	private String[] afterOnLivingUpdateSuperiors = null;
	private String[] afterOnLivingUpdateInferiors = null;

	private String[] beforeOnKillEntitySuperiors = null;
	private String[] beforeOnKillEntityInferiors = null;
	private String[] overrideOnKillEntitySuperiors = null;
	private String[] overrideOnKillEntityInferiors = null;
	private String[] afterOnKillEntitySuperiors = null;
	private String[] afterOnKillEntityInferiors = null;

	private String[] beforeOnStruckByLightningSuperiors = null;
	private String[] beforeOnStruckByLightningInferiors = null;
	private String[] overrideOnStruckByLightningSuperiors = null;
	private String[] overrideOnStruckByLightningInferiors = null;
	private String[] afterOnStruckByLightningSuperiors = null;
	private String[] afterOnStruckByLightningInferiors = null;

	private String[] beforeOnUpdateSuperiors = null;
	private String[] beforeOnUpdateInferiors = null;
	private String[] overrideOnUpdateSuperiors = null;
	private String[] overrideOnUpdateInferiors = null;
	private String[] afterOnUpdateSuperiors = null;
	private String[] afterOnUpdateInferiors = null;

	private String[] beforeOnUpdateEntitySuperiors = null;
	private String[] beforeOnUpdateEntityInferiors = null;
	private String[] overrideOnUpdateEntitySuperiors = null;
	private String[] overrideOnUpdateEntityInferiors = null;
	private String[] afterOnUpdateEntitySuperiors = null;
	private String[] afterOnUpdateEntityInferiors = null;

	private String[] beforeReadEntityFromNBTSuperiors = null;
	private String[] beforeReadEntityFromNBTInferiors = null;
	private String[] overrideReadEntityFromNBTSuperiors = null;
	private String[] overrideReadEntityFromNBTInferiors = null;
	private String[] afterReadEntityFromNBTSuperiors = null;
	private String[] afterReadEntityFromNBTInferiors = null;

	private String[] beforeSetDeadSuperiors = null;
	private String[] beforeSetDeadInferiors = null;
	private String[] overrideSetDeadSuperiors = null;
	private String[] overrideSetDeadInferiors = null;
	private String[] afterSetDeadSuperiors = null;
	private String[] afterSetDeadInferiors = null;

	private String[] beforeSetEntityActionStateSuperiors = null;
	private String[] beforeSetEntityActionStateInferiors = null;
	private String[] overrideSetEntityActionStateSuperiors = null;
	private String[] overrideSetEntityActionStateInferiors = null;
	private String[] afterSetEntityActionStateSuperiors = null;
	private String[] afterSetEntityActionStateInferiors = null;

	private String[] beforeSetPositionSuperiors = null;
	private String[] beforeSetPositionInferiors = null;
	private String[] overrideSetPositionSuperiors = null;
	private String[] overrideSetPositionInferiors = null;
	private String[] afterSetPositionSuperiors = null;
	private String[] afterSetPositionInferiors = null;

	private String[] beforeSetSneakingSuperiors = null;
	private String[] beforeSetSneakingInferiors = null;
	private String[] overrideSetSneakingSuperiors = null;
	private String[] overrideSetSneakingInferiors = null;
	private String[] afterSetSneakingSuperiors = null;
	private String[] afterSetSneakingInferiors = null;

	private String[] beforeSetSprintingSuperiors = null;
	private String[] beforeSetSprintingInferiors = null;
	private String[] overrideSetSprintingSuperiors = null;
	private String[] overrideSetSprintingInferiors = null;
	private String[] afterSetSprintingSuperiors = null;
	private String[] afterSetSprintingInferiors = null;

	private String[] beforeSwingItemSuperiors = null;
	private String[] beforeSwingItemInferiors = null;
	private String[] overrideSwingItemSuperiors = null;
	private String[] overrideSwingItemInferiors = null;
	private String[] afterSwingItemSuperiors = null;
	private String[] afterSwingItemInferiors = null;

	private String[] beforeUpdateEntityActionStateSuperiors = null;
	private String[] beforeUpdateEntityActionStateInferiors = null;
	private String[] overrideUpdateEntityActionStateSuperiors = null;
	private String[] overrideUpdateEntityActionStateInferiors = null;
	private String[] afterUpdateEntityActionStateSuperiors = null;
	private String[] afterUpdateEntityActionStateInferiors = null;

	private String[] beforeUpdatePotionEffectsSuperiors = null;
	private String[] beforeUpdatePotionEffectsInferiors = null;
	private String[] overrideUpdatePotionEffectsSuperiors = null;
	private String[] overrideUpdatePotionEffectsInferiors = null;
	private String[] afterUpdatePotionEffectsSuperiors = null;
	private String[] afterUpdatePotionEffectsInferiors = null;

	private String[] beforeUpdateRiddenSuperiors = null;
	private String[] beforeUpdateRiddenInferiors = null;
	private String[] overrideUpdateRiddenSuperiors = null;
	private String[] overrideUpdateRiddenInferiors = null;
	private String[] afterUpdateRiddenSuperiors = null;
	private String[] afterUpdateRiddenInferiors = null;

	private String[] beforeWakeUpPlayerSuperiors = null;
	private String[] beforeWakeUpPlayerInferiors = null;
	private String[] overrideWakeUpPlayerSuperiors = null;
	private String[] overrideWakeUpPlayerInferiors = null;
	private String[] afterWakeUpPlayerSuperiors = null;
	private String[] afterWakeUpPlayerInferiors = null;

	private String[] beforeWriteEntityToNBTSuperiors = null;
	private String[] beforeWriteEntityToNBTInferiors = null;
	private String[] overrideWriteEntityToNBTSuperiors = null;
	private String[] overrideWriteEntityToNBTInferiors = null;
	private String[] afterWriteEntityToNBTSuperiors = null;
	private String[] afterWriteEntityToNBTInferiors = null;


	public String[] getBeforeLocalConstructingSuperiors()
	{
		return beforeLocalConstructingSuperiors;
	}

	public String[] getBeforeLocalConstructingInferiors()
	{
		return beforeLocalConstructingInferiors;
	}

	public String[] getAfterLocalConstructingSuperiors()
	{
		return afterLocalConstructingSuperiors;
	}

	public String[] getAfterLocalConstructingInferiors()
	{
		return afterLocalConstructingInferiors;
	}

	public void setBeforeLocalConstructingSuperiors(String[] value)
	{
		beforeLocalConstructingSuperiors = value;
	}

	public void setBeforeLocalConstructingInferiors(String[] value)
	{
		beforeLocalConstructingInferiors = value;
	}

	public void setAfterLocalConstructingSuperiors(String[] value)
	{
		afterLocalConstructingSuperiors = value;
	}

	public void setAfterLocalConstructingInferiors(String[] value)
	{
		afterLocalConstructingInferiors = value;
	}

	public Map<String, String[]> getDynamicBeforeSuperiors()
	{
		return dynamicBeforeSuperiors;
	}

	public Map<String, String[]> getDynamicBeforeInferiors()
	{
		return dynamicBeforeInferiors;
	}

	public Map<String, String[]> getDynamicOverrideSuperiors()
	{
		return dynamicOverrideSuperiors;
	}

	public Map<String, String[]> getDynamicOverrideInferiors()
	{
		return dynamicOverrideInferiors;
	}

	public Map<String, String[]> getDynamicAfterSuperiors()
	{
		return dynamicAfterSuperiors;
	}

	public Map<String, String[]> getDynamicAfterInferiors()
	{
		return dynamicAfterInferiors;
	}

	public void setDynamicBeforeSuperiors(String name, String[] superiors)
	{
		dynamicBeforeSuperiors = setDynamic(name, superiors, dynamicBeforeSuperiors);
	}

	public void setDynamicBeforeInferiors(String name, String[] inferiors)
	{
		dynamicBeforeInferiors = setDynamic(name, inferiors, dynamicBeforeInferiors);
	}

	public void setDynamicOverrideSuperiors(String name, String[] superiors)
	{
		dynamicOverrideSuperiors = setDynamic(name, superiors, dynamicOverrideSuperiors);
	}

	public void setDynamicOverrideInferiors(String name, String[] inferiors)
	{
		dynamicOverrideInferiors = setDynamic(name, inferiors, dynamicOverrideInferiors);
	}

	public void setDynamicAfterSuperiors(String name, String[] superiors)
	{
		dynamicAfterSuperiors = setDynamic(name, superiors, dynamicAfterSuperiors);
	}

	public void setDynamicAfterInferiors(String name, String[] inferiors)
	{
		dynamicAfterInferiors = setDynamic(name, inferiors, dynamicAfterInferiors);
	}

	private Map<String, String[]> setDynamic(String name, String[] names, Map<String, String[]> map)
	{
		if(name == null)
			throw new IllegalArgumentException("Parameter 'name' may not be null");

		if(names == null)
		{
			if(map != null)
				map.remove(name);
			return map;
		}

		if(map == null)
			map = new HashMap<String, String[]>();
		map.put(name, names);

		return map;
	}

	public String[] getBeforeAddExhaustionSuperiors()
	{
		return beforeAddExhaustionSuperiors;
	}

	public String[] getBeforeAddExhaustionInferiors()
	{
		return beforeAddExhaustionInferiors;
	}

	public String[] getOverrideAddExhaustionSuperiors()
	{
		return overrideAddExhaustionSuperiors;
	}

	public String[] getOverrideAddExhaustionInferiors()
	{
		return overrideAddExhaustionInferiors;
	}

	public String[] getAfterAddExhaustionSuperiors()
	{
		return afterAddExhaustionSuperiors;
	}

	public String[] getAfterAddExhaustionInferiors()
	{
		return afterAddExhaustionInferiors;
	}

	public void setBeforeAddExhaustionSuperiors(String[] value)
	{
		beforeAddExhaustionSuperiors = value;
	}

	public void setBeforeAddExhaustionInferiors(String[] value)
	{
		beforeAddExhaustionInferiors = value;
	}

	public void setOverrideAddExhaustionSuperiors(String[] value)
	{
		overrideAddExhaustionSuperiors = value;
	}

	public void setOverrideAddExhaustionInferiors(String[] value)
	{
		overrideAddExhaustionInferiors = value;
	}

	public void setAfterAddExhaustionSuperiors(String[] value)
	{
		afterAddExhaustionSuperiors = value;
	}

	public void setAfterAddExhaustionInferiors(String[] value)
	{
		afterAddExhaustionInferiors = value;
	}

	public String[] getBeforeAddExperienceSuperiors()
	{
		return beforeAddExperienceSuperiors;
	}

	public String[] getBeforeAddExperienceInferiors()
	{
		return beforeAddExperienceInferiors;
	}

	public String[] getOverrideAddExperienceSuperiors()
	{
		return overrideAddExperienceSuperiors;
	}

	public String[] getOverrideAddExperienceInferiors()
	{
		return overrideAddExperienceInferiors;
	}

	public String[] getAfterAddExperienceSuperiors()
	{
		return afterAddExperienceSuperiors;
	}

	public String[] getAfterAddExperienceInferiors()
	{
		return afterAddExperienceInferiors;
	}

	public void setBeforeAddExperienceSuperiors(String[] value)
	{
		beforeAddExperienceSuperiors = value;
	}

	public void setBeforeAddExperienceInferiors(String[] value)
	{
		beforeAddExperienceInferiors = value;
	}

	public void setOverrideAddExperienceSuperiors(String[] value)
	{
		overrideAddExperienceSuperiors = value;
	}

	public void setOverrideAddExperienceInferiors(String[] value)
	{
		overrideAddExperienceInferiors = value;
	}

	public void setAfterAddExperienceSuperiors(String[] value)
	{
		afterAddExperienceSuperiors = value;
	}

	public void setAfterAddExperienceInferiors(String[] value)
	{
		afterAddExperienceInferiors = value;
	}

	public String[] getBeforeAddExperienceLevelSuperiors()
	{
		return beforeAddExperienceLevelSuperiors;
	}

	public String[] getBeforeAddExperienceLevelInferiors()
	{
		return beforeAddExperienceLevelInferiors;
	}

	public String[] getOverrideAddExperienceLevelSuperiors()
	{
		return overrideAddExperienceLevelSuperiors;
	}

	public String[] getOverrideAddExperienceLevelInferiors()
	{
		return overrideAddExperienceLevelInferiors;
	}

	public String[] getAfterAddExperienceLevelSuperiors()
	{
		return afterAddExperienceLevelSuperiors;
	}

	public String[] getAfterAddExperienceLevelInferiors()
	{
		return afterAddExperienceLevelInferiors;
	}

	public void setBeforeAddExperienceLevelSuperiors(String[] value)
	{
		beforeAddExperienceLevelSuperiors = value;
	}

	public void setBeforeAddExperienceLevelInferiors(String[] value)
	{
		beforeAddExperienceLevelInferiors = value;
	}

	public void setOverrideAddExperienceLevelSuperiors(String[] value)
	{
		overrideAddExperienceLevelSuperiors = value;
	}

	public void setOverrideAddExperienceLevelInferiors(String[] value)
	{
		overrideAddExperienceLevelInferiors = value;
	}

	public void setAfterAddExperienceLevelSuperiors(String[] value)
	{
		afterAddExperienceLevelSuperiors = value;
	}

	public void setAfterAddExperienceLevelInferiors(String[] value)
	{
		afterAddExperienceLevelInferiors = value;
	}

	public String[] getBeforeAddMovementStatSuperiors()
	{
		return beforeAddMovementStatSuperiors;
	}

	public String[] getBeforeAddMovementStatInferiors()
	{
		return beforeAddMovementStatInferiors;
	}

	public String[] getOverrideAddMovementStatSuperiors()
	{
		return overrideAddMovementStatSuperiors;
	}

	public String[] getOverrideAddMovementStatInferiors()
	{
		return overrideAddMovementStatInferiors;
	}

	public String[] getAfterAddMovementStatSuperiors()
	{
		return afterAddMovementStatSuperiors;
	}

	public String[] getAfterAddMovementStatInferiors()
	{
		return afterAddMovementStatInferiors;
	}

	public void setBeforeAddMovementStatSuperiors(String[] value)
	{
		beforeAddMovementStatSuperiors = value;
	}

	public void setBeforeAddMovementStatInferiors(String[] value)
	{
		beforeAddMovementStatInferiors = value;
	}

	public void setOverrideAddMovementStatSuperiors(String[] value)
	{
		overrideAddMovementStatSuperiors = value;
	}

	public void setOverrideAddMovementStatInferiors(String[] value)
	{
		overrideAddMovementStatInferiors = value;
	}

	public void setAfterAddMovementStatSuperiors(String[] value)
	{
		afterAddMovementStatSuperiors = value;
	}

	public void setAfterAddMovementStatInferiors(String[] value)
	{
		afterAddMovementStatInferiors = value;
	}

	public String[] getBeforeAttackEntityFromSuperiors()
	{
		return beforeAttackEntityFromSuperiors;
	}

	public String[] getBeforeAttackEntityFromInferiors()
	{
		return beforeAttackEntityFromInferiors;
	}

	public String[] getOverrideAttackEntityFromSuperiors()
	{
		return overrideAttackEntityFromSuperiors;
	}

	public String[] getOverrideAttackEntityFromInferiors()
	{
		return overrideAttackEntityFromInferiors;
	}

	public String[] getAfterAttackEntityFromSuperiors()
	{
		return afterAttackEntityFromSuperiors;
	}

	public String[] getAfterAttackEntityFromInferiors()
	{
		return afterAttackEntityFromInferiors;
	}

	public void setBeforeAttackEntityFromSuperiors(String[] value)
	{
		beforeAttackEntityFromSuperiors = value;
	}

	public void setBeforeAttackEntityFromInferiors(String[] value)
	{
		beforeAttackEntityFromInferiors = value;
	}

	public void setOverrideAttackEntityFromSuperiors(String[] value)
	{
		overrideAttackEntityFromSuperiors = value;
	}

	public void setOverrideAttackEntityFromInferiors(String[] value)
	{
		overrideAttackEntityFromInferiors = value;
	}

	public void setAfterAttackEntityFromSuperiors(String[] value)
	{
		afterAttackEntityFromSuperiors = value;
	}

	public void setAfterAttackEntityFromInferiors(String[] value)
	{
		afterAttackEntityFromInferiors = value;
	}

	public String[] getBeforeAttackTargetEntityWithCurrentItemSuperiors()
	{
		return beforeAttackTargetEntityWithCurrentItemSuperiors;
	}

	public String[] getBeforeAttackTargetEntityWithCurrentItemInferiors()
	{
		return beforeAttackTargetEntityWithCurrentItemInferiors;
	}

	public String[] getOverrideAttackTargetEntityWithCurrentItemSuperiors()
	{
		return overrideAttackTargetEntityWithCurrentItemSuperiors;
	}

	public String[] getOverrideAttackTargetEntityWithCurrentItemInferiors()
	{
		return overrideAttackTargetEntityWithCurrentItemInferiors;
	}

	public String[] getAfterAttackTargetEntityWithCurrentItemSuperiors()
	{
		return afterAttackTargetEntityWithCurrentItemSuperiors;
	}

	public String[] getAfterAttackTargetEntityWithCurrentItemInferiors()
	{
		return afterAttackTargetEntityWithCurrentItemInferiors;
	}

	public void setBeforeAttackTargetEntityWithCurrentItemSuperiors(String[] value)
	{
		beforeAttackTargetEntityWithCurrentItemSuperiors = value;
	}

	public void setBeforeAttackTargetEntityWithCurrentItemInferiors(String[] value)
	{
		beforeAttackTargetEntityWithCurrentItemInferiors = value;
	}

	public void setOverrideAttackTargetEntityWithCurrentItemSuperiors(String[] value)
	{
		overrideAttackTargetEntityWithCurrentItemSuperiors = value;
	}

	public void setOverrideAttackTargetEntityWithCurrentItemInferiors(String[] value)
	{
		overrideAttackTargetEntityWithCurrentItemInferiors = value;
	}

	public void setAfterAttackTargetEntityWithCurrentItemSuperiors(String[] value)
	{
		afterAttackTargetEntityWithCurrentItemSuperiors = value;
	}

	public void setAfterAttackTargetEntityWithCurrentItemInferiors(String[] value)
	{
		afterAttackTargetEntityWithCurrentItemInferiors = value;
	}

	public String[] getBeforeCanBreatheUnderwaterSuperiors()
	{
		return beforeCanBreatheUnderwaterSuperiors;
	}

	public String[] getBeforeCanBreatheUnderwaterInferiors()
	{
		return beforeCanBreatheUnderwaterInferiors;
	}

	public String[] getOverrideCanBreatheUnderwaterSuperiors()
	{
		return overrideCanBreatheUnderwaterSuperiors;
	}

	public String[] getOverrideCanBreatheUnderwaterInferiors()
	{
		return overrideCanBreatheUnderwaterInferiors;
	}

	public String[] getAfterCanBreatheUnderwaterSuperiors()
	{
		return afterCanBreatheUnderwaterSuperiors;
	}

	public String[] getAfterCanBreatheUnderwaterInferiors()
	{
		return afterCanBreatheUnderwaterInferiors;
	}

	public void setBeforeCanBreatheUnderwaterSuperiors(String[] value)
	{
		beforeCanBreatheUnderwaterSuperiors = value;
	}

	public void setBeforeCanBreatheUnderwaterInferiors(String[] value)
	{
		beforeCanBreatheUnderwaterInferiors = value;
	}

	public void setOverrideCanBreatheUnderwaterSuperiors(String[] value)
	{
		overrideCanBreatheUnderwaterSuperiors = value;
	}

	public void setOverrideCanBreatheUnderwaterInferiors(String[] value)
	{
		overrideCanBreatheUnderwaterInferiors = value;
	}

	public void setAfterCanBreatheUnderwaterSuperiors(String[] value)
	{
		afterCanBreatheUnderwaterSuperiors = value;
	}

	public void setAfterCanBreatheUnderwaterInferiors(String[] value)
	{
		afterCanBreatheUnderwaterInferiors = value;
	}

	public String[] getBeforeCanHarvestBlockSuperiors()
	{
		return beforeCanHarvestBlockSuperiors;
	}

	public String[] getBeforeCanHarvestBlockInferiors()
	{
		return beforeCanHarvestBlockInferiors;
	}

	public String[] getOverrideCanHarvestBlockSuperiors()
	{
		return overrideCanHarvestBlockSuperiors;
	}

	public String[] getOverrideCanHarvestBlockInferiors()
	{
		return overrideCanHarvestBlockInferiors;
	}

	public String[] getAfterCanHarvestBlockSuperiors()
	{
		return afterCanHarvestBlockSuperiors;
	}

	public String[] getAfterCanHarvestBlockInferiors()
	{
		return afterCanHarvestBlockInferiors;
	}

	public void setBeforeCanHarvestBlockSuperiors(String[] value)
	{
		beforeCanHarvestBlockSuperiors = value;
	}

	public void setBeforeCanHarvestBlockInferiors(String[] value)
	{
		beforeCanHarvestBlockInferiors = value;
	}

	public void setOverrideCanHarvestBlockSuperiors(String[] value)
	{
		overrideCanHarvestBlockSuperiors = value;
	}

	public void setOverrideCanHarvestBlockInferiors(String[] value)
	{
		overrideCanHarvestBlockInferiors = value;
	}

	public void setAfterCanHarvestBlockSuperiors(String[] value)
	{
		afterCanHarvestBlockSuperiors = value;
	}

	public void setAfterCanHarvestBlockInferiors(String[] value)
	{
		afterCanHarvestBlockInferiors = value;
	}

	public String[] getBeforeCanPlayerEditSuperiors()
	{
		return beforeCanPlayerEditSuperiors;
	}

	public String[] getBeforeCanPlayerEditInferiors()
	{
		return beforeCanPlayerEditInferiors;
	}

	public String[] getOverrideCanPlayerEditSuperiors()
	{
		return overrideCanPlayerEditSuperiors;
	}

	public String[] getOverrideCanPlayerEditInferiors()
	{
		return overrideCanPlayerEditInferiors;
	}

	public String[] getAfterCanPlayerEditSuperiors()
	{
		return afterCanPlayerEditSuperiors;
	}

	public String[] getAfterCanPlayerEditInferiors()
	{
		return afterCanPlayerEditInferiors;
	}

	public void setBeforeCanPlayerEditSuperiors(String[] value)
	{
		beforeCanPlayerEditSuperiors = value;
	}

	public void setBeforeCanPlayerEditInferiors(String[] value)
	{
		beforeCanPlayerEditInferiors = value;
	}

	public void setOverrideCanPlayerEditSuperiors(String[] value)
	{
		overrideCanPlayerEditSuperiors = value;
	}

	public void setOverrideCanPlayerEditInferiors(String[] value)
	{
		overrideCanPlayerEditInferiors = value;
	}

	public void setAfterCanPlayerEditSuperiors(String[] value)
	{
		afterCanPlayerEditSuperiors = value;
	}

	public void setAfterCanPlayerEditInferiors(String[] value)
	{
		afterCanPlayerEditInferiors = value;
	}

	public String[] getBeforeCanTriggerWalkingSuperiors()
	{
		return beforeCanTriggerWalkingSuperiors;
	}

	public String[] getBeforeCanTriggerWalkingInferiors()
	{
		return beforeCanTriggerWalkingInferiors;
	}

	public String[] getOverrideCanTriggerWalkingSuperiors()
	{
		return overrideCanTriggerWalkingSuperiors;
	}

	public String[] getOverrideCanTriggerWalkingInferiors()
	{
		return overrideCanTriggerWalkingInferiors;
	}

	public String[] getAfterCanTriggerWalkingSuperiors()
	{
		return afterCanTriggerWalkingSuperiors;
	}

	public String[] getAfterCanTriggerWalkingInferiors()
	{
		return afterCanTriggerWalkingInferiors;
	}

	public void setBeforeCanTriggerWalkingSuperiors(String[] value)
	{
		beforeCanTriggerWalkingSuperiors = value;
	}

	public void setBeforeCanTriggerWalkingInferiors(String[] value)
	{
		beforeCanTriggerWalkingInferiors = value;
	}

	public void setOverrideCanTriggerWalkingSuperiors(String[] value)
	{
		overrideCanTriggerWalkingSuperiors = value;
	}

	public void setOverrideCanTriggerWalkingInferiors(String[] value)
	{
		overrideCanTriggerWalkingInferiors = value;
	}

	public void setAfterCanTriggerWalkingSuperiors(String[] value)
	{
		afterCanTriggerWalkingSuperiors = value;
	}

	public void setAfterCanTriggerWalkingInferiors(String[] value)
	{
		afterCanTriggerWalkingInferiors = value;
	}

	public String[] getBeforeClonePlayerSuperiors()
	{
		return beforeClonePlayerSuperiors;
	}

	public String[] getBeforeClonePlayerInferiors()
	{
		return beforeClonePlayerInferiors;
	}

	public String[] getOverrideClonePlayerSuperiors()
	{
		return overrideClonePlayerSuperiors;
	}

	public String[] getOverrideClonePlayerInferiors()
	{
		return overrideClonePlayerInferiors;
	}

	public String[] getAfterClonePlayerSuperiors()
	{
		return afterClonePlayerSuperiors;
	}

	public String[] getAfterClonePlayerInferiors()
	{
		return afterClonePlayerInferiors;
	}

	public void setBeforeClonePlayerSuperiors(String[] value)
	{
		beforeClonePlayerSuperiors = value;
	}

	public void setBeforeClonePlayerInferiors(String[] value)
	{
		beforeClonePlayerInferiors = value;
	}

	public void setOverrideClonePlayerSuperiors(String[] value)
	{
		overrideClonePlayerSuperiors = value;
	}

	public void setOverrideClonePlayerInferiors(String[] value)
	{
		overrideClonePlayerInferiors = value;
	}

	public void setAfterClonePlayerSuperiors(String[] value)
	{
		afterClonePlayerSuperiors = value;
	}

	public void setAfterClonePlayerInferiors(String[] value)
	{
		afterClonePlayerInferiors = value;
	}

	public String[] getBeforeDamageEntitySuperiors()
	{
		return beforeDamageEntitySuperiors;
	}

	public String[] getBeforeDamageEntityInferiors()
	{
		return beforeDamageEntityInferiors;
	}

	public String[] getOverrideDamageEntitySuperiors()
	{
		return overrideDamageEntitySuperiors;
	}

	public String[] getOverrideDamageEntityInferiors()
	{
		return overrideDamageEntityInferiors;
	}

	public String[] getAfterDamageEntitySuperiors()
	{
		return afterDamageEntitySuperiors;
	}

	public String[] getAfterDamageEntityInferiors()
	{
		return afterDamageEntityInferiors;
	}

	public void setBeforeDamageEntitySuperiors(String[] value)
	{
		beforeDamageEntitySuperiors = value;
	}

	public void setBeforeDamageEntityInferiors(String[] value)
	{
		beforeDamageEntityInferiors = value;
	}

	public void setOverrideDamageEntitySuperiors(String[] value)
	{
		overrideDamageEntitySuperiors = value;
	}

	public void setOverrideDamageEntityInferiors(String[] value)
	{
		overrideDamageEntityInferiors = value;
	}

	public void setAfterDamageEntitySuperiors(String[] value)
	{
		afterDamageEntitySuperiors = value;
	}

	public void setAfterDamageEntityInferiors(String[] value)
	{
		afterDamageEntityInferiors = value;
	}

	public String[] getBeforeDisplayGUIChestSuperiors()
	{
		return beforeDisplayGUIChestSuperiors;
	}

	public String[] getBeforeDisplayGUIChestInferiors()
	{
		return beforeDisplayGUIChestInferiors;
	}

	public String[] getOverrideDisplayGUIChestSuperiors()
	{
		return overrideDisplayGUIChestSuperiors;
	}

	public String[] getOverrideDisplayGUIChestInferiors()
	{
		return overrideDisplayGUIChestInferiors;
	}

	public String[] getAfterDisplayGUIChestSuperiors()
	{
		return afterDisplayGUIChestSuperiors;
	}

	public String[] getAfterDisplayGUIChestInferiors()
	{
		return afterDisplayGUIChestInferiors;
	}

	public void setBeforeDisplayGUIChestSuperiors(String[] value)
	{
		beforeDisplayGUIChestSuperiors = value;
	}

	public void setBeforeDisplayGUIChestInferiors(String[] value)
	{
		beforeDisplayGUIChestInferiors = value;
	}

	public void setOverrideDisplayGUIChestSuperiors(String[] value)
	{
		overrideDisplayGUIChestSuperiors = value;
	}

	public void setOverrideDisplayGUIChestInferiors(String[] value)
	{
		overrideDisplayGUIChestInferiors = value;
	}

	public void setAfterDisplayGUIChestSuperiors(String[] value)
	{
		afterDisplayGUIChestSuperiors = value;
	}

	public void setAfterDisplayGUIChestInferiors(String[] value)
	{
		afterDisplayGUIChestInferiors = value;
	}

	public String[] getBeforeDisplayGUIDispenserSuperiors()
	{
		return beforeDisplayGUIDispenserSuperiors;
	}

	public String[] getBeforeDisplayGUIDispenserInferiors()
	{
		return beforeDisplayGUIDispenserInferiors;
	}

	public String[] getOverrideDisplayGUIDispenserSuperiors()
	{
		return overrideDisplayGUIDispenserSuperiors;
	}

	public String[] getOverrideDisplayGUIDispenserInferiors()
	{
		return overrideDisplayGUIDispenserInferiors;
	}

	public String[] getAfterDisplayGUIDispenserSuperiors()
	{
		return afterDisplayGUIDispenserSuperiors;
	}

	public String[] getAfterDisplayGUIDispenserInferiors()
	{
		return afterDisplayGUIDispenserInferiors;
	}

	public void setBeforeDisplayGUIDispenserSuperiors(String[] value)
	{
		beforeDisplayGUIDispenserSuperiors = value;
	}

	public void setBeforeDisplayGUIDispenserInferiors(String[] value)
	{
		beforeDisplayGUIDispenserInferiors = value;
	}

	public void setOverrideDisplayGUIDispenserSuperiors(String[] value)
	{
		overrideDisplayGUIDispenserSuperiors = value;
	}

	public void setOverrideDisplayGUIDispenserInferiors(String[] value)
	{
		overrideDisplayGUIDispenserInferiors = value;
	}

	public void setAfterDisplayGUIDispenserSuperiors(String[] value)
	{
		afterDisplayGUIDispenserSuperiors = value;
	}

	public void setAfterDisplayGUIDispenserInferiors(String[] value)
	{
		afterDisplayGUIDispenserInferiors = value;
	}

	public String[] getBeforeDisplayGUIFurnaceSuperiors()
	{
		return beforeDisplayGUIFurnaceSuperiors;
	}

	public String[] getBeforeDisplayGUIFurnaceInferiors()
	{
		return beforeDisplayGUIFurnaceInferiors;
	}

	public String[] getOverrideDisplayGUIFurnaceSuperiors()
	{
		return overrideDisplayGUIFurnaceSuperiors;
	}

	public String[] getOverrideDisplayGUIFurnaceInferiors()
	{
		return overrideDisplayGUIFurnaceInferiors;
	}

	public String[] getAfterDisplayGUIFurnaceSuperiors()
	{
		return afterDisplayGUIFurnaceSuperiors;
	}

	public String[] getAfterDisplayGUIFurnaceInferiors()
	{
		return afterDisplayGUIFurnaceInferiors;
	}

	public void setBeforeDisplayGUIFurnaceSuperiors(String[] value)
	{
		beforeDisplayGUIFurnaceSuperiors = value;
	}

	public void setBeforeDisplayGUIFurnaceInferiors(String[] value)
	{
		beforeDisplayGUIFurnaceInferiors = value;
	}

	public void setOverrideDisplayGUIFurnaceSuperiors(String[] value)
	{
		overrideDisplayGUIFurnaceSuperiors = value;
	}

	public void setOverrideDisplayGUIFurnaceInferiors(String[] value)
	{
		overrideDisplayGUIFurnaceInferiors = value;
	}

	public void setAfterDisplayGUIFurnaceSuperiors(String[] value)
	{
		afterDisplayGUIFurnaceSuperiors = value;
	}

	public void setAfterDisplayGUIFurnaceInferiors(String[] value)
	{
		afterDisplayGUIFurnaceInferiors = value;
	}

	public String[] getBeforeDisplayGUIWorkbenchSuperiors()
	{
		return beforeDisplayGUIWorkbenchSuperiors;
	}

	public String[] getBeforeDisplayGUIWorkbenchInferiors()
	{
		return beforeDisplayGUIWorkbenchInferiors;
	}

	public String[] getOverrideDisplayGUIWorkbenchSuperiors()
	{
		return overrideDisplayGUIWorkbenchSuperiors;
	}

	public String[] getOverrideDisplayGUIWorkbenchInferiors()
	{
		return overrideDisplayGUIWorkbenchInferiors;
	}

	public String[] getAfterDisplayGUIWorkbenchSuperiors()
	{
		return afterDisplayGUIWorkbenchSuperiors;
	}

	public String[] getAfterDisplayGUIWorkbenchInferiors()
	{
		return afterDisplayGUIWorkbenchInferiors;
	}

	public void setBeforeDisplayGUIWorkbenchSuperiors(String[] value)
	{
		beforeDisplayGUIWorkbenchSuperiors = value;
	}

	public void setBeforeDisplayGUIWorkbenchInferiors(String[] value)
	{
		beforeDisplayGUIWorkbenchInferiors = value;
	}

	public void setOverrideDisplayGUIWorkbenchSuperiors(String[] value)
	{
		overrideDisplayGUIWorkbenchSuperiors = value;
	}

	public void setOverrideDisplayGUIWorkbenchInferiors(String[] value)
	{
		overrideDisplayGUIWorkbenchInferiors = value;
	}

	public void setAfterDisplayGUIWorkbenchSuperiors(String[] value)
	{
		afterDisplayGUIWorkbenchSuperiors = value;
	}

	public void setAfterDisplayGUIWorkbenchInferiors(String[] value)
	{
		afterDisplayGUIWorkbenchInferiors = value;
	}

	public String[] getBeforeDropOneItemSuperiors()
	{
		return beforeDropOneItemSuperiors;
	}

	public String[] getBeforeDropOneItemInferiors()
	{
		return beforeDropOneItemInferiors;
	}

	public String[] getOverrideDropOneItemSuperiors()
	{
		return overrideDropOneItemSuperiors;
	}

	public String[] getOverrideDropOneItemInferiors()
	{
		return overrideDropOneItemInferiors;
	}

	public String[] getAfterDropOneItemSuperiors()
	{
		return afterDropOneItemSuperiors;
	}

	public String[] getAfterDropOneItemInferiors()
	{
		return afterDropOneItemInferiors;
	}

	public void setBeforeDropOneItemSuperiors(String[] value)
	{
		beforeDropOneItemSuperiors = value;
	}

	public void setBeforeDropOneItemInferiors(String[] value)
	{
		beforeDropOneItemInferiors = value;
	}

	public void setOverrideDropOneItemSuperiors(String[] value)
	{
		overrideDropOneItemSuperiors = value;
	}

	public void setOverrideDropOneItemInferiors(String[] value)
	{
		overrideDropOneItemInferiors = value;
	}

	public void setAfterDropOneItemSuperiors(String[] value)
	{
		afterDropOneItemSuperiors = value;
	}

	public void setAfterDropOneItemInferiors(String[] value)
	{
		afterDropOneItemInferiors = value;
	}

	public String[] getBeforeDropPlayerItemSuperiors()
	{
		return beforeDropPlayerItemSuperiors;
	}

	public String[] getBeforeDropPlayerItemInferiors()
	{
		return beforeDropPlayerItemInferiors;
	}

	public String[] getOverrideDropPlayerItemSuperiors()
	{
		return overrideDropPlayerItemSuperiors;
	}

	public String[] getOverrideDropPlayerItemInferiors()
	{
		return overrideDropPlayerItemInferiors;
	}

	public String[] getAfterDropPlayerItemSuperiors()
	{
		return afterDropPlayerItemSuperiors;
	}

	public String[] getAfterDropPlayerItemInferiors()
	{
		return afterDropPlayerItemInferiors;
	}

	public void setBeforeDropPlayerItemSuperiors(String[] value)
	{
		beforeDropPlayerItemSuperiors = value;
	}

	public void setBeforeDropPlayerItemInferiors(String[] value)
	{
		beforeDropPlayerItemInferiors = value;
	}

	public void setOverrideDropPlayerItemSuperiors(String[] value)
	{
		overrideDropPlayerItemSuperiors = value;
	}

	public void setOverrideDropPlayerItemInferiors(String[] value)
	{
		overrideDropPlayerItemInferiors = value;
	}

	public void setAfterDropPlayerItemSuperiors(String[] value)
	{
		afterDropPlayerItemSuperiors = value;
	}

	public void setAfterDropPlayerItemInferiors(String[] value)
	{
		afterDropPlayerItemInferiors = value;
	}

	public String[] getBeforeFallSuperiors()
	{
		return beforeFallSuperiors;
	}

	public String[] getBeforeFallInferiors()
	{
		return beforeFallInferiors;
	}

	public String[] getOverrideFallSuperiors()
	{
		return overrideFallSuperiors;
	}

	public String[] getOverrideFallInferiors()
	{
		return overrideFallInferiors;
	}

	public String[] getAfterFallSuperiors()
	{
		return afterFallSuperiors;
	}

	public String[] getAfterFallInferiors()
	{
		return afterFallInferiors;
	}

	public void setBeforeFallSuperiors(String[] value)
	{
		beforeFallSuperiors = value;
	}

	public void setBeforeFallInferiors(String[] value)
	{
		beforeFallInferiors = value;
	}

	public void setOverrideFallSuperiors(String[] value)
	{
		overrideFallSuperiors = value;
	}

	public void setOverrideFallInferiors(String[] value)
	{
		overrideFallInferiors = value;
	}

	public void setAfterFallSuperiors(String[] value)
	{
		afterFallSuperiors = value;
	}

	public void setAfterFallInferiors(String[] value)
	{
		afterFallInferiors = value;
	}

	public String[] getBeforeGetAIMoveSpeedSuperiors()
	{
		return beforeGetAIMoveSpeedSuperiors;
	}

	public String[] getBeforeGetAIMoveSpeedInferiors()
	{
		return beforeGetAIMoveSpeedInferiors;
	}

	public String[] getOverrideGetAIMoveSpeedSuperiors()
	{
		return overrideGetAIMoveSpeedSuperiors;
	}

	public String[] getOverrideGetAIMoveSpeedInferiors()
	{
		return overrideGetAIMoveSpeedInferiors;
	}

	public String[] getAfterGetAIMoveSpeedSuperiors()
	{
		return afterGetAIMoveSpeedSuperiors;
	}

	public String[] getAfterGetAIMoveSpeedInferiors()
	{
		return afterGetAIMoveSpeedInferiors;
	}

	public void setBeforeGetAIMoveSpeedSuperiors(String[] value)
	{
		beforeGetAIMoveSpeedSuperiors = value;
	}

	public void setBeforeGetAIMoveSpeedInferiors(String[] value)
	{
		beforeGetAIMoveSpeedInferiors = value;
	}

	public void setOverrideGetAIMoveSpeedSuperiors(String[] value)
	{
		overrideGetAIMoveSpeedSuperiors = value;
	}

	public void setOverrideGetAIMoveSpeedInferiors(String[] value)
	{
		overrideGetAIMoveSpeedInferiors = value;
	}

	public void setAfterGetAIMoveSpeedSuperiors(String[] value)
	{
		afterGetAIMoveSpeedSuperiors = value;
	}

	public void setAfterGetAIMoveSpeedInferiors(String[] value)
	{
		afterGetAIMoveSpeedInferiors = value;
	}

	public String[] getBeforeGetCurrentPlayerStrVsBlockSuperiors()
	{
		return beforeGetCurrentPlayerStrVsBlockSuperiors;
	}

	public String[] getBeforeGetCurrentPlayerStrVsBlockInferiors()
	{
		return beforeGetCurrentPlayerStrVsBlockInferiors;
	}

	public String[] getOverrideGetCurrentPlayerStrVsBlockSuperiors()
	{
		return overrideGetCurrentPlayerStrVsBlockSuperiors;
	}

	public String[] getOverrideGetCurrentPlayerStrVsBlockInferiors()
	{
		return overrideGetCurrentPlayerStrVsBlockInferiors;
	}

	public String[] getAfterGetCurrentPlayerStrVsBlockSuperiors()
	{
		return afterGetCurrentPlayerStrVsBlockSuperiors;
	}

	public String[] getAfterGetCurrentPlayerStrVsBlockInferiors()
	{
		return afterGetCurrentPlayerStrVsBlockInferiors;
	}

	public void setBeforeGetCurrentPlayerStrVsBlockSuperiors(String[] value)
	{
		beforeGetCurrentPlayerStrVsBlockSuperiors = value;
	}

	public void setBeforeGetCurrentPlayerStrVsBlockInferiors(String[] value)
	{
		beforeGetCurrentPlayerStrVsBlockInferiors = value;
	}

	public void setOverrideGetCurrentPlayerStrVsBlockSuperiors(String[] value)
	{
		overrideGetCurrentPlayerStrVsBlockSuperiors = value;
	}

	public void setOverrideGetCurrentPlayerStrVsBlockInferiors(String[] value)
	{
		overrideGetCurrentPlayerStrVsBlockInferiors = value;
	}

	public void setAfterGetCurrentPlayerStrVsBlockSuperiors(String[] value)
	{
		afterGetCurrentPlayerStrVsBlockSuperiors = value;
	}

	public void setAfterGetCurrentPlayerStrVsBlockInferiors(String[] value)
	{
		afterGetCurrentPlayerStrVsBlockInferiors = value;
	}

	public String[] getBeforeGetCurrentPlayerStrVsBlockForgeSuperiors()
	{
		return beforeGetCurrentPlayerStrVsBlockForgeSuperiors;
	}

	public String[] getBeforeGetCurrentPlayerStrVsBlockForgeInferiors()
	{
		return beforeGetCurrentPlayerStrVsBlockForgeInferiors;
	}

	public String[] getOverrideGetCurrentPlayerStrVsBlockForgeSuperiors()
	{
		return overrideGetCurrentPlayerStrVsBlockForgeSuperiors;
	}

	public String[] getOverrideGetCurrentPlayerStrVsBlockForgeInferiors()
	{
		return overrideGetCurrentPlayerStrVsBlockForgeInferiors;
	}

	public String[] getAfterGetCurrentPlayerStrVsBlockForgeSuperiors()
	{
		return afterGetCurrentPlayerStrVsBlockForgeSuperiors;
	}

	public String[] getAfterGetCurrentPlayerStrVsBlockForgeInferiors()
	{
		return afterGetCurrentPlayerStrVsBlockForgeInferiors;
	}

	public void setBeforeGetCurrentPlayerStrVsBlockForgeSuperiors(String[] value)
	{
		beforeGetCurrentPlayerStrVsBlockForgeSuperiors = value;
	}

	public void setBeforeGetCurrentPlayerStrVsBlockForgeInferiors(String[] value)
	{
		beforeGetCurrentPlayerStrVsBlockForgeInferiors = value;
	}

	public void setOverrideGetCurrentPlayerStrVsBlockForgeSuperiors(String[] value)
	{
		overrideGetCurrentPlayerStrVsBlockForgeSuperiors = value;
	}

	public void setOverrideGetCurrentPlayerStrVsBlockForgeInferiors(String[] value)
	{
		overrideGetCurrentPlayerStrVsBlockForgeInferiors = value;
	}

	public void setAfterGetCurrentPlayerStrVsBlockForgeSuperiors(String[] value)
	{
		afterGetCurrentPlayerStrVsBlockForgeSuperiors = value;
	}

	public void setAfterGetCurrentPlayerStrVsBlockForgeInferiors(String[] value)
	{
		afterGetCurrentPlayerStrVsBlockForgeInferiors = value;
	}

	public String[] getBeforeGetDistanceSqSuperiors()
	{
		return beforeGetDistanceSqSuperiors;
	}

	public String[] getBeforeGetDistanceSqInferiors()
	{
		return beforeGetDistanceSqInferiors;
	}

	public String[] getOverrideGetDistanceSqSuperiors()
	{
		return overrideGetDistanceSqSuperiors;
	}

	public String[] getOverrideGetDistanceSqInferiors()
	{
		return overrideGetDistanceSqInferiors;
	}

	public String[] getAfterGetDistanceSqSuperiors()
	{
		return afterGetDistanceSqSuperiors;
	}

	public String[] getAfterGetDistanceSqInferiors()
	{
		return afterGetDistanceSqInferiors;
	}

	public void setBeforeGetDistanceSqSuperiors(String[] value)
	{
		beforeGetDistanceSqSuperiors = value;
	}

	public void setBeforeGetDistanceSqInferiors(String[] value)
	{
		beforeGetDistanceSqInferiors = value;
	}

	public void setOverrideGetDistanceSqSuperiors(String[] value)
	{
		overrideGetDistanceSqSuperiors = value;
	}

	public void setOverrideGetDistanceSqInferiors(String[] value)
	{
		overrideGetDistanceSqInferiors = value;
	}

	public void setAfterGetDistanceSqSuperiors(String[] value)
	{
		afterGetDistanceSqSuperiors = value;
	}

	public void setAfterGetDistanceSqInferiors(String[] value)
	{
		afterGetDistanceSqInferiors = value;
	}

	public String[] getBeforeGetBrightnessSuperiors()
	{
		return beforeGetBrightnessSuperiors;
	}

	public String[] getBeforeGetBrightnessInferiors()
	{
		return beforeGetBrightnessInferiors;
	}

	public String[] getOverrideGetBrightnessSuperiors()
	{
		return overrideGetBrightnessSuperiors;
	}

	public String[] getOverrideGetBrightnessInferiors()
	{
		return overrideGetBrightnessInferiors;
	}

	public String[] getAfterGetBrightnessSuperiors()
	{
		return afterGetBrightnessSuperiors;
	}

	public String[] getAfterGetBrightnessInferiors()
	{
		return afterGetBrightnessInferiors;
	}

	public void setBeforeGetBrightnessSuperiors(String[] value)
	{
		beforeGetBrightnessSuperiors = value;
	}

	public void setBeforeGetBrightnessInferiors(String[] value)
	{
		beforeGetBrightnessInferiors = value;
	}

	public void setOverrideGetBrightnessSuperiors(String[] value)
	{
		overrideGetBrightnessSuperiors = value;
	}

	public void setOverrideGetBrightnessInferiors(String[] value)
	{
		overrideGetBrightnessInferiors = value;
	}

	public void setAfterGetBrightnessSuperiors(String[] value)
	{
		afterGetBrightnessSuperiors = value;
	}

	public void setAfterGetBrightnessInferiors(String[] value)
	{
		afterGetBrightnessInferiors = value;
	}

	public String[] getBeforeGetEyeHeightSuperiors()
	{
		return beforeGetEyeHeightSuperiors;
	}

	public String[] getBeforeGetEyeHeightInferiors()
	{
		return beforeGetEyeHeightInferiors;
	}

	public String[] getOverrideGetEyeHeightSuperiors()
	{
		return overrideGetEyeHeightSuperiors;
	}

	public String[] getOverrideGetEyeHeightInferiors()
	{
		return overrideGetEyeHeightInferiors;
	}

	public String[] getAfterGetEyeHeightSuperiors()
	{
		return afterGetEyeHeightSuperiors;
	}

	public String[] getAfterGetEyeHeightInferiors()
	{
		return afterGetEyeHeightInferiors;
	}

	public void setBeforeGetEyeHeightSuperiors(String[] value)
	{
		beforeGetEyeHeightSuperiors = value;
	}

	public void setBeforeGetEyeHeightInferiors(String[] value)
	{
		beforeGetEyeHeightInferiors = value;
	}

	public void setOverrideGetEyeHeightSuperiors(String[] value)
	{
		overrideGetEyeHeightSuperiors = value;
	}

	public void setOverrideGetEyeHeightInferiors(String[] value)
	{
		overrideGetEyeHeightInferiors = value;
	}

	public void setAfterGetEyeHeightSuperiors(String[] value)
	{
		afterGetEyeHeightSuperiors = value;
	}

	public void setAfterGetEyeHeightInferiors(String[] value)
	{
		afterGetEyeHeightInferiors = value;
	}

	public String[] getBeforeHealSuperiors()
	{
		return beforeHealSuperiors;
	}

	public String[] getBeforeHealInferiors()
	{
		return beforeHealInferiors;
	}

	public String[] getOverrideHealSuperiors()
	{
		return overrideHealSuperiors;
	}

	public String[] getOverrideHealInferiors()
	{
		return overrideHealInferiors;
	}

	public String[] getAfterHealSuperiors()
	{
		return afterHealSuperiors;
	}

	public String[] getAfterHealInferiors()
	{
		return afterHealInferiors;
	}

	public void setBeforeHealSuperiors(String[] value)
	{
		beforeHealSuperiors = value;
	}

	public void setBeforeHealInferiors(String[] value)
	{
		beforeHealInferiors = value;
	}

	public void setOverrideHealSuperiors(String[] value)
	{
		overrideHealSuperiors = value;
	}

	public void setOverrideHealInferiors(String[] value)
	{
		overrideHealInferiors = value;
	}

	public void setAfterHealSuperiors(String[] value)
	{
		afterHealSuperiors = value;
	}

	public void setAfterHealInferiors(String[] value)
	{
		afterHealInferiors = value;
	}

	public String[] getBeforeIsEntityInsideOpaqueBlockSuperiors()
	{
		return beforeIsEntityInsideOpaqueBlockSuperiors;
	}

	public String[] getBeforeIsEntityInsideOpaqueBlockInferiors()
	{
		return beforeIsEntityInsideOpaqueBlockInferiors;
	}

	public String[] getOverrideIsEntityInsideOpaqueBlockSuperiors()
	{
		return overrideIsEntityInsideOpaqueBlockSuperiors;
	}

	public String[] getOverrideIsEntityInsideOpaqueBlockInferiors()
	{
		return overrideIsEntityInsideOpaqueBlockInferiors;
	}

	public String[] getAfterIsEntityInsideOpaqueBlockSuperiors()
	{
		return afterIsEntityInsideOpaqueBlockSuperiors;
	}

	public String[] getAfterIsEntityInsideOpaqueBlockInferiors()
	{
		return afterIsEntityInsideOpaqueBlockInferiors;
	}

	public void setBeforeIsEntityInsideOpaqueBlockSuperiors(String[] value)
	{
		beforeIsEntityInsideOpaqueBlockSuperiors = value;
	}

	public void setBeforeIsEntityInsideOpaqueBlockInferiors(String[] value)
	{
		beforeIsEntityInsideOpaqueBlockInferiors = value;
	}

	public void setOverrideIsEntityInsideOpaqueBlockSuperiors(String[] value)
	{
		overrideIsEntityInsideOpaqueBlockSuperiors = value;
	}

	public void setOverrideIsEntityInsideOpaqueBlockInferiors(String[] value)
	{
		overrideIsEntityInsideOpaqueBlockInferiors = value;
	}

	public void setAfterIsEntityInsideOpaqueBlockSuperiors(String[] value)
	{
		afterIsEntityInsideOpaqueBlockSuperiors = value;
	}

	public void setAfterIsEntityInsideOpaqueBlockInferiors(String[] value)
	{
		afterIsEntityInsideOpaqueBlockInferiors = value;
	}

	public String[] getBeforeIsInWaterSuperiors()
	{
		return beforeIsInWaterSuperiors;
	}

	public String[] getBeforeIsInWaterInferiors()
	{
		return beforeIsInWaterInferiors;
	}

	public String[] getOverrideIsInWaterSuperiors()
	{
		return overrideIsInWaterSuperiors;
	}

	public String[] getOverrideIsInWaterInferiors()
	{
		return overrideIsInWaterInferiors;
	}

	public String[] getAfterIsInWaterSuperiors()
	{
		return afterIsInWaterSuperiors;
	}

	public String[] getAfterIsInWaterInferiors()
	{
		return afterIsInWaterInferiors;
	}

	public void setBeforeIsInWaterSuperiors(String[] value)
	{
		beforeIsInWaterSuperiors = value;
	}

	public void setBeforeIsInWaterInferiors(String[] value)
	{
		beforeIsInWaterInferiors = value;
	}

	public void setOverrideIsInWaterSuperiors(String[] value)
	{
		overrideIsInWaterSuperiors = value;
	}

	public void setOverrideIsInWaterInferiors(String[] value)
	{
		overrideIsInWaterInferiors = value;
	}

	public void setAfterIsInWaterSuperiors(String[] value)
	{
		afterIsInWaterSuperiors = value;
	}

	public void setAfterIsInWaterInferiors(String[] value)
	{
		afterIsInWaterInferiors = value;
	}

	public String[] getBeforeIsInsideOfMaterialSuperiors()
	{
		return beforeIsInsideOfMaterialSuperiors;
	}

	public String[] getBeforeIsInsideOfMaterialInferiors()
	{
		return beforeIsInsideOfMaterialInferiors;
	}

	public String[] getOverrideIsInsideOfMaterialSuperiors()
	{
		return overrideIsInsideOfMaterialSuperiors;
	}

	public String[] getOverrideIsInsideOfMaterialInferiors()
	{
		return overrideIsInsideOfMaterialInferiors;
	}

	public String[] getAfterIsInsideOfMaterialSuperiors()
	{
		return afterIsInsideOfMaterialSuperiors;
	}

	public String[] getAfterIsInsideOfMaterialInferiors()
	{
		return afterIsInsideOfMaterialInferiors;
	}

	public void setBeforeIsInsideOfMaterialSuperiors(String[] value)
	{
		beforeIsInsideOfMaterialSuperiors = value;
	}

	public void setBeforeIsInsideOfMaterialInferiors(String[] value)
	{
		beforeIsInsideOfMaterialInferiors = value;
	}

	public void setOverrideIsInsideOfMaterialSuperiors(String[] value)
	{
		overrideIsInsideOfMaterialSuperiors = value;
	}

	public void setOverrideIsInsideOfMaterialInferiors(String[] value)
	{
		overrideIsInsideOfMaterialInferiors = value;
	}

	public void setAfterIsInsideOfMaterialSuperiors(String[] value)
	{
		afterIsInsideOfMaterialSuperiors = value;
	}

	public void setAfterIsInsideOfMaterialInferiors(String[] value)
	{
		afterIsInsideOfMaterialInferiors = value;
	}

	public String[] getBeforeIsOnLadderSuperiors()
	{
		return beforeIsOnLadderSuperiors;
	}

	public String[] getBeforeIsOnLadderInferiors()
	{
		return beforeIsOnLadderInferiors;
	}

	public String[] getOverrideIsOnLadderSuperiors()
	{
		return overrideIsOnLadderSuperiors;
	}

	public String[] getOverrideIsOnLadderInferiors()
	{
		return overrideIsOnLadderInferiors;
	}

	public String[] getAfterIsOnLadderSuperiors()
	{
		return afterIsOnLadderSuperiors;
	}

	public String[] getAfterIsOnLadderInferiors()
	{
		return afterIsOnLadderInferiors;
	}

	public void setBeforeIsOnLadderSuperiors(String[] value)
	{
		beforeIsOnLadderSuperiors = value;
	}

	public void setBeforeIsOnLadderInferiors(String[] value)
	{
		beforeIsOnLadderInferiors = value;
	}

	public void setOverrideIsOnLadderSuperiors(String[] value)
	{
		overrideIsOnLadderSuperiors = value;
	}

	public void setOverrideIsOnLadderInferiors(String[] value)
	{
		overrideIsOnLadderInferiors = value;
	}

	public void setAfterIsOnLadderSuperiors(String[] value)
	{
		afterIsOnLadderSuperiors = value;
	}

	public void setAfterIsOnLadderInferiors(String[] value)
	{
		afterIsOnLadderInferiors = value;
	}

	public String[] getBeforeIsPlayerSleepingSuperiors()
	{
		return beforeIsPlayerSleepingSuperiors;
	}

	public String[] getBeforeIsPlayerSleepingInferiors()
	{
		return beforeIsPlayerSleepingInferiors;
	}

	public String[] getOverrideIsPlayerSleepingSuperiors()
	{
		return overrideIsPlayerSleepingSuperiors;
	}

	public String[] getOverrideIsPlayerSleepingInferiors()
	{
		return overrideIsPlayerSleepingInferiors;
	}

	public String[] getAfterIsPlayerSleepingSuperiors()
	{
		return afterIsPlayerSleepingSuperiors;
	}

	public String[] getAfterIsPlayerSleepingInferiors()
	{
		return afterIsPlayerSleepingInferiors;
	}

	public void setBeforeIsPlayerSleepingSuperiors(String[] value)
	{
		beforeIsPlayerSleepingSuperiors = value;
	}

	public void setBeforeIsPlayerSleepingInferiors(String[] value)
	{
		beforeIsPlayerSleepingInferiors = value;
	}

	public void setOverrideIsPlayerSleepingSuperiors(String[] value)
	{
		overrideIsPlayerSleepingSuperiors = value;
	}

	public void setOverrideIsPlayerSleepingInferiors(String[] value)
	{
		overrideIsPlayerSleepingInferiors = value;
	}

	public void setAfterIsPlayerSleepingSuperiors(String[] value)
	{
		afterIsPlayerSleepingSuperiors = value;
	}

	public void setAfterIsPlayerSleepingInferiors(String[] value)
	{
		afterIsPlayerSleepingInferiors = value;
	}

	public String[] getBeforeIsSneakingSuperiors()
	{
		return beforeIsSneakingSuperiors;
	}

	public String[] getBeforeIsSneakingInferiors()
	{
		return beforeIsSneakingInferiors;
	}

	public String[] getOverrideIsSneakingSuperiors()
	{
		return overrideIsSneakingSuperiors;
	}

	public String[] getOverrideIsSneakingInferiors()
	{
		return overrideIsSneakingInferiors;
	}

	public String[] getAfterIsSneakingSuperiors()
	{
		return afterIsSneakingSuperiors;
	}

	public String[] getAfterIsSneakingInferiors()
	{
		return afterIsSneakingInferiors;
	}

	public void setBeforeIsSneakingSuperiors(String[] value)
	{
		beforeIsSneakingSuperiors = value;
	}

	public void setBeforeIsSneakingInferiors(String[] value)
	{
		beforeIsSneakingInferiors = value;
	}

	public void setOverrideIsSneakingSuperiors(String[] value)
	{
		overrideIsSneakingSuperiors = value;
	}

	public void setOverrideIsSneakingInferiors(String[] value)
	{
		overrideIsSneakingInferiors = value;
	}

	public void setAfterIsSneakingSuperiors(String[] value)
	{
		afterIsSneakingSuperiors = value;
	}

	public void setAfterIsSneakingInferiors(String[] value)
	{
		afterIsSneakingInferiors = value;
	}

	public String[] getBeforeJumpSuperiors()
	{
		return beforeJumpSuperiors;
	}

	public String[] getBeforeJumpInferiors()
	{
		return beforeJumpInferiors;
	}

	public String[] getOverrideJumpSuperiors()
	{
		return overrideJumpSuperiors;
	}

	public String[] getOverrideJumpInferiors()
	{
		return overrideJumpInferiors;
	}

	public String[] getAfterJumpSuperiors()
	{
		return afterJumpSuperiors;
	}

	public String[] getAfterJumpInferiors()
	{
		return afterJumpInferiors;
	}

	public void setBeforeJumpSuperiors(String[] value)
	{
		beforeJumpSuperiors = value;
	}

	public void setBeforeJumpInferiors(String[] value)
	{
		beforeJumpInferiors = value;
	}

	public void setOverrideJumpSuperiors(String[] value)
	{
		overrideJumpSuperiors = value;
	}

	public void setOverrideJumpInferiors(String[] value)
	{
		overrideJumpInferiors = value;
	}

	public void setAfterJumpSuperiors(String[] value)
	{
		afterJumpSuperiors = value;
	}

	public void setAfterJumpInferiors(String[] value)
	{
		afterJumpInferiors = value;
	}

	public String[] getBeforeKnockBackSuperiors()
	{
		return beforeKnockBackSuperiors;
	}

	public String[] getBeforeKnockBackInferiors()
	{
		return beforeKnockBackInferiors;
	}

	public String[] getOverrideKnockBackSuperiors()
	{
		return overrideKnockBackSuperiors;
	}

	public String[] getOverrideKnockBackInferiors()
	{
		return overrideKnockBackInferiors;
	}

	public String[] getAfterKnockBackSuperiors()
	{
		return afterKnockBackSuperiors;
	}

	public String[] getAfterKnockBackInferiors()
	{
		return afterKnockBackInferiors;
	}

	public void setBeforeKnockBackSuperiors(String[] value)
	{
		beforeKnockBackSuperiors = value;
	}

	public void setBeforeKnockBackInferiors(String[] value)
	{
		beforeKnockBackInferiors = value;
	}

	public void setOverrideKnockBackSuperiors(String[] value)
	{
		overrideKnockBackSuperiors = value;
	}

	public void setOverrideKnockBackInferiors(String[] value)
	{
		overrideKnockBackInferiors = value;
	}

	public void setAfterKnockBackSuperiors(String[] value)
	{
		afterKnockBackSuperiors = value;
	}

	public void setAfterKnockBackInferiors(String[] value)
	{
		afterKnockBackInferiors = value;
	}

	public String[] getBeforeMountEntitySuperiors()
	{
		return beforeMountEntitySuperiors;
	}

	public String[] getBeforeMountEntityInferiors()
	{
		return beforeMountEntityInferiors;
	}

	public String[] getOverrideMountEntitySuperiors()
	{
		return overrideMountEntitySuperiors;
	}

	public String[] getOverrideMountEntityInferiors()
	{
		return overrideMountEntityInferiors;
	}

	public String[] getAfterMountEntitySuperiors()
	{
		return afterMountEntitySuperiors;
	}

	public String[] getAfterMountEntityInferiors()
	{
		return afterMountEntityInferiors;
	}

	public void setBeforeMountEntitySuperiors(String[] value)
	{
		beforeMountEntitySuperiors = value;
	}

	public void setBeforeMountEntityInferiors(String[] value)
	{
		beforeMountEntityInferiors = value;
	}

	public void setOverrideMountEntitySuperiors(String[] value)
	{
		overrideMountEntitySuperiors = value;
	}

	public void setOverrideMountEntityInferiors(String[] value)
	{
		overrideMountEntityInferiors = value;
	}

	public void setAfterMountEntitySuperiors(String[] value)
	{
		afterMountEntitySuperiors = value;
	}

	public void setAfterMountEntityInferiors(String[] value)
	{
		afterMountEntityInferiors = value;
	}

	public String[] getBeforeMoveEntitySuperiors()
	{
		return beforeMoveEntitySuperiors;
	}

	public String[] getBeforeMoveEntityInferiors()
	{
		return beforeMoveEntityInferiors;
	}

	public String[] getOverrideMoveEntitySuperiors()
	{
		return overrideMoveEntitySuperiors;
	}

	public String[] getOverrideMoveEntityInferiors()
	{
		return overrideMoveEntityInferiors;
	}

	public String[] getAfterMoveEntitySuperiors()
	{
		return afterMoveEntitySuperiors;
	}

	public String[] getAfterMoveEntityInferiors()
	{
		return afterMoveEntityInferiors;
	}

	public void setBeforeMoveEntitySuperiors(String[] value)
	{
		beforeMoveEntitySuperiors = value;
	}

	public void setBeforeMoveEntityInferiors(String[] value)
	{
		beforeMoveEntityInferiors = value;
	}

	public void setOverrideMoveEntitySuperiors(String[] value)
	{
		overrideMoveEntitySuperiors = value;
	}

	public void setOverrideMoveEntityInferiors(String[] value)
	{
		overrideMoveEntityInferiors = value;
	}

	public void setAfterMoveEntitySuperiors(String[] value)
	{
		afterMoveEntitySuperiors = value;
	}

	public void setAfterMoveEntityInferiors(String[] value)
	{
		afterMoveEntityInferiors = value;
	}

	public String[] getBeforeMoveEntityWithHeadingSuperiors()
	{
		return beforeMoveEntityWithHeadingSuperiors;
	}

	public String[] getBeforeMoveEntityWithHeadingInferiors()
	{
		return beforeMoveEntityWithHeadingInferiors;
	}

	public String[] getOverrideMoveEntityWithHeadingSuperiors()
	{
		return overrideMoveEntityWithHeadingSuperiors;
	}

	public String[] getOverrideMoveEntityWithHeadingInferiors()
	{
		return overrideMoveEntityWithHeadingInferiors;
	}

	public String[] getAfterMoveEntityWithHeadingSuperiors()
	{
		return afterMoveEntityWithHeadingSuperiors;
	}

	public String[] getAfterMoveEntityWithHeadingInferiors()
	{
		return afterMoveEntityWithHeadingInferiors;
	}

	public void setBeforeMoveEntityWithHeadingSuperiors(String[] value)
	{
		beforeMoveEntityWithHeadingSuperiors = value;
	}

	public void setBeforeMoveEntityWithHeadingInferiors(String[] value)
	{
		beforeMoveEntityWithHeadingInferiors = value;
	}

	public void setOverrideMoveEntityWithHeadingSuperiors(String[] value)
	{
		overrideMoveEntityWithHeadingSuperiors = value;
	}

	public void setOverrideMoveEntityWithHeadingInferiors(String[] value)
	{
		overrideMoveEntityWithHeadingInferiors = value;
	}

	public void setAfterMoveEntityWithHeadingSuperiors(String[] value)
	{
		afterMoveEntityWithHeadingSuperiors = value;
	}

	public void setAfterMoveEntityWithHeadingInferiors(String[] value)
	{
		afterMoveEntityWithHeadingInferiors = value;
	}

	public String[] getBeforeMoveFlyingSuperiors()
	{
		return beforeMoveFlyingSuperiors;
	}

	public String[] getBeforeMoveFlyingInferiors()
	{
		return beforeMoveFlyingInferiors;
	}

	public String[] getOverrideMoveFlyingSuperiors()
	{
		return overrideMoveFlyingSuperiors;
	}

	public String[] getOverrideMoveFlyingInferiors()
	{
		return overrideMoveFlyingInferiors;
	}

	public String[] getAfterMoveFlyingSuperiors()
	{
		return afterMoveFlyingSuperiors;
	}

	public String[] getAfterMoveFlyingInferiors()
	{
		return afterMoveFlyingInferiors;
	}

	public void setBeforeMoveFlyingSuperiors(String[] value)
	{
		beforeMoveFlyingSuperiors = value;
	}

	public void setBeforeMoveFlyingInferiors(String[] value)
	{
		beforeMoveFlyingInferiors = value;
	}

	public void setOverrideMoveFlyingSuperiors(String[] value)
	{
		overrideMoveFlyingSuperiors = value;
	}

	public void setOverrideMoveFlyingInferiors(String[] value)
	{
		overrideMoveFlyingInferiors = value;
	}

	public void setAfterMoveFlyingSuperiors(String[] value)
	{
		afterMoveFlyingSuperiors = value;
	}

	public void setAfterMoveFlyingInferiors(String[] value)
	{
		afterMoveFlyingInferiors = value;
	}

	public String[] getBeforeOnDeathSuperiors()
	{
		return beforeOnDeathSuperiors;
	}

	public String[] getBeforeOnDeathInferiors()
	{
		return beforeOnDeathInferiors;
	}

	public String[] getOverrideOnDeathSuperiors()
	{
		return overrideOnDeathSuperiors;
	}

	public String[] getOverrideOnDeathInferiors()
	{
		return overrideOnDeathInferiors;
	}

	public String[] getAfterOnDeathSuperiors()
	{
		return afterOnDeathSuperiors;
	}

	public String[] getAfterOnDeathInferiors()
	{
		return afterOnDeathInferiors;
	}

	public void setBeforeOnDeathSuperiors(String[] value)
	{
		beforeOnDeathSuperiors = value;
	}

	public void setBeforeOnDeathInferiors(String[] value)
	{
		beforeOnDeathInferiors = value;
	}

	public void setOverrideOnDeathSuperiors(String[] value)
	{
		overrideOnDeathSuperiors = value;
	}

	public void setOverrideOnDeathInferiors(String[] value)
	{
		overrideOnDeathInferiors = value;
	}

	public void setAfterOnDeathSuperiors(String[] value)
	{
		afterOnDeathSuperiors = value;
	}

	public void setAfterOnDeathInferiors(String[] value)
	{
		afterOnDeathInferiors = value;
	}

	public String[] getBeforeOnLivingUpdateSuperiors()
	{
		return beforeOnLivingUpdateSuperiors;
	}

	public String[] getBeforeOnLivingUpdateInferiors()
	{
		return beforeOnLivingUpdateInferiors;
	}

	public String[] getOverrideOnLivingUpdateSuperiors()
	{
		return overrideOnLivingUpdateSuperiors;
	}

	public String[] getOverrideOnLivingUpdateInferiors()
	{
		return overrideOnLivingUpdateInferiors;
	}

	public String[] getAfterOnLivingUpdateSuperiors()
	{
		return afterOnLivingUpdateSuperiors;
	}

	public String[] getAfterOnLivingUpdateInferiors()
	{
		return afterOnLivingUpdateInferiors;
	}

	public void setBeforeOnLivingUpdateSuperiors(String[] value)
	{
		beforeOnLivingUpdateSuperiors = value;
	}

	public void setBeforeOnLivingUpdateInferiors(String[] value)
	{
		beforeOnLivingUpdateInferiors = value;
	}

	public void setOverrideOnLivingUpdateSuperiors(String[] value)
	{
		overrideOnLivingUpdateSuperiors = value;
	}

	public void setOverrideOnLivingUpdateInferiors(String[] value)
	{
		overrideOnLivingUpdateInferiors = value;
	}

	public void setAfterOnLivingUpdateSuperiors(String[] value)
	{
		afterOnLivingUpdateSuperiors = value;
	}

	public void setAfterOnLivingUpdateInferiors(String[] value)
	{
		afterOnLivingUpdateInferiors = value;
	}

	public String[] getBeforeOnKillEntitySuperiors()
	{
		return beforeOnKillEntitySuperiors;
	}

	public String[] getBeforeOnKillEntityInferiors()
	{
		return beforeOnKillEntityInferiors;
	}

	public String[] getOverrideOnKillEntitySuperiors()
	{
		return overrideOnKillEntitySuperiors;
	}

	public String[] getOverrideOnKillEntityInferiors()
	{
		return overrideOnKillEntityInferiors;
	}

	public String[] getAfterOnKillEntitySuperiors()
	{
		return afterOnKillEntitySuperiors;
	}

	public String[] getAfterOnKillEntityInferiors()
	{
		return afterOnKillEntityInferiors;
	}

	public void setBeforeOnKillEntitySuperiors(String[] value)
	{
		beforeOnKillEntitySuperiors = value;
	}

	public void setBeforeOnKillEntityInferiors(String[] value)
	{
		beforeOnKillEntityInferiors = value;
	}

	public void setOverrideOnKillEntitySuperiors(String[] value)
	{
		overrideOnKillEntitySuperiors = value;
	}

	public void setOverrideOnKillEntityInferiors(String[] value)
	{
		overrideOnKillEntityInferiors = value;
	}

	public void setAfterOnKillEntitySuperiors(String[] value)
	{
		afterOnKillEntitySuperiors = value;
	}

	public void setAfterOnKillEntityInferiors(String[] value)
	{
		afterOnKillEntityInferiors = value;
	}

	public String[] getBeforeOnStruckByLightningSuperiors()
	{
		return beforeOnStruckByLightningSuperiors;
	}

	public String[] getBeforeOnStruckByLightningInferiors()
	{
		return beforeOnStruckByLightningInferiors;
	}

	public String[] getOverrideOnStruckByLightningSuperiors()
	{
		return overrideOnStruckByLightningSuperiors;
	}

	public String[] getOverrideOnStruckByLightningInferiors()
	{
		return overrideOnStruckByLightningInferiors;
	}

	public String[] getAfterOnStruckByLightningSuperiors()
	{
		return afterOnStruckByLightningSuperiors;
	}

	public String[] getAfterOnStruckByLightningInferiors()
	{
		return afterOnStruckByLightningInferiors;
	}

	public void setBeforeOnStruckByLightningSuperiors(String[] value)
	{
		beforeOnStruckByLightningSuperiors = value;
	}

	public void setBeforeOnStruckByLightningInferiors(String[] value)
	{
		beforeOnStruckByLightningInferiors = value;
	}

	public void setOverrideOnStruckByLightningSuperiors(String[] value)
	{
		overrideOnStruckByLightningSuperiors = value;
	}

	public void setOverrideOnStruckByLightningInferiors(String[] value)
	{
		overrideOnStruckByLightningInferiors = value;
	}

	public void setAfterOnStruckByLightningSuperiors(String[] value)
	{
		afterOnStruckByLightningSuperiors = value;
	}

	public void setAfterOnStruckByLightningInferiors(String[] value)
	{
		afterOnStruckByLightningInferiors = value;
	}

	public String[] getBeforeOnUpdateSuperiors()
	{
		return beforeOnUpdateSuperiors;
	}

	public String[] getBeforeOnUpdateInferiors()
	{
		return beforeOnUpdateInferiors;
	}

	public String[] getOverrideOnUpdateSuperiors()
	{
		return overrideOnUpdateSuperiors;
	}

	public String[] getOverrideOnUpdateInferiors()
	{
		return overrideOnUpdateInferiors;
	}

	public String[] getAfterOnUpdateSuperiors()
	{
		return afterOnUpdateSuperiors;
	}

	public String[] getAfterOnUpdateInferiors()
	{
		return afterOnUpdateInferiors;
	}

	public void setBeforeOnUpdateSuperiors(String[] value)
	{
		beforeOnUpdateSuperiors = value;
	}

	public void setBeforeOnUpdateInferiors(String[] value)
	{
		beforeOnUpdateInferiors = value;
	}

	public void setOverrideOnUpdateSuperiors(String[] value)
	{
		overrideOnUpdateSuperiors = value;
	}

	public void setOverrideOnUpdateInferiors(String[] value)
	{
		overrideOnUpdateInferiors = value;
	}

	public void setAfterOnUpdateSuperiors(String[] value)
	{
		afterOnUpdateSuperiors = value;
	}

	public void setAfterOnUpdateInferiors(String[] value)
	{
		afterOnUpdateInferiors = value;
	}

	public String[] getBeforeOnUpdateEntitySuperiors()
	{
		return beforeOnUpdateEntitySuperiors;
	}

	public String[] getBeforeOnUpdateEntityInferiors()
	{
		return beforeOnUpdateEntityInferiors;
	}

	public String[] getOverrideOnUpdateEntitySuperiors()
	{
		return overrideOnUpdateEntitySuperiors;
	}

	public String[] getOverrideOnUpdateEntityInferiors()
	{
		return overrideOnUpdateEntityInferiors;
	}

	public String[] getAfterOnUpdateEntitySuperiors()
	{
		return afterOnUpdateEntitySuperiors;
	}

	public String[] getAfterOnUpdateEntityInferiors()
	{
		return afterOnUpdateEntityInferiors;
	}

	public void setBeforeOnUpdateEntitySuperiors(String[] value)
	{
		beforeOnUpdateEntitySuperiors = value;
	}

	public void setBeforeOnUpdateEntityInferiors(String[] value)
	{
		beforeOnUpdateEntityInferiors = value;
	}

	public void setOverrideOnUpdateEntitySuperiors(String[] value)
	{
		overrideOnUpdateEntitySuperiors = value;
	}

	public void setOverrideOnUpdateEntityInferiors(String[] value)
	{
		overrideOnUpdateEntityInferiors = value;
	}

	public void setAfterOnUpdateEntitySuperiors(String[] value)
	{
		afterOnUpdateEntitySuperiors = value;
	}

	public void setAfterOnUpdateEntityInferiors(String[] value)
	{
		afterOnUpdateEntityInferiors = value;
	}

	public String[] getBeforeReadEntityFromNBTSuperiors()
	{
		return beforeReadEntityFromNBTSuperiors;
	}

	public String[] getBeforeReadEntityFromNBTInferiors()
	{
		return beforeReadEntityFromNBTInferiors;
	}

	public String[] getOverrideReadEntityFromNBTSuperiors()
	{
		return overrideReadEntityFromNBTSuperiors;
	}

	public String[] getOverrideReadEntityFromNBTInferiors()
	{
		return overrideReadEntityFromNBTInferiors;
	}

	public String[] getAfterReadEntityFromNBTSuperiors()
	{
		return afterReadEntityFromNBTSuperiors;
	}

	public String[] getAfterReadEntityFromNBTInferiors()
	{
		return afterReadEntityFromNBTInferiors;
	}

	public void setBeforeReadEntityFromNBTSuperiors(String[] value)
	{
		beforeReadEntityFromNBTSuperiors = value;
	}

	public void setBeforeReadEntityFromNBTInferiors(String[] value)
	{
		beforeReadEntityFromNBTInferiors = value;
	}

	public void setOverrideReadEntityFromNBTSuperiors(String[] value)
	{
		overrideReadEntityFromNBTSuperiors = value;
	}

	public void setOverrideReadEntityFromNBTInferiors(String[] value)
	{
		overrideReadEntityFromNBTInferiors = value;
	}

	public void setAfterReadEntityFromNBTSuperiors(String[] value)
	{
		afterReadEntityFromNBTSuperiors = value;
	}

	public void setAfterReadEntityFromNBTInferiors(String[] value)
	{
		afterReadEntityFromNBTInferiors = value;
	}

	public String[] getBeforeSetDeadSuperiors()
	{
		return beforeSetDeadSuperiors;
	}

	public String[] getBeforeSetDeadInferiors()
	{
		return beforeSetDeadInferiors;
	}

	public String[] getOverrideSetDeadSuperiors()
	{
		return overrideSetDeadSuperiors;
	}

	public String[] getOverrideSetDeadInferiors()
	{
		return overrideSetDeadInferiors;
	}

	public String[] getAfterSetDeadSuperiors()
	{
		return afterSetDeadSuperiors;
	}

	public String[] getAfterSetDeadInferiors()
	{
		return afterSetDeadInferiors;
	}

	public void setBeforeSetDeadSuperiors(String[] value)
	{
		beforeSetDeadSuperiors = value;
	}

	public void setBeforeSetDeadInferiors(String[] value)
	{
		beforeSetDeadInferiors = value;
	}

	public void setOverrideSetDeadSuperiors(String[] value)
	{
		overrideSetDeadSuperiors = value;
	}

	public void setOverrideSetDeadInferiors(String[] value)
	{
		overrideSetDeadInferiors = value;
	}

	public void setAfterSetDeadSuperiors(String[] value)
	{
		afterSetDeadSuperiors = value;
	}

	public void setAfterSetDeadInferiors(String[] value)
	{
		afterSetDeadInferiors = value;
	}

	public String[] getBeforeSetEntityActionStateSuperiors()
	{
		return beforeSetEntityActionStateSuperiors;
	}

	public String[] getBeforeSetEntityActionStateInferiors()
	{
		return beforeSetEntityActionStateInferiors;
	}

	public String[] getOverrideSetEntityActionStateSuperiors()
	{
		return overrideSetEntityActionStateSuperiors;
	}

	public String[] getOverrideSetEntityActionStateInferiors()
	{
		return overrideSetEntityActionStateInferiors;
	}

	public String[] getAfterSetEntityActionStateSuperiors()
	{
		return afterSetEntityActionStateSuperiors;
	}

	public String[] getAfterSetEntityActionStateInferiors()
	{
		return afterSetEntityActionStateInferiors;
	}

	public void setBeforeSetEntityActionStateSuperiors(String[] value)
	{
		beforeSetEntityActionStateSuperiors = value;
	}

	public void setBeforeSetEntityActionStateInferiors(String[] value)
	{
		beforeSetEntityActionStateInferiors = value;
	}

	public void setOverrideSetEntityActionStateSuperiors(String[] value)
	{
		overrideSetEntityActionStateSuperiors = value;
	}

	public void setOverrideSetEntityActionStateInferiors(String[] value)
	{
		overrideSetEntityActionStateInferiors = value;
	}

	public void setAfterSetEntityActionStateSuperiors(String[] value)
	{
		afterSetEntityActionStateSuperiors = value;
	}

	public void setAfterSetEntityActionStateInferiors(String[] value)
	{
		afterSetEntityActionStateInferiors = value;
	}

	public String[] getBeforeSetPositionSuperiors()
	{
		return beforeSetPositionSuperiors;
	}

	public String[] getBeforeSetPositionInferiors()
	{
		return beforeSetPositionInferiors;
	}

	public String[] getOverrideSetPositionSuperiors()
	{
		return overrideSetPositionSuperiors;
	}

	public String[] getOverrideSetPositionInferiors()
	{
		return overrideSetPositionInferiors;
	}

	public String[] getAfterSetPositionSuperiors()
	{
		return afterSetPositionSuperiors;
	}

	public String[] getAfterSetPositionInferiors()
	{
		return afterSetPositionInferiors;
	}

	public void setBeforeSetPositionSuperiors(String[] value)
	{
		beforeSetPositionSuperiors = value;
	}

	public void setBeforeSetPositionInferiors(String[] value)
	{
		beforeSetPositionInferiors = value;
	}

	public void setOverrideSetPositionSuperiors(String[] value)
	{
		overrideSetPositionSuperiors = value;
	}

	public void setOverrideSetPositionInferiors(String[] value)
	{
		overrideSetPositionInferiors = value;
	}

	public void setAfterSetPositionSuperiors(String[] value)
	{
		afterSetPositionSuperiors = value;
	}

	public void setAfterSetPositionInferiors(String[] value)
	{
		afterSetPositionInferiors = value;
	}

	public String[] getBeforeSetSneakingSuperiors()
	{
		return beforeSetSneakingSuperiors;
	}

	public String[] getBeforeSetSneakingInferiors()
	{
		return beforeSetSneakingInferiors;
	}

	public String[] getOverrideSetSneakingSuperiors()
	{
		return overrideSetSneakingSuperiors;
	}

	public String[] getOverrideSetSneakingInferiors()
	{
		return overrideSetSneakingInferiors;
	}

	public String[] getAfterSetSneakingSuperiors()
	{
		return afterSetSneakingSuperiors;
	}

	public String[] getAfterSetSneakingInferiors()
	{
		return afterSetSneakingInferiors;
	}

	public void setBeforeSetSneakingSuperiors(String[] value)
	{
		beforeSetSneakingSuperiors = value;
	}

	public void setBeforeSetSneakingInferiors(String[] value)
	{
		beforeSetSneakingInferiors = value;
	}

	public void setOverrideSetSneakingSuperiors(String[] value)
	{
		overrideSetSneakingSuperiors = value;
	}

	public void setOverrideSetSneakingInferiors(String[] value)
	{
		overrideSetSneakingInferiors = value;
	}

	public void setAfterSetSneakingSuperiors(String[] value)
	{
		afterSetSneakingSuperiors = value;
	}

	public void setAfterSetSneakingInferiors(String[] value)
	{
		afterSetSneakingInferiors = value;
	}

	public String[] getBeforeSetSprintingSuperiors()
	{
		return beforeSetSprintingSuperiors;
	}

	public String[] getBeforeSetSprintingInferiors()
	{
		return beforeSetSprintingInferiors;
	}

	public String[] getOverrideSetSprintingSuperiors()
	{
		return overrideSetSprintingSuperiors;
	}

	public String[] getOverrideSetSprintingInferiors()
	{
		return overrideSetSprintingInferiors;
	}

	public String[] getAfterSetSprintingSuperiors()
	{
		return afterSetSprintingSuperiors;
	}

	public String[] getAfterSetSprintingInferiors()
	{
		return afterSetSprintingInferiors;
	}

	public void setBeforeSetSprintingSuperiors(String[] value)
	{
		beforeSetSprintingSuperiors = value;
	}

	public void setBeforeSetSprintingInferiors(String[] value)
	{
		beforeSetSprintingInferiors = value;
	}

	public void setOverrideSetSprintingSuperiors(String[] value)
	{
		overrideSetSprintingSuperiors = value;
	}

	public void setOverrideSetSprintingInferiors(String[] value)
	{
		overrideSetSprintingInferiors = value;
	}

	public void setAfterSetSprintingSuperiors(String[] value)
	{
		afterSetSprintingSuperiors = value;
	}

	public void setAfterSetSprintingInferiors(String[] value)
	{
		afterSetSprintingInferiors = value;
	}

	public String[] getBeforeSwingItemSuperiors()
	{
		return beforeSwingItemSuperiors;
	}

	public String[] getBeforeSwingItemInferiors()
	{
		return beforeSwingItemInferiors;
	}

	public String[] getOverrideSwingItemSuperiors()
	{
		return overrideSwingItemSuperiors;
	}

	public String[] getOverrideSwingItemInferiors()
	{
		return overrideSwingItemInferiors;
	}

	public String[] getAfterSwingItemSuperiors()
	{
		return afterSwingItemSuperiors;
	}

	public String[] getAfterSwingItemInferiors()
	{
		return afterSwingItemInferiors;
	}

	public void setBeforeSwingItemSuperiors(String[] value)
	{
		beforeSwingItemSuperiors = value;
	}

	public void setBeforeSwingItemInferiors(String[] value)
	{
		beforeSwingItemInferiors = value;
	}

	public void setOverrideSwingItemSuperiors(String[] value)
	{
		overrideSwingItemSuperiors = value;
	}

	public void setOverrideSwingItemInferiors(String[] value)
	{
		overrideSwingItemInferiors = value;
	}

	public void setAfterSwingItemSuperiors(String[] value)
	{
		afterSwingItemSuperiors = value;
	}

	public void setAfterSwingItemInferiors(String[] value)
	{
		afterSwingItemInferiors = value;
	}

	public String[] getBeforeUpdateEntityActionStateSuperiors()
	{
		return beforeUpdateEntityActionStateSuperiors;
	}

	public String[] getBeforeUpdateEntityActionStateInferiors()
	{
		return beforeUpdateEntityActionStateInferiors;
	}

	public String[] getOverrideUpdateEntityActionStateSuperiors()
	{
		return overrideUpdateEntityActionStateSuperiors;
	}

	public String[] getOverrideUpdateEntityActionStateInferiors()
	{
		return overrideUpdateEntityActionStateInferiors;
	}

	public String[] getAfterUpdateEntityActionStateSuperiors()
	{
		return afterUpdateEntityActionStateSuperiors;
	}

	public String[] getAfterUpdateEntityActionStateInferiors()
	{
		return afterUpdateEntityActionStateInferiors;
	}

	public void setBeforeUpdateEntityActionStateSuperiors(String[] value)
	{
		beforeUpdateEntityActionStateSuperiors = value;
	}

	public void setBeforeUpdateEntityActionStateInferiors(String[] value)
	{
		beforeUpdateEntityActionStateInferiors = value;
	}

	public void setOverrideUpdateEntityActionStateSuperiors(String[] value)
	{
		overrideUpdateEntityActionStateSuperiors = value;
	}

	public void setOverrideUpdateEntityActionStateInferiors(String[] value)
	{
		overrideUpdateEntityActionStateInferiors = value;
	}

	public void setAfterUpdateEntityActionStateSuperiors(String[] value)
	{
		afterUpdateEntityActionStateSuperiors = value;
	}

	public void setAfterUpdateEntityActionStateInferiors(String[] value)
	{
		afterUpdateEntityActionStateInferiors = value;
	}

	public String[] getBeforeUpdatePotionEffectsSuperiors()
	{
		return beforeUpdatePotionEffectsSuperiors;
	}

	public String[] getBeforeUpdatePotionEffectsInferiors()
	{
		return beforeUpdatePotionEffectsInferiors;
	}

	public String[] getOverrideUpdatePotionEffectsSuperiors()
	{
		return overrideUpdatePotionEffectsSuperiors;
	}

	public String[] getOverrideUpdatePotionEffectsInferiors()
	{
		return overrideUpdatePotionEffectsInferiors;
	}

	public String[] getAfterUpdatePotionEffectsSuperiors()
	{
		return afterUpdatePotionEffectsSuperiors;
	}

	public String[] getAfterUpdatePotionEffectsInferiors()
	{
		return afterUpdatePotionEffectsInferiors;
	}

	public void setBeforeUpdatePotionEffectsSuperiors(String[] value)
	{
		beforeUpdatePotionEffectsSuperiors = value;
	}

	public void setBeforeUpdatePotionEffectsInferiors(String[] value)
	{
		beforeUpdatePotionEffectsInferiors = value;
	}

	public void setOverrideUpdatePotionEffectsSuperiors(String[] value)
	{
		overrideUpdatePotionEffectsSuperiors = value;
	}

	public void setOverrideUpdatePotionEffectsInferiors(String[] value)
	{
		overrideUpdatePotionEffectsInferiors = value;
	}

	public void setAfterUpdatePotionEffectsSuperiors(String[] value)
	{
		afterUpdatePotionEffectsSuperiors = value;
	}

	public void setAfterUpdatePotionEffectsInferiors(String[] value)
	{
		afterUpdatePotionEffectsInferiors = value;
	}

	public String[] getBeforeUpdateRiddenSuperiors()
	{
		return beforeUpdateRiddenSuperiors;
	}

	public String[] getBeforeUpdateRiddenInferiors()
	{
		return beforeUpdateRiddenInferiors;
	}

	public String[] getOverrideUpdateRiddenSuperiors()
	{
		return overrideUpdateRiddenSuperiors;
	}

	public String[] getOverrideUpdateRiddenInferiors()
	{
		return overrideUpdateRiddenInferiors;
	}

	public String[] getAfterUpdateRiddenSuperiors()
	{
		return afterUpdateRiddenSuperiors;
	}

	public String[] getAfterUpdateRiddenInferiors()
	{
		return afterUpdateRiddenInferiors;
	}

	public void setBeforeUpdateRiddenSuperiors(String[] value)
	{
		beforeUpdateRiddenSuperiors = value;
	}

	public void setBeforeUpdateRiddenInferiors(String[] value)
	{
		beforeUpdateRiddenInferiors = value;
	}

	public void setOverrideUpdateRiddenSuperiors(String[] value)
	{
		overrideUpdateRiddenSuperiors = value;
	}

	public void setOverrideUpdateRiddenInferiors(String[] value)
	{
		overrideUpdateRiddenInferiors = value;
	}

	public void setAfterUpdateRiddenSuperiors(String[] value)
	{
		afterUpdateRiddenSuperiors = value;
	}

	public void setAfterUpdateRiddenInferiors(String[] value)
	{
		afterUpdateRiddenInferiors = value;
	}

	public String[] getBeforeWakeUpPlayerSuperiors()
	{
		return beforeWakeUpPlayerSuperiors;
	}

	public String[] getBeforeWakeUpPlayerInferiors()
	{
		return beforeWakeUpPlayerInferiors;
	}

	public String[] getOverrideWakeUpPlayerSuperiors()
	{
		return overrideWakeUpPlayerSuperiors;
	}

	public String[] getOverrideWakeUpPlayerInferiors()
	{
		return overrideWakeUpPlayerInferiors;
	}

	public String[] getAfterWakeUpPlayerSuperiors()
	{
		return afterWakeUpPlayerSuperiors;
	}

	public String[] getAfterWakeUpPlayerInferiors()
	{
		return afterWakeUpPlayerInferiors;
	}

	public void setBeforeWakeUpPlayerSuperiors(String[] value)
	{
		beforeWakeUpPlayerSuperiors = value;
	}

	public void setBeforeWakeUpPlayerInferiors(String[] value)
	{
		beforeWakeUpPlayerInferiors = value;
	}

	public void setOverrideWakeUpPlayerSuperiors(String[] value)
	{
		overrideWakeUpPlayerSuperiors = value;
	}

	public void setOverrideWakeUpPlayerInferiors(String[] value)
	{
		overrideWakeUpPlayerInferiors = value;
	}

	public void setAfterWakeUpPlayerSuperiors(String[] value)
	{
		afterWakeUpPlayerSuperiors = value;
	}

	public void setAfterWakeUpPlayerInferiors(String[] value)
	{
		afterWakeUpPlayerInferiors = value;
	}

	public String[] getBeforeWriteEntityToNBTSuperiors()
	{
		return beforeWriteEntityToNBTSuperiors;
	}

	public String[] getBeforeWriteEntityToNBTInferiors()
	{
		return beforeWriteEntityToNBTInferiors;
	}

	public String[] getOverrideWriteEntityToNBTSuperiors()
	{
		return overrideWriteEntityToNBTSuperiors;
	}

	public String[] getOverrideWriteEntityToNBTInferiors()
	{
		return overrideWriteEntityToNBTInferiors;
	}

	public String[] getAfterWriteEntityToNBTSuperiors()
	{
		return afterWriteEntityToNBTSuperiors;
	}

	public String[] getAfterWriteEntityToNBTInferiors()
	{
		return afterWriteEntityToNBTInferiors;
	}

	public void setBeforeWriteEntityToNBTSuperiors(String[] value)
	{
		beforeWriteEntityToNBTSuperiors = value;
	}

	public void setBeforeWriteEntityToNBTInferiors(String[] value)
	{
		beforeWriteEntityToNBTInferiors = value;
	}

	public void setOverrideWriteEntityToNBTSuperiors(String[] value)
	{
		overrideWriteEntityToNBTSuperiors = value;
	}

	public void setOverrideWriteEntityToNBTInferiors(String[] value)
	{
		overrideWriteEntityToNBTInferiors = value;
	}

	public void setAfterWriteEntityToNBTSuperiors(String[] value)
	{
		afterWriteEntityToNBTSuperiors = value;
	}

	public void setAfterWriteEntityToNBTInferiors(String[] value)
	{
		afterWriteEntityToNBTInferiors = value;
	}

}
