package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFueler;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockLandingPadFull extends BlockAdvancedTile implements IPartialSealableBlock
{
    private IIcon[] icons = new IIcon[3];

    public BlockLandingPadFull(String assetName)
    {
        super(Material.rock);
        this.setHardness(1.0F);
        this.setResistance(10.0F);
        this.setStepSound(Block.soundTypeStone);
        this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setBlockName(assetName);
    }

    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 9;
    }

    @Override
    public void breakBlock(World var1, int var2, int var3, int var4, Block var5, int var6)
    {
        final TileEntity var9 = var1.getTileEntity(var2, var3, var4);

        if (var9 instanceof IMultiBlock)
        {
            ((IMultiBlock) var9).onDestroy(var9);
        }

        super.breakBlock(var1, var2, var3, var4, var5, var6);
    }

    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3)
    {
        return Item.getItemFromBlock(GCBlocks.landingPad);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        switch (world.getBlockMetadata(x, y, z))
        {
        case 0:
            return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
        case 2:
            return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
        default:
            return AxisAlignedBB.getBoundingBox(x + 0.0D, y + 0.0D, z + 0.0D, x + 1.0D, y + 0.2D, z + 1.0D);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        switch (world.getBlockMetadata(x, y, z))
        {
        case 0:
            return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
        case 2:
            return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
        default:
            return AxisAlignedBB.getBoundingBox(x + 0.0D, y + 0.0D, z + 0.0D, x + 1.0D, y + 0.2D, z + 1.0D);
        }
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.icons[0] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "launch_pad");
        this.icons[1] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "buggy_fueler");
        this.icons[2] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "buggy_fueler_blank");
        this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "launch_pad");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
        switch (par2)
        {
        case 0:
            return this.icons[0];
        case 1:
            return this.icons[1];
        case 2:
            return this.icons[2];
        }

        return this.blockIcon;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        for (int x2 = -1; x2 < 2; ++x2)
        {
            for (int z2 = -1; z2 < 2; ++z2)
            {
                if (!super.canPlaceBlockAt(world, x + x2, y, z + z2))
                {
                    return false;
                }
            }

        }

        return true;
    }

    @Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        switch (metadata)
        {
        case 0:
            return new TileEntityLandingPad();
        case 1:
            return new TileEntityBuggyFueler();
        // case 2:
        // return new GCCoreTileEntityCargoPad();
        default:
            return null;
        }
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        par1World.markBlockForUpdate(par2, par3, par4);
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
    public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction)
    {
        return direction == ForgeDirection.UP;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        return new ItemStack(Item.getItemFromBlock(GCBlocks.landingPad), 1, metadata);
    }
}
