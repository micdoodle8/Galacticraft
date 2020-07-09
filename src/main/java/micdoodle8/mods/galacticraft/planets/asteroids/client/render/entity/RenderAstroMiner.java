package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class RenderAstroMiner extends EntityRenderer<EntityAstroMiner>
{
    private static final float LSIZE = 0.12F;
    private static final float RETRACTIONSPEED = 0.02F;
    private float lastPartTime;

    public static ResourceLocation scanTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/misc/gradient.png");
    private static OBJModel.OBJBakedModel mainModel;
    private static OBJModel.OBJBakedModel hoverPadMain;
    private static OBJModel.OBJBakedModel hoverPadGlow;
    private static OBJModel.OBJBakedModel mainModelInactive;
    private static OBJModel.OBJBakedModel modellaser1;
    private static OBJModel.OBJBakedModel modellaser3;
    private static OBJModel.OBJBakedModel modellasergl;

    private final NoiseModule wobbleX;
    private final NoiseModule wobbleY;
    private final NoiseModule wobbleZ;
    private final NoiseModule wobbleXX;
    private final NoiseModule wobbleYY;
    private final NoiseModule wobbleZZ;

    public static void updateModels(ModelLoader modelLoader)
    {
        try
        {
            mainModel = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "astro_miner_full.obj"), ImmutableList.of("Hull", "Lasers"));
            hoverPadMain = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "astro_miner_full.obj"), ImmutableList.of("HoverPad"));
            hoverPadGlow = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "astro_miner_full.obj"), ImmutableList.of("Glow"));
            modellaser1 = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "astro_miner_full.obj"), ImmutableList.of("Main_Laser_Front"));
            modellaser3 = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "astro_miner_full.obj"), ImmutableList.of("Main_Laser_Center"));
            modellasergl = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "astro_miner_full.obj"), ImmutableList.of("Main_Laser_Left_Guard"));
            mainModelInactive = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "astro_miner_full.obj"), ImmutableList.of("Hull", "Lasers", "HoverPad"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public RenderAstroMiner(EntityRendererManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 2F;

        Random rand = new Random();
        this.wobbleX = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleX.amplitude = 0.5F;
        this.wobbleX.frequencyX = 0.025F;

        this.wobbleY = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleY.amplitude = 0.6F;
        this.wobbleY.frequencyX = 0.025F;

        this.wobbleZ = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleZ.amplitude = 0.1F;
        this.wobbleZ.frequencyX = 0.025F;

        this.wobbleXX = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleXX.amplitude = 0.1F;
        this.wobbleXX.frequencyX = 0.8F;

        this.wobbleYY = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleYY.amplitude = 0.15F;
        this.wobbleYY.frequencyX = 0.8F;

        this.wobbleZZ = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleZZ.amplitude = 0.04F;
        this.wobbleZZ.frequencyX = 0.8F;
    }

    @Override
    public void doRender(EntityAstroMiner astroMiner, double x, double y, double z, float f, float partialTickTime)
    {
        int ais = astroMiner.AIstate;
        boolean active = ais > EntityAstroMiner.AISTATE_ATBASE;
        float time = astroMiner.ticksExisted + partialTickTime;
        float sinOfTheTime = (MathHelper.sin(time / 4) + 1F) / 4F + 0.5F;
        float wx = active ? this.wobbleX.getNoise(time) + this.wobbleXX.getNoise(time) : 0F;
        float wy = active ? this.wobbleY.getNoise(time) + this.wobbleYY.getNoise(time) : 0F;
        float wz = active ? this.wobbleZ.getNoise(time) + this.wobbleZZ.getNoise(time) : 0F;
        float partTime = partialTickTime - this.lastPartTime;
        this.lastPartTime = partialTickTime;

        while (partTime < 0)
        {
            partTime += 1F;
        }

        this.bindEntityTexture(astroMiner);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        final float rotPitch = astroMiner.prevRotationPitch + (astroMiner.rotationPitch - astroMiner.prevRotationPitch) * partialTickTime;
        final float rotYaw = astroMiner.prevRotationYaw + (astroMiner.rotationYaw - astroMiner.prevRotationYaw) * partialTickTime;

        GlStateManager.translatef((float) x, (float) y + 1.4F, (float) z);
        float partBlock;

        switch (astroMiner.facing)
        {
        case DOWN:
            partBlock = (float) (astroMiner.posY % 2D);
            break;
        case UP:
            partBlock = 1F - (float) (astroMiner.posY % 2D);
            break;
        case NORTH:
            partBlock = (float) (astroMiner.posZ % 2D);
            break;
        case SOUTH:
            partBlock = 1F - (float) (astroMiner.posZ % 2D);
            break;
        case WEST:
            partBlock = (float) (astroMiner.posX % 2D);
            break;
        case EAST:
            partBlock = 1F - (float) (astroMiner.posX % 2D);
            break;
        default:
            partBlock = 0F;
        }
        partBlock /= 0.06F;

        GlStateManager.rotatef(rotYaw + 180F, 0, 1, 0);

        if (rotPitch != 0F)
        {
            GlStateManager.translatef(-0.65F, -0.65F, 0);
            GlStateManager.rotatef(rotPitch / 4F, 1, 0, 0);
            GlStateManager.translatef(0.65F, 0.65F, 0);
        }

        GlStateManager.translatef(0F, -0.42F, 0.28F);
        GlStateManager.scalef(0.0495F, 0.0495F, 0.0495F);
        GlStateManager.translatef(wx, wy, wz);

        if (active)
        {
            ClientUtil.drawBakedModel(mainModel);
            this.renderLaserModel(astroMiner.retraction);

            float lightMapSaveX = GLX.lastBrightnessX;
            float lightMapSaveY = GLX.lastBrightnessY;
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
            GlStateManager.disableLighting();
            GlStateManager.color4f(sinOfTheTime, sinOfTheTime, sinOfTheTime, 1.0F);
            ClientUtil.drawBakedModel(hoverPadMain);

            GlStateManager.disableCull();
            GlStateManager.disableAlphaTest();
            GlStateManager.depthMask(false);
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GlStateManager.enableBlend();
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GlStateManager.color4f(sinOfTheTime, sinOfTheTime, sinOfTheTime, 0.6F);
            ClientUtil.drawBakedModel(hoverPadGlow);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            if (ais < EntityAstroMiner.AISTATE_DOCKING)
            {
                //This is the scanning lasers:
                Minecraft.getInstance().textureManager.bindTexture(scanTexture);
                final Tessellator tess = Tessellator.getInstance();
                GlStateManager.color4f(0, 0.6F, 1.0F, 0.2F);
                BufferBuilder worldRenderer = tess.getBuffer();
                float scanProgress = (MathHelper.cos(partBlock * 0.012F * 6.283F)) * 0.747F;
                float scanAngle = 0.69866F - scanProgress * scanProgress;
                float scanEndX = 38.77F * MathHelper.sin(scanAngle);
                float scanEndY = 32F;
                float scanEndZ = 38.77F * MathHelper.cos(scanAngle);
                scanEndZ += 20F;
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                worldRenderer.pos(15.6F, -0.6F, -20F).tex(0D, 0D).endVertex();
                worldRenderer.pos(15.6F + scanEndX, scanEndY - 0.6F, -scanEndZ).tex(1D, 0D).endVertex();
                worldRenderer.pos(15.6F + scanEndX, -0.6F - scanEndY, -scanEndZ).tex(1D, 1D).endVertex();
                worldRenderer.pos(15.6F, -0.7F, -20F).tex(0D, 1D).endVertex();
                tess.draw();
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                worldRenderer.pos(-15.6F, -0.6F, -20F).tex(0D, 0D).endVertex();
                worldRenderer.pos(-15.6F - scanEndX, scanEndY - 0.6F, -scanEndZ).tex(1D, 0D).endVertex();
                worldRenderer.pos(-15.6F - scanEndX, -0.6F - scanEndY, -scanEndZ).tex(1D, 1D).endVertex();
                worldRenderer.pos(-15.6F, -0.7F, -20F).tex(0D, 1D).endVertex();
                tess.draw();

                int removeCount = 0;
                int afterglowCount = 0;
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.translatef((float) (x - astroMiner.posX), (float) (y - astroMiner.posY), (float) (z - astroMiner.posZ));

                for (Integer blockTime : new ArrayList<Integer>(astroMiner.laserTimes))
                {
                    if (blockTime < astroMiner.ticksExisted - 19)
                    {
                        removeCount++;
                    }
                    else if (blockTime < astroMiner.ticksExisted - 3)
                    {
                        afterglowCount++;
                    }
                }
                if (removeCount > 0)
                {
                    astroMiner.removeLaserBlocks(removeCount);
                }
                int count = 0;
                for (BlockVec3 blockLaser : new ArrayList<BlockVec3>(astroMiner.laserBlocks))
                {
                    if (count < afterglowCount)
                    {
                        int fade = astroMiner.ticksExisted - astroMiner.laserTimes.get(count) - 8;
                        if (fade < 0)
                        {
                            fade = 0;
                        }
                        this.doAfterGlow(blockLaser, fade);
                    }
                    else
                    {
                        this.doLaser(astroMiner, blockLaser);
                    }
                    count++;
                }
                if (astroMiner.retraction > 0F)
                {
                    astroMiner.retraction -= RETRACTIONSPEED * partTime;
                    if (astroMiner.retraction < 0F)
                    {
                        astroMiner.retraction = 0F;
                    }
                }
                GlStateManager.popMatrix();
            }
            else
            {
                if (astroMiner.retraction < 1F)
                {
                    astroMiner.retraction += RETRACTIONSPEED * partTime;
                    if (astroMiner.retraction > 1F)
                    {
                        astroMiner.retraction = 1F;
                    }
                }
                GlStateManager.popMatrix();
            }
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableCull();
            GlStateManager.enableAlphaTest();
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lightMapSaveX, lightMapSaveY);
        }
        else
        {
            this.bindEntityTexture(astroMiner);
            ClientUtil.drawBakedModel(mainModelInactive);
            this.renderLaserModel(astroMiner.retraction);
            if (astroMiner.retraction < 1F)
            {
                astroMiner.retraction += RETRACTIONSPEED * partTime;
                if (astroMiner.retraction > 1F)
                {
                    astroMiner.retraction = 1F;
                }
            }
            GlStateManager.popMatrix();
        }
    }

    private void doAfterGlow(BlockVec3 blockLaser, int level)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translatef(blockLaser.x, blockLaser.y, blockLaser.z);
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        GlStateManager.color4f(1.0F, 0.7F, 0.7F, 0.016667F * (12 - level));
        float cA = -0.01F;
        float cB = 1.01F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cB, cA).tex(0D, 1D).endVertex();
        worldRenderer.pos(cB, cB, cA).tex(1D, 1D).endVertex();
        worldRenderer.pos(cB, cB, cB).tex(1D, 0D).endVertex();
        worldRenderer.pos(cA, cB, cB).tex(0D, 0D).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(0D, 0D).endVertex();
        worldRenderer.pos(cA, cA, cB).tex(0D, 1D).endVertex();
        worldRenderer.pos(cB, cA, cB).tex(1D, 1D).endVertex();
        worldRenderer.pos(cB, cA, cA).tex(1D, 0D).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(1D, 0D).endVertex();
        worldRenderer.pos(cA, cB, cA).tex(0D, 0D).endVertex();
        worldRenderer.pos(cA, cB, cB).tex(0D, 1D).endVertex();
        worldRenderer.pos(cA, cA, cB).tex(1D, 1D).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cB, cA, cA).tex(1D, 1D).endVertex();
        worldRenderer.pos(cB, cA, cB).tex(1D, 0D).endVertex();
        worldRenderer.pos(cB, cB, cB).tex(0D, 0D).endVertex();
        worldRenderer.pos(cB, cB, cA).tex(0D, 1D).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(1D, 0D).endVertex();
        worldRenderer.pos(1F, cA, cA).tex(0D, 0D).endVertex();
        worldRenderer.pos(1F, 1F, cA).tex(0D, 1D).endVertex();
        worldRenderer.pos(cA, 1F, cA).tex(1D, 1D).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(1F, cA, 1F).tex(1D, 1D).endVertex();
        worldRenderer.pos(cA, cA, 1F).tex(1D, 0D).endVertex();
        worldRenderer.pos(cA, 1F, 1F).tex(0D, 0D).endVertex();
        worldRenderer.pos(1F, 1F, 1F).tex(0D, 1D).endVertex();
        tess.draw();
        GlStateManager.popMatrix();
    }

    private void doLaser(EntityAstroMiner entity, BlockVec3 blockLaser)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translatef(blockLaser.x, blockLaser.y, blockLaser.z);
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        GlStateManager.color4f(1.0F, 0.7F, 0.7F, 0.2F);
        float cA = -0.01F;
        float cB = 1.01F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cB, cA).tex(0D, 1D).endVertex();
        worldRenderer.pos(cB, cB, cA).tex(1D, 1D).endVertex();
        worldRenderer.pos(cB, cB, cB).tex(1D, 0D).endVertex();
        worldRenderer.pos(cA, cB, cB).tex(0D, 0D).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(0D, 0D).endVertex();
        worldRenderer.pos(cA, cA, cB).tex(0D, 1D).endVertex();
        worldRenderer.pos(cB, cA, cB).tex(1D, 1D).endVertex();
        worldRenderer.pos(cB, cA, cA).tex(1D, 0D).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(1D, 0D).endVertex();
        worldRenderer.pos(cA, cB, cA).tex(0D, 0D).endVertex();
        worldRenderer.pos(cA, cB, cB).tex(0D, 1D).endVertex();
        worldRenderer.pos(cA, cA, cB).tex(1D, 1D).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cB, cA, cA).tex(1D, 1D).endVertex();
        worldRenderer.pos(cB, cA, cB).tex(1D, 0D).endVertex();
        worldRenderer.pos(cB, cB, cB).tex(0D, 0D).endVertex();
        worldRenderer.pos(cB, cB, cA).tex(0D, 1D).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(1D, 0D).endVertex();
        worldRenderer.pos(1F, cA, cA).tex(0D, 0D).endVertex();
        worldRenderer.pos(1F, 1F, cA).tex(0D, 1D).endVertex();
        worldRenderer.pos(cA, 1F, cA).tex(1D, 1D).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(1F, cA, 1F).tex(1D, 1D).endVertex();
        worldRenderer.pos(cA, cA, 1F).tex(1D, 0D).endVertex();
        worldRenderer.pos(cA, 1F, 1F).tex(0D, 0D).endVertex();
        worldRenderer.pos(1F, 1F, 1F).tex(0D, 1D).endVertex();
        tess.draw();

        GlStateManager.color4f(1.0F, 0.79F, 0.79F, 0.17F);
        float bb = 1.7F;
        float cc = 0.4F;
        float radiansYaw = entity.rotationYaw / Constants.RADIANS_TO_DEGREES;
        float radiansPitch = entity.rotationPitch / Constants.RADIANS_TO_DEGREES / 4F;
        float mainLaserX = bb * MathHelper.sin(radiansYaw) * MathHelper.cos(radiansPitch);
        float mainLaserY = cc + bb * MathHelper.sin(radiansPitch);
        float mainLaserZ = bb * MathHelper.cos(radiansYaw) * MathHelper.cos(radiansPitch);

        mainLaserX += entity.posX - blockLaser.x;
        mainLaserY += entity.posY - blockLaser.y;
        mainLaserZ += entity.posZ - blockLaser.z;

        float xD = mainLaserX - 0.5F;
        float yD = mainLaserY - 0.5F;
        float zD = mainLaserZ - 0.5F;
        float xx, yy, zz;

        if (entity.facing.getIndex() > Direction.SOUTH.getIndex())
        {
            xx = xD < 0 ? cA : cB;
            this.drawLaserX(mainLaserX, mainLaserY, mainLaserZ, xx, 0.5F, 0.5F);
        }
        else if (entity.facing.getIndex() <= Direction.UP.getIndex())
        {
            yy = yD < 0 ? cA : cB;
            this.drawLaserY(mainLaserX, mainLaserY, mainLaserZ, 0.5F, yy, 0.5F);
        }
        else
        {
            zz = zD < 0 ? cA : cB;
            this.drawLaserZ(mainLaserX, mainLaserY, mainLaserZ, 0.5F, 0.5F, zz);
        }

        GlStateManager.popMatrix();
    }

    private void drawLaserX(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1, y1 - 0.01F, z1 - 0.01F).endVertex();
        worldRenderer.pos(x2, y2 - LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(x1, y1 + 0.01F, z1 - 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1, y1 - 0.01F, z1 + 0.01F).endVertex();
        worldRenderer.pos(x2, y2 - LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(x1, y1 + 0.01F, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1, y1 - 0.01F, z1 - 0.01F).endVertex();
        worldRenderer.pos(x2, y2 - LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(x2, y2 - LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(x1, y1 - 0.01F, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1, y1 + 0.01F, z1 + 0.01F).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(x1, y1 + 0.01F, z1 - 0.01F).endVertex();
        tess.draw();
    }

    private void drawLaserY(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 - 0.01F, y1, z1 - 0.01F).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(x1 + 0.01F, y1, z1 - 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 - 0.01F, y1, z1 + 0.01F).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(x1 + 0.01F, y1, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 - 0.01F, y1, z1 - 0.01F).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(x1 - 0.01F, y1, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 + 0.01F, y1, z1 + 0.01F).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(x1 + 0.01F, y1, z1 - 0.01F).endVertex();
        tess.draw();
    }

    private void drawLaserZ(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 - 0.01F, y1 - 0.01F, z1).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2 + LSIZE, z2).endVertex();
        worldRenderer.pos(x1 - 0.01F, y1 + 0.01F, z1).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 + 0.01F, y1 - 0.01F, z1).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2 + LSIZE, z2).endVertex();
        worldRenderer.pos(x1 + 0.01F, y1 + 0.01F, z1).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 - 0.01F, y1 - 0.01F, z1).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(x1 + 0.01F, y1 - 0.01F, z1).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1, y1 + 0.01F, z1 + 0.01F).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(x1, y1 + 0.01F, z1 - 0.01F).endVertex();
        tess.draw();
    }

    private void renderLaserModel(float retraction)
    {
        float laserretraction = retraction / 0.8F;
        if (laserretraction > 1F)
        {
            laserretraction = 1F;
        }
        float guardmovement = (retraction - 0.6F) / 0.4F * 1.875F;
        if (guardmovement < 0F)
        {
            guardmovement = 0F;
        }
        GlStateManager.pushMatrix();
        float zadjust = laserretraction * 5F;
        float yadjust = zadjust;

        if (yadjust > 0.938F)
        {
            yadjust = 0.938F;
            zadjust = (zadjust - yadjust) * 2.5F + yadjust;
        }
        GlStateManager.translatef(0F, yadjust, zadjust);
        ClientUtil.drawBakedModel(modellaser1);
        if (yadjust == 0.938F)
        {
            //Do not move laser centre into body
            GlStateManager.translatef(0F, 0F, -zadjust + 0.938F);
        }
        ClientUtil.drawBakedModel(modellaser3);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translatef(guardmovement, 0F, 0F);
        ClientUtil.drawBakedModel(modellasergl);
        GlStateManager.translatef(-2 * guardmovement + 8.75F, 0F, 0F);
        ClientUtil.drawBakedModel(modellasergl);
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityAstroMiner entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public boolean shouldRender(EntityAstroMiner miner, ICamera camera, double camX, double camY, double camZ)
    {
        return miner.isInRangeToRender3d(camX, camY, camZ);
    }
}
