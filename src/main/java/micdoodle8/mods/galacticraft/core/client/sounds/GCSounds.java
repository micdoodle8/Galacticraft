package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

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
    public static SoundEvent advanced_compressor;
    public static SoundEvent laserCharge;
    public static SoundEvent laserShoot;

    public static SoundEvent music;

    public static void registerSounds(IForgeRegistry<SoundEvent> registry)
    {
        bossDeath = registerSound("entity.bossdeath", registry);
        bossLaugh = registerSound("entity.bosslaugh", registry);
        bossOoh = registerSound("entity.ooh", registry);
        bossOuch = registerSound("entity.ouch", registry);
        slimeDeath = registerSound("entity.slime_death", registry);
        astroMiner = registerSound("entity.astrominer", registry);
        shuttle = registerSound("shuttle.shuttle", registry);
        parachute = registerSound("player.parachute", registry);
        openAirLock = registerSound("player.openairlock", registry);
        closeAirLock = registerSound("player.closeairlock", registry);
        singleDrip = registerSound("ambience.singledrip", registry);
        scaryScape = registerSound("ambience.scaryscape", registry);
        music = registerSound("galacticraft.music_space", registry);
        deconstructor = registerSound("block.deconstructor", registry);
        advanced_compressor = registerSound("player.unlockchest", registry);
        laserCharge = registerSound("laser.charge", registry);
        laserShoot = registerSound("laser.shoot", registry);
    }

    private static SoundEvent registerSound(String soundName, IForgeRegistry<SoundEvent> registry)
    {
        ResourceLocation soundID = new ResourceLocation(Constants.ASSET_PREFIX, soundName);
        SoundEvent result = new SoundEvent(soundID).setRegistryName(soundID);
        registry.register(result);
        return result;
    }
}
