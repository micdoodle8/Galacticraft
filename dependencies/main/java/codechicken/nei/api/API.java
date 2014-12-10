package codechicken.nei.api;

import codechicken.nei.*;
import codechicken.nei.KeyManager.KeyState;
import codechicken.nei.SearchField.ISearchProvider;
import codechicken.nei.SubsetWidget.SubsetTag;
import codechicken.nei.api.ItemFilter.ItemFilterProvider;
import codechicken.nei.config.Option;
import codechicken.nei.config.OptionKeyBind;
import codechicken.nei.recipe.*;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.Collections;
import java.util.Comparator;

/**
 * This is the main class that handles item property configuration.
 * WARNING: DO NOT access this class until the world has been loaded
 * These methods should be called from INEIConfig implementors
 */
public class API
{
    /**
     * Register a new Crafting Recipe handler;
     *
     * @param handler The handler to register
     */
    public static void registerRecipeHandler(ICraftingHandler handler) {
        GuiCraftingRecipe.registerRecipeHandler(handler);
    }

    /**
     * Register a new Usage Recipe handler;
     *
     * @param handler The handler to register
     */
    public static void registerUsageHandler(IUsageHandler handler) {
        GuiUsageRecipe.registerUsageHandler(handler);
    }

    /**
     * Add a gui to the default overlay renderer with the default position
     *
     * @param classz The class of your gui
     * @param ident  The identification string, currently
     *               {crafting, crafting2x2, smelting, fuel, brewing}
     */
    public static void registerGuiOverlay(Class<? extends GuiContainer> classz, String ident) {
        registerGuiOverlay(classz, ident, 5, 11);
    }

    /**
     * Add a gui to the default overlay renderer with an offset
     *
     * @param classz The class of your gui
     * @param ident  The identification string, currently
     *               {crafting, crafting2x2, smelting, fuel, brewing}
     * @param x      x-offset
     * @param y      y-offset
     */
    public static void registerGuiOverlay(Class<? extends GuiContainer> classz, String ident, int x, int y) {
        registerGuiOverlay(classz, ident, new OffsetPositioner(x, y));
    }

    /**
     * Add a gui to the default overlay renderer
     *
     * @param classz     The class of your gui
     * @param ident      The identification string, currently
     *                   {crafting, crafting2x2, smelting, fuel, brewing}
     * @param positioner A Stack Repositioner for moving the items to the right place
     */
    public static void registerGuiOverlay(Class<? extends GuiContainer> classz, String ident, IStackPositioner positioner) {
        RecipeInfo.registerGuiOverlay(classz, ident, positioner);
    }

    /**
     * @param classz  The class of your gui
     * @param handler The handler to register
     * @param ident   The recipe identification string
     */
    public static void registerGuiOverlayHandler(Class<? extends GuiContainer> classz, IOverlayHandler handler, String ident) {
        RecipeInfo.registerOverlayHandler(classz, handler, ident);
    }

    /**
     * Set the offset to be added to items to translate them into recipe coords on the actual gui, default is 5, 11. Primarily RecipeTransferRects
     *
     * @param classz The class of your gui
     * @param x
     * @param y
     */
    public static void setGuiOffset(Class<? extends GuiContainer> classz, int x, int y) {
        RecipeInfo.setGuiOffset(classz, x, y);
    }

    public static void registerNEIGuiHandler(INEIGuiHandler handler) {
        GuiInfo.guiHandlers.add(handler);
    }

    /**
     * Hide an item from the item panel
     * Damage values of OreDictionary.WILDCARD_VALUE and null NBT tags function as wildcards for their respective variables
     */
    public static void hideItem(ItemStack item) {
        ItemInfo.hiddenItems.add(item);
    }

    /**
     * Add or replace the name normally shown on the item tooltip
     */
    public static void setOverrideName(ItemStack item, String name) {
        ItemInfo.nameOverrides.put(item, name);
    }

    /**
     * Adds an item to the item panel. Any items added using this function will override the default search pattern.
     * @param item an item with data
     */
    public static void addItemListEntry(ItemStack item) {
        ItemInfo.itemOverrides.put(item.getItem(), item);
    }

    /**
     * Sets the item variants to appear in the item panel, overriding the default search pattern for a given item
     */
    public static void setItemListEntries(Item item, Iterable<ItemStack> items) {
        if(items == null)
            items = Collections.emptyList();
        ItemInfo.itemOverrides.replaceValues(item, items);
    }

