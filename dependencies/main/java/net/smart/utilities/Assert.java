package net.smart.utilities;

import java.io.*;
import org.apache.logging.log4j.*;

import cpw.mods.fml.common.*;

public class Assert
{
	public final static String messageBorder = "========================================";
	public final static String messageSeparator = "----------------------------------------";

	public final static boolean hasClientPlayerAPI = hasAPI("Client");
	public final static boolean hasServerPlayerAPI = hasAPI("Server");
	public final static boolean hasRenderPlayerAPI = hasAPI("Render");

	public static void clientPlayerAPI(String modName)
	{
		if(!hasClientPlayerAPI)
			throw new RuntimeException(error(getPlayerAPIMessage(modName, true)));
	}

	public static void serverPlayerAPI(String modName)
	{
		if(!hasServerPlayerAPI)
			throw new RuntimeException(error(getPlayerAPIMessage(modName, false)));
	}

	private static String[] getPlayerAPIMessage(String modName, boolean client)
	{
		return getMessage(
			modName + " could not find the required API \"" + (client ? "Client" : "Server") + " Player\"!",
			"Download Player API core from:",
			"\thttp://www.minecraftforum.net/topic/738498-/",
			"and install it on your system to fix this specific problem.");
	}

	private static String[] getMessage(String header, String... lines)
	{
		String[] message = new String[lines.length + 4];
		int i = 0;
		message[i++] = messageBorder;
		message[i++] = header;
		message[i++] = messageSeparator;
		for(int n=0; n<lines.length; n++)
			message[i++] = lines[n];
		message[i] = messageBorder;
		return message;
	}

	public static String error(String... lines)
	{
		return log(Level.ERROR, System.err, lines);
	}

	private static String log(Level level, PrintStream stream, String... lines)
	{
		String message = "\n";
		for(int i=0; i<lines.length; i++)
		{
			String line = lines[i];
			stream.println(line);
			message += "\n\t" + line;
		}
		message += "\n";

		FMLLog.log(level, message);

		return message;
	}

	private static boolean hasAPI(String prefix)
	{
		String fullPrefix = "api.player." + prefix.substring(0,1).toLowerCase() + prefix.substring(1) + "." + prefix + "Player";
		return Reflect.CheckClasses(Assert.class, new Name(fullPrefix + "API"), new Name(fullPrefix + "BaseSorter"));
	}
}