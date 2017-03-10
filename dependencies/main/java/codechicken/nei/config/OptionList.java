package codechicken.nei.config;

import codechicken.nei.config.GuiOptionList.OptionScrollSlot;
import codechicken.nei.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.HashMap;

public class OptionList extends OptionButton {
    public static final OptionList root = new RootOptionList();

    private static class RootOptionList extends OptionList {
        public RootOptionList() {
            super(null);
        }

        @Override
        public ConfigSet globalConfigSet() {
            return null;
        }

        @Override
        public ConfigSet worldConfigSet() {
            return null;
        }

        @Override
        public OptionList configBase() {
            return null;
        }

        @Override
        public GuiOptionList getGui(GuiScreen parent, OptionList list, boolean world) {
            return new GuiOptionList(parent, list, world);
        }
    }

    public static OptionList getOptionList(String fullName) {
        Option o = root.getOption(fullName);
        if (o == null) {
            root.addOption(o = new OptionList(fullName));
        }
        return (OptionList) o;
    }

    public static void setOptionList(OptionList list) {
        OptionList prev = (OptionList) root.getOption(list.fullName());
        if (prev == null) {
            root.addOption(list);
        } else {
            list.parent = prev.parent;
            list.options = prev.options;
            list.optionList = prev.optionList;
            for (Option o : list.optionList) {
                o.parent = list;
            }
            list.parent.options.put(list.fullName(), list);
            list.parent.optionList.remove(prev);
            list.parent.addSorted(list);
        }
    }

    public static String parent(String fullName) {
        int i = fullName.indexOf('.');
        if (i < 0) {
            return fullName;
        }
        return fullName.substring(0, i);
    }

    public static String child(String fullName) {
        int i = fullName.indexOf('.');
        return fullName.substring(i + 1);
    }

    public ArrayList<Option> optionList = new ArrayList<Option>();
    public HashMap<String, Option> options = new HashMap<String, Option>();

    public OptionList(String name) {
        super(name);
    }

    private OptionList subList(String fullName) {
        OptionList o = (OptionList) getOption(fullName);
        if (o == null) {
            addOption(o = new OptionList(fullName));
        }

        return o;
    }

    public Option getOption(String fullName) {
        if (fullName.contains(".")) {
            return subList(parent(fullName)).getOption(child(fullName));
        }

        return options.get(fullName);
    }

    public void addOption(Option o) {
        o.namespace = fullName();
        addOption(o, o.fullName(), o.name);
    }

    private void addOption(Option o, String fullName, String subName) {
        if (subName.contains(".")) {
            subList(parent(subName)).addOption(o, fullName, child(subName));
            return;
        }

        if (options.containsKey(subName)) {
            LogHelper.warn("Replacing option: " + fullName);
        }

        options.put(subName, o);
        addSorted(o);
        o.onAdded(this);
    }

    public void addSorted(Option o) {
        optionList.add(o);
    }

    /**
     * Create an instance of a GuiOptionList subclass for the given parameter list
     *
     * @param parent The parent screen for the back button
     * @param list   The option list to be displayed in the gui
     * @param world  true if in world config mode, false for global
     */
    public GuiOptionList getGui(GuiScreen parent, OptionList list, boolean world) {
        return this.parent.getGui(parent, list, world);
    }

    public void openGui(GuiScreen parent, boolean world) {
        Minecraft.getMinecraft().displayGuiScreen(getGui(parent, this, world));
    }

    /**
     * Adds this option to a temporary slot and gui for internal setting manipulation.
     */
    public void synthesizeEnvironment(boolean world) {
        getGui(null, this, world).addWidgets();
    }

    @Override
    public boolean onClick(int button) {
        openGui(slot.getGui(), slot.getGui().worldConfig());
        return true;
    }

    @Override
    public boolean showWorldSelector() {
        return false;
    }

    @Override
    public void onAdded(OptionScrollSlot slot) {
        super.onAdded(slot);

        globalConfigSet().config.getTag(configName()).useBraces();
        worldConfigSet().config.getTag(configName()).useBraces();
    }
}
