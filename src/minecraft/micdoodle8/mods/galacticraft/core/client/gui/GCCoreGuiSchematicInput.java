package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerRocketBench;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GCCoreGuiSchematicInput extends GuiContainer
{
	private GuiButton backButton;
	private GuiButton nextButton;
	private GuiButton unlockButton;
	
    public GCCoreGuiSchematicInput(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
    {
        super(new GCCoreContainerRocketBench(par1InventoryPlayer, x, y, z));
    }

    @Override
    public void initGui()
    {
    	super.initGui();
        this.buttonList.clear();
        this.buttonList.add((backButton = new GuiButton(0, this.width / 2 - 130, this.height / 2 - 30 + 27, 40, 20, "Back")));
        this.buttonList.add((nextButton = new GuiButton(1, this.width / 2 + 90, this.height / 2 - 30 + 27, 40, 20, "Next")));
        this.buttonList.add((unlockButton = new GuiButton(2, this.width / 2 - 20, this.height / 2 - 30 + 27, 40, 20, "Unlock Schematic")));
        this.nextButton.enabled = false;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            switch (par1GuiButton.id)
            {
            case 1:
                final Object[] toSend = {1};
                FMLClientHandler.instance().getClient().displayGuiScreen(null);

                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 4, toSend));
                FMLClientHandler.instance().getClient().thePlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiBuggyCraftingBench, FMLClientHandler.instance().getClient().thePlayer.worldObj, (int)FMLClientHandler.instance().getClient().thePlayer.posX, (int)FMLClientHandler.instance().getClient().thePlayer.posY, (int)FMLClientHandler.instance().getClient().thePlayer.posZ);
                break;
            case 2:
                final Object[] toSend2 = {0};
            	PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 16, toSend2));
            	break;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("NASA Workbench", 7, -20 + 27, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 202 - 104 + 2 + 27, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/micdoodle8/mods/galacticraft/core/client/gui/schematicpage.png");
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - 220) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, 220);
    }
}