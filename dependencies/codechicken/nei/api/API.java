package codechicken.nei.api;

import java.util.ArrayList;
import java.util.Collection;
import org.lwjgl.input.Keyboard;

import codechicken.core.inventory.ItemKey;
import codechicken.nei.DropDownFile;
import codechicken.nei.KeyManager;
import codechicken.nei.KeyManager.KeyState;
import codechicken.nei.LayoutManager;
import codechicken.nei.MultiItemRange;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.OffsetPositioner;
import codechicken.nei.SubSetRangeTag;
import codechicken.nei.config.Option;
import codechicken.nei.config.OptionKeyBind;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.RecipeInfo;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;

/**
 * This is the main class that handles item property configuration.
 * WARNING: DO NOT access this class until the world has been loaded
 * These methods should be called from INEIConfig implementors
 */
public class API
{
    /**
     * Register a new Crafting Recipe handler;
     * @param handler The handler to register
     */
    public static void registerRecipeHandler(ICraftingHandler handler)
    {
        GuiCraftingRecipe.registerRecipeHandler(handler);
    }
    
    /**
     * Register a new Usage Recipe handler;
     * @param handler The handler to register
     */
    public static void registerUsageHandler(IUsageHandler handler)
    {
        GuiUsageRecipe.registerUsageHandler(handler);
    }
    
    /**
     * Add a gui to the default overlay renderer with the default position
     * @param classz The class of your gui
     * @param ident The identification string, currently 
     * {crafting, crafting2x2, smelting, fuel, brewing}
     */
    public static void registerGuiOverlay(Class<? extends GuiContainer> class1, String string)
    {
        registerGuiOverlay(class1, string, 5, 11);
    }
    
    /**
     * Add a gui to the default overlay renderer with an offset
     * @param classz The class of your gui
     * @param ident The identification string, currently 
     * {crafting, crafting2x2, smelting, fuel, brewing}
     * @param x x-offset
     * @param y y-offset
     */
    public static void registerGuiOverlay(Class<? extends GuiContainer> class1, String string, int x, int y)
    {
        registerGuiOverlay(class1, string, new OffsetPositioner(x, y));
    }
    
    /**
     * Add a gui to the default overlay renderer
     * @param classz The class of your gui
     * @param ident The identification string, currently 
     * {crafting, crafting2x2, smelting, fuel, brewing}
     * @param positioner A Stack Repositioner for moving the items to the right place
     */
    public static void registerGuiOverlay(Class<? extends GuiContainer> classz, String ident, IStackPositioner positioner)
    {
        RecipeInfo.registerGuiOverlay(classz, ident, positioner);
    }
    
    /**
     * 
     * @param classz The class of your gui
     * @param handler The handler to register
     * @param ident The recipe identification string
     */
    public static void registerGuiOverlayHandler(Class<? extends GuiContainer> classz, IOverlayHandler handler, String ident)
    {
        RecipeInfo.registerOverlayHandler(classz, handler, ident);
    }
    
    /**
     * Set the offset to be added to items to translate them into recipe coords on the actual gui, default is 5, 11. Primarily RecipeTransferRects
     * @param classz The class of your gui
     * @param x
     * @param y
     */
    public static void setGuiOffset(Class<? extends GuiContainer> classz, int x, int y)
    {
        RecipeInfo.setGuiOffset(classz, x, y);
    }

    public static void registerNEIGuiHandler(INEIGuiHandler handler)
    {
        GuiInfo.guiHandlers.add(handler);
    }
    
    /**
     * Hide this item from the ItemPanel.
     * @param itemID The ItemID to hide.
     */
    public static void hideItem(int itemID)
    {
        ItemInfo.excludeIds.add(Integer.valueOf(itemID));
    }
    
    /**
     * Collection version of hideItem.
     * @param items A collection of ItemIDs to hide
     */
    public static void hideItems(Collection<Integer> items)
    {
        ItemInfo.excludeIds.addAll(items);
    }
    
    /**
    * Add or replace the name normally shown on the item tooltip
    * @param itemID
    * @param itemDamage
    * @param name The name to set.
    */
    public static void setOverrideName(int itemID, int itemDamage, String name)
    {
        ItemInfo.fallbackNames.put(new ItemKey(itemID, itemDamage), name);
    }
    
   /**
    * An advanced damage range setter, capable of handling multiple ranges. Removes the performance hit from simply searching from 0 - 32000.
    * Sets the item to have damages between the ranges specified by the pairs of ints in the int[]s
    * The int[] should have dimension of 2. The int[0] being the first damage and int[1] the last.
    * Damage ranges are inclusive. 
    * @param itemID The item to set the damage ranges for 
    * @param ranges An ArrayList of int[] pairs specifying the damage ranges.
    */
    public static void setItemDamageVariants(int itemID, ArrayList<int[]> ranges)
    {
        ItemInfo.damageVariants.put(itemID, ranges);
    }
    
   /**
    * A simplified wrapper version for specific damage values. Potions, Spawn Eggs etc.
    * @param itemID
    * @param damages A list of Integers specifying the valid damage values.
    */
    public static void setItemDamageVariants(int itemID, Collection<Integer> damages)
    {
        setItemDamageVariants(itemID, NEIClientUtils.concatIntegersToRanges(new ArrayList<Integer>(damages)));
    }
    
