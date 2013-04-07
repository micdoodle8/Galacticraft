package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.Iterator;
import java.util.LinkedList;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.common.FMLLog;

public class OxygenPressureProtocol
{
    private LinkedList<Vector3> checked = new LinkedList<Vector3>();
    private boolean airtight;

    public OxygenPressureProtocol(World var1) {}

    private void loopThrough(World var1, int var2, int var3, int var4, int var5)
    {
        this.checked.add(new Vector3(var2, var3, var4));

        if (this.isTouchingAir(var1, var2, var3, var4) && this.airtight)
        {
            if (var5 > 0)
            {
                this.nextNodes(var1, var2, var3, var4, var5);
            }
            else if (!this.getIsSealed(var1, var2, var3, var4))
            {
                this.airtight = false;
            }
        }
    }

    private void check(World var1, Vector3 vec)
    {
        this.checked.add(vec);

        if (this.isTouchingAirD(var1, vec.intX(), vec.intY(), vec.intZ()))
        {
            this.nextNodesD(var1, vec.intX(), vec.intY(), vec.intZ());
        }
    }

    private void nextNodes(World var1, int var2, int var3, int var4, int var5)
    {
    	for (ForgeDirection dir : ForgeDirection.values())
    	{
    		if (dir != ForgeDirection.UNKNOWN)
    		{
    			Vector3 vec = new Vector3(var2, var3, var4);
    			vec = vec.add(new Vector3(dir));
    			
    			if (this.isPermeable(var1, vec) && !this.isVisited(vec))
    			{
    				this.loopThrough(var1, vec.intX(), vec.intY(), vec.intZ(), var5 - 1);
    			}
    		}
    	}
    }

    private void nextNodesD(World var1, int var2, int var3, int var4)
    {
    	for (ForgeDirection dir : ForgeDirection.values())
    	{
    		if (dir != ForgeDirection.UNKNOWN)
    		{
    			Vector3 vec = new Vector3(var2, var3, var4);
    			vec = vec.add(new Vector3(dir));
    			
    			if (this.isPermeableD(var1, vec.intX(), vec.intY(), vec.intZ()) && !this.isVisited(vec))
    			{
    				this.check(var1, vec);
    			}
    		}
    	}
    }

