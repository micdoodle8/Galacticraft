package codechicken.nei.config;

import codechicken.lib.inventory.InventoryUtils;
import codechicken.lib.vec.Rectangle4i;
import codechicken.nei.ItemPanel;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.client.Minecraft;
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

public class ItemPanelDumper extends DataDumper {
    private static int[] resolutions = new int[] { 16, 32, 48, 64, 128, 256 };

    public ItemPanelDumper(String name) {
        super(name);
    }

    @Override
    public String[] header() {
        return new String[] { "Item Name", "Item ID", "Item meta", "Has NBT", "Display Name" };
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        for (ItemStack stack : ItemPanel.items) {
            list.add(new String[] { Item.itemRegistry.getNameForObject(stack.getItem()).toString(), Integer.toString(Item.getIdFromItem(stack.getItem())), Integer.toString(InventoryUtils.actualDamage(stack)), stack.getTagCompound() == null ? "false" : "true", EnumChatFormatting.getTextWithoutFormattingCodes(GuiContainerManager.itemDisplayNameShort(stack)) });
        }

        return list;
    }

    @Override
    public String renderName() {
        return translateN(name);
    }

    public int getRes() {
        int i = renderTag(name + ".res").getIntValue(0);
        if (i >= resolutions.length || i < 0) {
            renderTag().setIntValue(i = 0);
        }
        return resolutions[i];
    }

    public Rectangle4i resButtonSize() {
        int width = 50;
        return new Rectangle4i(modeButtonSize().x - width - 6, 0, width, 20);
    }

    @Override
    public void draw(int mousex, int mousey, float frame) {
        super.draw(mousex, mousey, frame);
        if (getMode() == 3) {
            int res = getRes();
            drawButton(mousex, mousey, resButtonSize(), res + "x" + res);
        }
    }

    @Override
    public void mouseClicked(int mousex, int mousey, int button) {
        if (getMode() == 3 && resButtonSize().contains(mousex, mousey)) {
            NEIClientUtils.playClickSound();
            getTag(name + ".res").setIntValue((renderTag(name + ".res").getIntValue(0) + 1) % resolutions.length);
        } else {
            super.mouseClicked(mousex, mousey, button);
        }
    }

    @Override
    public String getFileExtension() {
        switch (getMode()) {
        case 0:
            return ".csv";
        case 1:
            return ".nbt";
        case 2:
            return ".json";
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
    public void dumpFile() {
        if (getMode() == 3) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiItemIconDumper(this, getRes()));
        } else {
            super.dumpFile();
        }
    }

    @Override
    public void dumpTo(File file) throws IOException {
        if (getMode() == 0) {
            super.dumpTo(file);
        } else if (getMode() == 1) {
            dumpNBT(file);
        } else {
            dumpJson(file);
        }
    }

    public void dumpNBT(File file) throws IOException {
        NBTTagList list = new NBTTagList();
        for (ItemStack stack : ItemPanel.items) {
            list.appendTag(stack.writeToNBT(new NBTTagCompound()));
        }

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
        return 4;
    }
}
