package api.player.render;

import java.util.*;

public final class RenderPlayerBaseSorting
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

	private String[] beforeDoRenderLabelSuperiors = null;
	private String[] beforeDoRenderLabelInferiors = null;
	private String[] overrideDoRenderLabelSuperiors = null;
	private String[] overrideDoRenderLabelInferiors = null;
	private String[] afterDoRenderLabelSuperiors = null;
	private String[] afterDoRenderLabelInferiors = null;

	private String[] beforeDoRenderShadowAndFireSuperiors = null;
	private String[] beforeDoRenderShadowAndFireInferiors = null;
	private String[] overrideDoRenderShadowAndFireSuperiors = null;
	private String[] overrideDoRenderShadowAndFireInferiors = null;
	private String[] afterDoRenderShadowAndFireSuperiors = null;
	private String[] afterDoRenderShadowAndFireInferiors = null;

	private String[] beforeGetColorMultiplierSuperiors = null;
	private String[] beforeGetColorMultiplierInferiors = null;
	private String[] overrideGetColorMultiplierSuperiors = null;
	private String[] overrideGetColorMultiplierInferiors = null;
	private String[] afterGetColorMultiplierSuperiors = null;
	private String[] afterGetColorMultiplierInferiors = null;

	private String[] beforeGetDeathMaxRotationSuperiors = null;
	private String[] beforeGetDeathMaxRotationInferiors = null;
	private String[] overrideGetDeathMaxRotationSuperiors = null;
	private String[] overrideGetDeathMaxRotationInferiors = null;
	private String[] afterGetDeathMaxRotationSuperiors = null;
	private String[] afterGetDeathMaxRotationInferiors = null;

	private String[] beforeGetFontRendererFromRenderManagerSuperiors = null;
	private String[] beforeGetFontRendererFromRenderManagerInferiors = null;
	private String[] overrideGetFontRendererFromRenderManagerSuperiors = null;
	private String[] overrideGetFontRendererFromRenderManagerInferiors = null;
	private String[] afterGetFontRendererFromRenderManagerSuperiors = null;
	private String[] afterGetFontRendererFromRenderManagerInferiors = null;

	private String[] beforeGetResourceLocationFromPlayerSuperiors = null;
	private String[] beforeGetResourceLocationFromPlayerInferiors = null;
	private String[] overrideGetResourceLocationFromPlayerSuperiors = null;
	private String[] overrideGetResourceLocationFromPlayerInferiors = null;
	private String[] afterGetResourceLocationFromPlayerSuperiors = null;
	private String[] afterGetResourceLocationFromPlayerInferiors = null;

	private String[] beforeHandleRotationFloatSuperiors = null;
	private String[] beforeHandleRotationFloatInferiors = null;
	private String[] overrideHandleRotationFloatSuperiors = null;
	private String[] overrideHandleRotationFloatInferiors = null;
	private String[] afterHandleRotationFloatSuperiors = null;
	private String[] afterHandleRotationFloatInferiors = null;

	private String[] beforeInheritRenderPassSuperiors = null;
	private String[] beforeInheritRenderPassInferiors = null;
	private String[] overrideInheritRenderPassSuperiors = null;
	private String[] overrideInheritRenderPassInferiors = null;
	private String[] afterInheritRenderPassSuperiors = null;
	private String[] afterInheritRenderPassInferiors = null;

	private String[] beforeLoadTextureSuperiors = null;
	private String[] beforeLoadTextureInferiors = null;
	private String[] overrideLoadTextureSuperiors = null;
	private String[] overrideLoadTextureInferiors = null;
	private String[] afterLoadTextureSuperiors = null;
	private String[] afterLoadTextureInferiors = null;

	private String[] beforeLoadTextureOfEntitySuperiors = null;
	private String[] beforeLoadTextureOfEntityInferiors = null;
	private String[] overrideLoadTextureOfEntitySuperiors = null;
	private String[] overrideLoadTextureOfEntityInferiors = null;
	private String[] afterLoadTextureOfEntitySuperiors = null;
	private String[] afterLoadTextureOfEntityInferiors = null;

	private String[] beforePassSpecialRenderSuperiors = null;
	private String[] beforePassSpecialRenderInferiors = null;
	private String[] overridePassSpecialRenderSuperiors = null;
	private String[] overridePassSpecialRenderInferiors = null;
	private String[] afterPassSpecialRenderSuperiors = null;
	private String[] afterPassSpecialRenderInferiors = null;

	private String[] beforePerformStaticEntityRebuildSuperiors = null;
	private String[] beforePerformStaticEntityRebuildInferiors = null;
	private String[] overridePerformStaticEntityRebuildSuperiors = null;
	private String[] overridePerformStaticEntityRebuildInferiors = null;
	private String[] afterPerformStaticEntityRebuildSuperiors = null;
	private String[] afterPerformStaticEntityRebuildInferiors = null;

	private String[] beforeRenderArrowsStuckInEntitySuperiors = null;
	private String[] beforeRenderArrowsStuckInEntityInferiors = null;
	private String[] overrideRenderArrowsStuckInEntitySuperiors = null;
	private String[] overrideRenderArrowsStuckInEntityInferiors = null;
	private String[] afterRenderArrowsStuckInEntitySuperiors = null;
	private String[] afterRenderArrowsStuckInEntityInferiors = null;

	private String[] beforeRenderFirstPersonArmSuperiors = null;
	private String[] beforeRenderFirstPersonArmInferiors = null;
	private String[] overrideRenderFirstPersonArmSuperiors = null;
	private String[] overrideRenderFirstPersonArmInferiors = null;
	private String[] afterRenderFirstPersonArmSuperiors = null;
	private String[] afterRenderFirstPersonArmInferiors = null;

	private String[] beforeRenderLivingLabelSuperiors = null;
	private String[] beforeRenderLivingLabelInferiors = null;
	private String[] overrideRenderLivingLabelSuperiors = null;
	private String[] overrideRenderLivingLabelInferiors = null;
	private String[] afterRenderLivingLabelSuperiors = null;
	private String[] afterRenderLivingLabelInferiors = null;

	private String[] beforeRenderModelSuperiors = null;
	private String[] beforeRenderModelInferiors = null;
	private String[] overrideRenderModelSuperiors = null;
	private String[] overrideRenderModelInferiors = null;
	private String[] afterRenderModelSuperiors = null;
	private String[] afterRenderModelInferiors = null;

	private String[] beforeRenderPlayerSuperiors = null;
	private String[] beforeRenderPlayerInferiors = null;
	private String[] overrideRenderPlayerSuperiors = null;
	private String[] overrideRenderPlayerInferiors = null;
	private String[] afterRenderPlayerSuperiors = null;
	private String[] afterRenderPlayerInferiors = null;

	private String[] beforeRenderPlayerNameAndScoreLabelSuperiors = null;
	private String[] beforeRenderPlayerNameAndScoreLabelInferiors = null;
	private String[] overrideRenderPlayerNameAndScoreLabelSuperiors = null;
	private String[] overrideRenderPlayerNameAndScoreLabelInferiors = null;
	private String[] afterRenderPlayerNameAndScoreLabelSuperiors = null;
	private String[] afterRenderPlayerNameAndScoreLabelInferiors = null;

	private String[] beforeRenderPlayerScaleSuperiors = null;
	private String[] beforeRenderPlayerScaleInferiors = null;
	private String[] overrideRenderPlayerScaleSuperiors = null;
	private String[] overrideRenderPlayerScaleInferiors = null;
	private String[] afterRenderPlayerScaleSuperiors = null;
	private String[] afterRenderPlayerScaleInferiors = null;

	private String[] beforeRenderPlayerSleepSuperiors = null;
	private String[] beforeRenderPlayerSleepInferiors = null;
	private String[] overrideRenderPlayerSleepSuperiors = null;
	private String[] overrideRenderPlayerSleepInferiors = null;
	private String[] afterRenderPlayerSleepSuperiors = null;
	private String[] afterRenderPlayerSleepInferiors = null;

	private String[] beforeRenderSpecialsSuperiors = null;
	private String[] beforeRenderSpecialsInferiors = null;
	private String[] overrideRenderSpecialsSuperiors = null;
	private String[] overrideRenderSpecialsInferiors = null;
	private String[] afterRenderSpecialsSuperiors = null;
	private String[] afterRenderSpecialsInferiors = null;

	private String[] beforeRenderSwingProgressSuperiors = null;
	private String[] beforeRenderSwingProgressInferiors = null;
	private String[] overrideRenderSwingProgressSuperiors = null;
	private String[] overrideRenderSwingProgressInferiors = null;
	private String[] afterRenderSwingProgressSuperiors = null;
	private String[] afterRenderSwingProgressInferiors = null;

	private String[] beforeRotatePlayerSuperiors = null;
	private String[] beforeRotatePlayerInferiors = null;
	private String[] overrideRotatePlayerSuperiors = null;
	private String[] overrideRotatePlayerInferiors = null;
	private String[] afterRotatePlayerSuperiors = null;
	private String[] afterRotatePlayerInferiors = null;

	private String[] beforeSetArmorModelSuperiors = null;
	private String[] beforeSetArmorModelInferiors = null;
	private String[] overrideSetArmorModelSuperiors = null;
	private String[] overrideSetArmorModelInferiors = null;
	private String[] afterSetArmorModelSuperiors = null;
	private String[] afterSetArmorModelInferiors = null;

	private String[] beforeSetPassArmorModelSuperiors = null;
	private String[] beforeSetPassArmorModelInferiors = null;
	private String[] overrideSetPassArmorModelSuperiors = null;
	private String[] overrideSetPassArmorModelInferiors = null;
	private String[] afterSetPassArmorModelSuperiors = null;
	private String[] afterSetPassArmorModelInferiors = null;

	private String[] beforeSetRenderManagerSuperiors = null;
	private String[] beforeSetRenderManagerInferiors = null;
	private String[] overrideSetRenderManagerSuperiors = null;
	private String[] overrideSetRenderManagerInferiors = null;
	private String[] afterSetRenderManagerSuperiors = null;
	private String[] afterSetRenderManagerInferiors = null;

	private String[] beforeSetRenderPassModelSuperiors = null;
	private String[] beforeSetRenderPassModelInferiors = null;
	private String[] overrideSetRenderPassModelSuperiors = null;
	private String[] overrideSetRenderPassModelInferiors = null;
	private String[] afterSetRenderPassModelSuperiors = null;
	private String[] afterSetRenderPassModelInferiors = null;

	private String[] beforeUpdateIconsSuperiors = null;
	private String[] beforeUpdateIconsInferiors = null;
	private String[] overrideUpdateIconsSuperiors = null;
	private String[] overrideUpdateIconsInferiors = null;
	private String[] afterUpdateIconsSuperiors = null;
	private String[] afterUpdateIconsInferiors = null;


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

	public String[] getBeforeDoRenderLabelSuperiors()
	{
		return beforeDoRenderLabelSuperiors;
	}

	public String[] getBeforeDoRenderLabelInferiors()
	{
		return beforeDoRenderLabelInferiors;
	}

	public String[] getOverrideDoRenderLabelSuperiors()
	{
		return overrideDoRenderLabelSuperiors;
	}

	public String[] getOverrideDoRenderLabelInferiors()
	{
		return overrideDoRenderLabelInferiors;
	}

	public String[] getAfterDoRenderLabelSuperiors()
	{
		return afterDoRenderLabelSuperiors;
	}

	public String[] getAfterDoRenderLabelInferiors()
	{
		return afterDoRenderLabelInferiors;
	}

	public void setBeforeDoRenderLabelSuperiors(String[] value)
	{
		beforeDoRenderLabelSuperiors = value;
	}

	public void setBeforeDoRenderLabelInferiors(String[] value)
	{
		beforeDoRenderLabelInferiors = value;
	}

	public void setOverrideDoRenderLabelSuperiors(String[] value)
	{
		overrideDoRenderLabelSuperiors = value;
	}

	public void setOverrideDoRenderLabelInferiors(String[] value)
	{
		overrideDoRenderLabelInferiors = value;
	}

	public void setAfterDoRenderLabelSuperiors(String[] value)
	{
		afterDoRenderLabelSuperiors = value;
	}

	public void setAfterDoRenderLabelInferiors(String[] value)
	{
		afterDoRenderLabelInferiors = value;
	}

	public String[] getBeforeDoRenderShadowAndFireSuperiors()
	{
		return beforeDoRenderShadowAndFireSuperiors;
	}

	public String[] getBeforeDoRenderShadowAndFireInferiors()
	{
		return beforeDoRenderShadowAndFireInferiors;
	}

	public String[] getOverrideDoRenderShadowAndFireSuperiors()
	{
		return overrideDoRenderShadowAndFireSuperiors;
	}

	public String[] getOverrideDoRenderShadowAndFireInferiors()
	{
		return overrideDoRenderShadowAndFireInferiors;
	}

	public String[] getAfterDoRenderShadowAndFireSuperiors()
	{
		return afterDoRenderShadowAndFireSuperiors;
	}

	public String[] getAfterDoRenderShadowAndFireInferiors()
	{
		return afterDoRenderShadowAndFireInferiors;
	}

	public void setBeforeDoRenderShadowAndFireSuperiors(String[] value)
	{
		beforeDoRenderShadowAndFireSuperiors = value;
	}

	public void setBeforeDoRenderShadowAndFireInferiors(String[] value)
	{
		beforeDoRenderShadowAndFireInferiors = value;
	}

	public void setOverrideDoRenderShadowAndFireSuperiors(String[] value)
	{
		overrideDoRenderShadowAndFireSuperiors = value;
	}

	public void setOverrideDoRenderShadowAndFireInferiors(String[] value)
	{
		overrideDoRenderShadowAndFireInferiors = value;
	}

	public void setAfterDoRenderShadowAndFireSuperiors(String[] value)
	{
		afterDoRenderShadowAndFireSuperiors = value;
	}

	public void setAfterDoRenderShadowAndFireInferiors(String[] value)
	{
		afterDoRenderShadowAndFireInferiors = value;
	}

	public String[] getBeforeGetColorMultiplierSuperiors()
	{
		return beforeGetColorMultiplierSuperiors;
	}

	public String[] getBeforeGetColorMultiplierInferiors()
	{
		return beforeGetColorMultiplierInferiors;
	}

	public String[] getOverrideGetColorMultiplierSuperiors()
	{
		return overrideGetColorMultiplierSuperiors;
	}

	public String[] getOverrideGetColorMultiplierInferiors()
	{
		return overrideGetColorMultiplierInferiors;
	}

	public String[] getAfterGetColorMultiplierSuperiors()
	{
		return afterGetColorMultiplierSuperiors;
	}

	public String[] getAfterGetColorMultiplierInferiors()
	{
		return afterGetColorMultiplierInferiors;
	}

	public void setBeforeGetColorMultiplierSuperiors(String[] value)
	{
		beforeGetColorMultiplierSuperiors = value;
	}

	public void setBeforeGetColorMultiplierInferiors(String[] value)
	{
		beforeGetColorMultiplierInferiors = value;
	}

	public void setOverrideGetColorMultiplierSuperiors(String[] value)
	{
		overrideGetColorMultiplierSuperiors = value;
	}

	public void setOverrideGetColorMultiplierInferiors(String[] value)
	{
		overrideGetColorMultiplierInferiors = value;
	}

	public void setAfterGetColorMultiplierSuperiors(String[] value)
	{
		afterGetColorMultiplierSuperiors = value;
	}

	public void setAfterGetColorMultiplierInferiors(String[] value)
	{
		afterGetColorMultiplierInferiors = value;
	}

	public String[] getBeforeGetDeathMaxRotationSuperiors()
	{
		return beforeGetDeathMaxRotationSuperiors;
	}

	public String[] getBeforeGetDeathMaxRotationInferiors()
	{
		return beforeGetDeathMaxRotationInferiors;
	}

	public String[] getOverrideGetDeathMaxRotationSuperiors()
	{
		return overrideGetDeathMaxRotationSuperiors;
	}

	public String[] getOverrideGetDeathMaxRotationInferiors()
	{
		return overrideGetDeathMaxRotationInferiors;
	}

	public String[] getAfterGetDeathMaxRotationSuperiors()
	{
		return afterGetDeathMaxRotationSuperiors;
	}

	public String[] getAfterGetDeathMaxRotationInferiors()
	{
		return afterGetDeathMaxRotationInferiors;
	}

	public void setBeforeGetDeathMaxRotationSuperiors(String[] value)
	{
		beforeGetDeathMaxRotationSuperiors = value;
	}

	public void setBeforeGetDeathMaxRotationInferiors(String[] value)
	{
		beforeGetDeathMaxRotationInferiors = value;
	}

	public void setOverrideGetDeathMaxRotationSuperiors(String[] value)
	{
		overrideGetDeathMaxRotationSuperiors = value;
	}

	public void setOverrideGetDeathMaxRotationInferiors(String[] value)
	{
		overrideGetDeathMaxRotationInferiors = value;
	}

	public void setAfterGetDeathMaxRotationSuperiors(String[] value)
	{
		afterGetDeathMaxRotationSuperiors = value;
	}

	public void setAfterGetDeathMaxRotationInferiors(String[] value)
	{
		afterGetDeathMaxRotationInferiors = value;
	}

	public String[] getBeforeGetFontRendererFromRenderManagerSuperiors()
	{
		return beforeGetFontRendererFromRenderManagerSuperiors;
	}

	public String[] getBeforeGetFontRendererFromRenderManagerInferiors()
	{
		return beforeGetFontRendererFromRenderManagerInferiors;
	}

	public String[] getOverrideGetFontRendererFromRenderManagerSuperiors()
	{
		return overrideGetFontRendererFromRenderManagerSuperiors;
	}

	public String[] getOverrideGetFontRendererFromRenderManagerInferiors()
	{
		return overrideGetFontRendererFromRenderManagerInferiors;
	}

	public String[] getAfterGetFontRendererFromRenderManagerSuperiors()
	{
		return afterGetFontRendererFromRenderManagerSuperiors;
	}

	public String[] getAfterGetFontRendererFromRenderManagerInferiors()
	{
		return afterGetFontRendererFromRenderManagerInferiors;
	}

	public void setBeforeGetFontRendererFromRenderManagerSuperiors(String[] value)
	{
		beforeGetFontRendererFromRenderManagerSuperiors = value;
	}

	public void setBeforeGetFontRendererFromRenderManagerInferiors(String[] value)
	{
		beforeGetFontRendererFromRenderManagerInferiors = value;
	}

	public void setOverrideGetFontRendererFromRenderManagerSuperiors(String[] value)
	{
		overrideGetFontRendererFromRenderManagerSuperiors = value;
	}

	public void setOverrideGetFontRendererFromRenderManagerInferiors(String[] value)
	{
		overrideGetFontRendererFromRenderManagerInferiors = value;
	}

	public void setAfterGetFontRendererFromRenderManagerSuperiors(String[] value)
	{
		afterGetFontRendererFromRenderManagerSuperiors = value;
	}

	public void setAfterGetFontRendererFromRenderManagerInferiors(String[] value)
	{
		afterGetFontRendererFromRenderManagerInferiors = value;
	}

	public String[] getBeforeGetResourceLocationFromPlayerSuperiors()
	{
		return beforeGetResourceLocationFromPlayerSuperiors;
	}

	public String[] getBeforeGetResourceLocationFromPlayerInferiors()
	{
		return beforeGetResourceLocationFromPlayerInferiors;
	}

	public String[] getOverrideGetResourceLocationFromPlayerSuperiors()
	{
		return overrideGetResourceLocationFromPlayerSuperiors;
	}

	public String[] getOverrideGetResourceLocationFromPlayerInferiors()
	{
		return overrideGetResourceLocationFromPlayerInferiors;
	}

	public String[] getAfterGetResourceLocationFromPlayerSuperiors()
	{
		return afterGetResourceLocationFromPlayerSuperiors;
	}

	public String[] getAfterGetResourceLocationFromPlayerInferiors()
	{
		return afterGetResourceLocationFromPlayerInferiors;
	}

	public void setBeforeGetResourceLocationFromPlayerSuperiors(String[] value)
	{
		beforeGetResourceLocationFromPlayerSuperiors = value;
	}

	public void setBeforeGetResourceLocationFromPlayerInferiors(String[] value)
	{
		beforeGetResourceLocationFromPlayerInferiors = value;
	}

	public void setOverrideGetResourceLocationFromPlayerSuperiors(String[] value)
	{
		overrideGetResourceLocationFromPlayerSuperiors = value;
	}

	public void setOverrideGetResourceLocationFromPlayerInferiors(String[] value)
	{
		overrideGetResourceLocationFromPlayerInferiors = value;
	}

	public void setAfterGetResourceLocationFromPlayerSuperiors(String[] value)
	{
		afterGetResourceLocationFromPlayerSuperiors = value;
	}

	public void setAfterGetResourceLocationFromPlayerInferiors(String[] value)
	{
		afterGetResourceLocationFromPlayerInferiors = value;
	}

	public String[] getBeforeHandleRotationFloatSuperiors()
	{
		return beforeHandleRotationFloatSuperiors;
	}

	public String[] getBeforeHandleRotationFloatInferiors()
	{
		return beforeHandleRotationFloatInferiors;
	}

	public String[] getOverrideHandleRotationFloatSuperiors()
	{
		return overrideHandleRotationFloatSuperiors;
	}

	public String[] getOverrideHandleRotationFloatInferiors()
	{
		return overrideHandleRotationFloatInferiors;
	}

	public String[] getAfterHandleRotationFloatSuperiors()
	{
		return afterHandleRotationFloatSuperiors;
	}

	public String[] getAfterHandleRotationFloatInferiors()
	{
		return afterHandleRotationFloatInferiors;
	}

	public void setBeforeHandleRotationFloatSuperiors(String[] value)
	{
		beforeHandleRotationFloatSuperiors = value;
	}

	public void setBeforeHandleRotationFloatInferiors(String[] value)
	{
		beforeHandleRotationFloatInferiors = value;
	}

	public void setOverrideHandleRotationFloatSuperiors(String[] value)
	{
		overrideHandleRotationFloatSuperiors = value;
	}

	public void setOverrideHandleRotationFloatInferiors(String[] value)
	{
		overrideHandleRotationFloatInferiors = value;
	}

	public void setAfterHandleRotationFloatSuperiors(String[] value)
	{
		afterHandleRotationFloatSuperiors = value;
	}

	public void setAfterHandleRotationFloatInferiors(String[] value)
	{
		afterHandleRotationFloatInferiors = value;
	}

	public String[] getBeforeInheritRenderPassSuperiors()
	{
		return beforeInheritRenderPassSuperiors;
	}

	public String[] getBeforeInheritRenderPassInferiors()
	{
		return beforeInheritRenderPassInferiors;
	}

	public String[] getOverrideInheritRenderPassSuperiors()
	{
		return overrideInheritRenderPassSuperiors;
	}

	public String[] getOverrideInheritRenderPassInferiors()
	{
		return overrideInheritRenderPassInferiors;
	}

	public String[] getAfterInheritRenderPassSuperiors()
	{
		return afterInheritRenderPassSuperiors;
	}

	public String[] getAfterInheritRenderPassInferiors()
	{
		return afterInheritRenderPassInferiors;
	}

	public void setBeforeInheritRenderPassSuperiors(String[] value)
	{
		beforeInheritRenderPassSuperiors = value;
	}

	public void setBeforeInheritRenderPassInferiors(String[] value)
	{
		beforeInheritRenderPassInferiors = value;
	}

	public void setOverrideInheritRenderPassSuperiors(String[] value)
	{
		overrideInheritRenderPassSuperiors = value;
	}

	public void setOverrideInheritRenderPassInferiors(String[] value)
	{
		overrideInheritRenderPassInferiors = value;
	}

	public void setAfterInheritRenderPassSuperiors(String[] value)
	{
		afterInheritRenderPassSuperiors = value;
	}

	public void setAfterInheritRenderPassInferiors(String[] value)
	{
		afterInheritRenderPassInferiors = value;
	}

	public String[] getBeforeLoadTextureSuperiors()
	{
		return beforeLoadTextureSuperiors;
	}

	public String[] getBeforeLoadTextureInferiors()
	{
		return beforeLoadTextureInferiors;
	}

	public String[] getOverrideLoadTextureSuperiors()
	{
		return overrideLoadTextureSuperiors;
	}

	public String[] getOverrideLoadTextureInferiors()
	{
		return overrideLoadTextureInferiors;
	}

	public String[] getAfterLoadTextureSuperiors()
	{
		return afterLoadTextureSuperiors;
	}

	public String[] getAfterLoadTextureInferiors()
	{
		return afterLoadTextureInferiors;
	}

	public void setBeforeLoadTextureSuperiors(String[] value)
	{
		beforeLoadTextureSuperiors = value;
	}

	public void setBeforeLoadTextureInferiors(String[] value)
	{
		beforeLoadTextureInferiors = value;
	}

	public void setOverrideLoadTextureSuperiors(String[] value)
	{
		overrideLoadTextureSuperiors = value;
	}

	public void setOverrideLoadTextureInferiors(String[] value)
	{
		overrideLoadTextureInferiors = value;
	}

	public void setAfterLoadTextureSuperiors(String[] value)
	{
		afterLoadTextureSuperiors = value;
	}

	public void setAfterLoadTextureInferiors(String[] value)
	{
		afterLoadTextureInferiors = value;
	}

	public String[] getBeforeLoadTextureOfEntitySuperiors()
	{
		return beforeLoadTextureOfEntitySuperiors;
	}

	public String[] getBeforeLoadTextureOfEntityInferiors()
	{
		return beforeLoadTextureOfEntityInferiors;
	}

	public String[] getOverrideLoadTextureOfEntitySuperiors()
	{
		return overrideLoadTextureOfEntitySuperiors;
	}

	public String[] getOverrideLoadTextureOfEntityInferiors()
	{
		return overrideLoadTextureOfEntityInferiors;
	}

	public String[] getAfterLoadTextureOfEntitySuperiors()
	{
		return afterLoadTextureOfEntitySuperiors;
	}

	public String[] getAfterLoadTextureOfEntityInferiors()
	{
		return afterLoadTextureOfEntityInferiors;
	}

	public void setBeforeLoadTextureOfEntitySuperiors(String[] value)
	{
		beforeLoadTextureOfEntitySuperiors = value;
	}

	public void setBeforeLoadTextureOfEntityInferiors(String[] value)
	{
		beforeLoadTextureOfEntityInferiors = value;
	}

	public void setOverrideLoadTextureOfEntitySuperiors(String[] value)
	{
		overrideLoadTextureOfEntitySuperiors = value;
	}

	public void setOverrideLoadTextureOfEntityInferiors(String[] value)
	{
		overrideLoadTextureOfEntityInferiors = value;
	}

	public void setAfterLoadTextureOfEntitySuperiors(String[] value)
	{
		afterLoadTextureOfEntitySuperiors = value;
	}

	public void setAfterLoadTextureOfEntityInferiors(String[] value)
	{
		afterLoadTextureOfEntityInferiors = value;
	}

	public String[] getBeforePassSpecialRenderSuperiors()
	{
		return beforePassSpecialRenderSuperiors;
	}

	public String[] getBeforePassSpecialRenderInferiors()
	{
		return beforePassSpecialRenderInferiors;
	}

	public String[] getOverridePassSpecialRenderSuperiors()
	{
		return overridePassSpecialRenderSuperiors;
	}

	public String[] getOverridePassSpecialRenderInferiors()
	{
		return overridePassSpecialRenderInferiors;
	}

	public String[] getAfterPassSpecialRenderSuperiors()
	{
		return afterPassSpecialRenderSuperiors;
	}

	public String[] getAfterPassSpecialRenderInferiors()
	{
		return afterPassSpecialRenderInferiors;
	}

	public void setBeforePassSpecialRenderSuperiors(String[] value)
	{
		beforePassSpecialRenderSuperiors = value;
	}

	public void setBeforePassSpecialRenderInferiors(String[] value)
	{
		beforePassSpecialRenderInferiors = value;
	}

	public void setOverridePassSpecialRenderSuperiors(String[] value)
	{
		overridePassSpecialRenderSuperiors = value;
	}

	public void setOverridePassSpecialRenderInferiors(String[] value)
	{
		overridePassSpecialRenderInferiors = value;
	}

	public void setAfterPassSpecialRenderSuperiors(String[] value)
	{
		afterPassSpecialRenderSuperiors = value;
	}

	public void setAfterPassSpecialRenderInferiors(String[] value)
	{
		afterPassSpecialRenderInferiors = value;
	}

	public String[] getBeforePerformStaticEntityRebuildSuperiors()
	{
		return beforePerformStaticEntityRebuildSuperiors;
	}

	public String[] getBeforePerformStaticEntityRebuildInferiors()
	{
		return beforePerformStaticEntityRebuildInferiors;
	}

	public String[] getOverridePerformStaticEntityRebuildSuperiors()
	{
		return overridePerformStaticEntityRebuildSuperiors;
	}

	public String[] getOverridePerformStaticEntityRebuildInferiors()
	{
		return overridePerformStaticEntityRebuildInferiors;
	}

	public String[] getAfterPerformStaticEntityRebuildSuperiors()
	{
		return afterPerformStaticEntityRebuildSuperiors;
	}

	public String[] getAfterPerformStaticEntityRebuildInferiors()
	{
		return afterPerformStaticEntityRebuildInferiors;
	}

	public void setBeforePerformStaticEntityRebuildSuperiors(String[] value)
	{
		beforePerformStaticEntityRebuildSuperiors = value;
	}

	public void setBeforePerformStaticEntityRebuildInferiors(String[] value)
	{
		beforePerformStaticEntityRebuildInferiors = value;
	}

	public void setOverridePerformStaticEntityRebuildSuperiors(String[] value)
	{
		overridePerformStaticEntityRebuildSuperiors = value;
	}

	public void setOverridePerformStaticEntityRebuildInferiors(String[] value)
	{
		overridePerformStaticEntityRebuildInferiors = value;
	}

	public void setAfterPerformStaticEntityRebuildSuperiors(String[] value)
	{
		afterPerformStaticEntityRebuildSuperiors = value;
	}

	public void setAfterPerformStaticEntityRebuildInferiors(String[] value)
	{
		afterPerformStaticEntityRebuildInferiors = value;
	}

	public String[] getBeforeRenderArrowsStuckInEntitySuperiors()
	{
		return beforeRenderArrowsStuckInEntitySuperiors;
	}

	public String[] getBeforeRenderArrowsStuckInEntityInferiors()
	{
		return beforeRenderArrowsStuckInEntityInferiors;
	}

	public String[] getOverrideRenderArrowsStuckInEntitySuperiors()
	{
		return overrideRenderArrowsStuckInEntitySuperiors;
	}

	public String[] getOverrideRenderArrowsStuckInEntityInferiors()
	{
		return overrideRenderArrowsStuckInEntityInferiors;
	}

	public String[] getAfterRenderArrowsStuckInEntitySuperiors()
	{
		return afterRenderArrowsStuckInEntitySuperiors;
	}

	public String[] getAfterRenderArrowsStuckInEntityInferiors()
	{
		return afterRenderArrowsStuckInEntityInferiors;
	}

	public void setBeforeRenderArrowsStuckInEntitySuperiors(String[] value)
	{
		beforeRenderArrowsStuckInEntitySuperiors = value;
	}

	public void setBeforeRenderArrowsStuckInEntityInferiors(String[] value)
	{
		beforeRenderArrowsStuckInEntityInferiors = value;
	}

	public void setOverrideRenderArrowsStuckInEntitySuperiors(String[] value)
	{
		overrideRenderArrowsStuckInEntitySuperiors = value;
	}

	public void setOverrideRenderArrowsStuckInEntityInferiors(String[] value)
	{
		overrideRenderArrowsStuckInEntityInferiors = value;
	}

	public void setAfterRenderArrowsStuckInEntitySuperiors(String[] value)
	{
		afterRenderArrowsStuckInEntitySuperiors = value;
	}

	public void setAfterRenderArrowsStuckInEntityInferiors(String[] value)
	{
		afterRenderArrowsStuckInEntityInferiors = value;
	}

	public String[] getBeforeRenderFirstPersonArmSuperiors()
	{
		return beforeRenderFirstPersonArmSuperiors;
	}

	public String[] getBeforeRenderFirstPersonArmInferiors()
	{
		return beforeRenderFirstPersonArmInferiors;
	}

	public String[] getOverrideRenderFirstPersonArmSuperiors()
	{
		return overrideRenderFirstPersonArmSuperiors;
	}

	public String[] getOverrideRenderFirstPersonArmInferiors()
	{
		return overrideRenderFirstPersonArmInferiors;
	}

	public String[] getAfterRenderFirstPersonArmSuperiors()
	{
		return afterRenderFirstPersonArmSuperiors;
	}

	public String[] getAfterRenderFirstPersonArmInferiors()
	{
		return afterRenderFirstPersonArmInferiors;
	}

	public void setBeforeRenderFirstPersonArmSuperiors(String[] value)
	{
		beforeRenderFirstPersonArmSuperiors = value;
	}

	public void setBeforeRenderFirstPersonArmInferiors(String[] value)
	{
		beforeRenderFirstPersonArmInferiors = value;
	}

	public void setOverrideRenderFirstPersonArmSuperiors(String[] value)
	{
		overrideRenderFirstPersonArmSuperiors = value;
	}

	public void setOverrideRenderFirstPersonArmInferiors(String[] value)
	{
		overrideRenderFirstPersonArmInferiors = value;
	}

	public void setAfterRenderFirstPersonArmSuperiors(String[] value)
	{
		afterRenderFirstPersonArmSuperiors = value;
	}

	public void setAfterRenderFirstPersonArmInferiors(String[] value)
	{
		afterRenderFirstPersonArmInferiors = value;
	}

	public String[] getBeforeRenderLivingLabelSuperiors()
	{
		return beforeRenderLivingLabelSuperiors;
	}

	public String[] getBeforeRenderLivingLabelInferiors()
	{
		return beforeRenderLivingLabelInferiors;
	}

	public String[] getOverrideRenderLivingLabelSuperiors()
	{
		return overrideRenderLivingLabelSuperiors;
	}

	public String[] getOverrideRenderLivingLabelInferiors()
	{
		return overrideRenderLivingLabelInferiors;
	}

	public String[] getAfterRenderLivingLabelSuperiors()
	{
		return afterRenderLivingLabelSuperiors;
	}

	public String[] getAfterRenderLivingLabelInferiors()
	{
		return afterRenderLivingLabelInferiors;
	}

	public void setBeforeRenderLivingLabelSuperiors(String[] value)
	{
		beforeRenderLivingLabelSuperiors = value;
	}

	public void setBeforeRenderLivingLabelInferiors(String[] value)
	{
		beforeRenderLivingLabelInferiors = value;
	}

	public void setOverrideRenderLivingLabelSuperiors(String[] value)
	{
		overrideRenderLivingLabelSuperiors = value;
	}

	public void setOverrideRenderLivingLabelInferiors(String[] value)
	{
		overrideRenderLivingLabelInferiors = value;
	}

	public void setAfterRenderLivingLabelSuperiors(String[] value)
	{
		afterRenderLivingLabelSuperiors = value;
	}

	public void setAfterRenderLivingLabelInferiors(String[] value)
	{
		afterRenderLivingLabelInferiors = value;
	}

	public String[] getBeforeRenderModelSuperiors()
	{
		return beforeRenderModelSuperiors;
	}

	public String[] getBeforeRenderModelInferiors()
	{
		return beforeRenderModelInferiors;
	}

	public String[] getOverrideRenderModelSuperiors()
	{
		return overrideRenderModelSuperiors;
	}

	public String[] getOverrideRenderModelInferiors()
	{
		return overrideRenderModelInferiors;
	}

	public String[] getAfterRenderModelSuperiors()
	{
		return afterRenderModelSuperiors;
	}

	public String[] getAfterRenderModelInferiors()
	{
		return afterRenderModelInferiors;
	}

	public void setBeforeRenderModelSuperiors(String[] value)
	{
		beforeRenderModelSuperiors = value;
	}

	public void setBeforeRenderModelInferiors(String[] value)
	{
		beforeRenderModelInferiors = value;
	}

	public void setOverrideRenderModelSuperiors(String[] value)
	{
		overrideRenderModelSuperiors = value;
	}

	public void setOverrideRenderModelInferiors(String[] value)
	{
		overrideRenderModelInferiors = value;
	}

	public void setAfterRenderModelSuperiors(String[] value)
	{
		afterRenderModelSuperiors = value;
	}

	public void setAfterRenderModelInferiors(String[] value)
	{
		afterRenderModelInferiors = value;
	}

	public String[] getBeforeRenderPlayerSuperiors()
	{
		return beforeRenderPlayerSuperiors;
	}

	public String[] getBeforeRenderPlayerInferiors()
	{
		return beforeRenderPlayerInferiors;
	}

	public String[] getOverrideRenderPlayerSuperiors()
	{
		return overrideRenderPlayerSuperiors;
	}

	public String[] getOverrideRenderPlayerInferiors()
	{
		return overrideRenderPlayerInferiors;
	}

	public String[] getAfterRenderPlayerSuperiors()
	{
		return afterRenderPlayerSuperiors;
	}

	public String[] getAfterRenderPlayerInferiors()
	{
		return afterRenderPlayerInferiors;
	}

	public void setBeforeRenderPlayerSuperiors(String[] value)
	{
		beforeRenderPlayerSuperiors = value;
	}

	public void setBeforeRenderPlayerInferiors(String[] value)
	{
		beforeRenderPlayerInferiors = value;
	}

	public void setOverrideRenderPlayerSuperiors(String[] value)
	{
		overrideRenderPlayerSuperiors = value;
	}

	public void setOverrideRenderPlayerInferiors(String[] value)
	{
		overrideRenderPlayerInferiors = value;
	}

	public void setAfterRenderPlayerSuperiors(String[] value)
	{
		afterRenderPlayerSuperiors = value;
	}

	public void setAfterRenderPlayerInferiors(String[] value)
	{
		afterRenderPlayerInferiors = value;
	}

	public String[] getBeforeRenderPlayerNameAndScoreLabelSuperiors()
	{
		return beforeRenderPlayerNameAndScoreLabelSuperiors;
	}

	public String[] getBeforeRenderPlayerNameAndScoreLabelInferiors()
	{
		return beforeRenderPlayerNameAndScoreLabelInferiors;
	}

	public String[] getOverrideRenderPlayerNameAndScoreLabelSuperiors()
	{
		return overrideRenderPlayerNameAndScoreLabelSuperiors;
	}

	public String[] getOverrideRenderPlayerNameAndScoreLabelInferiors()
	{
		return overrideRenderPlayerNameAndScoreLabelInferiors;
	}

	public String[] getAfterRenderPlayerNameAndScoreLabelSuperiors()
	{
		return afterRenderPlayerNameAndScoreLabelSuperiors;
	}

	public String[] getAfterRenderPlayerNameAndScoreLabelInferiors()
	{
		return afterRenderPlayerNameAndScoreLabelInferiors;
	}

	public void setBeforeRenderPlayerNameAndScoreLabelSuperiors(String[] value)
	{
		beforeRenderPlayerNameAndScoreLabelSuperiors = value;
	}

	public void setBeforeRenderPlayerNameAndScoreLabelInferiors(String[] value)
	{
		beforeRenderPlayerNameAndScoreLabelInferiors = value;
	}

	public void setOverrideRenderPlayerNameAndScoreLabelSuperiors(String[] value)
	{
		overrideRenderPlayerNameAndScoreLabelSuperiors = value;
	}

	public void setOverrideRenderPlayerNameAndScoreLabelInferiors(String[] value)
	{
		overrideRenderPlayerNameAndScoreLabelInferiors = value;
	}

	public void setAfterRenderPlayerNameAndScoreLabelSuperiors(String[] value)
	{
		afterRenderPlayerNameAndScoreLabelSuperiors = value;
	}

	public void setAfterRenderPlayerNameAndScoreLabelInferiors(String[] value)
	{
		afterRenderPlayerNameAndScoreLabelInferiors = value;
	}

	public String[] getBeforeRenderPlayerScaleSuperiors()
	{
		return beforeRenderPlayerScaleSuperiors;
	}

	public String[] getBeforeRenderPlayerScaleInferiors()
	{
		return beforeRenderPlayerScaleInferiors;
	}

	public String[] getOverrideRenderPlayerScaleSuperiors()
	{
		return overrideRenderPlayerScaleSuperiors;
	}

	public String[] getOverrideRenderPlayerScaleInferiors()
	{
		return overrideRenderPlayerScaleInferiors;
	}

	public String[] getAfterRenderPlayerScaleSuperiors()
	{
		return afterRenderPlayerScaleSuperiors;
	}

	public String[] getAfterRenderPlayerScaleInferiors()
	{
		return afterRenderPlayerScaleInferiors;
	}

	public void setBeforeRenderPlayerScaleSuperiors(String[] value)
	{
		beforeRenderPlayerScaleSuperiors = value;
	}

	public void setBeforeRenderPlayerScaleInferiors(String[] value)
	{
		beforeRenderPlayerScaleInferiors = value;
	}

	public void setOverrideRenderPlayerScaleSuperiors(String[] value)
	{
		overrideRenderPlayerScaleSuperiors = value;
	}

	public void setOverrideRenderPlayerScaleInferiors(String[] value)
	{
		overrideRenderPlayerScaleInferiors = value;
	}

	public void setAfterRenderPlayerScaleSuperiors(String[] value)
	{
		afterRenderPlayerScaleSuperiors = value;
	}

	public void setAfterRenderPlayerScaleInferiors(String[] value)
	{
		afterRenderPlayerScaleInferiors = value;
	}

	public String[] getBeforeRenderPlayerSleepSuperiors()
	{
		return beforeRenderPlayerSleepSuperiors;
	}

	public String[] getBeforeRenderPlayerSleepInferiors()
	{
		return beforeRenderPlayerSleepInferiors;
	}

	public String[] getOverrideRenderPlayerSleepSuperiors()
	{
		return overrideRenderPlayerSleepSuperiors;
	}

	public String[] getOverrideRenderPlayerSleepInferiors()
	{
		return overrideRenderPlayerSleepInferiors;
	}

	public String[] getAfterRenderPlayerSleepSuperiors()
	{
		return afterRenderPlayerSleepSuperiors;
	}

	public String[] getAfterRenderPlayerSleepInferiors()
	{
		return afterRenderPlayerSleepInferiors;
	}

	public void setBeforeRenderPlayerSleepSuperiors(String[] value)
	{
		beforeRenderPlayerSleepSuperiors = value;
	}

	public void setBeforeRenderPlayerSleepInferiors(String[] value)
	{
		beforeRenderPlayerSleepInferiors = value;
	}

	public void setOverrideRenderPlayerSleepSuperiors(String[] value)
	{
		overrideRenderPlayerSleepSuperiors = value;
	}

	public void setOverrideRenderPlayerSleepInferiors(String[] value)
	{
		overrideRenderPlayerSleepInferiors = value;
	}

	public void setAfterRenderPlayerSleepSuperiors(String[] value)
	{
		afterRenderPlayerSleepSuperiors = value;
	}

	public void setAfterRenderPlayerSleepInferiors(String[] value)
	{
		afterRenderPlayerSleepInferiors = value;
	}

	public String[] getBeforeRenderSpecialsSuperiors()
	{
		return beforeRenderSpecialsSuperiors;
	}

	public String[] getBeforeRenderSpecialsInferiors()
	{
		return beforeRenderSpecialsInferiors;
	}

	public String[] getOverrideRenderSpecialsSuperiors()
	{
		return overrideRenderSpecialsSuperiors;
	}

	public String[] getOverrideRenderSpecialsInferiors()
	{
		return overrideRenderSpecialsInferiors;
	}

	public String[] getAfterRenderSpecialsSuperiors()
	{
		return afterRenderSpecialsSuperiors;
	}

	public String[] getAfterRenderSpecialsInferiors()
	{
		return afterRenderSpecialsInferiors;
	}

	public void setBeforeRenderSpecialsSuperiors(String[] value)
	{
		beforeRenderSpecialsSuperiors = value;
	}

	public void setBeforeRenderSpecialsInferiors(String[] value)
	{
		beforeRenderSpecialsInferiors = value;
	}

	public void setOverrideRenderSpecialsSuperiors(String[] value)
	{
		overrideRenderSpecialsSuperiors = value;
	}

	public void setOverrideRenderSpecialsInferiors(String[] value)
	{
		overrideRenderSpecialsInferiors = value;
	}

	public void setAfterRenderSpecialsSuperiors(String[] value)
	{
		afterRenderSpecialsSuperiors = value;
	}

	public void setAfterRenderSpecialsInferiors(String[] value)
	{
		afterRenderSpecialsInferiors = value;
	}

	public String[] getBeforeRenderSwingProgressSuperiors()
	{
		return beforeRenderSwingProgressSuperiors;
	}

	public String[] getBeforeRenderSwingProgressInferiors()
	{
		return beforeRenderSwingProgressInferiors;
	}

	public String[] getOverrideRenderSwingProgressSuperiors()
	{
		return overrideRenderSwingProgressSuperiors;
	}

	public String[] getOverrideRenderSwingProgressInferiors()
	{
		return overrideRenderSwingProgressInferiors;
	}

	public String[] getAfterRenderSwingProgressSuperiors()
	{
		return afterRenderSwingProgressSuperiors;
	}

	public String[] getAfterRenderSwingProgressInferiors()
	{
		return afterRenderSwingProgressInferiors;
	}

	public void setBeforeRenderSwingProgressSuperiors(String[] value)
	{
		beforeRenderSwingProgressSuperiors = value;
	}

	public void setBeforeRenderSwingProgressInferiors(String[] value)
	{
		beforeRenderSwingProgressInferiors = value;
	}

	public void setOverrideRenderSwingProgressSuperiors(String[] value)
	{
		overrideRenderSwingProgressSuperiors = value;
	}

	public void setOverrideRenderSwingProgressInferiors(String[] value)
	{
		overrideRenderSwingProgressInferiors = value;
	}

	public void setAfterRenderSwingProgressSuperiors(String[] value)
	{
		afterRenderSwingProgressSuperiors = value;
	}

	public void setAfterRenderSwingProgressInferiors(String[] value)
	{
		afterRenderSwingProgressInferiors = value;
	}

	public String[] getBeforeRotatePlayerSuperiors()
	{
		return beforeRotatePlayerSuperiors;
	}

	public String[] getBeforeRotatePlayerInferiors()
	{
		return beforeRotatePlayerInferiors;
	}

	public String[] getOverrideRotatePlayerSuperiors()
	{
		return overrideRotatePlayerSuperiors;
	}

	public String[] getOverrideRotatePlayerInferiors()
	{
		return overrideRotatePlayerInferiors;
	}

	public String[] getAfterRotatePlayerSuperiors()
	{
		return afterRotatePlayerSuperiors;
	}

	public String[] getAfterRotatePlayerInferiors()
	{
		return afterRotatePlayerInferiors;
	}

	public void setBeforeRotatePlayerSuperiors(String[] value)
	{
		beforeRotatePlayerSuperiors = value;
	}

	public void setBeforeRotatePlayerInferiors(String[] value)
	{
		beforeRotatePlayerInferiors = value;
	}

	public void setOverrideRotatePlayerSuperiors(String[] value)
	{
		overrideRotatePlayerSuperiors = value;
	}

	public void setOverrideRotatePlayerInferiors(String[] value)
	{
		overrideRotatePlayerInferiors = value;
	}

	public void setAfterRotatePlayerSuperiors(String[] value)
	{
		afterRotatePlayerSuperiors = value;
	}

	public void setAfterRotatePlayerInferiors(String[] value)
	{
		afterRotatePlayerInferiors = value;
	}

	public String[] getBeforeSetArmorModelSuperiors()
	{
		return beforeSetArmorModelSuperiors;
	}

	public String[] getBeforeSetArmorModelInferiors()
	{
		return beforeSetArmorModelInferiors;
	}

	public String[] getOverrideSetArmorModelSuperiors()
	{
		return overrideSetArmorModelSuperiors;
	}

	public String[] getOverrideSetArmorModelInferiors()
	{
		return overrideSetArmorModelInferiors;
	}

	public String[] getAfterSetArmorModelSuperiors()
	{
		return afterSetArmorModelSuperiors;
	}

	public String[] getAfterSetArmorModelInferiors()
	{
		return afterSetArmorModelInferiors;
	}

	public void setBeforeSetArmorModelSuperiors(String[] value)
	{
		beforeSetArmorModelSuperiors = value;
	}

	public void setBeforeSetArmorModelInferiors(String[] value)
	{
		beforeSetArmorModelInferiors = value;
	}

	public void setOverrideSetArmorModelSuperiors(String[] value)
	{
		overrideSetArmorModelSuperiors = value;
	}

	public void setOverrideSetArmorModelInferiors(String[] value)
	{
		overrideSetArmorModelInferiors = value;
	}

	public void setAfterSetArmorModelSuperiors(String[] value)
	{
		afterSetArmorModelSuperiors = value;
	}

	public void setAfterSetArmorModelInferiors(String[] value)
	{
		afterSetArmorModelInferiors = value;
	}

	public String[] getBeforeSetPassArmorModelSuperiors()
	{
		return beforeSetPassArmorModelSuperiors;
	}

	public String[] getBeforeSetPassArmorModelInferiors()
	{
		return beforeSetPassArmorModelInferiors;
	}

	public String[] getOverrideSetPassArmorModelSuperiors()
	{
		return overrideSetPassArmorModelSuperiors;
	}

	public String[] getOverrideSetPassArmorModelInferiors()
	{
		return overrideSetPassArmorModelInferiors;
	}

	public String[] getAfterSetPassArmorModelSuperiors()
	{
		return afterSetPassArmorModelSuperiors;
	}

	public String[] getAfterSetPassArmorModelInferiors()
	{
		return afterSetPassArmorModelInferiors;
	}

	public void setBeforeSetPassArmorModelSuperiors(String[] value)
	{
		beforeSetPassArmorModelSuperiors = value;
	}

	public void setBeforeSetPassArmorModelInferiors(String[] value)
	{
		beforeSetPassArmorModelInferiors = value;
	}

	public void setOverrideSetPassArmorModelSuperiors(String[] value)
	{
		overrideSetPassArmorModelSuperiors = value;
	}

	public void setOverrideSetPassArmorModelInferiors(String[] value)
	{
		overrideSetPassArmorModelInferiors = value;
	}

	public void setAfterSetPassArmorModelSuperiors(String[] value)
	{
		afterSetPassArmorModelSuperiors = value;
	}

	public void setAfterSetPassArmorModelInferiors(String[] value)
	{
		afterSetPassArmorModelInferiors = value;
	}

	public String[] getBeforeSetRenderManagerSuperiors()
	{
		return beforeSetRenderManagerSuperiors;
	}

	public String[] getBeforeSetRenderManagerInferiors()
	{
		return beforeSetRenderManagerInferiors;
	}

	public String[] getOverrideSetRenderManagerSuperiors()
	{
		return overrideSetRenderManagerSuperiors;
	}

	public String[] getOverrideSetRenderManagerInferiors()
	{
		return overrideSetRenderManagerInferiors;
	}

	public String[] getAfterSetRenderManagerSuperiors()
	{
		return afterSetRenderManagerSuperiors;
	}

	public String[] getAfterSetRenderManagerInferiors()
	{
		return afterSetRenderManagerInferiors;
	}

	public void setBeforeSetRenderManagerSuperiors(String[] value)
	{
		beforeSetRenderManagerSuperiors = value;
	}

	public void setBeforeSetRenderManagerInferiors(String[] value)
	{
		beforeSetRenderManagerInferiors = value;
	}

	public void setOverrideSetRenderManagerSuperiors(String[] value)
	{
		overrideSetRenderManagerSuperiors = value;
	}

	public void setOverrideSetRenderManagerInferiors(String[] value)
	{
		overrideSetRenderManagerInferiors = value;
	}

	public void setAfterSetRenderManagerSuperiors(String[] value)
	{
		afterSetRenderManagerSuperiors = value;
	}

	public void setAfterSetRenderManagerInferiors(String[] value)
	{
		afterSetRenderManagerInferiors = value;
	}

	public String[] getBeforeSetRenderPassModelSuperiors()
	{
		return beforeSetRenderPassModelSuperiors;
	}

	public String[] getBeforeSetRenderPassModelInferiors()
	{
		return beforeSetRenderPassModelInferiors;
	}

	public String[] getOverrideSetRenderPassModelSuperiors()
	{
		return overrideSetRenderPassModelSuperiors;
	}

	public String[] getOverrideSetRenderPassModelInferiors()
	{
		return overrideSetRenderPassModelInferiors;
	}

	public String[] getAfterSetRenderPassModelSuperiors()
	{
		return afterSetRenderPassModelSuperiors;
	}

	public String[] getAfterSetRenderPassModelInferiors()
	{
		return afterSetRenderPassModelInferiors;
	}

	public void setBeforeSetRenderPassModelSuperiors(String[] value)
	{
		beforeSetRenderPassModelSuperiors = value;
	}

	public void setBeforeSetRenderPassModelInferiors(String[] value)
	{
		beforeSetRenderPassModelInferiors = value;
	}

	public void setOverrideSetRenderPassModelSuperiors(String[] value)
	{
		overrideSetRenderPassModelSuperiors = value;
	}

	public void setOverrideSetRenderPassModelInferiors(String[] value)
	{
		overrideSetRenderPassModelInferiors = value;
	}

	public void setAfterSetRenderPassModelSuperiors(String[] value)
	{
		afterSetRenderPassModelSuperiors = value;
	}

	public void setAfterSetRenderPassModelInferiors(String[] value)
	{
		afterSetRenderPassModelInferiors = value;
	}

	public String[] getBeforeUpdateIconsSuperiors()
	{
		return beforeUpdateIconsSuperiors;
	}

	public String[] getBeforeUpdateIconsInferiors()
	{
		return beforeUpdateIconsInferiors;
	}

	public String[] getOverrideUpdateIconsSuperiors()
	{
		return overrideUpdateIconsSuperiors;
	}

	public String[] getOverrideUpdateIconsInferiors()
	{
		return overrideUpdateIconsInferiors;
	}

	public String[] getAfterUpdateIconsSuperiors()
	{
		return afterUpdateIconsSuperiors;
	}

	public String[] getAfterUpdateIconsInferiors()
	{
		return afterUpdateIconsInferiors;
	}

	public void setBeforeUpdateIconsSuperiors(String[] value)
	{
		beforeUpdateIconsSuperiors = value;
	}

	public void setBeforeUpdateIconsInferiors(String[] value)
	{
		beforeUpdateIconsInferiors = value;
	}

	public void setOverrideUpdateIconsSuperiors(String[] value)
	{
		overrideUpdateIconsSuperiors = value;
	}

	public void setOverrideUpdateIconsInferiors(String[] value)
	{
		overrideUpdateIconsInferiors = value;
	}

	public void setAfterUpdateIconsSuperiors(String[] value)
	{
		afterUpdateIconsSuperiors = value;
	}

	public void setAfterUpdateIconsInferiors(String[] value)
	{
		afterUpdateIconsInferiors = value;
	}

}
