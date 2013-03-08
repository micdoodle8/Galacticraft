package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;

public class GCCoreBlockCrudeOilMoving extends BlockFluid implements ILiquid 
{
	int numAdjacentSources = 0;
	boolean isOptimalFlowDirection[] = new boolean[4];
	int flowCost[] = new int[4];

	public GCCoreBlockCrudeOilMoving(int i, Material material) 
	{
		super(i, material);
		setHardness(100F);
		setLightOpacity(3);
		this.blockIndexInTexture = 237;
	}

	private void updateFlow(World world, int i, int j, int k) 
	{
		int l = world.getBlockMetadata(i, j, k);
		world.setBlockAndMetadata(i, j, k, blockID + 1, l);
		world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
		world.markBlockForUpdate(i, j, k);
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) 
	{
		int l = getFlowDecay(world, i, j, k);
		byte byte0 = 1;
		boolean flag = true;
		
		if (l > 0) 
		{
			int i1 = -100;
			numAdjacentSources = 0;
			i1 = getSmallestFlowDecay(world, i - 1, j, k, i1);
			i1 = getSmallestFlowDecay(world, i + 1, j, k, i1);
			i1 = getSmallestFlowDecay(world, i, j, k - 1, i1);
			i1 = getSmallestFlowDecay(world, i, j, k + 1, i1);
			int j1 = i1 + byte0;
			
			if (j1 >= 8 || i1 < 0) 
			{
				j1 = -1;
			}
			if (getFlowDecay(world, i, j + 1, k) >= 0) 
			{
				int l1 = getFlowDecay(world, i, j + 1, k);
				
				if (l1 >= 8) 
				{
					j1 = l1;
				} 
				else 
				{
					j1 = l1 + 8;
				}
			}
			if (j1 != l) 
			{
				l = j1;
				
				if (l < 0) 
				{
					world.setBlockWithNotify(i, j, k, 0);
				} 
				else 
				{
					world.setBlockMetadataWithNotify(i, j, k, l);
					world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
					world.notifyBlocksOfNeighborChange(i, j, k, blockID);
				}
			} 
			else if (flag) 
			{
				updateFlow(world, i, j, k);
			}
		} 
		else 
		{
			updateFlow(world, i, j, k);
		}
		
		if (liquidCanDisplaceBlock(world, i, j - 1, k)) 
		{
			if (l >= 8) 
			{
				world.setBlockAndMetadataWithNotify(i, j - 1, k, blockID, l);
			} 
			else 
			{
				world.setBlockAndMetadataWithNotify(i, j - 1, k, blockID, l + 8);
			}
		} 
		else if (l >= 0 && (l == 0 || blockBlocksFlow(world, i, j - 1, k))) 
		{
			boolean aflag[] = getOptimalFlowDirections(world, i, j, k);
			int k1 = l + byte0;
			
			if (l >= 8) 
			{
				k1 = 1;
			}
			
			if (k1 >= 8)
			{
				return;
			}
			
			if (aflag[0]) 
			{
				flowIntoBlock(world, i - 1, j, k, k1);
			}
			
			if (aflag[1]) 
			{
				flowIntoBlock(world, i + 1, j, k, k1);
			}
			
			if (aflag[2]) 
			{
				flowIntoBlock(world, i, j, k - 1, k1);
			}
			
			if (aflag[3])
			{
				flowIntoBlock(world, i, j, k + 1, k1);
			}
		}
	}

	private void flowIntoBlock(World world, int i, int j, int k, int l) 
	{
		if (liquidCanDisplaceBlock(world, i, j, k)) 
		{
			int i1 = world.getBlockId(i, j, k);
			
			if (i1 > 0)
			{
				Block.blocksList[i1].dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			}
			
			world.setBlockAndMetadataWithNotify(i, j, k, blockID, l);
		}
	}

