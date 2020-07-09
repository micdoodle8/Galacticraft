package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BlockAsteroidRock extends Block implements IPlantableBlock, ITerraformableBlock
{
//    public static final EnumProperty<EnumBlockBasic> BASIC_TYPE = EnumProperty.create("basictypeasteroids", EnumBlockBasic.class);
//
//    public enum EnumBlockBasic implements IStringSerializable
//    {
//        ASTEROID_0(0, "asteroid_rock_0"),
//        ASTEROID_1(1, "asteroid_rock_1"),
//        ASTEROID_2(2, "asteroid_rock_2"),
//        ORE_ALUMINUM(3, "ore_aluminum_asteroids"),
//        ORE_ILMENITE(4, "ore_ilmenite_asteroids"),
//        ORE_IRON(5, "ore_iron_asteroids"),
//        DECO(6, "asteroid_deco"),
//        TITANIUM_BLOCK(7, "titanium_block");
//
//        private final int meta;
//        private final String name;
//
//        private EnumBlockBasic(int meta, String name)
//        {
//            this.meta = meta;
//            this.name = name;
//        }
//
//        public int getMeta()
//        {
//            return this.meta;
//        }
//
//        private final static EnumBlockBasic[] values = values();
//        public static EnumBlockBasic byMetadata(int meta)
//        {
//            return values[meta % values.length];
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//    }

    public BlockAsteroidRock(Properties builder)
    {
        super(builder);
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        switch (getMetaFromState(state))
//        {
//        case 4:
//            return 0;
//        default:
//            return getMetaFromState(state);
//        }
//    }
//
//    @Override
//    public int quantityDropped(BlockState state, int fortune, Random random)
//    {
//        switch (getMetaFromState(state))
//        {
//        case 4:
//            if (fortune >= 1)
//            {
//                return (random.nextFloat() < fortune * 0.29F - 0.25F) ? 2 : 1;
//            }
//        default:
//            return 1;
//        }
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        int var4;
//
//        for (var4 = 0; var4 < EnumBlockBasic.values().length; ++var4)
//        {
//            list.add(new ItemStack(this, 1, var4));
//        }
//    }

//    @Override
//    public boolean isValueable(BlockState state)
//    {
//        switch (this.getMetaFromState(state))
//        {
//        case 3:
//        case 4:
//        case 5:
//            return true;
//        default:
//            return false;
//        }
//    }

//    @Override
//    public boolean canSustainPlant(BlockState state, IBlockAccess world, BlockPos pos, Direction direction, IPlantable plantable)
//    {
//        return false;
//    }

    @Override
    public int requiredLiquidBlocksNearby()
    {
        return 4;
    }

    @Override
    public boolean isPlantable(BlockState state)
    {
        return false;
    }

    @Override
    public boolean isTerraformable(World world, BlockPos pos)
    {
        return false;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        if (this == AsteroidBlocks.oreIlmenite)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1);
        }

        return super.getPickBlock(state, target, world, pos, player);
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(BASIC_TYPE, EnumBlockBasic.byMetadata(meta));
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(BASIC_TYPE);
//    }

//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        switch (meta)
//        {
//        case 3:
//        case 4:
//        case 5:
//            return EnumSortCategoryBlock.ORE;
//        case 6:
//            return EnumSortCategoryBlock.DECORATION;
//        case 7:
//            return EnumSortCategoryBlock.INGOT_BLOCK;
//        }
//        return EnumSortCategoryBlock.GENERAL;
//    }
}
