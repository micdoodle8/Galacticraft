package codechicken.nei.config;

import codechicken.lib.inventory.InventoryUtils;
import codechicken.nei.ItemPanel;
import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class ItemPanelDumper extends DataDumper
{
    public ItemPanelDumper(String name) {
        super(name);
    }

    @Override
    public String[] header() {
        return new String[]{"Item Name", "Item ID", "Item meta", "Has NBT", "Display Name"};
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        for (ItemStack stack : ItemPanel.items)
            list.add(new String[]{
                    Item.itemRegistry.getNameForObject(stack.getItem()),
                    Integer.toString(Item.getIdFromItem(stack.getItem())),
                    Integer.toString(InventoryUtils.actualDamage(stack)),
                    stack.stackTagCompound == null ? "false" : "true",
                    EnumChatFormatting.getTextWithoutFormattingCodes(GuiContainerManager.itemDisplayNameShort(stack))
            });

        return list;
    }

    @Override
    public String renderName() {
        return translateN(name);
    }

    @Override
    public String getFileExtension() {
        switch(getMode()) {
            case 0: return ".csv";
            case 1: return ".nbt";
            case 2: return ".json";
        }
        return null;
    }

    @Override
    public ChatComponentTranslation dumpMessage(File file) {
        return new ChatComponentTranslation(namespaced(name + ".dumped"), "dumps/" + file.getName());
    }

    @Override
    public String modeButtonText() {
        return translateN(name + ".mode." + getMode());
    }

    @Override
    public void dumpTo(File file) throws IOException {
        if (getMode() == 0)
            super.dumpTo(file);
        else if (getMode() == 1)
            dumpNBT(file);
        else
            dumpJson(file);
    }

    public void dumpNBT(File file) throws IOException {
        NBTTagList list = new NBTTagList();
        for (ItemStack stack : ItemPanel.items)
            list.appendTag(stack.writeToNBT(new NBTTagCompound()));

        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("list", list);

        CompressedStreamTools.writeCompressed(tag, new FileOutputStream(file));
    }

    public void dumpJson(File file) throws IOException {
        PrintWriter p = new PrintWriter(file);
        for (ItemStack stack : ItemPanel.items) {
            NBTTagCompound tag = stack.writeToNBT(new NBTTagCompound());
            tag.removeTag("Count");
            p.println(tag);
        }

        p.close();
    }

    @Override
    public int modeCount() {
        return 3;
    }
}
