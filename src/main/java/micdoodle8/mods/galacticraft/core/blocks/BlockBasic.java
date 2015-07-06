package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * Metadata: 3 = Tin Decoration Block 1 4 = Tin Decoration Block 2 5 = Copper
 * Ore 6 = Tin Ore 7 = Aluminium Ore 8 = Silicon Ore 9 = Copper Block
 * 10 = Tin Block  11 = Aluminium Block  12 = Meteoric Iron Block
 */
public class BlockBasic extends Block implements IDetectableResource
{
    /*IIcon[] iconBuffer;*/

    public static final PropertyEnum BASIC_TYPE = PropertyEnum.create("basicType", EnumBlockBasic.class);

    private enum EnumBlockBasic
    {
        ALUMINUM_DECORATION_BLOCK_0(3),
        ALUMINUM_DECORATION_BLOCK_1(4),
        ORE_COPPER(5),
        ORE_TIN(6),
        ORE_ALUMINUM(7),
        ORE_SILICON(8),
        DECO_BLOCK_COPPER(9),
        DECO_BLOCK_TIN(10),
        DECO_BLOCK_ALUMINUM(11),
        DECO_BLOCK_METEOR_IRON(12);

        private final int meta;

        private EnumBlockBasic(int meta)
        {
            this.meta = meta;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumBlockBasic byMetadata(int meta)
        {
            return values()[meta - 3];
        }
    }

    protected BlockBasic(String assetName)
    {
        super(Material.rock);
        this.setHardness(1.0F);
        this.blockResistance = 15F;
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    /*@Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.iconBuffer = new IIcon[12];
        this.iconBuffer[0] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_aluminium_2");
        this.iconBuffer[1] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_aluminium_4");
        this.iconBuffer[2] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_aluminium_1");
        this.iconBuffer[3] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_aluminium_4");
        this.iconBuffer[4] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "oreCopper");
        this.iconBuffer[5] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "oreTin");
        this.iconBuffer[6] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "oreAluminum");
        this.iconBuffer[7] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "oreSilicon");
        this.iconBuffer[8] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_copper_block");
        this.iconBuffer[9] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_tin_block");
        this.iconBuffer[10] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_aluminium_block");
        this.iconBuffer[11] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_meteoriron_block");
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        switch (meta)
        {
        case 3:
            switch (side)
            {
            case 0:
                return this.iconBuffer[1];
            case 1:
                return this.iconBuffer[0];
            default:
                return this.iconBuffer[2];
            }
        case 4:
            return this.iconBuffer[3];
        case 5:
            return this.iconBuffer[4];
        case 6:
            return this.iconBuffer[5];
        case 7:
            return this.iconBuffer[6];
        case 8:
            return this.iconBuffer[7];
        case 9:
            return this.iconBuffer[8];
        case 10:
            return this.iconBuffer[9];
        case 11:
            return this.iconBuffer[10];
        case 12:
            return this.iconBuffer[11];
        default:
            return meta < this.iconBuffer.length ? this.iconBuffer[meta] : this.iconBuffer[0];
        }
    }*/

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
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(state, random, fortune))
        {
            int j = random.nextInt(fortune + 2) - 1;

            if (j < 0)
            {
                j = 0;
            }

            return this.quantityDropped(random) * (j + 1);
        }
        else
        {
            return this.quantityDropped(random);
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tabs, List list)
    {
        for (int var4 = 3; var4 < 13; ++var4)
        {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    @Override
    public boolean isValueable(int metadata)
    {
        return metadata >= 5 && metadata <= 8;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
    {
        int metadata = getMetaFromState(world.getBlockState(pos));

        if (metadata == 8)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, metadata);
        }

        return super.getPickBlock(target, world, pos);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BASIC_TYPE, EnumBlockBasic.byMetadata(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((EnumBlockBasic)state.getValue(BASIC_TYPE)).getMeta();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, BASIC_TYPE);
    }
}
