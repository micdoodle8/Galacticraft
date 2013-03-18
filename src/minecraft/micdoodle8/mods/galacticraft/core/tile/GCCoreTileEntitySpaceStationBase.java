package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class GCCoreTileEntitySpaceStationBase extends TileEntity
{
	public String ownerUsername;

    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.ownerUsername = par1NBTTagCompound.getString("ownerUsername");
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	par1NBTTagCompound.setString("ownerUsername", ownerUsername);
    }
    
    public void setOwner(String username)
    {
    	this.ownerUsername = username;
    }
    
    public String getOwner()
    {
    	return this.ownerUsername;
    }
}
