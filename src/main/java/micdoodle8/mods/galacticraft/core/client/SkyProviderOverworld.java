package micdoodle8.mods.galacticraft.core.client;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.VersionUtil;
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

    public int starGLCallList = GLAllocation.generateDisplayLists(3);
    public int glSkyList;
    public int glSkyList2;
    private final ResourceLocation planetToRender = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png");

    public SkyProviderOverworld()
    {
        GL11.glPushMatrix();
        GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
        this.renderStars();
        GL11.glEndList();
        GL11.glPopMatrix();
        final Tessellator tessellator = Tessellator.instance;
        this.glSkyList = this.starGLCallList + 1;
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
        this.glSkyList2 = this.starGLCallList + 2;
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
        
        float var20 = (float) (mc.thePlayer.posY - 200.0F) / 1000.0F;
        final float var21 = Math.max(1.0F - var20 * 4.0F, 0.0F);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        final Vec3 var2 = this.minecraft.theWorld.getSkyColor(this.minecraft.renderViewEntity, partialTicks);
        float var3 = (float) var2.xCoord * var21;
        float var4 = (float) var2.yCoord * var21;
        float var5 = (float) var2.zCoord * var21;
        float var8;

        if (this.minecraft.gameSettings.anaglyph)
        {
            final float var6 = (var3 * 30.0F + var4 * 59.0F + var5 * 11.0F) / 100.0F;
            final float var7 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
            var8 = (var3 * 30.0F + var5 * 70.0F) / 100.0F;
            var3 = var6;
            var4 = var7;
            var5 = var8;
        }

        GL11.glColor3f(var3, var4, var5);
        final Tessellator var23 = Tessellator.instance;
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(var3, var4, var5);
        if (mc.thePlayer.posY < 214)
        {
            GL11.glCallList(this.glSkyList);
        }
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();
        final float[] var24 = this.minecraft.theWorld.provider.calcSunriseSunsetColors(this.minecraft.theWorld.getCelestialAngle(partialTicks), partialTicks);
        float var9;
        float var10;
        float var11;
        float var12;

        if (var24 != null && mc.thePlayer.posY < 250)
        {
            float sunsetMod = (float) (FMLClientHandler.instance().getClient().thePlayer.posY - 200.0F) / 1000.0F;
            final float sunsetModInv = Math.min(1.0F, Math.max(1.0F - var20 * 50.0F, 0.0F));
            
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(MathHelper.sin(this.minecraft.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            var8 = var24[0] * sunsetModInv;
            var9 = var24[1] * sunsetModInv;
            var10 = var24[2] * sunsetModInv;
            float var13;

            if (this.minecraft.gameSettings.anaglyph)
            {
                var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
                var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
                var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
                var8 = var11;
                var9 = var12;
                var10 = var13;
            }

            var23.startDrawing(6);

            var23.setColorRGBA_F(var8 * sunsetModInv, var9 * sunsetModInv, var10 * sunsetModInv, var24[3]);
            var23.addVertex(0.0D, 100.0D, 0.0D);
            final byte var26 = 16;
            var23.setColorRGBA_F(var24[0] * sunsetModInv, var24[1] * sunsetModInv, var24[2] * sunsetModInv, 0.0F);

            for (int var27 = 0; var27 <= var26; ++var27)
            {
                var13 = var27 * (float) Math.PI * 2.0F / var26;
                final float var14 = MathHelper.sin(var13);
                final float var15 = MathHelper.cos(var13);
                var23.addVertex(var14 * 120.0F, var15 * 120.0F, -var15 * 40.0F * var24[3]);
            }

            var23.draw();
            GL11.glPopMatrix();
            GL11.glShadeModel(GL11.GL_FLAT);
        }
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        
        GL11.glPushMatrix();
        var8 = 1.0F - this.minecraft.theWorld.getRainStrength(partialTicks);
        var9 = 0.0F;
        var10 = 0.0F;
        var11 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var8);
        GL11.glTranslatef(var9, var10, var11);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

        GL11.glRotatef(this.minecraft.theWorld.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
        var12 = 30.0F;
        this.minecraft.renderEngine.bindTexture(SkyProviderOverworld.sunTexture);
        var23.startDrawingQuads();
        var23.addVertexWithUV(-var12, 100.0D, -var12, 0.0D, 0.0D);
        var23.addVertexWithUV(var12, 100.0D, -var12, 1.0D, 0.0D);
        var23.addVertexWithUV(var12, 100.0D, var12, 1.0D, 1.0D);
        var23.addVertexWithUV(-var12, 100.0D, var12, 0.0D, 1.0D);
        var23.draw();

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);        
        var12 = 11.3F;
        var23.startDrawingQuads();
        var23.addVertex(-var12, -99.9D, var12);
        var23.addVertex(var12, -99.9D, var12);
        var23.addVertex(var12, -99.9D, -var12);
        var23.addVertex(-var12, -99.9D, -var12);
        var23.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        var12 = 40.0F;
        this.minecraft.renderEngine.bindTexture(SkyProviderOverworld.moonTexture);
        float var28 = this.minecraft.theWorld.getMoonPhase();
        final int var30 = (int) (var28 % 4);
        final int var29 = (int) (var28 / 4 % 2);
        final float var16 = (var30 + 0) / 4.0F;
        final float var17 = (var29 + 0) / 2.0F;
        final float var18 = (var30 + 1) / 4.0F;
        final float var19 = (var29 + 1) / 2.0F;
        var23.startDrawingQuads();
        var23.addVertexWithUV(-var12, -100.0D, var12, var18, var19);
        var23.addVertexWithUV(var12, -100.0D, var12, var16, var19);
        var23.addVertexWithUV(var12, -100.0D, -var12, var16, var17);
        var23.addVertexWithUV(-var12, -100.0D, -var12, var18, var17);
        var23.draw();

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glCallList(this.starGLCallList);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(0.0F, 0.0F, 0.0F);

        //TODO get exact height figure here
        double var25 = this.minecraft.thePlayer.posY - 64;

        if (var25 > this.minecraft.gameSettings.renderDistanceChunks * 16)
        {
	        var20 *= 400.0F;
	
	        final float var22 = Math.max(Math.min(var20 / 100.0F - 0.2F, 0.5F), 0.0F);
	
	        GL11.glPushMatrix();
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_FOG);
	        float scale = 850 * (0.25F - var20 / 10000.0F);
	        scale = Math.max(scale, 0.2F);
	        GL11.glScalef(scale, 1.0F, scale);
	        GL11.glTranslatef(0.0F, -(float)mc.thePlayer.posY, 0.0F);
	        if (ClientProxyCore.overworldTextureLocal != null)
	        {
	            GL11.glBindTexture(GL11.GL_TEXTURE_2D, ClientProxyCore.overworldTextureLocal.getGlTextureId());
	        }
	        else
	        {
	            this.minecraft.renderEngine.bindTexture(this.planetToRender);
	        }
	
	        var10 = 1.0F;
	
	        GL11.glColor4f(var22, var22, var22, 1.0F);
	        var23.startDrawingQuads();
	
	        float zoomIn = (1F - (float) var25 / 768F) / 5.86F;
	        if (zoomIn < 0F) zoomIn = 0F;
	        zoomIn = 0.0F;
	        float cornerB = 1.0F - zoomIn;
	        var23.addVertexWithUV(-var10, 0, var10, zoomIn, cornerB);
	        var23.addVertexWithUV(var10, 0, var10, cornerB, cornerB);
	        var23.addVertexWithUV(var10, 0, -var10, cornerB, zoomIn);
	        var23.addVertexWithUV(-var10, 0, -var10, zoomIn, zoomIn);
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

    private void renderStars()
    {
        final Random var1 = new Random(10842L);
        final Tessellator var2 = Tessellator.instance;
        var2.startDrawingQuads();

        for (int var3 = 0; var3 < (ConfigManagerCore.moreStars ? 20000 : 6000); ++var3)
        {
            double var4 = var1.nextFloat() * 2.0F - 1.0F;
            double var6 = var1.nextFloat() * 2.0F - 1.0F;
            double var8 = var1.nextFloat() * 2.0F - 1.0F;
            final double var10 = 0.15F + var1.nextFloat() * 0.1F;
            double var12 = var4 * var4 + var6 * var6 + var8 * var8;

            if (var12 < 1.0D && var12 > 0.01D)
            {
                var12 = 1.0D / Math.sqrt(var12);
                var4 *= var12;
                var6 *= var12;
                var8 *= var12;
                final double var14 = var4 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 100D + 150D : 100.0D);
                final double var16 = var6 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 100D + 150D : 100.0D);
                final double var18 = var8 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 100D + 150D : 100.0D);
                final double var20 = Math.atan2(var4, var8);
                final double var22 = Math.sin(var20);
                final double var24 = Math.cos(var20);
                final double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
                final double var28 = Math.sin(var26);
                final double var30 = Math.cos(var26);
                final double var32 = var1.nextDouble() * Math.PI * 2.0D;
                final double var34 = Math.sin(var32);
                final double var36 = Math.cos(var32);

                for (int var38 = 0; var38 < 4; ++var38)
                {
                    final double var39 = 0.0D;
                    final double var41 = ((var38 & 2) - 1) * var10;
                    final double var43 = ((var38 + 1 & 2) - 1) * var10;
                    final double var47 = var41 * var36 - var43 * var34;
                    final double var49 = var43 * var36 + var41 * var34;
                    final double var53 = var47 * var28 + var39 * var30;
                    final double var55 = var39 * var28 - var47 * var30;
                    final double var57 = var55 * var22 - var49 * var24;
                    final double var61 = var49 * var22 + var55 * var24;
                    var2.addVertex(var14 + var57, var16 + var53, var18 + var61);
                }
            }
        }

        var2.draw();
    }
}
