package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import micdoodle8.mods.galacticraft.API.IPartialSealedBlock;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;

public class OxygenPressureProtocol
{
    private LinkedList<Vector3> checked = new LinkedList<Vector3>();
    private boolean airtight;
    private static ArrayList<Integer> vanillaPermeableBlocks = new ArrayList<Integer>();

    static
    {
    	vanillaPermeableBlocks.add(Block.doorSteel.blockID);
    	vanillaPermeableBlocks.add(Block.doorWood.blockID);
    	vanillaPermeableBlocks.add(Block.torchWood.blockID);
    	vanillaPermeableBlocks.add(Block.torchRedstoneActive.blockID);
    	vanillaPermeableBlocks.add(Block.torchRedstoneIdle.blockID);
    	vanillaPermeableBlocks.add(Block.bed.blockID);
    	vanillaPermeableBlocks.add(Block.blockSnow.blockID);
    	vanillaPermeableBlocks.add(Block.anvil.blockID);
    	vanillaPermeableBlocks.add(Block.fence.blockID);
    	vanillaPermeableBlocks.add(Block.fenceGate.blockID);
    	vanillaPermeableBlocks.add(Block.fenceIron.blockID);
    	vanillaPermeableBlocks.add(Block.crops.blockID);
    	vanillaPermeableBlocks.add(Block.stoneSingleSlab.blockID);
    	vanillaPermeableBlocks.add(Block.woodSingleSlab.blockID);
    	vanillaPermeableBlocks.add(Block.ladder.blockID);
    	vanillaPermeableBlocks.add(Block.flowerPot.blockID);
    	vanillaPermeableBlocks.add(Block.tallGrass.blockID);
    	vanillaPermeableBlocks.add(Block.melonStem.blockID);
    	vanillaPermeableBlocks.add(Block.pressurePlateGold.blockID);
    	vanillaPermeableBlocks.add(Block.pressurePlateIron.blockID);
    	vanillaPermeableBlocks.add(Block.pressurePlatePlanks.blockID);
    	vanillaPermeableBlocks.add(Block.pressurePlateStone.blockID);
    	vanillaPermeableBlocks.add(Block.woodenButton.blockID);
    	vanillaPermeableBlocks.add(Block.stoneButton.blockID);
    	vanillaPermeableBlocks.add(Block.waterlily.blockID);
    	vanillaPermeableBlocks.add(Block.sapling.blockID);
    	vanillaPermeableBlocks.add(Block.redstoneComparatorActive.blockID);
    	vanillaPermeableBlocks.add(Block.redstoneComparatorIdle.blockID);
    	vanillaPermeableBlocks.add(Block.redstoneRepeaterActive.blockID);
    	vanillaPermeableBlocks.add(Block.redstoneRepeaterIdle.blockID);
    	vanillaPermeableBlocks.add(Block.daylightSensor.blockID);
    	vanillaPermeableBlocks.add(Block.redstoneWire.blockID);
    	vanillaPermeableBlocks.add(Block.stairsStoneBrick.blockID);
    	vanillaPermeableBlocks.add(Block.stairsBrick.blockID);
    	vanillaPermeableBlocks.add(Block.stairsNetherBrick.blockID);
    	vanillaPermeableBlocks.add(Block.stairsCobblestone.blockID);
    	vanillaPermeableBlocks.add(Block.stairsWoodOak.blockID);
    	vanillaPermeableBlocks.add(Block.waterStill.blockID);
    	vanillaPermeableBlocks.add(Block.waterMoving.blockID);
    }

    private void loopThrough(World var1, int var2, int var3, int var4, int var5)
    {
        this.checked.add(new Vector3(var2, var3, var4));

        if (this.touchingUnsealedBlock(var1, var2, var3, var4) && this.airtight)
        {
            if (var5 > 0)
            {
                this.nextVec(var1, var2, var3, var4, var5);
            }
            else if (!this.getIsSealed(var1, var2, var3, var4))
            {
                this.airtight = false;
            }
        }
    }

    private void checkAtVec(World var1, Vector3 vec)
    {
        this.checked.add(vec);

        if (this.isTouchingBreathableAir(var1, vec.intX(), vec.intY(), vec.intZ()))
        {
            this.nextVecD(var1, vec.intX(), vec.intY(), vec.intZ());
        }
    }

