package micdoodle8.mods.galacticraft.api.tile;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/***
 * Sends basic tile load configuration data
 * e.g. facing  (if not obtainable from blockState)
 * to the client.
 * 
 * IMPORTANT: call this.clientValidate() from the tile's validate() method
 */
public interface ITileClientUpdates
{
    /**
     * The supplied data array of ints
     * ALWAYS has length 4.  You don't
     * have to use all of them!
     */
    void buildDataPacket(int[] data);
    
    /**
     * The supplied data list has 4 ints
     * of data to use at positions 1 through 4.
     */
    @SideOnly(Side.CLIENT)
    void updateClient(List<Object> data);
    
    /**
     * Implement validate() in the tile and call this!
     */
    default void clientOnLoad()
    {
        TileEntity tile = (TileEntity)this;
        if (tile.getWorld().isRemote)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_REQUEST_MACHINE_DATA, tile.getWorld(), new Object[] { tile.getPos() }));
        }
    }
    
    /**
     * Do not override unless you want to use a custom data packet
     * with more than 4 ints of data.
     * (If overriding this you must override all other methods in
     * ITileClientUpdates as well ... in which case, why are you using it?)
     */
    default void sendUpdateToClient(EntityPlayerMP player)
    {
        int[] data = new int[4];
        this.buildDataPacket(data);
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_MACHINE_DATA, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { ((TileEntity)this).getPos(), data[0], data[1], data[2], data[3] }), player);
    }
    
    /**
     * Used to push updates out to clients
     */
    default void updateAllInDimension()
    {
        int[] data = new int[4];
        this.buildDataPacket(data);
        int dimID = GCCoreUtil.getDimensionID(((TileEntity)this).getWorld());
        GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_MACHINE_DATA, dimID, new Object[] { ((TileEntity)this).getPos(), data[0], data[1], data[2], data[3] }), dimID);
    }
}
