package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedWitch;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedWitch;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedWitch;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerHeldItemEvolvedWitch extends LayerRenderer<EntityEvolvedWitch, ModelEvolvedWitch>
{
    private RenderEvolvedWitch witchRenderer;

    public LayerHeldItemEvolvedWitch(RenderEvolvedWitch witchRenderer)
    {
        super(witchRenderer);
        this.witchRenderer = witchRenderer;
    }

    @Override
    public void render(EntityEvolvedWitch entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        ItemStack itemstack = entity.getHeldItemMainhand();

        if (!itemstack.isEmpty())
        {
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            GlStateManager.pushMatrix();

            if (this.witchRenderer.getEntityModel().isChild)
            {
                GlStateManager.translatef(0.0F, 0.625F, 0.0F);
                GlStateManager.rotatef(-20.0F, -1.0F, 0.0F, 0.0F);
                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            }

            ((ModelEvolvedWitch)this.witchRenderer.getEntityModel()).func_205073_b().postRender(0.0625F);
            GlStateManager.translatef(-0.0625F, 0.53125F, 0.21875F);
            Item item = itemstack.getItem();
            if (Block.getBlockFromItem(item).getDefaultState().getRenderType() == BlockRenderType.ENTITYBLOCK_ANIMATED) {
                GlStateManager.translatef(0.0F, 0.0625F, -0.25F);
                GlStateManager.rotatef(30.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-5.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.scalef(0.375F, -0.375F, 0.375F);
            } else if (item instanceof net.minecraft.item.BowItem) {
                GlStateManager.translatef(0.0F, 0.125F, -0.125F);
                GlStateManager.rotatef(-45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.scalef(0.625F, -0.625F, 0.625F);
                GlStateManager.rotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-20.0F, 0.0F, 1.0F, 0.0F);
            } else {
                GlStateManager.translatef(0.1875F, 0.1875F, 0.0F);
                GlStateManager.scalef(0.875F, 0.875F, 0.875F);
                GlStateManager.rotatef(-20.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotatef(-60.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-30.0F, 0.0F, 0.0F, 1.0F);
            }
            GlStateManager.rotatef(-15.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(40.0F, 0.0F, 0.0F, 1.0F);
            Minecraft.getInstance().getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}