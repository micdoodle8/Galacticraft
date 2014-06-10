package micdoodle8.mods.galacticraft.planets.asteroids.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * ItemBlockShortRangeTelepad.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ItemBlockShortRangeTelepad extends ItemBlockGC
{
	public ItemBlockShortRangeTelepad(Block block)
	{
		super(block);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) 
	{
		par3List.add(EnumColor.DARK_GREY + "*Prototype*");
	}
}