    /**
     * Another simplified wrapper version of setItemDamageVariants. 
     * Simply supports searching from 0-maxDamage
     * Use of this function is not recommended for large damage values as there is a good hit on performance.
     * @param itemID
     * @param maxDamage the maximum damage to search to (inclusive)
     * Setting this to -1 will disable NEI's normal damage value based search.
     */
    public static void setMaxDamageException(int itemID, int maxDamage)
    {
        ArrayList<int[]> damageset = new ArrayList<int[]>();
        damageset.add(new int[]{0, maxDamage});
        setItemDamageVariants(itemID, damageset);
    }
    
    /**
     * Adds an item with a data compound. 
     * Use this for adding items to the panel that are different depending on their compounds not just their damages.
     * @param item an item with data
     */
    public static void addNBTItem(ItemStack item)
    {
        ArrayList<ItemStack> datalist = ItemInfo.itemcompounds.get(item.itemID);
        if(datalist == null)
        {
            datalist = new ArrayList<ItemStack>();
            ItemInfo.itemcompounds.put(item.itemID, datalist);
        }
        datalist.add(item);
    }
    
    /**
     * The all important function for mods wanting to add custom Item Subset tags
     * @param setname The name of the item range Eg. "Items.Tools.Hammers"
     * @param range A {@link MultiItemRange} specifying the items encompassed by this tag.
     */
    public static void addSetRange(String setname, MultiItemRange range)
    {
        SubSetRangeTag tag = DropDownFile.dropDownInstance.getTag(setname);
        tag.saveTag = false;
        tag.setRange(range);
        DropDownFile.dropDownInstance.updateState();
    }
    
    /**
     * Use this to get the internal {@link SubSetRangeTag} for the tagname
     * Advanced plugins can use this to change the order of tags in the dropdown
     */
    public static SubSetRangeTag getRangeTag(String tagname)
    {
        return DropDownFile.dropDownInstance.getTag(tagname);
    }
    
    /**
     * Add more items to an existing Item Subset rather than replacing it
     * @param setname
     * @param range
     */
    public static void addToRange(String setname, MultiItemRange range)
    {
        SubSetRangeTag tag = DropDownFile.dropDownInstance.getTag(setname);
        if(tag.validranges == null)
            tag.setRange(range);
        else
            tag.validranges.add(range);
    }
    
    /**
     * Add a custom KeyBinding to be configured in the Controls menu.
     * @param ident An identifier for your key, eg "shoot"
     * @param defaultKey The default value, commonly obtained from {@link Keyboard}
     */
    public static void addKeyBind(String ident, int defaultKey)
    {
        NEIClientConfig.setDefaultKeyBinding(ident, defaultKey);
        KeyManager.keyStates.put(ident, new KeyState());
        addOption(new OptionKeyBind(ident));
    }
    
    public static void addOption(Option option)
    {
        NEIClientConfig.getOptionList().addOption(option);
    }
    
    /**
     * Add a new Layout Style for the NEI interface
     * @param styleID The Unique ID to be used for storing your style in the config and cycling through avaliable styles
     * @param style The style to add.
     */
    public static void addLayoutStyle(int styleID, LayoutStyle style)
    {
        LayoutManager.layoutStyles.put(styleID, style);
    }
    
    /**
     * Registers a new Infinite Item Handler.
     * @param handler The handler to be registered.
     */
    public static void addInfiniteItemHandler(IInfiniteItemHandler handler)
    {
        ItemInfo.infiniteHandlers.addFirst(handler);
    }
    
    /**
     * Registers a new Infinite Item Handler.
     * @param blockID The blockID to handle, 0 for all.
     * @param handler The handler to be registered.
     */
    public static void registerHighlightIdentifier(int blockID, IHighlightHandler handler)
    {
        ArrayList<IHighlightHandler> handlers = ItemInfo.highlightIdentifiers.get(blockID);
        if(handlers == null)
        {
            handlers = new ArrayList<IHighlightHandler>();
            ItemInfo.highlightIdentifiers.put(blockID, handlers);
        }
        handlers.add(handler);
    }
    
    /**
     * Tells NEI not to perform any Fast Transfer operations on slots of a particular class
     * @param slotClass The class of slot to be exempted
     */
    public static void addFastTransferExemptSlot(Class<? extends Slot> slotClass)
    {
        ItemInfo.fastTransferExemptions.add(slotClass);
    }

    /**
     * Register a new text handler for the block highlight tooltip with a layout specification (HEADER, BODY or FOOTER).
     * @param handler
     * @param layout    A HUDAugmenterRegistry.Layout entry. HEADER is displayed before BODY which is displayed before FOOTER.
     */
    public static void registerHighlightHandler(IHighlightHandler handler, ItemInfo.Layout... layout)
    {
        ItemInfo.registerHighlightHandler(handler, layout);
    }
    
    /**
     * Register a mode handler for overriding NEI recipe/utility/cheat mode settings.
     * @param handler
     */
    public static void registerModeHandler(INEIModeHandler handler)
    {
        NEIInfo.modeHandlers.add(handler);
    }
}
