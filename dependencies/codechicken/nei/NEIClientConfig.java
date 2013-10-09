package codechicken.nei;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import codechicken.core.CCUpdateChecker;
import codechicken.core.ClassDiscoverer;
import codechicken.core.ClientUtils;
import codechicken.core.CommonUtils;
import codechicken.core.IStringMatcher;
import codechicken.lib.config.ConfigFile;
import codechicken.lib.config.ConfigTag;
import codechicken.lib.config.ConfigTagParent;
import codechicken.core.inventory.ItemKey;
import codechicken.nei.api.API;
import codechicken.nei.api.GuiInfo;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.api.LayoutStyle;
import codechicken.nei.api.NEIInfo;
import codechicken.nei.api.TaggedInventoryArea;
import codechicken.nei.asm.NEIModContainer;
import codechicken.nei.config.ConfigSet;
import codechicken.nei.config.IConfigSetHolder;
import codechicken.nei.config.OptionCycled;
import codechicken.nei.config.OptionGamemodes;
import codechicken.nei.config.OptionHighlightTips;
import codechicken.nei.config.OptionList;
import codechicken.nei.config.OptionTextField;
import codechicken.nei.config.OptionToggleButton;
import codechicken.nei.config.OptionUtilities;
import codechicken.nei.recipe.RecipeInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class NEIClientConfig
{    
    private static boolean configLoaded;
    private static boolean enabledOverride;

    public static ConfigSet global = new ConfigSet(
            new File(Minecraft.getMinecraft().mcDataDir, "saves/NEI.dat"),
            new ConfigFile(new File(Minecraft.getMinecraft().mcDataDir, "config/NEI.cfg")));
    public static ConfigSet world;
    
    public static ItemVisibilityHash vishash;

    public static ItemStack creativeInv[];
    private static boolean statesSaved[] = new boolean[7];
    
    public static boolean hasSMPCounterpart;
    public static HashSet<String> permissableActions = new HashSet<String>();
    public static HashSet<String> disabledActions = new HashSet<String>();
    public static HashSet<String> enabledActions = new HashSet<String>();
    
    public static HashSet<ItemKey> bannedBlocks = new HashSet<ItemKey>();
    
    static
    {
        if(global.config.getTag("checkUpdates").getBooleanValue(true))
            CCUpdateChecker.updateCheck("NotEnoughItems");
        linkOptionList();
        setDefaults();
    }
    
    private static void setDefaults()
    {
        ConfigTagParent tag = global.config;
        tag.setComment("Main configuration of NEI.\nMost of these options can be changed ingame.\nDeleting any element will restore it to it's default value");
        
        tag.getTag("command").useBraces().setComment("Change these options if you have a different mod installed on the server that handles the commands differently, Eg. Bukkit Essentials");
        tag.setNewLineMode(1);
        
        tag.getTag("inventory.widgetsenabled").getBooleanValue(true);
        API.addOption(new OptionToggleButton("inventory.widgetsenabled"));
        
        tag.getTag("inventory.hidden").getBooleanValue(false);
        tag.getTag("inventory.cheatmode").getIntValue(2);
        tag.getTag("inventory.lockmode").setComment("For those who can't help themselves.\nSet this to a mode and you will be unable to change it ingame").getIntValue(-1);
        API.addOption(new OptionCycled("inventory.cheatmode", 3)
        {
            @Override
            public boolean optionValid(int index)
            {
                return getLockedMode() == -1 || getLockedMode() == index && NEIInfo.isValidMode(index);
            }
        });
        checkCheatMode();
        
        tag.getTag("inventory.utilities").setDefaultValue("delete, magnet");
        API.addOption(new OptionUtilities("inventory.utilities"));
        
        tag.getTag("inventory.gamemodes").setDefaultValue("creative, creative+, adventure");
        API.addOption(new OptionGamemodes("inventory.gamemodes"));
        
        tag.getTag("inventory.layoutstyle").getIntValue(0);
        API.addOption(new OptionCycled("inventory.layoutstyle", 0)
        {
            @Override
            public String getPrefix()
            {
                return translateN(name);
            }
            
            @Override
            public String getButtonText()
            {
                return NEIClientUtils.translate("layoutstyle."+
                        LayoutManager.getLayoutStyle(renderTag().getIntValue()).getName());
            }
            
            @Override
            public boolean cycle()
            {
                LinkedList<Integer> list = new LinkedList<Integer>();
                for(Entry<Integer, LayoutStyle> entry : LayoutManager.layoutStyles.entrySet())
                    list.add(entry.getKey());
                
                Collections.sort(list);
                
                int currentLayout = getTag().getIntValue();
                int nextLayout = currentLayout;
                
                if(nextLayout == list.getLast())//loop list
                    nextLayout = -1;
                for(Integer i : list)
                {
                    if(i > nextLayout)
                    {
                        nextLayout = i;
                        break;
                    }
                }
                
                getTag().setIntValue(nextLayout);
                return true;
            }
        });
        tag.getTag("options.edge-align buttons").setPosition(7).getBooleanValue(false);//TODO: kindof legacy... probs remove

        tag.getTag("inventory.itemIDs").getIntValue(1);
        API.addOption(new OptionCycled("inventory.itemIDs", 3, true));

        tag.getTag("world.highlight_tips").getBooleanValue(false);
        tag.getTag("world.highlight_tips.x").getIntValue(5000);
        tag.getTag("world.highlight_tips.y").getIntValue(100);
        API.addOption(new OptionHighlightTips("world.highlight_tips"));
        
        tag.getTag("inventory.profileRecipes").getBooleanValue(false);
        API.addOption(new OptionToggleButton("inventory.profileRecipes", true));
        
        tag.getTag("command.creative").setDefaultValue("/gamemode {0} {1}");
        API.addOption(new OptionTextField("command.creative"));
        tag.getTag("command.item").setDefaultValue("/give {0} {1} {2} {3}");
        API.addOption(new OptionTextField("command.item"));
        tag.getTag("command.time").setDefaultValue("/time set {0}");
        API.addOption(new OptionTextField("command.time"));
        tag.getTag("command.rain").setDefaultValue("/toggledownfall");
        API.addOption(new OptionTextField("command.rain"));
        tag.getTag("command.heal").setDefaultValue("");
        API.addOption(new OptionTextField("command.heal"));
        
        setDefaultKeyBindings();
    }
    
    private static void linkOptionList()
    {
        getOptionList().bindConfig(new IConfigSetHolder()
        {
            @Override
            public ConfigSet worldConfigSet()
            {
                return world;
            }
            
            @Override
            public ConfigSet globalConfigSet()
            {
                return global;
            }
        });
    }

    private static void setDefaultKeyBindings()
    {
        API.addKeyBind("gui.recipe", Keyboard.KEY_R);
        API.addKeyBind("gui.usage", Keyboard.KEY_U);
        API.addKeyBind("gui.back", Keyboard.KEY_BACK);
        API.addKeyBind("gui.enchant", Keyboard.KEY_X);
        API.addKeyBind("gui.potion", Keyboard.KEY_P);
        API.addKeyBind("gui.prev", Keyboard.KEY_PRIOR);
        API.addKeyBind("gui.next", Keyboard.KEY_NEXT);
        API.addKeyBind("gui.hide", Keyboard.KEY_O);
        API.addKeyBind("gui.search", Keyboard.KEY_F);
        API.addKeyBind("world.chunkoverlay", Keyboard.KEY_F9);
        API.addKeyBind("world.moboverlay", Keyboard.KEY_F7);
        API.addKeyBind("world.highlight_tips", Keyboard.KEY_NUMPAD0);
        API.addKeyBind("world.dawn", 0);
        API.addKeyBind("world.noon", 0);
        API.addKeyBind("world.dusk", 0);
        API.addKeyBind("world.midnight", 0);
        API.addKeyBind("world.rain", 0);
        API.addKeyBind("world.heal", 0);
        API.addKeyBind("world.creative", 0);
    }

    public static OptionList getOptionList()
    {
        return OptionList.getOptionList("nei.options");
    }
    
    public static void loadWorld(String saveName)
    {
        setInternalEnabled(true);
        System.out.println("Loading World: "+saveName);
        bootNEI(ClientUtils.getWorld());
        
        File saveDir = new File(CommonUtils.getMinecraftDir(), "saves/NEI/"+saveName);
        if(!saveDir.exists())
            saveDir.mkdirs();
        
        world = new ConfigSet(new File(saveDir, "NEI.dat"), new ConfigFile(new File(saveDir, "NEI.cfg")));
        
        ConfigTagParent tag = world.config;
        tag.setComment("World based configuration of NEI.\nMost of these options can be changed ingame.\nDeleting any element will restore it to it's default value");
        
        creativeInv = new ItemStack[54];
        setWorldDefaults();
        
        LayoutManager.searchField.setText(getSearchExpression());
        LayoutManager.quantity.setText(Integer.toString(getItemQuantity()));
        
        onWorldLoad();
    }
    
    private static void onWorldLoad() {
        NEIInfo.load(ClientUtils.getWorld());
    }

    private static void setWorldDefaults()
    {
        NBTTagCompound nbt = world.nbt;
        if(!nbt.hasKey("search")) nbt.setString("search", "");
        if(!nbt.hasKey("quantity")) nbt.setInteger("quantity", 0);
        if(!nbt.hasKey("validateenchantments")) nbt.setBoolean("validateenchantments", false);
        
        world.saveNBT();
    }

    public static int getKeyBinding(String string)
    {
        return getSetting("keys."+string).getIntValue();
    }

    public static void setDefaultKeyBinding(String string, int key)
    {
        getSetting("keys."+string).getIntValue(key);
    }

    public static void bootNEI(World world)
    {
        if(configLoaded)
            return;

        loadStates();
        ItemVisibilityHash.loadStates();  
        vishash = new ItemVisibilityHash();
        ItemInfo.load(world);
        GuiInfo.load();
        RecipeInfo.load();
        LayoutManager.load();
        NEIController.load();
        
        configLoaded = true;
        
        ClassDiscoverer classDiscoverer = new ClassDiscoverer(new IStringMatcher()
        {
            public boolean matches(String test)
            {
                return test.startsWith("NEI") && test.endsWith("Config.class");
            }
        }, IConfigureNEI.class);
        
        classDiscoverer.findClasses();
        
        for(Class<?> class1 : classDiscoverer.classes)
        {
            try
            {
                IConfigureNEI config = (IConfigureNEI)class1.newInstance();
                config.loadConfig();
                NEIModContainer.plugins.add(config);
                System.out.println("Loaded "+class1.getName());
            }
            catch(Exception e)
            {
                System.out.println("Failed to Load "+class1.getName());
                e.printStackTrace();
            }
        }
    }

    public static void loadStates()
    {
        for(int state = 0; state < 7; state++)
            statesSaved[state] = !global.nbt.getCompoundTag("save"+state).hasNoTags();
    }
    
    public static boolean isWorldSpecific(String setting)
    {
        return world != null && world.config.containsTag(setting);
    }
    
    public static boolean isStateSaved(int i)
    {
        return statesSaved[i];
    }

    public static ConfigTag getSetting(String s)
    {
        return isWorldSpecific(s) ? world.config.getTag(s) : global.config.getTag(s);
    }
    
    public static boolean getBooleanSetting(String s)
    {
        return getSetting(s).getBooleanValue();
    }

    public static boolean isHidden()
    {
        return !enabledOverride || getBooleanSetting("inventory.hidden");
    }

    public static boolean isEnabled()
    {
        return enabledOverride && getBooleanSetting("inventory.widgetsenabled");
    }
    
    public static void setEnabled(boolean flag)
    {
        getSetting("inventory.widgetsenabled").setBooleanValue(flag);
    }
    
    public static int getItemQuantity()
    {
        return world.nbt.getInteger("quantity");
    }

    public static int getCheatMode()
    {
        return getIntSetting("inventory.cheatmode");
    }

    private static void checkCheatMode()
    {
        if(getLockedMode() != -1)
            setIntSetting("inventory.cheatmode", getLockedMode());
    }
    
    public static int getLockedMode()
    {
        return getIntSetting("inventory.lockmode");
    }

    public static int getLayoutStyle()
    {
        return getIntSetting("inventory.layoutstyle");
    }

    public static String getStringSetting(String s)
    {
        return getSetting(s).getValue();
    }
    
    public static boolean canDump()
    {
        return getBooleanSetting("ID dump.itemIDs") || getBooleanSetting("ID dump.blockIDs") || getBooleanSetting("ID dump.unused itemIDs") || getBooleanSetting("ID dump.unused blockIDs");
    }

    public static boolean showIDs()
    {
        int i = getIntSetting("inventory.itemIDs");
        return i == 2 || (i == 1 && isEnabled() && !isHidden());
    }
    
    public static void toggleBooleanSetting(String setting)
    {
        ConfigTag tag = getSetting(setting);
        tag.setBooleanValue(!tag.getBooleanValue());
    }

    public static void cycleSetting(String setting, int max)
    {
        ConfigTag tag = getSetting(setting);
        tag.setIntValue((tag.getIntValue()+1)%max);
    }
    
    public static int getIntSetting(String setting)
    {
        return getSetting(setting).getIntValue();
    }
    
    public static void setIntSetting(String setting, int val)
    {
        getSetting(setting).setIntValue(val);
    }
    
    public static String getSearchExpression()
    {
        return world.nbt.getString("search");
    }
    
    public static void setSearchExpression(String expression)
    {        
        world.nbt.setString("search", expression);
        world.saveNBT();
    }
    
    public static boolean getMagnetMode()
    {
        return enabledActions.contains("magnet");
    }

    public static boolean invCreativeMode()
    {
        return enabledActions.contains("creative+") && canPerformAction("creative+");
    }
    
    public static boolean areDamageVariantsShown()
    {
        return hasSMPCounterPart() || getSetting("command.item").getValue().contains("{3}");
    }

    public static void clearState(int state)
    {
        statesSaved[state] = false;
        global.nbt.setTag("save"+state, new NBTTagCompound());
        global.saveNBT();
    }
    
    public static void loadState(int state)
    {
        if(!statesSaved[state])
        {
            return;
        }
        
        NBTTagCompound statesave = global.nbt.getCompoundTag("save"+state);
        GuiContainer currentContainer = NEIClientUtils.getGuiContainer();
        LinkedList<TaggedInventoryArea> saveAreas = new LinkedList<TaggedInventoryArea>();
        saveAreas.add(new TaggedInventoryArea(NEIClientUtils.mc().thePlayer.inventory));
        
        for(INEIGuiHandler handler : GuiInfo.guiHandlers)
        {
            List<TaggedInventoryArea> areaList = handler.getInventoryAreas(currentContainer);
            if(areaList != null)
                saveAreas.addAll(areaList);
        }
        
        for(TaggedInventoryArea area : saveAreas)
        {
            if(!statesave.hasKey(area.tag))
                continue;
            
            for(int slot : area.slots)
            {
                NEIClientUtils.setSlotContents(slot, null, area.isContainer());
            }
            
            NBTTagList areaTag = statesave.getTagList(area.tag);
            for(int i = 0; i < areaTag.tagCount(); i++)
            {
                NBTTagCompound stacksave = (NBTTagCompound) areaTag.tagAt(i);
                int slot = stacksave.getByte("Slot")&0xFF;
                if(!area.slots.contains(slot))
                    continue;
                                
                NEIClientUtils.setSlotContents(slot, ItemStack.loadItemStackFromNBT(stacksave), area.isContainer());
            }
        }
    }

    public static void saveState(int state)
    {        
        NBTTagCompound statesave = global.nbt.getCompoundTag("save"+state);
        GuiContainer currentContainer = NEIClientUtils.getGuiContainer();
        LinkedList<TaggedInventoryArea> saveAreas = new LinkedList<TaggedInventoryArea>();
        saveAreas.add(new TaggedInventoryArea(NEIClientUtils.mc().thePlayer.inventory));
        
        for(INEIGuiHandler handler : GuiInfo.guiHandlers)
        {
            List<TaggedInventoryArea> areaList = handler.getInventoryAreas(currentContainer);
            if(areaList != null)
                saveAreas.addAll(areaList);
        }
        
        for(TaggedInventoryArea area : saveAreas)
        {
            NBTTagList areaTag = new NBTTagList(area.tag);
            
            for(int i : area.slots)
            {
                ItemStack stack = area.getStackInSlot(i);
                if(stack == null)
                    continue;
                NBTTagCompound stacksave = new NBTTagCompound();
                stacksave.setByte("Slot", (byte)i);
                stack.writeToNBT(stacksave);
                areaTag.appendTag(stacksave);
            }
            statesave.setTag(area.tag, areaTag);
        }
        
        global.nbt.setTag("save"+state, statesave);
        global.saveNBT();
        statesSaved[state] = true;
    }
    
    public static boolean hasSMPCounterPart()
    {
        return hasSMPCounterpart;
    }
    
    public static void setHasSMPCounterPart(boolean flag)
    {
        hasSMPCounterpart = flag;
        permissableActions.clear();
        bannedBlocks.clear();
        disabledActions.clear();
        enabledActions.clear();
    }
    
    public static boolean canPerformAction(String name)
    {
        if(!isEnabled())
            return false;
        
        if(!modePermitsAction(name))
            return false;
        
        String base = NEIActions.base(name);
        if(hasSMPCounterpart)
            return permissableActions.contains(base);
        
        if(NEIActions.smpRequired(name))
            return false;
        
        String cmd = getStringSetting("command."+base);
        if(cmd == null || !cmd.startsWith("/"))
            return false;
        
        return true;
    }

    private static boolean modePermitsAction(String name)
    {
        if(getCheatMode() == 0) return false;
        if(getCheatMode() == 2) return true;
        
        String[] actions = getStringArrSetting("inventory.utilities");
        for(String action : actions)
            if(action.equalsIgnoreCase(name))
                return true;
        
        return false;
    }

    public static String[] getStringArrSetting(String s)
    {
        return getStringSetting(s).replace(" ", "").split(",");
    }

    public static void setBannedBlocks(ArrayList<ItemKey> ahash)
    {
        bannedBlocks.clear();
        for(ItemKey hash : ahash)
            bannedBlocks.add(hash);
    }
    
    public static boolean canGetItem(ItemKey item)
    {
        return !bannedBlocks.contains(item);
    }
    
    public static void setInternalEnabled(boolean b) 
    {
        enabledOverride = b;
    }
}
