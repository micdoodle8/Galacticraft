package micdoodle8.mods.galacticraft.core.world;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

/**
 * IChunkLoader.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public interface IChunkLoader
{
	public void onTicketLoaded(Ticket ticket, boolean placed);

	public Ticket getTicket();

	public World getWorldObj();

	public ChunkCoordinates getCoords();

	public String getOwnerName();

	public void setOwnerName(String ownerName);
}
