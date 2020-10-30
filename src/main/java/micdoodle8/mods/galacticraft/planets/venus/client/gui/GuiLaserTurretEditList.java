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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.List;

public class GuiLaserTurretEditList extends Screen implements GuiElementTextBox.ITextBoxCallback, GuiElementCheckbox.ICheckBoxCallback
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
        super(new StringTextComponent("Edit Turret List"));
        this.laserTurret = turret;
    }

    @Override
    public void init()
    {
        int yTop;
        this.buttons.clear();
        super.init();
        switch (mode)
        {
        case MAIN:
            this.ySize = 144;
            this.xSize = 222;
            yTop = (this.height - this.ySize) / 2;
            this.buttons.add(new Button((this.width - this.xSize) / 2 + 4, yTop + 5, 62, 20, GCCoreUtil.translate("gui.button.add_player"), (button) ->
            {
                mode = EnumMode.ADD_PLAYER;
                init();
            }));
            this.buttons.add(new Button((this.width - this.xSize) / 2 + 66, yTop + 5, 62, 20, GCCoreUtil.translate("gui.button.add_entity"), (button) ->
            {
                mode = EnumMode.ADD_ENTITY;
                init();
            }));
            this.buttons.add(new Button((this.width - this.xSize) / 2 + 128, yTop + 5, 90, 20, GCCoreUtil.translate("gui.button.remove_selected"), (button) ->
            {
                GuiElementGradientList.ListElement selected = entityListElement.getSelectedElement();
                if (selected != null)
                {
                    boolean isPlayer = selected.value.contains(GCCoreUtil.translate("gui.message.player") + ": ");
                    String toSend = isPlayer ? selected.value.substring(8) : selected.value;
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_MODIFY_LASER_TARGET, GCCoreUtil.getDimensionType(laserTurret.getWorld()), new Object[]{isPlayer ? 2 : 3, laserTurret.getPos(), toSend}));
                    if (isPlayer)
                    {
                        laserTurret.removePlayer(toSend);
                    }
                    else
                    {
                        laserTurret.removeEntity(new ResourceLocation(toSend));
                    }
                    init();
                }
            }));
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
                    alphabeticalList.set(i, GCCoreUtil.translate("gui.message.player") + ": " + alphabeticalList.get(i));
                }
            }
            List<GuiElementGradientList.ListElement> list = Lists.newArrayList();
            for (String str : alphabeticalList)
            {
                list.add(new GuiElementGradientList.ListElement(str, ColorUtil.to32BitColor(255, 240, 240, 240)));
            }
            this.entityListElement.updateListContents(list);
            this.neverAttackSpaceRace = new GuiElementCheckbox(this, this.width / 2 - 106, yTop + 126, GCCoreUtil.translate("gui.button.never_fire_team"), 4210752);
            this.buttons.add(this.neverAttackSpaceRace);
            break;
        case ADD_PLAYER:
        case ADD_ENTITY:
            this.ySize = 70;
            this.xSize = 148;
            yTop = (this.height - this.ySize) / 2;
            this.name = new GuiElementTextBox(this, (this.width - this.xSize) / 2 + 4, yTop + 16, 140, 20, "", false, 64, false);
            this.name.resetOnClick = false;
            this.addButton(this.name);
            this.buttons.add(new Button((this.width - this.xSize) / 2 + this.xSize / 2 - 31, yTop + 40, 62, 20, GCCoreUtil.translate(laserTurret.blacklistMode ? "gui.button.add_blacklist" : "gui.button.add_whitelist"), (button) ->
            {
                if (mode == EnumMode.ADD_ENTITY)
                {
                    EntityType<?> entry = getEntityEntry();
                    if (entry != null)
                    {
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_MODIFY_LASER_TARGET, GCCoreUtil.getDimensionType(laserTurret.getWorld()), new Object[]{1, laserTurret.getPos(), entry.getRegistryName().toString()}));
                        laserTurret.addEntity(entry.getRegistryName());
                    }
                }
                else if (mode == EnumMode.ADD_PLAYER)
                {
                    if (name.text != null && !name.text.isEmpty())
                    {
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_MODIFY_LASER_TARGET, GCCoreUtil.getDimensionType(laserTurret.getWorld()), new Object[]{0, laserTurret.getPos(), name.text}));
                        laserTurret.addPlayer(name.text);
                    }
                }
                name.text = null;
                mode = EnumMode.MAIN;
                init();
            }));
            break;
        }
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers)
    {
        if (key != GLFW.GLFW_KEY_ESCAPE)
        {
            if (this.mode != EnumMode.MAIN && this.name.keyPressed(key, scanCode, modifiers))
            {
                return true;
            }
        }
        else
        {
            switch (mode)
            {
            case MAIN:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_OPEN_LASER_TURRET_GUI, GCCoreUtil.getDimensionType(laserTurret.getWorld()), new Object[]{laserTurret.getPos()}));
                break;
            default:
                mode = EnumMode.MAIN;
                init();
                break;
            }
        }

        return super.keyPressed(key, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public void tick()
    {
        if (this.mode == EnumMode.MAIN)
        {
            this.entityListElement.update();
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GuiLaserTurretEditList.backgroundTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.blit(var5, var6, 0, mode == EnumMode.MAIN ? 0 : 144, this.xSize, this.ySize);

        super.render(mouseX, mouseY, partialTicks);

        if (this.mode == EnumMode.MAIN)
        {
            this.entityListElement.draw(mouseX, mouseY);
        }
        else if (this.mode == EnumMode.ADD_PLAYER)
        {
            String displayString = GCCoreUtil.translate("gui.message.enter_player");
            this.font.drawString(displayString, var5 + this.xSize / 2 - this.font.getStringWidth(displayString) / 2, var6 + 5, 4210752);
        }
        else if (this.mode == EnumMode.ADD_ENTITY)
        {
            String displayString = GCCoreUtil.translate("gui.message.enter_entity");
            this.font.drawString(displayString, var5 + this.xSize / 2 - this.font.getStringWidth(displayString) / 2, var6 + 5, 4210752);
        }
    }

    private EntityType<?> getEntityEntry()
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
                if (this.name.text.equalsIgnoreCase(loc.getPath()))
                {
                    return ForgeRegistries.ENTITIES.getValue(loc);
                }
            }
            // Then check names
            for (ResourceLocation loc : ForgeRegistries.ENTITIES.getKeys())
            {
                EntityType<?> entry = ForgeRegistries.ENTITIES.getValue(loc);
                if (entry != null)
                {
                    if (this.name.text.equalsIgnoreCase(entry.getName().getFormattedText()))
                    {
                        return entry;
                    }
                }
            }
            for (ResourceLocation loc : ForgeRegistries.ENTITIES.getKeys())
            {
                EntityType<?> entry = ForgeRegistries.ENTITIES.getValue(loc);
                if (entry != null)
                {
                    if (this.name.text.equalsIgnoreCase(LanguageMap.getInstance().translateKey("entity." + entry.getName() + "")))
                    {
                        return entry;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean canPlayerEdit(GuiElementTextBox textBox, PlayerEntity player)
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
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{2, this.laserTurret.getPos(), this.laserTurret.alwaysIgnoreSpaceRace ? 1 : 0}));
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, PlayerEntity player)
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
