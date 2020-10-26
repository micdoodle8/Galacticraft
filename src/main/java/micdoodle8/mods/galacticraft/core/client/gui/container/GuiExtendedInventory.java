package micdoodle8.mods.galacticraft.core.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.screen.InventoryTabGalacticraft;
import micdoodle8.mods.galacticraft.core.inventory.ContainerEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.inventory.ContainerExtendedInventory;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class GuiExtendedInventory extends DisplayEffectsScreen<ContainerExtendedInventory>
{
    private static final ResourceLocation inventoryTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/inventory.png");
    private int potionOffsetLast;
    private static float rotation = 0.0F;
    private boolean initWithPotion;

    public GuiExtendedInventory(ContainerExtendedInventory container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
//        super(new ContainerExtendedInventory(playerInv, inventory), playerInv.inventory, new StringTextComponent("Extended Inventory"));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GuiExtendedInventory.drawPlayerOnGui(this.minecraft, 33, 80, 29);
    }

    @Override
    protected void init()
    {
        super.init();

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiLeft += this.getPotionOffset();
        this.potionOffsetLast = this.getPotionOffsetNEI();

        int cornerX = this.guiLeft;
        int cornerY = this.guiTop;

        TabRegistry.updateTabValues(cornerX, cornerY, InventoryTabGalacticraft.class);
        TabRegistry.addTabsToList(this::addButton);

        this.buttons.add(new Button(this.guiLeft + 10, this.guiTop + 71, 7, 7, "", (button) ->
        {
            GuiExtendedInventory.rotation += 10.0F;
        }));
        this.buttons.add(new Button(this.guiLeft + 51, this.guiTop + 71, 7, 7, "", (button) ->
        {
            GuiExtendedInventory.rotation -= 10.0F;
        }));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GuiExtendedInventory.inventoryTexture);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        int newPotionOffset = this.getPotionOffsetNEI();

        if (newPotionOffset < this.potionOffsetLast)
        {
            int diff = newPotionOffset - this.potionOffsetLast;
            this.potionOffsetLast = newPotionOffset;
            this.guiLeft += diff;

//            for (int k = 0; k < this.buttons.size(); ++k)
//            {
//                Widget widget = this.buttons.get(k);
//
//                if (!(widget instanceof AbstractTab))
//                {
//                    widget.x += diff;
//                }
//            }
        }
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    public static void drawPlayerOnGui(Minecraft minecraft, int x, int y, int scale)
    {
        ClientPlayerEntity player = minecraft.player;
        RenderSystem.pushMatrix();
//        RenderSystem.translatef((float)x, (float)y, 1050.0F);
//        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(x, y, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)-scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
//        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
//        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        float f2 = player.renderYawOffset;
        float f3 = player.rotationYaw;
        float f4 = player.rotationPitch;
        float f5 = player.prevRotationYawHead;
        float f6 = player.rotationYawHead;
        player.renderYawOffset = GuiExtendedInventory.rotation;
        player.rotationPitch = (float) Math.sin(Util.milliTime() / 500.0F) * 3.0F;
        player.rotationYaw = GuiExtendedInventory.rotation;
        player.prevRotationYawHead = player.rotationYaw;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
//        quaternion1.conjugate();
//        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        entityrenderermanager.renderEntityStatic(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        player.renderYawOffset = f2;
        player.rotationYaw = f3;
        player.rotationPitch = f4;
        player.prevRotationYawHead = f5;
        player.rotationYawHead = f6;
        RenderSystem.popMatrix();
    }

    //Instanced method of this to have the instance field initWithPotion
    public int getPotionOffset()
    {
        /*Disabled in 1.12.2 because a vanilla bug means potion offsets are currently not a thing
         *The vanilla bug is that GuiInventory.init() resets GuiLeft to the recipe book version of GuiLeft,
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
//        if (this.initWithPotion && TabRegistry.clazzNEIConfig != null)
//        {
//            try
//            {
//                // Check whether NEI is hidden and enabled
//                Object hidden = TabRegistry.clazzNEIConfig.getMethod("isHidden").invoke(null);
//                Object enabled = TabRegistry.clazzNEIConfig.getMethod("isEnabled").invoke(null);
//
//                if (hidden instanceof Boolean && enabled instanceof Boolean)
//                {
//                    if ((Boolean) hidden || !((Boolean) enabled))
//                    {
//                        // If NEI is disabled or hidden, offset the tabs by the standard 60
//                        return 0;
//                    }
//                    //Active NEI undoes the standard potion offset
//                    return -60;
//                }
//            }
//            catch (Exception e)
//            {
//            }
//        } TODO Inv tabs
        //No NEI, no change
        return 0;
    }
}
