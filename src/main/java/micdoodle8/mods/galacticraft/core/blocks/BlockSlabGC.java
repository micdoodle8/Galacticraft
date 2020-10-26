//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GCBlocks;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.SlabBlock;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.properties.PropertyEnum;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.IStringSerializable;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.Random;
//
//public class BlockSlabGC extends SlabBlock implements ISortable
//{
//    public final static EnumProperty<BlockType> VARIANT = EnumProperty.create("variant", BlockType.class);
//
//    public BlockSlabGC(String name, Material material)
//    {
//        super(material);
//        this.setUnlocalizedName(name);
//        this.useNeighborBrightness = true;
//    }
//
//    public BlockSlabGC(Material material)
//    {
//        super(material);
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        for (int i = 0; i < (GalacticraftCore.isPlanetsLoaded ? 7 : 4); ++i)
//        {
//            list.add(new ItemStack(this, 1, i));
//        }
//    }
//
//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return this.getMetaFromState(state) & 7;
//    }
//
//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(GCBlocks.slabGCHalf);
//    }
//
//    @Override
//    public int quantityDropped(Random rand)
//    {
//        return this.isDouble() ? 2 : 1;
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return this.isDouble() ? null : GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    @Override
//    public float getBlockHardness(BlockState blockState, World worldIn, BlockPos pos)
//    {
//        Block block = worldIn.getBlockState(pos).getBlock();
//
//        if (!(block instanceof BlockSlabGC)) //This will prevent game crashing when harvest block
//        {
//            return 0;
//        }
//
//        switch (this.getMetaFromState(worldIn.getBlockState(pos)))
//        {
//        case 2:
//        case 3:
//            return 1.5F;
//        default:
//            return 2.0F;
//        }
//    }
//
//    @Override
//    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
//    {
//        return new ItemStack(this, 1, this.getMetaFromState(state) & 7);
//    }
//
//    @Override
//    public String getTranslationKey(int meta)
//    {
//        BlockType type = ((BlockType) this.getStateFromMeta(meta).getValue(VARIANT));
//        return type.getLangName();
//    }
//
//    @Override
//    public boolean isDouble()
//    {
//        return false;
//    }
//
//    @Override
//    public IProperty<?> getVariantProperty()
//    {
//        return VARIANT;
//    }
//
//    @Override
//    public Comparable<?> getTypeForItem(ItemStack stack)
//    {
//        return BlockType.byMetadata(stack.getMetadata() & 7);
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        BlockState state = this.getDefaultState().with(VARIANT, BlockType.byMetadata(meta & 7));
//
//        if (!this.isDouble())
//        {
//            state = state.with(HALF, (meta & 8) == 0 ? SlabBlock.EnumBlockHalf.BOTTOM : SlabBlock.EnumBlockHalf.TOP);
//        }
//        return state;
//    }
//
//    @Override
//    public int getMetaFromState(BlockState state)
//    {
//        byte b0 = 0;
//        int i = b0 | ((BlockType) state.get(VARIANT)).getMetadata();
//
//        if (!this.isDouble() && state.get(HALF) == EnumBlockHalf.TOP)
//        {
//            i |= 8;
//        }
//        return i;
//    }
//
//    @Override
//    protected BlockStateContainer createBlockState()
//    {
//        return this.isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, HALF, VARIANT);
//    }
//
//    @Override
//    public EnumSortCategory getCategory()
//    {
//        return EnumSortCategory.SLABS;
//    }
//
//    public enum BlockType implements IStringSerializable
//    {
//        TIN_SLAB_1(0, "tin_slab_1"),
//        TIN_SLAB_2(1, "tin_slab_2"),
//        MOON_STONE_SLAB(2, "moon_slab"),
//        MOON_DUNGEON_BRICK_SLAB(3, "moon_bricks_slab"),
//        MARS_COBBLESTONE_SLAB(4, "mars_slab"),
//        MARS_DUNGEON_SLAB(5, "mars_bricks_slab"),
//        ASTEROIDS_DECO(6, "asteroids_slab");
//
//        private int meta;
//        private String langName;
//        private static BlockType[] META_LOOKUP = new BlockType[values().length];
//
//        BlockType(int meta, String langName)
//        {
//            this.meta = meta;
//            this.langName = langName;
//        }
//
//        @Override
//        public String toString()
//        {
//            return this.getName();
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.langName;
//        }
//
//        public int getMetadata()
//        {
//            return this.meta;
//        }
//
//        public static BlockType byMetadata(int meta)
//        {
//            if (meta < 0 || meta >= META_LOOKUP.length)
//            {
//                meta = 0;
//            }
//            return META_LOOKUP[meta];
//        }
//
//        public String getLangName()
//        {
//            return langName;
//        }
//
//        static
//        {
//            BlockType[] var0 = values();
//
//            for (BlockType var3 : var0)
//            {
//                META_LOOKUP[var3.getMetadata()] = var3;
//            }
//        }
//    }
//}