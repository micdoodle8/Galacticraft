package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityMulti extends TileEntityAdvanced implements IPacketReceiver
{
    // The the position of the main block
    @NetworkedField(targetSide = Side.CLIENT)
    public BlockPos mainBlockPosition;

    public void setMainBlock(BlockPos mainBlock)
    {
        this.mainBlockPosition = mainBlock;

        if (!this.worldObj.isRemote)
        {
            this.worldObj.markBlockForUpdate(this.getPos());
        }
    }

    public void onBlockRemoval()
    {
        if (this.mainBlockPosition != null)
        {
            TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition);

            if (tileEntity instanceof IMultiBlock)
            {
                IMultiBlock mainBlock = (IMultiBlock) tileEntity;
                mainBlock.onDestroy(this);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, EntityPlayer player)
    {
        if (this.mainBlockPosition != null)
        {
            TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition);

            if (tileEntity instanceof IMultiBlock)
            {
            	return ((IMultiBlock) tileEntity).onActivated(player);
            }
        }

        return false;
    }
    
    public TileEntity getMainBlockTile()
    {
        if (this.mainBlockPosition != null)
        {
            return this.worldObj.getTileEntity(this.mainBlockPosition);
        }

        return null;
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        NBTTagCompound tag = nbt.getCompoundTag("mainBlockPosition");
        this.mainBlockPosition = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        if (this.mainBlockPosition != null)
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("x", this.mainBlockPosition.getX());
            tag.setInteger("y", this.mainBlockPosition.getY());
            tag.setInteger("z", this.mainBlockPosition.getZ());
            nbt.setTag("mainBlockPosition", tag);
        }
    }

    @Override
    public double getPacketRange()
    {
        return 30.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 50;
    }

    @Override
    public boolean isNetworkedTile()
    {
    	 return (this.mainBlockPosition != null);
    }
}
