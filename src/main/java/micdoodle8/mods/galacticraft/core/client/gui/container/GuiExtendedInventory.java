package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.screen.InventoryTabGalacticraft;
import micdoodle8.mods.galacticraft.core.inventory.ContainerExtendedInventory;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiExtendedInventory extends InventoryEffectRenderer
{
    private static final ResourceLocation inventoryTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/inventory.png");

    private float xSize_lo_2;
    private float ySize_lo_2;

    private int potionOffsetLast;
    private static float rotation = 0.0F;

    private boolean initWithPotion;

    public GuiExtendedInventory(EntityPlayer entityPlayer, InventoryExtended inventory)
    {
        super(new ContainerExtendedInventory(entityPlayer, inventory));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GuiExtendedInventory.drawPlayerOnGui(this.mc, 33, 60, 29, 51 - this.xSize_lo_2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiLeft += this.getPotionOffset();
        this.potionOffsetLast = this.getPotionOffsetNEI();

        int cornerX = this.guiLeft;
        int cornerY = this.guiTop;

        TabRegistry.updateTabValues(cornerX, cornerY, InventoryTabGalacticraft.class);
        TabRegistry.addTabsToList(this.buttonList);

        this.buttonList.add(new GuiButton(0, this.guiLeft + 10, this.guiTop + 71, 7, 7, ""));
        this.buttonList.add(new GuiButton(1, this.guiLeft + 51, this.guiTop + 71, 7, 7, ""));
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
            GuiExtendedInventory.rotation += 10.0F;
            break;
        case 1:
            GuiExtendedInventory.rotation -= 10.0F;
            break;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiExtendedInventory.inventoryTexture);
        final int k = this.guiLeft;
        final int l = this.guiTop;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        int newPotionOffset = this.getPotionOffsetNEI();
        if (newPotionOffset < this.potionOffsetLast)
        {
            int diff = newPotionOffset - this.potionOffsetLast;
            this.potionOffsetLast = newPotionOffset;
            this.guiLeft += diff;
            for (int k = 0; k < this.buttonList.size(); ++k)
            {
                GuiButton b = (GuiButton) this.buttonList.get(k);
                if (!(b instanceof AbstractTab))
                {
                    b.x += diff;
                }
            }
        }
        super.drawScreen(par1, par2, par3);
        this.xSize_lo_2 = par1;
        this.ySize_lo_2 = par2;
    }

    public static void drawPlayerOnGui(Minecraft mc, int x, int y, int scale, float mouseX)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = mc.player.renderYawOffset;
        float f3 = mc.player.rotationYaw;
        float f4 = mc.player.rotationPitch;
        float f5 = mc.player.rotationYawHead;
        mouseX -= 19;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        mc.player.renderYawOffset = GuiExtendedInventory.rotation;
        mc.player.rotationYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
        mc.player.rotationYaw = GuiExtendedInventory.rotation;
        mc.player.rotationYawHead = mc.player.rotationYaw;
        mc.player.rotationPitch = (float) Math.sin(mc.getSystemTime() / 500.0F) * 3.0F;
        GlStateManager.translate(0.0F, (float) mc.player.getYOffset(), 0.0F);
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(mc.player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        mc.player.renderYawOffset = f2;
        mc.player.rotationYaw = f3;
        mc.player.rotationPitch = f4;
        mc.player.rotationYawHead = f5;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    //Instanced method of this to have the instance field initWithPotion
    public int getPotionOffset()
    {
        // If at least one potion is active...
        if (!Minecraft.getMinecraft().player.getActivePotionEffects().isEmpty())
        {
            this.initWithPotion = true;
            return 60 + TabRegistry.getPotionOffsetJEI() + getPotionOffsetNEI();
        }

        // No potions, no offset needed
        this.initWithPotion = false;
        return 0;
    }

    //Instanced method of this to use the instance field initWithPotion
    public int getPotionOffsetNEI()
    {
        if (initWithPotion && TabRegistry.clazzNEIConfig != null)
        {
            try
            {
                // Check whether NEI is hidden and enabled
                Object hidden = TabRegistry.clazzNEIConfig.getMethod("isHidden").invoke(null);
                Object enabled = TabRegistry.clazzNEIConfig.getMethod("isEnabled").invoke(null);
                if (hidden instanceof Boolean && enabled instanceof Boolean)
                {
                    if ((Boolean) hidden || !((Boolean) enabled))
                    {
                        // If NEI is disabled or hidden, offset the tabs by the standard 60
                        return 0;
                    }
                    //Active NEI undoes the standard potion offset
                    return -60;
                }
            }
            catch (Exception e)
            {
            }
        }
        //No NEI, no change
        return 0;
    }
}
