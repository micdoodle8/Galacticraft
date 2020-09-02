//package micdoodle8.mods.galacticraft.planets.venus.client;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.BufferBuilder;
//import net.minecraft.client.renderer.GLAllocation;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.client.world.ClientWorld;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.Vec3d;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.IRenderHandler;
//import org.lwjgl.opengl.GL11;
//
//import java.util.Random;
//
//@OnlyIn(Dist.CLIENT)
//public class SkyProviderVenus implements IRenderHandler
//{
//    private static final ResourceLocation overworldTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/earth.png");
//    private static final ResourceLocation sunTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/planets/atmosphericsun.png");
//
//    public int starList;
//    public int glSkyList;
//    public int glSkyList2;
//    private final float sunSize;
//
//    public SkyProviderVenus(IGalacticraftDimension worldProvider)
//    {
//        this.sunSize = 30.0F * worldProvider.getSolarSize();
//
//        int displayLists = GLAllocation.generateDisplayLists(3);
//        this.starList = displayLists;
//        this.glSkyList = displayLists + 1;
//        this.glSkyList2 = displayLists + 2;
//
//        // Bind stars to display list
//        RenderSystem.pushMatrix();
//        RenderSystem.NewList(this.starList, GL11.GL_COMPILE);
//        this.renderStars();
//        GL11.glEndList();
//        RenderSystem.popMatrix();
//
//        final Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tessellator.getBuffer();
//        RenderSystem.NewList(this.glSkyList, GL11.GL_COMPILE);
//        final byte byte2 = 64;
//        final int i = 256 / byte2 + 2;
//        float f = 16F;
//
//        for (int j = -byte2 * i; j <= byte2 * i; j += byte2)
//        {
//            for (int l = -byte2 * i; l <= byte2 * i; l += byte2)
//            {
//                worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                worldRenderer.pos(j + 0, f, l + 0).endVertex();
//                worldRenderer.pos(j + byte2, f, l + 0).endVertex();
//                worldRenderer.pos(j + byte2, f, l + byte2).endVertex();
//                worldRenderer.pos(j + 0, f, l + byte2).endVertex();
//                tessellator.draw();
//            }
//        }
//
//        GL11.glEndList();
//        RenderSystem.NewList(this.glSkyList2, GL11.GL_COMPILE);
//        f = -16F;
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//
//        for (int k = -byte2 * i; k <= byte2 * i; k += byte2)
//        {
//            for (int i1 = -byte2 * i; i1 <= byte2 * i; i1 += byte2)
//            {
//                worldRenderer.pos(k + byte2, f, i1 + 0).endVertex();
//                worldRenderer.pos(k + 0, f, i1 + 0).endVertex();
//                worldRenderer.pos(k + 0, f, i1 + byte2).endVertex();
//                worldRenderer.pos(k + byte2, f, i1 + byte2).endVertex();
//            }
//        }
//
//        tessellator.draw();
//        GL11.glEndList();
//    }
//
//    @Override
//    public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc)
//    {
//        RenderSystem.disableTexture();
//        RenderSystem.disableRescaleNormal();
//        Vec3d vec3 = world.getSkyColor(mc.gameRenderer.getActiveRenderInfo().getBlockPos(), partialTicks);
//        float f1 = (float) vec3.x;
//        float f2 = (float) vec3.y;
//        float f3 = (float) vec3.z;
//        float f6;
//
////        if (mc.gameSettings.anaglyph)
////        {
////            float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
////            float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
////            f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
////            f1 = f4;
////            f2 = f5;
////            f3 = f6;
////        }
//
//        RenderSystem.color3f(f1, f2, f3);
//        Tessellator tessellator1 = Tessellator.getInstance();
//        BufferBuilder worldRenderer1 = tessellator1.getBuffer();
//        RenderSystem.depthMask(false);
//        RenderSystem.enable(GL11.GL_FOG);
//        RenderSystem.color3f(f1, f2, f3);
//        RenderSystem.callList(this.glSkyList);
//        RenderSystem.disable(GL11.GL_FOG);
//        RenderSystem.disableAlphaTest();
//        RenderSystem.enableBlend();
//        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
//        RenderHelper.disableStandardItemLighting();
//        float f7;
//        float f8;
//        float f9;
//        float f10;
//
//        float f18 = world.getStarBrightness(partialTicks);
//
//        if (f18 > 0.0F)
//        {
//            RenderSystem.pushMatrix();
//            RenderSystem.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//            RenderSystem.rotatef(-world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
//            RenderSystem.rotatef(-19.0F, 0, 1.0F, 0);
//            RenderSystem.color4f(f18, f18, f18, f18);
//            RenderSystem.callList(this.starList);
//            RenderSystem.popMatrix();
//        }
//
//        float[] afloat = new float[4];
//        RenderSystem.disableTexture();
//        RenderSystem.shadeModel(7425);
//        RenderSystem.pushMatrix();
//        RenderSystem.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//        RenderSystem.rotatef(-world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
//        afloat[0] = 255 / 255.0F;
//        afloat[1] = 194 / 255.0F;
//        afloat[2] = 180 / 255.0F;
//        afloat[3] = 0.3F;
//        f6 = afloat[0];
//        f7 = afloat[1];
//        f8 = afloat[2];
//        float f11;
//
////        if (mc.gameSettings.anaglyph)
////        {
////            f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
////            f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
////            f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
////            f6 = f9;
////            f7 = f10;
////            f8 = f11;
////        }
//
//        f18 = 1.0F - f18;
//
//        worldRenderer1.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
//        float r = f6 * f18;
//        float g = f7 * f18;
//        float b = f8 * f18;
//        float a = afloat[3] * 2 / f18;
//        worldRenderer1.pos(0.0D, 100.0D, 0.0D).color(r, g, b, a).endVertex();
//        byte b0 = 16;
//        r = afloat[0] * f18;
//        g = afloat[1] * f18;
//        b = afloat[2] * f18 / 20.0F;
//        a = 0.0F;
//
//        // Render sun aura
//        f10 = 20.0F;
//        worldRenderer1.pos(-f10, 100.0D, -f10).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(0, 100.0D, (double) -f10 * 1.5F).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(f10, 100.0D, -f10).color(r, g, b, a).endVertex();
//        worldRenderer1.pos((double) f10 * 1.5F, 100.0D, 0).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(f10, 100.0D, f10).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(0, 100.0D, (double) f10 * 1.5F).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(-f10, 100.0D, f10).color(r, g, b, a).endVertex();
//        worldRenderer1.pos((double) -f10 * 1.5F, 100.0D, 0).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(-f10, 100.0D, -f10).color(r, g, b, a).endVertex();
//
//        tessellator1.draw();
//        worldRenderer1.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
//        r = f6 * f18;
//        g = f7 * f18;
//        b = f8 * f18;
//        a = afloat[3] * f18;
//        worldRenderer1.pos(0.0D, 100.0D, 0.0D).color(r, g, b, a).endVertex();
//        r = afloat[0] * f18;
//        g = afloat[1] * f18;
//        b = afloat[2] * f18;
//        a = 0.0F;
//
//        // Render larger sun aura
//        f10 = 40.0F;
//        worldRenderer1.pos(-f10, 100.0D, -f10).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(0, 100.0D, (double) -f10 * 1.5F).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(f10, 100.0D, -f10).color(r, g, b, a).endVertex();
//        worldRenderer1.pos((double) f10 * 1.5F, 100.0D, 0).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(f10, 100.0D, f10).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(0, 100.0D, (double) f10 * 1.5F).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(-f10, 100.0D, f10).color(r, g, b, a).endVertex();
//        worldRenderer1.pos((double) -f10 * 1.5F, 100.0D, 0).color(r, g, b, a).endVertex();
//        worldRenderer1.pos(-f10, 100.0D, -f10).color(r, g, b, a).endVertex();
//
//        tessellator1.draw();
//        RenderSystem.popMatrix();
//        RenderSystem.shadeModel(7424);
//
//        RenderSystem.enableTexture();
//        RenderSystem.blendFuncSeparate(770, 1, 1, GL11.GL_ZERO);
//        RenderSystem.pushMatrix();
//        f7 = 0.0F;
//        f8 = 0.0F;
//        f9 = 0.0F;
//        RenderSystem.translatef(f7, f8, f9);
//        RenderSystem.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//        RenderSystem.rotatef(-world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
//        // Render sun
//        RenderSystem.disableTexture();
//        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
//        //Some blanking to conceal the stars
//        f10 = this.sunSize / 3.5F;
//        worldRenderer1.begin(7, DefaultVertexFormats.POSITION);
//        worldRenderer1.pos(-f10, 99.9D, -f10).endVertex();
//        worldRenderer1.pos(f10, 99.9D, -f10).endVertex();
//        worldRenderer1.pos(f10, 99.9D, f10).endVertex();
//        worldRenderer1.pos(-f10, 99.9D, f10).endVertex();
//        tessellator1.draw();
//        RenderSystem.enableTexture();
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.1F);
//        f10 = this.sunSize;
//        mc.textureManager.bindTexture(SkyProviderVenus.sunTexture);
//        worldRenderer1.begin(7, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer1.pos(-f10, 100.0D, -f10).tex(0.0D, 0.0D).endVertex();
//        worldRenderer1.pos(f10, 100.0D, -f10).tex(1.0D, 0.0D).endVertex();
//        worldRenderer1.pos(f10, 100.0D, f10).tex(1.0D, 1.0D).endVertex();
//        worldRenderer1.pos(-f10, 100.0D, f10).tex(0.0D, 1.0D).endVertex();
//        tessellator1.draw();
//
//        // Render earth
//        f10 = 0.5F;
//        RenderSystem.scalef(0.6F, 0.6F, 0.6F);
//        RenderSystem.rotatef(40.0F, 0.0F, 0.0F, 1.0F);
//        RenderSystem.rotatef(200F, 1.0F, 0.0F, 0.0F);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
//        Minecraft.getInstance().textureManager.bindTexture(SkyProviderVenus.overworldTexture);
//        worldRenderer1.begin(7, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer1.pos(-f10, -100.0D, f10).tex(0, 1.0).endVertex();
//        worldRenderer1.pos(f10, -100.0D, f10).tex(1.0, 1.0).endVertex();
//        worldRenderer1.pos(f10, -100.0D, -f10).tex(1.0, 0).endVertex();
//        worldRenderer1.pos(-f10, -100.0D, -f10).tex(0, 0).endVertex();
//        tessellator1.draw();
//
//        RenderSystem.disableTexture();
//
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.disableBlend();
//        RenderSystem.enableAlphaTest();
//        RenderSystem.enable(GL11.GL_FOG);
//        RenderSystem.popMatrix();
//        RenderSystem.disableTexture();
//        RenderSystem.color3f(0.0F, 0.0F, 0.0F);
//        double d0 = mc.player.getPosition().getY() - world.getHorizon();
//
//        if (d0 < 0.0D)
//        {
//            RenderSystem.pushMatrix();
//            RenderSystem.translatef(0.0F, 12.0F, 0.0F);
//            RenderSystem.callList(this.glSkyList2);
//            RenderSystem.popMatrix();
//            f8 = 1.0F;
//            f9 = -((float) (d0 + 65.0D));
//            f10 = -f8;
//            worldRenderer1.begin(7, DefaultVertexFormats.POSITION_COLOR);
//            worldRenderer1.pos(-f8, f9, f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(f8, f9, f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(f8, f10, f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(-f8, f10, f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(-f8, f10, -f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(f8, f10, -f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(f8, f9, -f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(-f8, f9, -f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(f8, f10, -f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(f8, f10, f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(f8, f9, f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(f8, f9, -f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(-f8, f9, -f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(-f8, f9, f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(-f8, f10, f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(-f8, f10, -f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(-f8, f10, -f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(-f8, f10, f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(f8, f10, f8).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer1.pos(f8, f10, -f8).color(0, 0, 0, 1.0F).endVertex();
//            tessellator1.draw();
//        }
//
//        if (world.getDimension().isSkyColored())
//        {
//            RenderSystem.color3f(f1 * 0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
//        }
//        else
//        {
//            RenderSystem.color3f(f1, f2, f3);
//        }
//
//        RenderSystem.pushMatrix();
//        RenderSystem.translatef(0.0F, -((float) (d0 - 16.0D)), 0.0F);
//        RenderSystem.callList(this.glSkyList2);
//        RenderSystem.popMatrix();
//        RenderSystem.enableTexture();
//        RenderSystem.enableRescaleNormal();
//        RenderSystem.enable(GL11.GL_COLOR_MATERIAL);
//        RenderSystem.blendFuncSeparate(770, 771, 1, GL11.GL_ZERO);
//        RenderSystem.depthMask(true);
//        RenderSystem.blendFunc(770, 771);
//        RenderSystem.disableBlend();
//    }
//
//    private void renderStars()
//    {
//        final Random rand = new Random(10842L);
//        final Tessellator var2 = Tessellator.getInstance();
//        BufferBuilder worldRenderer = var2.getBuffer();
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//
//        for (int starIndex = 0; starIndex < (ConfigManagerCore.INSTANCE.moreStars.get() ? 35000 : 6000); ++starIndex)
//        {
//            double var4 = rand.nextFloat() * 2.0F - 1.0F;
//            double var6 = rand.nextFloat() * 2.0F - 1.0F;
//            double var8 = rand.nextFloat() * 2.0F - 1.0F;
//            final double var10 = 0.15F + rand.nextFloat() * 0.1F;
//            double var12 = var4 * var4 + var6 * var6 + var8 * var8;
//
//            if (var12 < 1.0D && var12 > 0.01D)
//            {
//                var12 = 1.0D / Math.sqrt(var12);
//                var4 *= var12;
//                var6 *= var12;
//                var8 *= var12;
//                final double var14 = var4 * (ConfigManagerCore.INSTANCE.moreStars ? rand.nextDouble() * 150D + 130D : 100.0D);
//                final double var16 = var6 * (ConfigManagerCore.INSTANCE.moreStars ? rand.nextDouble() * 150D + 130D : 100.0D);
//                final double var18 = var8 * (ConfigManagerCore.INSTANCE.moreStars ? rand.nextDouble() * 150D + 130D : 100.0D);
//                final double var20 = Math.atan2(var4, var8);
//                final double var22 = Math.sin(var20);
//                final double var24 = Math.cos(var20);
//                final double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
//                final double var28 = Math.sin(var26);
//                final double var30 = Math.cos(var26);
//                final double var32 = rand.nextDouble() * Math.PI * 2.0D;
//                final double var34 = Math.sin(var32);
//                final double var36 = Math.cos(var32);
//
//                for (int var38 = 0; var38 < 4; ++var38)
//                {
//                    final double var39 = 0.0D;
//                    final double var41 = ((var38 & 2) - 1) * var10;
//                    final double var43 = ((var38 + 1 & 2) - 1) * var10;
//                    final double var47 = var41 * var36 - var43 * var34;
//                    final double var49 = var43 * var36 + var41 * var34;
//                    final double var53 = var47 * var28 + var39 * var30;
//                    final double var55 = var39 * var28 - var47 * var30;
//                    final double var57 = var55 * var22 - var49 * var24;
//                    final double var61 = var49 * var22 + var55 * var24;
//                    worldRenderer.pos(var14 + var57, var16 + var53, var18 + var61).endVertex();
//                }
//            }
//        }
//
//        var2.draw();
//    }
//
//    private Vec3d getCustomSkyColor()
//    {
//        return new Vec3d(0.26796875D, 0.1796875D, 0.0D);
//    }
//
//    public float getSkyBrightness(float par1)
//    {
//        final float var2 = Minecraft.getInstance().world.getCelestialAngle(par1);
//        float var3 = 1.0F - (MathHelper.sin(var2 * Constants.twoPI) * 2.0F + 0.25F);
//
//        if (var3 < 0.0F)
//        {
//            var3 = 0.0F;
//        }
//
//        if (var3 > 1.0F)
//        {
//            var3 = 1.0F;
//        }
//
//        return var3 * var3 * 1F;
//    }
//}
