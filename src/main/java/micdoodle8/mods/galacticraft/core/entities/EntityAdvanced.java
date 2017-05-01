package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.util.*;

public abstract class EntityAdvanced extends Entity implements IPacketReceiver
{
    protected long ticks = 0;
    private LinkedHashSet<Field> fieldCacheClient;
    private LinkedHashSet<Field> fieldCacheServer;
    private Map<Field, Object> lastSentData = new HashMap<Field, Object>();
    private boolean networkDataChanged = false;

    public EntityAdvanced(World world)
    {
        super(world);
        
        if (world != null && world.isRemote)
        {
            //Empty packet client->server just to kickstart the server into sending this client an initial packet
            this.fieldCacheServer = new LinkedHashSet<Field>();
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
    }

    /**
     * Whether or not this entity should be sending packets to/from server
     *
     * @return If the entity needs network capabilities
     */
    public abstract boolean isNetworkedEntity();

    /**
     * Get the amount of ticks between each packet send
     *
     * @param side The target side.
     * @return The amount of ticks to wait before sending another packet to this
     * target
     */
    public abstract int getPacketCooldown(Side side);

    /**
     * Add any additional data to the stream
     * (only effective if there are both CLIENT and SERVER targeted
     * regular networked fields ... currently nothing in GC uses this)
     *
     * @param networkedList List of additional data
     */
//    public void addExtraNetworkedData(List<Object> networkedList)
//    {
//
//    }

    /**
     * Read any additional data from the stream
     *
     * @param stream The ByteBuf stream to read data from
     */
//    public void readExtraNetworkedData(ByteBuf stream)
//    {
//
//    }

    /**
     * Called after a packet is read, only on client side.
     *
     * @param player The player associated with the received packet
     */
    public abstract void onPacketClient(EntityPlayer player);

    /**
     * Called after a packet is read, only on server side.
     *
     * @param player The player associated with the received packet
     */
    public abstract void onPacketServer(EntityPlayer player);

    /**
     * Packets will be sent to all (client-side) players within this range
     *
     * @return Maximum distance to send packets to client players
     */
    public abstract double getPacketRange();

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        this.ticks++;

        if (this.isNetworkedEntity())
        {
            if (!this.worldObj.isRemote && this.ticks % this.getPacketCooldown(Side.CLIENT) == 0)
            {
                if (this.fieldCacheClient == null)
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

                PacketDynamic packet = new PacketDynamic(this);
                if (networkDataChanged)
                {
                    GalacticraftCore.packetPipeline.sendToAllAround(packet, new TargetPoint(GCCoreUtil.getDimensionID(this.worldObj), this.posX, this.posY, this.posZ, this.getPacketRange()));
                }
            }

            if (this.worldObj.isRemote && this.ticks % this.getPacketCooldown(Side.SERVER) == 0)
            {
                if (this.fieldCacheClient == null)  //The target server cache may have been initialised to an empty set
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

                PacketDynamic packet = new PacketDynamic(this);
                if (networkDataChanged)
                {
                    GalacticraftCore.packetPipeline.sendToServer(packet);
                }
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

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        Set<Field> fieldList = null;
        boolean changed = false;

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
            boolean fieldChanged = false;
            try
            {
                Object data = f.get(this);
                Object lastData = lastSentData.get(f);

                if (!NetworkUtil.fuzzyEquals(lastData, data))
                {
                    fieldChanged = true;
                }

                sendData.add(data);

                if (fieldChanged)
                {
                    lastSentData.put(f, NetworkUtil.cloneNetworkedObject(data));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            changed |= fieldChanged;
        }

//Currently unused as there is no entity in Galacticraft with extraNetworkedData
        
//        if (changed)
//        {
//            this.addExtraNetworkedData(sendData);
//        }
//        else
//        {
//            ArrayList<Object> prevSendData = new ArrayList<Object>(sendData);
//
//            this.addExtraNetworkedData(sendData);
//
//            if (!prevSendData.equals(sendData))
//            {
//                changed = true;
//            }
//        }

        networkDataChanged = changed;
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

//        if (this.worldObj.isRemote && this.fieldCacheClient.size() == 0)
//        {
//            return;
//        }
//        else if (!this.worldObj.isRemote && this.fieldCacheServer.size() == 0)
//        {
//            return;
//        }

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

//        this.readExtraNetworkedData(buffer);
    }
}
