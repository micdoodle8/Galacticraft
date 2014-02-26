package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

/**
 * GCMarsWorldGenEggs.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class WorldGenSlimelingEggs extends WorldGenerator
{
	private Block eggBlock;

	public WorldGenSlimelingEggs(Block eggID)
	{
		this.eggBlock = eggID;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int x, int y, int z)
	{
		int i1 = x + par2Random.nextInt(8) - par2Random.nextInt(8);
		int j1 = y + par2Random.nextInt(4) - par2Random.nextInt(4);
		int k1 = z + par2Random.nextInt(8) - par2Random.nextInt(8);

		if (par1World.isAirBlock(i1, j1, k1) && (!par1World.provider.hasNoSky || j1 < 127) && this.eggBlock.canBlockStay(par1World, i1, j1, k1))
		{
			par1World.setBlock(i1, j1, k1, this.eggBlock, par2Random.nextInt(3), 2);
		}

		return true;
	}
}
