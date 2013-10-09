package codechicken.nei;

import java.util.ArrayList;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import codechicken.core.gui.GuiCCButton;
import codechicken.core.gui.GuiCCTextField;
import codechicken.core.gui.GuiScrollSlot;
import codechicken.core.inventory.GuiContainerWidget;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.FontUtils;

public class GuiPotionCreator extends GuiContainerWidget
{
    public class GuiSlotPotionEffects extends GuiScrollSlot
    {
        public int selectedslot = -1;
        public boolean enabled = true;
        private ArrayList<Potion> validPotions = new ArrayList<Potion>();

        public GuiSlotPotionEffects(int x, int y)
        {
            super(x, y, 108, 76);
            for(Potion p : Potion.potionTypes)
                if(p != null)
                    validPotions.add(p);
            setSmoothScroll(false);
            setContentSize(x, y, height);
        }

        @Override
        public int getSlotHeight()
        {
            return 19;
        }

        @Override
        public void drawSlotBox(float frame)
        {
            drawRect(x, y, x + width, y + height, 0xFF000000);
        }

        @Override
        public void drawOverlay(float frame)
        {
        }

        @Override
        public boolean drawLineGuide()
        {
            return false;
        }

        @Override
        public int getScrollBarWidth()
        {
            return 7;
        }

        @Override
        protected void drawSlot(int slot, int x, int y, int mx, int my, boolean selected, float frame)
        {
            GL11.glColor4f(1, 1, 1, 1);
            Potion potion = validPotions.get(slot);
            PotionEffect effect = getEffect(potion.id);
            boolean blank = effect == null;
            if(effect == null)
                effect = new PotionEffect(potion.id, 1200, 0);
            int shade = selectedslot == slot ? 2 : blank ? 1 : 0;

            CCRenderState.changeTexture("textures/gui/container/enchanting_table.png");
            drawTexturedModalRect(x, y, 0, 166 + getSlotHeight() * shade, width - 30, getSlotHeight());
            drawTexturedModalRect(x + width - 30, y, width - 23, 166 + getSlotHeight() * shade, 30, getSlotHeight());

            if(potion.hasStatusIcon())
            {
                CCRenderState.changeTexture("textures/gui/container/inventory.png");
                int icon = potion.getStatusIconIndex();
                drawTexturedModalRect(x + 1, y + 1, 0 + icon % 8 * 18, 198 + icon / 8 * 18, 18, 18);
            }

            String name = StatCollector.translateToLocal(potion.getName());
            String amp = effect.getAmplifier() > 0 ? " " + translateAmplifier(effect.getAmplifier()) : "";
            int textColour = shade == 0 ? 0x685e4a : shade == 1 ? 0x407f10 : 0xffff80;
            if(fontRenderer.getStringWidth(name + amp) < width - 20)
            {
                fontRenderer.drawString(name + amp, x + 20, y + 1, textColour);
            }
            else
            {
                fontRenderer.drawString(name, x + 20, y + 1, textColour);
                FontUtils.drawRightString(amp, x + width - 10, y + 10, textColour);
            }

            String duration = Potion.getDurationString(effect);
            textColour = shade == 0 ? 0xA0A0A0 : shade == 1 ? 0x808080 : 0xCCCCCC;
            fontRenderer.drawStringWithShadow(duration, x + 20, y + 10, textColour);
        }

        private PotionEffect getEffect(int id)
        {
            ItemStack potion = container.potionInv.getStackInSlot(0);
            if(potion != null && potion.hasTagCompound() && potion.getTagCompound().hasKey("CustomPotionEffects"))
            {
                NBTTagList potionTagList = potion.getTagCompound().getTagList("CustomPotionEffects");
                for(int i = 0; i < potionTagList.tagCount(); i++)
                {
                    PotionEffect effect = PotionEffect.readCustomPotionEffectFromNBT((NBTTagCompound) potionTagList.tagAt(i));
                    if(effect.getPotionID() == id)
                        return effect;
                }
            }
            return null;
        }

        @Override
        protected int getNumSlots()
        {
            return validPotions.size();
        }

        @Override
        protected boolean isSlotSelected(int slot)
        {
            return slot == selectedslot;
        }

        @Override
        protected void slotClicked(int slot, int button, int mx, int my, boolean doubleclick)
        {
            if(!enabled)
                return;

            if(button == 0)
            {
                select(slot);
                applyEffect();
            }
            else if(button == 1)
            {
                deselect();
                container.removePotionEffect(validPotions.get(slot).id);
            }
        }

        public void selectNext()
        {
            if(selectedslot >= 0 && selectedslot + 1 < getNumSlots())
            {
                select(selectedslot + 1);
                showSlot(selectedslot);
            }
        }

        public void selectPrev()
        {
            if(selectedslot > 0)
            {
                select(selectedslot - 1);
                showSlot(selectedslot);
            }
        }

        public void setEnabled(boolean b)
        {
            if(b == enabled)
                return;
            enabled = b;
            if(!enabled)
                deselect();
        }

