package micdoodle8.mods.galacticraft.planets.venus.client;

import java.lang.reflect.Field;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IRenderHandler;

public class WeatherRendererVenus extends IRenderHandler
{
    private final Random random = new Random();
    private final float[] rainXCoords = new float[1024];
    private final float[] rainYCoords = new float[1024];
    private static final ResourceLocation RAIN_TEXTURES = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/misc/rain_venus.png");

    public WeatherRendererVenus()
    {
        for (int i = 0; i < 32; ++i)
        {
            float f1 = (float)(i - 16);
            for (int j = 0; j < 32; ++j)
            {
                float f = (float)(j - 16);
                float f2 = MathHelper.sqrt_float(f * f + f1 * f1);
                this.rainXCoords[i << 5 | j] = -f1 / f2;
                this.rainYCoords[i << 5 | j] = f / f2;
            }
        }
    }

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc)
    {
        float strength = world.getRainStrength(partialTicks);

        if (strength > 0.0F)
        {
            int rendererUpdateCount = 0;
            try {
                Field fieldRUC = mc.entityRenderer.getClass().getDeclaredField(GCCoreUtil.isDeobfuscated() ? "rendererUpdateCount" : "field_78529_t");
                fieldRUC.setAccessible(true);
                rendererUpdateCount = fieldRUC.getInt(mc.entityRenderer);
            } catch (Exception e) { }
            mc.entityRenderer.enableLightmap();
            Entity entity = mc.getRenderViewEntity();

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableCull();
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.alphaFunc(516, 0.1F);
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
            int l = MathHelper.floor_double(d1);

            int r = 4;
            if (mc.gameSettings.fancyGraphics)
            {
                r = 8;
            }

            int drawFlag = -1;
            worldrenderer.setTranslation(-d0, -d1, -d2);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            int px = MathHelper.floor_double(entity.posX);
            int py = MathHelper.floor_double(entity.posY);
            int pz = MathHelper.floor_double(entity.posZ);

            for (int z = pz - r; z <= pz + r; ++z)
            {
                int indexZ = (z - pz + 16) * 32;
                for (int x = px - r; x <= px + r; ++x)
                {
                    mutablePos.set(x, 0, z);
                    int yHeight = world.getPrecipitationHeight(mutablePos).getY() + 4 - (int)(4.8F * strength);
                    int y = py - r;
                    int ymax = py + r;

                    if (y < yHeight)
                    {
                        y = yHeight;
                    }

                    if (ymax < yHeight)
                    {
                        ymax = yHeight;
                    }

                    int yBase = yHeight;

                    if (yHeight < l)
                    {
                        yBase = l;
                    }

                    if (y != ymax)
                    {
                        this.random.setSeed((long)(x * x * 3121 + x * 45238971 ^ z * z * 418711 + z * 13761));

                        if (drawFlag != 0)
                        {
                            if (drawFlag >= 0)
                            {
                                tessellator.draw();
                            }

                            drawFlag = 0;
                            mc.getTextureManager().bindTexture(RAIN_TEXTURES);
                            worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        int index = indexZ + x - px + 16;
                        double dx = (double)this.rainXCoords[index] * 0.5D;
                        double dz = (double)this.rainYCoords[index] * 0.5D;
                        double dy = -((double)(rendererUpdateCount + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31) + (double)partialTicks) / 80.0D * (3.0D + this.random.nextDouble());
                        double yo = this.random.nextDouble() / 1.8D;
                        double xx = x + 0.5D - entity.posX;
                        double zz = z + 0.5D - entity.posZ;
                        float rr = MathHelper.sqrt_double(xx * xx + zz * zz) / r;
                        float alpha = ((1.0F - rr * rr) * 0.5F + 0.5F) * strength / 0.6F;  //0.6F is the max rainstrength on Venus
                        mutablePos.set(x, yBase, z);
                        int light = world.getCombinedLight(mutablePos, 0);
                        int lx = light >> 16 & 65535;
                        int ly = light & 65535;
                        double xc = x + 0.5D;
                        double zc = z + 0.5D;
                        worldrenderer.pos(xc - dx, (double)ymax - yo, zc - dz).tex(0.0D, (double)y * 0.25D + dy).color(1.0F, 1.0F, 1.0F, alpha).lightmap(lx, ly).endVertex();
                        worldrenderer.pos(xc + dx, (double)ymax - yo, zc + dz).tex(1.0D, (double)y * 0.25D + dy).color(1.0F, 1.0F, 1.0F, alpha).lightmap(lx, ly).endVertex();
                        worldrenderer.pos(xc + dx, (double)y - yo, zc + dz).tex(1.0D, (double)ymax * 0.25D + dy).color(1.0F, 1.0F, 1.0F, alpha).lightmap(lx, ly).endVertex();
                        worldrenderer.pos(xc - dx, (double)y - yo, zc - dz).tex(0.0D, (double)ymax * 0.25D + dy).color(1.0F, 1.0F, 1.0F, alpha).lightmap(lx, ly).endVertex();
                    }
                }
            }

            if (drawFlag >= 0)
            {
                tessellator.draw();
            }

            worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            mc.entityRenderer.disableLightmap();
        }
    }
}
