package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
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

public class TileEntityOxygenPipe extends TileEntityOxygenTransmitter implements IColorable, IPacketReceiver
{
    @NetworkedField(targetSide = Side.CLIENT)
    public byte pipeColor = 15;
    private byte lastPipeColor = -1;

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
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
    public void update()
    {
        super.update();

        if (!this.worldObj.isRemote && this.ticks % 60 == 0 && this.lastPipeColor != this.getColor())
        {
            GalacticraftCore.packetPipeline.sendToDimension(new PacketDynamic(this), this.worldObj.provider.getDimensionId());
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
            this.worldObj.notifyLightSet(getPos());
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
                this.worldObj.notifyLightSet(getPos());
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
    public void onAdjacentColorChanged(EnumFacing direction)
    {
        this.worldObj.markBlockForUpdate(this.getPos());

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
            this.worldObj.notifyLightSet(this.getPos());
        }
    }
}
