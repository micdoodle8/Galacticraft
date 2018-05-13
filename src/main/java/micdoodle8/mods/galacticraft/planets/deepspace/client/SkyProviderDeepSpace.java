package micdoodle8.mods.galacticraft.planets.deepspace.client;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.render.RenderPlanet;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.deepspace.DeepSpaceModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Random;

public class SkyProviderDeepSpace extends IRenderHandler
{
    private static final ResourceLocation sunTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/planets/orbitalsun.png");
    private float sunSize;

    public int starGLCallList = GLAllocation.generateDisplayLists(3);
    public int glSkyList;
    public int glSkyList2;
    private final ResourceLocation planetToRender;
    private final boolean renderSun;
    public float spinAngle = 0;
    public float spinDeltaPerTick = 0;
    private float prevPartialTicks = 0;
    private long prevTick;

    public SkyProviderDeepSpace(ResourceLocation planet, boolean renderMoon, boolean renderSun)
    {
        this.sunSize = 17.5F / 1.2F / DeepSpaceModule.stationDeepSpace.getRelativeDistanceFromCenter().unScaledDistance;
        this.planetToRender = planet;
        this.renderSun = renderSun;
        GL11.glPushMatrix();
        GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
        this.renderStars();
        GL11.glEndList();
        GL11.glPopMatrix();
        final Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldRenderer = tessellator.getBuffer();
        this.glSkyList = this.starGLCallList + 1;
        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
        final byte byte2 = 64;
        final int i = 256 / byte2 + 2;
        float f = 16F;

        for (int j = -byte2 * i; j <= byte2 * i; j += byte2)
        {
            for (int l = -byte2 * i; l <= byte2 * i; l += byte2)
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                worldRenderer.pos(j + 0, f, l + 0).endVertex();
                worldRenderer.pos(j + byte2, f, l + 0).endVertex();
                worldRenderer.pos(j + byte2, f, l + byte2).endVertex();
                worldRenderer.pos(j + 0, f, l + byte2).endVertex();
                tessellator.draw();
            }
        }