        private void select(int slot)
        {
            selectedslot = slot;
            durationField.setEnabled(true);
            PotionEffect effect = getEffect(validPotions.get(slot).id);
            if(effect == null)
                effect = new PotionEffect(validPotions.get(slot).id, 1200, 0);
            durationField.setDurationTicks(effect.getDuration());
            amplifier = effect.getAmplifier();
            validateInputButtons();
        }

        private void deselect()
        {
            selectedslot = -1;
            durationField.setEnabled(false);
            ampDown.setEnabled(false);
            ampUp.setEnabled(false);
        }

        public int selectedPotion()
        {
            return validPotions.get(selectedslot).id;
        }
    }

    public class GuiDurationField extends GuiCCTextField
    {
        private String baseValue;

        public GuiDurationField(int x, int y, int width, int height)
        {
            super(x, y, width, height, "100");
            setMaxStringLength(4);
            setAllowedCharacters("0123456789");
            baseValue = getText();
        }

        public void setDurationTicks(int i)
        {
            i /= 20;
            String minutes = Integer.toString(i / 60);
            String seconds = Integer.toString(i % 60);
            if(seconds.length() == 1)
                seconds = '0' + seconds;
            setText(minutes + seconds);
        }

        @Override
        public void setEnabled(boolean b)
        {
            super.setEnabled(b);
            if(!isEnabled())
                setText("100");
        }

        @Override
        public void onFocusChanged()
        {
            if(isFocused())
                baseValue = getText();
            else
            {
                if(!validateValue())
                    setText(baseValue);
                else
                    applyEffect();
            }
        }

        @Override
        public void onTextChanged(String oldText)
        {
            validateInputButtons();
        }

        private boolean validateValue()
        {
            try
            {
                int i = Integer.parseInt(getText());
                return i > 1 && getDurationTicks() < Short.MAX_VALUE && i / 100 <= 60 && i % 100 < 60;
            }
            catch(NumberFormatException nfe)
            {
                return false;
            }
        }

        @Override
        public void drawText()
        {
            String s = getText();
            String seconds = s.substring(Math.max(0, s.length() - 2), Math.max(0, s.length() - 2) + Math.min(s.length(), 2));
            String minutes = s.length() < 3 ? "" : s.substring(0, s.length() - 2);
            int ty = y + height / 2 - 4;
            int tcolour = getTextColour();
            FontUtils.drawCenteredString(":", x + width / 2 + 1, ty, tcolour);
            FontUtils.drawRightString(seconds, x + width - 3, ty, tcolour);
            FontUtils.drawRightString(minutes, x + width / 2 - 1, ty, tcolour);
        }

        @Override
        public int getTextColour()
        {
            return isFocused() ? 0xCCCCCC : 0x909090;
        }

        public int getDurationTicks()
        {
            int i = Integer.parseInt(getText());
            return ((i / 100) * 60 + (i % 100)) * 20;
        }
    }

    int amplifier = 0;
    GuiCCButton ampDown;
    GuiCCButton ampUp;
    GuiSlotPotionEffects slotPotionEffects;
    GuiDurationField durationField;
    ContainerPotionCreator container;

    public GuiPotionCreator(InventoryPlayer inventoryplayer)
    {
        super(new ContainerPotionCreator(inventoryplayer, new ContainerPotionCreator.InventoryPotionStore()), 176, 208);
        container = (ContainerPotionCreator) inventorySlots;
    }

    public void applyEffect()
    {
        if(slotPotionEffects.selectedslot >= 0)
            container.setPotionEffect(slotPotionEffects.selectedPotion(), durationField.getDurationTicks(), amplifier);
    }

    public String translateAmplifier(int amplifier)
    {
        switch(amplifier)
        {
            case 0:
                return "I";
            case 1:
                return "II";
            case 2:
                return "III";
            case 3:
                return "IV";
            case 4:
                return "V";
        }
        return Integer.toString(amplifier);
    }

    public void validateInputButtons()
    {
        ampDown.setEnabled(amplifier > 0);
        ampUp.setEnabled(amplifier < 3);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        //fontRenderer.drawString("Potion", 12, 6, 0x404040);
    }

    @Override
    public void drawBackground()
    {
        CCRenderState.changeTexture("nei:textures/gui/potion.png");
        drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);

        FontUtils.drawCenteredString("Favourite Potions", xSize / 2, 4, 0x404040);
        fontRenderer.drawString("Duration", 12, 40, 0x404040);
        fontRenderer.drawString("Level", 19, 73, 0x404040);
        FontUtils.drawCenteredString(translateAmplifier(amplifier), 33, 86, 0xFF606060);
    }

    @Override
    public void addWidgets()
    {
        add(ampDown = new GuiCCButton(10, 84, 12, 12, "<").setActionCommand("ampDown"));
        add(ampUp = new GuiCCButton(44, 84, 12, 12, ">").setActionCommand("ampUp"));
        add(durationField = new GuiDurationField(15, 53, 35, 12));
        add(slotPotionEffects = new GuiSlotPotionEffects(60, 38));
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        slotPotionEffects.setEnabled(container.potionInv.getStackInSlot(0) != null);
    }

    @Override
    public void actionPerformed(String ident, Object... params)
    {
        if(ident.equals("ampDown"))
            amplifier--;
        else if(ident.equals("ampUp"))
            amplifier++;
        applyEffect();
        validateInputButtons();
    }
}
