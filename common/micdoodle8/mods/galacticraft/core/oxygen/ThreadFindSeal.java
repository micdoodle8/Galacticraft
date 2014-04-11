package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol.VecDirPair;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
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
	public List<GCCoreTileEntityOxygenSealer> sealers;
	public List<Vector3> oxygenReliantBlocks;
	public HashSet<VecDirPair> checked;
	public int checkCount;
	public boolean looping;
	private HashMap<Vector3, GCCoreTileEntityOxygenSealer> sealersAround = new HashMap<Vector3, GCCoreTileEntityOxygenSealer>();

	public ThreadFindSeal(GCCoreTileEntityOxygenSealer sealer)
	{
		this(sealer.worldObj, new Vector3(sealer), sealer.getFindSealChecks(), new ArrayList<GCCoreTileEntityOxygenSealer>(), new ArrayList<Vector3>(), new HashSet<VecDirPair>());
	}

	@SuppressWarnings("unchecked")
	public ThreadFindSeal(World world, Vector3 head, int checkCount, List<GCCoreTileEntityOxygenSealer> sealers, List<Vector3> oxygenReliantBlocks, HashSet<VecDirPair> checked)
	{
		super("GC Sealer Roomfinder Thread");
		this.world = world;
		this.head = head;
		this.checkCount = checkCount;
		this.sealers = sealers;
		this.oxygenReliantBlocks = oxygenReliantBlocks;
		this.checked = checked;
		
		if (this.isAlive())
		{
			this.interrupt();
		}
		
		for (TileEntity tile : new ArrayList<TileEntity>(world.loadedTileEntityList))
		{
			if (tile instanceof GCCoreTileEntityOxygenSealer && tile.getDistanceFrom(head.intX(), head.intY(), head.intZ()) < 2048 * 2048)
			{
				this.sealersAround.put(new Vector3(tile.xCoord, tile.yCoord, tile.zCoord), (GCCoreTileEntityOxygenSealer) tile);
			}
		}
		
		start();
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
				GCCoreTileEntityOxygenSealer sealer = this.sealers.get(i);
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
				int blockID = checkedVec.getPosition().getBlockID(this.world);

				if (this.sealed && blockID == 0)
				{
					GCCoreTickHandlerServer.scheduleNewBlockChange(world.provider.dimensionId, new ScheduledBlockChange(checkedVec.getPosition(), GCCoreBlocks.breatheableAir.blockID, 0, 3));
				}
			}
		}
		else
		{
			this.checked.clear();
			this.loopThroughD(this.head.clone().translate(new Vector3(0, 1, 0)));

			for (VecDirPair checkedVec : this.checked)
			{
				int blockID = checkedVec.getPosition().getBlockID(this.world);

				if (blockID == GCCoreBlocks.breatheableAir.blockID)
				{
					GCCoreTickHandlerServer.scheduleNewBlockChange(world.provider.dimensionId, new ScheduledBlockChange(checkedVec.getPosition(), 0, 0, 3));
				}
			}
		}

		TileEntity headTile = this.sealersAround.get(this.head);

		if (headTile instanceof GCCoreTileEntityOxygenSealer)
		{
			GCCoreTileEntityOxygenSealer headSealer = (GCCoreTileEntityOxygenSealer) headTile;

			for (GCCoreTileEntityOxygenSealer sealer : this.sealers)
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

		if (GCCoreConfigManager.enableDebug)
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

						TileEntity tileAtVec = this.sealersAround.get(sideVec);

						if (tileAtVec != null && tileAtVec instanceof GCCoreTileEntityOxygenSealer && !this.sealers.contains(tileAtVec))
						{
							GCCoreTileEntityOxygenSealer sealer = (GCCoreTileEntityOxygenSealer) tileAtVec;

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

					if ((sideVec.getBlockID(this.world) == 0 || sideVec.getBlockID(this.world) == GCCoreBlocks.breatheableAir.blockID) && !this.checked(sideVec, dir))
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
		return pair.getPosition().getBlockID(this.world) == GCCoreBlocks.breatheableAir.blockID;
	}
}
