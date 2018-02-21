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
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TileEntityLandingPad extends TileEntityMulti implements IMultiBlock, IFuelable, IFuelDock, ICargoEntity
{
    public TileEntityLandingPad()
    {
        super(null);
    }

    private IDockable dockedEntity;
    private boolean initialised;

    @Override
    public void update()
    {
        if (!this.initialised)
        {
            if (!this.worldObj.isRemote) this.onCreate(this.worldObj, this.getPos());
            this.initialiseMultiTiles(this.getPos(), this.worldObj);
            this.initialised = true;
        }

        if (!this.worldObj.isRemote)
        {
            final List<Entity> list = this.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.fromBounds(this.getPos().getX() - 0.5D, this.getPos().getY(), this.getPos().getZ() - 0.5D, this.getPos().getX() + 0.5D, this.getPos().getY() + 1.0D, this.getPos().getZ() + 0.5D));

            boolean docked = false;

            for (final Object o : list)
            {
                if (o instanceof IDockable && !((Entity)o).isDead)
                {
                    final IDockable fuelable = (IDockable) o;

                    if (!fuelable.inFlight())
                    {
                        docked = true;

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
    public void onCreate(World world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();

        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.ROCKET_PAD;
    }
    
    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int y = placedPosition.getY();
        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                if (x == 0 && z == 0) continue;
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            IBlockState stateAt = this.worldObj.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock && (EnumBlockMultiType) stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.ROCKET_PAD)
            {
                if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
                {
                    FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, this.worldObj.getBlockState(pos));
                }
                this.worldObj.destroyBlock(pos, false);
            }
        }
        this.worldObj.destroyBlock(thisBlock, true);

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
        if (!this.worldObj.isBlockLoaded(testPos, false))
            return;

        final TileEntity tile = this.worldObj.getTileEntity(testPos);

        if (tile instanceof ILandingPadAttachable && ((ILandingPadAttachable) tile).canAttachToLandingPad(this.worldObj, this.getPos()))
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
