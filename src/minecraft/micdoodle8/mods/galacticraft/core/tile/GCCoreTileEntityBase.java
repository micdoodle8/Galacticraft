package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;

import mekanism.api.ITileNetwork;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.prefab.tile.TileEntityDisableable;

import com.google.common.io.ByteArrayDataInput;

/**
 * Base code by Aidancbrady
 * 
 * Implemented into Galacticraft by micdoodle8
 *
 */
public abstract class GCCoreTileEntityBase extends TileEntityDisableable implements ITileNetwork
{
	public int direction;
	
	public int playersUsing = 0;

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		this.updateTile();
	}
	
	@Override
    public void readFromNBT(NBTTagCompound nbtTags)
    {
        super.readFromNBT(nbtTags);
        direction = nbtTags.getInteger("facing");
    }

	@Override
    public void writeToNBT(NBTTagCompound nbtTags)
    {
        super.writeToNBT(nbtTags);
        nbtTags.setInteger("facing", direction);
    }
	
	public abstract void updateTile();
	
	@Override
	public void handlePacketData(ByteArrayDataInput dataStream)
	{
		direction = dataStream.readInt();
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
		worldObj.updateAllLightTypes(xCoord, yCoord, zCoord);
	}
	
	@Override
	public ArrayList getNetworkedData(ArrayList data)
	{
		data.add(direction);
		return data;
	}
	
	@Override
	public void validate()
	{
		super.validate();
	}
}
