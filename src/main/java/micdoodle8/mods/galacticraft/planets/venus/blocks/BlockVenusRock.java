package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BlockVenusRock extends Block implements IPlantableBlock, ITerraformableBlock
{
    public BlockVenusRock(Properties builder)
    {
        super(builder);
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack tool)
    {
//        player.addStat(Stats.getBlockStats(this));
//        player.addExhaustion(0.025F);
//
//        if (this.canSilkHarvest(worldIn, pos, worldIn.getBlockState(pos), player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0)
//        {
//            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
//            items.add(this.getSilkTouchDrop(state));
//
//            net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, worldIn.getBlockState(pos), 0, 1.0f, true, player);
//
//            for (ItemStack is : items)
//            {
//                spawnAsEntity(worldIn, pos, is);
//            }
//        }
//        else
//        {
//            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
//            harvesters.set(player);
//            this.dropBlockAsItem(worldIn, pos, state, i);
//            harvesters.set(null);
//            if ((EnumBlockBasicVenus) state.get(BASIC_TYPE_VENUS) == EnumBlockBasicVenus.ROCK_MAGMA)
//            {
//                worldIn.setBlockState(pos, Blocks.FLOWING_LAVA.getDefaultState());
//            }
//        }
    }

//    @Override
//    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
//    {
//        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) world.getBlockState(pos).getValue(BASIC_TYPE_VENUS));
//
//        if (type == EnumBlockBasicVenus.ROCK_HARD)
//        {
//            return 6.0F;
//        }
//        else if (type == EnumBlockBasicVenus.DUNGEON_BRICK_1 || type == EnumBlockBasicVenus.DUNGEON_BRICK_2)
//        {
//            return 40.0F;
//        }
//        else if (type == EnumBlockBasicVenus.ORE_ALUMINUM || type == EnumBlockBasicVenus.ORE_COPPER ||
//                type == EnumBlockBasicVenus.ORE_GALENA || type == EnumBlockBasicVenus.ORE_QUARTZ ||
//                type == EnumBlockBasicVenus.ORE_SILICON || type == EnumBlockBasicVenus.ORE_TIN ||
//                type == EnumBlockBasicVenus.ORE_SOLAR_DUST)
//        {
//            return 3.0F;
//        }
//
//        return this.blockResistance / 5.0F;
//    }

//    @Override
//    public float getBlockHardness(BlockState blockState, World worldIn, BlockPos pos)
//    {
//        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) worldIn.getBlockState(pos).getValue(BASIC_TYPE_VENUS));
//
//        if (type == EnumBlockBasicVenus.ROCK_HARD)
//        {
//            return 1.5F;
//        }
//
//        if (type == EnumBlockBasicVenus.ROCK_SOFT)
//        {
//            return 0.9F;
//        }
//
//        if (type == EnumBlockBasicVenus.DUNGEON_BRICK_1 || type == EnumBlockBasicVenus.DUNGEON_BRICK_2)
//        {
//            return 4.0F;
//        }
//
//        if (type == EnumBlockBasicVenus.ORE_ALUMINUM || type == EnumBlockBasicVenus.ORE_COPPER ||
//                type == EnumBlockBasicVenus.ORE_GALENA || type == EnumBlockBasicVenus.ORE_QUARTZ ||
//                type == EnumBlockBasicVenus.ORE_SILICON || type == EnumBlockBasicVenus.ORE_TIN ||
//                type == EnumBlockBasicVenus.ORE_SOLAR_DUST)
//        {
//            return 5.0F;
//        }
//
//        return this.blockHardness;
//    }
//
//    @Override
//    public int quantityDropped(BlockState state, int fortune, Random random)
//    {
//        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) state.get(BASIC_TYPE_VENUS));
//        switch (type)
//        {
//        case ORE_SILICON:
//            if (fortune > 0)
//            {
//                int j = random.nextInt(fortune + 2) - 1;
//
//                if (j < 0)
//                {
//                    j = 0;
//                }
//
//                return this.quantityDropped(random) * (j + 1);
//            }
//        default:
//            return this.quantityDropped(random);
//        }
//    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable)
    {
        return false;
    }

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
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() == VenusBlocks.rockHard || state.getBlock() == VenusBlocks.rockSoft)
        {
            BlockPos above = pos.offset(Direction.UP);
            BlockState stateAbove = world.getBlockState(above);
            return stateAbove.getBlock().isAir(stateAbove, world, above);
        }

        return false;
    }

    @Override
    public boolean isReplaceableOreGen(BlockState state, IWorldReader world, BlockPos pos, java.util.function.Predicate<BlockState> target)
    {
        return state.getBlock() == VenusBlocks.rockHard;
    }

//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) getStateFromMeta(meta).getValue(BASIC_TYPE_VENUS));
//        switch (type)
//        {
//        case ORE_ALUMINUM:
//        case ORE_COPPER:
//        case ORE_GALENA:
//        case ORE_QUARTZ:
//        case ORE_SILICON:
//        case ORE_TIN:
//        case ORE_SOLAR_DUST:
//            return EnumSortCategoryBlock.ORE;
//        case DUNGEON_BRICK_1:
//        case DUNGEON_BRICK_2:
//            return EnumSortCategoryBlock.BRICKS;
//        case LEAD_BLOCK:
//            return EnumSortCategoryBlock.INGOT_BLOCK;
//        default:
//            return EnumSortCategoryBlock.GENERAL;
//        }
//    }
}
