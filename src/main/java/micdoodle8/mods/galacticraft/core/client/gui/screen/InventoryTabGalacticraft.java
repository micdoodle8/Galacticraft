package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.item.ItemStack;

/**
 * GCCoreInventoryTabGalacticraft.java
 *
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class InventoryTabGalacticraft extends AbstractTab
{
    public InventoryTabGalacticraft()
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
