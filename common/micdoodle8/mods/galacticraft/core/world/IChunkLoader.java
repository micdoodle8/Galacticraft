package micdoodle8.mods.galacticraft.core.world;

import net.minecraftforge.common.ForgeChunkManager.Ticket;

public interface IChunkLoader
{
    public void onTicketLoaded(Ticket ticket);
}
