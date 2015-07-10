package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockSolar extends BlockTileGC implements ItemBlockDesc.IBlockShiftDesc, IPartialSealableBlock
{
    public static final int BASIC_METADATA = 0;
    public static final int ADVANCED_METADATA = 4;

    public static String[] names = { "basic", "advanced" };

    // private IIcon[] icons = new IIcon[6];

    public BlockSolar(String assetName)
    {
        super(Material.iron);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, BlockSolar.BASIC_METADATA));
        par3List.add(new ItemStack(par1, 1, BlockSolar.ADVANCED_METADATA));
    }

    /*@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.icons[0] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "solar_basic_0");
        this.icons[1] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "solar_basic_1");
        this.icons[2] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "solar_advanced_0");
        this.icons[3] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "solar_advanced_1");
        this.icons[4] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_blank");
        this.icons[5] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_output");
        this.blockIcon = this.icons[0];
    }*/

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (meta >= BlockSolar.ADVANCED_METADATA)
        {
            int shiftedMeta = meta -= BlockSolar.ADVANCED_METADATA;

            if (side == ForgeDirection.getOrientation(shiftedMeta + 2).getOpposite().ordinal())
            {
                return this.icons[5];
            }
            else if (side == ForgeDirection.UP.ordinal())
            {
                return this.icons[2];
            }
            else if (side == ForgeDirection.DOWN.ordinal())
            {
                return this.icons[4];
            }
            else
            {
                return this.icons[3];
            }
        }
        else if (meta >= BlockSolar.BASIC_METADATA)
        {
            int shiftedMeta = meta -= BlockSolar.BASIC_METADATA;

            if (side == ForgeDirection.getOrientation(shiftedMeta + 2).getOpposite().ordinal())
            {
                return this.icons[5];
            }
            else if (side == ForgeDirection.UP.ordinal())
            {
                return this.icons[0];
            }
            else if (side == ForgeDirection.DOWN.ordinal())
            {
                return this.icons[4];
            }
            else
            {
                return this.icons[1];
            }
        }

        return this.blockIcon;
    }*/

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        for (int y = 1; y <= 2; y++)
        {
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    BlockPos posAt = pos.add(y == 2 ? x : 0, y, y == 2 ? z : 0);
                    Block block = worldIn.getBlockState(posAt).getBlock();

                    if (block.getMaterial() != Material.air && !block.isReplaceable(worldIn, posAt))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
        // return new BlockVec3(x1, y1, z1).newVecSide(side ^ 1).getBlock(world) != GCBlocks.fakeBlock; TODO
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int metadata = getMetaFromState(state);

        int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = 0;

        switch (angle)
        {
        case 0:
            change = 1;
            break;
        case 1:
            change = 2;
            break;
        case 2:
            change = 0;
            break;
        case 3:
            change = 3;
            break;
        }

        if (metadata >= BlockSolar.ADVANCED_METADATA)
        {
            worldIn.setBlockState(pos, getStateFromMeta(BlockSolar.ADVANCED_METADATA + change), 3);
        }
        else
        {
            worldIn.setBlockState(pos, getStateFromMeta(BlockSolar.BASIC_METADATA + change), 3);
        }

        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntitySolar)
        {
            ((TileEntitySolar) tile).onCreate(pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        if (var9 instanceof TileEntitySolar)
        {
            ((TileEntitySolar) var9).onDestroy(var9);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        int metadata = getMetaFromState(world.getBlockState(pos));
        int original = metadata;

        int change = 0;

        if (metadata >= BlockSolar.ADVANCED_METADATA)
        {
            original -= BlockSolar.ADVANCED_METADATA;
        }

        switch (original)
        {
        case 0:
            change = 3;
            break;
        case 3:
            change = 1;
            break;
        case 1:
            change = 2;
            break;
        case 2:
            change = 0;
            break;
        }

        if (metadata >= BlockSolar.ADVANCED_METADATA)
        {
            change += BlockSolar.ADVANCED_METADATA;
        }

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBaseUniversalElectrical)
        {
            ((TileBaseUniversalElectrical) te).updateFacing();
        }

        world.setBlockState(pos, getStateFromMeta(change), 3);
        return true;
    }

    @Override
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        playerIn.openGui(GalacticraftCore.instance, -1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        if (getMetaFromState(state) >= BlockSolar.ADVANCED_METADATA)
        {
            return BlockSolar.ADVANCED_METADATA;
        }
        else
        {
            return BlockSolar.BASIC_METADATA;
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
    {
        int metadata = this.getDamageValue(world, pos);

        return new ItemStack(this, 1, metadata);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        if (getMetaFromState(state) >= BlockSolar.ADVANCED_METADATA)
        {
            return new TileEntitySolar(2);
        }
        else
        {
            return new TileEntitySolar(1);
        }
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case BASIC_METADATA:
            return GCCoreUtil.translate("tile.solarBasic.description");
        case ADVANCED_METADATA:
            return GCCoreUtil.translate("tile.solarAdv.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return true;
    }
}
