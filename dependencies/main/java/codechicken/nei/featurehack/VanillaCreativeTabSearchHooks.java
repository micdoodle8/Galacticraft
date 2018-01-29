package codechicken.nei.featurehack;

import codechicken.lib.item.filtering.IItemFilter;
import codechicken.lib.thread.RestartableTask;
import com.google.common.base.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiContainerCreative.ContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by covers1624 on 7/20/2016.
 */
public class VanillaCreativeTabSearchHooks {

    private static HashMap<CreativeTabs, ArrayList<ItemStack>> tabCache = new HashMap<CreativeTabs, ArrayList<ItemStack>>();

    public static void updateSearchListThreaded(GuiContainerCreative guiContainerCreative) {
        filterTask.stop();
        filterTask.setContainerCreative((ContainerCreative) guiContainerCreative.inventorySlots);
        filterTask.setSearchBox(guiContainerCreative.searchField);
        filterTask.setCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[guiContainerCreative.getSelectedTabIndex()]);
        filterTask.restart();
    }

    public static final VanillaFilterTask filterTask = new VanillaFilterTask("NEI Vanilla creative tab filtering.");

    public static class VanillaFilterTask extends RestartableTask {
        private ContainerCreative containerCreative;
        private GuiTextField searchBox;
        private CreativeTabs creativeTab;

        public VanillaFilterTask(String name) {
            super(name);
        }

        public void setContainerCreative(ContainerCreative containerCreative) {
            this.containerCreative = containerCreative;
        }

        public void setSearchBox(GuiTextField searchBox) {
            this.searchBox = searchBox;
        }

        public void setCreativeTab(CreativeTabs creativeTab) {
            this.creativeTab = creativeTab;
        }

        public GuiTextField getSearchBox() {
            return searchBox;
        }

        public CreativeTabs getCreativeTab() {
            return creativeTab;
        }

        @Override
        public void execute() {
            ArrayList<ItemStack> filtered = new ArrayList<ItemStack>();
            if (containerCreative == null) {
                stop();
            }
            IItemFilter filter = VanillaFilter.INSTANCE;
            for (ItemStack item : getStacksForTab(getCreativeTab())) {
                if (interrupted()) {
                    return;
                }

                if (filter.matches(item)) {
                    filtered.add(item);
                }
            }

            if (interrupted()) {
                return;
            }
            containerCreative.itemList = filtered;
            containerCreative.scrollTo(0.0F);
        }

    }

    public static class VanillaFilter implements IItemFilter {

        public static VanillaFilter INSTANCE = new VanillaFilter();

        @Override
        public boolean matches(ItemStack item) {
            GuiTextField textField = filterTask.getSearchBox();
            if (Strings.isNullOrEmpty(textField.getText())) {
                return true;
            }//TODO Replace this with logic inside VanillaFilterTask.execute and allow for oreDict search.
            if (textField.getText().toLowerCase().startsWith("@")) {
                String expectedMod = textField.getText().toLowerCase().replace("@", "");
                if (expectedMod.isEmpty()) {
                    return true;
                }
                if (item.getItem().getRegistryName().getResourceDomain().startsWith(expectedMod)) {
                    return true;
                }
            }
            for (String toolTipString : item.getTooltip(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips)) {
                if (TextFormatting.getTextWithoutFormattingCodes(toolTipString).toLowerCase().contains(textField.getText().toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
    }

    private static List<ItemStack> getStacksForTab(CreativeTabs creativeTab) {
        ArrayList<ItemStack> tabStacks = new ArrayList<ItemStack>();
        if (creativeTab == CreativeTabs.SEARCH) {
            for (Item item : Item.REGISTRY) {
                if (item != null) {
                    item.getSubItems(item, null, tabStacks);
                }
            }
            for (Enchantment enchantment : Enchantment.REGISTRY) {
                if (enchantment != null && enchantment.type != null) {
                    Items.ENCHANTED_BOOK.getAll(enchantment, tabStacks);
                }
            }
        }

        creativeTab.displayAllRelevantItems(tabStacks);

        return tabStacks;
    }

}
