package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntitySlimelingEgg;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
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
    public static final EnumProperty<EnumEggColor> EGG_COLOR = EnumProperty.create("eggcolor", EnumEggColor.class);
    public static final BooleanProperty BROKEN = BooleanProperty.create("broken");
    protected static final VoxelShape AABB = Block.makeCuboidShape(0.25, 0.0, 0.25, 0.75, 0.625, 0.75);

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

    public BlockSlimelingEgg(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(EGG_COLOR, EnumEggColor.RED).with(BROKEN, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return AABB;
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
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

    private boolean beginHatch(World world, BlockPos pos, PlayerEntity player, int time)
    {
        BlockState state = world.getBlockState(pos);
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

            world.func_225319_b(pos, this.getDefaultState(), state); // Forces block render update. Better way to do this?

            return true;
        }
        else
        {
            world.func_225319_b(pos, this.getDefaultState(), state); // Forces block render update. Better way to do this?
            return false;
        }
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest)
    {
        ItemStack currentStack = player.getHeldItemMainhand();
        if (currentStack != null && currentStack.getItem() instanceof PickaxeItem)
        {
            return world.setBlockToAir(pos);
        }
        else if (player.abilities.isCreativeMode)
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
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        return beginHatch(worldIn, pos, playerIn, 20);
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, @Nullable ItemStack currentStack)
    {
        if (!currentStack.isEmpty() && currentStack.getItem() instanceof PickaxeItem)
        {
            player.addStat(Stats.getBlockStats(this));
            player.addExhaustion(0.025F);
            this.dropBlockAsItem(worldIn, pos, state.getBlock().getStateFromMeta(state.getBlock().getMetaFromState(state) % 3), 0);
            if (currentStack.getItem() == MarsItems.deshPickaxe && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, currentStack) > 0)
            {
                ItemStack itemstack = new ItemStack(MarsItems.deshPickSlime, 1, currentStack.getItemDamage());
                if (currentStack.getTag() != null)
                {
                    itemstack.setTag(currentStack.getTag().copy());
                }
                player.inventory.setInventorySlotContents(player.inventory.currentItem, itemstack);
            }
        }
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        return this.icons[metadata % 6];
    }*/

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public Item getItemDropped(int meta, Random random, int par3)
//    {
//        return Item.getItemFromBlock(this);
//    }

    @Override
    public int damageDropped(BlockState state)
    {
        return getMetaFromState(state);
    }

//    @Override
//    public int quantityDropped(int meta, int fortune, Random random)
//    {
//        return 1;
//    }

    @Override
    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
    {
        for (int var4 = 0; var4 < EnumEggColor.values().length; ++var4)
        {
            list.add(new ItemStack(this, 1, var4));
        }
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySlimelingEgg();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
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
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public BlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().with(EGG_COLOR, EnumEggColor.values()[meta % 3]).with(BROKEN, meta - 3 >= 0);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(EGG_COLOR, BROKEN);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.EGG;
    }
}
