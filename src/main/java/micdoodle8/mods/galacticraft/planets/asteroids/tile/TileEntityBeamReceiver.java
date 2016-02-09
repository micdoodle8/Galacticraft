package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceAdjacent;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceWireless;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorage;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorageTile;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseConductor;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.tile.ReceiverMode;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

@SuppressWarnings("unused")
public class TileEntityBeamReceiver extends TileEntityBeamOutput implements IEnergyHandlerGC, ILaserNode
{
    @NetworkedField(targetSide = Side.CLIENT)
    public int facing = ForgeDirection.UNKNOWN.ordinal();
    private int preLoadFacing = -1;
    private float maxRate = 1500;
    private EnergyStorage storage = new EnergyStorage(10 * maxRate, maxRate);  //In broken circuits, Beam Receiver will accept energy for 0.5s (15000gJ max) then stop
    @NetworkedField(targetSide = Side.CLIENT)
    public int modeReceive = ReceiverMode.UNDEFINED.ordinal();
    public Vector3 color = new Vector3(0, 1, 0);

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (this.preLoadFacing != -1)
        {
            this.setFacing(ForgeDirection.getOrientation(this.preLoadFacing));
            this.preLoadFacing = -1;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.getTarget() != null && this.modeReceive == ReceiverMode.EXTRACT.ordinal() && this.facing != ForgeDirection.UNKNOWN.ordinal())
            {
                TileEntity tile = this.getAttachedTile();

                if (tile instanceof TileBaseUniversalElectricalSource)
                {
                    //GC energy source
                	TileBaseUniversalElectricalSource electricalTile = (TileBaseUniversalElectricalSource) tile;

                    if (electricalTile.storage.getEnergyStoredGC() > 0)
                    {
                        EnergySourceAdjacent source = new EnergySourceAdjacent(ForgeDirection.getOrientation(this.facing ^ 1));
                        float toSend = Math.min(electricalTile.storage.getMaxExtract(), electricalTile.storage.getEnergyStoredGC());
                        float transmitted = this.getTarget().receiveEnergyGC(new EnergySourceWireless(Lists.newArrayList((ILaserNode) this)), toSend, false);
                        electricalTile.extractEnergyGC(source, transmitted, false);
                    }
                } else if (!(tile instanceof EnergyStorageTile) && !(tile instanceof TileBaseConductor))
                	  //Another mod's energy source
                	  //But don't use other mods methods to connect Beam Receivers to GC's own wires or machines
                {
                    ForgeDirection inputAdj = ForgeDirection.getOrientation(this.facing);
                	float availableToSend = EnergyUtil.otherModsEnergyExtract(tile, inputAdj, this.maxRate, true);
                	if (availableToSend > 0F)
                	{
                        float transmitted = this.getTarget().receiveEnergyGC(new EnergySourceWireless(Lists.newArrayList((ILaserNode) this)), availableToSend, false);
                        EnergyUtil.otherModsEnergyExtract(tile, inputAdj, transmitted, false);
                	}               	
                }
            }

            if (this.modeReceive == ReceiverMode.RECEIVE.ordinal() && this.storage.getEnergyStoredGC() > 0)
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
	                    EnergySourceAdjacent source = new EnergySourceAdjacent(ForgeDirection.getOrientation(this.facing ^ 1));
	                    this.storage.extractEnergyGCnoMax(electricalTile.receiveEnergyGC(source, maxTransfer, false), false);
	                }
	                else if (!(tileAdj instanceof EnergyStorageTile) && !(tileAdj instanceof TileBaseConductor))
	                	  //Dont use other mods methods to connect Beam Receivers to GC's own wires or machines
	                {
	                    ForgeDirection inputAdj = ForgeDirection.getOrientation(this.facing);
	                	float otherModTransferred = EnergyUtil.otherModsEnergyTransfer(tileAdj, inputAdj, maxTransfer, false);
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
        Vector3 headVec = new Vector3(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5);
        ForgeDirection facingDir = ForgeDirection.getOrientation(this.facing);
        headVec.x += facingDir.offsetX * 0.1F;
        headVec.y += facingDir.offsetY * 0.1F;
        headVec.z += facingDir.offsetZ * 0.1F;
        return headVec;
    }

    @Override
    public Vector3 getOutputPoint(boolean offset)
    {
        Vector3 headVec = new Vector3(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5);
        ForgeDirection facingDir = ForgeDirection.getOrientation(this.facing);
        headVec.x += facingDir.offsetX * 0.1F;
        headVec.y += facingDir.offsetY * 0.1F;
        headVec.z += facingDir.offsetZ * 0.1F;
        return headVec;
    }

    @Override
    public TileEntity getTile()
    {
        return this;
    }

    public TileEntity getAttachedTile()
    {
        if (this.facing == ForgeDirection.UNKNOWN.ordinal())
        {
            return null;
        }

        TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, this.facing);

        if (tile == null || tile.isInvalid())
        {
            this.setFacing(ForgeDirection.UNKNOWN);
        }

        if (tile instanceof IConductor)
        {
            this.setFacing(ForgeDirection.UNKNOWN);
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

        //TileEntity tile = this.getAttachedTile();

        if (this.facing == ForgeDirection.UNKNOWN.ordinal())
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

        if (this.facing == ForgeDirection.UNKNOWN.ordinal())
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

        if (this.facing == ForgeDirection.UNKNOWN.ordinal())
        {
            return 0;
        }

        return this.storage.getEnergyStoredGC();
    }

    @Override
    public float getMaxEnergyStoredGC(EnergySource from)
    {
        TileEntity tile = this.getAttachedTile();

        if (this.facing == ForgeDirection.UNKNOWN.ordinal())
        {
            return 0;
        }

        return this.storage.getCapacityGC();
    }

    @Override
    public boolean nodeAvailable(EnergySource from)
    {
        TileEntity tile = this.getAttachedTile();

        return this.facing != ForgeDirection.UNKNOWN.ordinal();

    }

    public void setFacing(ForgeDirection newDirection)
    {
        if (newDirection.ordinal() != this.facing)
        {
            if (newDirection == ForgeDirection.UNKNOWN)
            {
                this.modeReceive = ReceiverMode.UNDEFINED.ordinal();
            }
            else
            {
                TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, newDirection);

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

        this.facing = newDirection.ordinal();
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
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.preLoadFacing = nbt.getInteger("FacingSide");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("FacingSide", this.facing);
    }
}