	private int calculateFlowCost(World world, int i, int j, int k, int l, int i1) 
	{
		int j1 = 1000;
		
		for (int k1 = 0; k1 < 4; k1++) 
		{
			if (k1 == 0 && i1 == 1 || k1 == 1 && i1 == 0 || k1 == 2 && i1 == 3 || k1 == 3 && i1 == 2) 
			{
				continue;
			}
			
			int l1 = i;
			int i2 = j;
			int j2 = k;
			
			if (k1 == 0) 
			{
				l1--;
			}
			
			if (k1 == 1) 
			{
				l1++;
			}
			
			if (k1 == 2) 
			{
				j2--;
			}
			
			if (k1 == 3) 
			{
				j2++;
			}
			
			if (blockBlocksFlow(world, l1, i2, j2) || world.getBlockMaterial(l1, i2, j2) == blockMaterial && world.getBlockMetadata(l1, i2, j2) == 0)
			{
				continue;
			}
			if (!blockBlocksFlow(world, l1, i2 - 1, j2))
			{
				return l;
			}
			
			if (l >= 4) 
			{
				continue;
			}
			
			int k2 = calculateFlowCost(world, l1, i2, j2, l + 1, k1);
			
			if (k2 < j1) 
			{
				j1 = k2;
			}
		}

		return j1;
	}

	private boolean[] getOptimalFlowDirections(World world, int i, int j, int k) 
	{
		for (int l = 0; l < 4; l++) 
		{
			flowCost[l] = 1000;
			int j1 = i;
			int i2 = j;
			int j2 = k;
			
			if (l == 0) 
			{
				j1--;
			}
			
			if (l == 1) 
			{
				j1++;
			}
			
			if (l == 2) 
			{
				j2--;
			}
			
			if (l == 3) 
			{
				j2++;
			}
			
			if (blockBlocksFlow(world, j1, i2, j2) || world.getBlockMaterial(j1, i2, j2) == blockMaterial && world.getBlockMetadata(j1, i2, j2) == 0) 
			{
				continue;
			}
			
			if (!blockBlocksFlow(world, j1, i2 - 1, j2)) 
			{
				flowCost[l] = 0;
			} 
			else 
			{
				flowCost[l] = calculateFlowCost(world, j1, i2, j2, 1, l);
			}
		}

		int i1 = flowCost[0];
		for (int k1 = 1; k1 < 4; k1++) {
			if (flowCost[k1] < i1) {
				i1 = flowCost[k1];
			}
		}

		for (int l1 = 0; l1 < 4; l1++) {
			isOptimalFlowDirection[l1] = flowCost[l1] == i1;
		}

		return isOptimalFlowDirection;
	}

    public static double getFlowDirection(IBlockAccess par0IBlockAccess, int par1, int par2, int par3, Material par4Material)
    {
    	return 0.0D;
    }

	private boolean blockBlocksFlow(World world, int i, int j, int k) 
	{
		int l = world.getBlockId(i, j, k);
		
		if (l == Block.doorWood.blockID || l == Block.doorSteel.blockID || l == Block.signPost.blockID || l == Block.ladder.blockID || l == Block.reed.blockID)
		{
			return true;
		}
		if (l == 0)
		{
			return false;
		}
		
		Material material = Block.blocksList[l].blockMaterial;
		
		return material.isSolid();
	}

	protected int getSmallestFlowDecay(World world, int i, int j, int k, int l) 
	{
		int i1 = getFlowDecay(world, i, j, k);
		
		if (i1 < 0)
		{
			return l;
		}
		if (i1 >= 8) 
		{
			i1 = 0;
		}
		
		return l >= 0 && i1 >= l ? l : i1;
	}

	private boolean liquidCanDisplaceBlock(World world, int i, int j, int k) 
	{
		Material material = world.getBlockMaterial(i, j, k);
		
		if (material == blockMaterial)
		{
			return false;
		}
		else
		{
			return !blockBlocksFlow(world, i, j, k);
		}
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) 
	{
		super.onBlockAdded(world, i, j, k);
		if (world.getBlockId(i, j, k) == blockID) 
		{
			world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
		}
	}

	@Override
	public int stillLiquidId() 
	{
		return GCCoreBlocks.crudeOilStill.blockID;
	}

	@Override
	public boolean isMetaSensitive() 
	{
		return false;
	}

	@Override
	public int stillLiquidMeta() 
	{
		return 0;
	}

	@Override
	public boolean isBlockReplaceable(World world, int i, int j, int k) 
	{
		return true;
	}

