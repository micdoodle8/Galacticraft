package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockBasicVenus extends Block implements IDetectableResource, IPlantableBlock, ITerraformableBlock, ISortableBlock
{
    public static final PropertyEnum<EnumBlockBasicVenus> BASIC_TYPE_VENUS = PropertyEnum.create("basicTypeVenus", EnumBlockBasicVenus.class);

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
        LEAD_BLOCK(12, "lead_block");

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

        public static EnumBlockBasicVenus byMetadata(int meta)
        {
            return values()[meta];
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
        super(Material.rock);
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
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
    {
        player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest(worldIn, pos, worldIn.getBlockState(pos), player) && EnchantmentHelper.getSilkTouchModifier(player))
        {
            java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
            ItemStack itemstack = this.createStackedBlock(state);

            if (itemstack != null)
            {
                items.add(itemstack);
            }

            net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, worldIn.getBlockState(pos), 0, 1.0f, true, player);

            for (ItemStack is : items)
            {
                spawnAsEntity(worldIn, pos, is);
            }
        }
        else
        {
            int i = EnchantmentHelper.getFortuneModifier(player);
            harvesters.set(player);
            this.dropBlockAsItem(worldIn, pos, state, i);
            harvesters.set(null);
            if ((EnumBlockBasicVenus) state.getValue(BASIC_TYPE_VENUS) == EnumBlockBasicVenus.ROCK_MAGMA)
            {
                worldIn.setBlockState(pos, Blocks.flowing_lava.getDefaultState());
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
                type == EnumBlockBasicVenus.ORE_SILICON || type == EnumBlockBasicVenus.ORE_TIN)
        {
            return 3.0F;
        }

        return this.blockResistance / 5.0F;
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos)
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
                type == EnumBlockBasicVenus.ORE_SILICON || type == EnumBlockBasicVenus.ORE_TIN)
        {
            return 5.0F;
        }

        return this.blockHardness;
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos)
    {
        return getMetaFromState(worldIn.getBlockState(pos));
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
            return Items.quartz;
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
    public int getExpDrop(net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        IBlockState state = world.getBlockState(pos);
        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) state.getValue(BASIC_TYPE_VENUS));
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
        {
            int i;

            switch (type)
            {
            case ORE_QUARTZ:
            case ORE_SILICON:
                i = MathHelper.getRandomIntegerInRange(rand, 2, 5);
                break;
            default:
                i = 0;
                break;
            }

            return i;
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        for (EnumBlockBasicVenus type : EnumBlockBasicVenus.values())
        {
            par3List.add(new ItemStack(par1, 1, type.getMeta()));
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
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
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
            return world.getBlockState(pos.offset(EnumFacing.UP)).getBlock().isAir(world, pos);
        }

        return false;
    }

    @Override
    public boolean isReplaceableOreGen(World world, BlockPos pos, com.google.common.base.Predicate<IBlockState> target)
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
    protected BlockState createBlockState()
    {
        return new BlockState(this, BASIC_TYPE_VENUS);
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
