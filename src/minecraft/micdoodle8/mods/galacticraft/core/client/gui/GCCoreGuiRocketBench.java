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

public class GCCoreGuiRocketBench extends GuiContainer
{
    public GCCoreGuiRocketBench(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
    {
        super(new GCCoreContainerRocketBench(par1InventoryPlayer, x, y, z));
    }

    @Override
    public void initGui()
    {
    	super.initGui();
        this.controlList.clear();
        final GuiButton button = new GuiButton(0, this.width / 2 - 130, this.height / 2 - 30, 40, 20, "Back");
        button.enabled = false;
        this.controlList.add(button);
        this.controlList.add(new GuiButton(1, this.width / 2 + 90, this.height / 2 - 30, 40, 20, "Next"));
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

                while (FMLClientHandler.instance().getClient().currentScreen != null)
                {

                }

                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 4, toSend));
                FMLClientHandler.instance().getClient().thePlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiBuggyCraftingBench, FMLClientHandler.instance().getClient().thePlayer.worldObj, (int)FMLClientHandler.instance().getClient().thePlayer.posX, (int)FMLClientHandler.instance().getClient().thePlayer.posY, (int)FMLClientHandler.instance().getClient().thePlayer.posZ);
                break;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("NASA Workbench", 7, -20, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 202 - 104 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        final int var4 = this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/rocketbench.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - 220) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, 220);
    }
}