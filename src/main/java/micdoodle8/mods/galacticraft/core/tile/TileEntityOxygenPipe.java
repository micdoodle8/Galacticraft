package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.tile.IColorable;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityOxygenPipe extends TileEntityOxygenTransmitter implements IColorable, IPacketReceiver
{
    @NetworkedField(targetSide = Side.CLIENT)
    public byte pipeColor = 15;
    private byte lastPipeColor = -1;

    @Override
    public boolean canConnect(ForgeDirection direction, NetworkType type)
    {
        TileEntity adjacentTile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, direction);

        if (type == NetworkType.OXYGEN)
        {
            if (adjacentTile instanceof IColorable)
            {
                return this.getColor() == ((IColorable) adjacentTile).getColor();
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean canUpdate()
    {
        return this.worldObj == null || !this.worldObj.isRemote;

    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote && this.ticks % 60 == 0 && this.lastPipeColor != this.getColor())
        {
            GalacticraftCore.packetPipeline.sendToDimension(new PacketDynamic(this), this.worldObj.provider.dimensionId);
            this.lastPipeColor = this.getColor();
        }
    }

    @Override
    public double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 1;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return !this.worldObj.isRemote;
    }

    @Override
    public void validate()
    {
        super.validate();

        if (this.worldObj != null && this.worldObj.isRemote)
        {
            final BlockVec3 thisVec = new BlockVec3(this);
            this.worldObj.func_147479_m(thisVec.x, thisVec.y, thisVec.z);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setColor(byte col)
    {
        this.pipeColor = col;

        if (this.worldObj != null)
        {
            if (this.worldObj.isRemote)
            {
                final BlockVec3 thisVec = new BlockVec3(this);
                this.worldObj.func_147479_m(thisVec.x, thisVec.y, thisVec.z);
            }
            else
            {
                this.getNetwork().split(this);
                this.resetNetwork();
            }
        }

    }

    @Override
    public byte getColor()
    {
        return this.pipeColor;
    }

    @Override
    public void onAdjacentColorChanged(ForgeDirection direction)
    {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);

        if (!this.worldObj.isRemote)
        {
            this.refresh();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        final byte by = par1NBTTagCompound.getByte("pipeColor");
        this.setColor(by);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setByte("pipeColor", this.getColor());
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        byte colorBefore = this.pipeColor;
        super.decodePacketdata(buffer);

        if (this.pipeColor != colorBefore && this.worldObj instanceof WorldClient)
        {
            this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
        }
    }
}
