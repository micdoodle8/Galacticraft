package micdoodle8.mods.galacticraft.planets.venus.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class VenusBiomeProviderTypes
{
    public static final DeferredRegister<BiomeProviderType<?, ?>> BIOME_PROVIDER_TYPES = new DeferredRegister<>(ForgeRegistries.BIOME_PROVIDER_TYPES, Constants.MOD_ID_PLANETS);

    public static final RegistryObject<BiomeProviderType<VenusBiomeProviderSettings, VenusBiomeProvider>> VENUS = register(
            "venus_biome_provider_type", () -> new BiomeProviderType<>(VenusBiomeProvider::new, VenusBiomeProviderSettings::new));

    private static <T extends BiomeProviderType<?, ?>> RegistryObject<T> register(final String name, final Supplier<T> sup)
    {
        return BIOME_PROVIDER_TYPES.register(name, sup);
    }
}
