//package micdoodle8.mods.galacticraft.core.client;
//
//import com.mojang.blaze3d.systems.RenderSystem;
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
//import net.minecraftforge.client.IRenderHandler;
//import org.lwjgl.opengl.GL11;
//
//import java.util.Random;
//
//public class SkyProviderOrbit implements IRenderHandler
//{
//    private static final ResourceLocation moonTexture = new ResourceLocation("textures/environment/moon_phases.png");
//    private static final ResourceLocation sunTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/planets/orbitalsun.png");
//
//    public int starGLCallList = GLAllocation.generateDisplayLists(3);
//    public int glSkyList;
//    public int glSkyList2;
//    private final ResourceLocation planetToRender;
//    private final boolean renderMoon;
//    private final boolean renderSun;
//    public float spinAngle = 0;
//    public float spinDeltaPerTick = 0;
//    private float prevPartialTicks = 0;
//    private long prevTick;
//
//    public SkyProviderOrbit(ResourceLocation planet, boolean renderMoon, boolean renderSun)
//    {
//        this.planetToRender = planet;
//        this.renderMoon = renderMoon;
//        this.renderSun = renderSun;
//        RenderSystem.pushMatrix();
//        RenderSystem.NewList(this.starGLCallList, GL11.GL_COMPILE);
//        this.renderStars();
//        GL11.glEndList();
//        RenderSystem.popMatrix();
//        final Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tessellator.getBuffer();
//        this.glSkyList = this.starGLCallList + 1;
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
//        this.glSkyList2 = this.starGLCallList + 2;
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
//    private final Minecraft minecraft = Minecraft.getInstance();
//
//    @Override
//    public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc)
//    {
//        final float var20 = 400.0F + (float) this.minecraft.player.getPosY() / 2F;
//
//        // if (this.minecraft.player.getRidingEntity() != null)
//        {
//            // var20 = (float) (this.minecraft.player.getPosY() - 200.0F);
//        }
//
//        RenderSystem.disableTexture();
//        RenderSystem.disableRescaleNormal();
//        final Vec3d var2 = this.minecraft.world.getSkyColor(this.minecraft.gameRenderer.getActiveRenderInfo().getBlockPos(), partialTicks);
//        float var3 = (float) var2.x;
//        float var4 = (float) var2.y;
//        float var5 = (float) var2.z;
//        float var8;
//
////        if (this.minecraft.gameSettings.anaglyph)
////        {
////            final float var6 = (var3 * 30.0F + var4 * 59.0F + var5 * 11.0F) / 100.0F;
////            final float var7 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
////            var8 = (var3 * 30.0F + var5 * 70.0F) / 100.0F;
////            var3 = var6;
////            var4 = var7;
////            var5 = var8;
////        } rip
//
//        RenderSystem.color3f(var3, var4, var5);
//        final Tessellator var23 = Tessellator.getInstance();
//        RenderSystem.depthMask(false);
//        RenderSystem.enable(GL11.GL_FOG);
//        RenderSystem.color3f(var3, var4, var5);
//        RenderSystem.callList(this.glSkyList);
//        RenderSystem.disable(GL11.GL_FOG);
//        RenderSystem.disableAlphaTest();
//        RenderSystem.enableBlend();
//        RenderSystem.blendFunc(770, 771);
//        RenderHelper.disableStandardItemLighting();
//        final float[] var24 = this.minecraft.world.getDimension().calcSunriseSunsetColors(this.minecraft.world.getCelestialAngle(partialTicks), partialTicks);
//        float var9;
//        float var10;
//        float var11;
//        float var12;
//
//        if (var24 != null)
//        {
//            RenderSystem.disableTexture();
//            RenderSystem.shadeModel(7425);
//            RenderSystem.pushMatrix();
//            RenderSystem.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
//            RenderSystem.rotatef(MathHelper.sin(this.minecraft.world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
//            RenderSystem.rotatef(90.0F, 0.0F, 0.0F, 1.0F);
//            var8 = var24[0];
//            var9 = var24[1];
//            var10 = var24[2];
//            float var13;
//
////            if (this.minecraft.gameSettings.anaglyph)
////            {
////                var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
////                var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
////                var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
////                var8 = var11;
////                var9 = var12;
////                var10 = var13;
////            }
//
//            BufferBuilder worldRenderer = var23.getBuffer();
//            worldRenderer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
//            worldRenderer.pos(0.0D, 100.0D, 0.0D).color(var8, var9, var10, var24[3]).endVertex();
//            final byte var26 = 16;
//
//            for (int var27 = 0; var27 <= var26; ++var27)
//            {
//                var13 = var27 * Constants.twoPI / var26;
//                final float var14 = MathHelper.sin(var13);
//                final float var15 = MathHelper.cos(var13);
//                worldRenderer.pos(var14 * 120.0F, var15 * 120.0F, -var15 * 40.0F * var24[3]).color(var24[0], var24[1], var24[2], 0.0F).endVertex();
//            }
//
//            var23.draw();
//            RenderSystem.popMatrix();
//            RenderSystem.shadeModel(7424);
//        }
//
//        RenderSystem.blendFunc(770, 1);
//        RenderSystem.pushMatrix();
//        var8 = 1.0F - this.minecraft.world.getRainStrength(partialTicks);
//        var9 = 0.0F;
//        var10 = 0.0F;
//        var11 = 0.0F;
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, var8);
//        RenderSystem.translatef(var9, var10, var11);
//        RenderSystem.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//
//        //Code for rendering spinning spacestations
//        float deltaTick = partialTicks - this.prevPartialTicks;
//        //while (deltaTick < 0F) deltaTick += 1.0F;
//        this.prevPartialTicks = partialTicks;
//        long curTick = this.minecraft.world.getDayTime();
//        int tickDiff = (int) (curTick - this.prevTick);
//        this.prevTick = curTick;
//        if (tickDiff > 0 && tickDiff < 20)
//        {
//            deltaTick += tickDiff;
//        }
//        this.spinAngle = this.spinAngle - this.spinDeltaPerTick * deltaTick;
//        while (this.spinAngle < -180F)
//        {
//            this.spinAngle += 360F;
//        }
//        RenderSystem.rotatef(this.spinAngle, 0.0F, 1.0F, 0.0F);
//
//        //At 0.8, these will look bright against a black sky - allows some headroom for them to
//        //look even brighter in outer dimensions (further from the sun)
//        RenderSystem.color4f(0.8F, 0.8F, 0.8F, 0.8F);
//        RenderSystem.callList(this.starGLCallList);
//
//        RenderSystem.enableTexture();
//
//        RenderSystem.pushMatrix();
//        float celestialAngle = this.minecraft.world.getCelestialAngle(partialTicks);
//        RenderSystem.rotatef(celestialAngle * 360.0F, 1.0F, 0.0F, 0.0F);
//        if (this.renderSun)
//        {
//            RenderSystem.blendFunc(770, 771);
//            RenderSystem.disableTexture();
//            RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
//            var12 = 8.0F;
//            BufferBuilder worldRenderer = var23.getBuffer();
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//            worldRenderer.pos(-var12, 99.9D, -var12).endVertex();
//            worldRenderer.pos(var12, 99.9D, -var12).endVertex();
//            worldRenderer.pos(var12, 99.9D, var12).endVertex();
//            worldRenderer.pos(-var12, 99.9D, var12).endVertex();
//            var23.draw();
//            RenderSystem.enableTexture();
//            RenderSystem.blendFunc(770, 1);
//            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//            var12 = 28.0F;
//            this.minecraft.textureManager.bindTexture(SkyProviderOrbit.sunTexture);
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
//            worldRenderer.pos(-var12, 100.0D, -var12).tex(0.0D, 0.0D).endVertex();
//            worldRenderer.pos(var12, 100.0D, -var12).tex(1.0D, 0.0D).endVertex();
//            worldRenderer.pos(var12, 100.0D, var12).tex(1.0D, 1.0D).endVertex();
//            worldRenderer.pos(-var12, 100.0D, var12).tex(0.0D, 1.0D).endVertex();
//            var23.draw();
//        }
//
//        if (this.renderMoon)
//        {
//            RenderSystem.blendFunc(770, 771);
//            RenderSystem.disableTexture();
//            RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
//            var12 = 11.3F;
//            BufferBuilder worldRenderer = var23.getBuffer();
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//            worldRenderer.pos(-var12, -99.9D, var12).endVertex();
//            worldRenderer.pos(var12, -99.9D, var12).endVertex();
//            worldRenderer.pos(var12, -99.9D, -var12).endVertex();
//            worldRenderer.pos(-var12, -99.9D, -var12).endVertex();
//            var23.draw();
//            RenderSystem.enableTexture();
//            RenderSystem.blendFunc(770, 1);
//            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//            var12 = 40.0F;
//            this.minecraft.textureManager.bindTexture(SkyProviderOrbit.moonTexture);
//            float var28 = this.minecraft.world.getMoonPhase();
//            final int var30 = (int) (var28 % 4);
//            final int var29 = (int) (var28 / 4 % 2);
//            final float var16 = (var30 + 0) / 4.0F;
//            final float var17 = (var29 + 0) / 2.0F;
//            final float var18 = (var30 + 1) / 4.0F;
//            final float var19 = (var29 + 1) / 2.0F;
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
//            worldRenderer.pos(-var12, -100.0D, var12).tex(var18, var19).endVertex();
//            worldRenderer.pos(var12, -100.0D, var12).tex(var16, var19).endVertex();
//            worldRenderer.pos(var12, -100.0D, -var12).tex(var16, var17).endVertex();
//            worldRenderer.pos(-var12, -100.0D, -var12).tex(var18, var17).endVertex();
//            var23.draw();
//        }
//
//        RenderSystem.popMatrix();
//        RenderSystem.disableBlend();
//
//        if (this.planetToRender != null)
//        {
//            RenderSystem.pushMatrix();
//            RenderSystem.translatef(0.0F, -var20 / 10, 0.0F);
//            float scale = 100 * (0.3F - var20 / 10000.0F);
//            scale = Math.max(scale, 0.2F);
//            RenderSystem.scalef(scale, 0.0F, scale);
//            RenderSystem.translatef(0.0F, -var20, 0.0F);
//            RenderSystem.rotatef(90F, 0.0F, 1.0F, 0.0F);
//            this.minecraft.textureManager.bindTexture(this.planetToRender);
//
//            var10 = 1.0F;
//            final float alpha = 0.5F;
//            RenderSystem.color4f(Math.min(alpha, 1.0F), Math.min(alpha, 1.0F), Math.min(alpha, 1.0F), Math.min(alpha, 1.0F));
//            BufferBuilder worldRenderer = var23.getBuffer();
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
//            worldRenderer.pos(-var10, 0, var10).tex(0D, 1.0).endVertex();
//            worldRenderer.pos(var10, 0, var10).tex(1.0, 1.0).endVertex();
//            worldRenderer.pos(var10, 0, -var10).tex(1.0, 0D).endVertex();
//            worldRenderer.pos(-var10, 0, -var10).tex(0D, 0D).endVertex();
//            var23.draw();
//            RenderSystem.popMatrix();
//        }
//
//        RenderSystem.disableTexture();
//
//        RenderSystem.popMatrix();
//
//        RenderSystem.enableAlphaTest();
//
//        RenderSystem.color3f(0.0F, 0.0F, 0.0F);
//
//		/* This all does nothing!
//        double var25 = 0.0D;
//
//		// if (this.minecraft.player.getRidingEntity() != null)
//		{
//			var25 = this.minecraft.player.getPosY() - 64;
//
//			if (var25 < 0.0D)
//			{
//				// RenderSystem.pushMatrix();
//				// RenderSystem.translatef(0.0F, 12.0F, 0.0F);
//				// RenderSystem.callList(this.glSkyList2);
//				// RenderSystem.popMatrix();
//				// var10 = 1.0F;
//				// var11 = -((float)(var25 + 65.0D));
//				// var12 = -var10;
//				// var23.startDrawingQuads();
//				// var23.setColorRGBA_I(0, 255);
//				// var23.addVertex(-var10, var11, var10);
//				// var23.addVertex(var10, var11, var10);
//				// var23.addVertex(var10, var12, var10);
//				// var23.addVertex(-var10, var12, var10);
//				// var23.addVertex(-var10, var12, -var10);
//				// var23.addVertex(var10, var12, -var10);
//				// var23.addVertex(var10, var11, -var10);
//				// var23.addVertex(-var10, var11, -var10);
//				// var23.addVertex(var10, var12, -var10);
//				// var23.addVertex(var10, var12, var10);
//				// var23.addVertex(var10, var11, var10);
//				// var23.addVertex(var10, var11, -var10);
//				// var23.addVertex(-var10, var11, -var10);
//				// var23.addVertex(-var10, var11, var10);
//				// var23.addVertex(-var10, var12, var10);
//				// var23.addVertex(-var10, var12, -var10);
//				// var23.addVertex(-var10, var12, -var10);
//				// var23.addVertex(-var10, var12, var10);
//				// var23.addVertex(var10, var12, var10);
//				// var23.addVertex(var10, var12, -var10);
//				// var23.draw();
//			}
//		}
//
//		if (this.minecraft.world.getDimension().isSkyColored())
//		{
//			RenderSystem.color3f(0.0f, 0.0f, 0.0f);
//		}
//		else
//		{
//			RenderSystem.color3f(var3, var4, var5);
//		}
//		RenderSystem.color3f(0.0f, 0.0f, 0.0f);
//
//		RenderSystem.pushMatrix();
//		RenderSystem.translatef(0.0F, -((float) (var25 - 16.0D)), 0.0F);
//		RenderSystem.popMatrix();
//		*/
//        RenderSystem.enableRescaleNormal();
//        RenderSystem.enableTexture();
//        RenderSystem.enable(GL11.GL_COLOR_MATERIAL);
//        RenderSystem.depthMask(true);
//        RenderSystem.blendFunc(770, 771);
//        RenderSystem.disableBlend();
//    }
//
//    private void renderStars()
//    {
//        final Random var1 = new Random(10842L);
//        final Tessellator var2 = Tessellator.getInstance();
//        var2.getBuffer().begin(7, DefaultVertexFormats.POSITION);
//
//        for (int var3 = 0; var3 < (ConfigManagerCore.moreStars ? 20000 : 6000); ++var3)
//        {
//            double var4 = var1.nextFloat() * 2.0F - 1.0F;
//            double var6 = var1.nextFloat() * 2.0F - 1.0F;
//            double var8 = var1.nextFloat() * 2.0F - 1.0F;
//            final double var10 = 0.07F + var1.nextFloat() * 0.06F;
//            double var12 = var4 * var4 + var6 * var6 + var8 * var8;
//
//            if (var12 < 1.0D && var12 > 0.01D)
//            {
//                var12 = 1.0D / Math.sqrt(var12);
//                var4 *= var12;
//                var6 *= var12;
//                var8 *= var12;
//                final double var14 = var4 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 50D + 75D : 50.0D);
//                final double var16 = var6 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 50D + 75D : 50.0D);
//                final double var18 = var8 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 50D + 75D : 50.0D);
//                final double var20 = Math.atan2(var4, var8);
//                final double var22 = Math.sin(var20);
//                final double var24 = Math.cos(var20);
//                final double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
//                final double var28 = Math.sin(var26);
//                final double var30 = Math.cos(var26);
//                final double var32 = var1.nextDouble() * Math.PI * 2.0D;
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
//                    var2.getBuffer().pos(var14 + var57, var16 + var53, var18 + var61).endVertex();
//                }
//            }
//        }
//
//        var2.draw();
//    }
//}
