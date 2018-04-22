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
    private int potionOffsetLast;
    private static float rotation = 0.0F;
    private boolean initWithPotion;

    public GuiExtendedInventory(EntityPlayer player, InventoryExtended inventory)
    {
        super(new ContainerExtendedInventory(player, inventory));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GuiExtendedInventory.drawPlayerOnGui(this.mc, 33, 60, 29);
    }

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
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
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
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiExtendedInventory.inventoryTexture);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        int newPotionOffset = this.getPotionOffsetNEI();

        if (newPotionOffset < this.potionOffsetLast)
        {
            int diff = newPotionOffset - this.potionOffsetLast;
            this.potionOffsetLast = newPotionOffset;
            this.guiLeft += diff;

            for (int k = 0; k < this.buttonList.size(); ++k)
            {
                GuiButton button = this.buttonList.get(k);

                if (!(button instanceof AbstractTab))
                {
                    button.x += diff;
                }
            }
        }
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    public static void drawPlayerOnGui(Minecraft mc, int x, int y, int scale)
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
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        mc.player.renderYawOffset = GuiExtendedInventory.rotation;
        mc.player.rotationYaw = (float) Math.atan(32 / 40.0F) * 40.0F;
        mc.player.rotationYaw = GuiExtendedInventory.rotation;
        mc.player.rotationYawHead = mc.player.rotationYaw;
        mc.player.rotationPitch = (float) Math.sin(Minecraft.getSystemTime() / 500.0F) * 3.0F;
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
        /*Disabled in 1.12.2 because a vanilla bug means potion offsets are currently not a thing
         *The vanilla bug is that GuiInventory.initGui() resets GuiLeft to the recipe book version of GuiLeft,
         *and in GuiRecipeBook.updateScreenPosition() it takes no account of potion offset even if the recipe book is inactive.

        // If at least one potion is active...
        if (this.hasActivePotionEffects)
        {
            this.initWithPotion = true;
            return 60 + TabRegistry.getPotionOffsetJEI() + getPotionOffsetNEI();
        }
         */
        // No potions, no offset needed
        this.initWithPotion = false;
        return 0;
    }

    //Instanced method of this to use the instance field initWithPotion
    public int getPotionOffsetNEI()
    {
        if (this.initWithPotion && TabRegistry.clazzNEIConfig != null)
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
