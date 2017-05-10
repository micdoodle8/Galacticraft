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

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.lang.reflect.*;

public final class ServerPlayerAPI
{
	private final static Class<?>[] Class = new Class[] { ServerPlayerAPI.class };
	private final static Class<?>[] Classes = new Class[] { ServerPlayerAPI.class, String.class };

	private static boolean isCreated;
	private static final Logger logger = Logger.getLogger("ServerPlayerAPI");

	private static void log(String text)
	{
		System.out.println(text);
		logger.fine(text);
	}

	public static void register(String id, Class<?> baseClass)
	{
		register(id, baseClass, null);
	}

	public static void register(String id, Class<?> baseClass, ServerPlayerBaseSorting baseSorting)
	{
		try
		{
			register(baseClass, id, baseSorting);
		}
		catch(RuntimeException exception)
		{
			if(id != null)
				log("Server Player: failed to register id '" + id + "'");
			else
				log("Server Player: failed to register ServerPlayerBase");

			throw exception;
		}
	}

	private static void register(Class<?> baseClass, String id, ServerPlayerBaseSorting baseSorting)
	{
		if(!isCreated)
		{
			try
			{
				Method mandatory = net.minecraft.entity.player.EntityPlayerMP.class.getMethod("getServerPlayerBase", String.class);
				if (mandatory.getReturnType() != ServerPlayerBase.class)
					throw new NoSuchMethodException(ServerPlayerBase.class.getName() + " " + net.minecraft.entity.player.EntityPlayerMP.class.getName() + ".getServerPlayerBase(" + String.class.getName() + ")");
			}
			catch(NoSuchMethodException exception)
			{
				String[] errorMessageParts = new String[]
				{
					"========================================",
					"The API \"Server Player\" version " + api.player.forge.PlayerAPIPlugin.Version + " of the mod \"Player API Core " + api.player.forge.PlayerAPIPlugin.Version + "\" can not be created!",
					"----------------------------------------",
					"Mandatory member method \"{0} getServerPlayerBase({3})\" not found in class \"{1}\".",
					"There are three scenarios this can happen:",
					"* Minecraft Forge is missing a Player API Core which Minecraft version matches its own.",
					"  Download and install the latest Player API Core for the Minecraft version you were trying to run.",
					"* The code of the class \"{2}\" of Player API Core has been modified beyond recognition by another Minecraft Forge coremod.",
					"  Try temporary deinstallation of other core mods to find the culprit and deinstall it permanently to fix this specific problem.",
					"* Player API Core has not been installed correctly.",
					"  Deinstall Player API Core and install it again following the installation instructions in the readme file.",
					"========================================"
				};

				String baseEntityPlayerMPClassName = ServerPlayerBase.class.getName();
				String targetClassName = net.minecraft.entity.player.EntityPlayerMP.class.getName();
				String targetClassFileName = targetClassName.replace(".", File.separator);
				String stringClassName = String.class.getName();

				for(int i=0; i<errorMessageParts.length; i++)
					errorMessageParts[i] = MessageFormat.format(errorMessageParts[i], baseEntityPlayerMPClassName, targetClassName, targetClassFileName, stringClassName);

				for(String errorMessagePart : errorMessageParts)
					logger.severe(errorMessagePart);

				for(String errorMessagePart : errorMessageParts)
					System.err.println(errorMessagePart);

				String errorMessage = "\n\n";
				for(String errorMessagePart : errorMessageParts)
					errorMessage += "\t" + errorMessagePart + "\n";

				throw new RuntimeException(errorMessage, exception);
			}

			log("Server Player " + api.player.forge.PlayerAPIPlugin.Version + " Created");
			isCreated = true;
		}

		if(id == null)
			throw new NullPointerException("Argument 'id' can not be null");
		if(baseClass == null)
			throw new NullPointerException("Argument 'baseClass' can not be null");

		Constructor<?> allreadyRegistered = allBaseConstructors.get(id);
		if(allreadyRegistered != null)
			throw new IllegalArgumentException("The class '" + baseClass.getName() + "' can not be registered with the id '" + id + "' because the class '" + allreadyRegistered.getDeclaringClass().getName() + "' has allready been registered with the same id");

		Constructor<?> baseConstructor;
		try
		{
			baseConstructor = baseClass.getDeclaredConstructor(Classes);
		}
		catch (Throwable t)
		{
			try
			{
				baseConstructor = baseClass.getDeclaredConstructor(Class);
			}
			catch(Throwable s)
			{
				throw new IllegalArgumentException("Can not find necessary constructor with one argument of type '" + ServerPlayerAPI.class.getName() + "' and eventually a second argument of type 'String' in the class '" + baseClass.getName() + "'", t);
			}
		}

		allBaseConstructors.put(id, baseConstructor);

		if(baseSorting != null)
		{
			addSorting(id, allBaseBeforeLocalConstructingSuperiors, baseSorting.getBeforeLocalConstructingSuperiors());
			addSorting(id, allBaseBeforeLocalConstructingInferiors, baseSorting.getBeforeLocalConstructingInferiors());
			addSorting(id, allBaseAfterLocalConstructingSuperiors, baseSorting.getAfterLocalConstructingSuperiors());
			addSorting(id, allBaseAfterLocalConstructingInferiors, baseSorting.getAfterLocalConstructingInferiors());

			addDynamicSorting(id, allBaseBeforeDynamicSuperiors, baseSorting.getDynamicBeforeSuperiors());
			addDynamicSorting(id, allBaseBeforeDynamicInferiors, baseSorting.getDynamicBeforeInferiors());
			addDynamicSorting(id, allBaseOverrideDynamicSuperiors, baseSorting.getDynamicOverrideSuperiors());
			addDynamicSorting(id, allBaseOverrideDynamicInferiors, baseSorting.getDynamicOverrideInferiors());
			addDynamicSorting(id, allBaseAfterDynamicSuperiors, baseSorting.getDynamicAfterSuperiors());
			addDynamicSorting(id, allBaseAfterDynamicInferiors, baseSorting.getDynamicAfterInferiors());

			addSorting(id, allBaseBeforeAddExhaustionSuperiors, baseSorting.getBeforeAddExhaustionSuperiors());
			addSorting(id, allBaseBeforeAddExhaustionInferiors, baseSorting.getBeforeAddExhaustionInferiors());
			addSorting(id, allBaseOverrideAddExhaustionSuperiors, baseSorting.getOverrideAddExhaustionSuperiors());
			addSorting(id, allBaseOverrideAddExhaustionInferiors, baseSorting.getOverrideAddExhaustionInferiors());
			addSorting(id, allBaseAfterAddExhaustionSuperiors, baseSorting.getAfterAddExhaustionSuperiors());
			addSorting(id, allBaseAfterAddExhaustionInferiors, baseSorting.getAfterAddExhaustionInferiors());

			addSorting(id, allBaseBeforeAddExperienceSuperiors, baseSorting.getBeforeAddExperienceSuperiors());
			addSorting(id, allBaseBeforeAddExperienceInferiors, baseSorting.getBeforeAddExperienceInferiors());
			addSorting(id, allBaseOverrideAddExperienceSuperiors, baseSorting.getOverrideAddExperienceSuperiors());
			addSorting(id, allBaseOverrideAddExperienceInferiors, baseSorting.getOverrideAddExperienceInferiors());
			addSorting(id, allBaseAfterAddExperienceSuperiors, baseSorting.getAfterAddExperienceSuperiors());
			addSorting(id, allBaseAfterAddExperienceInferiors, baseSorting.getAfterAddExperienceInferiors());

			addSorting(id, allBaseBeforeAddExperienceLevelSuperiors, baseSorting.getBeforeAddExperienceLevelSuperiors());
			addSorting(id, allBaseBeforeAddExperienceLevelInferiors, baseSorting.getBeforeAddExperienceLevelInferiors());
			addSorting(id, allBaseOverrideAddExperienceLevelSuperiors, baseSorting.getOverrideAddExperienceLevelSuperiors());
			addSorting(id, allBaseOverrideAddExperienceLevelInferiors, baseSorting.getOverrideAddExperienceLevelInferiors());
			addSorting(id, allBaseAfterAddExperienceLevelSuperiors, baseSorting.getAfterAddExperienceLevelSuperiors());
			addSorting(id, allBaseAfterAddExperienceLevelInferiors, baseSorting.getAfterAddExperienceLevelInferiors());

			addSorting(id, allBaseBeforeAddMovementStatSuperiors, baseSorting.getBeforeAddMovementStatSuperiors());
			addSorting(id, allBaseBeforeAddMovementStatInferiors, baseSorting.getBeforeAddMovementStatInferiors());
			addSorting(id, allBaseOverrideAddMovementStatSuperiors, baseSorting.getOverrideAddMovementStatSuperiors());
			addSorting(id, allBaseOverrideAddMovementStatInferiors, baseSorting.getOverrideAddMovementStatInferiors());
			addSorting(id, allBaseAfterAddMovementStatSuperiors, baseSorting.getAfterAddMovementStatSuperiors());
			addSorting(id, allBaseAfterAddMovementStatInferiors, baseSorting.getAfterAddMovementStatInferiors());

			addSorting(id, allBaseBeforeAttackEntityFromSuperiors, baseSorting.getBeforeAttackEntityFromSuperiors());
			addSorting(id, allBaseBeforeAttackEntityFromInferiors, baseSorting.getBeforeAttackEntityFromInferiors());
			addSorting(id, allBaseOverrideAttackEntityFromSuperiors, baseSorting.getOverrideAttackEntityFromSuperiors());
			addSorting(id, allBaseOverrideAttackEntityFromInferiors, baseSorting.getOverrideAttackEntityFromInferiors());
			addSorting(id, allBaseAfterAttackEntityFromSuperiors, baseSorting.getAfterAttackEntityFromSuperiors());
			addSorting(id, allBaseAfterAttackEntityFromInferiors, baseSorting.getAfterAttackEntityFromInferiors());

			addSorting(id, allBaseBeforeAttackTargetEntityWithCurrentItemSuperiors, baseSorting.getBeforeAttackTargetEntityWithCurrentItemSuperiors());
			addSorting(id, allBaseBeforeAttackTargetEntityWithCurrentItemInferiors, baseSorting.getBeforeAttackTargetEntityWithCurrentItemInferiors());
			addSorting(id, allBaseOverrideAttackTargetEntityWithCurrentItemSuperiors, baseSorting.getOverrideAttackTargetEntityWithCurrentItemSuperiors());
			addSorting(id, allBaseOverrideAttackTargetEntityWithCurrentItemInferiors, baseSorting.getOverrideAttackTargetEntityWithCurrentItemInferiors());
			addSorting(id, allBaseAfterAttackTargetEntityWithCurrentItemSuperiors, baseSorting.getAfterAttackTargetEntityWithCurrentItemSuperiors());
			addSorting(id, allBaseAfterAttackTargetEntityWithCurrentItemInferiors, baseSorting.getAfterAttackTargetEntityWithCurrentItemInferiors());

			addSorting(id, allBaseBeforeCanBreatheUnderwaterSuperiors, baseSorting.getBeforeCanBreatheUnderwaterSuperiors());
			addSorting(id, allBaseBeforeCanBreatheUnderwaterInferiors, baseSorting.getBeforeCanBreatheUnderwaterInferiors());
			addSorting(id, allBaseOverrideCanBreatheUnderwaterSuperiors, baseSorting.getOverrideCanBreatheUnderwaterSuperiors());
			addSorting(id, allBaseOverrideCanBreatheUnderwaterInferiors, baseSorting.getOverrideCanBreatheUnderwaterInferiors());
			addSorting(id, allBaseAfterCanBreatheUnderwaterSuperiors, baseSorting.getAfterCanBreatheUnderwaterSuperiors());
			addSorting(id, allBaseAfterCanBreatheUnderwaterInferiors, baseSorting.getAfterCanBreatheUnderwaterInferiors());

			addSorting(id, allBaseBeforeCanHarvestBlockSuperiors, baseSorting.getBeforeCanHarvestBlockSuperiors());
			addSorting(id, allBaseBeforeCanHarvestBlockInferiors, baseSorting.getBeforeCanHarvestBlockInferiors());
			addSorting(id, allBaseOverrideCanHarvestBlockSuperiors, baseSorting.getOverrideCanHarvestBlockSuperiors());
			addSorting(id, allBaseOverrideCanHarvestBlockInferiors, baseSorting.getOverrideCanHarvestBlockInferiors());
			addSorting(id, allBaseAfterCanHarvestBlockSuperiors, baseSorting.getAfterCanHarvestBlockSuperiors());
			addSorting(id, allBaseAfterCanHarvestBlockInferiors, baseSorting.getAfterCanHarvestBlockInferiors());

			addSorting(id, allBaseBeforeCanPlayerEditSuperiors, baseSorting.getBeforeCanPlayerEditSuperiors());
			addSorting(id, allBaseBeforeCanPlayerEditInferiors, baseSorting.getBeforeCanPlayerEditInferiors());
			addSorting(id, allBaseOverrideCanPlayerEditSuperiors, baseSorting.getOverrideCanPlayerEditSuperiors());
			addSorting(id, allBaseOverrideCanPlayerEditInferiors, baseSorting.getOverrideCanPlayerEditInferiors());
			addSorting(id, allBaseAfterCanPlayerEditSuperiors, baseSorting.getAfterCanPlayerEditSuperiors());
			addSorting(id, allBaseAfterCanPlayerEditInferiors, baseSorting.getAfterCanPlayerEditInferiors());

			addSorting(id, allBaseBeforeCanTriggerWalkingSuperiors, baseSorting.getBeforeCanTriggerWalkingSuperiors());
			addSorting(id, allBaseBeforeCanTriggerWalkingInferiors, baseSorting.getBeforeCanTriggerWalkingInferiors());
			addSorting(id, allBaseOverrideCanTriggerWalkingSuperiors, baseSorting.getOverrideCanTriggerWalkingSuperiors());
			addSorting(id, allBaseOverrideCanTriggerWalkingInferiors, baseSorting.getOverrideCanTriggerWalkingInferiors());
			addSorting(id, allBaseAfterCanTriggerWalkingSuperiors, baseSorting.getAfterCanTriggerWalkingSuperiors());
			addSorting(id, allBaseAfterCanTriggerWalkingInferiors, baseSorting.getAfterCanTriggerWalkingInferiors());

			addSorting(id, allBaseBeforeClonePlayerSuperiors, baseSorting.getBeforeClonePlayerSuperiors());
			addSorting(id, allBaseBeforeClonePlayerInferiors, baseSorting.getBeforeClonePlayerInferiors());
			addSorting(id, allBaseOverrideClonePlayerSuperiors, baseSorting.getOverrideClonePlayerSuperiors());
			addSorting(id, allBaseOverrideClonePlayerInferiors, baseSorting.getOverrideClonePlayerInferiors());
			addSorting(id, allBaseAfterClonePlayerSuperiors, baseSorting.getAfterClonePlayerSuperiors());
			addSorting(id, allBaseAfterClonePlayerInferiors, baseSorting.getAfterClonePlayerInferiors());

			addSorting(id, allBaseBeforeDamageEntitySuperiors, baseSorting.getBeforeDamageEntitySuperiors());
			addSorting(id, allBaseBeforeDamageEntityInferiors, baseSorting.getBeforeDamageEntityInferiors());
			addSorting(id, allBaseOverrideDamageEntitySuperiors, baseSorting.getOverrideDamageEntitySuperiors());
			addSorting(id, allBaseOverrideDamageEntityInferiors, baseSorting.getOverrideDamageEntityInferiors());
			addSorting(id, allBaseAfterDamageEntitySuperiors, baseSorting.getAfterDamageEntitySuperiors());
			addSorting(id, allBaseAfterDamageEntityInferiors, baseSorting.getAfterDamageEntityInferiors());

			addSorting(id, allBaseBeforeDisplayGUIChestSuperiors, baseSorting.getBeforeDisplayGUIChestSuperiors());
			addSorting(id, allBaseBeforeDisplayGUIChestInferiors, baseSorting.getBeforeDisplayGUIChestInferiors());
			addSorting(id, allBaseOverrideDisplayGUIChestSuperiors, baseSorting.getOverrideDisplayGUIChestSuperiors());
			addSorting(id, allBaseOverrideDisplayGUIChestInferiors, baseSorting.getOverrideDisplayGUIChestInferiors());
			addSorting(id, allBaseAfterDisplayGUIChestSuperiors, baseSorting.getAfterDisplayGUIChestSuperiors());
			addSorting(id, allBaseAfterDisplayGUIChestInferiors, baseSorting.getAfterDisplayGUIChestInferiors());

			addSorting(id, allBaseBeforeDisplayGUIDispenserSuperiors, baseSorting.getBeforeDisplayGUIDispenserSuperiors());
			addSorting(id, allBaseBeforeDisplayGUIDispenserInferiors, baseSorting.getBeforeDisplayGUIDispenserInferiors());
			addSorting(id, allBaseOverrideDisplayGUIDispenserSuperiors, baseSorting.getOverrideDisplayGUIDispenserSuperiors());
			addSorting(id, allBaseOverrideDisplayGUIDispenserInferiors, baseSorting.getOverrideDisplayGUIDispenserInferiors());
			addSorting(id, allBaseAfterDisplayGUIDispenserSuperiors, baseSorting.getAfterDisplayGUIDispenserSuperiors());
			addSorting(id, allBaseAfterDisplayGUIDispenserInferiors, baseSorting.getAfterDisplayGUIDispenserInferiors());

			addSorting(id, allBaseBeforeDisplayGUIFurnaceSuperiors, baseSorting.getBeforeDisplayGUIFurnaceSuperiors());
			addSorting(id, allBaseBeforeDisplayGUIFurnaceInferiors, baseSorting.getBeforeDisplayGUIFurnaceInferiors());
			addSorting(id, allBaseOverrideDisplayGUIFurnaceSuperiors, baseSorting.getOverrideDisplayGUIFurnaceSuperiors());
			addSorting(id, allBaseOverrideDisplayGUIFurnaceInferiors, baseSorting.getOverrideDisplayGUIFurnaceInferiors());
			addSorting(id, allBaseAfterDisplayGUIFurnaceSuperiors, baseSorting.getAfterDisplayGUIFurnaceSuperiors());
			addSorting(id, allBaseAfterDisplayGUIFurnaceInferiors, baseSorting.getAfterDisplayGUIFurnaceInferiors());

			addSorting(id, allBaseBeforeDisplayGUIWorkbenchSuperiors, baseSorting.getBeforeDisplayGUIWorkbenchSuperiors());
			addSorting(id, allBaseBeforeDisplayGUIWorkbenchInferiors, baseSorting.getBeforeDisplayGUIWorkbenchInferiors());
			addSorting(id, allBaseOverrideDisplayGUIWorkbenchSuperiors, baseSorting.getOverrideDisplayGUIWorkbenchSuperiors());
			addSorting(id, allBaseOverrideDisplayGUIWorkbenchInferiors, baseSorting.getOverrideDisplayGUIWorkbenchInferiors());
			addSorting(id, allBaseAfterDisplayGUIWorkbenchSuperiors, baseSorting.getAfterDisplayGUIWorkbenchSuperiors());
			addSorting(id, allBaseAfterDisplayGUIWorkbenchInferiors, baseSorting.getAfterDisplayGUIWorkbenchInferiors());

			addSorting(id, allBaseBeforeDropOneItemSuperiors, baseSorting.getBeforeDropOneItemSuperiors());
			addSorting(id, allBaseBeforeDropOneItemInferiors, baseSorting.getBeforeDropOneItemInferiors());
			addSorting(id, allBaseOverrideDropOneItemSuperiors, baseSorting.getOverrideDropOneItemSuperiors());
			addSorting(id, allBaseOverrideDropOneItemInferiors, baseSorting.getOverrideDropOneItemInferiors());
			addSorting(id, allBaseAfterDropOneItemSuperiors, baseSorting.getAfterDropOneItemSuperiors());
			addSorting(id, allBaseAfterDropOneItemInferiors, baseSorting.getAfterDropOneItemInferiors());

			addSorting(id, allBaseBeforeDropPlayerItemSuperiors, baseSorting.getBeforeDropPlayerItemSuperiors());
			addSorting(id, allBaseBeforeDropPlayerItemInferiors, baseSorting.getBeforeDropPlayerItemInferiors());
			addSorting(id, allBaseOverrideDropPlayerItemSuperiors, baseSorting.getOverrideDropPlayerItemSuperiors());
			addSorting(id, allBaseOverrideDropPlayerItemInferiors, baseSorting.getOverrideDropPlayerItemInferiors());
			addSorting(id, allBaseAfterDropPlayerItemSuperiors, baseSorting.getAfterDropPlayerItemSuperiors());
			addSorting(id, allBaseAfterDropPlayerItemInferiors, baseSorting.getAfterDropPlayerItemInferiors());

			addSorting(id, allBaseBeforeFallSuperiors, baseSorting.getBeforeFallSuperiors());
			addSorting(id, allBaseBeforeFallInferiors, baseSorting.getBeforeFallInferiors());
			addSorting(id, allBaseOverrideFallSuperiors, baseSorting.getOverrideFallSuperiors());
			addSorting(id, allBaseOverrideFallInferiors, baseSorting.getOverrideFallInferiors());
			addSorting(id, allBaseAfterFallSuperiors, baseSorting.getAfterFallSuperiors());
			addSorting(id, allBaseAfterFallInferiors, baseSorting.getAfterFallInferiors());

			addSorting(id, allBaseBeforeGetAIMoveSpeedSuperiors, baseSorting.getBeforeGetAIMoveSpeedSuperiors());
			addSorting(id, allBaseBeforeGetAIMoveSpeedInferiors, baseSorting.getBeforeGetAIMoveSpeedInferiors());
			addSorting(id, allBaseOverrideGetAIMoveSpeedSuperiors, baseSorting.getOverrideGetAIMoveSpeedSuperiors());
			addSorting(id, allBaseOverrideGetAIMoveSpeedInferiors, baseSorting.getOverrideGetAIMoveSpeedInferiors());
			addSorting(id, allBaseAfterGetAIMoveSpeedSuperiors, baseSorting.getAfterGetAIMoveSpeedSuperiors());
			addSorting(id, allBaseAfterGetAIMoveSpeedInferiors, baseSorting.getAfterGetAIMoveSpeedInferiors());

			addSorting(id, allBaseBeforeGetCurrentPlayerStrVsBlockSuperiors, baseSorting.getBeforeGetCurrentPlayerStrVsBlockSuperiors());
			addSorting(id, allBaseBeforeGetCurrentPlayerStrVsBlockInferiors, baseSorting.getBeforeGetCurrentPlayerStrVsBlockInferiors());
			addSorting(id, allBaseOverrideGetCurrentPlayerStrVsBlockSuperiors, baseSorting.getOverrideGetCurrentPlayerStrVsBlockSuperiors());
			addSorting(id, allBaseOverrideGetCurrentPlayerStrVsBlockInferiors, baseSorting.getOverrideGetCurrentPlayerStrVsBlockInferiors());
			addSorting(id, allBaseAfterGetCurrentPlayerStrVsBlockSuperiors, baseSorting.getAfterGetCurrentPlayerStrVsBlockSuperiors());
			addSorting(id, allBaseAfterGetCurrentPlayerStrVsBlockInferiors, baseSorting.getAfterGetCurrentPlayerStrVsBlockInferiors());

			addSorting(id, allBaseBeforeGetCurrentPlayerStrVsBlockForgeSuperiors, baseSorting.getBeforeGetCurrentPlayerStrVsBlockForgeSuperiors());
			addSorting(id, allBaseBeforeGetCurrentPlayerStrVsBlockForgeInferiors, baseSorting.getBeforeGetCurrentPlayerStrVsBlockForgeInferiors());
			addSorting(id, allBaseOverrideGetCurrentPlayerStrVsBlockForgeSuperiors, baseSorting.getOverrideGetCurrentPlayerStrVsBlockForgeSuperiors());
			addSorting(id, allBaseOverrideGetCurrentPlayerStrVsBlockForgeInferiors, baseSorting.getOverrideGetCurrentPlayerStrVsBlockForgeInferiors());
			addSorting(id, allBaseAfterGetCurrentPlayerStrVsBlockForgeSuperiors, baseSorting.getAfterGetCurrentPlayerStrVsBlockForgeSuperiors());
			addSorting(id, allBaseAfterGetCurrentPlayerStrVsBlockForgeInferiors, baseSorting.getAfterGetCurrentPlayerStrVsBlockForgeInferiors());

			addSorting(id, allBaseBeforeGetDistanceSqSuperiors, baseSorting.getBeforeGetDistanceSqSuperiors());
			addSorting(id, allBaseBeforeGetDistanceSqInferiors, baseSorting.getBeforeGetDistanceSqInferiors());
			addSorting(id, allBaseOverrideGetDistanceSqSuperiors, baseSorting.getOverrideGetDistanceSqSuperiors());
			addSorting(id, allBaseOverrideGetDistanceSqInferiors, baseSorting.getOverrideGetDistanceSqInferiors());
			addSorting(id, allBaseAfterGetDistanceSqSuperiors, baseSorting.getAfterGetDistanceSqSuperiors());
			addSorting(id, allBaseAfterGetDistanceSqInferiors, baseSorting.getAfterGetDistanceSqInferiors());

			addSorting(id, allBaseBeforeGetBrightnessSuperiors, baseSorting.getBeforeGetBrightnessSuperiors());
			addSorting(id, allBaseBeforeGetBrightnessInferiors, baseSorting.getBeforeGetBrightnessInferiors());
			addSorting(id, allBaseOverrideGetBrightnessSuperiors, baseSorting.getOverrideGetBrightnessSuperiors());
			addSorting(id, allBaseOverrideGetBrightnessInferiors, baseSorting.getOverrideGetBrightnessInferiors());
			addSorting(id, allBaseAfterGetBrightnessSuperiors, baseSorting.getAfterGetBrightnessSuperiors());
			addSorting(id, allBaseAfterGetBrightnessInferiors, baseSorting.getAfterGetBrightnessInferiors());

			addSorting(id, allBaseBeforeGetEyeHeightSuperiors, baseSorting.getBeforeGetEyeHeightSuperiors());
			addSorting(id, allBaseBeforeGetEyeHeightInferiors, baseSorting.getBeforeGetEyeHeightInferiors());
			addSorting(id, allBaseOverrideGetEyeHeightSuperiors, baseSorting.getOverrideGetEyeHeightSuperiors());
			addSorting(id, allBaseOverrideGetEyeHeightInferiors, baseSorting.getOverrideGetEyeHeightInferiors());
			addSorting(id, allBaseAfterGetEyeHeightSuperiors, baseSorting.getAfterGetEyeHeightSuperiors());
			addSorting(id, allBaseAfterGetEyeHeightInferiors, baseSorting.getAfterGetEyeHeightInferiors());

			addSorting(id, allBaseBeforeHealSuperiors, baseSorting.getBeforeHealSuperiors());
			addSorting(id, allBaseBeforeHealInferiors, baseSorting.getBeforeHealInferiors());
			addSorting(id, allBaseOverrideHealSuperiors, baseSorting.getOverrideHealSuperiors());
			addSorting(id, allBaseOverrideHealInferiors, baseSorting.getOverrideHealInferiors());
			addSorting(id, allBaseAfterHealSuperiors, baseSorting.getAfterHealSuperiors());
			addSorting(id, allBaseAfterHealInferiors, baseSorting.getAfterHealInferiors());

			addSorting(id, allBaseBeforeIsEntityInsideOpaqueBlockSuperiors, baseSorting.getBeforeIsEntityInsideOpaqueBlockSuperiors());
			addSorting(id, allBaseBeforeIsEntityInsideOpaqueBlockInferiors, baseSorting.getBeforeIsEntityInsideOpaqueBlockInferiors());
			addSorting(id, allBaseOverrideIsEntityInsideOpaqueBlockSuperiors, baseSorting.getOverrideIsEntityInsideOpaqueBlockSuperiors());
			addSorting(id, allBaseOverrideIsEntityInsideOpaqueBlockInferiors, baseSorting.getOverrideIsEntityInsideOpaqueBlockInferiors());
			addSorting(id, allBaseAfterIsEntityInsideOpaqueBlockSuperiors, baseSorting.getAfterIsEntityInsideOpaqueBlockSuperiors());
			addSorting(id, allBaseAfterIsEntityInsideOpaqueBlockInferiors, baseSorting.getAfterIsEntityInsideOpaqueBlockInferiors());

			addSorting(id, allBaseBeforeIsInWaterSuperiors, baseSorting.getBeforeIsInWaterSuperiors());
			addSorting(id, allBaseBeforeIsInWaterInferiors, baseSorting.getBeforeIsInWaterInferiors());
			addSorting(id, allBaseOverrideIsInWaterSuperiors, baseSorting.getOverrideIsInWaterSuperiors());
			addSorting(id, allBaseOverrideIsInWaterInferiors, baseSorting.getOverrideIsInWaterInferiors());
			addSorting(id, allBaseAfterIsInWaterSuperiors, baseSorting.getAfterIsInWaterSuperiors());
			addSorting(id, allBaseAfterIsInWaterInferiors, baseSorting.getAfterIsInWaterInferiors());

			addSorting(id, allBaseBeforeIsInsideOfMaterialSuperiors, baseSorting.getBeforeIsInsideOfMaterialSuperiors());
			addSorting(id, allBaseBeforeIsInsideOfMaterialInferiors, baseSorting.getBeforeIsInsideOfMaterialInferiors());
			addSorting(id, allBaseOverrideIsInsideOfMaterialSuperiors, baseSorting.getOverrideIsInsideOfMaterialSuperiors());
			addSorting(id, allBaseOverrideIsInsideOfMaterialInferiors, baseSorting.getOverrideIsInsideOfMaterialInferiors());
			addSorting(id, allBaseAfterIsInsideOfMaterialSuperiors, baseSorting.getAfterIsInsideOfMaterialSuperiors());
			addSorting(id, allBaseAfterIsInsideOfMaterialInferiors, baseSorting.getAfterIsInsideOfMaterialInferiors());

			addSorting(id, allBaseBeforeIsOnLadderSuperiors, baseSorting.getBeforeIsOnLadderSuperiors());
			addSorting(id, allBaseBeforeIsOnLadderInferiors, baseSorting.getBeforeIsOnLadderInferiors());
			addSorting(id, allBaseOverrideIsOnLadderSuperiors, baseSorting.getOverrideIsOnLadderSuperiors());
			addSorting(id, allBaseOverrideIsOnLadderInferiors, baseSorting.getOverrideIsOnLadderInferiors());
			addSorting(id, allBaseAfterIsOnLadderSuperiors, baseSorting.getAfterIsOnLadderSuperiors());
			addSorting(id, allBaseAfterIsOnLadderInferiors, baseSorting.getAfterIsOnLadderInferiors());

			addSorting(id, allBaseBeforeIsPlayerSleepingSuperiors, baseSorting.getBeforeIsPlayerSleepingSuperiors());
			addSorting(id, allBaseBeforeIsPlayerSleepingInferiors, baseSorting.getBeforeIsPlayerSleepingInferiors());
			addSorting(id, allBaseOverrideIsPlayerSleepingSuperiors, baseSorting.getOverrideIsPlayerSleepingSuperiors());
			addSorting(id, allBaseOverrideIsPlayerSleepingInferiors, baseSorting.getOverrideIsPlayerSleepingInferiors());
			addSorting(id, allBaseAfterIsPlayerSleepingSuperiors, baseSorting.getAfterIsPlayerSleepingSuperiors());
			addSorting(id, allBaseAfterIsPlayerSleepingInferiors, baseSorting.getAfterIsPlayerSleepingInferiors());

			addSorting(id, allBaseBeforeIsSneakingSuperiors, baseSorting.getBeforeIsSneakingSuperiors());
			addSorting(id, allBaseBeforeIsSneakingInferiors, baseSorting.getBeforeIsSneakingInferiors());
			addSorting(id, allBaseOverrideIsSneakingSuperiors, baseSorting.getOverrideIsSneakingSuperiors());
			addSorting(id, allBaseOverrideIsSneakingInferiors, baseSorting.getOverrideIsSneakingInferiors());
			addSorting(id, allBaseAfterIsSneakingSuperiors, baseSorting.getAfterIsSneakingSuperiors());
			addSorting(id, allBaseAfterIsSneakingInferiors, baseSorting.getAfterIsSneakingInferiors());

			addSorting(id, allBaseBeforeJumpSuperiors, baseSorting.getBeforeJumpSuperiors());
			addSorting(id, allBaseBeforeJumpInferiors, baseSorting.getBeforeJumpInferiors());
			addSorting(id, allBaseOverrideJumpSuperiors, baseSorting.getOverrideJumpSuperiors());
			addSorting(id, allBaseOverrideJumpInferiors, baseSorting.getOverrideJumpInferiors());
			addSorting(id, allBaseAfterJumpSuperiors, baseSorting.getAfterJumpSuperiors());
			addSorting(id, allBaseAfterJumpInferiors, baseSorting.getAfterJumpInferiors());

			addSorting(id, allBaseBeforeKnockBackSuperiors, baseSorting.getBeforeKnockBackSuperiors());
			addSorting(id, allBaseBeforeKnockBackInferiors, baseSorting.getBeforeKnockBackInferiors());
			addSorting(id, allBaseOverrideKnockBackSuperiors, baseSorting.getOverrideKnockBackSuperiors());
			addSorting(id, allBaseOverrideKnockBackInferiors, baseSorting.getOverrideKnockBackInferiors());
			addSorting(id, allBaseAfterKnockBackSuperiors, baseSorting.getAfterKnockBackSuperiors());
			addSorting(id, allBaseAfterKnockBackInferiors, baseSorting.getAfterKnockBackInferiors());

			addSorting(id, allBaseBeforeMountEntitySuperiors, baseSorting.getBeforeMountEntitySuperiors());
			addSorting(id, allBaseBeforeMountEntityInferiors, baseSorting.getBeforeMountEntityInferiors());
			addSorting(id, allBaseOverrideMountEntitySuperiors, baseSorting.getOverrideMountEntitySuperiors());
			addSorting(id, allBaseOverrideMountEntityInferiors, baseSorting.getOverrideMountEntityInferiors());
			addSorting(id, allBaseAfterMountEntitySuperiors, baseSorting.getAfterMountEntitySuperiors());
			addSorting(id, allBaseAfterMountEntityInferiors, baseSorting.getAfterMountEntityInferiors());

			addSorting(id, allBaseBeforeMoveEntitySuperiors, baseSorting.getBeforeMoveEntitySuperiors());
			addSorting(id, allBaseBeforeMoveEntityInferiors, baseSorting.getBeforeMoveEntityInferiors());
			addSorting(id, allBaseOverrideMoveEntitySuperiors, baseSorting.getOverrideMoveEntitySuperiors());
			addSorting(id, allBaseOverrideMoveEntityInferiors, baseSorting.getOverrideMoveEntityInferiors());
			addSorting(id, allBaseAfterMoveEntitySuperiors, baseSorting.getAfterMoveEntitySuperiors());
			addSorting(id, allBaseAfterMoveEntityInferiors, baseSorting.getAfterMoveEntityInferiors());

			addSorting(id, allBaseBeforeMoveEntityWithHeadingSuperiors, baseSorting.getBeforeMoveEntityWithHeadingSuperiors());
			addSorting(id, allBaseBeforeMoveEntityWithHeadingInferiors, baseSorting.getBeforeMoveEntityWithHeadingInferiors());
			addSorting(id, allBaseOverrideMoveEntityWithHeadingSuperiors, baseSorting.getOverrideMoveEntityWithHeadingSuperiors());
			addSorting(id, allBaseOverrideMoveEntityWithHeadingInferiors, baseSorting.getOverrideMoveEntityWithHeadingInferiors());
			addSorting(id, allBaseAfterMoveEntityWithHeadingSuperiors, baseSorting.getAfterMoveEntityWithHeadingSuperiors());
			addSorting(id, allBaseAfterMoveEntityWithHeadingInferiors, baseSorting.getAfterMoveEntityWithHeadingInferiors());

			addSorting(id, allBaseBeforeMoveFlyingSuperiors, baseSorting.getBeforeMoveFlyingSuperiors());
			addSorting(id, allBaseBeforeMoveFlyingInferiors, baseSorting.getBeforeMoveFlyingInferiors());
			addSorting(id, allBaseOverrideMoveFlyingSuperiors, baseSorting.getOverrideMoveFlyingSuperiors());
			addSorting(id, allBaseOverrideMoveFlyingInferiors, baseSorting.getOverrideMoveFlyingInferiors());
			addSorting(id, allBaseAfterMoveFlyingSuperiors, baseSorting.getAfterMoveFlyingSuperiors());
			addSorting(id, allBaseAfterMoveFlyingInferiors, baseSorting.getAfterMoveFlyingInferiors());

			addSorting(id, allBaseBeforeOnDeathSuperiors, baseSorting.getBeforeOnDeathSuperiors());
			addSorting(id, allBaseBeforeOnDeathInferiors, baseSorting.getBeforeOnDeathInferiors());
			addSorting(id, allBaseOverrideOnDeathSuperiors, baseSorting.getOverrideOnDeathSuperiors());
			addSorting(id, allBaseOverrideOnDeathInferiors, baseSorting.getOverrideOnDeathInferiors());
			addSorting(id, allBaseAfterOnDeathSuperiors, baseSorting.getAfterOnDeathSuperiors());
			addSorting(id, allBaseAfterOnDeathInferiors, baseSorting.getAfterOnDeathInferiors());

			addSorting(id, allBaseBeforeOnLivingUpdateSuperiors, baseSorting.getBeforeOnLivingUpdateSuperiors());
			addSorting(id, allBaseBeforeOnLivingUpdateInferiors, baseSorting.getBeforeOnLivingUpdateInferiors());
			addSorting(id, allBaseOverrideOnLivingUpdateSuperiors, baseSorting.getOverrideOnLivingUpdateSuperiors());
			addSorting(id, allBaseOverrideOnLivingUpdateInferiors, baseSorting.getOverrideOnLivingUpdateInferiors());
			addSorting(id, allBaseAfterOnLivingUpdateSuperiors, baseSorting.getAfterOnLivingUpdateSuperiors());
			addSorting(id, allBaseAfterOnLivingUpdateInferiors, baseSorting.getAfterOnLivingUpdateInferiors());

			addSorting(id, allBaseBeforeOnKillEntitySuperiors, baseSorting.getBeforeOnKillEntitySuperiors());
			addSorting(id, allBaseBeforeOnKillEntityInferiors, baseSorting.getBeforeOnKillEntityInferiors());
			addSorting(id, allBaseOverrideOnKillEntitySuperiors, baseSorting.getOverrideOnKillEntitySuperiors());
			addSorting(id, allBaseOverrideOnKillEntityInferiors, baseSorting.getOverrideOnKillEntityInferiors());
			addSorting(id, allBaseAfterOnKillEntitySuperiors, baseSorting.getAfterOnKillEntitySuperiors());
			addSorting(id, allBaseAfterOnKillEntityInferiors, baseSorting.getAfterOnKillEntityInferiors());

			addSorting(id, allBaseBeforeOnStruckByLightningSuperiors, baseSorting.getBeforeOnStruckByLightningSuperiors());
			addSorting(id, allBaseBeforeOnStruckByLightningInferiors, baseSorting.getBeforeOnStruckByLightningInferiors());
			addSorting(id, allBaseOverrideOnStruckByLightningSuperiors, baseSorting.getOverrideOnStruckByLightningSuperiors());
			addSorting(id, allBaseOverrideOnStruckByLightningInferiors, baseSorting.getOverrideOnStruckByLightningInferiors());
			addSorting(id, allBaseAfterOnStruckByLightningSuperiors, baseSorting.getAfterOnStruckByLightningSuperiors());
			addSorting(id, allBaseAfterOnStruckByLightningInferiors, baseSorting.getAfterOnStruckByLightningInferiors());

			addSorting(id, allBaseBeforeOnUpdateSuperiors, baseSorting.getBeforeOnUpdateSuperiors());
			addSorting(id, allBaseBeforeOnUpdateInferiors, baseSorting.getBeforeOnUpdateInferiors());
			addSorting(id, allBaseOverrideOnUpdateSuperiors, baseSorting.getOverrideOnUpdateSuperiors());
			addSorting(id, allBaseOverrideOnUpdateInferiors, baseSorting.getOverrideOnUpdateInferiors());
			addSorting(id, allBaseAfterOnUpdateSuperiors, baseSorting.getAfterOnUpdateSuperiors());
			addSorting(id, allBaseAfterOnUpdateInferiors, baseSorting.getAfterOnUpdateInferiors());

			addSorting(id, allBaseBeforeOnUpdateEntitySuperiors, baseSorting.getBeforeOnUpdateEntitySuperiors());
			addSorting(id, allBaseBeforeOnUpdateEntityInferiors, baseSorting.getBeforeOnUpdateEntityInferiors());
			addSorting(id, allBaseOverrideOnUpdateEntitySuperiors, baseSorting.getOverrideOnUpdateEntitySuperiors());
			addSorting(id, allBaseOverrideOnUpdateEntityInferiors, baseSorting.getOverrideOnUpdateEntityInferiors());
			addSorting(id, allBaseAfterOnUpdateEntitySuperiors, baseSorting.getAfterOnUpdateEntitySuperiors());
			addSorting(id, allBaseAfterOnUpdateEntityInferiors, baseSorting.getAfterOnUpdateEntityInferiors());

			addSorting(id, allBaseBeforeReadEntityFromNBTSuperiors, baseSorting.getBeforeReadEntityFromNBTSuperiors());
			addSorting(id, allBaseBeforeReadEntityFromNBTInferiors, baseSorting.getBeforeReadEntityFromNBTInferiors());
			addSorting(id, allBaseOverrideReadEntityFromNBTSuperiors, baseSorting.getOverrideReadEntityFromNBTSuperiors());
			addSorting(id, allBaseOverrideReadEntityFromNBTInferiors, baseSorting.getOverrideReadEntityFromNBTInferiors());
			addSorting(id, allBaseAfterReadEntityFromNBTSuperiors, baseSorting.getAfterReadEntityFromNBTSuperiors());
			addSorting(id, allBaseAfterReadEntityFromNBTInferiors, baseSorting.getAfterReadEntityFromNBTInferiors());

			addSorting(id, allBaseBeforeSetDeadSuperiors, baseSorting.getBeforeSetDeadSuperiors());
			addSorting(id, allBaseBeforeSetDeadInferiors, baseSorting.getBeforeSetDeadInferiors());
			addSorting(id, allBaseOverrideSetDeadSuperiors, baseSorting.getOverrideSetDeadSuperiors());
			addSorting(id, allBaseOverrideSetDeadInferiors, baseSorting.getOverrideSetDeadInferiors());
			addSorting(id, allBaseAfterSetDeadSuperiors, baseSorting.getAfterSetDeadSuperiors());
			addSorting(id, allBaseAfterSetDeadInferiors, baseSorting.getAfterSetDeadInferiors());

			addSorting(id, allBaseBeforeSetEntityActionStateSuperiors, baseSorting.getBeforeSetEntityActionStateSuperiors());
			addSorting(id, allBaseBeforeSetEntityActionStateInferiors, baseSorting.getBeforeSetEntityActionStateInferiors());
			addSorting(id, allBaseOverrideSetEntityActionStateSuperiors, baseSorting.getOverrideSetEntityActionStateSuperiors());
			addSorting(id, allBaseOverrideSetEntityActionStateInferiors, baseSorting.getOverrideSetEntityActionStateInferiors());
			addSorting(id, allBaseAfterSetEntityActionStateSuperiors, baseSorting.getAfterSetEntityActionStateSuperiors());
			addSorting(id, allBaseAfterSetEntityActionStateInferiors, baseSorting.getAfterSetEntityActionStateInferiors());

			addSorting(id, allBaseBeforeSetPositionSuperiors, baseSorting.getBeforeSetPositionSuperiors());
			addSorting(id, allBaseBeforeSetPositionInferiors, baseSorting.getBeforeSetPositionInferiors());
			addSorting(id, allBaseOverrideSetPositionSuperiors, baseSorting.getOverrideSetPositionSuperiors());
			addSorting(id, allBaseOverrideSetPositionInferiors, baseSorting.getOverrideSetPositionInferiors());
			addSorting(id, allBaseAfterSetPositionSuperiors, baseSorting.getAfterSetPositionSuperiors());
			addSorting(id, allBaseAfterSetPositionInferiors, baseSorting.getAfterSetPositionInferiors());

			addSorting(id, allBaseBeforeSetSneakingSuperiors, baseSorting.getBeforeSetSneakingSuperiors());
			addSorting(id, allBaseBeforeSetSneakingInferiors, baseSorting.getBeforeSetSneakingInferiors());
			addSorting(id, allBaseOverrideSetSneakingSuperiors, baseSorting.getOverrideSetSneakingSuperiors());
			addSorting(id, allBaseOverrideSetSneakingInferiors, baseSorting.getOverrideSetSneakingInferiors());
			addSorting(id, allBaseAfterSetSneakingSuperiors, baseSorting.getAfterSetSneakingSuperiors());
			addSorting(id, allBaseAfterSetSneakingInferiors, baseSorting.getAfterSetSneakingInferiors());

			addSorting(id, allBaseBeforeSetSprintingSuperiors, baseSorting.getBeforeSetSprintingSuperiors());
			addSorting(id, allBaseBeforeSetSprintingInferiors, baseSorting.getBeforeSetSprintingInferiors());
			addSorting(id, allBaseOverrideSetSprintingSuperiors, baseSorting.getOverrideSetSprintingSuperiors());
			addSorting(id, allBaseOverrideSetSprintingInferiors, baseSorting.getOverrideSetSprintingInferiors());
			addSorting(id, allBaseAfterSetSprintingSuperiors, baseSorting.getAfterSetSprintingSuperiors());
			addSorting(id, allBaseAfterSetSprintingInferiors, baseSorting.getAfterSetSprintingInferiors());

			addSorting(id, allBaseBeforeSwingItemSuperiors, baseSorting.getBeforeSwingItemSuperiors());
			addSorting(id, allBaseBeforeSwingItemInferiors, baseSorting.getBeforeSwingItemInferiors());
			addSorting(id, allBaseOverrideSwingItemSuperiors, baseSorting.getOverrideSwingItemSuperiors());
			addSorting(id, allBaseOverrideSwingItemInferiors, baseSorting.getOverrideSwingItemInferiors());
			addSorting(id, allBaseAfterSwingItemSuperiors, baseSorting.getAfterSwingItemSuperiors());
			addSorting(id, allBaseAfterSwingItemInferiors, baseSorting.getAfterSwingItemInferiors());

			addSorting(id, allBaseBeforeUpdateEntityActionStateSuperiors, baseSorting.getBeforeUpdateEntityActionStateSuperiors());
			addSorting(id, allBaseBeforeUpdateEntityActionStateInferiors, baseSorting.getBeforeUpdateEntityActionStateInferiors());
			addSorting(id, allBaseOverrideUpdateEntityActionStateSuperiors, baseSorting.getOverrideUpdateEntityActionStateSuperiors());
			addSorting(id, allBaseOverrideUpdateEntityActionStateInferiors, baseSorting.getOverrideUpdateEntityActionStateInferiors());
			addSorting(id, allBaseAfterUpdateEntityActionStateSuperiors, baseSorting.getAfterUpdateEntityActionStateSuperiors());
			addSorting(id, allBaseAfterUpdateEntityActionStateInferiors, baseSorting.getAfterUpdateEntityActionStateInferiors());

			addSorting(id, allBaseBeforeUpdatePotionEffectsSuperiors, baseSorting.getBeforeUpdatePotionEffectsSuperiors());
			addSorting(id, allBaseBeforeUpdatePotionEffectsInferiors, baseSorting.getBeforeUpdatePotionEffectsInferiors());
			addSorting(id, allBaseOverrideUpdatePotionEffectsSuperiors, baseSorting.getOverrideUpdatePotionEffectsSuperiors());
			addSorting(id, allBaseOverrideUpdatePotionEffectsInferiors, baseSorting.getOverrideUpdatePotionEffectsInferiors());
			addSorting(id, allBaseAfterUpdatePotionEffectsSuperiors, baseSorting.getAfterUpdatePotionEffectsSuperiors());
			addSorting(id, allBaseAfterUpdatePotionEffectsInferiors, baseSorting.getAfterUpdatePotionEffectsInferiors());

			addSorting(id, allBaseBeforeUpdateRiddenSuperiors, baseSorting.getBeforeUpdateRiddenSuperiors());
			addSorting(id, allBaseBeforeUpdateRiddenInferiors, baseSorting.getBeforeUpdateRiddenInferiors());
			addSorting(id, allBaseOverrideUpdateRiddenSuperiors, baseSorting.getOverrideUpdateRiddenSuperiors());
			addSorting(id, allBaseOverrideUpdateRiddenInferiors, baseSorting.getOverrideUpdateRiddenInferiors());
			addSorting(id, allBaseAfterUpdateRiddenSuperiors, baseSorting.getAfterUpdateRiddenSuperiors());
			addSorting(id, allBaseAfterUpdateRiddenInferiors, baseSorting.getAfterUpdateRiddenInferiors());

			addSorting(id, allBaseBeforeWakeUpPlayerSuperiors, baseSorting.getBeforeWakeUpPlayerSuperiors());
			addSorting(id, allBaseBeforeWakeUpPlayerInferiors, baseSorting.getBeforeWakeUpPlayerInferiors());
			addSorting(id, allBaseOverrideWakeUpPlayerSuperiors, baseSorting.getOverrideWakeUpPlayerSuperiors());
			addSorting(id, allBaseOverrideWakeUpPlayerInferiors, baseSorting.getOverrideWakeUpPlayerInferiors());
			addSorting(id, allBaseAfterWakeUpPlayerSuperiors, baseSorting.getAfterWakeUpPlayerSuperiors());
			addSorting(id, allBaseAfterWakeUpPlayerInferiors, baseSorting.getAfterWakeUpPlayerInferiors());

			addSorting(id, allBaseBeforeWriteEntityToNBTSuperiors, baseSorting.getBeforeWriteEntityToNBTSuperiors());
			addSorting(id, allBaseBeforeWriteEntityToNBTInferiors, baseSorting.getBeforeWriteEntityToNBTInferiors());
			addSorting(id, allBaseOverrideWriteEntityToNBTSuperiors, baseSorting.getOverrideWriteEntityToNBTSuperiors());
			addSorting(id, allBaseOverrideWriteEntityToNBTInferiors, baseSorting.getOverrideWriteEntityToNBTInferiors());
			addSorting(id, allBaseAfterWriteEntityToNBTSuperiors, baseSorting.getAfterWriteEntityToNBTSuperiors());
			addSorting(id, allBaseAfterWriteEntityToNBTInferiors, baseSorting.getAfterWriteEntityToNBTInferiors());

		}

		addMethod(id, baseClass, beforeLocalConstructingHookTypes, "beforeLocalConstructing", net.minecraft.server.MinecraftServer.class, net.minecraft.world.WorldServer.class, com.mojang.authlib.GameProfile.class, net.minecraft.server.management.ItemInWorldManager.class);
		addMethod(id, baseClass, afterLocalConstructingHookTypes, "afterLocalConstructing", net.minecraft.server.MinecraftServer.class, net.minecraft.world.WorldServer.class, com.mojang.authlib.GameProfile.class, net.minecraft.server.management.ItemInWorldManager.class);


		addMethod(id, baseClass, beforeAddExhaustionHookTypes, "beforeAddExhaustion", float.class);
		addMethod(id, baseClass, overrideAddExhaustionHookTypes, "addExhaustion", float.class);
		addMethod(id, baseClass, afterAddExhaustionHookTypes, "afterAddExhaustion", float.class);

		addMethod(id, baseClass, beforeAddExperienceHookTypes, "beforeAddExperience", int.class);
		addMethod(id, baseClass, overrideAddExperienceHookTypes, "addExperience", int.class);
		addMethod(id, baseClass, afterAddExperienceHookTypes, "afterAddExperience", int.class);

		addMethod(id, baseClass, beforeAddExperienceLevelHookTypes, "beforeAddExperienceLevel", int.class);
		addMethod(id, baseClass, overrideAddExperienceLevelHookTypes, "addExperienceLevel", int.class);
		addMethod(id, baseClass, afterAddExperienceLevelHookTypes, "afterAddExperienceLevel", int.class);

		addMethod(id, baseClass, beforeAddMovementStatHookTypes, "beforeAddMovementStat", double.class, double.class, double.class);
		addMethod(id, baseClass, overrideAddMovementStatHookTypes, "addMovementStat", double.class, double.class, double.class);
		addMethod(id, baseClass, afterAddMovementStatHookTypes, "afterAddMovementStat", double.class, double.class, double.class);

		addMethod(id, baseClass, beforeAttackEntityFromHookTypes, "beforeAttackEntityFrom", net.minecraft.util.DamageSource.class, float.class);
		addMethod(id, baseClass, overrideAttackEntityFromHookTypes, "attackEntityFrom", net.minecraft.util.DamageSource.class, float.class);
		addMethod(id, baseClass, afterAttackEntityFromHookTypes, "afterAttackEntityFrom", net.minecraft.util.DamageSource.class, float.class);

		addMethod(id, baseClass, beforeAttackTargetEntityWithCurrentItemHookTypes, "beforeAttackTargetEntityWithCurrentItem", net.minecraft.entity.Entity.class);
		addMethod(id, baseClass, overrideAttackTargetEntityWithCurrentItemHookTypes, "attackTargetEntityWithCurrentItem", net.minecraft.entity.Entity.class);
		addMethod(id, baseClass, afterAttackTargetEntityWithCurrentItemHookTypes, "afterAttackTargetEntityWithCurrentItem", net.minecraft.entity.Entity.class);

		addMethod(id, baseClass, beforeCanBreatheUnderwaterHookTypes, "beforeCanBreatheUnderwater");
		addMethod(id, baseClass, overrideCanBreatheUnderwaterHookTypes, "canBreatheUnderwater");
		addMethod(id, baseClass, afterCanBreatheUnderwaterHookTypes, "afterCanBreatheUnderwater");

		addMethod(id, baseClass, beforeCanHarvestBlockHookTypes, "beforeCanHarvestBlock", net.minecraft.block.Block.class);
		addMethod(id, baseClass, overrideCanHarvestBlockHookTypes, "canHarvestBlock", net.minecraft.block.Block.class);
		addMethod(id, baseClass, afterCanHarvestBlockHookTypes, "afterCanHarvestBlock", net.minecraft.block.Block.class);

		addMethod(id, baseClass, beforeCanPlayerEditHookTypes, "beforeCanPlayerEdit", int.class, int.class, int.class, int.class, net.minecraft.item.ItemStack.class);
		addMethod(id, baseClass, overrideCanPlayerEditHookTypes, "canPlayerEdit", int.class, int.class, int.class, int.class, net.minecraft.item.ItemStack.class);
		addMethod(id, baseClass, afterCanPlayerEditHookTypes, "afterCanPlayerEdit", int.class, int.class, int.class, int.class, net.minecraft.item.ItemStack.class);

		addMethod(id, baseClass, beforeCanTriggerWalkingHookTypes, "beforeCanTriggerWalking");
		addMethod(id, baseClass, overrideCanTriggerWalkingHookTypes, "canTriggerWalking");
		addMethod(id, baseClass, afterCanTriggerWalkingHookTypes, "afterCanTriggerWalking");

		addMethod(id, baseClass, beforeClonePlayerHookTypes, "beforeClonePlayer", net.minecraft.entity.player.EntityPlayer.class, boolean.class);
		addMethod(id, baseClass, overrideClonePlayerHookTypes, "clonePlayer", net.minecraft.entity.player.EntityPlayer.class, boolean.class);
		addMethod(id, baseClass, afterClonePlayerHookTypes, "afterClonePlayer", net.minecraft.entity.player.EntityPlayer.class, boolean.class);

		addMethod(id, baseClass, beforeDamageEntityHookTypes, "beforeDamageEntity", net.minecraft.util.DamageSource.class, float.class);
		addMethod(id, baseClass, overrideDamageEntityHookTypes, "damageEntity", net.minecraft.util.DamageSource.class, float.class);
		addMethod(id, baseClass, afterDamageEntityHookTypes, "afterDamageEntity", net.minecraft.util.DamageSource.class, float.class);

		addMethod(id, baseClass, beforeDisplayGUIChestHookTypes, "beforeDisplayGUIChest", net.minecraft.inventory.IInventory.class);
		addMethod(id, baseClass, overrideDisplayGUIChestHookTypes, "displayGUIChest", net.minecraft.inventory.IInventory.class);
		addMethod(id, baseClass, afterDisplayGUIChestHookTypes, "afterDisplayGUIChest", net.minecraft.inventory.IInventory.class);

		addMethod(id, baseClass, beforeDisplayGUIDispenserHookTypes, "beforeDisplayGUIDispenser", net.minecraft.tileentity.TileEntityDispenser.class);
		addMethod(id, baseClass, overrideDisplayGUIDispenserHookTypes, "displayGUIDispenser", net.minecraft.tileentity.TileEntityDispenser.class);
		addMethod(id, baseClass, afterDisplayGUIDispenserHookTypes, "afterDisplayGUIDispenser", net.minecraft.tileentity.TileEntityDispenser.class);

		addMethod(id, baseClass, beforeDisplayGUIFurnaceHookTypes, "beforeDisplayGUIFurnace", net.minecraft.tileentity.TileEntityFurnace.class);
		addMethod(id, baseClass, overrideDisplayGUIFurnaceHookTypes, "displayGUIFurnace", net.minecraft.tileentity.TileEntityFurnace.class);
		addMethod(id, baseClass, afterDisplayGUIFurnaceHookTypes, "afterDisplayGUIFurnace", net.minecraft.tileentity.TileEntityFurnace.class);

		addMethod(id, baseClass, beforeDisplayGUIWorkbenchHookTypes, "beforeDisplayGUIWorkbench", int.class, int.class, int.class);
		addMethod(id, baseClass, overrideDisplayGUIWorkbenchHookTypes, "displayGUIWorkbench", int.class, int.class, int.class);
		addMethod(id, baseClass, afterDisplayGUIWorkbenchHookTypes, "afterDisplayGUIWorkbench", int.class, int.class, int.class);

		addMethod(id, baseClass, beforeDropOneItemHookTypes, "beforeDropOneItem", boolean.class);
		addMethod(id, baseClass, overrideDropOneItemHookTypes, "dropOneItem", boolean.class);
		addMethod(id, baseClass, afterDropOneItemHookTypes, "afterDropOneItem", boolean.class);

		addMethod(id, baseClass, beforeDropPlayerItemHookTypes, "beforeDropPlayerItem", net.minecraft.item.ItemStack.class, boolean.class);
		addMethod(id, baseClass, overrideDropPlayerItemHookTypes, "dropPlayerItem", net.minecraft.item.ItemStack.class, boolean.class);
		addMethod(id, baseClass, afterDropPlayerItemHookTypes, "afterDropPlayerItem", net.minecraft.item.ItemStack.class, boolean.class);

		addMethod(id, baseClass, beforeFallHookTypes, "beforeFall", float.class);
		addMethod(id, baseClass, overrideFallHookTypes, "fall", float.class);
		addMethod(id, baseClass, afterFallHookTypes, "afterFall", float.class);

		addMethod(id, baseClass, beforeGetAIMoveSpeedHookTypes, "beforeGetAIMoveSpeed");
		addMethod(id, baseClass, overrideGetAIMoveSpeedHookTypes, "getAIMoveSpeed");
		addMethod(id, baseClass, afterGetAIMoveSpeedHookTypes, "afterGetAIMoveSpeed");

		addMethod(id, baseClass, beforeGetCurrentPlayerStrVsBlockHookTypes, "beforeGetCurrentPlayerStrVsBlock", net.minecraft.block.Block.class, boolean.class);
		addMethod(id, baseClass, overrideGetCurrentPlayerStrVsBlockHookTypes, "getCurrentPlayerStrVsBlock", net.minecraft.block.Block.class, boolean.class);
		addMethod(id, baseClass, afterGetCurrentPlayerStrVsBlockHookTypes, "afterGetCurrentPlayerStrVsBlock", net.minecraft.block.Block.class, boolean.class);

		addMethod(id, baseClass, beforeGetCurrentPlayerStrVsBlockForgeHookTypes, "beforeGetCurrentPlayerStrVsBlockForge", net.minecraft.block.Block.class, boolean.class, int.class);
		addMethod(id, baseClass, overrideGetCurrentPlayerStrVsBlockForgeHookTypes, "getCurrentPlayerStrVsBlockForge", net.minecraft.block.Block.class, boolean.class, int.class);
		addMethod(id, baseClass, afterGetCurrentPlayerStrVsBlockForgeHookTypes, "afterGetCurrentPlayerStrVsBlockForge", net.minecraft.block.Block.class, boolean.class, int.class);

		addMethod(id, baseClass, beforeGetDistanceSqHookTypes, "beforeGetDistanceSq", double.class, double.class, double.class);
		addMethod(id, baseClass, overrideGetDistanceSqHookTypes, "getDistanceSq", double.class, double.class, double.class);
		addMethod(id, baseClass, afterGetDistanceSqHookTypes, "afterGetDistanceSq", double.class, double.class, double.class);

		addMethod(id, baseClass, beforeGetBrightnessHookTypes, "beforeGetBrightness", float.class);
		addMethod(id, baseClass, overrideGetBrightnessHookTypes, "getBrightness", float.class);
		addMethod(id, baseClass, afterGetBrightnessHookTypes, "afterGetBrightness", float.class);

		addMethod(id, baseClass, beforeGetEyeHeightHookTypes, "beforeGetEyeHeight");
		addMethod(id, baseClass, overrideGetEyeHeightHookTypes, "getEyeHeight");
		addMethod(id, baseClass, afterGetEyeHeightHookTypes, "afterGetEyeHeight");

		addMethod(id, baseClass, beforeHealHookTypes, "beforeHeal", float.class);
		addMethod(id, baseClass, overrideHealHookTypes, "heal", float.class);
		addMethod(id, baseClass, afterHealHookTypes, "afterHeal", float.class);

		addMethod(id, baseClass, beforeIsEntityInsideOpaqueBlockHookTypes, "beforeIsEntityInsideOpaqueBlock");
		addMethod(id, baseClass, overrideIsEntityInsideOpaqueBlockHookTypes, "isEntityInsideOpaqueBlock");
		addMethod(id, baseClass, afterIsEntityInsideOpaqueBlockHookTypes, "afterIsEntityInsideOpaqueBlock");

		addMethod(id, baseClass, beforeIsInWaterHookTypes, "beforeIsInWater");
		addMethod(id, baseClass, overrideIsInWaterHookTypes, "isInWater");
		addMethod(id, baseClass, afterIsInWaterHookTypes, "afterIsInWater");

		addMethod(id, baseClass, beforeIsInsideOfMaterialHookTypes, "beforeIsInsideOfMaterial", net.minecraft.block.material.Material.class);
		addMethod(id, baseClass, overrideIsInsideOfMaterialHookTypes, "isInsideOfMaterial", net.minecraft.block.material.Material.class);
		addMethod(id, baseClass, afterIsInsideOfMaterialHookTypes, "afterIsInsideOfMaterial", net.minecraft.block.material.Material.class);

		addMethod(id, baseClass, beforeIsOnLadderHookTypes, "beforeIsOnLadder");
		addMethod(id, baseClass, overrideIsOnLadderHookTypes, "isOnLadder");
		addMethod(id, baseClass, afterIsOnLadderHookTypes, "afterIsOnLadder");

		addMethod(id, baseClass, beforeIsPlayerSleepingHookTypes, "beforeIsPlayerSleeping");
		addMethod(id, baseClass, overrideIsPlayerSleepingHookTypes, "isPlayerSleeping");
		addMethod(id, baseClass, afterIsPlayerSleepingHookTypes, "afterIsPlayerSleeping");

		addMethod(id, baseClass, beforeIsSneakingHookTypes, "beforeIsSneaking");
		addMethod(id, baseClass, overrideIsSneakingHookTypes, "isSneaking");
		addMethod(id, baseClass, afterIsSneakingHookTypes, "afterIsSneaking");

		addMethod(id, baseClass, beforeJumpHookTypes, "beforeJump");
		addMethod(id, baseClass, overrideJumpHookTypes, "jump");
		addMethod(id, baseClass, afterJumpHookTypes, "afterJump");

		addMethod(id, baseClass, beforeKnockBackHookTypes, "beforeKnockBack", net.minecraft.entity.Entity.class, float.class, double.class, double.class);
		addMethod(id, baseClass, overrideKnockBackHookTypes, "knockBack", net.minecraft.entity.Entity.class, float.class, double.class, double.class);
		addMethod(id, baseClass, afterKnockBackHookTypes, "afterKnockBack", net.minecraft.entity.Entity.class, float.class, double.class, double.class);

		addMethod(id, baseClass, beforeMountEntityHookTypes, "beforeMountEntity", net.minecraft.entity.Entity.class);
		addMethod(id, baseClass, overrideMountEntityHookTypes, "mountEntity", net.minecraft.entity.Entity.class);
		addMethod(id, baseClass, afterMountEntityHookTypes, "afterMountEntity", net.minecraft.entity.Entity.class);

		addMethod(id, baseClass, beforeMoveEntityHookTypes, "beforeMoveEntity", double.class, double.class, double.class);
		addMethod(id, baseClass, overrideMoveEntityHookTypes, "moveEntity", double.class, double.class, double.class);
		addMethod(id, baseClass, afterMoveEntityHookTypes, "afterMoveEntity", double.class, double.class, double.class);

		addMethod(id, baseClass, beforeMoveEntityWithHeadingHookTypes, "beforeMoveEntityWithHeading", float.class, float.class);
		addMethod(id, baseClass, overrideMoveEntityWithHeadingHookTypes, "moveEntityWithHeading", float.class, float.class);
		addMethod(id, baseClass, afterMoveEntityWithHeadingHookTypes, "afterMoveEntityWithHeading", float.class, float.class);

		addMethod(id, baseClass, beforeMoveFlyingHookTypes, "beforeMoveFlying", float.class, float.class, float.class);
		addMethod(id, baseClass, overrideMoveFlyingHookTypes, "moveFlying", float.class, float.class, float.class);
		addMethod(id, baseClass, afterMoveFlyingHookTypes, "afterMoveFlying", float.class, float.class, float.class);

		addMethod(id, baseClass, beforeOnDeathHookTypes, "beforeOnDeath", net.minecraft.util.DamageSource.class);
		addMethod(id, baseClass, overrideOnDeathHookTypes, "onDeath", net.minecraft.util.DamageSource.class);
		addMethod(id, baseClass, afterOnDeathHookTypes, "afterOnDeath", net.minecraft.util.DamageSource.class);

		addMethod(id, baseClass, beforeOnLivingUpdateHookTypes, "beforeOnLivingUpdate");
		addMethod(id, baseClass, overrideOnLivingUpdateHookTypes, "onLivingUpdate");
		addMethod(id, baseClass, afterOnLivingUpdateHookTypes, "afterOnLivingUpdate");

		addMethod(id, baseClass, beforeOnKillEntityHookTypes, "beforeOnKillEntity", net.minecraft.entity.EntityLivingBase.class);
		addMethod(id, baseClass, overrideOnKillEntityHookTypes, "onKillEntity", net.minecraft.entity.EntityLivingBase.class);
		addMethod(id, baseClass, afterOnKillEntityHookTypes, "afterOnKillEntity", net.minecraft.entity.EntityLivingBase.class);

		addMethod(id, baseClass, beforeOnStruckByLightningHookTypes, "beforeOnStruckByLightning", net.minecraft.entity.effect.EntityLightningBolt.class);
		addMethod(id, baseClass, overrideOnStruckByLightningHookTypes, "onStruckByLightning", net.minecraft.entity.effect.EntityLightningBolt.class);
		addMethod(id, baseClass, afterOnStruckByLightningHookTypes, "afterOnStruckByLightning", net.minecraft.entity.effect.EntityLightningBolt.class);

		addMethod(id, baseClass, beforeOnUpdateHookTypes, "beforeOnUpdate");
		addMethod(id, baseClass, overrideOnUpdateHookTypes, "onUpdate");
		addMethod(id, baseClass, afterOnUpdateHookTypes, "afterOnUpdate");

		addMethod(id, baseClass, beforeOnUpdateEntityHookTypes, "beforeOnUpdateEntity");
		addMethod(id, baseClass, overrideOnUpdateEntityHookTypes, "onUpdateEntity");
		addMethod(id, baseClass, afterOnUpdateEntityHookTypes, "afterOnUpdateEntity");

		addMethod(id, baseClass, beforeReadEntityFromNBTHookTypes, "beforeReadEntityFromNBT", net.minecraft.nbt.NBTTagCompound.class);
		addMethod(id, baseClass, overrideReadEntityFromNBTHookTypes, "readEntityFromNBT", net.minecraft.nbt.NBTTagCompound.class);
		addMethod(id, baseClass, afterReadEntityFromNBTHookTypes, "afterReadEntityFromNBT", net.minecraft.nbt.NBTTagCompound.class);

		addMethod(id, baseClass, beforeSetDeadHookTypes, "beforeSetDead");
		addMethod(id, baseClass, overrideSetDeadHookTypes, "setDead");
		addMethod(id, baseClass, afterSetDeadHookTypes, "afterSetDead");

		addMethod(id, baseClass, beforeSetEntityActionStateHookTypes, "beforeSetEntityActionState", float.class, float.class, boolean.class, boolean.class);
		addMethod(id, baseClass, overrideSetEntityActionStateHookTypes, "setEntityActionState", float.class, float.class, boolean.class, boolean.class);
		addMethod(id, baseClass, afterSetEntityActionStateHookTypes, "afterSetEntityActionState", float.class, float.class, boolean.class, boolean.class);

		addMethod(id, baseClass, beforeSetPositionHookTypes, "beforeSetPosition", double.class, double.class, double.class);
		addMethod(id, baseClass, overrideSetPositionHookTypes, "setPosition", double.class, double.class, double.class);
		addMethod(id, baseClass, afterSetPositionHookTypes, "afterSetPosition", double.class, double.class, double.class);

		addMethod(id, baseClass, beforeSetSneakingHookTypes, "beforeSetSneaking", boolean.class);
		addMethod(id, baseClass, overrideSetSneakingHookTypes, "setSneaking", boolean.class);
		addMethod(id, baseClass, afterSetSneakingHookTypes, "afterSetSneaking", boolean.class);

		addMethod(id, baseClass, beforeSetSprintingHookTypes, "beforeSetSprinting", boolean.class);
		addMethod(id, baseClass, overrideSetSprintingHookTypes, "setSprinting", boolean.class);
		addMethod(id, baseClass, afterSetSprintingHookTypes, "afterSetSprinting", boolean.class);

		addMethod(id, baseClass, beforeSwingItemHookTypes, "beforeSwingItem");
		addMethod(id, baseClass, overrideSwingItemHookTypes, "swingItem");
		addMethod(id, baseClass, afterSwingItemHookTypes, "afterSwingItem");

		addMethod(id, baseClass, beforeUpdateEntityActionStateHookTypes, "beforeUpdateEntityActionState");
		addMethod(id, baseClass, overrideUpdateEntityActionStateHookTypes, "updateEntityActionState");
		addMethod(id, baseClass, afterUpdateEntityActionStateHookTypes, "afterUpdateEntityActionState");

		addMethod(id, baseClass, beforeUpdatePotionEffectsHookTypes, "beforeUpdatePotionEffects");
		addMethod(id, baseClass, overrideUpdatePotionEffectsHookTypes, "updatePotionEffects");
		addMethod(id, baseClass, afterUpdatePotionEffectsHookTypes, "afterUpdatePotionEffects");

		addMethod(id, baseClass, beforeUpdateRiddenHookTypes, "beforeUpdateRidden");
		addMethod(id, baseClass, overrideUpdateRiddenHookTypes, "updateRidden");
		addMethod(id, baseClass, afterUpdateRiddenHookTypes, "afterUpdateRidden");

		addMethod(id, baseClass, beforeWakeUpPlayerHookTypes, "beforeWakeUpPlayer", boolean.class, boolean.class, boolean.class);
		addMethod(id, baseClass, overrideWakeUpPlayerHookTypes, "wakeUpPlayer", boolean.class, boolean.class, boolean.class);
		addMethod(id, baseClass, afterWakeUpPlayerHookTypes, "afterWakeUpPlayer", boolean.class, boolean.class, boolean.class);

		addMethod(id, baseClass, beforeWriteEntityToNBTHookTypes, "beforeWriteEntityToNBT", net.minecraft.nbt.NBTTagCompound.class);
		addMethod(id, baseClass, overrideWriteEntityToNBTHookTypes, "writeEntityToNBT", net.minecraft.nbt.NBTTagCompound.class);
		addMethod(id, baseClass, afterWriteEntityToNBTHookTypes, "afterWriteEntityToNBT", net.minecraft.nbt.NBTTagCompound.class);


		addDynamicMethods(id, baseClass);

		addDynamicKeys(id, baseClass, beforeDynamicHookMethods, beforeDynamicHookTypes);
		addDynamicKeys(id, baseClass, overrideDynamicHookMethods, overrideDynamicHookTypes);
		addDynamicKeys(id, baseClass, afterDynamicHookMethods, afterDynamicHookTypes);

		initialize();

		for(IServerPlayerAPI instance : getAllInstancesList())
			instance.getServerPlayerAPI().attachServerPlayerBase(id);

		System.out.println("Server Player: registered " + id);
		logger.fine("Server Player: registered class '" + baseClass.getName() + "' with id '" + id + "'");

		initialized = false;
	}

