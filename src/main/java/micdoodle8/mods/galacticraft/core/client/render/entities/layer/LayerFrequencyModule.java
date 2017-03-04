package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

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
                OBJModel model = (OBJModel) ModelLoaderRegistry.getModel(new ResourceLocation(Constants.ASSET_PREFIX, "frequencyModule.obj"));
                model = (OBJModel) model.process(ImmutableMap.of("flip-v", "true"));

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
            PlayerGearData gearData = ModelPlayerGC.getGearData(player);

            if (gearData != null)
            {
                boolean wearingModule = gearData.getFrequencyModule() != -1;
                boolean wearingHelmet = gearData.getMask() != -1;
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerGC.playerTexture);

                if (wearingModule)
                {
                    this.updateModels();
                    GlStateManager.pushMatrix();
                    RenderHelper.disableStandardItemLighting();
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                    if (Minecraft.isAmbientOcclusionEnabled())
                    {
                        GlStateManager.shadeModel(GL11.GL_SMOOTH);
                    }
                    else
                    {
                        GlStateManager.shadeModel(GL11.GL_FLAT);
                    }

                    GlStateManager.rotate(180, 1, 0, 0);
                    GlStateManager.pushMatrix();
                    GlStateManager.rotate((float) (this.playerRenderer.getMainModel().bipedHeadwear.rotateAngleY * (-180.0F / Math.PI)), 0, 1, 0);
                    GlStateManager.rotate((float) (this.playerRenderer.getMainModel().bipedHeadwear.rotateAngleX * (180.0F / Math.PI)), 1, 0, 0);
                    GlStateManager.scale(0.3F, 0.3F, 0.3F);

                    if (wearingHelmet)
                    {
                        GlStateManager.translate(-1.1F, 1.2F, 0);
                    }
                    else
                    {
                        GlStateManager.translate(-0.9F, 0.9F, 0);
                    }

                    ClientUtil.drawBakedModel(this.moduleModel);
                    GlStateManager.translate(0.0F, 1.3F, 0.0F);
                    GlStateManager.rotate((float) (Math.sin(player.ticksExisted * 0.05) * 50.0F), 1, 0, 0);
                    GlStateManager.rotate((float) (Math.cos(player.ticksExisted * 0.1) * 50.0F), 0, 1, 0);
                    ClientUtil.drawBakedModel(this.radarModel);
                    GlStateManager.popMatrix();
                    RenderHelper.enableStandardItemLighting();
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
