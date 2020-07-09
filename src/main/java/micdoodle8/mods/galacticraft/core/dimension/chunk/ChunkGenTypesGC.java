package micdoodle8.mods.galacticraft.core.dimension.chunk;

import java.util.function.Supplier;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChunkGenTypesGC
{

    public static final DeferredRegister<ChunkGeneratorType<?, ?>> CHUNK_GENERATOR_TYPES = new DeferredRegister<>(ForgeRegistries.CHUNK_GENERATOR_TYPES, Constants.MOD_ID_CORE);

    public static final RegistryObject<ChunkGeneratorType<MoonGenSettings, MoonChunkGenerator>> MOON = register("moon_chunk_generator_type", () -> new ChunkGeneratorType<>(MoonChunkGenerator::new, true, MoonGenSettings::new));

    private static <T extends ChunkGeneratorType<?, ?>> RegistryObject<T> register(final String name, final Supplier<T> sup)
    {
        return CHUNK_GENERATOR_TYPES.register(name, sup);
    }
}