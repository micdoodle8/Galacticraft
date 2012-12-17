package micdoodle8.mods.galacticraft.core.blocks;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlockUnlitTorch extends GCCoreBlock
{
	public boolean lit;
	
	protected GCCoreBlockUnlitTorch(int par1, int par2, boolean lit)
    {
        super(par1, par2);
        this.setTickRandomly(true);
        this.setRequiresSelfNotify();
        this.lit = lit;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
	public int getRenderType()
    {
        return GalacticraftCore.proxy.getGCUnlitTorchRenderID();
    }

    /**
     * Gets if we can place a torch on a block.
     */
    private boolean canPlaceTorchOn(World par1World, int par2, int par3, int par4)
    {
        if (par1World.doesBlockHaveSolidTopSurface(par2, par3, par4))
        {
            return true;
        }
        else
        {
            final int var5 = par1World.getBlockId(par2, par3, par4);
            return Block.blocksList[var5] != null && Block.blocksList[var5].canPlaceTorchOnTop(par1World, par2, par3, par4);
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST,  true) ||
               par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST,  true) ||
               par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true) ||
               par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true) ||
               this.canPlaceTorchOn(par1World, par2, par3 - 1, par4);
    }

    /**
     * called before onBlockPlacedBy by ItemBlock and ItemReed
     */
    @Override
    public int func_85104_a(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        int var10 = par9;

        if (par5 == 1 && this.canPlaceTorchOn(par1World, par2, par3 - 1, par4))
        {
            var10 = 5;
        }

        if (par5 == 2 && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true))
        {
            var10 = 4;
        }

        if (par5 == 3 && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true))
        {
            var10 = 3;
        }

        if (par5 == 4 && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST, true))
        {
            var10 = 2;
        }

        if (par5 == 5 && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true))
        {
            var10 = 1;
        }

        return var10;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        super.updateTick(par1World, par2, par3, par4, par5Random);

        if (par1World.getBlockMetadata(par2, par3, par4) == 0)
        {
            this.onBlockAdded(par1World, par2, par3, par4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true))
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1);
        }
        else if (par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST, true))
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2);
        }
        else if (par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true))
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3);
        }
        else if (par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true))
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 4);
        }
        else if (this.canPlaceTorchOn(par1World, par2, par3 - 1, par4))
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 5);
        }

        this.dropTorchIfCantStay(par1World, par2, par3, par4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (this.dropTorchIfCantStay(par1World, par2, par3, par4))
        {
            final int var6 = par1World.getBlockMetadata(par2, par3, par4);
            boolean var7 = false;

            if (!par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true) && var6 == 1)
            {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST, true) && var6 == 2)
            {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true) && var6 == 3)
            {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true) && var6 == 4)
            {
                var7 = true;
            }

            if (!this.canPlaceTorchOn(par1World, par2, par3 - 1, par4) && var6 == 5)
            {
                var7 = true;
            }

            if (var7)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }
        }
    }

    /**
     * Tests if the block can remain at its current location and will drop as an item if it is unable to stay. Returns
     * True if it can stay and False if it drops. Args: world, x, y, z
     */
    private boolean dropTorchIfCantStay(World par1World, int par2, int par3, int par4)
    {
        if (!this.canPlaceBlockAt(par1World, par2, par3, par4))
        {
            if (par1World.getBlockId(par2, par3, par4) == this.blockID)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    @Override
	public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
        final int var7 = par1World.getBlockMetadata(par2, par3, par4) & 7;
        float var8 = 0.15F;

        if (var7 == 1)
        {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
        }
        else if (var7 == 2)
        {
            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
        }
        else if (var7 == 3)
        {
            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
        }
        else if (var7 == 4)
        {
            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
        }
        else
        {
            var8 = 0.1F;
            this.setBlockBounds(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
        }

        return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
    }

    @Override
	@SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	if (par5Random.nextInt(5) == 0)
    	{
    		final int var6 = par1World.getBlockMetadata(par2, par3, par4);
            final double var7 = par2 + 0.5F;
            final double var9 = par3 + 0.7F;
            final double var11 = par4 + 0.5F;
            final double var13 = 0.2199999988079071D;
            final double var15 = 0.27000001072883606D;

            if (var6 == 1)
            {
                par1World.spawnParticle("smoke", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 2)
            {
                par1World.spawnParticle("smoke", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 3)
            {
                par1World.spawnParticle("smoke", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 4)
            {
                par1World.spawnParticle("smoke", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
            }
            else
            {
                par1World.spawnParticle("smoke", var7, var9, var11, 0.0D, 0.0D, 0.0D);
            }
    	}
    }
}
