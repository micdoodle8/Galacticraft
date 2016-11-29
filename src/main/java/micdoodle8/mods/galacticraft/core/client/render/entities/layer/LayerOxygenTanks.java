package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
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

@SideOnly(Side.CLIENT)
public class LayerOxygenTanks implements LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer playerRenderer;
    public ModelRenderer[] greenOxygenTanks = new ModelRenderer[2];
    public ModelRenderer[] orangeOxygenTanks = new ModelRenderer[2];
    public ModelRenderer[] redOxygenTanks = new ModelRenderer[2];

    public LayerOxygenTanks(RenderPlayer playerRendererIn)
    {
        this.playerRenderer = playerRendererIn;
        float scaleFactor = 0.0F;
        ModelPlayer modelPlayer = playerRendererIn.getMainModel();

        this.greenOxygenTanks[0] = new ModelRenderer(modelPlayer, 4, 0).setTextureSize(128, 64);
        this.greenOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
        this.greenOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
        this.greenOxygenTanks[0].mirror = true;
        this.greenOxygenTanks[1] = new ModelRenderer(modelPlayer, 4, 0).setTextureSize(128, 64);
        this.greenOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
        this.greenOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
        this.greenOxygenTanks[1].mirror = true;

        this.orangeOxygenTanks[0] = new ModelRenderer(modelPlayer, 16, 0).setTextureSize(128, 64);
        this.orangeOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
        this.orangeOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
        this.orangeOxygenTanks[0].mirror = true;
        this.orangeOxygenTanks[1] = new ModelRenderer(modelPlayer, 16, 0).setTextureSize(128, 64);
        this.orangeOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
        this.orangeOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
        this.orangeOxygenTanks[1].mirror = true;

        this.redOxygenTanks[0] = new ModelRenderer(modelPlayer, 28, 0).setTextureSize(128, 64);
        this.redOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
        this.redOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
        this.redOxygenTanks[0].mirror = true;
        this.redOxygenTanks[1] = new ModelRenderer(modelPlayer, 28, 0).setTextureSize(128, 64);
        this.redOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
        this.redOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
        this.redOxygenTanks[1].mirror = true;
    }

    public void doRenderLayer(AbstractClientPlayer player, float f5, float f6, float partialTicks, float f8, float f2, float f7, float scale)
    {
        if (!player.isInvisible())
        {
            PlayerGearData gearData = ModelPlayerGC.getGearData(player);

            if (gearData != null)
            {
                boolean wearingLeftTankGreen = gearData.getLeftTank() == 0;
                boolean wearingLeftTankOrange = gearData.getLeftTank() == 1;
                boolean wearingLeftTankRed = gearData.getLeftTank() == 2;
                boolean wearingRightTankGreen = gearData.getRightTank() == 0;
                boolean wearingRightTankOrange = gearData.getRightTank() == 1;
                boolean wearingRightTankRed = gearData.getRightTank() == 2;

                FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerGC.playerTexture);

                ModelPlayer.copyModelAngles(this.playerRenderer.getMainModel().bipedBody, this.greenOxygenTanks[0]);
                ModelPlayer.copyModelAngles(this.playerRenderer.getMainModel().bipedBody, this.greenOxygenTanks[1]);
                ModelPlayer.copyModelAngles(this.playerRenderer.getMainModel().bipedBody, this.orangeOxygenTanks[0]);
                ModelPlayer.copyModelAngles(this.playerRenderer.getMainModel().bipedBody, this.orangeOxygenTanks[1]);
                ModelPlayer.copyModelAngles(this.playerRenderer.getMainModel().bipedBody, this.redOxygenTanks[0]);
                ModelPlayer.copyModelAngles(this.playerRenderer.getMainModel().bipedBody, this.redOxygenTanks[1]);

                if (playerRenderer.getMainModel().isSneak)
                {
                    this.greenOxygenTanks[0].rotationPointY = 2.0F;
                    this.greenOxygenTanks[1].rotationPointY = 2.0F;
                    this.greenOxygenTanks[0].rotationPointZ = 1.6F;
                    this.greenOxygenTanks[1].rotationPointZ = 1.6F;

                    this.orangeOxygenTanks[0].rotationPointY = 2.0F;
                    this.orangeOxygenTanks[1].rotationPointY = 2.0F;
                    this.orangeOxygenTanks[0].rotationPointZ = 1.6F;
                    this.orangeOxygenTanks[1].rotationPointZ = 1.6F;

                    this.redOxygenTanks[0].rotationPointY = 2.0F;
                    this.redOxygenTanks[1].rotationPointY = 2.0F;
                    this.redOxygenTanks[0].rotationPointZ = 1.6F;
                    this.redOxygenTanks[1].rotationPointZ = 1.6F;
                }
                else
                {
                    this.greenOxygenTanks[0].rotationPointY = 0.5F;
                    this.greenOxygenTanks[1].rotationPointY = 0.5F;
                    this.greenOxygenTanks[0].rotationPointZ = 0.5F;
                    this.greenOxygenTanks[1].rotationPointZ = 0.5F;

                    this.orangeOxygenTanks[0].rotationPointY = 0.5F;
                    this.orangeOxygenTanks[1].rotationPointY = 0.5F;
                    this.orangeOxygenTanks[0].rotationPointZ = 0.5F;
                    this.orangeOxygenTanks[1].rotationPointZ = 0.5F;

                    this.redOxygenTanks[0].rotationPointY = 0.5F;
                    this.redOxygenTanks[1].rotationPointY = 0.5F;
                    this.redOxygenTanks[0].rotationPointZ = 0.5F;
                    this.redOxygenTanks[1].rotationPointZ = 0.5F;
                }

                for (int i = 0; i < 2; ++i)
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0.175F, 0.0F, 0.0F);
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
                    GlStateManager.translate(0.0F, 0.0F, 0.2F);

                    if (wearingLeftTankRed)
                    {
                        this.redOxygenTanks[0].render(scale);
                    }

                    if (wearingLeftTankOrange)
                    {
                        this.orangeOxygenTanks[0].render(scale);
                    }

                    if (wearingLeftTankGreen)
                    {
                        this.greenOxygenTanks[0].render(scale);
                    }

                    GlStateManager.popMatrix();

                    GlStateManager.pushMatrix();
                    GlStateManager.translate(-0.175F, 0.0F, 0.0F);
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
                    GlStateManager.translate(0.0F, 0.0F, 0.2F);

                    if (wearingRightTankRed)
                    {
                        this.redOxygenTanks[1].render(scale);
                    }

                    if (wearingRightTankOrange)
                    {
                        this.orangeOxygenTanks[1].render(scale);
                    }

                    if (wearingRightTankGreen)
                    {
                        this.greenOxygenTanks[1].render(scale);
                    }

                    GlStateManager.popMatrix();
                }
            }
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}