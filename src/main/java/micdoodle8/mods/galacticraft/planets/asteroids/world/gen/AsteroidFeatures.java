package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class AsteroidFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, Constants.MOD_ID_CORE);

    public static final RegistryObject<MapGenAbandonedBase> ASTEROID_BASE = register("asteroid_base", () -> new MapGenAbandonedBase(BaseConfiguration::deserialize));
    public static IStructurePieceType CBASE_START = BaseStart::new;
    public static IStructurePieceType CBASE_ROOM = BaseRoom::new;
    public static IStructurePieceType CBASE_DECK = BaseDeck::new;
    public static IStructurePieceType CBASE_PLATE = BasePlate::new;
    public static IStructurePieceType CBASE_HANGAR = BaseHangar::new;
    public static IStructurePieceType CBASE_LINKING = BaseLinking::new;

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
//        Registry.register(Registry.STRUCTURE_PIECE, "AbandonedBase", CBASE_START);
        Registry.register(Registry.STRUCTURE_PIECE, "AbandonedBaseStart", CBASE_START);
        Registry.register(Registry.STRUCTURE_PIECE, "AbandonedBaseRoom", CBASE_ROOM);
        Registry.register(Registry.STRUCTURE_PIECE, "AbandonedBaseDeck", CBASE_DECK);
        Registry.register(Registry.STRUCTURE_PIECE, "AbandonedBasePlate", CBASE_PLATE);
        Registry.register(Registry.STRUCTURE_PIECE, "AbandonedBaseHangar", CBASE_HANGAR);
        Registry.register(Registry.STRUCTURE_PIECE, "AbandonedBaseCorridor", CBASE_LINKING);
    }

    public static <T extends Feature<?>> RegistryObject<T> register(final String name, final Supplier<T> sup)
    {
        return FEATURES.register(name, sup);
    }
}
