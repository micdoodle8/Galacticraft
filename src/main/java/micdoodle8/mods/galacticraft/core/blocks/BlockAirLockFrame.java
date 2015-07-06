package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockAirLockFrame extends BlockAdvancedTile implements ItemBlockDesc.IBlockShiftDesc
{
    /*@SideOnly(Side.CLIENT)
    private IIcon[] airLockIcons;*/

    private enum EnumAirLockType
    {
        AIR_LOCK_FRAME(0),
        AIR_LOCK_CONTROLLER(1);

        private final int meta;

        private EnumAirLockType(int meta)
        {
            this.meta = meta;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumAirLockType byMetadata(int meta)
        {
            return values()[meta];
        }
    }

    public static final PropertyEnum AIR_LOCK_TYPE = PropertyEnum.create("airLockType", EnumAirLockType.class);

    public BlockAirLockFrame(String assetName)
    {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AIR_LOCK_TYPE, EnumAirLockType.AIR_LOCK_FRAME));
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, EnumAirLockType.AIR_LOCK_FRAME.getMeta()));
        par3List.add(new ItemStack(par1, 1, EnumAirLockType.AIR_LOCK_CONTROLLER.getMeta()));
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityAirLockController && placer instanceof EntityPlayer)
        {
            ((TileEntityAirLockController) tile).ownerName = ((EntityPlayer) placer).getGameProfile().getName();
        }
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.airLockIcons = new IIcon[8];
        this.airLockIcons[0] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "airlock_off");
        this.airLockIcons[1] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "airlock_on_1");
        this.airLockIcons[2] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "airlock_on_2");
        this.airLockIcons[3] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "airlock_on_3");
        this.airLockIcons[4] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "airlock_on_4");
        this.airLockIcons[5] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "airlock_on_5");
        this.airLockIcons[6] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "airlock_control_on");
        this.airLockIcons[7] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "airlock_control_off");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
        if (par2 >= BlockAirLockFrame.METADATA_AIR_LOCK_CONTROLLER)
        {
            if (par1 == ForgeDirection.UP.ordinal() || par1 == ForgeDirection.DOWN.ordinal())
            {
                return this.airLockIcons[0];
            }

            return this.airLockIcons[7];
        }
        else
        {
            return this.airLockIcons[0];
        }
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int par2, int par3, int par4, int side)
    {
        if (world.getBlockMetadata(par2, par3, par4) >= BlockAirLockFrame.METADATA_AIR_LOCK_CONTROLLER)
        {
            if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
            {
                return this.airLockIcons[0];
            }

            TileEntity tile = world.getTileEntity(par2, par3, par4);

            if (tile instanceof TileEntityAirLockController)
            {
                TileEntityAirLockController controller = (TileEntityAirLockController) tile;

                if (controller.active)
                {
                    return this.airLockIcons[6];
                }
                else
                {
                    return this.airLockIcons[7];
                }
            }
            else
            {
                return this.airLockIcons[6];
            }
        }
        else
        {
            for (final ForgeDirection orientation : ForgeDirection.values())
            {
                if (orientation != ForgeDirection.UNKNOWN)
                {
                    final Vector3 vector = new Vector3(par2, par3, par4);
                    Vector3 blockVec = this.modifyPositionFromSide(vector.clone(), orientation, 1);
                    Block connection = blockVec.getBlock(world);

                    if (connection != null && connection.equals(GCBlocks.airLockSeal))
                    {
                        if (orientation.offsetY == -1)
                        {
                            if (side == 0)
                            {
                                return this.airLockIcons[1];
                            }
                            else if (side == 1)
                            {
                                return this.airLockIcons[0];
                            }
                            else
                            {
                                return this.airLockIcons[2];
                            }
                        }
                        else if (orientation.offsetY == 1)
                        {
                            if (side == 0)
                            {
                                return this.airLockIcons[0];
                            }
                            else if (side == 1)
                            {
                                return this.airLockIcons[1];
                            }
                            else
                            {
                                return this.airLockIcons[3];
                            }
                        }
                        else if (orientation.ordinal() == side)
                        {
                            if (side == 0)
                            {
                                return this.airLockIcons[0];
                            }
                            else if (side == 1)
                            {
                                return this.airLockIcons[1];
                            }
                            else
                            {
                                return this.airLockIcons[3];
                            }
                        }
                        else if (orientation.getOpposite().ordinal() == side)
                        {
                            return this.airLockIcons[0];
                        }

                        blockVec = vector.clone().translate(new Vector3(orientation.offsetX, orientation.offsetY, orientation.offsetZ));
                        connection = blockVec.getBlock(world);

                        if (connection != null && connection.equals(GCBlocks.airLockSeal))
                        {
                            if (orientation.offsetX == 1)
                            {
                                if (side == 0)
                                {
                                    return this.airLockIcons[4];
                                }
                                else if (side == 1)
                                {
                                    return this.airLockIcons[4];
                                }
                                else if (side == 3)
                                {
                                    return this.airLockIcons[4];
                                }
                                else if (side == 2)
                                {
                                    return this.airLockIcons[5];
                                }
                            }
                            else if (orientation.offsetX == -1)
                            {
                                if (side == 0)
                                {
                                    return this.airLockIcons[5];
                                }
                                else if (side == 1)
                                {
                                    return this.airLockIcons[5];
                                }
                                else if (side == 3)
                                {
                                    return this.airLockIcons[5];
                                }
                                else if (side == 2)
                                {
                                    return this.airLockIcons[4];
                                }
                            }
                            else if (orientation.offsetZ == 1)
                            {
                                if (side == 0)
                                {
                                    return this.airLockIcons[2];
                                }
                                else if (side == 1)
                                {
                                    return this.airLockIcons[2];
                                }
                                else if (side == 4)
                                {
                                    return this.airLockIcons[4];
                                }
                                else if (side == 5)
                                {
                                    return this.airLockIcons[5];
                                }
                            }
                            else if (orientation.offsetZ == -1)
                            {
                                if (side == 0)
                                {
                                    return this.airLockIcons[3];
                                }
                                else if (side == 1)
                                {
                                    return this.airLockIcons[3];
                                }
                                else if (side == 4)
                                {
                                    return this.airLockIcons[5];
                                }
                                else if (side == 5)
                                {
                                    return this.airLockIcons[4];
                                }
                            }
                        }
                    }
                }
            }
        }

        return this.airLockIcons[0];
    }*/

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        if (((EnumAirLockType)state.getValue(AIR_LOCK_TYPE)).getMeta() == EnumAirLockType.AIR_LOCK_FRAME.getMeta())
        {
            return new TileEntityAirLock();
        }
        else
        {
            return new TileEntityAirLockController();
        }
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);

        if (((EnumAirLockType)state.getValue(AIR_LOCK_TYPE)).getMeta() == EnumAirLockType.AIR_LOCK_CONTROLLER.getMeta() && tile instanceof TileEntityAirLockController)
        {
            entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityAirLockController)
        {
            ((TileEntityAirLockController) tile).unsealAirLock();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AIR_LOCK_TYPE, EnumAirLockType.byMetadata(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((EnumAirLockType)state.getValue(AIR_LOCK_TYPE)).getMeta();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, AIR_LOCK_TYPE);
    }

    @Override
    public String getShiftDescription(int itemDamage)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int itemDamage)
    {
        return true;
    }
}
