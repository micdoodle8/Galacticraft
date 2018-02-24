package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityGasLiquefier;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockMachineMarsT2 extends BlockTileGC implements IShiftDescription, ISortableBlock
{
    public static final int GAS_LIQUEFIER_META = 0;
    public static final int METHANE_SYNTHESIZER_META = 4;
    public static final int ELECTROLYZER_META = 8;

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum<EnumMachineType> TYPE = PropertyEnum.create("type", EnumMachineType.class);

    public enum EnumMachineType implements IStringSerializable
    {
        GAS_LIQUEFIER(GAS_LIQUEFIER_META, "gas_liquefier"),
        METHANE_SYNTHESIZER(METHANE_SYNTHESIZER_META, "methane_synthesizer"),
        ELECTROLYZER(ELECTROLYZER_META, "electrolyzer");

        private final int meta;
        private final String name;

        private EnumMachineType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    /*private IIcon iconMachineSide;
    private IIcon iconInput;
    private IIcon iconGasInput;
    private IIcon iconGasOutput;
    private IIcon iconWaterInput;

    private IIcon iconGasLiquefier;
    private IIcon iconMethaneSynthesizer;
    private IIcon iconElectrolyzer;*/

    public BlockMachineMarsT2(String assetName)
    {
        super(GCBlocks.machine);
        this.setStepSound(Block.soundTypeMetal);
        this.setUnlocalizedName(assetName);
    }

    /*@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("galacticraftasteroids:machine");
        this.iconInput = par1IconRegister.registerIcon("galacticraftasteroids:machine_input");

        this.iconMachineSide = par1IconRegister.registerIcon("galacticraftasteroids:machine_side_warning");
        this.iconGasInput = par1IconRegister.registerIcon("galacticraftasteroids:machine_oxygen_input_warning");
        this.iconGasOutput = par1IconRegister.registerIcon("galacticraftasteroids:machine_oxygen_output_warning");
        this.iconWaterInput = par1IconRegister.registerIcon("galacticraftasteroids:machine_water_input_warning");
        this.iconGasLiquefier = par1IconRegister.registerIcon("galacticraftasteroids:gasLiquefier");
        this.iconMethaneSynthesizer = par1IconRegister.registerIcon("galacticraftasteroids:methaneSynthesizer");
        this.iconElectrolyzer = par1IconRegister.registerIcon("galacticraftasteroids:electrolyzer");
    }*/

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    /*@Override
    public IIcon getIcon(int side, int metadata)
    {
        if (side == 0)
        {
            return this.iconInput;
        }

        if (side == 1)
        {
            return this.blockIcon;
        }

        int metaside = (metadata & 3) + 2;
        metadata &= 12;

        if (metadata == BlockMachineMarsT2.GAS_LIQUEFIER)
        {
            if (side == metaside)
            {
                return this.iconGasInput;
            }

            //2->5 3->4 4->2 5->3
            if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == side)
            {
                return this.iconGasLiquefier;
            }
        }
        else if (metadata == BlockMachineMarsT2.METHANE_SYNTHESIZER)
        {
            if (side == metaside)
            {
                return this.iconGasInput;
            }

            if (side == (metaside ^ 1))
            {
                return this.iconGasOutput;
            }

            //2->5 3->4 4->2 5->3
            if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == side)
            {
                return this.iconMethaneSynthesizer;
            }
        }
        else if (metadata == BlockMachineMarsT2.ELECTROLYZER)
        {
            if (side == (metaside ^ 1))
            {
                return this.iconGasOutput;
            }

            //2->5 3->4 4->2 5->3
            if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == side)
            {
                return this.iconElectrolyzer;
            }
            
            if (side == metaside)
            {
            	return this.iconWaterInput;
            }
            
           	return this.iconGasOutput;
        }

        return this.iconMachineSide;
    }*/

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int metadata = state.getBlock().getMetaFromState(state);

        int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex();

        worldIn.setBlockState(pos, getStateFromMeta((metadata & 12) + change), 3);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState state = world.getBlockState(pos);
        TileBaseUniversalElectrical.onUseWrenchBlock(state, world, pos, state.getValue(FACING));
        return true;
    }

    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        playerIn.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_MARS, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        int metadata = state.getBlock().getMetaFromState(state);
        metadata &= 12;

        if (metadata == BlockMachineMarsT2.GAS_LIQUEFIER_META)
        {
            return new TileEntityGasLiquefier();
        }
        else if (metadata == BlockMachineMarsT2.METHANE_SYNTHESIZER_META)
        {
            return new TileEntityMethaneSynthesizer();
        }
        else if (metadata == BlockMachineMarsT2.ELECTROLYZER_META)
        {
            return new TileEntityElectrolyzer();
        }

        return null;
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(new ItemStack(this, 1, BlockMachineMarsT2.GAS_LIQUEFIER_META));
        par3List.add(new ItemStack(this, 1, BlockMachineMarsT2.METHANE_SYNTHESIZER_META));
        par3List.add(new ItemStack(this, 1, BlockMachineMarsT2.ELECTROLYZER_META));
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getBlock().getMetaFromState(state) & 12;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        int metadata = this.getDamageValue(world, pos);

        return new ItemStack(this, 1, metadata);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        final TileEntity te = worldIn.getTileEntity(pos);

        if (te instanceof TileEntityGasLiquefier)
        {
            final TileEntityGasLiquefier tileEntity = (TileEntityGasLiquefier) te;

            if (tileEntity.processTicks > 0)
            {
                final float x = pos.getX() + 0.5F;
                final float y = pos.getY() + 0.8F + 0.05F * rand.nextInt(3);
                final float z = pos.getZ() + 0.5F;

                for (float i = -0.41F + 0.16F * rand.nextFloat(); i < 0.5F; i += 0.167F)
                {
                    if (rand.nextInt(3) == 0)
                    {
                        GalacticraftCore.proxy.spawnParticle("whiteSmokeTiny", new Vector3(x + i, y, z - 0.41F), new Vector3(0.0D, -0.015D, -0.0015D), new Object[] {});
                    }
                    if (rand.nextInt(3) == 0)
                    {
                        GalacticraftCore.proxy.spawnParticle("whiteSmokeTiny", new Vector3(x + i, y, z + 0.537F), new Vector3(0.0D, -0.015D, 0.0015D), new Object[] {});
                    }
                    if (rand.nextInt(3) == 0)
                    {
                        GalacticraftCore.proxy.spawnParticle("whiteSmokeTiny", new Vector3(x - 0.41F, y, z + i), new Vector3(-0.0015D, -0.015D, 0.0D), new Object[] {});
                    }
                    if (rand.nextInt(3) == 0)
                    {
                        GalacticraftCore.proxy.spawnParticle("whiteSmokeTiny", new Vector3(x + 0.537F, y, z + i), new Vector3(0.0015D, -0.015D, 0.0D), new Object[] {});
                    }
                }
            }
        }
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case ELECTROLYZER_META:
            return GCCoreUtil.translate("tile.electrolyzer.description");
        case GAS_LIQUEFIER_META:
            return GCCoreUtil.translate("tile.gas_liquefier.description");
        case METHANE_SYNTHESIZER_META:
            return GCCoreUtil.translate("tile.methane_synthesizer.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumMachineType type = EnumMachineType.values()[meta / 4];
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex() + ((EnumMachineType) state.getValue(TYPE)).getMeta();
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, FACING, TYPE);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
