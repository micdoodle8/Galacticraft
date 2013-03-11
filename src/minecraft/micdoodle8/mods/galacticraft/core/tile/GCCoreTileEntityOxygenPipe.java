package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.IPressurizedTube;
import mekanism.api.ITubeConnection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class GCCoreTileEntityOxygenPipe extends TileEntity implements ITubeConnection, IPressurizedTube
{
	@Override
	public boolean canTransferGas()
	{
		return true;
	}

	@Override
	public boolean canTubeConnect(ForgeDirection side) 
	{
		return true;
	}
}