package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFluidClassic extends BlockFluidRoot
{
    protected boolean[] isOptimalFlowDirection = new boolean[4];
    protected int[] flowCost = new int[4];

    public BlockFluidClassic(int id, Material material)
    {

        super(id, material);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z)
    {

        int bId = world.getBlockId(x, y, z);
        if (bId == 0)
        {
            return true;
        }
        if (bId == this.blockID)
        {
            return false;
        }
        if (BlockFluidRoot.displacementIds.containsKey(bId))
        {
            return BlockFluidRoot.displacementIds.get(bId);
        }
        Material material = Block.blocksList[bId].blockMaterial;
        if (material.blocksMovement() || material == Material.water || material == Material.lava || material == Material.portal)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z)
    {

        int bId = world.getBlockId(x, y, z);
        if (bId == 0)
        {
            return true;
        }
        if (bId == this.blockID)
        {
            return false;
        }
        if (BlockFluidRoot.displacementIds.containsKey(bId))
        {
            if (BlockFluidRoot.displacementIds.get(bId))
            {
                Block.blocksList[bId].dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                return true;
            }
            return false;
        }
        Material material = Block.blocksList[bId].blockMaterial;
        if (material.blocksMovement() || material == Material.water || material == Material.lava || material == Material.portal)
        {
            return false;
        }
        Block.blocksList[bId].dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        return true;
    }

    public boolean isFlowingVertically(IBlockAccess world, int x, int y, int z)
    {
        return world.getBlockId(x, y + this.densityDir, z) == this.blockID || world.getBlockId(x, y, z) == this.blockID && this.canFlowInto(world, x, y + this.densityDir, z);
    }

    public boolean isSourceBlock(World world, int x, int y, int z)
    {
        return world.getBlockId(x, y, z) == this.blockID && world.getBlockMetadata(x, y, z) + 1 == this.quantaPerBlock;
    }

    protected void updateFlowLevel(World world, int x, int y, int z, int quantaRemaining)
    {

    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving living, ItemStack theItem)
    {
        world.setBlock(x, y, z, this.blockID, this.quantaPerBlock - 1, 3);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        int quantaRemaining = world.getBlockMetadata(x, y, z) + 1;
        int expQuanta = -101;

        if (quantaRemaining < this.quantaPerBlock)
        {
            int y2 = y - this.densityDir;

            if (world.getBlockId(x, y2, z) == this.blockID || world.getBlockId(x - 1, y2, z) == this.blockID || world.getBlockId(x + 1, y2, z) == this.blockID || world.getBlockId(x, y2, z - 1) == this.blockID || world.getBlockId(x, y2, z + 1) == this.blockID)
            {
                expQuanta = this.quantaPerBlock - 1;

            }
            else
            {
                int maxQuanta = -100;
                maxQuanta = this.getLargerQuanta(world, x - 1, y, z, maxQuanta);
                maxQuanta = this.getLargerQuanta(world, x + 1, y, z, maxQuanta);
                maxQuanta = this.getLargerQuanta(world, x, y, z - 1, maxQuanta);
                maxQuanta = this.getLargerQuanta(world, x, y, z + 1, maxQuanta);

                expQuanta = maxQuanta - 1;
            }
            
            if (expQuanta != quantaRemaining)
            {
                quantaRemaining = expQuanta;
                
                if (expQuanta <= 0)
                {
                    world.setBlockToAir(x, y, z);
                }
                else
                {
                    world.setBlockMetadataWithNotify(x, y, z, expQuanta - 1, 3);
                    world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate);
                    world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
                }
            }
        }
        else if (quantaRemaining > this.quantaPerBlock)
        {
            world.setBlockMetadataWithNotify(x, y, z, this.quantaPerBlock - 1, 3);
        }

        if (this.canDisplace(world, x, y + this.densityDir, z))
        {
            this.flowIntoBlock(world, x, y + this.densityDir, z, this.quantaPerBlock - 2);
            return;
        }

        int flowMeta = quantaRemaining - 2;
        
        if (flowMeta < 0)
        {
            return;
        }

        if (this.isSourceBlock(world, x, y, z) || !this.isFlowingVertically(world, x, y, z))
        {
            if (world.getBlockId(x, y - this.densityDir, z) == this.blockID)
            {
                flowMeta = this.quantaPerBlock - 2;
            }

            boolean flowTo[] = this.getOptimalFlowDirections(world, x, y, z);

            if (flowTo[0])
            {
                this.flowIntoBlock(world, x - 1, y, z, flowMeta);
            }
            if (flowTo[1])
            {
                this.flowIntoBlock(world, x + 1, y, z, flowMeta);
            }
            if (flowTo[2])
            {
                this.flowIntoBlock(world, x, y, z - 1, flowMeta);
            }
            if (flowTo[3])
            {
                this.flowIntoBlock(world, x, y, z + 1, flowMeta);
            }
        }
    }

    protected boolean[] getOptimalFlowDirections(World world, int x, int y, int z)
    {

        for (int side = 0; side < 4; side++)
        {
            this.flowCost[side] = 1000;

            int x2 = x;
            int y2 = y;
            int z2 = z;

            switch (side)
            {
            case 0:
                --x2;
                break;
            case 1:
                ++x2;
                break;
            case 2:
                --z2;
                break;
            case 3:
                ++z2;
                break;
            }

            if (!this.canFlowInto(world, x2, y2, z2) || this.isSourceBlock(world, x2, y2, z2))
            {
                continue;
            }
            if (this.canFlowInto(world, x2, y2 + this.densityDir, z2))
            {
                this.flowCost[side] = 0;
            }
            else
            {
                this.flowCost[side] = this.calculateFlowCost(world, x2, y2, z2, 1, side);
            }
        }

        int min = this.flowCost[0];
        
        for (int side = 1; side < 4; side++)
        {
            if (this.flowCost[side] < min)
            {
                min = this.flowCost[side];
            }
        }
        
        for (int side = 0; side < 4; side++)
        {
            this.isOptimalFlowDirection[side] = this.flowCost[side] == min;
        }
        
        return this.isOptimalFlowDirection;
    }

    protected int calculateFlowCost(World world, int x, int y, int z, int recurseDepth, int side)
    {
        int cost = 1000;

        for (int adjSide = 0; adjSide < 4; adjSide++)
        {
            if (adjSide == 0 && side == 1 || adjSide == 1 && side == 0 || adjSide == 2 && side == 3 || adjSide == 3 && side == 2)
            {
                continue;
            }

            int x2 = x;
            int y2 = y;
            int z2 = z;

            switch (adjSide)
            {
            case 0:
                --x2;
                break;
            case 1:
                ++x2;
                break;
            case 2:
                --z2;
                break;
            case 3:
                ++z2;
                break;
            }

            if (!this.canFlowInto(world, x2, y2, z2) || this.isSourceBlock(world, x2, y2, z2))
            {
                continue;
            }
            
            if (this.canFlowInto(world, x2, y2 + this.densityDir, z2))
            {
                return recurseDepth;
            }
            
            if (recurseDepth >= 4)
            {
                continue;
            }
            
            int min = this.calculateFlowCost(world, x2, y2, z2, recurseDepth + 1, adjSide);
            if (min < cost)
            {
                cost = min;
            }
        }
        
        return cost;
    }

    protected void flowIntoBlock(World world, int x, int y, int z, int meta)
    {
        if (this.displaceIfPossible(world, x, y, z))
        {
            world.setBlock(x, y, z, this.blockID, meta, 3);
        }
    }

    protected boolean canFlowInto(IBlockAccess world, int x, int y, int z)
    {
        int bId = world.getBlockId(x, y, z);
        
        if (bId == 0)
        {
            return true;
        }
        
        if (bId == this.blockID)
        {
            return true;
        }
        
        if (BlockFluidRoot.displacementIds.containsKey(bId))
        {
            return BlockFluidRoot.displacementIds.get(bId);
        }
        
        Material material = Block.blocksList[bId].blockMaterial;
        
        if (material.blocksMovement() || material == Material.water || material == Material.lava || material == Material.portal)
        {
            return false;
        }
        
        return true;
    }

    protected int getLargerQuanta(IBlockAccess world, int x, int y, int z, int compare)
    {
        int quantaRemaining = this.getQuantaValue(world, x, y, z);

        if (quantaRemaining <= 0)
        {
            return compare;
        }
        
        return quantaRemaining >= compare ? quantaRemaining : compare;
    }

}
