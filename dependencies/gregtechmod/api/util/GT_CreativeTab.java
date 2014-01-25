package gregtechmod.api.util;

import gregtechmod.api.enums.GT_Items;
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
        return GT_Items.Tool_Cheat.getUndamaged(1, new ItemStack(Block.blockIron, 1));
    }
}