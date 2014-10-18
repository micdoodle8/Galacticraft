package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IFuelable;
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

public class TileEntityBuggyFueler extends TileEntityMulti implements IMultiBlock, IFuelable, IFuelDock, ICargoEntity
{
    protected long ticks = 0;
    private IDockable dockedEntity;

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            final List<?> list = this.worldObj.getEntitiesWithinAABB(IFuelable.class, AxisAlignedBB.getBoundingBox(this.xCoord - 1.5D, this.yCoord - 2.0, this.zCoord - 1.5D, this.xCoord + 1.5D, this.yCoord + 4.0, this.zCoord + 1.5D));

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
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        return false;
    }

    @Override
    public void onCreate(BlockVec3 placedPosition)
    {
        this.mainBlockPosition = placedPosition;

        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                final BlockVec3 vecToAdd = new BlockVec3(placedPosition.x + x, placedPosition.y, placedPosition.z + z);

                if (!vecToAdd.equals(placedPosition))
                {
                    ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 6);
                }
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockVec3 thisBlock = new BlockVec3(this);

        this.worldObj.func_147480_a(thisBlock.x, thisBlock.y, thisBlock.z, true);

        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
                {
                    FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.x + x, thisBlock.y, thisBlock.z + z, GCBlocks.landingPad, Block.getIdFromBlock(GCBlocks.landingPad) >> 12 & 255);
                }

                this.worldObj.setBlockToAir(thisBlock.x + x, thisBlock.y, thisBlock.z + z);
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
                        final TileEntity tile = this.worldObj.getTileEntity(this.xCoord + x, this.yCoord, this.zCoord + z);

                        if (tile != null && tile instanceof ILandingPadAttachable && ((ILandingPadAttachable) tile).canAttachToLandingPad(this.worldObj, this.xCoord, this.yCoord, this.zCoord))
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
    public boolean isBlockAttachable(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof ILandingPadAttachable)
        {
            return ((ILandingPadAttachable) tile).canAttachToLandingPad(world, this.xCoord, this.yCoord, this.zCoord);
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
