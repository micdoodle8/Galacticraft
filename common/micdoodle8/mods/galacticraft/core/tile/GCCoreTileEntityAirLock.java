package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class GCCoreTileEntityAirLock extends TileEntityAdvanced
{
    public boolean active;
    public boolean lastActive;
    public ArrayList<GCCoreTileEntityAirLock> otherAirLocks;
    public ArrayList<GCCoreTileEntityAirLock> lastOtherAirLocks;
    private AirLockProtocol protocol;
    private AirLockProtocol lastProtocol = this.protocol;

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (this.protocol == null)
        {
            this.protocol = this.lastProtocol = new AirLockProtocol(this, 40);
        }

        if (this.ticks % 10 == 0 && !this.worldObj.isRemote)
        {
            this.otherAirLocks = this.protocol.calculate();

            if (this.active && (this.otherAirLocks != null || this.otherAirLocks != null && this.lastOtherAirLocks != null && this.otherAirLocks != this.lastOtherAirLocks || this.otherAirLocks != null && this.lastOtherAirLocks != null && this.otherAirLocks.size() != this.lastOtherAirLocks.size()))
            {
                int x = this.lastProtocol.minX + (this.lastProtocol.maxX - this.lastProtocol.minX) / 2;
                int y = this.lastProtocol.minY + (this.lastProtocol.maxY - this.lastProtocol.minY) / 2;
                int z = this.lastProtocol.minZ + (this.lastProtocol.maxZ - this.lastProtocol.minZ) / 2;

                if (this.worldObj.getBlockId(x, y, z) != GCCoreBlocks.airLockSeal.blockID)
                {
                    this.worldObj.playSoundEffect(x, y, z, "galacticraft.player.openairlock", 1.0F, 1.0F);
                }

                if (this.protocol.minX != this.protocol.maxX)
                {
                    for (x = this.protocol.minX + 1; x <= this.protocol.maxX - 1; x++)
                    {
                        for (y = this.protocol.minY + 1; y <= this.protocol.maxY - 1; y++)
                        {
                            int id = this.worldObj.getBlockId(x, y, z);

                            if (id != 0 && id != GCCoreBlocks.airLockSeal.blockID)
                            {
                                Block block = Block.blocksList[id];

                                if (block != null)
                                {
                                    block.dropBlockAsItem(this.worldObj, x, y, z, this.worldObj.getBlockMetadata(x, y, z), 0);
                                }
                            }

                            this.worldObj.setBlock(x, y, this.protocol.minZ, GCCoreBlocks.airLockSeal.blockID, 0, 3);
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

                            if (id != 0 && id != GCCoreBlocks.airLockSeal.blockID)
                            {
                                Block block = Block.blocksList[id];

                                if (block != null)
                                {
                                    block.dropBlockAsItem(this.worldObj, x, y, z, this.worldObj.getBlockMetadata(x, y, z), 0);
                                }
                            }

                            this.worldObj.setBlock(this.protocol.minX, y, z, GCCoreBlocks.airLockSeal.blockID, 0, 3);
                        }
                    }
                }
            }
            else if (!this.active && this.lastActive || this.otherAirLocks == null && this.lastOtherAirLocks != null)
            {
                int x = this.lastProtocol.minX + (this.lastProtocol.maxX - this.lastProtocol.minX) / 2;
                int y = this.lastProtocol.minY + (this.lastProtocol.maxY - this.lastProtocol.minY) / 2;
                int z = this.lastProtocol.minZ + (this.lastProtocol.maxZ - this.lastProtocol.minZ) / 2;

                if (this.worldObj.getBlockId(x, y, z) != 0)
                {
                    this.worldObj.playSoundEffect(x, y, z, "galacticraft.player.closeairlock", 1.0F, 1.0F);
                }

                if (this.lastProtocol.minX != this.lastProtocol.maxX)
                {
                    for (x = this.lastProtocol.minX + 1; x <= this.lastProtocol.maxX - 1; x++)
                    {
                        for (y = this.lastProtocol.minY + 1; y <= this.lastProtocol.maxY - 1; y++)
                        {
                            int id = this.worldObj.getBlockId(x, y, z);

                            if (id != 0 && id != GCCoreBlocks.airLockSeal.blockID)
                            {
                                Block block = Block.blocksList[id];

                                if (block != null)
                                {
                                    block.dropBlockAsItem(this.worldObj, x, y, z, this.worldObj.getBlockMetadata(x, y, z), 0);
                                }
                            }

                            this.worldObj.setBlockToAir(x, y, this.lastProtocol.minZ);
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

                            if (id != 0 && id != GCCoreBlocks.airLockSeal.blockID)
                            {
                                Block block = Block.blocksList[id];

                                if (block != null)
                                {
                                    block.dropBlockAsItem(this.worldObj, x, y, z, this.worldObj.getBlockMetadata(x, y, z), 0);
                                }
                            }

                            this.worldObj.setBlockToAir(this.lastProtocol.minX, y, z);
                        }
                    }
                }
            }

            this.lastActive = this.active;
            this.lastOtherAirLocks = this.otherAirLocks;
            this.lastProtocol = this.protocol;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.active = par1NBTTagCompound.getBoolean("active");
        this.lastActive = par1NBTTagCompound.getBoolean("lastActive");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setBoolean("active", this.active);
        nbt.setBoolean("lastActive", this.lastActive);
    }
}
