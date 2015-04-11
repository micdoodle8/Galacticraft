package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockUnlitTorch extends Block implements IOxygenReliantBlock
{
    public boolean lit;

    public static IIcon[] torchIcons = new IIcon[2];

    protected BlockUnlitTorch(boolean lit, String assetName)
    {
        super(Material.circuits);
        this.setTickRandomly(true);
        this.lit = lit;
        this.setLightLevel(lit ? 0.9375F : 0.2F);
        this.setHardness(0.0F);
        this.setStepSound(Block.soundTypeWood);
        this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setBlockName(assetName);
    }

    private static boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection direction, boolean nope)
    {
        return world.getBlock(x, y, z).isSideSolid(world, x, y, z, direction);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (this == GCBlocks.unlitTorch)
        {
            return BlockUnlitTorch.torchIcons[1];
        }
        else if (this == GCBlocks.unlitTorchLit)
        {
            return BlockUnlitTorch.torchIcons[0];
        }

        return BlockUnlitTorch.torchIcons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
        return BlockUnlitTorch.torchIcons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        BlockUnlitTorch.torchIcons[0] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "unlitTorchLit");
        BlockUnlitTorch.torchIcons[1] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "unlitTorch");
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    private boolean canPlaceTorchOn(World par1World, int par2, int par3, int par4)
    {
        if (World.doesBlockHaveSolidTopSurface(par1World, par2, par3, par4))
        {
            return true;
        }
        else
        {
            final Block var5 = par1World.getBlock(par2, par3, par4);
            return var5.canPlaceTorchOnTop(par1World, par2, par3, par4);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return BlockUnlitTorch.isBlockSolidOnSide(par1World, par2 - 1, par3, par4, ForgeDirection.EAST, true) || BlockUnlitTorch.isBlockSolidOnSide(par1World, par2 + 1, par3, par4, ForgeDirection.WEST, true) || BlockUnlitTorch.isBlockSolidOnSide(par1World, par2, par3, par4 - 1, ForgeDirection.SOUTH, true) || BlockUnlitTorch.isBlockSolidOnSide(par1World, par2, par3, par4 + 1, ForgeDirection.NORTH, true) || this.canPlaceTorchOn(par1World, par2, par3 - 1, par4);
    }

    @Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        int var10 = par9;

        if (par5 == 1 && this.canPlaceTorchOn(par1World, par2, par3 - 1, par4))
        {
            var10 = 5;
        }

        if (par5 == 2 && BlockUnlitTorch.isBlockSolidOnSide(par1World, par2, par3, par4 + 1, ForgeDirection.NORTH, true))
        {
            var10 = 4;
        }

        if (par5 == 3 && BlockUnlitTorch.isBlockSolidOnSide(par1World, par2, par3, par4 - 1, ForgeDirection.SOUTH, true))
        {
            var10 = 3;
        }

        if (par5 == 4 && BlockUnlitTorch.isBlockSolidOnSide(par1World, par2 + 1, par3, par4, ForgeDirection.WEST, true))
        {
            var10 = 2;
        }

        if (par5 == 5 && BlockUnlitTorch.isBlockSolidOnSide(par1World, par2 - 1, par3, par4, ForgeDirection.EAST, true))
        {
            var10 = 1;
        }

        return var10;
    }

    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        super.updateTick(par1World, par2, par3, par4, par5Random);

        if (par1World.getBlockMetadata(par2, par3, par4) == 0)
        {
            this.onBlockAdded(par1World, par2, par3, par4);
        }
        else
        {
            this.checkOxygen(par1World, par2, par3, par4);
        }
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (par1World.getBlockMetadata(par2, par3, par4) == 0)
        {
            if (BlockUnlitTorch.isBlockSolidOnSide(par1World, par2 - 1, par3, par4, ForgeDirection.EAST, true))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
            }
            else if (BlockUnlitTorch.isBlockSolidOnSide(par1World, par2 + 1, par3, par4, ForgeDirection.WEST, true))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
            }
            else if (BlockUnlitTorch.isBlockSolidOnSide(par1World, par2, par3, par4 - 1, ForgeDirection.SOUTH, true))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
            }
            else if (BlockUnlitTorch.isBlockSolidOnSide(par1World, par2, par3, par4 + 1, ForgeDirection.NORTH, true))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
            }
            else if (this.canPlaceTorchOn(par1World, par2, par3 - 1, par4))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
            }
        }

        if (this.dropTorchIfCantStay(par1World, par2, par3, par4))
        {
            this.checkOxygen(par1World, par2, par3, par4);
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        if (this.dropTorchIfCantStay(par1World, par2, par3, par4))
        {
            final int var6 = par1World.getBlockMetadata(par2, par3, par4);
            boolean var7 = false;

            if (!BlockUnlitTorch.isBlockSolidOnSide(par1World, par2 - 1, par3, par4, ForgeDirection.EAST, true) && var6 == 1)
            {
                var7 = true;
            }

            if (!BlockUnlitTorch.isBlockSolidOnSide(par1World, par2 + 1, par3, par4, ForgeDirection.WEST, true) && var6 == 2)
            {
                var7 = true;
            }

            if (!BlockUnlitTorch.isBlockSolidOnSide(par1World, par2, par3, par4 - 1, ForgeDirection.SOUTH, true) && var6 == 3)
            {
                var7 = true;
            }

            if (!BlockUnlitTorch.isBlockSolidOnSide(par1World, par2, par3, par4 + 1, ForgeDirection.NORTH, true) && var6 == 4)
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
                par1World.setBlock(par2, par3, par4, Blocks.air);
            }
            else
            {
                this.checkOxygen(par1World, par2, par3, par4);
            }
        }
    }

    private void checkOxygen(World world, int x, int y, int z)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            if (OxygenUtil.checkTorchHasOxygen(world, this, x, y, z))
            {
                this.onOxygenAdded(world, x, y, z);
            }
            else
            {
                this.onOxygenRemoved(world, x, y, z);
            }
        }
    }

    /**
     * Tests if the block can remain at its current location and will drop as an
     * item if it is unable to stay. Returns True if it can stay and False if it
     * drops. Args: world, x, y, z
     */
    private boolean dropTorchIfCantStay(World par1World, int par2, int par3, int par4)
    {
        if (!this.canPlaceBlockAt(par1World, par2, par3, par4))
        {
            if (par1World.getBlock(par2, par3, par4) == this)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlock(par2, par3, par4, Blocks.air);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector
     * returning a ray trace hit. Args: world, x, y, z, startVec, endVec
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
        boolean doSmoke = par5Random.nextInt(5) == 0; 
    	if (this.lit || doSmoke)
        {
            final int var6 = par1World.getBlockMetadata(par2, par3, par4);
            final double var7 = par2 + 0.5F;
            final double var9 = par3 + 0.7F;
            final double var11 = par4 + 0.5F;
            final double var13 = 0.2199999988079071D;
            final double var15 = 0.27000001072883606D;

            if (var6 == 1)
            {
                if (doSmoke) par1World.spawnParticle("smoke", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
                if (this.lit) par1World.spawnParticle("flame", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 2)
            {
            	if (doSmoke) par1World.spawnParticle("smoke", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
                if (this.lit) par1World.spawnParticle("flame", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 3)
            {
            	if (doSmoke) par1World.spawnParticle("smoke", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
                if (this.lit) par1World.spawnParticle("flame", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 4)
            {
            	if (doSmoke) par1World.spawnParticle("smoke", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
                if (this.lit) par1World.spawnParticle("flame", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
            }
            else
            {
            	if (doSmoke) par1World.spawnParticle("smoke", var7, var9, var11, 0.0D, 0.0D, 0.0D);
                if (this.lit) par1World.spawnParticle("flame", var7, var9, var11, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void onOxygenRemoved(World world, int x, int y, int z)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            world.setBlock(x, y, z, GCBlocks.unlitTorch, world.getBlockMetadata(x, y, z), 2);
        }
        else
        {
            world.setBlock(x, y, z, Blocks.torch, world.getBlockMetadata(x, y, z), 2);
        }
    }

    @Override
    public void onOxygenAdded(World world, int x, int y, int z)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            world.setBlock(x, y, z, GCBlocks.unlitTorchLit, world.getBlockMetadata(x, y, z), 2);
        }
        else
        {
            world.setBlock(x, y, z, Blocks.torch, world.getBlockMetadata(x, y, z), 2);
        }
    }
}
