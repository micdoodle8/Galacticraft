/*
 * (C) 2014-2017 Team CoFH / CoFH / Cult of the Full Hub
 * http://www.teamcofh.com
 */
package cofh.redstoneflux;

import cofh.redstoneflux.internal.OldAPIChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod (modid = RedstoneFlux.MOD_ID, name = RedstoneFlux.MOD_NAME, version = RedstoneFlux.VERSION, certificateFingerprint = "d4f1503fbacd9b9fb767720420c5395104239ec9")
public class RedstoneFlux {

	public static final String MOD_ID = "redstoneflux";
	public static final String MOD_NAME = "Redstone Flux";

	public static final String VERSION = "2.0.1";
	public static final String VERSION_MAX = "2.1.0";
	public static final String VERSION_GROUP = "required-after:" + MOD_ID + "@[" + VERSION + "," + VERSION_MAX + ");";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		OldAPIChecker.check();
	}

}
