package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedWitch;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedWitch;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedWitch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerHeldItemEvolvedWitch implements LayerRenderer<EntityEvolvedWitch>
{
    private RenderEvolvedWitch witchRenderer;

    public LayerHeldItemEvolvedWitch(RenderEvolvedWitch witchRenderer)
    {
        this.witchRenderer = witchRenderer;
    }

    @Override
    public void doRenderLayer(EntityEvolvedWitch entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        ItemStack itemstack = entity.getHeldItemMainhand();

        if (!itemstack.isEmpty())
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            GlStateManager.pushMatrix();

            if (this.witchRenderer.getMainModel().isChild)
            {
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }

            ((ModelEvolvedWitch)this.witchRenderer.getMainModel()).villagerNose.postRender(0.0625F);
            GlStateManager.translate(-0.0625F, 0.53125F, 0.21875F);
            Item item = itemstack.getItem();
            Minecraft mc = Minecraft.getMinecraft();

//            if (item instanceof ItemBlock && mc.getBlockRendererDispatcher().isRenderTypeChest(Block.getBlockFromItem(item), itemstack.getMetadata()))
//            {
//                GlStateManager.translate(0.0F, 0.0625F, -0.25F);
//                GlStateManager.rotate(30.0F, 1.0F, 0.0F, 0.0F);
//                GlStateManager.rotate(-5.0F, 0.0F, 1.0F, 0.0F);
//                GlStateManager.scale(0.375F, -0.375F, 0.375F);
//            }
            if (item == Items.BOW)
            {
                GlStateManager.translate(0.0F, 0.125F, -0.125F);
                GlStateManager.rotate(-45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.scale(0.625F, -0.625F, 0.625F);
                GlStateManager.rotate(-100.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-20.0F, 0.0F, 1.0F, 0.0F);
            }
            else if (item.isFull3D())
            {
                if (item.shouldRotateAroundWhenRendering())
                {
                    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.translate(0.0F, -0.0625F, 0.0F);
                }

                this.witchRenderer.transformHeldFull3DItemLayer();
                GlStateManager.translate(0.0625F, -0.125F, 0.0F);
                GlStateManager.scale(0.625F, -0.625F, 0.625F);
                GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                GlStateManager.translate(0.1875F, 0.1875F, 0.0F);
                GlStateManager.scale(0.875F, 0.875F, 0.875F);
                GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(-60.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-30.0F, 0.0F, 0.0F, 1.0F);
            }
            GlStateManager.rotate(-15.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(40.0F, 0.0F, 0.0F, 1.0F);
            mc.getItemRenderer().renderItem(entity, itemstack, TransformType.THIRD_PERSON_RIGHT_HAND);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}