	@Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getGCCrudeOilRenderID();
    }

	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int var6;

        if (this.blockMaterial == Material.water)
        {
            if (par5Random.nextInt(10) == 0)
            {
                var6 = par1World.getBlockMetadata(par2, par3, par4);

                if (var6 <= 0 || var6 >= 8)
                {
                    par1World.spawnParticle("suspended", (double)((float)par2 + par5Random.nextFloat()), (double)((float)par3 + par5Random.nextFloat()), (double)((float)par4 + par5Random.nextFloat()), 0.0D, 0.0D, 0.0D);
                }
            }

            for (var6 = 0; var6 < 0; ++var6)
            {
                int var7 = par5Random.nextInt(4);
                int var8 = par2;
                int var9 = par4;

                if (var7 == 0)
                {
                    var8 = par2 - 1;
                }

                if (var7 == 1)
                {
                    ++var8;
                }

                if (var7 == 2)
                {
                    var9 = par4 - 1;
                }

                if (var7 == 3)
                {
                    ++var9;
                }

                if (par1World.getBlockMaterial(var8, par3, var9) == Material.air && (par1World.getBlockMaterial(var8, par3 - 1, var9).blocksMovement() || par1World.getBlockMaterial(var8, par3 - 1, var9).isLiquid()))
                {
                    float var10 = 0.0625F;
                    double var11 = (double)((float)par2 + par5Random.nextFloat());
                    double var13 = (double)((float)par3 + par5Random.nextFloat());
                    double var15 = (double)((float)par4 + par5Random.nextFloat());

                    if (var7 == 0)
                    {
                        var11 = (double)((float)par2 - var10);
                    }

                    if (var7 == 1)
                    {
                        var11 = (double)((float)(par2 + 1) + var10);
                    }

                    if (var7 == 2)
                    {
                        var15 = (double)((float)par4 - var10);
                    }

                    if (var7 == 3)
                    {
                        var15 = (double)((float)(par4 + 1) + var10);
                    }

                    double var17 = 0.0D;
                    double var19 = 0.0D;

                    if (var7 == 0)
                    {
                        var17 = (double)(-var10);
                    }

                    if (var7 == 1)
                    {
                        var17 = (double)var10;
                    }

                    if (var7 == 2)
                    {
                        var19 = (double)(-var10);
                    }

                    if (var7 == 3)
                    {
                        var19 = (double)var10;
                    }

                    par1World.spawnParticle("splash", var11, var13, var15, var17, 0.0D, var19);
                }
            }
        }

        if (this.blockMaterial == Material.water && par5Random.nextInt(64) == 0)
        {
            var6 = par1World.getBlockMetadata(par2, par3, par4);

            if (var6 > 0 && var6 < 8)
            {
//                par1World.playSound((double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), "liquid.water", par5Random.nextFloat() * 0.25F + 0.75F, par5Random.nextFloat() * 1.0F + 1.5F, false);
            }
        }

        double var21;
        double var23;
        double var22;

        if (this.blockMaterial == Material.lava && par1World.getBlockMaterial(par2, par3 + 1, par4) == Material.air && !par1World.isBlockOpaqueCube(par2, par3 + 1, par4))
        {
            if (par5Random.nextInt(100) == 0)
            {
                var21 = (double)((float)par2 + par5Random.nextFloat());
                var22 = (double)par3 + this.maxY;
                var23 = (double)((float)par4 + par5Random.nextFloat());
                par1World.spawnParticle("lava", var21, var22, var23, 0.0D, 0.0D, 0.0D);
                par1World.playSound(var21, var22, var23, "liquid.lavapop", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
            }

            if (par5Random.nextInt(200) == 0)
            {
                par1World.playSound((double)par2, (double)par3, (double)par4, "liquid.lava", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
            }
        }

        if (par5Random.nextInt(10) == 0 && par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !par1World.getBlockMaterial(par2, par3 - 2, par4).blocksMovement())
        {
            var21 = (double)((float)par2 + par5Random.nextFloat());
            var22 = (double)par3 - 1.05D;
            var23 = (double)((float)par4 + par5Random.nextFloat());

            if (this.blockMaterial == Material.water)
            {
                par1World.spawnParticle("dripWater", var21, var22, var23, 0.0D, 0.0D, 0.0D);
            }
            else
            {
                par1World.spawnParticle("dripLava", var21, var22, var23, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}