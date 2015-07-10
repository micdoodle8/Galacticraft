package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityGasLiquefier;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockMachineMarsT2 extends BlockTileGC implements ItemBlockDesc.IBlockShiftDesc
{
    public static final int GAS_LIQUEFIER = 0;
    public static final int METHANE_SYNTHESIZER = 4;
    public static final int ELECTROLYZER = 8;

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

    @Override
    public int getRenderType()
    {
        return GalacticraftPlanets.getBlockRenderID(this);
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
        int change = 0;

        switch (angle)
        {
        case 0:
            change = 3;
            break;
        case 1:
            change = 1;
            break;
        case 2:
            change = 2;
            break;
        case 3:
            change = 0;
            break;
        }

        worldIn.setBlockState(pos, getStateFromMeta((metadata & 12) + change), 3);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState state = world.getBlockState(pos);
        int metadata = state.getBlock().getMetaFromState(state);
        int original = metadata & 3;
        int change = 0;

        // Re-orient the block
        switch (original)
        {
        case 0:
            change = 3;
            break;
        case 3:
            change = 1;
            break;
        case 1:
            change = 2;
            break;
        case 2:
            change = 0;
            break;
        }

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBaseUniversalElectrical)
        {
            ((TileBaseUniversalElectrical) te).updateFacing();
        }

        world.setBlockState(pos, getStateFromMeta((metadata & 12) + change), 3);
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

        if (metadata == BlockMachineMarsT2.GAS_LIQUEFIER)
        {
            return new TileEntityGasLiquefier();
        }
        else if (metadata == BlockMachineMarsT2.METHANE_SYNTHESIZER)
        {
            return new TileEntityMethaneSynthesizer();
        }
        else if (metadata == BlockMachineMarsT2.ELECTROLYZER)
        {
            return new TileEntityElectrolyzer();
        }

        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(this, 1, BlockMachineMarsT2.GAS_LIQUEFIER));
        par3List.add(new ItemStack(this, 1, BlockMachineMarsT2.METHANE_SYNTHESIZER));
        par3List.add(new ItemStack(this, 1, BlockMachineMarsT2.ELECTROLYZER));
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
                        GalacticraftCore.proxy.spawnParticle("whiteSmokeTiny", new Vector3(x + i, y, z - 0.41F), new Vector3(0.0D, -0.015D, -0.0015D), new Object [] { } );
                    }
                    if (rand.nextInt(3) == 0)
                    {
                        GalacticraftCore.proxy.spawnParticle("whiteSmokeTiny", new Vector3(x + i, y, z + 0.537F), new Vector3(0.0D, -0.015D, 0.0015D), new Object [] { } );
                    }
                    if (rand.nextInt(3) == 0)
                    {
                        GalacticraftCore.proxy.spawnParticle("whiteSmokeTiny", new Vector3(x - 0.41F, y, z + i), new Vector3(-0.0015D, -0.015D, 0.0D), new Object [] { } );
                    }
                    if (rand.nextInt(3) == 0)
                    {
                        GalacticraftCore.proxy.spawnParticle("whiteSmokeTiny", new Vector3(x + 0.537F, y, z + i), new Vector3(0.0015D, -0.015D, 0.0D), new Object [] { } );
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
        case ELECTROLYZER:
            return GCCoreUtil.translate("tile.electrolyzer.description");
        case GAS_LIQUEFIER:
            return GCCoreUtil.translate("tile.gasLiquefier.description");
        case METHANE_SYNTHESIZER:
            return GCCoreUtil.translate("tile.methaneSynthesizer.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
