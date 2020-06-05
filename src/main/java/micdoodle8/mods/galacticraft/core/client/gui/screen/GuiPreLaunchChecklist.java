package micdoodle8.mods.galacticraft.core.client.gui.screen;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckboxPreLaunch;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

public class GuiPreLaunchChecklist extends Screen implements GuiElementCheckboxPreLaunch.ICheckBoxCallback
{
    private static final ResourceLocation bookGuiTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/checklist_book.png");
    private int bookImageWidth = 192;
    private int bookImageHeight = 192;
    private List<List<String>> checklistKeys;
    private int currPage = 0;
    private int bookTotalPages;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private CompoundNBT tagCompound;
    private Map<Integer, String> checkboxToKeyMap = Maps.newHashMap();

    public GuiPreLaunchChecklist(List<List<String>> checklistKeys, CompoundNBT tagCompound)
    {
        this.tagCompound = tagCompound != null ? tagCompound : new CompoundNBT();
        this.checklistKeys = checklistKeys;
    }

    @Override
    public void init()
    {
        this.buttons.clear();
        this.checkboxToKeyMap.clear();

        int i = (this.width - this.bookImageWidth) / 2;
        int j = 2;
        this.buttons.add(this.buttonNextPage = new NextPageButton(0, i + 120, j + 154, true));
        this.buttons.add(this.buttonPreviousPage = new NextPageButton(1, i + 38, j + 154, false));

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
                    this.buttons.add(element);
                    this.checkboxToKeyMap.put(element.id, title);
                    index++;
                }
                yPos += size + minecraft.fontRenderer.FONT_HEIGHT / 2;
            }
            else
            {
                page++;
                yPos = 25;
                size = element.willFit(152 - yPos);
                element = new GuiElementCheckboxPreLaunch(index, this, this.width / 2 - 73 + 11, yPos, GCCoreUtil.translate(title), 0);

                if (page == this.currPage)
                {
                    this.buttons.add(element);
                    this.checkboxToKeyMap.put(element.id, title);
                    index++;
                }
                yPos += size + minecraft.fontRenderer.FONT_HEIGHT / 2;
            }

            for (String checkbox : checkboxes)
            {
                element = new GuiElementCheckboxPreLaunch(index, this, this.width / 2 - 73 + 16, yPos, GCCoreUtil.translate("checklist." + checkbox + ".key"), 0);
                size = element.willFit(152 - yPos);
                if (size >= 0)
                {
                    if (page == this.currPage)
                    {
                        this.buttons.add(element);
                        this.checkboxToKeyMap.put(element.id, title + "." + checkbox + ".key");
                        index++;
                    }
                    yPos += size + minecraft.fontRenderer.FONT_HEIGHT / 2;
                }
                else
                {
                    page++;
                    yPos = 25;
                    size = element.willFit(152 - yPos);
                    element = new GuiElementCheckboxPreLaunch(index, this, this.width / 2 - 73 + 16, yPos, GCCoreUtil.translate("checklist." + checkbox + ".key"), 0);

                    if (page == this.currPage)
                    {
                        this.buttons.add(element);
                        this.checkboxToKeyMap.put(element.id, title + "." + checkbox + ".key");
                        index++;
                    }
                    yPos += size + minecraft.fontRenderer.FONT_HEIGHT / 2;
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
        for (Widget button : this.buttons)
        {
            if (button instanceof GuiElementCheckboxPreLaunch)
            {
                this.tagCompound.putBoolean(this.checkboxToKeyMap.get(button.id), ((GuiElementCheckboxPreLaunch) button).isSelected);
            }
        }

        // Send changed tag compound to server
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_UPDATE_CHECKLIST, GCCoreUtil.getDimensionID(minecraft.player.world), new Object[] { this.tagCompound }));

        // Update client item
        ItemStack stack = minecraft.player.getHeldItem(Hand.MAIN_HAND /* TODO Support off-hand use */);
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound == null)
        {
            tagCompound = new CompoundNBT();
        }
        tagCompound.put("checklistData", this.tagCompound);
        stack.setTag(tagCompound);
    }



    @Override
    protected void actionPerformed(Button buttonClicked)
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
        this.minecraft.getTextureManager().bindTexture(bookGuiTexture);
        int i = (this.width - this.bookImageWidth) / 2;
        int j = 2;
        this.blit(i, j, 0, 0, this.bookImageWidth, this.bookImageHeight);

        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void onSelectionChanged(GuiElementCheckboxPreLaunch element, boolean newSelected)
    {
        // Set all 'sub-boxes' to the new selection
        boolean started = false;
        for (int i = 2; i < this.buttons.size(); ++i)
        {
            Button button = this.buttons.get(i);
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
    public boolean canPlayerEdit(GuiElementCheckboxPreLaunch checkbox, PlayerEntity player)
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

    @OnlyIn(Dist.CLIENT)
    static class NextPageButton extends Button
    {
        private final boolean forward;

        public NextPageButton(int id, int x, int y, boolean forward)
        {
            super(id, x, y, 23, 13, "");
            this.forward = forward;
        }

        @Override
        public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partial)
        {
            if (this.visible)
            {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                minecraft.getTextureManager().bindTexture(bookGuiTexture);
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

                this.blit(this.x, this.y, i, j, 23, 13);
            }
        }
    }
}
