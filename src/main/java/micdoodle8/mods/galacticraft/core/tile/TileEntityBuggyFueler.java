package micdoodle8.mods.galacticraft.core.tile;

import com.google.common.base.Predicate;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.HashSet;
import java.util.List;

public class TileEntityBuggyFueler extends TileEntityMulti implements IMultiBlock, IFuelable, IFuelDock, ICargoEntity, ITickable
{
    public TileEntityBuggyFueler()
    {
        super(null);
    }

    private IDockable dockedEntity;

    @Override
    public void update()
    {
        super.update();

        if (!this.worldObj.isRemote)
        {
            final List<?> list = this.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.fromBounds(this.getPos().getX() - 1.5D, this.getPos().getY() - 2.0, this.getPos().getZ() - 1.5D,
                    this.getPos().getX() + 1.5D, this.getPos().getY() + 4.0, this.getPos().getZ() + 1.5D), new Predicate()
            {
                @Override
                public boolean apply(Object input)
                {
                    return input instanceof IFuelable;
                }
            });

            boolean changed = false;

            for (final Object o : list)
            {
                if (o != null && o instanceof IDockable && !this.worldObj.isRemote)
                {
                    final IDockable fuelable = (IDockable) o;

                    if (fuelable.isDockValid(this))
                    {
                        this.dockedEntity = fuelable;

                        this.dockedEntity.setPad(this);

                        changed = true;
                    }
                }
            }

            if (!changed)
            {
                if (this.dockedEntity != null)
                {
                    this.dockedEntity.setPad(null);
                }

                this.dockedEntity = null;
            }
        }
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        return false;
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
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
                    ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, vecToAdd, placedPosition, 6);
                }
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockVec3 thisBlock = new BlockVec3(this);

        this.worldObj.destroyBlock(new BlockPos(thisBlock.x, thisBlock.y, thisBlock.z), true);

        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                BlockPos pos = new BlockPos(thisBlock.x + x, thisBlock.y, thisBlock.z + z);

                if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
                {
                    FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, this.worldObj.getBlockState(pos));
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
