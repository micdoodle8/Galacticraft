package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.systems.RenderSystem;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OverlayRocket extends Overlay
{
    /**
     * Render the GUI when player is in inventory
     */
    public static void renderSpaceshipOverlay()
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.getRidingEntity() instanceof EntitySpaceshipBase)
        {
            ResourceLocation guiTexture = ((EntitySpaceshipBase) mc.player.getRidingEntity()).getSpaceshipGui();
            if (guiTexture == null)
            {
                return;
            }
            int height = (int) (mc.mouseHelper.getMouseY() * (double) mc.getMainWindow().getScaledHeight() / (double) mc.getMainWindow().getHeight());
//            mc.entityRenderer.setupOverlayRendering();
            RenderSystem.depthMask(true);
            RenderSystem.enableTexture();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.textureManager.bindTexture(guiTexture);

            float var1 = 0F;
            float var2 = height / 2 - 170 / 2;
            float var3 = 0.0F;
            float var3b = 0.0F;
            float var4 = 0.0F;
            float var5 = 1.0F;
            float var6 = 1.0F;
            float var7 = 1.0F;
            float var8 = 1.0F;
            float sizeScale = 0.65F;

            final Tessellator tess = Tessellator.getInstance();
            BufferBuilder worldRenderer = tess.getBuffer();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(var1 + 0, var2 + 242.0F * sizeScale, 0.0).tex((var3 + 0) * var7, (var4 + var6) * var8).endVertex();
            worldRenderer.pos(var1 + 20.0F * sizeScale, var2 + 242.0F * sizeScale, 0.0).tex((var3 + var5) * var7, (var4 + var6) * var8).endVertex();
            worldRenderer.pos(var1 + 20.0F * sizeScale, var2 + 0, 0.0).tex((var3 + var5) * var7, (var4 + 0) * var8).endVertex();
            worldRenderer.pos(var1 + 0, var2 + 0, 0.0).tex((var3 + 0) * var7, (var4 + 0) * var8).endVertex();
            tess.draw();

            RenderSystem.color3f(1.0F, 1.0F, 1.0F);

            Entity rocket = mc.player.getRidingEntity();
            float headOffset = 0;
            if (rocket instanceof EntityTier1Rocket)
            {
                headOffset = 5F;
            }
            EntityRenderer<EntitySpaceshipBase> spaceshipRender = (EntityRenderer<EntitySpaceshipBase>) mc.getRenderManager().renderers.get(rocket.getClass());

            final int y1 = height / 2 + 60 - (int) Math.floor(Overlay.getPlayerPositionY(mc.player) / 10.5F);
            var1 = 2.5F;
            var2 = y1;
            var3 = 8;
            var3b = 40;
            var4 = 8;
            var5 = 8;
            var6 = 8;
            var7 = 1.0F / 64.0F;
            var8 = 1.0F / 64.0F;

            RenderSystem.pushMatrix();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableColorMaterial();
            RenderSystem.translatef(var1 + 4, var2 + 6, 50F);
            RenderSystem.scalef(5F, 5F, 5F);
            RenderSystem.rotatef(180F, 1, 0, 0);
            RenderSystem.rotatef(90F, 0, 1, 0);

            try
            {
                //spaceshipRender.render((EntitySpaceshipBase) mc.player.getRidingEntity(), 0, 0, 0, 0, 0);TODO
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            RenderSystem.popMatrix();

            mc.textureManager.bindTexture(ClientProxyCore.playerHead);

            RenderSystem.disableLighting();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(770, 771);
            RenderSystem.translatef(0F, -12F + headOffset, 60F);

            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(var1 + 0, var2 + var6, 0.0).tex((var3 + 0) * var7, (var4 + var6) * var8).endVertex();
            worldRenderer.pos(var1 + var5, var2 + var6, 0.0).tex((var3 + var5) * var7, (var4 + var6) * var8).endVertex();
            worldRenderer.pos(var1 + var5, var2 + 0, 0.0).tex((var3 + var5) * var7, (var4 + 0) * var8).endVertex();
            worldRenderer.pos(var1 + 0, var2 + 0, 0.0).tex((var3 + 0) * var7, (var4 + 0) * var8).endVertex();
            tess.draw();

            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(var1 + 0, var2 + var6, 0.0).tex((var3b + 0) * var7, (var4 + var6) * var8).endVertex();
            worldRenderer.pos(var1 + var5, var2 + var6, 0.0).tex((var3b + var5) * var7, (var4 + var6) * var8).endVertex();
            worldRenderer.pos(var1 + var5, var2 + 0, 0.0).tex((var3b + var5) * var7, (var4 + 0) * var8).endVertex();
            worldRenderer.pos(var1 + 0, var2 + 0, 0.0).tex((var3b + 0) * var7, (var4 + 0) * var8).endVertex();
            tess.draw();

            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableLighting();
            RenderSystem.disableBlend();
        }

    }
}