	public static boolean unregister(String id)
	{
		if(id == null)
			return false;

		Constructor<?> constructor = allBaseConstructors.remove(id);
		if(constructor == null)
			return false;

		for(IServerPlayerAPI instance : getAllInstancesList())
			instance.getServerPlayerAPI().detachServerPlayerBase(id);

		beforeLocalConstructingHookTypes.remove(id);
		afterLocalConstructingHookTypes.remove(id);

		allBaseBeforeAddExhaustionSuperiors.remove(id);
		allBaseBeforeAddExhaustionInferiors.remove(id);
		allBaseOverrideAddExhaustionSuperiors.remove(id);
		allBaseOverrideAddExhaustionInferiors.remove(id);
		allBaseAfterAddExhaustionSuperiors.remove(id);
		allBaseAfterAddExhaustionInferiors.remove(id);

		beforeAddExhaustionHookTypes.remove(id);
		overrideAddExhaustionHookTypes.remove(id);
		afterAddExhaustionHookTypes.remove(id);

		allBaseBeforeAddExperienceSuperiors.remove(id);
		allBaseBeforeAddExperienceInferiors.remove(id);
		allBaseOverrideAddExperienceSuperiors.remove(id);
		allBaseOverrideAddExperienceInferiors.remove(id);
		allBaseAfterAddExperienceSuperiors.remove(id);
		allBaseAfterAddExperienceInferiors.remove(id);

		beforeAddExperienceHookTypes.remove(id);
		overrideAddExperienceHookTypes.remove(id);
		afterAddExperienceHookTypes.remove(id);

		allBaseBeforeAddExperienceLevelSuperiors.remove(id);
		allBaseBeforeAddExperienceLevelInferiors.remove(id);
		allBaseOverrideAddExperienceLevelSuperiors.remove(id);
		allBaseOverrideAddExperienceLevelInferiors.remove(id);
		allBaseAfterAddExperienceLevelSuperiors.remove(id);
		allBaseAfterAddExperienceLevelInferiors.remove(id);

		beforeAddExperienceLevelHookTypes.remove(id);
		overrideAddExperienceLevelHookTypes.remove(id);
		afterAddExperienceLevelHookTypes.remove(id);

		allBaseBeforeAddMovementStatSuperiors.remove(id);
		allBaseBeforeAddMovementStatInferiors.remove(id);
		allBaseOverrideAddMovementStatSuperiors.remove(id);
		allBaseOverrideAddMovementStatInferiors.remove(id);
		allBaseAfterAddMovementStatSuperiors.remove(id);
		allBaseAfterAddMovementStatInferiors.remove(id);

		beforeAddMovementStatHookTypes.remove(id);
		overrideAddMovementStatHookTypes.remove(id);
		afterAddMovementStatHookTypes.remove(id);

		allBaseBeforeAttackEntityFromSuperiors.remove(id);
		allBaseBeforeAttackEntityFromInferiors.remove(id);
		allBaseOverrideAttackEntityFromSuperiors.remove(id);
		allBaseOverrideAttackEntityFromInferiors.remove(id);
		allBaseAfterAttackEntityFromSuperiors.remove(id);
		allBaseAfterAttackEntityFromInferiors.remove(id);

		beforeAttackEntityFromHookTypes.remove(id);
		overrideAttackEntityFromHookTypes.remove(id);
		afterAttackEntityFromHookTypes.remove(id);

		allBaseBeforeAttackTargetEntityWithCurrentItemSuperiors.remove(id);
		allBaseBeforeAttackTargetEntityWithCurrentItemInferiors.remove(id);
		allBaseOverrideAttackTargetEntityWithCurrentItemSuperiors.remove(id);
		allBaseOverrideAttackTargetEntityWithCurrentItemInferiors.remove(id);
		allBaseAfterAttackTargetEntityWithCurrentItemSuperiors.remove(id);
		allBaseAfterAttackTargetEntityWithCurrentItemInferiors.remove(id);

		beforeAttackTargetEntityWithCurrentItemHookTypes.remove(id);
		overrideAttackTargetEntityWithCurrentItemHookTypes.remove(id);
		afterAttackTargetEntityWithCurrentItemHookTypes.remove(id);

		allBaseBeforeCanBreatheUnderwaterSuperiors.remove(id);
		allBaseBeforeCanBreatheUnderwaterInferiors.remove(id);
		allBaseOverrideCanBreatheUnderwaterSuperiors.remove(id);
		allBaseOverrideCanBreatheUnderwaterInferiors.remove(id);
		allBaseAfterCanBreatheUnderwaterSuperiors.remove(id);
		allBaseAfterCanBreatheUnderwaterInferiors.remove(id);

		beforeCanBreatheUnderwaterHookTypes.remove(id);
		overrideCanBreatheUnderwaterHookTypes.remove(id);
		afterCanBreatheUnderwaterHookTypes.remove(id);

		allBaseBeforeCanHarvestBlockSuperiors.remove(id);
		allBaseBeforeCanHarvestBlockInferiors.remove(id);
		allBaseOverrideCanHarvestBlockSuperiors.remove(id);
		allBaseOverrideCanHarvestBlockInferiors.remove(id);
		allBaseAfterCanHarvestBlockSuperiors.remove(id);
		allBaseAfterCanHarvestBlockInferiors.remove(id);

		beforeCanHarvestBlockHookTypes.remove(id);
		overrideCanHarvestBlockHookTypes.remove(id);
		afterCanHarvestBlockHookTypes.remove(id);

		allBaseBeforeCanPlayerEditSuperiors.remove(id);
		allBaseBeforeCanPlayerEditInferiors.remove(id);
		allBaseOverrideCanPlayerEditSuperiors.remove(id);
		allBaseOverrideCanPlayerEditInferiors.remove(id);
		allBaseAfterCanPlayerEditSuperiors.remove(id);
		allBaseAfterCanPlayerEditInferiors.remove(id);

		beforeCanPlayerEditHookTypes.remove(id);
		overrideCanPlayerEditHookTypes.remove(id);
		afterCanPlayerEditHookTypes.remove(id);

		allBaseBeforeCanTriggerWalkingSuperiors.remove(id);
		allBaseBeforeCanTriggerWalkingInferiors.remove(id);
		allBaseOverrideCanTriggerWalkingSuperiors.remove(id);
		allBaseOverrideCanTriggerWalkingInferiors.remove(id);
		allBaseAfterCanTriggerWalkingSuperiors.remove(id);
		allBaseAfterCanTriggerWalkingInferiors.remove(id);

		beforeCanTriggerWalkingHookTypes.remove(id);
		overrideCanTriggerWalkingHookTypes.remove(id);
		afterCanTriggerWalkingHookTypes.remove(id);

		allBaseBeforeClonePlayerSuperiors.remove(id);
		allBaseBeforeClonePlayerInferiors.remove(id);
		allBaseOverrideClonePlayerSuperiors.remove(id);
		allBaseOverrideClonePlayerInferiors.remove(id);
		allBaseAfterClonePlayerSuperiors.remove(id);
		allBaseAfterClonePlayerInferiors.remove(id);

		beforeClonePlayerHookTypes.remove(id);
		overrideClonePlayerHookTypes.remove(id);
		afterClonePlayerHookTypes.remove(id);

		allBaseBeforeDamageEntitySuperiors.remove(id);
		allBaseBeforeDamageEntityInferiors.remove(id);
		allBaseOverrideDamageEntitySuperiors.remove(id);
		allBaseOverrideDamageEntityInferiors.remove(id);
		allBaseAfterDamageEntitySuperiors.remove(id);
		allBaseAfterDamageEntityInferiors.remove(id);

		beforeDamageEntityHookTypes.remove(id);
		overrideDamageEntityHookTypes.remove(id);
		afterDamageEntityHookTypes.remove(id);

		allBaseBeforeDisplayGUIChestSuperiors.remove(id);
		allBaseBeforeDisplayGUIChestInferiors.remove(id);
		allBaseOverrideDisplayGUIChestSuperiors.remove(id);
		allBaseOverrideDisplayGUIChestInferiors.remove(id);
		allBaseAfterDisplayGUIChestSuperiors.remove(id);
		allBaseAfterDisplayGUIChestInferiors.remove(id);

		beforeDisplayGUIChestHookTypes.remove(id);
		overrideDisplayGUIChestHookTypes.remove(id);
		afterDisplayGUIChestHookTypes.remove(id);

		allBaseBeforeDisplayGUIDispenserSuperiors.remove(id);
		allBaseBeforeDisplayGUIDispenserInferiors.remove(id);
		allBaseOverrideDisplayGUIDispenserSuperiors.remove(id);
		allBaseOverrideDisplayGUIDispenserInferiors.remove(id);
		allBaseAfterDisplayGUIDispenserSuperiors.remove(id);
		allBaseAfterDisplayGUIDispenserInferiors.remove(id);

		beforeDisplayGUIDispenserHookTypes.remove(id);
		overrideDisplayGUIDispenserHookTypes.remove(id);
		afterDisplayGUIDispenserHookTypes.remove(id);

		allBaseBeforeDisplayGUIFurnaceSuperiors.remove(id);
		allBaseBeforeDisplayGUIFurnaceInferiors.remove(id);
		allBaseOverrideDisplayGUIFurnaceSuperiors.remove(id);
		allBaseOverrideDisplayGUIFurnaceInferiors.remove(id);
		allBaseAfterDisplayGUIFurnaceSuperiors.remove(id);
		allBaseAfterDisplayGUIFurnaceInferiors.remove(id);

		beforeDisplayGUIFurnaceHookTypes.remove(id);
		overrideDisplayGUIFurnaceHookTypes.remove(id);
		afterDisplayGUIFurnaceHookTypes.remove(id);

		allBaseBeforeDisplayGUIWorkbenchSuperiors.remove(id);
		allBaseBeforeDisplayGUIWorkbenchInferiors.remove(id);
		allBaseOverrideDisplayGUIWorkbenchSuperiors.remove(id);
		allBaseOverrideDisplayGUIWorkbenchInferiors.remove(id);
		allBaseAfterDisplayGUIWorkbenchSuperiors.remove(id);
		allBaseAfterDisplayGUIWorkbenchInferiors.remove(id);

		beforeDisplayGUIWorkbenchHookTypes.remove(id);
		overrideDisplayGUIWorkbenchHookTypes.remove(id);
		afterDisplayGUIWorkbenchHookTypes.remove(id);

		allBaseBeforeDropOneItemSuperiors.remove(id);
		allBaseBeforeDropOneItemInferiors.remove(id);
		allBaseOverrideDropOneItemSuperiors.remove(id);
		allBaseOverrideDropOneItemInferiors.remove(id);
		allBaseAfterDropOneItemSuperiors.remove(id);
		allBaseAfterDropOneItemInferiors.remove(id);

		beforeDropOneItemHookTypes.remove(id);
		overrideDropOneItemHookTypes.remove(id);
		afterDropOneItemHookTypes.remove(id);

		allBaseBeforeDropPlayerItemSuperiors.remove(id);
		allBaseBeforeDropPlayerItemInferiors.remove(id);
		allBaseOverrideDropPlayerItemSuperiors.remove(id);
		allBaseOverrideDropPlayerItemInferiors.remove(id);
		allBaseAfterDropPlayerItemSuperiors.remove(id);
		allBaseAfterDropPlayerItemInferiors.remove(id);

		beforeDropPlayerItemHookTypes.remove(id);
		overrideDropPlayerItemHookTypes.remove(id);
		afterDropPlayerItemHookTypes.remove(id);

		allBaseBeforeFallSuperiors.remove(id);
		allBaseBeforeFallInferiors.remove(id);
		allBaseOverrideFallSuperiors.remove(id);
		allBaseOverrideFallInferiors.remove(id);
		allBaseAfterFallSuperiors.remove(id);
		allBaseAfterFallInferiors.remove(id);

		beforeFallHookTypes.remove(id);
		overrideFallHookTypes.remove(id);
		afterFallHookTypes.remove(id);

		allBaseBeforeGetAIMoveSpeedSuperiors.remove(id);
		allBaseBeforeGetAIMoveSpeedInferiors.remove(id);
		allBaseOverrideGetAIMoveSpeedSuperiors.remove(id);
		allBaseOverrideGetAIMoveSpeedInferiors.remove(id);
		allBaseAfterGetAIMoveSpeedSuperiors.remove(id);
		allBaseAfterGetAIMoveSpeedInferiors.remove(id);

		beforeGetAIMoveSpeedHookTypes.remove(id);
		overrideGetAIMoveSpeedHookTypes.remove(id);
		afterGetAIMoveSpeedHookTypes.remove(id);

		allBaseBeforeGetCurrentPlayerStrVsBlockSuperiors.remove(id);
		allBaseBeforeGetCurrentPlayerStrVsBlockInferiors.remove(id);
		allBaseOverrideGetCurrentPlayerStrVsBlockSuperiors.remove(id);
		allBaseOverrideGetCurrentPlayerStrVsBlockInferiors.remove(id);
		allBaseAfterGetCurrentPlayerStrVsBlockSuperiors.remove(id);
		allBaseAfterGetCurrentPlayerStrVsBlockInferiors.remove(id);

		beforeGetCurrentPlayerStrVsBlockHookTypes.remove(id);
		overrideGetCurrentPlayerStrVsBlockHookTypes.remove(id);
		afterGetCurrentPlayerStrVsBlockHookTypes.remove(id);

		allBaseBeforeGetCurrentPlayerStrVsBlockForgeSuperiors.remove(id);
		allBaseBeforeGetCurrentPlayerStrVsBlockForgeInferiors.remove(id);
		allBaseOverrideGetCurrentPlayerStrVsBlockForgeSuperiors.remove(id);
		allBaseOverrideGetCurrentPlayerStrVsBlockForgeInferiors.remove(id);
		allBaseAfterGetCurrentPlayerStrVsBlockForgeSuperiors.remove(id);
		allBaseAfterGetCurrentPlayerStrVsBlockForgeInferiors.remove(id);

		beforeGetCurrentPlayerStrVsBlockForgeHookTypes.remove(id);
		overrideGetCurrentPlayerStrVsBlockForgeHookTypes.remove(id);
		afterGetCurrentPlayerStrVsBlockForgeHookTypes.remove(id);

		allBaseBeforeGetDistanceSqSuperiors.remove(id);
		allBaseBeforeGetDistanceSqInferiors.remove(id);
		allBaseOverrideGetDistanceSqSuperiors.remove(id);
		allBaseOverrideGetDistanceSqInferiors.remove(id);
		allBaseAfterGetDistanceSqSuperiors.remove(id);
		allBaseAfterGetDistanceSqInferiors.remove(id);

		beforeGetDistanceSqHookTypes.remove(id);
		overrideGetDistanceSqHookTypes.remove(id);
		afterGetDistanceSqHookTypes.remove(id);

		allBaseBeforeGetBrightnessSuperiors.remove(id);
		allBaseBeforeGetBrightnessInferiors.remove(id);
		allBaseOverrideGetBrightnessSuperiors.remove(id);
		allBaseOverrideGetBrightnessInferiors.remove(id);
		allBaseAfterGetBrightnessSuperiors.remove(id);
		allBaseAfterGetBrightnessInferiors.remove(id);

		beforeGetBrightnessHookTypes.remove(id);
		overrideGetBrightnessHookTypes.remove(id);
		afterGetBrightnessHookTypes.remove(id);

		allBaseBeforeGetEyeHeightSuperiors.remove(id);
		allBaseBeforeGetEyeHeightInferiors.remove(id);
		allBaseOverrideGetEyeHeightSuperiors.remove(id);
		allBaseOverrideGetEyeHeightInferiors.remove(id);
		allBaseAfterGetEyeHeightSuperiors.remove(id);
		allBaseAfterGetEyeHeightInferiors.remove(id);

		beforeGetEyeHeightHookTypes.remove(id);
		overrideGetEyeHeightHookTypes.remove(id);
		afterGetEyeHeightHookTypes.remove(id);

		allBaseBeforeHealSuperiors.remove(id);
		allBaseBeforeHealInferiors.remove(id);
		allBaseOverrideHealSuperiors.remove(id);
		allBaseOverrideHealInferiors.remove(id);
		allBaseAfterHealSuperiors.remove(id);
		allBaseAfterHealInferiors.remove(id);

		beforeHealHookTypes.remove(id);
		overrideHealHookTypes.remove(id);
		afterHealHookTypes.remove(id);

		allBaseBeforeIsEntityInsideOpaqueBlockSuperiors.remove(id);
		allBaseBeforeIsEntityInsideOpaqueBlockInferiors.remove(id);
		allBaseOverrideIsEntityInsideOpaqueBlockSuperiors.remove(id);
		allBaseOverrideIsEntityInsideOpaqueBlockInferiors.remove(id);
		allBaseAfterIsEntityInsideOpaqueBlockSuperiors.remove(id);
		allBaseAfterIsEntityInsideOpaqueBlockInferiors.remove(id);

		beforeIsEntityInsideOpaqueBlockHookTypes.remove(id);
		overrideIsEntityInsideOpaqueBlockHookTypes.remove(id);
		afterIsEntityInsideOpaqueBlockHookTypes.remove(id);

		allBaseBeforeIsInWaterSuperiors.remove(id);
		allBaseBeforeIsInWaterInferiors.remove(id);
		allBaseOverrideIsInWaterSuperiors.remove(id);
		allBaseOverrideIsInWaterInferiors.remove(id);
		allBaseAfterIsInWaterSuperiors.remove(id);
		allBaseAfterIsInWaterInferiors.remove(id);

		beforeIsInWaterHookTypes.remove(id);
		overrideIsInWaterHookTypes.remove(id);
		afterIsInWaterHookTypes.remove(id);

		allBaseBeforeIsInsideOfMaterialSuperiors.remove(id);
		allBaseBeforeIsInsideOfMaterialInferiors.remove(id);
		allBaseOverrideIsInsideOfMaterialSuperiors.remove(id);
		allBaseOverrideIsInsideOfMaterialInferiors.remove(id);
		allBaseAfterIsInsideOfMaterialSuperiors.remove(id);
		allBaseAfterIsInsideOfMaterialInferiors.remove(id);

		beforeIsInsideOfMaterialHookTypes.remove(id);
		overrideIsInsideOfMaterialHookTypes.remove(id);
		afterIsInsideOfMaterialHookTypes.remove(id);

		allBaseBeforeIsOnLadderSuperiors.remove(id);
		allBaseBeforeIsOnLadderInferiors.remove(id);
		allBaseOverrideIsOnLadderSuperiors.remove(id);
		allBaseOverrideIsOnLadderInferiors.remove(id);
		allBaseAfterIsOnLadderSuperiors.remove(id);
		allBaseAfterIsOnLadderInferiors.remove(id);

		beforeIsOnLadderHookTypes.remove(id);
		overrideIsOnLadderHookTypes.remove(id);
		afterIsOnLadderHookTypes.remove(id);

		allBaseBeforeIsPlayerSleepingSuperiors.remove(id);
		allBaseBeforeIsPlayerSleepingInferiors.remove(id);
		allBaseOverrideIsPlayerSleepingSuperiors.remove(id);
		allBaseOverrideIsPlayerSleepingInferiors.remove(id);
		allBaseAfterIsPlayerSleepingSuperiors.remove(id);
		allBaseAfterIsPlayerSleepingInferiors.remove(id);

		beforeIsPlayerSleepingHookTypes.remove(id);
		overrideIsPlayerSleepingHookTypes.remove(id);
		afterIsPlayerSleepingHookTypes.remove(id);

		allBaseBeforeIsSneakingSuperiors.remove(id);
		allBaseBeforeIsSneakingInferiors.remove(id);
		allBaseOverrideIsSneakingSuperiors.remove(id);
		allBaseOverrideIsSneakingInferiors.remove(id);
		allBaseAfterIsSneakingSuperiors.remove(id);
		allBaseAfterIsSneakingInferiors.remove(id);

		beforeIsSneakingHookTypes.remove(id);
		overrideIsSneakingHookTypes.remove(id);
		afterIsSneakingHookTypes.remove(id);

		allBaseBeforeJumpSuperiors.remove(id);
		allBaseBeforeJumpInferiors.remove(id);
		allBaseOverrideJumpSuperiors.remove(id);
		allBaseOverrideJumpInferiors.remove(id);
		allBaseAfterJumpSuperiors.remove(id);
		allBaseAfterJumpInferiors.remove(id);

		beforeJumpHookTypes.remove(id);
		overrideJumpHookTypes.remove(id);
		afterJumpHookTypes.remove(id);

		allBaseBeforeKnockBackSuperiors.remove(id);
		allBaseBeforeKnockBackInferiors.remove(id);
		allBaseOverrideKnockBackSuperiors.remove(id);
		allBaseOverrideKnockBackInferiors.remove(id);
		allBaseAfterKnockBackSuperiors.remove(id);
		allBaseAfterKnockBackInferiors.remove(id);

		beforeKnockBackHookTypes.remove(id);
		overrideKnockBackHookTypes.remove(id);
		afterKnockBackHookTypes.remove(id);

		allBaseBeforeMountEntitySuperiors.remove(id);
		allBaseBeforeMountEntityInferiors.remove(id);
		allBaseOverrideMountEntitySuperiors.remove(id);
		allBaseOverrideMountEntityInferiors.remove(id);
		allBaseAfterMountEntitySuperiors.remove(id);
		allBaseAfterMountEntityInferiors.remove(id);

		beforeMountEntityHookTypes.remove(id);
		overrideMountEntityHookTypes.remove(id);
		afterMountEntityHookTypes.remove(id);

		allBaseBeforeMoveEntitySuperiors.remove(id);
		allBaseBeforeMoveEntityInferiors.remove(id);
		allBaseOverrideMoveEntitySuperiors.remove(id);
		allBaseOverrideMoveEntityInferiors.remove(id);
		allBaseAfterMoveEntitySuperiors.remove(id);
		allBaseAfterMoveEntityInferiors.remove(id);

		beforeMoveEntityHookTypes.remove(id);
		overrideMoveEntityHookTypes.remove(id);
		afterMoveEntityHookTypes.remove(id);

		allBaseBeforeMoveEntityWithHeadingSuperiors.remove(id);
		allBaseBeforeMoveEntityWithHeadingInferiors.remove(id);
		allBaseOverrideMoveEntityWithHeadingSuperiors.remove(id);
		allBaseOverrideMoveEntityWithHeadingInferiors.remove(id);
		allBaseAfterMoveEntityWithHeadingSuperiors.remove(id);
		allBaseAfterMoveEntityWithHeadingInferiors.remove(id);

		beforeMoveEntityWithHeadingHookTypes.remove(id);
		overrideMoveEntityWithHeadingHookTypes.remove(id);
		afterMoveEntityWithHeadingHookTypes.remove(id);

		allBaseBeforeMoveFlyingSuperiors.remove(id);
		allBaseBeforeMoveFlyingInferiors.remove(id);
		allBaseOverrideMoveFlyingSuperiors.remove(id);
		allBaseOverrideMoveFlyingInferiors.remove(id);
		allBaseAfterMoveFlyingSuperiors.remove(id);
		allBaseAfterMoveFlyingInferiors.remove(id);

		beforeMoveFlyingHookTypes.remove(id);
		overrideMoveFlyingHookTypes.remove(id);
		afterMoveFlyingHookTypes.remove(id);

		allBaseBeforeOnDeathSuperiors.remove(id);
		allBaseBeforeOnDeathInferiors.remove(id);
		allBaseOverrideOnDeathSuperiors.remove(id);
		allBaseOverrideOnDeathInferiors.remove(id);
		allBaseAfterOnDeathSuperiors.remove(id);
		allBaseAfterOnDeathInferiors.remove(id);

		beforeOnDeathHookTypes.remove(id);
		overrideOnDeathHookTypes.remove(id);
		afterOnDeathHookTypes.remove(id);

		allBaseBeforeOnLivingUpdateSuperiors.remove(id);
		allBaseBeforeOnLivingUpdateInferiors.remove(id);
		allBaseOverrideOnLivingUpdateSuperiors.remove(id);
		allBaseOverrideOnLivingUpdateInferiors.remove(id);
		allBaseAfterOnLivingUpdateSuperiors.remove(id);
		allBaseAfterOnLivingUpdateInferiors.remove(id);

		beforeOnLivingUpdateHookTypes.remove(id);
		overrideOnLivingUpdateHookTypes.remove(id);
		afterOnLivingUpdateHookTypes.remove(id);

		allBaseBeforeOnKillEntitySuperiors.remove(id);
		allBaseBeforeOnKillEntityInferiors.remove(id);
		allBaseOverrideOnKillEntitySuperiors.remove(id);
		allBaseOverrideOnKillEntityInferiors.remove(id);
		allBaseAfterOnKillEntitySuperiors.remove(id);
		allBaseAfterOnKillEntityInferiors.remove(id);

		beforeOnKillEntityHookTypes.remove(id);
		overrideOnKillEntityHookTypes.remove(id);
		afterOnKillEntityHookTypes.remove(id);

		allBaseBeforeOnStruckByLightningSuperiors.remove(id);
		allBaseBeforeOnStruckByLightningInferiors.remove(id);
		allBaseOverrideOnStruckByLightningSuperiors.remove(id);
		allBaseOverrideOnStruckByLightningInferiors.remove(id);
		allBaseAfterOnStruckByLightningSuperiors.remove(id);
		allBaseAfterOnStruckByLightningInferiors.remove(id);

		beforeOnStruckByLightningHookTypes.remove(id);
		overrideOnStruckByLightningHookTypes.remove(id);
		afterOnStruckByLightningHookTypes.remove(id);

		allBaseBeforeOnUpdateSuperiors.remove(id);
		allBaseBeforeOnUpdateInferiors.remove(id);
		allBaseOverrideOnUpdateSuperiors.remove(id);
		allBaseOverrideOnUpdateInferiors.remove(id);
		allBaseAfterOnUpdateSuperiors.remove(id);
		allBaseAfterOnUpdateInferiors.remove(id);

		beforeOnUpdateHookTypes.remove(id);
		overrideOnUpdateHookTypes.remove(id);
		afterOnUpdateHookTypes.remove(id);

		allBaseBeforeOnUpdateEntitySuperiors.remove(id);
		allBaseBeforeOnUpdateEntityInferiors.remove(id);
		allBaseOverrideOnUpdateEntitySuperiors.remove(id);
		allBaseOverrideOnUpdateEntityInferiors.remove(id);
		allBaseAfterOnUpdateEntitySuperiors.remove(id);
		allBaseAfterOnUpdateEntityInferiors.remove(id);

		beforeOnUpdateEntityHookTypes.remove(id);
		overrideOnUpdateEntityHookTypes.remove(id);
		afterOnUpdateEntityHookTypes.remove(id);

		allBaseBeforeReadEntityFromNBTSuperiors.remove(id);
		allBaseBeforeReadEntityFromNBTInferiors.remove(id);
		allBaseOverrideReadEntityFromNBTSuperiors.remove(id);
		allBaseOverrideReadEntityFromNBTInferiors.remove(id);
		allBaseAfterReadEntityFromNBTSuperiors.remove(id);
		allBaseAfterReadEntityFromNBTInferiors.remove(id);

		beforeReadEntityFromNBTHookTypes.remove(id);
		overrideReadEntityFromNBTHookTypes.remove(id);
		afterReadEntityFromNBTHookTypes.remove(id);

		allBaseBeforeSetDeadSuperiors.remove(id);
		allBaseBeforeSetDeadInferiors.remove(id);
		allBaseOverrideSetDeadSuperiors.remove(id);
		allBaseOverrideSetDeadInferiors.remove(id);
		allBaseAfterSetDeadSuperiors.remove(id);
		allBaseAfterSetDeadInferiors.remove(id);

		beforeSetDeadHookTypes.remove(id);
		overrideSetDeadHookTypes.remove(id);
		afterSetDeadHookTypes.remove(id);

		allBaseBeforeSetEntityActionStateSuperiors.remove(id);
		allBaseBeforeSetEntityActionStateInferiors.remove(id);
		allBaseOverrideSetEntityActionStateSuperiors.remove(id);
		allBaseOverrideSetEntityActionStateInferiors.remove(id);
		allBaseAfterSetEntityActionStateSuperiors.remove(id);
		allBaseAfterSetEntityActionStateInferiors.remove(id);

		beforeSetEntityActionStateHookTypes.remove(id);
		overrideSetEntityActionStateHookTypes.remove(id);
		afterSetEntityActionStateHookTypes.remove(id);

		allBaseBeforeSetPositionSuperiors.remove(id);
		allBaseBeforeSetPositionInferiors.remove(id);
		allBaseOverrideSetPositionSuperiors.remove(id);
		allBaseOverrideSetPositionInferiors.remove(id);
		allBaseAfterSetPositionSuperiors.remove(id);
		allBaseAfterSetPositionInferiors.remove(id);

		beforeSetPositionHookTypes.remove(id);
		overrideSetPositionHookTypes.remove(id);
		afterSetPositionHookTypes.remove(id);

		allBaseBeforeSetSneakingSuperiors.remove(id);
		allBaseBeforeSetSneakingInferiors.remove(id);
		allBaseOverrideSetSneakingSuperiors.remove(id);
		allBaseOverrideSetSneakingInferiors.remove(id);
		allBaseAfterSetSneakingSuperiors.remove(id);
		allBaseAfterSetSneakingInferiors.remove(id);

		beforeSetSneakingHookTypes.remove(id);
		overrideSetSneakingHookTypes.remove(id);
		afterSetSneakingHookTypes.remove(id);

		allBaseBeforeSetSprintingSuperiors.remove(id);
		allBaseBeforeSetSprintingInferiors.remove(id);
		allBaseOverrideSetSprintingSuperiors.remove(id);
		allBaseOverrideSetSprintingInferiors.remove(id);
		allBaseAfterSetSprintingSuperiors.remove(id);
		allBaseAfterSetSprintingInferiors.remove(id);

		beforeSetSprintingHookTypes.remove(id);
		overrideSetSprintingHookTypes.remove(id);
		afterSetSprintingHookTypes.remove(id);

		allBaseBeforeSwingItemSuperiors.remove(id);
		allBaseBeforeSwingItemInferiors.remove(id);
		allBaseOverrideSwingItemSuperiors.remove(id);
		allBaseOverrideSwingItemInferiors.remove(id);
		allBaseAfterSwingItemSuperiors.remove(id);
		allBaseAfterSwingItemInferiors.remove(id);

		beforeSwingItemHookTypes.remove(id);
		overrideSwingItemHookTypes.remove(id);
		afterSwingItemHookTypes.remove(id);

		allBaseBeforeUpdateEntityActionStateSuperiors.remove(id);
		allBaseBeforeUpdateEntityActionStateInferiors.remove(id);
		allBaseOverrideUpdateEntityActionStateSuperiors.remove(id);
		allBaseOverrideUpdateEntityActionStateInferiors.remove(id);
		allBaseAfterUpdateEntityActionStateSuperiors.remove(id);
		allBaseAfterUpdateEntityActionStateInferiors.remove(id);

		beforeUpdateEntityActionStateHookTypes.remove(id);
		overrideUpdateEntityActionStateHookTypes.remove(id);
		afterUpdateEntityActionStateHookTypes.remove(id);

		allBaseBeforeUpdatePotionEffectsSuperiors.remove(id);
		allBaseBeforeUpdatePotionEffectsInferiors.remove(id);
		allBaseOverrideUpdatePotionEffectsSuperiors.remove(id);
		allBaseOverrideUpdatePotionEffectsInferiors.remove(id);
		allBaseAfterUpdatePotionEffectsSuperiors.remove(id);
		allBaseAfterUpdatePotionEffectsInferiors.remove(id);

		beforeUpdatePotionEffectsHookTypes.remove(id);
		overrideUpdatePotionEffectsHookTypes.remove(id);
		afterUpdatePotionEffectsHookTypes.remove(id);

		allBaseBeforeUpdateRiddenSuperiors.remove(id);
		allBaseBeforeUpdateRiddenInferiors.remove(id);
		allBaseOverrideUpdateRiddenSuperiors.remove(id);
		allBaseOverrideUpdateRiddenInferiors.remove(id);
		allBaseAfterUpdateRiddenSuperiors.remove(id);
		allBaseAfterUpdateRiddenInferiors.remove(id);

		beforeUpdateRiddenHookTypes.remove(id);
		overrideUpdateRiddenHookTypes.remove(id);
		afterUpdateRiddenHookTypes.remove(id);

		allBaseBeforeWakeUpPlayerSuperiors.remove(id);
		allBaseBeforeWakeUpPlayerInferiors.remove(id);
		allBaseOverrideWakeUpPlayerSuperiors.remove(id);
		allBaseOverrideWakeUpPlayerInferiors.remove(id);
		allBaseAfterWakeUpPlayerSuperiors.remove(id);
		allBaseAfterWakeUpPlayerInferiors.remove(id);

		beforeWakeUpPlayerHookTypes.remove(id);
		overrideWakeUpPlayerHookTypes.remove(id);
		afterWakeUpPlayerHookTypes.remove(id);

		allBaseBeforeWriteEntityToNBTSuperiors.remove(id);
		allBaseBeforeWriteEntityToNBTInferiors.remove(id);
		allBaseOverrideWriteEntityToNBTSuperiors.remove(id);
		allBaseOverrideWriteEntityToNBTInferiors.remove(id);
		allBaseAfterWriteEntityToNBTSuperiors.remove(id);
		allBaseAfterWriteEntityToNBTInferiors.remove(id);

		beforeWriteEntityToNBTHookTypes.remove(id);
		overrideWriteEntityToNBTHookTypes.remove(id);
		afterWriteEntityToNBTHookTypes.remove(id);

		for(IServerPlayerAPI instance : getAllInstancesList())
			instance.getServerPlayerAPI().updateServerPlayerBases();

		Iterator<String> iterator = keysToVirtualIds.keySet().iterator();
		while(iterator.hasNext())
		{
			String key = iterator.next();
			if(keysToVirtualIds.get(key).equals(id))
				keysToVirtualIds.remove(key);
		}

		boolean otherFound = false;
		Class<?> type = constructor.getDeclaringClass();

		iterator = allBaseConstructors.keySet().iterator();
		while(iterator.hasNext())
		{
			String otherId = iterator.next();
			Class<?> otherType = allBaseConstructors.get(otherId).getDeclaringClass();
			if(!otherId.equals(id) && otherType.equals(type))
			{
				otherFound = true;
				break;
			}
		}

		if(!otherFound)
		{
			dynamicTypes.remove(type);

			virtualDynamicHookMethods.remove(type);

			beforeDynamicHookMethods.remove(type);
			overrideDynamicHookMethods.remove(type);
			afterDynamicHookMethods.remove(type);
		}

		removeDynamicHookTypes(id, beforeDynamicHookTypes);
		removeDynamicHookTypes(id, overrideDynamicHookTypes);
		removeDynamicHookTypes(id, afterDynamicHookTypes);

		allBaseBeforeDynamicSuperiors.remove(id);
		allBaseBeforeDynamicInferiors.remove(id);
		allBaseOverrideDynamicSuperiors.remove(id);
		allBaseOverrideDynamicInferiors.remove(id);
		allBaseAfterDynamicSuperiors.remove(id);
		allBaseAfterDynamicInferiors.remove(id);

		log("ServerPlayerAPI: unregistered id '" + id + "'");

		return true;
	}

