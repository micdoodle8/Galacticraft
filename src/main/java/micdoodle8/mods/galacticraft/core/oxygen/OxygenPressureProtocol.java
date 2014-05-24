package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.material.Material;
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
	public final static Map<Block, ArrayList<Integer>> nonPermeableBlocks = new HashMap<Block, ArrayList<Integer>>();

	static
	{
		try
		{
			for (final String s : ConfigManagerCore.sealableIDs)
			{
				final String[] split = s.split(":");

				if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(Block.getBlockById(Integer.parseInt(split[0]))))
				{
					final ArrayList<Integer> l = OxygenPressureProtocol.nonPermeableBlocks.get(Block.getBlockById(Integer.parseInt(split[0])));
					l.add(Integer.parseInt(split[1]));
					OxygenPressureProtocol.nonPermeableBlocks.put(Block.getBlockById(Integer.parseInt(split[0])), l);
				}
				else
				{
					final ArrayList<Integer> a = new ArrayList<Integer>();
					a.add(Integer.parseInt(split[1]));
					OxygenPressureProtocol.nonPermeableBlocks.put(Block.getBlockById(Integer.parseInt(split[0])), a);
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

	public static void onEdgeBlockUpdated(World world, BlockVec3 vec)
	{
		if (ConfigManagerCore.enableSealerEdgeChecks)
			TickHandlerServer.scheduleNewEdgeCheck(world.provider.dimensionId, vec);
	}

	// Note this will NPE if id==0, so don't call this with id==0
	public static boolean canBlockPassAir(World world, Block block, BlockVec3 vec, int side)
	{
		//Check leaves first, because their isOpaqueCube() test depends on graphics settings
		//(See net.minecraft.block.BlockLeaves.isOpaqueCube()!)
		if (block instanceof BlockLeavesBase)
		{
			return true;
		}
		
		if (block.isOpaqueCube())
		{
			if (block instanceof BlockGravel || block.getMaterial() == Material.cloth || block instanceof BlockSponge)
			{
				return true;
			}
			
			return false;
		}
		
		if (block instanceof BlockGlass)
		{
			return false;
		}

		if (block instanceof IPartialSealableBlock)
		{
			return !(((IPartialSealableBlock) block).isSealed(world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side)));
		}

		//Solid but non-opaque blocks, for example glass
		if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(block) && OxygenPressureProtocol.nonPermeableBlocks.get(block).contains(vec.getBlockMetadata(world)))
		{
			return false;
		}

		//Half slab seals on the top side or the bottom side according to its metadata
		if (block instanceof BlockSlab)
        {
            return !((side == 0 && (vec.getBlockMetadata(world) & 8) == 8) || (side == 1 && (vec.getBlockMetadata(world) & 8) == 0));
        }
        
		//Farmland etc only seals on the solid underside
		if (block instanceof BlockFarmland || block instanceof BlockEnchantmentTable || block instanceof BlockLiquid)
        {
            return side!=1;
        }
		
		if (block instanceof BlockPistonBase)
		{
			BlockPistonBase piston = (BlockPistonBase)block;
			int meta = vec.getBlockMetadata(world);
			if (piston.isExtended(meta))
			{
				int facing = piston.getPistonOrientation(meta);
				return (side!=facing);
			}
			return false;
		}

		//General case - this should cover any block which correctly implements isBlockSolidOnSide
		//including most modded blocks - Forge microblocks in particular is covered by this.
		// ### Any exceptions in mods should implement the IPartialSealableBlock interface ###
		return !block.isSideSolid(world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side ^ 1));
	}
}
