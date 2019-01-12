package micdoodle8.mods.galacticraft.planets.venus.blocks;

import com.google.common.base.Predicate;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Random;

public class BlockBasicVenus extends Block implements IDetectableResource, IPlantableBlock, ITerraformableBlock, ISortableBlock
{
    public static final PropertyEnum<EnumBlockBasicVenus> BASIC_TYPE_VENUS = PropertyEnum.create("basictypevenus", EnumBlockBasicVenus.class);

    public enum EnumBlockBasicVenus implements IStringSerializable
    {
        ROCK_SOFT(0, "venus_rock_0"),
        ROCK_HARD(1, "venus_rock_1"),
        ROCK_MAGMA(2, "venus_rock_2"),
        ROCK_VOLCANIC_DEPOSIT(3, "venus_rock_3"),
        DUNGEON_BRICK_1(4, "dungeon_brick_venus_1"),
        DUNGEON_BRICK_2(5, "dungeon_brick_venus_2"),
        ORE_ALUMINUM(6, "venus_ore_aluminum"),
        ORE_COPPER(7, "venus_ore_copper"),
        ORE_GALENA(8, "venus_ore_galena"),
        ORE_QUARTZ(9, "venus_ore_quartz"),
        ORE_SILICON(10, "venus_ore_silicon"),
        ORE_TIN(11, "venus_ore_tin"),
        LEAD_BLOCK(12, "lead_block"),
        ORE_SOLAR_DUST(13, "venus_ore_solar");

        private final int meta;
        private final String name;

        EnumBlockBasicVenus(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumBlockBasicVenus[] values = values();
        public static EnumBlockBasicVenus byMetadata(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getName()
        {
            return this.name;
        }

        public ItemStack getItemStack()
        {
            return new ItemStack(VenusBlocks.venusBlock, 1, this.meta);
        }
    }

    public BlockBasicVenus(String assetName)
    {
        super(Material.ROCK);
        this.blockHardness = 2.2F;
        this.blockResistance = 2.5F;
        this.setDefaultState(this.blockState.getBaseState().withProperty(BASIC_TYPE_VENUS, EnumBlockBasicVenus.ROCK_SOFT));
        this.setUnlocalizedName(assetName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack tool)
    {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest(worldIn, pos, worldIn.getBlockState(pos), player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0)
        {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            items.add(this.getSilkTouchDrop(state));

            net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, worldIn.getBlockState(pos), 0, 1.0f, true, player);

            for (ItemStack is : items)
            {
                spawnAsEntity(worldIn, pos, is);
            }
        }
        else
        {
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
            harvesters.set(player);
            this.dropBlockAsItem(worldIn, pos, state, i);
            harvesters.set(null);
            if ((EnumBlockBasicVenus) state.getValue(BASIC_TYPE_VENUS) == EnumBlockBasicVenus.ROCK_MAGMA)
            {
                worldIn.setBlockState(pos, Blocks.FLOWING_LAVA.getDefaultState());
            }
        }
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) world.getBlockState(pos).getValue(BASIC_TYPE_VENUS));

        if (type == EnumBlockBasicVenus.ROCK_HARD)
        {
            return 6.0F;
        }
        else if (type == EnumBlockBasicVenus.DUNGEON_BRICK_1 || type == EnumBlockBasicVenus.DUNGEON_BRICK_2)
        {
            return 40.0F;
        }
        else if (type == EnumBlockBasicVenus.ORE_ALUMINUM || type == EnumBlockBasicVenus.ORE_COPPER ||
                type == EnumBlockBasicVenus.ORE_GALENA || type == EnumBlockBasicVenus.ORE_QUARTZ ||
                type == EnumBlockBasicVenus.ORE_SILICON || type == EnumBlockBasicVenus.ORE_TIN ||
                type == EnumBlockBasicVenus.ORE_SOLAR_DUST)
        {
            return 3.0F;
        }

