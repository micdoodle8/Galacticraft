package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * OxygenPressureProtocol.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class OxygenPressureProtocol
{
	public static ArrayList<Block> vanillaPermeableBlocks = new ArrayList<Block>();
	public static Map<Integer, ArrayList<Integer>> nonPermeableBlocks = new HashMap<Integer, ArrayList<Integer>>();
	public static final int MAX_SEAL_CHECKS = 400;

	static
	{
		OxygenPressureProtocol.vanillaPermeableBlocks.add(Blocks.sponge);

		try
		{
			for (final String s : ConfigManagerCore.sealableIDs)
			{
				final String[] split = s.split(":");

				if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(Integer.parseInt(split[0])))
				{
					final ArrayList<Integer> l = OxygenPressureProtocol.nonPermeableBlocks.get(Integer.parseInt(split[0]));
					l.add(Integer.parseInt(split[1]));
					OxygenPressureProtocol.nonPermeableBlocks.put(Integer.parseInt(split[0]), l);
				}
				else
				{
					final ArrayList<Integer> a = new ArrayList<Integer>();
					a.add(Integer.parseInt(split[1]));
					OxygenPressureProtocol.nonPermeableBlocks.put(Integer.parseInt(split[0]), a);
				}
			}
		}
		catch (final Exception e)
		{
			System.err.println();
			System.err.println("Error finding sealable IDs from the Galacticraft config, check that they are listed properly!");
			System.err.println();
			e.printStackTrace();
		}
	}

	public static void updateSealerStatus(TileEntityOxygenSealer head)
	{
		try
		{
			head.threadSeal = new ThreadFindSeal(head);
		}
		catch (IllegalThreadStateException e)
		{
			;
		}
	}

	public static ThreadFindSeal onEdgeBlockUpdated(World world, BlockVec3 vec)
	{
		return new ThreadFindSeal(world, vec, 2000, new ArrayList<TileEntityOxygenSealer>());
	}

	// Note this will NPE if id==0, so don't call this with id==0
	public static boolean canBlockPassAir(World world, int id, BlockVec3 vec, int side)
	{
		if (OxygenPressureProtocol.vanillaPermeableBlocks.contains(id))
		{
			return true;
		}

		Block block = Block.getBlockById(id);

		if (!block.isOpaqueCube())
		{
			if (block instanceof IPartialSealableBlock)
			{
				if (((IPartialSealableBlock) block).isSealed(world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side)))
				{
					return false;
				}
				return true;
			}

			if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(id) && OxygenPressureProtocol.nonPermeableBlocks.get(id).contains(vec.getBlockMetadata(world)))
			{
				return false;
			}

			return true;
		}

		return false;
	}
}
