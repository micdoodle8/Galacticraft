package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerFrequencyModule implements LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer playerRenderer;
    private OBJModel.OBJBakedModel moduleModel;
    private OBJModel.OBJBakedModel radarModel;

    public LayerFrequencyModule(RenderPlayer playerRendererIn)
    {
        this.playerRenderer = playerRendererIn;
    }

    private void updateModels()
    {
        if (this.moduleModel == null)
        {
            try
            {
                IModel model = OBJLoaderGC.instance.loadModel(new ResourceLocation(Constants.ASSET_PREFIX, "frequency_module.obj"));
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                this.moduleModel = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Main"), false), DefaultVertexFormats.ITEM, spriteFunction);
                this.radarModel = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Radar"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float f5, float f6, float partialTicks, float f8, float f2, float f7, float scale)
    {
        if (!player.isInvisible())
        {
            PlayerGearData gearData = GalacticraftCore.proxy.getGearData(player);

            if (gearData != null)
            {
                boolean wearingModule = gearData.getFrequencyModule() != GCPlayerHandler.GEAR_NOT_PRESENT;
                boolean wearingHelmet = gearData.getMask() != GCPlayerHandler.GEAR_NOT_PRESENT;
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerGC.playerTexture);

                if (wearingModule)
                {
                    this.updateModels();
                    GlStateManager.enableRescaleNormal();
                    GlStateManager.pushMatrix();
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                    GlStateManager.rotate(180, 1, 0, 0);
                    GlStateManager.pushMatrix();
                    GlStateManager.rotate((float) (this.playerRenderer.getMainModel().bipedHeadwear.rotateAngleY * -Constants.RADIANS_TO_DEGREES), 0, 1, 0);
                    GlStateManager.rotate((float) (this.playerRenderer.getMainModel().bipedHeadwear.rotateAngleX * Constants.RADIANS_TO_DEGREES), 1, 0, 0);
                    GlStateManager.scale(0.3F, 0.3F, 0.3F);

                    if (wearingHelmet)
                    {
                        GlStateManager.translate(-1.1F, player.isSneaking() ? 0.35F : 1.2F, 0);
                    }
                    else
                    {
                        GlStateManager.translate(-0.9F, player.isSneaking() ? 0.1F : 0.9F, 0);
                    }

                    ClientUtil.drawBakedModel(this.moduleModel);
                    GlStateManager.translate(0.0F, 1.3F, 0.0F);
                    GlStateManager.rotate((float) (Math.sin(player.ticksExisted * 0.05) * 50.0F), 1, 0, 0);
                    GlStateManager.rotate((float) (Math.cos(player.ticksExisted * 0.1) * 50.0F), 0, 1, 0);
                    ClientUtil.drawBakedModel(this.radarModel);
                    GlStateManager.popMatrix();
                    GlStateManager.color(1.0F, 1.0F, 1.0F);
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }
}
