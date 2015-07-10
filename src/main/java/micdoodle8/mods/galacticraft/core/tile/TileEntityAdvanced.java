package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class TileEntityAdvanced extends TileEntity implements IPacketReceiver, IUpdatePlayerListBox
{
    public long ticks = 0;
    private LinkedHashSet<Field> fieldCacheClient;
    private LinkedHashSet<Field> fieldCacheServer;
    @NetworkedField(targetSide = Side.CLIENT)
    public EnumFacing facing = EnumFacing.NORTH;

    @Override
    public void update()
    {
        if (this.ticks == 0)
        {
            this.initiate();
        }

        if (this.ticks >= Long.MAX_VALUE)
        {
            this.ticks = 1;
        }

        this.ticks++;

        if (this.isNetworkedTile() && this.ticks % this.getPacketCooldown() == 0)
        {
            if (this.fieldCacheClient == null || this.fieldCacheServer == null)
            {
                try
                {
                    this.initFieldCache();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            if (this.worldObj.isRemote && this.fieldCacheServer.size() > 0)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
            }
            else if (!this.worldObj.isRemote && this.fieldCacheClient.size() > 0)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new TargetPoint(this.worldObj.provider.getDimensionId(), getPos().getX(), getPos().getY(), getPos().getZ(), this.getPacketRange()));
            }
        }
    }

    private void initFieldCache() throws IllegalArgumentException, IllegalAccessException
    {
        this.fieldCacheClient = new LinkedHashSet<Field>();
        this.fieldCacheServer = new LinkedHashSet<Field>();

        for (Field field : this.getClass().getFields())
        {
            if (field.isAnnotationPresent(NetworkedField.class))
            {
                NetworkedField f = field.getAnnotation(NetworkedField.class);

                if (f.targetSide() == Side.CLIENT)
                {
                    this.fieldCacheClient.add(field);
                }
                else
                {
                    this.fieldCacheServer.add(field);
                }
            }
        }
    }

    public abstract double getPacketRange();

    public abstract int getPacketCooldown();

    public abstract boolean isNetworkedTile();

    public void addExtraNetworkedData(List<Object> networkedList)
    {
    }

    public void readExtraNetworkedData(ByteBuf dataStream)
    {
    }

    public void initiate()
    {
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        Set<Field> fieldList = null;

        if (this.fieldCacheClient == null || this.fieldCacheServer == null)
        {
            try
            {
                this.initFieldCache();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (this.worldObj.isRemote)
        {
            fieldList = this.fieldCacheServer;
        }
        else
        {
            fieldList = this.fieldCacheClient;
        }

        for (Field f : fieldList)
        {
            try
            {
                sendData.add(f.get(this));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        this.addExtraNetworkedData(sendData);
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.fieldCacheClient == null || this.fieldCacheServer == null)
        {
            try
            {
                this.initFieldCache();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (this.worldObj.isRemote && this.fieldCacheClient.size() == 0)
        {
            return;
        }
        else if (!this.worldObj.isRemote && this.fieldCacheServer.size() == 0)
        {
            return;
        }

        Set<Field> fieldSet = null;

        if (this.worldObj.isRemote)
        {
            fieldSet = this.fieldCacheClient;
        }
        else
        {
            fieldSet = this.fieldCacheServer;
        }

        for (Field field : fieldSet)
        {
            try
            {
                Object obj = NetworkUtil.getFieldValueFromStream(field, buffer, this.worldObj);
                field.set(this, obj);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        this.readExtraNetworkedData(buffer);
    }

    @Override
    public void handlePacketData(Side side, EntityPlayer player)
    {

    }

    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
    }
}