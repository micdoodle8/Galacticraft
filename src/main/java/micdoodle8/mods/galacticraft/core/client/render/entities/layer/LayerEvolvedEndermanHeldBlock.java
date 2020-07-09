package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedEnderman;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerEvolvedEndermanHeldBlock extends LayerRenderer<EntityEvolvedEnderman, ModelEvolvedEnderman>
{
    public LayerEvolvedEndermanHeldBlock(RenderEvolvedEnderman render)
    {
        super(render);
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }

    @Override
    public void render(EntityEvolvedEnderman entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        BlockState blockstate = entity.getHeldBlockState();
        if (blockstate != null)
        {
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 0.6875F, -0.75F);
            GlStateManager.rotatef(20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(0.25F, 0.1875F, 0.25F);
            float f = 0.5F;
            GlStateManager.scalef(-0.5F, -0.5F, 0.5F);
            int i = entity.getBrightnessForRender();
            int j = i % 65536;
            int k = i / 65536;
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) j, (float) k);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getInstance().getBlockRendererDispatcher().renderBlockBrightness(blockstate, 1.0F);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
        }
    }
}