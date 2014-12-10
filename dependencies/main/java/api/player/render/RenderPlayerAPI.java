package api.player.render;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.lang.reflect.*;

public final class RenderPlayerAPI
{
	private final static Class<?>[] Class = new Class[] { RenderPlayerAPI.class };
	private final static Class<?>[] Classes = new Class[] { RenderPlayerAPI.class, String.class };

	private static boolean isCreated;
	private static final Logger logger = Logger.getLogger("RenderPlayerAPI");

	private static void log(String text)
	{
		System.out.println(text);
		logger.fine(text);
	}

	public static void register(String id, Class<?> baseClass)
	{
		register(id, baseClass, null);
	}

	public static void register(String id, Class<?> baseClass, RenderPlayerBaseSorting baseSorting)
	{
		try
		{
			register(baseClass, id, baseSorting);
		}
		catch(RuntimeException exception)
		{
			if(id != null)
				log("Render Player: failed to register id '" + id + "'");
			else
				log("Render Player: failed to register RenderPlayerBase");

			throw exception;
		}
	}

	private static void register(Class<?> baseClass, String id, RenderPlayerBaseSorting baseSorting)
	{
		if(!isCreated)
		{
			try
			{
				Method mandatory = net.minecraft.client.renderer.entity.RenderPlayer.class.getMethod("getRenderPlayerBase", String.class);
				if (mandatory.getReturnType() != RenderPlayerBase.class)
					throw new NoSuchMethodException(RenderPlayerBase.class.getName() + " " + net.minecraft.client.renderer.entity.RenderPlayer.class.getName() + ".getRenderPlayerBase(" + String.class.getName() + ")");
			}
			catch(NoSuchMethodException exception)
			{
				String[] errorMessageParts = new String[]
				{
					"========================================",
					"The API \"Render Player\" version 1.0 of the mod \"Render Player API core 1.0\" can not be created!",
					"----------------------------------------",
					"Mandatory member method \"{0} getRenderPlayerBase({3})\" not found in class \"{1}\".",
					"There are three scenarios this can happen:",
					"* Minecraft Forge is missing a Render Player API core which Minecraft version matches its own.",
					"  Download and install the latest Render Player API core for the Minecraft version you were trying to run.",
					"* The code of the class \"{2}\" of Render Player API core has been modified beyond recognition by another Minecraft Forge coremod.",
					"  Try temporary deinstallation of other core mods to find the culprit and deinstall it permanently to fix this specific problem.",
					"* Render Player API core has not been installed correctly.",
					"  Deinstall Render Player API core and install it again following the installation instructions in the readme file.",
					"========================================"
				};

				String baseRenderPlayerClassName = RenderPlayerBase.class.getName();
				String targetClassName = net.minecraft.client.renderer.entity.RenderPlayer.class.getName();
				String targetClassFileName = targetClassName.replace(".", File.separator);
				String stringClassName = String.class.getName();

				for(int i=0; i<errorMessageParts.length; i++)
					errorMessageParts[i] = MessageFormat.format(errorMessageParts[i], baseRenderPlayerClassName, targetClassName, targetClassFileName, stringClassName);

				for(String errorMessagePart : errorMessageParts)
					logger.severe(errorMessagePart);

				for(String errorMessagePart : errorMessageParts)
					System.err.println(errorMessagePart);

				String errorMessage = "\n\n";
				for(String errorMessagePart : errorMessageParts)
					errorMessage += "\t" + errorMessagePart + "\n";

				throw new RuntimeException(errorMessage, exception);
			}

			log("Render Player 1.0 Created");
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
				throw new IllegalArgumentException("Can not find necessary constructor with one argument of type '" + RenderPlayerAPI.class.getName() + "' and eventually a second argument of type 'String' in the class '" + baseClass.getName() + "'", t);
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

			addSorting(id, allBaseBeforeDoRenderLabelSuperiors, baseSorting.getBeforeDoRenderLabelSuperiors());
			addSorting(id, allBaseBeforeDoRenderLabelInferiors, baseSorting.getBeforeDoRenderLabelInferiors());
			addSorting(id, allBaseOverrideDoRenderLabelSuperiors, baseSorting.getOverrideDoRenderLabelSuperiors());
			addSorting(id, allBaseOverrideDoRenderLabelInferiors, baseSorting.getOverrideDoRenderLabelInferiors());
			addSorting(id, allBaseAfterDoRenderLabelSuperiors, baseSorting.getAfterDoRenderLabelSuperiors());
			addSorting(id, allBaseAfterDoRenderLabelInferiors, baseSorting.getAfterDoRenderLabelInferiors());

			addSorting(id, allBaseBeforeDoRenderShadowAndFireSuperiors, baseSorting.getBeforeDoRenderShadowAndFireSuperiors());
			addSorting(id, allBaseBeforeDoRenderShadowAndFireInferiors, baseSorting.getBeforeDoRenderShadowAndFireInferiors());
			addSorting(id, allBaseOverrideDoRenderShadowAndFireSuperiors, baseSorting.getOverrideDoRenderShadowAndFireSuperiors());
			addSorting(id, allBaseOverrideDoRenderShadowAndFireInferiors, baseSorting.getOverrideDoRenderShadowAndFireInferiors());
			addSorting(id, allBaseAfterDoRenderShadowAndFireSuperiors, baseSorting.getAfterDoRenderShadowAndFireSuperiors());
			addSorting(id, allBaseAfterDoRenderShadowAndFireInferiors, baseSorting.getAfterDoRenderShadowAndFireInferiors());

			addSorting(id, allBaseBeforeGetColorMultiplierSuperiors, baseSorting.getBeforeGetColorMultiplierSuperiors());
			addSorting(id, allBaseBeforeGetColorMultiplierInferiors, baseSorting.getBeforeGetColorMultiplierInferiors());
			addSorting(id, allBaseOverrideGetColorMultiplierSuperiors, baseSorting.getOverrideGetColorMultiplierSuperiors());
			addSorting(id, allBaseOverrideGetColorMultiplierInferiors, baseSorting.getOverrideGetColorMultiplierInferiors());
			addSorting(id, allBaseAfterGetColorMultiplierSuperiors, baseSorting.getAfterGetColorMultiplierSuperiors());
			addSorting(id, allBaseAfterGetColorMultiplierInferiors, baseSorting.getAfterGetColorMultiplierInferiors());

			addSorting(id, allBaseBeforeGetDeathMaxRotationSuperiors, baseSorting.getBeforeGetDeathMaxRotationSuperiors());
			addSorting(id, allBaseBeforeGetDeathMaxRotationInferiors, baseSorting.getBeforeGetDeathMaxRotationInferiors());
			addSorting(id, allBaseOverrideGetDeathMaxRotationSuperiors, baseSorting.getOverrideGetDeathMaxRotationSuperiors());
			addSorting(id, allBaseOverrideGetDeathMaxRotationInferiors, baseSorting.getOverrideGetDeathMaxRotationInferiors());
			addSorting(id, allBaseAfterGetDeathMaxRotationSuperiors, baseSorting.getAfterGetDeathMaxRotationSuperiors());
			addSorting(id, allBaseAfterGetDeathMaxRotationInferiors, baseSorting.getAfterGetDeathMaxRotationInferiors());

			addSorting(id, allBaseBeforeGetFontRendererFromRenderManagerSuperiors, baseSorting.getBeforeGetFontRendererFromRenderManagerSuperiors());
			addSorting(id, allBaseBeforeGetFontRendererFromRenderManagerInferiors, baseSorting.getBeforeGetFontRendererFromRenderManagerInferiors());
			addSorting(id, allBaseOverrideGetFontRendererFromRenderManagerSuperiors, baseSorting.getOverrideGetFontRendererFromRenderManagerSuperiors());
			addSorting(id, allBaseOverrideGetFontRendererFromRenderManagerInferiors, baseSorting.getOverrideGetFontRendererFromRenderManagerInferiors());
			addSorting(id, allBaseAfterGetFontRendererFromRenderManagerSuperiors, baseSorting.getAfterGetFontRendererFromRenderManagerSuperiors());
			addSorting(id, allBaseAfterGetFontRendererFromRenderManagerInferiors, baseSorting.getAfterGetFontRendererFromRenderManagerInferiors());

			addSorting(id, allBaseBeforeGetResourceLocationFromPlayerSuperiors, baseSorting.getBeforeGetResourceLocationFromPlayerSuperiors());
			addSorting(id, allBaseBeforeGetResourceLocationFromPlayerInferiors, baseSorting.getBeforeGetResourceLocationFromPlayerInferiors());
			addSorting(id, allBaseOverrideGetResourceLocationFromPlayerSuperiors, baseSorting.getOverrideGetResourceLocationFromPlayerSuperiors());
			addSorting(id, allBaseOverrideGetResourceLocationFromPlayerInferiors, baseSorting.getOverrideGetResourceLocationFromPlayerInferiors());
			addSorting(id, allBaseAfterGetResourceLocationFromPlayerSuperiors, baseSorting.getAfterGetResourceLocationFromPlayerSuperiors());
			addSorting(id, allBaseAfterGetResourceLocationFromPlayerInferiors, baseSorting.getAfterGetResourceLocationFromPlayerInferiors());

			addSorting(id, allBaseBeforeHandleRotationFloatSuperiors, baseSorting.getBeforeHandleRotationFloatSuperiors());
			addSorting(id, allBaseBeforeHandleRotationFloatInferiors, baseSorting.getBeforeHandleRotationFloatInferiors());
			addSorting(id, allBaseOverrideHandleRotationFloatSuperiors, baseSorting.getOverrideHandleRotationFloatSuperiors());
			addSorting(id, allBaseOverrideHandleRotationFloatInferiors, baseSorting.getOverrideHandleRotationFloatInferiors());
			addSorting(id, allBaseAfterHandleRotationFloatSuperiors, baseSorting.getAfterHandleRotationFloatSuperiors());
			addSorting(id, allBaseAfterHandleRotationFloatInferiors, baseSorting.getAfterHandleRotationFloatInferiors());

			addSorting(id, allBaseBeforeInheritRenderPassSuperiors, baseSorting.getBeforeInheritRenderPassSuperiors());
			addSorting(id, allBaseBeforeInheritRenderPassInferiors, baseSorting.getBeforeInheritRenderPassInferiors());
			addSorting(id, allBaseOverrideInheritRenderPassSuperiors, baseSorting.getOverrideInheritRenderPassSuperiors());
			addSorting(id, allBaseOverrideInheritRenderPassInferiors, baseSorting.getOverrideInheritRenderPassInferiors());
			addSorting(id, allBaseAfterInheritRenderPassSuperiors, baseSorting.getAfterInheritRenderPassSuperiors());
			addSorting(id, allBaseAfterInheritRenderPassInferiors, baseSorting.getAfterInheritRenderPassInferiors());

			addSorting(id, allBaseBeforeLoadTextureSuperiors, baseSorting.getBeforeLoadTextureSuperiors());
			addSorting(id, allBaseBeforeLoadTextureInferiors, baseSorting.getBeforeLoadTextureInferiors());
			addSorting(id, allBaseOverrideLoadTextureSuperiors, baseSorting.getOverrideLoadTextureSuperiors());
			addSorting(id, allBaseOverrideLoadTextureInferiors, baseSorting.getOverrideLoadTextureInferiors());
			addSorting(id, allBaseAfterLoadTextureSuperiors, baseSorting.getAfterLoadTextureSuperiors());
			addSorting(id, allBaseAfterLoadTextureInferiors, baseSorting.getAfterLoadTextureInferiors());

			addSorting(id, allBaseBeforeLoadTextureOfEntitySuperiors, baseSorting.getBeforeLoadTextureOfEntitySuperiors());
			addSorting(id, allBaseBeforeLoadTextureOfEntityInferiors, baseSorting.getBeforeLoadTextureOfEntityInferiors());
			addSorting(id, allBaseOverrideLoadTextureOfEntitySuperiors, baseSorting.getOverrideLoadTextureOfEntitySuperiors());
			addSorting(id, allBaseOverrideLoadTextureOfEntityInferiors, baseSorting.getOverrideLoadTextureOfEntityInferiors());
			addSorting(id, allBaseAfterLoadTextureOfEntitySuperiors, baseSorting.getAfterLoadTextureOfEntitySuperiors());
			addSorting(id, allBaseAfterLoadTextureOfEntityInferiors, baseSorting.getAfterLoadTextureOfEntityInferiors());

			addSorting(id, allBaseBeforePassSpecialRenderSuperiors, baseSorting.getBeforePassSpecialRenderSuperiors());
			addSorting(id, allBaseBeforePassSpecialRenderInferiors, baseSorting.getBeforePassSpecialRenderInferiors());
			addSorting(id, allBaseOverridePassSpecialRenderSuperiors, baseSorting.getOverridePassSpecialRenderSuperiors());
			addSorting(id, allBaseOverridePassSpecialRenderInferiors, baseSorting.getOverridePassSpecialRenderInferiors());
			addSorting(id, allBaseAfterPassSpecialRenderSuperiors, baseSorting.getAfterPassSpecialRenderSuperiors());
			addSorting(id, allBaseAfterPassSpecialRenderInferiors, baseSorting.getAfterPassSpecialRenderInferiors());

			addSorting(id, allBaseBeforePerformStaticEntityRebuildSuperiors, baseSorting.getBeforePerformStaticEntityRebuildSuperiors());
			addSorting(id, allBaseBeforePerformStaticEntityRebuildInferiors, baseSorting.getBeforePerformStaticEntityRebuildInferiors());
			addSorting(id, allBaseOverridePerformStaticEntityRebuildSuperiors, baseSorting.getOverridePerformStaticEntityRebuildSuperiors());
			addSorting(id, allBaseOverridePerformStaticEntityRebuildInferiors, baseSorting.getOverridePerformStaticEntityRebuildInferiors());
			addSorting(id, allBaseAfterPerformStaticEntityRebuildSuperiors, baseSorting.getAfterPerformStaticEntityRebuildSuperiors());
			addSorting(id, allBaseAfterPerformStaticEntityRebuildInferiors, baseSorting.getAfterPerformStaticEntityRebuildInferiors());

			addSorting(id, allBaseBeforeRenderArrowsStuckInEntitySuperiors, baseSorting.getBeforeRenderArrowsStuckInEntitySuperiors());
			addSorting(id, allBaseBeforeRenderArrowsStuckInEntityInferiors, baseSorting.getBeforeRenderArrowsStuckInEntityInferiors());
			addSorting(id, allBaseOverrideRenderArrowsStuckInEntitySuperiors, baseSorting.getOverrideRenderArrowsStuckInEntitySuperiors());
			addSorting(id, allBaseOverrideRenderArrowsStuckInEntityInferiors, baseSorting.getOverrideRenderArrowsStuckInEntityInferiors());
			addSorting(id, allBaseAfterRenderArrowsStuckInEntitySuperiors, baseSorting.getAfterRenderArrowsStuckInEntitySuperiors());
			addSorting(id, allBaseAfterRenderArrowsStuckInEntityInferiors, baseSorting.getAfterRenderArrowsStuckInEntityInferiors());

			addSorting(id, allBaseBeforeRenderFirstPersonArmSuperiors, baseSorting.getBeforeRenderFirstPersonArmSuperiors());
			addSorting(id, allBaseBeforeRenderFirstPersonArmInferiors, baseSorting.getBeforeRenderFirstPersonArmInferiors());
			addSorting(id, allBaseOverrideRenderFirstPersonArmSuperiors, baseSorting.getOverrideRenderFirstPersonArmSuperiors());
			addSorting(id, allBaseOverrideRenderFirstPersonArmInferiors, baseSorting.getOverrideRenderFirstPersonArmInferiors());
			addSorting(id, allBaseAfterRenderFirstPersonArmSuperiors, baseSorting.getAfterRenderFirstPersonArmSuperiors());
			addSorting(id, allBaseAfterRenderFirstPersonArmInferiors, baseSorting.getAfterRenderFirstPersonArmInferiors());

			addSorting(id, allBaseBeforeRenderLivingLabelSuperiors, baseSorting.getBeforeRenderLivingLabelSuperiors());
			addSorting(id, allBaseBeforeRenderLivingLabelInferiors, baseSorting.getBeforeRenderLivingLabelInferiors());
			addSorting(id, allBaseOverrideRenderLivingLabelSuperiors, baseSorting.getOverrideRenderLivingLabelSuperiors());
			addSorting(id, allBaseOverrideRenderLivingLabelInferiors, baseSorting.getOverrideRenderLivingLabelInferiors());
			addSorting(id, allBaseAfterRenderLivingLabelSuperiors, baseSorting.getAfterRenderLivingLabelSuperiors());
			addSorting(id, allBaseAfterRenderLivingLabelInferiors, baseSorting.getAfterRenderLivingLabelInferiors());

			addSorting(id, allBaseBeforeRenderModelSuperiors, baseSorting.getBeforeRenderModelSuperiors());
			addSorting(id, allBaseBeforeRenderModelInferiors, baseSorting.getBeforeRenderModelInferiors());
			addSorting(id, allBaseOverrideRenderModelSuperiors, baseSorting.getOverrideRenderModelSuperiors());
			addSorting(id, allBaseOverrideRenderModelInferiors, baseSorting.getOverrideRenderModelInferiors());
			addSorting(id, allBaseAfterRenderModelSuperiors, baseSorting.getAfterRenderModelSuperiors());
			addSorting(id, allBaseAfterRenderModelInferiors, baseSorting.getAfterRenderModelInferiors());

			addSorting(id, allBaseBeforeRenderPlayerSuperiors, baseSorting.getBeforeRenderPlayerSuperiors());
			addSorting(id, allBaseBeforeRenderPlayerInferiors, baseSorting.getBeforeRenderPlayerInferiors());
			addSorting(id, allBaseOverrideRenderPlayerSuperiors, baseSorting.getOverrideRenderPlayerSuperiors());
			addSorting(id, allBaseOverrideRenderPlayerInferiors, baseSorting.getOverrideRenderPlayerInferiors());
			addSorting(id, allBaseAfterRenderPlayerSuperiors, baseSorting.getAfterRenderPlayerSuperiors());
			addSorting(id, allBaseAfterRenderPlayerInferiors, baseSorting.getAfterRenderPlayerInferiors());

			addSorting(id, allBaseBeforeRenderPlayerNameAndScoreLabelSuperiors, baseSorting.getBeforeRenderPlayerNameAndScoreLabelSuperiors());
			addSorting(id, allBaseBeforeRenderPlayerNameAndScoreLabelInferiors, baseSorting.getBeforeRenderPlayerNameAndScoreLabelInferiors());
			addSorting(id, allBaseOverrideRenderPlayerNameAndScoreLabelSuperiors, baseSorting.getOverrideRenderPlayerNameAndScoreLabelSuperiors());
			addSorting(id, allBaseOverrideRenderPlayerNameAndScoreLabelInferiors, baseSorting.getOverrideRenderPlayerNameAndScoreLabelInferiors());
			addSorting(id, allBaseAfterRenderPlayerNameAndScoreLabelSuperiors, baseSorting.getAfterRenderPlayerNameAndScoreLabelSuperiors());
			addSorting(id, allBaseAfterRenderPlayerNameAndScoreLabelInferiors, baseSorting.getAfterRenderPlayerNameAndScoreLabelInferiors());

			addSorting(id, allBaseBeforeRenderPlayerScaleSuperiors, baseSorting.getBeforeRenderPlayerScaleSuperiors());
			addSorting(id, allBaseBeforeRenderPlayerScaleInferiors, baseSorting.getBeforeRenderPlayerScaleInferiors());
			addSorting(id, allBaseOverrideRenderPlayerScaleSuperiors, baseSorting.getOverrideRenderPlayerScaleSuperiors());
			addSorting(id, allBaseOverrideRenderPlayerScaleInferiors, baseSorting.getOverrideRenderPlayerScaleInferiors());
			addSorting(id, allBaseAfterRenderPlayerScaleSuperiors, baseSorting.getAfterRenderPlayerScaleSuperiors());
			addSorting(id, allBaseAfterRenderPlayerScaleInferiors, baseSorting.getAfterRenderPlayerScaleInferiors());

			addSorting(id, allBaseBeforeRenderPlayerSleepSuperiors, baseSorting.getBeforeRenderPlayerSleepSuperiors());
			addSorting(id, allBaseBeforeRenderPlayerSleepInferiors, baseSorting.getBeforeRenderPlayerSleepInferiors());
			addSorting(id, allBaseOverrideRenderPlayerSleepSuperiors, baseSorting.getOverrideRenderPlayerSleepSuperiors());
			addSorting(id, allBaseOverrideRenderPlayerSleepInferiors, baseSorting.getOverrideRenderPlayerSleepInferiors());
			addSorting(id, allBaseAfterRenderPlayerSleepSuperiors, baseSorting.getAfterRenderPlayerSleepSuperiors());
			addSorting(id, allBaseAfterRenderPlayerSleepInferiors, baseSorting.getAfterRenderPlayerSleepInferiors());

			addSorting(id, allBaseBeforeRenderSpecialsSuperiors, baseSorting.getBeforeRenderSpecialsSuperiors());
			addSorting(id, allBaseBeforeRenderSpecialsInferiors, baseSorting.getBeforeRenderSpecialsInferiors());
			addSorting(id, allBaseOverrideRenderSpecialsSuperiors, baseSorting.getOverrideRenderSpecialsSuperiors());
			addSorting(id, allBaseOverrideRenderSpecialsInferiors, baseSorting.getOverrideRenderSpecialsInferiors());
			addSorting(id, allBaseAfterRenderSpecialsSuperiors, baseSorting.getAfterRenderSpecialsSuperiors());
			addSorting(id, allBaseAfterRenderSpecialsInferiors, baseSorting.getAfterRenderSpecialsInferiors());

			addSorting(id, allBaseBeforeRenderSwingProgressSuperiors, baseSorting.getBeforeRenderSwingProgressSuperiors());
			addSorting(id, allBaseBeforeRenderSwingProgressInferiors, baseSorting.getBeforeRenderSwingProgressInferiors());
			addSorting(id, allBaseOverrideRenderSwingProgressSuperiors, baseSorting.getOverrideRenderSwingProgressSuperiors());
			addSorting(id, allBaseOverrideRenderSwingProgressInferiors, baseSorting.getOverrideRenderSwingProgressInferiors());
			addSorting(id, allBaseAfterRenderSwingProgressSuperiors, baseSorting.getAfterRenderSwingProgressSuperiors());
			addSorting(id, allBaseAfterRenderSwingProgressInferiors, baseSorting.getAfterRenderSwingProgressInferiors());

			addSorting(id, allBaseBeforeRotatePlayerSuperiors, baseSorting.getBeforeRotatePlayerSuperiors());
			addSorting(id, allBaseBeforeRotatePlayerInferiors, baseSorting.getBeforeRotatePlayerInferiors());
			addSorting(id, allBaseOverrideRotatePlayerSuperiors, baseSorting.getOverrideRotatePlayerSuperiors());
			addSorting(id, allBaseOverrideRotatePlayerInferiors, baseSorting.getOverrideRotatePlayerInferiors());
			addSorting(id, allBaseAfterRotatePlayerSuperiors, baseSorting.getAfterRotatePlayerSuperiors());
			addSorting(id, allBaseAfterRotatePlayerInferiors, baseSorting.getAfterRotatePlayerInferiors());

			addSorting(id, allBaseBeforeSetArmorModelSuperiors, baseSorting.getBeforeSetArmorModelSuperiors());
			addSorting(id, allBaseBeforeSetArmorModelInferiors, baseSorting.getBeforeSetArmorModelInferiors());
			addSorting(id, allBaseOverrideSetArmorModelSuperiors, baseSorting.getOverrideSetArmorModelSuperiors());
			addSorting(id, allBaseOverrideSetArmorModelInferiors, baseSorting.getOverrideSetArmorModelInferiors());
			addSorting(id, allBaseAfterSetArmorModelSuperiors, baseSorting.getAfterSetArmorModelSuperiors());
			addSorting(id, allBaseAfterSetArmorModelInferiors, baseSorting.getAfterSetArmorModelInferiors());

			addSorting(id, allBaseBeforeSetPassArmorModelSuperiors, baseSorting.getBeforeSetPassArmorModelSuperiors());
			addSorting(id, allBaseBeforeSetPassArmorModelInferiors, baseSorting.getBeforeSetPassArmorModelInferiors());
			addSorting(id, allBaseOverrideSetPassArmorModelSuperiors, baseSorting.getOverrideSetPassArmorModelSuperiors());
			addSorting(id, allBaseOverrideSetPassArmorModelInferiors, baseSorting.getOverrideSetPassArmorModelInferiors());
			addSorting(id, allBaseAfterSetPassArmorModelSuperiors, baseSorting.getAfterSetPassArmorModelSuperiors());
			addSorting(id, allBaseAfterSetPassArmorModelInferiors, baseSorting.getAfterSetPassArmorModelInferiors());

			addSorting(id, allBaseBeforeSetRenderManagerSuperiors, baseSorting.getBeforeSetRenderManagerSuperiors());
			addSorting(id, allBaseBeforeSetRenderManagerInferiors, baseSorting.getBeforeSetRenderManagerInferiors());
			addSorting(id, allBaseOverrideSetRenderManagerSuperiors, baseSorting.getOverrideSetRenderManagerSuperiors());
			addSorting(id, allBaseOverrideSetRenderManagerInferiors, baseSorting.getOverrideSetRenderManagerInferiors());
			addSorting(id, allBaseAfterSetRenderManagerSuperiors, baseSorting.getAfterSetRenderManagerSuperiors());
			addSorting(id, allBaseAfterSetRenderManagerInferiors, baseSorting.getAfterSetRenderManagerInferiors());

			addSorting(id, allBaseBeforeSetRenderPassModelSuperiors, baseSorting.getBeforeSetRenderPassModelSuperiors());
			addSorting(id, allBaseBeforeSetRenderPassModelInferiors, baseSorting.getBeforeSetRenderPassModelInferiors());
			addSorting(id, allBaseOverrideSetRenderPassModelSuperiors, baseSorting.getOverrideSetRenderPassModelSuperiors());
			addSorting(id, allBaseOverrideSetRenderPassModelInferiors, baseSorting.getOverrideSetRenderPassModelInferiors());
			addSorting(id, allBaseAfterSetRenderPassModelSuperiors, baseSorting.getAfterSetRenderPassModelSuperiors());
			addSorting(id, allBaseAfterSetRenderPassModelInferiors, baseSorting.getAfterSetRenderPassModelInferiors());

			addSorting(id, allBaseBeforeUpdateIconsSuperiors, baseSorting.getBeforeUpdateIconsSuperiors());
			addSorting(id, allBaseBeforeUpdateIconsInferiors, baseSorting.getBeforeUpdateIconsInferiors());
			addSorting(id, allBaseOverrideUpdateIconsSuperiors, baseSorting.getOverrideUpdateIconsSuperiors());
			addSorting(id, allBaseOverrideUpdateIconsInferiors, baseSorting.getOverrideUpdateIconsInferiors());
			addSorting(id, allBaseAfterUpdateIconsSuperiors, baseSorting.getAfterUpdateIconsSuperiors());
			addSorting(id, allBaseAfterUpdateIconsInferiors, baseSorting.getAfterUpdateIconsInferiors());

		}

		addMethod(id, baseClass, beforeLocalConstructingHookTypes, "beforeLocalConstructing");
		addMethod(id, baseClass, afterLocalConstructingHookTypes, "afterLocalConstructing");


		addMethod(id, baseClass, beforeDoRenderLabelHookTypes, "beforeDoRenderLabel", net.minecraft.entity.EntityLivingBase.class);
		addMethod(id, baseClass, overrideDoRenderLabelHookTypes, "doRenderLabel", net.minecraft.entity.EntityLivingBase.class);
		addMethod(id, baseClass, afterDoRenderLabelHookTypes, "afterDoRenderLabel", net.minecraft.entity.EntityLivingBase.class);

		addMethod(id, baseClass, beforeDoRenderShadowAndFireHookTypes, "beforeDoRenderShadowAndFire", net.minecraft.entity.Entity.class, double.class, double.class, double.class, float.class, float.class);
		addMethod(id, baseClass, overrideDoRenderShadowAndFireHookTypes, "doRenderShadowAndFire", net.minecraft.entity.Entity.class, double.class, double.class, double.class, float.class, float.class);
		addMethod(id, baseClass, afterDoRenderShadowAndFireHookTypes, "afterDoRenderShadowAndFire", net.minecraft.entity.Entity.class, double.class, double.class, double.class, float.class, float.class);

		addMethod(id, baseClass, beforeGetColorMultiplierHookTypes, "beforeGetColorMultiplier", net.minecraft.entity.EntityLivingBase.class, float.class, float.class);
		addMethod(id, baseClass, overrideGetColorMultiplierHookTypes, "getColorMultiplier", net.minecraft.entity.EntityLivingBase.class, float.class, float.class);
		addMethod(id, baseClass, afterGetColorMultiplierHookTypes, "afterGetColorMultiplier", net.minecraft.entity.EntityLivingBase.class, float.class, float.class);

		addMethod(id, baseClass, beforeGetDeathMaxRotationHookTypes, "beforeGetDeathMaxRotation", net.minecraft.entity.EntityLivingBase.class);
		addMethod(id, baseClass, overrideGetDeathMaxRotationHookTypes, "getDeathMaxRotation", net.minecraft.entity.EntityLivingBase.class);
		addMethod(id, baseClass, afterGetDeathMaxRotationHookTypes, "afterGetDeathMaxRotation", net.minecraft.entity.EntityLivingBase.class);

		addMethod(id, baseClass, beforeGetFontRendererFromRenderManagerHookTypes, "beforeGetFontRendererFromRenderManager");
		addMethod(id, baseClass, overrideGetFontRendererFromRenderManagerHookTypes, "getFontRendererFromRenderManager");
		addMethod(id, baseClass, afterGetFontRendererFromRenderManagerHookTypes, "afterGetFontRendererFromRenderManager");

		addMethod(id, baseClass, beforeGetResourceLocationFromPlayerHookTypes, "beforeGetResourceLocationFromPlayer", net.minecraft.client.entity.AbstractClientPlayer.class);
		addMethod(id, baseClass, overrideGetResourceLocationFromPlayerHookTypes, "getResourceLocationFromPlayer", net.minecraft.client.entity.AbstractClientPlayer.class);
		addMethod(id, baseClass, afterGetResourceLocationFromPlayerHookTypes, "afterGetResourceLocationFromPlayer", net.minecraft.client.entity.AbstractClientPlayer.class);

		addMethod(id, baseClass, beforeHandleRotationFloatHookTypes, "beforeHandleRotationFloat", net.minecraft.entity.EntityLivingBase.class, float.class);
		addMethod(id, baseClass, overrideHandleRotationFloatHookTypes, "handleRotationFloat", net.minecraft.entity.EntityLivingBase.class, float.class);
		addMethod(id, baseClass, afterHandleRotationFloatHookTypes, "afterHandleRotationFloat", net.minecraft.entity.EntityLivingBase.class, float.class);

		addMethod(id, baseClass, beforeInheritRenderPassHookTypes, "beforeInheritRenderPass", net.minecraft.entity.EntityLivingBase.class, int.class, float.class);
		addMethod(id, baseClass, overrideInheritRenderPassHookTypes, "inheritRenderPass", net.minecraft.entity.EntityLivingBase.class, int.class, float.class);
		addMethod(id, baseClass, afterInheritRenderPassHookTypes, "afterInheritRenderPass", net.minecraft.entity.EntityLivingBase.class, int.class, float.class);

		addMethod(id, baseClass, beforeLoadTextureHookTypes, "beforeLoadTexture", net.minecraft.util.ResourceLocation.class);
		addMethod(id, baseClass, overrideLoadTextureHookTypes, "loadTexture", net.minecraft.util.ResourceLocation.class);
		addMethod(id, baseClass, afterLoadTextureHookTypes, "afterLoadTexture", net.minecraft.util.ResourceLocation.class);

		addMethod(id, baseClass, beforeLoadTextureOfEntityHookTypes, "beforeLoadTextureOfEntity", net.minecraft.entity.Entity.class);
		addMethod(id, baseClass, overrideLoadTextureOfEntityHookTypes, "loadTextureOfEntity", net.minecraft.entity.Entity.class);
		addMethod(id, baseClass, afterLoadTextureOfEntityHookTypes, "afterLoadTextureOfEntity", net.minecraft.entity.Entity.class);

		addMethod(id, baseClass, beforePassSpecialRenderHookTypes, "beforePassSpecialRender", net.minecraft.entity.EntityLivingBase.class, double.class, double.class, double.class);
		addMethod(id, baseClass, overridePassSpecialRenderHookTypes, "passSpecialRender", net.minecraft.entity.EntityLivingBase.class, double.class, double.class, double.class);
		addMethod(id, baseClass, afterPassSpecialRenderHookTypes, "afterPassSpecialRender", net.minecraft.entity.EntityLivingBase.class, double.class, double.class, double.class);

		addMethod(id, baseClass, beforePerformStaticEntityRebuildHookTypes, "beforePerformStaticEntityRebuild");
		addMethod(id, baseClass, overridePerformStaticEntityRebuildHookTypes, "performStaticEntityRebuild");
		addMethod(id, baseClass, afterPerformStaticEntityRebuildHookTypes, "afterPerformStaticEntityRebuild");

		addMethod(id, baseClass, beforeRenderArrowsStuckInEntityHookTypes, "beforeRenderArrowsStuckInEntity", net.minecraft.entity.EntityLivingBase.class, float.class);
		addMethod(id, baseClass, overrideRenderArrowsStuckInEntityHookTypes, "renderArrowsStuckInEntity", net.minecraft.entity.EntityLivingBase.class, float.class);
		addMethod(id, baseClass, afterRenderArrowsStuckInEntityHookTypes, "afterRenderArrowsStuckInEntity", net.minecraft.entity.EntityLivingBase.class, float.class);

		addMethod(id, baseClass, beforeRenderFirstPersonArmHookTypes, "beforeRenderFirstPersonArm", net.minecraft.entity.player.EntityPlayer.class);
		addMethod(id, baseClass, overrideRenderFirstPersonArmHookTypes, "renderFirstPersonArm", net.minecraft.entity.player.EntityPlayer.class);
		addMethod(id, baseClass, afterRenderFirstPersonArmHookTypes, "afterRenderFirstPersonArm", net.minecraft.entity.player.EntityPlayer.class);

		addMethod(id, baseClass, beforeRenderLivingLabelHookTypes, "beforeRenderLivingLabel", net.minecraft.entity.Entity.class, String.class, double.class, double.class, double.class, int.class);
		addMethod(id, baseClass, overrideRenderLivingLabelHookTypes, "renderLivingLabel", net.minecraft.entity.Entity.class, String.class, double.class, double.class, double.class, int.class);
		addMethod(id, baseClass, afterRenderLivingLabelHookTypes, "afterRenderLivingLabel", net.minecraft.entity.Entity.class, String.class, double.class, double.class, double.class, int.class);

		addMethod(id, baseClass, beforeRenderModelHookTypes, "beforeRenderModel", net.minecraft.entity.EntityLivingBase.class, float.class, float.class, float.class, float.class, float.class, float.class);
		addMethod(id, baseClass, overrideRenderModelHookTypes, "renderModel", net.minecraft.entity.EntityLivingBase.class, float.class, float.class, float.class, float.class, float.class, float.class);
		addMethod(id, baseClass, afterRenderModelHookTypes, "afterRenderModel", net.minecraft.entity.EntityLivingBase.class, float.class, float.class, float.class, float.class, float.class, float.class);

		addMethod(id, baseClass, beforeRenderPlayerHookTypes, "beforeRenderPlayer", net.minecraft.client.entity.AbstractClientPlayer.class, double.class, double.class, double.class, float.class, float.class);
		addMethod(id, baseClass, overrideRenderPlayerHookTypes, "renderPlayer", net.minecraft.client.entity.AbstractClientPlayer.class, double.class, double.class, double.class, float.class, float.class);
		addMethod(id, baseClass, afterRenderPlayerHookTypes, "afterRenderPlayer", net.minecraft.client.entity.AbstractClientPlayer.class, double.class, double.class, double.class, float.class, float.class);

		addMethod(id, baseClass, beforeRenderPlayerNameAndScoreLabelHookTypes, "beforeRenderPlayerNameAndScoreLabel", net.minecraft.client.entity.AbstractClientPlayer.class, double.class, double.class, double.class, String.class, float.class, double.class);
		addMethod(id, baseClass, overrideRenderPlayerNameAndScoreLabelHookTypes, "renderPlayerNameAndScoreLabel", net.minecraft.client.entity.AbstractClientPlayer.class, double.class, double.class, double.class, String.class, float.class, double.class);
		addMethod(id, baseClass, afterRenderPlayerNameAndScoreLabelHookTypes, "afterRenderPlayerNameAndScoreLabel", net.minecraft.client.entity.AbstractClientPlayer.class, double.class, double.class, double.class, String.class, float.class, double.class);

		addMethod(id, baseClass, beforeRenderPlayerScaleHookTypes, "beforeRenderPlayerScale", net.minecraft.client.entity.AbstractClientPlayer.class, float.class);
		addMethod(id, baseClass, overrideRenderPlayerScaleHookTypes, "renderPlayerScale", net.minecraft.client.entity.AbstractClientPlayer.class, float.class);
		addMethod(id, baseClass, afterRenderPlayerScaleHookTypes, "afterRenderPlayerScale", net.minecraft.client.entity.AbstractClientPlayer.class, float.class);

		addMethod(id, baseClass, beforeRenderPlayerSleepHookTypes, "beforeRenderPlayerSleep", net.minecraft.client.entity.AbstractClientPlayer.class, double.class, double.class, double.class);
		addMethod(id, baseClass, overrideRenderPlayerSleepHookTypes, "renderPlayerSleep", net.minecraft.client.entity.AbstractClientPlayer.class, double.class, double.class, double.class);
		addMethod(id, baseClass, afterRenderPlayerSleepHookTypes, "afterRenderPlayerSleep", net.minecraft.client.entity.AbstractClientPlayer.class, double.class, double.class, double.class);

		addMethod(id, baseClass, beforeRenderSpecialsHookTypes, "beforeRenderSpecials", net.minecraft.client.entity.AbstractClientPlayer.class, float.class);
		addMethod(id, baseClass, overrideRenderSpecialsHookTypes, "renderSpecials", net.minecraft.client.entity.AbstractClientPlayer.class, float.class);
		addMethod(id, baseClass, afterRenderSpecialsHookTypes, "afterRenderSpecials", net.minecraft.client.entity.AbstractClientPlayer.class, float.class);

		addMethod(id, baseClass, beforeRenderSwingProgressHookTypes, "beforeRenderSwingProgress", net.minecraft.entity.EntityLivingBase.class, float.class);
		addMethod(id, baseClass, overrideRenderSwingProgressHookTypes, "renderSwingProgress", net.minecraft.entity.EntityLivingBase.class, float.class);
		addMethod(id, baseClass, afterRenderSwingProgressHookTypes, "afterRenderSwingProgress", net.minecraft.entity.EntityLivingBase.class, float.class);

		addMethod(id, baseClass, beforeRotatePlayerHookTypes, "beforeRotatePlayer", net.minecraft.client.entity.AbstractClientPlayer.class, float.class, float.class, float.class);
		addMethod(id, baseClass, overrideRotatePlayerHookTypes, "rotatePlayer", net.minecraft.client.entity.AbstractClientPlayer.class, float.class, float.class, float.class);
		addMethod(id, baseClass, afterRotatePlayerHookTypes, "afterRotatePlayer", net.minecraft.client.entity.AbstractClientPlayer.class, float.class, float.class, float.class);

		addMethod(id, baseClass, beforeSetArmorModelHookTypes, "beforeSetArmorModel", net.minecraft.client.entity.AbstractClientPlayer.class, int.class, float.class);
		addMethod(id, baseClass, overrideSetArmorModelHookTypes, "setArmorModel", net.minecraft.client.entity.AbstractClientPlayer.class, int.class, float.class);
		addMethod(id, baseClass, afterSetArmorModelHookTypes, "afterSetArmorModel", net.minecraft.client.entity.AbstractClientPlayer.class, int.class, float.class);

		addMethod(id, baseClass, beforeSetPassArmorModelHookTypes, "beforeSetPassArmorModel", net.minecraft.client.entity.AbstractClientPlayer.class, int.class, float.class);
		addMethod(id, baseClass, overrideSetPassArmorModelHookTypes, "setPassArmorModel", net.minecraft.client.entity.AbstractClientPlayer.class, int.class, float.class);
		addMethod(id, baseClass, afterSetPassArmorModelHookTypes, "afterSetPassArmorModel", net.minecraft.client.entity.AbstractClientPlayer.class, int.class, float.class);

		addMethod(id, baseClass, beforeSetRenderManagerHookTypes, "beforeSetRenderManager", net.minecraft.client.renderer.entity.RenderManager.class);
		addMethod(id, baseClass, overrideSetRenderManagerHookTypes, "setRenderManager", net.minecraft.client.renderer.entity.RenderManager.class);
		addMethod(id, baseClass, afterSetRenderManagerHookTypes, "afterSetRenderManager", net.minecraft.client.renderer.entity.RenderManager.class);

		addMethod(id, baseClass, beforeSetRenderPassModelHookTypes, "beforeSetRenderPassModel", net.minecraft.client.model.ModelBase.class);
		addMethod(id, baseClass, overrideSetRenderPassModelHookTypes, "setRenderPassModel", net.minecraft.client.model.ModelBase.class);
		addMethod(id, baseClass, afterSetRenderPassModelHookTypes, "afterSetRenderPassModel", net.minecraft.client.model.ModelBase.class);

		addMethod(id, baseClass, beforeUpdateIconsHookTypes, "beforeUpdateIcons", net.minecraft.client.renderer.texture.IIconRegister.class);
		addMethod(id, baseClass, overrideUpdateIconsHookTypes, "updateIcons", net.minecraft.client.renderer.texture.IIconRegister.class);
		addMethod(id, baseClass, afterUpdateIconsHookTypes, "afterUpdateIcons", net.minecraft.client.renderer.texture.IIconRegister.class);


		addDynamicMethods(id, baseClass);

		addDynamicKeys(id, baseClass, beforeDynamicHookMethods, beforeDynamicHookTypes);
		addDynamicKeys(id, baseClass, overrideDynamicHookMethods, overrideDynamicHookTypes);
		addDynamicKeys(id, baseClass, afterDynamicHookMethods, afterDynamicHookTypes);

		initialize();

		for(IRenderPlayerAPI instance : allInstances)
			instance.getRenderPlayerAPI().attachRenderPlayerBase(id);

		System.out.println("Render Player: registered " + id);
		logger.fine("Render Player: registered class '" + baseClass.getName() + "' with id '" + id + "'");

		initialized = false;
	}

