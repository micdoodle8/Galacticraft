package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.item.ItemStack;
import tconstruct.client.tabs.AbstractTab;

public class InventoryTabGalacticraft extends AbstractTab
{
    public InventoryTabGalacticraft()
    {
        super(0, 0, 0, new ItemStack(GCItems.oxMask));
    }

    @Override
    public void onTabClicked()
    {
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_EXTENDED_INVENTORY, new Object[] { }));
    }

    @Override
    public boolean shouldAddToList()
    {
        return true;
    }
}
