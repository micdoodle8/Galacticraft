package micdoodle8.mods.galacticraft.core.event;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SoundEventsGC
{
    public static SoundEvent MUSIC_SPACE;
    public static SoundEvent UNLOCK_CHEST;
    public static SoundEvent PARACHUTE;
    public static SoundEvent OPEN_AIR_LOCK;
    public static SoundEvent CLOSE_AIR_LOCK;
    public static SoundEvent SLIME_DEATH;
    public static SoundEvent BOSS_LIVING;
    public static SoundEvent BOSS_LAUGH;
    public static SoundEvent BOSS_DEATH;
    public static SoundEvent ASTRO_MINER;
    public static SoundEvent SINGLE_DRIP;
    public static SoundEvent SCARY_SCAPE;
    public static SoundEvent SHUTTLE;

    public static void registerSounds()
    {
        MUSIC_SPACE = registerSound("galacticraft.musicSpace");
        UNLOCK_CHEST = registerSound("player.unlockchest");
        PARACHUTE = registerSound("player.parachute");
        OPEN_AIR_LOCK = registerSound("player.openairlock");
        CLOSE_AIR_LOCK = registerSound("player.closeairlock");
        SLIME_DEATH = registerSound("entity.slime_death");
        BOSS_LIVING = registerSound("entity.bossliving");
        BOSS_LAUGH = registerSound("entity.bosslaugh");
        BOSS_DEATH = registerSound("entity.bossdeath");
        ASTRO_MINER = registerSound("entity.astrominer");
        SINGLE_DRIP = registerSound("ambience.singledrip");
        SCARY_SCAPE = registerSound("ambience.scaryscape");
        SHUTTLE = registerSound("shuttle.shuttle");
    }

    private static SoundEvent registerSound(String soundName)
    {
        ResourceLocation soundID = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, soundName);
        return GameRegistry.register(new SoundEvent(soundID)).setRegistryName(soundID);
    }
}