    public boolean scrub(World var1, int var2, int var3, int var4, int var5)
    {
        this.airtight = true;
        this.nextNodes(var1, var2, var3, var4, var5);

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

    public boolean checkCompression(World var1, int var2, int var3, int var4, int var5)
    {
        this.airtight = true;

        if (var1.getBlockMetadata(var2, var3, var4) != 100)
        {
            this.nextNodes(var1, var2, var3, var4, var5);
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

    public void decompress(World var1, int var2, int var3, int var4)
    {
        this.nextNodesD(var1, var2, var3, var4);
        Iterator var5 = this.checked.iterator();
        Vector3 var6;

        while (var5.hasNext())
        {
            var6 = (Vector3)var5.next();

            if (isPermeable(var1, var6.intX(), var6.intY(), var6.intZ()))
            {
                int var9 = var1.getBlockId(var6.intX(), var6.intY(), var6.intZ());

//                Block.blocksList[var1.getBlockId(var6.intX(), var6.intY(), var6.intZ())].dropBlockAsItem(var1, var6.intX(), var6.intY(), var6.intZ(), var1.getBlockMetadata(var6.intX(), var6.intY(), var6.intZ()), 0);
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

    public static boolean isPermeable(World var0, int var1, int var2, int var3)
    {
    	return var0.getBlockId(var1, var3, var3) == 0 || var0.getBlockId(var1, var3, var3) == GCCoreBlocks.breatheableAir.blockID;
//        return var0.getBlockId(var1, var2, var3) == Block.pressurePlateStone.blockID || var0.getBlockId(var1, var2, var3) == Block.fenceIron.blockID || var0.getBlockId(var1, var2, var3) == Block.fenceIron.blockID || var0.getBlockId(var1, var2, var3) == Block.fenceIron.blockID || var0.getBlockId(var1, var2, var3) == Block.fenceIron.blockID || var0.getBlockId(var1, var2, var3) == Block.fenceIron.blockID || var0.getBlockId(var1, var2, var3) == Block.doorSteel.blockID && var0.getBlockMetadata(var1, var2, var3) == 4 || var0.getBlockId(var1, var2, var3) == Block.doorSteel.blockID && var0.getBlockMetadata(var1, var2, var3) == 5 || var0.getBlockId(var1, var2, var3) == Block.doorSteel.blockID && var0.getBlockMetadata(var1, var2, var3) == 6 || var0.getBlockId(var1, var2, var3) == Block.doorSteel.blockID && var0.getBlockMetadata(var1, var2, var3) == 7 || var0.getBlockId(var1, var2, var3) == Block.doorSteel.blockID && var0.getBlockMetadata(var1, var2, var3) == 12 || var0.getBlockId(var1, var2, var3) != 0 && var0.getBlockMaterial(var1, var2, var3) != Material.rock && var0.getBlockMaterial(var1, var2, var3) != Material.iron && var0.getBlockMaterial(var1, var2, var3) != Material.glass && var0.getBlockMaterial(var1, var2, var3) != Material.ice;
    }

    public static boolean isPermeable(World var0, Vector3 vec)
    {
    	return vec.getBlockID(var0) == 0 || vec.getBlockID(var0) == GCCoreBlocks.breatheableAir.blockID;
//        return vec.getBlockID(var0) == Block.pressurePlateStone.blockID 
//        		|| vec.getBlockID(var0) == Block.fenceIron.blockID 
//        		|| vec.getBlockID(var0) == Block.fenceIron.blockID 
//        		|| vec.getBlockID(var0) == Block.fenceIron.blockID 
//        		|| vec.getBlockID(var0) == Block.fenceIron.blockID 
//        		|| vec.getBlockID(var0) == Block.fenceIron.blockID 
//        		|| vec.getBlockID(var0) == Block.doorSteel.blockID && vec.getBlockMetadata(var0) == 4 
//        		|| vec.getBlockID(var0) == Block.doorSteel.blockID && vec.getBlockMetadata(var0) == 5 
//        		|| vec.getBlockID(var0) == Block.doorSteel.blockID && vec.getBlockMetadata(var0) == 6 
//        		|| vec.getBlockID(var0) == Block.doorSteel.blockID && vec.getBlockMetadata(var0) == 7 
//        		|| vec.getBlockID(var0) == Block.doorSteel.blockID && vec.getBlockMetadata(var0) == 12 
//        		|| vec.getBlockID(var0) == 0 && var0.getBlockMaterial(vec.intX(), vec.intY(), vec.intZ()) != Material.rock && var0.getBlockMaterial(vec.intX(), vec.intY(), vec.intZ()) != Material.iron && var0.getBlockMaterial(vec.intX(), vec.intY(), vec.intZ()) != Material.glass && var0.getBlockMaterial(vec.intX(), vec.intY(), vec.intZ()) != Material.ice;
    }

    public static boolean isPermeableD(World var0, int var1, int var2, int var3)
    {
        return var0.getBlockId(var1, var2, var3) == GCCoreBlocks.breatheableAir.blockID;
    }

    private boolean isTouchingAir(World var1, int var2, int var3, int var4)
    {
    	for (ForgeDirection dir : ForgeDirection.values())
    	{
    		if (dir != ForgeDirection.UNKNOWN)
    		{
    			Vector3 vec = new Vector3(var2, var3, var4);
    			vec = vec.add(new Vector3(dir));
    			
    			if (this.isPermeable(var1, vec.intX(), vec.intY(), vec.intZ()) && !this.isVisited(vec.intX(), vec.intY(), vec.intZ()))
    			{
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }

    private boolean getIsSealed(World var1, int var2, int var3, int var4)
    {
        return (var1.getBlockId(var2 + 1, var3, var4) != 0 
        		|| this.isVisited(var2 + 1, var3, var4)) && (var1.getBlockId(var2 - 1, var3, var4) != 0
        		|| this.isVisited(var2 - 1, var3, var4)) && (var1.getBlockId(var2, var3 + 1, var4) != 0
        		|| this.isVisited(var2, var3 + 1, var4)) && (var1.getBlockId(var2, var3 - 1, var4) != 0
        		|| this.isVisited(var2, var3 - 1, var4)) && (var1.getBlockId(var2, var3, var4 + 1) != 0
        		|| this.isVisited(var2, var3, var4 + 1)) && (var1.getBlockId(var2, var3, var4 - 1) != 0
        		|| this.isVisited(var2, var3, var4 - 1));
    }

    private boolean isTouchingAirD(World var1, int var2, int var3, int var4)
    {
        return isPermeableD(var1, var2 + 1, var3, var4) && !this.isVisited(var2 + 1, var3, var4) 
        		|| isPermeableD(var1, var2 - 1, var3, var4) && !this.isVisited(var2 - 1, var3, var4) 
        		|| isPermeableD(var1, var2, var3 + 1, var4) && !this.isVisited(var2, var3 + 1, var4) 
        		|| isPermeableD(var1, var2, var3 - 1, var4) && !this.isVisited(var2, var3 - 1, var4) 
        		|| isPermeableD(var1, var2, var3, var4 + 1) && !this.isVisited(var2, var3, var4 + 1) 
        		|| isPermeableD(var1, var2, var3, var4 - 1) && !this.isVisited(var2, var3, var4 - 1);
    }

    private boolean isVisited(int var1, int var2, int var3)
    {
        return this.isVisited(new Vector3(var1, var2, var3));
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
