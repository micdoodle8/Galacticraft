/*
 * (C) 2014-2017 Team CoFH / CoFH / Cult of the Full Hub
 * http://www.teamcofh.com
 */
package cofh.redstoneflux.internal;

import cofh.redstoneflux.RedstoneFlux;
import com.google.common.collect.ImmutableSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class OldAPIChecker {

	private static final Logger LOGGER = LogManager.getLogger(RedstoneFlux.MOD_NAME);
	private static final Set<String> oldAPIClasses;

	static {
		ImmutableSet.Builder<String> builder = ImmutableSet.builder();

		builder.add("cofh.api.energy.EnergyStorage");
		builder.add("cofh.api.energy.IEnergyConnection");
		builder.add("cofh.api.energy.IEnergyContainerItem");
		builder.add("cofh.api.energy.IEnergyHandler");
		builder.add("cofh.api.energy.IEnergyProvider");
		builder.add("cofh.api.energy.IEnergyReceiver");
		builder.add("cofh.api.energy.IEnergyStorage");
		builder.add("cofh.api.energy.IEnergyTransport");
		builder.add("cofh.api.energy.ItemEnergyContainer");
		builder.add("cofh.api.energy.TileEnergyHandler");

		oldAPIClasses = builder.build();
	}

	public static void check() {

		boolean ignoreOldAPI = Boolean.parseBoolean(System.getProperty("cofh.rf.ignoreOldAPI", "false"));
		boolean crashOnOldAPI = Boolean.parseBoolean(System.getProperty("cofh.rf.crashOnOldAPI", "true"));

		if (!ignoreOldAPI) {
			Set<String> repackedAPIClasses = new HashSet<>();
			for (String clazz : oldAPIClasses) {
				if (classExists(clazz)) {
					repackedAPIClasses.add(clazz);
				}
			}
			if (!repackedAPIClasses.isEmpty()) {
				StringBuilder builder = new StringBuilder();
				builder.append("\nRedstoneFlux has detected that the following OLD API classes from CoFHLib are being repacked!\n");
				for (String clazz : repackedAPIClasses) {
					builder.append("    ").append(clazz);
					builder.append(", loaded from: ").append(getLoadPath(clazz));
					builder.append("\n");
				}
				builder.append("\n");
				if (crashOnOldAPI) {
					builder.append("To prevent this crash, add \"-Dcofh.rf.crashOnOldAPI=false\" to your command line arguments.");
					builder.append("\n");
				}
				if (crashOnOldAPI) {
					throw new RuntimeException(builder.toString());
				} else {
					LOGGER.fatal(builder.toString());
				}
			}
		}
	}

	private static boolean classExists(String className) {

		try {
			Class.forName(className);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String getLoadPath(String clazz) {

		String loadPath = "<unknown>";
		try {
			Class c = Class.forName(clazz);
			ClassLoader loader = c.getClassLoader();
			if (loader == null) {
				// Try the bootstrap classloader - obtained from the ultimate parent of the System Class Loader.
				loader = ClassLoader.getSystemClassLoader();
				while (loader != null && loader.getParent() != null) {
					loader = loader.getParent();
				}
			}
			if (loader != null) {
				String name = c.getCanonicalName();
				URL resource = loader.getResource(name.replace(".", "/") + ".class");
				if (resource != null) {
					loadPath = resource.toString();
					int lastBang = loadPath.lastIndexOf("!");
					loadPath = loadPath.substring(0, lastBang);
					int lastSlash = loadPath.lastIndexOf("/");
					loadPath = loadPath.substring(lastSlash + 1);
				}
			}
		} catch (Throwable t) {
			// pokemon!
		}
		return loadPath;
	}

}
