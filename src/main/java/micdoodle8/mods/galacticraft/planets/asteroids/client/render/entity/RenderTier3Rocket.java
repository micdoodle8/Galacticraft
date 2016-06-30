package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import micdoodle8.mods.galacticraft.core.client.model.loader.IModelCustom;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderTier3Rocket extends Render
{
    private static Function<ResourceLocation, TextureAtlasSprite> TEXTUREGETTER;
    private ResourceLocation rocketTexture;
    private IFlexibleBakedModel rocketModel;

    protected IModelCustom rocketModelObj;


    public static class RocketTexture extends TextureAtlasSprite
    {
        public static ResourceLocation loc = new ResourceLocation("galacticraftplanets:model/tier3rocket");
        public static RocketTexture instance = new RocketTexture();

        protected RocketTexture()
        {
            super(loc.toString());
        }

        @Override
        public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location)
        {
            return true;
        }

        @Override
        public boolean load(IResourceManager manager, ResourceLocation location)
        {
            BufferedImage image = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setBackground(Color.WHITE);
            graphics.clearRect(0, 0, 16, 16);
            BufferedImage[] images = new BufferedImage[Minecraft.getMinecraft().gameSettings.mipmapLevels + 1];
            images[0] = image;
            try
            {
                loadSprite(images, null);
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
            return false;
        }

        public void register(TextureMap map)
        {
            map.setTextureEntry(RocketTexture.loc.toString(), RocketTexture.instance);
        }
    }

    public RenderTier3Rocket(IModelCustom spaceshipModel, String textureDomain, String texture)
    {
        super(FMLClientHandler.instance().getClient().getRenderManager());
        this.rocketModelObj = spaceshipModel;
        this.rocketTexture = new ResourceLocation(textureDomain, "textures/model/" + texture + ".png");
        this.shadowSize = 2F;

        TEXTUREGETTER = new Function<ResourceLocation, TextureAtlasSprite>() {
            @Override
            public TextureAtlasSprite apply(ResourceLocation input) {
                if (input.getResourceDomain().equals(GalacticraftPlanets.ASSET_PREFIX))
                {
                    return RocketTexture.instance;
                }
                return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
            }
        };

        try
        {
            OBJModel model = (OBJModel) OBJLoader.instance.loadModel(new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "models/tier3rocket.obj"));

            IModel rocketmodel = ((OBJModel) model.retexture(ImmutableMap.of("#tier3rocket", "galacticraftplanets:model/tier3rocket"))).process(ImmutableMap.of("flip-v", "true"));

            rocketModel = rocketmodel.bake(new OBJModel.OBJState(ImmutableList.of("Rocket", "Cube", "Boosters", "NoseCone"), false), Attributes.DEFAULT_BAKED_FORMAT, TEXTUREGETTER);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.rocketTexture;
    }

    public void renderSpaceship(EntitySpaceshipBase entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9 + 180;
        final float var25 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9 + 45;

        GL11.glTranslatef((float) par2, (float) par4 - 0.4F, (float) par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        final float var28 = entity.rollAmplitude / 3 - par9;
        float var30 = entity.shipDamage - par9;

        if (var30 < 0.0F)
        {
            var30 = 0.0F;
        }

        if (var28 > 0.0F)
        {
            final float i = entity.getLaunched() ? (5 - MathHelper.floor_double(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
            GL11.glRotatef(MathHelper.sin(var28) * var28 * i * par9, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(MathHelper.sin(var28) * var28 * i * par9, 1.0F, 0.0F, 1.0F);
        }

        this.bindEntityTexture(entity);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glScalef(0.9F, 0.9F, 0.9F);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        this.bindTexture(rocketTexture);
        worldrenderer.begin(GL11.GL_QUADS, rocketModel.getFormat());

        this.renderQuads(worldrenderer, rocketModel.getGeneralQuads(), -1);
        tessellator.draw();

//        this.rocketModelObj.renderAll();
//        this.rocketModelObj.renderPart("Boosters");
//        this.rocketModelObj.renderPart("Rocket");
        Vector3 teamColor = ClientUtil.updateTeamColor(FMLClientHandler.instance().getClient().thePlayer.getName(), true);
        if (teamColor != null)
        {
            GL11.glColor3f(teamColor.floatX(), teamColor.floatY(), teamColor.floatZ());
        }
//        this.rocketModelObj.renderPart("NoseCone");

        if (FMLClientHandler.instance().getClient().thePlayer.ticksExisted / 10 % 2 < 1)
        {
            GL11.glColor3f(1, 0, 0);
        }
        else
        {
            GL11.glColor3f(0, 1, 0);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glColor3f(1, 1, 1);

        GL11.glPopMatrix();
    }

    private void renderQuads(WorldRenderer renderer, List<BakedQuad> quads, int color) {
        for(BakedQuad bakedquad : quads)
            LightUtil.renderQuadColor(renderer, bakedquad, color);
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderSpaceship((EntitySpaceshipBase) par1Entity, par2, par4, par6, par8, par9);
    }
}
