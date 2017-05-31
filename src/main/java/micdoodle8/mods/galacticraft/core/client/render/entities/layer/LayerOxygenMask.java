package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerOxygenMask implements LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer playerRenderer;
    public ModelRenderer oxygenMask;

    public LayerOxygenMask(RenderPlayer playerRendererIn)
    {
        this.playerRenderer = playerRendererIn;
        float scaleFactor = 1.0F;
        ModelPlayer modelPlayer = playerRendererIn.getMainModel();

        this.oxygenMask = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
        this.oxygenMask.addBox(-8.0F, -16.0F, -8.0F, 16, 16, 16, scaleFactor);
        this.oxygenMask.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float f5, float f6, float partialTicks, float f8, float f2, float f7, float scale)
    {
        if (!player.isInvisible())
        {
            PlayerGearData gearData = GalacticraftCore.proxy.getGearData(player);

            if (gearData != null)
            {
                boolean wearingMask = gearData.getMask() != GCPlayerHandler.GEAR_NOT_PRESENT;
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerGC.oxygenMaskTexture);

                ModelPlayer.copyModelAngles(this.playerRenderer.getMainModel().bipedHeadwear, this.oxygenMask);
                this.oxygenMask.rotationPointY = this.playerRenderer.getMainModel().bipedHeadwear.rotationPointY * 8.0F;

                GlStateManager.pushMatrix();
                GlStateManager.scale(0.5F, 0.5F, 0.5F);

                if (wearingMask)
                {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.05F, 1.05F, 1.05F);
                    this.oxygenMask.render(scale);
                    GL11.glScalef(1F, 1F, 1F);
                    GL11.glPopMatrix();
                }

                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }
}