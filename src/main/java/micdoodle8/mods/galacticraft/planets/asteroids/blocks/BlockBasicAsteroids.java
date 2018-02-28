package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockBasicAsteroids extends Block implements IDetectableResource, IPlantableBlock, ITerraformableBlock, ISortableBlock
{
    public static final PropertyEnum<EnumBlockBasic> BASIC_TYPE = PropertyEnum.create("basicTypeAsteroids", EnumBlockBasic.class);

    public enum EnumBlockBasic implements IStringSerializable
    {
        ASTEROID_0(0, "asteroid_rock_0"),
        ASTEROID_1(1, "asteroid_rock_1"),
        ASTEROID_2(2, "asteroid_rock_2"),
        ORE_ALUMINUM(3, "ore_aluminum_asteroids"),
        ORE_ILMENITE(4, "ore_ilmenite_asteroids"),
        ORE_IRON(5, "ore_iron_asteroids"),
        DECO(6, "asteroid_deco"),
        TITANIUM_BLOCK(7, "titanium_block");

        private final int meta;
        private final String name;

        private EnumBlockBasic(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumBlockBasic byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockBasicAsteroids(String assetName)
    {
        super(Material.rock);
        this.blockHardness = 3.0F;
        this.setUnlocalizedName(assetName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        if (state.getValue(BASIC_TYPE) == EnumBlockBasic.ORE_ILMENITE && world instanceof World)
        {
            ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

            int count = quantityDropped(state, fortune, ((World) world).rand);
            for (int i = 0; i < count; i++)
            {
                ret.add(new ItemStack(AsteroidsItems.basicItem, 1, 3));
            }
            count = quantityDropped(state, fortune, ((World) world).rand);
            for (int i = 0; i < count; i++)
            {
                ret.add(new ItemStack(AsteroidsItems.basicItem, 1, 4));
            }
            return ret;
        }


        return super.getDrops(world, pos, state, fortune);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        switch (getMetaFromState(state))
        {
        case 4:
            return 0;
        default:
            return getMetaFromState(state);
        }
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        switch (getMetaFromState(state))
        {
        case 4:
            if (fortune >= 1)
            {
                return (random.nextFloat() < fortune * 0.29F - 0.25F) ? 2 : 1;
            }
        default:
            return 1;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        int var4;

        for (var4 = 0; var4 < EnumBlockBasic.values().length; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    @Override
    public boolean isValueable(IBlockState state)
    {
        switch (this.getMetaFromState(state))
        {
        case 3:
        case 4:
        case 5:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
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
        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        IBlockState state = world.getBlockState(pos);
        if (getMetaFromState(state) == 4)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(state));
        }

        return super.getPickBlock(target, world, pos, player);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BASIC_TYPE, EnumBlockBasic.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumBlockBasic) state.getValue(BASIC_TYPE)).getMeta();
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, BASIC_TYPE);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        switch (meta)
        {
        case 3:
        case 4:
        case 5:
            return EnumSortCategoryBlock.ORE;
        case 6:
            return EnumSortCategoryBlock.DECORATION;
        case 7:
            return EnumSortCategoryBlock.INGOT_BLOCK;
        }
        return EnumSortCategoryBlock.GENERAL;
    }

    @Override
    public int getExpDrop(IBlockAccess world, BlockPos pos, int fortune)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return 0;
        
        int meta = this.getMetaFromState(state);
        if (meta == 4)
        {
            Random rand = world instanceof World ? ((World)world).rand : new Random();
            return MathHelper.getRandomIntegerInRange(rand, 2, 3);
        }
        return 0;
    }
}
