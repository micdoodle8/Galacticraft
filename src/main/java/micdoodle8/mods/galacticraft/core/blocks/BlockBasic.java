package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * Metadata: 3 = Tin Decoration Block 1; 4 = Tin Decoration Block 2; 5 = Copper
 * Ore; 6 = Tin Ore; 7 = Aluminium Ore; 8 = Silicon Ore; 9 = Copper Block;
 * 10 = Tin Block;  11 = Aluminium Block;  12 = Meteoric Iron Block; 13 = Silicon Block;
 */
public class BlockBasic extends Block implements IDetectableResource, ISortableBlock
{
    public static final PropertyEnum<EnumBlockBasic> BASIC_TYPE = PropertyEnum.create("basicType", EnumBlockBasic.class);

    public enum EnumBlockBasic implements IStringSerializable
    {
        ALUMINUM_DECORATION_BLOCK_0(3, "deco_block_0"),
        ALUMINUM_DECORATION_BLOCK_1(4, "deco_block_1"),
        ORE_COPPER(5, "ore_copper_gc"),
        ORE_TIN(6, "ore_tin_gc"),
        ORE_ALUMINUM(7, "ore_aluminum_gc"),
        ORE_SILICON(8, "ore_silicon"),
        DECO_BLOCK_COPPER(9, "block_copper_gc"),
        DECO_BLOCK_TIN(10, "block_tin_gc"),
        DECO_BLOCK_ALUMINUM(11, "block_aluminum_gc"),
        DECO_BLOCK_METEOR_IRON(12, "block_meteoric_iron_gc"),
        DECO_BLOCK_SILICON(13, "block_silicon_gc");

        private final int meta;
        private final String name;

        EnumBlockBasic(int meta, String name)
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
            return values()[meta - 3];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockBasic(String assetName)
    {
        super(Material.rock);
        this.setHardness(1.0F);
        this.blockResistance = 15F;
        this.setDefaultState(this.blockState.getBaseState().withProperty(BASIC_TYPE, EnumBlockBasic.ALUMINUM_DECORATION_BLOCK_0));
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        switch (getMetaFromState(state))
        {
        case 8:
            return GCItems.basicItem;
        default:
            return Item.getItemFromBlock(this);
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        switch (getMetaFromState(state))
        {
        case 8:
            return 2;
        default:
            return getMetaFromState(state);
        }
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        int bonus = 0;
        if (ConfigManagerCore.quickMode && this.getMetaFromState(state) == 8)
        {
            bonus = 1;
        }
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(state, random, fortune))
        {
            int j = random.nextInt(fortune + 2) - 1;

            if (j < 0)
            {
                j = 0;
            }

            return this.quantityDropped(random) * (j + 1) + bonus;
        }
        else
        {
            return this.quantityDropped(random) + bonus;
        }
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        int metadata = getMetaFromState(world.getBlockState(pos));

        if (metadata < 5)
        {
            return 2.0F;
            //Decoration blocks are soft, like cauldrons or wood 
        }
        else if (metadata == 12)
        {
            return 8.0F;
            //Meteoric Iron is tougher than diamond
        }
        else if (metadata > 8)
        {
            return 6.0F;
            //Blocks of metal are tough - like diamond blocks in vanilla
        }

        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public float getBlockHardness(World world, BlockPos pos)
    {
        int metadata = getMetaFromState(world.getBlockState(pos));

        if (metadata == 5 || metadata == 6)
        {
            return 5.0F;
        }

        if (metadata == 7)
        {
            return 6.0F;
        }

        if (metadata == 8)
        {
            return 3.0F;
        }

        return this.blockHardness;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tabs, List<ItemStack> list)
    {
        for (int var4 = 3; var4 <= 13; ++var4)
        {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    @Override
    public boolean isValueable(IBlockState state)
    {
        switch (this.getMetaFromState(state))
        {
        case 5:
        case 6:
        case 7:
        case 8:
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        int metadata = getMetaFromState(world.getBlockState(pos));

        if (metadata == 8)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, metadata);
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
            return EnumSortCategoryBlock.DECORATION;
        case 5:
        case 6:
        case 7:
        case 8:
            return EnumSortCategoryBlock.ORE;
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
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
        if (meta == 8)
        {
            Random rand = world instanceof World ? ((World)world).rand : new Random();
            return MathHelper.getRandomIntegerInRange(rand, 2, 5);
        }
        return 0;
    }
}