    private void nextVec(World var1, int var2, int var3, int var4, int var5)
    {
    	for (ForgeDirection dir : ForgeDirection.values())
    	{
    		if (dir != ForgeDirection.UNKNOWN)
    		{
    			Vector3 vec = new Vector3(var2, var3, var4);
    			vec = vec.add(new Vector3(dir));
    			
    			if (this.canBlockPass(var1, vec) && !this.isVisited(vec))
    			{
    				this.loopThrough(var1, vec.intX(), vec.intY(), vec.intZ(), var5 - 1);
    			}
    		}
    	}
    }

    private void nextVecD(World var1, int var2, int var3, int var4)
    {
    	for (ForgeDirection dir : ForgeDirection.values())
    	{
    		if (dir != ForgeDirection.UNKNOWN)
    		{
    			Vector3 vec = new Vector3(var2, var3, var4);
    			vec = vec.add(new Vector3(dir));
    			
    			if (this.isBreathableAir(var1, vec.intX(), vec.intY(), vec.intZ()) && !this.isVisited(vec))
    			{
    				this.checkAtVec(var1, vec);
    			}
    		}
    	}
    }

    public boolean seal(World var1, int var2, int var3, int var4, int var5)
    {
        this.airtight = true;
        this.nextVec(var1, var2, var3, var4, var5);

        if (this.airtight)
        {
            Iterator var6 = this.checked.iterator();
            Vector3 var7;

            while (var6.hasNext())
            {
                var7 = (Vector3)var6.next();

                if (!this.getIsSealed(var1, var7.intX(), var7.intY(), var7.intZ()))
                {
                    this.airtight = false;
                }
            }

            var6 = this.checked.iterator();

            while (var6.hasNext())
            {
                var7 = (Vector3)var6.next();

                if (var7.getBlockID(var1) == 0)
                {
                    if (var1.getBlockId(var7.intX(), var7.intY() + 1, var7.intZ()) == 64)
                    {
                        var1.setBlock(var7.intX(), var7.intY() + 1, var7.intZ(), GCCoreBlocks.breatheableAir.blockID);
                    }

                    if (var1.getBlockId(var7.intX(), var7.intY() - 1, var7.intZ()) == 64 && var1.getBlockId(var7.intX(), var7.intY() - 2, var7.intZ()) != 64)
                    {
                        var1.setBlock(var7.intX(), var7.intY() - 1, var7.intZ(), GCCoreBlocks.breatheableAir.blockID);
                    }

                    var1.setBlock(var7.intX(), var7.intY(), var7.intZ(), GCCoreBlocks.breatheableAir.blockID, 0, 2);
                }
            }
        }

        this.checked = new LinkedList();
        return this.airtight;
    }

    public boolean checkSeal(World var1, int var2, int var3, int var4, int var5)
    {
        this.airtight = true;

        if (var1.getBlockMetadata(var2, var3, var4) != 100)
        {
            this.nextVec(var1, var2, var3, var4, var5);
        }

        if (this.airtight)
        {
            Iterator var6 = this.checked.iterator();

            while (var6.hasNext())
            {
            	Vector3 var7 = (Vector3)var6.next();

                if (!this.getIsSealed(var1, var7.intX(), var7.intY(), var7.intZ()))
                {
                    this.airtight = false;
                }
            }
        }

        this.checked = new LinkedList();
        return this.airtight;
    }

