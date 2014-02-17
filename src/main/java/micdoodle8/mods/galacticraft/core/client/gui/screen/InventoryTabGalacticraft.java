package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import tconstruct.client.tabs.AbstractTab;

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
		super(0, 0, 0, new ItemStack(Items.bone));
	}

	@Override
	public void onTabClicked()
	{
		GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_EXTENDED_INVENTORY, new Object[] {}));
	}

	@Override
	public boolean shouldAddToList()
	{
		return true;
	}
}
