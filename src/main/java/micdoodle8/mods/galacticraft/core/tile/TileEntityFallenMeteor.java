package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class TileEntityFallenMeteor extends TileEntityAdvanced implements IPacketReceiver
{
    public static final int MAX_HEAT_LEVEL = 5000;
    @NetworkedField(targetSide = Side.CLIENT)
    public int heatLevel = TileEntityFallenMeteor.MAX_HEAT_LEVEL;
    private boolean sentOnePacket = false;

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote && this.heatLevel > 0)
        {
            this.heatLevel--;
        }
    }

    public int getHeatLevel()
    {
        return this.heatLevel;
    }

    public void setHeatLevel(int heatLevel)
    {
        this.heatLevel = heatLevel;
    }

    public float getScaledHeatLevel()
    {
        return (float) this.heatLevel / TileEntityFallenMeteor.MAX_HEAT_LEVEL;
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        if (this.worldObj.isRemote)
        {
            this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        this.sentOnePacket = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.heatLevel = nbt.getInteger("MeteorHeatLevel");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("MeteorHeatLevel", this.heatLevel);
    }

    @Override
    public double getPacketRange()
    {
        return 50;
    }

    @Override
    public int getPacketCooldown()
    {
        return this.sentOnePacket ? 100 : 1;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }
}
