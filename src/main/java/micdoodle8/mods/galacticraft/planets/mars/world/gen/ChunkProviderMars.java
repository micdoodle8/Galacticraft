package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.prefab.core.BlockMetaPair;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeDecoratorSpace;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.ChunkProviderSpace;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.MapGenBaseMeta;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.MapGenDungeon;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockBasicMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.List;

public class ChunkProviderMars extends ChunkProviderSpace
{
    private final BiomeDecoratorMars marsBiomeDecorator = new BiomeDecoratorMars();
    private final MapGenCavernMars caveGenerator = new MapGenCavernMars();
    private final MapGenCaveMars cavernGenerator = new MapGenCaveMars();

    private final MapGenDungeon dungeonGenerator = new MapGenDungeonMars(new DungeonConfiguration(MarsBlocks.marsBlock.getDefaultState().withProperty(BlockBasicMars.BASIC_TYPE, BlockBasicMars.EnumBlockBasic.DUNGEON_BRICK), 30, 8, 16, 7, 7, RoomBossMars.class, RoomTreasureMars.class));

    public ChunkProviderMars(World par1World, long seed, boolean mapFeaturesEnabled)
    {
        super(par1World, seed, mapFeaturesEnabled);
    }

    @Override
    protected BiomeDecoratorSpace getBiomeGenerator()
    {
        return this.marsBiomeDecorator;
    }

    @Override
    protected BiomeGenBase[] getBiomesForGeneration()
    {
        return new BiomeGenBase[] { BiomeGenBaseMars.marsFlat };
    }

    @Override
    protected int getSeaLevel()
    {
        return 93;
    }

    @Override
    protected List<MapGenBaseMeta> getWorldGenerators()
    {
        List<MapGenBaseMeta> generators = Lists.newArrayList();
        generators.add(this.caveGenerator);
        generators.add(this.cavernGenerator);
        return generators;
    }

    @Override
    protected BlockMetaPair getGrassBlock()
    {
        return BiomeGenBaseMars.BLOCK_TOP;
    }

    @Override
    protected BlockMetaPair getDirtBlock()
    {
        return BiomeGenBaseMars.BLOCK_FILL;
    }

    @Override
    protected BlockMetaPair getStoneBlock()
    {
        return BiomeGenBaseMars.BLOCK_LOWER;
    }

    @Override
    public double getHeightModifier()
    {
        return 12;
    }

    @Override
    public double getSmallFeatureHeightModifier()
    {
        return 26;
    }

    @Override
    public double getMountainHeightModifier()
    {
        return 95;
    }

    @Override
    public double getValleyHeightModifier()
    {
        return 50;
    }

    @Override
    public int getCraterProbability()
    {
        return 2000;
    }

    @Override
    public void onChunkProvide(int cX, int cZ, ChunkPrimer primer)
    {
        this.dungeonGenerator.generate(this, this.worldObj, cX, cZ, primer);
    }

    @Override
    public void onPopulate(IChunkProvider provider, int cX, int cZ)
    {
        this.dungeonGenerator.generateStructure(this.worldObj, this.rand, new ChunkCoordIntPair(cX, cZ));
    }

    @Override
    public void recreateStructures(Chunk chunk, int x, int z)
    {
        this.dungeonGenerator.generate(this, this.worldObj, x, z, null);
    }
}
