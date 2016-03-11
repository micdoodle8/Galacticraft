package micdoodle8.mods.galacticraft.core.client;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.VersionUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.Project;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

public class SkyProviderOverworld extends IRenderHandler
{
    private static final ResourceLocation moonTexture = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation sunTexture = new ResourceLocation("textures/environment/sun.png");
    
    private static boolean optifinePresent = false;
    
    static
    {
        try {
            optifinePresent = Launch.classLoader.getClassBytes("CustomColorizer") != null;
        } catch (final Exception e) { }
    }

    public int starGLCallList = GLAllocation.generateDisplayLists(7);
    public int glSkyList;
    public int glSkyList2;
    private final ResourceLocation planetToRender = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png");

    public SkyProviderOverworld()
    {
        GL11.glPushMatrix();
        final Random rand = new Random(10842L);
        GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
        this.renderStars(rand);
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 1, GL11.GL_COMPILE);
        this.renderStars(rand);
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 2, GL11.GL_COMPILE);
        this.renderStars(rand);
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 3, GL11.GL_COMPILE);
        this.renderStars(rand);
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 4, GL11.GL_COMPILE);
        this.renderStars(rand);
        GL11.glEndList();
        GL11.glPopMatrix();
        final Tessellator tessellator = Tessellator.instance;
        this.glSkyList = this.starGLCallList + 5;
        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
        final byte byte2 = 5;
        final int i = 256 / byte2 + 2;
        float f = 16F;

        for (int j = -byte2 * i; j <= byte2 * i; j += byte2)
        {
            for (int l = -byte2 * i; l <= byte2 * i; l += byte2)
            {
                tessellator.startDrawingQuads();
                tessellator.addVertex(j + 0, f, l + 0);
                tessellator.addVertex(j + byte2, f, l + 0);
                tessellator.addVertex(j + byte2, f, l + byte2);
                tessellator.addVertex(j + 0, f, l + byte2);
                tessellator.draw();
            }
        }

        GL11.glEndList();
        this.glSkyList2 = this.starGLCallList + 6;
        GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
        f = -16F;
        tessellator.startDrawingQuads();

        for (int k = -byte2 * i; k <= byte2 * i; k += byte2)
        {
            for (int i1 = -byte2 * i; i1 <= byte2 * i; i1 += byte2)
            {
                tessellator.addVertex(k + byte2, f, i1 + 0);
                tessellator.addVertex(k + 0, f, i1 + 0);
                tessellator.addVertex(k + 0, f, i1 + byte2);
                tessellator.addVertex(k + byte2, f, i1 + byte2);
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
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, new Object[] {}));
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
	        	Field cameraZoom = c.getDeclaredField(VersionUtil.getNameDynamic(VersionUtil.KEY_FIELD_CAMERA_ZOOM));
	        	cameraZoom.setAccessible(true);
	        	zoom = cameraZoom.getDouble(mc.entityRenderer);
	        	Field cameraYaw = c.getDeclaredField(VersionUtil.getNameDynamic(VersionUtil.KEY_FIELD_CAMERA_YAW));
	        	cameraYaw.setAccessible(true);
	        	yaw = cameraYaw.getDouble(mc.entityRenderer);
	        	Field cameraPitch = c.getDeclaredField(VersionUtil.getNameDynamic(VersionUtil.KEY_FIELD_CAMERA_PITCH));
	        	cameraPitch.setAccessible(true);
	        	pitch = cameraPitch.getDouble(mc.entityRenderer);
	        	
	            GL11.glMatrixMode(GL11.GL_PROJECTION);
	            GL11.glLoadIdentity();
	
	            if (zoom != 1.0D)
	            {
	                GL11.glTranslatef((float)yaw, (float)(-pitch), 0.0F);
	                GL11.glScaled(zoom, zoom, 1.0D);
	            }

	            Project.gluPerspective(mc.gameSettings.fovSetting, (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, 1400.0F);
	            GL11.glMatrixMode(GL11.GL_MODELVIEW);
	            GL11.glLoadIdentity();
            
	        	m = c.getDeclaredMethod(VersionUtil.getNameDynamic(VersionUtil.KEY_METHOD_ORIENT_CAMERA), float.class);
	        	m.setAccessible(true);
	        	m.invoke(mc.entityRenderer, mc.gameSettings.fovSetting);
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
        }
        
        float theta = MathHelper.sqrt_float(((float) (mc.thePlayer.posY) - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / 1000.0F);
        final float var21 = Math.max(1.0F - theta * 4.0F, 0.0F);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        final Vec3 var2 = this.minecraft.theWorld.getSkyColor(this.minecraft.renderViewEntity, partialTicks);
        float i = (float) var2.xCoord * var21;
        float x = (float) var2.yCoord * var21;
        float var5 = (float) var2.zCoord * var21;
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
        final Tessellator var23 = Tessellator.instance;
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(i, x, var5);
        if (mc.thePlayer.posY < 214)
        {
            GL11.glCallList(this.glSkyList);
        }
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();
        final float[] costh = this.minecraft.theWorld.provider.calcSunriseSunsetColors(this.minecraft.theWorld.getCelestialAngle(partialTicks), partialTicks);
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
            GL11.glRotatef(MathHelper.sin(this.minecraft.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
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

            var23.startDrawing(6);

            var23.setColorRGBA_F(z * sunsetModInv, var9 * sunsetModInv, size * sunsetModInv, costh[3]);
            var23.addVertex(0.0D, 100.0D, 0.0D);
            final byte phi = 16;
            var23.setColorRGBA_F(costh[0] * sunsetModInv, costh[1] * sunsetModInv, costh[2] * sunsetModInv, 0.0F);

            for (int var27 = 0; var27 <= phi; ++var27)
            {
                rand3 = var27 * (float) Math.PI * 2.0F / phi;
                final float xx = MathHelper.sin(rand3);
                final float rand5 = MathHelper.cos(rand3);
                var23.addVertex(xx * 120.0F, rand5 * 120.0F, -rand5 * 40.0F * costh[3]);
            }

            var23.draw();
            GL11.glPopMatrix();
            GL11.glShadeModel(GL11.GL_FLAT);
        }
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        
        GL11.glPushMatrix();
        z = 1.0F - this.minecraft.theWorld.getRainStrength(partialTicks);
        var9 = 0.0F;
        size = 0.0F;
        rand1 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, z);
        GL11.glTranslatef(var9, size, rand1);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

        GL11.glRotatef(this.minecraft.theWorld.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
        double playerHeight = this.minecraft.thePlayer.posY;
        
        //Draw stars
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        float threshold;
        Vec3 vec = WorldUtil.getFogColorHook(this.minecraft.theWorld);
        threshold = Math.max(0.1F, (float) vec.lengthVector() - 0.1F);
        float var20 = ((float) playerHeight - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / 1000.0F;
        var20 = MathHelper.sqrt_float(var20);
        float bright1 = Math.min(0.9F, var20 * 3);
        float bright2 = Math.min(0.85F, var20 * 2.5F);
        float bright3 = Math.min(0.8F, var20 * 2);
        float bright4 = Math.min(0.75F, var20 * 1.5F);
        float bright5 = Math.min(0.7F, var20 * 0.75F);
        if (bright1 > threshold)
        {
	        GL11.glColor4f(bright1, bright1, bright1, 1.0F);
	        GL11.glCallList(this.starGLCallList);
        }
        if (bright2 > threshold)
        {
	        GL11.glColor4f(bright2, bright2, bright2, 1.0F);
	        GL11.glCallList(this.starGLCallList + 1);
        }
        if (bright3 > threshold)
        {
	        GL11.glColor4f(bright3, bright3, bright3, 1.0F);
	        GL11.glCallList(this.starGLCallList + 2);
        }
        if (bright4 > threshold)
        {
	        GL11.glColor4f(bright4, bright4, bright4, 1.0F);
	        GL11.glCallList(this.starGLCallList + 3);
        }
        if (bright5 > threshold)
        {
	        GL11.glColor4f(bright5, bright5, bright5, 1.0F);
	        GL11.glCallList(this.starGLCallList + 4);
        }

        //Draw sun
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        r = 30.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.renderEngine.bindTexture(SkyProviderOverworld.sunTexture);
        var23.startDrawingQuads();
        var23.addVertexWithUV(-r, 100.0D, -r, 0.0D, 0.0D);
        var23.addVertexWithUV(r, 100.0D, -r, 1.0D, 0.0D);
        var23.addVertexWithUV(r, 100.0D, r, 1.0D, 1.0D);
        var23.addVertexWithUV(-r, 100.0D, r, 0.0D, 1.0D);
        var23.draw();

        //Draw moon
        r = 40.0F;
        this.minecraft.renderEngine.bindTexture(SkyProviderOverworld.moonTexture);
        float sinphi = this.minecraft.theWorld.getMoonPhase();
        final int cosphi = (int) (sinphi % 4);
        final int var29 = (int) (sinphi / 4 % 2);
        final float yy = (cosphi + 0) / 4.0F;
        final float rand7 = (var29 + 0) / 2.0F;
        final float zz = (cosphi + 1) / 4.0F;
        final float rand9 = (var29 + 1) / 2.0F;
        var23.startDrawingQuads();
        var23.addVertexWithUV(-r, -100.0D, r, zz, rand9);
        var23.addVertexWithUV(r, -100.0D, r, yy, rand9);
        var23.addVertexWithUV(r, -100.0D, -r, yy, rand7);
        var23.addVertexWithUV(-r, -100.0D, -r, zz, rand7);
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
	        GL11.glTranslatef(0.0F, -(float)mc.thePlayer.posY, 0.0F);
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
	        var23.startDrawingQuads();
	
	        float zoomIn = (1F - (float) var25 / 768F) / 5.86F;
	        if (zoomIn < 0F) zoomIn = 0F;
	        zoomIn = 0.0F;
	        float cornerB = 1.0F - zoomIn;
	        var23.addVertexWithUV(-size, 0, size, zoomIn, cornerB);
	        var23.addVertexWithUV(size, 0, size, cornerB, cornerB);
	        var23.addVertexWithUV(size, 0, -size, cornerB, zoomIn);
	        var23.addVertexWithUV(-size, 0, -size, zoomIn, zoomIn);
	        var23.draw();
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glPopMatrix();
        }

        GL11.glColor3f(0.0f, 0.0f, 0.0f);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);

        if (!optifinePresent && m != null)
        {
	        try
	        {
	            GL11.glMatrixMode(GL11.GL_PROJECTION);
	            GL11.glLoadIdentity();
	
	            if (zoom != 1.0D)
	            {
	                GL11.glTranslatef((float)yaw, (float)(-pitch), 0.0F);
	                GL11.glScaled(zoom, zoom, 1.0D);
	            }
	            
	            Project.gluPerspective(mc.gameSettings.fovSetting, (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, this.minecraft.gameSettings.renderDistanceChunks * 16 * 2.0F);
	            GL11.glMatrixMode(GL11.GL_MODELVIEW);
	            GL11.glLoadIdentity();
            
	        	m.invoke(mc.entityRenderer, mc.gameSettings.fovSetting);
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
        }
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
    }

    private void renderStars(Random rand)
    {
        final Tessellator var2 = Tessellator.instance;
        var2.startDrawingQuads();

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
                	continue;
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
                    var2.addVertex(xx + dx, yy + dy, zz + dz);
                }
            }
        }

        var2.draw();
    }
}
