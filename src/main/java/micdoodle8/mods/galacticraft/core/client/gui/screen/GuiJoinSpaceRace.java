package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementGradientButton;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox.ITextBoxCallback;
import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiJoinSpaceRace extends Screen implements ICheckBoxCallback, ITextBoxCallback
{
    protected static final ResourceLocation texture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/gui.png");

    private int ticksPassed;
    private PlayerEntity thePlayer;
    private boolean initialized;

    private int buttonFlag_height;
    private int buttonFlag_xPosition;
    private int buttonFlag_yPosition;

    private EntityFlag dummyFlag = new EntityFlag(GCEntities.FLAG.get(), Minecraft.getInstance().world);
    private ModelFlag dummyModel = new ModelFlag();

    private SpaceRace spaceRaceData;

    public GuiJoinSpaceRace(ClientPlayerEntity player)
    {
        super(new StringTextComponent("Space Race"));
        this.thePlayer = player;
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);

        SpaceRace race = SpaceRaceManager.getSpaceRaceFromID(stats.getSpaceRaceInviteTeamID());

        if (race != null)
        {
            this.spaceRaceData = race;
        }
        else
        {
            List<String> playerList = new ArrayList<String>();
            playerList.add(PlayerUtil.getName(player));
            this.spaceRaceData = new SpaceRace(playerList, SpaceRace.DEFAULT_NAME, new FlagData(48, 32), new Vector3(1, 1, 1));
        }
    }

    @Override
    protected void init()
    {
        super.init();
        this.buttons.clear();

        if (this.initialized)
        {
            final int var5 = (this.width - this.width / 4) / 2;
            final int var6 = (this.height - this.height / 4) / 2;

            int buttonFlag_width = 81;
            this.buttonFlag_height = 58;
            this.buttonFlag_xPosition = this.width / 2 - buttonFlag_width / 2;
            this.buttonFlag_yPosition = this.height / 2 - this.height / 3 + 10;

            this.buttons.add(new GuiElementGradientButton(this.width / 2 - this.width / 3 + 15, this.height / 2 - this.height / 4 - 15, 50, 15, GCCoreUtil.translate("gui.space_race.create.close.name"), (button) -> {
                this.thePlayer.closeScreen();
            }));
            int width = (int) (var5 / 1.0F);
            this.buttons.add(new GuiElementGradientButton(this.width / 2 - width / 2, this.buttonFlag_yPosition + this.buttonFlag_height + 60, width, 20, GCCoreUtil.translateWithFormat("gui.space_race.join.name", this.spaceRaceData.getTeamName()), (button) -> {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ADD_RACE_PLAYER, GCCoreUtil.getDimensionID(minecraft.world), new Object[] { PlayerUtil.getName(this.thePlayer), this.spaceRaceData.getSpaceRaceID() }));
                this.thePlayer.closeScreen();
            }));
        }
    }

    @Override
    public void tick()
    {
        super.tick();
        ++this.ticksPassed;

        if (!this.initialized)
        {
        }
    }

    @Override
    public void render(int par1, int par2, float par3)
    {
        this.renderBackground();

        if (this.initialized)
        {
            this.drawCenteredString(this.font, GCCoreUtil.translate("gui.space_race.join.title.name"), this.width / 2, this.height / 2 - this.height / 3 - 15, 16777215);
            this.drawFlagButton(par1, par2);
            this.drawCenteredString(this.font, GCCoreUtil.translate("gui.space_race.join.owner.name") + ": " + this.spaceRaceData.getPlayerNames().get(0), this.width / 2, this.buttonFlag_yPosition + this.buttonFlag_height + 25, ColorUtil.to32BitColor(255, 150, 150, 150));
            this.drawCenteredString(this.font, GCCoreUtil.translateWithFormat("gui.space_race.join.member_count.name", this.spaceRaceData.getPlayerNames().size()), this.width / 2, this.buttonFlag_yPosition + this.buttonFlag_height + 40, ColorUtil.to32BitColor(255, 150, 150, 150));
            GL11.glPushMatrix();
            GL11.glTranslatef(this.width / 2, this.buttonFlag_yPosition + this.buttonFlag_height + 5 + Minecraft.getInstance().fontRenderer.FONT_HEIGHT / 2, 0);
            GL11.glScalef(1.5F, 1.5F, 1.0F);
            GL11.glTranslatef(-this.width / 2, (-(this.buttonFlag_yPosition + this.buttonFlag_height + 5)) - Minecraft.getInstance().fontRenderer.FONT_HEIGHT / 2, 0);
            this.drawCenteredString(this.font, this.spaceRaceData.getTeamName(), this.width / 2, this.buttonFlag_yPosition + this.buttonFlag_height + 5, ColorUtil.to32BitColor(255, 100, 150, 20));
            GL11.glPopMatrix();
        }

        super.render(par1, par2, par3);
    }

    private void drawFlagButton(int mouseX, int mouseY)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.buttonFlag_xPosition + 2.9F, this.buttonFlag_yPosition + this.buttonFlag_height + 1 - 4, 0);
        GL11.glScalef(74.0F, 74.0F, 1F);
        GL11.glTranslatef(0.0F, 0.36F, 1.0F);
        GL11.glScalef(1.0F, 1.0F, -1F);
        this.dummyFlag.flagData = this.spaceRaceData.getFlagData();
        this.dummyModel.renderFlag(this.dummyFlag, this.ticksPassed);
        GL11.glColor3f(1, 1, 1);
        GL11.glPopMatrix();
    }

    @Override
    public void renderBackground(int i)
    {
        if (this.minecraft.world != null)
        {
            int scaleX = Math.min(this.ticksPassed * 14, this.width / 3);
            int scaleY = Math.min(this.ticksPassed * 14, this.height / 3);

            if (scaleX == this.width / 3 && scaleY == this.height / 3 && !this.initialized)
            {
                this.initialized = true;
                this.init();
            }

            this.fillGradient(this.width / 2 - scaleX, this.height / 2 - scaleY, this.width / 2 + scaleX, this.height / 2 + scaleY, -1072689136, -804253680);
        }
        else
        {
            this.renderBackground(i);
        }
    }

    @Override
    public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
    {

    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, PlayerEntity player)
    {
        return true;
    }

    @Override
    public boolean getInitiallySelected(GuiElementCheckbox checkbox)
    {
        return false;
    }

    @Override
    public void onIntruderInteraction()
    {
    }

    @Override
    public void onIntruderInteraction(GuiElementTextBox textBox)
    {
    }

    @Override
    public boolean canPlayerEdit(GuiElementTextBox textBox, PlayerEntity player)
    {
        return true;
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
        return ColorUtil.to32BitColor(255, 255, 255, 255);
    }
}
