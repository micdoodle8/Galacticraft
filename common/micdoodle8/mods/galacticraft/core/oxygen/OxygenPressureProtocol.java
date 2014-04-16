package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import net.minecraft.block.Block;
import net.minecraft.world.World;

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
	public static ArrayList<Integer> vanillaPermeableBlocks = new ArrayList<Integer>();
	public static Map<Integer, ArrayList<Integer>> nonPermeableBlocks = new HashMap<Integer, ArrayList<Integer>>();
	public static final int MAX_SEAL_CHECKS = 400;

	static
	{
		OxygenPressureProtocol.vanillaPermeableBlocks.add(Block.sponge.blockID);

		try
		{
			for (final String s : GCCoreConfigManager.sealableIDs)
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

	public static void updateSealerStatus(GCCoreTileEntityOxygenSealer head)
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
		return new ThreadFindSeal(world, vec, 2000, new ArrayList<GCCoreTileEntityOxygenSealer>());
	}
}
