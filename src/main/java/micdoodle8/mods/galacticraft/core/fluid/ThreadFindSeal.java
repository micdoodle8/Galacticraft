package micdoodle8.mods.galacticraft.core.fluid;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockThermalAir;
import micdoodle8.mods.galacticraft.core.blocks.BlockUnlitTorch;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadFindSeal
{
    public final AtomicBoolean sealedFinal = new AtomicBoolean();
    public static final AtomicBoolean anylooping = new AtomicBoolean();
    public final AtomicBoolean looping = new AtomicBoolean();

    private World world;
    private BlockVec3 head;
    private boolean sealed;
    private List<TileEntityOxygenSealer> sealers;
    private static final intBucket[] buckets;
    private static int checkedSize;
    private int checkCount;
    private HashMap<BlockVec3, TileEntityOxygenSealer> sealersAround;
    private List<BlockVec3> currentLayer;
    private List<BlockVec3> airToReplace;
    private List<BlockVec3> fireToReplace;
    private List<BlockVec3> breatheableToReplace;
    private List<BlockVec3> airToReplaceBright;
    private List<BlockVec3> breatheableToReplaceBright;
    private List<BlockVec3> ambientThermalTracked;
    private List<BlockVec3> ambientThermalTrackedBright;
    private List<TileEntityOxygenSealer> otherSealers;
    private List<BlockVec3> torchesToUpdate;
    private boolean foundAmbientThermal;
    public List<BlockVec3> leakTrace;

    static
    {
        buckets = new intBucket[256];
        checkedInit();
    }

    public ThreadFindSeal(TileEntityOxygenSealer sealer)
    {
        this(sealer.getWorld(), sealer.getPos().up(), sealer.getFindSealChecks(), new ArrayList<>(Collections.singletonList(sealer)));
    }

    public ThreadFindSeal(World world, BlockPos head, int checkCount, List<TileEntityOxygenSealer> sealers)
    {
        if (ThreadFindSeal.anylooping.getAndSet(true))
        {
            return;
        }
        this.world = world;
        this.head = new BlockVec3(head);
        this.checkCount = checkCount;
        this.sealers = sealers;
        this.foundAmbientThermal = false;
        checkedClear();
        checkedSize = 0;
        this.torchesToUpdate = new LinkedList<>();

        this.sealersAround = TileEntityOxygenSealer.getSealersAround(world, head, 1024 * 1024);

        //If called by a sealer test the head block and if partiallySealable mark its sides done as required
        if (!sealers.isEmpty())
        {
            if (checkCount > 0)
            {
                BlockState headState = this.world.getBlockState(head);
                if (!(headState.getBlock().isAir(headState, world, head)))
                {
                    this.canBlockPassAirCheck(headState.getBlock(), this.head, Direction.UP);
                    //reset the checkCount as canBlockPassAirCheck might have changed it
                    this.checkCount = checkCount;
                }
            }

            this.looping.set(true);
            for (TileEntityOxygenSealer eachSealer : sealers)
            {
                eachSealer.threadSeal = this;
            }

//            if (ConfigManagerCore.INSTANCE.enableSealerMultithreading)
//            {
//                new ThreadedFindSeal();
//            }
//            else
//            {
            //            }
        }
        else
        //If not called by a sealer, it's a breathable air edge check
        {
            //Run this in the main thread
        }
        this.check();
        ThreadFindSeal.anylooping.set(false);
    }

    //Multi-threaded version of the code for sealer updates (not for edge checks).
    public class ThreadedFindSeal extends Thread
    {
        public ThreadedFindSeal()
        {
            super("GC Sealer Roomfinder Thread");

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
        TileEntity tile = this.head.getTileEntityOnSide(world, Direction.DOWN);
        this.foundAmbientThermal = tile instanceof TileEntityOxygenSealer && ((TileEntityOxygenSealer) tile).thermalControlEnabled();
        this.checkedAdd(this.head);
        this.currentLayer = new LinkedList<>();
        this.airToReplace = new LinkedList<>();
        this.airToReplaceBright = new LinkedList<>();
        this.ambientThermalTracked = new LinkedList<>();
        this.ambientThermalTrackedBright = new LinkedList<>();

        if (this.checkCount > 0)
        {
            this.currentLayer.add(this.head);
            if (this.head.x < -29990000 || this.head.z < -29990000 || this.head.x >= 29990000 || this.head.z >= 29990000)
            {
                BlockState state = this.head.getBlockState_noChunkLoad(this.world);
                if (Blocks.AIR == state.getBlock())
                {
                    this.airToReplace.add(this.head.clone());
                }
                else if (state.getBlock() == GCBlocks.brightAir)
                {
                    this.airToReplaceBright.add(this.head.clone());
                }
                this.doLayerNearMapEdge();
            }
            else
            {
                BlockState headState = this.head.getBlockStateSafe_noChunkLoad(this.world);
                if (Blocks.AIR == headState.getBlock())
                {
                    this.airToReplace.add(this.head.clone());
                }
                else if (headState.getBlock() == GCBlocks.brightAir)
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
            int checkedSave = checkedSize;
            checkedClear();
            this.breatheableToReplace = new LinkedList<>();
            this.breatheableToReplaceBright = new LinkedList<>();
            this.fireToReplace = new LinkedList<>();
            this.otherSealers = new LinkedList<>();
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
                List<TileEntityOxygenSealer> sealersDone = new ArrayList<>(this.sealers);
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
                        this.sealers = new LinkedList<>();
                        this.sealers.add(otherSealer);
                        if (otherSealer.thermalControlEnabled())
                        {
                            foundAmbientThermal = true;
                        }
                        checkedClear();
                        this.checkedAdd(newhead);
                        this.currentLayer.clear();
                        this.airToReplace.clear();
                        this.airToReplaceBright.clear();
                        this.torchesToUpdate = new LinkedList<>();
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
                            if (ConfigManagerCore.INSTANCE.enableDebug.get())
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
                            checkedSave += checkedSize;
                            break;
                        }
                        else
                        {
                            sealersDone.addAll(this.sealers);
                        }

                        checkedSave += checkedSize;
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
            checkedSize = checkedSave;

            if (!this.sealed)
            {
                Block block = this.head.getBlockState(this.world).getBlock();
                if (block == GCBlocks.breatheableAir)
                {
                    this.breatheableToReplace.add(this.head);
                }
                if (block == GCBlocks.brightBreatheableAir)
                {
                    this.breatheableToReplaceBright.add(this.head);
                }
                this.makeSealBad();
            }
            else
            {
                this.leakTrace = null;
            }
        }

        // Set any sealers found which are not the head sealer, not to run their
        // own seal checks for a while
        // (The player can control which is the head sealer in a space by
        // enabling just that one and disabling all the others)
        TileEntityOxygenSealer headSealer = this.sealersAround.get(this.head.clone().translate(0, -1, 0));

        //TODO: if multi-threaded, this final code block giving access to the sealer tiles needs to be threadsafe

        // If it is sealed, cooldown can be extended as frequent checks are not needed
        if (headSealer != null)
        {
            headSealer.stopSealThreadCooldown = 75 + TileEntityOxygenSealer.countEntities;
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

        this.sealedFinal.set(this.sealed);
        this.looping.set(false);

        if (ConfigManagerCore.INSTANCE.enableDebug.get())
        {
            long time3 = System.nanoTime();
            float total = (time3 - time1) / 1000000.0F;
            float looping = (time2 - time1) / 1000000.0F;
            float replacing = (time3 - time2) / 1000000.0F;
            GCLog.info("Oxygen Sealer Check Completed at x" + this.head.x + " y" + this.head.y + " z" + this.head.z);
            GCLog.info("   Sealed: " + this.sealed + "  ~  " + this.sealers.size() + " sealers  ~  " + (checkedSize - 1) + " blocks");
            GCLog.info("   Total Time taken: " + String.format("%.2f", total) + "ms  ~  " + String.format("%.2f", looping) + " + " + String.format("%.2f", replacing) + "");
        }
    }

    private void makeSealGood(boolean ambientThermal)
    {
        if (!this.airToReplace.isEmpty() || !this.airToReplaceBright.isEmpty() || !ambientThermalTracked.isEmpty())
        {
            List<ScheduledBlockChange> changeList = new LinkedList<>();
            BlockThermalAir breatheableAirID = (BlockThermalAir) GCBlocks.breatheableAir;
            BlockThermalAir breatheableAirIDBright = (BlockThermalAir) GCBlocks.brightBreatheableAir;
            //TODO: Can we somehow detect only changes in state of ambientThermal since last check?  tricky...
            for (BlockVec3 checkedVec : this.airToReplace)
            {
                //No block update for performance reasons; deal with unlit torches separately
                changeList.add(new ScheduledBlockChange(checkedVec.toBlockPos(), breatheableAirID.getDefaultState().with(BlockThermalAir.THERMAL, ambientThermal), 0));
            }
            for (BlockVec3 checkedVec : this.airToReplaceBright)
            {
                changeList.add(new ScheduledBlockChange(checkedVec.toBlockPos(), breatheableAirIDBright.getDefaultState().with(BlockThermalAir.THERMAL, ambientThermal), 0));
            }
            for (BlockVec3 checkedVec : this.ambientThermalTracked)
            {
                if (checkedVec.getBlockState(world).get(BlockThermalAir.THERMAL) != ambientThermal)
                {
                    changeList.add(new ScheduledBlockChange(checkedVec.toBlockPos(), breatheableAirID.getDefaultState().with(BlockThermalAir.THERMAL, ambientThermal), 0));
                }
            }
            for (BlockVec3 checkedVec : this.ambientThermalTrackedBright)
            {
                if (checkedVec.getBlockState(world).get(BlockThermalAir.THERMAL) != ambientThermal)
                {
                    changeList.add(new ScheduledBlockChange(checkedVec.toBlockPos(), breatheableAirIDBright.getDefaultState().with(BlockThermalAir.THERMAL, ambientThermal), 0));
                }
            }

            TickHandlerServer.scheduleNewBlockChange(GCCoreUtil.getDimensionType(this.world), changeList);
        }
        if (!this.torchesToUpdate.isEmpty())
        {
            TickHandlerServer.scheduleNewTorchUpdate(GCCoreUtil.getDimensionType(this.world), this.torchesToUpdate);
        }
    }

    private void makeSealBad()
    {
        if (!this.breatheableToReplace.isEmpty() || !this.breatheableToReplaceBright.isEmpty())
        {
            List<ScheduledBlockChange> changeList = new LinkedList<>();
            for (BlockVec3 checkedVec : this.breatheableToReplace)
            {
                changeList.add(new ScheduledBlockChange(checkedVec.toBlockPos(), Blocks.AIR.getDefaultState(), 0));
            }
            for (BlockVec3 checkedVec : this.fireToReplace)
            {
                changeList.add(new ScheduledBlockChange(checkedVec.toBlockPos(), Blocks.AIR.getDefaultState(), 2));
            }
            for (BlockVec3 checkedVec : this.breatheableToReplaceBright)
            {
                changeList.add(new ScheduledBlockChange(checkedVec.toBlockPos(), GCBlocks.brightAir.getDefaultState(), 0));
            }
            TickHandlerServer.scheduleNewBlockChange(GCCoreUtil.getDimensionType(this.world), changeList);
        }
        if (!this.torchesToUpdate.isEmpty())
        {
            TickHandlerServer.scheduleNewTorchUpdate(GCCoreUtil.getDimensionType(this.world), this.torchesToUpdate);
        }
    }

    private void unseal()
    {
        //Local variables are fractionally faster than statics
        Block breatheableAirID = GCBlocks.breatheableAir;
        Block breatheableAirIDBright = GCBlocks.brightBreatheableAir;
        Block oxygenSealerID = GCBlocks.oxygenSealer;
        Block fireBlock = Blocks.FIRE;
        Block airBlock = Blocks.AIR;
        Block airBlockBright = GCBlocks.brightAir;
        List<BlockVec3> toReplaceLocal = this.breatheableToReplace;
        List<BlockVec3> toReplaceLocalBright = this.breatheableToReplaceBright;
        LinkedList<BlockVec3> nextLayer = new LinkedList<>();
        World world = this.world;
        int LogicalSide, bits;

        while (this.currentLayer.size() > 0)
        {
            for (BlockVec3 vec : this.currentLayer)
            {
                LogicalSide = 0;
                bits = vec.sideDoneBits;
                do
                {
                    if ((bits & (1 << LogicalSide)) == 0)
                    {
                        if (!checkedContains(vec, LogicalSide))
                        {
                            BlockVec3 sideVec = vec.newVecSide(LogicalSide);
                            BlockState state = sideVec.getBlockStateSafe_noChunkLoad(world);
                            Block block = state == null ? null : state.getBlock();

                            if (block == breatheableAirID)
                            {
                                toReplaceLocal.add(sideVec);
                                nextLayer.add(sideVec);
                                checkedAdd(sideVec);
                            }
                            else if (block == breatheableAirIDBright)
                            {
                                toReplaceLocalBright.add(sideVec);
                                nextLayer.add(sideVec);
                                checkedAdd(sideVec);
                            }
                            else if (block == fireBlock)
                            {
                                this.fireToReplace.add(sideVec);
                                nextLayer.add(sideVec);
                                checkedAdd(sideVec);
                            }
                            else if (block == oxygenSealerID)
                            {
                                TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);

                                if (sealer != null && !this.sealers.contains(sealer))
                                {
                                    if (LogicalSide == 0)
                                    {
                                        //Accessing the vent LogicalSide of the sealer, so add it
                                        this.otherSealers.add(sealer);
                                        checkedAdd(sideVec);
                                    }
                                    //if LogicalSide is not 0, do not add to checked so can be rechecked from other sides
                                }
                                else
                                {
                                    checkedAdd(sideVec);
                                }
                            }
                            else
                            {
                                if (block != null && block != airBlock && block != airBlockBright)
                                {
                                    //This test applies any necessary checkedAdd();
                                    if (this.canBlockPassAirCheck(block, sideVec, Direction.byIndex(LogicalSide)))
                                    {
                                        //Look outbound through partially sealable blocks in case there is breatheableAir to clear beyond
                                        nextLayer.add(sideVec);
                                    }
                                }
                                else
                                {
                                    if (state != null)
                                    {
                                        checkedAdd(sideVec);
                                    }
                                }
                            }
                        }
                    }
                    LogicalSide++;
                }
                while (LogicalSide < 6);
            }

            // Set up the next layer as current layer for the while loop
            this.currentLayer = nextLayer;
            nextLayer = new LinkedList<>();
        }
    }

    /**
     * Literally the only difference from unseal() should be this:
     * Block id = sideVec.getBlockID_noChunkLoad(world);
     * <p>
     * In this code, there is a map edge check on the x, z coordinates (outside map edge at 30,000,000 blocks?)
     * This check is skipped in the "safe" version of the same code, for higher performance
     * because doing this check 50000 times when looking at blocks around a sealer at spawn is obviously dumb
     */
    private void unsealNearMapEdge()
    {
        //Local variables are fractionally faster than statics
        Block breatheableAirID = GCBlocks.breatheableAir;
        Block breatheableAirIDBright = GCBlocks.brightBreatheableAir;
        Block oxygenSealerID = GCBlocks.oxygenSealer;
        Block fireBlock = Blocks.FIRE;
        Block airBlock = Blocks.AIR;
        Block airBlockBright = GCBlocks.brightAir;
        List<BlockVec3> toReplaceLocal = this.breatheableToReplace;
        LinkedList<BlockVec3> nextLayer = new LinkedList<>();
        World world = this.world;
        int LogicalSide, bits;

        while (this.currentLayer.size() > 0)
        {
            for (BlockVec3 vec : this.currentLayer)
            {
                LogicalSide = 0;
                bits = vec.sideDoneBits;
                do
                {
                    if ((bits & (1 << LogicalSide)) == 0)
                    {
                        if (!checkedContains(vec, LogicalSide))
                        {
                            BlockVec3 sideVec = vec.newVecSide(LogicalSide);
                            BlockState state = sideVec.getBlockState_noChunkLoad(world);
                            Block block = state == null ? null : state.getBlock();

                            if (block == breatheableAirID)
                            {
                                toReplaceLocal.add(sideVec);
                                nextLayer.add(sideVec);
                                checkedAdd(sideVec);
                            }
                            else if (block == breatheableAirIDBright)
                            {
                                this.breatheableToReplaceBright.add(sideVec);
                                nextLayer.add(sideVec);
                                checkedAdd(sideVec);
                            }
                            else if (block == fireBlock)
                            {
                                this.fireToReplace.add(sideVec);
                                nextLayer.add(sideVec);
                                checkedAdd(sideVec);
                            }
                            else if (block == oxygenSealerID)
                            {
                                TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);

                                if (sealer != null && !this.sealers.contains(sealer))
                                {
                                    if (LogicalSide == 0)
                                    {
                                        //Accessing the vent LogicalSide of the sealer, so add it
                                        this.otherSealers.add(sealer);
                                        checkedAdd(sideVec);
                                    }
                                    //if LogicalSide is not 0, do not add to checked so can be rechecked from other sides
                                }
                                else
                                {
                                    checkedAdd(sideVec);
                                }
                            }
                            else
                            {
                                if (block != null && block != airBlock && block != airBlockBright)
                                {
                                    //This test applies any necessary checkedAdd();
                                    if (this.canBlockPassAirCheck(block, sideVec, Direction.byIndex(LogicalSide)))
                                    {
                                        //Look outbound through partially sealable blocks in case there is breatheableAir to clear beyond
                                        nextLayer.add(sideVec);
                                    }
                                }
                                else
                                {
                                    if (block != null)
                                    {
                                        checkedAdd(sideVec);
                                    }
                                }
                            }
                        }
                    }
                    LogicalSide++;
                }
                while (LogicalSide < 6);
            }

            // Set up the next layer as current layer for the while loop
            this.currentLayer = nextLayer;
            nextLayer = new LinkedList<>();
        }
    }

    private void doLayer()
    {
        //Local variables are fractionally faster than statics
        Block breatheableAirID = GCBlocks.breatheableAir;
        Block airID = Blocks.AIR;
        Block breatheableAirIDBright = GCBlocks.brightBreatheableAir;
        Block airIDBright = GCBlocks.brightAir;
        Block oxygenSealerID = GCBlocks.oxygenSealer;
        LinkedList<BlockVec3> nextLayer = new LinkedList<>();
        World world = this.world;
        int LogicalSide, bits;

        while (this.sealed && this.currentLayer.size() > 0)
        {
            for (BlockVec3 vec : this.currentLayer)
            {
                //This is for LogicalSide = 0 to 5 - but using do...while() is fractionally quicker
                LogicalSide = 0;
                bits = vec.sideDoneBits;
                do
                {
                    //Skip the LogicalSide which this was entered from
                    //This is also used to skip looking on the solid sides of partially sealable blocks
                    if ((bits & (1 << LogicalSide)) == 0)
                    {
                        // The sides 0 to 5 correspond with the EnumFacings
                        // but saves a bit of time not to call EnumFacing

                        if (!checkedContains(vec, LogicalSide))
                        {
                            BlockVec3 sideVec = vec.newVecSide(LogicalSide);
                            if (this.checkCount > 0)
                            {
                                this.checkCount--;

                                BlockState state = sideVec.getBlockStateSafe_noChunkLoad(world);
                                Block block = state == null ? null : state.getBlock();
                                // The most likely case
                                if (block == breatheableAirID)
                                {
                                    checkedAdd(sideVec);
                                    nextLayer.add(sideVec);
                                    this.ambientThermalTracked.add(sideVec);
                                }
                                else if (block == airID)
                                {
                                    checkedAdd(sideVec);
                                    nextLayer.add(sideVec);
                                    this.airToReplace.add(sideVec);
                                }
                                else if (block == breatheableAirIDBright)
                                {
                                    checkedAdd(sideVec);
                                    nextLayer.add(sideVec);
                                    this.ambientThermalTrackedBright.add(sideVec);
                                }
                                else if (block == airIDBright)
                                {
                                    checkedAdd(sideVec);
                                    nextLayer.add(sideVec);
                                    this.airToReplaceBright.add(sideVec);
                                }
                                else if (block == null)
                                {
                                    // Broken through to the void or the
                                    // stratosphere (above y==255) - set
                                    // unsealed and abort
                                    this.checkCount = 0;
                                    this.sealed = false;
                                    return;
                                }
                                else if (block == oxygenSealerID)
                                {
                                    TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);

                                    if (sealer != null && !this.sealers.contains(sealer))
                                    {
                                        if (LogicalSide == 0)
                                        {
                                            checkedAdd(sideVec);
                                            this.sealers.add(sealer);
                                            if (sealer.thermalControlEnabled())
                                            {
                                                foundAmbientThermal = true;
                                            }
                                            this.checkCount += sealer.getFindSealChecks();
                                        }
                                        //if LogicalSide != 0, no checkedAdd() - allows this sealer to be checked again from other sides
                                    }
                                }
                                else if (this.canBlockPassAirCheck(block, sideVec, Direction.byIndex(LogicalSide)))
                                {
                                    nextLayer.add(sideVec);
                                }
                                //If the chunk was unloaded, BlockVec3.getBlockID returns Blocks.bedrock
                                //which is a solid block, so the loop will treat that as a sealed edge
                                //and not iterate any further in that direction
                            }
                            // the if (this.isSealed) check here is unnecessary because of the returns
                            else
                            {
                                BlockState state = sideVec.getBlockStateSafe_noChunkLoad(this.world);
                                Block block = state == null ? null : state.getBlock();
                                // id == null means the void or height y>255, both
                                // of which are unsealed obviously
                                if (block == null || block == airID || block == breatheableAirID || block == airIDBright || block == breatheableAirIDBright || this.canBlockPassAirCheck(block, sideVec, Direction.byIndex(LogicalSide)))
                                {
                                    this.sealed = false;
                                    if (this.sealers.size() > 0)
                                    {
                                        vec.sideDoneBits = LogicalSide << 6;
                                        traceLeak(vec);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                    LogicalSide++;
                }
                while (LogicalSide < 6);
            }

            // Is there a further layer of air/permeable blocks to test?
            this.currentLayer = nextLayer;
            nextLayer = new LinkedList<>();
        }
    }

    /**
     * Again, literally the only difference from doLayer() should be these two lines:
     * Block id = sideVec.getBlockID_noChunkLoad(world);
     * <p>
     * In this code, there is a map edge check on the x, z coordinates (outside map edge at 30,000,000 blocks?)
     * This check is skipped in the "safe" version of the same code, for higher performance
     * because doing this check 50000 times when looking at blocks around a sealer at spawn is obviously dumb
     */
    private void doLayerNearMapEdge()
    {
        //Local variables are fractionally faster than statics
        Block breatheableAirID = GCBlocks.breatheableAir;
        Block airID = Blocks.AIR;
        Block breatheableAirIDBright = GCBlocks.brightBreatheableAir;
        Block airIDBright = GCBlocks.brightAir;
        Block oxygenSealerID = GCBlocks.oxygenSealer;
        LinkedList<BlockVec3> nextLayer = new LinkedList<>();
        World world = this.world;
        int LogicalSide, bits;

        while (this.sealed && this.currentLayer.size() > 0)
        {
            for (BlockVec3 vec : this.currentLayer)
            {
                //This is for LogicalSide = 0 to 5 - but using do...while() is fractionally quicker
                LogicalSide = 0;
                bits = vec.sideDoneBits;
                do
                {
                    //Skip the LogicalSide which this was entered from
                    //This is also used to skip looking on the solid sides of partially sealable blocks
                    if ((bits & (1 << LogicalSide)) == 0)
                    {
                        // The sides 0 to 5 correspond with the EnumFacings
                        // but saves a bit of time not to call EnumFacing

                        if (!checkedContains(vec, LogicalSide))
                        {
                            BlockVec3 sideVec = vec.newVecSide(LogicalSide);
                            if (this.checkCount > 0)
                            {
                                this.checkCount--;

                                BlockState state = sideVec.getBlockState_noChunkLoad(world);
                                Block block = state == null ? null : state.getBlock();
                                // The most likely case
                                if (block == breatheableAirID)
                                {
                                    checkedAdd(sideVec);
                                    nextLayer.add(sideVec);
                                    this.ambientThermalTracked.add(sideVec);
                                }
                                else if (block == airID)
                                {
                                    checkedAdd(sideVec);
                                    nextLayer.add(sideVec);
                                    this.airToReplace.add(sideVec);
                                }
                                else if (block == breatheableAirIDBright)
                                {
                                    checkedAdd(sideVec);
                                    nextLayer.add(sideVec);
                                    this.ambientThermalTrackedBright.add(sideVec);
                                }
                                else if (block == airIDBright)
                                {
                                    checkedAdd(sideVec);
                                    nextLayer.add(sideVec);
                                    this.airToReplaceBright.add(sideVec);
                                }
                                else if (block == null)
                                {
                                    // Broken through to the void or the
                                    // stratosphere (above y==255) - set
                                    // unsealed and abort
                                    this.checkCount = 0;
                                    this.sealed = false;
                                    return;
                                }
                                else if (block == oxygenSealerID)
                                {
                                    TileEntityOxygenSealer sealer = this.sealersAround.get(sideVec);

                                    if (sealer != null && !this.sealers.contains(sealer))
                                    {
                                        if (LogicalSide == 0)
                                        {
                                            checkedAdd(sideVec);
                                            this.sealers.add(sealer);
                                            if (sealer.thermalControlEnabled())
                                            {
                                                foundAmbientThermal = true;
                                            }
                                            this.checkCount += sealer.getFindSealChecks();
                                        }
                                        //if LogicalSide != 0, no checkedAdd() - allows this sealer to be checked again from other sides
                                    }
                                }
                                else if (this.canBlockPassAirCheck(block, sideVec, Direction.byIndex(LogicalSide)))
                                {
                                    nextLayer.add(sideVec);
                                }
                                //If the chunk was unloaded, BlockVec3.getBlockID returns Blocks.bedrock
                                //which is a solid block, so the loop will treat that as a sealed edge
                                //and not iterate any further in that direction
                            }
                            // the if (this.isSealed) check here is unnecessary because of the returns
                            else
                            {
                                BlockState state = sideVec.getBlockState_noChunkLoad(this.world);
                                Block block = state == null ? null : state.getBlock();
                                // id == null means the void or height y>255, both
                                // of which are unsealed obviously
                                if (block == null || block == airID || block == breatheableAirID || block == airIDBright || block == breatheableAirIDBright || this.canBlockPassAirCheck(block, sideVec, Direction.byIndex(LogicalSide)))
                                {
                                    this.sealed = false;
                                    if (this.sealers.size() > 0)
                                    {
                                        vec.sideDoneBits = LogicalSide << 6;
                                        traceLeak(vec);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                    LogicalSide++;
                }
                while (LogicalSide < 6);
            }

            // Is there a further layer of air/permeable blocks to test?
            this.currentLayer = nextLayer;
            nextLayer = new LinkedList<>();
        }
    }


    private void checkedAdd(BlockVec3 vec)
    {
        int dx = this.head.x - vec.x;
        int dz = this.head.z - vec.z;
        if (dx < -8191 || dx > 8192)
        {
            return;
        }
        if (dz < -8191 || dz > 8192)
        {
            return;
        }
        intBucket bucket = buckets[((dx & 15) << 4) + (dz & 15)];
        bucket.add(vec.y + ((dx & 0x3FF0) + ((dz & 0x3FF0) << 10) + ((vec.sideDoneBits & 0x1C0) << 18) << 4));
    }

    /**
     * Currently unused - the sided implementation is used instead
     */
    private boolean checkedContains(BlockVec3 vec)
    {
        int dx = this.head.x - vec.x;
        int dz = this.head.z - vec.z;
        if (dx < -8191 || dx > 8192)
        {
            return true;
        }
        if (dz < -8191 || dz > 8192)
        {
            return true;
        }
        intBucket bucket = buckets[((dx & 15) << 4) + (dz & 15)];
        return bucket.contains(vec.y + ((dx & 0x3FF0) + ((dz & 0x3FF0) << 10) << 4));
    }

    private boolean checkedContains(BlockVec3 vec, int LogicalSide)
    {
        int y = vec.y;
        int dx = this.head.x - vec.x;
        int dz = this.head.z - vec.z;
        switch (LogicalSide)
        {
            case 0:
                y--;
                if (y < 0)
                {
                    return false;
                }
                break;
            case 1:
                y++;
                if (y > 255)
                {
                    return false;
                }
                break;
            case 2:
                dz++;
                break;
            case 3:
                dz--;
                break;
            case 4:
                dx++;
                break;
            case 5:
                dx--;
        }
        if (dx < -8191 || dx > 8192)
        {
            return true;
        }
        if (dz < -8191 || dz > 8192)
        {
            return true;
        }
        intBucket bucket = buckets[((dx & 15) << 4) + (dz & 15)];
        return bucket.contains(y + ((dx & 0x3FF0) + ((dz & 0x3FF0) << 10) << 4));
    }

    private BlockVec3 checkedContainsTrace(int x, int y, int z)
    {
        int dx = this.head.x - x;
        int dz = this.head.z - z;
        if (dx < -8191 || dx > 8192)
        {
            return null;
        }
        if (dz < -8191 || dz > 8192)
        {
            return null;
        }
        intBucket bucket = buckets[((dx & 15) << 4) + (dz & 15)];
        int LogicalSide = bucket.getMSB4shifted(y + ((dx & 0x3FF0) + ((dz & 0x3FF0) << 10) << 4));
        if (LogicalSide >= 0)
        {
            BlockVec3 vec = new BlockVec3(x, y, z);
            vec.sideDoneBits = LogicalSide;
            return vec;
        }
        return null;
    }

    private static void checkedInit()
    {
        for (int i = 0; i < 256; i++)
        {
            buckets[i] = new intBucket();
        }
    }

    private static void checkedClear()
    {
        for (int i = 0; i < 256; i++)
        {
            buckets[i].clear();
        }
        checkedSize = 0;
    }

    public List<BlockPos> checkedAll()
    {
        List<BlockPos> list = new LinkedList<>();
        int x = head.x;
        int z = head.z;
        for (int i = 0; i < 256; i++)
        {
            int size = buckets[i].size();
            if (size == 0)
            {
                continue;
            }
            int ddx = i >> 4;
            int ddz = i & 15;
            int[] ints = buckets[i].contents();
            for (int j = 0; j < size; j++)
            {
                int k = ints[j];
                int y = k & 255;
                k >>= 4;
                int dx = (k & 0x3FF0) + ddx;
                int dz = ((k >> 10) & 0x3FF0) + ddz;
                if (dx > 0x2000)
                {
                    dx -= 0x4000;
                }
                if (dz > 0x2000)
                {
                    dz -= 0x4000;
                }
                list.add(new BlockPos(x + dx, y, z + dz));
            }
        }
        return list;
    }

    private void traceLeak(BlockVec3 tracer)
    {
        ArrayList<BlockVec3> route = new ArrayList<>();
        BlockVec3 start = this.head.clone().translate(0, 1, 0);
        int count = 0;
        int x = tracer.x;
        int y = tracer.y;
        int z = tracer.z;
        while (!tracer.equals(start) && count < 90)
        {
            route.add(tracer);
            switch (tracer.sideDoneBits >> 6)
            {
                case 1:
                    y--;
                    break;
                case 0:
                    y++;
                    break;
                case 3:
                    z--;
                    break;
                case 2:
                    z++;
                    break;
                case 5:
                    x--;
                    break;
                case 4:
                    x++;
                    break;
            }
            tracer = checkedContainsTrace(x, y, z);
            if (tracer == null)
            {
                return;
            }
            count++;
        }

        this.leakTrace = new ArrayList<>();
        this.leakTrace.add(start);
        for (int j = route.size() - 1; j >= 0; j--)
        {
            this.leakTrace.add(route.get(j));
        }
    }

    private boolean canBlockPassAirCheck(Block block, BlockVec3 vec, Direction side)
    {
        if (block instanceof IPartialSealableBlock)
        {
//            Direction testSide = Direction.byIndex();
            IPartialSealableBlock blockPartial = (IPartialSealableBlock) block;
            BlockPos vecPos = new BlockPos(vec.x, vec.y, vec.z);
            if (blockPartial.isSealed(this.world, vecPos, side))
            {
                // If a partial block checks as solid, allow it to be tested
                // again from other directions
                // This won't cause an endless loop, because the block won't
                // be included in nextLayer if it checks as solid
                this.checkCount--;
                return false;
            }

            //Find the solid sides so they don't get iterated into, when doing the next layer
            for (Direction face : Direction.values())
            {
                if (face == side)
                {
                    continue;
                }
                if (blockPartial.isSealed(this.world, vecPos, face))
                {
                    vec.setSideDone(face.getIndex() ^ 1);
                }
            }
            checkedAdd(vec);
            return true;
        }

        //Check leaves first, because their isOpaqueCube() test depends on graphics settings
        //(See net.minecraft.block.BlockLeaves.isOpaqueCube()!)
        if (block instanceof LeavesBlock)
        {
            checkedAdd(vec);
            return true;
        }

        BlockState state = world.getBlockState(vec.toBlockPos());
        if (state.isOpaqueCube(world, vec.toBlockPos()))
        {
            checkedAdd(vec);
            //Gravel, wool and sponge are porous
            return block instanceof GravelBlock || state.getMaterial() == Material.WOOL || block instanceof SpongeBlock;

        }

        if (block instanceof GlassBlock || block instanceof StainedGlassBlock)
        {
            checkedAdd(vec);
            return false;
        }

        //Solid but non-opaque blocks, for example special glass
        if (OxygenPressureProtocol.nonPermeableBlocks.contains(block))
        {
//            ArrayList<Integer> metaList = OxygenPressureProtocol.nonPermeableBlocks.get(block);
//            if (metaList.contains(Integer.valueOf(-1)) || metaList.contains(vec.getBlockMetadata(this.world)))
//            {
            checkedAdd(vec);
            return false;
//            }
        }

        if (block instanceof BlockUnlitTorch)
        {
            this.torchesToUpdate.add(vec);
            checkedAdd(vec);
            return true;
        }

        //Half slab seals on the top side or the bottom side according to its metadata
        if (block instanceof SlabBlock)
        {
            boolean isTopSlab = state.get(SlabBlock.TYPE) == SlabType.TOP; //(vec.getBlockMetadata(this.world) & 8) == 8;
            //Looking down onto a top slab or looking up onto a bottom slab
            if (side == Direction.DOWN && isTopSlab || side == Direction.UP && !isTopSlab)
            {
                //Sealed from that solid side but allow other sides still to be checked
                this.checkCount--;
                return false;
            }
            //Not sealed
            vec.setSideDone(isTopSlab ? 1 : 0);
            checkedAdd(vec);
            return true;
        }

        //Farmland etc only seals on the solid underside
        if (block instanceof FarmlandBlock || block instanceof EnchantingTableBlock || block instanceof FlowingFluidBlock)
        {
            if (side == Direction.UP)
            {
                //Sealed from the underside but allow other sides still to be checked
                this.checkCount--;
                return false;
            }
            //Not sealed
            vec.setSideDone(0);
            checkedAdd(vec);
            return true;
        }

        if (block instanceof PistonBlock)
        {
            PistonBlock piston = (PistonBlock) block;
            if (state.get(PistonBlock.EXTENDED))
            {
                Direction facing = state.get(PistonBlock.FACING);
                if (side == facing)
                {
                    this.checkCount--;
                    return false;
                }
                vec.setSideDone(facing.getOpposite().getIndex());
                checkedAdd(vec);
                return true;
            }
            checkedAdd(vec);
            return false;
        }

        //General case - this should cover any block which correctly implements isBlockSolidOnSide
        //including most modded blocks - Forge microblocks in particular is covered by this.
        // ### Any exceptions in mods should implement the IPartialSealableBlock interface ###
//        if (block.isSideSolid(state, this.world, vec.toBlockPos(), Direction.byIndex(side ^| 1)))
        if (vec.getBlockState(world).isSolidSide(world, vec.toBlockPos(), side.getOpposite()))
        {
            //Solid on all sides
            if (block.getMaterial(state).blocksMovement() && state.getShape(world, vec.toBlockPos()) == VoxelShapes.fullCube())
            {
                checkedAdd(vec);
                return false;
            }
            //Sealed from this side but allow other sides still to be checked
            this.checkCount--;
            return false;
        }

        //Easy case: airblock, return without checking other sides
        if (block.getMaterial(state) == Material.AIR)
        {
            checkedAdd(vec);
            return true;
        }

        //Not solid on that side.
        //Look to see if there is any other side which is solid in which case a check will not be needed next time
//        for (int i = 0; i < 6; i++)
        for (Direction dir : Direction.values())
        {
            if (dir == side.getOpposite())
            {
                continue;
            }
            if (vec.getBlockState(world).isSolidSide(this.world, vec.toBlockPos(), dir))
            {
                vec.setSideDone(dir.getIndex());
            }
        }

        //Not solid from this side, so this is not sealed
        checkedAdd(vec);
        return true;
    }

    public static class intBucket
    {
        private int maxSize = 64;  //default size
        private int size = 0;
        private int[] table = new int[maxSize];

        public void add(int i)
        {
            if (this.contains(i))
            {
                return;
            }

            if (size >= maxSize)
            {
                int[] newTable = new int[maxSize + maxSize];
                System.arraycopy(table, 0, newTable, 0, maxSize);
                table = newTable;
                maxSize += maxSize;
            }
            table[size] = i;
            size++;
            checkedSize++;
        }

        public boolean contains(int test)
        {
            for (int i = size - 1; i >= 0; i--)
            {
                if ((table[i] & 0xFFFFFFF) == test)
                {
                    return true;
                }
            }
            return false;
        }

        public int getMSB4shifted(int test)
        {
            for (int i = size - 1; i >= 0; i--)
            {
                if ((table[i] & 0xFFFFFFF) == test)
                {
                    return (table[i] & 0xF0000000) >> 22;
                }
            }
            return -1;
        }

        public void clear()
        {
            size = 0;
        }

        public int size()
        {
            return size;
        }

        public int[] contents()
        {
            return table;
        }
    }
}