	public static void removeDynamicHookTypes(String id, Map<String, List<String>> map)
	{
		Iterator<String> keys = map.keySet().iterator();
		while(keys.hasNext())
			map.get(keys.next()).remove(id);
	}

	public static Set<String> getRegisteredIds()
	{
		return unmodifiableAllIds;
	}

	private static void addSorting(String id, Map<String, String[]> map, String[] values)
	{
		if(values != null && values.length > 0)
			map.put(id, values);
	}

	private static void addDynamicSorting(String id, Map<String, Map<String, String[]>> map, Map<String, String[]> values)
	{
		if(values != null && values.size() > 0)
			map.put(id, values);
	}

	private static boolean addMethod(String id, Class<?> baseClass, List<String> list, String methodName, Class<?>... _parameterTypes)
	{
		try
		{
			Method method = baseClass.getMethod(methodName, _parameterTypes);
			boolean isOverridden = method.getDeclaringClass() != ServerPlayerBase.class;
			if(isOverridden)
				list.add(id);
			return isOverridden;
		}
		catch(Exception e)
		{
			throw new RuntimeException("Can not reflect method '" + methodName + "' of class '" + baseClass.getName() + "'", e);
		}
	}

	private static void addDynamicMethods(String id, Class<?> baseClass)
	{
		if(!dynamicTypes.add(baseClass))
			return;

		Map<String, Method> virtuals = null;
		Map<String, Method> befores = null;
		Map<String, Method> overrides = null;
		Map<String, Method> afters = null;

		Method[] methods = baseClass.getDeclaredMethods();
		for(int i=0; i<methods.length; i++)
		{
			Method method = methods[i];
			if(method.getDeclaringClass() != baseClass)
				continue;

			int modifiers = method.getModifiers();
			if(Modifier.isAbstract(modifiers))
				continue;

			if(Modifier.isStatic(modifiers))
				continue;

			String name = method.getName();
			if(name.length() < 7 || !name.substring(0, 7).equalsIgnoreCase("dynamic"))
				continue;
			else
				name = name.substring(7);

			while(name.charAt(0) == '_')
				name = name.substring(1);

			boolean before = false;
			boolean virtual = false;
			boolean override = false;
			boolean after = false;

			if(name.substring(0, 7).equalsIgnoreCase("virtual"))
			{
				virtual = true;
				name = name.substring(7);
			}
			else
			{
				if(name.length() >= 8 && name.substring(0, 8).equalsIgnoreCase("override"))
				{
					name = name.substring(8);
					override = true;
				}
				else if(name.length() >= 6 && name.substring(0, 6).equalsIgnoreCase("before"))
				{
					before = true;
					name = name.substring(6);
				}
				else if(name.length() >= 5 && name.substring(0, 5).equalsIgnoreCase("after"))
				{
					after = true;
					name = name.substring(5);
				}
			}

			if(name.length() >= 1 && (before || virtual || override || after))
				name = name.substring(0,1).toLowerCase() + name.substring(1);

			while(name.charAt(0) == '_')
				name = name.substring(1);

			if(name.length() == 0)
				throw new RuntimeException("Can not process dynamic hook method with no key");

			keys.add(name);

			if(virtual)
			{
				if(keysToVirtualIds.containsKey(name))
					throw new RuntimeException("Can not process more than one dynamic virtual method");

				keysToVirtualIds.put(name, id);
				virtuals = addDynamicMethod(name, method, virtuals);
			}
			else if(before)
				befores = addDynamicMethod(name, method, befores);
			else if(after)
				afters = addDynamicMethod(name, method, afters);
			else
				overrides = addDynamicMethod(name, method, overrides);
		}

		if(virtuals != null)
			virtualDynamicHookMethods.put(baseClass, virtuals);
		if(befores != null)
			beforeDynamicHookMethods.put(baseClass, befores);
		if(overrides != null)
			overrideDynamicHookMethods.put(baseClass, overrides);
		if(afters != null)
			afterDynamicHookMethods.put(baseClass, afters);
	}

