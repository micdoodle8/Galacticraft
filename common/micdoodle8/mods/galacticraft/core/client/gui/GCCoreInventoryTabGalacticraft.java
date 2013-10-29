package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.item.ItemStack;
import tconstruct.client.tabs.AbstractTab;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GCCoreInventoryTabGalacticraft extends AbstractTab
{
    public GCCoreInventoryTabGalacticraft()
    {
        super(0, 0, 0, new ItemStack(GCCoreItems.oxMask));
    }

    @Override
    public void onTabClicked()
    {
        PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.OPEN_EXTENDED_INVENTORY, new Object[] {}));
    }

    @Override
    public boolean shouldAddToList()
    {
        return true;
    }
}
