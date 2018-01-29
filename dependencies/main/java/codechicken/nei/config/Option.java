package codechicken.nei.config;

import codechicken.lib.config.ConfigTag;
import codechicken.nei.config.GuiOptionList.OptionScrollSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public abstract class Option {
    public static void playClickSound() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public OptionScrollSlot slot;

    public String namespace = null;
    public final String name;
    public OptionList parent;

    public Option(String name) {
        this.name = name;
    }

    public String fullName() {
        return namespaced(name);
    }

    public String configName() {
        return fullName().substring(configBase().fullName().length() + 1);
    }

    public String namespaced(String name) {
        return namespace == null ? name : (namespace + "." + name);
    }

    public String translateN(String s, Object... args) {
        return I18n.translateToLocalFormatted(namespaced(s), args);
    }

    public int getHeight() {
        return 20;
    }

    private <T> T assertParentOverride(T elem) {
        if (elem == null) {
            throw new IllegalStateException("Option " + fullName() + " does not have a defined parent list. Use OptionList.setOptionList to insert a parent");
        }
        return elem;
    }

    public ConfigSet globalConfigSet() {
        return assertParentOverride(parent.globalConfigSet());
    }

    public ConfigSet worldConfigSet() {
        return assertParentOverride(parent.worldConfigSet());
    }

    public OptionList configBase() {
        return assertParentOverride(parent.configBase());
    }

    /**
     * @return true if the world config contains a tag with this name
     */
    public boolean worldSpecific() {
        return worldSpecific(configName());
    }

    /**
     * @return true if the world config contains a tag with this name
     */
    public boolean worldSpecific(String s) {
        ConfigTag tag = worldConfigSet().config.getTag(s, false);
        return tag != null && tag.value != null;
    }

    /**
     * @return The currently config set for the gui based on the world/global button
     */
    public ConfigSet configSet() {
        return worldConfig() ? worldConfigSet() : globalConfigSet();
    }

    /**
     * @return The tag to render, world if world override, otherwise global
     */
    public ConfigTag renderTag() {
        return renderTag(configName());
    }

    /**
     * @return The tag to render, world if world override, otherwise global
     */
    public ConfigTag renderTag(String s) {
        return (worldConfig() && worldSpecific(s) ? worldConfigSet() : globalConfigSet()).config.getTag(s);
    }

    /**
     * @return The tag with this name from the current configSet
     */
    public ConfigTag getTag() {
        return getTag(configName());
    }

    /**
     * @return The tag with this name from the current configSet
     */
    public ConfigTag getTag(String s) {
        return configSet().config.getTag(s);
    }

    /**
     * @return The tag currently being used ingame, world if a world override exists, otherwise global
     */
    public ConfigTag activeTag() {
        return activeTag(configName());
    }

    /**
     * @return The tag currently being used ingame, world if a world override exists, otherwise global
     */
    public ConfigTag activeTag(String s) {
        return (worldSpecific() ? worldConfigSet() : globalConfigSet()).config.getTag(s);
    }

    /**
     * @return true if the gui is in world config mode
     */
    public boolean worldConfig() {
        return slot.getGui().worldConfig();
    }

    /**
     * @return true if in world mode, but this tag has no world override. Changes should not be made in this mode
     */
    public boolean defaulting() {
        return defaulting(configName());
    }

    /**
     * @return true if in world mode, but this tag has no world override. Changes should not be made in this mode
     */
    public boolean defaulting(String s) {
        return worldConfig() && !worldSpecific(s);
    }

    /**
     * Called when world specific is disabled for this option. Should delete all world specific overrides
     */
    public void useGlobals() {
        useGlobal(configName());
    }

    /**
     * Deletes a specific named tag from the worldConfig
     */
    public void useGlobal(String s) {
        if (worldConfig()) {
            worldConfigSet().config.removeTag(s);
        }
    }

    /**
     * Called when world specific is activated for this option. Should copy the global value into a world specific override
     */
    public void copyGlobals() {
        copyGlobal(configName(), true);
    }

    /**
     * Copies the global value into a world specific override
     */
    public void copyGlobal(String s) {
        copyGlobal(s, false);
    }

    public void copyGlobal(String s, boolean recursive) {
        if (!worldConfig()) {
            return;
        }

        ConfigTag tag = globalConfigSet().config.getTag(s);
        worldConfigSet().config.getTag(s).setValue(tag.getValue());
        if (recursive) {
            for (String s2 : tag.childTagMap().keySet()) {
                copyGlobal(s + "." + s2);
            }
        }
    }

    public void onAdded(OptionScrollSlot slot) {
        this.slot = slot;
    }

    public void onAdded(OptionList list) {
        parent = list;
    }

    /**
     * Mouse coordinates are relative to screen
     */
    public void onMouseClicked(int mx, int my, int button) {
    }

    public void mouseClicked(int mx, int my, int button) {
    }

    public void update() {
    }

    public void draw(int mx, int my, float frame) {
    }

    public void keyTyped(char c, int keycode) {
    }

    public List<String> handleTooltip(int mx, int my, List<String> currenttip) {
        return currenttip;
    }

    public boolean showWorldSelector() {
        return true;
    }

    public boolean hasWorldOverride() {
        return worldSpecific();
    }
}
