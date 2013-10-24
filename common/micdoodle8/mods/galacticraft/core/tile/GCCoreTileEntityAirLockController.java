package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import java.util.List;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.AxisAlignedBB;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;

public class GCCoreTileEntityAirLockController extends GCCoreTileEntityAirLock implements IPacketReceiver
{
    public boolean redstoneActivation;
    public boolean playerDistanceActivation;
    public int playerDistanceSelection;
    public boolean playerNameMatches;
    public String playerToOpenFor;
    public boolean invertSelection;
    public boolean horizontalModeEnabled;
    public boolean lastHorizontalModeEnabled;
    private String ownerName = "";

    public boolean active;
    public boolean lastActive;
    public ArrayList<GCCoreTileEntityAirLock> otherAirLocks;
    public ArrayList<GCCoreTileEntityAirLock> lastOtherAirLocks;
    private AirLockProtocol protocol;
    private AirLockProtocol lastProtocol = this.protocol;

    @SuppressWarnings("rawtypes")
    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            boolean optionHandled = false;

            if (this.redstoneActivation)
            {
                optionHandled = true;

                if (this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
                {
                    this.active = true;
                }
                else
                {
                    this.active = false;
                }
            }

            if (this.playerDistanceActivation)
            {
                optionHandled = true;

                double distance = 0.0F;

                switch (this.playerDistanceSelection)
                {
                case 0:
                    distance = 1.0D;
                    break;
                case 1:
                    distance = 2.0D;
                    break;
                case 2:
                    distance = 5.0D;
                    break;
                case 3:
                    distance = 10.0D;
                    break;
                }

                Vector3 thisPos = new Vector3(this).translate(0.5F);
                Vector3 minPos = new Vector3(thisPos).translate(-distance);
                Vector3 maxPos = new Vector3(thisPos).translate(distance);
                AxisAlignedBB matchingRegion = AxisAlignedBB.getBoundingBox(minPos.x, minPos.y, minPos.z, maxPos.x, maxPos.y, maxPos.z);
                List playersWithin = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, matchingRegion);

                boolean foundPlayer = false;

                for (int i = 0; i < playersWithin.size(); i++)
                {
                    Object o = playersWithin.get(i);

                    if (o instanceof EntityPlayer)
                    {
                        if (this.playerNameMatches)
                        {
                            if (((EntityPlayer) o).username.equalsIgnoreCase(this.playerToOpenFor))
                            {
                                foundPlayer = true;
                                break;
                            }
                        }
                        else
                        {
                            foundPlayer = true;
                            break;
                        }
                    }
                }

                this.active = !foundPlayer;
            }

            if (!optionHandled)
            {
                this.active = false;
            }

            if (this.invertSelection)
            {
                this.active = !this.active;
            }

            if (this.protocol == null)
            {
                this.protocol = this.lastProtocol = new AirLockProtocol(this, 40);
            }

            if (this.ticks % 10 == 0)
            {
                if (this.horizontalModeEnabled != this.lastHorizontalModeEnabled)
                {
                    this.unsealAirLock();
                }
                else
                {
                    this.otherAirLocks = this.protocol.calculate(this.horizontalModeEnabled);

                    if (this.active && (this.otherAirLocks != null || this.otherAirLocks != null && this.lastOtherAirLocks != null && this.otherAirLocks != this.lastOtherAirLocks || this.otherAirLocks != null && this.lastOtherAirLocks != null && this.otherAirLocks.size() != this.lastOtherAirLocks.size()))
                    {
                        this.sealAirLock();
                    }
                    else if (!this.active && this.lastActive || this.otherAirLocks == null && this.lastOtherAirLocks != null)
                    {
                        this.unsealAirLock();
                    }
                }

                this.lastActive = this.active;
                this.lastOtherAirLocks = this.otherAirLocks;
                this.lastProtocol = this.protocol;
                this.lastHorizontalModeEnabled = this.horizontalModeEnabled;
            }

