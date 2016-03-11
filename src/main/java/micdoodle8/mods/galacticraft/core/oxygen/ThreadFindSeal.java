package micdoodle8.mods.galacticraft.core.oxygen;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.BlockUnlitTorch;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadFindSeal
{
    public AtomicBoolean sealedFinal = new AtomicBoolean();
    public static AtomicBoolean anylooping = new AtomicBoolean();
    public AtomicBoolean looping = new AtomicBoolean();

    private World world;
    private BlockVec3 head;
    private boolean sealed;
    private List<TileEntityOxygenSealer> sealers;
    public HashSet<BlockVec3> checked;
    private int checkCount;
    private HashMap<BlockVec3, TileEntityOxygenSealer> sealersAround;
    private List<BlockVec3> currentLayer;
    private List<BlockVec3> airToReplace;
    private List<BlockVec3> breatheableToReplace;
    private List<BlockVec3> airToReplaceBright;
    private List<BlockVec3> breatheableToReplaceBright;
    private List<BlockVec3> ambientThermalTracked;
    private List<TileEntityOxygenSealer> otherSealers;
    private List<BlockVec3> torchesToUpdate;
    private boolean foundAmbientThermal;
    public List<BlockVec3> leakTrace;

    public ThreadFindSeal(TileEntityOxygenSealer sealer)
    {
        this(sealer.getWorldObj(), new BlockVec3(sealer).translate(0, 1, 0), sealer.getFindSealChecks(), new ArrayList<TileEntityOxygenSealer>(Arrays.asList(sealer)));
    }

    @SuppressWarnings("unchecked")
    public ThreadFindSeal(World world, BlockVec3 head, int checkCount, List<TileEntityOxygenSealer> sealers)
    {
        this.world = world;
        this.head = head;
        this.checkCount = checkCount;
        this.sealers = sealers;
        this.foundAmbientThermal = false;
        this.checked = new HashSet<BlockVec3>();
        this.torchesToUpdate = new LinkedList<BlockVec3>();

        this.sealersAround = TileEntityOxygenSealer.getSealersAround(world, head.x, head.y, head.z, 1024 * 1024);

        //If called by a sealer test the head block and if partiallySealable mark its sides done as required
        if (!sealers.isEmpty())
        {
            if (checkCount > 0)
            {
                Block headBlock = head.getBlockID(this.world);
                if (headBlock != null && !(headBlock.isAir(world, head.x, head.y, head.z)))
                {
                    this.canBlockPassAirCheck(headBlock, this.head, 1);
                    //reset the checkCount as canBlockPassAirCheck might have changed it
                    this.checkCount = checkCount;
                }
            }

            this.looping.set(true);

//            if (ConfigManagerCore.enableSealerMultithreading)
//            {
//                new ThreadedFindSeal();
//            }
//            else
//            {
                this.check();
//            }
        }
        else
        //If not called by a sealer, it's a breathable air edge check
        {
            //Run this in the main thread
            this.check();
        }
    }

    //Multi-threaded version of the code for sealer updates (not for edge checks).
    public class ThreadedFindSeal extends Thread
    {
        public ThreadedFindSeal()
        {
            super("GC Sealer Roomfinder Thread");
            ThreadFindSeal.anylooping.set(true);

            if (this.isAlive())
            {
                this.interrupt();
            }

            //Run this as a separate thread
            this.start();
        }

        @Override
        public void run()
        {
            ThreadFindSeal.this.check();
            ThreadFindSeal.anylooping.set(false);
        }
    }

    public void check()
    {
        long time1 = System.nanoTime();

        this.sealed = true;
        TileEntity tile = this.head.getTileEntityOnSide(world, ForgeDirection.DOWN);
        this.foundAmbientThermal = tile instanceof TileEntityOxygenSealer && ((TileEntityOxygenSealer) tile).thermalControlEnabled();
        this.checked.add(this.head.clone());
        this.currentLayer = new LinkedList<BlockVec3>();
        this.airToReplace = new LinkedList<BlockVec3>();
        this.airToReplaceBright = new LinkedList<BlockVec3>();
        this.ambientThermalTracked = new LinkedList<BlockVec3>();

        if (this.checkCount > 0)
        {
            this.currentLayer.add(this.head);
            if (this.head.x < -29990000 || this.head.z < -29990000 || this.head.x >= 29990000 || this.head.z >= 29990000)
            {
                Block b = this.head.getBlockID_noChunkLoad(this.world);
            	if (Blocks.air == b)
                {
                    this.airToReplace.add(this.head.clone());
                }
            	else if (b == GCBlocks.brightAir)
                {
                    this.airToReplaceBright.add(this.head.clone());
                }
                this.doLayerNearMapEdge();
            }
            else
            {
                Block headblock = this.head.getBlockIDsafe_noChunkLoad(this.world);
                if (Blocks.air == headblock)
                {
                    this.airToReplace.add(this.head.clone());
                }
                else if (headblock == GCBlocks.brightAir)
                {
                    this.airToReplaceBright.add(this.head.clone());
                }
                this.doLayer();
            }
        }
        else
        {
            this.sealed = false;
        }

        long time2 = System.nanoTime();

        //Can only be properly sealed if there is at least one sealer here (on edge check)
        if (this.sealers.isEmpty())
        {
            this.sealed = false;
        }

        if (this.sealed)
        {
        	this.makeSealGood(this.foundAmbientThermal);
        	this.leakTrace = null;
        }
        else
        {
            HashSet checkedSave = this.checked;
            this.checked = new HashSet<BlockVec3>();
            this.breatheableToReplace = new LinkedList<BlockVec3>();
            this.breatheableToReplaceBright = new LinkedList<BlockVec3>();
            this.otherSealers = new LinkedList<TileEntityOxygenSealer>();
            // unseal() will mark breatheableAir blocks for change as it
            // finds them, also searches for unchecked sealers
            this.currentLayer.clear();
            this.currentLayer.add(this.head);
            this.torchesToUpdate.clear();
            if (this.head.x < -29990000 || this.head.z < -29990000 || this.head.x >= 29990000 || this.head.z >= 29990000)
            {
                this.unsealNearMapEdge();
            }
            else
            {
                this.unseal();
            }

            if (!this.otherSealers.isEmpty())
            {
                // OtherSealers will have members if the space to be made
                // unbreathable actually still has an unchecked sealer in it
                List<TileEntityOxygenSealer> sealersSave = this.sealers;
                List<BlockVec3> torchesSave = this.torchesToUpdate;
                List<TileEntityOxygenSealer> sealersDone = new ArrayList();
                sealersDone.addAll(this.sealers);
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
                        if (otherSealer.thermalControlEnabled())
                        {
                            foundAmbientThermal = true;
                        }
                        this.checked = new HashSet<BlockVec3>();
                        this.checked.add(newhead);
                        this.currentLayer.clear();
                        this.airToReplace.clear();
                        this.airToReplaceBright.clear();
                        this.torchesToUpdate = new LinkedList<BlockVec3>();
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
                                GCLog.info("Oxygen Sealer replacing head at x" + this.head.x + " y" + (this.head.y - 1) + " z" + this.head.z);
                            }
                            if (!sealersSave.isEmpty())
                            {
                                TileEntityOxygenSealer oldHead = sealersSave.get(0);
                                if (!this.sealers.contains(oldHead))
                                {
                                    this.sealers.add(oldHead);
                                    if (oldHead.thermalControlEnabled())
                                    {
                                        foundAmbientThermal = true;
                                    }
                                }
                            }
                            this.head = newhead.clone();
                            otherSealer.threadSeal = this;
                            otherSealer.stopSealThreadCooldown = 75 + TileEntityOxygenSealer.countEntities;
                            checkedSave.addAll(this.checked);
                            break;
                        }
                        else
                        {
                            sealersDone.addAll(this.sealers);
                        }

                        checkedSave.addAll(this.checked);
                    }
                }

                // Restore sealers to what it was, if this search did not
                // result in a seal
                if (!this.sealed)
                {
                    this.sealers = sealersSave;
                    this.torchesToUpdate = torchesSave;
                }
                else
                {
                    //If the second search sealed the area, there may also be air or torches to update
                	this.makeSealGood(foundAmbientThermal);
                }
            }
            this.checked = checkedSave;

            if (!this.sealed)
            {
                if (this.head.getBlockID(this.world) == GCBlocks.breatheableAir)
                {
                    this.breatheableToReplace.add(this.head);
                }
                if (this.head.getBlockID(this.world) == GCBlocks.brightBreatheableAir)
                {
                    this.breatheableToReplaceBright.add(this.head);
                }
                this.makeSealBad();
            }
            else
            	this.leakTrace = null;
        }

        // Set any sealers found which are not the head sealer, not to run their
        // own seal checks for a while
        // (The player can control which is the head sealer in a space by
        // enabling just that one and disabling all the others)
        TileEntityOxygenSealer headSealer = this.sealersAround.get(this.head.clone().translate(0, -1, 0));

        // If it is sealed, cooldown can be extended as frequent checks are not needed
        if (headSealer != null)
        {
            headSealer.stopSealThreadCooldown += 75;
        }

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

        this.looping.set(false);

        if (ConfigManagerCore.enableDebug)
        {
            long time3 = System.nanoTime();
            GCLog.info("Oxygen Sealer Check Completed at x" + this.head.x + " y" + this.head.y + " z" + this.head.z);
            GCLog.info("   Sealed: " + this.sealed);
            GCLog.info("   Loop Time taken: " + (time2 - time1) / 1000000.0D + "ms");
            GCLog.info("   Place Time taken: " + (time3 - time2) / 1000000.0D + "ms");
            GCLog.info("   Total Time taken: " + (time3 - time1) / 1000000.0D + "ms");
            GCLog.info("   Found: " + this.sealers.size() + " sealers");
            GCLog.info("   Looped through: " + this.checked.size() + " blocks");
        }

        //Help the Garbage Collector
