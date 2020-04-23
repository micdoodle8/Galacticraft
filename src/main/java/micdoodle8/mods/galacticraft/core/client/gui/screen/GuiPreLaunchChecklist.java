package micdoodle8.mods.galacticraft.core.client.gui.screen;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckboxPreLaunch;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

public class GuiPreLaunchChecklist extends GuiScreen implements GuiElementCheckboxPreLaunch.ICheckBoxCallback
{
    private static final ResourceLocation bookGuiTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/checklist_book.png");
    private int bookImageWidth = 192;
    private int bookImageHeight = 192;
    private List<List<String>> checklistKeys;
    private int currPage = 0;
    private int bookTotalPages;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private NBTTagCompound tagCompound;
    private Map<Integer, String> checkboxToKeyMap = Maps.newHashMap();

    public GuiPreLaunchChecklist(List<List<String>> checklistKeys, NBTTagCompound tagCompound)
    {
        this.tagCompound = tagCompound != null ? tagCompound : new NBTTagCompound();
        this.checklistKeys = checklistKeys;
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.checkboxToKeyMap.clear();

        int i = (this.width - this.bookImageWidth) / 2;
        int j = 2;
        this.buttonList.add(this.buttonNextPage = new NextPageButton(0, i + 120, j + 154, true));
        this.buttonList.add(this.buttonPreviousPage = new NextPageButton(1, i + 38, j + 154, false));

        int yPos = 25;
        int index = 2;
        int page = 0;

        for (List<String> e : this.checklistKeys)
        {
            if (e.isEmpty())
            {
                continue;
            }
            String title = e.get(0);
            List<String> checkboxes = e.subList(1, e.size());
            GuiElementCheckboxPreLaunch element = new GuiElementCheckboxPreLaunch(index, this, this.width / 2 - 73 + 11, yPos, GCCoreUtil.translate(title), 0);
            int size = element.willFit(152 - yPos);
            if (size >= 0)
            {
                if (page == this.currPage)
                {
                    this.buttonList.add(element);
                    this.checkboxToKeyMap.put(element.id, title);
                    index++;
                }
                yPos += size + mc.fontRenderer.FONT_HEIGHT / 2;
            }
            else
            {
                page++;
                yPos = 25;
                size = element.willFit(152 - yPos);
                element = new GuiElementCheckboxPreLaunch(index, this, this.width / 2 - 73 + 11, yPos, GCCoreUtil.translate(title), 0);

                if (page == this.currPage)
                {
                    this.buttonList.add(element);
                    this.checkboxToKeyMap.put(element.id, title);
                    index++;
                }
                yPos += size + mc.fontRenderer.FONT_HEIGHT / 2;
            }

            for (String checkbox : checkboxes)
            {
                element = new GuiElementCheckboxPreLaunch(index, this, this.width / 2 - 73 + 16, yPos, GCCoreUtil.translate("checklist." + checkbox + ".key"), 0);
                size = element.willFit(152 - yPos);
                if (size >= 0)
                {
                    if (page == this.currPage)
                    {
                        this.buttonList.add(element);
                        this.checkboxToKeyMap.put(element.id, title + "." + checkbox + ".key");
                        index++;
                    }
                    yPos += size + mc.fontRenderer.FONT_HEIGHT / 2;
                }
                else
                {
                    page++;
                    yPos = 25;
                    size = element.willFit(152 - yPos);
                    element = new GuiElementCheckboxPreLaunch(index, this, this.width / 2 - 73 + 16, yPos, GCCoreUtil.translate("checklist." + checkbox + ".key"), 0);

                    if (page == this.currPage)
                    {
                        this.buttonList.add(element);
                        this.checkboxToKeyMap.put(element.id, title + "." + checkbox + ".key");
                        index++;
                    }
                    yPos += size + mc.fontRenderer.FONT_HEIGHT / 2;
                }
            }
        }

        bookTotalPages = page + 1;
        this.updateButtons();
    }

    private void updateButtons()
    {
        this.buttonNextPage.visible = this.currPage < this.bookTotalPages - 1;
        this.buttonPreviousPage.visible = this.currPage > 0;
    }

    private void onDataChange()
    {
        for (GuiButton button : this.buttonList)
        {
            if (button instanceof GuiElementCheckboxPreLaunch)
            {
                this.tagCompound.setBoolean(this.checkboxToKeyMap.get(button.id), ((GuiElementCheckboxPreLaunch) button).isSelected);
            }
        }

        // Send changed tag compound to server
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_UPDATE_CHECKLIST, GCCoreUtil.getDimensionID(mc.player.world), new Object[] { this.tagCompound }));

        // Update client item
        ItemStack stack = mc.player.getHeldItem(EnumHand.MAIN_HAND /* TODO Support off-hand use */);
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null)
        {
            tagCompound = new NBTTagCompound();
        }
        tagCompound.setTag("checklistData", this.tagCompound);
        stack.setTagCompound(tagCompound);
    }

    @Override
    protected void actionPerformed(GuiButton buttonClicked)
    {
        if (buttonClicked == this.buttonNextPage)
        {
            this.currPage++;
            this.initGui();
        }
        else if (buttonClicked == this.buttonPreviousPage)
        {
            this.currPage--;
            this.initGui();
        }
    }

    @Override
    public void updateScreen()
    {

    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bookGuiTexture);
        int i = (this.width - this.bookImageWidth) / 2;
        int j = 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.bookImageWidth, this.bookImageHeight);

        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void onSelectionChanged(GuiElementCheckboxPreLaunch element, boolean newSelected)
    {
        // Set all 'sub-boxes' to the new selection
        boolean started = false;
        for (int i = 2; i < this.buttonList.size(); ++i)
        {
            GuiButton button = this.buttonList.get(i);
            if (started)
            {
                if (button.x > element.x)
                {
                    ((GuiElementCheckboxPreLaunch) button).isSelected = newSelected;
                }
                else
                {
                    break;
                }
            }
            else if (button == element)
            {
                started = true;
            }
        }

        this.onDataChange();
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckboxPreLaunch checkbox, EntityPlayer player)
    {
        return true;
    }

    @Override
    public boolean getInitiallySelected(GuiElementCheckboxPreLaunch checkbox)
    {
        return this.tagCompound.getBoolean(this.checkboxToKeyMap.get(checkbox.id));
    }

    @Override
    public void onIntruderInteraction()
    {
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
    {
        private final boolean forward;

        public NextPageButton(int id, int x, int y, boolean forward)
        {
            super(id, x, y, 23, 13, "");
            this.forward = forward;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial)
        {
            if (this.visible)
            {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(bookGuiTexture);
                int i = 0;
                int j = 192;

                if (flag)
                {
                    i += 23;
                }

                if (!this.forward)
                {
                    j += 13;
                }

                this.drawTexturedModalRect(this.x, this.y, i, j, 23, 13);
            }
        }
    }
}
