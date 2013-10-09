package gregtechmod.api.util;

import gregtechmod.api.GregTech_API;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GT_CreativeTab extends CreativeTabs {

	public GT_CreativeTab() {
		super("GregTech");
		LanguageRegistry.instance().addStringLocalization("itemGroup.GregTech", "GregTech Intergalactical");
	}
	
	@Override
    public ItemStack getIconItemStack() {
		ItemStack rStack = GregTech_API.getGregTechItem(55, 1, 0);
		if (rStack == null) rStack = new ItemStack(Block.blockIron, 1);
        return rStack;
    }
}