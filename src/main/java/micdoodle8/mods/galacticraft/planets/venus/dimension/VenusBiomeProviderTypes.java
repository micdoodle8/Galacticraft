package micdoodle8.mods.galacticraft.planets.venus.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import static micdoodle8.mods.galacticraft.core.GCBlocks.register;

public class VenusBiomeProviderTypes
{
    public static final DeferredRegister<BiomeProviderType<?, ?>> BIOME_PROVIDER_TYPES = new DeferredRegister<>(ForgeRegistries.BIOME_PROVIDER_TYPES, Constants.MOD_ID_PLANETS);

    public static final BiomeProviderType<VenusBiomeProviderSettings, VenusBiomeProvider> VENUS_TYPE = newType();

    // TODO Yikes, fix this once forge makes BiomeProviderType constructor public
    private static BiomeProviderType<VenusBiomeProviderSettings, VenusBiomeProvider> newType()
    {
        Class<?> c = BiomeProviderType.class;
        try
        {
            Constructor<?> cons = c.getDeclaredConstructor(Function.class, Function.class);
            cons.setAccessible(true);
            Function<VenusBiomeProviderSettings, VenusBiomeProvider> f1 = VenusBiomeProvider::new;
            Function<WorldInfo, VenusBiomeProviderSettings> f2 = VenusBiomeProviderSettings::new;
            return (BiomeProviderType<VenusBiomeProviderSettings, VenusBiomeProvider>)cons.newInstance(f1, f2);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return null;
    }

//    @SubscribeEvent
//    public static void registerBiomeTypes(RegistryEvent.Register<BiomeProviderType<?, ?>> evt)
//    {
//        IForgeRegistry<BiomeProviderType<?, ?>> r = evt.getRegistry();
//        register(r, "venus_biome_provider", VENUS_TYPE);
//    }

//    public static final RegistryObject<BiomeProviderType<VenusBiomeProviderSettings, VenusBiomeProvider>> VENUS = register(
//            "venus_biome_provider_type", () -> {
//
//            });
}
