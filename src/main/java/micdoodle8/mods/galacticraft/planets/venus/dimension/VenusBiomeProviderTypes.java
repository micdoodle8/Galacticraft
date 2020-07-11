package micdoodle8.mods.galacticraft.planets.venus.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.function.Supplier;

public class VenusBiomeProviderTypes
{
    public static final DeferredRegister<BiomeProviderType<?, ?>> BIOME_PROVIDER_TYPES = new DeferredRegister<>(ForgeRegistries.BIOME_PROVIDER_TYPES, Constants.MOD_ID_PLANETS);

    // TODO Yikes, fix this once forge makes BiomeProviderType constructor public
    public static final RegistryObject<BiomeProviderType<VenusBiomeProviderSettings, VenusBiomeProvider>> VENUS = register(
            "venus_biome_provider_type", () -> {
                Class<?> c = BiomeProviderType.class;
                try
                {
                    Constructor<?> cons = c.getConstructor(Function.class, Function.class);
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
            });

    private static <T extends BiomeProviderType<?, ?>> RegistryObject<T> register(final String name, final Supplier<T> sup)
    {
        return BIOME_PROVIDER_TYPES.register(name, sup);
    }
}
