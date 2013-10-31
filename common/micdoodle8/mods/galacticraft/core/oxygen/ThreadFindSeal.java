package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.LinkedList;
import java.util.List;
import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol.VecDirPair;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.common.FMLLog;

public class ThreadFindSeal extends Thread
{
    public World world;
    public Vector3 head;
    public boolean sealed;
    public List<GCCoreTileEntityOxygenSealer> sealers;
    public List<Vector3> oxygenReliantBlocks;
    public LinkedList<VecDirPair> checked;
    public int checkCount;
    
    public ThreadFindSeal()
    {
        super("GC Sealer Roomfinder Thread");
    }
    
    @Override
    public void run()
    {
        long time1 = System.nanoTime();
        
        this.sealed = true;        
        loopThrough(this.head.clone().translate(new Vector3(0, 1, 0)));
        
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
            loopThrough(this.head.clone().translate(new Vector3(0, 1, 0)));
        }
        
        if (sealed)
        {
            for (VecDirPair checkedVec : checked)
            {
                int blockID = checkedVec.getPosition().getBlockID(world);
                
                if (this.sealed && blockID == 0)
                {
                    world.setBlock(checkedVec.getPosition().intX(), checkedVec.getPosition().intY(), checkedVec.getPosition().intZ(), GCCoreBlocks.breatheableAir.blockID, 0, 2);
                }
            }
        }
        else
        {
            this.checked.clear();
            this.loopThroughD(this.head.clone().translate(new Vector3(0, 1, 0)));
            
            for (VecDirPair checkedVec : checked)
            {
                int blockID = checkedVec.getPosition().getBlockID(world);
                
                if (blockID == GCCoreBlocks.breatheableAir.blockID)
                {
                    world.setBlock(checkedVec.getPosition().intX(), checkedVec.getPosition().intY(), checkedVec.getPosition().intZ(), 0, 0, 2);
                }
            }
        }
        
        
        long time2 = System.nanoTime();
        
        for (VecDirPair checkedVec : checked)
        {
            int blockID = checkedVec.getPosition().getBlockID(world);
            
            if (this.sealed && blockID == 0)
            {
                world.setBlock(checkedVec.getPosition().intX(), checkedVec.getPosition().intY(), checkedVec.getPosition().intZ(), GCCoreBlocks.breatheableAir.blockID, 0, 2);
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
        
        if (GCCoreConfigManager.enableDebug)
        {
            FMLLog.info("Oxygen Sealer Check Completed at x" + this.head.intX() + " y" + this.head.intY() + " z" + this.head.intZ());
            FMLLog.info("   Sealed: " + this.sealed);
            FMLLog.info("   Loop Time taken: " + ((time2 - time1) / 1000000.0D) + "ms");
            FMLLog.info("   Place Time taken: " + ((time3 - time2) / 1000000.0D) + "ms");
            FMLLog.info("   Total Time taken: " + ((time3 - time1) / 1000000.0D) + "ms");
            FMLLog.info("   Found: " + this.sealers.size() + " sealers");
            FMLLog.info("   Looped through: " + checked.size() + " blocks");
        }
    }
    
    private void loopThroughD(Vector3 vec)
    {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            Vector3 sideVec = vec.clone().modifyPositionFromSide(dir);
            VecDirPair pair = new VecDirPair(sideVec, dir);
            
            if (!checked(pair))
            {
                check(pair);
                
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
                    
                    if (!checked(pair))
                    {
                        this.checkCount--;
                        check(pair);
                        
                        if (this.canBlockPass(pair))
                        {
                            this.loopThrough(sideVec);
                        }
                        
                        TileEntity tileAtVec = sideVec.getTileEntity(this.world);
                        
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

                    if ((sideVec.getBlockID(this.world) == 0 || sideVec.getBlockID(this.world) == GCCoreBlocks.breatheableAir.blockID) && !checked(sideVec, dir))
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
        for (VecDirPair pair2 : this.checked)
        {
            if (pair2.getPosition().equals(pair.getPosition()))
            {
                return true;
            }
        }
        
        return false;
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

    private boolean canBlockPass(VecDirPair pair)
    {
        int id = pair.getPosition().getBlockID(this.world);

        if (id > 0)
        {
            Block block = Block.blocksList[id];
            int metadata = this.world.getBlockMetadata(pair.getPosition().intX(), pair.getPosition().intY(), pair.getPosition().intZ());

            if (id == GCCoreBlocks.breatheableAir.blockID)
            {
                return true;
            }

            if (OxygenPressureProtocol.vanillaPermeableBlocks.contains(id))
            {
                return true;
            }

            if (!block.isOpaqueCube())
            {
                if (block instanceof IPartialSealableBlock)
                {
                    return !((IPartialSealableBlock) block).isSealed(this.world, pair.getPosition().intX(), pair.getPosition().intY(), pair.getPosition().intZ(), pair.getDirection());
                }

                if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(id) && OxygenPressureProtocol.nonPermeableBlocks.get(id).contains(metadata))
                {
                    return false;
                }

                return true;
            }

            return false;
        }

        return true;
    }
}
