package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public class GuiSlimelingFeed extends GuiScreen
{
    private final int xSize;
    private final int ySize;
    private static final ResourceLocation slimelingPanelGui = new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/gui/slimelingPanel1.png");
    private final EntitySlimeling slimeling;

    public long timeBackspacePressed;
    public int cursorPulse;
    public int backspacePressed;
    public boolean isTextFocused = false;

    public GuiButton buttonGrowSlimeling;
    public GuiButton buttonBreedSlimeling;
    public GuiButton buttonStrengthenSlimeling;
    public GuiButton buttonHealSlimeling;

    public GuiSlimelingFeed(EntitySlimeling slimeling)
    {
        this.slimeling = slimeling;
        this.xSize = 138;
        this.ySize = 51;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        final int var6 = (this.height - this.ySize) / 2;
        this.buttonGrowSlimeling = new GuiButton(0, this.width / 2 - 65, var6 - 15, 64, 20, GCCoreUtil.translate("gui.message.grow.name"));
        this.buttonList.add(this.buttonGrowSlimeling);
        this.buttonBreedSlimeling = new GuiButton(1, this.width / 2 + 1, var6 - 15, 64, 20, GCCoreUtil.translate("gui.message.breed.name"));
        this.buttonList.add(this.buttonBreedSlimeling);
        this.buttonStrengthenSlimeling = new GuiButton(2, this.width / 2 - 65, var6 + 7, 64, 20, GCCoreUtil.translate("gui.message.strengthen.name"));
        this.buttonList.add(this.buttonStrengthenSlimeling);
        this.buttonHealSlimeling = new GuiButton(3, this.width / 2 + 1, var6 + 7, 64, 20, GCCoreUtil.translate("gui.message.heal.name"));
        this.buttonList.add(this.buttonHealSlimeling);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            switch (par1GuiButton.id)
            {
            case 0:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, new Object[] { this.slimeling.getEntityId(), 2, "" }));
                break;
            case 1:
                if (!this.slimeling.isInLove() && this.slimeling.isOwner(this.mc.thePlayer) && this.slimeling.worldObj.isRemote)
                {
                    this.slimeling.func_146082_f(this.mc.thePlayer);
                }

                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, new Object[] { this.slimeling.getEntityId(), 3, "" }));
                break;
            case 2:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, new Object[] { this.slimeling.getEntityId(), 4, "" }));
                break;
            case 3:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, new Object[] { this.slimeling.getEntityId(), 5, "" }));
                break;
            }

            FMLClientHandler.instance().getClient().displayGuiScreen(null);
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        this.mc.renderEngine.bindTexture(GuiSlimelingFeed.slimelingPanelGui);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 - 20, 0, 0, this.xSize, this.ySize);

        super.drawScreen(par1, par2, par3);

        this.buttonHealSlimeling.enabled = this.slimeling.getHealth() < Math.floor(this.slimeling.getMaxHealth());
        this.buttonGrowSlimeling.enabled = this.slimeling.getScale() < 1.0F;
        this.buttonStrengthenSlimeling.enabled = this.slimeling.getAttackDamage() < 1.0;
        this.buttonBreedSlimeling.enabled = !this.slimeling.isInLove() && !this.slimeling.isChild();
    }
}
