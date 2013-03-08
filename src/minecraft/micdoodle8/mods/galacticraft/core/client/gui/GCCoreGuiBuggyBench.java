package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerBuggyBench;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GCCoreGuiBuggyBench extends GuiContainer
{
    public GCCoreGuiBuggyBench(InventoryPlayer par1InventoryPlayer)
    {
        super(new GCCoreContainerBuggyBench(par1InventoryPlayer, 0, 0, 0));
    }

    @Override
    public void initGui()
    {
    	super.initGui();
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 130, this.height / 2 - 30, 40, 20, "Back"));
        this.controlList.add(new GuiButton(1, this.width / 2 + 90, this.height / 2 - 30, 40, 20, "Next"));
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
        	Object[] toSend;

            switch (par1GuiButton.id)
            {
            case 0:
            	toSend = new Object[]{0};
                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 4, toSend));
                FMLClientHandler.instance().getClient().displayGuiScreen(null);
                FMLClientHandler.instance().getClient().thePlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiRocketCraftingBench, FMLClientHandler.instance().getClient().thePlayer.worldObj, (int)FMLClientHandler.instance().getClient().thePlayer.posX, (int)FMLClientHandler.instance().getClient().thePlayer.posY, (int)FMLClientHandler.instance().getClient().thePlayer.posZ);
                break;
            case 1:
                toSend = new Object[]{1};
                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 4, toSend));
                FMLClientHandler.instance().getClient().displayGuiScreen(null);
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
        final int var4 = this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/buggybench.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - 220) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, 220);
    }
}