package net.smart.utilities;

public class Name
{
	public final String obfuscated;
	public final String forgefuscated;
	public final String deobfuscated;

	public Name(String name)
	{
		this(name, null);
	}

	public Name(String deobfuscatedName, String obfuscatedName)
	{
		this(deobfuscatedName, null, obfuscatedName);
	}

	public Name(String deobfuscatedName, String forgefuscatedName, String obfuscatedName)
	{
		deobfuscated = deobfuscatedName;
		forgefuscated = forgefuscatedName;
		obfuscated = obfuscatedName;
	}
}