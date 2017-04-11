package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDish;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.FMLClientHandler;

public class TileEntityDishRenderer extends TileEntitySpecialRenderer<TileEntityDish>
{
    private static final ResourceLocation textureSupport = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/telesupport.png");
    private static final ResourceLocation textureFork = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/telefork.png");
    private static final ResourceLocation textureDish = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/teledish.png");
    private static OBJModel.OBJBakedModel modelSupport;
    private static OBJModel.OBJBakedModel modelFork;
    private static OBJModel.OBJBakedModel modelDish;
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;

    private void updateModels()
    {
        if (modelDish == null)
        {
            try
            {
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                OBJModel teleDish = (OBJModel) ModelLoaderRegistry.getModel(new ResourceLocation(Constants.ASSET_PREFIX, "teledish.obj"));
                OBJModel teleFork = (OBJModel) ModelLoaderRegistry.getModel(new ResourceLocation(Constants.ASSET_PREFIX, "telefork.obj"));
                OBJModel teleSupp = (OBJModel) ModelLoaderRegistry.getModel(new ResourceLocation(Constants.ASSET_PREFIX, "telesupport.obj"));
                teleDish = (OBJModel) teleDish.process(ImmutableMap.of("flip-v", "true"));
                teleFork = (OBJModel) teleFork.process(ImmutableMap.of("flip-v", "true"));
                teleSupp = (OBJModel) teleSupp.process(ImmutableMap.of("flip-v", "true"));
                modelDish = (OBJModel.OBJBakedModel) teleDish.bake(new OBJModel.OBJState(ImmutableList.of("main"), false), DefaultVertexFormats.ITEM, spriteFunction);
                modelFork = (OBJModel.OBJBakedModel) teleFork.bake(new OBJModel.OBJState(ImmutableList.of("main"), false), DefaultVertexFormats.ITEM, spriteFunction);
                modelSupport = (OBJModel.OBJBakedModel) teleSupp.bake(new OBJModel.OBJState(ImmutableList.of("main"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void renderTileEntityAt(TileEntityDish tile, double par2, double par4, double par6, float partialTickTime, int par9)
    {
        this.updateModels();
        TileEntityDish dish = (TileEntityDish) tile;
        float time = (dish.ticks + partialTickTime) % 1440F;
        final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glTranslatef(0.5F, 1.0F, 0.5F);
        GL11.glScalef(1.6F, 1.25F, 1.6F);

        this.renderEngine.bindTexture(textureSupport);
        ClientUtil.drawBakedModel(modelSupport);
        GL11.glScalef(1.25F, 1.6F, 1.25F);
        GL11.glRotatef(time / 4, 0, -1, 0);
        this.renderEngine.bindTexture(textureFork);
        ClientUtil.drawBakedModel(modelFork);

//        float celestialAngle = (dish.getWorldObj().getCelestialAngle(1.0F) - 0.784690560F) * 360.0F;
//        float celestialAngle2 = dish.getWorldObj().getCelestialAngle(1.0F) * 360.0F;

        GL11.glTranslatef(0.0F, 2.3F, 0.0F);
        GL11.glRotatef((MathHelper.sin(time / 144) + 1.0F) * 22.5F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.0F, -2.3F, 0.0F);

        this.renderEngine.bindTexture(textureDish);
        ClientUtil.drawBakedModel(modelDish);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
