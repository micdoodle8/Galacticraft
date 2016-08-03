package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenPipe;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
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

public class TileEntityOxygenPipe extends TileEntityOxygenTransmitter implements IColorable
{
    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        TileEntity adjacentTile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, direction);

        if (type == NetworkType.OXYGEN)
        {
            if (adjacentTile instanceof IColorable)
            {
                IBlockState state = this.worldObj.getBlockState(this.getPos());
                IBlockState adjacentTileState = adjacentTile.getWorld().getBlockState(adjacentTile.getPos());
                return this.getColor(state) == ((IColorable) adjacentTile).getColor(adjacentTileState);
            }

            return true;
        }

        return false;
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return this.worldObj == null || !this.worldObj.isRemote;
//
//    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 5;
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
    public void onColorUpdate()
    {
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
    public byte getColor(IBlockState state)
    {
        if (state.getBlock() == GCBlocks.oxygenPipe)
        {
            return (byte) state.getValue(BlockOxygenPipe.COLOR).getDyeDamage();
        }
        return 15;
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

        if (par1NBTTagCompound.hasKey("pipeColor"))
        {
            // Backwards compatibility
            this.worldObj.setBlockState(getPos(), this.worldObj.getBlockState(getPos()).withProperty(BlockOxygenPipe.COLOR, EnumDyeColor.byDyeDamage(par1NBTTagCompound.getByte("pipeColor"))));
        }
    }
}