	private static void addDynamicKeys(String id, Class<?> baseClass,  Map<Class<?>, Map<String, Method>> dynamicHookMethods, Map<String, List<String>> dynamicHookTypes)
	{
		Map<String, Method> methods = dynamicHookMethods.get(baseClass);
		if(methods == null || methods.size() == 0)
			return;

		Iterator<String> keys = methods.keySet().iterator();
		while(keys.hasNext())
		{
			String key = keys.next();
			if(!dynamicHookTypes.containsKey(key))
				dynamicHookTypes.put(key, new ArrayList<String>(1));
			dynamicHookTypes.get(key).add(id);
		}
	}

	private static Map<String, Method> addDynamicMethod(String key, Method method, Map<String, Method> methods)
	{
		if(methods == null)
			methods = new HashMap<String, Method>();
		if(methods.containsKey(key))
			throw new RuntimeException("method with key '" + key + "' allready exists");
		methods.put(key, method);
		return methods;
	}

	public static ServerPlayerAPI create(IServerPlayerAPI serverPlayer)
	{
		if(allBaseConstructors.size() > 0 && !initialized)
			initialize();
		return new ServerPlayerAPI(serverPlayer);
	}

	private static void initialize()
	{
		sortBases(beforeLocalConstructingHookTypes, allBaseBeforeLocalConstructingSuperiors, allBaseBeforeLocalConstructingInferiors, "beforeLocalConstructing");
		sortBases(afterLocalConstructingHookTypes, allBaseAfterLocalConstructingSuperiors, allBaseAfterLocalConstructingInferiors, "afterLocalConstructing");

		Iterator<String> keyIterator = keys.iterator();
		while(keyIterator.hasNext())
		{
			String key = keyIterator.next();
			sortDynamicBases(beforeDynamicHookTypes, allBaseBeforeDynamicSuperiors, allBaseBeforeDynamicInferiors, key);
			sortDynamicBases(overrideDynamicHookTypes, allBaseOverrideDynamicSuperiors, allBaseOverrideDynamicInferiors, key);
			sortDynamicBases(afterDynamicHookTypes, allBaseAfterDynamicSuperiors, allBaseAfterDynamicInferiors, key);
		}

		sortBases(beforeAddExhaustionHookTypes, allBaseBeforeAddExhaustionSuperiors, allBaseBeforeAddExhaustionInferiors, "beforeAddExhaustion");
		sortBases(overrideAddExhaustionHookTypes, allBaseOverrideAddExhaustionSuperiors, allBaseOverrideAddExhaustionInferiors, "overrideAddExhaustion");
		sortBases(afterAddExhaustionHookTypes, allBaseAfterAddExhaustionSuperiors, allBaseAfterAddExhaustionInferiors, "afterAddExhaustion");

		sortBases(beforeAddExperienceHookTypes, allBaseBeforeAddExperienceSuperiors, allBaseBeforeAddExperienceInferiors, "beforeAddExperience");
		sortBases(overrideAddExperienceHookTypes, allBaseOverrideAddExperienceSuperiors, allBaseOverrideAddExperienceInferiors, "overrideAddExperience");
		sortBases(afterAddExperienceHookTypes, allBaseAfterAddExperienceSuperiors, allBaseAfterAddExperienceInferiors, "afterAddExperience");

		sortBases(beforeAddExperienceLevelHookTypes, allBaseBeforeAddExperienceLevelSuperiors, allBaseBeforeAddExperienceLevelInferiors, "beforeAddExperienceLevel");
		sortBases(overrideAddExperienceLevelHookTypes, allBaseOverrideAddExperienceLevelSuperiors, allBaseOverrideAddExperienceLevelInferiors, "overrideAddExperienceLevel");
		sortBases(afterAddExperienceLevelHookTypes, allBaseAfterAddExperienceLevelSuperiors, allBaseAfterAddExperienceLevelInferiors, "afterAddExperienceLevel");

		sortBases(beforeAddMovementStatHookTypes, allBaseBeforeAddMovementStatSuperiors, allBaseBeforeAddMovementStatInferiors, "beforeAddMovementStat");
		sortBases(overrideAddMovementStatHookTypes, allBaseOverrideAddMovementStatSuperiors, allBaseOverrideAddMovementStatInferiors, "overrideAddMovementStat");
		sortBases(afterAddMovementStatHookTypes, allBaseAfterAddMovementStatSuperiors, allBaseAfterAddMovementStatInferiors, "afterAddMovementStat");

		sortBases(beforeAttackEntityFromHookTypes, allBaseBeforeAttackEntityFromSuperiors, allBaseBeforeAttackEntityFromInferiors, "beforeAttackEntityFrom");
		sortBases(overrideAttackEntityFromHookTypes, allBaseOverrideAttackEntityFromSuperiors, allBaseOverrideAttackEntityFromInferiors, "overrideAttackEntityFrom");
		sortBases(afterAttackEntityFromHookTypes, allBaseAfterAttackEntityFromSuperiors, allBaseAfterAttackEntityFromInferiors, "afterAttackEntityFrom");

		sortBases(beforeAttackTargetEntityWithCurrentItemHookTypes, allBaseBeforeAttackTargetEntityWithCurrentItemSuperiors, allBaseBeforeAttackTargetEntityWithCurrentItemInferiors, "beforeAttackTargetEntityWithCurrentItem");
		sortBases(overrideAttackTargetEntityWithCurrentItemHookTypes, allBaseOverrideAttackTargetEntityWithCurrentItemSuperiors, allBaseOverrideAttackTargetEntityWithCurrentItemInferiors, "overrideAttackTargetEntityWithCurrentItem");
		sortBases(afterAttackTargetEntityWithCurrentItemHookTypes, allBaseAfterAttackTargetEntityWithCurrentItemSuperiors, allBaseAfterAttackTargetEntityWithCurrentItemInferiors, "afterAttackTargetEntityWithCurrentItem");

		sortBases(beforeCanBreatheUnderwaterHookTypes, allBaseBeforeCanBreatheUnderwaterSuperiors, allBaseBeforeCanBreatheUnderwaterInferiors, "beforeCanBreatheUnderwater");
		sortBases(overrideCanBreatheUnderwaterHookTypes, allBaseOverrideCanBreatheUnderwaterSuperiors, allBaseOverrideCanBreatheUnderwaterInferiors, "overrideCanBreatheUnderwater");
		sortBases(afterCanBreatheUnderwaterHookTypes, allBaseAfterCanBreatheUnderwaterSuperiors, allBaseAfterCanBreatheUnderwaterInferiors, "afterCanBreatheUnderwater");

		sortBases(beforeCanHarvestBlockHookTypes, allBaseBeforeCanHarvestBlockSuperiors, allBaseBeforeCanHarvestBlockInferiors, "beforeCanHarvestBlock");
		sortBases(overrideCanHarvestBlockHookTypes, allBaseOverrideCanHarvestBlockSuperiors, allBaseOverrideCanHarvestBlockInferiors, "overrideCanHarvestBlock");
		sortBases(afterCanHarvestBlockHookTypes, allBaseAfterCanHarvestBlockSuperiors, allBaseAfterCanHarvestBlockInferiors, "afterCanHarvestBlock");

		sortBases(beforeCanPlayerEditHookTypes, allBaseBeforeCanPlayerEditSuperiors, allBaseBeforeCanPlayerEditInferiors, "beforeCanPlayerEdit");
		sortBases(overrideCanPlayerEditHookTypes, allBaseOverrideCanPlayerEditSuperiors, allBaseOverrideCanPlayerEditInferiors, "overrideCanPlayerEdit");
		sortBases(afterCanPlayerEditHookTypes, allBaseAfterCanPlayerEditSuperiors, allBaseAfterCanPlayerEditInferiors, "afterCanPlayerEdit");

		sortBases(beforeCanTriggerWalkingHookTypes, allBaseBeforeCanTriggerWalkingSuperiors, allBaseBeforeCanTriggerWalkingInferiors, "beforeCanTriggerWalking");
		sortBases(overrideCanTriggerWalkingHookTypes, allBaseOverrideCanTriggerWalkingSuperiors, allBaseOverrideCanTriggerWalkingInferiors, "overrideCanTriggerWalking");
		sortBases(afterCanTriggerWalkingHookTypes, allBaseAfterCanTriggerWalkingSuperiors, allBaseAfterCanTriggerWalkingInferiors, "afterCanTriggerWalking");

		sortBases(beforeClonePlayerHookTypes, allBaseBeforeClonePlayerSuperiors, allBaseBeforeClonePlayerInferiors, "beforeClonePlayer");
		sortBases(overrideClonePlayerHookTypes, allBaseOverrideClonePlayerSuperiors, allBaseOverrideClonePlayerInferiors, "overrideClonePlayer");
		sortBases(afterClonePlayerHookTypes, allBaseAfterClonePlayerSuperiors, allBaseAfterClonePlayerInferiors, "afterClonePlayer");

		sortBases(beforeDamageEntityHookTypes, allBaseBeforeDamageEntitySuperiors, allBaseBeforeDamageEntityInferiors, "beforeDamageEntity");
		sortBases(overrideDamageEntityHookTypes, allBaseOverrideDamageEntitySuperiors, allBaseOverrideDamageEntityInferiors, "overrideDamageEntity");
		sortBases(afterDamageEntityHookTypes, allBaseAfterDamageEntitySuperiors, allBaseAfterDamageEntityInferiors, "afterDamageEntity");

		sortBases(beforeDisplayGUIChestHookTypes, allBaseBeforeDisplayGUIChestSuperiors, allBaseBeforeDisplayGUIChestInferiors, "beforeDisplayGUIChest");
		sortBases(overrideDisplayGUIChestHookTypes, allBaseOverrideDisplayGUIChestSuperiors, allBaseOverrideDisplayGUIChestInferiors, "overrideDisplayGUIChest");
		sortBases(afterDisplayGUIChestHookTypes, allBaseAfterDisplayGUIChestSuperiors, allBaseAfterDisplayGUIChestInferiors, "afterDisplayGUIChest");

		sortBases(beforeDisplayGUIDispenserHookTypes, allBaseBeforeDisplayGUIDispenserSuperiors, allBaseBeforeDisplayGUIDispenserInferiors, "beforeDisplayGUIDispenser");
		sortBases(overrideDisplayGUIDispenserHookTypes, allBaseOverrideDisplayGUIDispenserSuperiors, allBaseOverrideDisplayGUIDispenserInferiors, "overrideDisplayGUIDispenser");
		sortBases(afterDisplayGUIDispenserHookTypes, allBaseAfterDisplayGUIDispenserSuperiors, allBaseAfterDisplayGUIDispenserInferiors, "afterDisplayGUIDispenser");

		sortBases(beforeDisplayGUIFurnaceHookTypes, allBaseBeforeDisplayGUIFurnaceSuperiors, allBaseBeforeDisplayGUIFurnaceInferiors, "beforeDisplayGUIFurnace");
		sortBases(overrideDisplayGUIFurnaceHookTypes, allBaseOverrideDisplayGUIFurnaceSuperiors, allBaseOverrideDisplayGUIFurnaceInferiors, "overrideDisplayGUIFurnace");
		sortBases(afterDisplayGUIFurnaceHookTypes, allBaseAfterDisplayGUIFurnaceSuperiors, allBaseAfterDisplayGUIFurnaceInferiors, "afterDisplayGUIFurnace");

		sortBases(beforeDisplayGUIWorkbenchHookTypes, allBaseBeforeDisplayGUIWorkbenchSuperiors, allBaseBeforeDisplayGUIWorkbenchInferiors, "beforeDisplayGUIWorkbench");
		sortBases(overrideDisplayGUIWorkbenchHookTypes, allBaseOverrideDisplayGUIWorkbenchSuperiors, allBaseOverrideDisplayGUIWorkbenchInferiors, "overrideDisplayGUIWorkbench");
		sortBases(afterDisplayGUIWorkbenchHookTypes, allBaseAfterDisplayGUIWorkbenchSuperiors, allBaseAfterDisplayGUIWorkbenchInferiors, "afterDisplayGUIWorkbench");

		sortBases(beforeDropOneItemHookTypes, allBaseBeforeDropOneItemSuperiors, allBaseBeforeDropOneItemInferiors, "beforeDropOneItem");
		sortBases(overrideDropOneItemHookTypes, allBaseOverrideDropOneItemSuperiors, allBaseOverrideDropOneItemInferiors, "overrideDropOneItem");
		sortBases(afterDropOneItemHookTypes, allBaseAfterDropOneItemSuperiors, allBaseAfterDropOneItemInferiors, "afterDropOneItem");

		sortBases(beforeDropPlayerItemHookTypes, allBaseBeforeDropPlayerItemSuperiors, allBaseBeforeDropPlayerItemInferiors, "beforeDropPlayerItem");
		sortBases(overrideDropPlayerItemHookTypes, allBaseOverrideDropPlayerItemSuperiors, allBaseOverrideDropPlayerItemInferiors, "overrideDropPlayerItem");
		sortBases(afterDropPlayerItemHookTypes, allBaseAfterDropPlayerItemSuperiors, allBaseAfterDropPlayerItemInferiors, "afterDropPlayerItem");

		sortBases(beforeFallHookTypes, allBaseBeforeFallSuperiors, allBaseBeforeFallInferiors, "beforeFall");
		sortBases(overrideFallHookTypes, allBaseOverrideFallSuperiors, allBaseOverrideFallInferiors, "overrideFall");
		sortBases(afterFallHookTypes, allBaseAfterFallSuperiors, allBaseAfterFallInferiors, "afterFall");

		sortBases(beforeGetAIMoveSpeedHookTypes, allBaseBeforeGetAIMoveSpeedSuperiors, allBaseBeforeGetAIMoveSpeedInferiors, "beforeGetAIMoveSpeed");
		sortBases(overrideGetAIMoveSpeedHookTypes, allBaseOverrideGetAIMoveSpeedSuperiors, allBaseOverrideGetAIMoveSpeedInferiors, "overrideGetAIMoveSpeed");
		sortBases(afterGetAIMoveSpeedHookTypes, allBaseAfterGetAIMoveSpeedSuperiors, allBaseAfterGetAIMoveSpeedInferiors, "afterGetAIMoveSpeed");

		sortBases(beforeGetCurrentPlayerStrVsBlockHookTypes, allBaseBeforeGetCurrentPlayerStrVsBlockSuperiors, allBaseBeforeGetCurrentPlayerStrVsBlockInferiors, "beforeGetCurrentPlayerStrVsBlock");
		sortBases(overrideGetCurrentPlayerStrVsBlockHookTypes, allBaseOverrideGetCurrentPlayerStrVsBlockSuperiors, allBaseOverrideGetCurrentPlayerStrVsBlockInferiors, "overrideGetCurrentPlayerStrVsBlock");
		sortBases(afterGetCurrentPlayerStrVsBlockHookTypes, allBaseAfterGetCurrentPlayerStrVsBlockSuperiors, allBaseAfterGetCurrentPlayerStrVsBlockInferiors, "afterGetCurrentPlayerStrVsBlock");

		sortBases(beforeGetCurrentPlayerStrVsBlockForgeHookTypes, allBaseBeforeGetCurrentPlayerStrVsBlockForgeSuperiors, allBaseBeforeGetCurrentPlayerStrVsBlockForgeInferiors, "beforeGetCurrentPlayerStrVsBlockForge");
		sortBases(overrideGetCurrentPlayerStrVsBlockForgeHookTypes, allBaseOverrideGetCurrentPlayerStrVsBlockForgeSuperiors, allBaseOverrideGetCurrentPlayerStrVsBlockForgeInferiors, "overrideGetCurrentPlayerStrVsBlockForge");
		sortBases(afterGetCurrentPlayerStrVsBlockForgeHookTypes, allBaseAfterGetCurrentPlayerStrVsBlockForgeSuperiors, allBaseAfterGetCurrentPlayerStrVsBlockForgeInferiors, "afterGetCurrentPlayerStrVsBlockForge");

		sortBases(beforeGetDistanceSqHookTypes, allBaseBeforeGetDistanceSqSuperiors, allBaseBeforeGetDistanceSqInferiors, "beforeGetDistanceSq");
		sortBases(overrideGetDistanceSqHookTypes, allBaseOverrideGetDistanceSqSuperiors, allBaseOverrideGetDistanceSqInferiors, "overrideGetDistanceSq");
		sortBases(afterGetDistanceSqHookTypes, allBaseAfterGetDistanceSqSuperiors, allBaseAfterGetDistanceSqInferiors, "afterGetDistanceSq");

		sortBases(beforeGetBrightnessHookTypes, allBaseBeforeGetBrightnessSuperiors, allBaseBeforeGetBrightnessInferiors, "beforeGetBrightness");
		sortBases(overrideGetBrightnessHookTypes, allBaseOverrideGetBrightnessSuperiors, allBaseOverrideGetBrightnessInferiors, "overrideGetBrightness");
		sortBases(afterGetBrightnessHookTypes, allBaseAfterGetBrightnessSuperiors, allBaseAfterGetBrightnessInferiors, "afterGetBrightness");

		sortBases(beforeGetEyeHeightHookTypes, allBaseBeforeGetEyeHeightSuperiors, allBaseBeforeGetEyeHeightInferiors, "beforeGetEyeHeight");
		sortBases(overrideGetEyeHeightHookTypes, allBaseOverrideGetEyeHeightSuperiors, allBaseOverrideGetEyeHeightInferiors, "overrideGetEyeHeight");
		sortBases(afterGetEyeHeightHookTypes, allBaseAfterGetEyeHeightSuperiors, allBaseAfterGetEyeHeightInferiors, "afterGetEyeHeight");

		sortBases(beforeHealHookTypes, allBaseBeforeHealSuperiors, allBaseBeforeHealInferiors, "beforeHeal");
		sortBases(overrideHealHookTypes, allBaseOverrideHealSuperiors, allBaseOverrideHealInferiors, "overrideHeal");
		sortBases(afterHealHookTypes, allBaseAfterHealSuperiors, allBaseAfterHealInferiors, "afterHeal");

		sortBases(beforeIsEntityInsideOpaqueBlockHookTypes, allBaseBeforeIsEntityInsideOpaqueBlockSuperiors, allBaseBeforeIsEntityInsideOpaqueBlockInferiors, "beforeIsEntityInsideOpaqueBlock");
		sortBases(overrideIsEntityInsideOpaqueBlockHookTypes, allBaseOverrideIsEntityInsideOpaqueBlockSuperiors, allBaseOverrideIsEntityInsideOpaqueBlockInferiors, "overrideIsEntityInsideOpaqueBlock");
		sortBases(afterIsEntityInsideOpaqueBlockHookTypes, allBaseAfterIsEntityInsideOpaqueBlockSuperiors, allBaseAfterIsEntityInsideOpaqueBlockInferiors, "afterIsEntityInsideOpaqueBlock");

		sortBases(beforeIsInWaterHookTypes, allBaseBeforeIsInWaterSuperiors, allBaseBeforeIsInWaterInferiors, "beforeIsInWater");
		sortBases(overrideIsInWaterHookTypes, allBaseOverrideIsInWaterSuperiors, allBaseOverrideIsInWaterInferiors, "overrideIsInWater");
		sortBases(afterIsInWaterHookTypes, allBaseAfterIsInWaterSuperiors, allBaseAfterIsInWaterInferiors, "afterIsInWater");

		sortBases(beforeIsInsideOfMaterialHookTypes, allBaseBeforeIsInsideOfMaterialSuperiors, allBaseBeforeIsInsideOfMaterialInferiors, "beforeIsInsideOfMaterial");
		sortBases(overrideIsInsideOfMaterialHookTypes, allBaseOverrideIsInsideOfMaterialSuperiors, allBaseOverrideIsInsideOfMaterialInferiors, "overrideIsInsideOfMaterial");
		sortBases(afterIsInsideOfMaterialHookTypes, allBaseAfterIsInsideOfMaterialSuperiors, allBaseAfterIsInsideOfMaterialInferiors, "afterIsInsideOfMaterial");

		sortBases(beforeIsOnLadderHookTypes, allBaseBeforeIsOnLadderSuperiors, allBaseBeforeIsOnLadderInferiors, "beforeIsOnLadder");
		sortBases(overrideIsOnLadderHookTypes, allBaseOverrideIsOnLadderSuperiors, allBaseOverrideIsOnLadderInferiors, "overrideIsOnLadder");
		sortBases(afterIsOnLadderHookTypes, allBaseAfterIsOnLadderSuperiors, allBaseAfterIsOnLadderInferiors, "afterIsOnLadder");

		sortBases(beforeIsPlayerSleepingHookTypes, allBaseBeforeIsPlayerSleepingSuperiors, allBaseBeforeIsPlayerSleepingInferiors, "beforeIsPlayerSleeping");
		sortBases(overrideIsPlayerSleepingHookTypes, allBaseOverrideIsPlayerSleepingSuperiors, allBaseOverrideIsPlayerSleepingInferiors, "overrideIsPlayerSleeping");
		sortBases(afterIsPlayerSleepingHookTypes, allBaseAfterIsPlayerSleepingSuperiors, allBaseAfterIsPlayerSleepingInferiors, "afterIsPlayerSleeping");

		sortBases(beforeIsSneakingHookTypes, allBaseBeforeIsSneakingSuperiors, allBaseBeforeIsSneakingInferiors, "beforeIsSneaking");
		sortBases(overrideIsSneakingHookTypes, allBaseOverrideIsSneakingSuperiors, allBaseOverrideIsSneakingInferiors, "overrideIsSneaking");
		sortBases(afterIsSneakingHookTypes, allBaseAfterIsSneakingSuperiors, allBaseAfterIsSneakingInferiors, "afterIsSneaking");

		sortBases(beforeJumpHookTypes, allBaseBeforeJumpSuperiors, allBaseBeforeJumpInferiors, "beforeJump");
		sortBases(overrideJumpHookTypes, allBaseOverrideJumpSuperiors, allBaseOverrideJumpInferiors, "overrideJump");
		sortBases(afterJumpHookTypes, allBaseAfterJumpSuperiors, allBaseAfterJumpInferiors, "afterJump");

		sortBases(beforeKnockBackHookTypes, allBaseBeforeKnockBackSuperiors, allBaseBeforeKnockBackInferiors, "beforeKnockBack");
		sortBases(overrideKnockBackHookTypes, allBaseOverrideKnockBackSuperiors, allBaseOverrideKnockBackInferiors, "overrideKnockBack");
		sortBases(afterKnockBackHookTypes, allBaseAfterKnockBackSuperiors, allBaseAfterKnockBackInferiors, "afterKnockBack");

		sortBases(beforeMountEntityHookTypes, allBaseBeforeMountEntitySuperiors, allBaseBeforeMountEntityInferiors, "beforeMountEntity");
		sortBases(overrideMountEntityHookTypes, allBaseOverrideMountEntitySuperiors, allBaseOverrideMountEntityInferiors, "overrideMountEntity");
		sortBases(afterMountEntityHookTypes, allBaseAfterMountEntitySuperiors, allBaseAfterMountEntityInferiors, "afterMountEntity");

		sortBases(beforeMoveEntityHookTypes, allBaseBeforeMoveEntitySuperiors, allBaseBeforeMoveEntityInferiors, "beforeMoveEntity");
		sortBases(overrideMoveEntityHookTypes, allBaseOverrideMoveEntitySuperiors, allBaseOverrideMoveEntityInferiors, "overrideMoveEntity");
		sortBases(afterMoveEntityHookTypes, allBaseAfterMoveEntitySuperiors, allBaseAfterMoveEntityInferiors, "afterMoveEntity");

		sortBases(beforeMoveEntityWithHeadingHookTypes, allBaseBeforeMoveEntityWithHeadingSuperiors, allBaseBeforeMoveEntityWithHeadingInferiors, "beforeMoveEntityWithHeading");
		sortBases(overrideMoveEntityWithHeadingHookTypes, allBaseOverrideMoveEntityWithHeadingSuperiors, allBaseOverrideMoveEntityWithHeadingInferiors, "overrideMoveEntityWithHeading");
		sortBases(afterMoveEntityWithHeadingHookTypes, allBaseAfterMoveEntityWithHeadingSuperiors, allBaseAfterMoveEntityWithHeadingInferiors, "afterMoveEntityWithHeading");

		sortBases(beforeMoveFlyingHookTypes, allBaseBeforeMoveFlyingSuperiors, allBaseBeforeMoveFlyingInferiors, "beforeMoveFlying");
		sortBases(overrideMoveFlyingHookTypes, allBaseOverrideMoveFlyingSuperiors, allBaseOverrideMoveFlyingInferiors, "overrideMoveFlying");
		sortBases(afterMoveFlyingHookTypes, allBaseAfterMoveFlyingSuperiors, allBaseAfterMoveFlyingInferiors, "afterMoveFlying");

		sortBases(beforeOnDeathHookTypes, allBaseBeforeOnDeathSuperiors, allBaseBeforeOnDeathInferiors, "beforeOnDeath");
		sortBases(overrideOnDeathHookTypes, allBaseOverrideOnDeathSuperiors, allBaseOverrideOnDeathInferiors, "overrideOnDeath");
		sortBases(afterOnDeathHookTypes, allBaseAfterOnDeathSuperiors, allBaseAfterOnDeathInferiors, "afterOnDeath");

		sortBases(beforeOnLivingUpdateHookTypes, allBaseBeforeOnLivingUpdateSuperiors, allBaseBeforeOnLivingUpdateInferiors, "beforeOnLivingUpdate");
		sortBases(overrideOnLivingUpdateHookTypes, allBaseOverrideOnLivingUpdateSuperiors, allBaseOverrideOnLivingUpdateInferiors, "overrideOnLivingUpdate");
		sortBases(afterOnLivingUpdateHookTypes, allBaseAfterOnLivingUpdateSuperiors, allBaseAfterOnLivingUpdateInferiors, "afterOnLivingUpdate");

		sortBases(beforeOnKillEntityHookTypes, allBaseBeforeOnKillEntitySuperiors, allBaseBeforeOnKillEntityInferiors, "beforeOnKillEntity");
		sortBases(overrideOnKillEntityHookTypes, allBaseOverrideOnKillEntitySuperiors, allBaseOverrideOnKillEntityInferiors, "overrideOnKillEntity");
		sortBases(afterOnKillEntityHookTypes, allBaseAfterOnKillEntitySuperiors, allBaseAfterOnKillEntityInferiors, "afterOnKillEntity");

		sortBases(beforeOnStruckByLightningHookTypes, allBaseBeforeOnStruckByLightningSuperiors, allBaseBeforeOnStruckByLightningInferiors, "beforeOnStruckByLightning");
		sortBases(overrideOnStruckByLightningHookTypes, allBaseOverrideOnStruckByLightningSuperiors, allBaseOverrideOnStruckByLightningInferiors, "overrideOnStruckByLightning");
		sortBases(afterOnStruckByLightningHookTypes, allBaseAfterOnStruckByLightningSuperiors, allBaseAfterOnStruckByLightningInferiors, "afterOnStruckByLightning");

		sortBases(beforeOnUpdateHookTypes, allBaseBeforeOnUpdateSuperiors, allBaseBeforeOnUpdateInferiors, "beforeOnUpdate");
		sortBases(overrideOnUpdateHookTypes, allBaseOverrideOnUpdateSuperiors, allBaseOverrideOnUpdateInferiors, "overrideOnUpdate");
		sortBases(afterOnUpdateHookTypes, allBaseAfterOnUpdateSuperiors, allBaseAfterOnUpdateInferiors, "afterOnUpdate");

		sortBases(beforeOnUpdateEntityHookTypes, allBaseBeforeOnUpdateEntitySuperiors, allBaseBeforeOnUpdateEntityInferiors, "beforeOnUpdateEntity");
		sortBases(overrideOnUpdateEntityHookTypes, allBaseOverrideOnUpdateEntitySuperiors, allBaseOverrideOnUpdateEntityInferiors, "overrideOnUpdateEntity");
		sortBases(afterOnUpdateEntityHookTypes, allBaseAfterOnUpdateEntitySuperiors, allBaseAfterOnUpdateEntityInferiors, "afterOnUpdateEntity");

		sortBases(beforeReadEntityFromNBTHookTypes, allBaseBeforeReadEntityFromNBTSuperiors, allBaseBeforeReadEntityFromNBTInferiors, "beforeReadEntityFromNBT");
		sortBases(overrideReadEntityFromNBTHookTypes, allBaseOverrideReadEntityFromNBTSuperiors, allBaseOverrideReadEntityFromNBTInferiors, "overrideReadEntityFromNBT");
		sortBases(afterReadEntityFromNBTHookTypes, allBaseAfterReadEntityFromNBTSuperiors, allBaseAfterReadEntityFromNBTInferiors, "afterReadEntityFromNBT");

		sortBases(beforeSetDeadHookTypes, allBaseBeforeSetDeadSuperiors, allBaseBeforeSetDeadInferiors, "beforeSetDead");
		sortBases(overrideSetDeadHookTypes, allBaseOverrideSetDeadSuperiors, allBaseOverrideSetDeadInferiors, "overrideSetDead");
		sortBases(afterSetDeadHookTypes, allBaseAfterSetDeadSuperiors, allBaseAfterSetDeadInferiors, "afterSetDead");

		sortBases(beforeSetEntityActionStateHookTypes, allBaseBeforeSetEntityActionStateSuperiors, allBaseBeforeSetEntityActionStateInferiors, "beforeSetEntityActionState");
		sortBases(overrideSetEntityActionStateHookTypes, allBaseOverrideSetEntityActionStateSuperiors, allBaseOverrideSetEntityActionStateInferiors, "overrideSetEntityActionState");
		sortBases(afterSetEntityActionStateHookTypes, allBaseAfterSetEntityActionStateSuperiors, allBaseAfterSetEntityActionStateInferiors, "afterSetEntityActionState");

		sortBases(beforeSetPositionHookTypes, allBaseBeforeSetPositionSuperiors, allBaseBeforeSetPositionInferiors, "beforeSetPosition");
		sortBases(overrideSetPositionHookTypes, allBaseOverrideSetPositionSuperiors, allBaseOverrideSetPositionInferiors, "overrideSetPosition");
		sortBases(afterSetPositionHookTypes, allBaseAfterSetPositionSuperiors, allBaseAfterSetPositionInferiors, "afterSetPosition");

		sortBases(beforeSetSneakingHookTypes, allBaseBeforeSetSneakingSuperiors, allBaseBeforeSetSneakingInferiors, "beforeSetSneaking");
		sortBases(overrideSetSneakingHookTypes, allBaseOverrideSetSneakingSuperiors, allBaseOverrideSetSneakingInferiors, "overrideSetSneaking");
		sortBases(afterSetSneakingHookTypes, allBaseAfterSetSneakingSuperiors, allBaseAfterSetSneakingInferiors, "afterSetSneaking");

		sortBases(beforeSetSprintingHookTypes, allBaseBeforeSetSprintingSuperiors, allBaseBeforeSetSprintingInferiors, "beforeSetSprinting");
		sortBases(overrideSetSprintingHookTypes, allBaseOverrideSetSprintingSuperiors, allBaseOverrideSetSprintingInferiors, "overrideSetSprinting");
		sortBases(afterSetSprintingHookTypes, allBaseAfterSetSprintingSuperiors, allBaseAfterSetSprintingInferiors, "afterSetSprinting");

		sortBases(beforeSwingItemHookTypes, allBaseBeforeSwingItemSuperiors, allBaseBeforeSwingItemInferiors, "beforeSwingItem");
		sortBases(overrideSwingItemHookTypes, allBaseOverrideSwingItemSuperiors, allBaseOverrideSwingItemInferiors, "overrideSwingItem");
		sortBases(afterSwingItemHookTypes, allBaseAfterSwingItemSuperiors, allBaseAfterSwingItemInferiors, "afterSwingItem");

		sortBases(beforeUpdateEntityActionStateHookTypes, allBaseBeforeUpdateEntityActionStateSuperiors, allBaseBeforeUpdateEntityActionStateInferiors, "beforeUpdateEntityActionState");
		sortBases(overrideUpdateEntityActionStateHookTypes, allBaseOverrideUpdateEntityActionStateSuperiors, allBaseOverrideUpdateEntityActionStateInferiors, "overrideUpdateEntityActionState");
		sortBases(afterUpdateEntityActionStateHookTypes, allBaseAfterUpdateEntityActionStateSuperiors, allBaseAfterUpdateEntityActionStateInferiors, "afterUpdateEntityActionState");

		sortBases(beforeUpdatePotionEffectsHookTypes, allBaseBeforeUpdatePotionEffectsSuperiors, allBaseBeforeUpdatePotionEffectsInferiors, "beforeUpdatePotionEffects");
		sortBases(overrideUpdatePotionEffectsHookTypes, allBaseOverrideUpdatePotionEffectsSuperiors, allBaseOverrideUpdatePotionEffectsInferiors, "overrideUpdatePotionEffects");
		sortBases(afterUpdatePotionEffectsHookTypes, allBaseAfterUpdatePotionEffectsSuperiors, allBaseAfterUpdatePotionEffectsInferiors, "afterUpdatePotionEffects");

		sortBases(beforeUpdateRiddenHookTypes, allBaseBeforeUpdateRiddenSuperiors, allBaseBeforeUpdateRiddenInferiors, "beforeUpdateRidden");
		sortBases(overrideUpdateRiddenHookTypes, allBaseOverrideUpdateRiddenSuperiors, allBaseOverrideUpdateRiddenInferiors, "overrideUpdateRidden");
		sortBases(afterUpdateRiddenHookTypes, allBaseAfterUpdateRiddenSuperiors, allBaseAfterUpdateRiddenInferiors, "afterUpdateRidden");

		sortBases(beforeWakeUpPlayerHookTypes, allBaseBeforeWakeUpPlayerSuperiors, allBaseBeforeWakeUpPlayerInferiors, "beforeWakeUpPlayer");
		sortBases(overrideWakeUpPlayerHookTypes, allBaseOverrideWakeUpPlayerSuperiors, allBaseOverrideWakeUpPlayerInferiors, "overrideWakeUpPlayer");
		sortBases(afterWakeUpPlayerHookTypes, allBaseAfterWakeUpPlayerSuperiors, allBaseAfterWakeUpPlayerInferiors, "afterWakeUpPlayer");

		sortBases(beforeWriteEntityToNBTHookTypes, allBaseBeforeWriteEntityToNBTSuperiors, allBaseBeforeWriteEntityToNBTInferiors, "beforeWriteEntityToNBT");
		sortBases(overrideWriteEntityToNBTHookTypes, allBaseOverrideWriteEntityToNBTSuperiors, allBaseOverrideWriteEntityToNBTInferiors, "overrideWriteEntityToNBT");
		sortBases(afterWriteEntityToNBTHookTypes, allBaseAfterWriteEntityToNBTSuperiors, allBaseAfterWriteEntityToNBTInferiors, "afterWriteEntityToNBT");

		initialized = true;
	}

	private static List<IServerPlayerAPI> getAllInstancesList()
	{
		List<IServerPlayerAPI> result = new ArrayList<IServerPlayerAPI>();
		Object entityPlayerList;
		try
		{
			Object minecraftServer = net.minecraft.server.MinecraftServer.class.getMethod("func_71276_C").invoke(null);
			Object serverConfigurationManager = minecraftServer != null ? net.minecraft.server.MinecraftServer.class.getMethod("func_71203_ab").invoke(minecraftServer) : null;
			entityPlayerList = serverConfigurationManager != null ? serverConfigurationManager.getClass().getField("field_72404_b").get(serverConfigurationManager) : null;
		}
		catch(Exception obfuscatedException)
		{
			try
			{
				Object minecraftServer = net.minecraft.server.MinecraftServer.class.getMethod("getServer").invoke(null);
				Object serverConfigurationManager = minecraftServer != null ? net.minecraft.server.MinecraftServer.class.getMethod("getConfigurationManager").invoke(minecraftServer) : null;
				entityPlayerList = serverConfigurationManager != null ? serverConfigurationManager.getClass().getField("playerEntityList").get(serverConfigurationManager) : null;
			}
			catch(Exception deobfuscatedException)
			{
				throw new RuntimeException("Unable to aquire list of current server players.", obfuscatedException);
			}
		}
		if(entityPlayerList != null)
			for(Object entityPlayer : (List<?>)entityPlayerList)
				result.add((IServerPlayerAPI)entityPlayer);
		return result;
	}

	public static net.minecraft.entity.player.EntityPlayerMP[] getAllInstances()
	{
		List<IServerPlayerAPI> allInstances = getAllInstancesList();
		return allInstances.toArray(new net.minecraft.entity.player.EntityPlayerMP[allInstances.size()]);
	}

	public static void beforeLocalConstructing(IServerPlayerAPI serverPlayer, net.minecraft.server.MinecraftServer paramMinecraftServer, net.minecraft.world.WorldServer paramWorldServer, com.mojang.authlib.GameProfile paramGameProfile, net.minecraft.server.management.ItemInWorldManager paramItemInWorldManager)
	{
		ServerPlayerAPI serverPlayerAPI = serverPlayer.getServerPlayerAPI();
		if(serverPlayerAPI != null)
			serverPlayerAPI.load();

		if(serverPlayerAPI != null)
			serverPlayerAPI.beforeLocalConstructing(paramMinecraftServer, paramWorldServer, paramGameProfile, paramItemInWorldManager);
	}

