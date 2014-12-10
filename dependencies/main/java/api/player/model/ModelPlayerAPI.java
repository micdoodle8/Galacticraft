package api.player.model;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.lang.reflect.*;

public final class ModelPlayerAPI
{
	private final static Class<?>[] Class = new Class[] { ModelPlayerAPI.class };
	private final static Class<?>[] Classes = new Class[] { ModelPlayerAPI.class, String.class };

	private static boolean isCreated;
	private static final Logger logger = Logger.getLogger("ModelPlayerAPI");

	private static void log(String text)
	{
		System.out.println(text);
		logger.fine(text);
	}

	public static void register(String id, Class<?> baseClass)
	{
		register(id, baseClass, null);
	}

	public static void register(String id, Class<?> baseClass, ModelPlayerBaseSorting baseSorting)
	{
		try
		{
			register(baseClass, id, baseSorting);
		}
		catch(RuntimeException exception)
		{
			if(id != null)
				log("Model Player: failed to register id '" + id + "'");
			else
				log("Model Player: failed to register ModelPlayerBase");

			throw exception;
		}
	}

	private static void register(Class<?> baseClass, String id, ModelPlayerBaseSorting baseSorting)
	{
		if(!isCreated)
		{
			try
			{
				Method mandatory = api.player.model.ModelPlayer.class.getMethod("getModelPlayerBase", String.class);
				if (mandatory.getReturnType() != ModelPlayerBase.class)
					throw new NoSuchMethodException(ModelPlayerBase.class.getName() + " " + api.player.model.ModelPlayer.class.getName() + ".getModelPlayerBase(" + String.class.getName() + ")");
			}
			catch(NoSuchMethodException exception)
			{
				String[] errorMessageParts = new String[]
				{
					"========================================",
					"The API \"Model Player\" version 1.0 of the mod \"Render Player API core 1.0\" can not be created!",
					"----------------------------------------",
					"Mandatory member method \"{0} getModelPlayerBase({3})\" not found in class \"{1}\".",
					"There are three scenarios this can happen:",
					"* Minecraft Forge is missing a Render Player API core which Minecraft version matches its own.",
					"  Download and install the latest Render Player API core for the Minecraft version you were trying to run.",
					"* The code of the class \"{2}\" of Render Player API core has been modified beyond recognition by another Minecraft Forge coremod.",
					"  Try temporary deinstallation of other core mods to find the culprit and deinstall it permanently to fix this specific problem.",
					"* Render Player API core has not been installed correctly.",
					"  Deinstall Render Player API core and install it again following the installation instructions in the readme file.",
					"========================================"
				};

				String baseModelPlayerClassName = ModelPlayerBase.class.getName();
				String targetClassName = api.player.model.ModelPlayer.class.getName();
				String targetClassFileName = targetClassName.replace(".", File.separator);
				String stringClassName = String.class.getName();

				for(int i=0; i<errorMessageParts.length; i++)
					errorMessageParts[i] = MessageFormat.format(errorMessageParts[i], baseModelPlayerClassName, targetClassName, targetClassFileName, stringClassName);

				for(String errorMessagePart : errorMessageParts)
					logger.severe(errorMessagePart);

				for(String errorMessagePart : errorMessageParts)
					System.err.println(errorMessagePart);

				String errorMessage = "\n\n";
				for(String errorMessagePart : errorMessageParts)
					errorMessage += "\t" + errorMessagePart + "\n";

				throw new RuntimeException(errorMessage, exception);
			}

			log("Model Player 1.0 Created");
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
				throw new IllegalArgumentException("Can not find necessary constructor with one argument of type '" + ModelPlayerAPI.class.getName() + "' and eventually a second argument of type 'String' in the class '" + baseClass.getName() + "'", t);
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

			addSorting(id, allBaseBeforeGetRandomModelBoxSuperiors, baseSorting.getBeforeGetRandomModelBoxSuperiors());
			addSorting(id, allBaseBeforeGetRandomModelBoxInferiors, baseSorting.getBeforeGetRandomModelBoxInferiors());
			addSorting(id, allBaseOverrideGetRandomModelBoxSuperiors, baseSorting.getOverrideGetRandomModelBoxSuperiors());
			addSorting(id, allBaseOverrideGetRandomModelBoxInferiors, baseSorting.getOverrideGetRandomModelBoxInferiors());
			addSorting(id, allBaseAfterGetRandomModelBoxSuperiors, baseSorting.getAfterGetRandomModelBoxSuperiors());
			addSorting(id, allBaseAfterGetRandomModelBoxInferiors, baseSorting.getAfterGetRandomModelBoxInferiors());

			addSorting(id, allBaseBeforeGetTextureOffsetSuperiors, baseSorting.getBeforeGetTextureOffsetSuperiors());
			addSorting(id, allBaseBeforeGetTextureOffsetInferiors, baseSorting.getBeforeGetTextureOffsetInferiors());
			addSorting(id, allBaseOverrideGetTextureOffsetSuperiors, baseSorting.getOverrideGetTextureOffsetSuperiors());
			addSorting(id, allBaseOverrideGetTextureOffsetInferiors, baseSorting.getOverrideGetTextureOffsetInferiors());
			addSorting(id, allBaseAfterGetTextureOffsetSuperiors, baseSorting.getAfterGetTextureOffsetSuperiors());
			addSorting(id, allBaseAfterGetTextureOffsetInferiors, baseSorting.getAfterGetTextureOffsetInferiors());

			addSorting(id, allBaseBeforeRenderSuperiors, baseSorting.getBeforeRenderSuperiors());
			addSorting(id, allBaseBeforeRenderInferiors, baseSorting.getBeforeRenderInferiors());
			addSorting(id, allBaseOverrideRenderSuperiors, baseSorting.getOverrideRenderSuperiors());
			addSorting(id, allBaseOverrideRenderInferiors, baseSorting.getOverrideRenderInferiors());
			addSorting(id, allBaseAfterRenderSuperiors, baseSorting.getAfterRenderSuperiors());
			addSorting(id, allBaseAfterRenderInferiors, baseSorting.getAfterRenderInferiors());

			addSorting(id, allBaseBeforeRenderCloakSuperiors, baseSorting.getBeforeRenderCloakSuperiors());
			addSorting(id, allBaseBeforeRenderCloakInferiors, baseSorting.getBeforeRenderCloakInferiors());
			addSorting(id, allBaseOverrideRenderCloakSuperiors, baseSorting.getOverrideRenderCloakSuperiors());
			addSorting(id, allBaseOverrideRenderCloakInferiors, baseSorting.getOverrideRenderCloakInferiors());
			addSorting(id, allBaseAfterRenderCloakSuperiors, baseSorting.getAfterRenderCloakSuperiors());
			addSorting(id, allBaseAfterRenderCloakInferiors, baseSorting.getAfterRenderCloakInferiors());

			addSorting(id, allBaseBeforeRenderEarsSuperiors, baseSorting.getBeforeRenderEarsSuperiors());
			addSorting(id, allBaseBeforeRenderEarsInferiors, baseSorting.getBeforeRenderEarsInferiors());
			addSorting(id, allBaseOverrideRenderEarsSuperiors, baseSorting.getOverrideRenderEarsSuperiors());
			addSorting(id, allBaseOverrideRenderEarsInferiors, baseSorting.getOverrideRenderEarsInferiors());
			addSorting(id, allBaseAfterRenderEarsSuperiors, baseSorting.getAfterRenderEarsSuperiors());
			addSorting(id, allBaseAfterRenderEarsInferiors, baseSorting.getAfterRenderEarsInferiors());

			addSorting(id, allBaseBeforeSetLivingAnimationsSuperiors, baseSorting.getBeforeSetLivingAnimationsSuperiors());
			addSorting(id, allBaseBeforeSetLivingAnimationsInferiors, baseSorting.getBeforeSetLivingAnimationsInferiors());
			addSorting(id, allBaseOverrideSetLivingAnimationsSuperiors, baseSorting.getOverrideSetLivingAnimationsSuperiors());
			addSorting(id, allBaseOverrideSetLivingAnimationsInferiors, baseSorting.getOverrideSetLivingAnimationsInferiors());
			addSorting(id, allBaseAfterSetLivingAnimationsSuperiors, baseSorting.getAfterSetLivingAnimationsSuperiors());
			addSorting(id, allBaseAfterSetLivingAnimationsInferiors, baseSorting.getAfterSetLivingAnimationsInferiors());

			addSorting(id, allBaseBeforeSetRotationAnglesSuperiors, baseSorting.getBeforeSetRotationAnglesSuperiors());
			addSorting(id, allBaseBeforeSetRotationAnglesInferiors, baseSorting.getBeforeSetRotationAnglesInferiors());
			addSorting(id, allBaseOverrideSetRotationAnglesSuperiors, baseSorting.getOverrideSetRotationAnglesSuperiors());
			addSorting(id, allBaseOverrideSetRotationAnglesInferiors, baseSorting.getOverrideSetRotationAnglesInferiors());
			addSorting(id, allBaseAfterSetRotationAnglesSuperiors, baseSorting.getAfterSetRotationAnglesSuperiors());
			addSorting(id, allBaseAfterSetRotationAnglesInferiors, baseSorting.getAfterSetRotationAnglesInferiors());

			addSorting(id, allBaseBeforeSetTextureOffsetSuperiors, baseSorting.getBeforeSetTextureOffsetSuperiors());
			addSorting(id, allBaseBeforeSetTextureOffsetInferiors, baseSorting.getBeforeSetTextureOffsetInferiors());
			addSorting(id, allBaseOverrideSetTextureOffsetSuperiors, baseSorting.getOverrideSetTextureOffsetSuperiors());
			addSorting(id, allBaseOverrideSetTextureOffsetInferiors, baseSorting.getOverrideSetTextureOffsetInferiors());
			addSorting(id, allBaseAfterSetTextureOffsetSuperiors, baseSorting.getAfterSetTextureOffsetSuperiors());
			addSorting(id, allBaseAfterSetTextureOffsetInferiors, baseSorting.getAfterSetTextureOffsetInferiors());

		}

		addMethod(id, baseClass, beforeLocalConstructingHookTypes, "beforeLocalConstructing", float.class);
		addMethod(id, baseClass, afterLocalConstructingHookTypes, "afterLocalConstructing", float.class);


		addMethod(id, baseClass, beforeGetRandomModelBoxHookTypes, "beforeGetRandomModelBox", java.util.Random.class);
		addMethod(id, baseClass, overrideGetRandomModelBoxHookTypes, "getRandomModelBox", java.util.Random.class);
		addMethod(id, baseClass, afterGetRandomModelBoxHookTypes, "afterGetRandomModelBox", java.util.Random.class);

		addMethod(id, baseClass, beforeGetTextureOffsetHookTypes, "beforeGetTextureOffset", String.class);
		addMethod(id, baseClass, overrideGetTextureOffsetHookTypes, "getTextureOffset", String.class);
		addMethod(id, baseClass, afterGetTextureOffsetHookTypes, "afterGetTextureOffset", String.class);

		addMethod(id, baseClass, beforeRenderHookTypes, "beforeRender", net.minecraft.entity.Entity.class, float.class, float.class, float.class, float.class, float.class, float.class);
		addMethod(id, baseClass, overrideRenderHookTypes, "render", net.minecraft.entity.Entity.class, float.class, float.class, float.class, float.class, float.class, float.class);
		addMethod(id, baseClass, afterRenderHookTypes, "afterRender", net.minecraft.entity.Entity.class, float.class, float.class, float.class, float.class, float.class, float.class);

		addMethod(id, baseClass, beforeRenderCloakHookTypes, "beforeRenderCloak", float.class);
		addMethod(id, baseClass, overrideRenderCloakHookTypes, "renderCloak", float.class);
		addMethod(id, baseClass, afterRenderCloakHookTypes, "afterRenderCloak", float.class);

		addMethod(id, baseClass, beforeRenderEarsHookTypes, "beforeRenderEars", float.class);
		addMethod(id, baseClass, overrideRenderEarsHookTypes, "renderEars", float.class);
		addMethod(id, baseClass, afterRenderEarsHookTypes, "afterRenderEars", float.class);

		addMethod(id, baseClass, beforeSetLivingAnimationsHookTypes, "beforeSetLivingAnimations", net.minecraft.entity.EntityLivingBase.class, float.class, float.class, float.class);
		addMethod(id, baseClass, overrideSetLivingAnimationsHookTypes, "setLivingAnimations", net.minecraft.entity.EntityLivingBase.class, float.class, float.class, float.class);
		addMethod(id, baseClass, afterSetLivingAnimationsHookTypes, "afterSetLivingAnimations", net.minecraft.entity.EntityLivingBase.class, float.class, float.class, float.class);

		addMethod(id, baseClass, beforeSetRotationAnglesHookTypes, "beforeSetRotationAngles", float.class, float.class, float.class, float.class, float.class, float.class, net.minecraft.entity.Entity.class);
		addMethod(id, baseClass, overrideSetRotationAnglesHookTypes, "setRotationAngles", float.class, float.class, float.class, float.class, float.class, float.class, net.minecraft.entity.Entity.class);
		addMethod(id, baseClass, afterSetRotationAnglesHookTypes, "afterSetRotationAngles", float.class, float.class, float.class, float.class, float.class, float.class, net.minecraft.entity.Entity.class);

		addMethod(id, baseClass, beforeSetTextureOffsetHookTypes, "beforeSetTextureOffset", String.class, int.class, int.class);
		addMethod(id, baseClass, overrideSetTextureOffsetHookTypes, "setTextureOffset", String.class, int.class, int.class);
		addMethod(id, baseClass, afterSetTextureOffsetHookTypes, "afterSetTextureOffset", String.class, int.class, int.class);


		addDynamicMethods(id, baseClass);

		addDynamicKeys(id, baseClass, beforeDynamicHookMethods, beforeDynamicHookTypes);
		addDynamicKeys(id, baseClass, overrideDynamicHookMethods, overrideDynamicHookTypes);
		addDynamicKeys(id, baseClass, afterDynamicHookMethods, afterDynamicHookTypes);

		initialize();

		for(IModelPlayerAPI instance : allInstances)
			instance.getModelPlayerAPI().attachModelPlayerBase(id);

		System.out.println("Model Player: registered " + id);
		logger.fine("Model Player: registered class '" + baseClass.getName() + "' with id '" + id + "'");

		initialized = false;
	}

