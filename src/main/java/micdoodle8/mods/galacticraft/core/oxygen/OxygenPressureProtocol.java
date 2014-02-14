package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.collect.Lists;

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

	public static class VecDirPair
	{
		private final Vector3 position;
		private final ForgeDirection direction;

		public VecDirPair(Vector3 position, ForgeDirection direction)
		{
			this.position = position;
			this.direction = direction;
		}

		public Vector3 getPosition()
		{
			return this.position;
		}

		public ForgeDirection getDirection()
		{
			return this.direction;
		}

		@Override
		public int hashCode()
		{
			return new HashCodeBuilder().append(this.position.x).append(this.position.y).append(this.position.z).hashCode();
		}

		@Override
		public boolean equals(Object other)
		{
			if (other == null)
			{
				return false;
			}

			if (other == this)
			{
				return true;
			}

			if (other instanceof VecDirPair)
			{
				VecDirPair otherPair = (VecDirPair) other;
				return new EqualsBuilder().append(this.position.x, otherPair.position.x).append(this.position.y, otherPair.position.y).append(this.position.z, otherPair.position.z).isEquals();
			}

			return false;
		}
	}

	public static void updateSealerStatus(TileEntityOxygenSealer head)
	{
		if (head.threadSeal != null)
		{
			head.threadSeal.interrupt();
		}

		head.threadSeal = new ThreadFindSeal();
		head.threadSeal.world = head.getWorldObj();
		head.threadSeal.head = new Vector3(head);
		head.threadSeal.checkCount = head.getFindSealChecks();
		head.threadSeal.sealers = new ArrayList<TileEntityOxygenSealer>();
		head.threadSeal.oxygenReliantBlocks = Lists.newArrayList();
		head.threadSeal.checked = new HashSet<VecDirPair>();
		head.threadSeal.start();
	}

	public static void onEdgeBlockUpdated(World world, Vector3 vec)
	{
		ThreadFindSeal threadSeal = new ThreadFindSeal();
		threadSeal.world = world;
		threadSeal.head = vec;
		threadSeal.checkCount = 1500;
		threadSeal.sealers = new ArrayList<TileEntityOxygenSealer>();
		threadSeal.oxygenReliantBlocks = Lists.newArrayList();
		threadSeal.checked = new HashSet<VecDirPair>();
		threadSeal.start();
	}
}
