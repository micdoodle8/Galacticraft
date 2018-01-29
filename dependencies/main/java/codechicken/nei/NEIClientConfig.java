package codechicken.nei;

import codechicken.core.*;
import codechicken.lib.asm.discovery.ClassDiscoverer;
import codechicken.lib.asm.discovery.IStringMatcher;
import codechicken.lib.config.ConfigFile;
import codechicken.lib.config.ConfigTag;
import codechicken.lib.config.ConfigTagParent;
import codechicken.lib.util.ClientUtils;
import codechicken.lib.util.CommonUtils;
import codechicken.nei.api.*;
import codechicken.nei.api.layout.LayoutStyle;
import codechicken.nei.config.*;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.util.LogHelper;
import codechicken.nei.util.NEIClientUtils;
import codechicken.obfuscator.ObfuscationRun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSummary;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class NEIClientConfig {
    private static boolean configLoaded;
    private static boolean enabledOverride;

    //public static Logger logger = LogManager.getLogger("NotEnoughItems");
    public static File configDir = new File(CommonUtils.getMinecraftDir(), "config/NEI/");
    public static ConfigSet global = new ConfigSet(new File("saves/NEI/client.dat"), new ConfigFile(new File(configDir, "client.cfg")));
    public static ConfigSet world;

    public static ItemStack creativeInv[];
    private static boolean statesSaved[] = new boolean[7];

    public static boolean hasSMPCounterpart;
    public static HashSet<String> permissableActions = new HashSet<String>();
    public static HashSet<String> disabledActions = new HashSet<String>();
    public static HashSet<String> enabledActions = new HashSet<String>();

    public static ItemStackSet bannedBlocks = new ItemStackSet();

    static {
        if (global.config.getTag("checkUpdates").getBooleanValue(true)) {
            CCUpdateChecker.updateCheck("NotEnoughItems");
        }
        linkOptionList();
        setDefaults();
    }

    private static void setDefaults() {
        ConfigTagParent tag = global.config;
        tag.setComment("Main configuration of NEI.\nMost of these options can be changed ingame.\nDeleting any element will restore it to it's default value");

        tag.getTag("command").useBraces().setComment("Change these options if you have a different mod installed on the server that handles the commands differently, Eg. Bukkit Essentials");
        tag.setNewLineMode(1);

        tag.getTag("inventory.widgetsenabled").getBooleanValue(true);
        API.addOption(new OptionToggleButton("inventory.widgetsenabled"));

        tag.getTag("inventory.hidden").getBooleanValue(false);
        tag.getTag("inventory.cheatmode").getIntValue(2);
        tag.getTag("inventory.lockmode").setComment("For those who can't help themselves.\nSet this to a mode and you will be unable to change it ingame").getIntValue(-1);
        API.addOption(new OptionCycled("inventory.cheatmode", 3) {
            @Override
            public boolean optionValid(int index) {
                return getLockedMode() == -1 || getLockedMode() == index && NEIInfo.isValidMode(index);
            }
        });
        checkCheatMode();

        tag.getTag("inventory.utilities").setDefaultValue("delete, magnet");
        API.addOption(new OptionUtilities("inventory.utilities"));

        tag.getTag("inventory.gamemodes").setDefaultValue("creative, creative+, adventure");
        API.addOption(new OptionGamemodes("inventory.gamemodes"));

        tag.getTag("inventory.layoutstyle").getIntValue(0);
        API.addOption(new OptionCycled("inventory.layoutstyle", 0) {
            @Override
            public String getPrefix() {
                return translateN(name);
            }

            @Override
            public String getButtonText() {
                return NEIClientUtils.translate("layoutstyle." + LayoutManager.getLayoutStyle(renderTag().getIntValue()).getName());
            }

            @Override
            public boolean cycle() {
                LinkedList<Integer> list = new LinkedList<Integer>();
                for (Entry<Integer, LayoutStyle> entry : LayoutManager.layoutStyles.entrySet()) {
                    list.add(entry.getKey());
                }

                Collections.sort(list);

                int nextLayout = renderTag().getIntValue();
                if (nextLayout == list.getLast())//loop list
                {
                    nextLayout = -1;
                }
                for (Integer i : list) {
                    if (i > nextLayout) {
                        nextLayout = i;
                        break;
                    }
                }

                getTag().setIntValue(nextLayout);
                return true;
            }
        });

        ItemSorter.initConfig(tag);

        tag.getTag("inventory.itemIDs").getIntValue(1);
        API.addOption(new OptionCycled("inventory.itemIDs", 3, true));

        tag.getTag("inventory.searchmode").getIntValue(1);
        API.addOption(new OptionCycled("inventory.searchmode", 3, true));

        //tag.getTag("world.highlight_tips").getBooleanValue(false);
        //tag.getTag("world.highlight_tips.x").getIntValue(5000);
        //tag.getTag("world.highlight_tips.y").getIntValue(100);
        //API.addOption(new OptionOpenGui("world.highlight_tips", GuiHighlightTips.class));

        tag.getTag("inventory.profileRecipes").getBooleanValue(false);
        API.addOption(new OptionToggleButton("inventory.profileRecipes", true));

        tag.getTag("command.creative").setDefaultValue("/gamemode {0} {1}");
        API.addOption(new OptionTextField("command.creative"));
        tag.getTag("command.item").setDefaultValue("/give {0} {1} {2} {3} {4}");
        API.addOption(new OptionTextField("command.item"));
        tag.getTag("command.time").setDefaultValue("/time set {0}");
        API.addOption(new OptionTextField("command.time"));
        tag.getTag("command.rain").setDefaultValue("/toggledownfall");
        API.addOption(new OptionTextField("command.rain"));
        tag.getTag("command.heal").setDefaultValue("");
        API.addOption(new OptionTextField("command.heal"));

//        JEIIntegrationManager.initConfig(tag);
//
//        API.addOption(new ItemBrowserButton("jei.itemPanel") {
//            @Override
//            protected void setValue(EnumItemBrowser itemBrowser) {
//                JEIIntegrationManager.setItemPanelOwner(itemBrowser);
//            }
//        });
//
//        API.addOption(new ItemBrowserButton("jei.searchBox") {
//            @Override
//            public boolean isEnabled() {
//                return JEIIntegrationManager.itemPannelOwner == EnumItemBrowser.JEI;
//            }
//
//            @Override
//            protected void setValue(EnumItemBrowser itemBrowser) {
//                JEIIntegrationManager.setSearchBoxOwner(itemBrowser);
//            }
//        });
    }

    private static void linkOptionList() {
        OptionList.setOptionList(new OptionList("nei.options") {
            @Override
            public ConfigSet globalConfigSet() {
                return global;
            }

            @Override
            public ConfigSet worldConfigSet() {
                return world;
            }

            @Override
            public OptionList configBase() {
                return this;
            }

            @Override
            public GuiOptionList getGui(GuiScreen parent, OptionList list, boolean world) {
                return new GuiNEIOptionList(parent, list, world);
            }
        });
    }

    public static OptionList getOptionList() {
        return OptionList.getOptionList("nei.options");
    }

    public static void loadWorld(String saveName) {
        setInternalEnabled(true);
        LogHelper.debug("Loading " + (Minecraft.getMinecraft().isSingleplayer() ? "Local" : "Remote") + " World");
        bootNEI(ClientUtils.getWorld());

        File saveDir = new File(CommonUtils.getMinecraftDir(), "saves/NEI/" + saveName);
        boolean newWorld = !saveDir.exists();
        if (newWorld) {
            saveDir.mkdirs();
        }

        world = new ConfigSet(new File(saveDir, "NEI.dat"), new ConfigFile(new File(saveDir, "NEI.cfg")));
        onWorldLoad(newWorld);
    }

    private static void onWorldLoad(boolean newWorld) {
        world.config.setComment("World based configuration of NEI.\nMost of these options can be changed ingame.\nDeleting any element will restore it to it's default value");

        setWorldDefaults();
        creativeInv = new ItemStack[54];
        LayoutManager.searchField.setText(getSearchExpression());
        LayoutManager.quantity.setText(Integer.toString(getItemQuantity()));
        SubsetWidget.loadHidden();

        if (newWorld && Minecraft.getMinecraft().isSingleplayer()) {
            world.config.getTag("inventory.cheatmode").setIntValue(Minecraft.getMinecraft().playerController.isInCreativeMode() ? 2 : 0);
        }

        NEIInfo.load(ClientUtils.getWorld());
    }

    private static void setWorldDefaults() {
        NBTTagCompound nbt = world.nbt;
        if (!nbt.hasKey("search")) {
            nbt.setString("search", "");
        }
        if (!nbt.hasKey("quantity")) {
            nbt.setInteger("quantity", 0);
        }
        if (!nbt.hasKey("validateenchantments")) {
            nbt.setBoolean("validateenchantments", false);
        }

        world.saveNBT();
    }

    public static void bootNEI(World world) {
        if (configLoaded) {
            return;
        }

        loadStates();
        //ItemVisibilityHash.loadStates();
        //vishash = new ItemVisibilityHash();
        ItemInfo.load(world);
        GuiInfo.load();
        RecipeInfo.load();
        LayoutManager.load();
        NEIController.load();

        configLoaded = true;

        ClassDiscoverer classDiscoverer = new ClassDiscoverer(new IStringMatcher() {
            public boolean matches(String test) {
                return test.startsWith("NEI") && test.endsWith("Config.class");
            }
        }, IConfigureNEI.class);

        classDiscoverer.findClasses();

        for (Class<?> clazz : classDiscoverer.classes) {
            try {
                IConfigureNEI config = (IConfigureNEI) clazz.newInstance();
                config.loadConfig();
                NEIModContainer.plugins.add(config);
                LogHelper.debug("Loaded " + clazz.getName());
            } catch (Exception e) {
                LogHelper.errorError("Failed to Load " + clazz.getName(), e);
            }
        }

        ItemSorter.loadConfig();
    }

    public static void loadStates() {
        for (int state = 0; state < 7; state++) {
            statesSaved[state] = !global.nbt.getCompoundTag("save" + state).hasNoTags();
        }
    }

    public static boolean isWorldSpecific(String setting) {
        if (world == null) {
            return false;
        }
        ConfigTag tag = world.config.getTag(setting, false);
        return tag != null && tag.value != null;
    }

    public static boolean isStateSaved(int i) {
        return statesSaved[i];
    }

    public static ConfigTag getSetting(String s) {
        return isWorldSpecific(s) ? world.config.getTag(s) : global.config.getTag(s);
    }

    public static boolean getBooleanSetting(String s) {
        return getSetting(s).getBooleanValue();
    }

    public static boolean isHidden() {
        return !enabledOverride || getBooleanSetting("inventory.hidden");
    }

    public static boolean isEnabled() {
        return enabledOverride && getBooleanSetting("inventory.widgetsenabled");
    }

    public static void setEnabled(boolean flag) {
        getSetting("inventory.widgetsenabled").setBooleanValue(flag);
    }

    public static int getItemQuantity() {
        return world.nbt.getInteger("quantity");
    }

    public static int getCheatMode() {
        return getIntSetting("inventory.cheatmode");
    }

    private static void checkCheatMode() {
        if (getLockedMode() != -1) {
            setIntSetting("inventory.cheatmode", getLockedMode());
        }
    }

    public static int getLockedMode() {
        return getIntSetting("inventory.lockmode");
    }

    public static int getLayoutStyle() {
        return getIntSetting("inventory.layoutstyle");
    }

    public static String getStringSetting(String s) {
        return getSetting(s).getValue();
    }

    public static boolean showIDs() {
        int i = getIntSetting("inventory.itemIDs");
        return i == 2 || (i == 1 && isEnabled() && !isHidden());
    }

    public static void toggleBooleanSetting(String setting) {
        ConfigTag tag = getSetting(setting);
        tag.setBooleanValue(!tag.getBooleanValue());
    }

    public static void cycleSetting(String setting, int max) {
        ConfigTag tag = getSetting(setting);
        tag.setIntValue((tag.getIntValue() + 1) % max);
    }

    public static int getIntSetting(String setting) {
        return getSetting(setting).getIntValue();
    }

    public static void setIntSetting(String setting, int val) {
        getSetting(setting).setIntValue(val);
    }

    public static String getSearchExpression() {
        return world.nbt.getString("search");
    }

    public static void setSearchExpression(String expression) {
//        JEIIntegrationManager.setFilterText(expression);
        world.nbt.setString("search", expression);
        world.saveNBT();
    }

    /**
     * A split off of setSearchExpression that avoids a JEI update.
     *
     * @param expression Search term.
     */
    public static void setSearchExpression(String expression, boolean updateJEI) {
        if (updateJEI) {
            setSearchExpression(expression);
        } else {
            world.nbt.setString("search", expression);
            world.saveNBT();
        }
    }

    public static boolean getMagnetMode() {
        return enabledActions.contains("magnet");
    }

    public static boolean invCreativeMode() {
        return enabledActions.contains("creative+") && canPerformAction("creative+");
    }

    public static boolean areDamageVariantsShown() {
        return hasSMPCounterPart() || getSetting("command.item").getValue().contains("{3}");
    }

    public static void clearState(int state) {
        statesSaved[state] = false;
        global.nbt.setTag("save" + state, new NBTTagCompound());
        global.saveNBT();
    }

    public static void loadState(int state) {
        if (!statesSaved[state]) {
            return;
        }

        NBTTagCompound statesave = global.nbt.getCompoundTag("save" + state);
        GuiContainer currentContainer = NEIClientUtils.getGuiContainer();
        LinkedList<TaggedInventoryArea> saveAreas = new LinkedList<TaggedInventoryArea>();
        saveAreas.add(new TaggedInventoryArea(Minecraft.getMinecraft().thePlayer.inventory));

        for (INEIGuiHandler handler : GuiInfo.guiHandlers) {
            List<TaggedInventoryArea> areaList = handler.getInventoryAreas(currentContainer);
            if (areaList != null) {
                saveAreas.addAll(areaList);
            }
        }

        for (TaggedInventoryArea area : saveAreas) {
            if (!statesave.hasKey(area.tagName)) {
                continue;
            }

            for (int slot : area.slots) {
                NEIClientUtils.setSlotContents(slot, null, area.isContainer());
            }

            NBTTagList areaTag = statesave.getTagList(area.tagName, 10);
            for (int i = 0; i < areaTag.tagCount(); i++) {
                NBTTagCompound stacksave = areaTag.getCompoundTagAt(i);
                int slot = stacksave.getByte("Slot") & 0xFF;
                if (!area.slots.contains(slot)) {
                    continue;
                }

                NEIClientUtils.setSlotContents(slot, ItemStack.loadItemStackFromNBT(stacksave), area.isContainer());
            }
        }
    }

    public static void saveState(int state) {
        NBTTagCompound statesave = global.nbt.getCompoundTag("save" + state);
        GuiContainer currentContainer = NEIClientUtils.getGuiContainer();
        LinkedList<TaggedInventoryArea> saveAreas = new LinkedList<TaggedInventoryArea>();
        saveAreas.add(new TaggedInventoryArea(Minecraft.getMinecraft().thePlayer.inventory));

        for (INEIGuiHandler handler : GuiInfo.guiHandlers) {
            List<TaggedInventoryArea> areaList = handler.getInventoryAreas(currentContainer);
            if (areaList != null) {
                saveAreas.addAll(areaList);
            }
        }

        for (TaggedInventoryArea area : saveAreas) {
            NBTTagList areaTag = new NBTTagList();

            for (int i : area.slots) {
                ItemStack stack = area.getStackInSlot(i);
                if (stack == null) {
                    continue;
                }
                NBTTagCompound stacksave = new NBTTagCompound();
                stacksave.setByte("Slot", (byte) i);
                stack.writeToNBT(stacksave);
                areaTag.appendTag(stacksave);
            }
            statesave.setTag(area.tagName, areaTag);
        }

        global.nbt.setTag("save" + state, statesave);
        global.saveNBT();
        statesSaved[state] = true;
    }

    public static boolean hasSMPCounterPart() {
        return hasSMPCounterpart;
    }

    public static void setHasSMPCounterPart(boolean flag) {
        hasSMPCounterpart = flag;
        permissableActions.clear();
        bannedBlocks.clear();
        disabledActions.clear();
        enabledActions.clear();
    }

    public static boolean canCheatItem(ItemStack stack) {
        return canPerformAction("item") && !bannedBlocks.contains(stack);
    }

    public static boolean canPerformAction(String name) {
        if (!isEnabled()) {
            return false;
        }

        if (!modePermitsAction(name)) {
            return false;
        }

        String base = NEIActions.base(name);
        if (hasSMPCounterpart) {
            return permissableActions.contains(base);
        }

        if (NEIActions.smpRequired(name)) {
            return false;
        }

        String cmd = getStringSetting("command." + base);
        return !(cmd == null || !cmd.startsWith("/"));

    }

    private static boolean modePermitsAction(String name) {
        if (getCheatMode() == 0) {
            return false;
        }
        if (getCheatMode() == 2) {
            return true;
        }

        String[] actions = getStringArrSetting("inventory.utilities");
        for (String action : actions) {
            if (action.equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    public static String[] getStringArrSetting(String s) {
        return getStringSetting(s).replace(" ", "").split(",");
    }

    public static void setInternalEnabled(boolean b) {
        enabledOverride = b;
    }

    public static void reloadSaves() {
        File saveDir = new File(CommonUtils.getMinecraftDir(), "saves/NEI/local");
        if (!saveDir.exists()) {
            return;
        }

        List<WorldSummary> saves;
        try {
            saves = Minecraft.getMinecraft().getSaveLoader().getSaveList();
        } catch (Exception e) {
            LogHelper.errorError("Error loading saves", e);
            return;
        }
        HashSet<String> saveFileNames = new HashSet<String>();
        for (WorldSummary save : saves) {
            saveFileNames.add(save.getFileName());
        }

        for (File file : saveDir.listFiles()) {
            if (file.isDirectory() && !saveFileNames.contains(file.getName())) {
                ObfuscationRun.deleteDir(file, true);
            }
        }
    }
}