	public static boolean unregister(String id)
	{
		if(id == null)
			return false;

		Constructor<?> constructor = allBaseConstructors.remove(id);
		if(constructor == null)
			return false;

		for(IRenderPlayerAPI instance : allInstances)
			instance.getRenderPlayerAPI().detachRenderPlayerBase(id);

		beforeLocalConstructingHookTypes.remove(id);
		afterLocalConstructingHookTypes.remove(id);

		allBaseBeforeDoRenderLabelSuperiors.remove(id);
		allBaseBeforeDoRenderLabelInferiors.remove(id);
		allBaseOverrideDoRenderLabelSuperiors.remove(id);
		allBaseOverrideDoRenderLabelInferiors.remove(id);
		allBaseAfterDoRenderLabelSuperiors.remove(id);
		allBaseAfterDoRenderLabelInferiors.remove(id);

		beforeDoRenderLabelHookTypes.remove(id);
		overrideDoRenderLabelHookTypes.remove(id);
		afterDoRenderLabelHookTypes.remove(id);

		allBaseBeforeDoRenderShadowAndFireSuperiors.remove(id);
		allBaseBeforeDoRenderShadowAndFireInferiors.remove(id);
		allBaseOverrideDoRenderShadowAndFireSuperiors.remove(id);
		allBaseOverrideDoRenderShadowAndFireInferiors.remove(id);
		allBaseAfterDoRenderShadowAndFireSuperiors.remove(id);
		allBaseAfterDoRenderShadowAndFireInferiors.remove(id);

		beforeDoRenderShadowAndFireHookTypes.remove(id);
		overrideDoRenderShadowAndFireHookTypes.remove(id);
		afterDoRenderShadowAndFireHookTypes.remove(id);

		allBaseBeforeGetColorMultiplierSuperiors.remove(id);
		allBaseBeforeGetColorMultiplierInferiors.remove(id);
		allBaseOverrideGetColorMultiplierSuperiors.remove(id);
		allBaseOverrideGetColorMultiplierInferiors.remove(id);
		allBaseAfterGetColorMultiplierSuperiors.remove(id);
		allBaseAfterGetColorMultiplierInferiors.remove(id);

		beforeGetColorMultiplierHookTypes.remove(id);
		overrideGetColorMultiplierHookTypes.remove(id);
		afterGetColorMultiplierHookTypes.remove(id);

		allBaseBeforeGetDeathMaxRotationSuperiors.remove(id);
		allBaseBeforeGetDeathMaxRotationInferiors.remove(id);
		allBaseOverrideGetDeathMaxRotationSuperiors.remove(id);
		allBaseOverrideGetDeathMaxRotationInferiors.remove(id);
		allBaseAfterGetDeathMaxRotationSuperiors.remove(id);
		allBaseAfterGetDeathMaxRotationInferiors.remove(id);

		beforeGetDeathMaxRotationHookTypes.remove(id);
		overrideGetDeathMaxRotationHookTypes.remove(id);
		afterGetDeathMaxRotationHookTypes.remove(id);

		allBaseBeforeGetFontRendererFromRenderManagerSuperiors.remove(id);
		allBaseBeforeGetFontRendererFromRenderManagerInferiors.remove(id);
		allBaseOverrideGetFontRendererFromRenderManagerSuperiors.remove(id);
		allBaseOverrideGetFontRendererFromRenderManagerInferiors.remove(id);
		allBaseAfterGetFontRendererFromRenderManagerSuperiors.remove(id);
		allBaseAfterGetFontRendererFromRenderManagerInferiors.remove(id);

		beforeGetFontRendererFromRenderManagerHookTypes.remove(id);
		overrideGetFontRendererFromRenderManagerHookTypes.remove(id);
		afterGetFontRendererFromRenderManagerHookTypes.remove(id);

		allBaseBeforeGetResourceLocationFromPlayerSuperiors.remove(id);
		allBaseBeforeGetResourceLocationFromPlayerInferiors.remove(id);
		allBaseOverrideGetResourceLocationFromPlayerSuperiors.remove(id);
		allBaseOverrideGetResourceLocationFromPlayerInferiors.remove(id);
		allBaseAfterGetResourceLocationFromPlayerSuperiors.remove(id);
		allBaseAfterGetResourceLocationFromPlayerInferiors.remove(id);

		beforeGetResourceLocationFromPlayerHookTypes.remove(id);
		overrideGetResourceLocationFromPlayerHookTypes.remove(id);
		afterGetResourceLocationFromPlayerHookTypes.remove(id);

		allBaseBeforeHandleRotationFloatSuperiors.remove(id);
		allBaseBeforeHandleRotationFloatInferiors.remove(id);
		allBaseOverrideHandleRotationFloatSuperiors.remove(id);
		allBaseOverrideHandleRotationFloatInferiors.remove(id);
		allBaseAfterHandleRotationFloatSuperiors.remove(id);
		allBaseAfterHandleRotationFloatInferiors.remove(id);

		beforeHandleRotationFloatHookTypes.remove(id);
		overrideHandleRotationFloatHookTypes.remove(id);
		afterHandleRotationFloatHookTypes.remove(id);

		allBaseBeforeInheritRenderPassSuperiors.remove(id);
		allBaseBeforeInheritRenderPassInferiors.remove(id);
		allBaseOverrideInheritRenderPassSuperiors.remove(id);
		allBaseOverrideInheritRenderPassInferiors.remove(id);
		allBaseAfterInheritRenderPassSuperiors.remove(id);
		allBaseAfterInheritRenderPassInferiors.remove(id);

		beforeInheritRenderPassHookTypes.remove(id);
		overrideInheritRenderPassHookTypes.remove(id);
		afterInheritRenderPassHookTypes.remove(id);

		allBaseBeforeLoadTextureSuperiors.remove(id);
		allBaseBeforeLoadTextureInferiors.remove(id);
		allBaseOverrideLoadTextureSuperiors.remove(id);
		allBaseOverrideLoadTextureInferiors.remove(id);
		allBaseAfterLoadTextureSuperiors.remove(id);
		allBaseAfterLoadTextureInferiors.remove(id);

		beforeLoadTextureHookTypes.remove(id);
		overrideLoadTextureHookTypes.remove(id);
		afterLoadTextureHookTypes.remove(id);

		allBaseBeforeLoadTextureOfEntitySuperiors.remove(id);
		allBaseBeforeLoadTextureOfEntityInferiors.remove(id);
		allBaseOverrideLoadTextureOfEntitySuperiors.remove(id);
		allBaseOverrideLoadTextureOfEntityInferiors.remove(id);
		allBaseAfterLoadTextureOfEntitySuperiors.remove(id);
		allBaseAfterLoadTextureOfEntityInferiors.remove(id);

		beforeLoadTextureOfEntityHookTypes.remove(id);
		overrideLoadTextureOfEntityHookTypes.remove(id);
		afterLoadTextureOfEntityHookTypes.remove(id);

		allBaseBeforePassSpecialRenderSuperiors.remove(id);
		allBaseBeforePassSpecialRenderInferiors.remove(id);
		allBaseOverridePassSpecialRenderSuperiors.remove(id);
		allBaseOverridePassSpecialRenderInferiors.remove(id);
		allBaseAfterPassSpecialRenderSuperiors.remove(id);
		allBaseAfterPassSpecialRenderInferiors.remove(id);

		beforePassSpecialRenderHookTypes.remove(id);
		overridePassSpecialRenderHookTypes.remove(id);
		afterPassSpecialRenderHookTypes.remove(id);

		allBaseBeforePerformStaticEntityRebuildSuperiors.remove(id);
		allBaseBeforePerformStaticEntityRebuildInferiors.remove(id);
		allBaseOverridePerformStaticEntityRebuildSuperiors.remove(id);
		allBaseOverridePerformStaticEntityRebuildInferiors.remove(id);
		allBaseAfterPerformStaticEntityRebuildSuperiors.remove(id);
		allBaseAfterPerformStaticEntityRebuildInferiors.remove(id);

		beforePerformStaticEntityRebuildHookTypes.remove(id);
		overridePerformStaticEntityRebuildHookTypes.remove(id);
		afterPerformStaticEntityRebuildHookTypes.remove(id);

		allBaseBeforeRenderArrowsStuckInEntitySuperiors.remove(id);
		allBaseBeforeRenderArrowsStuckInEntityInferiors.remove(id);
		allBaseOverrideRenderArrowsStuckInEntitySuperiors.remove(id);
		allBaseOverrideRenderArrowsStuckInEntityInferiors.remove(id);
		allBaseAfterRenderArrowsStuckInEntitySuperiors.remove(id);
		allBaseAfterRenderArrowsStuckInEntityInferiors.remove(id);

		beforeRenderArrowsStuckInEntityHookTypes.remove(id);
		overrideRenderArrowsStuckInEntityHookTypes.remove(id);
		afterRenderArrowsStuckInEntityHookTypes.remove(id);

		allBaseBeforeRenderFirstPersonArmSuperiors.remove(id);
		allBaseBeforeRenderFirstPersonArmInferiors.remove(id);
		allBaseOverrideRenderFirstPersonArmSuperiors.remove(id);
		allBaseOverrideRenderFirstPersonArmInferiors.remove(id);
		allBaseAfterRenderFirstPersonArmSuperiors.remove(id);
		allBaseAfterRenderFirstPersonArmInferiors.remove(id);

		beforeRenderFirstPersonArmHookTypes.remove(id);
		overrideRenderFirstPersonArmHookTypes.remove(id);
		afterRenderFirstPersonArmHookTypes.remove(id);

		allBaseBeforeRenderLivingLabelSuperiors.remove(id);
		allBaseBeforeRenderLivingLabelInferiors.remove(id);
		allBaseOverrideRenderLivingLabelSuperiors.remove(id);
		allBaseOverrideRenderLivingLabelInferiors.remove(id);
		allBaseAfterRenderLivingLabelSuperiors.remove(id);
		allBaseAfterRenderLivingLabelInferiors.remove(id);

		beforeRenderLivingLabelHookTypes.remove(id);
		overrideRenderLivingLabelHookTypes.remove(id);
		afterRenderLivingLabelHookTypes.remove(id);

		allBaseBeforeRenderModelSuperiors.remove(id);
		allBaseBeforeRenderModelInferiors.remove(id);
		allBaseOverrideRenderModelSuperiors.remove(id);
		allBaseOverrideRenderModelInferiors.remove(id);
		allBaseAfterRenderModelSuperiors.remove(id);
		allBaseAfterRenderModelInferiors.remove(id);

		beforeRenderModelHookTypes.remove(id);
		overrideRenderModelHookTypes.remove(id);
		afterRenderModelHookTypes.remove(id);

		allBaseBeforeRenderPlayerSuperiors.remove(id);
		allBaseBeforeRenderPlayerInferiors.remove(id);
		allBaseOverrideRenderPlayerSuperiors.remove(id);
		allBaseOverrideRenderPlayerInferiors.remove(id);
		allBaseAfterRenderPlayerSuperiors.remove(id);
		allBaseAfterRenderPlayerInferiors.remove(id);

		beforeRenderPlayerHookTypes.remove(id);
		overrideRenderPlayerHookTypes.remove(id);
		afterRenderPlayerHookTypes.remove(id);

		allBaseBeforeRenderPlayerNameAndScoreLabelSuperiors.remove(id);
		allBaseBeforeRenderPlayerNameAndScoreLabelInferiors.remove(id);
		allBaseOverrideRenderPlayerNameAndScoreLabelSuperiors.remove(id);
		allBaseOverrideRenderPlayerNameAndScoreLabelInferiors.remove(id);
		allBaseAfterRenderPlayerNameAndScoreLabelSuperiors.remove(id);
		allBaseAfterRenderPlayerNameAndScoreLabelInferiors.remove(id);

		beforeRenderPlayerNameAndScoreLabelHookTypes.remove(id);
		overrideRenderPlayerNameAndScoreLabelHookTypes.remove(id);
		afterRenderPlayerNameAndScoreLabelHookTypes.remove(id);

		allBaseBeforeRenderPlayerScaleSuperiors.remove(id);
		allBaseBeforeRenderPlayerScaleInferiors.remove(id);
		allBaseOverrideRenderPlayerScaleSuperiors.remove(id);
		allBaseOverrideRenderPlayerScaleInferiors.remove(id);
		allBaseAfterRenderPlayerScaleSuperiors.remove(id);
		allBaseAfterRenderPlayerScaleInferiors.remove(id);

		beforeRenderPlayerScaleHookTypes.remove(id);
		overrideRenderPlayerScaleHookTypes.remove(id);
		afterRenderPlayerScaleHookTypes.remove(id);

		allBaseBeforeRenderPlayerSleepSuperiors.remove(id);
		allBaseBeforeRenderPlayerSleepInferiors.remove(id);
		allBaseOverrideRenderPlayerSleepSuperiors.remove(id);
		allBaseOverrideRenderPlayerSleepInferiors.remove(id);
		allBaseAfterRenderPlayerSleepSuperiors.remove(id);
		allBaseAfterRenderPlayerSleepInferiors.remove(id);

		beforeRenderPlayerSleepHookTypes.remove(id);
		overrideRenderPlayerSleepHookTypes.remove(id);
		afterRenderPlayerSleepHookTypes.remove(id);

		allBaseBeforeRenderSpecialsSuperiors.remove(id);
		allBaseBeforeRenderSpecialsInferiors.remove(id);
		allBaseOverrideRenderSpecialsSuperiors.remove(id);
		allBaseOverrideRenderSpecialsInferiors.remove(id);
		allBaseAfterRenderSpecialsSuperiors.remove(id);
		allBaseAfterRenderSpecialsInferiors.remove(id);

		beforeRenderSpecialsHookTypes.remove(id);
		overrideRenderSpecialsHookTypes.remove(id);
		afterRenderSpecialsHookTypes.remove(id);

		allBaseBeforeRenderSwingProgressSuperiors.remove(id);
		allBaseBeforeRenderSwingProgressInferiors.remove(id);
		allBaseOverrideRenderSwingProgressSuperiors.remove(id);
		allBaseOverrideRenderSwingProgressInferiors.remove(id);
		allBaseAfterRenderSwingProgressSuperiors.remove(id);
		allBaseAfterRenderSwingProgressInferiors.remove(id);

		beforeRenderSwingProgressHookTypes.remove(id);
		overrideRenderSwingProgressHookTypes.remove(id);
		afterRenderSwingProgressHookTypes.remove(id);

		allBaseBeforeRotatePlayerSuperiors.remove(id);
		allBaseBeforeRotatePlayerInferiors.remove(id);
		allBaseOverrideRotatePlayerSuperiors.remove(id);
		allBaseOverrideRotatePlayerInferiors.remove(id);
		allBaseAfterRotatePlayerSuperiors.remove(id);
		allBaseAfterRotatePlayerInferiors.remove(id);

		beforeRotatePlayerHookTypes.remove(id);
		overrideRotatePlayerHookTypes.remove(id);
		afterRotatePlayerHookTypes.remove(id);

		allBaseBeforeSetArmorModelSuperiors.remove(id);
		allBaseBeforeSetArmorModelInferiors.remove(id);
		allBaseOverrideSetArmorModelSuperiors.remove(id);
		allBaseOverrideSetArmorModelInferiors.remove(id);
		allBaseAfterSetArmorModelSuperiors.remove(id);
		allBaseAfterSetArmorModelInferiors.remove(id);

		beforeSetArmorModelHookTypes.remove(id);
		overrideSetArmorModelHookTypes.remove(id);
		afterSetArmorModelHookTypes.remove(id);

		allBaseBeforeSetPassArmorModelSuperiors.remove(id);
		allBaseBeforeSetPassArmorModelInferiors.remove(id);
		allBaseOverrideSetPassArmorModelSuperiors.remove(id);
		allBaseOverrideSetPassArmorModelInferiors.remove(id);
		allBaseAfterSetPassArmorModelSuperiors.remove(id);
		allBaseAfterSetPassArmorModelInferiors.remove(id);

		beforeSetPassArmorModelHookTypes.remove(id);
		overrideSetPassArmorModelHookTypes.remove(id);
		afterSetPassArmorModelHookTypes.remove(id);

		allBaseBeforeSetRenderManagerSuperiors.remove(id);
		allBaseBeforeSetRenderManagerInferiors.remove(id);
		allBaseOverrideSetRenderManagerSuperiors.remove(id);
		allBaseOverrideSetRenderManagerInferiors.remove(id);
		allBaseAfterSetRenderManagerSuperiors.remove(id);
		allBaseAfterSetRenderManagerInferiors.remove(id);

		beforeSetRenderManagerHookTypes.remove(id);
		overrideSetRenderManagerHookTypes.remove(id);
		afterSetRenderManagerHookTypes.remove(id);

		allBaseBeforeSetRenderPassModelSuperiors.remove(id);
		allBaseBeforeSetRenderPassModelInferiors.remove(id);
		allBaseOverrideSetRenderPassModelSuperiors.remove(id);
		allBaseOverrideSetRenderPassModelInferiors.remove(id);
		allBaseAfterSetRenderPassModelSuperiors.remove(id);
		allBaseAfterSetRenderPassModelInferiors.remove(id);

		beforeSetRenderPassModelHookTypes.remove(id);
		overrideSetRenderPassModelHookTypes.remove(id);
		afterSetRenderPassModelHookTypes.remove(id);

		allBaseBeforeUpdateIconsSuperiors.remove(id);
		allBaseBeforeUpdateIconsInferiors.remove(id);
		allBaseOverrideUpdateIconsSuperiors.remove(id);
		allBaseOverrideUpdateIconsInferiors.remove(id);
		allBaseAfterUpdateIconsSuperiors.remove(id);
		allBaseAfterUpdateIconsInferiors.remove(id);

		beforeUpdateIconsHookTypes.remove(id);
		overrideUpdateIconsHookTypes.remove(id);
		afterUpdateIconsHookTypes.remove(id);


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

		log("RenderPlayerAPI: unregistered id '" + id + "'");

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
			boolean isOverridden = method.getDeclaringClass() != RenderPlayerBase.class;
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

	public static RenderPlayerAPI create(IRenderPlayerAPI renderPlayer)
	{
		if(allBaseConstructors.size() > 0 && !initialized)
			initialize();
		return new RenderPlayerAPI(renderPlayer);
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

		sortBases(beforeDoRenderLabelHookTypes, allBaseBeforeDoRenderLabelSuperiors, allBaseBeforeDoRenderLabelInferiors, "beforeDoRenderLabel");
		sortBases(overrideDoRenderLabelHookTypes, allBaseOverrideDoRenderLabelSuperiors, allBaseOverrideDoRenderLabelInferiors, "overrideDoRenderLabel");
		sortBases(afterDoRenderLabelHookTypes, allBaseAfterDoRenderLabelSuperiors, allBaseAfterDoRenderLabelInferiors, "afterDoRenderLabel");

		sortBases(beforeDoRenderShadowAndFireHookTypes, allBaseBeforeDoRenderShadowAndFireSuperiors, allBaseBeforeDoRenderShadowAndFireInferiors, "beforeDoRenderShadowAndFire");
		sortBases(overrideDoRenderShadowAndFireHookTypes, allBaseOverrideDoRenderShadowAndFireSuperiors, allBaseOverrideDoRenderShadowAndFireInferiors, "overrideDoRenderShadowAndFire");
		sortBases(afterDoRenderShadowAndFireHookTypes, allBaseAfterDoRenderShadowAndFireSuperiors, allBaseAfterDoRenderShadowAndFireInferiors, "afterDoRenderShadowAndFire");

		sortBases(beforeGetColorMultiplierHookTypes, allBaseBeforeGetColorMultiplierSuperiors, allBaseBeforeGetColorMultiplierInferiors, "beforeGetColorMultiplier");
		sortBases(overrideGetColorMultiplierHookTypes, allBaseOverrideGetColorMultiplierSuperiors, allBaseOverrideGetColorMultiplierInferiors, "overrideGetColorMultiplier");
		sortBases(afterGetColorMultiplierHookTypes, allBaseAfterGetColorMultiplierSuperiors, allBaseAfterGetColorMultiplierInferiors, "afterGetColorMultiplier");

		sortBases(beforeGetDeathMaxRotationHookTypes, allBaseBeforeGetDeathMaxRotationSuperiors, allBaseBeforeGetDeathMaxRotationInferiors, "beforeGetDeathMaxRotation");
		sortBases(overrideGetDeathMaxRotationHookTypes, allBaseOverrideGetDeathMaxRotationSuperiors, allBaseOverrideGetDeathMaxRotationInferiors, "overrideGetDeathMaxRotation");
		sortBases(afterGetDeathMaxRotationHookTypes, allBaseAfterGetDeathMaxRotationSuperiors, allBaseAfterGetDeathMaxRotationInferiors, "afterGetDeathMaxRotation");

		sortBases(beforeGetFontRendererFromRenderManagerHookTypes, allBaseBeforeGetFontRendererFromRenderManagerSuperiors, allBaseBeforeGetFontRendererFromRenderManagerInferiors, "beforeGetFontRendererFromRenderManager");
		sortBases(overrideGetFontRendererFromRenderManagerHookTypes, allBaseOverrideGetFontRendererFromRenderManagerSuperiors, allBaseOverrideGetFontRendererFromRenderManagerInferiors, "overrideGetFontRendererFromRenderManager");
		sortBases(afterGetFontRendererFromRenderManagerHookTypes, allBaseAfterGetFontRendererFromRenderManagerSuperiors, allBaseAfterGetFontRendererFromRenderManagerInferiors, "afterGetFontRendererFromRenderManager");

		sortBases(beforeGetResourceLocationFromPlayerHookTypes, allBaseBeforeGetResourceLocationFromPlayerSuperiors, allBaseBeforeGetResourceLocationFromPlayerInferiors, "beforeGetResourceLocationFromPlayer");
		sortBases(overrideGetResourceLocationFromPlayerHookTypes, allBaseOverrideGetResourceLocationFromPlayerSuperiors, allBaseOverrideGetResourceLocationFromPlayerInferiors, "overrideGetResourceLocationFromPlayer");
		sortBases(afterGetResourceLocationFromPlayerHookTypes, allBaseAfterGetResourceLocationFromPlayerSuperiors, allBaseAfterGetResourceLocationFromPlayerInferiors, "afterGetResourceLocationFromPlayer");

		sortBases(beforeHandleRotationFloatHookTypes, allBaseBeforeHandleRotationFloatSuperiors, allBaseBeforeHandleRotationFloatInferiors, "beforeHandleRotationFloat");
		sortBases(overrideHandleRotationFloatHookTypes, allBaseOverrideHandleRotationFloatSuperiors, allBaseOverrideHandleRotationFloatInferiors, "overrideHandleRotationFloat");
		sortBases(afterHandleRotationFloatHookTypes, allBaseAfterHandleRotationFloatSuperiors, allBaseAfterHandleRotationFloatInferiors, "afterHandleRotationFloat");

		sortBases(beforeInheritRenderPassHookTypes, allBaseBeforeInheritRenderPassSuperiors, allBaseBeforeInheritRenderPassInferiors, "beforeInheritRenderPass");
		sortBases(overrideInheritRenderPassHookTypes, allBaseOverrideInheritRenderPassSuperiors, allBaseOverrideInheritRenderPassInferiors, "overrideInheritRenderPass");
		sortBases(afterInheritRenderPassHookTypes, allBaseAfterInheritRenderPassSuperiors, allBaseAfterInheritRenderPassInferiors, "afterInheritRenderPass");

		sortBases(beforeLoadTextureHookTypes, allBaseBeforeLoadTextureSuperiors, allBaseBeforeLoadTextureInferiors, "beforeLoadTexture");
		sortBases(overrideLoadTextureHookTypes, allBaseOverrideLoadTextureSuperiors, allBaseOverrideLoadTextureInferiors, "overrideLoadTexture");
		sortBases(afterLoadTextureHookTypes, allBaseAfterLoadTextureSuperiors, allBaseAfterLoadTextureInferiors, "afterLoadTexture");

		sortBases(beforeLoadTextureOfEntityHookTypes, allBaseBeforeLoadTextureOfEntitySuperiors, allBaseBeforeLoadTextureOfEntityInferiors, "beforeLoadTextureOfEntity");
		sortBases(overrideLoadTextureOfEntityHookTypes, allBaseOverrideLoadTextureOfEntitySuperiors, allBaseOverrideLoadTextureOfEntityInferiors, "overrideLoadTextureOfEntity");
		sortBases(afterLoadTextureOfEntityHookTypes, allBaseAfterLoadTextureOfEntitySuperiors, allBaseAfterLoadTextureOfEntityInferiors, "afterLoadTextureOfEntity");

		sortBases(beforePassSpecialRenderHookTypes, allBaseBeforePassSpecialRenderSuperiors, allBaseBeforePassSpecialRenderInferiors, "beforePassSpecialRender");
		sortBases(overridePassSpecialRenderHookTypes, allBaseOverridePassSpecialRenderSuperiors, allBaseOverridePassSpecialRenderInferiors, "overridePassSpecialRender");
		sortBases(afterPassSpecialRenderHookTypes, allBaseAfterPassSpecialRenderSuperiors, allBaseAfterPassSpecialRenderInferiors, "afterPassSpecialRender");

		sortBases(beforePerformStaticEntityRebuildHookTypes, allBaseBeforePerformStaticEntityRebuildSuperiors, allBaseBeforePerformStaticEntityRebuildInferiors, "beforePerformStaticEntityRebuild");
		sortBases(overridePerformStaticEntityRebuildHookTypes, allBaseOverridePerformStaticEntityRebuildSuperiors, allBaseOverridePerformStaticEntityRebuildInferiors, "overridePerformStaticEntityRebuild");
		sortBases(afterPerformStaticEntityRebuildHookTypes, allBaseAfterPerformStaticEntityRebuildSuperiors, allBaseAfterPerformStaticEntityRebuildInferiors, "afterPerformStaticEntityRebuild");

		sortBases(beforeRenderArrowsStuckInEntityHookTypes, allBaseBeforeRenderArrowsStuckInEntitySuperiors, allBaseBeforeRenderArrowsStuckInEntityInferiors, "beforeRenderArrowsStuckInEntity");
		sortBases(overrideRenderArrowsStuckInEntityHookTypes, allBaseOverrideRenderArrowsStuckInEntitySuperiors, allBaseOverrideRenderArrowsStuckInEntityInferiors, "overrideRenderArrowsStuckInEntity");
		sortBases(afterRenderArrowsStuckInEntityHookTypes, allBaseAfterRenderArrowsStuckInEntitySuperiors, allBaseAfterRenderArrowsStuckInEntityInferiors, "afterRenderArrowsStuckInEntity");

		sortBases(beforeRenderFirstPersonArmHookTypes, allBaseBeforeRenderFirstPersonArmSuperiors, allBaseBeforeRenderFirstPersonArmInferiors, "beforeRenderFirstPersonArm");
		sortBases(overrideRenderFirstPersonArmHookTypes, allBaseOverrideRenderFirstPersonArmSuperiors, allBaseOverrideRenderFirstPersonArmInferiors, "overrideRenderFirstPersonArm");
		sortBases(afterRenderFirstPersonArmHookTypes, allBaseAfterRenderFirstPersonArmSuperiors, allBaseAfterRenderFirstPersonArmInferiors, "afterRenderFirstPersonArm");

		sortBases(beforeRenderLivingLabelHookTypes, allBaseBeforeRenderLivingLabelSuperiors, allBaseBeforeRenderLivingLabelInferiors, "beforeRenderLivingLabel");
		sortBases(overrideRenderLivingLabelHookTypes, allBaseOverrideRenderLivingLabelSuperiors, allBaseOverrideRenderLivingLabelInferiors, "overrideRenderLivingLabel");
		sortBases(afterRenderLivingLabelHookTypes, allBaseAfterRenderLivingLabelSuperiors, allBaseAfterRenderLivingLabelInferiors, "afterRenderLivingLabel");

		sortBases(beforeRenderModelHookTypes, allBaseBeforeRenderModelSuperiors, allBaseBeforeRenderModelInferiors, "beforeRenderModel");
		sortBases(overrideRenderModelHookTypes, allBaseOverrideRenderModelSuperiors, allBaseOverrideRenderModelInferiors, "overrideRenderModel");
		sortBases(afterRenderModelHookTypes, allBaseAfterRenderModelSuperiors, allBaseAfterRenderModelInferiors, "afterRenderModel");

		sortBases(beforeRenderPlayerHookTypes, allBaseBeforeRenderPlayerSuperiors, allBaseBeforeRenderPlayerInferiors, "beforeRenderPlayer");
		sortBases(overrideRenderPlayerHookTypes, allBaseOverrideRenderPlayerSuperiors, allBaseOverrideRenderPlayerInferiors, "overrideRenderPlayer");
		sortBases(afterRenderPlayerHookTypes, allBaseAfterRenderPlayerSuperiors, allBaseAfterRenderPlayerInferiors, "afterRenderPlayer");

		sortBases(beforeRenderPlayerNameAndScoreLabelHookTypes, allBaseBeforeRenderPlayerNameAndScoreLabelSuperiors, allBaseBeforeRenderPlayerNameAndScoreLabelInferiors, "beforeRenderPlayerNameAndScoreLabel");
		sortBases(overrideRenderPlayerNameAndScoreLabelHookTypes, allBaseOverrideRenderPlayerNameAndScoreLabelSuperiors, allBaseOverrideRenderPlayerNameAndScoreLabelInferiors, "overrideRenderPlayerNameAndScoreLabel");
		sortBases(afterRenderPlayerNameAndScoreLabelHookTypes, allBaseAfterRenderPlayerNameAndScoreLabelSuperiors, allBaseAfterRenderPlayerNameAndScoreLabelInferiors, "afterRenderPlayerNameAndScoreLabel");

		sortBases(beforeRenderPlayerScaleHookTypes, allBaseBeforeRenderPlayerScaleSuperiors, allBaseBeforeRenderPlayerScaleInferiors, "beforeRenderPlayerScale");
		sortBases(overrideRenderPlayerScaleHookTypes, allBaseOverrideRenderPlayerScaleSuperiors, allBaseOverrideRenderPlayerScaleInferiors, "overrideRenderPlayerScale");
		sortBases(afterRenderPlayerScaleHookTypes, allBaseAfterRenderPlayerScaleSuperiors, allBaseAfterRenderPlayerScaleInferiors, "afterRenderPlayerScale");

		sortBases(beforeRenderPlayerSleepHookTypes, allBaseBeforeRenderPlayerSleepSuperiors, allBaseBeforeRenderPlayerSleepInferiors, "beforeRenderPlayerSleep");
		sortBases(overrideRenderPlayerSleepHookTypes, allBaseOverrideRenderPlayerSleepSuperiors, allBaseOverrideRenderPlayerSleepInferiors, "overrideRenderPlayerSleep");
		sortBases(afterRenderPlayerSleepHookTypes, allBaseAfterRenderPlayerSleepSuperiors, allBaseAfterRenderPlayerSleepInferiors, "afterRenderPlayerSleep");

		sortBases(beforeRenderSpecialsHookTypes, allBaseBeforeRenderSpecialsSuperiors, allBaseBeforeRenderSpecialsInferiors, "beforeRenderSpecials");
		sortBases(overrideRenderSpecialsHookTypes, allBaseOverrideRenderSpecialsSuperiors, allBaseOverrideRenderSpecialsInferiors, "overrideRenderSpecials");
		sortBases(afterRenderSpecialsHookTypes, allBaseAfterRenderSpecialsSuperiors, allBaseAfterRenderSpecialsInferiors, "afterRenderSpecials");

		sortBases(beforeRenderSwingProgressHookTypes, allBaseBeforeRenderSwingProgressSuperiors, allBaseBeforeRenderSwingProgressInferiors, "beforeRenderSwingProgress");
		sortBases(overrideRenderSwingProgressHookTypes, allBaseOverrideRenderSwingProgressSuperiors, allBaseOverrideRenderSwingProgressInferiors, "overrideRenderSwingProgress");
		sortBases(afterRenderSwingProgressHookTypes, allBaseAfterRenderSwingProgressSuperiors, allBaseAfterRenderSwingProgressInferiors, "afterRenderSwingProgress");

		sortBases(beforeRotatePlayerHookTypes, allBaseBeforeRotatePlayerSuperiors, allBaseBeforeRotatePlayerInferiors, "beforeRotatePlayer");
		sortBases(overrideRotatePlayerHookTypes, allBaseOverrideRotatePlayerSuperiors, allBaseOverrideRotatePlayerInferiors, "overrideRotatePlayer");
		sortBases(afterRotatePlayerHookTypes, allBaseAfterRotatePlayerSuperiors, allBaseAfterRotatePlayerInferiors, "afterRotatePlayer");

		sortBases(beforeSetArmorModelHookTypes, allBaseBeforeSetArmorModelSuperiors, allBaseBeforeSetArmorModelInferiors, "beforeSetArmorModel");
		sortBases(overrideSetArmorModelHookTypes, allBaseOverrideSetArmorModelSuperiors, allBaseOverrideSetArmorModelInferiors, "overrideSetArmorModel");
		sortBases(afterSetArmorModelHookTypes, allBaseAfterSetArmorModelSuperiors, allBaseAfterSetArmorModelInferiors, "afterSetArmorModel");

		sortBases(beforeSetPassArmorModelHookTypes, allBaseBeforeSetPassArmorModelSuperiors, allBaseBeforeSetPassArmorModelInferiors, "beforeSetPassArmorModel");
		sortBases(overrideSetPassArmorModelHookTypes, allBaseOverrideSetPassArmorModelSuperiors, allBaseOverrideSetPassArmorModelInferiors, "overrideSetPassArmorModel");
		sortBases(afterSetPassArmorModelHookTypes, allBaseAfterSetPassArmorModelSuperiors, allBaseAfterSetPassArmorModelInferiors, "afterSetPassArmorModel");

		sortBases(beforeSetRenderManagerHookTypes, allBaseBeforeSetRenderManagerSuperiors, allBaseBeforeSetRenderManagerInferiors, "beforeSetRenderManager");
		sortBases(overrideSetRenderManagerHookTypes, allBaseOverrideSetRenderManagerSuperiors, allBaseOverrideSetRenderManagerInferiors, "overrideSetRenderManager");
		sortBases(afterSetRenderManagerHookTypes, allBaseAfterSetRenderManagerSuperiors, allBaseAfterSetRenderManagerInferiors, "afterSetRenderManager");

		sortBases(beforeSetRenderPassModelHookTypes, allBaseBeforeSetRenderPassModelSuperiors, allBaseBeforeSetRenderPassModelInferiors, "beforeSetRenderPassModel");
		sortBases(overrideSetRenderPassModelHookTypes, allBaseOverrideSetRenderPassModelSuperiors, allBaseOverrideSetRenderPassModelInferiors, "overrideSetRenderPassModel");
		sortBases(afterSetRenderPassModelHookTypes, allBaseAfterSetRenderPassModelSuperiors, allBaseAfterSetRenderPassModelInferiors, "afterSetRenderPassModel");

		sortBases(beforeUpdateIconsHookTypes, allBaseBeforeUpdateIconsSuperiors, allBaseBeforeUpdateIconsInferiors, "beforeUpdateIcons");
		sortBases(overrideUpdateIconsHookTypes, allBaseOverrideUpdateIconsSuperiors, allBaseOverrideUpdateIconsInferiors, "overrideUpdateIcons");
		sortBases(afterUpdateIconsHookTypes, allBaseAfterUpdateIconsSuperiors, allBaseAfterUpdateIconsInferiors, "afterUpdateIcons");

		initialized = true;
	}

	private static List<IRenderPlayerAPI> allInstances = new ArrayList<IRenderPlayerAPI>();

	public static net.minecraft.client.renderer.entity.RenderPlayer[] getAllInstances()
	{
		return allInstances.toArray(new net.minecraft.client.renderer.entity.RenderPlayer[allInstances.size()]);
	}

	public static void beforeLocalConstructing(IRenderPlayerAPI renderPlayer)
	{
		RenderPlayerAPI renderPlayerAPI = renderPlayer.getRenderPlayerAPI();
		if(renderPlayerAPI != null)
			renderPlayerAPI.load();

		allInstances.add(renderPlayer);

		if(renderPlayerAPI != null)
			renderPlayerAPI.beforeLocalConstructing();
	}

	public static void afterLocalConstructing(IRenderPlayerAPI renderPlayer)
	{
		RenderPlayerAPI renderPlayerAPI = renderPlayer.getRenderPlayerAPI();
		if(renderPlayerAPI != null)
			renderPlayerAPI.afterLocalConstructing();
	}

	public static RenderPlayerBase getRenderPlayerBase(IRenderPlayerAPI renderPlayer, String baseId)
	{
		RenderPlayerAPI renderPlayerAPI = renderPlayer.getRenderPlayerAPI();
		if(renderPlayerAPI != null)
			return renderPlayerAPI.getRenderPlayerBase(baseId);
		return null;
	}

	public static Set<String> getRenderPlayerBaseIds(IRenderPlayerAPI renderPlayer)
	{
		RenderPlayerAPI renderPlayerAPI = renderPlayer.getRenderPlayerAPI();
		Set<String> result = null;
		if(renderPlayerAPI != null)
			result = renderPlayerAPI.getRenderPlayerBaseIds();
		else
			result = Collections.<String>emptySet();
		return result;
	}

	public static Object dynamic(IRenderPlayerAPI renderPlayer, String key, Object[] parameters)
	{
		RenderPlayerAPI renderPlayerAPI = renderPlayer.getRenderPlayerAPI();
		if(renderPlayerAPI != null)
			return renderPlayerAPI.dynamic(key, parameters);
		return null;
	}

	private static void sortBases(List<String> list, Map<String, String[]> allBaseSuperiors, Map<String, String[]> allBaseInferiors, String methodName)
	{
		new RenderPlayerBaseSorter(list, allBaseSuperiors, allBaseInferiors, methodName).Sort();
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

	private RenderPlayerAPI(IRenderPlayerAPI renderPlayer)
	{
		this.renderPlayer = renderPlayer;
	}

	private void load()
	{
		Iterator<String> iterator = allBaseConstructors.keySet().iterator();
		while(iterator.hasNext())
		{
			String id = iterator.next();
			RenderPlayerBase toAttach = createRenderPlayerBase(id);
			toAttach.beforeBaseAttach(false);
			allBaseObjects.put(id, toAttach);
			baseObjectsToId.put(toAttach, id);
		}

		beforeLocalConstructingHooks = create(beforeLocalConstructingHookTypes);
		afterLocalConstructingHooks = create(afterLocalConstructingHookTypes);

		updateRenderPlayerBases();

		iterator = allBaseObjects.keySet().iterator();
		while(iterator.hasNext())
			allBaseObjects.get(iterator.next()).afterBaseAttach(false);
	}

	private RenderPlayerBase createRenderPlayerBase(String id)
	{
		Constructor<?> contructor = allBaseConstructors.get(id);

		RenderPlayerBase base;
		try
		{
			if(contructor.getParameterTypes().length == 1)
				base = (RenderPlayerBase)contructor.newInstance(this);
			else
				base = (RenderPlayerBase)contructor.newInstance(this, id);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Exception while creating a RenderPlayerBase of type '" + contructor.getDeclaringClass() + "'", e);
		}
		return base;
	}

	private void updateRenderPlayerBases()
	{
		beforeDoRenderLabelHooks = create(beforeDoRenderLabelHookTypes);
		overrideDoRenderLabelHooks = create(overrideDoRenderLabelHookTypes);
		afterDoRenderLabelHooks = create(afterDoRenderLabelHookTypes);
		isDoRenderLabelModded =
			beforeDoRenderLabelHooks != null ||
			overrideDoRenderLabelHooks != null ||
			afterDoRenderLabelHooks != null;

		beforeDoRenderShadowAndFireHooks = create(beforeDoRenderShadowAndFireHookTypes);
		overrideDoRenderShadowAndFireHooks = create(overrideDoRenderShadowAndFireHookTypes);
		afterDoRenderShadowAndFireHooks = create(afterDoRenderShadowAndFireHookTypes);
		isDoRenderShadowAndFireModded =
			beforeDoRenderShadowAndFireHooks != null ||
			overrideDoRenderShadowAndFireHooks != null ||
			afterDoRenderShadowAndFireHooks != null;

		beforeGetColorMultiplierHooks = create(beforeGetColorMultiplierHookTypes);
		overrideGetColorMultiplierHooks = create(overrideGetColorMultiplierHookTypes);
		afterGetColorMultiplierHooks = create(afterGetColorMultiplierHookTypes);
		isGetColorMultiplierModded =
			beforeGetColorMultiplierHooks != null ||
			overrideGetColorMultiplierHooks != null ||
			afterGetColorMultiplierHooks != null;

		beforeGetDeathMaxRotationHooks = create(beforeGetDeathMaxRotationHookTypes);
		overrideGetDeathMaxRotationHooks = create(overrideGetDeathMaxRotationHookTypes);
		afterGetDeathMaxRotationHooks = create(afterGetDeathMaxRotationHookTypes);
		isGetDeathMaxRotationModded =
			beforeGetDeathMaxRotationHooks != null ||
			overrideGetDeathMaxRotationHooks != null ||
			afterGetDeathMaxRotationHooks != null;

		beforeGetFontRendererFromRenderManagerHooks = create(beforeGetFontRendererFromRenderManagerHookTypes);
		overrideGetFontRendererFromRenderManagerHooks = create(overrideGetFontRendererFromRenderManagerHookTypes);
		afterGetFontRendererFromRenderManagerHooks = create(afterGetFontRendererFromRenderManagerHookTypes);
		isGetFontRendererFromRenderManagerModded =
			beforeGetFontRendererFromRenderManagerHooks != null ||
			overrideGetFontRendererFromRenderManagerHooks != null ||
			afterGetFontRendererFromRenderManagerHooks != null;

		beforeGetResourceLocationFromPlayerHooks = create(beforeGetResourceLocationFromPlayerHookTypes);
		overrideGetResourceLocationFromPlayerHooks = create(overrideGetResourceLocationFromPlayerHookTypes);
		afterGetResourceLocationFromPlayerHooks = create(afterGetResourceLocationFromPlayerHookTypes);
		isGetResourceLocationFromPlayerModded =
			beforeGetResourceLocationFromPlayerHooks != null ||
			overrideGetResourceLocationFromPlayerHooks != null ||
			afterGetResourceLocationFromPlayerHooks != null;

		beforeHandleRotationFloatHooks = create(beforeHandleRotationFloatHookTypes);
		overrideHandleRotationFloatHooks = create(overrideHandleRotationFloatHookTypes);
		afterHandleRotationFloatHooks = create(afterHandleRotationFloatHookTypes);
		isHandleRotationFloatModded =
			beforeHandleRotationFloatHooks != null ||
			overrideHandleRotationFloatHooks != null ||
			afterHandleRotationFloatHooks != null;

		beforeInheritRenderPassHooks = create(beforeInheritRenderPassHookTypes);
		overrideInheritRenderPassHooks = create(overrideInheritRenderPassHookTypes);
		afterInheritRenderPassHooks = create(afterInheritRenderPassHookTypes);
		isInheritRenderPassModded =
			beforeInheritRenderPassHooks != null ||
			overrideInheritRenderPassHooks != null ||
			afterInheritRenderPassHooks != null;

		beforeLoadTextureHooks = create(beforeLoadTextureHookTypes);
		overrideLoadTextureHooks = create(overrideLoadTextureHookTypes);
		afterLoadTextureHooks = create(afterLoadTextureHookTypes);
		isLoadTextureModded =
			beforeLoadTextureHooks != null ||
			overrideLoadTextureHooks != null ||
			afterLoadTextureHooks != null;

		beforeLoadTextureOfEntityHooks = create(beforeLoadTextureOfEntityHookTypes);
		overrideLoadTextureOfEntityHooks = create(overrideLoadTextureOfEntityHookTypes);
		afterLoadTextureOfEntityHooks = create(afterLoadTextureOfEntityHookTypes);
		isLoadTextureOfEntityModded =
			beforeLoadTextureOfEntityHooks != null ||
			overrideLoadTextureOfEntityHooks != null ||
			afterLoadTextureOfEntityHooks != null;

		beforePassSpecialRenderHooks = create(beforePassSpecialRenderHookTypes);
		overridePassSpecialRenderHooks = create(overridePassSpecialRenderHookTypes);
		afterPassSpecialRenderHooks = create(afterPassSpecialRenderHookTypes);
		isPassSpecialRenderModded =
			beforePassSpecialRenderHooks != null ||
			overridePassSpecialRenderHooks != null ||
			afterPassSpecialRenderHooks != null;

		beforePerformStaticEntityRebuildHooks = create(beforePerformStaticEntityRebuildHookTypes);
		overridePerformStaticEntityRebuildHooks = create(overridePerformStaticEntityRebuildHookTypes);
		afterPerformStaticEntityRebuildHooks = create(afterPerformStaticEntityRebuildHookTypes);
		isPerformStaticEntityRebuildModded =
			beforePerformStaticEntityRebuildHooks != null ||
			overridePerformStaticEntityRebuildHooks != null ||
			afterPerformStaticEntityRebuildHooks != null;

		beforeRenderArrowsStuckInEntityHooks = create(beforeRenderArrowsStuckInEntityHookTypes);
		overrideRenderArrowsStuckInEntityHooks = create(overrideRenderArrowsStuckInEntityHookTypes);
		afterRenderArrowsStuckInEntityHooks = create(afterRenderArrowsStuckInEntityHookTypes);
		isRenderArrowsStuckInEntityModded =
			beforeRenderArrowsStuckInEntityHooks != null ||
			overrideRenderArrowsStuckInEntityHooks != null ||
			afterRenderArrowsStuckInEntityHooks != null;

		beforeRenderFirstPersonArmHooks = create(beforeRenderFirstPersonArmHookTypes);
		overrideRenderFirstPersonArmHooks = create(overrideRenderFirstPersonArmHookTypes);
		afterRenderFirstPersonArmHooks = create(afterRenderFirstPersonArmHookTypes);
		isRenderFirstPersonArmModded =
			beforeRenderFirstPersonArmHooks != null ||
			overrideRenderFirstPersonArmHooks != null ||
			afterRenderFirstPersonArmHooks != null;

		beforeRenderLivingLabelHooks = create(beforeRenderLivingLabelHookTypes);
		overrideRenderLivingLabelHooks = create(overrideRenderLivingLabelHookTypes);
		afterRenderLivingLabelHooks = create(afterRenderLivingLabelHookTypes);
		isRenderLivingLabelModded =
			beforeRenderLivingLabelHooks != null ||
			overrideRenderLivingLabelHooks != null ||
			afterRenderLivingLabelHooks != null;

		beforeRenderModelHooks = create(beforeRenderModelHookTypes);
		overrideRenderModelHooks = create(overrideRenderModelHookTypes);
		afterRenderModelHooks = create(afterRenderModelHookTypes);
		isRenderModelModded =
			beforeRenderModelHooks != null ||
			overrideRenderModelHooks != null ||
			afterRenderModelHooks != null;

		beforeRenderPlayerHooks = create(beforeRenderPlayerHookTypes);
		overrideRenderPlayerHooks = create(overrideRenderPlayerHookTypes);
		afterRenderPlayerHooks = create(afterRenderPlayerHookTypes);
		isRenderPlayerModded =
			beforeRenderPlayerHooks != null ||
			overrideRenderPlayerHooks != null ||
			afterRenderPlayerHooks != null;

		beforeRenderPlayerNameAndScoreLabelHooks = create(beforeRenderPlayerNameAndScoreLabelHookTypes);
		overrideRenderPlayerNameAndScoreLabelHooks = create(overrideRenderPlayerNameAndScoreLabelHookTypes);
		afterRenderPlayerNameAndScoreLabelHooks = create(afterRenderPlayerNameAndScoreLabelHookTypes);
		isRenderPlayerNameAndScoreLabelModded =
			beforeRenderPlayerNameAndScoreLabelHooks != null ||
			overrideRenderPlayerNameAndScoreLabelHooks != null ||
			afterRenderPlayerNameAndScoreLabelHooks != null;

		beforeRenderPlayerScaleHooks = create(beforeRenderPlayerScaleHookTypes);
		overrideRenderPlayerScaleHooks = create(overrideRenderPlayerScaleHookTypes);
		afterRenderPlayerScaleHooks = create(afterRenderPlayerScaleHookTypes);
		isRenderPlayerScaleModded =
			beforeRenderPlayerScaleHooks != null ||
			overrideRenderPlayerScaleHooks != null ||
			afterRenderPlayerScaleHooks != null;

		beforeRenderPlayerSleepHooks = create(beforeRenderPlayerSleepHookTypes);
		overrideRenderPlayerSleepHooks = create(overrideRenderPlayerSleepHookTypes);
		afterRenderPlayerSleepHooks = create(afterRenderPlayerSleepHookTypes);
		isRenderPlayerSleepModded =
			beforeRenderPlayerSleepHooks != null ||
			overrideRenderPlayerSleepHooks != null ||
			afterRenderPlayerSleepHooks != null;

		beforeRenderSpecialsHooks = create(beforeRenderSpecialsHookTypes);
		overrideRenderSpecialsHooks = create(overrideRenderSpecialsHookTypes);
		afterRenderSpecialsHooks = create(afterRenderSpecialsHookTypes);
		isRenderSpecialsModded =
			beforeRenderSpecialsHooks != null ||
			overrideRenderSpecialsHooks != null ||
			afterRenderSpecialsHooks != null;

		beforeRenderSwingProgressHooks = create(beforeRenderSwingProgressHookTypes);
		overrideRenderSwingProgressHooks = create(overrideRenderSwingProgressHookTypes);
		afterRenderSwingProgressHooks = create(afterRenderSwingProgressHookTypes);
		isRenderSwingProgressModded =
			beforeRenderSwingProgressHooks != null ||
			overrideRenderSwingProgressHooks != null ||
			afterRenderSwingProgressHooks != null;

		beforeRotatePlayerHooks = create(beforeRotatePlayerHookTypes);
		overrideRotatePlayerHooks = create(overrideRotatePlayerHookTypes);
		afterRotatePlayerHooks = create(afterRotatePlayerHookTypes);
		isRotatePlayerModded =
			beforeRotatePlayerHooks != null ||
			overrideRotatePlayerHooks != null ||
			afterRotatePlayerHooks != null;

		beforeSetArmorModelHooks = create(beforeSetArmorModelHookTypes);
		overrideSetArmorModelHooks = create(overrideSetArmorModelHookTypes);
		afterSetArmorModelHooks = create(afterSetArmorModelHookTypes);
		isSetArmorModelModded =
			beforeSetArmorModelHooks != null ||
			overrideSetArmorModelHooks != null ||
			afterSetArmorModelHooks != null;

		beforeSetPassArmorModelHooks = create(beforeSetPassArmorModelHookTypes);
		overrideSetPassArmorModelHooks = create(overrideSetPassArmorModelHookTypes);
		afterSetPassArmorModelHooks = create(afterSetPassArmorModelHookTypes);
		isSetPassArmorModelModded =
			beforeSetPassArmorModelHooks != null ||
			overrideSetPassArmorModelHooks != null ||
			afterSetPassArmorModelHooks != null;

		beforeSetRenderManagerHooks = create(beforeSetRenderManagerHookTypes);
		overrideSetRenderManagerHooks = create(overrideSetRenderManagerHookTypes);
		afterSetRenderManagerHooks = create(afterSetRenderManagerHookTypes);
		isSetRenderManagerModded =
			beforeSetRenderManagerHooks != null ||
			overrideSetRenderManagerHooks != null ||
			afterSetRenderManagerHooks != null;

		beforeSetRenderPassModelHooks = create(beforeSetRenderPassModelHookTypes);
		overrideSetRenderPassModelHooks = create(overrideSetRenderPassModelHookTypes);
		afterSetRenderPassModelHooks = create(afterSetRenderPassModelHookTypes);
		isSetRenderPassModelModded =
			beforeSetRenderPassModelHooks != null ||
			overrideSetRenderPassModelHooks != null ||
			afterSetRenderPassModelHooks != null;

		beforeUpdateIconsHooks = create(beforeUpdateIconsHookTypes);
		overrideUpdateIconsHooks = create(overrideUpdateIconsHookTypes);
		afterUpdateIconsHooks = create(afterUpdateIconsHookTypes);
		isUpdateIconsModded =
			beforeUpdateIconsHooks != null ||
			overrideUpdateIconsHooks != null ||
			afterUpdateIconsHooks != null;

	}

	private void attachRenderPlayerBase(String id)
	{
        RenderPlayerBase toAttach = createRenderPlayerBase(id);
		toAttach.beforeBaseAttach(true);
		allBaseObjects.put(id, toAttach);
		updateRenderPlayerBases();
		toAttach.afterBaseAttach(true);
	}

	private void detachRenderPlayerBase(String id)
	{
		RenderPlayerBase toDetach = allBaseObjects.get(id);
		toDetach.beforeBaseDetach(true);
		allBaseObjects.remove(id);
		updateRenderPlayerBases();
		toDetach.afterBaseDetach(true);
	}

	private RenderPlayerBase[] create(List<String> types)
	{
		if(types.isEmpty())
			return null;

		RenderPlayerBase[] result = new RenderPlayerBase[types.size()];
		for(int i = 0; i < result.length; i++)
			result[i] = getRenderPlayerBase(types.get(i));
		return result;
	}

	private void beforeLocalConstructing()
	{
		if(beforeLocalConstructingHooks != null)
			for(int i = beforeLocalConstructingHooks.length - 1; i >= 0 ; i--)
				beforeLocalConstructingHooks[i].beforeLocalConstructing();
		beforeLocalConstructingHooks = null;
	}

	private void afterLocalConstructing()
	{
		if(afterLocalConstructingHooks != null)
			for(int i = 0; i < afterLocalConstructingHooks.length; i++)
				afterLocalConstructingHooks[i].afterLocalConstructing();
		afterLocalConstructingHooks = null;
	}

	public RenderPlayerBase getRenderPlayerBase(String id)
	{
		return allBaseObjects.get(id);
	}

	public Set<String> getRenderPlayerBaseIds()
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

	public Object dynamicOverwritten(String key, Object[] parameters, RenderPlayerBase overwriter)
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
		if(methods == null)
			return null;

		return execute(getRenderPlayerBase(id), method, parameters);
	}

	private void executeAll(String key, Object[] parameters, Map<String, List<String>> dynamicHookTypes, Map<Class<?>, Map<String, Method>> dynamicHookMethods, boolean reverse)
	{
		List<String> beforeIds = dynamicHookTypes.get(key);
		if(beforeIds == null)
			return;

		for(int i= reverse ? beforeIds.size() - 1 : 0; reverse ? i >= 0 : i < beforeIds.size(); i = i + (reverse ? -1 : 1))
		{
			String id = beforeIds.get(i);
			RenderPlayerBase base = getRenderPlayerBase(id);
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

	private Object execute(RenderPlayerBase base, Method method, Object[] parameters)
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

	public static boolean doRenderLabel(IRenderPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
		boolean _result;
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isDoRenderLabelModded)
			_result = renderPlayerAPI.doRenderLabel(paramEntityLivingBase);
		else
			_result = target.localDoRenderLabel(paramEntityLivingBase);
		return _result;
	}

	private boolean doRenderLabel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
		if(beforeDoRenderLabelHooks != null)
			for(int i = beforeDoRenderLabelHooks.length - 1; i >= 0 ; i--)
				beforeDoRenderLabelHooks[i].beforeDoRenderLabel(paramEntityLivingBase);

		boolean _result;
		if(overrideDoRenderLabelHooks != null)
			_result = overrideDoRenderLabelHooks[overrideDoRenderLabelHooks.length - 1].doRenderLabel(paramEntityLivingBase);
		else
			_result = renderPlayer.localDoRenderLabel(paramEntityLivingBase);

		if(afterDoRenderLabelHooks != null)
			for(int i = 0; i < afterDoRenderLabelHooks.length; i++)
				afterDoRenderLabelHooks[i].afterDoRenderLabel(paramEntityLivingBase);

		return _result;
	}

	protected RenderPlayerBase GetOverwrittenDoRenderLabel(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideDoRenderLabelHooks.length; i++)
			if(overrideDoRenderLabelHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDoRenderLabelHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDoRenderLabelHookTypes = new LinkedList<String>();
	private final static List<String> overrideDoRenderLabelHookTypes = new LinkedList<String>();
	private final static List<String> afterDoRenderLabelHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeDoRenderLabelHooks;
	private RenderPlayerBase[] overrideDoRenderLabelHooks;
	private RenderPlayerBase[] afterDoRenderLabelHooks;

	public boolean isDoRenderLabelModded;

	private static final Map<String, String[]> allBaseBeforeDoRenderLabelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDoRenderLabelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDoRenderLabelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDoRenderLabelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDoRenderLabelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDoRenderLabelInferiors = new Hashtable<String, String[]>(0);

	public static void doRenderShadowAndFire(IRenderPlayerAPI target, net.minecraft.entity.Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isDoRenderShadowAndFireModded)
			renderPlayerAPI.doRenderShadowAndFire(paramEntity, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
		else
			target.localDoRenderShadowAndFire(paramEntity, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
	}

	private void doRenderShadowAndFire(net.minecraft.entity.Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
		if(beforeDoRenderShadowAndFireHooks != null)
			for(int i = beforeDoRenderShadowAndFireHooks.length - 1; i >= 0 ; i--)
				beforeDoRenderShadowAndFireHooks[i].beforeDoRenderShadowAndFire(paramEntity, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

		if(overrideDoRenderShadowAndFireHooks != null)
			overrideDoRenderShadowAndFireHooks[overrideDoRenderShadowAndFireHooks.length - 1].doRenderShadowAndFire(paramEntity, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
		else
			renderPlayer.localDoRenderShadowAndFire(paramEntity, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

		if(afterDoRenderShadowAndFireHooks != null)
			for(int i = 0; i < afterDoRenderShadowAndFireHooks.length; i++)
				afterDoRenderShadowAndFireHooks[i].afterDoRenderShadowAndFire(paramEntity, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

	}

	protected RenderPlayerBase GetOverwrittenDoRenderShadowAndFire(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideDoRenderShadowAndFireHooks.length; i++)
			if(overrideDoRenderShadowAndFireHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideDoRenderShadowAndFireHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeDoRenderShadowAndFireHookTypes = new LinkedList<String>();
	private final static List<String> overrideDoRenderShadowAndFireHookTypes = new LinkedList<String>();
	private final static List<String> afterDoRenderShadowAndFireHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeDoRenderShadowAndFireHooks;
	private RenderPlayerBase[] overrideDoRenderShadowAndFireHooks;
	private RenderPlayerBase[] afterDoRenderShadowAndFireHooks;

	public boolean isDoRenderShadowAndFireModded;

	private static final Map<String, String[]> allBaseBeforeDoRenderShadowAndFireSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeDoRenderShadowAndFireInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDoRenderShadowAndFireSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideDoRenderShadowAndFireInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDoRenderShadowAndFireSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterDoRenderShadowAndFireInferiors = new Hashtable<String, String[]>(0);

	public static int getColorMultiplier(IRenderPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2)
	{
		int _result;
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isGetColorMultiplierModded)
			_result = renderPlayerAPI.getColorMultiplier(paramEntityLivingBase, paramFloat1, paramFloat2);
		else
			_result = target.localGetColorMultiplier(paramEntityLivingBase, paramFloat1, paramFloat2);
		return _result;
	}

	private int getColorMultiplier(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2)
	{
		if(beforeGetColorMultiplierHooks != null)
			for(int i = beforeGetColorMultiplierHooks.length - 1; i >= 0 ; i--)
				beforeGetColorMultiplierHooks[i].beforeGetColorMultiplier(paramEntityLivingBase, paramFloat1, paramFloat2);

		int _result;
		if(overrideGetColorMultiplierHooks != null)
			_result = overrideGetColorMultiplierHooks[overrideGetColorMultiplierHooks.length - 1].getColorMultiplier(paramEntityLivingBase, paramFloat1, paramFloat2);
		else
			_result = renderPlayer.localGetColorMultiplier(paramEntityLivingBase, paramFloat1, paramFloat2);

		if(afterGetColorMultiplierHooks != null)
			for(int i = 0; i < afterGetColorMultiplierHooks.length; i++)
				afterGetColorMultiplierHooks[i].afterGetColorMultiplier(paramEntityLivingBase, paramFloat1, paramFloat2);

		return _result;
	}

	protected RenderPlayerBase GetOverwrittenGetColorMultiplier(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideGetColorMultiplierHooks.length; i++)
			if(overrideGetColorMultiplierHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetColorMultiplierHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetColorMultiplierHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetColorMultiplierHookTypes = new LinkedList<String>();
	private final static List<String> afterGetColorMultiplierHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeGetColorMultiplierHooks;
	private RenderPlayerBase[] overrideGetColorMultiplierHooks;
	private RenderPlayerBase[] afterGetColorMultiplierHooks;

	public boolean isGetColorMultiplierModded;

	private static final Map<String, String[]> allBaseBeforeGetColorMultiplierSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetColorMultiplierInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetColorMultiplierSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetColorMultiplierInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetColorMultiplierSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetColorMultiplierInferiors = new Hashtable<String, String[]>(0);

	public static float getDeathMaxRotation(IRenderPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
		float _result;
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isGetDeathMaxRotationModded)
			_result = renderPlayerAPI.getDeathMaxRotation(paramEntityLivingBase);
		else
			_result = target.localGetDeathMaxRotation(paramEntityLivingBase);
		return _result;
	}

	private float getDeathMaxRotation(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
		if(beforeGetDeathMaxRotationHooks != null)
			for(int i = beforeGetDeathMaxRotationHooks.length - 1; i >= 0 ; i--)
				beforeGetDeathMaxRotationHooks[i].beforeGetDeathMaxRotation(paramEntityLivingBase);

		float _result;
		if(overrideGetDeathMaxRotationHooks != null)
			_result = overrideGetDeathMaxRotationHooks[overrideGetDeathMaxRotationHooks.length - 1].getDeathMaxRotation(paramEntityLivingBase);
		else
			_result = renderPlayer.localGetDeathMaxRotation(paramEntityLivingBase);

		if(afterGetDeathMaxRotationHooks != null)
			for(int i = 0; i < afterGetDeathMaxRotationHooks.length; i++)
				afterGetDeathMaxRotationHooks[i].afterGetDeathMaxRotation(paramEntityLivingBase);

		return _result;
	}

	protected RenderPlayerBase GetOverwrittenGetDeathMaxRotation(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideGetDeathMaxRotationHooks.length; i++)
			if(overrideGetDeathMaxRotationHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetDeathMaxRotationHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetDeathMaxRotationHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetDeathMaxRotationHookTypes = new LinkedList<String>();
	private final static List<String> afterGetDeathMaxRotationHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeGetDeathMaxRotationHooks;
	private RenderPlayerBase[] overrideGetDeathMaxRotationHooks;
	private RenderPlayerBase[] afterGetDeathMaxRotationHooks;

	public boolean isGetDeathMaxRotationModded;

	private static final Map<String, String[]> allBaseBeforeGetDeathMaxRotationSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetDeathMaxRotationInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetDeathMaxRotationSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetDeathMaxRotationInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetDeathMaxRotationSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetDeathMaxRotationInferiors = new Hashtable<String, String[]>(0);

	public static net.minecraft.client.gui.FontRenderer getFontRendererFromRenderManager(IRenderPlayerAPI target)
	{
		net.minecraft.client.gui.FontRenderer _result;
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isGetFontRendererFromRenderManagerModded)
			_result = renderPlayerAPI.getFontRendererFromRenderManager();
		else
			_result = target.localGetFontRendererFromRenderManager();
		return _result;
	}

	private net.minecraft.client.gui.FontRenderer getFontRendererFromRenderManager()
	{
		if(beforeGetFontRendererFromRenderManagerHooks != null)
			for(int i = beforeGetFontRendererFromRenderManagerHooks.length - 1; i >= 0 ; i--)
				beforeGetFontRendererFromRenderManagerHooks[i].beforeGetFontRendererFromRenderManager();

		net.minecraft.client.gui.FontRenderer _result;
		if(overrideGetFontRendererFromRenderManagerHooks != null)
			_result = overrideGetFontRendererFromRenderManagerHooks[overrideGetFontRendererFromRenderManagerHooks.length - 1].getFontRendererFromRenderManager();
		else
			_result = renderPlayer.localGetFontRendererFromRenderManager();

		if(afterGetFontRendererFromRenderManagerHooks != null)
			for(int i = 0; i < afterGetFontRendererFromRenderManagerHooks.length; i++)
				afterGetFontRendererFromRenderManagerHooks[i].afterGetFontRendererFromRenderManager();

		return _result;
	}

	protected RenderPlayerBase GetOverwrittenGetFontRendererFromRenderManager(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideGetFontRendererFromRenderManagerHooks.length; i++)
			if(overrideGetFontRendererFromRenderManagerHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetFontRendererFromRenderManagerHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetFontRendererFromRenderManagerHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetFontRendererFromRenderManagerHookTypes = new LinkedList<String>();
	private final static List<String> afterGetFontRendererFromRenderManagerHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeGetFontRendererFromRenderManagerHooks;
	private RenderPlayerBase[] overrideGetFontRendererFromRenderManagerHooks;
	private RenderPlayerBase[] afterGetFontRendererFromRenderManagerHooks;

	public boolean isGetFontRendererFromRenderManagerModded;

	private static final Map<String, String[]> allBaseBeforeGetFontRendererFromRenderManagerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetFontRendererFromRenderManagerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetFontRendererFromRenderManagerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetFontRendererFromRenderManagerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetFontRendererFromRenderManagerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetFontRendererFromRenderManagerInferiors = new Hashtable<String, String[]>(0);

	public static net.minecraft.util.ResourceLocation getResourceLocationFromPlayer(IRenderPlayerAPI target, net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer)
	{
		net.minecraft.util.ResourceLocation _result;
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isGetResourceLocationFromPlayerModded)
			_result = renderPlayerAPI.getResourceLocationFromPlayer(paramAbstractClientPlayer);
		else
			_result = target.localGetResourceLocationFromPlayer(paramAbstractClientPlayer);
		return _result;
	}

	private net.minecraft.util.ResourceLocation getResourceLocationFromPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer)
	{
		if(beforeGetResourceLocationFromPlayerHooks != null)
			for(int i = beforeGetResourceLocationFromPlayerHooks.length - 1; i >= 0 ; i--)
				beforeGetResourceLocationFromPlayerHooks[i].beforeGetResourceLocationFromPlayer(paramAbstractClientPlayer);

		net.minecraft.util.ResourceLocation _result;
		if(overrideGetResourceLocationFromPlayerHooks != null)
			_result = overrideGetResourceLocationFromPlayerHooks[overrideGetResourceLocationFromPlayerHooks.length - 1].getResourceLocationFromPlayer(paramAbstractClientPlayer);
		else
			_result = renderPlayer.localGetResourceLocationFromPlayer(paramAbstractClientPlayer);

		if(afterGetResourceLocationFromPlayerHooks != null)
			for(int i = 0; i < afterGetResourceLocationFromPlayerHooks.length; i++)
				afterGetResourceLocationFromPlayerHooks[i].afterGetResourceLocationFromPlayer(paramAbstractClientPlayer);

		return _result;
	}

	protected RenderPlayerBase GetOverwrittenGetResourceLocationFromPlayer(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideGetResourceLocationFromPlayerHooks.length; i++)
			if(overrideGetResourceLocationFromPlayerHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetResourceLocationFromPlayerHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetResourceLocationFromPlayerHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetResourceLocationFromPlayerHookTypes = new LinkedList<String>();
	private final static List<String> afterGetResourceLocationFromPlayerHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeGetResourceLocationFromPlayerHooks;
	private RenderPlayerBase[] overrideGetResourceLocationFromPlayerHooks;
	private RenderPlayerBase[] afterGetResourceLocationFromPlayerHooks;

	public boolean isGetResourceLocationFromPlayerModded;

	private static final Map<String, String[]> allBaseBeforeGetResourceLocationFromPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetResourceLocationFromPlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetResourceLocationFromPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetResourceLocationFromPlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetResourceLocationFromPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetResourceLocationFromPlayerInferiors = new Hashtable<String, String[]>(0);

	public static float handleRotationFloat(IRenderPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
		float _result;
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isHandleRotationFloatModded)
			_result = renderPlayerAPI.handleRotationFloat(paramEntityLivingBase, paramFloat);
		else
			_result = target.localHandleRotationFloat(paramEntityLivingBase, paramFloat);
		return _result;
	}

	private float handleRotationFloat(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
		if(beforeHandleRotationFloatHooks != null)
			for(int i = beforeHandleRotationFloatHooks.length - 1; i >= 0 ; i--)
				beforeHandleRotationFloatHooks[i].beforeHandleRotationFloat(paramEntityLivingBase, paramFloat);

		float _result;
		if(overrideHandleRotationFloatHooks != null)
			_result = overrideHandleRotationFloatHooks[overrideHandleRotationFloatHooks.length - 1].handleRotationFloat(paramEntityLivingBase, paramFloat);
		else
			_result = renderPlayer.localHandleRotationFloat(paramEntityLivingBase, paramFloat);

		if(afterHandleRotationFloatHooks != null)
			for(int i = 0; i < afterHandleRotationFloatHooks.length; i++)
				afterHandleRotationFloatHooks[i].afterHandleRotationFloat(paramEntityLivingBase, paramFloat);

		return _result;
	}

	protected RenderPlayerBase GetOverwrittenHandleRotationFloat(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideHandleRotationFloatHooks.length; i++)
			if(overrideHandleRotationFloatHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideHandleRotationFloatHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeHandleRotationFloatHookTypes = new LinkedList<String>();
	private final static List<String> overrideHandleRotationFloatHookTypes = new LinkedList<String>();
	private final static List<String> afterHandleRotationFloatHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeHandleRotationFloatHooks;
	private RenderPlayerBase[] overrideHandleRotationFloatHooks;
	private RenderPlayerBase[] afterHandleRotationFloatHooks;

	public boolean isHandleRotationFloatModded;

	private static final Map<String, String[]> allBaseBeforeHandleRotationFloatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeHandleRotationFloatInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideHandleRotationFloatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideHandleRotationFloatInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterHandleRotationFloatSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterHandleRotationFloatInferiors = new Hashtable<String, String[]>(0);

	public static int inheritRenderPass(IRenderPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase, int paramInt, float paramFloat)
	{
		int _result;
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isInheritRenderPassModded)
			_result = renderPlayerAPI.inheritRenderPass(paramEntityLivingBase, paramInt, paramFloat);
		else
			_result = target.localInheritRenderPass(paramEntityLivingBase, paramInt, paramFloat);
		return _result;
	}

	private int inheritRenderPass(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, int paramInt, float paramFloat)
	{
		if(beforeInheritRenderPassHooks != null)
			for(int i = beforeInheritRenderPassHooks.length - 1; i >= 0 ; i--)
				beforeInheritRenderPassHooks[i].beforeInheritRenderPass(paramEntityLivingBase, paramInt, paramFloat);

		int _result;
		if(overrideInheritRenderPassHooks != null)
			_result = overrideInheritRenderPassHooks[overrideInheritRenderPassHooks.length - 1].inheritRenderPass(paramEntityLivingBase, paramInt, paramFloat);
		else
			_result = renderPlayer.localInheritRenderPass(paramEntityLivingBase, paramInt, paramFloat);

		if(afterInheritRenderPassHooks != null)
			for(int i = 0; i < afterInheritRenderPassHooks.length; i++)
				afterInheritRenderPassHooks[i].afterInheritRenderPass(paramEntityLivingBase, paramInt, paramFloat);

		return _result;
	}

	protected RenderPlayerBase GetOverwrittenInheritRenderPass(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideInheritRenderPassHooks.length; i++)
			if(overrideInheritRenderPassHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideInheritRenderPassHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeInheritRenderPassHookTypes = new LinkedList<String>();
	private final static List<String> overrideInheritRenderPassHookTypes = new LinkedList<String>();
	private final static List<String> afterInheritRenderPassHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeInheritRenderPassHooks;
	private RenderPlayerBase[] overrideInheritRenderPassHooks;
	private RenderPlayerBase[] afterInheritRenderPassHooks;

	public boolean isInheritRenderPassModded;

	private static final Map<String, String[]> allBaseBeforeInheritRenderPassSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeInheritRenderPassInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideInheritRenderPassSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideInheritRenderPassInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterInheritRenderPassSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterInheritRenderPassInferiors = new Hashtable<String, String[]>(0);

	public static void loadTexture(IRenderPlayerAPI target, net.minecraft.util.ResourceLocation paramResourceLocation)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isLoadTextureModded)
			renderPlayerAPI.loadTexture(paramResourceLocation);
		else
			target.localLoadTexture(paramResourceLocation);
	}

	private void loadTexture(net.minecraft.util.ResourceLocation paramResourceLocation)
	{
		if(beforeLoadTextureHooks != null)
			for(int i = beforeLoadTextureHooks.length - 1; i >= 0 ; i--)
				beforeLoadTextureHooks[i].beforeLoadTexture(paramResourceLocation);

		if(overrideLoadTextureHooks != null)
			overrideLoadTextureHooks[overrideLoadTextureHooks.length - 1].loadTexture(paramResourceLocation);
		else
			renderPlayer.localLoadTexture(paramResourceLocation);

		if(afterLoadTextureHooks != null)
			for(int i = 0; i < afterLoadTextureHooks.length; i++)
				afterLoadTextureHooks[i].afterLoadTexture(paramResourceLocation);

	}

	protected RenderPlayerBase GetOverwrittenLoadTexture(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideLoadTextureHooks.length; i++)
			if(overrideLoadTextureHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideLoadTextureHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeLoadTextureHookTypes = new LinkedList<String>();
	private final static List<String> overrideLoadTextureHookTypes = new LinkedList<String>();
	private final static List<String> afterLoadTextureHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeLoadTextureHooks;
	private RenderPlayerBase[] overrideLoadTextureHooks;
	private RenderPlayerBase[] afterLoadTextureHooks;

	public boolean isLoadTextureModded;

	private static final Map<String, String[]> allBaseBeforeLoadTextureSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeLoadTextureInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideLoadTextureSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideLoadTextureInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterLoadTextureSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterLoadTextureInferiors = new Hashtable<String, String[]>(0);

	public static void loadTextureOfEntity(IRenderPlayerAPI target, net.minecraft.entity.Entity paramEntity)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isLoadTextureOfEntityModded)
			renderPlayerAPI.loadTextureOfEntity(paramEntity);
		else
			target.localLoadTextureOfEntity(paramEntity);
	}

	private void loadTextureOfEntity(net.minecraft.entity.Entity paramEntity)
	{
		if(beforeLoadTextureOfEntityHooks != null)
			for(int i = beforeLoadTextureOfEntityHooks.length - 1; i >= 0 ; i--)
				beforeLoadTextureOfEntityHooks[i].beforeLoadTextureOfEntity(paramEntity);

		if(overrideLoadTextureOfEntityHooks != null)
			overrideLoadTextureOfEntityHooks[overrideLoadTextureOfEntityHooks.length - 1].loadTextureOfEntity(paramEntity);
		else
			renderPlayer.localLoadTextureOfEntity(paramEntity);

		if(afterLoadTextureOfEntityHooks != null)
			for(int i = 0; i < afterLoadTextureOfEntityHooks.length; i++)
				afterLoadTextureOfEntityHooks[i].afterLoadTextureOfEntity(paramEntity);

	}

	protected RenderPlayerBase GetOverwrittenLoadTextureOfEntity(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideLoadTextureOfEntityHooks.length; i++)
			if(overrideLoadTextureOfEntityHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideLoadTextureOfEntityHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeLoadTextureOfEntityHookTypes = new LinkedList<String>();
	private final static List<String> overrideLoadTextureOfEntityHookTypes = new LinkedList<String>();
	private final static List<String> afterLoadTextureOfEntityHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeLoadTextureOfEntityHooks;
	private RenderPlayerBase[] overrideLoadTextureOfEntityHooks;
	private RenderPlayerBase[] afterLoadTextureOfEntityHooks;

	public boolean isLoadTextureOfEntityModded;

	private static final Map<String, String[]> allBaseBeforeLoadTextureOfEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeLoadTextureOfEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideLoadTextureOfEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideLoadTextureOfEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterLoadTextureOfEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterLoadTextureOfEntityInferiors = new Hashtable<String, String[]>(0);

	public static void passSpecialRender(IRenderPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isPassSpecialRenderModded)
			renderPlayerAPI.passSpecialRender(paramEntityLivingBase, paramDouble1, paramDouble2, paramDouble3);
		else
			target.localPassSpecialRender(paramEntityLivingBase, paramDouble1, paramDouble2, paramDouble3);
	}

	private void passSpecialRender(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		if(beforePassSpecialRenderHooks != null)
			for(int i = beforePassSpecialRenderHooks.length - 1; i >= 0 ; i--)
				beforePassSpecialRenderHooks[i].beforePassSpecialRender(paramEntityLivingBase, paramDouble1, paramDouble2, paramDouble3);

		if(overridePassSpecialRenderHooks != null)
			overridePassSpecialRenderHooks[overridePassSpecialRenderHooks.length - 1].passSpecialRender(paramEntityLivingBase, paramDouble1, paramDouble2, paramDouble3);
		else
			renderPlayer.localPassSpecialRender(paramEntityLivingBase, paramDouble1, paramDouble2, paramDouble3);

		if(afterPassSpecialRenderHooks != null)
			for(int i = 0; i < afterPassSpecialRenderHooks.length; i++)
				afterPassSpecialRenderHooks[i].afterPassSpecialRender(paramEntityLivingBase, paramDouble1, paramDouble2, paramDouble3);

	}

	protected RenderPlayerBase GetOverwrittenPassSpecialRender(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overridePassSpecialRenderHooks.length; i++)
			if(overridePassSpecialRenderHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overridePassSpecialRenderHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforePassSpecialRenderHookTypes = new LinkedList<String>();
	private final static List<String> overridePassSpecialRenderHookTypes = new LinkedList<String>();
	private final static List<String> afterPassSpecialRenderHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforePassSpecialRenderHooks;
	private RenderPlayerBase[] overridePassSpecialRenderHooks;
	private RenderPlayerBase[] afterPassSpecialRenderHooks;

	public boolean isPassSpecialRenderModded;

	private static final Map<String, String[]> allBaseBeforePassSpecialRenderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforePassSpecialRenderInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverridePassSpecialRenderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverridePassSpecialRenderInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterPassSpecialRenderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterPassSpecialRenderInferiors = new Hashtable<String, String[]>(0);

	public static boolean performStaticEntityRebuild(IRenderPlayerAPI target)
	{
		boolean _result;
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isPerformStaticEntityRebuildModded)
			_result = renderPlayerAPI.performStaticEntityRebuild();
		else
			_result = target.localPerformStaticEntityRebuild();
		return _result;
	}

	private boolean performStaticEntityRebuild()
	{
		if(beforePerformStaticEntityRebuildHooks != null)
			for(int i = beforePerformStaticEntityRebuildHooks.length - 1; i >= 0 ; i--)
				beforePerformStaticEntityRebuildHooks[i].beforePerformStaticEntityRebuild();

		boolean _result;
		if(overridePerformStaticEntityRebuildHooks != null)
			_result = overridePerformStaticEntityRebuildHooks[overridePerformStaticEntityRebuildHooks.length - 1].performStaticEntityRebuild();
		else
			_result = renderPlayer.localPerformStaticEntityRebuild();

		if(afterPerformStaticEntityRebuildHooks != null)
			for(int i = 0; i < afterPerformStaticEntityRebuildHooks.length; i++)
				afterPerformStaticEntityRebuildHooks[i].afterPerformStaticEntityRebuild();

		return _result;
	}

	protected RenderPlayerBase GetOverwrittenPerformStaticEntityRebuild(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overridePerformStaticEntityRebuildHooks.length; i++)
			if(overridePerformStaticEntityRebuildHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overridePerformStaticEntityRebuildHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforePerformStaticEntityRebuildHookTypes = new LinkedList<String>();
	private final static List<String> overridePerformStaticEntityRebuildHookTypes = new LinkedList<String>();
	private final static List<String> afterPerformStaticEntityRebuildHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforePerformStaticEntityRebuildHooks;
	private RenderPlayerBase[] overridePerformStaticEntityRebuildHooks;
	private RenderPlayerBase[] afterPerformStaticEntityRebuildHooks;

	public boolean isPerformStaticEntityRebuildModded;

	private static final Map<String, String[]> allBaseBeforePerformStaticEntityRebuildSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforePerformStaticEntityRebuildInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverridePerformStaticEntityRebuildSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverridePerformStaticEntityRebuildInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterPerformStaticEntityRebuildSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterPerformStaticEntityRebuildInferiors = new Hashtable<String, String[]>(0);

	public static void renderArrowsStuckInEntity(IRenderPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isRenderArrowsStuckInEntityModded)
			renderPlayerAPI.renderArrowsStuckInEntity(paramEntityLivingBase, paramFloat);
		else
			target.localRenderArrowsStuckInEntity(paramEntityLivingBase, paramFloat);
	}

	private void renderArrowsStuckInEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
		if(beforeRenderArrowsStuckInEntityHooks != null)
			for(int i = beforeRenderArrowsStuckInEntityHooks.length - 1; i >= 0 ; i--)
				beforeRenderArrowsStuckInEntityHooks[i].beforeRenderArrowsStuckInEntity(paramEntityLivingBase, paramFloat);

		if(overrideRenderArrowsStuckInEntityHooks != null)
			overrideRenderArrowsStuckInEntityHooks[overrideRenderArrowsStuckInEntityHooks.length - 1].renderArrowsStuckInEntity(paramEntityLivingBase, paramFloat);
		else
			renderPlayer.localRenderArrowsStuckInEntity(paramEntityLivingBase, paramFloat);

		if(afterRenderArrowsStuckInEntityHooks != null)
			for(int i = 0; i < afterRenderArrowsStuckInEntityHooks.length; i++)
				afterRenderArrowsStuckInEntityHooks[i].afterRenderArrowsStuckInEntity(paramEntityLivingBase, paramFloat);

	}

	protected RenderPlayerBase GetOverwrittenRenderArrowsStuckInEntity(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderArrowsStuckInEntityHooks.length; i++)
			if(overrideRenderArrowsStuckInEntityHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderArrowsStuckInEntityHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderArrowsStuckInEntityHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderArrowsStuckInEntityHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderArrowsStuckInEntityHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeRenderArrowsStuckInEntityHooks;
	private RenderPlayerBase[] overrideRenderArrowsStuckInEntityHooks;
	private RenderPlayerBase[] afterRenderArrowsStuckInEntityHooks;

	public boolean isRenderArrowsStuckInEntityModded;

	private static final Map<String, String[]> allBaseBeforeRenderArrowsStuckInEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderArrowsStuckInEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderArrowsStuckInEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderArrowsStuckInEntityInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderArrowsStuckInEntitySuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderArrowsStuckInEntityInferiors = new Hashtable<String, String[]>(0);

	public static void renderFirstPersonArm(IRenderPlayerAPI target, net.minecraft.entity.player.EntityPlayer paramEntityPlayer)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isRenderFirstPersonArmModded)
			renderPlayerAPI.renderFirstPersonArm(paramEntityPlayer);
		else
			target.localRenderFirstPersonArm(paramEntityPlayer);
	}

	private void renderFirstPersonArm(net.minecraft.entity.player.EntityPlayer paramEntityPlayer)
	{
		if(beforeRenderFirstPersonArmHooks != null)
			for(int i = beforeRenderFirstPersonArmHooks.length - 1; i >= 0 ; i--)
				beforeRenderFirstPersonArmHooks[i].beforeRenderFirstPersonArm(paramEntityPlayer);

		if(overrideRenderFirstPersonArmHooks != null)
			overrideRenderFirstPersonArmHooks[overrideRenderFirstPersonArmHooks.length - 1].renderFirstPersonArm(paramEntityPlayer);
		else
			renderPlayer.localRenderFirstPersonArm(paramEntityPlayer);

		if(afterRenderFirstPersonArmHooks != null)
			for(int i = 0; i < afterRenderFirstPersonArmHooks.length; i++)
				afterRenderFirstPersonArmHooks[i].afterRenderFirstPersonArm(paramEntityPlayer);

	}

	protected RenderPlayerBase GetOverwrittenRenderFirstPersonArm(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderFirstPersonArmHooks.length; i++)
			if(overrideRenderFirstPersonArmHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderFirstPersonArmHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderFirstPersonArmHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderFirstPersonArmHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderFirstPersonArmHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeRenderFirstPersonArmHooks;
	private RenderPlayerBase[] overrideRenderFirstPersonArmHooks;
	private RenderPlayerBase[] afterRenderFirstPersonArmHooks;

	public boolean isRenderFirstPersonArmModded;

	private static final Map<String, String[]> allBaseBeforeRenderFirstPersonArmSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderFirstPersonArmInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderFirstPersonArmSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderFirstPersonArmInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderFirstPersonArmSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderFirstPersonArmInferiors = new Hashtable<String, String[]>(0);

	public static void renderLivingLabel(IRenderPlayerAPI target, net.minecraft.entity.Entity paramEntity, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isRenderLivingLabelModded)
			renderPlayerAPI.renderLivingLabel(paramEntity, paramString, paramDouble1, paramDouble2, paramDouble3, paramInt);
		else
			target.localRenderLivingLabel(paramEntity, paramString, paramDouble1, paramDouble2, paramDouble3, paramInt);
	}

	private void renderLivingLabel(net.minecraft.entity.Entity paramEntity, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt)
	{
		if(beforeRenderLivingLabelHooks != null)
			for(int i = beforeRenderLivingLabelHooks.length - 1; i >= 0 ; i--)
				beforeRenderLivingLabelHooks[i].beforeRenderLivingLabel(paramEntity, paramString, paramDouble1, paramDouble2, paramDouble3, paramInt);

		if(overrideRenderLivingLabelHooks != null)
			overrideRenderLivingLabelHooks[overrideRenderLivingLabelHooks.length - 1].renderLivingLabel(paramEntity, paramString, paramDouble1, paramDouble2, paramDouble3, paramInt);
		else
			renderPlayer.localRenderLivingLabel(paramEntity, paramString, paramDouble1, paramDouble2, paramDouble3, paramInt);

		if(afterRenderLivingLabelHooks != null)
			for(int i = 0; i < afterRenderLivingLabelHooks.length; i++)
				afterRenderLivingLabelHooks[i].afterRenderLivingLabel(paramEntity, paramString, paramDouble1, paramDouble2, paramDouble3, paramInt);

	}

	protected RenderPlayerBase GetOverwrittenRenderLivingLabel(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderLivingLabelHooks.length; i++)
			if(overrideRenderLivingLabelHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderLivingLabelHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderLivingLabelHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderLivingLabelHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderLivingLabelHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeRenderLivingLabelHooks;
	private RenderPlayerBase[] overrideRenderLivingLabelHooks;
	private RenderPlayerBase[] afterRenderLivingLabelHooks;

	public boolean isRenderLivingLabelModded;

	private static final Map<String, String[]> allBaseBeforeRenderLivingLabelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderLivingLabelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderLivingLabelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderLivingLabelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderLivingLabelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderLivingLabelInferiors = new Hashtable<String, String[]>(0);

	public static void renderModel(IRenderPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isRenderModelModded)
			renderPlayerAPI.renderModel(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
		else
			target.localRenderModel(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
	}

	private void renderModel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
	{
		if(beforeRenderModelHooks != null)
			for(int i = beforeRenderModelHooks.length - 1; i >= 0 ; i--)
				beforeRenderModelHooks[i].beforeRenderModel(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);

		if(overrideRenderModelHooks != null)
			overrideRenderModelHooks[overrideRenderModelHooks.length - 1].renderModel(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
		else
			renderPlayer.localRenderModel(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);

		if(afterRenderModelHooks != null)
			for(int i = 0; i < afterRenderModelHooks.length; i++)
				afterRenderModelHooks[i].afterRenderModel(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);

	}

	protected RenderPlayerBase GetOverwrittenRenderModel(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderModelHooks.length; i++)
			if(overrideRenderModelHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderModelHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderModelHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderModelHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderModelHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeRenderModelHooks;
	private RenderPlayerBase[] overrideRenderModelHooks;
	private RenderPlayerBase[] afterRenderModelHooks;

	public boolean isRenderModelModded;

	private static final Map<String, String[]> allBaseBeforeRenderModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderModelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderModelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderModelInferiors = new Hashtable<String, String[]>(0);

	public static void renderPlayer(IRenderPlayerAPI target, net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isRenderPlayerModded)
			renderPlayerAPI.renderPlayer(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
		else
			target.localRenderPlayer(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
	}

	private void renderPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
		if(beforeRenderPlayerHooks != null)
			for(int i = beforeRenderPlayerHooks.length - 1; i >= 0 ; i--)
				beforeRenderPlayerHooks[i].beforeRenderPlayer(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

		if(overrideRenderPlayerHooks != null)
			overrideRenderPlayerHooks[overrideRenderPlayerHooks.length - 1].renderPlayer(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
		else
			renderPlayer.localRenderPlayer(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

		if(afterRenderPlayerHooks != null)
			for(int i = 0; i < afterRenderPlayerHooks.length; i++)
				afterRenderPlayerHooks[i].afterRenderPlayer(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

	}

	protected RenderPlayerBase GetOverwrittenRenderPlayer(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderPlayerHooks.length; i++)
			if(overrideRenderPlayerHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderPlayerHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderPlayerHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderPlayerHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderPlayerHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeRenderPlayerHooks;
	private RenderPlayerBase[] overrideRenderPlayerHooks;
	private RenderPlayerBase[] afterRenderPlayerHooks;

	public boolean isRenderPlayerModded;

	private static final Map<String, String[]> allBaseBeforeRenderPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderPlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderPlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderPlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderPlayerInferiors = new Hashtable<String, String[]>(0);

	public static void renderPlayerNameAndScoreLabel(IRenderPlayerAPI target, net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, String paramString, float paramFloat, double paramDouble4)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isRenderPlayerNameAndScoreLabelModded)
			renderPlayerAPI.renderPlayerNameAndScoreLabel(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramString, paramFloat, paramDouble4);
		else
			target.localRenderPlayerNameAndScoreLabel(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramString, paramFloat, paramDouble4);
	}

	private void renderPlayerNameAndScoreLabel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, String paramString, float paramFloat, double paramDouble4)
	{
		if(beforeRenderPlayerNameAndScoreLabelHooks != null)
			for(int i = beforeRenderPlayerNameAndScoreLabelHooks.length - 1; i >= 0 ; i--)
				beforeRenderPlayerNameAndScoreLabelHooks[i].beforeRenderPlayerNameAndScoreLabel(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramString, paramFloat, paramDouble4);

		if(overrideRenderPlayerNameAndScoreLabelHooks != null)
			overrideRenderPlayerNameAndScoreLabelHooks[overrideRenderPlayerNameAndScoreLabelHooks.length - 1].renderPlayerNameAndScoreLabel(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramString, paramFloat, paramDouble4);
		else
			renderPlayer.localRenderPlayerNameAndScoreLabel(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramString, paramFloat, paramDouble4);

		if(afterRenderPlayerNameAndScoreLabelHooks != null)
			for(int i = 0; i < afterRenderPlayerNameAndScoreLabelHooks.length; i++)
				afterRenderPlayerNameAndScoreLabelHooks[i].afterRenderPlayerNameAndScoreLabel(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramString, paramFloat, paramDouble4);

	}

	protected RenderPlayerBase GetOverwrittenRenderPlayerNameAndScoreLabel(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderPlayerNameAndScoreLabelHooks.length; i++)
			if(overrideRenderPlayerNameAndScoreLabelHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderPlayerNameAndScoreLabelHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderPlayerNameAndScoreLabelHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderPlayerNameAndScoreLabelHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderPlayerNameAndScoreLabelHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeRenderPlayerNameAndScoreLabelHooks;
	private RenderPlayerBase[] overrideRenderPlayerNameAndScoreLabelHooks;
	private RenderPlayerBase[] afterRenderPlayerNameAndScoreLabelHooks;

	public boolean isRenderPlayerNameAndScoreLabelModded;

	private static final Map<String, String[]> allBaseBeforeRenderPlayerNameAndScoreLabelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderPlayerNameAndScoreLabelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderPlayerNameAndScoreLabelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderPlayerNameAndScoreLabelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderPlayerNameAndScoreLabelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderPlayerNameAndScoreLabelInferiors = new Hashtable<String, String[]>(0);

	public static void renderPlayerScale(IRenderPlayerAPI target, net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isRenderPlayerScaleModded)
			renderPlayerAPI.renderPlayerScale(paramAbstractClientPlayer, paramFloat);
		else
			target.localRenderPlayerScale(paramAbstractClientPlayer, paramFloat);
	}

	private void renderPlayerScale(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat)
	{
		if(beforeRenderPlayerScaleHooks != null)
			for(int i = beforeRenderPlayerScaleHooks.length - 1; i >= 0 ; i--)
				beforeRenderPlayerScaleHooks[i].beforeRenderPlayerScale(paramAbstractClientPlayer, paramFloat);

		if(overrideRenderPlayerScaleHooks != null)
			overrideRenderPlayerScaleHooks[overrideRenderPlayerScaleHooks.length - 1].renderPlayerScale(paramAbstractClientPlayer, paramFloat);
		else
			renderPlayer.localRenderPlayerScale(paramAbstractClientPlayer, paramFloat);

		if(afterRenderPlayerScaleHooks != null)
			for(int i = 0; i < afterRenderPlayerScaleHooks.length; i++)
				afterRenderPlayerScaleHooks[i].afterRenderPlayerScale(paramAbstractClientPlayer, paramFloat);

	}

	protected RenderPlayerBase GetOverwrittenRenderPlayerScale(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderPlayerScaleHooks.length; i++)
			if(overrideRenderPlayerScaleHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderPlayerScaleHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderPlayerScaleHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderPlayerScaleHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderPlayerScaleHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeRenderPlayerScaleHooks;
	private RenderPlayerBase[] overrideRenderPlayerScaleHooks;
	private RenderPlayerBase[] afterRenderPlayerScaleHooks;

	public boolean isRenderPlayerScaleModded;

	private static final Map<String, String[]> allBaseBeforeRenderPlayerScaleSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderPlayerScaleInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderPlayerScaleSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderPlayerScaleInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderPlayerScaleSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderPlayerScaleInferiors = new Hashtable<String, String[]>(0);

	public static void renderPlayerSleep(IRenderPlayerAPI target, net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isRenderPlayerSleepModded)
			renderPlayerAPI.renderPlayerSleep(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3);
		else
			target.localRenderPlayerSleep(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3);
	}

	private void renderPlayerSleep(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		if(beforeRenderPlayerSleepHooks != null)
			for(int i = beforeRenderPlayerSleepHooks.length - 1; i >= 0 ; i--)
				beforeRenderPlayerSleepHooks[i].beforeRenderPlayerSleep(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3);

		if(overrideRenderPlayerSleepHooks != null)
			overrideRenderPlayerSleepHooks[overrideRenderPlayerSleepHooks.length - 1].renderPlayerSleep(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3);
		else
			renderPlayer.localRenderPlayerSleep(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3);

		if(afterRenderPlayerSleepHooks != null)
			for(int i = 0; i < afterRenderPlayerSleepHooks.length; i++)
				afterRenderPlayerSleepHooks[i].afterRenderPlayerSleep(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3);

	}

	protected RenderPlayerBase GetOverwrittenRenderPlayerSleep(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderPlayerSleepHooks.length; i++)
			if(overrideRenderPlayerSleepHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderPlayerSleepHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderPlayerSleepHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderPlayerSleepHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderPlayerSleepHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeRenderPlayerSleepHooks;
	private RenderPlayerBase[] overrideRenderPlayerSleepHooks;
	private RenderPlayerBase[] afterRenderPlayerSleepHooks;

	public boolean isRenderPlayerSleepModded;

	private static final Map<String, String[]> allBaseBeforeRenderPlayerSleepSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderPlayerSleepInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderPlayerSleepSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderPlayerSleepInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderPlayerSleepSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderPlayerSleepInferiors = new Hashtable<String, String[]>(0);

	public static void renderSpecials(IRenderPlayerAPI target, net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isRenderSpecialsModded)
			renderPlayerAPI.renderSpecials(paramAbstractClientPlayer, paramFloat);
		else
			target.localRenderSpecials(paramAbstractClientPlayer, paramFloat);
	}

	private void renderSpecials(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat)
	{
		if(beforeRenderSpecialsHooks != null)
			for(int i = beforeRenderSpecialsHooks.length - 1; i >= 0 ; i--)
				beforeRenderSpecialsHooks[i].beforeRenderSpecials(paramAbstractClientPlayer, paramFloat);

		if(overrideRenderSpecialsHooks != null)
			overrideRenderSpecialsHooks[overrideRenderSpecialsHooks.length - 1].renderSpecials(paramAbstractClientPlayer, paramFloat);
		else
			renderPlayer.localRenderSpecials(paramAbstractClientPlayer, paramFloat);

		if(afterRenderSpecialsHooks != null)
			for(int i = 0; i < afterRenderSpecialsHooks.length; i++)
				afterRenderSpecialsHooks[i].afterRenderSpecials(paramAbstractClientPlayer, paramFloat);

	}

	protected RenderPlayerBase GetOverwrittenRenderSpecials(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderSpecialsHooks.length; i++)
			if(overrideRenderSpecialsHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderSpecialsHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderSpecialsHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderSpecialsHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderSpecialsHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeRenderSpecialsHooks;
	private RenderPlayerBase[] overrideRenderSpecialsHooks;
	private RenderPlayerBase[] afterRenderSpecialsHooks;

	public boolean isRenderSpecialsModded;

	private static final Map<String, String[]> allBaseBeforeRenderSpecialsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderSpecialsInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderSpecialsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderSpecialsInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderSpecialsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderSpecialsInferiors = new Hashtable<String, String[]>(0);

	public static float renderSwingProgress(IRenderPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
		float _result;
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isRenderSwingProgressModded)
			_result = renderPlayerAPI.renderSwingProgress(paramEntityLivingBase, paramFloat);
		else
			_result = target.localRenderSwingProgress(paramEntityLivingBase, paramFloat);
		return _result;
	}

	private float renderSwingProgress(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
		if(beforeRenderSwingProgressHooks != null)
			for(int i = beforeRenderSwingProgressHooks.length - 1; i >= 0 ; i--)
				beforeRenderSwingProgressHooks[i].beforeRenderSwingProgress(paramEntityLivingBase, paramFloat);

		float _result;
		if(overrideRenderSwingProgressHooks != null)
			_result = overrideRenderSwingProgressHooks[overrideRenderSwingProgressHooks.length - 1].renderSwingProgress(paramEntityLivingBase, paramFloat);
		else
			_result = renderPlayer.localRenderSwingProgress(paramEntityLivingBase, paramFloat);

		if(afterRenderSwingProgressHooks != null)
			for(int i = 0; i < afterRenderSwingProgressHooks.length; i++)
				afterRenderSwingProgressHooks[i].afterRenderSwingProgress(paramEntityLivingBase, paramFloat);

		return _result;
	}

	protected RenderPlayerBase GetOverwrittenRenderSwingProgress(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderSwingProgressHooks.length; i++)
			if(overrideRenderSwingProgressHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderSwingProgressHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderSwingProgressHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderSwingProgressHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderSwingProgressHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeRenderSwingProgressHooks;
	private RenderPlayerBase[] overrideRenderSwingProgressHooks;
	private RenderPlayerBase[] afterRenderSwingProgressHooks;

	public boolean isRenderSwingProgressModded;

	private static final Map<String, String[]> allBaseBeforeRenderSwingProgressSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderSwingProgressInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderSwingProgressSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderSwingProgressInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderSwingProgressSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderSwingProgressInferiors = new Hashtable<String, String[]>(0);

	public static void rotatePlayer(IRenderPlayerAPI target, net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, float paramFloat3)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isRotatePlayerModded)
			renderPlayerAPI.rotatePlayer(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3);
		else
			target.localRotatePlayer(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3);
	}

	private void rotatePlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, float paramFloat3)
	{
		if(beforeRotatePlayerHooks != null)
			for(int i = beforeRotatePlayerHooks.length - 1; i >= 0 ; i--)
				beforeRotatePlayerHooks[i].beforeRotatePlayer(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3);

		if(overrideRotatePlayerHooks != null)
			overrideRotatePlayerHooks[overrideRotatePlayerHooks.length - 1].rotatePlayer(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3);
		else
			renderPlayer.localRotatePlayer(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3);

		if(afterRotatePlayerHooks != null)
			for(int i = 0; i < afterRotatePlayerHooks.length; i++)
				afterRotatePlayerHooks[i].afterRotatePlayer(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3);

	}

	protected RenderPlayerBase GetOverwrittenRotatePlayer(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRotatePlayerHooks.length; i++)
			if(overrideRotatePlayerHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRotatePlayerHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRotatePlayerHookTypes = new LinkedList<String>();
	private final static List<String> overrideRotatePlayerHookTypes = new LinkedList<String>();
	private final static List<String> afterRotatePlayerHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeRotatePlayerHooks;
	private RenderPlayerBase[] overrideRotatePlayerHooks;
	private RenderPlayerBase[] afterRotatePlayerHooks;

	public boolean isRotatePlayerModded;

	private static final Map<String, String[]> allBaseBeforeRotatePlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRotatePlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRotatePlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRotatePlayerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRotatePlayerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRotatePlayerInferiors = new Hashtable<String, String[]>(0);

	public static int setArmorModel(IRenderPlayerAPI target, net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat)
	{
		int _result;
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isSetArmorModelModded)
			_result = renderPlayerAPI.setArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);
		else
			_result = target.localSetArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);
		return _result;
	}

	private int setArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat)
	{
		if(beforeSetArmorModelHooks != null)
			for(int i = beforeSetArmorModelHooks.length - 1; i >= 0 ; i--)
				beforeSetArmorModelHooks[i].beforeSetArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);

		int _result;
		if(overrideSetArmorModelHooks != null)
			_result = overrideSetArmorModelHooks[overrideSetArmorModelHooks.length - 1].setArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);
		else
			_result = renderPlayer.localSetArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);

		if(afterSetArmorModelHooks != null)
			for(int i = 0; i < afterSetArmorModelHooks.length; i++)
				afterSetArmorModelHooks[i].afterSetArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);

