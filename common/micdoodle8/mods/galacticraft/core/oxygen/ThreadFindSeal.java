package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.Lists;

import micdoodle8.mods.galacticraft.core.oxygen.BlockVec3;
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
	public BlockVec3 head;
	public boolean sealed;
	public List<GCCoreTileEntityOxygenSealer> sealers;
	public List<BlockVec3> oxygenReliantBlocks;
	public HashSet<BlockVec3> checked;
	public int checkCount;
	public boolean looping;
	private int breatheableAirID;

	public ThreadFindSeal(GCCoreTileEntityOxygenSealer sealer)
	{
		this(sealer.worldObj, new BlockVec3(sealer), sealer.getFindSealChecks(), new ArrayList<GCCoreTileEntityOxygenSealer>(), new ArrayList<BlockVec3>(), new HashSet<BlockVec3>());
	}

	public ThreadFindSeal(World world, BlockVec3 head, int checkCount, List<GCCoreTileEntityOxygenSealer> sealers, List<BlockVec3> oxygenReliantBlocks, HashSet<BlockVec3> checked)
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

		start();
	}

	@Override
	public void run()
	{
		long time1 = System.nanoTime();

		this.sealed = true;
		this.looping = true;
		this.loopThrough(this.head.clone().translate(new BlockVec3(0, 1, 0)),ForgeDirection.UNKNOWN);

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
			this.loopThrough(this.head.clone().translate(new BlockVec3(0, 1, 0)),ForgeDirection.UNKNOWN);
		}

		long time2 = System.nanoTime();

		if (this.sealed)
		{
			for (BlockVec3 checkedVec : this.checked)
			{
				int blockID = checkedVec.getBlockID(this.world);

				if (blockID == 0)
				{
					GCCoreTickHandlerServer.scheduleNewBlockChange(world.provider.dimensionId, new ScheduledBlockChange(checkedVec, breatheableAirID, 0, 3));
				}
			}
		}
		else
		{
			this.checked.clear();
			this.loopThroughD(this.head.clone().translate(new BlockVec3(0, 1, 0)),ForgeDirection.UNKNOWN);

			for (BlockVec3 checkedVec : this.checked)
			{
				int blockID = checkedVec.getBlockID(this.world);

				if (blockID == breatheableAirID)
				{
					GCCoreTickHandlerServer.scheduleNewBlockChange(world.provider.dimensionId, new ScheduledBlockChange(checkedVec, 0, 0, 3));
				}
			}
		}

		TileEntity headTile = this.head.getTileEntity(this.world);

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
			FMLLog.info("Oxygen Sealer Check Completed at x" + this.head.x + " y" + this.head.y + " z" + this.head.z);
			FMLLog.info("   Sealed: " + this.sealed);
			FMLLog.info("   Loop Time taken: " + (time2 - time1) / 1000000.0D + "ms");
			FMLLog.info("   Place Time taken: " + (time3 - time2) / 1000000.0D + "ms");
			FMLLog.info("   Total Time taken: " + (time3 - time1) / 1000000.0D + "ms");
			FMLLog.info("   Found: " + this.sealers.size() + " sealers");
			FMLLog.info("   Looped through: " + this.checked.size() + " blocks");
		}
	}

	private void loopThroughD(BlockVec3 vec, ForgeDirection dirIn)
	{
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if (dir==dirIn) continue;
			BlockVec3 sideVec = vec.clone().modifyPositionFromSide(dir,1);
			//VecDirPair pair = new VecDirPair(sideVec, dir);

			if (!this.checked.contains(sideVec))
			{
				this.checked.add(sideVec);

				if (this.breathableAirAdjacent(sideVec))
				{
					this.loopThroughD(sideVec,dir.getOpposite());
				}
			}
		}
	}

	private void loopThrough(BlockVec3 vec, ForgeDirection dirIn)
	{
		if (this.sealed)
		{
			if (this.checkCount > 0)
			{
				for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
				{
					if (dir==dirIn) continue;
					BlockVec3 sideVec = vec.clone().modifyPositionFromSide(dir,1);
					//VecDirPair pair = new VecDirPair(sideVec, dir);

					if (!this.checked.contains(sideVec))
					{
						this.checkCount--;
						this.checked.add(sideVec);
						
						int id=sideVec.getBlockID(this.world);
						if (id==0 || id==breatheableAirID || WorldUtil.canBlockPass(this.world, id, sideVec.getBlockMetadata(this.world), new VecDirPair(sideVec, dir)))
						{
							this.loopThrough(sideVec,dir.getOpposite());
						}

						TileEntity tileAtVec = sideVec.getTileEntity(this.world);

						if (tileAtVec instanceof GCCoreTileEntityOxygenSealer)
						{
							GCCoreTileEntityOxygenSealer sealer = (GCCoreTileEntityOxygenSealer) tileAtVec;

							if (sealer.active)
							{
								 if (!this.sealers.contains(tileAtVec)) this.sealers.add(sealer);
							}
						}
					}
				}
			}
			else
			{
				for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
				{
					if (dir==dirIn) continue;
					BlockVec3 sideVec = vec.clone().modifyPositionFromSide(dir,1);

					int id=sideVec.getBlockID(this.world);
					//This is the bugfix: adding check for canBlockPass (this is testing the extremities of the sealable area for a good seal)
					if ((id == 0 || id == breatheableAirID || WorldUtil.canBlockPass(this.world, id, sideVec.getBlockMetadata(this.world), new VecDirPair(sideVec, dir))) && !this.checked.contains(new VecDirPair(sideVec, dir)))
					{
						this.sealed = false;
						break;
					}
				}
			}
		}
	}

	private boolean breathableAirAdjacent(BlockVec3 vec)
	{
		int x=vec.x;
		int y=vec.y;
		int z=vec.z;
        if (world.getBlockId(x, y-1, z) == breatheableAirID) return true;
        if (world.getBlockId(x, y+1, z) == breatheableAirID) return true;
        if (world.getBlockId(x, y, z-1) == breatheableAirID) return true;
        if (world.getBlockId(x, y, z+1) == breatheableAirID) return true;
        if (world.getBlockId(x-1, y, z) == breatheableAirID) return true;
        if (world.getBlockId(x+1, y, z) == breatheableAirID) return true;
		return false;
	}
	/* **
	 * ** Speedup project for OxygenPressureProtocol and ThreadFindSeal
	 * **
	 * ** Steps completed so far:
	 * ** - conversion of Vector3 to integer arithmetic,
	 * ** - speedup of breatheableAirAdjacent() which is called the most in the inner loops
	 * ** - minor changes to some method calls to reduce call stacking
	 * ** - more changes to checked() to reduce call stacking
	 * ** - remove some unnecessary double testing in if() statements
	 * ** - loopThrough now doesn't go backwards down the direction it came in on (small change which makes quite a difference)
	 * ** 
	 */
}
