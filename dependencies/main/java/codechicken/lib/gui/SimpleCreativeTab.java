package codechicken.lib.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Created by covers1624 on 7/11/2016.
 * Provides a Simple way to create a CreativeTab
 * TODO BlockState parsing.
 */
public class SimpleCreativeTab extends CreativeTabs {

    private ItemStack tabIcon;
    private final String name;
    private final int meta;

    /**
     * Create a SimpleCreativeTab!
     * The tab icon will be "baked" when the icon is requested the first time.
     * If the item cannot be found it will throw a RuntimeException, This can be avoided by setting "-Dccl.ignoreInvalidTabItem=true" in your command line args, this will default the tab to Redstone.
     *
     * @param tabLabel The label to be displayed, This WILL be localized by minecraft.
     * @param tabIcon  The item to find in the Item Registry e.g "minecraft:redstone" or "chickenchunks:chickenChunkLoader"
     */
    public SimpleCreativeTab(String tabLabel, String tabIcon) {
        this(tabLabel, tabIcon, 0);
    }

    /**
     * Create a SimpleCreativeTab!
     * The tab icon will be "baked" when the icon is requested the first time.
     * If the item cannot be found it will throw a RuntimeException, This can be avoided by setting "-Dccl.ignoreInvalidTabItem=true" in your command line args, this will default the tab to Redstone.
     *
     * @param tabLabel The label to be displayed, This WILL be localized by minecraft.
     * @param tabIcon  The item to find in the Item Registry e.g "minecraft:redstone" or "chickenchunks:chickenChunkLoader"
     * @param meta     The metadata of the item.
     */
    public SimpleCreativeTab(String tabLabel, String tabIcon, int meta) {
        super(tabLabel);
        this.name = tabIcon;
        this.meta = meta;
    }

    /**
     * "Bakes" the SimpleCreativeTab when the icon is requested the first time.
     * If the item cannot be found it will throw a RuntimeException.
     * This can be avoided by setting "-Dccl.ignoreInvalidTabItem=true" in your command line args.
     * This will default the tab to Redstone.
     */
    private void bakeTab() {
        ResourceLocation location = new ResourceLocation(name);
        Item item = ForgeRegistries.ITEMS.getValue(location);
        if (item == null) {
            if (Boolean.parseBoolean(System.getProperty("ccl.ignoreInvalidTabItem", "false"))) {
                FMLLog.warning("Unable to find [%s] in the Item Registry. Please ensure the name for the item is correct. Unable to craft SimpleCreativeTab icon.");
                tabIcon = new ItemStack(Items.REDSTONE);
            } else {
                throw new RuntimeException("Unable to find [%s] in the Item Registry. Please ensure the name for the item is correct. Unable to craft SimpleCreativeTab icon!");
            }
        }
        tabIcon = new ItemStack(item, 1, meta);
    }

    @Override
    public Item getTabIconItem() {
        return Items.REDSTONE;
    }

    @Override
    public ItemStack getIconItemStack() {
        if (tabIcon == null) {
            bakeTab();
        }
        return tabIcon;
    }
}
