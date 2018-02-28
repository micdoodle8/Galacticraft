package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockSlabGC extends BlockSlab implements ISortableBlock
{
    public final static PropertyEnum<BlockType> VARIANT = PropertyEnum.create("variant", BlockType.class);

    public BlockSlabGC(String name, Material material)
    {
        super(material);
        this.setUnlocalizedName(name);
        this.useNeighborBrightness = true;
    }

    public BlockSlabGC(Material material)
    {
        super(material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list)
    {
        for (int i = 0; i < (GalacticraftCore.isPlanetsLoaded ? 7 : 4); ++i)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return this.getMetaFromState(state) & 7;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(GCBlocks.slabGCHalf);
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return this.isDouble() ? 2 : 1;
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return this.isDouble() ? null : GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public float getBlockHardness(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();

        if (!(block instanceof BlockSlabGC)) //This will prevent game crashing when harvest block
        {
            return 0;
        }

        switch (this.getMetaFromState(world.getBlockState(pos)))
        {
        case 2:
        case 3:
            return 1.5F;
        default:
            return 2.0F;
        }
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        return super.getBlockHardness(world, pos);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos)) & 7);
    }

    @Override
    public String getUnlocalizedName(int meta)
    {
        BlockType type = ((BlockType) this.getStateFromMeta(meta).getValue(VARIANT));
        return type.getLangName();
    }

    @Override
    public boolean isDouble()
    {
        return false;
    }

    @Override
    public IProperty<?> getVariantProperty()
    {
        return VARIANT;
    }

    @Override
    public Object getVariant(ItemStack itemStack)
    {
        return BlockType.byMetadata(itemStack.getMetadata() & 7);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState state = this.getDefaultState().withProperty(VARIANT, BlockType.byMetadata(meta & 7));

        if (!this.isDouble())
        {
            state = state.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        byte b0 = 0;
        int i = b0 | ((BlockType) state.getValue(VARIANT)).getMetadata();

        if (!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP)
        {
            i |= 8;
        }
        return i;
    }

    @Override
    protected BlockState createBlockState()
    {
        return this.isDouble() ? new BlockState(this, VARIANT) : new BlockState(this, HALF, VARIANT);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.SLABS;
    }

    public enum BlockType implements IStringSerializable
    {
        TIN_SLAB_1(0, "tin_slab_1"),
        TIN_SLAB_2(1, "tin_slab_2"),
        MOON_STONE_SLAB(2, "moon_slab"),
        MOON_DUNGEON_BRICK_SLAB(3, "moon_bricks_slab"),
        MARS_COBBLESTONE_SLAB(4, "mars_slab"),
        MARS_DUNGEON_SLAB(5, "mars_bricks_slab"),
        ASTEROIDS_DECO(6, "asteroids_slab");

        private int meta;
        private String langName;
        private static BlockType[] META_LOOKUP = new BlockType[values().length];

        BlockType(int meta, String langName)
        {
            this.meta = meta;
            this.langName = langName;
        }

        @Override
        public String toString()
        {
            return this.getName();
        }

        @Override
        public String getName()
        {
            return this.langName;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public static BlockType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }
            return META_LOOKUP[meta];
        }

        public String getLangName()
        {
            return langName;
        }

        static
        {
            BlockType[] var0 = values();

            for (BlockType var3 : var0)
            {
                META_LOOKUP[var3.getMetadata()] = var3;
            }
        }
    }
}