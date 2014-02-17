package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.HashSet;
import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.oxygen.SealerThreadManager.VecDirPair;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.GCConfigManager;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.FMLLog;

/**
 * ThreadFindSeal.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ThreadFindSeal extends Thread
{
	public World world;
	public Vector3 head;
	public boolean sealed;
	public List<TileEntityOxygenSealer> sealers;
	public List<Vector3> oxygenReliantBlocks;
	public HashSet<VecDirPair> checked;
	public int checkCount;
	public boolean looping;

	public ThreadFindSeal()
	{
		super("GC Sealer Roomfinder Thread");
	}

	@Override
	public void run()
	{
		long time1 = System.nanoTime();

		this.sealed = true;
		this.looping = true;
		this.loopThrough(this.head.clone().translate(new Vector3(0, 1, 0)));

		if (this.sealers.size() > 1)
		{
			this.checkCount = 0;

			for (int i = 0; i < this.sealers.size(); i++)
			{
				TileEntityOxygenSealer sealer = this.sealers.get(i);
				this.checkCount += sealer.getFindSealChecks();
			}

			this.sealed = true;
			this.checked.clear();
			this.loopThrough(this.head.clone().translate(new Vector3(0, 1, 0)));
		}

		long time2 = System.nanoTime();

		if (this.sealed)
		{
			for (VecDirPair checkedVec : this.checked)
			{
				Block block = checkedVec.getPosition().getBlock(this.world);

				if (this.sealed && block == Blocks.air)
				{
					TickHandlerServer.scheduleNewBlockChange(this.world.provider.dimensionId, new ScheduledBlockChange(checkedVec.getPosition(), GCBlocks.breatheableAir, 0, 3));
				}
			}
		}
		else
		{
			this.checked.clear();
			this.loopThroughD(this.head.clone().translate(new Vector3(0, 1, 0)));

			for (VecDirPair checkedVec : this.checked)
			{
				Block block = checkedVec.getPosition().getBlock(this.world);

				if (block == GCBlocks.breatheableAir)
				{
					TickHandlerServer.scheduleNewBlockChange(this.world.provider.dimensionId, new ScheduledBlockChange(checkedVec.getPosition(), Blocks.air, 0, 3));
				}
			}
		}

		TileEntity headTile = this.head.getTileEntity(this.world);

		if (headTile instanceof TileEntityOxygenSealer)
		{
			TileEntityOxygenSealer headSealer = (TileEntityOxygenSealer) headTile;

			for (TileEntityOxygenSealer sealer : this.sealers)
			{
				if (sealer != null && headSealer != sealer && headSealer.stopSealThreadCooldown <= sealer.stopSealThreadCooldown)
				{
					sealer.threadSeal = this;
					sealer.stopSealThreadCooldown = 100;
				}
			}
		}

		long time3 = System.nanoTime();

		this.looping = false;

		if (GCConfigManager.enableDebug)
		{
			FMLLog.info("Oxygen Sealer Check Completed at x" + this.head.intX() + " y" + this.head.intY() + " z" + this.head.intZ());
			FMLLog.info("   Sealed: " + this.sealed);
			FMLLog.info("   Loop Time taken: " + (time2 - time1) / 1000000.0D + "ms");
			FMLLog.info("   Place Time taken: " + (time3 - time2) / 1000000.0D + "ms");
			FMLLog.info("   Total Time taken: " + (time3 - time1) / 1000000.0D + "ms");
			FMLLog.info("   Found: " + this.sealers.size() + " sealers");
			FMLLog.info("   Looped through: " + this.checked.size() + " blocks");
		}
	}

	private void loopThroughD(Vector3 vec)
	{
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			Vector3 sideVec = vec.clone().modifyPositionFromSide(dir);
			VecDirPair pair = new VecDirPair(sideVec, dir);

			if (!this.checked(pair))
			{
				this.check(pair);

				if (this.breathableAirAdjacent(pair))
				{
					this.loopThroughD(sideVec);
				}
			}
		}
	}

	private void loopThrough(Vector3 vec)
	{
		if (this.sealed)
		{
			if (this.checkCount > 0)
			{
				for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
				{
					Vector3 sideVec = vec.clone().modifyPositionFromSide(dir);
					VecDirPair pair = new VecDirPair(sideVec, dir);

					if (!this.checked(pair))
					{
						this.checkCount--;
						this.check(pair);

						if (WorldUtil.canBlockPass(this.world, pair))
						{
							this.loopThrough(sideVec);
						}

						TileEntity tileAtVec = sideVec.getTileEntity(this.world);

						if (tileAtVec != null && tileAtVec instanceof TileEntityOxygenSealer && !this.sealers.contains(tileAtVec))
						{
							TileEntityOxygenSealer sealer = (TileEntityOxygenSealer) tileAtVec;

							if (sealer.active)
							{
								this.sealers.add(sealer);
							}
						}
					}
				}
			}
			else
			{
				for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
				{
					Vector3 sideVec = vec.clone().modifyPositionFromSide(dir);

					if ((sideVec.getBlock(this.world) == Blocks.air || sideVec.getBlock(this.world) == GCBlocks.breatheableAir) && !this.checked(sideVec, dir))
					{
						this.sealed = false;
					}
				}
			}
		}
	}

	private boolean checked(Vector3 vec, ForgeDirection dir)
	{
		return this.checked(new VecDirPair(vec, dir));
	}

	private boolean checked(VecDirPair pair)
	{
		// for (VecDirPair pair2 : this.checked)
		// {
		// if (pair2.getPosition().equals(pair.getPosition()))
		// {
		// return true;
		// }
		// }

		return this.checked.contains(pair);
	}

	private void check(VecDirPair pair)
	{
		this.checked.add(pair);
	}

	private boolean breathableAirAdjacent(VecDirPair pair)
	{
		for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			Vector3 vec = pair.getPosition().clone().modifyPositionFromSide(dir);

			if (this.isBreathableAir(new VecDirPair(vec, dir)))
			{
				return true;
			}
		}

		return false;
	}

	private boolean isBreathableAir(VecDirPair pair)
	{
		return pair.getPosition().getBlock(this.world) == GCBlocks.breatheableAir;
	}
}
