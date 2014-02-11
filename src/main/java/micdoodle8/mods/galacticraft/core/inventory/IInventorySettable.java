package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.inventory.IInventory;

/**
 * IInventorySettable.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public interface IInventorySettable extends IInventory
{
	public void setSizeInventory(int size);
}
