package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntitySlimelingEgg;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockSlimelingEgg extends Block implements ITileEntityProvider, IShiftDescription, ISortableBlock
{
    //    private IIcon[] icons;
    public static final PropertyEnum<EnumEggColor> EGG_COLOR = PropertyEnum.create("eggcolor", EnumEggColor.class);
    public static final PropertyBool BROKEN = PropertyBool.create("broken");
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 0.625, 0.75);

    public enum EnumEggColor implements IStringSerializable
    {
        RED(0, "red"),
        BLUE(1, "blue"),
        YELLOW(2, "yellow");

        private final int meta;
        private final String name;

        private EnumEggColor(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockSlimelingEgg(String assetName)
    {
        super(Material.ROCK);
//        this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.625F, 0.75F);
        this.setUnlocalizedName(assetName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(EGG_COLOR, EnumEggColor.RED).withProperty(BROKEN, false));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
    }

//    @Override
//    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
//    {
//        Block block = par1World.getBlock(par2, par3 - 1, par4);
//        return block.isSideSolid(par1World, par2, par3, par4, ForgeDirection.UP);
//    }

    private boolean beginHatch(World world, BlockPos pos, EntityPlayer player, int time)
    {
        IBlockState state = world.getBlockState(pos);
        int l = state.getBlock().getMetaFromState(state);

        if (l < 3)
        {
            world.setBlockState(pos, state.getBlock().getStateFromMeta(l + 3), 2);

            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileEntitySlimelingEgg)
            {
                ((TileEntitySlimelingEgg) tile).timeToHatch = world.rand.nextInt(30 + time) + time;
                ((TileEntitySlimelingEgg) tile).lastTouchedPlayerUUID = player.getUniqueID().toString();
                ((TileEntitySlimelingEgg) tile).lastTouchedPlayerName = player.getName();
            }

            world.markBlockRangeForRenderUpdate(pos, pos);

            return true;
        }
        else
        {
            world.markBlockRangeForRenderUpdate(pos, pos);
            return false;
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        ItemStack currentStack = player.getHeldItemMainhand();
        if (currentStack != null && currentStack.getItem() instanceof ItemPickaxe)
        {
            return world.setBlockToAir(pos);
        }
        else if (player.capabilities.isCreativeMode)
        {
            return world.setBlockToAir(pos);
        }
        else
        {
            beginHatch(world, pos, player, 0);
            return false;
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return beginHatch(worldIn, pos, playerIn, 20);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack currentStack)
    {
        if (!currentStack.isEmpty() && currentStack.getItem() instanceof ItemPickaxe)
        {
            player.addStat(StatList.getBlockStats(this));
            player.addExhaustion(0.025F);
            this.dropBlockAsItem(worldIn, pos, state.getBlock().getStateFromMeta(state.getBlock().getMetaFromState(state) % 3), 0);
            if (currentStack.getItem() == MarsItems.deshPickaxe && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, currentStack) > 0)
            {
                ItemStack itemstack = new ItemStack(MarsItems.deshPickSlime, 1, currentStack.getItemDamage());
                if (currentStack.getTagCompound() != null)
                {
                    itemstack.setTagCompound(currentStack.getTagCompound().copy());
                }
                player.inventory.setInventorySlotContents(player.inventory.currentItem, itemstack);
            }
        }
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        return this.icons[metadata % 6];
    }*/

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

//    @Override
//    public Item getItemDropped(int meta, Random random, int par3)
//    {
//        return Item.getItemFromBlock(this);
//    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

//    @Override
//    public int quantityDropped(int meta, int fortune, Random random)
//    {
//        return 1;
//    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (int var4 = 0; var4 < EnumEggColor.values().length; ++var4)
        {
            list.add(new ItemStack(this, 1, var4));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntitySlimelingEgg();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        int metadata = state.getBlock().getMetaFromState(state);

        if (metadata == 3)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, 0);
        }
        if (metadata == 4)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, 1);
        }
        if (metadata == 5)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, 2);
        }
        return super.getPickBlock(state, target, world, pos, player);
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

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(EGG_COLOR, EnumEggColor.values()[meta % 3]).withProperty(BROKEN, meta - 3 >= 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumEggColor) state.getValue(EGG_COLOR)).getMeta() + (state.getValue(BROKEN) ? 3 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, EGG_COLOR, BROKEN);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.EGG;
    }
}