//        this.sealers.clear();
//        this.checked.clear();
//        this.sealersAround.clear();
//        this.currentLayer.clear();
//        this.airToReplace.clear();
//        this.airToReplaceBright.clear();
//        this.torchesToUpdate.clear();
//        this.ambientThermalTracked.clear();
//        if (this.breatheableToReplace != null) this.breatheableToReplace.clear();
//        if (this.breatheableToReplaceBright != null) this.breatheableToReplaceBright.clear();
//        if (this.otherSealers != null) this.otherSealers.clear();

        this.sealedFinal.set(this.sealed);
    }

    private void makeSealGood(boolean ambientThermal)
    {
        if (!this.airToReplace.isEmpty() || !this.airToReplaceBright.isEmpty() || !ambientThermalTracked.isEmpty())
        {
            List<ScheduledBlockChange> changeList = new LinkedList<ScheduledBlockChange>();
            Block breatheableAirID = GCBlocks.breatheableAir;
            int metadata = 0;
            if (ambientThermal)
            {
                metadata = 1;
            }
            for (BlockVec3 checkedVec : this.airToReplace)
            {
                //No block update for performance reasons; deal with unlit torches separately
                changeList.add(new ScheduledBlockChange(checkedVec.clone(), breatheableAirID, metadata, 2));
            }
            for (BlockVec3 checkedVec : this.airToReplaceBright)
            {
                changeList.add(new ScheduledBlockChange(checkedVec.clone(), GCBlocks.brightBreatheableAir, metadata, 2));
            }
            for (BlockVec3 checkedVec : this.ambientThermalTracked)
            {
                changeList.add(new ScheduledBlockChange(checkedVec.clone(), checkedVec.getBlock(world), metadata, 3));
            }

            TickHandlerServer.scheduleNewBlockChange(this.world.provider.dimensionId, changeList);
        }
        if (!this.torchesToUpdate.isEmpty())
        {
            TickHandlerServer.scheduleNewTorchUpdate(this.world.provider.dimensionId, this.torchesToUpdate);
        }
	}

    private void makeSealBad()
    {
        if (!this.breatheableToReplace.isEmpty() || !this.breatheableToReplaceBright.isEmpty())
        {
            List<ScheduledBlockChange> changeList = new LinkedList<ScheduledBlockChange>();
            for (BlockVec3 checkedVec : this.breatheableToReplace)
            {
                changeList.add(new ScheduledBlockChange(checkedVec.clone(), Blocks.air, 0, 2));
            }
            for (BlockVec3 checkedVec : this.breatheableToReplaceBright)
            {
                changeList.add(new ScheduledBlockChange(checkedVec.clone(), GCBlocks.brightAir, 0, 2));
            }
            TickHandlerServer.scheduleNewBlockChange(this.world.provider.dimensionId, changeList);
        }
        if (!this.torchesToUpdate.isEmpty())
        {
            TickHandlerServer.scheduleNewTorchUpdate(this.world.provider.dimensionId, this.torchesToUpdate);
        }
    }
    
	private void unseal()
    {
        //Local variables are fractionally faster than statics
        Block breatheableAirID = GCBlocks.breatheableAir;
        Block breatheableAirIDBright = GCBlocks.brightBreatheableAir;
        Block oxygenSealerID = GCBlocks.oxygenSealer;
        Block fireBlock = Blocks.fire;
        HashSet<BlockVec3> checkedLocal = this.checked;
        LinkedList nextLayer = new LinkedList<BlockVec3>();
        World world = this.world;
        int side, bits;

        while (this.currentLayer.size() > 0)
        {
            for (BlockVec3 vec : this.currentLayer)
            {
                side = 0;
                bits = vec.sideDoneBits;
                do
                {
                    if ((bits & (1 << side)) == 0)
                    {
                        BlockVec3 sideVec = vec.newVecSide(side);

                        if (!checkedLocal.contains(sideVec))
                        {
                            Block id = sideVec.getBlockIDsafe_noChunkLoad(world);

                            if (id == breatheableAirID)
                            {
                                this.breatheableToReplace.add(sideVec);
                                nextLayer.add(sideVec);
                                checkedLocal.add(sideVec);
                            }
                            else if (id == breatheableAirIDBright)
                            {
                                this.breatheableToReplaceBright.add(sideVec);
                                nextLayer.add(sideVec);
                                checkedLocal.add(sideVec);
                            }
                            else if (id == fireBlock)
                            {
                                this.breatheableToReplace.add(sideVec);
                                nextLayer.add(sideVec);
                                checkedLocal.add(sideVec);
                            }
                            else if (id == oxygenSealerID)
                            {
                                TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);

                                if (sealer != null && !this.sealers.contains(sealer))
                                {
                                    if (side == 0)
                                    {
                                        //Accessing the vent side of the sealer, so add it
                                        this.otherSealers.add(sealer);
                                        checkedLocal.add(sideVec);
                                    }
                                    //if side is not 0, do not add to checked so can be rechecked from other sides
                                }
                                else
                                {
                                    checkedLocal.add(sideVec);
                                }
                            }
                            else
                            {
                                checkedLocal.add(sideVec);
                                if (id != null && Blocks.air != id && id != GCBlocks.brightAir && this.canBlockPassAirCheck(id, sideVec, side))
                                {
                                    //Look outbound through partially sealable blocks in case there is breatheableAir to clear beyond
                                    nextLayer.add(sideVec);
                                }
                            }
                        }
                    }
                    side++;
                }
                while (side < 6);
            }

            // Set up the next layer as current layer for the while loop
            this.currentLayer = nextLayer;
            nextLayer = new LinkedList<BlockVec3>();
        }
    }

    private void unsealNearMapEdge()
    {
        //Local variables are fractionally faster than statics
        Block breatheableAirID = GCBlocks.breatheableAir;
        Block breatheableAirIDBright = GCBlocks.brightBreatheableAir;
        Block oxygenSealerID = GCBlocks.oxygenSealer;
        Block fireBlock = Blocks.fire;
        HashSet<BlockVec3> checkedLocal = this.checked;
        LinkedList nextLayer = new LinkedList<BlockVec3>();
        int bits;

        while (this.currentLayer.size() > 0)
        {
            for (BlockVec3 vec : this.currentLayer)
            {
                bits = vec.sideDoneBits;
                for (int side = 0; side < 6; side++)
                {
                    if ((bits & (1 << side)) == 1)
                    {
                        continue;
                    }
                    BlockVec3 sideVec = vec.newVecSide(side);

                    if (!checkedLocal.contains(sideVec))
                    {
                        Block id = sideVec.getBlockID_noChunkLoad(this.world);

                        if (id == breatheableAirID)
                        {
                            this.breatheableToReplace.add(sideVec);
                            nextLayer.add(sideVec);
                            checkedLocal.add(sideVec);
                        }
                        else if (id == breatheableAirIDBright)
                        {
                            this.breatheableToReplaceBright.add(sideVec);
                            nextLayer.add(sideVec);
                            checkedLocal.add(sideVec);
                        }
                        else if (id == fireBlock)
                        {
                            nextLayer.add(sideVec);
                            this.breatheableToReplace.add(sideVec);
                            checkedLocal.add(sideVec);
                        }
                        else if (id == oxygenSealerID)
                        {
                            TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);

                            if (sealer != null && !this.sealers.contains(sealer))
                            {
                                if (side == 0)
                                {
                                    //Accessing the vent side of the sealer, so add it
                                    this.otherSealers.add(sealer);
                                    checkedLocal.add(sideVec);
                                }
                                //if side is not 0, do not add to checked so can be rechecked from other sides
                            }
                            else
                            {
                                checkedLocal.add(sideVec);
                            }
                        }
                        else
                        {
                            checkedLocal.add(sideVec);
                            if (id != null && Blocks.air != id && id != GCBlocks.brightAir && this.canBlockPassAirCheck(id, sideVec, side))
                            {
                                //Look outbound through partially sealable blocks in case there is breatheableAir to clear beyond
                                nextLayer.add(sideVec);
                            }
                        }
                    }
                }
            }

            // Set up the next layer as current layer for the while loop
            this.currentLayer = nextLayer;
            nextLayer = new LinkedList<BlockVec3>();
        }
    }

    private void doLayer()
    {
        //Local variables are fractionally faster than statics
        Block breatheableAirID = GCBlocks.breatheableAir;
        Block airID = Blocks.air;
        Block breatheableAirIDBright = GCBlocks.brightBreatheableAir;
        Block airIDBright = GCBlocks.brightAir;
        Block oxygenSealerID = GCBlocks.oxygenSealer;
        HashSet<BlockVec3> checkedLocal = this.checked;
        LinkedList nextLayer = new LinkedList<BlockVec3>();
        World world = this.world;
        int side, bits;
        
        while (this.sealed && this.currentLayer.size() > 0)
        {
            for (BlockVec3 vec : this.currentLayer)
            {
                //This is for side = 0 to 5 - but using do...while() is fractionally quicker
                side = 0;
                bits = vec.sideDoneBits;
                do
                {
                    //Skip the side which this was entered from
                    //This is also used to skip looking on the solid sides of partially sealable blocks
                    if ((bits & (1 << side)) == 0)
                    {
                        // The sides 0 to 5 correspond with the ForgeDirections
                        // but saves a bit of time not to call ForgeDirection
                        BlockVec3 sideVec = vec.newVecSide(side);

                        if (!checkedLocal.contains(sideVec))
                        {
                            if (this.checkCount > 0)
                            {
                                this.checkCount--;
                                checkedLocal.add(sideVec);
//                                GCLog.debug("Checking vec " + sideVec.toString() + " : " + (sideVec.sideDoneBits >> 6) + " @ " + this.checkCount);

                                Block id = sideVec.getBlockIDsafe_noChunkLoad(world);
                                // The most likely case
                                if (id == breatheableAirID)
                                {
                                    nextLayer.add(sideVec);
                                    this.ambientThermalTracked.add(sideVec);
                                }
                                else if (id == airID)
                                {
                                    nextLayer.add(sideVec);
                                    this.airToReplace.add(sideVec);
                                }
                                else if (id == breatheableAirIDBright)
                                {
                                    nextLayer.add(sideVec);
                                    this.ambientThermalTracked.add(sideVec);
                                }
                                else if (id == airIDBright)
                                {
                                    nextLayer.add(sideVec);
                                    this.airToReplaceBright.add(sideVec);
                                }
                                else if (id == null)
                                {
                                    // Broken through to the void or the
                                    // stratosphere (above y==255) - set
                                    // unsealed and abort
                                    this.checkCount = 0;
                                    this.sealed = false;
                                    return;
                                }
                                else if (this.canBlockPassAirCheck(id, sideVec, side))
                                {
                                    nextLayer.add(sideVec);
                                }
                                else if (id == oxygenSealerID)
                                {
                                    TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);

                                    if (sealer != null && !this.sealers.contains(sealer))
                                    {
                                        if (side == 0)
                                        {
                                            this.sealers.add(sealer);
                                            if (sealer.thermalControlEnabled())
                                            {
                                                foundAmbientThermal = true;
                                            }
                                            this.checkCount += sealer.getFindSealChecks();
                                        }
                                        else
                                        {
                                            //Allow this sealer to be checked from other sides
                                            checkedLocal.remove(sideVec);
                                        }
                                    }
                                }
                                //If the chunk was unloaded, BlockVec3.getBlockID returns Blocks.bedrock
                                //which is a solid block, so the loop will treat that as a sealed edge
                                //and not iterate any further in that direction
                            }
                            // the if (this.isSealed) check here is unnecessary because of the returns
                            else
                            {
                                Block id = sideVec.getBlockIDsafe_noChunkLoad(this.world);
                                // id == null means the void or height y>255, both
                                // of which are unsealed obviously
                                if (id == null || id == airID || id == breatheableAirID || id == airIDBright || id == breatheableAirIDBright || this.canBlockPassAirCheck(id, sideVec, side))
                                {
                                    this.sealed = false;
                                    if (this.sealers.size() > 0) traceLeak(vec);
                                    return;
                                }
                            }
                        }
                    }
                    side++;
                }
                while (side < 6);
            }

            // Is there a further layer of air/permeable blocks to test?
            this.currentLayer = nextLayer;
            nextLayer = new LinkedList<BlockVec3>();
        }
    }

    private void doLayerNearMapEdge()
    {
        //Local variables are fractionally faster than statics
        Block breatheableAirID = GCBlocks.breatheableAir;
        Block airID = Blocks.air;
        Block breatheableAirIDBright = GCBlocks.brightBreatheableAir;
        Block airIDBright = GCBlocks.brightAir;
        Block oxygenSealerID = GCBlocks.oxygenSealer;
        HashSet<BlockVec3> checkedLocal = this.checked;
        LinkedList nextLayer = new LinkedList<BlockVec3>();
        int side;
        int bits;
        
        while (this.sealed && this.currentLayer.size() > 0)
        {

            for (BlockVec3 vec : this.currentLayer)
            {
                //This is for side = 0 to 5 - but using do...while() is fractionally quicker
                side = 0;
                bits = vec.sideDoneBits;
                do
                {
                    //Skip the side which this was entered from
                    //This is also used to skip looking on the solid sides of partially sealable blocks
                    if ((bits & (1 << side)) == 0)
                    {
                        // The sides 0 to 5 correspond with the ForgeDirections
                        // but saves a bit of time not to call ForgeDirection
                        BlockVec3 sideVec = vec.newVecSide(side);

                        if (!checkedLocal.contains(sideVec))
                        {
                            if (this.checkCount > 0)
                            {
                                this.checkCount--;
                                checkedLocal.add(sideVec);

                                Block id = sideVec.getBlockID_noChunkLoad(this.world);
                                // The most likely case
                                if (id == breatheableAirID)
                                {
                                    nextLayer.add(sideVec);
                                    this.ambientThermalTracked.add(sideVec);
                                }
                                else if (id == airID)
                                {
                                    nextLayer.add(sideVec);
                                    this.airToReplace.add(sideVec);
                                }
                                else if (id == breatheableAirIDBright)
                                {
                                    nextLayer.add(sideVec);
                                    this.ambientThermalTracked.add(sideVec);
                                }
                                else if (id == airIDBright)
                                {
                                    nextLayer.add(sideVec);
                                    this.airToReplaceBright.add(sideVec);
                                }
                                else if (id == null)
                                {
                                    // Broken through to the void or the
                                    // stratosphere (above y==255) - set
                                    // unsealed and abort
                                    this.checkCount = 0;
                                    this.sealed = false;
                                    return;
                                }
                                else if (this.canBlockPassAirCheck(id, sideVec, side))
                                {
                                    nextLayer.add(sideVec);
                                }
                                else if (id == oxygenSealerID)
                                {
                                    TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);

                                    if (sealer != null && !this.sealers.contains(sealer))
                                    {
                                        if (side == 0)
                                        {
                                            this.sealers.add(sealer);
                                            if (sealer.thermalControlEnabled())
                                            {
                                                foundAmbientThermal = true;
                                            }
                                            this.checkCount += sealer.getFindSealChecks();
                                        }
                                        else
                                        {
                                            //Allow this sealer to be checked from other sides
                                            checkedLocal.remove(sideVec);
                                        }
                                    }
                                }
                                //If the chunk was unloaded, BlockVec3.getBlockID returns Blocks.bedrock
                                //which is a solid block, so the loop will treat that as a sealed edge
                                //and not iterate any further in that direction
                            }
                            // the if (this.isSealed) check here is unnecessary because of the returns
                            else
                            {
                                Block id = sideVec.getBlockID_noChunkLoad(this.world);
                                // id == null means the void or height y>255, both
                                // of which are unsealed obviously
                                if (id == null || id == airID || id == breatheableAirID || id == airIDBright || id == breatheableAirIDBright || this.canBlockPassAirCheck(id, sideVec, side))
                                {
                                    this.sealed = false;
                                    return;
                                }
                            }
                        }
                    }
                    side++;
                }
                while (side < 6);
            }

            // Is there a further layer of air/permeable blocks to test?
            this.currentLayer = nextLayer;
            nextLayer = new LinkedList<BlockVec3>();
        }
    }
    
    private void traceLeak(BlockVec3 tracer)
    {
    	GCLog.debug("Leak tracing test length = " + this.checked.size());
    	ArrayList<BlockVec3> route = new ArrayList();
    	BlockVec3 start = this.head.clone().translate(0, 1, 0);
    	int count = 0;
    	while (!tracer.equals(start) && count < 25)
    	{
	    	route.add(tracer);
	    	int x = tracer.x;
	    	int y = tracer.y;
	    	int z = tracer.z;
    		switch(tracer.sideDoneBits >> 6)
	    	{
		    	case 1: y--;
		    	break;
		    	case 0: y++;
		    	break;
		    	case 3: z--;
		    	break;
		    	case 2: z++;
		    	break;
		    	case 5: x--;
		    	break;
		    	case 4: x++;
		    	break;   	
	    	}
    		boolean flag = false;
	    	for (BlockVec3 test : this.checked)
	    	{
	    		if (test.x == x && test.y == y && test.z == z)
	    		{
	    			tracer = test;
	    			flag = true;
	    			break;
	    		}
	    	}
	    	if (!flag) return;
	    	count ++;
    	}
    	
    	this.leakTrace = new ArrayList();
    	this.leakTrace.add(start);
    	for (int j = route.size() - 1; j >= 0; j--)
    	{
    		this.leakTrace.add(route.get(j));
    	}
    }

    /**
     * 
     * @param block - the block ID, already taken from the world (can't be null or air here)
     * @param vec - the position of the block to check: metadata might be needed
     * @param side - this is the side approached from, e.g. 1 means this was approached from beneath
     * @return
     */
    private boolean canBlockPassAirCheck(Block block, BlockVec3 vec, int side)
    {
        //Check leaves first, because their isOpaqueCube() test depends on graphics settings
        //(See net.minecraft.block.BlockLeaves.isOpaqueCube()!)
        if (block instanceof BlockLeavesBase)
        {
            return true;
        }

        if (block.isOpaqueCube())
        {
            //Gravel, wool and sponge are porous
            return block instanceof BlockGravel || block.getMaterial() == Material.cloth || block instanceof BlockSponge;

        }

        if (block instanceof BlockGlass || block instanceof BlockStainedGlass)
        {
            return false;
        }

        //Solid but non-opaque blocks, for example special glass
        if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(block))
        {
            ArrayList<Integer> metaList = OxygenPressureProtocol.nonPermeableBlocks.get(block);
            if (metaList.contains(Integer.valueOf(-1)) || metaList.contains(vec.getBlockMetadata(this.world)))
            {
                return false;
            }
        }

        if (block instanceof IPartialSealableBlock)
        {
            IPartialSealableBlock blockPartial = (IPartialSealableBlock) block;
            if (blockPartial.isSealed(this.world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side)))
            {
                // If a partial block checks as solid, allow it to be tested
                // again from other directions
                // This won't cause an endless loop, because the block won't
                // be included in nextLayer if it checks as solid
                this.checked.remove(vec);
                this.checkCount--;
                return false;
            }

            //Find the solid sides so they don't get iterated into, when doing the next layer
            for (int i = 0; i < 6; i++)
            {
                if (i == side)
                {
                    continue;
                }
                if (blockPartial.isSealed(this.world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(i)))
                {
                    vec.setSideDone(i ^ 1);
                }
            }
            return true;
        }

        if (block instanceof BlockUnlitTorch)
        {
            this.torchesToUpdate.add(vec);
            return true;
        }

        //Half slab seals on the top side or the bottom side according to its metadata
        if (block instanceof BlockSlab)
        {
            boolean isTopSlab = (vec.getBlockMetadata(this.world) & 8) == 8;
            //Looking down onto a top slab or looking up onto a bottom slab
            if (side == 0 && isTopSlab || side == 1 && !isTopSlab)
            {
                //Sealed from that solid side but allow other sides still to be checked
                this.checked.remove(vec);
                this.checkCount--;
                return false;
            }
            //Not sealed
            vec.setSideDone(isTopSlab ? 1 : 0);
            return true;
        }

        //Farmland etc only seals on the solid underside
        if (block instanceof BlockFarmland || block instanceof BlockEnchantmentTable || block instanceof BlockLiquid)
        {
            if (side == 1)
            {
                //Sealed from the underside but allow other sides still to be checked
                this.checked.remove(vec);
                this.checkCount--;
                return false;
            }
            //Not sealed
            vec.setSideDone(0);
            return true;
        }

        if (block instanceof BlockPistonBase)
        {
            BlockPistonBase piston = (BlockPistonBase) block;
            int meta = vec.getBlockMetadata(this.world);
            if (BlockPistonBase.isExtended(meta))
            {
                int facing = BlockPistonBase.getPistonOrientation(meta);
                if (side == facing)
                {
                    this.checked.remove(vec);
                    this.checkCount--;
                    return false;
                }
                vec.setSideDone(facing ^ 1);
                return true;
            }
            return false;
        }

        //General case - this should cover any block which correctly implements isBlockSolidOnSide
        //including most modded blocks - Forge microblocks in particular is covered by this.
        // ### Any exceptions in mods should implement the IPartialSealableBlock interface ###
        if (block.isSideSolid(this.world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side ^ 1)))
        {
            //Solid on all sides
            if (block.getMaterial().blocksMovement() && block.renderAsNormalBlock())
            {
                return false;
            }
            //Sealed from this side but allow other sides still to be checked
            this.checked.remove(vec);
            this.checkCount--;
            return false;
        }

        //Easy case: airblock, return without checking other sides
        if (block.getMaterial() == Material.air)
        {
            return true;
        }

        //Not solid on that side.
        //Look to see if there is any other side which is solid in which case a check will not be needed next time
        for (int i = 0; i < 6; i++)
        {
            if (i == (side ^ 1))
            {
                continue;
            }
            if (block.isSideSolid(this.world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(i)))
            {
                vec.setSideDone(i);
            }
        }

        //Not solid from this side, so this is not sealed
        return true;
    }
}