    public void unSeal(World var1, int var2, int var3, int var4)
    {
        this.nextVecD(var1, var2, var3, var4);
        Iterator var5 = this.checked.iterator();
        Vector3 var6;

        while (var5.hasNext())
        {
            var6 = (Vector3)var5.next();

            if (canBlockPass(var1, var6.intX(), var6.intY(), var6.intZ()))
            {
                int var9 = var1.getBlockId(var6.intX(), var6.intY(), var6.intZ());

                Block.blocksList[var1.getBlockId(var6.intX(), var6.intY(), var6.intZ())].dropBlockAsItem(var1, var6.intX(), var6.intY(), var6.intZ(), var1.getBlockMetadata(var6.intX(), var6.intY(), var6.intZ()), 0);
                var1.setBlock(var6.intX(), var6.intY(), var6.intZ(), 0, 0, 2);
            }
        }

        var5 = this.checked.iterator();

        while (var5.hasNext())
        {
            var6 = (Vector3)var5.next();

            if (var1.getBlockId(var6.intX(), var6.intY(), var6.intZ()) == GCCoreBlocks.breatheableAir.blockID)
            {
                var1.setBlock(var6.intX(), var6.intY(), var6.intZ(), 0, 0, 2);
            }
        }

        var5 = this.checked.iterator();

        while (var5.hasNext())
        {
            var6 = (Vector3)var5.next();

            if (var6.getBlockID(var1) == 0)
            {
                var1.notifyBlocksOfNeighborChange(var6.intX(), var6.intY(), var6.intZ(), 0);
            }
        }

        this.checked = new LinkedList();
    }

    public boolean canBlockPass(World var0, int var1, int var2, int var3)
    {
    	Block block = Block.blocksList[var0.getBlockId(var1, var2, var3)];

    	return block == null
    			|| block.blockID == 0
    			|| block.blockID == GCCoreBlocks.breatheableAir.blockID
    			|| (!block.isOpaqueCube() && !(block instanceof IPartialSealedBlock) && this.vanillaPermeableBlocks.contains(block.blockID))
    			|| (!block.isOpaqueCube() && block instanceof IPartialSealedBlock && !((IPartialSealedBlock) block).isSealed(var0, var1, var2, var3));
    }

    public boolean canBlockPass(World var0, Vector3 vec)
    {
    	Block block = Block.blocksList[vec.getBlockID(var0)];
    	
    	return block == null
    			|| block.blockID == 0 
    			|| block.blockID == GCCoreBlocks.breatheableAir.blockID
    			|| (!block.isOpaqueCube() && !(block instanceof IPartialSealedBlock) && this.vanillaPermeableBlocks.contains(block.blockID))
    			|| (!block.isOpaqueCube() && block instanceof IPartialSealedBlock && !((IPartialSealedBlock) block).isSealed(var0, vec.intX(), vec.intY(), vec.intZ()));
    }

    public static boolean isBreathableAir(World var0, int var1, int var2, int var3)
    {
        return var0.getBlockId(var1, var2, var3) == GCCoreBlocks.breatheableAir.blockID;
    }

    private boolean touchingUnsealedBlock(World var1, int var2, int var3, int var4)
    {
    	for (ForgeDirection dir : ForgeDirection.values())
    	{
    		if (dir != ForgeDirection.UNKNOWN)
    		{
    			Vector3 vec = new Vector3(var2, var3, var4);
    			vec = vec.add(new Vector3(dir));
    			
    			if (this.canBlockPass(var1, vec.intX(), vec.intY(), vec.intZ()) && !this.isVisited(vec))
    			{
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }

    private boolean getIsSealed(World var1, int var2, int var3, int var4)
    {
    	for (ForgeDirection dir : ForgeDirection.values())
    	{
    		if (dir != ForgeDirection.UNKNOWN)
    		{
    			Vector3 vec = new Vector3(var2, var3, var4);
    			vec = vec.add(new Vector3(dir));
    			
    			if (vec.getBlockID(var1) == 0 && !this.isVisited(vec))
    			{
    				return false;
    			}
    		}
    	}
    	
    	return true;
    }

    private boolean isTouchingBreathableAir(World var1, int var2, int var3, int var4)
    {
    	for (ForgeDirection dir : ForgeDirection.values())
    	{
    		if (dir != ForgeDirection.UNKNOWN)
    		{
    			Vector3 vec = new Vector3(var2, var3, var4);
    			vec = vec.add(new Vector3(dir));
    			
    			if (this.isBreathableAir(var1, vec.intX(), vec.intY(), vec.intZ()) && !this.isVisited(vec))
    			{
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }

    private boolean isVisited(Vector3 var1)
    {
        Iterator var2 = this.checked.iterator();
        Vector3 var3;
        
        for (Vector3 vec : this.checked)
        {
        	if (vec.equals(var1))
        	{
        		return true;
        	}
        }
        
        return false;
    }
}
