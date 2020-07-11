package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class GuiSlimelingFeed extends Screen
{
    private final int xSize;
    private final int ySize;
    private static final ResourceLocation slimelingPanelGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/slimeling_panel1.png");
    private final EntitySlimeling slimeling;

//    public static RenderItem drawItems = new RenderItem();

    public long timeBackspacePressed;
    public int cursorPulse;
    public int backspacePressed;
    public boolean isTextFocused = false;

    public Button buttonGrowSlimeling;
    public Button buttonBreedSlimeling;
    public Button buttonStrengthenSlimeling;
    public Button buttonHealSlimeling;

    public GuiSlimelingFeed(EntitySlimeling slimeling)
    {
        super(new StringTextComponent("gui.slimeling.feed"));
        this.slimeling = slimeling;
        this.xSize = 138;
        this.ySize = 51;
    }

    @Override
    public void init()
    {
        super.init();
        this.buttons.clear();
        final int var6 = (this.height - this.ySize) / 2;
        this.buttonGrowSlimeling = new Button(this.width / 2 - 65, var6 - 15, 64, 20, GCCoreUtil.translate("gui.message.grow.name"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{this.slimeling.getEntityId(), 2, ""}));
            Minecraft.getInstance().displayGuiScreen(null);
        });
        this.buttons.add(this.buttonGrowSlimeling);
        this.buttonBreedSlimeling = new Button(this.width / 2 + 1, var6 - 15, 64, 20, GCCoreUtil.translate("gui.message.breed.name"), (button) ->
        {
            if (!this.slimeling.isInLove() && this.slimeling.isOwner(this.minecraft.player) && this.slimeling.world.isRemote)
            {
                this.slimeling.setInLove(this.minecraft.player);
            }

            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{this.slimeling.getEntityId(), 3, ""}));
            Minecraft.getInstance().displayGuiScreen(null);
        });
        this.buttons.add(this.buttonBreedSlimeling);
        this.buttonStrengthenSlimeling = new Button(this.width / 2 - 65, var6 + 7, 64, 20, GCCoreUtil.translate("gui.message.strengthen.name"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{this.slimeling.getEntityId(), 4, ""}));
            Minecraft.getInstance().displayGuiScreen(null);
        });
        this.buttons.add(this.buttonStrengthenSlimeling);
        this.buttonHealSlimeling = new Button(this.width / 2 + 1, var6 + 7, 64, 20, GCCoreUtil.translate("gui.message.heal.name"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{this.slimeling.getEntityId(), 5, ""}));
            Minecraft.getInstance().displayGuiScreen(null);
        });
        this.buttons.add(this.buttonHealSlimeling);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

//    @Override
//    protected void keyTyped(char keyChar, int keyID) throws IOException
//    {
//        if (!this.buttonGrowSlimeling.enabled && !this.buttonBreedSlimeling.enabled && !this.buttonStrengthenSlimeling.enabled && !this.buttonHealSlimeling.enabled)
//    {
//            super.keyTyped(keyChar, keyID);
//        }
//        return;
//    } TOOD ?

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.minecraft.textureManager.bindTexture(GuiSlimelingFeed.slimelingPanelGui);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.blit(var5, var6 - 20, 0, 0, this.xSize, this.ySize);

        super.render(mouseX, mouseY, partialTicks);

        this.buttonHealSlimeling.active = this.slimeling.getHealth() < Math.floor(this.slimeling.getMaxHealth());
        this.buttonGrowSlimeling.active = this.slimeling.getScale() < 1.0F;
        this.buttonStrengthenSlimeling.active = this.slimeling.getAttackDamage() < 1.0;
        this.buttonBreedSlimeling.active = !this.slimeling.isInLove() && !this.slimeling.isChild();
    }
}
