package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.UUID;

public class BlockTelemetry extends BlockAdvancedTile implements IShiftDescription
{
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    //Metadata: 0-3 = orientation;  bits 2,3 = reserved for future use
    public BlockTelemetry(Properties builder)
    {
        super(builder);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
//        int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
//        int change = 0;
//
//        switch (angle)
//        {
//        case 0:
//            change = 3;
//            break;
//        case 1:
//            change = 4;
//            break;
//        case 2:
//            change = 2;
//            break;
//        case 3:
//            change = 5;
//            break;
//        }

        worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing()), 3);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        Direction facing = world.getBlockState(pos).get(FACING);

        int change = 0;

        switch (facing)
        {
        case DOWN:
            change = 1;
            break;
        case UP:
            change = 3;
            break;
        case NORTH:
            change = 5;
            break;
        case SOUTH:
            change = 4;
            break;
        case WEST:
            change = 2;
            break;
        case EAST:
            change = 0;
            break;
        }
//        change += (12 & metadata);
        world.setBlockState(pos, this.getDefaultState().with(FACING, Direction.byIndex(change)), 2);

        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityTelemetry();
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        if (!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityTelemetry)
            {
                ItemStack held = entityPlayer.inventory.getCurrentItem();
                //Look for Frequency Module
                if (!held.isEmpty() && held.getItem() == GCItems.frequencyModule)
                {
                    CompoundNBT fmData = held.getTag();
                    if (fmData != null && fmData.contains("linkedUUIDMost") && fmData.contains("linkedUUIDLeast"))
                    {
                        UUID uuid = new UUID(fmData.getLong("linkedUUIDMost"), fmData.getLong("linkedUUIDLeast"));
                        ((TileEntityTelemetry) tile).addTrackedEntity(uuid);
                        entityPlayer.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.telemetry_succeed.message")));
                    }
                    else
                    {
                        entityPlayer.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.telemetry_fail.message")));

                        if (fmData == null)
                        {
                            fmData = new CompoundNBT();
                            held.setTag(fmData);
                        }
                    }
                    fmData.putInt("teCoordX", pos.getX());
                    fmData.putInt("teCoordY", pos.getY());
                    fmData.putInt("teCoordZ", pos.getZ());
                    fmData.putString("teDim", GCCoreUtil.getDimensionID(world).getRegistryName().toString());
                    return true;
                }

                ItemStack wearing = GCPlayerStats.get(entityPlayer).getFrequencyModuleInSlot();
                if (wearing != null)
                {
                    if (wearing.hasTag() && wearing.getTag().contains("teDim"))
                    {
                        return false;
                    }
                    entityPlayer.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.telemetry_fail_wearing_it.message")));
                }
                else
                {
                    entityPlayer.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.telemetry_fail_no_frequency_module.message")));
                }
            }
        }
        return false;
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.MACHINE;
//    }
}
