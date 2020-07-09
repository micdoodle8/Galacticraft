package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class GCFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, Constants.MOD_ID_CORE);

    public static final RegistryObject<MapGenDungeon> MOON_DUNGEON = register("moon_dungeon", () -> new MapGenDungeon(DungeonConfiguration::deserialize));
    public static IStructurePieceType CMOON_DUNGEON_START = DungeonStart::new;
    public static IStructurePieceType CMOON_DUNGEON_CORRIDOR = Corridor::new;
    public static IStructurePieceType CMOON_DUNGEON_EMPTY = RoomEmpty::new;
    public static IStructurePieceType CMOON_DUNGEON_BOSS = RoomBoss::new;
    public static IStructurePieceType CMOON_DUNGEON_TREASURE = RoomTreasure::new;
    public static IStructurePieceType CMOON_DUNGEON_SPAWNER = RoomSpawner::new;
    public static IStructurePieceType CMOON_DUNGEON_CHEST = RoomChest::new;
    public static IStructurePieceType CMOON_DUNGEON_ENTRANCE = RoomEntrance::new;

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
        Registry.register(Registry.STRUCTURE_PIECE, "MoonDungeon", CMOON_DUNGEON_START);
        Registry.register(Registry.STRUCTURE_PIECE, "MoonDungeonCorridor", CMOON_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "MoonDungeonEmptyRoom", CMOON_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "MoonDungeonBossRoom", CMOON_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "MoonDungeonTreasureRoom", CMOON_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "MoonDungeonSpawnerRoom", CMOON_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "MoonDungeonChestRoom", CMOON_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "MoonDungeonEntranceRoom", CMOON_DUNGEON_CORRIDOR);
    }

    public static <T extends Feature<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return FEATURES.register(name, sup);
    }
}
