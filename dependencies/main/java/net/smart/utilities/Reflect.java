package net.smart.utilities;

import java.lang.reflect.*;

public class Reflect
{
	public static Object NewInstance(Class<?> base, Name name)
	{
		try
		{
			return LoadClass(base, name, true).getConstructor().newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException(name.deobfuscated, e);
		}
	}

	public static boolean CheckClasses(Class<?> base, Name... names)
	{
		for(int i=0; i<names.length; i++)
			if(LoadClass(base, names[i], false) == null)
				return false;
		return true;
	}

	public static Class<?> LoadClass(Class<?> base, Name name, boolean throwException)
	{
		ClassLoader loader = base.getClassLoader();

		if(name.obfuscated != null)
			try
			{
				return loader.loadClass(name.obfuscated);
			}
			catch (ClassNotFoundException cnfe)
			{
			}

		try
		{
			return loader.loadClass(name.deobfuscated);
		}
		catch (ClassNotFoundException cnfe)
		{
			if(throwException)
				throw new RuntimeException(cnfe);
			return null;
		}
	}

	public static void SetField(Field field, Object object, Object value)
	{
		try
		{
			field.set(object, value);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Object GetField(Field field, Object object)
	{
		try
		{
			return field.get(object);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void SetField(Class<?> theClass, Object object, Name name, Object value)
	{
		try
		{
			GetField(theClass, name).set(object, value);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Object GetField(Class<?> theClass, Object object, Name name)
	{
		try
		{
			return GetField(theClass, name).get(object);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Field GetField(Class<?> theClass, Name name)
	{
		return GetField(theClass, name, true);
	}

	public static Field GetField(Class<?> theClass, Name name, boolean throwException)
	{
		if(theClass == null && !throwException)
			return null;

		Field field = null;
		try
		{
			field = GetRawField(theClass, name);
			field.setAccessible(true);
		}
		catch (NoSuchFieldException oe)
		{
			if(throwException)
				throw new RuntimeException(GetFieldMessage(theClass, name), oe);
		}
		return field;
	}

	private static String GetFieldMessage(Class<?> theClass, Name name)
	{
		Field[] fields = theClass.getDeclaredFields();
		StringBuffer message = GetMessage(theClass, name, "field");
		for(int i = 0; i < fields.length; i++)
			AppendField(message, fields[i]);
		return message.toString();
	}

	private static Field GetRawField(Class<?> theClass, Name name) throws NoSuchFieldException
	{
		if(name.obfuscated != null)
			try
			{
				return theClass.getDeclaredField(name.obfuscated);
			}
			catch (NoSuchFieldException oe)
			{
			}

		if(name.forgefuscated != null)
			try
			{
				return theClass.getDeclaredField(name.forgefuscated);
			}
			catch (NoSuchFieldException oe)
			{
			}

		return theClass.getDeclaredField(name.deobfuscated);
	}

	public static Method GetMethod(Class<?> theClass, Name name, Class<?>... paramArrayOfClass)
	{
		return GetMethod(theClass, name, true, paramArrayOfClass);
	}

	public static Method GetMethod(Class<?> theClass, Name name, boolean throwException, Class<?>... paramArrayOfClass)
	{
		if(theClass == null && !throwException)
			return null;

		Method method = null;
		try
		{
			method = GetRawMethod(theClass, name, paramArrayOfClass);
			method.setAccessible(true);
		}
		catch (NoSuchMethodException oe)
		{
			if(throwException)
				throw new RuntimeException(GetMethodMessage(theClass, name), oe);
		}
		return method;
	}

	private static String GetMethodMessage(Class<?> theClass, Name name)
	{
		Method[] methods = theClass.getDeclaredMethods();
		StringBuffer message = GetMessage(theClass, name, "method");
		for(int i = 0; i < methods.length; i++)
			AppendMethod(message, methods[i]);
		return message.toString();
	}

	private static Method GetRawMethod(Class<?> theClass, Name name, Class<?>... paramArrayOfClass) throws NoSuchMethodException
	{
		if(name.obfuscated != null)
			try
			{
				return theClass.getDeclaredMethod(name.obfuscated, paramArrayOfClass);
			}
			catch (NoSuchMethodException oe)
			{
			}

		if(name.forgefuscated != null)
			try
			{
				return theClass.getDeclaredMethod(name.forgefuscated, paramArrayOfClass);
			}
			catch (NoSuchMethodException oe)
			{
			}

		return theClass.getDeclaredMethod(name.deobfuscated, paramArrayOfClass);
	}

	public static Object Invoke(Method method, Object paramObject, Object... paramArrayOfObject)
	{
		try
		{
			return method.invoke(paramObject, paramArrayOfObject);
		}
		catch (Exception e)
		{
			throw new RuntimeException(method.getName(), e);
		}
	}

	private static StringBuffer GetMessage(Class<?> theClass, Name name, String elementName)
	{
		StringBuffer message = new StringBuffer()
			.append("Can not find ")
			.append(elementName)
			.append(" \"")
			.append(name.deobfuscated)
			.append("\"");

		if(name.obfuscated != null)
			message
			.append(" (ofuscated \"")
			.append(name.obfuscated)
			.append("\")");

		message
			.append(" in class \"")
			.append(theClass.getName())
			.append("\".\nExisting ")
			.append(elementName)
			.append("s are:");

	   return message;
	}

	private static void AppendMethod(StringBuffer message, Method method)
	{
		message
			.append("\n\t\t")
			.append(method.getReturnType().getName())
			.append(" ")
			.append(method.getName())
		 	.append("(");

		Class<?>[] types = method.getParameterTypes();
		for(int i=0; i<types.length; i++)
		{
			if(i != 0)
				message.append(", ");
			message.append(types[i].getName());
		}

		message.append(")");
	}

	private static void AppendField(StringBuffer message, Field field)
	{
		message
			.append("\n\t\t")
			.append(field.getType().getName())
		 	.append(" ")
		 	.append(field.getName());
	}
}
