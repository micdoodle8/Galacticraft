package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockCrudeOilMoving extends BlockFluid implements ILiquid
{
	int numAdjacentSources = 0;
	boolean isOptimalFlowDirection[] = new boolean[4];
	int flowCost[] = new int[4];
	
    @SideOnly(Side.CLIENT)
    public Icon[] fluidIcons;

	public GCCoreBlockCrudeOilMoving(int i, Material material)
	{
		super(i, material);
		this.setHardness(100F);
		this.setLightOpacity(3);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return par1 != 0 && par1 != 1 ? this.fluidIcons[1] : this.fluidIcons[0];
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.fluidIcons = new Icon[] {par1IconRegister.registerIcon("galacticraftcore:oil"), par1IconRegister.registerIcon("galacticraftcore:oil_flow")};
    }

    @SideOnly(Side.CLIENT)
    public static Icon func_94424_b(String par0Str)
    {
        return par0Str == "galacticraftcore:oil" ? GCCoreBlocks.crudeOilMoving.fluidIcons[0] : par0Str == "galacticraftcore:oil_flow" ? GCCoreBlocks.crudeOilMoving.fluidIcons[1] : null;
    }

	private void updateFlow(World world, int i, int j, int k)
	{
		final int l = world.getBlockMetadata(i, j, k);
		world.setBlock(i, j, k, this.blockID + 1, l, 3);
		world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
		world.markBlockForUpdate(i, j, k);
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		int l = this.getFlowDecay(world, i, j, k);
		final byte byte0 = 1;
		final boolean flag = true;

		if (l > 0)
		{
			int i1 = -100;
			this.numAdjacentSources = 0;
			i1 = this.getSmallestFlowDecay(world, i - 1, j, k, i1);
			i1 = this.getSmallestFlowDecay(world, i + 1, j, k, i1);
			i1 = this.getSmallestFlowDecay(world, i, j, k - 1, i1);
			i1 = this.getSmallestFlowDecay(world, i, j, k + 1, i1);
			int j1 = i1 + byte0;

			if (j1 >= 8 || i1 < 0)
			{
				j1 = -1;
			}
			if (this.getFlowDecay(world, i, j + 1, k) >= 0)
			{
				final int l1 = this.getFlowDecay(world, i, j + 1, k);

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
					world.setBlock(i, j, k, 0);
				}
				else
				{
					world.setBlockMetadataWithNotify(i, j, k, l, 3);
					world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
					world.notifyBlocksOfNeighborChange(i, j, k, this.blockID);
				}
			}
			else if (flag)
			{
				this.updateFlow(world, i, j, k);
			}
		}
		else
		{
			this.updateFlow(world, i, j, k);
		}

		if (this.liquidCanDisplaceBlock(world, i, j - 1, k))
		{
			if (l >= 8)
			{
				world.setBlock(i, j - 1, k, this.blockID, l, 3);
			}
			else
			{
				world.setBlock(i, j - 1, k, this.blockID, l + 8, 3);
			}
		}
		else if (l >= 0 && (l == 0 || this.blockBlocksFlow(world, i, j - 1, k)))
		{
			final boolean aflag[] = this.getOptimalFlowDirections(world, i, j, k);
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
				this.flowIntoBlock(world, i - 1, j, k, k1);
			}

			if (aflag[1])
			{
				this.flowIntoBlock(world, i + 1, j, k, k1);
			}

			if (aflag[2])
			{
				this.flowIntoBlock(world, i, j, k - 1, k1);
			}

			if (aflag[3])
			{
				this.flowIntoBlock(world, i, j, k + 1, k1);
			}
		}
	}

	private void flowIntoBlock(World world, int i, int j, int k, int l)
	{
		if (this.liquidCanDisplaceBlock(world, i, j, k))
		{
			final int i1 = world.getBlockId(i, j, k);

			if (i1 > 0)
			{
				Block.blocksList[i1].dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			}

			world.setBlock(i, j, k, this.blockID, l, 3);
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
			final int i2 = j;
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

			if (this.blockBlocksFlow(world, l1, i2, j2) || world.getBlockMaterial(l1, i2, j2) == this.blockMaterial && world.getBlockMetadata(l1, i2, j2) == 0)
			{
				continue;
			}
			if (!this.blockBlocksFlow(world, l1, i2 - 1, j2))
			{
				return l;
			}

			if (l >= 4)
			{
				continue;
			}

			final int k2 = this.calculateFlowCost(world, l1, i2, j2, l + 1, k1);

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
			this.flowCost[l] = 1000;
			int j1 = i;
			final int i2 = j;
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

			if (this.blockBlocksFlow(world, j1, i2, j2) || world.getBlockMaterial(j1, i2, j2) == this.blockMaterial && world.getBlockMetadata(j1, i2, j2) == 0)
			{
				continue;
			}

			if (!this.blockBlocksFlow(world, j1, i2 - 1, j2))
			{
				this.flowCost[l] = 0;
			}
			else
			{
				this.flowCost[l] = this.calculateFlowCost(world, j1, i2, j2, 1, l);
			}
		}

		int i1 = this.flowCost[0];
		for (int k1 = 1; k1 < 4; k1++) {
			if (this.flowCost[k1] < i1) {
				i1 = this.flowCost[k1];
			}
		}

		for (int l1 = 0; l1 < 4; l1++) {
			this.isOptimalFlowDirection[l1] = this.flowCost[l1] == i1;
		}

		return this.isOptimalFlowDirection;
	}

    public static double getFlowDirection(IBlockAccess par0IBlockAccess, int par1, int par2, int par3, Material par4Material)
    {
    	return 0.0D;
    }

	private boolean blockBlocksFlow(World world, int i, int j, int k)
	{
		final int l = world.getBlockId(i, j, k);

		if (l == Block.doorWood.blockID || l == Block.doorSteel.blockID || l == Block.signPost.blockID || l == Block.ladder.blockID || l == Block.reed.blockID)
		{
			return true;
		}
		if (l == 0)
		{
			return false;
		}

		final Material material = Block.blocksList[l].blockMaterial;

		return material.isSolid();
	}

	protected int getSmallestFlowDecay(World world, int i, int j, int k, int l)
	{
		int i1 = this.getFlowDecay(world, i, j, k);

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
		final Material material = world.getBlockMaterial(i, j, k);

		if (material == this.blockMaterial)
		{
			return false;
		}
		else
		{
			return !this.blockBlocksFlow(world, i, j, k);
		}
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k)
	{
		super.onBlockAdded(world, i, j, k);
		if (world.getBlockId(i, j, k) == this.blockID)
		{
			world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
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
                    par1World.spawnParticle("suspended", par2 + par5Random.nextFloat(), par3 + par5Random.nextFloat(), par4 + par5Random.nextFloat(), 0.0D, 0.0D, 0.0D);
                }
            }

            for (var6 = 0; var6 < 0; ++var6)
            {
                final int var7 = par5Random.nextInt(4);
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
                    final float var10 = 0.0625F;
                    double var11 = par2 + par5Random.nextFloat();
                    final double var13 = par3 + par5Random.nextFloat();
                    double var15 = par4 + par5Random.nextFloat();

                    if (var7 == 0)
                    {
                        var11 = par2 - var10;
                    }

                    if (var7 == 1)
                    {
                        var11 = par2 + 1 + var10;
                    }

                    if (var7 == 2)
                    {
                        var15 = par4 - var10;
                    }

                    if (var7 == 3)
                    {
                        var15 = par4 + 1 + var10;
                    }

                    double var17 = 0.0D;
                    double var19 = 0.0D;

                    if (var7 == 0)
                    {
                        var17 = -var10;
                    }

                    if (var7 == 1)
                    {
                        var17 = var10;
                    }

                    if (var7 == 2)
                    {
                        var19 = -var10;
                    }

                    if (var7 == 3)
                    {
                        var19 = var10;
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
                var21 = par2 + par5Random.nextFloat();
                var22 = par3 + this.maxY;
                var23 = par4 + par5Random.nextFloat();
                par1World.spawnParticle("lava", var21, var22, var23, 0.0D, 0.0D, 0.0D);
                par1World.playSound(var21, var22, var23, "liquid.lavapop", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
            }

            if (par5Random.nextInt(200) == 0)
            {
                par1World.playSound(par2, par3, par4, "liquid.lava", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
            }
        }

        if (par5Random.nextInt(10) == 0 && par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !par1World.getBlockMaterial(par2, par3 - 2, par4).blocksMovement())
        {
            var21 = par2 + par5Random.nextFloat();
            var22 = par3 - 1.05D;
            var23 = par4 + par5Random.nextFloat();

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