package micdoodle8.mods.galacticraft.core.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public interface IChunkLoader
{
    void onTicketLoaded(Ticket ticket, boolean placed);

    Ticket getTicket();

    World getWorld();

    BlockPos getCoords();

    String getOwnerName();

    void setOwnerName(String ownerName);
}
