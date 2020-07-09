package micdoodle8.mods.galacticraft.planets.venus.world.gen;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.*;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class VenusFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, Constants.MOD_ID_PLANETS);

    public static final RegistryObject<MapGenDungeonVenus> VENUS_DUNGEON = register("venus_dungeon", () -> new MapGenDungeonVenus(DungeonConfigurationVenus::deserialize));
    public static IStructurePieceType CVENUS_DUNGEON_START = DungeonStartVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_CORRIDOR = CorridorVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_EMPTY = RoomEmptyVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_BOSS = RoomBossVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_TREASURE = RoomTreasureVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_SPAWNER = RoomSpawnerVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_CHEST = RoomChestVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_ENTRANCE = RoomEntranceVenus::new;

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
        Registry.register(Registry.STRUCTURE_PIECE, "VenusDungeon", CVENUS_DUNGEON_START);
        Registry.register(Registry.STRUCTURE_PIECE, "VenusDungeonCorridor", CVENUS_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "VenusDungeonEmptyRoom", CVENUS_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "VenusDungeonBossRoom", CVENUS_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "VenusDungeonTreasureRoom", CVENUS_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "VenusDungeonSpawnerRoom", CVENUS_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "VenusDungeonChestRoom", CVENUS_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "VenusDungeonEntranceRoom", CVENUS_DUNGEON_CORRIDOR);
    }

    public static <T extends Feature<?>> RegistryObject<T> register(final String name, final Supplier<T> sup)
    {
        return FEATURES.register(name, sup);
    }
}
