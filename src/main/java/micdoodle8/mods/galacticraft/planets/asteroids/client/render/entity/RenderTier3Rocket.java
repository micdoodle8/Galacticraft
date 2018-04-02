package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityTier3Rocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class RenderTier3Rocket extends Render<EntityTier3Rocket>
{
    private OBJModel.OBJBakedModel rocketModel;
    private OBJModel.OBJBakedModel coneModel;
    private OBJModel.OBJBakedModel cubeModel;

    public RenderTier3Rocket(RenderManager manager)
    {
        super(manager);
        this.shadowSize = 2F;
    }

    private void updateModel()
    {
        if (rocketModel == null)
        {
            try
            {
                IModel model = OBJLoaderGC.instance.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "tier3rocket.obj"));
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                this.rocketModel = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Boosters", "Rocket"), false), DefaultVertexFormats.ITEM, spriteFunction);
                this.coneModel = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("NoseCone"), false), DefaultVertexFormats.ITEM, spriteFunction);
                this.cubeModel = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Cube"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
//            Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
//            {
//                @Override
//                public TextureAtlasSprite apply(ResourceLocation input)
//                {
//                    return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
//                }
//            };
//
//            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "rocket_t3", "inventory");
//            rocketModel = (ItemModelRocketT3) FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().getModelManager().getModel(modelResourceLocation);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTier3Rocket par1Entity)
    {
        return new ResourceLocation("missing");
    }

    @Override
    public void doRender(EntityTier3Rocket entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9 + 180;

        GL11.glTranslatef((float) par2, (float) par4 + entity.getRenderOffsetY(), (float) par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        final float var28 = entity.rollAmplitude / 3 - par9;

        if (var28 > 0.0F)
        {
            final float i = entity.getLaunched() ? (5 - MathHelper.floor_double(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
            GL11.glRotatef(MathHelper.sin(var28) * var28 * i * par9, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(MathHelper.sin(var28) * var28 * i * par9, 1.0F, 0.0F, 1.0F);
        }

        updateModel();

        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.locationBlocksTexture);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glScalef(0.8F, 0.8F, 0.8F);

        ClientUtil.drawBakedModel(rocketModel);

        GlStateManager.disableTexture2D();

        Vector3 teamColor = ClientUtil.updateTeamColor(FMLClientHandler.instance().getClient().thePlayer.getName(), true);
        if (teamColor != null)
        {
            int color = ColorUtil.to32BitColor(255, (int)(teamColor.floatZ() * 255), (int)(teamColor.floatY() * 255), (int)(teamColor.floatX() * 255));
            ClientUtil.drawBakedModelColored(coneModel, color);
        }
        else
        {
            ClientUtil.drawBakedModel(coneModel);
        }

        GlStateManager.disableLighting();

        boolean red = FMLClientHandler.instance().getClient().thePlayer.ticksExisted / 10 % 2 < 1;
        int color = ColorUtil.to32BitColor(255, 0, red ? 0 : 255, red ? 255 : 0);
        ClientUtil.drawBakedModelColored(cubeModel, color);

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();

        GlStateManager.color(1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();

        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(EntityTier3Rocket rocket, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = rocket.getEntityBoundingBox().expand(0.5D, 0, 0.5D);

        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