        return this.blockResistance / 5.0F;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) worldIn.getBlockState(pos).getValue(BASIC_TYPE_VENUS));

        if (type == EnumBlockBasicVenus.ROCK_HARD)
        {
            return 1.5F;
        }

        if (type == EnumBlockBasicVenus.ROCK_SOFT)
        {
            return 0.9F;
        }

        if (type == EnumBlockBasicVenus.DUNGEON_BRICK_1 || type == EnumBlockBasicVenus.DUNGEON_BRICK_2)
        {
            return 4.0F;
        }

        if (type == EnumBlockBasicVenus.ORE_ALUMINUM || type == EnumBlockBasicVenus.ORE_COPPER ||
                type == EnumBlockBasicVenus.ORE_GALENA || type == EnumBlockBasicVenus.ORE_QUARTZ ||
                type == EnumBlockBasicVenus.ORE_SILICON || type == EnumBlockBasicVenus.ORE_TIN ||
                type == EnumBlockBasicVenus.ORE_SOLAR_DUST)
        {
            return 5.0F;
        }

        return this.blockHardness;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) state.getValue(BASIC_TYPE_VENUS));
        switch (type)
        {
        case ORE_SILICON:
            return GCItems.basicItem;
        case ORE_QUARTZ:
            return Items.QUARTZ;
        case ORE_SOLAR_DUST:
            return VenusItems.basicItem;
        default:
            return Item.getItemFromBlock(this);
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) state.getValue(BASIC_TYPE_VENUS));
        switch (type)
        {
        case ORE_SILICON:
            return 2;
        case ORE_QUARTZ:
            return 0;
        case ORE_SOLAR_DUST:
            return 4;
        default:
            return getMetaFromState(state);
        }
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) state.getValue(BASIC_TYPE_VENUS));
        switch (type)
        {
        case ORE_SILICON:
            if (fortune > 0)
            {
                int j = random.nextInt(fortune + 2) - 1;

                if (j < 0)
                {
                    j = 0;
                }

                return this.quantityDropped(random) * (j + 1);
            }
        default:
            return this.quantityDropped(random);
        }
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
    {
        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) state.getValue(BASIC_TYPE_VENUS));
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
        {
            int i;

            switch (type)
            {
            case ORE_QUARTZ:
            case ORE_SILICON:
                i = MathHelper.getInt(rand, 2, 5);
                break;
            default:
                i = 0;
                break;
            }

            return i;
        }
        return 0;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (EnumBlockBasicVenus type : EnumBlockBasicVenus.values())
        {
            list.add(new ItemStack(this, 1, type.getMeta()));
        }
    }

    @Override
    public boolean isValueable(IBlockState state)
    {
        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) state.getValue(BASIC_TYPE_VENUS));
        switch (type)
        {
        case ORE_ALUMINUM:
        case ORE_COPPER:
        case ORE_GALENA:
        case ORE_QUARTZ:
        case ORE_SILICON:
        case ORE_TIN:
        case ORE_SOLAR_DUST:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        return false;
    }

    @Override
    public int requiredLiquidBlocksNearby()
    {
        return 4;
    }

    @Override
    public boolean isPlantable(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isTerraformable(World world, BlockPos pos)
    {
        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) world.getBlockState(pos).getValue(BASIC_TYPE_VENUS));

        if (type == EnumBlockBasicVenus.ROCK_HARD || type == EnumBlockBasicVenus.ROCK_SOFT)
        {
            BlockPos above = pos.offset(EnumFacing.UP);
            IBlockState stateAbove = world.getBlockState(above); 
            return stateAbove.getBlock().isAir(stateAbove, world, above);
        }

        return false;
    }

    @Override
    public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target)
    {
        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) world.getBlockState(pos).getValue(BASIC_TYPE_VENUS));
        return type == EnumBlockBasicVenus.ROCK_HARD;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BASIC_TYPE_VENUS, EnumBlockBasicVenus.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumBlockBasicVenus) state.getValue(BASIC_TYPE_VENUS)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BASIC_TYPE_VENUS);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) getStateFromMeta(meta).getValue(BASIC_TYPE_VENUS));
        switch (type)
        {
        case ORE_ALUMINUM:
        case ORE_COPPER:
        case ORE_GALENA:
        case ORE_QUARTZ:
        case ORE_SILICON:
        case ORE_TIN:
        case ORE_SOLAR_DUST:
            return EnumSortCategoryBlock.ORE;
        case DUNGEON_BRICK_1:
        case DUNGEON_BRICK_2:
            return EnumSortCategoryBlock.BRICKS;
        case LEAD_BLOCK:
            return EnumSortCategoryBlock.INGOT_BLOCK;
        default:
            return EnumSortCategoryBlock.GENERAL;
        }
    }
}
