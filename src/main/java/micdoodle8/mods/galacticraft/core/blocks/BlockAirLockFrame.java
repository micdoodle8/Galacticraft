package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
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
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockAirLockFrame extends BlockAdvancedTile implements IShiftDescription, ISortableBlock
{
    private enum EnumAirLockType implements IStringSerializable
    {
        AIR_LOCK_FRAME(0, "air_lock_frame"),
        AIR_LOCK_CONTROLLER(1, "air_lock_controller");

        private final int meta;
        private final String name;

        EnumAirLockType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumAirLockType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public static final PropertyEnum<EnumAirLockType> AIR_LOCK_TYPE = PropertyEnum.create("airLockType", EnumAirLockType.class);

    public BlockAirLockFrame(String assetName)
    {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AIR_LOCK_TYPE, EnumAirLockType.AIR_LOCK_FRAME));
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        this.setUnlocalizedName(assetName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
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
            ((TileEntityAirLockController) tile).ownerName = PlayerUtil.getName(((EntityPlayer) placer));
        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        if (((EnumAirLockType) state.getValue(AIR_LOCK_TYPE)).getMeta() == EnumAirLockType.AIR_LOCK_FRAME.getMeta())
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
    public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (((EnumAirLockType) state.getValue(AIR_LOCK_TYPE)).getMeta() == EnumAirLockType.AIR_LOCK_CONTROLLER.getMeta() && tile instanceof TileEntityAirLockController)
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

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AIR_LOCK_TYPE, EnumAirLockType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumAirLockType) state.getValue(AIR_LOCK_TYPE)).getMeta();
    }

    @Override
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

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
