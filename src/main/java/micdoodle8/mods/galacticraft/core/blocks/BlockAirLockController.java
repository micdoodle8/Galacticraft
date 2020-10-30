package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.client.gui.container.GuiAirLockController;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAirLockController extends BlockAdvancedTile implements IShiftDescription, ISortable
{
    public BlockAirLockController(Properties builder)
    {
        super(builder);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityAirLockController && placer instanceof PlayerEntity)
        {
            ((TileEntityAirLockController) tile).ownerName = PlayerUtil.getName(((PlayerEntity) placer));
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityAirLockController();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
    {
        return true;
    }

    @Override
    public ActionResultType onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntityAirLockController && world.isRemote)
        {
            Minecraft.getInstance().displayGuiScreen(new GuiAirLockController((TileEntityAirLockController) tile));
//            NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(state, world, pos), buf -> buf.writeBlockPos(pos));
//            playerIn.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }


    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            TileEntity tile = worldIn.getTileEntity(pos);

            if (tile instanceof TileEntityAirLockController)
            {
                ((TileEntityAirLockController) tile).unsealAirLock();
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
    {
        return null;
    }

    @Override
    public String getShiftDescription(ItemStack item)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack item)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }
}
