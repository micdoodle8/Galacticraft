package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeDecoratorSpace;
import micdoodle8.mods.galacticraft.core.world.gen.WorldGenMinableMeta;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeDecoratorMars extends BiomeDecoratorSpace
{
	private WorldGenerator dirtGen;
	private WorldGenerator deshGen;
	private WorldGenerator tinGen;
	private WorldGenerator copperGen;
	private WorldGenerator ironGen;

	public BiomeDecoratorMars()
	{
		this.copperGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 4, 0, true, MarsBlocks.marsBlock, 9);
		this.tinGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 4, 1, true, MarsBlocks.marsBlock, 9);
		this.deshGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 4, 2, true, MarsBlocks.marsBlock, 9);
		this.ironGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 8, 3, true, MarsBlocks.marsBlock, 9);
		this.dirtGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 32, 6, true, MarsBlocks.marsBlock, 9);
	}

    @Override
    protected void decorate()
    {
        this.generateOre(20, this.dirtGen, 0, 200);
        this.generateOre(15, this.deshGen, 0, 128);
        this.generateOre(26, this.copperGen, 0, 60);
        this.generateOre(23, this.tinGen, 0, 60);
        this.generateOre(20, this.ironGen, 0, 64);
    }
}