	public static boolean unregister(String id)
	{
		if(id == null)
			return false;

		Constructor<?> constructor = allBaseConstructors.remove(id);
		if(constructor == null)
			return false;

		for(IModelPlayerAPI instance : allInstances)
			instance.getModelPlayerAPI().detachModelPlayerBase(id);

		beforeLocalConstructingHookTypes.remove(id);
		afterLocalConstructingHookTypes.remove(id);

		allBaseBeforeGetRandomModelBoxSuperiors.remove(id);
		allBaseBeforeGetRandomModelBoxInferiors.remove(id);
		allBaseOverrideGetRandomModelBoxSuperiors.remove(id);
		allBaseOverrideGetRandomModelBoxInferiors.remove(id);
		allBaseAfterGetRandomModelBoxSuperiors.remove(id);
		allBaseAfterGetRandomModelBoxInferiors.remove(id);

		beforeGetRandomModelBoxHookTypes.remove(id);
		overrideGetRandomModelBoxHookTypes.remove(id);
		afterGetRandomModelBoxHookTypes.remove(id);

		allBaseBeforeGetTextureOffsetSuperiors.remove(id);
		allBaseBeforeGetTextureOffsetInferiors.remove(id);
		allBaseOverrideGetTextureOffsetSuperiors.remove(id);
		allBaseOverrideGetTextureOffsetInferiors.remove(id);
		allBaseAfterGetTextureOffsetSuperiors.remove(id);
		allBaseAfterGetTextureOffsetInferiors.remove(id);

		beforeGetTextureOffsetHookTypes.remove(id);
		overrideGetTextureOffsetHookTypes.remove(id);
		afterGetTextureOffsetHookTypes.remove(id);

		allBaseBeforeRenderSuperiors.remove(id);
		allBaseBeforeRenderInferiors.remove(id);
		allBaseOverrideRenderSuperiors.remove(id);
		allBaseOverrideRenderInferiors.remove(id);
		allBaseAfterRenderSuperiors.remove(id);
		allBaseAfterRenderInferiors.remove(id);

		beforeRenderHookTypes.remove(id);
		overrideRenderHookTypes.remove(id);
		afterRenderHookTypes.remove(id);

		allBaseBeforeRenderCloakSuperiors.remove(id);
		allBaseBeforeRenderCloakInferiors.remove(id);
		allBaseOverrideRenderCloakSuperiors.remove(id);
		allBaseOverrideRenderCloakInferiors.remove(id);
		allBaseAfterRenderCloakSuperiors.remove(id);
		allBaseAfterRenderCloakInferiors.remove(id);

		beforeRenderCloakHookTypes.remove(id);
		overrideRenderCloakHookTypes.remove(id);
		afterRenderCloakHookTypes.remove(id);

		allBaseBeforeRenderEarsSuperiors.remove(id);
		allBaseBeforeRenderEarsInferiors.remove(id);
		allBaseOverrideRenderEarsSuperiors.remove(id);
		allBaseOverrideRenderEarsInferiors.remove(id);
		allBaseAfterRenderEarsSuperiors.remove(id);
		allBaseAfterRenderEarsInferiors.remove(id);

		beforeRenderEarsHookTypes.remove(id);
		overrideRenderEarsHookTypes.remove(id);
		afterRenderEarsHookTypes.remove(id);

		allBaseBeforeSetLivingAnimationsSuperiors.remove(id);
		allBaseBeforeSetLivingAnimationsInferiors.remove(id);
		allBaseOverrideSetLivingAnimationsSuperiors.remove(id);
		allBaseOverrideSetLivingAnimationsInferiors.remove(id);
		allBaseAfterSetLivingAnimationsSuperiors.remove(id);
		allBaseAfterSetLivingAnimationsInferiors.remove(id);

		beforeSetLivingAnimationsHookTypes.remove(id);
		overrideSetLivingAnimationsHookTypes.remove(id);
		afterSetLivingAnimationsHookTypes.remove(id);

		allBaseBeforeSetRotationAnglesSuperiors.remove(id);
		allBaseBeforeSetRotationAnglesInferiors.remove(id);
		allBaseOverrideSetRotationAnglesSuperiors.remove(id);
		allBaseOverrideSetRotationAnglesInferiors.remove(id);
		allBaseAfterSetRotationAnglesSuperiors.remove(id);
		allBaseAfterSetRotationAnglesInferiors.remove(id);

		beforeSetRotationAnglesHookTypes.remove(id);
		overrideSetRotationAnglesHookTypes.remove(id);
		afterSetRotationAnglesHookTypes.remove(id);

		allBaseBeforeSetTextureOffsetSuperiors.remove(id);
		allBaseBeforeSetTextureOffsetInferiors.remove(id);
		allBaseOverrideSetTextureOffsetSuperiors.remove(id);
		allBaseOverrideSetTextureOffsetInferiors.remove(id);
		allBaseAfterSetTextureOffsetSuperiors.remove(id);
		allBaseAfterSetTextureOffsetInferiors.remove(id);

		beforeSetTextureOffsetHookTypes.remove(id);
		overrideSetTextureOffsetHookTypes.remove(id);
		afterSetTextureOffsetHookTypes.remove(id);


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

		log("ModelPlayerAPI: unregistered id '" + id + "'");

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
			boolean isOverridden = method.getDeclaringClass() != ModelPlayerBase.class;
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

	public static ModelPlayerAPI create(IModelPlayerAPI modelPlayer, float paramFloat, String type)
	{
		if(allBaseConstructors.size() > 0 && !initialized)
			initialize();
		return new ModelPlayerAPI(modelPlayer, paramFloat, type);
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

		sortBases(beforeGetRandomModelBoxHookTypes, allBaseBeforeGetRandomModelBoxSuperiors, allBaseBeforeGetRandomModelBoxInferiors, "beforeGetRandomModelBox");
		sortBases(overrideGetRandomModelBoxHookTypes, allBaseOverrideGetRandomModelBoxSuperiors, allBaseOverrideGetRandomModelBoxInferiors, "overrideGetRandomModelBox");
		sortBases(afterGetRandomModelBoxHookTypes, allBaseAfterGetRandomModelBoxSuperiors, allBaseAfterGetRandomModelBoxInferiors, "afterGetRandomModelBox");

		sortBases(beforeGetTextureOffsetHookTypes, allBaseBeforeGetTextureOffsetSuperiors, allBaseBeforeGetTextureOffsetInferiors, "beforeGetTextureOffset");
		sortBases(overrideGetTextureOffsetHookTypes, allBaseOverrideGetTextureOffsetSuperiors, allBaseOverrideGetTextureOffsetInferiors, "overrideGetTextureOffset");
		sortBases(afterGetTextureOffsetHookTypes, allBaseAfterGetTextureOffsetSuperiors, allBaseAfterGetTextureOffsetInferiors, "afterGetTextureOffset");

		sortBases(beforeRenderHookTypes, allBaseBeforeRenderSuperiors, allBaseBeforeRenderInferiors, "beforeRender");
		sortBases(overrideRenderHookTypes, allBaseOverrideRenderSuperiors, allBaseOverrideRenderInferiors, "overrideRender");
		sortBases(afterRenderHookTypes, allBaseAfterRenderSuperiors, allBaseAfterRenderInferiors, "afterRender");

		sortBases(beforeRenderCloakHookTypes, allBaseBeforeRenderCloakSuperiors, allBaseBeforeRenderCloakInferiors, "beforeRenderCloak");
		sortBases(overrideRenderCloakHookTypes, allBaseOverrideRenderCloakSuperiors, allBaseOverrideRenderCloakInferiors, "overrideRenderCloak");
		sortBases(afterRenderCloakHookTypes, allBaseAfterRenderCloakSuperiors, allBaseAfterRenderCloakInferiors, "afterRenderCloak");

		sortBases(beforeRenderEarsHookTypes, allBaseBeforeRenderEarsSuperiors, allBaseBeforeRenderEarsInferiors, "beforeRenderEars");
		sortBases(overrideRenderEarsHookTypes, allBaseOverrideRenderEarsSuperiors, allBaseOverrideRenderEarsInferiors, "overrideRenderEars");
		sortBases(afterRenderEarsHookTypes, allBaseAfterRenderEarsSuperiors, allBaseAfterRenderEarsInferiors, "afterRenderEars");

		sortBases(beforeSetLivingAnimationsHookTypes, allBaseBeforeSetLivingAnimationsSuperiors, allBaseBeforeSetLivingAnimationsInferiors, "beforeSetLivingAnimations");
		sortBases(overrideSetLivingAnimationsHookTypes, allBaseOverrideSetLivingAnimationsSuperiors, allBaseOverrideSetLivingAnimationsInferiors, "overrideSetLivingAnimations");
		sortBases(afterSetLivingAnimationsHookTypes, allBaseAfterSetLivingAnimationsSuperiors, allBaseAfterSetLivingAnimationsInferiors, "afterSetLivingAnimations");

		sortBases(beforeSetRotationAnglesHookTypes, allBaseBeforeSetRotationAnglesSuperiors, allBaseBeforeSetRotationAnglesInferiors, "beforeSetRotationAngles");
		sortBases(overrideSetRotationAnglesHookTypes, allBaseOverrideSetRotationAnglesSuperiors, allBaseOverrideSetRotationAnglesInferiors, "overrideSetRotationAngles");
		sortBases(afterSetRotationAnglesHookTypes, allBaseAfterSetRotationAnglesSuperiors, allBaseAfterSetRotationAnglesInferiors, "afterSetRotationAngles");

		sortBases(beforeSetTextureOffsetHookTypes, allBaseBeforeSetTextureOffsetSuperiors, allBaseBeforeSetTextureOffsetInferiors, "beforeSetTextureOffset");
		sortBases(overrideSetTextureOffsetHookTypes, allBaseOverrideSetTextureOffsetSuperiors, allBaseOverrideSetTextureOffsetInferiors, "overrideSetTextureOffset");
		sortBases(afterSetTextureOffsetHookTypes, allBaseAfterSetTextureOffsetSuperiors, allBaseAfterSetTextureOffsetInferiors, "afterSetTextureOffset");

		initialized = true;
	}

	private static List<IModelPlayerAPI> allInstances = new ArrayList<IModelPlayerAPI>();

	public static api.player.model.ModelPlayer[] getAllInstances()
	{
		return allInstances.toArray(new api.player.model.ModelPlayer[allInstances.size()]);
	}

	public static void beforeLocalConstructing(IModelPlayerAPI modelPlayer, float paramFloat)
	{
		ModelPlayerAPI modelPlayerAPI = modelPlayer.getModelPlayerAPI();
		if(modelPlayerAPI != null)
			modelPlayerAPI.load();

		allInstances.add(modelPlayer);

		if(modelPlayerAPI != null)
			modelPlayerAPI.beforeLocalConstructing(paramFloat);
	}

	public static void afterLocalConstructing(IModelPlayerAPI modelPlayer, float paramFloat)
	{
		ModelPlayerAPI modelPlayerAPI = modelPlayer.getModelPlayerAPI();
		if(modelPlayerAPI != null)
			modelPlayerAPI.afterLocalConstructing(paramFloat);
	}

	public static ModelPlayerBase getModelPlayerBase(IModelPlayerAPI modelPlayer, String baseId)
	{
		ModelPlayerAPI modelPlayerAPI = modelPlayer.getModelPlayerAPI();
		if(modelPlayerAPI != null)
			return modelPlayerAPI.getModelPlayerBase(baseId);
		return null;
	}

	public static Set<String> getModelPlayerBaseIds(IModelPlayerAPI modelPlayer)
	{
		ModelPlayerAPI modelPlayerAPI = modelPlayer.getModelPlayerAPI();
		Set<String> result = null;
		if(modelPlayerAPI != null)
			result = modelPlayerAPI.getModelPlayerBaseIds();
		else
			result = Collections.<String>emptySet();
		return result;
	}

	public static float getExpandParameter(IModelPlayerAPI modelPlayer)
	{
		ModelPlayerAPI modelPlayerAPI = modelPlayer.getModelPlayerAPI();
		if(modelPlayerAPI != null)
			return modelPlayerAPI.paramFloat;
		return 0;
	}

	public static String getModelPlayerType(IModelPlayerAPI modelPlayer)
	{
		ModelPlayerAPI modelPlayerAPI = modelPlayer.getModelPlayerAPI();
		if(modelPlayerAPI != null && modelPlayerAPI.type != null)
			return modelPlayerAPI.type;
		return "other";
	}

	public static Object dynamic(IModelPlayerAPI modelPlayer, String key, Object[] parameters)
	{
		ModelPlayerAPI modelPlayerAPI = modelPlayer.getModelPlayerAPI();
		if(modelPlayerAPI != null)
			return modelPlayerAPI.dynamic(key, parameters);
		return null;
	}

	private static void sortBases(List<String> list, Map<String, String[]> allBaseSuperiors, Map<String, String[]> allBaseInferiors, String methodName)
	{
		new ModelPlayerBaseSorter(list, allBaseSuperiors, allBaseInferiors, methodName).Sort();
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

	private ModelPlayerAPI(IModelPlayerAPI modelPlayer, float paramFloat, String type)
	{
		this.modelPlayer = modelPlayer;
		this.paramFloat = paramFloat;
		this.type = type;
	}

	private void load()
	{
		Iterator<String> iterator = allBaseConstructors.keySet().iterator();
		while(iterator.hasNext())
		{
			String id = iterator.next();
			ModelPlayerBase toAttach = createModelPlayerBase(id);
			toAttach.beforeBaseAttach(false);
			allBaseObjects.put(id, toAttach);
			baseObjectsToId.put(toAttach, id);
		}

		beforeLocalConstructingHooks = create(beforeLocalConstructingHookTypes);
		afterLocalConstructingHooks = create(afterLocalConstructingHookTypes);

		updateModelPlayerBases();

		iterator = allBaseObjects.keySet().iterator();
		while(iterator.hasNext())
			allBaseObjects.get(iterator.next()).afterBaseAttach(false);
	}

	private ModelPlayerBase createModelPlayerBase(String id)
	{
		Constructor<?> contructor = allBaseConstructors.get(id);

		ModelPlayerBase base;
		try
		{
			if(contructor.getParameterTypes().length == 1)
				base = (ModelPlayerBase)contructor.newInstance(this);
			else
				base = (ModelPlayerBase)contructor.newInstance(this, id);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Exception while creating a ModelPlayerBase of type '" + contructor.getDeclaringClass() + "'", e);
		}
		return base;
	}

	private void updateModelPlayerBases()
	{
		beforeGetRandomModelBoxHooks = create(beforeGetRandomModelBoxHookTypes);
		overrideGetRandomModelBoxHooks = create(overrideGetRandomModelBoxHookTypes);
		afterGetRandomModelBoxHooks = create(afterGetRandomModelBoxHookTypes);
		isGetRandomModelBoxModded =
			beforeGetRandomModelBoxHooks != null ||
			overrideGetRandomModelBoxHooks != null ||
			afterGetRandomModelBoxHooks != null;

		beforeGetTextureOffsetHooks = create(beforeGetTextureOffsetHookTypes);
		overrideGetTextureOffsetHooks = create(overrideGetTextureOffsetHookTypes);
		afterGetTextureOffsetHooks = create(afterGetTextureOffsetHookTypes);
		isGetTextureOffsetModded =
			beforeGetTextureOffsetHooks != null ||
			overrideGetTextureOffsetHooks != null ||
			afterGetTextureOffsetHooks != null;

		beforeRenderHooks = create(beforeRenderHookTypes);
		overrideRenderHooks = create(overrideRenderHookTypes);
		afterRenderHooks = create(afterRenderHookTypes);
		isRenderModded =
			beforeRenderHooks != null ||
			overrideRenderHooks != null ||
			afterRenderHooks != null;

		beforeRenderCloakHooks = create(beforeRenderCloakHookTypes);
		overrideRenderCloakHooks = create(overrideRenderCloakHookTypes);
		afterRenderCloakHooks = create(afterRenderCloakHookTypes);
		isRenderCloakModded =
			beforeRenderCloakHooks != null ||
			overrideRenderCloakHooks != null ||
			afterRenderCloakHooks != null;

		beforeRenderEarsHooks = create(beforeRenderEarsHookTypes);
		overrideRenderEarsHooks = create(overrideRenderEarsHookTypes);
		afterRenderEarsHooks = create(afterRenderEarsHookTypes);
		isRenderEarsModded =
			beforeRenderEarsHooks != null ||
			overrideRenderEarsHooks != null ||
			afterRenderEarsHooks != null;

		beforeSetLivingAnimationsHooks = create(beforeSetLivingAnimationsHookTypes);
		overrideSetLivingAnimationsHooks = create(overrideSetLivingAnimationsHookTypes);
		afterSetLivingAnimationsHooks = create(afterSetLivingAnimationsHookTypes);
		isSetLivingAnimationsModded =
			beforeSetLivingAnimationsHooks != null ||
			overrideSetLivingAnimationsHooks != null ||
			afterSetLivingAnimationsHooks != null;

		beforeSetRotationAnglesHooks = create(beforeSetRotationAnglesHookTypes);
		overrideSetRotationAnglesHooks = create(overrideSetRotationAnglesHookTypes);
		afterSetRotationAnglesHooks = create(afterSetRotationAnglesHookTypes);
		isSetRotationAnglesModded =
			beforeSetRotationAnglesHooks != null ||
			overrideSetRotationAnglesHooks != null ||
			afterSetRotationAnglesHooks != null;

		beforeSetTextureOffsetHooks = create(beforeSetTextureOffsetHookTypes);
		overrideSetTextureOffsetHooks = create(overrideSetTextureOffsetHookTypes);
		afterSetTextureOffsetHooks = create(afterSetTextureOffsetHookTypes);
		isSetTextureOffsetModded =
			beforeSetTextureOffsetHooks != null ||
			overrideSetTextureOffsetHooks != null ||
			afterSetTextureOffsetHooks != null;

	}

	private void attachModelPlayerBase(String id)
	{
        ModelPlayerBase toAttach = createModelPlayerBase(id);
		toAttach.beforeBaseAttach(true);
		allBaseObjects.put(id, toAttach);
		updateModelPlayerBases();
		toAttach.afterBaseAttach(true);
	}

	private void detachModelPlayerBase(String id)
	{
		ModelPlayerBase toDetach = allBaseObjects.get(id);
		toDetach.beforeBaseDetach(true);
		allBaseObjects.remove(id);
		updateModelPlayerBases();
		toDetach.afterBaseDetach(true);
	}

	private ModelPlayerBase[] create(List<String> types)
	{
		if(types.isEmpty())
			return null;

		ModelPlayerBase[] result = new ModelPlayerBase[types.size()];
		for(int i = 0; i < result.length; i++)
			result[i] = getModelPlayerBase(types.get(i));
		return result;
	}

	private void beforeLocalConstructing(float paramFloat)
	{
		if(beforeLocalConstructingHooks != null)
			for(int i = beforeLocalConstructingHooks.length - 1; i >= 0 ; i--)
				beforeLocalConstructingHooks[i].beforeLocalConstructing(paramFloat);
		beforeLocalConstructingHooks = null;
	}

	private void afterLocalConstructing(float paramFloat)
	{
		if(afterLocalConstructingHooks != null)
			for(int i = 0; i < afterLocalConstructingHooks.length; i++)
				afterLocalConstructingHooks[i].afterLocalConstructing(paramFloat);
		afterLocalConstructingHooks = null;
	}

	public ModelPlayerBase getModelPlayerBase(String id)
	{
		return allBaseObjects.get(id);
	}

	public Set<String> getModelPlayerBaseIds()
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

	public Object dynamicOverwritten(String key, Object[] parameters, ModelPlayerBase overwriter)
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

		return execute(getModelPlayerBase(id), method, parameters);
	}

	private void executeAll(String key, Object[] parameters, Map<String, List<String>> dynamicHookTypes, Map<Class<?>, Map<String, Method>> dynamicHookMethods, boolean reverse)
	{
		List<String> beforeIds = dynamicHookTypes.get(key);
		if(beforeIds == null)
			return;

		for(int i= reverse ? beforeIds.size() - 1 : 0; reverse ? i >= 0 : i < beforeIds.size(); i = i + (reverse ? -1 : 1))
		{
			String id = beforeIds.get(i);
			ModelPlayerBase base = getModelPlayerBase(id);
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

	private Object execute(ModelPlayerBase base, Method method, Object[] parameters)
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

	public static net.minecraft.client.model.ModelRenderer getRandomModelBox(IModelPlayerAPI target, java.util.Random paramRandom)
	{
		net.minecraft.client.model.ModelRenderer _result;
		ModelPlayerAPI modelPlayerAPI = target.getModelPlayerAPI();
		if(modelPlayerAPI != null && modelPlayerAPI.isGetRandomModelBoxModded)
			_result = modelPlayerAPI.getRandomModelBox(paramRandom);
		else
			_result = target.localGetRandomModelBox(paramRandom);
		return _result;
	}

	private net.minecraft.client.model.ModelRenderer getRandomModelBox(java.util.Random paramRandom)
	{
		if(beforeGetRandomModelBoxHooks != null)
			for(int i = beforeGetRandomModelBoxHooks.length - 1; i >= 0 ; i--)
				beforeGetRandomModelBoxHooks[i].beforeGetRandomModelBox(paramRandom);

		net.minecraft.client.model.ModelRenderer _result;
		if(overrideGetRandomModelBoxHooks != null)
			_result = overrideGetRandomModelBoxHooks[overrideGetRandomModelBoxHooks.length - 1].getRandomModelBox(paramRandom);
		else
			_result = modelPlayer.localGetRandomModelBox(paramRandom);

		if(afterGetRandomModelBoxHooks != null)
			for(int i = 0; i < afterGetRandomModelBoxHooks.length; i++)
				afterGetRandomModelBoxHooks[i].afterGetRandomModelBox(paramRandom);

		return _result;
	}

	protected ModelPlayerBase GetOverwrittenGetRandomModelBox(ModelPlayerBase overWriter)
	{
		for(int i = 0; i < overrideGetRandomModelBoxHooks.length; i++)
			if(overrideGetRandomModelBoxHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetRandomModelBoxHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetRandomModelBoxHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetRandomModelBoxHookTypes = new LinkedList<String>();
	private final static List<String> afterGetRandomModelBoxHookTypes = new LinkedList<String>();

	private ModelPlayerBase[] beforeGetRandomModelBoxHooks;
	private ModelPlayerBase[] overrideGetRandomModelBoxHooks;
	private ModelPlayerBase[] afterGetRandomModelBoxHooks;

	public boolean isGetRandomModelBoxModded;

	private static final Map<String, String[]> allBaseBeforeGetRandomModelBoxSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetRandomModelBoxInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetRandomModelBoxSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetRandomModelBoxInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetRandomModelBoxSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetRandomModelBoxInferiors = new Hashtable<String, String[]>(0);

	public static net.minecraft.client.model.TextureOffset getTextureOffset(IModelPlayerAPI target, String paramString)
	{
		net.minecraft.client.model.TextureOffset _result;
		ModelPlayerAPI modelPlayerAPI = target.getModelPlayerAPI();
		if(modelPlayerAPI != null && modelPlayerAPI.isGetTextureOffsetModded)
			_result = modelPlayerAPI.getTextureOffset(paramString);
		else
			_result = target.localGetTextureOffset(paramString);
		return _result;
	}

	private net.minecraft.client.model.TextureOffset getTextureOffset(String paramString)
	{
		if(beforeGetTextureOffsetHooks != null)
			for(int i = beforeGetTextureOffsetHooks.length - 1; i >= 0 ; i--)
				beforeGetTextureOffsetHooks[i].beforeGetTextureOffset(paramString);

		net.minecraft.client.model.TextureOffset _result;
		if(overrideGetTextureOffsetHooks != null)
			_result = overrideGetTextureOffsetHooks[overrideGetTextureOffsetHooks.length - 1].getTextureOffset(paramString);
		else
			_result = modelPlayer.localGetTextureOffset(paramString);

		if(afterGetTextureOffsetHooks != null)
			for(int i = 0; i < afterGetTextureOffsetHooks.length; i++)
				afterGetTextureOffsetHooks[i].afterGetTextureOffset(paramString);

		return _result;
	}

	protected ModelPlayerBase GetOverwrittenGetTextureOffset(ModelPlayerBase overWriter)
	{
		for(int i = 0; i < overrideGetTextureOffsetHooks.length; i++)
			if(overrideGetTextureOffsetHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideGetTextureOffsetHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeGetTextureOffsetHookTypes = new LinkedList<String>();
	private final static List<String> overrideGetTextureOffsetHookTypes = new LinkedList<String>();
	private final static List<String> afterGetTextureOffsetHookTypes = new LinkedList<String>();

	private ModelPlayerBase[] beforeGetTextureOffsetHooks;
	private ModelPlayerBase[] overrideGetTextureOffsetHooks;
	private ModelPlayerBase[] afterGetTextureOffsetHooks;

	public boolean isGetTextureOffsetModded;

	private static final Map<String, String[]> allBaseBeforeGetTextureOffsetSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeGetTextureOffsetInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetTextureOffsetSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideGetTextureOffsetInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetTextureOffsetSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterGetTextureOffsetInferiors = new Hashtable<String, String[]>(0);

	public static void render(IModelPlayerAPI target, net.minecraft.entity.Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
	{
		ModelPlayerAPI modelPlayerAPI = target.getModelPlayerAPI();
		if(modelPlayerAPI != null && modelPlayerAPI.isRenderModded)
			modelPlayerAPI.render(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
		else
			target.localRender(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
	}

	private void render(net.minecraft.entity.Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
	{
		if(beforeRenderHooks != null)
			for(int i = beforeRenderHooks.length - 1; i >= 0 ; i--)
				beforeRenderHooks[i].beforeRender(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);

		if(overrideRenderHooks != null)
			overrideRenderHooks[overrideRenderHooks.length - 1].render(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
		else
			modelPlayer.localRender(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);

		if(afterRenderHooks != null)
			for(int i = 0; i < afterRenderHooks.length; i++)
				afterRenderHooks[i].afterRender(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);

	}

	protected ModelPlayerBase GetOverwrittenRender(ModelPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderHooks.length; i++)
			if(overrideRenderHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderHookTypes = new LinkedList<String>();

	private ModelPlayerBase[] beforeRenderHooks;
	private ModelPlayerBase[] overrideRenderHooks;
	private ModelPlayerBase[] afterRenderHooks;

	public boolean isRenderModded;

	private static final Map<String, String[]> allBaseBeforeRenderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderInferiors = new Hashtable<String, String[]>(0);

	public static void renderCloak(IModelPlayerAPI target, float paramFloat)
	{
		ModelPlayerAPI modelPlayerAPI = target.getModelPlayerAPI();
		if(modelPlayerAPI != null && modelPlayerAPI.isRenderCloakModded)
			modelPlayerAPI.renderCloak(paramFloat);
		else
			target.localRenderCloak(paramFloat);
	}

	private void renderCloak(float paramFloat)
	{
		if(beforeRenderCloakHooks != null)
			for(int i = beforeRenderCloakHooks.length - 1; i >= 0 ; i--)
				beforeRenderCloakHooks[i].beforeRenderCloak(paramFloat);

		if(overrideRenderCloakHooks != null)
			overrideRenderCloakHooks[overrideRenderCloakHooks.length - 1].renderCloak(paramFloat);
		else
			modelPlayer.localRenderCloak(paramFloat);

		if(afterRenderCloakHooks != null)
			for(int i = 0; i < afterRenderCloakHooks.length; i++)
				afterRenderCloakHooks[i].afterRenderCloak(paramFloat);

	}

	protected ModelPlayerBase GetOverwrittenRenderCloak(ModelPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderCloakHooks.length; i++)
			if(overrideRenderCloakHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderCloakHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderCloakHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderCloakHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderCloakHookTypes = new LinkedList<String>();

	private ModelPlayerBase[] beforeRenderCloakHooks;
	private ModelPlayerBase[] overrideRenderCloakHooks;
	private ModelPlayerBase[] afterRenderCloakHooks;

	public boolean isRenderCloakModded;

	private static final Map<String, String[]> allBaseBeforeRenderCloakSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderCloakInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderCloakSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderCloakInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderCloakSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderCloakInferiors = new Hashtable<String, String[]>(0);

	public static void renderEars(IModelPlayerAPI target, float paramFloat)
	{
		ModelPlayerAPI modelPlayerAPI = target.getModelPlayerAPI();
		if(modelPlayerAPI != null && modelPlayerAPI.isRenderEarsModded)
			modelPlayerAPI.renderEars(paramFloat);
		else
			target.localRenderEars(paramFloat);
	}

	private void renderEars(float paramFloat)
	{
		if(beforeRenderEarsHooks != null)
			for(int i = beforeRenderEarsHooks.length - 1; i >= 0 ; i--)
				beforeRenderEarsHooks[i].beforeRenderEars(paramFloat);

		if(overrideRenderEarsHooks != null)
			overrideRenderEarsHooks[overrideRenderEarsHooks.length - 1].renderEars(paramFloat);
		else
			modelPlayer.localRenderEars(paramFloat);

		if(afterRenderEarsHooks != null)
			for(int i = 0; i < afterRenderEarsHooks.length; i++)
				afterRenderEarsHooks[i].afterRenderEars(paramFloat);

	}

	protected ModelPlayerBase GetOverwrittenRenderEars(ModelPlayerBase overWriter)
	{
		for(int i = 0; i < overrideRenderEarsHooks.length; i++)
			if(overrideRenderEarsHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideRenderEarsHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeRenderEarsHookTypes = new LinkedList<String>();
	private final static List<String> overrideRenderEarsHookTypes = new LinkedList<String>();
	private final static List<String> afterRenderEarsHookTypes = new LinkedList<String>();

	private ModelPlayerBase[] beforeRenderEarsHooks;
	private ModelPlayerBase[] overrideRenderEarsHooks;
	private ModelPlayerBase[] afterRenderEarsHooks;

	public boolean isRenderEarsModded;

	private static final Map<String, String[]> allBaseBeforeRenderEarsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeRenderEarsInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderEarsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideRenderEarsInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderEarsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterRenderEarsInferiors = new Hashtable<String, String[]>(0);

	public static void setLivingAnimations(IModelPlayerAPI target, net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3)
	{
		ModelPlayerAPI modelPlayerAPI = target.getModelPlayerAPI();
		if(modelPlayerAPI != null && modelPlayerAPI.isSetLivingAnimationsModded)
			modelPlayerAPI.setLivingAnimations(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3);
		else
			target.localSetLivingAnimations(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3);
	}

	private void setLivingAnimations(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3)
	{
		if(beforeSetLivingAnimationsHooks != null)
			for(int i = beforeSetLivingAnimationsHooks.length - 1; i >= 0 ; i--)
				beforeSetLivingAnimationsHooks[i].beforeSetLivingAnimations(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3);

		if(overrideSetLivingAnimationsHooks != null)
			overrideSetLivingAnimationsHooks[overrideSetLivingAnimationsHooks.length - 1].setLivingAnimations(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3);
		else
			modelPlayer.localSetLivingAnimations(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3);

		if(afterSetLivingAnimationsHooks != null)
			for(int i = 0; i < afterSetLivingAnimationsHooks.length; i++)
				afterSetLivingAnimationsHooks[i].afterSetLivingAnimations(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3);

	}

	protected ModelPlayerBase GetOverwrittenSetLivingAnimations(ModelPlayerBase overWriter)
	{
		for(int i = 0; i < overrideSetLivingAnimationsHooks.length; i++)
			if(overrideSetLivingAnimationsHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetLivingAnimationsHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetLivingAnimationsHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetLivingAnimationsHookTypes = new LinkedList<String>();
	private final static List<String> afterSetLivingAnimationsHookTypes = new LinkedList<String>();

	private ModelPlayerBase[] beforeSetLivingAnimationsHooks;
	private ModelPlayerBase[] overrideSetLivingAnimationsHooks;
	private ModelPlayerBase[] afterSetLivingAnimationsHooks;

	public boolean isSetLivingAnimationsModded;

	private static final Map<String, String[]> allBaseBeforeSetLivingAnimationsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetLivingAnimationsInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetLivingAnimationsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetLivingAnimationsInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetLivingAnimationsSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetLivingAnimationsInferiors = new Hashtable<String, String[]>(0);

	public static void setRotationAngles(IModelPlayerAPI target, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, net.minecraft.entity.Entity paramEntity)
	{
		ModelPlayerAPI modelPlayerAPI = target.getModelPlayerAPI();
		if(modelPlayerAPI != null && modelPlayerAPI.isSetRotationAnglesModded)
			modelPlayerAPI.setRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramEntity);
		else
			target.localSetRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramEntity);
	}

	private void setRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, net.minecraft.entity.Entity paramEntity)
	{
		if(beforeSetRotationAnglesHooks != null)
			for(int i = beforeSetRotationAnglesHooks.length - 1; i >= 0 ; i--)
				beforeSetRotationAnglesHooks[i].beforeSetRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramEntity);

		if(overrideSetRotationAnglesHooks != null)
			overrideSetRotationAnglesHooks[overrideSetRotationAnglesHooks.length - 1].setRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramEntity);
		else
			modelPlayer.localSetRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramEntity);

		if(afterSetRotationAnglesHooks != null)
			for(int i = 0; i < afterSetRotationAnglesHooks.length; i++)
				afterSetRotationAnglesHooks[i].afterSetRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramEntity);

	}

	protected ModelPlayerBase GetOverwrittenSetRotationAngles(ModelPlayerBase overWriter)
	{
		for(int i = 0; i < overrideSetRotationAnglesHooks.length; i++)
			if(overrideSetRotationAnglesHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetRotationAnglesHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetRotationAnglesHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetRotationAnglesHookTypes = new LinkedList<String>();
	private final static List<String> afterSetRotationAnglesHookTypes = new LinkedList<String>();

	private ModelPlayerBase[] beforeSetRotationAnglesHooks;
	private ModelPlayerBase[] overrideSetRotationAnglesHooks;
	private ModelPlayerBase[] afterSetRotationAnglesHooks;

	public boolean isSetRotationAnglesModded;

	private static final Map<String, String[]> allBaseBeforeSetRotationAnglesSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetRotationAnglesInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetRotationAnglesSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetRotationAnglesInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetRotationAnglesSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetRotationAnglesInferiors = new Hashtable<String, String[]>(0);

	public static void setTextureOffset(IModelPlayerAPI target, String paramString, int paramInt1, int paramInt2)
	{
		ModelPlayerAPI modelPlayerAPI = target.getModelPlayerAPI();
		if(modelPlayerAPI != null && modelPlayerAPI.isSetTextureOffsetModded)
			modelPlayerAPI.setTextureOffset(paramString, paramInt1, paramInt2);
		else
			target.localSetTextureOffset(paramString, paramInt1, paramInt2);
	}

	private void setTextureOffset(String paramString, int paramInt1, int paramInt2)
	{
		if(beforeSetTextureOffsetHooks != null)
			for(int i = beforeSetTextureOffsetHooks.length - 1; i >= 0 ; i--)
				beforeSetTextureOffsetHooks[i].beforeSetTextureOffset(paramString, paramInt1, paramInt2);

		if(overrideSetTextureOffsetHooks != null)
			overrideSetTextureOffsetHooks[overrideSetTextureOffsetHooks.length - 1].setTextureOffset(paramString, paramInt1, paramInt2);
		else
			modelPlayer.localSetTextureOffset(paramString, paramInt1, paramInt2);

		if(afterSetTextureOffsetHooks != null)
			for(int i = 0; i < afterSetTextureOffsetHooks.length; i++)
				afterSetTextureOffsetHooks[i].afterSetTextureOffset(paramString, paramInt1, paramInt2);

	}

	protected ModelPlayerBase GetOverwrittenSetTextureOffset(ModelPlayerBase overWriter)
	{
		for(int i = 0; i < overrideSetTextureOffsetHooks.length; i++)
			if(overrideSetTextureOffsetHooks[i] == overWriter)
				if(i == 0)
					return null;
				else
					return overrideSetTextureOffsetHooks[i - 1];

		return overWriter;
	}

	private final static List<String> beforeSetTextureOffsetHookTypes = new LinkedList<String>();
	private final static List<String> overrideSetTextureOffsetHookTypes = new LinkedList<String>();
	private final static List<String> afterSetTextureOffsetHookTypes = new LinkedList<String>();

	private ModelPlayerBase[] beforeSetTextureOffsetHooks;
	private ModelPlayerBase[] overrideSetTextureOffsetHooks;
	private ModelPlayerBase[] afterSetTextureOffsetHooks;

	public boolean isSetTextureOffsetModded;

	private static final Map<String, String[]> allBaseBeforeSetTextureOffsetSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseBeforeSetTextureOffsetInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetTextureOffsetSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseOverrideSetTextureOffsetInferiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetTextureOffsetSuperiors = new Hashtable<String, String[]>(0);
	private static final Map<String, String[]> allBaseAfterSetTextureOffsetInferiors = new Hashtable<String, String[]>(0);

	
	protected final IModelPlayerAPI modelPlayer;
	private final float paramFloat;
	private final String type;

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

	private ModelPlayerBase[] beforeLocalConstructingHooks;
	private ModelPlayerBase[] afterLocalConstructingHooks;

	private final Map<ModelPlayerBase, String> baseObjectsToId = new Hashtable<ModelPlayerBase, String>();
	private final Map<String, ModelPlayerBase> allBaseObjects = new Hashtable<String, ModelPlayerBase>();
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
