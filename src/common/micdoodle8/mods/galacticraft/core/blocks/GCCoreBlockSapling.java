package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.wgen.GCCoreWorldGenForest;
import micdoodle8.mods.galacticraft.core.wgen.GCCoreWorldGenTaiga2;
import micdoodle8.mods.galacticraft.core.wgen.GCCoreWorldGenTrees;
import net.minecraft.src.Block;
import net.minecraft.src.BlockSapling;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

public class GCCoreBlockSapling extends BlockSapling
{
	protected GCCoreBlockSapling(int par1, int par2) 
	{
		super(par1, par2);
		this.setRequiresSelfNotify();
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
            super.updateTick(par1World, par2, par3, par4, par5Random);
            
            int waterBlocksNearby = 0;
            
            for (int i = -4; i < 5; i++)
            {
                for (int j = -4; j < 5; j++)
                {
                    if(par1World.getBlockId(par2 + i, par3 - 1, par4 + j) == Block.waterMoving.blockID || par1World.getBlockId(par2 + i, par3 - 1, par4 + j) == Block.waterStill.blockID)
                    {
                    	waterBlocksNearby++;
                    }
                }
            }
            
            if (!(waterBlocksNearby > 3))
            {
            	par1World.setBlock(par2, par3, par4, Block.deadBush.blockID);
            }

            if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9 && par5Random.nextInt(7) == 0)
            {
            	
                final int var6 = par1World.getBlockMetadata(par2, par3, par4);

                if ((var6 & 8) == 0)
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, var6 | 8);
                }
                else
                {
                    this.growTree(par1World, par2, par3, par4, par5Random);
                }
            }
        }
    }
    
	@Override
    public void growTree(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        final int var6 = par1World.getBlockMetadata(par2, par3, par4) & 3;
        Object var7 = null;
        final int var8 = 0;
        final int var9 = 0;
        final boolean var10 = false;
		
		if (var6 == 1)
        {
            var7 = new GCCoreWorldGenTaiga2(true);
        }
        else if (var6 == 2)
        {
            var7 = new GCCoreWorldGenForest(true);
        }
		// No jungle trees...
        else
        {
            var7 = new GCCoreWorldGenTrees(true);
        }

        if (var10)
        {
            par1World.setBlock(par2 + var8, par3, par4 + var9, 0);
            par1World.setBlock(par2 + var8 + 1, par3, par4 + var9, 0);
            par1World.setBlock(par2 + var8, par3, par4 + var9 + 1, 0);
            par1World.setBlock(par2 + var8 + 1, par3, par4 + var9 + 1, 0);
        }
        else
        {
            par1World.setBlock(par2, par3, par4, 0);
        }

        if (!((WorldGenerator)var7).generate(par1World, par5Random, par2 + var8, par3, par4 + var9))
        {
            if (var10)
            {
                par1World.setBlockAndMetadata(par2 + var8, par3, par4 + var9, this.blockID, var6);
                par1World.setBlockAndMetadata(par2 + var8 + 1, par3, par4 + var9, this.blockID, var6);
                par1World.setBlockAndMetadata(par2 + var8, par3, par4 + var9 + 1, this.blockID, var6);
                par1World.setBlockAndMetadata(par2 + var8 + 1, par3, par4 + var9 + 1, this.blockID, var6);
            }
            else
            {
                par1World.setBlockAndMetadata(par2, par3, par4, this.blockID, var6);
            }
        }
    }
}
