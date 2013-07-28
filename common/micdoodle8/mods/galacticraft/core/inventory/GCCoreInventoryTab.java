package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GCCoreInventoryTab extends GuiButton
{
    int xOffset;
    ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    ItemStack renderStack;
    RenderItem itemRenderer = new RenderItem();

    public GCCoreInventoryTab(int id, int posX, int posY, ItemStack renderStack, int xOffset)
    {
        super(id, posX, posY, 28, 32, "");
        this.xOffset = xOffset;
        this.renderStack = renderStack;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.drawButton)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.func_110577_a(this.texture);

            int yTexPos = this.enabled ? 0 : 32;
            int ySize = this.enabled ? 28 : 32;

            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.xOffset * 28, yTexPos, 28, ySize);

            RenderHelper.enableGUIStandardItemLighting();
            this.zLevel = 100.0F;
            this.itemRenderer.zLevel = 100.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            this.itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, this.renderStack, this.xPosition + 6, this.yPosition + 8);
            this.itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, this.renderStack, this.xPosition + 6, this.yPosition + 8);
            GL11.glDisable(GL11.GL_LIGHTING);
            this.itemRenderer.zLevel = 0.0F;
            this.zLevel = 0.0F;
            RenderHelper.disableStandardItemLighting();
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        boolean inWindow = this.enabled && this.drawButton && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

        if (inWindow)
        {
            this.onTabClicked();
        }

        return inWindow;
    }

    public abstract void onTabClicked();

    public static class GCCoreInventoryTabPlayer extends GCCoreInventoryTab
    {
        public GCCoreInventoryTabPlayer(int id, int posX, int posY, int xOffset)
        {
            super(id, posX, posY, new ItemStack(Block.workbench), xOffset);
        }

        @Override
        public void onTabClicked()
        {
            FMLClientHandler.instance().getClient().displayGuiScreen(new GuiInventory(FMLClientHandler.instance().getClient().thePlayer));
            ClientProxyCore.addTabsToInventory((GuiContainer) FMLClientHandler.instance().getClient().currentScreen);
        }
    }

    public static class GCCoreInventoryTabExtended extends GCCoreInventoryTab
    {
        public GCCoreInventoryTabExtended(int id, int posX, int posY, int xOffset)
        {
            super(id, posX, posY, new ItemStack(GCCoreItems.oxygenMask), xOffset);
        }

        @Override
        public void onTabClicked()
        {
            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 23, new Object[] {}));
        }
    }
}
