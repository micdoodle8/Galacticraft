package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockFuelLoader extends BlockAdvancedTile implements ItemBlockDesc.IBlockShiftDesc
{
    /*private IIcon iconMachineSide;
    private IIcon iconInput;
    private IIcon iconFront;
    private IIcon iconFuelInput;*/

    public BlockFuelLoader(String assetName)
    {
        super(Material.rock);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_input");
        this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_blank");
        this.iconFront = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_fuelloader");
        this.iconFuelInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_fuel_input");
    }*/

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityFuelLoader();
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    /*@Override
    public IIcon getIcon(int side, int metadata)
    {
        if (side == 0 || side == 1)
        {
            return this.iconMachineSide;
        }
        else if (side == metadata + 2)
        {
            return this.iconInput;
        }
        else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
        {
            return this.iconFuelInput;
        }
        else
        {
            return this.iconFront;
        }
    }*/

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        int change = 0;

        // Re-orient the block
        switch (getMetaFromState(world.getBlockState(pos)))
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

        world.setBlockState(pos, this.getStateFromMeta(change), 3);

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBaseUniversalElectrical)
        {
            ((TileBaseUniversalElectrical) te).updateFacing();
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        final int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = 0;

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

        worldIn.setBlockState(pos, this.getStateFromMeta(change), 3);

        for (int dX = -2; dX < 3; dX++)
        {
            for (int dZ = -2; dZ < 3; dZ++)
            {
                BlockPos offsetPos = new BlockPos(pos.getX() + dX, pos.getY(), pos.getZ() + dZ);
                final Block block = worldIn.getBlockState(offsetPos).getBlock();

                if (block == GCBlocks.landingPadFull)
                {
                    worldIn.markBlockForUpdate(offsetPos);
                }
            }
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockDestroyedByPlayer(worldIn, pos, state);

        for (int dX = -2; dX < 3; dX++)
        {
            for (int dZ = -2; dZ < 3; dZ++)
            {
                BlockPos offsetPos = new BlockPos(pos.getX() + dX, pos.getY(), pos.getZ() + dZ);
                final Block block = worldIn.getBlockState(offsetPos).getBlock();

                if (block == GCBlocks.landingPadFull)
                {
                    worldIn.markBlockForUpdate(offsetPos);
                }
            }
        }
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
