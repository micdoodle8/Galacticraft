package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class TileEntityAirLockController extends TileEntityAirLock
{
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean redstoneActivation;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean playerDistanceActivation = true;
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
    private int otherAirLocks;
    private int lastOtherAirLocks;
    private AirLockProtocol protocol;
    private AirLockProtocol lastProtocol;

    public TileEntityAirLockController()
    {
        this.lastProtocol = this.protocol;
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            this.active = false;

            if (this.redstoneActivation)
            {
                this.active = this.world.isBlockIndirectlyGettingPowered(this.getPos()) > 0;
            }

            if ((this.active || !this.redstoneActivation) && this.playerDistanceActivation)
            {
                double distance = 0D;

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

                Vector3 minPos = new Vector3(this).translate(0.5D - distance);
                Vector3 maxPos = new Vector3(this).translate(0.5D + distance);
                AxisAlignedBB matchingRegion = new AxisAlignedBB(minPos.x, minPos.y, minPos.z, maxPos.x, maxPos.y, maxPos.z);
                List<EntityPlayer> playersWithin = this.world.getEntitiesWithinAABB(EntityPlayer.class, matchingRegion);

                if (this.playerNameMatches)
                {
                    boolean foundPlayer = false;
                    for (EntityPlayer p : playersWithin)
                    {
                        if (PlayerUtil.getName(p).equalsIgnoreCase(this.playerToOpenFor))
                        {
                            foundPlayer = true;
                            break;
                        }
                    }
                    this.active = foundPlayer;
                }
                else
                {
                    this.active = !playersWithin.isEmpty();
                }
            }

            if (!this.invertSelection)
            {
                this.active = !this.active;
            }

            if (this.protocol == null)
            {
                this.protocol = this.lastProtocol = new AirLockProtocol(this);
            }

            if (this.ticks % 5 == 0)
            {
                if (this.horizontalModeEnabled != this.lastHorizontalModeEnabled)
                {
                    this.unsealAirLock();
                }
                else if (this.active || this.lastActive)
                {
                    this.lastOtherAirLocks = this.otherAirLocks;
                    this.otherAirLocks = this.protocol.calculate(this.horizontalModeEnabled);

                    if (this.active)
                    {
                        if (this.otherAirLocks != this.lastOtherAirLocks || !this.lastActive)
                        {
                            this.unsealAirLock();
                            if (this.otherAirLocks >= 0)
                            {
                                this.sealAirLock();
                            }
                        }
                    }
                    else
                    {
                        if (this.lastActive)
                        {
                            this.unsealAirLock();
                        }
                    }
                }

                if (this.active != this.lastActive)
                {
                    IBlockState state = this.world.getBlockState(this.getPos());
                    this.world.notifyBlockUpdate(this.getPos(), state, state, 3);
                }

                this.lastActive = this.active;
                this.lastProtocol = this.protocol;
                this.lastHorizontalModeEnabled = this.horizontalModeEnabled;
            }
        }
    }

    private void sealAirLock()
    {
        int x = (this.lastProtocol.maxX + this.lastProtocol.minX) / 2;
        int y = (this.lastProtocol.maxY + this.lastProtocol.minY) / 2;
        int z = (this.lastProtocol.maxZ + this.lastProtocol.minZ) / 2;

        if (this.world.getBlockState(new BlockPos(x, y, z)).getBlock() != GCBlocks.airLockSeal)
        {
            this.world.playSound(null, x, y, z, GCSounds.openAirLock, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        if (this.horizontalModeEnabled)
        {
            if (this.protocol.minY == this.protocol.maxY && this.protocol.minX != this.protocol.maxX && this.protocol.minZ != this.protocol.maxZ)
            {
                for (x = this.protocol.minX + 1; x <= this.protocol.maxX - 1; x++)
                {
                    for (z = this.protocol.minZ + 1; z <= this.protocol.maxZ - 1; z++)
                    {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (this.world.getBlockState(pos).getBlock().isAir(this.world.getBlockState(pos), this.world, pos))
                        {
                            this.world.setBlockState(pos, GCBlocks.airLockSeal.getDefaultState(), 3);
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
                        BlockPos pos = new BlockPos(x, y, z);
                        if (this.world.getBlockState(pos).getBlock().isAir(this.world.getBlockState(pos), this.world, pos))
                        {
                            this.world.setBlockState(pos, GCBlocks.airLockSeal.getDefaultState(), 3);
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
                        BlockPos pos = new BlockPos(x, y, z);
                        if (this.world.getBlockState(pos).getBlock().isAir(this.world.getBlockState(pos), this.world, pos))
                        {
                            this.world.setBlockState(pos, GCBlocks.airLockSeal.getDefaultState(), 3);
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

        BlockPos pos = new BlockPos(x, y, z);
        IBlockState state = this.world.getBlockState(pos);

        if (state.getMaterial() != Material.AIR)
        {
            this.world.playSound(null, x, y, z, GCSounds.closeAirLock, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        boolean sealedSide = false;
        Block b;
        if (this.lastHorizontalModeEnabled)
        {
            if (this.protocol.minY == this.protocol.maxY && this.protocol.minX != this.protocol.maxX && this.protocol.minZ != this.protocol.maxZ)
            {
                // First test if there is sealed air to either side
                for (x = this.protocol.minX + 1; x <= this.protocol.maxX - 1; x++)
                {
                    for (z = this.protocol.minZ + 1; z <= this.protocol.maxZ - 1; z++)
                    {
                        pos = new BlockPos(x, y, z);
                        b = this.world.getBlockState(pos.up()).getBlock();
                        if (b == GCBlocks.breatheableAir || b == GCBlocks.brightBreatheableAir)
                        {
                            if (this.world.getBlockState(pos).getBlock() == GCBlocks.airLockSeal)
                            {
                                sealedSide = true;
                                break;
                            }
                            continue;
                        }
                        b = this.world.getBlockState(pos.down()).getBlock();
                        if (b == GCBlocks.breatheableAir || b == GCBlocks.brightBreatheableAir)
                        {
                            if (this.world.getBlockState(pos).getBlock() == GCBlocks.airLockSeal)
                            {
                                sealedSide = true;
                                break;
                            }
                        }
                    }
                    if (sealedSide) break;
                }
                // Now replace the airlock blocks with either air, or sealed air
                for (x = this.protocol.minX + 1; x <= this.protocol.maxX - 1; x++)
                {
                    for (z = this.protocol.minZ + 1; z <= this.protocol.maxZ - 1; z++)
                    {
                        pos = new BlockPos(x, y, z);
                        if (this.world.getBlockState(pos).getBlock() == GCBlocks.airLockSeal)
                        {
                            if (sealedSide)
                                this.world.setBlockState(pos, GCBlocks.breatheableAir.getDefaultState(), 3);
                            else
                                this.world.setBlockToAir(pos);
                        }
                    }
                }
            }
        }
        else
        {
            if (this.lastProtocol.minX != this.lastProtocol.maxX)
            {
                // First test if there is sealed air to either side
                for (x = this.lastProtocol.minX + 1; x <= this.lastProtocol.maxX - 1; x++)
                {
                    for (y = this.lastProtocol.minY + 1; y <= this.lastProtocol.maxY - 1; y++)
                    {
                        pos = new BlockPos(x, y, z);
                        b = this.world.getBlockState(pos.north()).getBlock();
                        if (b == GCBlocks.breatheableAir || b == GCBlocks.brightBreatheableAir)
                        {
                            if (this.world.getBlockState(pos).getBlock() == GCBlocks.airLockSeal)
                            {
                                sealedSide = true;
                                break;
                            }
                            continue;
                        }
                        b = this.world.getBlockState(pos.south()).getBlock();
                        if (b == GCBlocks.breatheableAir || b == GCBlocks.brightBreatheableAir)
                        {
                            if (this.world.getBlockState(pos).getBlock() == GCBlocks.airLockSeal)
                            {
                                sealedSide = true;
                                break;
                            }
                        }
                    }
                    if (sealedSide) break;
                }
                // Now replace the airlock blocks with either air, or sealed air
                for (x = this.lastProtocol.minX + 1; x <= this.lastProtocol.maxX - 1; x++)
                {
                    for (y = this.lastProtocol.minY + 1; y <= this.lastProtocol.maxY - 1; y++)
                    {
                        pos = new BlockPos(x, y, z);
                        if (this.world.getBlockState(pos).getBlock() == GCBlocks.airLockSeal)
                        {
                            if (sealedSide)
                                this.world.setBlockState(pos, GCBlocks.breatheableAir.getDefaultState(), 3);
                            else
                                this.world.setBlockToAir(pos);
                        }
                    }
                }
            }
            else if (this.lastProtocol.minZ != this.lastProtocol.maxZ)
            {
                // First test if there is sealed air to either side
                for (z = this.lastProtocol.minZ + 1; z <= this.lastProtocol.maxZ - 1; z++)
                {
                    for (y = this.lastProtocol.minY + 1; y <= this.lastProtocol.maxY - 1; y++)
                    {
                        pos = new BlockPos(x, y, z);
                        b = this.world.getBlockState(pos.west()).getBlock();
                        if (b == GCBlocks.breatheableAir || b == GCBlocks.brightBreatheableAir)
                        {
                            if (this.world.getBlockState(pos).getBlock() == GCBlocks.airLockSeal)
                            {
                                sealedSide = true;
                                break;
                            }
                            continue;
                        }
                        b = this.world.getBlockState(pos.east()).getBlock();
                        if (b == GCBlocks.breatheableAir || b == GCBlocks.brightBreatheableAir)
                        {
                            if (this.world.getBlockState(pos).getBlock() == GCBlocks.airLockSeal)
                            {
                                sealedSide = true;
                                break;
                            }
                        }
                    }
                    if (sealedSide) break;
                }
                // Now replace the airlock blocks with either air, or sealed air
                for (z = this.lastProtocol.minZ + 1; z <= this.lastProtocol.maxZ - 1; z++)
                {
                    for (y = this.lastProtocol.minY + 1; y <= this.lastProtocol.maxY - 1; y++)
                    {
                        pos = new BlockPos(x, y, z);
                        if (this.world.getBlockState(pos).getBlock() == GCBlocks.airLockSeal)
                        {
                            if (sealedSide)
                                this.world.setBlockState(pos, GCBlocks.breatheableAir.getDefaultState(), 3);
                            else
                                this.world.setBlockToAir(pos);
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
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
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
