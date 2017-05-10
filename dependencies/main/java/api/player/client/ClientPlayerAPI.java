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
import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.lang.reflect.*;

public final class ClientPlayerAPI
{
	private final static Class<?>[] Class = new Class[] { ClientPlayerAPI.class };
	private final static Class<?>[] Classes = new Class[] { ClientPlayerAPI.class, String.class };

	private static boolean isCreated;
	private static final Logger logger = Logger.getLogger("ClientPlayerAPI");

	private static void log(String text)
	{
		System.out.println(text);
		logger.fine(text);
	}

	public static void register(String id, Class<?> baseClass)
	{
		register(id, baseClass, null);
	}

	public static void register(String id, Class<?> baseClass, ClientPlayerBaseSorting baseSorting)
	{
		try
		{
			register(baseClass, id, baseSorting);
		}
		catch(RuntimeException exception)
		{
			if(id != null)
				log("Client Player: failed to register id '" + id + "'");
			else
				log("Client Player: failed to register ClientPlayerBase");

			throw exception;
		}
	}

	private static void register(Class<?> baseClass, String id, ClientPlayerBaseSorting baseSorting)
	{
		if(!isCreated)
		{
			try
			{
				Method mandatory = net.minecraft.client.entity.EntityPlayerSP.class.getMethod("getClientPlayerBase", String.class);
				if (mandatory.getReturnType() != ClientPlayerBase.class)
					throw new NoSuchMethodException(ClientPlayerBase.class.getName() + " " + net.minecraft.client.entity.EntityPlayerSP.class.getName() + ".getClientPlayerBase(" + String.class.getName() + ")");
			}
			catch(NoSuchMethodException exception)
			{
				String[] errorMessageParts = new String[]
				{
					"========================================",
					"The API \"Client Player\" version " + api.player.forge.PlayerAPIPlugin.Version + " of the mod \"Player API Core " + api.player.forge.PlayerAPIPlugin.Version + "\" can not be created!",
					"----------------------------------------",
					"Mandatory member method \"{0} getClientPlayerBase({3})\" not found in class \"{1}\".",
					"There are three scenarios this can happen:",
					"* Minecraft Forge is missing a Player API Core which Minecraft version matches its own.",
					"  Download and install the latest Player API Core for the Minecraft version you were trying to run.",
					"* The code of the class \"{2}\" of Player API Core has been modified beyond recognition by another Minecraft Forge coremod.",
					"  Try temporary deinstallation of other core mods to find the culprit and deinstall it permanently to fix this specific problem.",
					"* Player API Core has not been installed correctly.",
					"  Deinstall Player API Core and install it again following the installation instructions in the readme file.",
					"========================================"
				};

				String baseEntityPlayerSPClassName = ClientPlayerBase.class.getName();
				String targetClassName = net.minecraft.client.entity.EntityPlayerSP.class.getName();
				String targetClassFileName = targetClassName.replace(".", File.separator);
				String stringClassName = String.class.getName();

				for(int i=0; i<errorMessageParts.length; i++)
					errorMessageParts[i] = MessageFormat.format(errorMessageParts[i], baseEntityPlayerSPClassName, targetClassName, targetClassFileName, stringClassName);

				for(String errorMessagePart : errorMessageParts)
					logger.severe(errorMessagePart);

				for(String errorMessagePart : errorMessageParts)
					System.err.println(errorMessagePart);

				String errorMessage = "\n\n";
				for(String errorMessagePart : errorMessageParts)
					errorMessage += "\t" + errorMessagePart + "\n";

				throw new RuntimeException(errorMessage, exception);
			}

			log("Client Player " + api.player.forge.PlayerAPIPlugin.Version + " Created");
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
				throw new IllegalArgumentException("Can not find necessary constructor with one argument of type '" + ClientPlayerAPI.class.getName() + "' and eventually a second argument of type 'String' in the class '" + baseClass.getName() + "'", t);
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

			addSorting(id, allBaseBeforeAddMovementStatSuperiors, baseSorting.getBeforeAddMovementStatSuperiors());
			addSorting(id, allBaseBeforeAddMovementStatInferiors, baseSorting.getBeforeAddMovementStatInferiors());
			addSorting(id, allBaseOverrideAddMovementStatSuperiors, baseSorting.getOverrideAddMovementStatSuperiors());
			addSorting(id, allBaseOverrideAddMovementStatInferiors, baseSorting.getOverrideAddMovementStatInferiors());
			addSorting(id, allBaseAfterAddMovementStatSuperiors, baseSorting.getAfterAddMovementStatSuperiors());
			addSorting(id, allBaseAfterAddMovementStatInferiors, baseSorting.getAfterAddMovementStatInferiors());

			addSorting(id, allBaseBeforeAddStatSuperiors, baseSorting.getBeforeAddStatSuperiors());
			addSorting(id, allBaseBeforeAddStatInferiors, baseSorting.getBeforeAddStatInferiors());
			addSorting(id, allBaseOverrideAddStatSuperiors, baseSorting.getOverrideAddStatSuperiors());
			addSorting(id, allBaseOverrideAddStatInferiors, baseSorting.getOverrideAddStatInferiors());
			addSorting(id, allBaseAfterAddStatSuperiors, baseSorting.getAfterAddStatSuperiors());
			addSorting(id, allBaseAfterAddStatInferiors, baseSorting.getAfterAddStatInferiors());

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

			addSorting(id, allBaseBeforeCloseScreenSuperiors, baseSorting.getBeforeCloseScreenSuperiors());
			addSorting(id, allBaseBeforeCloseScreenInferiors, baseSorting.getBeforeCloseScreenInferiors());
			addSorting(id, allBaseOverrideCloseScreenSuperiors, baseSorting.getOverrideCloseScreenSuperiors());
			addSorting(id, allBaseOverrideCloseScreenInferiors, baseSorting.getOverrideCloseScreenInferiors());
			addSorting(id, allBaseAfterCloseScreenSuperiors, baseSorting.getAfterCloseScreenSuperiors());
			addSorting(id, allBaseAfterCloseScreenInferiors, baseSorting.getAfterCloseScreenInferiors());

			addSorting(id, allBaseBeforeDamageEntitySuperiors, baseSorting.getBeforeDamageEntitySuperiors());
			addSorting(id, allBaseBeforeDamageEntityInferiors, baseSorting.getBeforeDamageEntityInferiors());
			addSorting(id, allBaseOverrideDamageEntitySuperiors, baseSorting.getOverrideDamageEntitySuperiors());
			addSorting(id, allBaseOverrideDamageEntityInferiors, baseSorting.getOverrideDamageEntityInferiors());
			addSorting(id, allBaseAfterDamageEntitySuperiors, baseSorting.getAfterDamageEntitySuperiors());
			addSorting(id, allBaseAfterDamageEntityInferiors, baseSorting.getAfterDamageEntityInferiors());

			addSorting(id, allBaseBeforeDisplayGUIBrewingStandSuperiors, baseSorting.getBeforeDisplayGUIBrewingStandSuperiors());
			addSorting(id, allBaseBeforeDisplayGUIBrewingStandInferiors, baseSorting.getBeforeDisplayGUIBrewingStandInferiors());
			addSorting(id, allBaseOverrideDisplayGUIBrewingStandSuperiors, baseSorting.getOverrideDisplayGUIBrewingStandSuperiors());
			addSorting(id, allBaseOverrideDisplayGUIBrewingStandInferiors, baseSorting.getOverrideDisplayGUIBrewingStandInferiors());
			addSorting(id, allBaseAfterDisplayGUIBrewingStandSuperiors, baseSorting.getAfterDisplayGUIBrewingStandSuperiors());
			addSorting(id, allBaseAfterDisplayGUIBrewingStandInferiors, baseSorting.getAfterDisplayGUIBrewingStandInferiors());

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

			addSorting(id, allBaseBeforeDisplayGUIEditSignSuperiors, baseSorting.getBeforeDisplayGUIEditSignSuperiors());
			addSorting(id, allBaseBeforeDisplayGUIEditSignInferiors, baseSorting.getBeforeDisplayGUIEditSignInferiors());
			addSorting(id, allBaseOverrideDisplayGUIEditSignSuperiors, baseSorting.getOverrideDisplayGUIEditSignSuperiors());
			addSorting(id, allBaseOverrideDisplayGUIEditSignInferiors, baseSorting.getOverrideDisplayGUIEditSignInferiors());
			addSorting(id, allBaseAfterDisplayGUIEditSignSuperiors, baseSorting.getAfterDisplayGUIEditSignSuperiors());
			addSorting(id, allBaseAfterDisplayGUIEditSignInferiors, baseSorting.getAfterDisplayGUIEditSignInferiors());

			addSorting(id, allBaseBeforeDisplayGUIEnchantmentSuperiors, baseSorting.getBeforeDisplayGUIEnchantmentSuperiors());
			addSorting(id, allBaseBeforeDisplayGUIEnchantmentInferiors, baseSorting.getBeforeDisplayGUIEnchantmentInferiors());
			addSorting(id, allBaseOverrideDisplayGUIEnchantmentSuperiors, baseSorting.getOverrideDisplayGUIEnchantmentSuperiors());
			addSorting(id, allBaseOverrideDisplayGUIEnchantmentInferiors, baseSorting.getOverrideDisplayGUIEnchantmentInferiors());
			addSorting(id, allBaseAfterDisplayGUIEnchantmentSuperiors, baseSorting.getAfterDisplayGUIEnchantmentSuperiors());
			addSorting(id, allBaseAfterDisplayGUIEnchantmentInferiors, baseSorting.getAfterDisplayGUIEnchantmentInferiors());

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

			addSorting(id, allBaseBeforeDropPlayerItemWithRandomChoiceSuperiors, baseSorting.getBeforeDropPlayerItemWithRandomChoiceSuperiors());
			addSorting(id, allBaseBeforeDropPlayerItemWithRandomChoiceInferiors, baseSorting.getBeforeDropPlayerItemWithRandomChoiceInferiors());
			addSorting(id, allBaseOverrideDropPlayerItemWithRandomChoiceSuperiors, baseSorting.getOverrideDropPlayerItemWithRandomChoiceSuperiors());
			addSorting(id, allBaseOverrideDropPlayerItemWithRandomChoiceInferiors, baseSorting.getOverrideDropPlayerItemWithRandomChoiceInferiors());
			addSorting(id, allBaseAfterDropPlayerItemWithRandomChoiceSuperiors, baseSorting.getAfterDropPlayerItemWithRandomChoiceSuperiors());
			addSorting(id, allBaseAfterDropPlayerItemWithRandomChoiceInferiors, baseSorting.getAfterDropPlayerItemWithRandomChoiceInferiors());

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

			addSorting(id, allBaseBeforeGetBedOrientationInDegreesSuperiors, baseSorting.getBeforeGetBedOrientationInDegreesSuperiors());
			addSorting(id, allBaseBeforeGetBedOrientationInDegreesInferiors, baseSorting.getBeforeGetBedOrientationInDegreesInferiors());
			addSorting(id, allBaseOverrideGetBedOrientationInDegreesSuperiors, baseSorting.getOverrideGetBedOrientationInDegreesSuperiors());
			addSorting(id, allBaseOverrideGetBedOrientationInDegreesInferiors, baseSorting.getOverrideGetBedOrientationInDegreesInferiors());
			addSorting(id, allBaseAfterGetBedOrientationInDegreesSuperiors, baseSorting.getAfterGetBedOrientationInDegreesSuperiors());
			addSorting(id, allBaseAfterGetBedOrientationInDegreesInferiors, baseSorting.getAfterGetBedOrientationInDegreesInferiors());

			addSorting(id, allBaseBeforeGetBrightnessSuperiors, baseSorting.getBeforeGetBrightnessSuperiors());
			addSorting(id, allBaseBeforeGetBrightnessInferiors, baseSorting.getBeforeGetBrightnessInferiors());
			addSorting(id, allBaseOverrideGetBrightnessSuperiors, baseSorting.getOverrideGetBrightnessSuperiors());
			addSorting(id, allBaseOverrideGetBrightnessInferiors, baseSorting.getOverrideGetBrightnessInferiors());
			addSorting(id, allBaseAfterGetBrightnessSuperiors, baseSorting.getAfterGetBrightnessSuperiors());
			addSorting(id, allBaseAfterGetBrightnessInferiors, baseSorting.getAfterGetBrightnessInferiors());

			addSorting(id, allBaseBeforeGetBrightnessForRenderSuperiors, baseSorting.getBeforeGetBrightnessForRenderSuperiors());
			addSorting(id, allBaseBeforeGetBrightnessForRenderInferiors, baseSorting.getBeforeGetBrightnessForRenderInferiors());
			addSorting(id, allBaseOverrideGetBrightnessForRenderSuperiors, baseSorting.getOverrideGetBrightnessForRenderSuperiors());
			addSorting(id, allBaseOverrideGetBrightnessForRenderInferiors, baseSorting.getOverrideGetBrightnessForRenderInferiors());
			addSorting(id, allBaseAfterGetBrightnessForRenderSuperiors, baseSorting.getAfterGetBrightnessForRenderSuperiors());
			addSorting(id, allBaseAfterGetBrightnessForRenderInferiors, baseSorting.getAfterGetBrightnessForRenderInferiors());

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

			addSorting(id, allBaseBeforeGetDistanceSqToEntitySuperiors, baseSorting.getBeforeGetDistanceSqToEntitySuperiors());
			addSorting(id, allBaseBeforeGetDistanceSqToEntityInferiors, baseSorting.getBeforeGetDistanceSqToEntityInferiors());
			addSorting(id, allBaseOverrideGetDistanceSqToEntitySuperiors, baseSorting.getOverrideGetDistanceSqToEntitySuperiors());
			addSorting(id, allBaseOverrideGetDistanceSqToEntityInferiors, baseSorting.getOverrideGetDistanceSqToEntityInferiors());
			addSorting(id, allBaseAfterGetDistanceSqToEntitySuperiors, baseSorting.getAfterGetDistanceSqToEntitySuperiors());
			addSorting(id, allBaseAfterGetDistanceSqToEntityInferiors, baseSorting.getAfterGetDistanceSqToEntityInferiors());

			addSorting(id, allBaseBeforeGetFOVMultiplierSuperiors, baseSorting.getBeforeGetFOVMultiplierSuperiors());
			addSorting(id, allBaseBeforeGetFOVMultiplierInferiors, baseSorting.getBeforeGetFOVMultiplierInferiors());
			addSorting(id, allBaseOverrideGetFOVMultiplierSuperiors, baseSorting.getOverrideGetFOVMultiplierSuperiors());
			addSorting(id, allBaseOverrideGetFOVMultiplierInferiors, baseSorting.getOverrideGetFOVMultiplierInferiors());
			addSorting(id, allBaseAfterGetFOVMultiplierSuperiors, baseSorting.getAfterGetFOVMultiplierSuperiors());
			addSorting(id, allBaseAfterGetFOVMultiplierInferiors, baseSorting.getAfterGetFOVMultiplierInferiors());

			addSorting(id, allBaseBeforeGetHurtSoundSuperiors, baseSorting.getBeforeGetHurtSoundSuperiors());
			addSorting(id, allBaseBeforeGetHurtSoundInferiors, baseSorting.getBeforeGetHurtSoundInferiors());
			addSorting(id, allBaseOverrideGetHurtSoundSuperiors, baseSorting.getOverrideGetHurtSoundSuperiors());
			addSorting(id, allBaseOverrideGetHurtSoundInferiors, baseSorting.getOverrideGetHurtSoundInferiors());
			addSorting(id, allBaseAfterGetHurtSoundSuperiors, baseSorting.getAfterGetHurtSoundSuperiors());
			addSorting(id, allBaseAfterGetHurtSoundInferiors, baseSorting.getAfterGetHurtSoundInferiors());

			addSorting(id, allBaseBeforeGetItemIconSuperiors, baseSorting.getBeforeGetItemIconSuperiors());
			addSorting(id, allBaseBeforeGetItemIconInferiors, baseSorting.getBeforeGetItemIconInferiors());
			addSorting(id, allBaseOverrideGetItemIconSuperiors, baseSorting.getOverrideGetItemIconSuperiors());
			addSorting(id, allBaseOverrideGetItemIconInferiors, baseSorting.getOverrideGetItemIconInferiors());
			addSorting(id, allBaseAfterGetItemIconSuperiors, baseSorting.getAfterGetItemIconSuperiors());
			addSorting(id, allBaseAfterGetItemIconInferiors, baseSorting.getAfterGetItemIconInferiors());

			addSorting(id, allBaseBeforeGetSleepTimerSuperiors, baseSorting.getBeforeGetSleepTimerSuperiors());
			addSorting(id, allBaseBeforeGetSleepTimerInferiors, baseSorting.getBeforeGetSleepTimerInferiors());
			addSorting(id, allBaseOverrideGetSleepTimerSuperiors, baseSorting.getOverrideGetSleepTimerSuperiors());
			addSorting(id, allBaseOverrideGetSleepTimerInferiors, baseSorting.getOverrideGetSleepTimerInferiors());
			addSorting(id, allBaseAfterGetSleepTimerSuperiors, baseSorting.getAfterGetSleepTimerSuperiors());
			addSorting(id, allBaseAfterGetSleepTimerInferiors, baseSorting.getAfterGetSleepTimerInferiors());

			addSorting(id, allBaseBeforeHandleLavaMovementSuperiors, baseSorting.getBeforeHandleLavaMovementSuperiors());
			addSorting(id, allBaseBeforeHandleLavaMovementInferiors, baseSorting.getBeforeHandleLavaMovementInferiors());
			addSorting(id, allBaseOverrideHandleLavaMovementSuperiors, baseSorting.getOverrideHandleLavaMovementSuperiors());
			addSorting(id, allBaseOverrideHandleLavaMovementInferiors, baseSorting.getOverrideHandleLavaMovementInferiors());
			addSorting(id, allBaseAfterHandleLavaMovementSuperiors, baseSorting.getAfterHandleLavaMovementSuperiors());
			addSorting(id, allBaseAfterHandleLavaMovementInferiors, baseSorting.getAfterHandleLavaMovementInferiors());

			addSorting(id, allBaseBeforeHandleWaterMovementSuperiors, baseSorting.getBeforeHandleWaterMovementSuperiors());
			addSorting(id, allBaseBeforeHandleWaterMovementInferiors, baseSorting.getBeforeHandleWaterMovementInferiors());
			addSorting(id, allBaseOverrideHandleWaterMovementSuperiors, baseSorting.getOverrideHandleWaterMovementSuperiors());
			addSorting(id, allBaseOverrideHandleWaterMovementInferiors, baseSorting.getOverrideHandleWaterMovementInferiors());
			addSorting(id, allBaseAfterHandleWaterMovementSuperiors, baseSorting.getAfterHandleWaterMovementSuperiors());
			addSorting(id, allBaseAfterHandleWaterMovementInferiors, baseSorting.getAfterHandleWaterMovementInferiors());

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

			addSorting(id, allBaseBeforeIsSprintingSuperiors, baseSorting.getBeforeIsSprintingSuperiors());
			addSorting(id, allBaseBeforeIsSprintingInferiors, baseSorting.getBeforeIsSprintingInferiors());
			addSorting(id, allBaseOverrideIsSprintingSuperiors, baseSorting.getOverrideIsSprintingSuperiors());
			addSorting(id, allBaseOverrideIsSprintingInferiors, baseSorting.getOverrideIsSprintingInferiors());
			addSorting(id, allBaseAfterIsSprintingSuperiors, baseSorting.getAfterIsSprintingSuperiors());
			addSorting(id, allBaseAfterIsSprintingInferiors, baseSorting.getAfterIsSprintingInferiors());

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

			addSorting(id, allBaseBeforePlayStepSoundSuperiors, baseSorting.getBeforePlayStepSoundSuperiors());
			addSorting(id, allBaseBeforePlayStepSoundInferiors, baseSorting.getBeforePlayStepSoundInferiors());
			addSorting(id, allBaseOverridePlayStepSoundSuperiors, baseSorting.getOverridePlayStepSoundSuperiors());
			addSorting(id, allBaseOverridePlayStepSoundInferiors, baseSorting.getOverridePlayStepSoundInferiors());
			addSorting(id, allBaseAfterPlayStepSoundSuperiors, baseSorting.getAfterPlayStepSoundSuperiors());
			addSorting(id, allBaseAfterPlayStepSoundInferiors, baseSorting.getAfterPlayStepSoundInferiors());

			addSorting(id, allBaseBeforePushOutOfBlocksSuperiors, baseSorting.getBeforePushOutOfBlocksSuperiors());
			addSorting(id, allBaseBeforePushOutOfBlocksInferiors, baseSorting.getBeforePushOutOfBlocksInferiors());
			addSorting(id, allBaseOverridePushOutOfBlocksSuperiors, baseSorting.getOverridePushOutOfBlocksSuperiors());
			addSorting(id, allBaseOverridePushOutOfBlocksInferiors, baseSorting.getOverridePushOutOfBlocksInferiors());
			addSorting(id, allBaseAfterPushOutOfBlocksSuperiors, baseSorting.getAfterPushOutOfBlocksSuperiors());
			addSorting(id, allBaseAfterPushOutOfBlocksInferiors, baseSorting.getAfterPushOutOfBlocksInferiors());

			addSorting(id, allBaseBeforeRayTraceSuperiors, baseSorting.getBeforeRayTraceSuperiors());
			addSorting(id, allBaseBeforeRayTraceInferiors, baseSorting.getBeforeRayTraceInferiors());
			addSorting(id, allBaseOverrideRayTraceSuperiors, baseSorting.getOverrideRayTraceSuperiors());
			addSorting(id, allBaseOverrideRayTraceInferiors, baseSorting.getOverrideRayTraceInferiors());
			addSorting(id, allBaseAfterRayTraceSuperiors, baseSorting.getAfterRayTraceSuperiors());
			addSorting(id, allBaseAfterRayTraceInferiors, baseSorting.getAfterRayTraceInferiors());

			addSorting(id, allBaseBeforeReadEntityFromNBTSuperiors, baseSorting.getBeforeReadEntityFromNBTSuperiors());
			addSorting(id, allBaseBeforeReadEntityFromNBTInferiors, baseSorting.getBeforeReadEntityFromNBTInferiors());
			addSorting(id, allBaseOverrideReadEntityFromNBTSuperiors, baseSorting.getOverrideReadEntityFromNBTSuperiors());
			addSorting(id, allBaseOverrideReadEntityFromNBTInferiors, baseSorting.getOverrideReadEntityFromNBTInferiors());
			addSorting(id, allBaseAfterReadEntityFromNBTSuperiors, baseSorting.getAfterReadEntityFromNBTSuperiors());
			addSorting(id, allBaseAfterReadEntityFromNBTInferiors, baseSorting.getAfterReadEntityFromNBTInferiors());

			addSorting(id, allBaseBeforeRespawnPlayerSuperiors, baseSorting.getBeforeRespawnPlayerSuperiors());
			addSorting(id, allBaseBeforeRespawnPlayerInferiors, baseSorting.getBeforeRespawnPlayerInferiors());
			addSorting(id, allBaseOverrideRespawnPlayerSuperiors, baseSorting.getOverrideRespawnPlayerSuperiors());
			addSorting(id, allBaseOverrideRespawnPlayerInferiors, baseSorting.getOverrideRespawnPlayerInferiors());
			addSorting(id, allBaseAfterRespawnPlayerSuperiors, baseSorting.getAfterRespawnPlayerSuperiors());
			addSorting(id, allBaseAfterRespawnPlayerInferiors, baseSorting.getAfterRespawnPlayerInferiors());

			addSorting(id, allBaseBeforeSetDeadSuperiors, baseSorting.getBeforeSetDeadSuperiors());
			addSorting(id, allBaseBeforeSetDeadInferiors, baseSorting.getBeforeSetDeadInferiors());
			addSorting(id, allBaseOverrideSetDeadSuperiors, baseSorting.getOverrideSetDeadSuperiors());
			addSorting(id, allBaseOverrideSetDeadInferiors, baseSorting.getOverrideSetDeadInferiors());
			addSorting(id, allBaseAfterSetDeadSuperiors, baseSorting.getAfterSetDeadSuperiors());
			addSorting(id, allBaseAfterSetDeadInferiors, baseSorting.getAfterSetDeadInferiors());

			addSorting(id, allBaseBeforeSetPlayerSPHealthSuperiors, baseSorting.getBeforeSetPlayerSPHealthSuperiors());
			addSorting(id, allBaseBeforeSetPlayerSPHealthInferiors, baseSorting.getBeforeSetPlayerSPHealthInferiors());
			addSorting(id, allBaseOverrideSetPlayerSPHealthSuperiors, baseSorting.getOverrideSetPlayerSPHealthSuperiors());
			addSorting(id, allBaseOverrideSetPlayerSPHealthInferiors, baseSorting.getOverrideSetPlayerSPHealthInferiors());
			addSorting(id, allBaseAfterSetPlayerSPHealthSuperiors, baseSorting.getAfterSetPlayerSPHealthSuperiors());
			addSorting(id, allBaseAfterSetPlayerSPHealthInferiors, baseSorting.getAfterSetPlayerSPHealthInferiors());

			addSorting(id, allBaseBeforeSetPositionAndRotationSuperiors, baseSorting.getBeforeSetPositionAndRotationSuperiors());
			addSorting(id, allBaseBeforeSetPositionAndRotationInferiors, baseSorting.getBeforeSetPositionAndRotationInferiors());
			addSorting(id, allBaseOverrideSetPositionAndRotationSuperiors, baseSorting.getOverrideSetPositionAndRotationSuperiors());
			addSorting(id, allBaseOverrideSetPositionAndRotationInferiors, baseSorting.getOverrideSetPositionAndRotationInferiors());
			addSorting(id, allBaseAfterSetPositionAndRotationSuperiors, baseSorting.getAfterSetPositionAndRotationSuperiors());
			addSorting(id, allBaseAfterSetPositionAndRotationInferiors, baseSorting.getAfterSetPositionAndRotationInferiors());

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

			addSorting(id, allBaseBeforeSleepInBedAtSuperiors, baseSorting.getBeforeSleepInBedAtSuperiors());
			addSorting(id, allBaseBeforeSleepInBedAtInferiors, baseSorting.getBeforeSleepInBedAtInferiors());
			addSorting(id, allBaseOverrideSleepInBedAtSuperiors, baseSorting.getOverrideSleepInBedAtSuperiors());
			addSorting(id, allBaseOverrideSleepInBedAtInferiors, baseSorting.getOverrideSleepInBedAtInferiors());
			addSorting(id, allBaseAfterSleepInBedAtSuperiors, baseSorting.getAfterSleepInBedAtSuperiors());
			addSorting(id, allBaseAfterSleepInBedAtInferiors, baseSorting.getAfterSleepInBedAtInferiors());

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

		addMethod(id, baseClass, beforeLocalConstructingHookTypes, "beforeLocalConstructing", net.minecraft.client.Minecraft.class, net.minecraft.world.World.class, net.minecraft.util.Session.class, int.class);
		addMethod(id, baseClass, afterLocalConstructingHookTypes, "afterLocalConstructing", net.minecraft.client.Minecraft.class, net.minecraft.world.World.class, net.minecraft.util.Session.class, int.class);


		addMethod(id, baseClass, beforeAddExhaustionHookTypes, "beforeAddExhaustion", float.class);
		addMethod(id, baseClass, overrideAddExhaustionHookTypes, "addExhaustion", float.class);
		addMethod(id, baseClass, afterAddExhaustionHookTypes, "afterAddExhaustion", float.class);

		addMethod(id, baseClass, beforeAddMovementStatHookTypes, "beforeAddMovementStat", double.class, double.class, double.class);
		addMethod(id, baseClass, overrideAddMovementStatHookTypes, "addMovementStat", double.class, double.class, double.class);
		addMethod(id, baseClass, afterAddMovementStatHookTypes, "afterAddMovementStat", double.class, double.class, double.class);

		addMethod(id, baseClass, beforeAddStatHookTypes, "beforeAddStat", net.minecraft.stats.StatBase.class, int.class);
		addMethod(id, baseClass, overrideAddStatHookTypes, "addStat", net.minecraft.stats.StatBase.class, int.class);
		addMethod(id, baseClass, afterAddStatHookTypes, "afterAddStat", net.minecraft.stats.StatBase.class, int.class);

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

		addMethod(id, baseClass, beforeCloseScreenHookTypes, "beforeCloseScreen");
		addMethod(id, baseClass, overrideCloseScreenHookTypes, "closeScreen");
		addMethod(id, baseClass, afterCloseScreenHookTypes, "afterCloseScreen");

		addMethod(id, baseClass, beforeDamageEntityHookTypes, "beforeDamageEntity", net.minecraft.util.DamageSource.class, float.class);
		addMethod(id, baseClass, overrideDamageEntityHookTypes, "damageEntity", net.minecraft.util.DamageSource.class, float.class);
		addMethod(id, baseClass, afterDamageEntityHookTypes, "afterDamageEntity", net.minecraft.util.DamageSource.class, float.class);

		addMethod(id, baseClass, beforeDisplayGUIBrewingStandHookTypes, "beforeDisplayGUIBrewingStand", net.minecraft.tileentity.TileEntityBrewingStand.class);
		addMethod(id, baseClass, overrideDisplayGUIBrewingStandHookTypes, "displayGUIBrewingStand", net.minecraft.tileentity.TileEntityBrewingStand.class);
		addMethod(id, baseClass, afterDisplayGUIBrewingStandHookTypes, "afterDisplayGUIBrewingStand", net.minecraft.tileentity.TileEntityBrewingStand.class);

		addMethod(id, baseClass, beforeDisplayGUIChestHookTypes, "beforeDisplayGUIChest", net.minecraft.inventory.IInventory.class);
		addMethod(id, baseClass, overrideDisplayGUIChestHookTypes, "displayGUIChest", net.minecraft.inventory.IInventory.class);
		addMethod(id, baseClass, afterDisplayGUIChestHookTypes, "afterDisplayGUIChest", net.minecraft.inventory.IInventory.class);

		addMethod(id, baseClass, beforeDisplayGUIDispenserHookTypes, "beforeDisplayGUIDispenser", net.minecraft.tileentity.TileEntityDispenser.class);
		addMethod(id, baseClass, overrideDisplayGUIDispenserHookTypes, "displayGUIDispenser", net.minecraft.tileentity.TileEntityDispenser.class);
		addMethod(id, baseClass, afterDisplayGUIDispenserHookTypes, "afterDisplayGUIDispenser", net.minecraft.tileentity.TileEntityDispenser.class);

		addMethod(id, baseClass, beforeDisplayGUIEditSignHookTypes, "beforeDisplayGUIEditSign", net.minecraft.tileentity.TileEntity.class);
		addMethod(id, baseClass, overrideDisplayGUIEditSignHookTypes, "displayGUIEditSign", net.minecraft.tileentity.TileEntity.class);
		addMethod(id, baseClass, afterDisplayGUIEditSignHookTypes, "afterDisplayGUIEditSign", net.minecraft.tileentity.TileEntity.class);

		addMethod(id, baseClass, beforeDisplayGUIEnchantmentHookTypes, "beforeDisplayGUIEnchantment", int.class, int.class, int.class, String.class);
		addMethod(id, baseClass, overrideDisplayGUIEnchantmentHookTypes, "displayGUIEnchantment", int.class, int.class, int.class, String.class);
		addMethod(id, baseClass, afterDisplayGUIEnchantmentHookTypes, "afterDisplayGUIEnchantment", int.class, int.class, int.class, String.class);

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

		addMethod(id, baseClass, beforeDropPlayerItemWithRandomChoiceHookTypes, "beforeDropPlayerItemWithRandomChoice", net.minecraft.item.ItemStack.class, boolean.class, boolean.class);
		addMethod(id, baseClass, overrideDropPlayerItemWithRandomChoiceHookTypes, "dropPlayerItemWithRandomChoice", net.minecraft.item.ItemStack.class, boolean.class, boolean.class);
		addMethod(id, baseClass, afterDropPlayerItemWithRandomChoiceHookTypes, "afterDropPlayerItemWithRandomChoice", net.minecraft.item.ItemStack.class, boolean.class, boolean.class);

		addMethod(id, baseClass, beforeFallHookTypes, "beforeFall", float.class);
		addMethod(id, baseClass, overrideFallHookTypes, "fall", float.class);
		addMethod(id, baseClass, afterFallHookTypes, "afterFall", float.class);

		addMethod(id, baseClass, beforeGetAIMoveSpeedHookTypes, "beforeGetAIMoveSpeed");
		addMethod(id, baseClass, overrideGetAIMoveSpeedHookTypes, "getAIMoveSpeed");
		addMethod(id, baseClass, afterGetAIMoveSpeedHookTypes, "afterGetAIMoveSpeed");

		addMethod(id, baseClass, beforeGetBedOrientationInDegreesHookTypes, "beforeGetBedOrientationInDegrees");
		addMethod(id, baseClass, overrideGetBedOrientationInDegreesHookTypes, "getBedOrientationInDegrees");
		addMethod(id, baseClass, afterGetBedOrientationInDegreesHookTypes, "afterGetBedOrientationInDegrees");

		addMethod(id, baseClass, beforeGetBrightnessHookTypes, "beforeGetBrightness", float.class);
		addMethod(id, baseClass, overrideGetBrightnessHookTypes, "getBrightness", float.class);
		addMethod(id, baseClass, afterGetBrightnessHookTypes, "afterGetBrightness", float.class);

		addMethod(id, baseClass, beforeGetBrightnessForRenderHookTypes, "beforeGetBrightnessForRender", float.class);
		addMethod(id, baseClass, overrideGetBrightnessForRenderHookTypes, "getBrightnessForRender", float.class);
		addMethod(id, baseClass, afterGetBrightnessForRenderHookTypes, "afterGetBrightnessForRender", float.class);

		addMethod(id, baseClass, beforeGetCurrentPlayerStrVsBlockHookTypes, "beforeGetCurrentPlayerStrVsBlock", net.minecraft.block.Block.class, boolean.class);
		addMethod(id, baseClass, overrideGetCurrentPlayerStrVsBlockHookTypes, "getCurrentPlayerStrVsBlock", net.minecraft.block.Block.class, boolean.class);
		addMethod(id, baseClass, afterGetCurrentPlayerStrVsBlockHookTypes, "afterGetCurrentPlayerStrVsBlock", net.minecraft.block.Block.class, boolean.class);

		addMethod(id, baseClass, beforeGetCurrentPlayerStrVsBlockForgeHookTypes, "beforeGetCurrentPlayerStrVsBlockForge", net.minecraft.block.Block.class, boolean.class, int.class);
		addMethod(id, baseClass, overrideGetCurrentPlayerStrVsBlockForgeHookTypes, "getCurrentPlayerStrVsBlockForge", net.minecraft.block.Block.class, boolean.class, int.class);
		addMethod(id, baseClass, afterGetCurrentPlayerStrVsBlockForgeHookTypes, "afterGetCurrentPlayerStrVsBlockForge", net.minecraft.block.Block.class, boolean.class, int.class);

		addMethod(id, baseClass, beforeGetDistanceSqHookTypes, "beforeGetDistanceSq", double.class, double.class, double.class);
		addMethod(id, baseClass, overrideGetDistanceSqHookTypes, "getDistanceSq", double.class, double.class, double.class);
		addMethod(id, baseClass, afterGetDistanceSqHookTypes, "afterGetDistanceSq", double.class, double.class, double.class);

		addMethod(id, baseClass, beforeGetDistanceSqToEntityHookTypes, "beforeGetDistanceSqToEntity", net.minecraft.entity.Entity.class);
		addMethod(id, baseClass, overrideGetDistanceSqToEntityHookTypes, "getDistanceSqToEntity", net.minecraft.entity.Entity.class);
		addMethod(id, baseClass, afterGetDistanceSqToEntityHookTypes, "afterGetDistanceSqToEntity", net.minecraft.entity.Entity.class);

		addMethod(id, baseClass, beforeGetFOVMultiplierHookTypes, "beforeGetFOVMultiplier");
		addMethod(id, baseClass, overrideGetFOVMultiplierHookTypes, "getFOVMultiplier");
		addMethod(id, baseClass, afterGetFOVMultiplierHookTypes, "afterGetFOVMultiplier");

		addMethod(id, baseClass, beforeGetHurtSoundHookTypes, "beforeGetHurtSound");
		addMethod(id, baseClass, overrideGetHurtSoundHookTypes, "getHurtSound");
		addMethod(id, baseClass, afterGetHurtSoundHookTypes, "afterGetHurtSound");

		addMethod(id, baseClass, beforeGetItemIconHookTypes, "beforeGetItemIcon", net.minecraft.item.ItemStack.class, int.class);
		addMethod(id, baseClass, overrideGetItemIconHookTypes, "getItemIcon", net.minecraft.item.ItemStack.class, int.class);
		addMethod(id, baseClass, afterGetItemIconHookTypes, "afterGetItemIcon", net.minecraft.item.ItemStack.class, int.class);

		addMethod(id, baseClass, beforeGetSleepTimerHookTypes, "beforeGetSleepTimer");
		addMethod(id, baseClass, overrideGetSleepTimerHookTypes, "getSleepTimer");
		addMethod(id, baseClass, afterGetSleepTimerHookTypes, "afterGetSleepTimer");

		addMethod(id, baseClass, beforeHandleLavaMovementHookTypes, "beforeHandleLavaMovement");
		addMethod(id, baseClass, overrideHandleLavaMovementHookTypes, "handleLavaMovement");
		addMethod(id, baseClass, afterHandleLavaMovementHookTypes, "afterHandleLavaMovement");

		addMethod(id, baseClass, beforeHandleWaterMovementHookTypes, "beforeHandleWaterMovement");
		addMethod(id, baseClass, overrideHandleWaterMovementHookTypes, "handleWaterMovement");
		addMethod(id, baseClass, afterHandleWaterMovementHookTypes, "afterHandleWaterMovement");

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

		addMethod(id, baseClass, beforeIsSprintingHookTypes, "beforeIsSprinting");
		addMethod(id, baseClass, overrideIsSprintingHookTypes, "isSprinting");
		addMethod(id, baseClass, afterIsSprintingHookTypes, "afterIsSprinting");

		addMethod(id, baseClass, beforeJumpHookTypes, "beforeJump");
		addMethod(id, baseClass, overrideJumpHookTypes, "jump");
		addMethod(id, baseClass, afterJumpHookTypes, "afterJump");

		addMethod(id, baseClass, beforeKnockBackHookTypes, "beforeKnockBack", net.minecraft.entity.Entity.class, float.class, double.class, double.class);
		addMethod(id, baseClass, overrideKnockBackHookTypes, "knockBack", net.minecraft.entity.Entity.class, float.class, double.class, double.class);
		addMethod(id, baseClass, afterKnockBackHookTypes, "afterKnockBack", net.minecraft.entity.Entity.class, float.class, double.class, double.class);

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

		addMethod(id, baseClass, beforePlayStepSoundHookTypes, "beforePlayStepSound", int.class, int.class, int.class, net.minecraft.block.Block.class);
		addMethod(id, baseClass, overridePlayStepSoundHookTypes, "playStepSound", int.class, int.class, int.class, net.minecraft.block.Block.class);
		addMethod(id, baseClass, afterPlayStepSoundHookTypes, "afterPlayStepSound", int.class, int.class, int.class, net.minecraft.block.Block.class);

		addMethod(id, baseClass, beforePushOutOfBlocksHookTypes, "beforePushOutOfBlocks", double.class, double.class, double.class);
		addMethod(id, baseClass, overridePushOutOfBlocksHookTypes, "pushOutOfBlocks", double.class, double.class, double.class);
		addMethod(id, baseClass, afterPushOutOfBlocksHookTypes, "afterPushOutOfBlocks", double.class, double.class, double.class);

		addMethod(id, baseClass, beforeRayTraceHookTypes, "beforeRayTrace", double.class, float.class);
		addMethod(id, baseClass, overrideRayTraceHookTypes, "rayTrace", double.class, float.class);
		addMethod(id, baseClass, afterRayTraceHookTypes, "afterRayTrace", double.class, float.class);

		addMethod(id, baseClass, beforeReadEntityFromNBTHookTypes, "beforeReadEntityFromNBT", net.minecraft.nbt.NBTTagCompound.class);
		addMethod(id, baseClass, overrideReadEntityFromNBTHookTypes, "readEntityFromNBT", net.minecraft.nbt.NBTTagCompound.class);
		addMethod(id, baseClass, afterReadEntityFromNBTHookTypes, "afterReadEntityFromNBT", net.minecraft.nbt.NBTTagCompound.class);

		addMethod(id, baseClass, beforeRespawnPlayerHookTypes, "beforeRespawnPlayer");
		addMethod(id, baseClass, overrideRespawnPlayerHookTypes, "respawnPlayer");
		addMethod(id, baseClass, afterRespawnPlayerHookTypes, "afterRespawnPlayer");

		addMethod(id, baseClass, beforeSetDeadHookTypes, "beforeSetDead");
		addMethod(id, baseClass, overrideSetDeadHookTypes, "setDead");
		addMethod(id, baseClass, afterSetDeadHookTypes, "afterSetDead");

		addMethod(id, baseClass, beforeSetPlayerSPHealthHookTypes, "beforeSetPlayerSPHealth", float.class);
		addMethod(id, baseClass, overrideSetPlayerSPHealthHookTypes, "setPlayerSPHealth", float.class);
		addMethod(id, baseClass, afterSetPlayerSPHealthHookTypes, "afterSetPlayerSPHealth", float.class);

		addMethod(id, baseClass, beforeSetPositionAndRotationHookTypes, "beforeSetPositionAndRotation", double.class, double.class, double.class, float.class, float.class);
		addMethod(id, baseClass, overrideSetPositionAndRotationHookTypes, "setPositionAndRotation", double.class, double.class, double.class, float.class, float.class);
		addMethod(id, baseClass, afterSetPositionAndRotationHookTypes, "afterSetPositionAndRotation", double.class, double.class, double.class, float.class, float.class);

		addMethod(id, baseClass, beforeSetSneakingHookTypes, "beforeSetSneaking", boolean.class);
		addMethod(id, baseClass, overrideSetSneakingHookTypes, "setSneaking", boolean.class);
		addMethod(id, baseClass, afterSetSneakingHookTypes, "afterSetSneaking", boolean.class);

		addMethod(id, baseClass, beforeSetSprintingHookTypes, "beforeSetSprinting", boolean.class);
		addMethod(id, baseClass, overrideSetSprintingHookTypes, "setSprinting", boolean.class);
		addMethod(id, baseClass, afterSetSprintingHookTypes, "afterSetSprinting", boolean.class);

		addMethod(id, baseClass, beforeSleepInBedAtHookTypes, "beforeSleepInBedAt", int.class, int.class, int.class);
		addMethod(id, baseClass, overrideSleepInBedAtHookTypes, "sleepInBedAt", int.class, int.class, int.class);
		addMethod(id, baseClass, afterSleepInBedAtHookTypes, "afterSleepInBedAt", int.class, int.class, int.class);

		addMethod(id, baseClass, beforeSwingItemHookTypes, "beforeSwingItem");
		addMethod(id, baseClass, overrideSwingItemHookTypes, "swingItem");
		addMethod(id, baseClass, afterSwingItemHookTypes, "afterSwingItem");

		addMethod(id, baseClass, beforeUpdateEntityActionStateHookTypes, "beforeUpdateEntityActionState");
		addMethod(id, baseClass, overrideUpdateEntityActionStateHookTypes, "updateEntityActionState");
		addMethod(id, baseClass, afterUpdateEntityActionStateHookTypes, "afterUpdateEntityActionState");

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

		for(IClientPlayerAPI instance : getAllInstancesList())
			instance.getClientPlayerAPI().attachClientPlayerBase(id);

		System.out.println("Client Player: registered " + id);
		logger.fine("Client Player: registered class '" + baseClass.getName() + "' with id '" + id + "'");

		initialized = false;
	}

	public static boolean unregister(String id)
	{
		if(id == null)
			return false;

		Constructor<?> constructor = allBaseConstructors.remove(id);
		if(constructor == null)
			return false;

		for(IClientPlayerAPI instance : getAllInstancesList())
			instance.getClientPlayerAPI().detachClientPlayerBase(id);

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

		allBaseBeforeAddMovementStatSuperiors.remove(id);
		allBaseBeforeAddMovementStatInferiors.remove(id);
		allBaseOverrideAddMovementStatSuperiors.remove(id);
		allBaseOverrideAddMovementStatInferiors.remove(id);
		allBaseAfterAddMovementStatSuperiors.remove(id);
		allBaseAfterAddMovementStatInferiors.remove(id);

		beforeAddMovementStatHookTypes.remove(id);
		overrideAddMovementStatHookTypes.remove(id);
		afterAddMovementStatHookTypes.remove(id);

		allBaseBeforeAddStatSuperiors.remove(id);
		allBaseBeforeAddStatInferiors.remove(id);
		allBaseOverrideAddStatSuperiors.remove(id);
		allBaseOverrideAddStatInferiors.remove(id);
		allBaseAfterAddStatSuperiors.remove(id);
		allBaseAfterAddStatInferiors.remove(id);

		beforeAddStatHookTypes.remove(id);
		overrideAddStatHookTypes.remove(id);
		afterAddStatHookTypes.remove(id);

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

		allBaseBeforeCloseScreenSuperiors.remove(id);
		allBaseBeforeCloseScreenInferiors.remove(id);
		allBaseOverrideCloseScreenSuperiors.remove(id);
		allBaseOverrideCloseScreenInferiors.remove(id);
		allBaseAfterCloseScreenSuperiors.remove(id);
		allBaseAfterCloseScreenInferiors.remove(id);

		beforeCloseScreenHookTypes.remove(id);
		overrideCloseScreenHookTypes.remove(id);
		afterCloseScreenHookTypes.remove(id);

		allBaseBeforeDamageEntitySuperiors.remove(id);
		allBaseBeforeDamageEntityInferiors.remove(id);
		allBaseOverrideDamageEntitySuperiors.remove(id);
		allBaseOverrideDamageEntityInferiors.remove(id);
		allBaseAfterDamageEntitySuperiors.remove(id);
		allBaseAfterDamageEntityInferiors.remove(id);

		beforeDamageEntityHookTypes.remove(id);
		overrideDamageEntityHookTypes.remove(id);
		afterDamageEntityHookTypes.remove(id);

		allBaseBeforeDisplayGUIBrewingStandSuperiors.remove(id);
		allBaseBeforeDisplayGUIBrewingStandInferiors.remove(id);
		allBaseOverrideDisplayGUIBrewingStandSuperiors.remove(id);
		allBaseOverrideDisplayGUIBrewingStandInferiors.remove(id);
		allBaseAfterDisplayGUIBrewingStandSuperiors.remove(id);
		allBaseAfterDisplayGUIBrewingStandInferiors.remove(id);

		beforeDisplayGUIBrewingStandHookTypes.remove(id);
		overrideDisplayGUIBrewingStandHookTypes.remove(id);
		afterDisplayGUIBrewingStandHookTypes.remove(id);

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

		allBaseBeforeDisplayGUIEditSignSuperiors.remove(id);
		allBaseBeforeDisplayGUIEditSignInferiors.remove(id);
		allBaseOverrideDisplayGUIEditSignSuperiors.remove(id);
		allBaseOverrideDisplayGUIEditSignInferiors.remove(id);
		allBaseAfterDisplayGUIEditSignSuperiors.remove(id);
		allBaseAfterDisplayGUIEditSignInferiors.remove(id);

		beforeDisplayGUIEditSignHookTypes.remove(id);
		overrideDisplayGUIEditSignHookTypes.remove(id);
		afterDisplayGUIEditSignHookTypes.remove(id);

		allBaseBeforeDisplayGUIEnchantmentSuperiors.remove(id);
		allBaseBeforeDisplayGUIEnchantmentInferiors.remove(id);
		allBaseOverrideDisplayGUIEnchantmentSuperiors.remove(id);
		allBaseOverrideDisplayGUIEnchantmentInferiors.remove(id);
		allBaseAfterDisplayGUIEnchantmentSuperiors.remove(id);
		allBaseAfterDisplayGUIEnchantmentInferiors.remove(id);

		beforeDisplayGUIEnchantmentHookTypes.remove(id);
		overrideDisplayGUIEnchantmentHookTypes.remove(id);
		afterDisplayGUIEnchantmentHookTypes.remove(id);

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

		allBaseBeforeDropPlayerItemWithRandomChoiceSuperiors.remove(id);
		allBaseBeforeDropPlayerItemWithRandomChoiceInferiors.remove(id);
		allBaseOverrideDropPlayerItemWithRandomChoiceSuperiors.remove(id);
		allBaseOverrideDropPlayerItemWithRandomChoiceInferiors.remove(id);
		allBaseAfterDropPlayerItemWithRandomChoiceSuperiors.remove(id);
		allBaseAfterDropPlayerItemWithRandomChoiceInferiors.remove(id);

		beforeDropPlayerItemWithRandomChoiceHookTypes.remove(id);
		overrideDropPlayerItemWithRandomChoiceHookTypes.remove(id);
		afterDropPlayerItemWithRandomChoiceHookTypes.remove(id);

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

		allBaseBeforeGetBedOrientationInDegreesSuperiors.remove(id);
		allBaseBeforeGetBedOrientationInDegreesInferiors.remove(id);
		allBaseOverrideGetBedOrientationInDegreesSuperiors.remove(id);
		allBaseOverrideGetBedOrientationInDegreesInferiors.remove(id);
		allBaseAfterGetBedOrientationInDegreesSuperiors.remove(id);
		allBaseAfterGetBedOrientationInDegreesInferiors.remove(id);

		beforeGetBedOrientationInDegreesHookTypes.remove(id);
		overrideGetBedOrientationInDegreesHookTypes.remove(id);
		afterGetBedOrientationInDegreesHookTypes.remove(id);

		allBaseBeforeGetBrightnessSuperiors.remove(id);
		allBaseBeforeGetBrightnessInferiors.remove(id);
		allBaseOverrideGetBrightnessSuperiors.remove(id);
		allBaseOverrideGetBrightnessInferiors.remove(id);
		allBaseAfterGetBrightnessSuperiors.remove(id);
		allBaseAfterGetBrightnessInferiors.remove(id);

		beforeGetBrightnessHookTypes.remove(id);
		overrideGetBrightnessHookTypes.remove(id);
		afterGetBrightnessHookTypes.remove(id);

		allBaseBeforeGetBrightnessForRenderSuperiors.remove(id);
		allBaseBeforeGetBrightnessForRenderInferiors.remove(id);
		allBaseOverrideGetBrightnessForRenderSuperiors.remove(id);
		allBaseOverrideGetBrightnessForRenderInferiors.remove(id);
		allBaseAfterGetBrightnessForRenderSuperiors.remove(id);
		allBaseAfterGetBrightnessForRenderInferiors.remove(id);

		beforeGetBrightnessForRenderHookTypes.remove(id);
		overrideGetBrightnessForRenderHookTypes.remove(id);
		afterGetBrightnessForRenderHookTypes.remove(id);

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

		allBaseBeforeGetDistanceSqToEntitySuperiors.remove(id);
		allBaseBeforeGetDistanceSqToEntityInferiors.remove(id);
		allBaseOverrideGetDistanceSqToEntitySuperiors.remove(id);
		allBaseOverrideGetDistanceSqToEntityInferiors.remove(id);
		allBaseAfterGetDistanceSqToEntitySuperiors.remove(id);
		allBaseAfterGetDistanceSqToEntityInferiors.remove(id);

		beforeGetDistanceSqToEntityHookTypes.remove(id);
		overrideGetDistanceSqToEntityHookTypes.remove(id);
		afterGetDistanceSqToEntityHookTypes.remove(id);

		allBaseBeforeGetFOVMultiplierSuperiors.remove(id);
		allBaseBeforeGetFOVMultiplierInferiors.remove(id);
		allBaseOverrideGetFOVMultiplierSuperiors.remove(id);
		allBaseOverrideGetFOVMultiplierInferiors.remove(id);
		allBaseAfterGetFOVMultiplierSuperiors.remove(id);
		allBaseAfterGetFOVMultiplierInferiors.remove(id);

		beforeGetFOVMultiplierHookTypes.remove(id);
		overrideGetFOVMultiplierHookTypes.remove(id);
		afterGetFOVMultiplierHookTypes.remove(id);

		allBaseBeforeGetHurtSoundSuperiors.remove(id);
		allBaseBeforeGetHurtSoundInferiors.remove(id);
		allBaseOverrideGetHurtSoundSuperiors.remove(id);
		allBaseOverrideGetHurtSoundInferiors.remove(id);
		allBaseAfterGetHurtSoundSuperiors.remove(id);
		allBaseAfterGetHurtSoundInferiors.remove(id);

		beforeGetHurtSoundHookTypes.remove(id);
		overrideGetHurtSoundHookTypes.remove(id);
		afterGetHurtSoundHookTypes.remove(id);

		allBaseBeforeGetItemIconSuperiors.remove(id);
		allBaseBeforeGetItemIconInferiors.remove(id);
		allBaseOverrideGetItemIconSuperiors.remove(id);
		allBaseOverrideGetItemIconInferiors.remove(id);
		allBaseAfterGetItemIconSuperiors.remove(id);
		allBaseAfterGetItemIconInferiors.remove(id);

		beforeGetItemIconHookTypes.remove(id);
		overrideGetItemIconHookTypes.remove(id);
		afterGetItemIconHookTypes.remove(id);

		allBaseBeforeGetSleepTimerSuperiors.remove(id);
		allBaseBeforeGetSleepTimerInferiors.remove(id);
		allBaseOverrideGetSleepTimerSuperiors.remove(id);
		allBaseOverrideGetSleepTimerInferiors.remove(id);
		allBaseAfterGetSleepTimerSuperiors.remove(id);
		allBaseAfterGetSleepTimerInferiors.remove(id);

		beforeGetSleepTimerHookTypes.remove(id);
		overrideGetSleepTimerHookTypes.remove(id);
		afterGetSleepTimerHookTypes.remove(id);

		allBaseBeforeHandleLavaMovementSuperiors.remove(id);
		allBaseBeforeHandleLavaMovementInferiors.remove(id);
		allBaseOverrideHandleLavaMovementSuperiors.remove(id);
		allBaseOverrideHandleLavaMovementInferiors.remove(id);
		allBaseAfterHandleLavaMovementSuperiors.remove(id);
		allBaseAfterHandleLavaMovementInferiors.remove(id);

		beforeHandleLavaMovementHookTypes.remove(id);
		overrideHandleLavaMovementHookTypes.remove(id);
		afterHandleLavaMovementHookTypes.remove(id);

		allBaseBeforeHandleWaterMovementSuperiors.remove(id);
		allBaseBeforeHandleWaterMovementInferiors.remove(id);
		allBaseOverrideHandleWaterMovementSuperiors.remove(id);
		allBaseOverrideHandleWaterMovementInferiors.remove(id);
		allBaseAfterHandleWaterMovementSuperiors.remove(id);
		allBaseAfterHandleWaterMovementInferiors.remove(id);

		beforeHandleWaterMovementHookTypes.remove(id);
		overrideHandleWaterMovementHookTypes.remove(id);
		afterHandleWaterMovementHookTypes.remove(id);

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

		allBaseBeforeIsSprintingSuperiors.remove(id);
		allBaseBeforeIsSprintingInferiors.remove(id);
		allBaseOverrideIsSprintingSuperiors.remove(id);
		allBaseOverrideIsSprintingInferiors.remove(id);
		allBaseAfterIsSprintingSuperiors.remove(id);
		allBaseAfterIsSprintingInferiors.remove(id);

		beforeIsSprintingHookTypes.remove(id);
		overrideIsSprintingHookTypes.remove(id);
		afterIsSprintingHookTypes.remove(id);

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

		allBaseBeforePlayStepSoundSuperiors.remove(id);
		allBaseBeforePlayStepSoundInferiors.remove(id);
		allBaseOverridePlayStepSoundSuperiors.remove(id);
		allBaseOverridePlayStepSoundInferiors.remove(id);
		allBaseAfterPlayStepSoundSuperiors.remove(id);
		allBaseAfterPlayStepSoundInferiors.remove(id);

		beforePlayStepSoundHookTypes.remove(id);
		overridePlayStepSoundHookTypes.remove(id);
		afterPlayStepSoundHookTypes.remove(id);

		allBaseBeforePushOutOfBlocksSuperiors.remove(id);
		allBaseBeforePushOutOfBlocksInferiors.remove(id);
		allBaseOverridePushOutOfBlocksSuperiors.remove(id);
		allBaseOverridePushOutOfBlocksInferiors.remove(id);
		allBaseAfterPushOutOfBlocksSuperiors.remove(id);
		allBaseAfterPushOutOfBlocksInferiors.remove(id);

		beforePushOutOfBlocksHookTypes.remove(id);
		overridePushOutOfBlocksHookTypes.remove(id);
		afterPushOutOfBlocksHookTypes.remove(id);

		allBaseBeforeRayTraceSuperiors.remove(id);
		allBaseBeforeRayTraceInferiors.remove(id);
		allBaseOverrideRayTraceSuperiors.remove(id);
		allBaseOverrideRayTraceInferiors.remove(id);
		allBaseAfterRayTraceSuperiors.remove(id);
		allBaseAfterRayTraceInferiors.remove(id);

		beforeRayTraceHookTypes.remove(id);
		overrideRayTraceHookTypes.remove(id);
		afterRayTraceHookTypes.remove(id);

		allBaseBeforeReadEntityFromNBTSuperiors.remove(id);
		allBaseBeforeReadEntityFromNBTInferiors.remove(id);
		allBaseOverrideReadEntityFromNBTSuperiors.remove(id);
		allBaseOverrideReadEntityFromNBTInferiors.remove(id);
		allBaseAfterReadEntityFromNBTSuperiors.remove(id);
		allBaseAfterReadEntityFromNBTInferiors.remove(id);

		beforeReadEntityFromNBTHookTypes.remove(id);
		overrideReadEntityFromNBTHookTypes.remove(id);
		afterReadEntityFromNBTHookTypes.remove(id);

		allBaseBeforeRespawnPlayerSuperiors.remove(id);
		allBaseBeforeRespawnPlayerInferiors.remove(id);
		allBaseOverrideRespawnPlayerSuperiors.remove(id);
		allBaseOverrideRespawnPlayerInferiors.remove(id);
		allBaseAfterRespawnPlayerSuperiors.remove(id);
		allBaseAfterRespawnPlayerInferiors.remove(id);

		beforeRespawnPlayerHookTypes.remove(id);
		overrideRespawnPlayerHookTypes.remove(id);
		afterRespawnPlayerHookTypes.remove(id);

		allBaseBeforeSetDeadSuperiors.remove(id);
		allBaseBeforeSetDeadInferiors.remove(id);
		allBaseOverrideSetDeadSuperiors.remove(id);
		allBaseOverrideSetDeadInferiors.remove(id);
		allBaseAfterSetDeadSuperiors.remove(id);
		allBaseAfterSetDeadInferiors.remove(id);

		beforeSetDeadHookTypes.remove(id);
		overrideSetDeadHookTypes.remove(id);
		afterSetDeadHookTypes.remove(id);

		allBaseBeforeSetPlayerSPHealthSuperiors.remove(id);
		allBaseBeforeSetPlayerSPHealthInferiors.remove(id);
		allBaseOverrideSetPlayerSPHealthSuperiors.remove(id);
		allBaseOverrideSetPlayerSPHealthInferiors.remove(id);
		allBaseAfterSetPlayerSPHealthSuperiors.remove(id);
		allBaseAfterSetPlayerSPHealthInferiors.remove(id);

		beforeSetPlayerSPHealthHookTypes.remove(id);
		overrideSetPlayerSPHealthHookTypes.remove(id);
		afterSetPlayerSPHealthHookTypes.remove(id);

		allBaseBeforeSetPositionAndRotationSuperiors.remove(id);
		allBaseBeforeSetPositionAndRotationInferiors.remove(id);
		allBaseOverrideSetPositionAndRotationSuperiors.remove(id);
		allBaseOverrideSetPositionAndRotationInferiors.remove(id);
		allBaseAfterSetPositionAndRotationSuperiors.remove(id);
		allBaseAfterSetPositionAndRotationInferiors.remove(id);

		beforeSetPositionAndRotationHookTypes.remove(id);
		overrideSetPositionAndRotationHookTypes.remove(id);
		afterSetPositionAndRotationHookTypes.remove(id);

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

		allBaseBeforeSleepInBedAtSuperiors.remove(id);
		allBaseBeforeSleepInBedAtInferiors.remove(id);
		allBaseOverrideSleepInBedAtSuperiors.remove(id);
		allBaseOverrideSleepInBedAtInferiors.remove(id);
		allBaseAfterSleepInBedAtSuperiors.remove(id);
		allBaseAfterSleepInBedAtInferiors.remove(id);

		beforeSleepInBedAtHookTypes.remove(id);
		overrideSleepInBedAtHookTypes.remove(id);
		afterSleepInBedAtHookTypes.remove(id);

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

		for(IClientPlayerAPI instance : getAllInstancesList())
			instance.getClientPlayerAPI().updateClientPlayerBases();

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

		log("ClientPlayerAPI: unregistered id '" + id + "'");

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
			boolean isOverridden = method.getDeclaringClass() != ClientPlayerBase.class;
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

	public static ClientPlayerAPI create(IClientPlayerAPI clientPlayer)
	{
		if(allBaseConstructors.size() > 0 && !initialized)
			initialize();
		return new ClientPlayerAPI(clientPlayer);
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

		sortBases(beforeAddMovementStatHookTypes, allBaseBeforeAddMovementStatSuperiors, allBaseBeforeAddMovementStatInferiors, "beforeAddMovementStat");
		sortBases(overrideAddMovementStatHookTypes, allBaseOverrideAddMovementStatSuperiors, allBaseOverrideAddMovementStatInferiors, "overrideAddMovementStat");
		sortBases(afterAddMovementStatHookTypes, allBaseAfterAddMovementStatSuperiors, allBaseAfterAddMovementStatInferiors, "afterAddMovementStat");

		sortBases(beforeAddStatHookTypes, allBaseBeforeAddStatSuperiors, allBaseBeforeAddStatInferiors, "beforeAddStat");
		sortBases(overrideAddStatHookTypes, allBaseOverrideAddStatSuperiors, allBaseOverrideAddStatInferiors, "overrideAddStat");
		sortBases(afterAddStatHookTypes, allBaseAfterAddStatSuperiors, allBaseAfterAddStatInferiors, "afterAddStat");

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

		sortBases(beforeCloseScreenHookTypes, allBaseBeforeCloseScreenSuperiors, allBaseBeforeCloseScreenInferiors, "beforeCloseScreen");
		sortBases(overrideCloseScreenHookTypes, allBaseOverrideCloseScreenSuperiors, allBaseOverrideCloseScreenInferiors, "overrideCloseScreen");
		sortBases(afterCloseScreenHookTypes, allBaseAfterCloseScreenSuperiors, allBaseAfterCloseScreenInferiors, "afterCloseScreen");

		sortBases(beforeDamageEntityHookTypes, allBaseBeforeDamageEntitySuperiors, allBaseBeforeDamageEntityInferiors, "beforeDamageEntity");
		sortBases(overrideDamageEntityHookTypes, allBaseOverrideDamageEntitySuperiors, allBaseOverrideDamageEntityInferiors, "overrideDamageEntity");
		sortBases(afterDamageEntityHookTypes, allBaseAfterDamageEntitySuperiors, allBaseAfterDamageEntityInferiors, "afterDamageEntity");

		sortBases(beforeDisplayGUIBrewingStandHookTypes, allBaseBeforeDisplayGUIBrewingStandSuperiors, allBaseBeforeDisplayGUIBrewingStandInferiors, "beforeDisplayGUIBrewingStand");
		sortBases(overrideDisplayGUIBrewingStandHookTypes, allBaseOverrideDisplayGUIBrewingStandSuperiors, allBaseOverrideDisplayGUIBrewingStandInferiors, "overrideDisplayGUIBrewingStand");
		sortBases(afterDisplayGUIBrewingStandHookTypes, allBaseAfterDisplayGUIBrewingStandSuperiors, allBaseAfterDisplayGUIBrewingStandInferiors, "afterDisplayGUIBrewingStand");

		sortBases(beforeDisplayGUIChestHookTypes, allBaseBeforeDisplayGUIChestSuperiors, allBaseBeforeDisplayGUIChestInferiors, "beforeDisplayGUIChest");
		sortBases(overrideDisplayGUIChestHookTypes, allBaseOverrideDisplayGUIChestSuperiors, allBaseOverrideDisplayGUIChestInferiors, "overrideDisplayGUIChest");
		sortBases(afterDisplayGUIChestHookTypes, allBaseAfterDisplayGUIChestSuperiors, allBaseAfterDisplayGUIChestInferiors, "afterDisplayGUIChest");

		sortBases(beforeDisplayGUIDispenserHookTypes, allBaseBeforeDisplayGUIDispenserSuperiors, allBaseBeforeDisplayGUIDispenserInferiors, "beforeDisplayGUIDispenser");
		sortBases(overrideDisplayGUIDispenserHookTypes, allBaseOverrideDisplayGUIDispenserSuperiors, allBaseOverrideDisplayGUIDispenserInferiors, "overrideDisplayGUIDispenser");
		sortBases(afterDisplayGUIDispenserHookTypes, allBaseAfterDisplayGUIDispenserSuperiors, allBaseAfterDisplayGUIDispenserInferiors, "afterDisplayGUIDispenser");

		sortBases(beforeDisplayGUIEditSignHookTypes, allBaseBeforeDisplayGUIEditSignSuperiors, allBaseBeforeDisplayGUIEditSignInferiors, "beforeDisplayGUIEditSign");
		sortBases(overrideDisplayGUIEditSignHookTypes, allBaseOverrideDisplayGUIEditSignSuperiors, allBaseOverrideDisplayGUIEditSignInferiors, "overrideDisplayGUIEditSign");
		sortBases(afterDisplayGUIEditSignHookTypes, allBaseAfterDisplayGUIEditSignSuperiors, allBaseAfterDisplayGUIEditSignInferiors, "afterDisplayGUIEditSign");

		sortBases(beforeDisplayGUIEnchantmentHookTypes, allBaseBeforeDisplayGUIEnchantmentSuperiors, allBaseBeforeDisplayGUIEnchantmentInferiors, "beforeDisplayGUIEnchantment");
		sortBases(overrideDisplayGUIEnchantmentHookTypes, allBaseOverrideDisplayGUIEnchantmentSuperiors, allBaseOverrideDisplayGUIEnchantmentInferiors, "overrideDisplayGUIEnchantment");
		sortBases(afterDisplayGUIEnchantmentHookTypes, allBaseAfterDisplayGUIEnchantmentSuperiors, allBaseAfterDisplayGUIEnchantmentInferiors, "afterDisplayGUIEnchantment");

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

		sortBases(beforeDropPlayerItemWithRandomChoiceHookTypes, allBaseBeforeDropPlayerItemWithRandomChoiceSuperiors, allBaseBeforeDropPlayerItemWithRandomChoiceInferiors, "beforeDropPlayerItemWithRandomChoice");
		sortBases(overrideDropPlayerItemWithRandomChoiceHookTypes, allBaseOverrideDropPlayerItemWithRandomChoiceSuperiors, allBaseOverrideDropPlayerItemWithRandomChoiceInferiors, "overrideDropPlayerItemWithRandomChoice");
		sortBases(afterDropPlayerItemWithRandomChoiceHookTypes, allBaseAfterDropPlayerItemWithRandomChoiceSuperiors, allBaseAfterDropPlayerItemWithRandomChoiceInferiors, "afterDropPlayerItemWithRandomChoice");

		sortBases(beforeFallHookTypes, allBaseBeforeFallSuperiors, allBaseBeforeFallInferiors, "beforeFall");
		sortBases(overrideFallHookTypes, allBaseOverrideFallSuperiors, allBaseOverrideFallInferiors, "overrideFall");
		sortBases(afterFallHookTypes, allBaseAfterFallSuperiors, allBaseAfterFallInferiors, "afterFall");

		sortBases(beforeGetAIMoveSpeedHookTypes, allBaseBeforeGetAIMoveSpeedSuperiors, allBaseBeforeGetAIMoveSpeedInferiors, "beforeGetAIMoveSpeed");
		sortBases(overrideGetAIMoveSpeedHookTypes, allBaseOverrideGetAIMoveSpeedSuperiors, allBaseOverrideGetAIMoveSpeedInferiors, "overrideGetAIMoveSpeed");
		sortBases(afterGetAIMoveSpeedHookTypes, allBaseAfterGetAIMoveSpeedSuperiors, allBaseAfterGetAIMoveSpeedInferiors, "afterGetAIMoveSpeed");

		sortBases(beforeGetBedOrientationInDegreesHookTypes, allBaseBeforeGetBedOrientationInDegreesSuperiors, allBaseBeforeGetBedOrientationInDegreesInferiors, "beforeGetBedOrientationInDegrees");
		sortBases(overrideGetBedOrientationInDegreesHookTypes, allBaseOverrideGetBedOrientationInDegreesSuperiors, allBaseOverrideGetBedOrientationInDegreesInferiors, "overrideGetBedOrientationInDegrees");
		sortBases(afterGetBedOrientationInDegreesHookTypes, allBaseAfterGetBedOrientationInDegreesSuperiors, allBaseAfterGetBedOrientationInDegreesInferiors, "afterGetBedOrientationInDegrees");

		sortBases(beforeGetBrightnessHookTypes, allBaseBeforeGetBrightnessSuperiors, allBaseBeforeGetBrightnessInferiors, "beforeGetBrightness");
		sortBases(overrideGetBrightnessHookTypes, allBaseOverrideGetBrightnessSuperiors, allBaseOverrideGetBrightnessInferiors, "overrideGetBrightness");
		sortBases(afterGetBrightnessHookTypes, allBaseAfterGetBrightnessSuperiors, allBaseAfterGetBrightnessInferiors, "afterGetBrightness");

		sortBases(beforeGetBrightnessForRenderHookTypes, allBaseBeforeGetBrightnessForRenderSuperiors, allBaseBeforeGetBrightnessForRenderInferiors, "beforeGetBrightnessForRender");
		sortBases(overrideGetBrightnessForRenderHookTypes, allBaseOverrideGetBrightnessForRenderSuperiors, allBaseOverrideGetBrightnessForRenderInferiors, "overrideGetBrightnessForRender");
		sortBases(afterGetBrightnessForRenderHookTypes, allBaseAfterGetBrightnessForRenderSuperiors, allBaseAfterGetBrightnessForRenderInferiors, "afterGetBrightnessForRender");

		sortBases(beforeGetCurrentPlayerStrVsBlockHookTypes, allBaseBeforeGetCurrentPlayerStrVsBlockSuperiors, allBaseBeforeGetCurrentPlayerStrVsBlockInferiors, "beforeGetCurrentPlayerStrVsBlock");
		sortBases(overrideGetCurrentPlayerStrVsBlockHookTypes, allBaseOverrideGetCurrentPlayerStrVsBlockSuperiors, allBaseOverrideGetCurrentPlayerStrVsBlockInferiors, "overrideGetCurrentPlayerStrVsBlock");
		sortBases(afterGetCurrentPlayerStrVsBlockHookTypes, allBaseAfterGetCurrentPlayerStrVsBlockSuperiors, allBaseAfterGetCurrentPlayerStrVsBlockInferiors, "afterGetCurrentPlayerStrVsBlock");

		sortBases(beforeGetCurrentPlayerStrVsBlockForgeHookTypes, allBaseBeforeGetCurrentPlayerStrVsBlockForgeSuperiors, allBaseBeforeGetCurrentPlayerStrVsBlockForgeInferiors, "beforeGetCurrentPlayerStrVsBlockForge");
		sortBases(overrideGetCurrentPlayerStrVsBlockForgeHookTypes, allBaseOverrideGetCurrentPlayerStrVsBlockForgeSuperiors, allBaseOverrideGetCurrentPlayerStrVsBlockForgeInferiors, "overrideGetCurrentPlayerStrVsBlockForge");
		sortBases(afterGetCurrentPlayerStrVsBlockForgeHookTypes, allBaseAfterGetCurrentPlayerStrVsBlockForgeSuperiors, allBaseAfterGetCurrentPlayerStrVsBlockForgeInferiors, "afterGetCurrentPlayerStrVsBlockForge");

		sortBases(beforeGetDistanceSqHookTypes, allBaseBeforeGetDistanceSqSuperiors, allBaseBeforeGetDistanceSqInferiors, "beforeGetDistanceSq");
		sortBases(overrideGetDistanceSqHookTypes, allBaseOverrideGetDistanceSqSuperiors, allBaseOverrideGetDistanceSqInferiors, "overrideGetDistanceSq");
		sortBases(afterGetDistanceSqHookTypes, allBaseAfterGetDistanceSqSuperiors, allBaseAfterGetDistanceSqInferiors, "afterGetDistanceSq");

		sortBases(beforeGetDistanceSqToEntityHookTypes, allBaseBeforeGetDistanceSqToEntitySuperiors, allBaseBeforeGetDistanceSqToEntityInferiors, "beforeGetDistanceSqToEntity");
		sortBases(overrideGetDistanceSqToEntityHookTypes, allBaseOverrideGetDistanceSqToEntitySuperiors, allBaseOverrideGetDistanceSqToEntityInferiors, "overrideGetDistanceSqToEntity");
		sortBases(afterGetDistanceSqToEntityHookTypes, allBaseAfterGetDistanceSqToEntitySuperiors, allBaseAfterGetDistanceSqToEntityInferiors, "afterGetDistanceSqToEntity");

		sortBases(beforeGetFOVMultiplierHookTypes, allBaseBeforeGetFOVMultiplierSuperiors, allBaseBeforeGetFOVMultiplierInferiors, "beforeGetFOVMultiplier");
		sortBases(overrideGetFOVMultiplierHookTypes, allBaseOverrideGetFOVMultiplierSuperiors, allBaseOverrideGetFOVMultiplierInferiors, "overrideGetFOVMultiplier");
		sortBases(afterGetFOVMultiplierHookTypes, allBaseAfterGetFOVMultiplierSuperiors, allBaseAfterGetFOVMultiplierInferiors, "afterGetFOVMultiplier");

		sortBases(beforeGetHurtSoundHookTypes, allBaseBeforeGetHurtSoundSuperiors, allBaseBeforeGetHurtSoundInferiors, "beforeGetHurtSound");
		sortBases(overrideGetHurtSoundHookTypes, allBaseOverrideGetHurtSoundSuperiors, allBaseOverrideGetHurtSoundInferiors, "overrideGetHurtSound");
		sortBases(afterGetHurtSoundHookTypes, allBaseAfterGetHurtSoundSuperiors, allBaseAfterGetHurtSoundInferiors, "afterGetHurtSound");

		sortBases(beforeGetItemIconHookTypes, allBaseBeforeGetItemIconSuperiors, allBaseBeforeGetItemIconInferiors, "beforeGetItemIcon");
		sortBases(overrideGetItemIconHookTypes, allBaseOverrideGetItemIconSuperiors, allBaseOverrideGetItemIconInferiors, "overrideGetItemIcon");
		sortBases(afterGetItemIconHookTypes, allBaseAfterGetItemIconSuperiors, allBaseAfterGetItemIconInferiors, "afterGetItemIcon");

		sortBases(beforeGetSleepTimerHookTypes, allBaseBeforeGetSleepTimerSuperiors, allBaseBeforeGetSleepTimerInferiors, "beforeGetSleepTimer");
		sortBases(overrideGetSleepTimerHookTypes, allBaseOverrideGetSleepTimerSuperiors, allBaseOverrideGetSleepTimerInferiors, "overrideGetSleepTimer");
		sortBases(afterGetSleepTimerHookTypes, allBaseAfterGetSleepTimerSuperiors, allBaseAfterGetSleepTimerInferiors, "afterGetSleepTimer");

		sortBases(beforeHandleLavaMovementHookTypes, allBaseBeforeHandleLavaMovementSuperiors, allBaseBeforeHandleLavaMovementInferiors, "beforeHandleLavaMovement");
		sortBases(overrideHandleLavaMovementHookTypes, allBaseOverrideHandleLavaMovementSuperiors, allBaseOverrideHandleLavaMovementInferiors, "overrideHandleLavaMovement");
		sortBases(afterHandleLavaMovementHookTypes, allBaseAfterHandleLavaMovementSuperiors, allBaseAfterHandleLavaMovementInferiors, "afterHandleLavaMovement");

		sortBases(beforeHandleWaterMovementHookTypes, allBaseBeforeHandleWaterMovementSuperiors, allBaseBeforeHandleWaterMovementInferiors, "beforeHandleWaterMovement");
		sortBases(overrideHandleWaterMovementHookTypes, allBaseOverrideHandleWaterMovementSuperiors, allBaseOverrideHandleWaterMovementInferiors, "overrideHandleWaterMovement");
		sortBases(afterHandleWaterMovementHookTypes, allBaseAfterHandleWaterMovementSuperiors, allBaseAfterHandleWaterMovementInferiors, "afterHandleWaterMovement");

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

		sortBases(beforeIsSprintingHookTypes, allBaseBeforeIsSprintingSuperiors, allBaseBeforeIsSprintingInferiors, "beforeIsSprinting");
		sortBases(overrideIsSprintingHookTypes, allBaseOverrideIsSprintingSuperiors, allBaseOverrideIsSprintingInferiors, "overrideIsSprinting");
		sortBases(afterIsSprintingHookTypes, allBaseAfterIsSprintingSuperiors, allBaseAfterIsSprintingInferiors, "afterIsSprinting");

		sortBases(beforeJumpHookTypes, allBaseBeforeJumpSuperiors, allBaseBeforeJumpInferiors, "beforeJump");
		sortBases(overrideJumpHookTypes, allBaseOverrideJumpSuperiors, allBaseOverrideJumpInferiors, "overrideJump");
		sortBases(afterJumpHookTypes, allBaseAfterJumpSuperiors, allBaseAfterJumpInferiors, "afterJump");

		sortBases(beforeKnockBackHookTypes, allBaseBeforeKnockBackSuperiors, allBaseBeforeKnockBackInferiors, "beforeKnockBack");
		sortBases(overrideKnockBackHookTypes, allBaseOverrideKnockBackSuperiors, allBaseOverrideKnockBackInferiors, "overrideKnockBack");
		sortBases(afterKnockBackHookTypes, allBaseAfterKnockBackSuperiors, allBaseAfterKnockBackInferiors, "afterKnockBack");

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

		sortBases(beforePlayStepSoundHookTypes, allBaseBeforePlayStepSoundSuperiors, allBaseBeforePlayStepSoundInferiors, "beforePlayStepSound");
		sortBases(overridePlayStepSoundHookTypes, allBaseOverridePlayStepSoundSuperiors, allBaseOverridePlayStepSoundInferiors, "overridePlayStepSound");
		sortBases(afterPlayStepSoundHookTypes, allBaseAfterPlayStepSoundSuperiors, allBaseAfterPlayStepSoundInferiors, "afterPlayStepSound");

		sortBases(beforePushOutOfBlocksHookTypes, allBaseBeforePushOutOfBlocksSuperiors, allBaseBeforePushOutOfBlocksInferiors, "beforePushOutOfBlocks");
		sortBases(overridePushOutOfBlocksHookTypes, allBaseOverridePushOutOfBlocksSuperiors, allBaseOverridePushOutOfBlocksInferiors, "overridePushOutOfBlocks");
		sortBases(afterPushOutOfBlocksHookTypes, allBaseAfterPushOutOfBlocksSuperiors, allBaseAfterPushOutOfBlocksInferiors, "afterPushOutOfBlocks");

		sortBases(beforeRayTraceHookTypes, allBaseBeforeRayTraceSuperiors, allBaseBeforeRayTraceInferiors, "beforeRayTrace");
		sortBases(overrideRayTraceHookTypes, allBaseOverrideRayTraceSuperiors, allBaseOverrideRayTraceInferiors, "overrideRayTrace");
		sortBases(afterRayTraceHookTypes, allBaseAfterRayTraceSuperiors, allBaseAfterRayTraceInferiors, "afterRayTrace");

		sortBases(beforeReadEntityFromNBTHookTypes, allBaseBeforeReadEntityFromNBTSuperiors, allBaseBeforeReadEntityFromNBTInferiors, "beforeReadEntityFromNBT");
		sortBases(overrideReadEntityFromNBTHookTypes, allBaseOverrideReadEntityFromNBTSuperiors, allBaseOverrideReadEntityFromNBTInferiors, "overrideReadEntityFromNBT");
		sortBases(afterReadEntityFromNBTHookTypes, allBaseAfterReadEntityFromNBTSuperiors, allBaseAfterReadEntityFromNBTInferiors, "afterReadEntityFromNBT");

		sortBases(beforeRespawnPlayerHookTypes, allBaseBeforeRespawnPlayerSuperiors, allBaseBeforeRespawnPlayerInferiors, "beforeRespawnPlayer");
		sortBases(overrideRespawnPlayerHookTypes, allBaseOverrideRespawnPlayerSuperiors, allBaseOverrideRespawnPlayerInferiors, "overrideRespawnPlayer");
		sortBases(afterRespawnPlayerHookTypes, allBaseAfterRespawnPlayerSuperiors, allBaseAfterRespawnPlayerInferiors, "afterRespawnPlayer");

		sortBases(beforeSetDeadHookTypes, allBaseBeforeSetDeadSuperiors, allBaseBeforeSetDeadInferiors, "beforeSetDead");
		sortBases(overrideSetDeadHookTypes, allBaseOverrideSetDeadSuperiors, allBaseOverrideSetDeadInferiors, "overrideSetDead");
		sortBases(afterSetDeadHookTypes, allBaseAfterSetDeadSuperiors, allBaseAfterSetDeadInferiors, "afterSetDead");

		sortBases(beforeSetPlayerSPHealthHookTypes, allBaseBeforeSetPlayerSPHealthSuperiors, allBaseBeforeSetPlayerSPHealthInferiors, "beforeSetPlayerSPHealth");
		sortBases(overrideSetPlayerSPHealthHookTypes, allBaseOverrideSetPlayerSPHealthSuperiors, allBaseOverrideSetPlayerSPHealthInferiors, "overrideSetPlayerSPHealth");
		sortBases(afterSetPlayerSPHealthHookTypes, allBaseAfterSetPlayerSPHealthSuperiors, allBaseAfterSetPlayerSPHealthInferiors, "afterSetPlayerSPHealth");

		sortBases(beforeSetPositionAndRotationHookTypes, allBaseBeforeSetPositionAndRotationSuperiors, allBaseBeforeSetPositionAndRotationInferiors, "beforeSetPositionAndRotation");
		sortBases(overrideSetPositionAndRotationHookTypes, allBaseOverrideSetPositionAndRotationSuperiors, allBaseOverrideSetPositionAndRotationInferiors, "overrideSetPositionAndRotation");
		sortBases(afterSetPositionAndRotationHookTypes, allBaseAfterSetPositionAndRotationSuperiors, allBaseAfterSetPositionAndRotationInferiors, "afterSetPositionAndRotation");

		sortBases(beforeSetSneakingHookTypes, allBaseBeforeSetSneakingSuperiors, allBaseBeforeSetSneakingInferiors, "beforeSetSneaking");
		sortBases(overrideSetSneakingHookTypes, allBaseOverrideSetSneakingSuperiors, allBaseOverrideSetSneakingInferiors, "overrideSetSneaking");
		sortBases(afterSetSneakingHookTypes, allBaseAfterSetSneakingSuperiors, allBaseAfterSetSneakingInferiors, "afterSetSneaking");

		sortBases(beforeSetSprintingHookTypes, allBaseBeforeSetSprintingSuperiors, allBaseBeforeSetSprintingInferiors, "beforeSetSprinting");
		sortBases(overrideSetSprintingHookTypes, allBaseOverrideSetSprintingSuperiors, allBaseOverrideSetSprintingInferiors, "overrideSetSprinting");
		sortBases(afterSetSprintingHookTypes, allBaseAfterSetSprintingSuperiors, allBaseAfterSetSprintingInferiors, "afterSetSprinting");

		sortBases(beforeSleepInBedAtHookTypes, allBaseBeforeSleepInBedAtSuperiors, allBaseBeforeSleepInBedAtInferiors, "beforeSleepInBedAt");
		sortBases(overrideSleepInBedAtHookTypes, allBaseOverrideSleepInBedAtSuperiors, allBaseOverrideSleepInBedAtInferiors, "overrideSleepInBedAt");
		sortBases(afterSleepInBedAtHookTypes, allBaseAfterSleepInBedAtSuperiors, allBaseAfterSleepInBedAtInferiors, "afterSleepInBedAt");

		sortBases(beforeSwingItemHookTypes, allBaseBeforeSwingItemSuperiors, allBaseBeforeSwingItemInferiors, "beforeSwingItem");
		sortBases(overrideSwingItemHookTypes, allBaseOverrideSwingItemSuperiors, allBaseOverrideSwingItemInferiors, "overrideSwingItem");
		sortBases(afterSwingItemHookTypes, allBaseAfterSwingItemSuperiors, allBaseAfterSwingItemInferiors, "afterSwingItem");

		sortBases(beforeUpdateEntityActionStateHookTypes, allBaseBeforeUpdateEntityActionStateSuperiors, allBaseBeforeUpdateEntityActionStateInferiors, "beforeUpdateEntityActionState");
		sortBases(overrideUpdateEntityActionStateHookTypes, allBaseOverrideUpdateEntityActionStateSuperiors, allBaseOverrideUpdateEntityActionStateInferiors, "overrideUpdateEntityActionState");
		sortBases(afterUpdateEntityActionStateHookTypes, allBaseAfterUpdateEntityActionStateSuperiors, allBaseAfterUpdateEntityActionStateInferiors, "afterUpdateEntityActionState");

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

	private static List<IClientPlayerAPI> getAllInstancesList()
	{
		List<IClientPlayerAPI> result = new ArrayList<IClientPlayerAPI>();
		Object player;
		try
		{
			Object minecraft = net.minecraft.client.Minecraft.class.getMethod("func_71410_x").invoke(null);
			player = minecraft != null ? net.minecraft.client.Minecraft.class.getField("field_71439_g").get(minecraft) : null;
		}
		catch(Exception obfuscatedException)
		{
			try
			{
				Object minecraft = net.minecraft.client.Minecraft.class.getMethod("getMinecraft").invoke(null);
				player = minecraft != null ? net.minecraft.client.Minecraft.class.getField("thePlayer").get(minecraft) : null;
			}
			catch(Exception deobfuscatedException)
			{
				throw new RuntimeException("Unable to aquire list of current server players.", obfuscatedException);
			}
		}
		if(player != null)
			result.add((IClientPlayerAPI)player);
		return result;
	}

	public static net.minecraft.client.entity.EntityPlayerSP[] getAllInstances()
	{
		List<IClientPlayerAPI> allInstances = getAllInstancesList();
		return allInstances.toArray(new net.minecraft.client.entity.EntityPlayerSP[allInstances.size()]);
	}

	public static void beforeLocalConstructing(IClientPlayerAPI clientPlayer, net.minecraft.client.Minecraft paramMinecraft, net.minecraft.world.World paramWorld, net.minecraft.util.Session paramSession, int paramInt)
	{
		ClientPlayerAPI clientPlayerAPI = clientPlayer.getClientPlayerAPI();
		if(clientPlayerAPI != null)
			clientPlayerAPI.load();

		if(clientPlayerAPI != null)
			clientPlayerAPI.beforeLocalConstructing(paramMinecraft, paramWorld, paramSession, paramInt);
	}

	public static void afterLocalConstructing(IClientPlayerAPI clientPlayer, net.minecraft.client.Minecraft paramMinecraft, net.minecraft.world.World paramWorld, net.minecraft.util.Session paramSession, int paramInt)
	{
		ClientPlayerAPI clientPlayerAPI = clientPlayer.getClientPlayerAPI();
		if(clientPlayerAPI != null)
			clientPlayerAPI.afterLocalConstructing(paramMinecraft, paramWorld, paramSession, paramInt);
	}

	public static ClientPlayerBase getClientPlayerBase(IClientPlayerAPI clientPlayer, String baseId)
	{
		ClientPlayerAPI clientPlayerAPI = clientPlayer.getClientPlayerAPI();
		if(clientPlayerAPI != null)
			return clientPlayerAPI.getClientPlayerBase(baseId);
		return null;
	}

	public static Set<String> getClientPlayerBaseIds(IClientPlayerAPI clientPlayer)
	{
		ClientPlayerAPI clientPlayerAPI = clientPlayer.getClientPlayerAPI();
		Set<String> result = null;
		if(clientPlayerAPI != null)
			result = clientPlayerAPI.getClientPlayerBaseIds();
		else
			result = Collections.<String>emptySet();
		return result;
	}

	public static Object dynamic(IClientPlayerAPI clientPlayer, String key, Object[] parameters)
	{
		ClientPlayerAPI clientPlayerAPI = clientPlayer.getClientPlayerAPI();
		if(clientPlayerAPI != null)
			return clientPlayerAPI.dynamic(key, parameters);
		return null;
	}

	private static void sortBases(List<String> list, Map<String, String[]> allBaseSuperiors, Map<String, String[]> allBaseInferiors, String methodName)
	{
		new ClientPlayerBaseSorter(list, allBaseSuperiors, allBaseInferiors, methodName).Sort();
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

	private ClientPlayerAPI(IClientPlayerAPI player)
	{
		this.player = player;
	}

	private void load()
	{
		Iterator<String> iterator = allBaseConstructors.keySet().iterator();
		while(iterator.hasNext())
		{
			String id = iterator.next();
			ClientPlayerBase toAttach = createClientPlayerBase(id);
			toAttach.beforeBaseAttach(false);
			allBaseObjects.put(id, toAttach);
			baseObjectsToId.put(toAttach, id);
		}

		beforeLocalConstructingHooks = create(beforeLocalConstructingHookTypes);
		afterLocalConstructingHooks = create(afterLocalConstructingHookTypes);

		updateClientPlayerBases();

		iterator = allBaseObjects.keySet().iterator();
		while(iterator.hasNext())
			allBaseObjects.get(iterator.next()).afterBaseAttach(false);
	}

	private ClientPlayerBase createClientPlayerBase(String id)
	{
		Constructor<?> contructor = allBaseConstructors.get(id);

		ClientPlayerBase base;
		try
		{
			if(contructor.getParameterTypes().length == 1)
				base = (ClientPlayerBase)contructor.newInstance(this);
			else
				base = (ClientPlayerBase)contructor.newInstance(this, id);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Exception while creating a ClientPlayerBase of type '" + contructor.getDeclaringClass() + "'", e);
		}
		return base;
	}

	private void updateClientPlayerBases()
	{
		beforeAddExhaustionHooks = create(beforeAddExhaustionHookTypes);
		overrideAddExhaustionHooks = create(overrideAddExhaustionHookTypes);
		afterAddExhaustionHooks = create(afterAddExhaustionHookTypes);
		isAddExhaustionModded =
			beforeAddExhaustionHooks != null ||
			overrideAddExhaustionHooks != null ||
			afterAddExhaustionHooks != null;

		beforeAddMovementStatHooks = create(beforeAddMovementStatHookTypes);
		overrideAddMovementStatHooks = create(overrideAddMovementStatHookTypes);
		afterAddMovementStatHooks = create(afterAddMovementStatHookTypes);
		isAddMovementStatModded =
			beforeAddMovementStatHooks != null ||
			overrideAddMovementStatHooks != null ||
			afterAddMovementStatHooks != null;

		beforeAddStatHooks = create(beforeAddStatHookTypes);
		overrideAddStatHooks = create(overrideAddStatHookTypes);
		afterAddStatHooks = create(afterAddStatHookTypes);
		isAddStatModded =
			beforeAddStatHooks != null ||
			overrideAddStatHooks != null ||
			afterAddStatHooks != null;

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

		beforeCloseScreenHooks = create(beforeCloseScreenHookTypes);
		overrideCloseScreenHooks = create(overrideCloseScreenHookTypes);
		afterCloseScreenHooks = create(afterCloseScreenHookTypes);
		isCloseScreenModded =
			beforeCloseScreenHooks != null ||
			overrideCloseScreenHooks != null ||
			afterCloseScreenHooks != null;

		beforeDamageEntityHooks = create(beforeDamageEntityHookTypes);
		overrideDamageEntityHooks = create(overrideDamageEntityHookTypes);
		afterDamageEntityHooks = create(afterDamageEntityHookTypes);
		isDamageEntityModded =
			beforeDamageEntityHooks != null ||
			overrideDamageEntityHooks != null ||
			afterDamageEntityHooks != null;

		beforeDisplayGUIBrewingStandHooks = create(beforeDisplayGUIBrewingStandHookTypes);
		overrideDisplayGUIBrewingStandHooks = create(overrideDisplayGUIBrewingStandHookTypes);
		afterDisplayGUIBrewingStandHooks = create(afterDisplayGUIBrewingStandHookTypes);
		isDisplayGUIBrewingStandModded =
			beforeDisplayGUIBrewingStandHooks != null ||
			overrideDisplayGUIBrewingStandHooks != null ||
			afterDisplayGUIBrewingStandHooks != null;

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

		beforeDisplayGUIEditSignHooks = create(beforeDisplayGUIEditSignHookTypes);
		overrideDisplayGUIEditSignHooks = create(overrideDisplayGUIEditSignHookTypes);
		afterDisplayGUIEditSignHooks = create(afterDisplayGUIEditSignHookTypes);
		isDisplayGUIEditSignModded =
			beforeDisplayGUIEditSignHooks != null ||
			overrideDisplayGUIEditSignHooks != null ||
			afterDisplayGUIEditSignHooks != null;

		beforeDisplayGUIEnchantmentHooks = create(beforeDisplayGUIEnchantmentHookTypes);
		overrideDisplayGUIEnchantmentHooks = create(overrideDisplayGUIEnchantmentHookTypes);
		afterDisplayGUIEnchantmentHooks = create(afterDisplayGUIEnchantmentHookTypes);
		isDisplayGUIEnchantmentModded =
			beforeDisplayGUIEnchantmentHooks != null ||
			overrideDisplayGUIEnchantmentHooks != null ||
			afterDisplayGUIEnchantmentHooks != null;

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

		beforeDropPlayerItemWithRandomChoiceHooks = create(beforeDropPlayerItemWithRandomChoiceHookTypes);
		overrideDropPlayerItemWithRandomChoiceHooks = create(overrideDropPlayerItemWithRandomChoiceHookTypes);
		afterDropPlayerItemWithRandomChoiceHooks = create(afterDropPlayerItemWithRandomChoiceHookTypes);
		isDropPlayerItemWithRandomChoiceModded =
			beforeDropPlayerItemWithRandomChoiceHooks != null ||
			overrideDropPlayerItemWithRandomChoiceHooks != null ||
			afterDropPlayerItemWithRandomChoiceHooks != null;

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

		beforeGetBedOrientationInDegreesHooks = create(beforeGetBedOrientationInDegreesHookTypes);
		overrideGetBedOrientationInDegreesHooks = create(overrideGetBedOrientationInDegreesHookTypes);
		afterGetBedOrientationInDegreesHooks = create(afterGetBedOrientationInDegreesHookTypes);
		isGetBedOrientationInDegreesModded =
			beforeGetBedOrientationInDegreesHooks != null ||
			overrideGetBedOrientationInDegreesHooks != null ||
			afterGetBedOrientationInDegreesHooks != null;

		beforeGetBrightnessHooks = create(beforeGetBrightnessHookTypes);
		overrideGetBrightnessHooks = create(overrideGetBrightnessHookTypes);
		afterGetBrightnessHooks = create(afterGetBrightnessHookTypes);
		isGetBrightnessModded =
			beforeGetBrightnessHooks != null ||
			overrideGetBrightnessHooks != null ||
			afterGetBrightnessHooks != null;

		beforeGetBrightnessForRenderHooks = create(beforeGetBrightnessForRenderHookTypes);
		overrideGetBrightnessForRenderHooks = create(overrideGetBrightnessForRenderHookTypes);
		afterGetBrightnessForRenderHooks = create(afterGetBrightnessForRenderHookTypes);
		isGetBrightnessForRenderModded =
			beforeGetBrightnessForRenderHooks != null ||
			overrideGetBrightnessForRenderHooks != null ||
			afterGetBrightnessForRenderHooks != null;

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

		beforeGetDistanceSqToEntityHooks = create(beforeGetDistanceSqToEntityHookTypes);
		overrideGetDistanceSqToEntityHooks = create(overrideGetDistanceSqToEntityHookTypes);
		afterGetDistanceSqToEntityHooks = create(afterGetDistanceSqToEntityHookTypes);
		isGetDistanceSqToEntityModded =
			beforeGetDistanceSqToEntityHooks != null ||
			overrideGetDistanceSqToEntityHooks != null ||
			afterGetDistanceSqToEntityHooks != null;

		beforeGetFOVMultiplierHooks = create(beforeGetFOVMultiplierHookTypes);
		overrideGetFOVMultiplierHooks = create(overrideGetFOVMultiplierHookTypes);
		afterGetFOVMultiplierHooks = create(afterGetFOVMultiplierHookTypes);
		isGetFOVMultiplierModded =
			beforeGetFOVMultiplierHooks != null ||
			overrideGetFOVMultiplierHooks != null ||
			afterGetFOVMultiplierHooks != null;

		beforeGetHurtSoundHooks = create(beforeGetHurtSoundHookTypes);
		overrideGetHurtSoundHooks = create(overrideGetHurtSoundHookTypes);
		afterGetHurtSoundHooks = create(afterGetHurtSoundHookTypes);
		isGetHurtSoundModded =
			beforeGetHurtSoundHooks != null ||
			overrideGetHurtSoundHooks != null ||
			afterGetHurtSoundHooks != null;

		beforeGetItemIconHooks = create(beforeGetItemIconHookTypes);
		overrideGetItemIconHooks = create(overrideGetItemIconHookTypes);
		afterGetItemIconHooks = create(afterGetItemIconHookTypes);
		isGetItemIconModded =
			beforeGetItemIconHooks != null ||
			overrideGetItemIconHooks != null ||
			afterGetItemIconHooks != null;

		beforeGetSleepTimerHooks = create(beforeGetSleepTimerHookTypes);
		overrideGetSleepTimerHooks = create(overrideGetSleepTimerHookTypes);
		afterGetSleepTimerHooks = create(afterGetSleepTimerHookTypes);
		isGetSleepTimerModded =
			beforeGetSleepTimerHooks != null ||
			overrideGetSleepTimerHooks != null ||
			afterGetSleepTimerHooks != null;

		beforeHandleLavaMovementHooks = create(beforeHandleLavaMovementHookTypes);
		overrideHandleLavaMovementHooks = create(overrideHandleLavaMovementHookTypes);
		afterHandleLavaMovementHooks = create(afterHandleLavaMovementHookTypes);
		isHandleLavaMovementModded =
			beforeHandleLavaMovementHooks != null ||
			overrideHandleLavaMovementHooks != null ||
			afterHandleLavaMovementHooks != null;

		beforeHandleWaterMovementHooks = create(beforeHandleWaterMovementHookTypes);
		overrideHandleWaterMovementHooks = create(overrideHandleWaterMovementHookTypes);
		afterHandleWaterMovementHooks = create(afterHandleWaterMovementHookTypes);
		isHandleWaterMovementModded =
			beforeHandleWaterMovementHooks != null ||
			overrideHandleWaterMovementHooks != null ||
			afterHandleWaterMovementHooks != null;

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

		beforeIsSprintingHooks = create(beforeIsSprintingHookTypes);
		overrideIsSprintingHooks = create(overrideIsSprintingHookTypes);
		afterIsSprintingHooks = create(afterIsSprintingHookTypes);
		isIsSprintingModded =
			beforeIsSprintingHooks != null ||
			overrideIsSprintingHooks != null ||
			afterIsSprintingHooks != null;

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

		beforePlayStepSoundHooks = create(beforePlayStepSoundHookTypes);
		overridePlayStepSoundHooks = create(overridePlayStepSoundHookTypes);
		afterPlayStepSoundHooks = create(afterPlayStepSoundHookTypes);
		isPlayStepSoundModded =
			beforePlayStepSoundHooks != null ||
			overridePlayStepSoundHooks != null ||
			afterPlayStepSoundHooks != null;

		beforePushOutOfBlocksHooks = create(beforePushOutOfBlocksHookTypes);
		overridePushOutOfBlocksHooks = create(overridePushOutOfBlocksHookTypes);
		afterPushOutOfBlocksHooks = create(afterPushOutOfBlocksHookTypes);
		isPushOutOfBlocksModded =
			beforePushOutOfBlocksHooks != null ||
			overridePushOutOfBlocksHooks != null ||
			afterPushOutOfBlocksHooks != null;

		beforeRayTraceHooks = create(beforeRayTraceHookTypes);
		overrideRayTraceHooks = create(overrideRayTraceHookTypes);
		afterRayTraceHooks = create(afterRayTraceHookTypes);
		isRayTraceModded =
			beforeRayTraceHooks != null ||
			overrideRayTraceHooks != null ||
			afterRayTraceHooks != null;

		beforeReadEntityFromNBTHooks = create(beforeReadEntityFromNBTHookTypes);
		overrideReadEntityFromNBTHooks = create(overrideReadEntityFromNBTHookTypes);
		afterReadEntityFromNBTHooks = create(afterReadEntityFromNBTHookTypes);
		isReadEntityFromNBTModded =
			beforeReadEntityFromNBTHooks != null ||
			overrideReadEntityFromNBTHooks != null ||
			afterReadEntityFromNBTHooks != null;

		beforeRespawnPlayerHooks = create(beforeRespawnPlayerHookTypes);
		overrideRespawnPlayerHooks = create(overrideRespawnPlayerHookTypes);
		afterRespawnPlayerHooks = create(afterRespawnPlayerHookTypes);
		isRespawnPlayerModded =
			beforeRespawnPlayerHooks != null ||
			overrideRespawnPlayerHooks != null ||
			afterRespawnPlayerHooks != null;

		beforeSetDeadHooks = create(beforeSetDeadHookTypes);
		overrideSetDeadHooks = create(overrideSetDeadHookTypes);
		afterSetDeadHooks = create(afterSetDeadHookTypes);
		isSetDeadModded =
			beforeSetDeadHooks != null ||
			overrideSetDeadHooks != null ||
			afterSetDeadHooks != null;

		beforeSetPlayerSPHealthHooks = create(beforeSetPlayerSPHealthHookTypes);
		overrideSetPlayerSPHealthHooks = create(overrideSetPlayerSPHealthHookTypes);
		afterSetPlayerSPHealthHooks = create(afterSetPlayerSPHealthHookTypes);
		isSetPlayerSPHealthModded =
			beforeSetPlayerSPHealthHooks != null ||
			overrideSetPlayerSPHealthHooks != null ||
			afterSetPlayerSPHealthHooks != null;

		beforeSetPositionAndRotationHooks = create(beforeSetPositionAndRotationHookTypes);
		overrideSetPositionAndRotationHooks = create(overrideSetPositionAndRotationHookTypes);
		afterSetPositionAndRotationHooks = create(afterSetPositionAndRotationHookTypes);
		isSetPositionAndRotationModded =
			beforeSetPositionAndRotationHooks != null ||
			overrideSetPositionAndRotationHooks != null ||
			afterSetPositionAndRotationHooks != null;

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

		beforeSleepInBedAtHooks = create(beforeSleepInBedAtHookTypes);
		overrideSleepInBedAtHooks = create(overrideSleepInBedAtHookTypes);
		afterSleepInBedAtHooks = create(afterSleepInBedAtHookTypes);
		isSleepInBedAtModded =
			beforeSleepInBedAtHooks != null ||
			overrideSleepInBedAtHooks != null ||
			afterSleepInBedAtHooks != null;

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

	private void attachClientPlayerBase(String id)
	{
        ClientPlayerBase toAttach = createClientPlayerBase(id);
		toAttach.beforeBaseAttach(true);
		allBaseObjects.put(id, toAttach);
		updateClientPlayerBases();
		toAttach.afterBaseAttach(true);
	}

	private void detachClientPlayerBase(String id)
	{
		ClientPlayerBase toDetach = allBaseObjects.get(id);
		toDetach.beforeBaseDetach(true);
		allBaseObjects.remove(id);
		toDetach.afterBaseDetach(true);
	}

	private ClientPlayerBase[] create(List<String> types)
	{
		if(types.isEmpty())
			return null;

		ClientPlayerBase[] result = new ClientPlayerBase[types.size()];
		for(int i = 0; i < result.length; i++)
			result[i] = getClientPlayerBase(types.get(i));
		return result;
	}

	private void beforeLocalConstructing(net.minecraft.client.Minecraft paramMinecraft, net.minecraft.world.World paramWorld, net.minecraft.util.Session paramSession, int paramInt)
	{
		if(beforeLocalConstructingHooks != null)
			for(int i = beforeLocalConstructingHooks.length - 1; i >= 0 ; i--)
				beforeLocalConstructingHooks[i].beforeLocalConstructing(paramMinecraft, paramWorld, paramSession, paramInt);
		beforeLocalConstructingHooks = null;
	}

	private void afterLocalConstructing(net.minecraft.client.Minecraft paramMinecraft, net.minecraft.world.World paramWorld, net.minecraft.util.Session paramSession, int paramInt)
	{
		if(afterLocalConstructingHooks != null)
			for(int i = 0; i < afterLocalConstructingHooks.length; i++)
				afterLocalConstructingHooks[i].afterLocalConstructing(paramMinecraft, paramWorld, paramSession, paramInt);
		afterLocalConstructingHooks = null;
	}

	public ClientPlayerBase getClientPlayerBase(String id)
	{
		return allBaseObjects.get(id);
	}

	public Set<String> getClientPlayerBaseIds()
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

	public Object dynamicOverwritten(String key, Object[] parameters, ClientPlayerBase overwriter)
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

		return execute(getClientPlayerBase(id), method, parameters);
	}

	private void executeAll(String key, Object[] parameters, Map<String, List<String>> dynamicHookTypes, Map<Class<?>, Map<String, Method>> dynamicHookMethods, boolean reverse)
	{
		List<String> beforeIds = dynamicHookTypes.get(key);
		if(beforeIds == null)
			return;

		for(int i= reverse ? beforeIds.size() - 1 : 0; reverse ? i >= 0 : i < beforeIds.size(); i = i + (reverse ? -1 : 1))
		{
			String id = beforeIds.get(i);
			ClientPlayerBase base = getClientPlayerBase(id);
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

	private Object execute(ClientPlayerBase base, Method method, Object[] parameters)
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

	public static void addExhaustion(IClientPlayerAPI target, float paramFloat)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isAddExhaustionModded)
			clientPlayerAPI.addExhaustion(paramFloat);
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

	protected ClientPlayerBase GetOverwrittenAddExhaustion(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeAddExhaustionHooks;
	private ClientPlayerBase[] overrideAddExhaustionHooks;
	private ClientPlayerBase[] afterAddExhaustionHooks;

	public boolean isAddExhaustionModded;

	private static final Map<String, String[]> allBaseBeforeAddExhaustionSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeAddExhaustionInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddExhaustionSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddExhaustionInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddExhaustionSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddExhaustionInferiors = new Hashtable<String, String[]>(0);

	public static void addMovementStat(IClientPlayerAPI target, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isAddMovementStatModded)
			clientPlayerAPI.addMovementStat(paramDouble1, paramDouble2, paramDouble3);
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

	protected ClientPlayerBase GetOverwrittenAddMovementStat(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeAddMovementStatHooks;
	private ClientPlayerBase[] overrideAddMovementStatHooks;
	private ClientPlayerBase[] afterAddMovementStatHooks;

	public boolean isAddMovementStatModded;

	private static final Map<String, String[]> allBaseBeforeAddMovementStatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeAddMovementStatInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddMovementStatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddMovementStatInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddMovementStatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddMovementStatInferiors = new Hashtable<String, String[]>(0);

	public static void addStat(IClientPlayerAPI target, net.minecraft.stats.StatBase paramStatBase, int paramInt)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isAddStatModded)
			clientPlayerAPI.addStat(paramStatBase, paramInt);
		else
			target.localAddStat(paramStatBase, paramInt);
	}

	private void addStat(net.minecraft.stats.StatBase paramStatBase, int paramInt)
	{
		if(beforeAddStatHooks != null)
			for(int i = beforeAddStatHooks.length - 1; i >= 0 ; i--)
				beforeAddStatHooks[i].beforeAddStat(paramStatBase, paramInt);

		if(overrideAddStatHooks != null)
			overrideAddStatHooks[overrideAddStatHooks.length - 1].addStat(paramStatBase, paramInt);
		else
			player.localAddStat(paramStatBase, paramInt);

		if(afterAddStatHooks != null)
			for(int i = 0; i < afterAddStatHooks.length; i++)
				afterAddStatHooks[i].afterAddStat(paramStatBase, paramInt);

	}

	protected ClientPlayerBase GetOverwrittenAddStat(ClientPlayerBase overWriter)
	{
		if (overrideAddStatHooks == null)
			return overWriter;

		for(int i = 0; i < overrideAddStatHooks.length; i++)
			if(overrideAddStatHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideAddStatHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeAddStatHookTypes = new LinkedList<String>();
	private final static List<String> overrideAddStatHookTypes = new LinkedList<String>();
	private final static List<String> afterAddStatHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeAddStatHooks;
	private ClientPlayerBase[] overrideAddStatHooks;
	private ClientPlayerBase[] afterAddStatHooks;

	public boolean isAddStatModded;

	private static final Map<String, String[]> allBaseBeforeAddStatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeAddStatInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddStatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAddStatInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddStatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAddStatInferiors = new Hashtable<String, String[]>(0);

	public static boolean attackEntityFrom(IClientPlayerAPI target, net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isAttackEntityFromModded)
			_result = clientPlayerAPI.attackEntityFrom(paramDamageSource, paramFloat);
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

	protected ClientPlayerBase GetOverwrittenAttackEntityFrom(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeAttackEntityFromHooks;
	private ClientPlayerBase[] overrideAttackEntityFromHooks;
	private ClientPlayerBase[] afterAttackEntityFromHooks;

	public boolean isAttackEntityFromModded;

	private static final Map<String, String[]> allBaseBeforeAttackEntityFromSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeAttackEntityFromInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAttackEntityFromSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAttackEntityFromInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAttackEntityFromSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAttackEntityFromInferiors = new Hashtable<String, String[]>(0);

	public static void attackTargetEntityWithCurrentItem(IClientPlayerAPI target, net.minecraft.entity.Entity paramEntity)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isAttackTargetEntityWithCurrentItemModded)
			clientPlayerAPI.attackTargetEntityWithCurrentItem(paramEntity);
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

	protected ClientPlayerBase GetOverwrittenAttackTargetEntityWithCurrentItem(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeAttackTargetEntityWithCurrentItemHooks;
	private ClientPlayerBase[] overrideAttackTargetEntityWithCurrentItemHooks;
	private ClientPlayerBase[] afterAttackTargetEntityWithCurrentItemHooks;

	public boolean isAttackTargetEntityWithCurrentItemModded;

	private static final Map<String, String[]> allBaseBeforeAttackTargetEntityWithCurrentItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeAttackTargetEntityWithCurrentItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAttackTargetEntityWithCurrentItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideAttackTargetEntityWithCurrentItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAttackTargetEntityWithCurrentItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterAttackTargetEntityWithCurrentItemInferiors = new Hashtable<String, String[]>(0);

	public static boolean canBreatheUnderwater(IClientPlayerAPI target)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isCanBreatheUnderwaterModded)
			_result = clientPlayerAPI.canBreatheUnderwater();
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

	protected ClientPlayerBase GetOverwrittenCanBreatheUnderwater(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeCanBreatheUnderwaterHooks;
	private ClientPlayerBase[] overrideCanBreatheUnderwaterHooks;
	private ClientPlayerBase[] afterCanBreatheUnderwaterHooks;

	public boolean isCanBreatheUnderwaterModded;

	private static final Map<String, String[]> allBaseBeforeCanBreatheUnderwaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeCanBreatheUnderwaterInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanBreatheUnderwaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanBreatheUnderwaterInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanBreatheUnderwaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanBreatheUnderwaterInferiors = new Hashtable<String, String[]>(0);

	public static boolean canHarvestBlock(IClientPlayerAPI target, net.minecraft.block.Block paramBlock)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isCanHarvestBlockModded)
			_result = clientPlayerAPI.canHarvestBlock(paramBlock);
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

	protected ClientPlayerBase GetOverwrittenCanHarvestBlock(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeCanHarvestBlockHooks;
	private ClientPlayerBase[] overrideCanHarvestBlockHooks;
	private ClientPlayerBase[] afterCanHarvestBlockHooks;

	public boolean isCanHarvestBlockModded;

	private static final Map<String, String[]> allBaseBeforeCanHarvestBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeCanHarvestBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanHarvestBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanHarvestBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanHarvestBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanHarvestBlockInferiors = new Hashtable<String, String[]>(0);

	public static boolean canPlayerEdit(IClientPlayerAPI target, int paramInt1, int paramInt2, int paramInt3, int paramInt4, net.minecraft.item.ItemStack paramItemStack)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isCanPlayerEditModded)
			_result = clientPlayerAPI.canPlayerEdit(paramInt1, paramInt2, paramInt3, paramInt4, paramItemStack);
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

	protected ClientPlayerBase GetOverwrittenCanPlayerEdit(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeCanPlayerEditHooks;
	private ClientPlayerBase[] overrideCanPlayerEditHooks;
	private ClientPlayerBase[] afterCanPlayerEditHooks;

	public boolean isCanPlayerEditModded;

	private static final Map<String, String[]> allBaseBeforeCanPlayerEditSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeCanPlayerEditInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanPlayerEditSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanPlayerEditInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanPlayerEditSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanPlayerEditInferiors = new Hashtable<String, String[]>(0);

	public static boolean canTriggerWalking(IClientPlayerAPI target)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isCanTriggerWalkingModded)
			_result = clientPlayerAPI.canTriggerWalking();
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

	protected ClientPlayerBase GetOverwrittenCanTriggerWalking(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeCanTriggerWalkingHooks;
	private ClientPlayerBase[] overrideCanTriggerWalkingHooks;
	private ClientPlayerBase[] afterCanTriggerWalkingHooks;

	public boolean isCanTriggerWalkingModded;

	private static final Map<String, String[]> allBaseBeforeCanTriggerWalkingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeCanTriggerWalkingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanTriggerWalkingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCanTriggerWalkingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanTriggerWalkingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCanTriggerWalkingInferiors = new Hashtable<String, String[]>(0);

	public static void closeScreen(IClientPlayerAPI target)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isCloseScreenModded)
			clientPlayerAPI.closeScreen();
		else
			target.localCloseScreen();
	}

	private void closeScreen()
	{
		if(beforeCloseScreenHooks != null)
			for(int i = beforeCloseScreenHooks.length - 1; i >= 0 ; i--)
				beforeCloseScreenHooks[i].beforeCloseScreen();

		if(overrideCloseScreenHooks != null)
			overrideCloseScreenHooks[overrideCloseScreenHooks.length - 1].closeScreen();
		else
			player.localCloseScreen();

		if(afterCloseScreenHooks != null)
			for(int i = 0; i < afterCloseScreenHooks.length; i++)
				afterCloseScreenHooks[i].afterCloseScreen();

	}

	protected ClientPlayerBase GetOverwrittenCloseScreen(ClientPlayerBase overWriter)
	{
		if (overrideCloseScreenHooks == null)
			return overWriter;

		for(int i = 0; i < overrideCloseScreenHooks.length; i++)
			if(overrideCloseScreenHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideCloseScreenHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeCloseScreenHookTypes = new LinkedList<String>();
	private final static List<String> overrideCloseScreenHookTypes = new LinkedList<String>();
	private final static List<String> afterCloseScreenHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeCloseScreenHooks;
	private ClientPlayerBase[] overrideCloseScreenHooks;
	private ClientPlayerBase[] afterCloseScreenHooks;

	public boolean isCloseScreenModded;

	private static final Map<String, String[]> allBaseBeforeCloseScreenSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeCloseScreenInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCloseScreenSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideCloseScreenInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCloseScreenSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterCloseScreenInferiors = new Hashtable<String, String[]>(0);

	public static void damageEntity(IClientPlayerAPI target, net.minecraft.util.DamageSource paramDamageSource, float paramFloat)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isDamageEntityModded)
			clientPlayerAPI.damageEntity(paramDamageSource, paramFloat);
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

	protected ClientPlayerBase GetOverwrittenDamageEntity(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeDamageEntityHooks;
	private ClientPlayerBase[] overrideDamageEntityHooks;
	private ClientPlayerBase[] afterDamageEntityHooks;

	public boolean isDamageEntityModded;

	private static final Map<String, String[]> allBaseBeforeDamageEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDamageEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDamageEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDamageEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDamageEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDamageEntityInferiors = new Hashtable<String, String[]>(0);

	public static void displayGUIBrewingStand(IClientPlayerAPI target, net.minecraft.tileentity.TileEntityBrewingStand paramTileEntityBrewingStand)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isDisplayGUIBrewingStandModded)
			clientPlayerAPI.displayGUIBrewingStand(paramTileEntityBrewingStand);
		else
			target.localDisplayGUIBrewingStand(paramTileEntityBrewingStand);
	}

	private void displayGUIBrewingStand(net.minecraft.tileentity.TileEntityBrewingStand paramTileEntityBrewingStand)
	{
		if(beforeDisplayGUIBrewingStandHooks != null)
			for(int i = beforeDisplayGUIBrewingStandHooks.length - 1; i >= 0 ; i--)
				beforeDisplayGUIBrewingStandHooks[i].beforeDisplayGUIBrewingStand(paramTileEntityBrewingStand);

		if(overrideDisplayGUIBrewingStandHooks != null)
			overrideDisplayGUIBrewingStandHooks[overrideDisplayGUIBrewingStandHooks.length - 1].displayGUIBrewingStand(paramTileEntityBrewingStand);
		else
			player.localDisplayGUIBrewingStand(paramTileEntityBrewingStand);

		if(afterDisplayGUIBrewingStandHooks != null)
			for(int i = 0; i < afterDisplayGUIBrewingStandHooks.length; i++)
				afterDisplayGUIBrewingStandHooks[i].afterDisplayGUIBrewingStand(paramTileEntityBrewingStand);

	}

	protected ClientPlayerBase GetOverwrittenDisplayGUIBrewingStand(ClientPlayerBase overWriter)
	{
		if (overrideDisplayGUIBrewingStandHooks == null)
			return overWriter;

		for(int i = 0; i < overrideDisplayGUIBrewingStandHooks.length; i++)
			if(overrideDisplayGUIBrewingStandHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDisplayGUIBrewingStandHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDisplayGUIBrewingStandHookTypes = new LinkedList<String>();
	private final static List<String> overrideDisplayGUIBrewingStandHookTypes = new LinkedList<String>();
	private final static List<String> afterDisplayGUIBrewingStandHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeDisplayGUIBrewingStandHooks;
	private ClientPlayerBase[] overrideDisplayGUIBrewingStandHooks;
	private ClientPlayerBase[] afterDisplayGUIBrewingStandHooks;

	public boolean isDisplayGUIBrewingStandModded;

	private static final Map<String, String[]> allBaseBeforeDisplayGUIBrewingStandSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDisplayGUIBrewingStandInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIBrewingStandSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIBrewingStandInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIBrewingStandSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIBrewingStandInferiors = new Hashtable<String, String[]>(0);

	public static void displayGUIChest(IClientPlayerAPI target, net.minecraft.inventory.IInventory paramIInventory)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isDisplayGUIChestModded)
			clientPlayerAPI.displayGUIChest(paramIInventory);
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

	protected ClientPlayerBase GetOverwrittenDisplayGUIChest(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeDisplayGUIChestHooks;
	private ClientPlayerBase[] overrideDisplayGUIChestHooks;
	private ClientPlayerBase[] afterDisplayGUIChestHooks;

	public boolean isDisplayGUIChestModded;

	private static final Map<String, String[]> allBaseBeforeDisplayGUIChestSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDisplayGUIChestInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIChestSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIChestInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIChestSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIChestInferiors = new Hashtable<String, String[]>(0);

	public static void displayGUIDispenser(IClientPlayerAPI target, net.minecraft.tileentity.TileEntityDispenser paramTileEntityDispenser)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isDisplayGUIDispenserModded)
			clientPlayerAPI.displayGUIDispenser(paramTileEntityDispenser);
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

	protected ClientPlayerBase GetOverwrittenDisplayGUIDispenser(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeDisplayGUIDispenserHooks;
	private ClientPlayerBase[] overrideDisplayGUIDispenserHooks;
	private ClientPlayerBase[] afterDisplayGUIDispenserHooks;

	public boolean isDisplayGUIDispenserModded;

	private static final Map<String, String[]> allBaseBeforeDisplayGUIDispenserSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDisplayGUIDispenserInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIDispenserSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIDispenserInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIDispenserSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIDispenserInferiors = new Hashtable<String, String[]>(0);

	public static void displayGUIEditSign(IClientPlayerAPI target, net.minecraft.tileentity.TileEntity paramTileEntity)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isDisplayGUIEditSignModded)
			clientPlayerAPI.displayGUIEditSign(paramTileEntity);
		else
			target.localDisplayGUIEditSign(paramTileEntity);
	}

	private void displayGUIEditSign(net.minecraft.tileentity.TileEntity paramTileEntity)
	{
		if(beforeDisplayGUIEditSignHooks != null)
			for(int i = beforeDisplayGUIEditSignHooks.length - 1; i >= 0 ; i--)
				beforeDisplayGUIEditSignHooks[i].beforeDisplayGUIEditSign(paramTileEntity);

		if(overrideDisplayGUIEditSignHooks != null)
			overrideDisplayGUIEditSignHooks[overrideDisplayGUIEditSignHooks.length - 1].displayGUIEditSign(paramTileEntity);
		else
			player.localDisplayGUIEditSign(paramTileEntity);

		if(afterDisplayGUIEditSignHooks != null)
			for(int i = 0; i < afterDisplayGUIEditSignHooks.length; i++)
				afterDisplayGUIEditSignHooks[i].afterDisplayGUIEditSign(paramTileEntity);

	}

	protected ClientPlayerBase GetOverwrittenDisplayGUIEditSign(ClientPlayerBase overWriter)
	{
		if (overrideDisplayGUIEditSignHooks == null)
			return overWriter;

		for(int i = 0; i < overrideDisplayGUIEditSignHooks.length; i++)
			if(overrideDisplayGUIEditSignHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDisplayGUIEditSignHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDisplayGUIEditSignHookTypes = new LinkedList<String>();
	private final static List<String> overrideDisplayGUIEditSignHookTypes = new LinkedList<String>();
	private final static List<String> afterDisplayGUIEditSignHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeDisplayGUIEditSignHooks;
	private ClientPlayerBase[] overrideDisplayGUIEditSignHooks;
	private ClientPlayerBase[] afterDisplayGUIEditSignHooks;

	public boolean isDisplayGUIEditSignModded;

	private static final Map<String, String[]> allBaseBeforeDisplayGUIEditSignSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDisplayGUIEditSignInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIEditSignSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIEditSignInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIEditSignSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIEditSignInferiors = new Hashtable<String, String[]>(0);

	public static void displayGUIEnchantment(IClientPlayerAPI target, int paramInt1, int paramInt2, int paramInt3, String paramString)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isDisplayGUIEnchantmentModded)
			clientPlayerAPI.displayGUIEnchantment(paramInt1, paramInt2, paramInt3, paramString);
		else
			target.localDisplayGUIEnchantment(paramInt1, paramInt2, paramInt3, paramString);
	}

	private void displayGUIEnchantment(int paramInt1, int paramInt2, int paramInt3, String paramString)
	{
		if(beforeDisplayGUIEnchantmentHooks != null)
			for(int i = beforeDisplayGUIEnchantmentHooks.length - 1; i >= 0 ; i--)
				beforeDisplayGUIEnchantmentHooks[i].beforeDisplayGUIEnchantment(paramInt1, paramInt2, paramInt3, paramString);

		if(overrideDisplayGUIEnchantmentHooks != null)
			overrideDisplayGUIEnchantmentHooks[overrideDisplayGUIEnchantmentHooks.length - 1].displayGUIEnchantment(paramInt1, paramInt2, paramInt3, paramString);
		else
			player.localDisplayGUIEnchantment(paramInt1, paramInt2, paramInt3, paramString);

		if(afterDisplayGUIEnchantmentHooks != null)
			for(int i = 0; i < afterDisplayGUIEnchantmentHooks.length; i++)
				afterDisplayGUIEnchantmentHooks[i].afterDisplayGUIEnchantment(paramInt1, paramInt2, paramInt3, paramString);

	}

	protected ClientPlayerBase GetOverwrittenDisplayGUIEnchantment(ClientPlayerBase overWriter)
	{
		if (overrideDisplayGUIEnchantmentHooks == null)
			return overWriter;

		for(int i = 0; i < overrideDisplayGUIEnchantmentHooks.length; i++)
			if(overrideDisplayGUIEnchantmentHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDisplayGUIEnchantmentHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDisplayGUIEnchantmentHookTypes = new LinkedList<String>();
	private final static List<String> overrideDisplayGUIEnchantmentHookTypes = new LinkedList<String>();
	private final static List<String> afterDisplayGUIEnchantmentHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeDisplayGUIEnchantmentHooks;
	private ClientPlayerBase[] overrideDisplayGUIEnchantmentHooks;
	private ClientPlayerBase[] afterDisplayGUIEnchantmentHooks;

	public boolean isDisplayGUIEnchantmentModded;

	private static final Map<String, String[]> allBaseBeforeDisplayGUIEnchantmentSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDisplayGUIEnchantmentInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIEnchantmentSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIEnchantmentInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIEnchantmentSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIEnchantmentInferiors = new Hashtable<String, String[]>(0);

	public static void displayGUIFurnace(IClientPlayerAPI target, net.minecraft.tileentity.TileEntityFurnace paramTileEntityFurnace)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isDisplayGUIFurnaceModded)
			clientPlayerAPI.displayGUIFurnace(paramTileEntityFurnace);
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

	protected ClientPlayerBase GetOverwrittenDisplayGUIFurnace(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeDisplayGUIFurnaceHooks;
	private ClientPlayerBase[] overrideDisplayGUIFurnaceHooks;
	private ClientPlayerBase[] afterDisplayGUIFurnaceHooks;

	public boolean isDisplayGUIFurnaceModded;

	private static final Map<String, String[]> allBaseBeforeDisplayGUIFurnaceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDisplayGUIFurnaceInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIFurnaceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIFurnaceInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIFurnaceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIFurnaceInferiors = new Hashtable<String, String[]>(0);

	public static void displayGUIWorkbench(IClientPlayerAPI target, int paramInt1, int paramInt2, int paramInt3)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isDisplayGUIWorkbenchModded)
			clientPlayerAPI.displayGUIWorkbench(paramInt1, paramInt2, paramInt3);
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

	protected ClientPlayerBase GetOverwrittenDisplayGUIWorkbench(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeDisplayGUIWorkbenchHooks;
	private ClientPlayerBase[] overrideDisplayGUIWorkbenchHooks;
	private ClientPlayerBase[] afterDisplayGUIWorkbenchHooks;

	public boolean isDisplayGUIWorkbenchModded;

	private static final Map<String, String[]> allBaseBeforeDisplayGUIWorkbenchSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDisplayGUIWorkbenchInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIWorkbenchSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDisplayGUIWorkbenchInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIWorkbenchSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDisplayGUIWorkbenchInferiors = new Hashtable<String, String[]>(0);

	public static net.minecraft.entity.item.EntityItem dropOneItem(IClientPlayerAPI target, boolean paramBoolean)
	{
		net.minecraft.entity.item.EntityItem _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isDropOneItemModded)
			_result = clientPlayerAPI.dropOneItem(paramBoolean);
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

	protected ClientPlayerBase GetOverwrittenDropOneItem(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeDropOneItemHooks;
	private ClientPlayerBase[] overrideDropOneItemHooks;
	private ClientPlayerBase[] afterDropOneItemHooks;

	public boolean isDropOneItemModded;

	private static final Map<String, String[]> allBaseBeforeDropOneItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDropOneItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDropOneItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDropOneItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDropOneItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDropOneItemInferiors = new Hashtable<String, String[]>(0);

	public static net.minecraft.entity.item.EntityItem dropPlayerItem(IClientPlayerAPI target, net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean)
	{
		net.minecraft.entity.item.EntityItem _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isDropPlayerItemModded)
			_result = clientPlayerAPI.dropPlayerItem(paramItemStack, paramBoolean);
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

	protected ClientPlayerBase GetOverwrittenDropPlayerItem(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeDropPlayerItemHooks;
	private ClientPlayerBase[] overrideDropPlayerItemHooks;
	private ClientPlayerBase[] afterDropPlayerItemHooks;

	public boolean isDropPlayerItemModded;

	private static final Map<String, String[]> allBaseBeforeDropPlayerItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDropPlayerItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDropPlayerItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDropPlayerItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDropPlayerItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDropPlayerItemInferiors = new Hashtable<String, String[]>(0);

	public static net.minecraft.entity.item.EntityItem dropPlayerItemWithRandomChoice(IClientPlayerAPI target, net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean1, boolean paramBoolean2)
	{
		net.minecraft.entity.item.EntityItem _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isDropPlayerItemWithRandomChoiceModded)
			_result = clientPlayerAPI.dropPlayerItemWithRandomChoice(paramItemStack, paramBoolean1, paramBoolean2);
		else
			_result = target.localDropPlayerItemWithRandomChoice(paramItemStack, paramBoolean1, paramBoolean2);
		return _result;
	}

	private net.minecraft.entity.item.EntityItem dropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean1, boolean paramBoolean2)
	{
		if(beforeDropPlayerItemWithRandomChoiceHooks != null)
			for(int i = beforeDropPlayerItemWithRandomChoiceHooks.length - 1; i >= 0 ; i--)
				beforeDropPlayerItemWithRandomChoiceHooks[i].beforeDropPlayerItemWithRandomChoice(paramItemStack, paramBoolean1, paramBoolean2);

		net.minecraft.entity.item.EntityItem _result;
		if(overrideDropPlayerItemWithRandomChoiceHooks != null)
			_result = overrideDropPlayerItemWithRandomChoiceHooks[overrideDropPlayerItemWithRandomChoiceHooks.length - 1].dropPlayerItemWithRandomChoice(paramItemStack, paramBoolean1, paramBoolean2);
		else
			_result = player.localDropPlayerItemWithRandomChoice(paramItemStack, paramBoolean1, paramBoolean2);

		if(afterDropPlayerItemWithRandomChoiceHooks != null)
			for(int i = 0; i < afterDropPlayerItemWithRandomChoiceHooks.length; i++)
				afterDropPlayerItemWithRandomChoiceHooks[i].afterDropPlayerItemWithRandomChoice(paramItemStack, paramBoolean1, paramBoolean2);

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenDropPlayerItemWithRandomChoice(ClientPlayerBase overWriter)
	{
		if (overrideDropPlayerItemWithRandomChoiceHooks == null)
			return overWriter;

		for(int i = 0; i < overrideDropPlayerItemWithRandomChoiceHooks.length; i++)
			if(overrideDropPlayerItemWithRandomChoiceHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDropPlayerItemWithRandomChoiceHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDropPlayerItemWithRandomChoiceHookTypes = new LinkedList<String>();
	private final static List<String> overrideDropPlayerItemWithRandomChoiceHookTypes = new LinkedList<String>();
	private final static List<String> afterDropPlayerItemWithRandomChoiceHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeDropPlayerItemWithRandomChoiceHooks;
	private ClientPlayerBase[] overrideDropPlayerItemWithRandomChoiceHooks;
	private ClientPlayerBase[] afterDropPlayerItemWithRandomChoiceHooks;

	public boolean isDropPlayerItemWithRandomChoiceModded;

	private static final Map<String, String[]> allBaseBeforeDropPlayerItemWithRandomChoiceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDropPlayerItemWithRandomChoiceInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDropPlayerItemWithRandomChoiceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDropPlayerItemWithRandomChoiceInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDropPlayerItemWithRandomChoiceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDropPlayerItemWithRandomChoiceInferiors = new Hashtable<String, String[]>(0);

	public static void fall(IClientPlayerAPI target, float paramFloat)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isFallModded)
			clientPlayerAPI.fall(paramFloat);
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

	protected ClientPlayerBase GetOverwrittenFall(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeFallHooks;
	private ClientPlayerBase[] overrideFallHooks;
	private ClientPlayerBase[] afterFallHooks;

	public boolean isFallModded;

	private static final Map<String, String[]> allBaseBeforeFallSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeFallInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideFallSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideFallInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterFallSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterFallInferiors = new Hashtable<String, String[]>(0);

	public static float getAIMoveSpeed(IClientPlayerAPI target)
	{
		float _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetAIMoveSpeedModded)
			_result = clientPlayerAPI.getAIMoveSpeed();
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

	protected ClientPlayerBase GetOverwrittenGetAIMoveSpeed(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeGetAIMoveSpeedHooks;
	private ClientPlayerBase[] overrideGetAIMoveSpeedHooks;
	private ClientPlayerBase[] afterGetAIMoveSpeedHooks;

	public boolean isGetAIMoveSpeedModded;

	private static final Map<String, String[]> allBaseBeforeGetAIMoveSpeedSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetAIMoveSpeedInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetAIMoveSpeedSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetAIMoveSpeedInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetAIMoveSpeedSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetAIMoveSpeedInferiors = new Hashtable<String, String[]>(0);

	public static float getBedOrientationInDegrees(IClientPlayerAPI target)
	{
		float _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetBedOrientationInDegreesModded)
			_result = clientPlayerAPI.getBedOrientationInDegrees();
		else
			_result = target.localGetBedOrientationInDegrees();
		return _result;
	}

	private float getBedOrientationInDegrees()
	{
		if(beforeGetBedOrientationInDegreesHooks != null)
			for(int i = beforeGetBedOrientationInDegreesHooks.length - 1; i >= 0 ; i--)
				beforeGetBedOrientationInDegreesHooks[i].beforeGetBedOrientationInDegrees();

		float _result;
		if(overrideGetBedOrientationInDegreesHooks != null)
			_result = overrideGetBedOrientationInDegreesHooks[overrideGetBedOrientationInDegreesHooks.length - 1].getBedOrientationInDegrees();
		else
			_result = player.localGetBedOrientationInDegrees();

		if(afterGetBedOrientationInDegreesHooks != null)
			for(int i = 0; i < afterGetBedOrientationInDegreesHooks.length; i++)
				afterGetBedOrientationInDegreesHooks[i].afterGetBedOrientationInDegrees();

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenGetBedOrientationInDegrees(ClientPlayerBase overWriter)
	{
		if (overrideGetBedOrientationInDegreesHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetBedOrientationInDegreesHooks.length; i++)
			if(overrideGetBedOrientationInDegreesHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetBedOrientationInDegreesHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetBedOrientationInDegreesHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetBedOrientationInDegreesHookTypes = new LinkedList<String>();
	private final static List<String> afterGetBedOrientationInDegreesHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeGetBedOrientationInDegreesHooks;
	private ClientPlayerBase[] overrideGetBedOrientationInDegreesHooks;
	private ClientPlayerBase[] afterGetBedOrientationInDegreesHooks;

	public boolean isGetBedOrientationInDegreesModded;

	private static final Map<String, String[]> allBaseBeforeGetBedOrientationInDegreesSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetBedOrientationInDegreesInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetBedOrientationInDegreesSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetBedOrientationInDegreesInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetBedOrientationInDegreesSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetBedOrientationInDegreesInferiors = new Hashtable<String, String[]>(0);

	public static float getBrightness(IClientPlayerAPI target, float paramFloat)
	{
		float _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetBrightnessModded)
			_result = clientPlayerAPI.getBrightness(paramFloat);
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

	protected ClientPlayerBase GetOverwrittenGetBrightness(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeGetBrightnessHooks;
	private ClientPlayerBase[] overrideGetBrightnessHooks;
	private ClientPlayerBase[] afterGetBrightnessHooks;

	public boolean isGetBrightnessModded;

	private static final Map<String, String[]> allBaseBeforeGetBrightnessSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetBrightnessInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetBrightnessSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetBrightnessInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetBrightnessSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetBrightnessInferiors = new Hashtable<String, String[]>(0);

	public static int getBrightnessForRender(IClientPlayerAPI target, float paramFloat)
	{
		int _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetBrightnessForRenderModded)
			_result = clientPlayerAPI.getBrightnessForRender(paramFloat);
		else
			_result = target.localGetBrightnessForRender(paramFloat);
		return _result;
	}

	private int getBrightnessForRender(float paramFloat)
	{
		if(beforeGetBrightnessForRenderHooks != null)
			for(int i = beforeGetBrightnessForRenderHooks.length - 1; i >= 0 ; i--)
				beforeGetBrightnessForRenderHooks[i].beforeGetBrightnessForRender(paramFloat);

		int _result;
		if(overrideGetBrightnessForRenderHooks != null)
			_result = overrideGetBrightnessForRenderHooks[overrideGetBrightnessForRenderHooks.length - 1].getBrightnessForRender(paramFloat);
		else
			_result = player.localGetBrightnessForRender(paramFloat);

		if(afterGetBrightnessForRenderHooks != null)
			for(int i = 0; i < afterGetBrightnessForRenderHooks.length; i++)
				afterGetBrightnessForRenderHooks[i].afterGetBrightnessForRender(paramFloat);

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenGetBrightnessForRender(ClientPlayerBase overWriter)
	{
		if (overrideGetBrightnessForRenderHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetBrightnessForRenderHooks.length; i++)
			if(overrideGetBrightnessForRenderHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetBrightnessForRenderHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetBrightnessForRenderHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetBrightnessForRenderHookTypes = new LinkedList<String>();
	private final static List<String> afterGetBrightnessForRenderHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeGetBrightnessForRenderHooks;
	private ClientPlayerBase[] overrideGetBrightnessForRenderHooks;
	private ClientPlayerBase[] afterGetBrightnessForRenderHooks;

	public boolean isGetBrightnessForRenderModded;

	private static final Map<String, String[]> allBaseBeforeGetBrightnessForRenderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetBrightnessForRenderInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetBrightnessForRenderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetBrightnessForRenderInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetBrightnessForRenderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetBrightnessForRenderInferiors = new Hashtable<String, String[]>(0);

	public static float getCurrentPlayerStrVsBlock(IClientPlayerAPI target, net.minecraft.block.Block paramBlock, boolean paramBoolean)
	{
		float _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetCurrentPlayerStrVsBlockModded)
			_result = clientPlayerAPI.getCurrentPlayerStrVsBlock(paramBlock, paramBoolean);
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

	protected ClientPlayerBase GetOverwrittenGetCurrentPlayerStrVsBlock(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeGetCurrentPlayerStrVsBlockHooks;
	private ClientPlayerBase[] overrideGetCurrentPlayerStrVsBlockHooks;
	private ClientPlayerBase[] afterGetCurrentPlayerStrVsBlockHooks;

	public boolean isGetCurrentPlayerStrVsBlockModded;

	private static final Map<String, String[]> allBaseBeforeGetCurrentPlayerStrVsBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetCurrentPlayerStrVsBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetCurrentPlayerStrVsBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetCurrentPlayerStrVsBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetCurrentPlayerStrVsBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetCurrentPlayerStrVsBlockInferiors = new Hashtable<String, String[]>(0);

	public static float getCurrentPlayerStrVsBlockForge(IClientPlayerAPI target, net.minecraft.block.Block paramBlock, boolean paramBoolean, int paramInt)
	{
		float _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetCurrentPlayerStrVsBlockForgeModded)
			_result = clientPlayerAPI.getCurrentPlayerStrVsBlockForge(paramBlock, paramBoolean, paramInt);
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

	protected ClientPlayerBase GetOverwrittenGetCurrentPlayerStrVsBlockForge(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeGetCurrentPlayerStrVsBlockForgeHooks;
	private ClientPlayerBase[] overrideGetCurrentPlayerStrVsBlockForgeHooks;
	private ClientPlayerBase[] afterGetCurrentPlayerStrVsBlockForgeHooks;

	public boolean isGetCurrentPlayerStrVsBlockForgeModded;

	private static final Map<String, String[]> allBaseBeforeGetCurrentPlayerStrVsBlockForgeSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetCurrentPlayerStrVsBlockForgeInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetCurrentPlayerStrVsBlockForgeSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetCurrentPlayerStrVsBlockForgeInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetCurrentPlayerStrVsBlockForgeSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetCurrentPlayerStrVsBlockForgeInferiors = new Hashtable<String, String[]>(0);

	public static double getDistanceSq(IClientPlayerAPI target, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		double _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetDistanceSqModded)
			_result = clientPlayerAPI.getDistanceSq(paramDouble1, paramDouble2, paramDouble3);
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

	protected ClientPlayerBase GetOverwrittenGetDistanceSq(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeGetDistanceSqHooks;
	private ClientPlayerBase[] overrideGetDistanceSqHooks;
	private ClientPlayerBase[] afterGetDistanceSqHooks;

	public boolean isGetDistanceSqModded;

	private static final Map<String, String[]> allBaseBeforeGetDistanceSqSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetDistanceSqInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetDistanceSqSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetDistanceSqInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetDistanceSqSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetDistanceSqInferiors = new Hashtable<String, String[]>(0);

	public static double getDistanceSqToEntity(IClientPlayerAPI target, net.minecraft.entity.Entity paramEntity)
	{
		double _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetDistanceSqToEntityModded)
			_result = clientPlayerAPI.getDistanceSqToEntity(paramEntity);
		else
			_result = target.localGetDistanceSqToEntity(paramEntity);
		return _result;
	}

	private double getDistanceSqToEntity(net.minecraft.entity.Entity paramEntity)
	{
		if(beforeGetDistanceSqToEntityHooks != null)
			for(int i = beforeGetDistanceSqToEntityHooks.length - 1; i >= 0 ; i--)
				beforeGetDistanceSqToEntityHooks[i].beforeGetDistanceSqToEntity(paramEntity);

		double _result;
		if(overrideGetDistanceSqToEntityHooks != null)
			_result = overrideGetDistanceSqToEntityHooks[overrideGetDistanceSqToEntityHooks.length - 1].getDistanceSqToEntity(paramEntity);
		else
			_result = player.localGetDistanceSqToEntity(paramEntity);

		if(afterGetDistanceSqToEntityHooks != null)
			for(int i = 0; i < afterGetDistanceSqToEntityHooks.length; i++)
				afterGetDistanceSqToEntityHooks[i].afterGetDistanceSqToEntity(paramEntity);

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenGetDistanceSqToEntity(ClientPlayerBase overWriter)
	{
		if (overrideGetDistanceSqToEntityHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetDistanceSqToEntityHooks.length; i++)
			if(overrideGetDistanceSqToEntityHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetDistanceSqToEntityHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetDistanceSqToEntityHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetDistanceSqToEntityHookTypes = new LinkedList<String>();
	private final static List<String> afterGetDistanceSqToEntityHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeGetDistanceSqToEntityHooks;
	private ClientPlayerBase[] overrideGetDistanceSqToEntityHooks;
	private ClientPlayerBase[] afterGetDistanceSqToEntityHooks;

	public boolean isGetDistanceSqToEntityModded;

	private static final Map<String, String[]> allBaseBeforeGetDistanceSqToEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetDistanceSqToEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetDistanceSqToEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetDistanceSqToEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetDistanceSqToEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetDistanceSqToEntityInferiors = new Hashtable<String, String[]>(0);

	public static float getFOVMultiplier(IClientPlayerAPI target)
	{
		float _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetFOVMultiplierModded)
			_result = clientPlayerAPI.getFOVMultiplier();
		else
			_result = target.localGetFOVMultiplier();
		return _result;
	}

	private float getFOVMultiplier()
	{
		if(beforeGetFOVMultiplierHooks != null)
			for(int i = beforeGetFOVMultiplierHooks.length - 1; i >= 0 ; i--)
				beforeGetFOVMultiplierHooks[i].beforeGetFOVMultiplier();

		float _result;
		if(overrideGetFOVMultiplierHooks != null)
			_result = overrideGetFOVMultiplierHooks[overrideGetFOVMultiplierHooks.length - 1].getFOVMultiplier();
		else
			_result = player.localGetFOVMultiplier();

		if(afterGetFOVMultiplierHooks != null)
			for(int i = 0; i < afterGetFOVMultiplierHooks.length; i++)
				afterGetFOVMultiplierHooks[i].afterGetFOVMultiplier();

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenGetFOVMultiplier(ClientPlayerBase overWriter)
	{
		if (overrideGetFOVMultiplierHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetFOVMultiplierHooks.length; i++)
			if(overrideGetFOVMultiplierHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetFOVMultiplierHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetFOVMultiplierHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetFOVMultiplierHookTypes = new LinkedList<String>();
	private final static List<String> afterGetFOVMultiplierHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeGetFOVMultiplierHooks;
	private ClientPlayerBase[] overrideGetFOVMultiplierHooks;
	private ClientPlayerBase[] afterGetFOVMultiplierHooks;

	public boolean isGetFOVMultiplierModded;

	private static final Map<String, String[]> allBaseBeforeGetFOVMultiplierSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetFOVMultiplierInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetFOVMultiplierSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetFOVMultiplierInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetFOVMultiplierSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetFOVMultiplierInferiors = new Hashtable<String, String[]>(0);

	public static java.lang.String getHurtSound(IClientPlayerAPI target)
	{
		java.lang.String _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetHurtSoundModded)
			_result = clientPlayerAPI.getHurtSound();
		else
			_result = target.localGetHurtSound();
		return _result;
	}

	private java.lang.String getHurtSound()
	{
		if(beforeGetHurtSoundHooks != null)
			for(int i = beforeGetHurtSoundHooks.length - 1; i >= 0 ; i--)
				beforeGetHurtSoundHooks[i].beforeGetHurtSound();

		java.lang.String _result;
		if(overrideGetHurtSoundHooks != null)
			_result = overrideGetHurtSoundHooks[overrideGetHurtSoundHooks.length - 1].getHurtSound();
		else
			_result = player.localGetHurtSound();

		if(afterGetHurtSoundHooks != null)
			for(int i = 0; i < afterGetHurtSoundHooks.length; i++)
				afterGetHurtSoundHooks[i].afterGetHurtSound();

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenGetHurtSound(ClientPlayerBase overWriter)
	{
		if (overrideGetHurtSoundHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetHurtSoundHooks.length; i++)
			if(overrideGetHurtSoundHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetHurtSoundHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetHurtSoundHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetHurtSoundHookTypes = new LinkedList<String>();
	private final static List<String> afterGetHurtSoundHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeGetHurtSoundHooks;
	private ClientPlayerBase[] overrideGetHurtSoundHooks;
	private ClientPlayerBase[] afterGetHurtSoundHooks;

	public boolean isGetHurtSoundModded;

	private static final Map<String, String[]> allBaseBeforeGetHurtSoundSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetHurtSoundInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetHurtSoundSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetHurtSoundInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetHurtSoundSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetHurtSoundInferiors = new Hashtable<String, String[]>(0);

	public static net.minecraft.util.IIcon getItemIcon(IClientPlayerAPI target, net.minecraft.item.ItemStack paramItemStack, int paramInt)
	{
		net.minecraft.util.IIcon _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetItemIconModded)
			_result = clientPlayerAPI.getItemIcon(paramItemStack, paramInt);
		else
			_result = target.localGetItemIcon(paramItemStack, paramInt);
		return _result;
	}

	private net.minecraft.util.IIcon getItemIcon(net.minecraft.item.ItemStack paramItemStack, int paramInt)
	{
		if(beforeGetItemIconHooks != null)
			for(int i = beforeGetItemIconHooks.length - 1; i >= 0 ; i--)
				beforeGetItemIconHooks[i].beforeGetItemIcon(paramItemStack, paramInt);

		net.minecraft.util.IIcon _result;
		if(overrideGetItemIconHooks != null)
			_result = overrideGetItemIconHooks[overrideGetItemIconHooks.length - 1].getItemIcon(paramItemStack, paramInt);
		else
			_result = player.localGetItemIcon(paramItemStack, paramInt);

		if(afterGetItemIconHooks != null)
			for(int i = 0; i < afterGetItemIconHooks.length; i++)
				afterGetItemIconHooks[i].afterGetItemIcon(paramItemStack, paramInt);

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenGetItemIcon(ClientPlayerBase overWriter)
	{
		if (overrideGetItemIconHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetItemIconHooks.length; i++)
			if(overrideGetItemIconHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetItemIconHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetItemIconHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetItemIconHookTypes = new LinkedList<String>();
	private final static List<String> afterGetItemIconHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeGetItemIconHooks;
	private ClientPlayerBase[] overrideGetItemIconHooks;
	private ClientPlayerBase[] afterGetItemIconHooks;

	public boolean isGetItemIconModded;

	private static final Map<String, String[]> allBaseBeforeGetItemIconSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetItemIconInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetItemIconSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetItemIconInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetItemIconSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetItemIconInferiors = new Hashtable<String, String[]>(0);

	public static int getSleepTimer(IClientPlayerAPI target)
	{
		int _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isGetSleepTimerModded)
			_result = clientPlayerAPI.getSleepTimer();
		else
			_result = target.localGetSleepTimer();
		return _result;
	}

	private int getSleepTimer()
	{
		if(beforeGetSleepTimerHooks != null)
			for(int i = beforeGetSleepTimerHooks.length - 1; i >= 0 ; i--)
				beforeGetSleepTimerHooks[i].beforeGetSleepTimer();

		int _result;
		if(overrideGetSleepTimerHooks != null)
			_result = overrideGetSleepTimerHooks[overrideGetSleepTimerHooks.length - 1].getSleepTimer();
		else
			_result = player.localGetSleepTimer();

		if(afterGetSleepTimerHooks != null)
			for(int i = 0; i < afterGetSleepTimerHooks.length; i++)
				afterGetSleepTimerHooks[i].afterGetSleepTimer();

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenGetSleepTimer(ClientPlayerBase overWriter)
	{
		if (overrideGetSleepTimerHooks == null)
			return overWriter;

		for(int i = 0; i < overrideGetSleepTimerHooks.length; i++)
			if(overrideGetSleepTimerHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetSleepTimerHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetSleepTimerHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetSleepTimerHookTypes = new LinkedList<String>();
	private final static List<String> afterGetSleepTimerHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeGetSleepTimerHooks;
	private ClientPlayerBase[] overrideGetSleepTimerHooks;
	private ClientPlayerBase[] afterGetSleepTimerHooks;

	public boolean isGetSleepTimerModded;

	private static final Map<String, String[]> allBaseBeforeGetSleepTimerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetSleepTimerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetSleepTimerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetSleepTimerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetSleepTimerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetSleepTimerInferiors = new Hashtable<String, String[]>(0);

	public static boolean handleLavaMovement(IClientPlayerAPI target)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isHandleLavaMovementModded)
			_result = clientPlayerAPI.handleLavaMovement();
		else
			_result = target.localHandleLavaMovement();
		return _result;
	}

	private boolean handleLavaMovement()
	{
		if(beforeHandleLavaMovementHooks != null)
			for(int i = beforeHandleLavaMovementHooks.length - 1; i >= 0 ; i--)
				beforeHandleLavaMovementHooks[i].beforeHandleLavaMovement();

		boolean _result;
		if(overrideHandleLavaMovementHooks != null)
			_result = overrideHandleLavaMovementHooks[overrideHandleLavaMovementHooks.length - 1].handleLavaMovement();
		else
			_result = player.localHandleLavaMovement();

		if(afterHandleLavaMovementHooks != null)
			for(int i = 0; i < afterHandleLavaMovementHooks.length; i++)
				afterHandleLavaMovementHooks[i].afterHandleLavaMovement();

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenHandleLavaMovement(ClientPlayerBase overWriter)
	{
		if (overrideHandleLavaMovementHooks == null)
			return overWriter;

		for(int i = 0; i < overrideHandleLavaMovementHooks.length; i++)
			if(overrideHandleLavaMovementHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideHandleLavaMovementHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeHandleLavaMovementHookTypes = new LinkedList<String>();
	private final static List<String> overrideHandleLavaMovementHookTypes = new LinkedList<String>();
	private final static List<String> afterHandleLavaMovementHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeHandleLavaMovementHooks;
	private ClientPlayerBase[] overrideHandleLavaMovementHooks;
	private ClientPlayerBase[] afterHandleLavaMovementHooks;

	public boolean isHandleLavaMovementModded;

	private static final Map<String, String[]> allBaseBeforeHandleLavaMovementSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeHandleLavaMovementInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideHandleLavaMovementSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideHandleLavaMovementInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterHandleLavaMovementSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterHandleLavaMovementInferiors = new Hashtable<String, String[]>(0);

	public static boolean handleWaterMovement(IClientPlayerAPI target)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isHandleWaterMovementModded)
			_result = clientPlayerAPI.handleWaterMovement();
		else
			_result = target.localHandleWaterMovement();
		return _result;
	}

	private boolean handleWaterMovement()
	{
		if(beforeHandleWaterMovementHooks != null)
			for(int i = beforeHandleWaterMovementHooks.length - 1; i >= 0 ; i--)
				beforeHandleWaterMovementHooks[i].beforeHandleWaterMovement();

		boolean _result;
		if(overrideHandleWaterMovementHooks != null)
			_result = overrideHandleWaterMovementHooks[overrideHandleWaterMovementHooks.length - 1].handleWaterMovement();
		else
			_result = player.localHandleWaterMovement();

		if(afterHandleWaterMovementHooks != null)
			for(int i = 0; i < afterHandleWaterMovementHooks.length; i++)
				afterHandleWaterMovementHooks[i].afterHandleWaterMovement();

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenHandleWaterMovement(ClientPlayerBase overWriter)
	{
		if (overrideHandleWaterMovementHooks == null)
			return overWriter;

		for(int i = 0; i < overrideHandleWaterMovementHooks.length; i++)
			if(overrideHandleWaterMovementHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideHandleWaterMovementHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeHandleWaterMovementHookTypes = new LinkedList<String>();
	private final static List<String> overrideHandleWaterMovementHookTypes = new LinkedList<String>();
	private final static List<String> afterHandleWaterMovementHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeHandleWaterMovementHooks;
	private ClientPlayerBase[] overrideHandleWaterMovementHooks;
	private ClientPlayerBase[] afterHandleWaterMovementHooks;

	public boolean isHandleWaterMovementModded;

	private static final Map<String, String[]> allBaseBeforeHandleWaterMovementSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeHandleWaterMovementInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideHandleWaterMovementSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideHandleWaterMovementInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterHandleWaterMovementSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterHandleWaterMovementInferiors = new Hashtable<String, String[]>(0);

	public static void heal(IClientPlayerAPI target, float paramFloat)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isHealModded)
			clientPlayerAPI.heal(paramFloat);
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

	protected ClientPlayerBase GetOverwrittenHeal(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeHealHooks;
	private ClientPlayerBase[] overrideHealHooks;
	private ClientPlayerBase[] afterHealHooks;

	public boolean isHealModded;

	private static final Map<String, String[]> allBaseBeforeHealSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeHealInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideHealSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideHealInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterHealSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterHealInferiors = new Hashtable<String, String[]>(0);

	public static boolean isEntityInsideOpaqueBlock(IClientPlayerAPI target)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isIsEntityInsideOpaqueBlockModded)
			_result = clientPlayerAPI.isEntityInsideOpaqueBlock();
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

	protected ClientPlayerBase GetOverwrittenIsEntityInsideOpaqueBlock(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeIsEntityInsideOpaqueBlockHooks;
	private ClientPlayerBase[] overrideIsEntityInsideOpaqueBlockHooks;
	private ClientPlayerBase[] afterIsEntityInsideOpaqueBlockHooks;

	public boolean isIsEntityInsideOpaqueBlockModded;

	private static final Map<String, String[]> allBaseBeforeIsEntityInsideOpaqueBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsEntityInsideOpaqueBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsEntityInsideOpaqueBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsEntityInsideOpaqueBlockInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsEntityInsideOpaqueBlockSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsEntityInsideOpaqueBlockInferiors = new Hashtable<String, String[]>(0);

	public static boolean isInWater(IClientPlayerAPI target)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isIsInWaterModded)
			_result = clientPlayerAPI.isInWater();
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

	protected ClientPlayerBase GetOverwrittenIsInWater(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeIsInWaterHooks;
	private ClientPlayerBase[] overrideIsInWaterHooks;
	private ClientPlayerBase[] afterIsInWaterHooks;

	public boolean isIsInWaterModded;

	private static final Map<String, String[]> allBaseBeforeIsInWaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsInWaterInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsInWaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsInWaterInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsInWaterSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsInWaterInferiors = new Hashtable<String, String[]>(0);

	public static boolean isInsideOfMaterial(IClientPlayerAPI target, net.minecraft.block.material.Material paramMaterial)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isIsInsideOfMaterialModded)
			_result = clientPlayerAPI.isInsideOfMaterial(paramMaterial);
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

	protected ClientPlayerBase GetOverwrittenIsInsideOfMaterial(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeIsInsideOfMaterialHooks;
	private ClientPlayerBase[] overrideIsInsideOfMaterialHooks;
	private ClientPlayerBase[] afterIsInsideOfMaterialHooks;

	public boolean isIsInsideOfMaterialModded;

	private static final Map<String, String[]> allBaseBeforeIsInsideOfMaterialSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsInsideOfMaterialInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsInsideOfMaterialSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsInsideOfMaterialInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsInsideOfMaterialSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsInsideOfMaterialInferiors = new Hashtable<String, String[]>(0);

	public static boolean isOnLadder(IClientPlayerAPI target)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isIsOnLadderModded)
			_result = clientPlayerAPI.isOnLadder();
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

	protected ClientPlayerBase GetOverwrittenIsOnLadder(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeIsOnLadderHooks;
	private ClientPlayerBase[] overrideIsOnLadderHooks;
	private ClientPlayerBase[] afterIsOnLadderHooks;

	public boolean isIsOnLadderModded;

	private static final Map<String, String[]> allBaseBeforeIsOnLadderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsOnLadderInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsOnLadderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsOnLadderInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsOnLadderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsOnLadderInferiors = new Hashtable<String, String[]>(0);

	public static boolean isPlayerSleeping(IClientPlayerAPI target)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isIsPlayerSleepingModded)
			_result = clientPlayerAPI.isPlayerSleeping();
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

	protected ClientPlayerBase GetOverwrittenIsPlayerSleeping(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeIsPlayerSleepingHooks;
	private ClientPlayerBase[] overrideIsPlayerSleepingHooks;
	private ClientPlayerBase[] afterIsPlayerSleepingHooks;

	public boolean isIsPlayerSleepingModded;

	private static final Map<String, String[]> allBaseBeforeIsPlayerSleepingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsPlayerSleepingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsPlayerSleepingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsPlayerSleepingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsPlayerSleepingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsPlayerSleepingInferiors = new Hashtable<String, String[]>(0);

	public static boolean isSneaking(IClientPlayerAPI target)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isIsSneakingModded)
			_result = clientPlayerAPI.isSneaking();
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

	protected ClientPlayerBase GetOverwrittenIsSneaking(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeIsSneakingHooks;
	private ClientPlayerBase[] overrideIsSneakingHooks;
	private ClientPlayerBase[] afterIsSneakingHooks;

	public boolean isIsSneakingModded;

	private static final Map<String, String[]> allBaseBeforeIsSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsSneakingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsSneakingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsSneakingInferiors = new Hashtable<String, String[]>(0);

	public static boolean isSprinting(IClientPlayerAPI target)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isIsSprintingModded)
			_result = clientPlayerAPI.isSprinting();
		else
			_result = target.localIsSprinting();
		return _result;
	}

	private boolean isSprinting()
	{
		if(beforeIsSprintingHooks != null)
			for(int i = beforeIsSprintingHooks.length - 1; i >= 0 ; i--)
				beforeIsSprintingHooks[i].beforeIsSprinting();

		boolean _result;
		if(overrideIsSprintingHooks != null)
			_result = overrideIsSprintingHooks[overrideIsSprintingHooks.length - 1].isSprinting();
		else
			_result = player.localIsSprinting();

		if(afterIsSprintingHooks != null)
			for(int i = 0; i < afterIsSprintingHooks.length; i++)
				afterIsSprintingHooks[i].afterIsSprinting();

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenIsSprinting(ClientPlayerBase overWriter)
	{
		if (overrideIsSprintingHooks == null)
			return overWriter;

		for(int i = 0; i < overrideIsSprintingHooks.length; i++)
			if(overrideIsSprintingHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideIsSprintingHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeIsSprintingHookTypes = new LinkedList<String>();
	private final static List<String> overrideIsSprintingHookTypes = new LinkedList<String>();
	private final static List<String> afterIsSprintingHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeIsSprintingHooks;
	private ClientPlayerBase[] overrideIsSprintingHooks;
	private ClientPlayerBase[] afterIsSprintingHooks;

	public boolean isIsSprintingModded;

	private static final Map<String, String[]> allBaseBeforeIsSprintingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeIsSprintingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsSprintingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideIsSprintingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsSprintingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterIsSprintingInferiors = new Hashtable<String, String[]>(0);

	public static void jump(IClientPlayerAPI target)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isJumpModded)
			clientPlayerAPI.jump();
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

	protected ClientPlayerBase GetOverwrittenJump(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeJumpHooks;
	private ClientPlayerBase[] overrideJumpHooks;
	private ClientPlayerBase[] afterJumpHooks;

	public boolean isJumpModded;

	private static final Map<String, String[]> allBaseBeforeJumpSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeJumpInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideJumpSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideJumpInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterJumpSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterJumpInferiors = new Hashtable<String, String[]>(0);

	public static void knockBack(IClientPlayerAPI target, net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isKnockBackModded)
			clientPlayerAPI.knockBack(paramEntity, paramFloat, paramDouble1, paramDouble2);
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

	protected ClientPlayerBase GetOverwrittenKnockBack(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeKnockBackHooks;
	private ClientPlayerBase[] overrideKnockBackHooks;
	private ClientPlayerBase[] afterKnockBackHooks;

	public boolean isKnockBackModded;

	private static final Map<String, String[]> allBaseBeforeKnockBackSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeKnockBackInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideKnockBackSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideKnockBackInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterKnockBackSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterKnockBackInferiors = new Hashtable<String, String[]>(0);

	public static void moveEntity(IClientPlayerAPI target, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isMoveEntityModded)
			clientPlayerAPI.moveEntity(paramDouble1, paramDouble2, paramDouble3);
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

	protected ClientPlayerBase GetOverwrittenMoveEntity(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeMoveEntityHooks;
	private ClientPlayerBase[] overrideMoveEntityHooks;
	private ClientPlayerBase[] afterMoveEntityHooks;

	public boolean isMoveEntityModded;

	private static final Map<String, String[]> allBaseBeforeMoveEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeMoveEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveEntityInferiors = new Hashtable<String, String[]>(0);

	public static void moveEntityWithHeading(IClientPlayerAPI target, float paramFloat1, float paramFloat2)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isMoveEntityWithHeadingModded)
			clientPlayerAPI.moveEntityWithHeading(paramFloat1, paramFloat2);
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

	protected ClientPlayerBase GetOverwrittenMoveEntityWithHeading(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeMoveEntityWithHeadingHooks;
	private ClientPlayerBase[] overrideMoveEntityWithHeadingHooks;
	private ClientPlayerBase[] afterMoveEntityWithHeadingHooks;

	public boolean isMoveEntityWithHeadingModded;

	private static final Map<String, String[]> allBaseBeforeMoveEntityWithHeadingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeMoveEntityWithHeadingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveEntityWithHeadingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveEntityWithHeadingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveEntityWithHeadingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveEntityWithHeadingInferiors = new Hashtable<String, String[]>(0);

	public static void moveFlying(IClientPlayerAPI target, float paramFloat1, float paramFloat2, float paramFloat3)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isMoveFlyingModded)
			clientPlayerAPI.moveFlying(paramFloat1, paramFloat2, paramFloat3);
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

	protected ClientPlayerBase GetOverwrittenMoveFlying(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeMoveFlyingHooks;
	private ClientPlayerBase[] overrideMoveFlyingHooks;
	private ClientPlayerBase[] afterMoveFlyingHooks;

	public boolean isMoveFlyingModded;

	private static final Map<String, String[]> allBaseBeforeMoveFlyingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeMoveFlyingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveFlyingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideMoveFlyingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveFlyingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterMoveFlyingInferiors = new Hashtable<String, String[]>(0);

	public static void onDeath(IClientPlayerAPI target, net.minecraft.util.DamageSource paramDamageSource)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isOnDeathModded)
			clientPlayerAPI.onDeath(paramDamageSource);
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

	protected ClientPlayerBase GetOverwrittenOnDeath(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeOnDeathHooks;
	private ClientPlayerBase[] overrideOnDeathHooks;
	private ClientPlayerBase[] afterOnDeathHooks;

	public boolean isOnDeathModded;

	private static final Map<String, String[]> allBaseBeforeOnDeathSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeOnDeathInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnDeathSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnDeathInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnDeathSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnDeathInferiors = new Hashtable<String, String[]>(0);

	public static void onLivingUpdate(IClientPlayerAPI target)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isOnLivingUpdateModded)
			clientPlayerAPI.onLivingUpdate();
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

	protected ClientPlayerBase GetOverwrittenOnLivingUpdate(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeOnLivingUpdateHooks;
	private ClientPlayerBase[] overrideOnLivingUpdateHooks;
	private ClientPlayerBase[] afterOnLivingUpdateHooks;

	public boolean isOnLivingUpdateModded;

	private static final Map<String, String[]> allBaseBeforeOnLivingUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeOnLivingUpdateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnLivingUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnLivingUpdateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnLivingUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnLivingUpdateInferiors = new Hashtable<String, String[]>(0);

	public static void onKillEntity(IClientPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isOnKillEntityModded)
			clientPlayerAPI.onKillEntity(paramEntityLivingBase);
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

	protected ClientPlayerBase GetOverwrittenOnKillEntity(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeOnKillEntityHooks;
	private ClientPlayerBase[] overrideOnKillEntityHooks;
	private ClientPlayerBase[] afterOnKillEntityHooks;

	public boolean isOnKillEntityModded;

	private static final Map<String, String[]> allBaseBeforeOnKillEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeOnKillEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnKillEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnKillEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnKillEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnKillEntityInferiors = new Hashtable<String, String[]>(0);

	public static void onStruckByLightning(IClientPlayerAPI target, net.minecraft.entity.effect.EntityLightningBolt paramEntityLightningBolt)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isOnStruckByLightningModded)
			clientPlayerAPI.onStruckByLightning(paramEntityLightningBolt);
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

	protected ClientPlayerBase GetOverwrittenOnStruckByLightning(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeOnStruckByLightningHooks;
	private ClientPlayerBase[] overrideOnStruckByLightningHooks;
	private ClientPlayerBase[] afterOnStruckByLightningHooks;

	public boolean isOnStruckByLightningModded;

	private static final Map<String, String[]> allBaseBeforeOnStruckByLightningSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeOnStruckByLightningInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnStruckByLightningSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnStruckByLightningInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnStruckByLightningSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnStruckByLightningInferiors = new Hashtable<String, String[]>(0);

	public static void onUpdate(IClientPlayerAPI target)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isOnUpdateModded)
			clientPlayerAPI.onUpdate();
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

	protected ClientPlayerBase GetOverwrittenOnUpdate(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeOnUpdateHooks;
	private ClientPlayerBase[] overrideOnUpdateHooks;
	private ClientPlayerBase[] afterOnUpdateHooks;

	public boolean isOnUpdateModded;

	private static final Map<String, String[]> allBaseBeforeOnUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeOnUpdateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideOnUpdateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnUpdateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterOnUpdateInferiors = new Hashtable<String, String[]>(0);

	public static void playStepSound(IClientPlayerAPI target, int paramInt1, int paramInt2, int paramInt3, net.minecraft.block.Block paramBlock)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isPlayStepSoundModded)
			clientPlayerAPI.playStepSound(paramInt1, paramInt2, paramInt3, paramBlock);
		else
			target.localPlayStepSound(paramInt1, paramInt2, paramInt3, paramBlock);
	}

	private void playStepSound(int paramInt1, int paramInt2, int paramInt3, net.minecraft.block.Block paramBlock)
	{
		if(beforePlayStepSoundHooks != null)
			for(int i = beforePlayStepSoundHooks.length - 1; i >= 0 ; i--)
				beforePlayStepSoundHooks[i].beforePlayStepSound(paramInt1, paramInt2, paramInt3, paramBlock);

		if(overridePlayStepSoundHooks != null)
			overridePlayStepSoundHooks[overridePlayStepSoundHooks.length - 1].playStepSound(paramInt1, paramInt2, paramInt3, paramBlock);
		else
			player.localPlayStepSound(paramInt1, paramInt2, paramInt3, paramBlock);

		if(afterPlayStepSoundHooks != null)
			for(int i = 0; i < afterPlayStepSoundHooks.length; i++)
				afterPlayStepSoundHooks[i].afterPlayStepSound(paramInt1, paramInt2, paramInt3, paramBlock);

	}

	protected ClientPlayerBase GetOverwrittenPlayStepSound(ClientPlayerBase overWriter)
	{
		if (overridePlayStepSoundHooks == null)
			return overWriter;

		for(int i = 0; i < overridePlayStepSoundHooks.length; i++)
			if(overridePlayStepSoundHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overridePlayStepSoundHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforePlayStepSoundHookTypes = new LinkedList<String>();
	private final static List<String> overridePlayStepSoundHookTypes = new LinkedList<String>();
	private final static List<String> afterPlayStepSoundHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforePlayStepSoundHooks;
	private ClientPlayerBase[] overridePlayStepSoundHooks;
	private ClientPlayerBase[] afterPlayStepSoundHooks;

	public boolean isPlayStepSoundModded;

	private static final Map<String, String[]> allBaseBeforePlayStepSoundSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforePlayStepSoundInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverridePlayStepSoundSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverridePlayStepSoundInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterPlayStepSoundSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterPlayStepSoundInferiors = new Hashtable<String, String[]>(0);

	public static boolean pushOutOfBlocks(IClientPlayerAPI target, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		boolean _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isPushOutOfBlocksModded)
			_result = clientPlayerAPI.pushOutOfBlocks(paramDouble1, paramDouble2, paramDouble3);
		else
			_result = target.localPushOutOfBlocks(paramDouble1, paramDouble2, paramDouble3);
		return _result;
	}

	private boolean pushOutOfBlocks(double paramDouble1, double paramDouble2, double paramDouble3)
	{
		if(beforePushOutOfBlocksHooks != null)
			for(int i = beforePushOutOfBlocksHooks.length - 1; i >= 0 ; i--)
				beforePushOutOfBlocksHooks[i].beforePushOutOfBlocks(paramDouble1, paramDouble2, paramDouble3);

		boolean _result;
		if(overridePushOutOfBlocksHooks != null)
			_result = overridePushOutOfBlocksHooks[overridePushOutOfBlocksHooks.length - 1].pushOutOfBlocks(paramDouble1, paramDouble2, paramDouble3);
		else
			_result = player.localPushOutOfBlocks(paramDouble1, paramDouble2, paramDouble3);

		if(afterPushOutOfBlocksHooks != null)
			for(int i = 0; i < afterPushOutOfBlocksHooks.length; i++)
				afterPushOutOfBlocksHooks[i].afterPushOutOfBlocks(paramDouble1, paramDouble2, paramDouble3);

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenPushOutOfBlocks(ClientPlayerBase overWriter)
	{
		if (overridePushOutOfBlocksHooks == null)
			return overWriter;

		for(int i = 0; i < overridePushOutOfBlocksHooks.length; i++)
			if(overridePushOutOfBlocksHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overridePushOutOfBlocksHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforePushOutOfBlocksHookTypes = new LinkedList<String>();
	private final static List<String> overridePushOutOfBlocksHookTypes = new LinkedList<String>();
	private final static List<String> afterPushOutOfBlocksHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforePushOutOfBlocksHooks;
	private ClientPlayerBase[] overridePushOutOfBlocksHooks;
	private ClientPlayerBase[] afterPushOutOfBlocksHooks;

	public boolean isPushOutOfBlocksModded;

	private static final Map<String, String[]> allBaseBeforePushOutOfBlocksSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforePushOutOfBlocksInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverridePushOutOfBlocksSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverridePushOutOfBlocksInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterPushOutOfBlocksSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterPushOutOfBlocksInferiors = new Hashtable<String, String[]>(0);

	public static net.minecraft.util.MovingObjectPosition rayTrace(IClientPlayerAPI target, double paramDouble, float paramFloat)
	{
		net.minecraft.util.MovingObjectPosition _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isRayTraceModded)
			_result = clientPlayerAPI.rayTrace(paramDouble, paramFloat);
		else
			_result = target.localRayTrace(paramDouble, paramFloat);
		return _result;
	}

	private net.minecraft.util.MovingObjectPosition rayTrace(double paramDouble, float paramFloat)
	{
		if(beforeRayTraceHooks != null)
			for(int i = beforeRayTraceHooks.length - 1; i >= 0 ; i--)
				beforeRayTraceHooks[i].beforeRayTrace(paramDouble, paramFloat);

		net.minecraft.util.MovingObjectPosition _result;
		if(overrideRayTraceHooks != null)
			_result = overrideRayTraceHooks[overrideRayTraceHooks.length - 1].rayTrace(paramDouble, paramFloat);
		else
			_result = player.localRayTrace(paramDouble, paramFloat);

		if(afterRayTraceHooks != null)
			for(int i = 0; i < afterRayTraceHooks.length; i++)
				afterRayTraceHooks[i].afterRayTrace(paramDouble, paramFloat);

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenRayTrace(ClientPlayerBase overWriter)
	{
		if (overrideRayTraceHooks == null)
			return overWriter;

		for(int i = 0; i < overrideRayTraceHooks.length; i++)
			if(overrideRayTraceHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRayTraceHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRayTraceHookTypes = new LinkedList<String>();
	private final static List<String> overrideRayTraceHookTypes = new LinkedList<String>();
	private final static List<String> afterRayTraceHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeRayTraceHooks;
	private ClientPlayerBase[] overrideRayTraceHooks;
	private ClientPlayerBase[] afterRayTraceHooks;

	public boolean isRayTraceModded;

	private static final Map<String, String[]> allBaseBeforeRayTraceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRayTraceInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRayTraceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRayTraceInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRayTraceSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRayTraceInferiors = new Hashtable<String, String[]>(0);

	public static void readEntityFromNBT(IClientPlayerAPI target, net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isReadEntityFromNBTModded)
			clientPlayerAPI.readEntityFromNBT(paramNBTTagCompound);
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

	protected ClientPlayerBase GetOverwrittenReadEntityFromNBT(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeReadEntityFromNBTHooks;
	private ClientPlayerBase[] overrideReadEntityFromNBTHooks;
	private ClientPlayerBase[] afterReadEntityFromNBTHooks;

	public boolean isReadEntityFromNBTModded;

	private static final Map<String, String[]> allBaseBeforeReadEntityFromNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeReadEntityFromNBTInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideReadEntityFromNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideReadEntityFromNBTInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterReadEntityFromNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterReadEntityFromNBTInferiors = new Hashtable<String, String[]>(0);

	public static void respawnPlayer(IClientPlayerAPI target)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isRespawnPlayerModded)
			clientPlayerAPI.respawnPlayer();
		else
			target.localRespawnPlayer();
	}

	private void respawnPlayer()
	{
		if(beforeRespawnPlayerHooks != null)
			for(int i = beforeRespawnPlayerHooks.length - 1; i >= 0 ; i--)
				beforeRespawnPlayerHooks[i].beforeRespawnPlayer();

		if(overrideRespawnPlayerHooks != null)
			overrideRespawnPlayerHooks[overrideRespawnPlayerHooks.length - 1].respawnPlayer();
		else
			player.localRespawnPlayer();

		if(afterRespawnPlayerHooks != null)
			for(int i = 0; i < afterRespawnPlayerHooks.length; i++)
				afterRespawnPlayerHooks[i].afterRespawnPlayer();

	}

	protected ClientPlayerBase GetOverwrittenRespawnPlayer(ClientPlayerBase overWriter)
	{
		if (overrideRespawnPlayerHooks == null)
			return overWriter;

		for(int i = 0; i < overrideRespawnPlayerHooks.length; i++)
			if(overrideRespawnPlayerHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRespawnPlayerHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRespawnPlayerHookTypes = new LinkedList<String>();
	private final static List<String> overrideRespawnPlayerHookTypes = new LinkedList<String>();
	private final static List<String> afterRespawnPlayerHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeRespawnPlayerHooks;
	private ClientPlayerBase[] overrideRespawnPlayerHooks;
	private ClientPlayerBase[] afterRespawnPlayerHooks;

	public boolean isRespawnPlayerModded;

	private static final Map<String, String[]> allBaseBeforeRespawnPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRespawnPlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRespawnPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRespawnPlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRespawnPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRespawnPlayerInferiors = new Hashtable<String, String[]>(0);

	public static void setDead(IClientPlayerAPI target)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isSetDeadModded)
			clientPlayerAPI.setDead();
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

	protected ClientPlayerBase GetOverwrittenSetDead(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeSetDeadHooks;
	private ClientPlayerBase[] overrideSetDeadHooks;
	private ClientPlayerBase[] afterSetDeadHooks;

	public boolean isSetDeadModded;

	private static final Map<String, String[]> allBaseBeforeSetDeadSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetDeadInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetDeadSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetDeadInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetDeadSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetDeadInferiors = new Hashtable<String, String[]>(0);

	public static void setPlayerSPHealth(IClientPlayerAPI target, float paramFloat)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isSetPlayerSPHealthModded)
			clientPlayerAPI.setPlayerSPHealth(paramFloat);
		else
			target.localSetPlayerSPHealth(paramFloat);
	}

	private void setPlayerSPHealth(float paramFloat)
	{
		if(beforeSetPlayerSPHealthHooks != null)
			for(int i = beforeSetPlayerSPHealthHooks.length - 1; i >= 0 ; i--)
				beforeSetPlayerSPHealthHooks[i].beforeSetPlayerSPHealth(paramFloat);

		if(overrideSetPlayerSPHealthHooks != null)
			overrideSetPlayerSPHealthHooks[overrideSetPlayerSPHealthHooks.length - 1].setPlayerSPHealth(paramFloat);
		else
			player.localSetPlayerSPHealth(paramFloat);

		if(afterSetPlayerSPHealthHooks != null)
			for(int i = 0; i < afterSetPlayerSPHealthHooks.length; i++)
				afterSetPlayerSPHealthHooks[i].afterSetPlayerSPHealth(paramFloat);

	}

	protected ClientPlayerBase GetOverwrittenSetPlayerSPHealth(ClientPlayerBase overWriter)
	{
		if (overrideSetPlayerSPHealthHooks == null)
			return overWriter;

		for(int i = 0; i < overrideSetPlayerSPHealthHooks.length; i++)
			if(overrideSetPlayerSPHealthHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetPlayerSPHealthHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetPlayerSPHealthHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetPlayerSPHealthHookTypes = new LinkedList<String>();
	private final static List<String> afterSetPlayerSPHealthHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeSetPlayerSPHealthHooks;
	private ClientPlayerBase[] overrideSetPlayerSPHealthHooks;
	private ClientPlayerBase[] afterSetPlayerSPHealthHooks;

	public boolean isSetPlayerSPHealthModded;

	private static final Map<String, String[]> allBaseBeforeSetPlayerSPHealthSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetPlayerSPHealthInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetPlayerSPHealthSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetPlayerSPHealthInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetPlayerSPHealthSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetPlayerSPHealthInferiors = new Hashtable<String, String[]>(0);

	public static void setPositionAndRotation(IClientPlayerAPI target, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isSetPositionAndRotationModded)
			clientPlayerAPI.setPositionAndRotation(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
		else
			target.localSetPositionAndRotation(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
	}

	private void setPositionAndRotation(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
		if(beforeSetPositionAndRotationHooks != null)
			for(int i = beforeSetPositionAndRotationHooks.length - 1; i >= 0 ; i--)
				beforeSetPositionAndRotationHooks[i].beforeSetPositionAndRotation(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

		if(overrideSetPositionAndRotationHooks != null)
			overrideSetPositionAndRotationHooks[overrideSetPositionAndRotationHooks.length - 1].setPositionAndRotation(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
		else
			player.localSetPositionAndRotation(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

		if(afterSetPositionAndRotationHooks != null)
			for(int i = 0; i < afterSetPositionAndRotationHooks.length; i++)
				afterSetPositionAndRotationHooks[i].afterSetPositionAndRotation(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

	}

	protected ClientPlayerBase GetOverwrittenSetPositionAndRotation(ClientPlayerBase overWriter)
	{
		if (overrideSetPositionAndRotationHooks == null)
			return overWriter;

		for(int i = 0; i < overrideSetPositionAndRotationHooks.length; i++)
			if(overrideSetPositionAndRotationHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetPositionAndRotationHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetPositionAndRotationHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetPositionAndRotationHookTypes = new LinkedList<String>();
	private final static List<String> afterSetPositionAndRotationHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeSetPositionAndRotationHooks;
	private ClientPlayerBase[] overrideSetPositionAndRotationHooks;
	private ClientPlayerBase[] afterSetPositionAndRotationHooks;

	public boolean isSetPositionAndRotationModded;

	private static final Map<String, String[]> allBaseBeforeSetPositionAndRotationSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetPositionAndRotationInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetPositionAndRotationSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetPositionAndRotationInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetPositionAndRotationSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetPositionAndRotationInferiors = new Hashtable<String, String[]>(0);

	public static void setSneaking(IClientPlayerAPI target, boolean paramBoolean)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isSetSneakingModded)
			clientPlayerAPI.setSneaking(paramBoolean);
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

	protected ClientPlayerBase GetOverwrittenSetSneaking(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeSetSneakingHooks;
	private ClientPlayerBase[] overrideSetSneakingHooks;
	private ClientPlayerBase[] afterSetSneakingHooks;

	public boolean isSetSneakingModded;

	private static final Map<String, String[]> allBaseBeforeSetSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetSneakingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetSneakingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetSneakingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetSneakingInferiors = new Hashtable<String, String[]>(0);

	public static void setSprinting(IClientPlayerAPI target, boolean paramBoolean)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isSetSprintingModded)
			clientPlayerAPI.setSprinting(paramBoolean);
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

	protected ClientPlayerBase GetOverwrittenSetSprinting(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeSetSprintingHooks;
	private ClientPlayerBase[] overrideSetSprintingHooks;
	private ClientPlayerBase[] afterSetSprintingHooks;

	public boolean isSetSprintingModded;

	private static final Map<String, String[]> allBaseBeforeSetSprintingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetSprintingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetSprintingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetSprintingInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetSprintingSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetSprintingInferiors = new Hashtable<String, String[]>(0);

	public static net.minecraft.entity.player.EntityPlayer.EnumStatus sleepInBedAt(IClientPlayerAPI target, int paramInt1, int paramInt2, int paramInt3)
	{
		net.minecraft.entity.player.EntityPlayer.EnumStatus _result;
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isSleepInBedAtModded)
			_result = clientPlayerAPI.sleepInBedAt(paramInt1, paramInt2, paramInt3);
		else
			_result = target.localSleepInBedAt(paramInt1, paramInt2, paramInt3);
		return _result;
	}

	private net.minecraft.entity.player.EntityPlayer.EnumStatus sleepInBedAt(int paramInt1, int paramInt2, int paramInt3)
	{
		if(beforeSleepInBedAtHooks != null)
			for(int i = beforeSleepInBedAtHooks.length - 1; i >= 0 ; i--)
				beforeSleepInBedAtHooks[i].beforeSleepInBedAt(paramInt1, paramInt2, paramInt3);

		net.minecraft.entity.player.EntityPlayer.EnumStatus _result;
		if(overrideSleepInBedAtHooks != null)
			_result = overrideSleepInBedAtHooks[overrideSleepInBedAtHooks.length - 1].sleepInBedAt(paramInt1, paramInt2, paramInt3);
		else
			_result = player.localSleepInBedAt(paramInt1, paramInt2, paramInt3);

		if(afterSleepInBedAtHooks != null)
			for(int i = 0; i < afterSleepInBedAtHooks.length; i++)
				afterSleepInBedAtHooks[i].afterSleepInBedAt(paramInt1, paramInt2, paramInt3);

		return _result;
	}

	protected ClientPlayerBase GetOverwrittenSleepInBedAt(ClientPlayerBase overWriter)
	{
		if (overrideSleepInBedAtHooks == null)
			return overWriter;

		for(int i = 0; i < overrideSleepInBedAtHooks.length; i++)
			if(overrideSleepInBedAtHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSleepInBedAtHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSleepInBedAtHookTypes = new LinkedList<String>();
	private final static List<String> overrideSleepInBedAtHookTypes = new LinkedList<String>();
	private final static List<String> afterSleepInBedAtHookTypes = new LinkedList<String>();

	private ClientPlayerBase[] beforeSleepInBedAtHooks;
	private ClientPlayerBase[] overrideSleepInBedAtHooks;
	private ClientPlayerBase[] afterSleepInBedAtHooks;

	public boolean isSleepInBedAtModded;

	private static final Map<String, String[]> allBaseBeforeSleepInBedAtSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSleepInBedAtInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSleepInBedAtSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSleepInBedAtInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSleepInBedAtSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSleepInBedAtInferiors = new Hashtable<String, String[]>(0);

	public static void swingItem(IClientPlayerAPI target)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isSwingItemModded)
			clientPlayerAPI.swingItem();
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

	protected ClientPlayerBase GetOverwrittenSwingItem(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeSwingItemHooks;
	private ClientPlayerBase[] overrideSwingItemHooks;
	private ClientPlayerBase[] afterSwingItemHooks;

	public boolean isSwingItemModded;

	private static final Map<String, String[]> allBaseBeforeSwingItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSwingItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSwingItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSwingItemInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSwingItemSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSwingItemInferiors = new Hashtable<String, String[]>(0);

	public static void updateEntityActionState(IClientPlayerAPI target)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isUpdateEntityActionStateModded)
			clientPlayerAPI.updateEntityActionState();
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

	protected ClientPlayerBase GetOverwrittenUpdateEntityActionState(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeUpdateEntityActionStateHooks;
	private ClientPlayerBase[] overrideUpdateEntityActionStateHooks;
	private ClientPlayerBase[] afterUpdateEntityActionStateHooks;

	public boolean isUpdateEntityActionStateModded;

	private static final Map<String, String[]> allBaseBeforeUpdateEntityActionStateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeUpdateEntityActionStateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdateEntityActionStateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdateEntityActionStateInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdateEntityActionStateSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdateEntityActionStateInferiors = new Hashtable<String, String[]>(0);

	public static void updateRidden(IClientPlayerAPI target)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isUpdateRiddenModded)
			clientPlayerAPI.updateRidden();
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

	protected ClientPlayerBase GetOverwrittenUpdateRidden(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeUpdateRiddenHooks;
	private ClientPlayerBase[] overrideUpdateRiddenHooks;
	private ClientPlayerBase[] afterUpdateRiddenHooks;

	public boolean isUpdateRiddenModded;

	private static final Map<String, String[]> allBaseBeforeUpdateRiddenSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeUpdateRiddenInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdateRiddenSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdateRiddenInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdateRiddenSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdateRiddenInferiors = new Hashtable<String, String[]>(0);

	public static void wakeUpPlayer(IClientPlayerAPI target, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isWakeUpPlayerModded)
			clientPlayerAPI.wakeUpPlayer(paramBoolean1, paramBoolean2, paramBoolean3);
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

	protected ClientPlayerBase GetOverwrittenWakeUpPlayer(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeWakeUpPlayerHooks;
	private ClientPlayerBase[] overrideWakeUpPlayerHooks;
	private ClientPlayerBase[] afterWakeUpPlayerHooks;

	public boolean isWakeUpPlayerModded;

	private static final Map<String, String[]> allBaseBeforeWakeUpPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeWakeUpPlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideWakeUpPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideWakeUpPlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterWakeUpPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterWakeUpPlayerInferiors = new Hashtable<String, String[]>(0);

	public static void writeEntityToNBT(IClientPlayerAPI target, net.minecraft.nbt.NBTTagCompound paramNBTTagCompound)
	{
		ClientPlayerAPI clientPlayerAPI = target.getClientPlayerAPI();
		if(clientPlayerAPI != null && clientPlayerAPI.isWriteEntityToNBTModded)
			clientPlayerAPI.writeEntityToNBT(paramNBTTagCompound);
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

	protected ClientPlayerBase GetOverwrittenWriteEntityToNBT(ClientPlayerBase overWriter)
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

	private ClientPlayerBase[] beforeWriteEntityToNBTHooks;
	private ClientPlayerBase[] overrideWriteEntityToNBTHooks;
	private ClientPlayerBase[] afterWriteEntityToNBTHooks;

	public boolean isWriteEntityToNBTModded;

	private static final Map<String, String[]> allBaseBeforeWriteEntityToNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeWriteEntityToNBTInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideWriteEntityToNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideWriteEntityToNBTInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterWriteEntityToNBTSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterWriteEntityToNBTInferiors = new Hashtable<String, String[]>(0);

	
	protected final IClientPlayerAPI player;

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

	private ClientPlayerBase[] beforeLocalConstructingHooks;
	private ClientPlayerBase[] afterLocalConstructingHooks;

	private final Map<ClientPlayerBase, String> baseObjectsToId = new Hashtable<ClientPlayerBase, String>();
	private final Map<String, ClientPlayerBase> allBaseObjects = new Hashtable<String, ClientPlayerBase>();
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
