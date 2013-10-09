package codechicken.core.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import codechicken.lib.packet.PacketCustom;

public abstract class ContainerSynchronised extends ContainerExtended
{
    private ArrayList<IContainerSyncVar> syncVars = new ArrayList<IContainerSyncVar>();
    
    public abstract PacketCustom createSyncPacket();
    
    @Override
    public final void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        
        for(int i = 0; i < syncVars.size(); i++)
        {
            IContainerSyncVar var = syncVars.get(i);
            if(var.changed())
            {
                PacketCustom packet = createSyncPacket();
                packet.writeByte(i);
                var.writeChange(packet);
                sendContainerPacket(packet);
                var.reset();
            }
        }
    }
    
    @Override
    public void sendContainerAndContentsToPlayer(Container container, List<ItemStack> list, List<EntityPlayerMP> playerCrafters)
    {
        super.sendContainerAndContentsToPlayer(container, list, playerCrafters);
        for(int i = 0; i < syncVars.size(); i++)
        {
            IContainerSyncVar var = syncVars.get(i);
            PacketCustom packet = createSyncPacket();
            packet.writeByte(i);
            var.writeChange(packet);
            for(EntityPlayerMP player : playerCrafters)
                packet.sendToPlayer(player);
        }
    }
    
    public void addSyncVar(IContainerSyncVar var)
    {
        syncVars.add(var);
    }
    
    @Override
    public final void handleOutputPacket(PacketCustom packet)
    {
        syncVars.get(packet.readUByte()).readChange(packet);
    }
    
    public List<IContainerSyncVar> getSyncedVars()
    {
        return Collections.unmodifiableList(syncVars);
    }
}
