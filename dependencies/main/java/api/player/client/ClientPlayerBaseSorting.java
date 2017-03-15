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

import java.util.*;

public final class ClientPlayerBaseSorting
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

	private String[] beforeAddMovementStatSuperiors = null;
	private String[] beforeAddMovementStatInferiors = null;
	private String[] overrideAddMovementStatSuperiors = null;
	private String[] overrideAddMovementStatInferiors = null;
	private String[] afterAddMovementStatSuperiors = null;
	private String[] afterAddMovementStatInferiors = null;

	private String[] beforeAddStatSuperiors = null;
	private String[] beforeAddStatInferiors = null;
	private String[] overrideAddStatSuperiors = null;
	private String[] overrideAddStatInferiors = null;
	private String[] afterAddStatSuperiors = null;
	private String[] afterAddStatInferiors = null;

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

	private String[] beforeCloseScreenSuperiors = null;
	private String[] beforeCloseScreenInferiors = null;
	private String[] overrideCloseScreenSuperiors = null;
	private String[] overrideCloseScreenInferiors = null;
	private String[] afterCloseScreenSuperiors = null;
	private String[] afterCloseScreenInferiors = null;

	private String[] beforeDamageEntitySuperiors = null;
	private String[] beforeDamageEntityInferiors = null;
	private String[] overrideDamageEntitySuperiors = null;
	private String[] overrideDamageEntityInferiors = null;
	private String[] afterDamageEntitySuperiors = null;
	private String[] afterDamageEntityInferiors = null;

	private String[] beforeDisplayGuiSuperiors = null;
	private String[] beforeDisplayGuiInferiors = null;
	private String[] overrideDisplayGuiSuperiors = null;
	private String[] overrideDisplayGuiInferiors = null;
	private String[] afterDisplayGuiSuperiors = null;
	private String[] afterDisplayGuiInferiors = null;

	private String[] beforeDisplayGUIChestSuperiors = null;
	private String[] beforeDisplayGUIChestInferiors = null;
	private String[] overrideDisplayGUIChestSuperiors = null;
	private String[] overrideDisplayGUIChestInferiors = null;
	private String[] afterDisplayGUIChestSuperiors = null;
	private String[] afterDisplayGUIChestInferiors = null;

	private String[] beforeDropItemSuperiors = null;
	private String[] beforeDropItemInferiors = null;
	private String[] overrideDropItemSuperiors = null;
	private String[] overrideDropItemInferiors = null;
	private String[] afterDropItemSuperiors = null;
	private String[] afterDropItemInferiors = null;

	private String[] beforeDropOneItemSuperiors = null;
	private String[] beforeDropOneItemInferiors = null;
	private String[] overrideDropOneItemSuperiors = null;
	private String[] overrideDropOneItemInferiors = null;
	private String[] afterDropOneItemSuperiors = null;
	private String[] afterDropOneItemInferiors = null;

	private String[] beforeDropPlayerItemWithRandomChoiceSuperiors = null;
	private String[] beforeDropPlayerItemWithRandomChoiceInferiors = null;
	private String[] overrideDropPlayerItemWithRandomChoiceSuperiors = null;
	private String[] overrideDropPlayerItemWithRandomChoiceInferiors = null;
	private String[] afterDropPlayerItemWithRandomChoiceSuperiors = null;
	private String[] afterDropPlayerItemWithRandomChoiceInferiors = null;

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

	private String[] beforeGetBedOrientationInDegreesSuperiors = null;
	private String[] beforeGetBedOrientationInDegreesInferiors = null;
	private String[] overrideGetBedOrientationInDegreesSuperiors = null;
	private String[] overrideGetBedOrientationInDegreesInferiors = null;
	private String[] afterGetBedOrientationInDegreesSuperiors = null;
	private String[] afterGetBedOrientationInDegreesInferiors = null;

	private String[] beforeGetBrightnessSuperiors = null;
	private String[] beforeGetBrightnessInferiors = null;
	private String[] overrideGetBrightnessSuperiors = null;
	private String[] overrideGetBrightnessInferiors = null;
	private String[] afterGetBrightnessSuperiors = null;
	private String[] afterGetBrightnessInferiors = null;

	private String[] beforeGetBrightnessForRenderSuperiors = null;
	private String[] beforeGetBrightnessForRenderInferiors = null;
	private String[] overrideGetBrightnessForRenderSuperiors = null;
	private String[] overrideGetBrightnessForRenderInferiors = null;
	private String[] afterGetBrightnessForRenderSuperiors = null;
	private String[] afterGetBrightnessForRenderInferiors = null;

	private String[] beforeGetBreakSpeedSuperiors = null;
	private String[] beforeGetBreakSpeedInferiors = null;
	private String[] overrideGetBreakSpeedSuperiors = null;
	private String[] overrideGetBreakSpeedInferiors = null;
	private String[] afterGetBreakSpeedSuperiors = null;
	private String[] afterGetBreakSpeedInferiors = null;

	private String[] beforeGetDistanceSqSuperiors = null;
	private String[] beforeGetDistanceSqInferiors = null;
	private String[] overrideGetDistanceSqSuperiors = null;
	private String[] overrideGetDistanceSqInferiors = null;
	private String[] afterGetDistanceSqSuperiors = null;
	private String[] afterGetDistanceSqInferiors = null;

	private String[] beforeGetDistanceSqToEntitySuperiors = null;
	private String[] beforeGetDistanceSqToEntityInferiors = null;
	private String[] overrideGetDistanceSqToEntitySuperiors = null;
	private String[] overrideGetDistanceSqToEntityInferiors = null;
	private String[] afterGetDistanceSqToEntitySuperiors = null;
	private String[] afterGetDistanceSqToEntityInferiors = null;

	private String[] beforeGetFovModifierSuperiors = null;
	private String[] beforeGetFovModifierInferiors = null;
	private String[] overrideGetFovModifierSuperiors = null;
	private String[] overrideGetFovModifierInferiors = null;
	private String[] afterGetFovModifierSuperiors = null;
	private String[] afterGetFovModifierInferiors = null;

	private String[] beforeGetHurtSoundSuperiors = null;
	private String[] beforeGetHurtSoundInferiors = null;
	private String[] overrideGetHurtSoundSuperiors = null;
	private String[] overrideGetHurtSoundInferiors = null;
	private String[] afterGetHurtSoundSuperiors = null;
	private String[] afterGetHurtSoundInferiors = null;

	private String[] beforeGetNameSuperiors = null;
	private String[] beforeGetNameInferiors = null;
	private String[] overrideGetNameSuperiors = null;
	private String[] overrideGetNameInferiors = null;
	private String[] afterGetNameSuperiors = null;
	private String[] afterGetNameInferiors = null;

	private String[] beforeGetSleepTimerSuperiors = null;
	private String[] beforeGetSleepTimerInferiors = null;
	private String[] overrideGetSleepTimerSuperiors = null;
	private String[] overrideGetSleepTimerInferiors = null;
	private String[] afterGetSleepTimerSuperiors = null;
	private String[] afterGetSleepTimerInferiors = null;

	private String[] beforeHandleWaterMovementSuperiors = null;
	private String[] beforeHandleWaterMovementInferiors = null;
	private String[] overrideHandleWaterMovementSuperiors = null;
	private String[] overrideHandleWaterMovementInferiors = null;
	private String[] afterHandleWaterMovementSuperiors = null;
	private String[] afterHandleWaterMovementInferiors = null;

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

	private String[] beforeIsSprintingSuperiors = null;
	private String[] beforeIsSprintingInferiors = null;
	private String[] overrideIsSprintingSuperiors = null;
	private String[] overrideIsSprintingInferiors = null;
	private String[] afterIsSprintingSuperiors = null;
	private String[] afterIsSprintingInferiors = null;

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

	private String[] beforePlayStepSoundSuperiors = null;
	private String[] beforePlayStepSoundInferiors = null;
	private String[] overridePlayStepSoundSuperiors = null;
	private String[] overridePlayStepSoundInferiors = null;
	private String[] afterPlayStepSoundSuperiors = null;
	private String[] afterPlayStepSoundInferiors = null;

	private String[] beforePushOutOfBlocksSuperiors = null;
	private String[] beforePushOutOfBlocksInferiors = null;
	private String[] overridePushOutOfBlocksSuperiors = null;
	private String[] overridePushOutOfBlocksInferiors = null;
	private String[] afterPushOutOfBlocksSuperiors = null;
	private String[] afterPushOutOfBlocksInferiors = null;

	private String[] beforeRayTraceSuperiors = null;
	private String[] beforeRayTraceInferiors = null;
	private String[] overrideRayTraceSuperiors = null;
	private String[] overrideRayTraceInferiors = null;
	private String[] afterRayTraceSuperiors = null;
	private String[] afterRayTraceInferiors = null;

	private String[] beforeReadEntityFromNBTSuperiors = null;
	private String[] beforeReadEntityFromNBTInferiors = null;
	private String[] overrideReadEntityFromNBTSuperiors = null;
	private String[] overrideReadEntityFromNBTInferiors = null;
	private String[] afterReadEntityFromNBTSuperiors = null;
	private String[] afterReadEntityFromNBTInferiors = null;

	private String[] beforeRespawnPlayerSuperiors = null;
	private String[] beforeRespawnPlayerInferiors = null;
	private String[] overrideRespawnPlayerSuperiors = null;
	private String[] overrideRespawnPlayerInferiors = null;
	private String[] afterRespawnPlayerSuperiors = null;
	private String[] afterRespawnPlayerInferiors = null;

	private String[] beforeSetDeadSuperiors = null;
	private String[] beforeSetDeadInferiors = null;
	private String[] overrideSetDeadSuperiors = null;
	private String[] overrideSetDeadInferiors = null;
	private String[] afterSetDeadSuperiors = null;
	private String[] afterSetDeadInferiors = null;

	private String[] beforeSetPlayerSPHealthSuperiors = null;
	private String[] beforeSetPlayerSPHealthInferiors = null;
	private String[] overrideSetPlayerSPHealthSuperiors = null;
	private String[] overrideSetPlayerSPHealthInferiors = null;
	private String[] afterSetPlayerSPHealthSuperiors = null;
	private String[] afterSetPlayerSPHealthInferiors = null;

	private String[] beforeSetPositionAndRotationSuperiors = null;
	private String[] beforeSetPositionAndRotationInferiors = null;
	private String[] overrideSetPositionAndRotationSuperiors = null;
	private String[] overrideSetPositionAndRotationInferiors = null;
	private String[] afterSetPositionAndRotationSuperiors = null;
	private String[] afterSetPositionAndRotationInferiors = null;

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

	private String[] beforeTrySleepSuperiors = null;
	private String[] beforeTrySleepInferiors = null;
	private String[] overrideTrySleepSuperiors = null;
	private String[] overrideTrySleepInferiors = null;
	private String[] afterTrySleepSuperiors = null;
	private String[] afterTrySleepInferiors = null;

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

	public String[] getBeforeAddStatSuperiors()
	{
		return beforeAddStatSuperiors;
	}

	public String[] getBeforeAddStatInferiors()
	{
		return beforeAddStatInferiors;
	}

	public String[] getOverrideAddStatSuperiors()
	{
		return overrideAddStatSuperiors;
	}

	public String[] getOverrideAddStatInferiors()
	{
		return overrideAddStatInferiors;
	}

	public String[] getAfterAddStatSuperiors()
	{
		return afterAddStatSuperiors;
	}

	public String[] getAfterAddStatInferiors()
	{
		return afterAddStatInferiors;
	}

	public void setBeforeAddStatSuperiors(String[] value)
	{
		beforeAddStatSuperiors = value;
	}

	public void setBeforeAddStatInferiors(String[] value)
	{
		beforeAddStatInferiors = value;
	}

	public void setOverrideAddStatSuperiors(String[] value)
	{
		overrideAddStatSuperiors = value;
	}

	public void setOverrideAddStatInferiors(String[] value)
	{
		overrideAddStatInferiors = value;
	}

	public void setAfterAddStatSuperiors(String[] value)
	{
		afterAddStatSuperiors = value;
	}

	public void setAfterAddStatInferiors(String[] value)
	{
		afterAddStatInferiors = value;
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

	public String[] getBeforeCloseScreenSuperiors()
	{
		return beforeCloseScreenSuperiors;
	}

	public String[] getBeforeCloseScreenInferiors()
	{
		return beforeCloseScreenInferiors;
	}

	public String[] getOverrideCloseScreenSuperiors()
	{
		return overrideCloseScreenSuperiors;
	}

	public String[] getOverrideCloseScreenInferiors()
	{
		return overrideCloseScreenInferiors;
	}

	public String[] getAfterCloseScreenSuperiors()
	{
		return afterCloseScreenSuperiors;
	}

	public String[] getAfterCloseScreenInferiors()
	{
		return afterCloseScreenInferiors;
	}

	public void setBeforeCloseScreenSuperiors(String[] value)
	{
		beforeCloseScreenSuperiors = value;
	}

	public void setBeforeCloseScreenInferiors(String[] value)
	{
		beforeCloseScreenInferiors = value;
	}

	public void setOverrideCloseScreenSuperiors(String[] value)
	{
		overrideCloseScreenSuperiors = value;
	}

	public void setOverrideCloseScreenInferiors(String[] value)
	{
		overrideCloseScreenInferiors = value;
	}

	public void setAfterCloseScreenSuperiors(String[] value)
	{
		afterCloseScreenSuperiors = value;
	}

	public void setAfterCloseScreenInferiors(String[] value)
	{
		afterCloseScreenInferiors = value;
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

	public String[] getBeforeDisplayGuiSuperiors()
	{
		return beforeDisplayGuiSuperiors;
	}

	public String[] getBeforeDisplayGuiInferiors()
	{
		return beforeDisplayGuiInferiors;
	}

	public String[] getOverrideDisplayGuiSuperiors()
	{
		return overrideDisplayGuiSuperiors;
	}

	public String[] getOverrideDisplayGuiInferiors()
	{
		return overrideDisplayGuiInferiors;
	}

	public String[] getAfterDisplayGuiSuperiors()
	{
		return afterDisplayGuiSuperiors;
	}

	public String[] getAfterDisplayGuiInferiors()
	{
		return afterDisplayGuiInferiors;
	}

	public void setBeforeDisplayGuiSuperiors(String[] value)
	{
		beforeDisplayGuiSuperiors = value;
	}

	public void setBeforeDisplayGuiInferiors(String[] value)
	{
		beforeDisplayGuiInferiors = value;
	}

	public void setOverrideDisplayGuiSuperiors(String[] value)
	{
		overrideDisplayGuiSuperiors = value;
	}

	public void setOverrideDisplayGuiInferiors(String[] value)
	{
		overrideDisplayGuiInferiors = value;
	}

	public void setAfterDisplayGuiSuperiors(String[] value)
	{
		afterDisplayGuiSuperiors = value;
	}

	public void setAfterDisplayGuiInferiors(String[] value)
	{
		afterDisplayGuiInferiors = value;
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

	public String[] getBeforeDropItemSuperiors()
	{
		return beforeDropItemSuperiors;
	}

	public String[] getBeforeDropItemInferiors()
	{
		return beforeDropItemInferiors;
	}

	public String[] getOverrideDropItemSuperiors()
	{
		return overrideDropItemSuperiors;
	}

	public String[] getOverrideDropItemInferiors()
	{
		return overrideDropItemInferiors;
	}

	public String[] getAfterDropItemSuperiors()
	{
		return afterDropItemSuperiors;
	}

	public String[] getAfterDropItemInferiors()
	{
		return afterDropItemInferiors;
	}

	public void setBeforeDropItemSuperiors(String[] value)
	{
		beforeDropItemSuperiors = value;
	}

	public void setBeforeDropItemInferiors(String[] value)
	{
		beforeDropItemInferiors = value;
	}

	public void setOverrideDropItemSuperiors(String[] value)
	{
		overrideDropItemSuperiors = value;
	}

	public void setOverrideDropItemInferiors(String[] value)
	{
		overrideDropItemInferiors = value;
	}

	public void setAfterDropItemSuperiors(String[] value)
	{
		afterDropItemSuperiors = value;
	}

	public void setAfterDropItemInferiors(String[] value)
	{
		afterDropItemInferiors = value;
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

	public String[] getBeforeDropPlayerItemWithRandomChoiceSuperiors()
	{
		return beforeDropPlayerItemWithRandomChoiceSuperiors;
	}

	public String[] getBeforeDropPlayerItemWithRandomChoiceInferiors()
	{
		return beforeDropPlayerItemWithRandomChoiceInferiors;
	}

	public String[] getOverrideDropPlayerItemWithRandomChoiceSuperiors()
	{
		return overrideDropPlayerItemWithRandomChoiceSuperiors;
	}

	public String[] getOverrideDropPlayerItemWithRandomChoiceInferiors()
	{
		return overrideDropPlayerItemWithRandomChoiceInferiors;
	}

	public String[] getAfterDropPlayerItemWithRandomChoiceSuperiors()
	{
		return afterDropPlayerItemWithRandomChoiceSuperiors;
	}

	public String[] getAfterDropPlayerItemWithRandomChoiceInferiors()
	{
		return afterDropPlayerItemWithRandomChoiceInferiors;
	}

	public void setBeforeDropPlayerItemWithRandomChoiceSuperiors(String[] value)
	{
		beforeDropPlayerItemWithRandomChoiceSuperiors = value;
	}

	public void setBeforeDropPlayerItemWithRandomChoiceInferiors(String[] value)
	{
		beforeDropPlayerItemWithRandomChoiceInferiors = value;
	}

	public void setOverrideDropPlayerItemWithRandomChoiceSuperiors(String[] value)
	{
		overrideDropPlayerItemWithRandomChoiceSuperiors = value;
	}

	public void setOverrideDropPlayerItemWithRandomChoiceInferiors(String[] value)
	{
		overrideDropPlayerItemWithRandomChoiceInferiors = value;
	}

	public void setAfterDropPlayerItemWithRandomChoiceSuperiors(String[] value)
	{
		afterDropPlayerItemWithRandomChoiceSuperiors = value;
	}

	public void setAfterDropPlayerItemWithRandomChoiceInferiors(String[] value)
	{
		afterDropPlayerItemWithRandomChoiceInferiors = value;
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

	public String[] getBeforeGetBedOrientationInDegreesSuperiors()
	{
		return beforeGetBedOrientationInDegreesSuperiors;
	}

	public String[] getBeforeGetBedOrientationInDegreesInferiors()
	{
		return beforeGetBedOrientationInDegreesInferiors;
	}

	public String[] getOverrideGetBedOrientationInDegreesSuperiors()
	{
		return overrideGetBedOrientationInDegreesSuperiors;
	}

	public String[] getOverrideGetBedOrientationInDegreesInferiors()
	{
		return overrideGetBedOrientationInDegreesInferiors;
	}

	public String[] getAfterGetBedOrientationInDegreesSuperiors()
	{
		return afterGetBedOrientationInDegreesSuperiors;
	}

	public String[] getAfterGetBedOrientationInDegreesInferiors()
	{
		return afterGetBedOrientationInDegreesInferiors;
	}

	public void setBeforeGetBedOrientationInDegreesSuperiors(String[] value)
	{
		beforeGetBedOrientationInDegreesSuperiors = value;
	}

	public void setBeforeGetBedOrientationInDegreesInferiors(String[] value)
	{
		beforeGetBedOrientationInDegreesInferiors = value;
	}

	public void setOverrideGetBedOrientationInDegreesSuperiors(String[] value)
	{
		overrideGetBedOrientationInDegreesSuperiors = value;
	}

	public void setOverrideGetBedOrientationInDegreesInferiors(String[] value)
	{
		overrideGetBedOrientationInDegreesInferiors = value;
	}

	public void setAfterGetBedOrientationInDegreesSuperiors(String[] value)
	{
		afterGetBedOrientationInDegreesSuperiors = value;
	}

	public void setAfterGetBedOrientationInDegreesInferiors(String[] value)
	{
		afterGetBedOrientationInDegreesInferiors = value;
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

	public String[] getBeforeGetBrightnessForRenderSuperiors()
	{
		return beforeGetBrightnessForRenderSuperiors;
	}

	public String[] getBeforeGetBrightnessForRenderInferiors()
	{
		return beforeGetBrightnessForRenderInferiors;
	}

	public String[] getOverrideGetBrightnessForRenderSuperiors()
	{
		return overrideGetBrightnessForRenderSuperiors;
	}

	public String[] getOverrideGetBrightnessForRenderInferiors()
	{
		return overrideGetBrightnessForRenderInferiors;
	}

	public String[] getAfterGetBrightnessForRenderSuperiors()
	{
		return afterGetBrightnessForRenderSuperiors;
	}

	public String[] getAfterGetBrightnessForRenderInferiors()
	{
		return afterGetBrightnessForRenderInferiors;
	}

	public void setBeforeGetBrightnessForRenderSuperiors(String[] value)
	{
		beforeGetBrightnessForRenderSuperiors = value;
	}

	public void setBeforeGetBrightnessForRenderInferiors(String[] value)
	{
		beforeGetBrightnessForRenderInferiors = value;
	}

	public void setOverrideGetBrightnessForRenderSuperiors(String[] value)
	{
		overrideGetBrightnessForRenderSuperiors = value;
	}

	public void setOverrideGetBrightnessForRenderInferiors(String[] value)
	{
		overrideGetBrightnessForRenderInferiors = value;
	}

	public void setAfterGetBrightnessForRenderSuperiors(String[] value)
	{
		afterGetBrightnessForRenderSuperiors = value;
	}

	public void setAfterGetBrightnessForRenderInferiors(String[] value)
	{
		afterGetBrightnessForRenderInferiors = value;
	}

	public String[] getBeforeGetBreakSpeedSuperiors()
	{
		return beforeGetBreakSpeedSuperiors;
	}

	public String[] getBeforeGetBreakSpeedInferiors()
	{
		return beforeGetBreakSpeedInferiors;
	}

	public String[] getOverrideGetBreakSpeedSuperiors()
	{
		return overrideGetBreakSpeedSuperiors;
	}

	public String[] getOverrideGetBreakSpeedInferiors()
	{
		return overrideGetBreakSpeedInferiors;
	}

	public String[] getAfterGetBreakSpeedSuperiors()
	{
		return afterGetBreakSpeedSuperiors;
	}

	public String[] getAfterGetBreakSpeedInferiors()
	{
		return afterGetBreakSpeedInferiors;
	}

	public void setBeforeGetBreakSpeedSuperiors(String[] value)
	{
		beforeGetBreakSpeedSuperiors = value;
	}

	public void setBeforeGetBreakSpeedInferiors(String[] value)
	{
		beforeGetBreakSpeedInferiors = value;
	}

	public void setOverrideGetBreakSpeedSuperiors(String[] value)
	{
		overrideGetBreakSpeedSuperiors = value;
	}

	public void setOverrideGetBreakSpeedInferiors(String[] value)
	{
		overrideGetBreakSpeedInferiors = value;
	}

	public void setAfterGetBreakSpeedSuperiors(String[] value)
	{
		afterGetBreakSpeedSuperiors = value;
	}

	public void setAfterGetBreakSpeedInferiors(String[] value)
	{
		afterGetBreakSpeedInferiors = value;
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

	public String[] getBeforeGetDistanceSqToEntitySuperiors()
	{
		return beforeGetDistanceSqToEntitySuperiors;
	}

	public String[] getBeforeGetDistanceSqToEntityInferiors()
	{
		return beforeGetDistanceSqToEntityInferiors;
	}

	public String[] getOverrideGetDistanceSqToEntitySuperiors()
	{
		return overrideGetDistanceSqToEntitySuperiors;
	}

	public String[] getOverrideGetDistanceSqToEntityInferiors()
	{
		return overrideGetDistanceSqToEntityInferiors;
	}

	public String[] getAfterGetDistanceSqToEntitySuperiors()
	{
		return afterGetDistanceSqToEntitySuperiors;
	}

	public String[] getAfterGetDistanceSqToEntityInferiors()
	{
		return afterGetDistanceSqToEntityInferiors;
	}

	public void setBeforeGetDistanceSqToEntitySuperiors(String[] value)
	{
		beforeGetDistanceSqToEntitySuperiors = value;
	}

	public void setBeforeGetDistanceSqToEntityInferiors(String[] value)
	{
		beforeGetDistanceSqToEntityInferiors = value;
	}

	public void setOverrideGetDistanceSqToEntitySuperiors(String[] value)
	{
		overrideGetDistanceSqToEntitySuperiors = value;
	}

	public void setOverrideGetDistanceSqToEntityInferiors(String[] value)
	{
		overrideGetDistanceSqToEntityInferiors = value;
	}

	public void setAfterGetDistanceSqToEntitySuperiors(String[] value)
	{
		afterGetDistanceSqToEntitySuperiors = value;
	}

	public void setAfterGetDistanceSqToEntityInferiors(String[] value)
	{
		afterGetDistanceSqToEntityInferiors = value;
	}

	public String[] getBeforeGetFovModifierSuperiors()
	{
		return beforeGetFovModifierSuperiors;
	}

	public String[] getBeforeGetFovModifierInferiors()
	{
		return beforeGetFovModifierInferiors;
	}

	public String[] getOverrideGetFovModifierSuperiors()
	{
		return overrideGetFovModifierSuperiors;
	}

	public String[] getOverrideGetFovModifierInferiors()
	{
		return overrideGetFovModifierInferiors;
	}

	public String[] getAfterGetFovModifierSuperiors()
	{
		return afterGetFovModifierSuperiors;
	}

	public String[] getAfterGetFovModifierInferiors()
	{
		return afterGetFovModifierInferiors;
	}

	public void setBeforeGetFovModifierSuperiors(String[] value)
	{
		beforeGetFovModifierSuperiors = value;
	}

	public void setBeforeGetFovModifierInferiors(String[] value)
	{
		beforeGetFovModifierInferiors = value;
	}

	public void setOverrideGetFovModifierSuperiors(String[] value)
	{
		overrideGetFovModifierSuperiors = value;
	}

	public void setOverrideGetFovModifierInferiors(String[] value)
	{
		overrideGetFovModifierInferiors = value;
	}

	public void setAfterGetFovModifierSuperiors(String[] value)
	{
		afterGetFovModifierSuperiors = value;
	}

	public void setAfterGetFovModifierInferiors(String[] value)
	{
		afterGetFovModifierInferiors = value;
	}

	public String[] getBeforeGetHurtSoundSuperiors()
	{
		return beforeGetHurtSoundSuperiors;
	}

	public String[] getBeforeGetHurtSoundInferiors()
	{
		return beforeGetHurtSoundInferiors;
	}

	public String[] getOverrideGetHurtSoundSuperiors()
	{
		return overrideGetHurtSoundSuperiors;
	}

	public String[] getOverrideGetHurtSoundInferiors()
	{
		return overrideGetHurtSoundInferiors;
	}

	public String[] getAfterGetHurtSoundSuperiors()
	{
		return afterGetHurtSoundSuperiors;
	}

	public String[] getAfterGetHurtSoundInferiors()
	{
		return afterGetHurtSoundInferiors;
	}

	public void setBeforeGetHurtSoundSuperiors(String[] value)
	{
		beforeGetHurtSoundSuperiors = value;
	}

	public void setBeforeGetHurtSoundInferiors(String[] value)
	{
		beforeGetHurtSoundInferiors = value;
	}

	public void setOverrideGetHurtSoundSuperiors(String[] value)
	{
		overrideGetHurtSoundSuperiors = value;
	}

	public void setOverrideGetHurtSoundInferiors(String[] value)
	{
		overrideGetHurtSoundInferiors = value;
	}

	public void setAfterGetHurtSoundSuperiors(String[] value)
	{
		afterGetHurtSoundSuperiors = value;
	}

	public void setAfterGetHurtSoundInferiors(String[] value)
	{
		afterGetHurtSoundInferiors = value;
	}

	public String[] getBeforeGetNameSuperiors()
	{
		return beforeGetNameSuperiors;
	}

	public String[] getBeforeGetNameInferiors()
	{
		return beforeGetNameInferiors;
	}

	public String[] getOverrideGetNameSuperiors()
	{
		return overrideGetNameSuperiors;
	}

	public String[] getOverrideGetNameInferiors()
	{
		return overrideGetNameInferiors;
	}

	public String[] getAfterGetNameSuperiors()
	{
		return afterGetNameSuperiors;
	}

	public String[] getAfterGetNameInferiors()
	{
		return afterGetNameInferiors;
	}

	public void setBeforeGetNameSuperiors(String[] value)
	{
		beforeGetNameSuperiors = value;
	}

	public void setBeforeGetNameInferiors(String[] value)
	{
		beforeGetNameInferiors = value;
	}

	public void setOverrideGetNameSuperiors(String[] value)
	{
		overrideGetNameSuperiors = value;
	}

	public void setOverrideGetNameInferiors(String[] value)
	{
		overrideGetNameInferiors = value;
	}

	public void setAfterGetNameSuperiors(String[] value)
	{
		afterGetNameSuperiors = value;
	}

	public void setAfterGetNameInferiors(String[] value)
	{
		afterGetNameInferiors = value;
	}

	public String[] getBeforeGetSleepTimerSuperiors()
	{
		return beforeGetSleepTimerSuperiors;
	}

	public String[] getBeforeGetSleepTimerInferiors()
	{
		return beforeGetSleepTimerInferiors;
	}

	public String[] getOverrideGetSleepTimerSuperiors()
	{
		return overrideGetSleepTimerSuperiors;
	}

	public String[] getOverrideGetSleepTimerInferiors()
	{
		return overrideGetSleepTimerInferiors;
	}

	public String[] getAfterGetSleepTimerSuperiors()
	{
		return afterGetSleepTimerSuperiors;
	}

	public String[] getAfterGetSleepTimerInferiors()
	{
		return afterGetSleepTimerInferiors;
	}

	public void setBeforeGetSleepTimerSuperiors(String[] value)
	{
		beforeGetSleepTimerSuperiors = value;
	}

	public void setBeforeGetSleepTimerInferiors(String[] value)
	{
		beforeGetSleepTimerInferiors = value;
	}

	public void setOverrideGetSleepTimerSuperiors(String[] value)
	{
		overrideGetSleepTimerSuperiors = value;
	}

	public void setOverrideGetSleepTimerInferiors(String[] value)
	{
		overrideGetSleepTimerInferiors = value;
	}

	public void setAfterGetSleepTimerSuperiors(String[] value)
	{
		afterGetSleepTimerSuperiors = value;
	}

	public void setAfterGetSleepTimerInferiors(String[] value)
	{
		afterGetSleepTimerInferiors = value;
	}

	public String[] getBeforeHandleWaterMovementSuperiors()
	{
		return beforeHandleWaterMovementSuperiors;
	}

	public String[] getBeforeHandleWaterMovementInferiors()
	{
		return beforeHandleWaterMovementInferiors;
	}

	public String[] getOverrideHandleWaterMovementSuperiors()
	{
		return overrideHandleWaterMovementSuperiors;
	}

	public String[] getOverrideHandleWaterMovementInferiors()
	{
		return overrideHandleWaterMovementInferiors;
	}

	public String[] getAfterHandleWaterMovementSuperiors()
	{
		return afterHandleWaterMovementSuperiors;
	}

	public String[] getAfterHandleWaterMovementInferiors()
	{
		return afterHandleWaterMovementInferiors;
	}

	public void setBeforeHandleWaterMovementSuperiors(String[] value)
	{
		beforeHandleWaterMovementSuperiors = value;
	}

	public void setBeforeHandleWaterMovementInferiors(String[] value)
	{
		beforeHandleWaterMovementInferiors = value;
	}

	public void setOverrideHandleWaterMovementSuperiors(String[] value)
	{
		overrideHandleWaterMovementSuperiors = value;
	}

	public void setOverrideHandleWaterMovementInferiors(String[] value)
	{
		overrideHandleWaterMovementInferiors = value;
	}

	public void setAfterHandleWaterMovementSuperiors(String[] value)
	{
		afterHandleWaterMovementSuperiors = value;
	}

	public void setAfterHandleWaterMovementInferiors(String[] value)
	{
		afterHandleWaterMovementInferiors = value;
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

	public String[] getBeforeIsSprintingSuperiors()
	{
		return beforeIsSprintingSuperiors;
	}

	public String[] getBeforeIsSprintingInferiors()
	{
		return beforeIsSprintingInferiors;
	}

	public String[] getOverrideIsSprintingSuperiors()
	{
		return overrideIsSprintingSuperiors;
	}

	public String[] getOverrideIsSprintingInferiors()
	{
		return overrideIsSprintingInferiors;
	}

	public String[] getAfterIsSprintingSuperiors()
	{
		return afterIsSprintingSuperiors;
	}

	public String[] getAfterIsSprintingInferiors()
	{
		return afterIsSprintingInferiors;
	}

	public void setBeforeIsSprintingSuperiors(String[] value)
	{
		beforeIsSprintingSuperiors = value;
	}

	public void setBeforeIsSprintingInferiors(String[] value)
	{
		beforeIsSprintingInferiors = value;
	}

	public void setOverrideIsSprintingSuperiors(String[] value)
	{
		overrideIsSprintingSuperiors = value;
	}

	public void setOverrideIsSprintingInferiors(String[] value)
	{
		overrideIsSprintingInferiors = value;
	}

	public void setAfterIsSprintingSuperiors(String[] value)
	{
		afterIsSprintingSuperiors = value;
	}

	public void setAfterIsSprintingInferiors(String[] value)
	{
		afterIsSprintingInferiors = value;
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

	public String[] getBeforePlayStepSoundSuperiors()
	{
		return beforePlayStepSoundSuperiors;
	}

	public String[] getBeforePlayStepSoundInferiors()
	{
		return beforePlayStepSoundInferiors;
	}

	public String[] getOverridePlayStepSoundSuperiors()
	{
		return overridePlayStepSoundSuperiors;
	}

	public String[] getOverridePlayStepSoundInferiors()
	{
		return overridePlayStepSoundInferiors;
	}

	public String[] getAfterPlayStepSoundSuperiors()
	{
		return afterPlayStepSoundSuperiors;
	}

	public String[] getAfterPlayStepSoundInferiors()
	{
		return afterPlayStepSoundInferiors;
	}

	public void setBeforePlayStepSoundSuperiors(String[] value)
	{
		beforePlayStepSoundSuperiors = value;
	}

	public void setBeforePlayStepSoundInferiors(String[] value)
	{
		beforePlayStepSoundInferiors = value;
	}

	public void setOverridePlayStepSoundSuperiors(String[] value)
	{
		overridePlayStepSoundSuperiors = value;
	}

	public void setOverridePlayStepSoundInferiors(String[] value)
	{
		overridePlayStepSoundInferiors = value;
	}

	public void setAfterPlayStepSoundSuperiors(String[] value)
	{
		afterPlayStepSoundSuperiors = value;
	}

	public void setAfterPlayStepSoundInferiors(String[] value)
	{
		afterPlayStepSoundInferiors = value;
	}

	public String[] getBeforePushOutOfBlocksSuperiors()
	{
		return beforePushOutOfBlocksSuperiors;
	}

	public String[] getBeforePushOutOfBlocksInferiors()
	{
		return beforePushOutOfBlocksInferiors;
	}

	public String[] getOverridePushOutOfBlocksSuperiors()
	{
		return overridePushOutOfBlocksSuperiors;
	}

	public String[] getOverridePushOutOfBlocksInferiors()
	{
		return overridePushOutOfBlocksInferiors;
	}

	public String[] getAfterPushOutOfBlocksSuperiors()
	{
		return afterPushOutOfBlocksSuperiors;
	}

	public String[] getAfterPushOutOfBlocksInferiors()
	{
		return afterPushOutOfBlocksInferiors;
	}

	public void setBeforePushOutOfBlocksSuperiors(String[] value)
	{
		beforePushOutOfBlocksSuperiors = value;
	}

	public void setBeforePushOutOfBlocksInferiors(String[] value)
	{
		beforePushOutOfBlocksInferiors = value;
	}

	public void setOverridePushOutOfBlocksSuperiors(String[] value)
	{
		overridePushOutOfBlocksSuperiors = value;
	}

	public void setOverridePushOutOfBlocksInferiors(String[] value)
	{
		overridePushOutOfBlocksInferiors = value;
	}

	public void setAfterPushOutOfBlocksSuperiors(String[] value)
	{
		afterPushOutOfBlocksSuperiors = value;
	}

	public void setAfterPushOutOfBlocksInferiors(String[] value)
	{
		afterPushOutOfBlocksInferiors = value;
	}

	public String[] getBeforeRayTraceSuperiors()
	{
		return beforeRayTraceSuperiors;
	}

	public String[] getBeforeRayTraceInferiors()
	{
		return beforeRayTraceInferiors;
	}

	public String[] getOverrideRayTraceSuperiors()
	{
		return overrideRayTraceSuperiors;
	}

	public String[] getOverrideRayTraceInferiors()
	{
		return overrideRayTraceInferiors;
	}

	public String[] getAfterRayTraceSuperiors()
	{
		return afterRayTraceSuperiors;
	}

	public String[] getAfterRayTraceInferiors()
	{
		return afterRayTraceInferiors;
	}

	public void setBeforeRayTraceSuperiors(String[] value)
	{
		beforeRayTraceSuperiors = value;
	}

	public void setBeforeRayTraceInferiors(String[] value)
	{
		beforeRayTraceInferiors = value;
	}

	public void setOverrideRayTraceSuperiors(String[] value)
	{
		overrideRayTraceSuperiors = value;
	}

	public void setOverrideRayTraceInferiors(String[] value)
	{
		overrideRayTraceInferiors = value;
	}

	public void setAfterRayTraceSuperiors(String[] value)
	{
		afterRayTraceSuperiors = value;
	}

	public void setAfterRayTraceInferiors(String[] value)
	{
		afterRayTraceInferiors = value;
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

	public String[] getBeforeRespawnPlayerSuperiors()
	{
		return beforeRespawnPlayerSuperiors;
	}

	public String[] getBeforeRespawnPlayerInferiors()
	{
		return beforeRespawnPlayerInferiors;
	}

	public String[] getOverrideRespawnPlayerSuperiors()
	{
		return overrideRespawnPlayerSuperiors;
	}

	public String[] getOverrideRespawnPlayerInferiors()
	{
		return overrideRespawnPlayerInferiors;
	}

	public String[] getAfterRespawnPlayerSuperiors()
	{
		return afterRespawnPlayerSuperiors;
	}

	public String[] getAfterRespawnPlayerInferiors()
	{
		return afterRespawnPlayerInferiors;
	}

	public void setBeforeRespawnPlayerSuperiors(String[] value)
	{
		beforeRespawnPlayerSuperiors = value;
	}

	public void setBeforeRespawnPlayerInferiors(String[] value)
	{
		beforeRespawnPlayerInferiors = value;
	}

	public void setOverrideRespawnPlayerSuperiors(String[] value)
	{
		overrideRespawnPlayerSuperiors = value;
	}

	public void setOverrideRespawnPlayerInferiors(String[] value)
	{
		overrideRespawnPlayerInferiors = value;
	}

	public void setAfterRespawnPlayerSuperiors(String[] value)
	{
		afterRespawnPlayerSuperiors = value;
	}

	public void setAfterRespawnPlayerInferiors(String[] value)
	{
		afterRespawnPlayerInferiors = value;
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

	public String[] getBeforeSetPlayerSPHealthSuperiors()
	{
		return beforeSetPlayerSPHealthSuperiors;
	}

	public String[] getBeforeSetPlayerSPHealthInferiors()
	{
		return beforeSetPlayerSPHealthInferiors;
	}

	public String[] getOverrideSetPlayerSPHealthSuperiors()
	{
		return overrideSetPlayerSPHealthSuperiors;
	}

	public String[] getOverrideSetPlayerSPHealthInferiors()
	{
		return overrideSetPlayerSPHealthInferiors;
	}

	public String[] getAfterSetPlayerSPHealthSuperiors()
	{
		return afterSetPlayerSPHealthSuperiors;
	}

	public String[] getAfterSetPlayerSPHealthInferiors()
	{
		return afterSetPlayerSPHealthInferiors;
	}

	public void setBeforeSetPlayerSPHealthSuperiors(String[] value)
	{
		beforeSetPlayerSPHealthSuperiors = value;
	}

	public void setBeforeSetPlayerSPHealthInferiors(String[] value)
	{
		beforeSetPlayerSPHealthInferiors = value;
	}

	public void setOverrideSetPlayerSPHealthSuperiors(String[] value)
	{
		overrideSetPlayerSPHealthSuperiors = value;
	}

	public void setOverrideSetPlayerSPHealthInferiors(String[] value)
	{
		overrideSetPlayerSPHealthInferiors = value;
	}

	public void setAfterSetPlayerSPHealthSuperiors(String[] value)
	{
		afterSetPlayerSPHealthSuperiors = value;
	}

	public void setAfterSetPlayerSPHealthInferiors(String[] value)
	{
		afterSetPlayerSPHealthInferiors = value;
	}

	public String[] getBeforeSetPositionAndRotationSuperiors()
	{
		return beforeSetPositionAndRotationSuperiors;
	}

	public String[] getBeforeSetPositionAndRotationInferiors()
	{
		return beforeSetPositionAndRotationInferiors;
	}

	public String[] getOverrideSetPositionAndRotationSuperiors()
	{
		return overrideSetPositionAndRotationSuperiors;
	}

	public String[] getOverrideSetPositionAndRotationInferiors()
	{
		return overrideSetPositionAndRotationInferiors;
	}

	public String[] getAfterSetPositionAndRotationSuperiors()
	{
		return afterSetPositionAndRotationSuperiors;
	}

	public String[] getAfterSetPositionAndRotationInferiors()
	{
		return afterSetPositionAndRotationInferiors;
	}

	public void setBeforeSetPositionAndRotationSuperiors(String[] value)
	{
		beforeSetPositionAndRotationSuperiors = value;
	}

	public void setBeforeSetPositionAndRotationInferiors(String[] value)
	{
		beforeSetPositionAndRotationInferiors = value;
	}

	public void setOverrideSetPositionAndRotationSuperiors(String[] value)
	{
		overrideSetPositionAndRotationSuperiors = value;
	}

	public void setOverrideSetPositionAndRotationInferiors(String[] value)
	{
		overrideSetPositionAndRotationInferiors = value;
	}

	public void setAfterSetPositionAndRotationSuperiors(String[] value)
	{
		afterSetPositionAndRotationSuperiors = value;
	}

	public void setAfterSetPositionAndRotationInferiors(String[] value)
	{
		afterSetPositionAndRotationInferiors = value;
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

	public String[] getBeforeTrySleepSuperiors()
	{
		return beforeTrySleepSuperiors;
	}

	public String[] getBeforeTrySleepInferiors()
	{
		return beforeTrySleepInferiors;
	}

	public String[] getOverrideTrySleepSuperiors()
	{
		return overrideTrySleepSuperiors;
	}

	public String[] getOverrideTrySleepInferiors()
	{
		return overrideTrySleepInferiors;
	}

	public String[] getAfterTrySleepSuperiors()
	{
		return afterTrySleepSuperiors;
	}

	public String[] getAfterTrySleepInferiors()
	{
		return afterTrySleepInferiors;
	}

	public void setBeforeTrySleepSuperiors(String[] value)
	{
		beforeTrySleepSuperiors = value;
	}

	public void setBeforeTrySleepInferiors(String[] value)
	{
		beforeTrySleepInferiors = value;
	}

	public void setOverrideTrySleepSuperiors(String[] value)
	{
		overrideTrySleepSuperiors = value;
	}

	public void setOverrideTrySleepInferiors(String[] value)
	{
		overrideTrySleepInferiors = value;
	}

	public void setAfterTrySleepSuperiors(String[] value)
	{
		afterTrySleepSuperiors = value;
	}

	public void setAfterTrySleepInferiors(String[] value)
	{
		afterTrySleepInferiors = value;
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
