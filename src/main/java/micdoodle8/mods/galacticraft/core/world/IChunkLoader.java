package micdoodle8.mods.galacticraft.core.world;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public interface IChunkLoader
{
	public void onTicketLoaded(Ticket ticket, boolean placed);

	public Ticket getTicket();

	public World getWorldObj();

	public ChunkCoordinates getCoords();

	public String getOwnerName();

	public void setOwnerName(String ownerName);
}
