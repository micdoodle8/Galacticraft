package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class InventoryTabGalacticraft extends AbstractTab
{
    public InventoryTabGalacticraft()
    {
        super(0, new ItemStack(GCItems.oxMask));
    }

    @Override
    public void onTabClicked()
    {
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_EXTENDED_INVENTORY, GCCoreUtil.getDimensionType(Minecraft.getInstance().world), new Object[] {}));
        ClientProxyCore.playerClientHandler.onBuild(0, Minecraft.getInstance().player);
    }

    @Override
    public boolean shouldAddToList()
    {
        return true;
    }
}
