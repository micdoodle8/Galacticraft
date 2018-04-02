package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

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
        if (this.rocketModel == null)
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
    protected ResourceLocation getEntityTexture(EntityTier3Rocket entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void doRender(EntityTier3Rocket entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 180;
        GlStateManager.disableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-pitch, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(0.0F, entity.getRenderOffsetY(), 0.0F);
        float rollAmplitude = entity.rollAmplitude / 3 - partialTicks;

        if (rollAmplitude > 0.0F)
        {
            final float i = entity.getLaunched() ? (5 - MathHelper.floor(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
            GlStateManager.rotate(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks, 1.0F, 0.0F, 1.0F);
        }

        this.updateModel();
        this.bindEntityTexture(entity);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		GlStateManager.scale(0.8F, 0.8F, 0.8F);
        ClientUtil.drawBakedModel(this.rocketModel);

        Vector3 teamColor = ClientUtil.updateTeamColor(PlayerUtil.getName(FMLClientHandler.instance().getClient().player), true);

        if (teamColor != null)
        {
            int color = ColorUtil.to32BitColor(255, (int)(teamColor.floatZ() * 255), (int)(teamColor.floatY() * 255), (int)(teamColor.floatX() * 255));
            GlStateManager.disableTexture2D();
            ClientUtil.drawBakedModelColored(coneModel, color);
        }
        else
        {
            ClientUtil.drawBakedModel(coneModel);
            GlStateManager.disableTexture2D();
        }

        GlStateManager.disableLighting();

        boolean red = FMLClientHandler.instance().getClient().player.ticksExisted / 10 % 2 < 1;
        int color = ColorUtil.to32BitColor(255, 0, red ? 0 : 255, red ? 255 : 0);
        ClientUtil.drawBakedModelColored(cubeModel, color);

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();

        GlStateManager.color(1F, 1F, 1F);
        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(EntityTier3Rocket rocket, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = rocket.getEntityBoundingBox().grow(0.5D, 0, 0.5D);
        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
