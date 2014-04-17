package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

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

	// Note this will NPE if id==0, so don't call this with id==0
	public static boolean canBlockPassAir(World world, int id, BlockVec3 vec, int side)
	{
		if (OxygenPressureProtocol.vanillaPermeableBlocks.contains(id))
		{
			return true;
		}

		Block block = Block.blocksList[id];

		if (!block.isOpaqueCube())
		{
			if (block instanceof IPartialSealableBlock)
			{
				return !(((IPartialSealableBlock) block).isSealed(world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side)));
			}

			//Solid but non-opaque blocks, for example glass
			if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(id) && OxygenPressureProtocol.nonPermeableBlocks.get(id).contains(vec.getBlockMetadata(world)))
			{
				return false;
			}

			//Half slab seals on the top side or the bottom side according to its metadata
			if (block instanceof BlockHalfSlab)
	        {
	            return !((side == 0 && (vec.getBlockMetadata(world) & 8) == 8) || (side == 1 && (vec.getBlockMetadata(world) & 8) == 0));
	        }
	        
			//Farmland only seals on the solid underside
			if (block instanceof BlockFarmland)
	        {
	            return side!=1;
	        }

			//General case - this should cover any block which correctly implements isBlockSolidOnSide
			//including most modded blocks - Forge microblocks in particular is covered by this.
			// ### Any exceptions in mods should implement the IPartialSealableBlock interface ###
			return !block.isBlockSolidOnSide(world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side ^ 1));
		}

		return false;
	}
}
