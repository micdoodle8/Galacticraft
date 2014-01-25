package gregtechmod.api.util;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GT_Shaped_NBT_Keeping_Recipe extends ShapedOreRecipe {
	public GT_Shaped_NBT_Keeping_Recipe(ItemStack aResult, Object... aRecipe) {
		super(aResult, aRecipe);
	}
	
	@Override
	public boolean matches(InventoryCrafting aGrid, World aWorld) {
		ItemStack tStack = null;
		for (int i = 0; i < aGrid.getSizeInventory(); i++) {
			if (aGrid.getStackInSlot(i) != null) {
				if (tStack != null) {
					if ((tStack.hasTagCompound() != aGrid.getStackInSlot(i).hasTagCompound()) || (tStack.hasTagCompound() && !tStack.getTagCompound().equals(aGrid.getStackInSlot(i).getTagCompound()))) return false;
				}
				tStack = aGrid.getStackInSlot(i);
			}
		}
		return super.matches(aGrid, aWorld);
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting aGrid) {
		ItemStack rStack = super.getCraftingResult(aGrid);
		if (rStack != null) {
			for (int i = 0; i < aGrid.getSizeInventory(); i++) {
				if (aGrid.getStackInSlot(i) != null && aGrid.getStackInSlot(i).hasTagCompound()) {
					rStack.setTagCompound((NBTTagCompound)aGrid.getStackInSlot(i).getTagCompound().copy());
					break;
				}
			}
		}
		return rStack;
	}
}