            if (this.ticks % 3 == 0)
            {
                PacketManager.sendPacketToClients(this.getPacket(), this.worldObj, new Vector3(this), 12.0D);
            }
        }
    }

    public void sealAirLock()
    {
        int x = this.lastProtocol.minX + (this.lastProtocol.maxX - this.lastProtocol.minX) / 2;
        int y = this.lastProtocol.minY + (this.lastProtocol.maxY - this.lastProtocol.minY) / 2;
        int z = this.lastProtocol.minZ + (this.lastProtocol.maxZ - this.lastProtocol.minZ) / 2;

        if (this.worldObj.getBlockId(x, y, z) != GCCoreBlocks.airLockSeal.blockID)
        {
            this.worldObj.playSoundEffect(x, y, z, GalacticraftCore.ASSET_PREFIX + "player.openairlock", 1.0F, 1.0F);
        }

        if (this.horizontalModeEnabled)
        {
            if (this.protocol.minY == this.protocol.maxY && this.protocol.minX != this.protocol.maxX && this.protocol.minZ != this.protocol.maxZ)
            {
                for (x = this.protocol.minX + 1; x <= this.protocol.maxX - 1; x++)
                {
                    for (z = this.protocol.minZ + 1; z <= this.protocol.maxZ - 1; z++)
                    {
                        int id = this.worldObj.getBlockId(x, y, z);

                        if (id == 0 || Block.blocksList[id].isAirBlock(this.worldObj, x, y, z))
                        {
                            this.worldObj.setBlock(x, this.protocol.minY, z, GCCoreBlocks.airLockSeal.blockID, 0, 3);
                        }
                    }
                }
            }
        }
        else
        {
            if (this.protocol.minX != this.protocol.maxX)
            {
                for (x = this.protocol.minX + 1; x <= this.protocol.maxX - 1; x++)
                {
                    for (y = this.protocol.minY + 1; y <= this.protocol.maxY - 1; y++)
                    {
                        int id = this.worldObj.getBlockId(x, y, z);

                        if (id == 0 || Block.blocksList[id].isAirBlock(this.worldObj, x, y, z))
                        {
                            this.worldObj.setBlock(x, y, this.protocol.minZ, GCCoreBlocks.airLockSeal.blockID, 0, 3);
                        }
                    }
                }
            }
            else if (this.protocol.minZ != this.protocol.maxZ)
            {
                for (z = this.protocol.minZ + 1; z <= this.protocol.maxZ - 1; z++)
                {
                    for (y = this.protocol.minY + 1; y <= this.protocol.maxY - 1; y++)
                    {
                        int id = this.worldObj.getBlockId(x, y, z);

                        if (id == 0 || Block.blocksList[id].isAirBlock(this.worldObj, x, y, z))
                        {
                            this.worldObj.setBlock(this.protocol.minX, y, z, GCCoreBlocks.airLockSeal.blockID, 0, 3);
                        }
                    }
                }
            }
        }
    }

    public void unsealAirLock()
    {
        int x = this.lastProtocol.minX + (this.lastProtocol.maxX - this.lastProtocol.minX) / 2;
        int y = this.lastProtocol.minY + (this.lastProtocol.maxY - this.lastProtocol.minY) / 2;
        int z = this.lastProtocol.minZ + (this.lastProtocol.maxZ - this.lastProtocol.minZ) / 2;

        if (this.worldObj.getBlockId(x, y, z) != 0)
        {
            this.worldObj.playSoundEffect(x, y, z, GalacticraftCore.ASSET_PREFIX + "player.closeairlock", 1.0F, 1.0F);
        }

        if (this.lastHorizontalModeEnabled)
        {
            if (this.protocol.minY == this.protocol.maxY && this.protocol.minX != this.protocol.maxX && this.protocol.minZ != this.protocol.maxZ)
            {
                for (x = this.protocol.minX + 1; x <= this.protocol.maxX - 1; x++)
                {
                    for (z = this.protocol.minZ + 1; z <= this.protocol.maxZ - 1; z++)
                    {
                        int id = this.worldObj.getBlockId(x, y, z);

                        if (id == GCCoreBlocks.airLockSeal.blockID)
                        {
                            this.worldObj.setBlockToAir(x, this.protocol.minY, z);
                        }
                    }
                }
            }
        }
        else
        {
            if (this.lastProtocol.minX != this.lastProtocol.maxX)
            {
                for (x = this.lastProtocol.minX + 1; x <= this.lastProtocol.maxX - 1; x++)
                {
                    for (y = this.lastProtocol.minY + 1; y <= this.lastProtocol.maxY - 1; y++)
                    {
                        int id = this.worldObj.getBlockId(x, y, z);

                        if (id == GCCoreBlocks.airLockSeal.blockID)
                        {
                            this.worldObj.setBlockToAir(x, y, this.lastProtocol.minZ);
                        }
                    }
                }
            }
            else if (this.lastProtocol.minZ != this.lastProtocol.maxZ)
            {
                for (z = this.lastProtocol.minZ + 1; z <= this.lastProtocol.maxZ - 1; z++)
                {
                    for (y = this.lastProtocol.minY + 1; y <= this.lastProtocol.maxY - 1; y++)
                    {
                        int id = this.worldObj.getBlockId(x, y, z);

                        if (id == GCCoreBlocks.airLockSeal.blockID)
                        {
                            this.worldObj.setBlockToAir(this.lastProtocol.minX, y, z);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.ownerName = nbt.getString("OwnerName");
        this.redstoneActivation = nbt.getBoolean("RedstoneActivation");
        this.playerDistanceActivation = nbt.getBoolean("PlayerDistanceActivation");
        this.playerDistanceSelection = nbt.getInteger("PlayerDistanceSelection");
        this.playerNameMatches = nbt.getBoolean("PlayerNameMatches");
        this.playerToOpenFor = nbt.getString("PlayerToOpenFor");
        this.invertSelection = nbt.getBoolean("InvertSelection");
        this.active = nbt.getBoolean("active");
        this.lastActive = nbt.getBoolean("lastActive");
        this.horizontalModeEnabled = nbt.getBoolean("HorizontalModeEnabled");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setString("OwnerName", this.ownerName);
        nbt.setBoolean("RedstoneActivation", this.redstoneActivation);
        nbt.setBoolean("PlayerDistanceActivation", this.playerDistanceActivation);
        nbt.setInteger("PlayerDistanceSelection", this.playerDistanceSelection);
        nbt.setBoolean("PlayerNameMatches", this.playerNameMatches);
        nbt.setString("PlayerToOpenFor", this.playerToOpenFor);
        nbt.setBoolean("InvertSelection", this.invertSelection);
        nbt.setBoolean("active", this.active);
        nbt.setBoolean("lastActive", this.lastActive);
        nbt.setBoolean("HorizontalModeEnabled", this.horizontalModeEnabled);
    }

    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput data)
    {
        try
        {
            this.ownerName = data.readUTF();
            this.redstoneActivation = data.readBoolean();
            this.playerDistanceActivation = data.readBoolean();
            this.playerDistanceSelection = data.readInt();
            this.playerNameMatches = data.readBoolean();
            this.playerToOpenFor = data.readUTF();
            this.invertSelection = data.readBoolean();
            this.horizontalModeEnabled = data.readBoolean();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public Packet getPacket()
    {
        return PacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.ownerName, this.redstoneActivation, this.playerDistanceActivation, this.playerDistanceSelection, this.playerNameMatches, this.playerToOpenFor != null ? this.playerToOpenFor : "", this.invertSelection, this.horizontalModeEnabled);
    }

    public String getOwnerName()
    {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }
}
