package api.player.model;

import java.util.*;

public final class ModelPlayerBaseSorting
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

	private String[] beforeGetRandomModelBoxSuperiors = null;
	private String[] beforeGetRandomModelBoxInferiors = null;
	private String[] overrideGetRandomModelBoxSuperiors = null;
	private String[] overrideGetRandomModelBoxInferiors = null;
	private String[] afterGetRandomModelBoxSuperiors = null;
	private String[] afterGetRandomModelBoxInferiors = null;

	private String[] beforeGetTextureOffsetSuperiors = null;
	private String[] beforeGetTextureOffsetInferiors = null;
	private String[] overrideGetTextureOffsetSuperiors = null;
	private String[] overrideGetTextureOffsetInferiors = null;
	private String[] afterGetTextureOffsetSuperiors = null;
	private String[] afterGetTextureOffsetInferiors = null;

	private String[] beforeRenderSuperiors = null;
	private String[] beforeRenderInferiors = null;
	private String[] overrideRenderSuperiors = null;
	private String[] overrideRenderInferiors = null;
	private String[] afterRenderSuperiors = null;
	private String[] afterRenderInferiors = null;

	private String[] beforeRenderCloakSuperiors = null;
	private String[] beforeRenderCloakInferiors = null;
	private String[] overrideRenderCloakSuperiors = null;
	private String[] overrideRenderCloakInferiors = null;
	private String[] afterRenderCloakSuperiors = null;
	private String[] afterRenderCloakInferiors = null;

	private String[] beforeRenderEarsSuperiors = null;
	private String[] beforeRenderEarsInferiors = null;
	private String[] overrideRenderEarsSuperiors = null;
	private String[] overrideRenderEarsInferiors = null;
	private String[] afterRenderEarsSuperiors = null;
	private String[] afterRenderEarsInferiors = null;

	private String[] beforeSetLivingAnimationsSuperiors = null;
	private String[] beforeSetLivingAnimationsInferiors = null;
	private String[] overrideSetLivingAnimationsSuperiors = null;
	private String[] overrideSetLivingAnimationsInferiors = null;
	private String[] afterSetLivingAnimationsSuperiors = null;
	private String[] afterSetLivingAnimationsInferiors = null;

	private String[] beforeSetRotationAnglesSuperiors = null;
	private String[] beforeSetRotationAnglesInferiors = null;
	private String[] overrideSetRotationAnglesSuperiors = null;
	private String[] overrideSetRotationAnglesInferiors = null;
	private String[] afterSetRotationAnglesSuperiors = null;
	private String[] afterSetRotationAnglesInferiors = null;

	private String[] beforeSetTextureOffsetSuperiors = null;
	private String[] beforeSetTextureOffsetInferiors = null;
	private String[] overrideSetTextureOffsetSuperiors = null;
	private String[] overrideSetTextureOffsetInferiors = null;
	private String[] afterSetTextureOffsetSuperiors = null;
	private String[] afterSetTextureOffsetInferiors = null;


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

	public String[] getBeforeGetRandomModelBoxSuperiors()
	{
		return beforeGetRandomModelBoxSuperiors;
	}

	public String[] getBeforeGetRandomModelBoxInferiors()
	{
		return beforeGetRandomModelBoxInferiors;
	}

	public String[] getOverrideGetRandomModelBoxSuperiors()
	{
		return overrideGetRandomModelBoxSuperiors;
	}

	public String[] getOverrideGetRandomModelBoxInferiors()
	{
		return overrideGetRandomModelBoxInferiors;
	}

	public String[] getAfterGetRandomModelBoxSuperiors()
	{
		return afterGetRandomModelBoxSuperiors;
	}

	public String[] getAfterGetRandomModelBoxInferiors()
	{
		return afterGetRandomModelBoxInferiors;
	}

	public void setBeforeGetRandomModelBoxSuperiors(String[] value)
	{
		beforeGetRandomModelBoxSuperiors = value;
	}

	public void setBeforeGetRandomModelBoxInferiors(String[] value)
	{
		beforeGetRandomModelBoxInferiors = value;
	}

	public void setOverrideGetRandomModelBoxSuperiors(String[] value)
	{
		overrideGetRandomModelBoxSuperiors = value;
	}

	public void setOverrideGetRandomModelBoxInferiors(String[] value)
	{
		overrideGetRandomModelBoxInferiors = value;
	}

	public void setAfterGetRandomModelBoxSuperiors(String[] value)
	{
		afterGetRandomModelBoxSuperiors = value;
	}

	public void setAfterGetRandomModelBoxInferiors(String[] value)
	{
		afterGetRandomModelBoxInferiors = value;
	}

	public String[] getBeforeGetTextureOffsetSuperiors()
	{
		return beforeGetTextureOffsetSuperiors;
	}

	public String[] getBeforeGetTextureOffsetInferiors()
	{
		return beforeGetTextureOffsetInferiors;
	}

	public String[] getOverrideGetTextureOffsetSuperiors()
	{
		return overrideGetTextureOffsetSuperiors;
	}

	public String[] getOverrideGetTextureOffsetInferiors()
	{
		return overrideGetTextureOffsetInferiors;
	}

	public String[] getAfterGetTextureOffsetSuperiors()
	{
		return afterGetTextureOffsetSuperiors;
	}

	public String[] getAfterGetTextureOffsetInferiors()
	{
		return afterGetTextureOffsetInferiors;
	}

	public void setBeforeGetTextureOffsetSuperiors(String[] value)
	{
		beforeGetTextureOffsetSuperiors = value;
	}

	public void setBeforeGetTextureOffsetInferiors(String[] value)
	{
		beforeGetTextureOffsetInferiors = value;
	}

	public void setOverrideGetTextureOffsetSuperiors(String[] value)
	{
		overrideGetTextureOffsetSuperiors = value;
	}

	public void setOverrideGetTextureOffsetInferiors(String[] value)
	{
		overrideGetTextureOffsetInferiors = value;
	}

	public void setAfterGetTextureOffsetSuperiors(String[] value)
	{
		afterGetTextureOffsetSuperiors = value;
	}

	public void setAfterGetTextureOffsetInferiors(String[] value)
	{
		afterGetTextureOffsetInferiors = value;
	}

	public String[] getBeforeRenderSuperiors()
	{
		return beforeRenderSuperiors;
	}

	public String[] getBeforeRenderInferiors()
	{
		return beforeRenderInferiors;
	}

	public String[] getOverrideRenderSuperiors()
	{
		return overrideRenderSuperiors;
	}

	public String[] getOverrideRenderInferiors()
	{
		return overrideRenderInferiors;
	}

	public String[] getAfterRenderSuperiors()
	{
		return afterRenderSuperiors;
	}

	public String[] getAfterRenderInferiors()
	{
		return afterRenderInferiors;
	}

	public void setBeforeRenderSuperiors(String[] value)
	{
		beforeRenderSuperiors = value;
	}

	public void setBeforeRenderInferiors(String[] value)
	{
		beforeRenderInferiors = value;
	}

	public void setOverrideRenderSuperiors(String[] value)
	{
		overrideRenderSuperiors = value;
	}

	public void setOverrideRenderInferiors(String[] value)
	{
		overrideRenderInferiors = value;
	}

	public void setAfterRenderSuperiors(String[] value)
	{
		afterRenderSuperiors = value;
	}

	public void setAfterRenderInferiors(String[] value)
	{
		afterRenderInferiors = value;
	}

	public String[] getBeforeRenderCloakSuperiors()
	{
		return beforeRenderCloakSuperiors;
	}

	public String[] getBeforeRenderCloakInferiors()
	{
		return beforeRenderCloakInferiors;
	}

	public String[] getOverrideRenderCloakSuperiors()
	{
		return overrideRenderCloakSuperiors;
	}

	public String[] getOverrideRenderCloakInferiors()
	{
		return overrideRenderCloakInferiors;
	}

	public String[] getAfterRenderCloakSuperiors()
	{
		return afterRenderCloakSuperiors;
	}

	public String[] getAfterRenderCloakInferiors()
	{
		return afterRenderCloakInferiors;
	}

	public void setBeforeRenderCloakSuperiors(String[] value)
	{
		beforeRenderCloakSuperiors = value;
	}

	public void setBeforeRenderCloakInferiors(String[] value)
	{
		beforeRenderCloakInferiors = value;
	}

	public void setOverrideRenderCloakSuperiors(String[] value)
	{
		overrideRenderCloakSuperiors = value;
	}

	public void setOverrideRenderCloakInferiors(String[] value)
	{
		overrideRenderCloakInferiors = value;
	}

	public void setAfterRenderCloakSuperiors(String[] value)
	{
		afterRenderCloakSuperiors = value;
	}

	public void setAfterRenderCloakInferiors(String[] value)
	{
		afterRenderCloakInferiors = value;
	}

	public String[] getBeforeRenderEarsSuperiors()
	{
		return beforeRenderEarsSuperiors;
	}

	public String[] getBeforeRenderEarsInferiors()
	{
		return beforeRenderEarsInferiors;
	}

	public String[] getOverrideRenderEarsSuperiors()
	{
		return overrideRenderEarsSuperiors;
	}

	public String[] getOverrideRenderEarsInferiors()
	{
		return overrideRenderEarsInferiors;
	}

	public String[] getAfterRenderEarsSuperiors()
	{
		return afterRenderEarsSuperiors;
	}

	public String[] getAfterRenderEarsInferiors()
	{
		return afterRenderEarsInferiors;
	}

	public void setBeforeRenderEarsSuperiors(String[] value)
	{
		beforeRenderEarsSuperiors = value;
	}

	public void setBeforeRenderEarsInferiors(String[] value)
	{
		beforeRenderEarsInferiors = value;
	}

	public void setOverrideRenderEarsSuperiors(String[] value)
	{
		overrideRenderEarsSuperiors = value;
	}

	public void setOverrideRenderEarsInferiors(String[] value)
	{
		overrideRenderEarsInferiors = value;
	}

	public void setAfterRenderEarsSuperiors(String[] value)
	{
		afterRenderEarsSuperiors = value;
	}

	public void setAfterRenderEarsInferiors(String[] value)
	{
		afterRenderEarsInferiors = value;
	}

	public String[] getBeforeSetLivingAnimationsSuperiors()
	{
		return beforeSetLivingAnimationsSuperiors;
	}

	public String[] getBeforeSetLivingAnimationsInferiors()
	{
		return beforeSetLivingAnimationsInferiors;
	}

	public String[] getOverrideSetLivingAnimationsSuperiors()
	{
		return overrideSetLivingAnimationsSuperiors;
	}

	public String[] getOverrideSetLivingAnimationsInferiors()
	{
		return overrideSetLivingAnimationsInferiors;
	}

	public String[] getAfterSetLivingAnimationsSuperiors()
	{
		return afterSetLivingAnimationsSuperiors;
	}

	public String[] getAfterSetLivingAnimationsInferiors()
	{
		return afterSetLivingAnimationsInferiors;
	}

	public void setBeforeSetLivingAnimationsSuperiors(String[] value)
	{
		beforeSetLivingAnimationsSuperiors = value;
	}

	public void setBeforeSetLivingAnimationsInferiors(String[] value)
	{
		beforeSetLivingAnimationsInferiors = value;
	}

	public void setOverrideSetLivingAnimationsSuperiors(String[] value)
	{
		overrideSetLivingAnimationsSuperiors = value;
	}

	public void setOverrideSetLivingAnimationsInferiors(String[] value)
	{
		overrideSetLivingAnimationsInferiors = value;
	}

	public void setAfterSetLivingAnimationsSuperiors(String[] value)
	{
		afterSetLivingAnimationsSuperiors = value;
	}

	public void setAfterSetLivingAnimationsInferiors(String[] value)
	{
		afterSetLivingAnimationsInferiors = value;
	}

	public String[] getBeforeSetRotationAnglesSuperiors()
	{
		return beforeSetRotationAnglesSuperiors;
	}

	public String[] getBeforeSetRotationAnglesInferiors()
	{
		return beforeSetRotationAnglesInferiors;
	}

	public String[] getOverrideSetRotationAnglesSuperiors()
	{
		return overrideSetRotationAnglesSuperiors;
	}

	public String[] getOverrideSetRotationAnglesInferiors()
	{
		return overrideSetRotationAnglesInferiors;
	}

	public String[] getAfterSetRotationAnglesSuperiors()
	{
		return afterSetRotationAnglesSuperiors;
	}

	public String[] getAfterSetRotationAnglesInferiors()
	{
		return afterSetRotationAnglesInferiors;
	}

	public void setBeforeSetRotationAnglesSuperiors(String[] value)
	{
		beforeSetRotationAnglesSuperiors = value;
	}

	public void setBeforeSetRotationAnglesInferiors(String[] value)
	{
		beforeSetRotationAnglesInferiors = value;
	}

	public void setOverrideSetRotationAnglesSuperiors(String[] value)
	{
		overrideSetRotationAnglesSuperiors = value;
	}

	public void setOverrideSetRotationAnglesInferiors(String[] value)
	{
		overrideSetRotationAnglesInferiors = value;
	}

	public void setAfterSetRotationAnglesSuperiors(String[] value)
	{
		afterSetRotationAnglesSuperiors = value;
	}

	public void setAfterSetRotationAnglesInferiors(String[] value)
	{
		afterSetRotationAnglesInferiors = value;
	}

	public String[] getBeforeSetTextureOffsetSuperiors()
	{
		return beforeSetTextureOffsetSuperiors;
	}

	public String[] getBeforeSetTextureOffsetInferiors()
	{
		return beforeSetTextureOffsetInferiors;
	}

	public String[] getOverrideSetTextureOffsetSuperiors()
	{
		return overrideSetTextureOffsetSuperiors;
	}

	public String[] getOverrideSetTextureOffsetInferiors()
	{
		return overrideSetTextureOffsetInferiors;
	}

	public String[] getAfterSetTextureOffsetSuperiors()
	{
		return afterSetTextureOffsetSuperiors;
	}

	public String[] getAfterSetTextureOffsetInferiors()
	{
		return afterSetTextureOffsetInferiors;
	}

	public void setBeforeSetTextureOffsetSuperiors(String[] value)
	{
		beforeSetTextureOffsetSuperiors = value;
	}

	public void setBeforeSetTextureOffsetInferiors(String[] value)
	{
		beforeSetTextureOffsetInferiors = value;
	}

	public void setOverrideSetTextureOffsetSuperiors(String[] value)
	{
		overrideSetTextureOffsetSuperiors = value;
	}

	public void setOverrideSetTextureOffsetInferiors(String[] value)
	{
		overrideSetTextureOffsetInferiors = value;
	}

	public void setAfterSetTextureOffsetSuperiors(String[] value)
	{
		afterSetTextureOffsetSuperiors = value;
	}

	public void setAfterSetTextureOffsetInferiors(String[] value)
	{
		afterSetTextureOffsetInferiors = value;
	}

}
