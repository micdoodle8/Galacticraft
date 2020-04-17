package micdoodle8.mods.galacticraft.planets.venus.client.gui;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementGradientList;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.network.PacketSimpleVenus;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GuiLaserTurretEditList extends GuiScreen implements GuiElementTextBox.ITextBoxCallback, GuiElementCheckbox.ICheckBoxCallback
{
    private static final ResourceLocation backgroundTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/laser_turret_edit.png");

    private final TileEntityLaserTurret laserTurret;
    private GuiElementGradientList entityListElement;

    private int ySize;
    private int xSize;
    private EnumMode mode = EnumMode.MAIN;
    private GuiElementTextBox name;
    private GuiElementCheckbox neverAttackSpaceRace;

    private enum EnumMode
    {
        MAIN,
        ADD_PLAYER,
        ADD_ENTITY
    }

    public GuiLaserTurretEditList(TileEntityLaserTurret turret)
    {
        this.laserTurret = turret;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
        case 0:
            mode = EnumMode.ADD_PLAYER;
            initGui();
            break;
        case 1:
            mode = EnumMode.ADD_ENTITY;
            initGui();
            break;
        case 2:
            GuiElementGradientList.ListElement selected = entityListElement.getSelectedElement();
            if (selected != null)
            {
                boolean isPlayer = selected.value.contains(GCCoreUtil.translate("gui.message.player.name") + ": ");
                String toSend = isPlayer ? selected.value.substring(8) : selected.value;
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_MODIFY_LASER_TARGET, GCCoreUtil.getDimensionID(laserTurret.getWorld()), new Object[] { isPlayer ? 2 : 3, laserTurret.getPos(), toSend }));
                if (isPlayer)
                {
                    laserTurret.removePlayer(toSend);
                }
                else
                {
                    laserTurret.removeEntity(new ResourceLocation(toSend));
                }
                initGui();
            }
            break;
        case 4:
            if (mode == EnumMode.ADD_ENTITY)
            {
                EntityEntry entry = getEntityEntry();
                if (entry != null)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_MODIFY_LASER_TARGET, GCCoreUtil.getDimensionID(laserTurret.getWorld()), new Object[] { 1, laserTurret.getPos(), entry.getRegistryName().toString() }));
                    laserTurret.addEntity(entry.getRegistryName());
                }
            }
            else if (mode == EnumMode.ADD_PLAYER)
            {
                if (name.text != null && !name.text.isEmpty())
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_MODIFY_LASER_TARGET, GCCoreUtil.getDimensionID(laserTurret.getWorld()), new Object[] { 0, laserTurret.getPos(), name.text }));
                    laserTurret.addPlayer(name.text);
                }
            }
            name.text = null;
            mode = EnumMode.MAIN;
            initGui();
            break;
        }
    }

    @Override
    public void initGui()
    {
        int yTop;
        this.buttonList.clear();
        super.initGui();
        switch (mode)
        {
        case MAIN:
            this.ySize = 144;
            this.xSize = 222;
            yTop = (this.height - this.ySize) / 2;
            this.buttonList.add(new GuiButton(0, (this.width - this.xSize) / 2 + 4, yTop + 5, 62, 20, GCCoreUtil.translate("gui.button.add_player.name")));
            this.buttonList.add(new GuiButton(1, (this.width - this.xSize) / 2 + 66, yTop + 5, 62, 20, GCCoreUtil.translate("gui.button.add_entity.name")));
            this.buttonList.add(new GuiButton(2, (this.width - this.xSize) / 2 + 128, yTop + 5, 90, 20, GCCoreUtil.translate("gui.button.remove_selected.name")));
            this.entityListElement = new GuiElementGradientList((this.width - this.xSize) / 2 + 4, yTop + 26, xSize - 8, ySize - 45);
            List<String> alphabeticalList = Lists.newArrayList();
            alphabeticalList.addAll(laserTurret.getPlayers());
            for (ResourceLocation res : laserTurret.getEntities())
            {
                alphabeticalList.add(res.toString());
            }
            Collections.sort(alphabeticalList);
            for (int i = 0; i < alphabeticalList.size(); ++i)
            {
                if (laserTurret.getPlayers().contains(alphabeticalList.get(i)))
                {
                    alphabeticalList.set(i, GCCoreUtil.translate("gui.message.player.name") + ": " + alphabeticalList.get(i));
                }
            }
            List<GuiElementGradientList.ListElement> list = Lists.newArrayList();
            for (String str : alphabeticalList)
            {
                list.add(new GuiElementGradientList.ListElement(str, ColorUtil.to32BitColor(255, 240, 240, 240)));
            }
            this.entityListElement.updateListContents(list);
            this.neverAttackSpaceRace = new GuiElementCheckbox(5, this, this.width / 2 - 106, yTop + 126, GCCoreUtil.translate("gui.button.never_fire_team.name"), 4210752);
            this.buttonList.add(this.neverAttackSpaceRace);
            break;
        case ADD_PLAYER:
        case ADD_ENTITY:
            this.ySize = 70;
            this.xSize = 148;
            yTop = (this.height - this.ySize) / 2;
            this.name = new GuiElementTextBox(3, this, (this.width - this.xSize) / 2 + 4, yTop + 16, 140, 20, "", false, 64, false);
            this.name.resetOnClick = false;
            this.addButton(this.name);
            this.buttonList.add(new GuiButton(4, (this.width - this.xSize) / 2 + this.xSize / 2 - 31, yTop + 40, 62, 20, GCCoreUtil.translate(laserTurret.blacklistMode ? "gui.button.add_blacklist.name" : "gui.button.add_whitelist.name")));
            break;
        }
    }

    @Override
    protected void keyTyped(char keyChar, int keyID) throws IOException
    {
        if (this.mode != EnumMode.MAIN && keyID != Keyboard.KEY_ESCAPE)
        {
            if (this.name.keyTyped(keyChar, keyID))
            {
                return;
            }
        }

        if (keyID == 1)
        {
            switch (mode)
            {
            case MAIN:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_OPEN_LASER_TURRET_GUI, GCCoreUtil.getDimensionID(laserTurret.getWorld()), new Object[] { laserTurret.getPos() }));
                break;
            default:
                mode = EnumMode.MAIN;
                initGui();
                break;
            }
        }
        else
        {
            super.keyTyped(keyChar, keyID);
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void updateScreen()
    {
        if (this.mode == EnumMode.MAIN)
        {
            this.entityListElement.update();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiLaserTurretEditList.backgroundTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, mode == EnumMode.MAIN ? 0 : 144, this.xSize, this.ySize);

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.mode == EnumMode.MAIN)
        {
            this.entityListElement.draw(mouseX, mouseY);
        }
        else if (this.mode == EnumMode.ADD_PLAYER)
        {
            String displayString = GCCoreUtil.translate("gui.message.enter_player.name");
            this.fontRenderer.drawString(displayString, var5 + this.xSize / 2 - this.fontRenderer.getStringWidth(displayString) / 2, var6 + 5, 4210752);
        }
        else if (this.mode == EnumMode.ADD_ENTITY)
        {
            String displayString = GCCoreUtil.translate("gui.message.enter_entity.name");
            this.fontRenderer.drawString(displayString, var5 + this.xSize / 2 - this.fontRenderer.getStringWidth(displayString) / 2, var6 + 5, 4210752);
        }
    }

    private EntityEntry getEntityEntry()
    {
        if (this.name.text != null)
        {
            // Check all full entity names first
            for (ResourceLocation loc : ForgeRegistries.ENTITIES.getKeys())
            {
                if (this.name.text.equalsIgnoreCase(loc.toString()))
                {
                    return ForgeRegistries.ENTITIES.getValue(loc);
                }
            }
            // Then check without domain
            for (ResourceLocation loc : ForgeRegistries.ENTITIES.getKeys())
            {
                if (this.name.text.equalsIgnoreCase(loc.getResourcePath()))
                {
                    return ForgeRegistries.ENTITIES.getValue(loc);
                }
            }
            // Then check names
            for (ResourceLocation loc : ForgeRegistries.ENTITIES.getKeys())
            {
                EntityEntry entry = ForgeRegistries.ENTITIES.getValue(loc);
                if (entry != null)
                {
                    if (this.name.text.equalsIgnoreCase(entry.getName()))
                    {
                        return entry;
                    }
                }
            }
            for (ResourceLocation loc : ForgeRegistries.ENTITIES.getKeys())
            {
                EntityEntry entry = ForgeRegistries.ENTITIES.getValue(loc);
                if (entry != null)
                {
                    if (this.name.text.equalsIgnoreCase(I18n.translateToLocal("entity." + entry.getName() + ".name")))
                    {
                        return entry;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean canPlayerEdit(GuiElementTextBox textBox, EntityPlayer player)
    {
        return player.getUniqueID().equals(this.laserTurret.getOwnerUUID());
    }

    @Override
    public void onTextChanged(GuiElementTextBox textBox, String newText)
    {

    }

    @Override
    public String getInitialText(GuiElementTextBox textBox)
    {
        return "";
    }

    @Override
    public int getTextColor(GuiElementTextBox textBox)
    {
        if (this.mode == EnumMode.ADD_ENTITY)
        {
            if (getEntityEntry() != null)
            {
                return ColorUtil.to32BitColor(255, 80, 200, 80);
            }
            else
            {
                return ColorUtil.to32BitColor(255, 200, 80, 80);
            }
        }

        return ColorUtil.to32BitColor(255, 200, 200, 200);
    }

    @Override
    public void onIntruderInteraction(GuiElementTextBox textBox)
    {

    }

    @Override
    public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
    {
        this.laserTurret.alwaysIgnoreSpaceRace = newSelected;
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionID(mc.world), new Object[] { 2, this.laserTurret.getPos(), this.laserTurret.alwaysIgnoreSpaceRace ? 1 : 0 }));
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player)
    {
        return player.getUniqueID().equals(this.laserTurret.getOwnerUUID());
    }

    @Override
    public boolean getInitiallySelected(GuiElementCheckbox checkbox)
    {
        return laserTurret.alwaysIgnoreSpaceRace;
    }

    @Override
    public void onIntruderInteraction()
    {

    }
}
