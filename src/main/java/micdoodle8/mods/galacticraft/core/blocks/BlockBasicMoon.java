package micdoodle8.mods.galacticraft.core.blocks;

import com.google.common.base.Predicate;
import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockBasicMoon extends Block implements IDetectableResource, IPlantableBlock, ITerraformableBlock, ISortableBlock
{
    public static final PropertyEnum<EnumBlockBasicMoon> BASIC_TYPE_MOON = PropertyEnum.create("basictypemoon", EnumBlockBasicMoon.class);

    public enum EnumBlockBasicMoon implements IStringSerializable
    {
        ORE_COPPER_MOON(0, "ore_copper_moon"),
        ORE_TIN_MOON(1, "ore_tin_moon"),
        ORE_CHEESE_MOON(2, "ore_cheese_moon"),
        MOON_DIRT(3, "moon_dirt_moon"),
        MOON_STONE(4, "moon_stone"),
        MOON_TURF(5, "moon_turf"),
        ORE_SAPPHIRE(6, "ore_sapphire_moon"),
        MOON_DUNGEON_BRICK(14, "moon_dungeon_brick");

        private final int meta;
        private final String name;

        EnumBlockBasicMoon(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumBlockBasicMoon[] values = values();
        public static EnumBlockBasicMoon byMetadata(int meta)
        {
            if (meta < 7)
            {
                return values[meta];
            }
            
            return MOON_DUNGEON_BRICK;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockBasicMoon(String assetName)
    {
        super(Material.ROCK);
        this.blockHardness = 1.5F;
        this.blockResistance = 2.5F;
        this.setDefaultState(this.blockState.getBaseState().withProperty(BASIC_TYPE_MOON, EnumBlockBasicMoon.ORE_COPPER_MOON));
        this.setUnlocalizedName(assetName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));

        if (type == EnumBlockBasicMoon.MOON_DUNGEON_BRICK)
        {
            return 40.0F;
        }
        else if (type == EnumBlockBasicMoon.MOON_STONE)
        {
            return 6.0F;
        }
        else if (type == EnumBlockBasicMoon.ORE_COPPER_MOON || type == EnumBlockBasicMoon.ORE_TIN_MOON ||
                type == EnumBlockBasicMoon.ORE_SAPPHIRE || type == EnumBlockBasicMoon.ORE_CHEESE_MOON)
        {
            return 3.0F;
        }

        return this.blockResistance / 5.0F;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) worldIn.getBlockState(pos).getValue(BASIC_TYPE_MOON));

        if (type == EnumBlockBasicMoon.MOON_DIRT || type == EnumBlockBasicMoon.MOON_TURF)
        {
            return 0.5F;
        }

        if (type == EnumBlockBasicMoon.MOON_DUNGEON_BRICK)
        {
            return 4.0F;
        }

        if (type == EnumBlockBasicMoon.ORE_COPPER_MOON || type == EnumBlockBasicMoon.ORE_TIN_MOON || type == EnumBlockBasicMoon.ORE_SAPPHIRE)
        {
            return 5.0F;
        }

        if (type == EnumBlockBasicMoon.ORE_CHEESE_MOON)
        {
            return 3.0F;
        }

        return this.blockHardness;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        IBlockState bs = world.getBlockState(pos);
        if (bs.getBlock() != this) return false;
        EnumBlockBasicMoon type = (EnumBlockBasicMoon) bs.getValue(BASIC_TYPE_MOON);
        if (type == EnumBlockBasicMoon.MOON_DIRT || type == EnumBlockBasicMoon.MOON_TURF)
        {
            return true;
        }

        return super.canHarvestBlock(world, pos, player);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) state.getValue(BASIC_TYPE_MOON));
        switch (type)
        {
        case ORE_CHEESE_MOON:
            return GCItems.cheeseCurd;
        case ORE_SAPPHIRE:
            return GCItems.itemBasicMoon;
        default:
            return Item.getItemFromBlock(this);
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) state.getValue(BASIC_TYPE_MOON));
        if (type == EnumBlockBasicMoon.ORE_CHEESE_MOON)
        {
            return 0;
        }
        else if (type == EnumBlockBasicMoon.ORE_SAPPHIRE)
        {
            return 2;
        }
        else
        {
            return getMetaFromState(state);
        }
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) state.getValue(BASIC_TYPE_MOON));
        switch (type)
        {
        case ORE_CHEESE_MOON:
            if (fortune >= 1)
            {
                return (random.nextFloat() < fortune * 0.29F - 0.25F) ? 2 : 1;
            }
            return 1;
        default:
            return 1;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (EnumBlockBasicMoon type : EnumBlockBasicMoon.values())
        {
            list.add(new ItemStack(this, 1, type.getMeta()));
        }
    }

    @Override
    public boolean isValueable(IBlockState state)
    {
        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) state.getValue(BASIC_TYPE_MOON));
        switch (type)
        {
        case ORE_CHEESE_MOON:
        case ORE_COPPER_MOON:
        case ORE_SAPPHIRE:
        case ORE_TIN_MOON:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));

        if (type != EnumBlockBasicMoon.MOON_TURF)
        {
            return false;
        }

        plantable.getPlant(world, pos.offset(EnumFacing.UP));

        return plantable instanceof BlockFlower;
    }

    @Override
    public int requiredLiquidBlocksNearby()
    {
        return 4;
    }

    @Override
    public boolean isPlantable(IBlockState state)
    {
        return state.getValue(BASIC_TYPE_MOON) == EnumBlockBasicMoon.MOON_TURF;

    }

    @Override
    public boolean isTerraformable(World world, BlockPos pos)
    {
        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));

        if (type == EnumBlockBasicMoon.MOON_TURF)
        {
            BlockPos above = pos.offset(EnumFacing.UP);
            IBlockState stateAbove = world.getBlockState(above); 
            return stateAbove.getBlock().isAir(stateAbove, world, above);
        }

        return false;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) state.getValue(BASIC_TYPE_MOON));

        if (!worldIn.isRemote && type == EnumBlockBasicMoon.MOON_TURF)
        {
            Map<Long, List<Footprint>> footprintChunkMap = TickHandlerServer.serverFootprintMap.get(GCCoreUtil.getDimensionID(worldIn));

            if (footprintChunkMap != null)
            {
                long chunkKey = ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4);
                List<Footprint> footprintList = footprintChunkMap.get(chunkKey);

                if (footprintList != null && !footprintList.isEmpty())
                {
                    List<Footprint> toRemove = new ArrayList<Footprint>();

                    for (Footprint footprint : footprintList)
                    {
                        if (footprint.position.x > pos.getX() && footprint.position.x < pos.getX() + 1 &&
                                footprint.position.z > pos.getZ() && footprint.position.z < pos.getZ() + 1)
                        {
                            toRemove.add(footprint);
                        }
                    }

                    if (!toRemove.isEmpty())
                    {
                        footprintList.removeAll(toRemove);
                    }
                }
            }

            TickHandlerServer.footprintBlockChanges.add(new BlockVec3Dim(pos, GCCoreUtil.getDimensionID(worldIn)));
        }
    }

    @Override
    public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target)
    {
        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));
        return type == EnumBlockBasicMoon.MOON_STONE || type == EnumBlockBasicMoon.MOON_DIRT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BASIC_TYPE_MOON, EnumBlockBasicMoon.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumBlockBasicMoon) state.getValue(BASIC_TYPE_MOON)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BASIC_TYPE_MOON);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) getStateFromMeta(meta).getValue(BASIC_TYPE_MOON));
        switch (type)
        {
        case ORE_CHEESE_MOON:
        case ORE_COPPER_MOON:
        case ORE_SAPPHIRE:
        case ORE_TIN_MOON:
            return EnumSortCategoryBlock.ORE;
        case MOON_DUNGEON_BRICK:
            return EnumSortCategoryBlock.BRICKS;
        }
        return EnumSortCategoryBlock.GENERAL;
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
    {
        if (state.getBlock() != this) return 0;

        EnumBlockBasicMoon type = state.getValue(BASIC_TYPE_MOON);
        if (type == EnumBlockBasicMoon.ORE_CHEESE_MOON || type == EnumBlockBasicMoon.ORE_SAPPHIRE)
        {
            Random rand = world instanceof World ? ((World)world).rand : new Random();
            return MathHelper.getInt(rand, 2, 5) + (type == EnumBlockBasicMoon.ORE_SAPPHIRE ? 1 : 0);
        }
        return 0;
    }
}
