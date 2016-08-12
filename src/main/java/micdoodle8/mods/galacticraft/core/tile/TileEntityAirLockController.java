package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class TileEntityAirLockController extends TileEntityAirLock
{
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean redstoneActivation;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean playerDistanceActivation;
    @NetworkedField(targetSide = Side.CLIENT)
    public int playerDistanceSelection;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean playerNameMatches;
    @NetworkedField(targetSide = Side.CLIENT)
    public String playerToOpenFor = "";
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean invertSelection;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean horizontalModeEnabled;
    public boolean lastHorizontalModeEnabled;
    @NetworkedField(targetSide = Side.CLIENT)
    public String ownerName = "";

    @NetworkedField(targetSide = Side.CLIENT)
    public boolean active;
    public boolean lastActive;
    public ArrayList<TileEntityAirLock> otherAirLocks;
    public ArrayList<TileEntityAirLock> lastOtherAirLocks;
    private AirLockProtocol protocol;
    private AirLockProtocol lastProtocol;

    public TileEntityAirLockController()
    {
        this.lastProtocol = this.protocol;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void update()
    {
        super.update();
 
        if (!this.worldObj.isRemote)
        {
            this.active = false;

            if (this.redstoneActivation)
            {
                this.active = this.worldObj.isBlockIndirectlyGettingPowered(this.getPos()) > 0;
            }

            if ((this.active || !this.redstoneActivation) && this.playerDistanceActivation)
            {
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
                AxisAlignedBB matchingRegion = AxisAlignedBB.fromBounds(minPos.x, minPos.y, minPos.z, maxPos.x, maxPos.y, maxPos.z);
                List playersWithin = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, matchingRegion);

                boolean foundPlayer = false;

                for (int i = 0; i < playersWithin.size(); i++)
                {
                    Object o = playersWithin.get(i);

                    if (o instanceof EntityPlayer)
                    {
                        if (this.playerNameMatches)
                        {
                            if (((EntityPlayer) o).getGameProfile().getName().equalsIgnoreCase(this.playerToOpenFor))
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

            if (this.invertSelection)
            {
                this.active = !this.active;
            }

            if (this.protocol == null)
            {
                this.protocol = this.lastProtocol = new AirLockProtocol(this);
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

                    if (this.active && (this.otherAirLocks != null || 
                    		(this.lastOtherAirLocks != null && this.otherAirLocks != this.lastOtherAirLocks) || 
                    		(this.lastOtherAirLocks != null && this.otherAirLocks.size() != this.lastOtherAirLocks.size())))
                    {
                        this.sealAirLock();
                    }
                    else if (!this.active && this.lastActive || this.otherAirLocks == null && this.lastOtherAirLocks != null)
                    {
                        this.unsealAirLock();
                    }
                }

                if (this.active != this.lastActive)
                {
                    this.worldObj.markBlockForUpdate(this.getPos());
                }

                this.lastActive = this.active;
                this.lastOtherAirLocks = this.otherAirLocks;
                this.lastProtocol = this.protocol;
                this.lastHorizontalModeEnabled = this.horizontalModeEnabled;
            }
        }
    }

    public void sealAirLock()
    {
        int x = this.lastProtocol.minX + (this.lastProtocol.maxX - this.lastProtocol.minX) / 2;
        int y = this.lastProtocol.minY + (this.lastProtocol.maxY - this.lastProtocol.minY) / 2;
        int z = this.lastProtocol.minZ + (this.lastProtocol.maxZ - this.lastProtocol.minZ) / 2;

        if (this.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock() != GCBlocks.airLockSeal)
        {
            this.worldObj.playSoundEffect(x, y, z, GalacticraftCore.TEXTURE_PREFIX + "player.openairlock", 1.0F, 1.0F);
        }

        if (this.horizontalModeEnabled)
        {
            if (this.protocol.minY == this.protocol.maxY && this.protocol.minX != this.protocol.maxX && this.protocol.minZ != this.protocol.maxZ)
            {
                for (x = this.protocol.minX + 1; x <= this.protocol.maxX - 1; x++)
                {
                    for (z = this.protocol.minZ + 1; z <= this.protocol.maxZ - 1; z++)
                    {
                        Block blockAt = this.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock();

                        if (blockAt.isAir(this.worldObj, new BlockPos(x, y, z)))
                        {
                            this.worldObj.setBlockState(new BlockPos(x, this.protocol.minY, z), GCBlocks.airLockSeal.getDefaultState(), 3);
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
                        Block blockAt = this.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock();

                        if (blockAt.isAir(this.worldObj, new BlockPos(x, y, z)))
                        {
                            this.worldObj.setBlockState(new BlockPos(x, y, this.protocol.minZ), GCBlocks.airLockSeal.getDefaultState(), 3);
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
                        Block blockAt = this.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock();

                        if (blockAt.isAir(this.worldObj, new BlockPos(x, y, z)))
                        {
                            this.worldObj.setBlockState(new BlockPos(this.protocol.minX, y, z), GCBlocks.airLockSeal.getDefaultState(), 3);
                        }
                    }
                }
            }
        }
    }

    public void unsealAirLock()
    {
        if (this.lastProtocol == null)
        {
            return;
        }

        int x = this.lastProtocol.minX + (this.lastProtocol.maxX - this.lastProtocol.minX) / 2;
        int y = this.lastProtocol.minY + (this.lastProtocol.maxY - this.lastProtocol.minY) / 2;
        int z = this.lastProtocol.minZ + (this.lastProtocol.maxZ - this.lastProtocol.minZ) / 2;

        if (this.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock().getMaterial() != Material.AIR)
        {
            this.worldObj.playSoundEffect(x, y, z, GalacticraftCore.TEXTURE_PREFIX + "player.closeairlock", 1.0F, 1.0F);
        }

        if (this.lastHorizontalModeEnabled)
        {
            if (this.protocol.minY == this.protocol.maxY && this.protocol.minX != this.protocol.maxX && this.protocol.minZ != this.protocol.maxZ)
            {
                for (x = this.protocol.minX + 1; x <= this.protocol.maxX - 1; x++)
                {
                    for (z = this.protocol.minZ + 1; z <= this.protocol.maxZ - 1; z++)
                    {
                        Block blockAt = this.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock();

                        if (blockAt == GCBlocks.airLockSeal)
                        {
                            this.worldObj.setBlockToAir(new BlockPos(x, this.protocol.minY, z));
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
                        Block blockAt = this.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock();

                        if (blockAt == GCBlocks.airLockSeal)
                        {
                            this.worldObj.setBlockToAir(new BlockPos(x, y, this.lastProtocol.minZ));
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
                        Block blockAt = this.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock();

                        if (blockAt == GCBlocks.airLockSeal)
                        {
                            this.worldObj.setBlockToAir(new BlockPos(this.lastProtocol.minX, y, z));
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
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
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
    public double getPacketRange()
    {
        return 20.0D;
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
}
