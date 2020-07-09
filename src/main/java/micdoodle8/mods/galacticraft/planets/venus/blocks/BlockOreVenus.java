package micdoodle8.mods.galacticraft.planets.venus.blocks;

import com.google.common.base.Predicate;
import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BlockOreVenus extends Block implements IDetectableResource, IPlantableBlock
{
    public BlockOreVenus(Properties builder)
    {
        super(builder);
    }

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
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch)
    {
        if (state.getBlock() == VenusBlocks.oreQuartz || state.getBlock() == VenusBlocks.oreSilicon)
        {
            return MathHelper.nextInt(world instanceof World ? ((World) world).rand : new Random(), 2, 5);
        }
        return 0;
    }

    @Override
    public boolean isValueable(BlockState state)
    {
        return true;
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
