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
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
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

    public BlockCrashedProbe(Properties builder)
    {
        super(builder);
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityCrashedProbe();
    }

    @Override
    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.65, pos.getY() + 1.0, pos.getZ() + 0.9, 0.0, 0.0, 0.0);
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.2, pos.getY() + 1.0, pos.getZ() + 0.2, 0.0, 0.0, 0.0);
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 1.0, pos.getY() + 0.25, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        playerIn.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_VENUS, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
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
        final ItemEntity entityitem = new ItemEntity(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, new ItemStack(VenusItems.basicItem, 1, 2));
        entityitem.setDefaultPickupDelay();
        worldIn.addEntity(entityitem);
    }
}
