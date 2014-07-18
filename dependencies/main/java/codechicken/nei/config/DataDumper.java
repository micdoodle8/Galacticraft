package codechicken.nei.config;

import codechicken.core.CommonUtils;
import codechicken.lib.vec.Rectangle4i;
import codechicken.nei.LayoutManager;
import codechicken.nei.NEIClientUtils;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static codechicken.lib.gui.GuiDraw.drawString;
import static codechicken.lib.gui.GuiDraw.drawStringC;

public abstract class DataDumper extends Option
{
    public DataDumper(String name) {
        super(name);
    }

    public abstract String[] header();

    public abstract Iterable<String[]> dump(int mode);

    public String renderName() {
        return translateN(name + "s");
    }

    public void dumpFile() {
        try {
            File file = new File(CommonUtils.getMinecraftDir(), "dumps/" + getFileName(name.replaceFirst(".+\\.", "")));
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            if (!file.exists())
                file.createNewFile();

            dumpTo(file);

            NEIClientUtils.printChatMessage(dumpMessage(file));
        } catch (Exception e) {
            System.err.println("Error dumping " + renderName() + " mode: " + getMode());
            e.printStackTrace();
        }
    }

    public String getFileName(String prefix) {
        return prefix + getFileExtension();
    }

    public String getFileExtension() {
        return ".csv";
    }

    public IChatComponent dumpMessage(File file) {
        return new ChatComponentTranslation("nei.options.tools.dump.dumped",
                translateN(name), "dumps/" + file.getName());
    }

    public void dumpTo(File file) throws IOException {
        int mode = getMode();
        PrintWriter w = new PrintWriter(file);
        w.println(concat(header()));
        for (String[] line : dump(mode))
            w.println(concat(line));
        w.close();
    }

    public static String concat(String[] header) {
        StringBuilder sb = new StringBuilder();
        for (String s : header) {
            if (sb.length() > 0)
                sb.append(',');
            if (s == null)
                s = "null";
            if (s.indexOf(',') > 0 || s.indexOf('\"') > 0)
                s = '\"' + s.replace("\"", "\"\"") + '\"';
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public void draw(int mousex, int mousey, float frame) {
        drawPrefix();
        if(modeCount() > 1)
            drawModeButton(mousex, mousey);
        drawDumpButton(mousex, mousey);
    }

    public void drawPrefix() {
        drawString(renderName(), 10, 6, -1);
    }

    public Rectangle4i dumpButtonSize() {
        int width = 80;
        return new Rectangle4i(slot.slotWidth() - width, 0, width, 20);
    }

    public Rectangle4i modeButtonSize() {
        int width = 60;
        return new Rectangle4i(slot.slotWidth() - width - 10 - dumpButtonSize().w, 0, width, 20);
    }

    public String dumpButtonText() {
        return NEIClientUtils.lang.translate("options.tools.dump.dump");
    }

    public String modeButtonText() {
        return NEIClientUtils.lang.translate("options.tools.dump.mode." + getMode());
    }

    public int getMode() {
        return getTag().getIntValue(0);
    }

    public void drawModeButton(int mousex, int mousey) {
        GL11.glColor4f(1, 1, 1, 1);
        Rectangle4i b = modeButtonSize();
        boolean hover = b.contains(mousex, mousey);
        LayoutManager.drawButtonBackground(b.x, b.y, b.w, b.h, true, getButtonTex(hover));
        drawStringC(modeButtonText(), b.x, b.y, b.w, b.h, getTextColour(hover));
    }

    public void drawDumpButton(int mousex, int mousey) {
        GL11.glColor4f(1, 1, 1, 1);
        Rectangle4i b = dumpButtonSize();
        boolean hover = b.contains(mousex, mousey);
        LayoutManager.drawButtonBackground(b.x, b.y, b.w, b.h, true, getButtonTex(hover));
        drawStringC(dumpButtonText(), b.x, b.y, b.w, b.h, getTextColour(hover));
    }

    public int getButtonTex(boolean hover) {
        return hover ? 2 : 1;
    }

    public int getTextColour(boolean hover) {
        return hover ? 0xFFFFFFA0 : 0xFFE0E0E0;
    }

    @Override
    public void mouseClicked(int mousex, int mousey, int button) {
        if (modeCount() > 1 && modeButtonSize().contains(mousex, mousey)) {
            NEIClientUtils.playClickSound();
            getTag().setIntValue((getMode() + 1) % modeCount());
        } else if (dumpButtonSize().contains(mousex, mousey)) {
            NEIClientUtils.playClickSound();
            dumpFile();
        }
    }

    public int modeCount() {
        return 3;
    }
}
