package micdoodle8.mods.galacticraft.core;

import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ChunkLoadingCallback implements LoadingCallback
{
    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world)
    {
        for (Ticket ticket : tickets)
        {
            NBTTagCompound nbt = ticket.getModData();
            
            if (nbt != null)
            {
                int tileX = nbt.getInteger("ChunkLoaderTileX");
                int tileY = nbt.getInteger("ChunkLoaderTileY");
                int tileZ = nbt.getInteger("ChunkLoaderTileZ");
                TileEntity tile = world.getBlockTileEntity(tileX, tileY, tileZ);
                
                if (tile instanceof IChunkLoader)
                {
                    ((IChunkLoader) tile).onTicketLoaded(ticket);
                }
            }
        }
    }
}
