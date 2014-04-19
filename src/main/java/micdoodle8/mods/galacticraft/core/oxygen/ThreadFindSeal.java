package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
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
	public BlockVec3 head;
	public AtomicBoolean sealedFinal = new AtomicBoolean();
	private boolean sealed;
	public List<TileEntityOxygenSealer> sealers;
	public List<BlockVec3> oxygenReliantBlocks;
	public HashSet<BlockVec3> checked;
	public HashSet<BlockVec3> partiallySealableChecked;
	public int checkCount;
	public static AtomicBoolean anylooping = new AtomicBoolean();
	public AtomicBoolean looping = new AtomicBoolean();
	private HashMap<BlockVec3, TileEntityOxygenSealer> sealersAround = new HashMap<BlockVec3, TileEntityOxygenSealer>();
	private Block breatheableAirID;
	private Block oxygenSealerID;
	private List<BlockVec3> currentLayer = new LinkedList<BlockVec3>();
	private List<BlockVec3> nextLayer;
	private List<BlockVec3> airToReplace = new LinkedList<BlockVec3>();
	private List<BlockVec3> breatheableToReplace = new LinkedList<BlockVec3>();
	private List<TileEntityOxygenSealer> otherSealers = new LinkedList<TileEntityOxygenSealer>();

	public ThreadFindSeal(TileEntityOxygenSealer sealer)
	{
		this(sealer.getWorldObj(), new BlockVec3(sealer).translate(0, 1, 0), sealer.getFindSealChecks(), new ArrayList<TileEntityOxygenSealer>(Arrays.asList(sealer)));
	}

	@SuppressWarnings("unchecked")
	public ThreadFindSeal(World world, BlockVec3 head, int checkCount, List<TileEntityOxygenSealer> sealers)
	{
		super("GC Sealer Roomfinder Thread");
		ThreadFindSeal.anylooping.set(true);
		this.world = world;
		this.head = head;
		this.checkCount = checkCount;
		this.sealers = sealers;
		this.oxygenReliantBlocks = new ArrayList<BlockVec3>();
		this.checked = new HashSet<BlockVec3>();
		this.partiallySealableChecked = new HashSet<BlockVec3>();
		this.breatheableAirID = GCBlocks.breatheableAir;
		this.oxygenSealerID = GCBlocks.oxygenSealer;

		if (this.isAlive())
		{
			this.interrupt();
		}

		for (TileEntity tile : new ArrayList<TileEntity>(world.loadedTileEntityList))
		{
			if (tile instanceof TileEntityOxygenSealer && tile.getDistanceFrom(head.x, head.y, head.z) < 2048 * 2048)
			{
				this.sealersAround.put(new BlockVec3(tile.xCoord, tile.yCoord, tile.zCoord), (TileEntityOxygenSealer) tile);
			}
		}

		this.looping.set(true);
		this.start();
	}

	@Override
	public void run()
	{
		long time1 = System.nanoTime();

		this.sealed = true;
		this.checked.add(this.head);
		this.currentLayer.clear();
		this.nextLayer = new LinkedList<BlockVec3>();
		this.airToReplace.clear();
		if (this.checkCount > 0)
		{
			this.currentLayer.add(this.head.clone());
			if (this.head.x < -29990000 || this.head.z < -29990000 || this.head.x >= 29990000 || this.head.z >= 29990000)
			{
				this.doLayerNearMapEdge();
			}
			else
			{
				this.doLayer();
			}
		}
		else
		{
			this.sealed = false;
		}

		long time2 = System.nanoTime();

		if (this.sealed)
		{
			if (!this.airToReplace.isEmpty())
			{
				List<ScheduledBlockChange> changeList = new LinkedList<ScheduledBlockChange>();
				// Note: it is faster to use a LinkedList than an ArrayList when
				// adding a lot of small elements to a list
				for (BlockVec3 checkedVec : this.airToReplace)
				{
					changeList.add(new ScheduledBlockChange(checkedVec.clone(), this.breatheableAirID, 0, 3));
				}
				TickHandlerServer.scheduleNewBlockChange(this.world.provider.dimensionId, changeList);
			}
		}
		else
		{
			this.checked.clear();
			this.breatheableToReplace.clear();
			this.otherSealers.clear();
			// loopThroughD will mark breatheableAir blocks for change as it
			// finds them, also searches for unchecked sealers
			this.currentLayer.clear();
			this.currentLayer.add(this.head.clone());
			this.nextLayer = new LinkedList<BlockVec3>();
			this.loopThroughD();

			if (!this.otherSealers.isEmpty())
			{
				// OtherSealers will have members if the space to be made
				// unbreathable actually still has an unchecked sealer in it
				List<TileEntityOxygenSealer> sealersDone = this.sealers;
				for (TileEntityOxygenSealer otherSealer : this.otherSealers)
				{
					// If it hasn't already been counted, need to check the
					// other sealer immediately in case it can keep the space
					// sealed
					if (!sealersDone.contains(otherSealer) && otherSealer.getFindSealChecks() > 0)
					{
						BlockVec3 newhead = new BlockVec3(otherSealer).translate(0, 1, 0);
						this.sealed = true;
						this.checkCount = otherSealer.getFindSealChecks();
						this.sealers = new LinkedList<TileEntityOxygenSealer>();
						this.sealers.add(otherSealer);
						this.checked.clear();
						this.checked.add(newhead);
						this.currentLayer.clear();
						this.nextLayer = new LinkedList<BlockVec3>();
						this.airToReplace.clear();
						this.currentLayer.add(newhead.clone());
						if (newhead.x < -29990000 || newhead.z < -29990000 || newhead.x >= 29990000 || newhead.z >= 29990000)
						{
							this.doLayerNearMapEdge();
						}
						else
						{
							this.doLayer();
						}

						// If found a sealer which can still seal the space, it
						// should take over as head
						if (this.sealed)
						{
							if (ConfigManagerCore.enableDebug)
							{
								FMLLog.info("Oxygen Sealer replacing head at x" + this.head.x + " y" + (this.head.y - 1) + " z" + this.head.z);
							}
							TileEntityOxygenSealer oldHead = sealersDone.get(0);
							if (!this.sealers.contains(oldHead))
							{
								this.sealers.add(oldHead);
							}
							this.head = newhead.clone();
							otherSealer.threadSeal = this;
							otherSealer.stopSealThreadCooldown = 50;
							break;
						}
					}
					// Restore sealers to what it was, if this search did not
					// result in a seal
					this.sealers = sealersDone;
				}
			}

			if (this.sealed == false && !this.breatheableToReplace.isEmpty())
			{
				List<ScheduledBlockChange> changeList = new LinkedList<ScheduledBlockChange>();
				// Note: a LinkedList is faster than an ArrayList when adding a
				// lot of small elements to a list
				for (BlockVec3 checkedVec : this.breatheableToReplace)
				{
					changeList.add(new ScheduledBlockChange(checkedVec.clone(), Blocks.air, 0, 3));
				}
				TickHandlerServer.scheduleNewBlockChange(this.world.provider.dimensionId, changeList);
			}
		}

		// Set any sealers found which are not the head sealer, not to run their
		// own seal checks for a while
		// (The player can control which is the head sealer in a space by
		// enabling just that one and disabling all the others)
		TileEntityOxygenSealer headSealer = this.sealersAround.get(this.head.translate(0, -1, 0));

		for (TileEntityOxygenSealer sealer : this.sealers)
		{
			// Sealers which are not the head sealer: put them on cooldown so
			// the inactive ones don't start their own threads and so unseal
			// this volume
			// and update threadSeal reference of all sealers found (even the
			// inactive ones)
			if (sealer != headSealer && headSealer != null)
			{
				sealer.threadSeal = this;
				sealer.stopSealThreadCooldown = headSealer.stopSealThreadCooldown + 51;
			}
		}

		// Help the Java garbage collector by clearing big data structures which
		// are no longer needed
		this.airToReplace.clear();
		this.breatheableToReplace.clear();

		this.looping.set(false);

		if (ConfigManagerCore.enableDebug)
		{
			long time3 = System.nanoTime();
			FMLLog.info("Oxygen Sealer Check Completed at x" + this.head.x + " y" + this.head.y + " z" + this.head.z);
			FMLLog.info("   Sealed: " + this.sealed);
			FMLLog.info("   Loop Time taken: " + (time2 - time1) / 1000000.0D + "ms");
			FMLLog.info("   Place Time taken: " + (time3 - time2) / 1000000.0D + "ms");
			FMLLog.info("   Total Time taken: " + (time3 - time1) / 1000000.0D + "ms");
			FMLLog.info("   Found: " + this.sealers.size() + " sealers");
			FMLLog.info("   Looped through: " + this.checked.size() + " blocks");
		}
		this.checked.clear();
		this.sealedFinal.set(this.sealed);
		ThreadFindSeal.anylooping.set(false);
	}

	private void loopThroughD()
	{
		while (this.currentLayer.size() > 0)
		{
			for (BlockVec3 vec : this.currentLayer)
			{
				for (int side = 0; side < 6; side++)
				{
					BlockVec3 sideVec = vec.newVecSide(side);

					if (!this.checked.contains(sideVec))
					{
						this.checked.add(sideVec);
						Block id = sideVec.getBlockID(this.world);
						if (id == this.breatheableAirID)
						{
							this.breatheableToReplace.add(sideVec);
							// Only follow paths with adjacent breatheableAir
							// blocks - this now can't jump through walls etc
							this.nextLayer.add(sideVec);
						}
						else if (id == this.oxygenSealerID)
						{
							TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);
							if (sealer != null)
							{
								if (!this.sealers.contains(sealer))
								{
									this.otherSealers.add(sealer);
								}
							}
						}
					}
				}
			}

			// Set up the next layer as current layer for the while loop
			this.currentLayer = this.nextLayer;
			this.nextLayer = new LinkedList<BlockVec3>();
		}
	}

	private void doLayer()
	{
		LAYERLOOP:
		while (this.sealed && this.currentLayer.size() > 0)
		{
			for (BlockVec3 vec : this.currentLayer)
			{
				if (this.partiallySealableChecked.isEmpty() || !this.partiallySealableChecked.contains(vec))
				{
					for (int side = 0; side < 6; side++)
					{
						// The sides 0 to 5 correspond with the ForgeDirections
						// but saves a bit of time not to call ForgeDirection
						BlockVec3 sideVec = vec.newVecSide(side);

						if (!this.checked.contains(sideVec))
						{
							if (this.checkCount > 0)
							{
								this.checkCount--;
								this.checked.add(sideVec);

								Block id = sideVec.getBlockIDsafe(this.world);
								// NB getBlockIDsafe will return -1, not 0, if
								// the y coordinates are out of bounds - so this
								// won't keep iterating into the void or the
								// stratosphere
								if (id == Blocks.air)
								{
									this.nextLayer.add(sideVec);
									this.airToReplace.add(sideVec);
									// Using airToReplace will save time later
									// on in run(), if the volume is sealed
									// More importantly, if the volume is sealed
									// and has no blocks with id==0
									// (i.e. a stable sealed volume) then it
									// will save a lot of time later in the
									// thread
								}
								else if (id == null)
								{
									// Broken through to the void or the
									// stratosphere (above y==255) - set
									// unsealed and abort
									this.checkCount = 0;
									this.sealed = false;
									break LAYERLOOP;
								}
								else if (id == this.breatheableAirID || this.canBlockPassAir(id, sideVec, side))
								{
									this.nextLayer.add(sideVec);
								}
								else if (id == this.oxygenSealerID)
								{
									TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);

									if (sealer != null)
									{
										if (!this.sealers.contains(sealer))
										{
											this.sealers.add(sealer);
											this.checkCount += sealer.getFindSealChecks();
										}
									}
								}
							}
							else if (this.sealed)
							{
								Block id = sideVec.getBlockIDsafe(this.world);
								// id == -1 means the void or height y>255, both
								// of which are unsealed obviously
								if (id == Blocks.air || id == this.breatheableAirID || id == null || this.canBlockPassAir(id, sideVec, side))
								{
									this.sealed = false;
									break LAYERLOOP;
								}
							}
						}
					}
				}
				else
				// special case: repeat all the same code for a partially
				// sealable block
				{
					Block block = vec.getBlockIDsafe(this.world);
					if (block instanceof IPartialSealableBlock)
					{
						IPartialSealableBlock partialBlock = (IPartialSealableBlock) block;
						for (int side = 0; side < 6; side++)
						{
							if (!partialBlock.isSealed(this.world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side ^ 1))) // opposite
																																	// side,
																																	// testing
																																	// issealed
																																	// from
																																	// inside
																																	// out
							{
								BlockVec3 sideVec = vec.newVecSide(side);
								if (!this.checked.contains(sideVec))
								{
									if (this.checkCount > 0)
									{
										this.checkCount--;
										this.checked.add(sideVec);
										Block id = sideVec.getBlockIDsafe(this.world);
										if (id == Blocks.air)
										{
											this.nextLayer.add(sideVec);
											this.airToReplace.add(sideVec);
										}
										else if (id == null)
										{
											this.checkCount = 0;
											this.sealed = false;
											break LAYERLOOP;
										}
										else if (id == this.breatheableAirID || this.canBlockPassAir(id, sideVec, side))
										{
											this.nextLayer.add(sideVec);
										}
										else if (id == this.oxygenSealerID)
										{
											TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);
											if (sealer != null)
											{
												if (!this.sealers.contains(sealer))
												{
													this.sealers.add(sealer);
													this.checkCount += sealer.getFindSealChecks();
												}
											}
										}
									}
									else if (this.sealed)
									{
										Block id = sideVec.getBlockIDsafe(this.world);
										if (id == Blocks.air || id == this.breatheableAirID || id == null || this.canBlockPassAir(id, sideVec, side))
										{
											this.sealed = false;
											break LAYERLOOP;
										}
									}
								}
							}
						}
					}
				}
			}

			// Is there a further layer of air/permeable blocks to test?
			this.currentLayer = this.nextLayer;
			this.nextLayer = new LinkedList<BlockVec3>();
		}
	}

	private void doLayerNearMapEdge()
	{
		LAYERLOOPNME:
		while (this.sealed && this.currentLayer.size() > 0)
		{
			for (BlockVec3 vec : this.currentLayer)
			{
				for (int side = 0; side < 6; side++)
				{
					// The sides 0 to 5 correspond with the ForgeDirections but
					// saves a bit of time not to call ForgeDirection
					BlockVec3 sideVec = vec.newVecSide(side);

					if (!this.checked.contains(sideVec))
					{
						if (this.checkCount > 0)
						{
							this.checkCount--;
							this.checked.add(sideVec);

							Block id = sideVec.getBlockID(this.world); // This is
																		// a
																		// slower
																		// operation
																		// as it
																		// involves
																		// map
																		// edge
																		// checks
							// NB getBlockID will return -1, not 0, if any of
							// the coordinates are out of bounds - so this won't
							// keep iterating into the void or the stratosphere
							// or off the map edge
							if (id == Blocks.air)
							{
								this.nextLayer.add(sideVec);
								this.airToReplace.add(sideVec);
								// This will save time later on in run(), if the
								// volume is sealed
								// More importantly, if the volume is sealed and
								// has no blocks with id==0
								// (i.e. a stable sealed volume) then it will
								// save a lot of time later in run()
							}
							else if (id == null)
							{
								// We've hit the void or the stratosphere or the
								// world edge - abort
								this.checkCount = 0;
								this.sealed = false;
								break LAYERLOOPNME;
							}
							else if (id == this.breatheableAirID || this.canBlockPassAir(id, sideVec, side))
							{
								this.nextLayer.add(sideVec);
							}
							else if (id == this.oxygenSealerID)
							{
								TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);

								if (sealer != null)
								{
									if (!this.sealers.contains(sealer))
									{
										this.sealers.add(sealer);
										this.checkCount += sealer.getFindSealChecks();
									}
								}
							}
						}
						else if (this.sealed)
						{
							Block id = sideVec.getBlockID(this.world);
							// id == -1 means the void or height y>255, both of
							// which are unsealed obviously
							if (id == Blocks.air || id == this.breatheableAirID || id == null || this.canBlockPassAir(id, sideVec, side))
							{
								this.sealed = false;
								break LAYERLOOPNME;
							}
						}
					}
				}
			}

			// Is there a further layer of air/permeable blocks to test?
			this.currentLayer = this.nextLayer;
			this.nextLayer = new LinkedList<BlockVec3>();
		}
	}

	private boolean canBlockPassAir(Block block, BlockVec3 vec, int side)
	{
		int id = Block.getIdFromBlock(block);
		
		if (OxygenPressureProtocol.vanillaPermeableBlocks.contains(id))
		{
			return true;
		}

		if (!block.isOpaqueCube()) // Note this will NPE if id==0, so don't call
									// this with id==0
		{
			if (block instanceof IPartialSealableBlock)
			{
				if (((IPartialSealableBlock) block).isSealed(this.world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side)))
				{
					// If a partial block checks as solid, allow it to be tested
					// again from other directions
					// This won't cause an endless loop, because the block won't
					// be included in nextLayer if it checks as solid
					this.checked.remove(vec);
					this.checkCount--;
					return false;
				}
				this.partiallySealableChecked.add(vec);
				return true;
			}

			if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(id) && OxygenPressureProtocol.nonPermeableBlocks.get(id).contains(vec.getBlockMetadata(this.world)))
			{
				return false;
			}

			return true;
		}

		return false;
	}

}
