package micdoodle8.mods.galacticraft.API;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

/**
 *  The BLOCK file should implement this interface, not the TileEntity. 
 *  
 *  For the Tile Entities, see {@link="IOxygenAcceptor"} {@link="IOxygenTransmitter"} {@link="IOxygenSource"}
 */
public interface IConnectableToPipe 
{
	public boolean isConnectableOnSide(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection side);
}