	public static void afterLocalConstructing(IServerPlayerAPI serverPlayer, net.minecraft.server.MinecraftServer paramMinecraftServer, net.minecraft.world.WorldServer paramWorldServer, com.mojang.authlib.GameProfile paramGameProfile, net.minecraft.server.management.ItemInWorldManager paramItemInWorldManager)
	{
		ServerPlayerAPI serverPlayerAPI = serverPlayer.getServerPlayerAPI();
		if(serverPlayerAPI != null)
			serverPlayerAPI.afterLocalConstructing(paramMinecraftServer, paramWorldServer, paramGameProfile, paramItemInWorldManager);
	}

	public static ServerPlayerBase getServerPlayerBase(IServerPlayerAPI serverPlayer, String baseId)
	{
		ServerPlayerAPI serverPlayerAPI = serverPlayer.getServerPlayerAPI();
		if(serverPlayerAPI != null)
			return serverPlayerAPI.getServerPlayerBase(baseId);
		return null;
	}

	public static Set<String> getServerPlayerBaseIds(IServerPlayerAPI serverPlayer)
	{
		ServerPlayerAPI serverPlayerAPI = serverPlayer.getServerPlayerAPI();
		Set<String> result = null;
		if(serverPlayerAPI != null)
			result = serverPlayerAPI.getServerPlayerBaseIds();
		else
			result = Collections.<String>emptySet();
		return result;
	}

	public static Object dynamic(IServerPlayerAPI serverPlayer, String key, Object[] parameters)
	{
		ServerPlayerAPI serverPlayerAPI = serverPlayer.getServerPlayerAPI();
		if(serverPlayerAPI != null)
			return serverPlayerAPI.dynamic(key, parameters);
		return null;
	}

	private static void sortBases(List<String> list, Map<String, String[]> allBaseSuperiors, Map<String, String[]> allBaseInferiors, String methodName)
	{
		new ServerPlayerBaseSorter(list, allBaseSuperiors, allBaseInferiors, methodName).Sort();
	}

	private final static Map<String, String[]> EmptySortMap = Collections.unmodifiableMap(new HashMap<String, String[]>());

	private static void sortDynamicBases(Map<String, List<String>> lists, Map<String, Map<String, String[]>> allBaseSuperiors, Map<String, Map<String, String[]>> allBaseInferiors, String key)
	{
		List<String> types = lists.get(key);
		if(types != null && types.size() > 1)
			sortBases(types, getDynamicSorters(key, types, allBaseSuperiors), getDynamicSorters(key, types, allBaseInferiors), key);
	}

	private static Map<String, String[]> getDynamicSorters(String key, List<String> toSort, Map<String, Map<String, String[]>> allBaseValues)
	{
		Map<String, String[]> superiors = null;

		Iterator<String> ids = toSort.iterator();
		while(ids.hasNext())
		{
			String id = ids.next();
			Map<String, String[]> idSuperiors = allBaseValues.get(id);
			if(idSuperiors == null)
				continue;

			String[] keySuperiorIds = idSuperiors.get(key);
			if(keySuperiorIds != null && keySuperiorIds.length > 0)
			{
				if(superiors == null)
					superiors = new HashMap<String, String[]>(1);
				superiors.put(id, keySuperiorIds);
			}
		}

		return superiors != null ? superiors : EmptySortMap;
	}

	private ServerPlayerAPI(IServerPlayerAPI player)
	{
		this.player = player;
	}

	private void load()
	{
		Iterator<String> iterator = allBaseConstructors.keySet().iterator();
		while(iterator.hasNext())
		{
			String id = iterator.next();
			ServerPlayerBase toAttach = createServerPlayerBase(id);
			toAttach.beforeBaseAttach(false);
			allBaseObjects.put(id, toAttach);
			baseObjectsToId.put(toAttach, id);
		}

		beforeLocalConstructingHooks = create(beforeLocalConstructingHookTypes);
		afterLocalConstructingHooks = create(afterLocalConstructingHookTypes);

		updateServerPlayerBases();

		iterator = allBaseObjects.keySet().iterator();
		while(iterator.hasNext())
			allBaseObjects.get(iterator.next()).afterBaseAttach(false);
	}

	private ServerPlayerBase createServerPlayerBase(String id)
	{
		Constructor<?> contructor = allBaseConstructors.get(id);

		ServerPlayerBase base;
		try
		{
			if(contructor.getParameterTypes().length == 1)
				base = (ServerPlayerBase)contructor.newInstance(this);
			else
				base = (ServerPlayerBase)contructor.newInstance(this, id);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Exception while creating a ServerPlayerBase of type '" + contructor.getDeclaringClass() + "'", e);
		}
		return base;
	}

	private void updateServerPlayerBases()
	{
		beforeAddExhaustionHooks = create(beforeAddExhaustionHookTypes);
		overrideAddExhaustionHooks = create(overrideAddExhaustionHookTypes);
		afterAddExhaustionHooks = create(afterAddExhaustionHookTypes);
		isAddExhaustionModded =
			beforeAddExhaustionHooks != null ||
			overrideAddExhaustionHooks != null ||
			afterAddExhaustionHooks != null;

		beforeAddExperienceHooks = create(beforeAddExperienceHookTypes);
		overrideAddExperienceHooks = create(overrideAddExperienceHookTypes);
		afterAddExperienceHooks = create(afterAddExperienceHookTypes);
		isAddExperienceModded =
			beforeAddExperienceHooks != null ||
			overrideAddExperienceHooks != null ||
			afterAddExperienceHooks != null;

		beforeAddExperienceLevelHooks = create(beforeAddExperienceLevelHookTypes);
		overrideAddExperienceLevelHooks = create(overrideAddExperienceLevelHookTypes);
		afterAddExperienceLevelHooks = create(afterAddExperienceLevelHookTypes);
		isAddExperienceLevelModded =
			beforeAddExperienceLevelHooks != null ||
			overrideAddExperienceLevelHooks != null ||
			afterAddExperienceLevelHooks != null;

		beforeAddMovementStatHooks = create(beforeAddMovementStatHookTypes);
		overrideAddMovementStatHooks = create(overrideAddMovementStatHookTypes);
		afterAddMovementStatHooks = create(afterAddMovementStatHookTypes);
		isAddMovementStatModded =
			beforeAddMovementStatHooks != null ||
			overrideAddMovementStatHooks != null ||
			afterAddMovementStatHooks != null;

		beforeAttackEntityFromHooks = create(beforeAttackEntityFromHookTypes);
		overrideAttackEntityFromHooks = create(overrideAttackEntityFromHookTypes);
		afterAttackEntityFromHooks = create(afterAttackEntityFromHookTypes);
		isAttackEntityFromModded =
			beforeAttackEntityFromHooks != null ||
			overrideAttackEntityFromHooks != null ||
			afterAttackEntityFromHooks != null;

		beforeAttackTargetEntityWithCurrentItemHooks = create(beforeAttackTargetEntityWithCurrentItemHookTypes);
		overrideAttackTargetEntityWithCurrentItemHooks = create(overrideAttackTargetEntityWithCurrentItemHookTypes);
		afterAttackTargetEntityWithCurrentItemHooks = create(afterAttackTargetEntityWithCurrentItemHookTypes);
		isAttackTargetEntityWithCurrentItemModded =
			beforeAttackTargetEntityWithCurrentItemHooks != null ||
			overrideAttackTargetEntityWithCurrentItemHooks != null ||
			afterAttackTargetEntityWithCurrentItemHooks != null;

		beforeCanBreatheUnderwaterHooks = create(beforeCanBreatheUnderwaterHookTypes);
		overrideCanBreatheUnderwaterHooks = create(overrideCanBreatheUnderwaterHookTypes);
		afterCanBreatheUnderwaterHooks = create(afterCanBreatheUnderwaterHookTypes);
		isCanBreatheUnderwaterModded =
			beforeCanBreatheUnderwaterHooks != null ||
			overrideCanBreatheUnderwaterHooks != null ||
			afterCanBreatheUnderwaterHooks != null;

		beforeCanHarvestBlockHooks = create(beforeCanHarvestBlockHookTypes);
		overrideCanHarvestBlockHooks = create(overrideCanHarvestBlockHookTypes);
		afterCanHarvestBlockHooks = create(afterCanHarvestBlockHookTypes);
		isCanHarvestBlockModded =
			beforeCanHarvestBlockHooks != null ||
			overrideCanHarvestBlockHooks != null ||
			afterCanHarvestBlockHooks != null;

		beforeCanPlayerEditHooks = create(beforeCanPlayerEditHookTypes);
		overrideCanPlayerEditHooks = create(overrideCanPlayerEditHookTypes);
		afterCanPlayerEditHooks = create(afterCanPlayerEditHookTypes);
		isCanPlayerEditModded =
			beforeCanPlayerEditHooks != null ||
			overrideCanPlayerEditHooks != null ||
			afterCanPlayerEditHooks != null;

		beforeCanTriggerWalkingHooks = create(beforeCanTriggerWalkingHookTypes);
		overrideCanTriggerWalkingHooks = create(overrideCanTriggerWalkingHookTypes);
		afterCanTriggerWalkingHooks = create(afterCanTriggerWalkingHookTypes);
		isCanTriggerWalkingModded =
			beforeCanTriggerWalkingHooks != null ||
			overrideCanTriggerWalkingHooks != null ||
			afterCanTriggerWalkingHooks != null;

		beforeClonePlayerHooks = create(beforeClonePlayerHookTypes);
		overrideClonePlayerHooks = create(overrideClonePlayerHookTypes);
		afterClonePlayerHooks = create(afterClonePlayerHookTypes);
		isClonePlayerModded =
			beforeClonePlayerHooks != null ||
			overrideClonePlayerHooks != null ||
			afterClonePlayerHooks != null;

		beforeDamageEntityHooks = create(beforeDamageEntityHookTypes);
		overrideDamageEntityHooks = create(overrideDamageEntityHookTypes);
		afterDamageEntityHooks = create(afterDamageEntityHookTypes);
		isDamageEntityModded =
			beforeDamageEntityHooks != null ||
			overrideDamageEntityHooks != null ||
			afterDamageEntityHooks != null;

		beforeDisplayGUIChestHooks = create(beforeDisplayGUIChestHookTypes);
		overrideDisplayGUIChestHooks = create(overrideDisplayGUIChestHookTypes);
		afterDisplayGUIChestHooks = create(afterDisplayGUIChestHookTypes);
		isDisplayGUIChestModded =
			beforeDisplayGUIChestHooks != null ||
			overrideDisplayGUIChestHooks != null ||
			afterDisplayGUIChestHooks != null;

		beforeDisplayGUIDispenserHooks = create(beforeDisplayGUIDispenserHookTypes);
		overrideDisplayGUIDispenserHooks = create(overrideDisplayGUIDispenserHookTypes);
		afterDisplayGUIDispenserHooks = create(afterDisplayGUIDispenserHookTypes);
		isDisplayGUIDispenserModded =
			beforeDisplayGUIDispenserHooks != null ||
			overrideDisplayGUIDispenserHooks != null ||
			afterDisplayGUIDispenserHooks != null;

		beforeDisplayGUIFurnaceHooks = create(beforeDisplayGUIFurnaceHookTypes);
		overrideDisplayGUIFurnaceHooks = create(overrideDisplayGUIFurnaceHookTypes);
		afterDisplayGUIFurnaceHooks = create(afterDisplayGUIFurnaceHookTypes);
		isDisplayGUIFurnaceModded =
			beforeDisplayGUIFurnaceHooks != null ||
			overrideDisplayGUIFurnaceHooks != null ||
			afterDisplayGUIFurnaceHooks != null;

		beforeDisplayGUIWorkbenchHooks = create(beforeDisplayGUIWorkbenchHookTypes);
		overrideDisplayGUIWorkbenchHooks = create(overrideDisplayGUIWorkbenchHookTypes);
		afterDisplayGUIWorkbenchHooks = create(afterDisplayGUIWorkbenchHookTypes);
		isDisplayGUIWorkbenchModded =
			beforeDisplayGUIWorkbenchHooks != null ||
			overrideDisplayGUIWorkbenchHooks != null ||
			afterDisplayGUIWorkbenchHooks != null;

		beforeDropOneItemHooks = create(beforeDropOneItemHookTypes);
		overrideDropOneItemHooks = create(overrideDropOneItemHookTypes);
		afterDropOneItemHooks = create(afterDropOneItemHookTypes);
		isDropOneItemModded =
			beforeDropOneItemHooks != null ||
			overrideDropOneItemHooks != null ||
			afterDropOneItemHooks != null;

		beforeDropPlayerItemHooks = create(beforeDropPlayerItemHookTypes);
		overrideDropPlayerItemHooks = create(overrideDropPlayerItemHookTypes);
		afterDropPlayerItemHooks = create(afterDropPlayerItemHookTypes);
		isDropPlayerItemModded =
			beforeDropPlayerItemHooks != null ||
			overrideDropPlayerItemHooks != null ||
			afterDropPlayerItemHooks != null;

		beforeFallHooks = create(beforeFallHookTypes);
		overrideFallHooks = create(overrideFallHookTypes);
		afterFallHooks = create(afterFallHookTypes);
		isFallModded =
			beforeFallHooks != null ||
			overrideFallHooks != null ||
			afterFallHooks != null;

		beforeGetAIMoveSpeedHooks = create(beforeGetAIMoveSpeedHookTypes);
		overrideGetAIMoveSpeedHooks = create(overrideGetAIMoveSpeedHookTypes);
		afterGetAIMoveSpeedHooks = create(afterGetAIMoveSpeedHookTypes);
		isGetAIMoveSpeedModded =
			beforeGetAIMoveSpeedHooks != null ||
			overrideGetAIMoveSpeedHooks != null ||
			afterGetAIMoveSpeedHooks != null;

		beforeGetCurrentPlayerStrVsBlockHooks = create(beforeGetCurrentPlayerStrVsBlockHookTypes);
		overrideGetCurrentPlayerStrVsBlockHooks = create(overrideGetCurrentPlayerStrVsBlockHookTypes);
		afterGetCurrentPlayerStrVsBlockHooks = create(afterGetCurrentPlayerStrVsBlockHookTypes);
		isGetCurrentPlayerStrVsBlockModded =
			beforeGetCurrentPlayerStrVsBlockHooks != null ||
			overrideGetCurrentPlayerStrVsBlockHooks != null ||
			afterGetCurrentPlayerStrVsBlockHooks != null;

		beforeGetCurrentPlayerStrVsBlockForgeHooks = create(beforeGetCurrentPlayerStrVsBlockForgeHookTypes);
		overrideGetCurrentPlayerStrVsBlockForgeHooks = create(overrideGetCurrentPlayerStrVsBlockForgeHookTypes);
		afterGetCurrentPlayerStrVsBlockForgeHooks = create(afterGetCurrentPlayerStrVsBlockForgeHookTypes);
		isGetCurrentPlayerStrVsBlockForgeModded =
			beforeGetCurrentPlayerStrVsBlockForgeHooks != null ||
			overrideGetCurrentPlayerStrVsBlockForgeHooks != null ||
			afterGetCurrentPlayerStrVsBlockForgeHooks != null;

		beforeGetDistanceSqHooks = create(beforeGetDistanceSqHookTypes);
		overrideGetDistanceSqHooks = create(overrideGetDistanceSqHookTypes);
		afterGetDistanceSqHooks = create(afterGetDistanceSqHookTypes);
		isGetDistanceSqModded =
			beforeGetDistanceSqHooks != null ||
			overrideGetDistanceSqHooks != null ||
			afterGetDistanceSqHooks != null;

		beforeGetBrightnessHooks = create(beforeGetBrightnessHookTypes);
		overrideGetBrightnessHooks = create(overrideGetBrightnessHookTypes);
		afterGetBrightnessHooks = create(afterGetBrightnessHookTypes);
		isGetBrightnessModded =
			beforeGetBrightnessHooks != null ||
			overrideGetBrightnessHooks != null ||
			afterGetBrightnessHooks != null;

		beforeGetEyeHeightHooks = create(beforeGetEyeHeightHookTypes);
		overrideGetEyeHeightHooks = create(overrideGetEyeHeightHookTypes);
		afterGetEyeHeightHooks = create(afterGetEyeHeightHookTypes);
		isGetEyeHeightModded =
			beforeGetEyeHeightHooks != null ||
			overrideGetEyeHeightHooks != null ||
			afterGetEyeHeightHooks != null;

		beforeHealHooks = create(beforeHealHookTypes);
		overrideHealHooks = create(overrideHealHookTypes);
		afterHealHooks = create(afterHealHookTypes);
		isHealModded =
			beforeHealHooks != null ||
			overrideHealHooks != null ||
			afterHealHooks != null;

		beforeIsEntityInsideOpaqueBlockHooks = create(beforeIsEntityInsideOpaqueBlockHookTypes);
		overrideIsEntityInsideOpaqueBlockHooks = create(overrideIsEntityInsideOpaqueBlockHookTypes);
		afterIsEntityInsideOpaqueBlockHooks = create(afterIsEntityInsideOpaqueBlockHookTypes);
		isIsEntityInsideOpaqueBlockModded =
			beforeIsEntityInsideOpaqueBlockHooks != null ||
			overrideIsEntityInsideOpaqueBlockHooks != null ||
			afterIsEntityInsideOpaqueBlockHooks != null;

		beforeIsInWaterHooks = create(beforeIsInWaterHookTypes);
		overrideIsInWaterHooks = create(overrideIsInWaterHookTypes);
		afterIsInWaterHooks = create(afterIsInWaterHookTypes);
		isIsInWaterModded =
			beforeIsInWaterHooks != null ||
			overrideIsInWaterHooks != null ||
			afterIsInWaterHooks != null;

		beforeIsInsideOfMaterialHooks = create(beforeIsInsideOfMaterialHookTypes);
		overrideIsInsideOfMaterialHooks = create(overrideIsInsideOfMaterialHookTypes);
		afterIsInsideOfMaterialHooks = create(afterIsInsideOfMaterialHookTypes);
		isIsInsideOfMaterialModded =
			beforeIsInsideOfMaterialHooks != null ||
			overrideIsInsideOfMaterialHooks != null ||
			afterIsInsideOfMaterialHooks != null;

		beforeIsOnLadderHooks = create(beforeIsOnLadderHookTypes);
		overrideIsOnLadderHooks = create(overrideIsOnLadderHookTypes);
		afterIsOnLadderHooks = create(afterIsOnLadderHookTypes);
		isIsOnLadderModded =
			beforeIsOnLadderHooks != null ||
			overrideIsOnLadderHooks != null ||
			afterIsOnLadderHooks != null;

		beforeIsPlayerSleepingHooks = create(beforeIsPlayerSleepingHookTypes);
		overrideIsPlayerSleepingHooks = create(overrideIsPlayerSleepingHookTypes);
		afterIsPlayerSleepingHooks = create(afterIsPlayerSleepingHookTypes);
		isIsPlayerSleepingModded =
			beforeIsPlayerSleepingHooks != null ||
			overrideIsPlayerSleepingHooks != null ||
			afterIsPlayerSleepingHooks != null;

		beforeIsSneakingHooks = create(beforeIsSneakingHookTypes);
		overrideIsSneakingHooks = create(overrideIsSneakingHookTypes);
		afterIsSneakingHooks = create(afterIsSneakingHookTypes);
		isIsSneakingModded =
			beforeIsSneakingHooks != null ||
			overrideIsSneakingHooks != null ||
			afterIsSneakingHooks != null;

		beforeJumpHooks = create(beforeJumpHookTypes);
		overrideJumpHooks = create(overrideJumpHookTypes);
		afterJumpHooks = create(afterJumpHookTypes);
		isJumpModded =
			beforeJumpHooks != null ||
			overrideJumpHooks != null ||
			afterJumpHooks != null;

		beforeKnockBackHooks = create(beforeKnockBackHookTypes);
		overrideKnockBackHooks = create(overrideKnockBackHookTypes);
		afterKnockBackHooks = create(afterKnockBackHookTypes);
		isKnockBackModded =
			beforeKnockBackHooks != null ||
			overrideKnockBackHooks != null ||
			afterKnockBackHooks != null;

		beforeMountEntityHooks = create(beforeMountEntityHookTypes);
		overrideMountEntityHooks = create(overrideMountEntityHookTypes);
		afterMountEntityHooks = create(afterMountEntityHookTypes);
		isMountEntityModded =
			beforeMountEntityHooks != null ||
			overrideMountEntityHooks != null ||
			afterMountEntityHooks != null;

		beforeMoveEntityHooks = create(beforeMoveEntityHookTypes);
		overrideMoveEntityHooks = create(overrideMoveEntityHookTypes);
		afterMoveEntityHooks = create(afterMoveEntityHookTypes);
		isMoveEntityModded =
			beforeMoveEntityHooks != null ||
			overrideMoveEntityHooks != null ||
			afterMoveEntityHooks != null;

		beforeMoveEntityWithHeadingHooks = create(beforeMoveEntityWithHeadingHookTypes);
		overrideMoveEntityWithHeadingHooks = create(overrideMoveEntityWithHeadingHookTypes);
		afterMoveEntityWithHeadingHooks = create(afterMoveEntityWithHeadingHookTypes);
		isMoveEntityWithHeadingModded =
			beforeMoveEntityWithHeadingHooks != null ||
			overrideMoveEntityWithHeadingHooks != null ||
			afterMoveEntityWithHeadingHooks != null;

		beforeMoveFlyingHooks = create(beforeMoveFlyingHookTypes);
		overrideMoveFlyingHooks = create(overrideMoveFlyingHookTypes);
		afterMoveFlyingHooks = create(afterMoveFlyingHookTypes);
		isMoveFlyingModded =
			beforeMoveFlyingHooks != null ||
			overrideMoveFlyingHooks != null ||
			afterMoveFlyingHooks != null;

		beforeOnDeathHooks = create(beforeOnDeathHookTypes);
		overrideOnDeathHooks = create(overrideOnDeathHookTypes);
		afterOnDeathHooks = create(afterOnDeathHookTypes);
		isOnDeathModded =
			beforeOnDeathHooks != null ||
			overrideOnDeathHooks != null ||
			afterOnDeathHooks != null;

		beforeOnLivingUpdateHooks = create(beforeOnLivingUpdateHookTypes);
		overrideOnLivingUpdateHooks = create(overrideOnLivingUpdateHookTypes);
		afterOnLivingUpdateHooks = create(afterOnLivingUpdateHookTypes);
		isOnLivingUpdateModded =
			beforeOnLivingUpdateHooks != null ||
			overrideOnLivingUpdateHooks != null ||
			afterOnLivingUpdateHooks != null;

		beforeOnKillEntityHooks = create(beforeOnKillEntityHookTypes);
		overrideOnKillEntityHooks = create(overrideOnKillEntityHookTypes);
		afterOnKillEntityHooks = create(afterOnKillEntityHookTypes);
		isOnKillEntityModded =
			beforeOnKillEntityHooks != null ||
			overrideOnKillEntityHooks != null ||
			afterOnKillEntityHooks != null;

		beforeOnStruckByLightningHooks = create(beforeOnStruckByLightningHookTypes);
		overrideOnStruckByLightningHooks = create(overrideOnStruckByLightningHookTypes);
		afterOnStruckByLightningHooks = create(afterOnStruckByLightningHookTypes);
		isOnStruckByLightningModded =
			beforeOnStruckByLightningHooks != null ||
			overrideOnStruckByLightningHooks != null ||
			afterOnStruckByLightningHooks != null;

		beforeOnUpdateHooks = create(beforeOnUpdateHookTypes);
		overrideOnUpdateHooks = create(overrideOnUpdateHookTypes);
		afterOnUpdateHooks = create(afterOnUpdateHookTypes);
		isOnUpdateModded =
			beforeOnUpdateHooks != null ||
			overrideOnUpdateHooks != null ||
			afterOnUpdateHooks != null;

		beforeOnUpdateEntityHooks = create(beforeOnUpdateEntityHookTypes);
		overrideOnUpdateEntityHooks = create(overrideOnUpdateEntityHookTypes);
		afterOnUpdateEntityHooks = create(afterOnUpdateEntityHookTypes);
		isOnUpdateEntityModded =
			beforeOnUpdateEntityHooks != null ||
			overrideOnUpdateEntityHooks != null ||
			afterOnUpdateEntityHooks != null;

		beforeReadEntityFromNBTHooks = create(beforeReadEntityFromNBTHookTypes);
		overrideReadEntityFromNBTHooks = create(overrideReadEntityFromNBTHookTypes);
		afterReadEntityFromNBTHooks = create(afterReadEntityFromNBTHookTypes);
		isReadEntityFromNBTModded =
			beforeReadEntityFromNBTHooks != null ||
			overrideReadEntityFromNBTHooks != null ||
			afterReadEntityFromNBTHooks != null;

		beforeSetDeadHooks = create(beforeSetDeadHookTypes);
		overrideSetDeadHooks = create(overrideSetDeadHookTypes);
		afterSetDeadHooks = create(afterSetDeadHookTypes);
		isSetDeadModded =
			beforeSetDeadHooks != null ||
			overrideSetDeadHooks != null ||
			afterSetDeadHooks != null;

		beforeSetEntityActionStateHooks = create(beforeSetEntityActionStateHookTypes);
		overrideSetEntityActionStateHooks = create(overrideSetEntityActionStateHookTypes);
		afterSetEntityActionStateHooks = create(afterSetEntityActionStateHookTypes);
		isSetEntityActionStateModded =
			beforeSetEntityActionStateHooks != null ||
			overrideSetEntityActionStateHooks != null ||
			afterSetEntityActionStateHooks != null;

		beforeSetPositionHooks = create(beforeSetPositionHookTypes);
		overrideSetPositionHooks = create(overrideSetPositionHookTypes);
		afterSetPositionHooks = create(afterSetPositionHookTypes);
		isSetPositionModded =
			beforeSetPositionHooks != null ||
			overrideSetPositionHooks != null ||
			afterSetPositionHooks != null;

		beforeSetSneakingHooks = create(beforeSetSneakingHookTypes);
		overrideSetSneakingHooks = create(overrideSetSneakingHookTypes);
		afterSetSneakingHooks = create(afterSetSneakingHookTypes);
		isSetSneakingModded =
			beforeSetSneakingHooks != null ||
			overrideSetSneakingHooks != null ||
			afterSetSneakingHooks != null;

		beforeSetSprintingHooks = create(beforeSetSprintingHookTypes);
		overrideSetSprintingHooks = create(overrideSetSprintingHookTypes);
		afterSetSprintingHooks = create(afterSetSprintingHookTypes);
		isSetSprintingModded =
			beforeSetSprintingHooks != null ||
			overrideSetSprintingHooks != null ||
			afterSetSprintingHooks != null;

		beforeSwingItemHooks = create(beforeSwingItemHookTypes);
		overrideSwingItemHooks = create(overrideSwingItemHookTypes);
		afterSwingItemHooks = create(afterSwingItemHookTypes);
		isSwingItemModded =
			beforeSwingItemHooks != null ||
			overrideSwingItemHooks != null ||
			afterSwingItemHooks != null;

		beforeUpdateEntityActionStateHooks = create(beforeUpdateEntityActionStateHookTypes);
		overrideUpdateEntityActionStateHooks = create(overrideUpdateEntityActionStateHookTypes);
		afterUpdateEntityActionStateHooks = create(afterUpdateEntityActionStateHookTypes);
		isUpdateEntityActionStateModded =
			beforeUpdateEntityActionStateHooks != null ||
			overrideUpdateEntityActionStateHooks != null ||
			afterUpdateEntityActionStateHooks != null;

		beforeUpdatePotionEffectsHooks = create(beforeUpdatePotionEffectsHookTypes);
		overrideUpdatePotionEffectsHooks = create(overrideUpdatePotionEffectsHookTypes);
		afterUpdatePotionEffectsHooks = create(afterUpdatePotionEffectsHookTypes);
		isUpdatePotionEffectsModded =
			beforeUpdatePotionEffectsHooks != null ||
			overrideUpdatePotionEffectsHooks != null ||
			afterUpdatePotionEffectsHooks != null;

		beforeUpdateRiddenHooks = create(beforeUpdateRiddenHookTypes);
		overrideUpdateRiddenHooks = create(overrideUpdateRiddenHookTypes);
		afterUpdateRiddenHooks = create(afterUpdateRiddenHookTypes);
		isUpdateRiddenModded =
			beforeUpdateRiddenHooks != null ||
			overrideUpdateRiddenHooks != null ||
			afterUpdateRiddenHooks != null;

		beforeWakeUpPlayerHooks = create(beforeWakeUpPlayerHookTypes);
		overrideWakeUpPlayerHooks = create(overrideWakeUpPlayerHookTypes);
		afterWakeUpPlayerHooks = create(afterWakeUpPlayerHookTypes);
		isWakeUpPlayerModded =
			beforeWakeUpPlayerHooks != null ||
			overrideWakeUpPlayerHooks != null ||
			afterWakeUpPlayerHooks != null;

		beforeWriteEntityToNBTHooks = create(beforeWriteEntityToNBTHookTypes);
		overrideWriteEntityToNBTHooks = create(overrideWriteEntityToNBTHookTypes);
		afterWriteEntityToNBTHooks = create(afterWriteEntityToNBTHookTypes);
		isWriteEntityToNBTModded =
			beforeWriteEntityToNBTHooks != null ||
			overrideWriteEntityToNBTHooks != null ||
			afterWriteEntityToNBTHooks != null;

	}

	private void attachServerPlayerBase(String id)
	{
        ServerPlayerBase toAttach = createServerPlayerBase(id);
		toAttach.beforeBaseAttach(true);
		allBaseObjects.put(id, toAttach);
		updateServerPlayerBases();
		toAttach.afterBaseAttach(true);
	}

	private void detachServerPlayerBase(String id)
	{
		ServerPlayerBase toDetach = allBaseObjects.get(id);
		toDetach.beforeBaseDetach(true);
		allBaseObjects.remove(id);
		toDetach.afterBaseDetach(true);
	}

	private ServerPlayerBase[] create(List<String> types)
	{
		if(types.isEmpty())
			return null;

		ServerPlayerBase[] result = new ServerPlayerBase[types.size()];
		for(int i = 0; i < result.length; i++)
			result[i] = getServerPlayerBase(types.get(i));
		return result;
	}

	private void beforeLocalConstructing(net.minecraft.server.MinecraftServer paramMinecraftServer, net.minecraft.world.WorldServer paramWorldServer, com.mojang.authlib.GameProfile paramGameProfile, net.minecraft.server.management.ItemInWorldManager paramItemInWorldManager)
	{
		if(beforeLocalConstructingHooks != null)
			for(int i = beforeLocalConstructingHooks.length - 1; i >= 0 ; i--)
				beforeLocalConstructingHooks[i].beforeLocalConstructing(paramMinecraftServer, paramWorldServer, paramGameProfile, paramItemInWorldManager);
		beforeLocalConstructingHooks = null;
	}

	private void afterLocalConstructing(net.minecraft.server.MinecraftServer paramMinecraftServer, net.minecraft.world.WorldServer paramWorldServer, com.mojang.authlib.GameProfile paramGameProfile, net.minecraft.server.management.ItemInWorldManager paramItemInWorldManager)
	{
		if(afterLocalConstructingHooks != null)
			for(int i = 0; i < afterLocalConstructingHooks.length; i++)
				afterLocalConstructingHooks[i].afterLocalConstructing(paramMinecraftServer, paramWorldServer, paramGameProfile, paramItemInWorldManager);
		afterLocalConstructingHooks = null;
	}

	public ServerPlayerBase getServerPlayerBase(String id)
	{
		return allBaseObjects.get(id);
	}

	public Set<String> getServerPlayerBaseIds()
	{
		return unmodifiableAllBaseIds;
	}

	public Object dynamic(String key, Object[] parameters)
	{
		key = key.replace('.', '_').replace(' ', '_');
		executeAll(key, parameters, beforeDynamicHookTypes, beforeDynamicHookMethods, true);
		Object result = dynamicOverwritten(key, parameters, null);
		executeAll(key, parameters, afterDynamicHookTypes, afterDynamicHookMethods, false);
		return result;
	}

	public Object dynamicOverwritten(String key, Object[] parameters, ServerPlayerBase overwriter)
	{
		List<String> overrideIds = overrideDynamicHookTypes.get(key);

		String id = null;
		if(overrideIds != null)
			if(overwriter != null)
			{
				id = baseObjectsToId.get(overwriter);
				int index = overrideIds.indexOf(id);
				if(index > 0)
					id = overrideIds.get(index - 1);
				else
					id = null;
			}
			else if(overrideIds.size() > 0)
				id = overrideIds.get(overrideIds.size() - 1);

		Map<Class<?>, Map<String, Method>> methodMap;

		if(id == null)
		{
			id = keysToVirtualIds.get(key);
			if(id == null)
				return null;
			methodMap = virtualDynamicHookMethods;
		}
		else
			methodMap = overrideDynamicHookMethods;

		Map<String, Method> methods = methodMap.get(allBaseConstructors.get(id).getDeclaringClass());
		if(methods == null)
			return null;

		Method method = methods.get(key);
		if(method == null)
			return null;

		return execute(getServerPlayerBase(id), method, parameters);
	}

	private void executeAll(String key, Object[] parameters, Map<String, List<String>> dynamicHookTypes, Map<Class<?>, Map<String, Method>> dynamicHookMethods, boolean reverse)
	{
		List<String> beforeIds = dynamicHookTypes.get(key);
		if(beforeIds == null)
			return;

		for(int i= reverse ? beforeIds.size() - 1 : 0; reverse ? i >= 0 : i < beforeIds.size(); i = i + (reverse ? -1 : 1))
		{
			String id = beforeIds.get(i);
			ServerPlayerBase base = getServerPlayerBase(id);
			Class<?> type = base.getClass();

			Map<String, Method> methods = dynamicHookMethods.get(type);
			if(methods == null)
				continue;

			Method method = methods.get(key);
			if(method == null)
				continue;

			execute(base, method, parameters);
		}
	}

	private Object execute(ServerPlayerBase base, Method method, Object[] parameters)
	{
		try
		{
			return method.invoke(base, parameters);
		}
		catch(Exception e)
		{
			throw new RuntimeException("Exception while invoking dynamic method", e);
		}
	}

