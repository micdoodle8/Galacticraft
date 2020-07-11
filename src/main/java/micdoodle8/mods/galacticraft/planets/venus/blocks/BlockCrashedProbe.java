package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityCrashedProbe;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockCrashedProbe extends BlockTileGC implements ISortableBlock, ITileEntityProvider
{
//    public static final String CRASHED_PROBE = "crashedProbe";
//    private static final List<WeightedRandomChestContent> CONTENTS = Lists.newArrayList(
//            new WeightedRandomChestContent(MarsItems.marsItemBasic, 3, 3, 6, 5), // Tier 2 plate
//            new WeightedRandomChestContent(GCItems.heavyPlatingTier1, 0, 3, 6, 5), // Tier 1 plate
//            new WeightedRandomChestContent(AsteroidsItems.basicItem, 6, 3, 6, 5), // Titanium plate
//            new WeightedRandomChestContent(Items.IRON_INGOT, 0, 5, 9, 5), // Titanium plate
//            new WeightedRandomChestContent(AsteroidsItems.basicItem, 5, 3, 6, 5)); // Tier 3 plate

//    static
//    {
//        net.minecraftforge.common.ChestGenHooks.init(CRASHED_PROBE, CONTENTS, 4, 6);
//    }

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockCrashedProbe(String assetName)
    {
        super(Material.IRON);
        this.blockHardness = 4.5F;
        this.blockResistance = 2.5F;
        this.setSoundType(SoundType.METAL);
        this.setTickRandomly(true);
        this.setUnlocalizedName(assetName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityCrashedProbe();
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.65, pos.getY() + 1.0, pos.getZ() + 0.9, 0.0, 0.0, 0.0);
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.2, pos.getY() + 1.0, pos.getZ() + 0.2, 0.0, 0.0, 0.0);
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 1.0, pos.getY() + 0.25, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        playerIn.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_VENUS, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityCrashedProbe && ((TileEntityCrashedProbe)tile).getDropCore())
        {
            spawnItem(worldIn, pos);
        }

        super.breakBlock(worldIn, pos, state);
    }

    //Drops a Radioisotope Core as well as the Crashed Probe block
    private void spawnItem(World worldIn, BlockPos pos)
    {
        final float f = 0.7F;
        Random syncRandom = GCCoreUtil.getRandom(pos);
        final double d0 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final double d1 = syncRandom.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
        final double d2 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final EntityItem entityitem = new EntityItem(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, new ItemStack(VenusItems.basicItem, 1, 2));
        entityitem.setDefaultPickupDelay();
        worldIn.spawnEntity(entityitem);
    }
    
        @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex();
        worldIn.setBlockState(pos, getStateFromMeta(change), 3);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
