package micdoodle8.mods.galacticraft.core;

import net.minecraftforge.common.ForgeChunkManager.Ticket;

public interface IChunkLoader
{
    public void onTicketLoaded(Ticket ticket);
}
