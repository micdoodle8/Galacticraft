package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.entity.ILandable;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashSet;
import java.util.List;

public class TileEntityLandingPad extends TileEntityMulti implements IMultiBlock, IFuelable, IFuelDock, ICargoEntity
{
    private IDockable dockedEntity;

    @Override
    public void update()
    {
        super.update();

        if (!this.worldObj.isRemote)
        {
            final List<?> list = this.worldObj.getEntitiesWithinAABB(IFuelable.class, AxisAlignedBB.fromBounds(this.getPos().getX() - 0.5D, this.getPos().getY(), this.getPos().getZ() - 0.5D, this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D));

            boolean docked = false;

            for (final Object o : list)
            {
                if (o instanceof IDockable)
                {
                    docked = true;

                    final IDockable fuelable = (IDockable) o;

                    if (fuelable != this.dockedEntity && fuelable.isDockValid(this))
                    {
                        if (fuelable instanceof ILandable)
                        {
                        	((ILandable) fuelable).landEntity(this.getPos());
                        }
                        else
                        {
                            fuelable.setPad(this);
                        }
                    }

                    break;
                }
            }

            if (!docked)
            {
                this.dockedEntity = null;
            }
        }
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return true;
//    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        return false;
    }

    @Override
    public void onCreate(BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();

        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                final BlockPos vecToAdd = new BlockPos(placedPosition.getX() + x, placedPosition.getY(), placedPosition.getZ() + z);

                if (!vecToAdd.equals(placedPosition))
                {
                    ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, worldObj.getBlockState(vecToAdd).getBlock().getStateFromMeta(2));
                }
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();

        this.worldObj.destroyBlock(thisBlock, true);

        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                BlockPos pos = new BlockPos(thisBlock.getX() + x, thisBlock.getY(), thisBlock.getZ() + z);
                if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
                {
                    FMLClientHandler.instance().getClient().effectRenderer.func_180533_a(pos, GCBlocks.landingPad.getDefaultState());
                }

                this.worldObj.destroyBlock(pos, false);
            }
        }

        if (this.dockedEntity != null)
        {
            this.dockedEntity.onPadDestroyed();
            this.dockedEntity = null;
        }

    }

    @Override
    public int addFuel(FluidStack liquid, boolean doFill)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.addFuel(liquid, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack removeFuel(int amount)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.removeFuel(amount);
        }

        return null;
    }

    @Override
    public HashSet<ILandingPadAttachable> getConnectedTiles()
    {
        HashSet<ILandingPadAttachable> connectedTiles = new HashSet<ILandingPadAttachable>();

        for (int x = -2; x < 3; x++)
        {
            for (int z = -2; z < 3; z++)
            {
                if (x == -2 || x == 2 || z == -2 || z == 2)
                {
                    if (Math.abs(x) != Math.abs(z))
                    {
                        final TileEntity tile = this.worldObj.getTileEntity(new BlockPos(this.getPos().getX() + x, this.getPos().getY(), this.getPos().getZ() + z));

                        if (tile != null && tile instanceof ILandingPadAttachable && ((ILandingPadAttachable) tile).canAttachToLandingPad(this.worldObj, this.getPos()))
                        {
                            connectedTiles.add((ILandingPadAttachable) tile);
                        }
                    }
                }
            }
        }

        return connectedTiles;
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.addCargo(stack, doAdd);
        }

        return EnumCargoLoadingState.NOTARGET;
    }

    @Override
    public RemovalResult removeCargo(boolean doRemove)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.removeCargo(doRemove);
        }

        return new RemovalResult(EnumCargoLoadingState.NOTARGET, null);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
    	return AxisAlignedBB.fromBounds(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 0.4D, getPos().getZ() + 2);
    }

    @Override
    public boolean isBlockAttachable(IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof ILandingPadAttachable)
        {
            return ((ILandingPadAttachable) tile).canAttachToLandingPad(world, this.getPos());
        }

        return false;
    }

    @Override
    public IDockable getDockedEntity()
    {
        return this.dockedEntity;
    }

    @Override
    public void dockEntity(IDockable entity)
    {
        this.dockedEntity = entity;
    }
}
