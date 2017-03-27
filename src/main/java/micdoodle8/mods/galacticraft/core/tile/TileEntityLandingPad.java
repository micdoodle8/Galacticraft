package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.entity.ILandable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.List;

public class TileEntityLandingPad extends TileEntityMulti implements IMultiBlock, IFuelable, IFuelDock, ICargoEntity
{
    public TileEntityLandingPad()
    {
        super(null);
    }

    private IDockable dockedEntity;

    @Override
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            final List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.getPos().getX() - 0.5D, this.getPos().getY(), this.getPos().getZ() - 0.5D, this.getPos().getX() + 0.5D, this.getPos().getY() + 1.0D, this.getPos().getZ() + 0.5D));

            boolean docked = false;

            for (final Object o : list)
            {
                if (o instanceof IDockable && !((Entity)o).isDead) 
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

        // TODO: Find a more efficient way to fix this
        //          Broken since 1.8 and this is an inefficient fix
        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                if (!(x == 0 && z == 0))
                {
                    final BlockPos vecToAdd = new BlockPos(getPos().getX() + x, getPos().getY(), getPos().getZ() + z);

                    TileEntity tile = this.world.getTileEntity(vecToAdd);
                    if (tile instanceof TileEntityMulti)
                    {
                        BlockPos pos = ((TileEntityMulti) tile).mainBlockPosition;
                        if (pos == null || !pos.equals(getPos()))
                        {
                            ((TileEntityMulti) tile).mainBlockPosition = getPos();
                        }
                    }
                }
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
                    ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, vecToAdd, placedPosition, 2);
                }
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();

        this.world.destroyBlock(thisBlock, true);

        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                BlockPos pos = new BlockPos(thisBlock.getX() + x, thisBlock.getY(), thisBlock.getZ() + z);
                if (this.world.isRemote && this.world.rand.nextDouble() < 0.1D)
                {
                    FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, GCBlocks.landingPad.getDefaultState());
                }

                this.world.destroyBlock(pos, false);
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

        for (int x = this.getPos().getX() - 1; x < this.getPos().getX() + 2; x++)
        {
        	this.testConnectedTile(x, this.getPos().getZ() - 2, connectedTiles);
        	this.testConnectedTile(x, this.getPos().getZ() + 2, connectedTiles);
        }

        for (int z = this.getPos().getZ() -1; z < this.getPos().getZ() + 2; z++)
                {
        	this.testConnectedTile(this.getPos().getX() - 2, z, connectedTiles);
        	this.testConnectedTile(this.getPos().getX() + 2, z, connectedTiles);
        }

        return connectedTiles;
    }
    
    private void testConnectedTile(int x, int z, HashSet<ILandingPadAttachable> connectedTiles)
                    {
        BlockPos testPos = new BlockPos(x, this.getPos().getY(), z);
        if (!this.world.isBlockLoaded(testPos, false))
            return;

        final TileEntity tile = this.world.getTileEntity(testPos);

        if (tile instanceof ILandingPadAttachable && ((ILandingPadAttachable) tile).canAttachToLandingPad(this.world, this.getPos()))
                        {
                            connectedTiles.add((ILandingPadAttachable) tile);
                            if (GalacticraftCore.isPlanetsLoaded && tile instanceof TileEntityLaunchController)
                            {
                                ((TileEntityLaunchController) tile).setAttachedPad(this);
                            }
                        }
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
        return new AxisAlignedBB(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 0.4D, getPos().getZ() + 2);
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
