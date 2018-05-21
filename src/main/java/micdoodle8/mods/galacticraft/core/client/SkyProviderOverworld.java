package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.lang.reflect.Method;
import java.util.Random;

public class SkyProviderOverworld extends IRenderHandler
{
    private static final ResourceLocation moonTexture = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation sunTexture = new ResourceLocation("textures/environment/sun.png");

    private static boolean optifinePresent = false;

    static
    {
        try
        {
            optifinePresent = Launch.classLoader.getClassBytes("CustomColorizer") != null;
        }
        catch (final Exception e)
        {
        }
    }

    private int starGLCallList = GLAllocation.generateDisplayLists(7);
    private int glSkyList;
    private int glSkyList2;
    private final ResourceLocation planetToRender = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png");

    public SkyProviderOverworld()
    {
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        final Random rand = new Random(10842L);
        GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
        this.renderStars(worldrenderer, rand);
        tessellator.draw();
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 1, GL11.GL_COMPILE);
        this.renderStars(worldrenderer, rand);
        tessellator.draw();
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 2, GL11.GL_COMPILE);
        this.renderStars(worldrenderer, rand);
        tessellator.draw();
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 3, GL11.GL_COMPILE);
        this.renderStars(worldrenderer, rand);
        tessellator.draw();
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 4, GL11.GL_COMPILE);
        this.renderStars(worldrenderer, rand);
        tessellator.draw();
        GL11.glEndList();
        GL11.glPopMatrix();
        this.glSkyList = this.starGLCallList + 5;
        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
        final byte byte2 = 5;
        final int i = 256 / byte2 + 2;
        float f = 16F;
        BufferBuilder worldRenderer = tessellator.getBuffer();

        for (int j = -byte2 * i; j <= byte2 * i; j += byte2)
        {
            for (int l = -byte2 * i; l <= byte2 * i; l += byte2)
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                worldRenderer.pos(j, f, l).endVertex();
                worldRenderer.pos(j + byte2, f, l).endVertex();
                worldRenderer.pos(j + byte2, f, l + byte2).endVertex();
                worldRenderer.pos(j, f, l + byte2).endVertex();
                tessellator.draw();
            }
        }

        GL11.glEndList();
        this.glSkyList2 = this.starGLCallList + 6;
        GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
        f = -16F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        for (int k = -byte2 * i; k <= byte2 * i; k += byte2)
        {
            for (int i1 = -byte2 * i; i1 <= byte2 * i; i1 += byte2)
            {
                worldRenderer.pos(k + byte2, f, i1).endVertex();
                worldRenderer.pos(k, f, i1).endVertex();
                worldRenderer.pos(k, f, i1 + byte2).endVertex();
                worldRenderer.pos(k + byte2, f, i1 + byte2).endVertex();
            }
        }

        tessellator.draw();
        GL11.glEndList();
    }

    private final Minecraft minecraft = FMLClientHandler.instance().getClient();

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc)
    {
        if (!ClientProxyCore.overworldTextureRequestSent)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, GCCoreUtil.getDimensionID(mc.world), new Object[] {}));
            ClientProxyCore.overworldTextureRequestSent = true;
        }

        double zoom = 0.0;
        double yaw = 0.0;
        double pitch = 0.0;
        Method m = null;

        if (!optifinePresent)
        {
            try
            {
                Class<?> c = mc.entityRenderer.getClass();
                zoom = mc.entityRenderer.cameraZoom;
                yaw = mc.entityRenderer.cameraYaw;
                pitch = mc.entityRenderer.cameraPitch;

                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();

                if (zoom != 1.0D)
                {
                    GL11.glTranslatef((float) yaw, (float) (-pitch), 0.0F);
                    GL11.glScaled(zoom, zoom, 1.0D);
                }

                Project.gluPerspective(mc.gameSettings.fovSetting, (float) mc.displayWidth / (float) mc.displayHeight, 0.05F, 1400.0F);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();

                mc.entityRenderer.orientCamera(partialTicks);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        float theta = MathHelper.sqrt(((float) (mc.player.posY) - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / 1000.0F);
        final float var21 = Math.max(1.0F - theta * 4.0F, 0.0F);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.disableRescaleNormal();
        final Vec3d var2 = this.minecraft.world.getSkyColor(this.minecraft.getRenderViewEntity(), partialTicks);
        float i = (float) var2.x * var21;
        float x = (float) var2.y * var21;
        float var5 = (float) var2.z * var21;
        float z;

        if (this.minecraft.gameSettings.anaglyph)
        {
            final float y = (i * 30.0F + x * 59.0F + var5 * 11.0F) / 100.0F;
            final float var7 = (i * 30.0F + x * 70.0F) / 100.0F;
            z = (i * 30.0F + var5 * 70.0F) / 100.0F;
            i = y;
            x = var7;
            var5 = z;
        }

        GL11.glColor3f(i, x, var5);
        final Tessellator var23 = Tessellator.getInstance();
        BufferBuilder worldRenderer = var23.getBuffer();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(i, x, var5);
        if (mc.player.posY < 214)
        {
            GL11.glCallList(this.glSkyList);
        }
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();
        final float[] costh = this.minecraft.world.provider.calcSunriseSunsetColors(this.minecraft.world.getCelestialAngle(partialTicks), partialTicks);
        float var9;
        float size;
        float rand1;
        float r;

        if (costh != null)
        {
            final float sunsetModInv = Math.min(1.0F, Math.max(1.0F - theta * 50.0F, 0.0F));

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(MathHelper.sin(this.minecraft.world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            z = costh[0] * sunsetModInv;
            var9 = costh[1] * sunsetModInv;
            size = costh[2] * sunsetModInv;
            float rand3;

            if (this.minecraft.gameSettings.anaglyph)
            {
                rand1 = (z * 30.0F + var9 * 59.0F + size * 11.0F) / 100.0F;
                r = (z * 30.0F + var9 * 70.0F) / 100.0F;
                rand3 = (z * 30.0F + size * 70.0F) / 100.0F;
                z = rand1;
                var9 = r;
                size = rand3;
            }

            worldRenderer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);

            worldRenderer.pos(0.0D, 100.0D, 0.0D).color(z * sunsetModInv, var9 * sunsetModInv, size * sunsetModInv, costh[3]).endVertex();
            final byte phi = 16;

            for (int var27 = 0; var27 <= phi; ++var27)
            {
                rand3 = var27 * Constants.twoPI / phi;
                final float xx = MathHelper.sin(rand3);
                final float rand5 = MathHelper.cos(rand3);
                worldRenderer.pos(xx * 120.0F, rand5 * 120.0F, -rand5 * 40.0F * costh[3]).color(costh[0] * sunsetModInv, costh[1] * sunsetModInv, costh[2] * sunsetModInv, 0.0F).endVertex();
            }

            var23.draw();
            GL11.glPopMatrix();
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

        GL11.glPushMatrix();
        z = 1.0F - this.minecraft.world.getRainStrength(partialTicks);
        var9 = 0.0F;
        size = 0.0F;
        rand1 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, z);
        GL11.glTranslatef(var9, size, rand1);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

        GL11.glRotatef(this.minecraft.world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
        double playerHeight = this.minecraft.player.posY;

        //Draw stars
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        float threshold;
        Vec3d vec = TransformerHooks.getFogColorHook(this.minecraft.world);
        threshold = Math.max(0.1F, (float) vec.lengthVector() - 0.1F);
        float var20 = ((float) playerHeight - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / 1000.0F;
        var20 = MathHelper.sqrt(var20);
        float bright1 = Math.min(0.9F, var20 * 3);
//        float bright2 = Math.min(0.85F, var20 * 2.5F);
//        float bright3 = Math.min(0.8F, var20 * 2);
//        float bright4 = Math.min(0.75F, var20 * 1.5F);
//        float bright5 = Math.min(0.7F, var20 * 0.75F);
        if (bright1 > threshold)
        {
            GL11.glColor4f(bright1, bright1, bright1, 1.0F);
            GL11.glCallList(this.starGLCallList);
        }
//        if (bright2 > threshold)
//        {
//	        GL11.glColor4f(bright2, bright2, bright2, 1.0F);
//	        GL11.glCallList(this.starGLCallList + 1);
//        }
//        if (bright3 > threshold)
//        {
//	        GL11.glColor4f(bright3, bright3, bright3, 1.0F);
        GL11.glCallList(this.starGLCallList + 2);
//        }
//        if (bright4 > threshold)
//        {
//	        GL11.glColor4f(bright4, bright4, bright4, 1.0F);
        GL11.glCallList(this.starGLCallList + 3);
//        }
//        if (bright5 > threshold)
//        {
//	        GL11.glColor4f(bright5, bright5, bright5, 1.0F);
        GL11.glCallList(this.starGLCallList + 4);
//        }

        //Draw sun
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        r = 30.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.renderEngine.bindTexture(SkyProviderOverworld.sunTexture);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(-r, 100.0D, -r).tex(0.0D, 0.0D).endVertex();
        worldRenderer.pos(r, 100.0D, -r).tex(1.0D, 0.0D).endVertex();
        worldRenderer.pos(r, 100.0D, r).tex(1.0D, 1.0D).endVertex();
        worldRenderer.pos(-r, 100.0D, r).tex(0.0D, 1.0D).endVertex();
        var23.draw();

        //Draw moon
        r = 40.0F;
        this.minecraft.renderEngine.bindTexture(SkyProviderOverworld.moonTexture);
        float sinphi = this.minecraft.world.getMoonPhase();
        final int cosphi = (int) (sinphi % 4);
        final int var29 = (int) (sinphi / 4 % 2);
        final float yy = (cosphi) / 4.0F;
        final float rand7 = (var29) / 2.0F;
        final float zz = (cosphi + 1) / 4.0F;
        final float rand9 = (var29 + 1) / 2.0F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(-r, -100.0D, r).tex(zz, rand9).endVertex();
        worldRenderer.pos(r, -100.0D, r).tex(yy, rand9).endVertex();
        worldRenderer.pos(r, -100.0D, -r).tex(yy, rand7).endVertex();
        worldRenderer.pos(-r, -100.0D, -r).tex(zz, rand7).endVertex();
        var23.draw();
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(0.0F, 0.0F, 0.0F);

        //TODO get exact height figure here
        double var25 = playerHeight - 64;

        if (var25 > this.minecraft.gameSettings.renderDistanceChunks * 16)
        {
            theta *= 400.0F;

            final float sinth = Math.max(Math.min(theta / 100.0F - 0.2F, 0.5F), 0.0F);

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_FOG);
            float scale = 850 * (0.25F - theta / 10000.0F);
            scale = Math.max(scale, 0.2F);
            GL11.glScalef(scale, 1.0F, scale);
            GL11.glTranslatef(0.0F, -(float) mc.player.posY, 0.0F);
//	        if (ClientProxyCore.overworldTextureLocal != null)
//	        {
//	            GL11.glBindTexture(GL11.GL_TEXTURE_2D, ClientProxyCore.overworldTextureLocal.getGlTextureId());
//	        }
//	        else
            {
                this.minecraft.renderEngine.bindTexture(this.planetToRender);
            }

            size = 1.0F;

            GL11.glColor4f(sinth, sinth, sinth, 1.0F);
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

//	        float zoomIn = (1F - (float) var25 / 768F) / 5.86F;
//	        if (zoomIn < 0F) zoomIn = 0F;
            double zoomIn = 0.0D;
            double cornerB = 1.0 - zoomIn;
            worldRenderer.pos(-size, 0, size).tex(zoomIn, cornerB).endVertex();
            worldRenderer.pos(size, 0, size).tex(cornerB, cornerB).endVertex();
            worldRenderer.pos(size, 0, -size).tex(cornerB, zoomIn).endVertex();
            worldRenderer.pos(-size, 0, -size).tex(zoomIn, zoomIn).endVertex();
            var23.draw();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }

        GL11.glColor3f(0.0f, 0.0f, 0.0f);

        GlStateManager.enableRescaleNormal();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);

        if (!optifinePresent)
        {
            try
            {
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();

                if (zoom != 1.0D)
                {
                    GL11.glTranslatef((float) yaw, (float) (-pitch), 0.0F);
                    GL11.glScaled(zoom, zoom, 1.0D);
                }

                Project.gluPerspective(mc.gameSettings.fovSetting, (float) mc.displayWidth / (float) mc.displayHeight, 0.05F, this.minecraft.gameSettings.renderDistanceChunks * 16 * 2.0F);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();

                mc.entityRenderer.orientCamera(partialTicks);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderStars(BufferBuilder worldRenderer, Random rand)
    {
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        for (int i = 0; i < (ConfigManagerCore.moreStars ? 4000 : 1200); ++i)
        {
            double x = rand.nextFloat() * 2.0F - 1.0F;
            double y = rand.nextFloat() * 2.0F - 1.0F;
            double z = rand.nextFloat() * 2.0F - 1.0F;
            final double size = 0.15F + rand.nextFloat() * 0.1F;
            double r = x * x + y * y + z * z;

            if (r < 1.0D && r > 0.01D)
            {
                r = 1.0D / Math.sqrt(r);
                x *= r;
                y *= r;
                z *= r;
                final double xx = x * (ConfigManagerCore.moreStars ? rand.nextDouble() * 100D + 150D : 100.0D);
                final double zz = z * (ConfigManagerCore.moreStars ? rand.nextDouble() * 100D + 150D : 100.0D);
                if (Math.abs(xx) < 29D && Math.abs(zz) < 29D)
                {
                    continue;
                }
                final double yy = y * (ConfigManagerCore.moreStars ? rand.nextDouble() * 100D + 150D : 100.0D);
                final double theta = Math.atan2(x, z);
                final double sinth = Math.sin(theta);
                final double costh = Math.cos(theta);
                final double phi = Math.atan2(Math.sqrt(x * x + z * z), y);
                final double sinphi = Math.sin(phi);
                final double cosphi = Math.cos(phi);
                final double rho = rand.nextDouble() * Math.PI * 2.0D;
                final double sinrho = Math.sin(rho);
                final double cosrho = Math.cos(rho);

                for (int j = 0; j < 4; ++j)
                {
                    final double a = 0.0D;
                    final double b = ((j & 2) - 1) * size;
                    final double c = ((j + 1 & 2) - 1) * size;
                    final double d = b * cosrho - c * sinrho;
                    final double e = c * cosrho + b * sinrho;
                    final double dy = d * sinphi + a * cosphi;
                    final double ff = a * sinphi - d * cosphi;
                    final double dx = ff * sinth - e * costh;
                    final double dz = e * sinth + ff * costh;
                    worldRenderer.pos(xx + dx, yy + dy, zz + dz).endVertex();
                }
            }
        }
    }
}
