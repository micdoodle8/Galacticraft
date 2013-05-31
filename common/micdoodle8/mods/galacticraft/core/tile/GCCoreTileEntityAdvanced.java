package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public abstract class GCCoreTileEntityAdvanced extends TileEntity
{
    public int direction;

    @Override
    public void validate()
    {
        super.validate();

        if (!this.isInvalid() && this.worldObj != null)
        {
            this.onTileEntityCreation();
        }
    }

    public abstract void onTileEntityCreation();

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.direction = par1NBTTagCompound.getInteger("direction");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("direction", this.direction);
    }

    public int getDirection()
    {
        return this.direction;
    }

    public ForgeDirection getForgeDirection()
    {
        return ForgeDirection.getOrientation(this.direction);
    }

    public void setDirection(int dir)
    {
        this.direction = dir;
    }
}
