package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GCSounds
{
    public static SoundEvent bossDeath;
    public static SoundEvent bossLaugh;
    public static SoundEvent bossOoh;
    public static SoundEvent bossOuch;
    public static SoundEvent slimeDeath;
    public static SoundEvent astroMiner;
    public static SoundEvent shuttle;
    public static SoundEvent parachute;
    public static SoundEvent openAirLock;
    public static SoundEvent closeAirLock;
    public static SoundEvent singleDrip;
    public static SoundEvent scaryScape;
    public static SoundEvent deconstructor;

    public static SoundEvent music;

    public static void registerSounds()
    {
        bossDeath = registerSound("entity.bossdeath");
        bossLaugh = registerSound("entity.bosslaugh");
        bossOoh = registerSound("entity.ooh");
        bossOuch = registerSound("entity.ouch");
        slimeDeath = registerSound("entity.slime_death");
        astroMiner = registerSound("entity.astrominer");
        shuttle = registerSound("shuttle.shuttle");
        parachute = registerSound("player.parachute");
        openAirLock = registerSound("player.openairlock");
        closeAirLock = registerSound("player.closeairlock");
        singleDrip = registerSound("ambience.singledrip");
        scaryScape = registerSound("ambience.scaryscape");
        music = registerSound("galacticraft.musicSpace");
        deconstructor = registerSound("block.deconstructor");
    }

    private static SoundEvent registerSound(String soundName)
    {
        ResourceLocation soundID = new ResourceLocation(Constants.ASSET_PREFIX, soundName);
        return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
    }
}