		return _result;
	}

	protected RenderPlayerBase GetOverwrittenSetArmorModel(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideSetArmorModelHooks.length; i++)
			if(overrideSetArmorModelHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetArmorModelHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetArmorModelHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetArmorModelHookTypes = new LinkedList<String>();
	private final static List<String> afterSetArmorModelHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeSetArmorModelHooks;
	private RenderPlayerBase[] overrideSetArmorModelHooks;
	private RenderPlayerBase[] afterSetArmorModelHooks;

	public boolean isSetArmorModelModded;

	private static final Map<String, String[]> allBaseBeforeSetArmorModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetArmorModelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetArmorModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetArmorModelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetArmorModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetArmorModelInferiors = new Hashtable<String, String[]>(0);

	public static void setPassArmorModel(IRenderPlayerAPI target, net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isSetPassArmorModelModded)
			renderPlayerAPI.setPassArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);
		else
			target.localSetPassArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);
	}

	private void setPassArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat)
	{
		if(beforeSetPassArmorModelHooks != null)
			for(int i = beforeSetPassArmorModelHooks.length - 1; i >= 0 ; i--)
				beforeSetPassArmorModelHooks[i].beforeSetPassArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);

		if(overrideSetPassArmorModelHooks != null)
			overrideSetPassArmorModelHooks[overrideSetPassArmorModelHooks.length - 1].setPassArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);
		else
			renderPlayer.localSetPassArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);

		if(afterSetPassArmorModelHooks != null)
			for(int i = 0; i < afterSetPassArmorModelHooks.length; i++)
				afterSetPassArmorModelHooks[i].afterSetPassArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);

	}

	protected RenderPlayerBase GetOverwrittenSetPassArmorModel(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideSetPassArmorModelHooks.length; i++)
			if(overrideSetPassArmorModelHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetPassArmorModelHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetPassArmorModelHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetPassArmorModelHookTypes = new LinkedList<String>();
	private final static List<String> afterSetPassArmorModelHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeSetPassArmorModelHooks;
	private RenderPlayerBase[] overrideSetPassArmorModelHooks;
	private RenderPlayerBase[] afterSetPassArmorModelHooks;

	public boolean isSetPassArmorModelModded;

	private static final Map<String, String[]> allBaseBeforeSetPassArmorModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetPassArmorModelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetPassArmorModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetPassArmorModelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetPassArmorModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetPassArmorModelInferiors = new Hashtable<String, String[]>(0);

	public static void setRenderManager(IRenderPlayerAPI target, net.minecraft.client.renderer.entity.RenderManager paramRenderManager)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isSetRenderManagerModded)
			renderPlayerAPI.setRenderManager(paramRenderManager);
		else
			target.localSetRenderManager(paramRenderManager);
	}

	private void setRenderManager(net.minecraft.client.renderer.entity.RenderManager paramRenderManager)
	{
		if(beforeSetRenderManagerHooks != null)
			for(int i = beforeSetRenderManagerHooks.length - 1; i >= 0 ; i--)
				beforeSetRenderManagerHooks[i].beforeSetRenderManager(paramRenderManager);

		if(overrideSetRenderManagerHooks != null)
			overrideSetRenderManagerHooks[overrideSetRenderManagerHooks.length - 1].setRenderManager(paramRenderManager);
		else
			renderPlayer.localSetRenderManager(paramRenderManager);

		if(afterSetRenderManagerHooks != null)
			for(int i = 0; i < afterSetRenderManagerHooks.length; i++)
				afterSetRenderManagerHooks[i].afterSetRenderManager(paramRenderManager);

	}

	protected RenderPlayerBase GetOverwrittenSetRenderManager(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideSetRenderManagerHooks.length; i++)
			if(overrideSetRenderManagerHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetRenderManagerHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetRenderManagerHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetRenderManagerHookTypes = new LinkedList<String>();
	private final static List<String> afterSetRenderManagerHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeSetRenderManagerHooks;
	private RenderPlayerBase[] overrideSetRenderManagerHooks;
	private RenderPlayerBase[] afterSetRenderManagerHooks;

	public boolean isSetRenderManagerModded;

	private static final Map<String, String[]> allBaseBeforeSetRenderManagerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetRenderManagerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetRenderManagerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetRenderManagerInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetRenderManagerSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetRenderManagerInferiors = new Hashtable<String, String[]>(0);

	public static void setRenderPassModel(IRenderPlayerAPI target, net.minecraft.client.model.ModelBase paramModelBase)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isSetRenderPassModelModded)
			renderPlayerAPI.setRenderPassModel(paramModelBase);
		else
			target.localSetRenderPassModel(paramModelBase);
	}

	private void setRenderPassModel(net.minecraft.client.model.ModelBase paramModelBase)
	{
		if(beforeSetRenderPassModelHooks != null)
			for(int i = beforeSetRenderPassModelHooks.length - 1; i >= 0 ; i--)
				beforeSetRenderPassModelHooks[i].beforeSetRenderPassModel(paramModelBase);

		if(overrideSetRenderPassModelHooks != null)
			overrideSetRenderPassModelHooks[overrideSetRenderPassModelHooks.length - 1].setRenderPassModel(paramModelBase);
		else
			renderPlayer.localSetRenderPassModel(paramModelBase);

		if(afterSetRenderPassModelHooks != null)
			for(int i = 0; i < afterSetRenderPassModelHooks.length; i++)
				afterSetRenderPassModelHooks[i].afterSetRenderPassModel(paramModelBase);

	}

	protected RenderPlayerBase GetOverwrittenSetRenderPassModel(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideSetRenderPassModelHooks.length; i++)
			if(overrideSetRenderPassModelHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetRenderPassModelHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetRenderPassModelHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetRenderPassModelHookTypes = new LinkedList<String>();
	private final static List<String> afterSetRenderPassModelHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeSetRenderPassModelHooks;
	private RenderPlayerBase[] overrideSetRenderPassModelHooks;
	private RenderPlayerBase[] afterSetRenderPassModelHooks;

	public boolean isSetRenderPassModelModded;

	private static final Map<String, String[]> allBaseBeforeSetRenderPassModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetRenderPassModelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetRenderPassModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetRenderPassModelInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetRenderPassModelSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetRenderPassModelInferiors = new Hashtable<String, String[]>(0);

	public static void updateIcons(IRenderPlayerAPI target, net.minecraft.client.renderer.texture.IIconRegister paramIIconRegister)
	{
		RenderPlayerAPI renderPlayerAPI = target.getRenderPlayerAPI();
		if(renderPlayerAPI != null && renderPlayerAPI.isUpdateIconsModded)
			renderPlayerAPI.updateIcons(paramIIconRegister);
		else
			target.localUpdateIcons(paramIIconRegister);
	}

	private void updateIcons(net.minecraft.client.renderer.texture.IIconRegister paramIIconRegister)
	{
		if(beforeUpdateIconsHooks != null)
			for(int i = beforeUpdateIconsHooks.length - 1; i >= 0 ; i--)
				beforeUpdateIconsHooks[i].beforeUpdateIcons(paramIIconRegister);

		if(overrideUpdateIconsHooks != null)
			overrideUpdateIconsHooks[overrideUpdateIconsHooks.length - 1].updateIcons(paramIIconRegister);
		else
			renderPlayer.localUpdateIcons(paramIIconRegister);

		if(afterUpdateIconsHooks != null)
			for(int i = 0; i < afterUpdateIconsHooks.length; i++)
				afterUpdateIconsHooks[i].afterUpdateIcons(paramIIconRegister);

	}

	protected RenderPlayerBase GetOverwrittenUpdateIcons(RenderPlayerBase overWriter)
	{
		for(int i = 0; i < overrideUpdateIconsHooks.length; i++)
			if(overrideUpdateIconsHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideUpdateIconsHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeUpdateIconsHookTypes = new LinkedList<String>();
	private final static List<String> overrideUpdateIconsHookTypes = new LinkedList<String>();
	private final static List<String> afterUpdateIconsHookTypes = new LinkedList<String>();

	private RenderPlayerBase[] beforeUpdateIconsHooks;
	private RenderPlayerBase[] overrideUpdateIconsHooks;
	private RenderPlayerBase[] afterUpdateIconsHooks;

	public boolean isUpdateIconsModded;

	private static final Map<String, String[]> allBaseBeforeUpdateIconsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeUpdateIconsInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdateIconsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideUpdateIconsInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdateIconsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterUpdateIconsInferiors = new Hashtable<String, String[]>(0);

	
	protected final IRenderPlayerAPI renderPlayer;

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

	private RenderPlayerBase[] beforeLocalConstructingHooks;
	private RenderPlayerBase[] afterLocalConstructingHooks;

	private final Map<RenderPlayerBase, String> baseObjectsToId = new Hashtable<RenderPlayerBase, String>();
	private final Map<String, RenderPlayerBase> allBaseObjects = new Hashtable<String, RenderPlayerBase>();
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
