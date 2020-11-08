package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomBoss;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomTreasure;
import net.minecraft.block.Block;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class BiomeMoon extends BiomeGC
{
    public OreFeatureConfig.FillerBlockType FILLER_TYPE_MOON = OreFeatureConfig.FillerBlockType.create("MOON_STONE", "moon_stone", (state) -> {
        if (state == null) {
            return false;
        } else {
            return state.getBlock() == GCBlocks.moonStone;
        }
    });

    public BiomeMoon(Builder biomeBuilder, boolean isAdaptive)
    {
        super(biomeBuilder, isAdaptive);
        this.addStructure(GCFeatures.MOON_DUNGEON.withConfiguration(new DungeonConfiguration(GCBlocks.moonDungeonBrick.getDefaultState(), 25, 8, 16, 5, 6, RoomBoss.class, RoomTreasure.class)));
        this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, GCFeatures.MOON_DUNGEON.withConfiguration(new DungeonConfiguration(GCBlocks.moonDungeonBrick.getDefaultState(), 25, 8, 16, 5, 6, RoomBoss.class, RoomTreasure.class)).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    }

    protected void addDefaultFeatures()
    {
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(FILLER_TYPE_MOON, GCBlocks.oreCopperMoon.getDefaultState(), 4)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(26, 0, 0, 60))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(FILLER_TYPE_MOON, GCBlocks.oreTinMoon.getDefaultState(), 4)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(23, 0, 0, 60))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(FILLER_TYPE_MOON, GCBlocks.oreCheeseMoon.getDefaultState(), 3)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(14, 0, 0, 85))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(FILLER_TYPE_MOON, GCBlocks.moonDirt.getDefaultState(), 32)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 200))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, GCFeatures.SAPPHIRE_ORE.withConfiguration(new ReplaceBlockConfig(GCBlocks.moonStone.getDefaultState(), GCBlocks.oreSapphire.getDefaultState())).withPlacement(GCFeatures.SAPPHIRE_ORE_PLACEMENT.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    }
}
