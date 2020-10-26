package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
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

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> evt)
    {
        IForgeRegistry<SoundEvent> r = evt.getRegistry();
        bossDeath = registerSound("entity.bossdeath", r);
        bossLaugh = registerSound("entity.bosslaugh", r);
        bossOoh = registerSound("entity.ooh", r);
        bossOuch = registerSound("entity.ouch", r);
        slimeDeath = registerSound("entity.slime_death", r);
        astroMiner = registerSound("entity.astrominer", r);
        shuttle = registerSound("shuttle.shuttle", r);
        parachute = registerSound("player.parachute", r);
        openAirLock = registerSound("player.openairlock", r);
        closeAirLock = registerSound("player.closeairlock", r);
        singleDrip = registerSound("ambience.singledrip", r);
        scaryScape = registerSound("ambience.scaryscape", r);
        music = registerSound("galacticraft.music_space", r);
        deconstructor = registerSound("block.deconstructor", r);
        advanced_compressor = registerSound("player.unlockchest", r);
        laserCharge = registerSound("laser.charge", r);
        laserShoot = registerSound("laser.shoot", r);
    }

    private static SoundEvent registerSound(String soundName, IForgeRegistry<SoundEvent> registry)
    {
        ResourceLocation soundID = new ResourceLocation(Constants.MOD_ID_CORE, soundName);
        SoundEvent result = new SoundEvent(soundID).setRegistryName(soundID);
        registry.register(result);
        return result;
    }
}