    /**
     * Add a custom KeyBinding to be configured in the Controls menu.
     * @param ident      An identifier for your key, eg "shoot"
     * @param defaultKey The default value, commonly obtained from {@link Keyboard}
     */
    public static void addKeyBind(String ident, int defaultKey) {
        NEIClientConfig.setDefaultKeyBinding(ident, defaultKey);
        KeyManager.keyStates.put(ident, new KeyState());
        addOption(new OptionKeyBind(ident));
    }

    public static void addOption(Option option) {
        NEIClientConfig.getOptionList().addOption(option);
    }

    /**
     * Add a new Layout Style for the NEI interface
     * @param styleID The Unique ID to be used for storing your style in the config and cycling through avaliable styles
     * @param style   The style to add.
     */
    public static void addLayoutStyle(int styleID, LayoutStyle style) {
        LayoutManager.layoutStyles.put(styleID, style);
    }

    /**
     * Registers a new Infinite Item Handler.
     * @param handler The handler to be registered.
     */
    public static void addInfiniteItemHandler(IInfiniteItemHandler handler) {
        ItemInfo.infiniteHandlers.addFirst(handler);
    }

    /**
     * Registers a new Infinite Item Handler.
     * @param block   The block to handle, null for all.
     * @param handler The handler to be registered.
     */
    public static void registerHighlightIdentifier(Block block, IHighlightHandler handler) {
        ItemInfo.highlightIdentifiers.put(block, handler);
    }

    /**
     * Tells NEI not to perform any Fast Transfer operations on slots of a particular class
     * @param slotClass The class of slot to be exempted
     */
    public static void addFastTransferExemptSlot(Class<? extends Slot> slotClass) {
        ItemInfo.fastTransferExemptions.add(slotClass);
    }

    /**
     * Register a new text handler for the block highlight tooltip with a layout specification (HEADER, BODY or FOOTER).
     * @param handler The handler to be registered.
     * @param layout  A HUDAugmenterRegistry.Layout entry. HEADER is displayed before BODY which is displayed before FOOTER.
     */
    public static void registerHighlightHandler(IHighlightHandler handler, ItemInfo.Layout... layout) {
        ItemInfo.registerHighlightHandler(handler, layout);
    }

    /**
     * Register a mode handler for overriding NEI recipe/utility/cheat mode settings.
     * @param handler The handler to be registered.
     */
    public static void registerModeHandler(INEIModeHandler handler) {
        NEIInfo.modeHandlers.add(handler);
    }

    /**
     * Register a filter provider for the item panel.
     * @param filterProvider The filter provider to be registered.
     */
    public static void addItemFilter(ItemFilterProvider filterProvider) {
        synchronized (ItemList.itemFilterers) {
            ItemList.itemFilterers.add(filterProvider);
        }
    }

    /**
     * Adds a new tag to the item subset dropdown.
     * @param name The fully qualified name, Eg Blocks.MobSpawners. NOT case sensitive
     * @param filter A filter for matching items that fit in this subset
     */
    public static void addSubset(String name, ItemFilter filter) {
        addSubset(new SubsetTag(name, filter));
    }

    /**
     * Adds a new tag to the item subset dropdown.
     * @param name The fully qualified name, Eg Blocks.MobSpawners. NOT case sensitive
     * @param items An iterable of itemstacks to be added as a subset
     */
    public static void addSubset(String name, Iterable<ItemStack> items) {
        ItemStackSet filter = new ItemStackSet();
        for(ItemStack item : items)
            filter.add(item);
        addSubset(new SubsetTag(name, filter));
    }

    /**
     * Adds a new tag to the item subset dropdown.
     */
    public static void addSubset(SubsetTag tag) {
        SubsetWidget.addTag(tag);
    }

    /**
     * Adds a new search provider to the search field
     */
    public static void addSearchProvider(ISearchProvider provider) {
        SearchField.searchProviders.add(provider);
    }

    /**
     * Adds a new sorting option to the item panel sort menu
     * @param name A unique id for this sort option. Will be used in the config for saving and translated in the options gui. Note that if the translation key name.tip exists, it will be used for a tooltip
     */
    public static void addSortOption(String name, Comparator<ItemStack> comparator) {
        ItemSorter.add(name, comparator);
    }
}
