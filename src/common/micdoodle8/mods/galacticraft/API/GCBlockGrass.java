package micdoodle8.mods.galacticraft.API;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

/**
 *  Extend this to allow planting of trees/flowers in space.
 *
 */
public class GCBlockGrass extends Block
{
	public GCBlockGrass(int i, int j)
	{
		super(i, j, Material.grass);
	}
}