        GL11.glEndList();
        this.glSkyList2 = this.starGLCallList + 2;
        GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
        f = -16F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        for (int k = -byte2 * i; k <= byte2 * i; k += byte2)
        {
            for (int i1 = -byte2 * i; i1 <= byte2 * i; i1 += byte2)
            {
                worldRenderer.pos(k + byte2, f, i1 + 0).endVertex();
                worldRenderer.pos(k + 0, f, i1 + 0).endVertex();
                worldRenderer.pos(k + 0, f, i1 + byte2).endVertex();
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
        final float var20 = 400.0F + (float) this.minecraft.thePlayer.posY / 2F;
        float ticks = partialTicks + world.getWorldTime();

        // if (this.minecraft.thePlayer.getRidingEntity() != null)
        {
            // var20 = (float) (this.minecraft.thePlayer.posY - 200.0F);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        final Vec3d var2 = this.minecraft.theWorld.getSkyColor(this.minecraft.getRenderViewEntity(), partialTicks);
        float var3 = (float) var2.xCoord;
        float var4 = (float) var2.yCoord;
        float var5 = (float) var2.zCoord;
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
        final Tessellator var23 = Tessellator.getInstance();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(var3, var4, var5);
        GL11.glCallList(this.glSkyList);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();
        float var9;
        float var10;
        float var11;
        float var12;

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glPushMatrix();
        float skyAngle = 15F / 16F * TransformerHooksClient.preViewRender((EntityLivingBase) ClientProxyCore.mc.getRenderViewEntity(), partialTicks, -1F);
        while (skyAngle < 0F) skyAngle += 360F;
        while (skyAngle > 360F) skyAngle -= 360F;
        GL11.glRotatef(skyAngle, 1F, 0.0F, 0.0F);

        var8 = 1.0F - this.minecraft.theWorld.getRainStrength(partialTicks);
        var9 = 0.0F;
        var10 = 0.0F;
        var11 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var8);
        GL11.glTranslatef(var9, var10, var11);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

        //Code for rendering spinning spacestations
        float deltaTick = partialTicks - this.prevPartialTicks;
        //while (deltaTick < 0F) deltaTick += 1.0F;
        this.prevPartialTicks = partialTicks;
        long curTick = this.minecraft.theWorld.getTotalWorldTime();
        int tickDiff = (int) (curTick - this.prevTick);
        this.prevTick = curTick;
        if (tickDiff > 0 && tickDiff < 20)
        {
            deltaTick += tickDiff;
        }
        this.spinAngle = this.spinAngle - this.spinDeltaPerTick * deltaTick;
        while (this.spinAngle < -180F)
        {
            this.spinAngle += 360F;
        }
        GL11.glRotatef(this.spinAngle, 0.0F, 1.0F, 0.0F);

        //At 0.8, these will look bright against a black sky - allows some headroom for them to
        //look even brighter in outer dimensions (further from the sun)
        GL11.glColor4f(0.9F, 0.9F, 0.9F, 0.9F);
        GL11.glCallList(this.starGLCallList);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glPushMatrix();
        float celestialAngle = this.minecraft.theWorld.getCelestialAngle(partialTicks);
        GL11.glRotatef(celestialAngle * 360.0F, 1.0F, 0.0F, 0.0F);
        if (this.renderSun)
        {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
            var12 = this.sunSize * 10F;
            VertexBuffer worldRenderer = var23.getBuffer();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldRenderer.pos(-var12, 99.9D, -var12).endVertex();
            worldRenderer.pos(var12, 99.9D, -var12).endVertex();
            worldRenderer.pos(var12, 99.9D, var12).endVertex();
            worldRenderer.pos(-var12, 99.9D, var12).endVertex();
            var23.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            var12 = this.sunSize * 10F;
            this.minecraft.renderEngine.bindTexture(SkyProviderDeepSpace.sunTexture);
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-var12, 100.0D, -var12).tex(0.0D, 0.0D).endVertex();
            worldRenderer.pos(var12, 100.0D, -var12).tex(1.0D, 0.0D).endVertex();
            worldRenderer.pos(var12, 100.0D, var12).tex(1.0D, 1.0D).endVertex();
            worldRenderer.pos(-var12, 100.0D, var12).tex(0.0D, 1.0D).endVertex();
            var23.draw();
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);

        if (this.planetToRender != null)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 30F, 0.0F);
            GL11.glRotatef(109F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -var20 / 10, 0.0F);
            float scale = 100 * (0.3F - var20 / 10000.0F);
            scale = Math.max(scale, 0.2F);
            GL11.glScalef(scale, 0.0F, scale);
            GL11.glTranslatef(0.0F, -var20, 0.0F);
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);

            final float alpha = 0.6F;
            GL11.glColor4f(Math.min(alpha, 1.0F), Math.min(alpha, 1.0F), Math.min(alpha, 1.0F), Math.min(alpha, 1.0F));
            RenderPlanet.renderID(3, 4.0F, ticks / 8F);

            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glColor3f(0.0F, 0.0F, 0.0F);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glDepthMask(true);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderStars()
    {
        final Random var1 = new Random(10842L);
        final Tessellator var2 = Tessellator.getInstance();
        var2.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        for (int var3 = 0; var3 < (ConfigManagerCore.moreStars ? 20000 : 6000); ++var3)
        {
            double var4 = var1.nextFloat() * 2.0F - 1.0F;
            double var6 = var1.nextFloat() * 2.0F - 1.0F;
            double var8 = var1.nextFloat() * 2.0F - 1.0F;
            final double var10 = 0.07F + var1.nextFloat() * 0.06F;
            double var12 = var4 * var4 + var6 * var6 + var8 * var8;

            if (var12 < 1.0D && var12 > 0.01D)
            {
                var12 = 1.0D / Math.sqrt(var12);
                var4 *= var12;
                var6 *= var12;
                var8 *= var12;
                final double var14 = var4 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 50D + 75D : 50.0D);
                final double var16 = var6 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 50D + 75D : 50.0D);
                final double var18 = var8 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 50D + 75D : 50.0D);
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
                    var2.getBuffer().pos(var14 + var57, var16 + var53, var18 + var61).endVertex();
                }
            }
        }

        var2.draw();
    }
}
