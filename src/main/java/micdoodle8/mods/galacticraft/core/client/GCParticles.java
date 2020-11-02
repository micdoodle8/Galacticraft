package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.fx.*;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_CORE)
public class GCParticles
{
    @ObjectHolder(ParticleTypeNames.whiteSmoke)
    public static BasicParticleType WHITE_SMOKE_IDLE;
    @ObjectHolder(ParticleTypeNames.whiteSmokeLaunched)
    public static BasicParticleType WHITE_SMOKE_LAUNCHED;
    @ObjectHolder(ParticleTypeNames.whiteSmokeLarge)
    public static BasicParticleType WHITE_SMOKE_IDLE_LARGE;
    @ObjectHolder(ParticleTypeNames.whiteSmokeLaunchedLarge)
    public static BasicParticleType WHITE_SMOKE_LAUNCHED_LARGE;
    @ObjectHolder(ParticleTypeNames.launchFlame)
    public static ParticleType<EntityParticleData> LAUNCH_FLAME_IDLE;
    @ObjectHolder(ParticleTypeNames.launchFlameLaunched)
    public static ParticleType<EntityParticleData> LAUNCH_FLAME_LAUNCHED;
    @ObjectHolder(ParticleTypeNames.launchSmoke)
    public static BasicParticleType LAUNCH_SMOKE_TINY;
    @ObjectHolder(ParticleTypeNames.oilDrip)
    public static BasicParticleType OIL_DRIP;
    @ObjectHolder(ParticleTypeNames.oxygen)
    public static BasicParticleType OXYGEN;
    @ObjectHolder(ParticleTypeNames.landerFlame)
    public static ParticleType<EntityParticleData> LANDER_FLAME;

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name)
    {
        reg.register(thing.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        register(reg, thing, new ResourceLocation(Constants.MOD_ID_CORE, name));
    }

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> evt)
    {
        IForgeRegistry<ParticleType<?>> r = evt.getRegistry();

        register(r, new BasicParticleType(false), ParticleTypeNames.whiteSmoke);
        register(r, new BasicParticleType(false), ParticleTypeNames.whiteSmokeLaunched);
        register(r, new BasicParticleType(false), ParticleTypeNames.whiteSmokeLarge);
        register(r, new BasicParticleType(false), ParticleTypeNames.whiteSmokeLaunchedLarge);
        register(r, new ParticleType<>(false, EntityParticleData.DESERIALIZER), ParticleTypeNames.launchFlame);
        register(r, new ParticleType<>(false, EntityParticleData.DESERIALIZER), ParticleTypeNames.launchFlameLaunched);
        register(r, new BasicParticleType(false), ParticleTypeNames.launchSmoke);
        register(r, new BasicParticleType(false), ParticleTypeNames.oilDrip);
        register(r, new BasicParticleType(false), ParticleTypeNames.oxygen);
        register(r, new ParticleType<>(false, EntityParticleData.DESERIALIZER), ParticleTypeNames.landerFlame);
    }
}
