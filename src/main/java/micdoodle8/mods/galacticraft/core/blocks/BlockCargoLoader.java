package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoUnloader;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockCargoLoader extends BlockAdvancedTile implements ItemBlockDesc.IBlockShiftDesc
{
    /*private IIcon iconMachineSide;
    private IIcon iconInput;
    private IIcon iconFrontLoader;
    private IIcon iconFrontUnloader;
    private IIcon iconItemInput;
    private IIcon iconItemOutput;*/


    private enum EnumLoaderType
    {
        CARGO_LOADER(0),
        CARGO_UNLOADER(1);

        private final int meta;

        private EnumLoaderType(int meta)
        {
            this.meta = meta;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumLoaderType byMetadata(int meta)
        {
            return values()[(int)Math.floor(meta / 4.0)];
        }

        public static EnumLoaderType byIndex(int index)
        {
            return values()[index];
        }
    }

    public static final PropertyEnum LOADER_TYPE = PropertyEnum.create("loaderType", EnumLoaderType.class);
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public static final int METADATA_CARGO_LOADER = 0;
    public static final int METADATA_CARGO_UNLOADER = 4;

    public BlockCargoLoader(String assetName)
    {
        super(Material.rock);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, BlockCargoLoader.METADATA_CARGO_LOADER));
        par3List.add(new ItemStack(par1, 1, BlockCargoLoader.METADATA_CARGO_UNLOADER));
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity != null)
        {
            if (tileEntity instanceof TileEntityCargoLoader)
            {
                ((TileEntityCargoLoader) tileEntity).checkForCargoEntity();
            }
            else if (tileEntity instanceof TileEntityCargoUnloader)
            {
                ((TileEntityCargoUnloader) tileEntity).checkForCargoEntity();
            }
        }
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_input");
        this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_blank");
        this.iconFrontLoader = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_cargoloader");
        this.iconFrontUnloader = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_cargounloader");
        this.iconItemInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_item_input");
        this.iconItemOutput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_item_output");
    }*/

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    /*@Override
    public IIcon getIcon(int side, int metadata)
    {
        int shiftedMeta = metadata;

        if (side == 0 || side == 1)
        {
            return this.iconMachineSide;
        }

        if (metadata >= BlockCargoLoader.METADATA_CARGO_UNLOADER)
        {
            shiftedMeta -= BlockCargoLoader.METADATA_CARGO_UNLOADER;

            if (side == shiftedMeta + 2)
            {
                return this.iconInput;
            }
            else if (side == ForgeDirection.getOrientation(shiftedMeta + 2).getOpposite().ordinal())
            {
                return metadata < 4 ? this.iconItemInput : this.iconItemOutput;
            }
            else
            {
                return metadata < 4 ? this.iconFrontLoader : this.iconFrontUnloader;
            }
        }
        else if (metadata >= BlockCargoLoader.METADATA_CARGO_LOADER)
        {
            shiftedMeta -= BlockCargoLoader.METADATA_CARGO_LOADER;

            if (side == shiftedMeta + 2)
            {
                return this.iconInput;
            }
            else if (side == ForgeDirection.getOrientation(shiftedMeta + 2).getOpposite().ordinal())
            {
                return metadata < 4 ? this.iconItemInput : this.iconItemOutput;
            }
            else
            {
                return metadata < 4 ? this.iconFrontLoader : this.iconFrontUnloader;
            }
        }

        return this.iconMachineSide;
    }*/

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        if (getMetaFromState(state) < BlockCargoLoader.METADATA_CARGO_UNLOADER)
        {
            return new TileEntityCargoLoader();
        }
        else
        {
            return new TileEntityCargoUnloader();
        }
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        /*IBlockState state = world.getBlockState(pos);
        int shiftedMeta = metadata;
        int baseMeta = 0;

        if (metadata >= BlockCargoLoader.METADATA_CARGO_UNLOADER)
        {
            baseMeta = BlockCargoLoader.METADATA_CARGO_UNLOADER;
        }
        else if (metadata >= BlockCargoLoader.METADATA_CARGO_LOADER)
        {
            baseMeta = BlockCargoLoader.METADATA_CARGO_LOADER;
        }

        shiftedMeta -= baseMeta;
        int change = 0;

        // Re-orient the block
        switch (shiftedMeta)
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

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBaseUniversalElectrical)
        {
            ((TileBaseUniversalElectrical) te).updateFacing();
        }

        return world.setBlockMetadataWithNotify(x, y, z, baseMeta + change, 3);*/
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        /*final int angle = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        final int metadata = world.getBlockMetadata(x, y, z);
        int change = 0;
        int baseMeta = 0;

        if (metadata >= BlockCargoLoader.METADATA_CARGO_UNLOADER)
        {
            baseMeta = BlockCargoLoader.METADATA_CARGO_UNLOADER;
        }
        else if (metadata >= BlockCargoLoader.METADATA_CARGO_LOADER)
        {
            baseMeta = BlockCargoLoader.METADATA_CARGO_LOADER;
        }

        switch (angle)
        {
        case 0:
            change = 3;
            break;
        case 1:
            change = 1;
            break;
        case 2:
            change = 2;
            break;
        case 3:
            change = 0;
            break;
        }

        world.setBlockMetadataWithNotify(x, y, z, baseMeta + change, 3);

        for (int dX = -2; dX < 3; dX++)
        {
            for (int dZ = -2; dZ < 3; dZ++)
            {
                final Block block = world.getBlock(x + dX, y, z + dZ);

                if (block == GCBlocks.landingPadFull)
                {
                    world.markBlockForUpdate(x + dX, y, z + dZ);
                }
            }
        }*/
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockDestroyedByPlayer(worldIn, pos, state);

        for (int dX = -2; dX < 3; dX++)
        {
            for (int dZ = -2; dZ < 3; dZ++)
            {
                BlockPos newPos = new BlockPos(pos.getX() + dX, pos.getY(), pos.getZ() + dZ);
                final Block block = worldIn.getBlockState(newPos).getBlock();

                if (block == GCBlocks.landingPadFull)
                {
                    worldIn.markBlockForUpdate(newPos);
                }
            }
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        /*if (metadata >= BlockCargoLoader.METADATA_CARGO_UNLOADER)
        {
            return BlockCargoLoader.METADATA_CARGO_UNLOADER;
        }
        else if (metadata >= BlockCargoLoader.METADATA_CARGO_LOADER)
        {
            return BlockCargoLoader.METADATA_CARGO_LOADER;
        }*/

        return 0;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case METADATA_CARGO_LOADER:
            return GCCoreUtil.translate("tile.cargoLoader.description");
        case METADATA_CARGO_UNLOADER:
            return GCCoreUtil.translate("tile.cargoUnloader.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
