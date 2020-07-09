package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceAdjacent;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceWireless;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.tile.*;
import micdoodle8.mods.galacticraft.core.tile.ReceiverMode;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlockNames;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityBeamReceiver extends TileEntityBeamOutput implements IEnergyHandlerGC, ILaserNode
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + AsteroidBlockNames.beamReceiver)
    public static TileEntityType<TileEntityBeamReceiver> TYPE;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public Direction facing = null;
    private int preLoadFacing = -1;
    private final float maxRate = 1500;
    private final EnergyStorage storage = new EnergyStorage(10 * maxRate, maxRate);  //In broken circuits, Beam Receiver will accept energy for 0.5s (15000gJ max) then stop
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int modeReceive = ReceiverMode.UNDEFINED.ordinal();
    public Vector3 color = new Vector3(0, 1, 0);

    public TileEntityBeamReceiver()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.preLoadFacing != -1)
        {
            this.setFacing(Direction.byIndex(this.preLoadFacing));
            this.preLoadFacing = -1;
        }

        if (!this.world.isRemote)
        {
            if (this.getTarget() != null && this.modeReceive == ReceiverMode.EXTRACT.ordinal() && this.facing != null)
            {
                TileEntity tile = this.getAttachedTile();

                if (tile instanceof TileBaseUniversalElectricalSource)
                {
                    //GC energy source
                    TileBaseUniversalElectricalSource electricalTile = (TileBaseUniversalElectricalSource) tile;

                    if (electricalTile.storage.getEnergyStoredGC() > 0)
                    {
                        EnergySourceAdjacent source = new EnergySourceAdjacent(Direction.byIndex(this.facing.getIndex() ^ 1));
                        float toSend = Math.min(electricalTile.storage.getMaxExtract(), electricalTile.storage.getEnergyStoredGC());
                        float transmitted = this.getTarget().receiveEnergyGC(new EnergySourceWireless(Lists.newArrayList(this)), toSend, false);
                        electricalTile.extractEnergyGC(source, transmitted, false);
                    }
                }
                else if (!(tile instanceof EnergyStorageTile) && !(tile instanceof TileBaseConductor))
                //Another mod's energy source
                //But don't use other mods methods to connect Beam Receivers to GC's own wires or machines
                {
                    float availableToSend = EnergyUtil.otherModsEnergyExtract(tile, this.facing, this.maxRate, true);
                    if (availableToSend > 0F)
                    {
                        float transmitted = this.getTarget().receiveEnergyGC(new EnergySourceWireless(Lists.newArrayList(this)), availableToSend, false);
                        EnergyUtil.otherModsEnergyExtract(tile, this.facing, transmitted, false);
                    }
                }
            }
            else if (this.modeReceive == ReceiverMode.RECEIVE.ordinal() && this.storage.getEnergyStoredGC() > 0)
            {
                //One Beam Receiver might be powered by multiple transmitters - allow for 5 at maximum transfer rate
                float maxTransfer = Math.min(this.storage.getEnergyStoredGC(), maxRate * 5);

                if (maxTransfer < 0.01F)
                //Stop updating this when de minimis energy remains
                {
                    this.storage.extractEnergyGCnoMax(maxTransfer, false);
                }
                else
                {
                    TileEntity tileAdj = this.getAttachedTile();

                    if (tileAdj instanceof TileBaseUniversalElectrical)
                    {
                        TileBaseUniversalElectrical electricalTile = (TileBaseUniversalElectrical) tileAdj;
                        EnergySourceAdjacent source = new EnergySourceAdjacent(this.facing.getOpposite());
                        this.storage.extractEnergyGCnoMax(electricalTile.receiveEnergyGC(source, maxTransfer, false), false);
                    }
                    else if (!(tileAdj instanceof EnergyStorageTile) && !(tileAdj instanceof TileBaseConductor))
                    //Dont use other mods methods to connect Beam Receivers to GC's own wires or machines
                    {
                        float otherModTransferred = EnergyUtil.otherModsEnergyTransfer(tileAdj, this.facing, maxTransfer, false);
                        if (otherModTransferred > 0F)
                        {
                            this.storage.extractEnergyGCnoMax(otherModTransferred, false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public double getPacketRange()
    {
        return 24.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    @Override
    public Vector3 getInputPoint()
    {
        Vector3 headVec = new Vector3(this.getPos().getX() + 0.5F, this.getPos().getY() + 0.5F, this.getPos().getZ() + 0.5F);
        if (this.facing != null)
        {
            headVec.x += this.facing.getXOffset() * 0.1F;
            headVec.y += this.facing.getYOffset() * 0.1F;
            headVec.z += this.facing.getZOffset() * 0.1F;
        }
        return headVec;
    }

    @Override
    public Vector3 getOutputPoint(boolean offset)
    {
        Vector3 headVec = new Vector3(this.getPos().getX() + 0.5F, this.getPos().getY() + 0.5F, this.getPos().getZ() + 0.5F);
        if (this.facing != null)
        {
            headVec.x += this.facing.getXOffset() * 0.1F;
            headVec.y += this.facing.getYOffset() * 0.1F;
            headVec.z += this.facing.getZOffset() * 0.1F;
        }
        return headVec;
    }

    @Override
    public TileEntity getTile()
    {
        return this;
    }

    public TileEntity getAttachedTile()
    {
        if (this.facing == null)
        {
            return null;
        }

        TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.world, this.facing);

        if (tile == null || tile.isRemoved())
        {
            this.setFacing(null);
        }

        if (tile instanceof IConductor)
        {
            this.setFacing(null /* TODO */);
            return null;
        }

        if (tile instanceof EnergyStorageTile)
        {
            EnergyStorage attachedStorage = ((EnergyStorageTile) tile).storage;
            this.storage.setCapacity(attachedStorage.getCapacityGC() - attachedStorage.getEnergyStoredGC());
            this.storage.setMaxExtract(attachedStorage.getMaxExtract());
            this.storage.setMaxReceive(attachedStorage.getMaxReceive());
        }

        return tile;
    }

    @Override
    public float receiveEnergyGC(EnergySource from, float amount, boolean simulate)
    {
        if (this.modeReceive != ReceiverMode.RECEIVE.ordinal())
        {
            return 0;
        }

        this.getAttachedTile();

        if (this.facing == null)
        {
            return 0;
        }

        //		if (received < amount)
        //		{
        //			if (tile instanceof EnergyStorageTile)
        //			{
        //				received += ((EnergyStorageTile) tile).storage.receiveEnergyGC(amount - received, simulate);
        //			}
        //		}

        return this.storage.receiveEnergyGC(amount, simulate);
    }

    @Override
    public float extractEnergyGC(EnergySource from, float amount, boolean simulate)
    {
        if (this.modeReceive != ReceiverMode.EXTRACT.ordinal())
        {
            return 0;
        }

        TileEntity tile = this.getAttachedTile();

        if (this.facing == null)
        {
            return 0;
        }

        float extracted = this.storage.extractEnergyGC(amount, simulate);

        if (extracted < amount)
        {
            if (tile instanceof EnergyStorageTile)
            {
                extracted += ((EnergyStorageTile) tile).storage.extractEnergyGC(amount - extracted, simulate);
            }
        }

        return extracted;
    }

    @Override
    public float getEnergyStoredGC(EnergySource from)
    {
        TileEntity tile = this.getAttachedTile();

        if (this.facing == null)
        {
            return 0;
        }

        return this.storage.getEnergyStoredGC();
    }

    @Override
    public float getMaxEnergyStoredGC(EnergySource from)
    {
        TileEntity tile = this.getAttachedTile();

        if (this.facing == null)
        {
            return 0;
        }

        return this.storage.getCapacityGC();
    }

    @Override
    public boolean nodeAvailable(EnergySource from)
    {
        TileEntity tile = this.getAttachedTile();

        return this.facing != null;

    }

    public void setFacing(Direction newDirection)
    {
        if (newDirection != this.facing)
        {
            if (newDirection == null)
            {
                this.modeReceive = ReceiverMode.UNDEFINED.ordinal();
            }
            else
            {
                TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.world, newDirection);

                if (tile == null)
                {
                    this.modeReceive = ReceiverMode.UNDEFINED.ordinal();
                }
                else if (tile instanceof EnergyStorageTile)
                {
                    ReceiverMode mode = ((EnergyStorageTile) tile).getModeFromDirection(newDirection.getOpposite());

                    if (mode != null)
                    {
                        this.modeReceive = mode.ordinal();
                    }
                    else
                    {
                        this.modeReceive = ReceiverMode.UNDEFINED.ordinal();
                    }
                }
                else if (EnergyUtil.otherModCanReceive(tile, newDirection.getOpposite()))
                {
                    this.modeReceive = ReceiverMode.RECEIVE.ordinal();
                }
                else if (EnergyUtil.otherModCanProduce(tile, newDirection.getOpposite()))
                {
                    this.modeReceive = ReceiverMode.EXTRACT.ordinal();
                }
            }
        }

        this.facing = newDirection;
    }

    @Override
    public boolean canConnectTo(ILaserNode laserNode)
    {
        if (this.modeReceive != ReceiverMode.UNDEFINED.ordinal() && this.color.equals(laserNode.getColor()))
        {
            if (laserNode instanceof TileEntityBeamReceiver)
            {
                return ((TileEntityBeamReceiver) laserNode).modeReceive != this.modeReceive;
            }

            return true;
        }

        return false;
    }

    @Override
    public Vector3 getColor()
    {
        return new Vector3(0, 1, 0);
    }

    @Override
    public ILaserNode getTarget()
    {
        if (this.modeReceive == ReceiverMode.EXTRACT.ordinal())
        {
            return super.getTarget();
        }

        return null;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.preLoadFacing = nbt.getInt("FacingSide");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("FacingSide", this.facing == null ? this.preLoadFacing : this.facing.ordinal());
        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    private AxisAlignedBB renderAABB;

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(pos, pos.add(1, 2, 1));
        }
        return this.renderAABB;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_SHORT;
    }
}