	public static void addExhaustion(IServerPlayerAPI target, float paramFloat)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isAddExhaustionModded)
			serverPlayerAPI.addExhaustion(paramFloat);
		else
			target.localAddExhaustion(paramFloat);
	}

	private void addExhaustion(float paramFloat)
	{
		if(beforeAddExhaustionHooks != null)
			for(int i = beforeAddExhaustionHooks.length - 1; i >= 0 ; i--)
				beforeAddExhaustionHooks[i].beforeAddExhaustion(paramFloat);

		if(overrideAddExhaustionHooks != null)
			overrideAddExhaustionHooks[overrideAddExhaustionHooks.length - 1].addExhaustion(paramFloat);
		else
			player.localAddExhaustion(paramFloat);

		if(afterAddExhaustionHooks != null)
			for(int i = 0; i < afterAddExhaustionHooks.length; i++)
				afterAddExhaustionHooks[i].afterAddExhaustion(paramFloat);

	}

	protected ServerPlayerBase GetOverwrittenAddExhaustion(ServerPlayerBase overWriter)
	{
		if (overrideAddExhaustionHooks == null)
			return overWriter;

		for(int i = 0; i < overrideAddExhaustionHooks.length; i++)
			if(overrideAddExhaustionHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideAddExhaustionHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeAddExhaustionHookTypes = new LinkedList<String>();
	private final static List<String> overrideAddExhaustionHookTypes = new LinkedList<String>();
	private final static List<String> afterAddExhaustionHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeAddExhaustionHooks;
	private ServerPlayerBase[] overrideAddExhaustionHooks;
	private ServerPlayerBase[] afterAddExhaustionHooks;

	public boolean isAddExhaustionModded;

	private static final Map<String, String[]> allBaseBeforeAddExhaustionSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeAddExhaustionInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddExhaustionSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddExhaustionInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddExhaustionSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddExhaustionInferiors = new Hashtable<String, String[]>(0);

	public static void addExperience(IServerPlayerAPI target, int paramInt)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isAddExperienceModded)
			serverPlayerAPI.addExperience(paramInt);
		else
			target.localAddExperience(paramInt);
	}

	private void addExperience(int paramInt)
	{
		if(beforeAddExperienceHooks != null)
			for(int i = beforeAddExperienceHooks.length - 1; i >= 0 ; i--)
				beforeAddExperienceHooks[i].beforeAddExperience(paramInt);

		if(overrideAddExperienceHooks != null)
			overrideAddExperienceHooks[overrideAddExperienceHooks.length - 1].addExperience(paramInt);
		else
			player.localAddExperience(paramInt);

		if(afterAddExperienceHooks != null)
			for(int i = 0; i < afterAddExperienceHooks.length; i++)
				afterAddExperienceHooks[i].afterAddExperience(paramInt);

	}

	protected ServerPlayerBase GetOverwrittenAddExperience(ServerPlayerBase overWriter)
	{
		if (overrideAddExperienceHooks == null)
			return overWriter;

		for(int i = 0; i < overrideAddExperienceHooks.length; i++)
			if(overrideAddExperienceHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideAddExperienceHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeAddExperienceHookTypes = new LinkedList<String>();
	private final static List<String> overrideAddExperienceHookTypes = new LinkedList<String>();
	private final static List<String> afterAddExperienceHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeAddExperienceHooks;
	private ServerPlayerBase[] overrideAddExperienceHooks;
	private ServerPlayerBase[] afterAddExperienceHooks;

	public boolean isAddExperienceModded;

	private static final Map<String, String[]> allBaseBeforeAddExperienceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeAddExperienceInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddExperienceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddExperienceInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddExperienceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddExperienceInferiors = new Hashtable<String, String[]>(0);

	public static void addExperienceLevel(IServerPlayerAPI target, int paramInt)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isAddExperienceLevelModded)
			serverPlayerAPI.addExperienceLevel(paramInt);
		else
			target.localAddExperienceLevel(paramInt);
	}

	private void addExperienceLevel(int paramInt)
	{
		if(beforeAddExperienceLevelHooks != null)
			for(int i = beforeAddExperienceLevelHooks.length - 1; i >= 0 ; i--)
				beforeAddExperienceLevelHooks[i].beforeAddExperienceLevel(paramInt);

		if(overrideAddExperienceLevelHooks != null)
			overrideAddExperienceLevelHooks[overrideAddExperienceLevelHooks.length - 1].addExperienceLevel(paramInt);
		else
			player.localAddExperienceLevel(paramInt);

		if(afterAddExperienceLevelHooks != null)
			for(int i = 0; i < afterAddExperienceLevelHooks.length; i++)
				afterAddExperienceLevelHooks[i].afterAddExperienceLevel(paramInt);

	}

	protected ServerPlayerBase GetOverwrittenAddExperienceLevel(ServerPlayerBase overWriter)
	{
		if (overrideAddExperienceLevelHooks == null)
			return overWriter;

		for(int i = 0; i < overrideAddExperienceLevelHooks.length; i++)
			if(overrideAddExperienceLevelHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideAddExperienceLevelHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeAddExperienceLevelHookTypes = new LinkedList<String>();
	private final static List<String> overrideAddExperienceLevelHookTypes = new LinkedList<String>();
	private final static List<String> afterAddExperienceLevelHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeAddExperienceLevelHooks;
	private ServerPlayerBase[] overrideAddExperienceLevelHooks;
	private ServerPlayerBase[] afterAddExperienceLevelHooks;

	public boolean isAddExperienceLevelModded;

	private static final Map<String, String[]> allBaseBeforeAddExperienceLevelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeAddExperienceLevelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddExperienceLevelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddExperienceLevelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddExperienceLevelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddExperienceLevelInferiors = new Hashtable<String, String[]>(0);

	public static void addMovementStat(IServerPlayerAPI target, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isAddMovementStatModded)
			serverPlayerAPI.addMovementStat(paramDouble1, paramDouble2, paramDouble3);
		else
			target.localAddMovementStat(paramDouble1, paramDouble2, paramDouble3);
	}

	private void addMovementStat(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		if(beforeAddMovementStatHooks != null)
			for(int i = beforeAddMovementStatHooks.length - 1; i >= 0 ; i--)
				beforeAddMovementStatHooks[i].beforeAddMovementStat(paramDouble1, paramDouble2, paramDouble3);

		if(overrideAddMovementStatHooks != null)
			overrideAddMovementStatHooks[overrideAddMovementStatHooks.length - 1].addMovementStat(paramDouble1, paramDouble2, paramDouble3);
		else
			player.localAddMovementStat(paramDouble1, paramDouble2, paramDouble3);

		if(afterAddMovementStatHooks != null)
			for(int i = 0; i < afterAddMovementStatHooks.length; i++)
				afterAddMovementStatHooks[i].afterAddMovementStat(paramDouble1, paramDouble2, paramDouble3);

	}

	protected ServerPlayerBase GetOverwrittenAddMovementStat(ServerPlayerBase overWriter)
	{
		if (overrideAddMovementStatHooks == null)
			return overWriter;

		for(int i = 0; i < overrideAddMovementStatHooks.length; i++)
			if(overrideAddMovementStatHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideAddMovementStatHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeAddMovementStatHookTypes = new LinkedList<String>();
	private final static List<String> overrideAddMovementStatHookTypes = new LinkedList<String>();
	private final static List<String> afterAddMovementStatHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeAddMovementStatHooks;
	private ServerPlayerBase[] overrideAddMovementStatHooks;
	private ServerPlayerBase[] afterAddMovementStatHooks;

	public boolean isAddMovementStatModded;

	private static final Map<String, String[]> allBaseBeforeAddMovementStatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeAddMovementStatInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddMovementStatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddMovementStatInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddMovementStatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddMovementStatInferiors = new Hashtable<String, String[]>(0);

	public static boolean attackEntityFrom(IServerPlayerAPI target, net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
		boolean _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isAttackEntityFromModded)
			_result = serverPlayerAPI.attackEntityFrom(paramDamageSource, paramFloat);
		else
			_result = target.localAttackEntityFrom(paramDamageSource, paramFloat);
		return _result;
	}

	private boolean attackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
		if(beforeAttackEntityFromHooks != null)
			for(int i = beforeAttackEntityFromHooks.length - 1; i >= 0 ; i--)
				beforeAttackEntityFromHooks[i].beforeAttackEntityFrom(paramDamageSource, paramFloat);

		boolean _result;
		if(overrideAttackEntityFromHooks != null)
			_result = overrideAttackEntityFromHooks[overrideAttackEntityFromHooks.length - 1].attackEntityFrom(paramDamageSource, paramFloat);
		else
			_result = player.localAttackEntityFrom(paramDamageSource, paramFloat);

		if(afterAttackEntityFromHooks != null)
			for(int i = 0; i < afterAttackEntityFromHooks.length; i++)
				afterAttackEntityFromHooks[i].afterAttackEntityFrom(paramDamageSource, paramFloat);

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenAttackEntityFrom(ServerPlayerBase overWriter)
	{
		if (overrideAttackEntityFromHooks == null)
			return overWriter;

		for(int i = 0; i < overrideAttackEntityFromHooks.length; i++)
			if(overrideAttackEntityFromHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideAttackEntityFromHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeAttackEntityFromHookTypes = new LinkedList<String>();
	private final static List<String> overrideAttackEntityFromHookTypes = new LinkedList<String>();
	private final static List<String> afterAttackEntityFromHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeAttackEntityFromHooks;
	private ServerPlayerBase[] overrideAttackEntityFromHooks;
	private ServerPlayerBase[] afterAttackEntityFromHooks;

	public boolean isAttackEntityFromModded;

	private static final Map<String, String[]> allBaseBeforeAttackEntityFromSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeAttackEntityFromInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAttackEntityFromSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAttackEntityFromInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAttackEntityFromSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAttackEntityFromInferiors = new Hashtable<String, String[]>(0);

	public static void attackTargetEntityWithCurrentItem(IServerPlayerAPI target, net.minecraft.entity.Entity paramEntity)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isAttackTargetEntityWithCurrentItemModded)
			serverPlayerAPI.attackTargetEntityWithCurrentItem(paramEntity);
		else
			target.localAttackTargetEntityWithCurrentItem(paramEntity);
	}

	private void attackTargetEntityWithCurrentItem(net.minecraft.entity.Entity paramEntity)
	{
		if(beforeAttackTargetEntityWithCurrentItemHooks != null)
			for(int i = beforeAttackTargetEntityWithCurrentItemHooks.length - 1; i >= 0 ; i--)
				beforeAttackTargetEntityWithCurrentItemHooks[i].beforeAttackTargetEntityWithCurrentItem(paramEntity);

		if(overrideAttackTargetEntityWithCurrentItemHooks != null)
			overrideAttackTargetEntityWithCurrentItemHooks[overrideAttackTargetEntityWithCurrentItemHooks.length - 1].attackTargetEntityWithCurrentItem(paramEntity);
		else
			player.localAttackTargetEntityWithCurrentItem(paramEntity);

		if(afterAttackTargetEntityWithCurrentItemHooks != null)
			for(int i = 0; i < afterAttackTargetEntityWithCurrentItemHooks.length; i++)
				afterAttackTargetEntityWithCurrentItemHooks[i].afterAttackTargetEntityWithCurrentItem(paramEntity);

	}

	protected ServerPlayerBase GetOverwrittenAttackTargetEntityWithCurrentItem(ServerPlayerBase overWriter)
	{
		if (overrideAttackTargetEntityWithCurrentItemHooks == null)
			return overWriter;

		for(int i = 0; i < overrideAttackTargetEntityWithCurrentItemHooks.length; i++)
			if(overrideAttackTargetEntityWithCurrentItemHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideAttackTargetEntityWithCurrentItemHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeAttackTargetEntityWithCurrentItemHookTypes = new LinkedList<String>();
	private final static List<String> overrideAttackTargetEntityWithCurrentItemHookTypes = new LinkedList<String>();
	private final static List<String> afterAttackTargetEntityWithCurrentItemHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeAttackTargetEntityWithCurrentItemHooks;
	private ServerPlayerBase[] overrideAttackTargetEntityWithCurrentItemHooks;
	private ServerPlayerBase[] afterAttackTargetEntityWithCurrentItemHooks;

	public boolean isAttackTargetEntityWithCurrentItemModded;

	private static final Map<String, String[]> allBaseBeforeAttackTargetEntityWithCurrentItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeAttackTargetEntityWithCurrentItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAttackTargetEntityWithCurrentItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAttackTargetEntityWithCurrentItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAttackTargetEntityWithCurrentItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAttackTargetEntityWithCurrentItemInferiors = new Hashtable<String, String[]>(0);

	public static boolean canBreatheUnderwater(IServerPlayerAPI target)
	{
		boolean _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isCanBreatheUnderwaterModded)
			_result = serverPlayerAPI.canBreatheUnderwater();
		else
			_result = target.localCanBreatheUnderwater();
		return _result;
	}

	private boolean canBreatheUnderwater()
	{
		if(beforeCanBreatheUnderwaterHooks != null)
			for(int i = beforeCanBreatheUnderwaterHooks.length - 1; i >= 0 ; i--)
				beforeCanBreatheUnderwaterHooks[i].beforeCanBreatheUnderwater();

		boolean _result;
		if(overrideCanBreatheUnderwaterHooks != null)
			_result = overrideCanBreatheUnderwaterHooks[overrideCanBreatheUnderwaterHooks.length - 1].canBreatheUnderwater();
		else
			_result = player.localCanBreatheUnderwater();

		if(afterCanBreatheUnderwaterHooks != null)
			for(int i = 0; i < afterCanBreatheUnderwaterHooks.length; i++)
				afterCanBreatheUnderwaterHooks[i].afterCanBreatheUnderwater();

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenCanBreatheUnderwater(ServerPlayerBase overWriter)
	{
		if (overrideCanBreatheUnderwaterHooks == null)
			return overWriter;

		for(int i = 0; i < overrideCanBreatheUnderwaterHooks.length; i++)
			if(overrideCanBreatheUnderwaterHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideCanBreatheUnderwaterHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeCanBreatheUnderwaterHookTypes = new LinkedList<String>();
	private final static List<String> overrideCanBreatheUnderwaterHookTypes = new LinkedList<String>();
	private final static List<String> afterCanBreatheUnderwaterHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeCanBreatheUnderwaterHooks;
	private ServerPlayerBase[] overrideCanBreatheUnderwaterHooks;
	private ServerPlayerBase[] afterCanBreatheUnderwaterHooks;

	public boolean isCanBreatheUnderwaterModded;

	private static final Map<String, String[]> allBaseBeforeCanBreatheUnderwaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeCanBreatheUnderwaterInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanBreatheUnderwaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanBreatheUnderwaterInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanBreatheUnderwaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanBreatheUnderwaterInferiors = new Hashtable<String, String[]>(0);

	public static boolean canHarvestBlock(IServerPlayerAPI target, net.minecraft.block.Block paramBlock)
	{
		boolean _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isCanHarvestBlockModded)
			_result = serverPlayerAPI.canHarvestBlock(paramBlock);
		else
			_result = target.localCanHarvestBlock(paramBlock);
		return _result;
	}

	private boolean canHarvestBlock(net.minecraft.block.Block paramBlock)
	{
		if(beforeCanHarvestBlockHooks != null)
			for(int i = beforeCanHarvestBlockHooks.length - 1; i >= 0 ; i--)
				beforeCanHarvestBlockHooks[i].beforeCanHarvestBlock(paramBlock);

		boolean _result;
		if(overrideCanHarvestBlockHooks != null)
			_result = overrideCanHarvestBlockHooks[overrideCanHarvestBlockHooks.length - 1].canHarvestBlock(paramBlock);
		else
			_result = player.localCanHarvestBlock(paramBlock);

		if(afterCanHarvestBlockHooks != null)
			for(int i = 0; i < afterCanHarvestBlockHooks.length; i++)
				afterCanHarvestBlockHooks[i].afterCanHarvestBlock(paramBlock);

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenCanHarvestBlock(ServerPlayerBase overWriter)
	{
		if (overrideCanHarvestBlockHooks == null)
			return overWriter;

		for(int i = 0; i < overrideCanHarvestBlockHooks.length; i++)
			if(overrideCanHarvestBlockHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideCanHarvestBlockHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeCanHarvestBlockHookTypes = new LinkedList<String>();
	private final static List<String> overrideCanHarvestBlockHookTypes = new LinkedList<String>();
	private final static List<String> afterCanHarvestBlockHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeCanHarvestBlockHooks;
	private ServerPlayerBase[] overrideCanHarvestBlockHooks;
	private ServerPlayerBase[] afterCanHarvestBlockHooks;

	public boolean isCanHarvestBlockModded;

	private static final Map<String, String[]> allBaseBeforeCanHarvestBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeCanHarvestBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanHarvestBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanHarvestBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanHarvestBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanHarvestBlockInferiors = new Hashtable<String, String[]>(0);

	public static boolean canPlayerEdit(IServerPlayerAPI target, int paramInt1, int paramInt2, int paramInt3, int paramInt4, net.minecraft.item.ItemStack paramItemStack)
	{
		boolean _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isCanPlayerEditModded)
			_result = serverPlayerAPI.canPlayerEdit(paramInt1, paramInt2, paramInt3, paramInt4, paramItemStack);
		else
			_result = target.localCanPlayerEdit(paramInt1, paramInt2, paramInt3, paramInt4, paramItemStack);
		return _result;
	}

	private boolean canPlayerEdit(int paramInt1, int paramInt2, int paramInt3, int paramInt4, net.minecraft.item.ItemStack paramItemStack)
	{
		if(beforeCanPlayerEditHooks != null)
			for(int i = beforeCanPlayerEditHooks.length - 1; i >= 0 ; i--)
				beforeCanPlayerEditHooks[i].beforeCanPlayerEdit(paramInt1, paramInt2, paramInt3, paramInt4, paramItemStack);

		boolean _result;
		if(overrideCanPlayerEditHooks != null)
			_result = overrideCanPlayerEditHooks[overrideCanPlayerEditHooks.length - 1].canPlayerEdit(paramInt1, paramInt2, paramInt3, paramInt4, paramItemStack);
		else
			_result = player.localCanPlayerEdit(paramInt1, paramInt2, paramInt3, paramInt4, paramItemStack);

		if(afterCanPlayerEditHooks != null)
			for(int i = 0; i < afterCanPlayerEditHooks.length; i++)
				afterCanPlayerEditHooks[i].afterCanPlayerEdit(paramInt1, paramInt2, paramInt3, paramInt4, paramItemStack);

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenCanPlayerEdit(ServerPlayerBase overWriter)
	{
		if (overrideCanPlayerEditHooks == null)
			return overWriter;

		for(int i = 0; i < overrideCanPlayerEditHooks.length; i++)
			if(overrideCanPlayerEditHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideCanPlayerEditHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeCanPlayerEditHookTypes = new LinkedList<String>();
	private final static List<String> overrideCanPlayerEditHookTypes = new LinkedList<String>();
	private final static List<String> afterCanPlayerEditHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeCanPlayerEditHooks;
	private ServerPlayerBase[] overrideCanPlayerEditHooks;
	private ServerPlayerBase[] afterCanPlayerEditHooks;

	public boolean isCanPlayerEditModded;

	private static final Map<String, String[]> allBaseBeforeCanPlayerEditSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeCanPlayerEditInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanPlayerEditSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanPlayerEditInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanPlayerEditSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanPlayerEditInferiors = new Hashtable<String, String[]>(0);

	public static boolean canTriggerWalking(IServerPlayerAPI target)
	{
		boolean _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isCanTriggerWalkingModded)
			_result = serverPlayerAPI.canTriggerWalking();
		else
			_result = target.localCanTriggerWalking();
		return _result;
	}

	private boolean canTriggerWalking()
	{
		if(beforeCanTriggerWalkingHooks != null)
			for(int i = beforeCanTriggerWalkingHooks.length - 1; i >= 0 ; i--)
				beforeCanTriggerWalkingHooks[i].beforeCanTriggerWalking();

		boolean _result;
		if(overrideCanTriggerWalkingHooks != null)
			_result = overrideCanTriggerWalkingHooks[overrideCanTriggerWalkingHooks.length - 1].canTriggerWalking();
		else
			_result = player.localCanTriggerWalking();

		if(afterCanTriggerWalkingHooks != null)
			for(int i = 0; i < afterCanTriggerWalkingHooks.length; i++)
				afterCanTriggerWalkingHooks[i].afterCanTriggerWalking();

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenCanTriggerWalking(ServerPlayerBase overWriter)
	{
		if (overrideCanTriggerWalkingHooks == null)
			return overWriter;

		for(int i = 0; i < overrideCanTriggerWalkingHooks.length; i++)
			if(overrideCanTriggerWalkingHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideCanTriggerWalkingHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeCanTriggerWalkingHookTypes = new LinkedList<String>();
	private final static List<String> overrideCanTriggerWalkingHookTypes = new LinkedList<String>();
	private final static List<String> afterCanTriggerWalkingHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeCanTriggerWalkingHooks;
	private ServerPlayerBase[] overrideCanTriggerWalkingHooks;
	private ServerPlayerBase[] afterCanTriggerWalkingHooks;

	public boolean isCanTriggerWalkingModded;

	private static final Map<String, String[]> allBaseBeforeCanTriggerWalkingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeCanTriggerWalkingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanTriggerWalkingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanTriggerWalkingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanTriggerWalkingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanTriggerWalkingInferiors = new Hashtable<String, String[]>(0);

	public static void clonePlayer(IServerPlayerAPI target, net.minecraft.entity.player.EntityPlayer paramEntityPlayer, boolean paramBoolean)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isClonePlayerModded)
			serverPlayerAPI.clonePlayer(paramEntityPlayer, paramBoolean);
		else
			target.localClonePlayer(paramEntityPlayer, paramBoolean);
	}

	private void clonePlayer(net.minecraft.entity.player.EntityPlayer paramEntityPlayer, boolean paramBoolean)
	{
		if(beforeClonePlayerHooks != null)
			for(int i = beforeClonePlayerHooks.length - 1; i >= 0 ; i--)
				beforeClonePlayerHooks[i].beforeClonePlayer(paramEntityPlayer, paramBoolean);

		if(overrideClonePlayerHooks != null)
			overrideClonePlayerHooks[overrideClonePlayerHooks.length - 1].clonePlayer(paramEntityPlayer, paramBoolean);
		else
			player.localClonePlayer(paramEntityPlayer, paramBoolean);

		if(afterClonePlayerHooks != null)
			for(int i = 0; i < afterClonePlayerHooks.length; i++)
				afterClonePlayerHooks[i].afterClonePlayer(paramEntityPlayer, paramBoolean);

	}

	protected ServerPlayerBase GetOverwrittenClonePlayer(ServerPlayerBase overWriter)
	{
		if (overrideClonePlayerHooks == null)
			return overWriter;

		for(int i = 0; i < overrideClonePlayerHooks.length; i++)
			if(overrideClonePlayerHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideClonePlayerHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeClonePlayerHookTypes = new LinkedList<String>();
	private final static List<String> overrideClonePlayerHookTypes = new LinkedList<String>();
	private final static List<String> afterClonePlayerHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeClonePlayerHooks;
	private ServerPlayerBase[] overrideClonePlayerHooks;
	private ServerPlayerBase[] afterClonePlayerHooks;

	public boolean isClonePlayerModded;

	private static final Map<String, String[]> allBaseBeforeClonePlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeClonePlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideClonePlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideClonePlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterClonePlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterClonePlayerInferiors = new Hashtable<String, String[]>(0);

	public static void damageEntity(IServerPlayerAPI target, net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isDamageEntityModded)
			serverPlayerAPI.damageEntity(paramDamageSource, paramFloat);
		else
			target.localDamageEntity(paramDamageSource, paramFloat);
	}

	private void damageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
		if(beforeDamageEntityHooks != null)
			for(int i = beforeDamageEntityHooks.length - 1; i >= 0 ; i--)
				beforeDamageEntityHooks[i].beforeDamageEntity(paramDamageSource, paramFloat);

		if(overrideDamageEntityHooks != null)
			overrideDamageEntityHooks[overrideDamageEntityHooks.length - 1].damageEntity(paramDamageSource, paramFloat);
		else
			player.localDamageEntity(paramDamageSource, paramFloat);

		if(afterDamageEntityHooks != null)
			for(int i = 0; i < afterDamageEntityHooks.length; i++)
				afterDamageEntityHooks[i].afterDamageEntity(paramDamageSource, paramFloat);

	}

	protected ServerPlayerBase GetOverwrittenDamageEntity(ServerPlayerBase overWriter)
	{
		if (overrideDamageEntityHooks == null)
			return overWriter;

		for(int i = 0; i < overrideDamageEntityHooks.length; i++)
			if(overrideDamageEntityHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDamageEntityHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDamageEntityHookTypes = new LinkedList<String>();
	private final static List<String> overrideDamageEntityHookTypes = new LinkedList<String>();
	private final static List<String> afterDamageEntityHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeDamageEntityHooks;
	private ServerPlayerBase[] overrideDamageEntityHooks;
	private ServerPlayerBase[] afterDamageEntityHooks;

	public boolean isDamageEntityModded;

	private static final Map<String, String[]> allBaseBeforeDamageEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDamageEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDamageEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDamageEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDamageEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDamageEntityInferiors = new Hashtable<String, String[]>(0);

	public static void displayGUIChest(IServerPlayerAPI target, net.minecraft.inventory.IInventory paramIInventory)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isDisplayGUIChestModded)
			serverPlayerAPI.displayGUIChest(paramIInventory);
		else
			target.localDisplayGUIChest(paramIInventory);
	}

	private void displayGUIChest(net.minecraft.inventory.IInventory paramIInventory)
	{
		if(beforeDisplayGUIChestHooks != null)
			for(int i = beforeDisplayGUIChestHooks.length - 1; i >= 0 ; i--)
				beforeDisplayGUIChestHooks[i].beforeDisplayGUIChest(paramIInventory);

		if(overrideDisplayGUIChestHooks != null)
			overrideDisplayGUIChestHooks[overrideDisplayGUIChestHooks.length - 1].displayGUIChest(paramIInventory);
		else
			player.localDisplayGUIChest(paramIInventory);

		if(afterDisplayGUIChestHooks != null)
			for(int i = 0; i < afterDisplayGUIChestHooks.length; i++)
				afterDisplayGUIChestHooks[i].afterDisplayGUIChest(paramIInventory);

	}

	protected ServerPlayerBase GetOverwrittenDisplayGUIChest(ServerPlayerBase overWriter)
	{
		if (overrideDisplayGUIChestHooks == null)
			return overWriter;

		for(int i = 0; i < overrideDisplayGUIChestHooks.length; i++)
			if(overrideDisplayGUIChestHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDisplayGUIChestHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDisplayGUIChestHookTypes = new LinkedList<String>();
	private final static List<String> overrideDisplayGUIChestHookTypes = new LinkedList<String>();
	private final static List<String> afterDisplayGUIChestHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeDisplayGUIChestHooks;
	private ServerPlayerBase[] overrideDisplayGUIChestHooks;
	private ServerPlayerBase[] afterDisplayGUIChestHooks;

	public boolean isDisplayGUIChestModded;

	private static final Map<String, String[]> allBaseBeforeDisplayGUIChestSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDisplayGUIChestInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIChestSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIChestInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIChestSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIChestInferiors = new Hashtable<String, String[]>(0);

	public static void displayGUIDispenser(IServerPlayerAPI target, net.minecraft.tileentity.TileEntityDispenser paramTileEntityDispenser)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isDisplayGUIDispenserModded)
			serverPlayerAPI.displayGUIDispenser(paramTileEntityDispenser);
		else
			target.localDisplayGUIDispenser(paramTileEntityDispenser);
	}

	private void displayGUIDispenser(net.minecraft.tileentity.TileEntityDispenser paramTileEntityDispenser)
	{
		if(beforeDisplayGUIDispenserHooks != null)
			for(int i = beforeDisplayGUIDispenserHooks.length - 1; i >= 0 ; i--)
				beforeDisplayGUIDispenserHooks[i].beforeDisplayGUIDispenser(paramTileEntityDispenser);

		if(overrideDisplayGUIDispenserHooks != null)
			overrideDisplayGUIDispenserHooks[overrideDisplayGUIDispenserHooks.length - 1].displayGUIDispenser(paramTileEntityDispenser);
		else
			player.localDisplayGUIDispenser(paramTileEntityDispenser);

		if(afterDisplayGUIDispenserHooks != null)
			for(int i = 0; i < afterDisplayGUIDispenserHooks.length; i++)
				afterDisplayGUIDispenserHooks[i].afterDisplayGUIDispenser(paramTileEntityDispenser);

	}

	protected ServerPlayerBase GetOverwrittenDisplayGUIDispenser(ServerPlayerBase overWriter)
	{
		if (overrideDisplayGUIDispenserHooks == null)
			return overWriter;

		for(int i = 0; i < overrideDisplayGUIDispenserHooks.length; i++)
			if(overrideDisplayGUIDispenserHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDisplayGUIDispenserHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDisplayGUIDispenserHookTypes = new LinkedList<String>();
	private final static List<String> overrideDisplayGUIDispenserHookTypes = new LinkedList<String>();
	private final static List<String> afterDisplayGUIDispenserHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeDisplayGUIDispenserHooks;
	private ServerPlayerBase[] overrideDisplayGUIDispenserHooks;
	private ServerPlayerBase[] afterDisplayGUIDispenserHooks;

	public boolean isDisplayGUIDispenserModded;

	private static final Map<String, String[]> allBaseBeforeDisplayGUIDispenserSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDisplayGUIDispenserInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIDispenserSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIDispenserInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIDispenserSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIDispenserInferiors = new Hashtable<String, String[]>(0);

	public static void displayGUIFurnace(IServerPlayerAPI target, net.minecraft.tileentity.TileEntityFurnace paramTileEntityFurnace)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isDisplayGUIFurnaceModded)
			serverPlayerAPI.displayGUIFurnace(paramTileEntityFurnace);
		else
			target.localDisplayGUIFurnace(paramTileEntityFurnace);
	}

	private void displayGUIFurnace(net.minecraft.tileentity.TileEntityFurnace paramTileEntityFurnace)
	{
		if(beforeDisplayGUIFurnaceHooks != null)
			for(int i = beforeDisplayGUIFurnaceHooks.length - 1; i >= 0 ; i--)
				beforeDisplayGUIFurnaceHooks[i].beforeDisplayGUIFurnace(paramTileEntityFurnace);

		if(overrideDisplayGUIFurnaceHooks != null)
			overrideDisplayGUIFurnaceHooks[overrideDisplayGUIFurnaceHooks.length - 1].displayGUIFurnace(paramTileEntityFurnace);
		else
			player.localDisplayGUIFurnace(paramTileEntityFurnace);

		if(afterDisplayGUIFurnaceHooks != null)
			for(int i = 0; i < afterDisplayGUIFurnaceHooks.length; i++)
				afterDisplayGUIFurnaceHooks[i].afterDisplayGUIFurnace(paramTileEntityFurnace);

	}

	protected ServerPlayerBase GetOverwrittenDisplayGUIFurnace(ServerPlayerBase overWriter)
	{
		if (overrideDisplayGUIFurnaceHooks == null)
			return overWriter;

		for(int i = 0; i < overrideDisplayGUIFurnaceHooks.length; i++)
			if(overrideDisplayGUIFurnaceHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDisplayGUIFurnaceHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDisplayGUIFurnaceHookTypes = new LinkedList<String>();
	private final static List<String> overrideDisplayGUIFurnaceHookTypes = new LinkedList<String>();
	private final static List<String> afterDisplayGUIFurnaceHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeDisplayGUIFurnaceHooks;
	private ServerPlayerBase[] overrideDisplayGUIFurnaceHooks;
	private ServerPlayerBase[] afterDisplayGUIFurnaceHooks;

	public boolean isDisplayGUIFurnaceModded;

	private static final Map<String, String[]> allBaseBeforeDisplayGUIFurnaceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDisplayGUIFurnaceInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIFurnaceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIFurnaceInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIFurnaceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIFurnaceInferiors = new Hashtable<String, String[]>(0);

	public static void displayGUIWorkbench(IServerPlayerAPI target, int paramInt1, int paramInt2, int paramInt3)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isDisplayGUIWorkbenchModded)
			serverPlayerAPI.displayGUIWorkbench(paramInt1, paramInt2, paramInt3);
		else
			target.localDisplayGUIWorkbench(paramInt1, paramInt2, paramInt3);
	}

	private void displayGUIWorkbench(int paramInt1, int paramInt2, int paramInt3)
	{
		if(beforeDisplayGUIWorkbenchHooks != null)
			for(int i = beforeDisplayGUIWorkbenchHooks.length - 1; i >= 0 ; i--)
				beforeDisplayGUIWorkbenchHooks[i].beforeDisplayGUIWorkbench(paramInt1, paramInt2, paramInt3);

		if(overrideDisplayGUIWorkbenchHooks != null)
			overrideDisplayGUIWorkbenchHooks[overrideDisplayGUIWorkbenchHooks.length - 1].displayGUIWorkbench(paramInt1, paramInt2, paramInt3);
		else
			player.localDisplayGUIWorkbench(paramInt1, paramInt2, paramInt3);

		if(afterDisplayGUIWorkbenchHooks != null)
			for(int i = 0; i < afterDisplayGUIWorkbenchHooks.length; i++)
				afterDisplayGUIWorkbenchHooks[i].afterDisplayGUIWorkbench(paramInt1, paramInt2, paramInt3);

	}

	protected ServerPlayerBase GetOverwrittenDisplayGUIWorkbench(ServerPlayerBase overWriter)
	{
		if (overrideDisplayGUIWorkbenchHooks == null)
			return overWriter;

		for(int i = 0; i < overrideDisplayGUIWorkbenchHooks.length; i++)
			if(overrideDisplayGUIWorkbenchHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDisplayGUIWorkbenchHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDisplayGUIWorkbenchHookTypes = new LinkedList<String>();
	private final static List<String> overrideDisplayGUIWorkbenchHookTypes = new LinkedList<String>();
	private final static List<String> afterDisplayGUIWorkbenchHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeDisplayGUIWorkbenchHooks;
	private ServerPlayerBase[] overrideDisplayGUIWorkbenchHooks;
	private ServerPlayerBase[] afterDisplayGUIWorkbenchHooks;

	public boolean isDisplayGUIWorkbenchModded;

	private static final Map<String, String[]> allBaseBeforeDisplayGUIWorkbenchSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDisplayGUIWorkbenchInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIWorkbenchSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIWorkbenchInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIWorkbenchSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIWorkbenchInferiors = new Hashtable<String, String[]>(0);

	public static net.minecraft.entity.item.EntityItem dropOneItem(IServerPlayerAPI target, boolean paramBoolean)
	{
		net.minecraft.entity.item.EntityItem _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isDropOneItemModded)
			_result = serverPlayerAPI.dropOneItem(paramBoolean);
		else
			_result = target.localDropOneItem(paramBoolean);
		return _result;
	}

	private net.minecraft.entity.item.EntityItem dropOneItem(boolean paramBoolean)
	{
		if(beforeDropOneItemHooks != null)
			for(int i = beforeDropOneItemHooks.length - 1; i >= 0 ; i--)
				beforeDropOneItemHooks[i].beforeDropOneItem(paramBoolean);

		net.minecraft.entity.item.EntityItem _result;
		if(overrideDropOneItemHooks != null)
			_result = overrideDropOneItemHooks[overrideDropOneItemHooks.length - 1].dropOneItem(paramBoolean);
		else
			_result = player.localDropOneItem(paramBoolean);

		if(afterDropOneItemHooks != null)
			for(int i = 0; i < afterDropOneItemHooks.length; i++)
				afterDropOneItemHooks[i].afterDropOneItem(paramBoolean);

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenDropOneItem(ServerPlayerBase overWriter)
	{
		if (overrideDropOneItemHooks == null)
			return overWriter;

		for(int i = 0; i < overrideDropOneItemHooks.length; i++)
			if(overrideDropOneItemHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDropOneItemHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDropOneItemHookTypes = new LinkedList<String>();
	private final static List<String> overrideDropOneItemHookTypes = new LinkedList<String>();
	private final static List<String> afterDropOneItemHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeDropOneItemHooks;
	private ServerPlayerBase[] overrideDropOneItemHooks;
	private ServerPlayerBase[] afterDropOneItemHooks;

	public boolean isDropOneItemModded;

	private static final Map<String, String[]> allBaseBeforeDropOneItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDropOneItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDropOneItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDropOneItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDropOneItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDropOneItemInferiors = new Hashtable<String, String[]>(0);

	public static net.minecraft.entity.item.EntityItem dropPlayerItem(IServerPlayerAPI target, net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean)
	{
		net.minecraft.entity.item.EntityItem _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isDropPlayerItemModded)
			_result = serverPlayerAPI.dropPlayerItem(paramItemStack, paramBoolean);
		else
			_result = target.localDropPlayerItem(paramItemStack, paramBoolean);
		return _result;
	}

	private net.minecraft.entity.item.EntityItem dropPlayerItem(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean)
	{
		if(beforeDropPlayerItemHooks != null)
			for(int i = beforeDropPlayerItemHooks.length - 1; i >= 0 ; i--)
				beforeDropPlayerItemHooks[i].beforeDropPlayerItem(paramItemStack, paramBoolean);

		net.minecraft.entity.item.EntityItem _result;
		if(overrideDropPlayerItemHooks != null)
			_result = overrideDropPlayerItemHooks[overrideDropPlayerItemHooks.length - 1].dropPlayerItem(paramItemStack, paramBoolean);
		else
			_result = player.localDropPlayerItem(paramItemStack, paramBoolean);

		if(afterDropPlayerItemHooks != null)
			for(int i = 0; i < afterDropPlayerItemHooks.length; i++)
				afterDropPlayerItemHooks[i].afterDropPlayerItem(paramItemStack, paramBoolean);

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenDropPlayerItem(ServerPlayerBase overWriter)
	{
		if (overrideDropPlayerItemHooks == null)
			return overWriter;

		for(int i = 0; i < overrideDropPlayerItemHooks.length; i++)
			if(overrideDropPlayerItemHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDropPlayerItemHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDropPlayerItemHookTypes = new LinkedList<String>();
	private final static List<String> overrideDropPlayerItemHookTypes = new LinkedList<String>();
	private final static List<String> afterDropPlayerItemHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeDropPlayerItemHooks;
	private ServerPlayerBase[] overrideDropPlayerItemHooks;
	private ServerPlayerBase[] afterDropPlayerItemHooks;

	public boolean isDropPlayerItemModded;

	private static final Map<String, String[]> allBaseBeforeDropPlayerItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDropPlayerItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDropPlayerItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDropPlayerItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDropPlayerItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDropPlayerItemInferiors = new Hashtable<String, String[]>(0);

	public static void fall(IServerPlayerAPI target, float paramFloat)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isFallModded)
			serverPlayerAPI.fall(paramFloat);
		else
			target.localFall(paramFloat);
	}

	private void fall(float paramFloat)
	{
		if(beforeFallHooks != null)
			for(int i = beforeFallHooks.length - 1; i >= 0 ; i--)
				beforeFallHooks[i].beforeFall(paramFloat);

		if(overrideFallHooks != null)
			overrideFallHooks[overrideFallHooks.length - 1].fall(paramFloat);
		else
			player.localFall(paramFloat);

		if(afterFallHooks != null)
			for(int i = 0; i < afterFallHooks.length; i++)
				afterFallHooks[i].afterFall(paramFloat);

	}

	protected ServerPlayerBase GetOverwrittenFall(ServerPlayerBase overWriter)
	{
		if (overrideFallHooks == null)
			return overWriter;

		for(int i = 0; i < overrideFallHooks.length; i++)
			if(overrideFallHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideFallHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeFallHookTypes = new LinkedList<String>();
	private final static List<String> overrideFallHookTypes = new LinkedList<String>();
	private final static List<String> afterFallHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeFallHooks;
	private ServerPlayerBase[] overrideFallHooks;
	private ServerPlayerBase[] afterFallHooks;

	public boolean isFallModded;

	private static final Map<String, String[]> allBaseBeforeFallSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeFallInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideFallSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideFallInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterFallSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterFallInferiors = new Hashtable<String, String[]>(0);

	public static float getAIMoveSpeed(IServerPlayerAPI target)
	{
		float _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isGetAIMoveSpeedModded)
			_result = serverPlayerAPI.getAIMoveSpeed();
		else
			_result = target.localGetAIMoveSpeed();
		return _result;
	}

	private float getAIMoveSpeed()
	{
		if(beforeGetAIMoveSpeedHooks != null)
			for(int i = beforeGetAIMoveSpeedHooks.length - 1; i >= 0 ; i--)
				beforeGetAIMoveSpeedHooks[i].beforeGetAIMoveSpeed();

		float _result;
		if(overrideGetAIMoveSpeedHooks != null)
			_result = overrideGetAIMoveSpeedHooks[overrideGetAIMoveSpeedHooks.length - 1].getAIMoveSpeed();
		else
			_result = player.localGetAIMoveSpeed();

		if(afterGetAIMoveSpeedHooks != null)
			for(int i = 0; i < afterGetAIMoveSpeedHooks.length; i++)
				afterGetAIMoveSpeedHooks[i].afterGetAIMoveSpeed();

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenGetAIMoveSpeed(ServerPlayerBase overWriter)
	{
		if (overrideGetAIMoveSpeedHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetAIMoveSpeedHooks.length; i++)
			if(overrideGetAIMoveSpeedHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetAIMoveSpeedHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetAIMoveSpeedHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetAIMoveSpeedHookTypes = new LinkedList<String>();
	private final static List<String> afterGetAIMoveSpeedHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeGetAIMoveSpeedHooks;
	private ServerPlayerBase[] overrideGetAIMoveSpeedHooks;
	private ServerPlayerBase[] afterGetAIMoveSpeedHooks;

	public boolean isGetAIMoveSpeedModded;

	private static final Map<String, String[]> allBaseBeforeGetAIMoveSpeedSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetAIMoveSpeedInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetAIMoveSpeedSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetAIMoveSpeedInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetAIMoveSpeedSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetAIMoveSpeedInferiors = new Hashtable<String, String[]>(0);

	public static float getCurrentPlayerStrVsBlock(IServerPlayerAPI target, net.minecraft.block.Block paramBlock, boolean paramBoolean)
	{
		float _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isGetCurrentPlayerStrVsBlockModded)
			_result = serverPlayerAPI.getCurrentPlayerStrVsBlock(paramBlock, paramBoolean);
		else
			_result = target.localGetCurrentPlayerStrVsBlock(paramBlock, paramBoolean);
		return _result;
	}

	private float getCurrentPlayerStrVsBlock(net.minecraft.block.Block paramBlock, boolean paramBoolean)
	{
		if(beforeGetCurrentPlayerStrVsBlockHooks != null)
			for(int i = beforeGetCurrentPlayerStrVsBlockHooks.length - 1; i >= 0 ; i--)
				beforeGetCurrentPlayerStrVsBlockHooks[i].beforeGetCurrentPlayerStrVsBlock(paramBlock, paramBoolean);

		float _result;
		if(overrideGetCurrentPlayerStrVsBlockHooks != null)
			_result = overrideGetCurrentPlayerStrVsBlockHooks[overrideGetCurrentPlayerStrVsBlockHooks.length - 1].getCurrentPlayerStrVsBlock(paramBlock, paramBoolean);
		else
			_result = player.localGetCurrentPlayerStrVsBlock(paramBlock, paramBoolean);

		if(afterGetCurrentPlayerStrVsBlockHooks != null)
			for(int i = 0; i < afterGetCurrentPlayerStrVsBlockHooks.length; i++)
				afterGetCurrentPlayerStrVsBlockHooks[i].afterGetCurrentPlayerStrVsBlock(paramBlock, paramBoolean);

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenGetCurrentPlayerStrVsBlock(ServerPlayerBase overWriter)
	{
		if (overrideGetCurrentPlayerStrVsBlockHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetCurrentPlayerStrVsBlockHooks.length; i++)
			if(overrideGetCurrentPlayerStrVsBlockHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetCurrentPlayerStrVsBlockHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetCurrentPlayerStrVsBlockHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetCurrentPlayerStrVsBlockHookTypes = new LinkedList<String>();
	private final static List<String> afterGetCurrentPlayerStrVsBlockHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeGetCurrentPlayerStrVsBlockHooks;
	private ServerPlayerBase[] overrideGetCurrentPlayerStrVsBlockHooks;
	private ServerPlayerBase[] afterGetCurrentPlayerStrVsBlockHooks;

	public boolean isGetCurrentPlayerStrVsBlockModded;

	private static final Map<String, String[]> allBaseBeforeGetCurrentPlayerStrVsBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetCurrentPlayerStrVsBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetCurrentPlayerStrVsBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetCurrentPlayerStrVsBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetCurrentPlayerStrVsBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetCurrentPlayerStrVsBlockInferiors = new Hashtable<String, String[]>(0);

	public static float getCurrentPlayerStrVsBlockForge(IServerPlayerAPI target, net.minecraft.block.Block paramBlock, boolean paramBoolean, int paramInt)
	{
		float _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isGetCurrentPlayerStrVsBlockForgeModded)
			_result = serverPlayerAPI.getCurrentPlayerStrVsBlockForge(paramBlock, paramBoolean, paramInt);
		else
			_result = target.localGetCurrentPlayerStrVsBlockForge(paramBlock, paramBoolean, paramInt);
		return _result;
	}

	private float getCurrentPlayerStrVsBlockForge(net.minecraft.block.Block paramBlock, boolean paramBoolean, int paramInt)
	{
		if(beforeGetCurrentPlayerStrVsBlockForgeHooks != null)
			for(int i = beforeGetCurrentPlayerStrVsBlockForgeHooks.length - 1; i >= 0 ; i--)
				beforeGetCurrentPlayerStrVsBlockForgeHooks[i].beforeGetCurrentPlayerStrVsBlockForge(paramBlock, paramBoolean, paramInt);

		float _result;
		if(overrideGetCurrentPlayerStrVsBlockForgeHooks != null)
			_result = overrideGetCurrentPlayerStrVsBlockForgeHooks[overrideGetCurrentPlayerStrVsBlockForgeHooks.length - 1].getCurrentPlayerStrVsBlockForge(paramBlock, paramBoolean, paramInt);
		else
			_result = player.localGetCurrentPlayerStrVsBlockForge(paramBlock, paramBoolean, paramInt);

		if(afterGetCurrentPlayerStrVsBlockForgeHooks != null)
			for(int i = 0; i < afterGetCurrentPlayerStrVsBlockForgeHooks.length; i++)
				afterGetCurrentPlayerStrVsBlockForgeHooks[i].afterGetCurrentPlayerStrVsBlockForge(paramBlock, paramBoolean, paramInt);

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenGetCurrentPlayerStrVsBlockForge(ServerPlayerBase overWriter)
	{
		if (overrideGetCurrentPlayerStrVsBlockForgeHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetCurrentPlayerStrVsBlockForgeHooks.length; i++)
			if(overrideGetCurrentPlayerStrVsBlockForgeHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetCurrentPlayerStrVsBlockForgeHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetCurrentPlayerStrVsBlockForgeHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetCurrentPlayerStrVsBlockForgeHookTypes = new LinkedList<String>();
	private final static List<String> afterGetCurrentPlayerStrVsBlockForgeHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeGetCurrentPlayerStrVsBlockForgeHooks;
	private ServerPlayerBase[] overrideGetCurrentPlayerStrVsBlockForgeHooks;
	private ServerPlayerBase[] afterGetCurrentPlayerStrVsBlockForgeHooks;

	public boolean isGetCurrentPlayerStrVsBlockForgeModded;

	private static final Map<String, String[]> allBaseBeforeGetCurrentPlayerStrVsBlockForgeSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetCurrentPlayerStrVsBlockForgeInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetCurrentPlayerStrVsBlockForgeSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetCurrentPlayerStrVsBlockForgeInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetCurrentPlayerStrVsBlockForgeSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetCurrentPlayerStrVsBlockForgeInferiors = new Hashtable<String, String[]>(0);

	public static double getDistanceSq(IServerPlayerAPI target, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		double _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isGetDistanceSqModded)
			_result = serverPlayerAPI.getDistanceSq(paramDouble1, paramDouble2, paramDouble3);
		else
			_result = target.localGetDistanceSq(paramDouble1, paramDouble2, paramDouble3);
		return _result;
	}

	private double getDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		if(beforeGetDistanceSqHooks != null)
			for(int i = beforeGetDistanceSqHooks.length - 1; i >= 0 ; i--)
				beforeGetDistanceSqHooks[i].beforeGetDistanceSq(paramDouble1, paramDouble2, paramDouble3);

		double _result;
		if(overrideGetDistanceSqHooks != null)
			_result = overrideGetDistanceSqHooks[overrideGetDistanceSqHooks.length - 1].getDistanceSq(paramDouble1, paramDouble2, paramDouble3);
		else
			_result = player.localGetDistanceSq(paramDouble1, paramDouble2, paramDouble3);

		if(afterGetDistanceSqHooks != null)
			for(int i = 0; i < afterGetDistanceSqHooks.length; i++)
				afterGetDistanceSqHooks[i].afterGetDistanceSq(paramDouble1, paramDouble2, paramDouble3);

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenGetDistanceSq(ServerPlayerBase overWriter)
	{
		if (overrideGetDistanceSqHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetDistanceSqHooks.length; i++)
			if(overrideGetDistanceSqHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetDistanceSqHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetDistanceSqHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetDistanceSqHookTypes = new LinkedList<String>();
	private final static List<String> afterGetDistanceSqHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeGetDistanceSqHooks;
	private ServerPlayerBase[] overrideGetDistanceSqHooks;
	private ServerPlayerBase[] afterGetDistanceSqHooks;

	public boolean isGetDistanceSqModded;

	private static final Map<String, String[]> allBaseBeforeGetDistanceSqSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetDistanceSqInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetDistanceSqSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetDistanceSqInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetDistanceSqSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetDistanceSqInferiors = new Hashtable<String, String[]>(0);

	public static float getBrightness(IServerPlayerAPI target, float paramFloat)
	{
		float _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isGetBrightnessModded)
			_result = serverPlayerAPI.getBrightness(paramFloat);
		else
			_result = target.localGetBrightness(paramFloat);
		return _result;
	}

	private float getBrightness(float paramFloat)
	{
		if(beforeGetBrightnessHooks != null)
			for(int i = beforeGetBrightnessHooks.length - 1; i >= 0 ; i--)
				beforeGetBrightnessHooks[i].beforeGetBrightness(paramFloat);

		float _result;
		if(overrideGetBrightnessHooks != null)
			_result = overrideGetBrightnessHooks[overrideGetBrightnessHooks.length - 1].getBrightness(paramFloat);
		else
			_result = player.localGetBrightness(paramFloat);

		if(afterGetBrightnessHooks != null)
			for(int i = 0; i < afterGetBrightnessHooks.length; i++)
				afterGetBrightnessHooks[i].afterGetBrightness(paramFloat);

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenGetBrightness(ServerPlayerBase overWriter)
	{
		if (overrideGetBrightnessHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetBrightnessHooks.length; i++)
			if(overrideGetBrightnessHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetBrightnessHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetBrightnessHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetBrightnessHookTypes = new LinkedList<String>();
	private final static List<String> afterGetBrightnessHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeGetBrightnessHooks;
	private ServerPlayerBase[] overrideGetBrightnessHooks;
	private ServerPlayerBase[] afterGetBrightnessHooks;

	public boolean isGetBrightnessModded;

	private static final Map<String, String[]> allBaseBeforeGetBrightnessSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetBrightnessInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetBrightnessSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetBrightnessInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetBrightnessSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetBrightnessInferiors = new Hashtable<String, String[]>(0);

	public static float getEyeHeight(IServerPlayerAPI target)
	{
		float _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isGetEyeHeightModded)
			_result = serverPlayerAPI.getEyeHeight();
		else
			_result = target.localGetEyeHeight();
		return _result;
	}

	private float getEyeHeight()
	{
		if(beforeGetEyeHeightHooks != null)
			for(int i = beforeGetEyeHeightHooks.length - 1; i >= 0 ; i--)
				beforeGetEyeHeightHooks[i].beforeGetEyeHeight();

		float _result;
		if(overrideGetEyeHeightHooks != null)
			_result = overrideGetEyeHeightHooks[overrideGetEyeHeightHooks.length - 1].getEyeHeight();
		else
			_result = player.localGetEyeHeight();

		if(afterGetEyeHeightHooks != null)
			for(int i = 0; i < afterGetEyeHeightHooks.length; i++)
				afterGetEyeHeightHooks[i].afterGetEyeHeight();

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenGetEyeHeight(ServerPlayerBase overWriter)
	{
		if (overrideGetEyeHeightHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetEyeHeightHooks.length; i++)
			if(overrideGetEyeHeightHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetEyeHeightHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetEyeHeightHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetEyeHeightHookTypes = new LinkedList<String>();
	private final static List<String> afterGetEyeHeightHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeGetEyeHeightHooks;
	private ServerPlayerBase[] overrideGetEyeHeightHooks;
	private ServerPlayerBase[] afterGetEyeHeightHooks;

	public boolean isGetEyeHeightModded;

	private static final Map<String, String[]> allBaseBeforeGetEyeHeightSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetEyeHeightInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetEyeHeightSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetEyeHeightInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetEyeHeightSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetEyeHeightInferiors = new Hashtable<String, String[]>(0);

	public static void heal(IServerPlayerAPI target, float paramFloat)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isHealModded)
			serverPlayerAPI.heal(paramFloat);
		else
			target.localHeal(paramFloat);
	}

	private void heal(float paramFloat)
	{
		if(beforeHealHooks != null)
			for(int i = beforeHealHooks.length - 1; i >= 0 ; i--)
				beforeHealHooks[i].beforeHeal(paramFloat);

		if(overrideHealHooks != null)
			overrideHealHooks[overrideHealHooks.length - 1].heal(paramFloat);
		else
			player.localHeal(paramFloat);

		if(afterHealHooks != null)
			for(int i = 0; i < afterHealHooks.length; i++)
				afterHealHooks[i].afterHeal(paramFloat);

	}

	protected ServerPlayerBase GetOverwrittenHeal(ServerPlayerBase overWriter)
	{
		if (overrideHealHooks == null)
			return overWriter;

		for(int i = 0; i < overrideHealHooks.length; i++)
			if(overrideHealHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideHealHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeHealHookTypes = new LinkedList<String>();
	private final static List<String> overrideHealHookTypes = new LinkedList<String>();
	private final static List<String> afterHealHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeHealHooks;
	private ServerPlayerBase[] overrideHealHooks;
	private ServerPlayerBase[] afterHealHooks;

	public boolean isHealModded;

	private static final Map<String, String[]> allBaseBeforeHealSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeHealInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideHealSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideHealInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterHealSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterHealInferiors = new Hashtable<String, String[]>(0);

	public static boolean isEntityInsideOpaqueBlock(IServerPlayerAPI target)
	{
		boolean _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isIsEntityInsideOpaqueBlockModded)
			_result = serverPlayerAPI.isEntityInsideOpaqueBlock();
		else
			_result = target.localIsEntityInsideOpaqueBlock();
		return _result;
	}

	private boolean isEntityInsideOpaqueBlock()
	{
		if(beforeIsEntityInsideOpaqueBlockHooks != null)
			for(int i = beforeIsEntityInsideOpaqueBlockHooks.length - 1; i >= 0 ; i--)
				beforeIsEntityInsideOpaqueBlockHooks[i].beforeIsEntityInsideOpaqueBlock();

		boolean _result;
		if(overrideIsEntityInsideOpaqueBlockHooks != null)
			_result = overrideIsEntityInsideOpaqueBlockHooks[overrideIsEntityInsideOpaqueBlockHooks.length - 1].isEntityInsideOpaqueBlock();
		else
			_result = player.localIsEntityInsideOpaqueBlock();

		if(afterIsEntityInsideOpaqueBlockHooks != null)
			for(int i = 0; i < afterIsEntityInsideOpaqueBlockHooks.length; i++)
				afterIsEntityInsideOpaqueBlockHooks[i].afterIsEntityInsideOpaqueBlock();

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenIsEntityInsideOpaqueBlock(ServerPlayerBase overWriter)
	{
		if (overrideIsEntityInsideOpaqueBlockHooks == null)
			return overWriter;

		for(int i = 0; i < overrideIsEntityInsideOpaqueBlockHooks.length; i++)
			if(overrideIsEntityInsideOpaqueBlockHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideIsEntityInsideOpaqueBlockHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeIsEntityInsideOpaqueBlockHookTypes = new LinkedList<String>();
	private final static List<String> overrideIsEntityInsideOpaqueBlockHookTypes = new LinkedList<String>();
	private final static List<String> afterIsEntityInsideOpaqueBlockHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeIsEntityInsideOpaqueBlockHooks;
	private ServerPlayerBase[] overrideIsEntityInsideOpaqueBlockHooks;
	private ServerPlayerBase[] afterIsEntityInsideOpaqueBlockHooks;

	public boolean isIsEntityInsideOpaqueBlockModded;

	private static final Map<String, String[]> allBaseBeforeIsEntityInsideOpaqueBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsEntityInsideOpaqueBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsEntityInsideOpaqueBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsEntityInsideOpaqueBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsEntityInsideOpaqueBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsEntityInsideOpaqueBlockInferiors = new Hashtable<String, String[]>(0);

	public static boolean isInWater(IServerPlayerAPI target)
	{
		boolean _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isIsInWaterModded)
			_result = serverPlayerAPI.isInWater();
		else
			_result = target.localIsInWater();
		return _result;
	}

	private boolean isInWater()
	{
		if(beforeIsInWaterHooks != null)
			for(int i = beforeIsInWaterHooks.length - 1; i >= 0 ; i--)
				beforeIsInWaterHooks[i].beforeIsInWater();

		boolean _result;
		if(overrideIsInWaterHooks != null)
			_result = overrideIsInWaterHooks[overrideIsInWaterHooks.length - 1].isInWater();
		else
			_result = player.localIsInWater();

		if(afterIsInWaterHooks != null)
			for(int i = 0; i < afterIsInWaterHooks.length; i++)
				afterIsInWaterHooks[i].afterIsInWater();

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenIsInWater(ServerPlayerBase overWriter)
	{
		if (overrideIsInWaterHooks == null)
			return overWriter;

		for(int i = 0; i < overrideIsInWaterHooks.length; i++)
			if(overrideIsInWaterHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideIsInWaterHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeIsInWaterHookTypes = new LinkedList<String>();
	private final static List<String> overrideIsInWaterHookTypes = new LinkedList<String>();
	private final static List<String> afterIsInWaterHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeIsInWaterHooks;
	private ServerPlayerBase[] overrideIsInWaterHooks;
	private ServerPlayerBase[] afterIsInWaterHooks;

	public boolean isIsInWaterModded;

	private static final Map<String, String[]> allBaseBeforeIsInWaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsInWaterInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsInWaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsInWaterInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsInWaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsInWaterInferiors = new Hashtable<String, String[]>(0);

	public static boolean isInsideOfMaterial(IServerPlayerAPI target, net.minecraft.block.material.Material paramMaterial)
	{
		boolean _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isIsInsideOfMaterialModded)
			_result = serverPlayerAPI.isInsideOfMaterial(paramMaterial);
		else
			_result = target.localIsInsideOfMaterial(paramMaterial);
		return _result;
	}

	private boolean isInsideOfMaterial(net.minecraft.block.material.Material paramMaterial)
	{
		if(beforeIsInsideOfMaterialHooks != null)
			for(int i = beforeIsInsideOfMaterialHooks.length - 1; i >= 0 ; i--)
				beforeIsInsideOfMaterialHooks[i].beforeIsInsideOfMaterial(paramMaterial);

		boolean _result;
		if(overrideIsInsideOfMaterialHooks != null)
			_result = overrideIsInsideOfMaterialHooks[overrideIsInsideOfMaterialHooks.length - 1].isInsideOfMaterial(paramMaterial);
		else
			_result = player.localIsInsideOfMaterial(paramMaterial);

		if(afterIsInsideOfMaterialHooks != null)
			for(int i = 0; i < afterIsInsideOfMaterialHooks.length; i++)
				afterIsInsideOfMaterialHooks[i].afterIsInsideOfMaterial(paramMaterial);

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenIsInsideOfMaterial(ServerPlayerBase overWriter)
	{
		if (overrideIsInsideOfMaterialHooks == null)
			return overWriter;

		for(int i = 0; i < overrideIsInsideOfMaterialHooks.length; i++)
			if(overrideIsInsideOfMaterialHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideIsInsideOfMaterialHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeIsInsideOfMaterialHookTypes = new LinkedList<String>();
	private final static List<String> overrideIsInsideOfMaterialHookTypes = new LinkedList<String>();
	private final static List<String> afterIsInsideOfMaterialHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeIsInsideOfMaterialHooks;
	private ServerPlayerBase[] overrideIsInsideOfMaterialHooks;
	private ServerPlayerBase[] afterIsInsideOfMaterialHooks;

	public boolean isIsInsideOfMaterialModded;

	private static final Map<String, String[]> allBaseBeforeIsInsideOfMaterialSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsInsideOfMaterialInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsInsideOfMaterialSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsInsideOfMaterialInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsInsideOfMaterialSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsInsideOfMaterialInferiors = new Hashtable<String, String[]>(0);

	public static boolean isOnLadder(IServerPlayerAPI target)
	{
		boolean _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isIsOnLadderModded)
			_result = serverPlayerAPI.isOnLadder();
		else
			_result = target.localIsOnLadder();
		return _result;
	}

	private boolean isOnLadder()
	{
		if(beforeIsOnLadderHooks != null)
			for(int i = beforeIsOnLadderHooks.length - 1; i >= 0 ; i--)
				beforeIsOnLadderHooks[i].beforeIsOnLadder();

		boolean _result;
		if(overrideIsOnLadderHooks != null)
			_result = overrideIsOnLadderHooks[overrideIsOnLadderHooks.length - 1].isOnLadder();
		else
			_result = player.localIsOnLadder();

		if(afterIsOnLadderHooks != null)
			for(int i = 0; i < afterIsOnLadderHooks.length; i++)
				afterIsOnLadderHooks[i].afterIsOnLadder();

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenIsOnLadder(ServerPlayerBase overWriter)
	{
		if (overrideIsOnLadderHooks == null)
			return overWriter;

		for(int i = 0; i < overrideIsOnLadderHooks.length; i++)
			if(overrideIsOnLadderHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideIsOnLadderHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeIsOnLadderHookTypes = new LinkedList<String>();
	private final static List<String> overrideIsOnLadderHookTypes = new LinkedList<String>();
	private final static List<String> afterIsOnLadderHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeIsOnLadderHooks;
	private ServerPlayerBase[] overrideIsOnLadderHooks;
	private ServerPlayerBase[] afterIsOnLadderHooks;

	public boolean isIsOnLadderModded;

	private static final Map<String, String[]> allBaseBeforeIsOnLadderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsOnLadderInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsOnLadderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsOnLadderInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsOnLadderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsOnLadderInferiors = new Hashtable<String, String[]>(0);

	public static boolean isPlayerSleeping(IServerPlayerAPI target)
	{
		boolean _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isIsPlayerSleepingModded)
			_result = serverPlayerAPI.isPlayerSleeping();
		else
			_result = target.localIsPlayerSleeping();
		return _result;
	}

	private boolean isPlayerSleeping()
	{
		if(beforeIsPlayerSleepingHooks != null)
			for(int i = beforeIsPlayerSleepingHooks.length - 1; i >= 0 ; i--)
				beforeIsPlayerSleepingHooks[i].beforeIsPlayerSleeping();

		boolean _result;
		if(overrideIsPlayerSleepingHooks != null)
			_result = overrideIsPlayerSleepingHooks[overrideIsPlayerSleepingHooks.length - 1].isPlayerSleeping();
		else
			_result = player.localIsPlayerSleeping();

		if(afterIsPlayerSleepingHooks != null)
			for(int i = 0; i < afterIsPlayerSleepingHooks.length; i++)
				afterIsPlayerSleepingHooks[i].afterIsPlayerSleeping();

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenIsPlayerSleeping(ServerPlayerBase overWriter)
	{
		if (overrideIsPlayerSleepingHooks == null)
			return overWriter;

		for(int i = 0; i < overrideIsPlayerSleepingHooks.length; i++)
			if(overrideIsPlayerSleepingHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideIsPlayerSleepingHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeIsPlayerSleepingHookTypes = new LinkedList<String>();
	private final static List<String> overrideIsPlayerSleepingHookTypes = new LinkedList<String>();
	private final static List<String> afterIsPlayerSleepingHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeIsPlayerSleepingHooks;
	private ServerPlayerBase[] overrideIsPlayerSleepingHooks;
	private ServerPlayerBase[] afterIsPlayerSleepingHooks;

	public boolean isIsPlayerSleepingModded;

	private static final Map<String, String[]> allBaseBeforeIsPlayerSleepingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsPlayerSleepingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsPlayerSleepingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsPlayerSleepingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsPlayerSleepingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsPlayerSleepingInferiors = new Hashtable<String, String[]>(0);

	public static boolean isSneaking(IServerPlayerAPI target)
	{
		boolean _result;
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isIsSneakingModded)
			_result = serverPlayerAPI.isSneaking();
		else
			_result = target.localIsSneaking();
		return _result;
	}

	private boolean isSneaking()
	{
		if(beforeIsSneakingHooks != null)
			for(int i = beforeIsSneakingHooks.length - 1; i >= 0 ; i--)
				beforeIsSneakingHooks[i].beforeIsSneaking();

		boolean _result;
		if(overrideIsSneakingHooks != null)
			_result = overrideIsSneakingHooks[overrideIsSneakingHooks.length - 1].isSneaking();
		else
			_result = player.localIsSneaking();

		if(afterIsSneakingHooks != null)
			for(int i = 0; i < afterIsSneakingHooks.length; i++)
				afterIsSneakingHooks[i].afterIsSneaking();

		return _result;
	}

	protected ServerPlayerBase GetOverwrittenIsSneaking(ServerPlayerBase overWriter)
	{
		if (overrideIsSneakingHooks == null)
			return overWriter;

		for(int i = 0; i < overrideIsSneakingHooks.length; i++)
			if(overrideIsSneakingHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideIsSneakingHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeIsSneakingHookTypes = new LinkedList<String>();
	private final static List<String> overrideIsSneakingHookTypes = new LinkedList<String>();
	private final static List<String> afterIsSneakingHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeIsSneakingHooks;
	private ServerPlayerBase[] overrideIsSneakingHooks;
	private ServerPlayerBase[] afterIsSneakingHooks;

	public boolean isIsSneakingModded;

	private static final Map<String, String[]> allBaseBeforeIsSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsSneakingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsSneakingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsSneakingInferiors = new Hashtable<String, String[]>(0);

	public static void jump(IServerPlayerAPI target)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isJumpModded)
			serverPlayerAPI.jump();
		else
			target.localJump();
	}

	private void jump()
	{
		if(beforeJumpHooks != null)
			for(int i = beforeJumpHooks.length - 1; i >= 0 ; i--)
				beforeJumpHooks[i].beforeJump();

		if(overrideJumpHooks != null)
			overrideJumpHooks[overrideJumpHooks.length - 1].jump();
		else
			player.localJump();

		if(afterJumpHooks != null)
			for(int i = 0; i < afterJumpHooks.length; i++)
				afterJumpHooks[i].afterJump();

	}

	protected ServerPlayerBase GetOverwrittenJump(ServerPlayerBase overWriter)
	{
		if (overrideJumpHooks == null)
			return overWriter;

		for(int i = 0; i < overrideJumpHooks.length; i++)
			if(overrideJumpHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideJumpHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeJumpHookTypes = new LinkedList<String>();
	private final static List<String> overrideJumpHookTypes = new LinkedList<String>();
	private final static List<String> afterJumpHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeJumpHooks;
	private ServerPlayerBase[] overrideJumpHooks;
	private ServerPlayerBase[] afterJumpHooks;

	public boolean isJumpModded;

	private static final Map<String, String[]> allBaseBeforeJumpSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeJumpInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideJumpSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideJumpInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterJumpSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterJumpInferiors = new Hashtable<String, String[]>(0);

	public static void knockBack(IServerPlayerAPI target, net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isKnockBackModded)
			serverPlayerAPI.knockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);
		else
			target.localKnockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);
	}

	private void knockBack(net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2)
	{
		if(beforeKnockBackHooks != null)
			for(int i = beforeKnockBackHooks.length - 1; i >= 0 ; i--)
				beforeKnockBackHooks[i].beforeKnockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);

		if(overrideKnockBackHooks != null)
			overrideKnockBackHooks[overrideKnockBackHooks.length - 1].knockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);
		else
			player.localKnockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);

		if(afterKnockBackHooks != null)
			for(int i = 0; i < afterKnockBackHooks.length; i++)
				afterKnockBackHooks[i].afterKnockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);

	}

	protected ServerPlayerBase GetOverwrittenKnockBack(ServerPlayerBase overWriter)
	{
		if (overrideKnockBackHooks == null)
			return overWriter;

		for(int i = 0; i < overrideKnockBackHooks.length; i++)
			if(overrideKnockBackHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideKnockBackHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeKnockBackHookTypes = new LinkedList<String>();
	private final static List<String> overrideKnockBackHookTypes = new LinkedList<String>();
	private final static List<String> afterKnockBackHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeKnockBackHooks;
	private ServerPlayerBase[] overrideKnockBackHooks;
	private ServerPlayerBase[] afterKnockBackHooks;

	public boolean isKnockBackModded;

	private static final Map<String, String[]> allBaseBeforeKnockBackSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeKnockBackInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideKnockBackSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideKnockBackInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterKnockBackSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterKnockBackInferiors = new Hashtable<String, String[]>(0);

	public static void mountEntity(IServerPlayerAPI target, net.minecraft.entity.Entity paramEntity)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isMountEntityModded)
			serverPlayerAPI.mountEntity(paramEntity);
		else
			target.localMountEntity(paramEntity);
	}

	private void mountEntity(net.minecraft.entity.Entity paramEntity)
	{
		if(beforeMountEntityHooks != null)
			for(int i = beforeMountEntityHooks.length - 1; i >= 0 ; i--)
				beforeMountEntityHooks[i].beforeMountEntity(paramEntity);

		if(overrideMountEntityHooks != null)
			overrideMountEntityHooks[overrideMountEntityHooks.length - 1].mountEntity(paramEntity);
		else
			player.localMountEntity(paramEntity);

		if(afterMountEntityHooks != null)
			for(int i = 0; i < afterMountEntityHooks.length; i++)
				afterMountEntityHooks[i].afterMountEntity(paramEntity);

	}

	protected ServerPlayerBase GetOverwrittenMountEntity(ServerPlayerBase overWriter)
	{
		if (overrideMountEntityHooks == null)
			return overWriter;

		for(int i = 0; i < overrideMountEntityHooks.length; i++)
			if(overrideMountEntityHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideMountEntityHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeMountEntityHookTypes = new LinkedList<String>();
	private final static List<String> overrideMountEntityHookTypes = new LinkedList<String>();
	private final static List<String> afterMountEntityHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeMountEntityHooks;
	private ServerPlayerBase[] overrideMountEntityHooks;
	private ServerPlayerBase[] afterMountEntityHooks;

	public boolean isMountEntityModded;

	private static final Map<String, String[]> allBaseBeforeMountEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeMountEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMountEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMountEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMountEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMountEntityInferiors = new Hashtable<String, String[]>(0);

	public static void moveEntity(IServerPlayerAPI target, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isMoveEntityModded)
			serverPlayerAPI.moveEntity(paramDouble1, paramDouble2, paramDouble3);
		else
			target.localMoveEntity(paramDouble1, paramDouble2, paramDouble3);
	}

	private void moveEntity(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		if(beforeMoveEntityHooks != null)
			for(int i = beforeMoveEntityHooks.length - 1; i >= 0 ; i--)
				beforeMoveEntityHooks[i].beforeMoveEntity(paramDouble1, paramDouble2, paramDouble3);

		if(overrideMoveEntityHooks != null)
			overrideMoveEntityHooks[overrideMoveEntityHooks.length - 1].moveEntity(paramDouble1, paramDouble2, paramDouble3);
		else
			player.localMoveEntity(paramDouble1, paramDouble2, paramDouble3);

		if(afterMoveEntityHooks != null)
			for(int i = 0; i < afterMoveEntityHooks.length; i++)
				afterMoveEntityHooks[i].afterMoveEntity(paramDouble1, paramDouble2, paramDouble3);

	}

	protected ServerPlayerBase GetOverwrittenMoveEntity(ServerPlayerBase overWriter)
	{
		if (overrideMoveEntityHooks == null)
			return overWriter;

		for(int i = 0; i < overrideMoveEntityHooks.length; i++)
			if(overrideMoveEntityHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideMoveEntityHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeMoveEntityHookTypes = new LinkedList<String>();
	private final static List<String> overrideMoveEntityHookTypes = new LinkedList<String>();
	private final static List<String> afterMoveEntityHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeMoveEntityHooks;
	private ServerPlayerBase[] overrideMoveEntityHooks;
	private ServerPlayerBase[] afterMoveEntityHooks;

	public boolean isMoveEntityModded;

	private static final Map<String, String[]> allBaseBeforeMoveEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeMoveEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveEntityInferiors = new Hashtable<String, String[]>(0);

	public static void moveEntityWithHeading(IServerPlayerAPI target, float paramFloat1, float paramFloat2)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isMoveEntityWithHeadingModded)
			serverPlayerAPI.moveEntityWithHeading(paramFloat1, paramFloat2);
		else
			target.localMoveEntityWithHeading(paramFloat1, paramFloat2);
	}

	private void moveEntityWithHeading(float paramFloat1, float paramFloat2)
	{
		if(beforeMoveEntityWithHeadingHooks != null)
			for(int i = beforeMoveEntityWithHeadingHooks.length - 1; i >= 0 ; i--)
				beforeMoveEntityWithHeadingHooks[i].beforeMoveEntityWithHeading(paramFloat1, paramFloat2);

		if(overrideMoveEntityWithHeadingHooks != null)
			overrideMoveEntityWithHeadingHooks[overrideMoveEntityWithHeadingHooks.length - 1].moveEntityWithHeading(paramFloat1, paramFloat2);
		else
			player.localMoveEntityWithHeading(paramFloat1, paramFloat2);

		if(afterMoveEntityWithHeadingHooks != null)
			for(int i = 0; i < afterMoveEntityWithHeadingHooks.length; i++)
				afterMoveEntityWithHeadingHooks[i].afterMoveEntityWithHeading(paramFloat1, paramFloat2);

	}

	protected ServerPlayerBase GetOverwrittenMoveEntityWithHeading(ServerPlayerBase overWriter)
	{
		if (overrideMoveEntityWithHeadingHooks == null)
			return overWriter;

		for(int i = 0; i < overrideMoveEntityWithHeadingHooks.length; i++)
			if(overrideMoveEntityWithHeadingHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideMoveEntityWithHeadingHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeMoveEntityWithHeadingHookTypes = new LinkedList<String>();
	private final static List<String> overrideMoveEntityWithHeadingHookTypes = new LinkedList<String>();
	private final static List<String> afterMoveEntityWithHeadingHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeMoveEntityWithHeadingHooks;
	private ServerPlayerBase[] overrideMoveEntityWithHeadingHooks;
	private ServerPlayerBase[] afterMoveEntityWithHeadingHooks;

	public boolean isMoveEntityWithHeadingModded;

	private static final Map<String, String[]> allBaseBeforeMoveEntityWithHeadingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeMoveEntityWithHeadingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveEntityWithHeadingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveEntityWithHeadingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveEntityWithHeadingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveEntityWithHeadingInferiors = new Hashtable<String, String[]>(0);

	public static void moveFlying(IServerPlayerAPI target, float paramFloat1, float paramFloat2, float paramFloat3)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isMoveFlyingModded)
			serverPlayerAPI.moveFlying(paramFloat1, paramFloat2, paramFloat3);
		else
			target.localMoveFlying(paramFloat1, paramFloat2, paramFloat3);
	}

	private void moveFlying(float paramFloat1, float paramFloat2, float paramFloat3)
	{
		if(beforeMoveFlyingHooks != null)
			for(int i = beforeMoveFlyingHooks.length - 1; i >= 0 ; i--)
				beforeMoveFlyingHooks[i].beforeMoveFlying(paramFloat1, paramFloat2, paramFloat3);

		if(overrideMoveFlyingHooks != null)
			overrideMoveFlyingHooks[overrideMoveFlyingHooks.length - 1].moveFlying(paramFloat1, paramFloat2, paramFloat3);
		else
			player.localMoveFlying(paramFloat1, paramFloat2, paramFloat3);

		if(afterMoveFlyingHooks != null)
			for(int i = 0; i < afterMoveFlyingHooks.length; i++)
				afterMoveFlyingHooks[i].afterMoveFlying(paramFloat1, paramFloat2, paramFloat3);

	}

	protected ServerPlayerBase GetOverwrittenMoveFlying(ServerPlayerBase overWriter)
	{
		if (overrideMoveFlyingHooks == null)
			return overWriter;

		for(int i = 0; i < overrideMoveFlyingHooks.length; i++)
			if(overrideMoveFlyingHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideMoveFlyingHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeMoveFlyingHookTypes = new LinkedList<String>();
	private final static List<String> overrideMoveFlyingHookTypes = new LinkedList<String>();
	private final static List<String> afterMoveFlyingHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeMoveFlyingHooks;
	private ServerPlayerBase[] overrideMoveFlyingHooks;
	private ServerPlayerBase[] afterMoveFlyingHooks;

	public boolean isMoveFlyingModded;

	private static final Map<String, String[]> allBaseBeforeMoveFlyingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeMoveFlyingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveFlyingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveFlyingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveFlyingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveFlyingInferiors = new Hashtable<String, String[]>(0);

	public static void onDeath(IServerPlayerAPI target, net.minecraft.util.DamageSource paramDamageSource)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isOnDeathModded)
			serverPlayerAPI.onDeath(paramDamageSource);
		else
			target.localOnDeath(paramDamageSource);
	}

	private void onDeath(net.minecraft.util.DamageSource paramDamageSource)
	{
		if(beforeOnDeathHooks != null)
			for(int i = beforeOnDeathHooks.length - 1; i >= 0 ; i--)
				beforeOnDeathHooks[i].beforeOnDeath(paramDamageSource);

		if(overrideOnDeathHooks != null)
			overrideOnDeathHooks[overrideOnDeathHooks.length - 1].onDeath(paramDamageSource);
		else
			player.localOnDeath(paramDamageSource);

		if(afterOnDeathHooks != null)
			for(int i = 0; i < afterOnDeathHooks.length; i++)
				afterOnDeathHooks[i].afterOnDeath(paramDamageSource);

	}

	protected ServerPlayerBase GetOverwrittenOnDeath(ServerPlayerBase overWriter)
	{
		if (overrideOnDeathHooks == null)
			return overWriter;

		for(int i = 0; i < overrideOnDeathHooks.length; i++)
			if(overrideOnDeathHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideOnDeathHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeOnDeathHookTypes = new LinkedList<String>();
	private final static List<String> overrideOnDeathHookTypes = new LinkedList<String>();
	private final static List<String> afterOnDeathHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeOnDeathHooks;
	private ServerPlayerBase[] overrideOnDeathHooks;
	private ServerPlayerBase[] afterOnDeathHooks;

	public boolean isOnDeathModded;

	private static final Map<String, String[]> allBaseBeforeOnDeathSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeOnDeathInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnDeathSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnDeathInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnDeathSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnDeathInferiors = new Hashtable<String, String[]>(0);

	public static void onLivingUpdate(IServerPlayerAPI target)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isOnLivingUpdateModded)
			serverPlayerAPI.onLivingUpdate();
		else
			target.localOnLivingUpdate();
	}

	private void onLivingUpdate()
	{
		if(beforeOnLivingUpdateHooks != null)
			for(int i = beforeOnLivingUpdateHooks.length - 1; i >= 0 ; i--)
				beforeOnLivingUpdateHooks[i].beforeOnLivingUpdate();

		if(overrideOnLivingUpdateHooks != null)
			overrideOnLivingUpdateHooks[overrideOnLivingUpdateHooks.length - 1].onLivingUpdate();
		else
			player.localOnLivingUpdate();

		if(afterOnLivingUpdateHooks != null)
			for(int i = 0; i < afterOnLivingUpdateHooks.length; i++)
				afterOnLivingUpdateHooks[i].afterOnLivingUpdate();

	}

	protected ServerPlayerBase GetOverwrittenOnLivingUpdate(ServerPlayerBase overWriter)
	{
		if (overrideOnLivingUpdateHooks == null)
			return overWriter;

		for(int i = 0; i < overrideOnLivingUpdateHooks.length; i++)
			if(overrideOnLivingUpdateHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideOnLivingUpdateHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeOnLivingUpdateHookTypes = new LinkedList<String>();
	private final static List<String> overrideOnLivingUpdateHookTypes = new LinkedList<String>();
	private final static List<String> afterOnLivingUpdateHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeOnLivingUpdateHooks;
	private ServerPlayerBase[] overrideOnLivingUpdateHooks;
	private ServerPlayerBase[] afterOnLivingUpdateHooks;

	public boolean isOnLivingUpdateModded;

	private static final Map<String, String[]> allBaseBeforeOnLivingUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeOnLivingUpdateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnLivingUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnLivingUpdateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnLivingUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnLivingUpdateInferiors = new Hashtable<String, String[]>(0);

	public static void onKillEntity(IServerPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isOnKillEntityModded)
			serverPlayerAPI.onKillEntity(paramEntityLivingBase);
		else
			target.localOnKillEntity(paramEntityLivingBase);
	}

	private void onKillEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
		if(beforeOnKillEntityHooks != null)
			for(int i = beforeOnKillEntityHooks.length - 1; i >= 0 ; i--)
				beforeOnKillEntityHooks[i].beforeOnKillEntity(paramEntityLivingBase);

		if(overrideOnKillEntityHooks != null)
			overrideOnKillEntityHooks[overrideOnKillEntityHooks.length - 1].onKillEntity(paramEntityLivingBase);
		else
			player.localOnKillEntity(paramEntityLivingBase);

		if(afterOnKillEntityHooks != null)
			for(int i = 0; i < afterOnKillEntityHooks.length; i++)
				afterOnKillEntityHooks[i].afterOnKillEntity(paramEntityLivingBase);

	}

	protected ServerPlayerBase GetOverwrittenOnKillEntity(ServerPlayerBase overWriter)
	{
		if (overrideOnKillEntityHooks == null)
			return overWriter;

		for(int i = 0; i < overrideOnKillEntityHooks.length; i++)
			if(overrideOnKillEntityHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideOnKillEntityHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeOnKillEntityHookTypes = new LinkedList<String>();
	private final static List<String> overrideOnKillEntityHookTypes = new LinkedList<String>();
	private final static List<String> afterOnKillEntityHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeOnKillEntityHooks;
	private ServerPlayerBase[] overrideOnKillEntityHooks;
	private ServerPlayerBase[] afterOnKillEntityHooks;

	public boolean isOnKillEntityModded;

	private static final Map<String, String[]> allBaseBeforeOnKillEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeOnKillEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnKillEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnKillEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnKillEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnKillEntityInferiors = new Hashtable<String, String[]>(0);

	public static void onStruckByLightning(IServerPlayerAPI target, net.minecraft.entity.effect.EntityLightningBolt paramEntityLightningBolt)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isOnStruckByLightningModded)
			serverPlayerAPI.onStruckByLightning(paramEntityLightningBolt);
		else
			target.localOnStruckByLightning(paramEntityLightningBolt);
	}

	private void onStruckByLightning(net.minecraft.entity.effect.EntityLightningBolt paramEntityLightningBolt)
	{
		if(beforeOnStruckByLightningHooks != null)
			for(int i = beforeOnStruckByLightningHooks.length - 1; i >= 0 ; i--)
				beforeOnStruckByLightningHooks[i].beforeOnStruckByLightning(paramEntityLightningBolt);

		if(overrideOnStruckByLightningHooks != null)
			overrideOnStruckByLightningHooks[overrideOnStruckByLightningHooks.length - 1].onStruckByLightning(paramEntityLightningBolt);
		else
			player.localOnStruckByLightning(paramEntityLightningBolt);

		if(afterOnStruckByLightningHooks != null)
			for(int i = 0; i < afterOnStruckByLightningHooks.length; i++)
				afterOnStruckByLightningHooks[i].afterOnStruckByLightning(paramEntityLightningBolt);

	}

	protected ServerPlayerBase GetOverwrittenOnStruckByLightning(ServerPlayerBase overWriter)
	{
		if (overrideOnStruckByLightningHooks == null)
			return overWriter;

		for(int i = 0; i < overrideOnStruckByLightningHooks.length; i++)
			if(overrideOnStruckByLightningHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideOnStruckByLightningHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeOnStruckByLightningHookTypes = new LinkedList<String>();
	private final static List<String> overrideOnStruckByLightningHookTypes = new LinkedList<String>();
	private final static List<String> afterOnStruckByLightningHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeOnStruckByLightningHooks;
	private ServerPlayerBase[] overrideOnStruckByLightningHooks;
	private ServerPlayerBase[] afterOnStruckByLightningHooks;

	public boolean isOnStruckByLightningModded;

	private static final Map<String, String[]> allBaseBeforeOnStruckByLightningSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeOnStruckByLightningInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnStruckByLightningSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnStruckByLightningInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnStruckByLightningSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnStruckByLightningInferiors = new Hashtable<String, String[]>(0);

	public static void onUpdate(IServerPlayerAPI target)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isOnUpdateModded)
			serverPlayerAPI.onUpdate();
		else
			target.localOnUpdate();
	}

	private void onUpdate()
	{
		if(beforeOnUpdateHooks != null)
			for(int i = beforeOnUpdateHooks.length - 1; i >= 0 ; i--)
				beforeOnUpdateHooks[i].beforeOnUpdate();

		if(overrideOnUpdateHooks != null)
			overrideOnUpdateHooks[overrideOnUpdateHooks.length - 1].onUpdate();
		else
			player.localOnUpdate();

		if(afterOnUpdateHooks != null)
			for(int i = 0; i < afterOnUpdateHooks.length; i++)
				afterOnUpdateHooks[i].afterOnUpdate();

	}

	protected ServerPlayerBase GetOverwrittenOnUpdate(ServerPlayerBase overWriter)
	{
		if (overrideOnUpdateHooks == null)
			return overWriter;

		for(int i = 0; i < overrideOnUpdateHooks.length; i++)
			if(overrideOnUpdateHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideOnUpdateHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeOnUpdateHookTypes = new LinkedList<String>();
	private final static List<String> overrideOnUpdateHookTypes = new LinkedList<String>();
	private final static List<String> afterOnUpdateHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeOnUpdateHooks;
	private ServerPlayerBase[] overrideOnUpdateHooks;
	private ServerPlayerBase[] afterOnUpdateHooks;

	public boolean isOnUpdateModded;

	private static final Map<String, String[]> allBaseBeforeOnUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeOnUpdateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnUpdateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnUpdateInferiors = new Hashtable<String, String[]>(0);

	public static void onUpdateEntity(IServerPlayerAPI target)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isOnUpdateEntityModded)
			serverPlayerAPI.onUpdateEntity();
		else
			target.localOnUpdateEntity();
	}

	private void onUpdateEntity()
	{
		if(beforeOnUpdateEntityHooks != null)
			for(int i = beforeOnUpdateEntityHooks.length - 1; i >= 0 ; i--)
				beforeOnUpdateEntityHooks[i].beforeOnUpdateEntity();

		if(overrideOnUpdateEntityHooks != null)
			overrideOnUpdateEntityHooks[overrideOnUpdateEntityHooks.length - 1].onUpdateEntity();
		else
			player.localOnUpdateEntity();

		if(afterOnUpdateEntityHooks != null)
			for(int i = 0; i < afterOnUpdateEntityHooks.length; i++)
				afterOnUpdateEntityHooks[i].afterOnUpdateEntity();

	}

	protected ServerPlayerBase GetOverwrittenOnUpdateEntity(ServerPlayerBase overWriter)
	{
		if (overrideOnUpdateEntityHooks == null)
			return overWriter;

		for(int i = 0; i < overrideOnUpdateEntityHooks.length; i++)
			if(overrideOnUpdateEntityHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideOnUpdateEntityHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeOnUpdateEntityHookTypes = new LinkedList<String>();
	private final static List<String> overrideOnUpdateEntityHookTypes = new LinkedList<String>();
	private final static List<String> afterOnUpdateEntityHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeOnUpdateEntityHooks;
	private ServerPlayerBase[] overrideOnUpdateEntityHooks;
	private ServerPlayerBase[] afterOnUpdateEntityHooks;

	public boolean isOnUpdateEntityModded;

	private static final Map<String, String[]> allBaseBeforeOnUpdateEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeOnUpdateEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnUpdateEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnUpdateEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnUpdateEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnUpdateEntityInferiors = new Hashtable<String, String[]>(0);

	public static void readEntityFromNBT(IServerPlayerAPI target, net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isReadEntityFromNBTModded)
			serverPlayerAPI.readEntityFromNBT(paramNBTTagCompound);
		else
			target.localReadEntityFromNBT(paramNBTTagCompound);
	}

	private void readEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
		if(beforeReadEntityFromNBTHooks != null)
			for(int i = beforeReadEntityFromNBTHooks.length - 1; i >= 0 ; i--)
				beforeReadEntityFromNBTHooks[i].beforeReadEntityFromNBT(paramNBTTagCompound);

		if(overrideReadEntityFromNBTHooks != null)
			overrideReadEntityFromNBTHooks[overrideReadEntityFromNBTHooks.length - 1].readEntityFromNBT(paramNBTTagCompound);
		else
			player.localReadEntityFromNBT(paramNBTTagCompound);

		if(afterReadEntityFromNBTHooks != null)
			for(int i = 0; i < afterReadEntityFromNBTHooks.length; i++)
				afterReadEntityFromNBTHooks[i].afterReadEntityFromNBT(paramNBTTagCompound);

	}

	protected ServerPlayerBase GetOverwrittenReadEntityFromNBT(ServerPlayerBase overWriter)
	{
		if (overrideReadEntityFromNBTHooks == null)
			return overWriter;

		for(int i = 0; i < overrideReadEntityFromNBTHooks.length; i++)
			if(overrideReadEntityFromNBTHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideReadEntityFromNBTHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeReadEntityFromNBTHookTypes = new LinkedList<String>();
	private final static List<String> overrideReadEntityFromNBTHookTypes = new LinkedList<String>();
	private final static List<String> afterReadEntityFromNBTHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeReadEntityFromNBTHooks;
	private ServerPlayerBase[] overrideReadEntityFromNBTHooks;
	private ServerPlayerBase[] afterReadEntityFromNBTHooks;

	public boolean isReadEntityFromNBTModded;

	private static final Map<String, String[]> allBaseBeforeReadEntityFromNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeReadEntityFromNBTInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideReadEntityFromNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideReadEntityFromNBTInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterReadEntityFromNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterReadEntityFromNBTInferiors = new Hashtable<String, String[]>(0);

	public static void setDead(IServerPlayerAPI target)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isSetDeadModded)
			serverPlayerAPI.setDead();
		else
			target.localSetDead();
	}

	private void setDead()
	{
		if(beforeSetDeadHooks != null)
			for(int i = beforeSetDeadHooks.length - 1; i >= 0 ; i--)
				beforeSetDeadHooks[i].beforeSetDead();

		if(overrideSetDeadHooks != null)
			overrideSetDeadHooks[overrideSetDeadHooks.length - 1].setDead();
		else
			player.localSetDead();

		if(afterSetDeadHooks != null)
			for(int i = 0; i < afterSetDeadHooks.length; i++)
				afterSetDeadHooks[i].afterSetDead();

	}

	protected ServerPlayerBase GetOverwrittenSetDead(ServerPlayerBase overWriter)
	{
		if (overrideSetDeadHooks == null)
			return overWriter;

		for(int i = 0; i < overrideSetDeadHooks.length; i++)
			if(overrideSetDeadHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetDeadHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetDeadHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetDeadHookTypes = new LinkedList<String>();
	private final static List<String> afterSetDeadHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeSetDeadHooks;
	private ServerPlayerBase[] overrideSetDeadHooks;
	private ServerPlayerBase[] afterSetDeadHooks;

	public boolean isSetDeadModded;

	private static final Map<String, String[]> allBaseBeforeSetDeadSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetDeadInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetDeadSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetDeadInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetDeadSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetDeadInferiors = new Hashtable<String, String[]>(0);

	public static void setEntityActionState(IServerPlayerAPI target, float paramFloat1, float paramFloat2, boolean paramBoolean1, boolean paramBoolean2)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isSetEntityActionStateModded)
			serverPlayerAPI.setEntityActionState(paramFloat1, paramFloat2, paramBoolean1, paramBoolean2);
		else
			target.localSetEntityActionState(paramFloat1, paramFloat2, paramBoolean1, paramBoolean2);
	}

	private void setEntityActionState(float paramFloat1, float paramFloat2, boolean paramBoolean1, boolean paramBoolean2)
	{
		if(beforeSetEntityActionStateHooks != null)
			for(int i = beforeSetEntityActionStateHooks.length - 1; i >= 0 ; i--)
				beforeSetEntityActionStateHooks[i].beforeSetEntityActionState(paramFloat1, paramFloat2, paramBoolean1, paramBoolean2);

		if(overrideSetEntityActionStateHooks != null)
			overrideSetEntityActionStateHooks[overrideSetEntityActionStateHooks.length - 1].setEntityActionState(paramFloat1, paramFloat2, paramBoolean1, paramBoolean2);
		else
			player.localSetEntityActionState(paramFloat1, paramFloat2, paramBoolean1, paramBoolean2);

		if(afterSetEntityActionStateHooks != null)
			for(int i = 0; i < afterSetEntityActionStateHooks.length; i++)
				afterSetEntityActionStateHooks[i].afterSetEntityActionState(paramFloat1, paramFloat2, paramBoolean1, paramBoolean2);

	}

	protected ServerPlayerBase GetOverwrittenSetEntityActionState(ServerPlayerBase overWriter)
	{
		if (overrideSetEntityActionStateHooks == null)
			return overWriter;

		for(int i = 0; i < overrideSetEntityActionStateHooks.length; i++)
			if(overrideSetEntityActionStateHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetEntityActionStateHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetEntityActionStateHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetEntityActionStateHookTypes = new LinkedList<String>();
	private final static List<String> afterSetEntityActionStateHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeSetEntityActionStateHooks;
	private ServerPlayerBase[] overrideSetEntityActionStateHooks;
	private ServerPlayerBase[] afterSetEntityActionStateHooks;

	public boolean isSetEntityActionStateModded;

	private static final Map<String, String[]> allBaseBeforeSetEntityActionStateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetEntityActionStateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetEntityActionStateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetEntityActionStateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetEntityActionStateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetEntityActionStateInferiors = new Hashtable<String, String[]>(0);

	public static void setPosition(IServerPlayerAPI target, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isSetPositionModded)
			serverPlayerAPI.setPosition(paramDouble1, paramDouble2, paramDouble3);
		else
			target.localSetPosition(paramDouble1, paramDouble2, paramDouble3);
	}

	private void setPosition(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		if(beforeSetPositionHooks != null)
			for(int i = beforeSetPositionHooks.length - 1; i >= 0 ; i--)
				beforeSetPositionHooks[i].beforeSetPosition(paramDouble1, paramDouble2, paramDouble3);

		if(overrideSetPositionHooks != null)
			overrideSetPositionHooks[overrideSetPositionHooks.length - 1].setPosition(paramDouble1, paramDouble2, paramDouble3);
		else
			player.localSetPosition(paramDouble1, paramDouble2, paramDouble3);

		if(afterSetPositionHooks != null)
			for(int i = 0; i < afterSetPositionHooks.length; i++)
				afterSetPositionHooks[i].afterSetPosition(paramDouble1, paramDouble2, paramDouble3);

	}

	protected ServerPlayerBase GetOverwrittenSetPosition(ServerPlayerBase overWriter)
	{
		if (overrideSetPositionHooks == null)
			return overWriter;

		for(int i = 0; i < overrideSetPositionHooks.length; i++)
			if(overrideSetPositionHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetPositionHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetPositionHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetPositionHookTypes = new LinkedList<String>();
	private final static List<String> afterSetPositionHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeSetPositionHooks;
	private ServerPlayerBase[] overrideSetPositionHooks;
	private ServerPlayerBase[] afterSetPositionHooks;

	public boolean isSetPositionModded;

	private static final Map<String, String[]> allBaseBeforeSetPositionSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetPositionInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetPositionSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetPositionInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetPositionSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetPositionInferiors = new Hashtable<String, String[]>(0);

	public static void setSneaking(IServerPlayerAPI target, boolean paramBoolean)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isSetSneakingModded)
			serverPlayerAPI.setSneaking(paramBoolean);
		else
			target.localSetSneaking(paramBoolean);
	}

	private void setSneaking(boolean paramBoolean)
	{
		if(beforeSetSneakingHooks != null)
			for(int i = beforeSetSneakingHooks.length - 1; i >= 0 ; i--)
				beforeSetSneakingHooks[i].beforeSetSneaking(paramBoolean);

		if(overrideSetSneakingHooks != null)
			overrideSetSneakingHooks[overrideSetSneakingHooks.length - 1].setSneaking(paramBoolean);
		else
			player.localSetSneaking(paramBoolean);

		if(afterSetSneakingHooks != null)
			for(int i = 0; i < afterSetSneakingHooks.length; i++)
				afterSetSneakingHooks[i].afterSetSneaking(paramBoolean);

	}

	protected ServerPlayerBase GetOverwrittenSetSneaking(ServerPlayerBase overWriter)
	{
		if (overrideSetSneakingHooks == null)
			return overWriter;

		for(int i = 0; i < overrideSetSneakingHooks.length; i++)
			if(overrideSetSneakingHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetSneakingHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetSneakingHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetSneakingHookTypes = new LinkedList<String>();
	private final static List<String> afterSetSneakingHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeSetSneakingHooks;
	private ServerPlayerBase[] overrideSetSneakingHooks;
	private ServerPlayerBase[] afterSetSneakingHooks;

	public boolean isSetSneakingModded;

	private static final Map<String, String[]> allBaseBeforeSetSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetSneakingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetSneakingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetSneakingInferiors = new Hashtable<String, String[]>(0);

	public static void setSprinting(IServerPlayerAPI target, boolean paramBoolean)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isSetSprintingModded)
			serverPlayerAPI.setSprinting(paramBoolean);
		else
			target.localSetSprinting(paramBoolean);
	}

	private void setSprinting(boolean paramBoolean)
	{
		if(beforeSetSprintingHooks != null)
			for(int i = beforeSetSprintingHooks.length - 1; i >= 0 ; i--)
				beforeSetSprintingHooks[i].beforeSetSprinting(paramBoolean);

		if(overrideSetSprintingHooks != null)
			overrideSetSprintingHooks[overrideSetSprintingHooks.length - 1].setSprinting(paramBoolean);
		else
			player.localSetSprinting(paramBoolean);

		if(afterSetSprintingHooks != null)
			for(int i = 0; i < afterSetSprintingHooks.length; i++)
				afterSetSprintingHooks[i].afterSetSprinting(paramBoolean);

	}

	protected ServerPlayerBase GetOverwrittenSetSprinting(ServerPlayerBase overWriter)
	{
		if (overrideSetSprintingHooks == null)
			return overWriter;

		for(int i = 0; i < overrideSetSprintingHooks.length; i++)
			if(overrideSetSprintingHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetSprintingHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetSprintingHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetSprintingHookTypes = new LinkedList<String>();
	private final static List<String> afterSetSprintingHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeSetSprintingHooks;
	private ServerPlayerBase[] overrideSetSprintingHooks;
	private ServerPlayerBase[] afterSetSprintingHooks;

	public boolean isSetSprintingModded;

	private static final Map<String, String[]> allBaseBeforeSetSprintingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetSprintingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetSprintingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetSprintingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetSprintingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetSprintingInferiors = new Hashtable<String, String[]>(0);

	public static void swingItem(IServerPlayerAPI target)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isSwingItemModded)
			serverPlayerAPI.swingItem();
		else
			target.localSwingItem();
	}

	private void swingItem()
	{
		if(beforeSwingItemHooks != null)
			for(int i = beforeSwingItemHooks.length - 1; i >= 0 ; i--)
				beforeSwingItemHooks[i].beforeSwingItem();

		if(overrideSwingItemHooks != null)
			overrideSwingItemHooks[overrideSwingItemHooks.length - 1].swingItem();
		else
			player.localSwingItem();

		if(afterSwingItemHooks != null)
			for(int i = 0; i < afterSwingItemHooks.length; i++)
				afterSwingItemHooks[i].afterSwingItem();

	}

	protected ServerPlayerBase GetOverwrittenSwingItem(ServerPlayerBase overWriter)
	{
		if (overrideSwingItemHooks == null)
			return overWriter;

		for(int i = 0; i < overrideSwingItemHooks.length; i++)
			if(overrideSwingItemHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSwingItemHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSwingItemHookTypes = new LinkedList<String>();
	private final static List<String> overrideSwingItemHookTypes = new LinkedList<String>();
	private final static List<String> afterSwingItemHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeSwingItemHooks;
	private ServerPlayerBase[] overrideSwingItemHooks;
	private ServerPlayerBase[] afterSwingItemHooks;

	public boolean isSwingItemModded;

	private static final Map<String, String[]> allBaseBeforeSwingItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSwingItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSwingItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSwingItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSwingItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSwingItemInferiors = new Hashtable<String, String[]>(0);

	public static void updateEntityActionState(IServerPlayerAPI target)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isUpdateEntityActionStateModded)
			serverPlayerAPI.updateEntityActionState();
		else
			target.localUpdateEntityActionState();
	}

	private void updateEntityActionState()
	{
		if(beforeUpdateEntityActionStateHooks != null)
			for(int i = beforeUpdateEntityActionStateHooks.length - 1; i >= 0 ; i--)
				beforeUpdateEntityActionStateHooks[i].beforeUpdateEntityActionState();

		if(overrideUpdateEntityActionStateHooks != null)
			overrideUpdateEntityActionStateHooks[overrideUpdateEntityActionStateHooks.length - 1].updateEntityActionState();
		else
			player.localUpdateEntityActionState();

		if(afterUpdateEntityActionStateHooks != null)
			for(int i = 0; i < afterUpdateEntityActionStateHooks.length; i++)
				afterUpdateEntityActionStateHooks[i].afterUpdateEntityActionState();

	}

	protected ServerPlayerBase GetOverwrittenUpdateEntityActionState(ServerPlayerBase overWriter)
	{
		if (overrideUpdateEntityActionStateHooks == null)
			return overWriter;

		for(int i = 0; i < overrideUpdateEntityActionStateHooks.length; i++)
			if(overrideUpdateEntityActionStateHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideUpdateEntityActionStateHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeUpdateEntityActionStateHookTypes = new LinkedList<String>();
	private final static List<String> overrideUpdateEntityActionStateHookTypes = new LinkedList<String>();
	private final static List<String> afterUpdateEntityActionStateHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeUpdateEntityActionStateHooks;
	private ServerPlayerBase[] overrideUpdateEntityActionStateHooks;
	private ServerPlayerBase[] afterUpdateEntityActionStateHooks;

	public boolean isUpdateEntityActionStateModded;

	private static final Map<String, String[]> allBaseBeforeUpdateEntityActionStateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeUpdateEntityActionStateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdateEntityActionStateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdateEntityActionStateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdateEntityActionStateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdateEntityActionStateInferiors = new Hashtable<String, String[]>(0);

	public static void updatePotionEffects(IServerPlayerAPI target)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isUpdatePotionEffectsModded)
			serverPlayerAPI.updatePotionEffects();
		else
			target.localUpdatePotionEffects();
	}

	private void updatePotionEffects()
	{
		if(beforeUpdatePotionEffectsHooks != null)
			for(int i = beforeUpdatePotionEffectsHooks.length - 1; i >= 0 ; i--)
				beforeUpdatePotionEffectsHooks[i].beforeUpdatePotionEffects();

		if(overrideUpdatePotionEffectsHooks != null)
			overrideUpdatePotionEffectsHooks[overrideUpdatePotionEffectsHooks.length - 1].updatePotionEffects();
		else
			player.localUpdatePotionEffects();

		if(afterUpdatePotionEffectsHooks != null)
			for(int i = 0; i < afterUpdatePotionEffectsHooks.length; i++)
				afterUpdatePotionEffectsHooks[i].afterUpdatePotionEffects();

	}

	protected ServerPlayerBase GetOverwrittenUpdatePotionEffects(ServerPlayerBase overWriter)
	{
		if (overrideUpdatePotionEffectsHooks == null)
			return overWriter;

		for(int i = 0; i < overrideUpdatePotionEffectsHooks.length; i++)
			if(overrideUpdatePotionEffectsHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideUpdatePotionEffectsHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeUpdatePotionEffectsHookTypes = new LinkedList<String>();
	private final static List<String> overrideUpdatePotionEffectsHookTypes = new LinkedList<String>();
	private final static List<String> afterUpdatePotionEffectsHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeUpdatePotionEffectsHooks;
	private ServerPlayerBase[] overrideUpdatePotionEffectsHooks;
	private ServerPlayerBase[] afterUpdatePotionEffectsHooks;

	public boolean isUpdatePotionEffectsModded;

	private static final Map<String, String[]> allBaseBeforeUpdatePotionEffectsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeUpdatePotionEffectsInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdatePotionEffectsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdatePotionEffectsInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdatePotionEffectsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdatePotionEffectsInferiors = new Hashtable<String, String[]>(0);

	public static void updateRidden(IServerPlayerAPI target)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isUpdateRiddenModded)
			serverPlayerAPI.updateRidden();
		else
			target.localUpdateRidden();
	}

	private void updateRidden()
	{
		if(beforeUpdateRiddenHooks != null)
			for(int i = beforeUpdateRiddenHooks.length - 1; i >= 0 ; i--)
				beforeUpdateRiddenHooks[i].beforeUpdateRidden();

		if(overrideUpdateRiddenHooks != null)
			overrideUpdateRiddenHooks[overrideUpdateRiddenHooks.length - 1].updateRidden();
		else
			player.localUpdateRidden();

		if(afterUpdateRiddenHooks != null)
			for(int i = 0; i < afterUpdateRiddenHooks.length; i++)
				afterUpdateRiddenHooks[i].afterUpdateRidden();

	}

	protected ServerPlayerBase GetOverwrittenUpdateRidden(ServerPlayerBase overWriter)
	{
		if (overrideUpdateRiddenHooks == null)
			return overWriter;

		for(int i = 0; i < overrideUpdateRiddenHooks.length; i++)
			if(overrideUpdateRiddenHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideUpdateRiddenHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeUpdateRiddenHookTypes = new LinkedList<String>();
	private final static List<String> overrideUpdateRiddenHookTypes = new LinkedList<String>();
	private final static List<String> afterUpdateRiddenHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeUpdateRiddenHooks;
	private ServerPlayerBase[] overrideUpdateRiddenHooks;
	private ServerPlayerBase[] afterUpdateRiddenHooks;

	public boolean isUpdateRiddenModded;

	private static final Map<String, String[]> allBaseBeforeUpdateRiddenSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeUpdateRiddenInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdateRiddenSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdateRiddenInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdateRiddenSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdateRiddenInferiors = new Hashtable<String, String[]>(0);

	public static void wakeUpPlayer(IServerPlayerAPI target, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isWakeUpPlayerModded)
			serverPlayerAPI.wakeUpPlayer(paramBoolean1, paramBoolean2, paramBoolean3);
		else
			target.localWakeUpPlayer(paramBoolean1, paramBoolean2, paramBoolean3);
	}

	private void wakeUpPlayer(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
	{
		if(beforeWakeUpPlayerHooks != null)
			for(int i = beforeWakeUpPlayerHooks.length - 1; i >= 0 ; i--)
				beforeWakeUpPlayerHooks[i].beforeWakeUpPlayer(paramBoolean1, paramBoolean2, paramBoolean3);

		if(overrideWakeUpPlayerHooks != null)
			overrideWakeUpPlayerHooks[overrideWakeUpPlayerHooks.length - 1].wakeUpPlayer(paramBoolean1, paramBoolean2, paramBoolean3);
		else
			player.localWakeUpPlayer(paramBoolean1, paramBoolean2, paramBoolean3);

		if(afterWakeUpPlayerHooks != null)
			for(int i = 0; i < afterWakeUpPlayerHooks.length; i++)
				afterWakeUpPlayerHooks[i].afterWakeUpPlayer(paramBoolean1, paramBoolean2, paramBoolean3);

	}

	protected ServerPlayerBase GetOverwrittenWakeUpPlayer(ServerPlayerBase overWriter)
	{
		if (overrideWakeUpPlayerHooks == null)
			return overWriter;

		for(int i = 0; i < overrideWakeUpPlayerHooks.length; i++)
			if(overrideWakeUpPlayerHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideWakeUpPlayerHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeWakeUpPlayerHookTypes = new LinkedList<String>();
	private final static List<String> overrideWakeUpPlayerHookTypes = new LinkedList<String>();
	private final static List<String> afterWakeUpPlayerHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeWakeUpPlayerHooks;
	private ServerPlayerBase[] overrideWakeUpPlayerHooks;
	private ServerPlayerBase[] afterWakeUpPlayerHooks;

	public boolean isWakeUpPlayerModded;

	private static final Map<String, String[]> allBaseBeforeWakeUpPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeWakeUpPlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideWakeUpPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideWakeUpPlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterWakeUpPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterWakeUpPlayerInferiors = new Hashtable<String, String[]>(0);

	public static void writeEntityToNBT(IServerPlayerAPI target, net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
		ServerPlayerAPI serverPlayerAPI = target.getServerPlayerAPI();
		if(serverPlayerAPI != null && serverPlayerAPI.isWriteEntityToNBTModded)
			serverPlayerAPI.writeEntityToNBT(paramNBTTagCompound);
		else
			target.localWriteEntityToNBT(paramNBTTagCompound);
	}

	private void writeEntityToNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
		if(beforeWriteEntityToNBTHooks != null)
			for(int i = beforeWriteEntityToNBTHooks.length - 1; i >= 0 ; i--)
				beforeWriteEntityToNBTHooks[i].beforeWriteEntityToNBT(paramNBTTagCompound);

		if(overrideWriteEntityToNBTHooks != null)
			overrideWriteEntityToNBTHooks[overrideWriteEntityToNBTHooks.length - 1].writeEntityToNBT(paramNBTTagCompound);
		else
			player.localWriteEntityToNBT(paramNBTTagCompound);

		if(afterWriteEntityToNBTHooks != null)
			for(int i = 0; i < afterWriteEntityToNBTHooks.length; i++)
				afterWriteEntityToNBTHooks[i].afterWriteEntityToNBT(paramNBTTagCompound);

	}

	protected ServerPlayerBase GetOverwrittenWriteEntityToNBT(ServerPlayerBase overWriter)
	{
		if (overrideWriteEntityToNBTHooks == null)
			return overWriter;

		for(int i = 0; i < overrideWriteEntityToNBTHooks.length; i++)
			if(overrideWriteEntityToNBTHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideWriteEntityToNBTHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeWriteEntityToNBTHookTypes = new LinkedList<String>();
	private final static List<String> overrideWriteEntityToNBTHookTypes = new LinkedList<String>();
	private final static List<String> afterWriteEntityToNBTHookTypes = new LinkedList<String>();

	private ServerPlayerBase[] beforeWriteEntityToNBTHooks;
	private ServerPlayerBase[] overrideWriteEntityToNBTHooks;
	private ServerPlayerBase[] afterWriteEntityToNBTHooks;

	public boolean isWriteEntityToNBTModded;

	private static final Map<String, String[]> allBaseBeforeWriteEntityToNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeWriteEntityToNBTInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideWriteEntityToNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideWriteEntityToNBTInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterWriteEntityToNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterWriteEntityToNBTInferiors = new Hashtable<String, String[]>(0);

	
	protected final IServerPlayerAPI player;

	private final static Set<String> keys = new HashSet<String>();
	private final static Map<String, String> keysToVirtualIds = new HashMap<String, String>();
	private final static Set<Class<?>> dynamicTypes = new HashSet<Class<?>>();

	private final static Map<Class<?>, Map<String, Method>> virtualDynamicHookMethods = new HashMap<Class<?>, Map<String, Method>>();

	private final static Map<Class<?>, Map<String, Method>> beforeDynamicHookMethods = new HashMap<Class<?>, Map<String, Method>>();
	private final static Map<Class<?>, Map<String, Method>> overrideDynamicHookMethods = new HashMap<Class<?>, Map<String, Method>>();
	private final static Map<Class<?>, Map<String, Method>> afterDynamicHookMethods = new HashMap<Class<?>, Map<String, Method>>();

	private final static List<String> beforeLocalConstructingHookTypes = new LinkedList<String>();
	private final static List<String> afterLocalConstructingHookTypes = new LinkedList<String>();

	private static final Map<String, List<String>> beforeDynamicHookTypes = new Hashtable<String, List<String>>(0);
	private static final Map<String, List<String>> overrideDynamicHookTypes = new Hashtable<String, List<String>>(0);
	private static final Map<String, List<String>> afterDynamicHookTypes = new Hashtable<String, List<String>>(0);

	private ServerPlayerBase[] beforeLocalConstructingHooks;
	private ServerPlayerBase[] afterLocalConstructingHooks;

	private final Map<ServerPlayerBase, String> baseObjectsToId = new Hashtable<ServerPlayerBase, String>();
	private final Map<String, ServerPlayerBase> allBaseObjects = new Hashtable<String, ServerPlayerBase>();
	private final Set<String> unmodifiableAllBaseIds = Collections.unmodifiableSet(allBaseObjects.keySet());

	private static final Map<String, Constructor<?>> allBaseConstructors = new Hashtable<String, Constructor<?>>();
	private static final Set<String> unmodifiableAllIds = Collections.unmodifiableSet(allBaseConstructors.keySet());

	private static final Map<String, String[]> allBaseBeforeLocalConstructingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeLocalConstructingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterLocalConstructingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterLocalConstructingInferiors = new Hashtable<String, String[]>(0);

	private static final Map<String, Map<String, String[]>> allBaseBeforeDynamicSuperiors = new Hashtable<String, Map<String, String[]>>(0);
	private static final Map<String, Map<String, String[]>> allBaseBeforeDynamicInferiors = new Hashtable<String, Map<String, String[]>>(0);
	private static final Map<String, Map<String, String[]>> allBaseOverrideDynamicSuperiors = new Hashtable<String, Map<String, String[]>>(0);
	private static final Map<String, Map<String, String[]>> allBaseOverrideDynamicInferiors = new Hashtable<String, Map<String, String[]>>(0);
	private static final Map<String, Map<String, String[]>> allBaseAfterDynamicSuperiors = new Hashtable<String, Map<String, String[]>>(0);
	private static final Map<String, Map<String, String[]>> allBaseAfterDynamicInferiors = new Hashtable<String, Map<String, String[]>>(0);

	private static boolean initialized = false;
}
