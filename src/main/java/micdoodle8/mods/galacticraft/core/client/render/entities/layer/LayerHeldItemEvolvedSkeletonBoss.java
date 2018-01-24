package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSkeletonBoss;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedSkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerHeldItemEvolvedSkeletonBoss implements LayerRenderer<EntitySkeletonBoss>
{
    private final RenderEvolvedSkeletonBoss renderer;

    public LayerHeldItemEvolvedSkeletonBoss(RenderEvolvedSkeletonBoss renderer)
    {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntitySkeletonBoss entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (entity.throwTimer + entity.postThrowDelay == 0)
        {
            this.renderHeldItem(entity, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
            this.renderHeldItem(entity, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }

    private void renderHeldItem(EntitySkeletonBoss entity, ItemCameraTransforms.TransformType type)
    {
        ItemStack bow = new ItemStack(Items.BOW);

        if (bow != null)
        {
            GlStateManager.pushMatrix();
            ((ModelEvolvedSkeletonBoss)this.renderer.getMainModel()).postRenderArm(0.0625F, type);

            if (type == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
            {
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.05F, 0.125F, -1.525F);
            }
            else
            {
                GlStateManager.translate(0.025F, 1.525F, -0.125F);
            }

            Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, bow, type, false);
            GlStateManager.popMatrix();
        }
    }
}