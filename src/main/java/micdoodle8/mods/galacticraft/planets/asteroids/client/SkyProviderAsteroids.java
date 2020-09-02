//package micdoodle8.mods.galacticraft.planets.asteroids.client;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
//import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
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
//public class SkyProviderAsteroids implements IRenderHandler
//{
//    private static final ResourceLocation overworldTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/earth.png");
//    private static final ResourceLocation galaxyTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/planets/galaxy.png");
//    private static final ResourceLocation sunTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/planets/orbitalsun.png");
//
//    public int starGLCallList = GLAllocation.generateDisplayLists(3);
//    public int glSkyList;
//    public int glSkyList2;
//
//    private final float sunSize;
//
//    public SkyProviderAsteroids(IGalacticraftDimension asteroidsProvider)
//    {
//        this.sunSize = 17.5F * asteroidsProvider.getSolarSize();
//
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
//    @Override
//    public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc)
//    {
//        float var10;
//        float var11;
//        float var12;
//        final Tessellator var23 = Tessellator.getInstance();
//        BufferBuilder worldRenderer = var23.getBuffer();
//
//        RenderSystem.disableTexture();
//        RenderSystem.disableRescaleNormal();
//        RenderSystem.color3f(1F, 1F, 1F);
//        RenderSystem.depthMask(false);
//        RenderSystem.enable(GL11.GL_FOG);
//        RenderSystem.color3f(0, 0, 0);
//        RenderSystem.callList(this.glSkyList);
//        RenderSystem.disable(GL11.GL_FOG);
//        RenderSystem.disableAlphaTest();
//        RenderSystem.enableBlend();
//        RenderSystem.blendFunc(770, 771);
//        RenderHelper.disableStandardItemLighting();
//
//        RenderSystem.disableTexture();
//        RenderSystem.color4f(0.7F, 0.7F, 0.7F, 0.7F);
//        RenderSystem.callList(this.starGLCallList);
//
//        RenderSystem.pushMatrix();
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//        // Sun:
//        RenderSystem.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//        RenderSystem.rotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
//        RenderSystem.blendFunc(770, 771);
//        RenderSystem.disableTexture();
//        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
//        var12 = this.sunSize / 4.2F;
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//        worldRenderer.pos(-var12, 90.0D, -var12).endVertex();
//        worldRenderer.pos(var12, 90.0D, -var12).endVertex();
//        worldRenderer.pos(var12, 90.0D, var12).endVertex();
//        worldRenderer.pos(-var12, 90.0D, var12).endVertex();
//        var23.draw();
//        RenderSystem.enableTexture();
//        RenderSystem.blendFunc(770, 1);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        var12 = this.sunSize / 1.2F;
//        //110 distance instead of the normal 100, because there is no atmosphere to make the disk seem larger
//        Minecraft.getInstance().textureManager.bindTexture(SkyProviderAsteroids.sunTexture);
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer.pos(-var12, 90.0D, -var12).tex(0.0D, 0.0D).endVertex();
//        worldRenderer.pos(var12, 90.0D, -var12).tex(1.0D, 0.0D).endVertex();
//        worldRenderer.pos(var12, 90.0D, var12).tex(1.0D, 1.0D).endVertex();
//        worldRenderer.pos(-var12, 90.0D, var12).tex(0.0D, 1.0D).endVertex();
//        var23.draw();
//
//        RenderSystem.popMatrix();
//
//        RenderSystem.pushMatrix();
//
//        // HOME:
//        var12 = 0.5F;
//        RenderSystem.scalef(0.6F, 0.6F, 0.6F);
//        RenderSystem.rotatef(40.0F, 0.0F, 0.0F, 1.0F);
//        RenderSystem.rotatef(200F, 1.0F, 0.0F, 0.0F);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
//        Minecraft.getInstance().textureManager.bindTexture(SkyProviderAsteroids.overworldTexture);
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer.pos(-var12, -100.0D, var12).tex(0, 1.0).endVertex();
//        worldRenderer.pos(var12, -100.0D, var12).tex(1.0, 1.0).endVertex();
//        worldRenderer.pos(var12, -100.0D, -var12).tex(1.0, 0).endVertex();
//        worldRenderer.pos(-var12, -100.0D, -var12).tex(0, 0).endVertex();
//        var23.draw();
//
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.disableBlend();
//        RenderSystem.enableAlphaTest();
//        RenderSystem.enable(GL11.GL_FOG);
//        RenderSystem.popMatrix();
//
//        RenderSystem.disableTexture();
//        RenderSystem.color3f(0.0F, 0.0F, 0.0F);
//        final double var25 = mc.player.getPosition().getY() - world.getHorizon();
//
//        //		if (var25 < 0.0D)
//        //		{
//        //			RenderSystem.pushMatrix();
//        //			RenderSystem.translatef(0.0F, 12.0F, 0.0F);
//        //			RenderSystem.callList(this.glSkyList2);
//        //			RenderSystem.popMatrix();
//        //			var10 = 1.0F;
//        //			var11 = -((float) (var25 + 65.0D));
//        //			var12 = -var10;
//        //			var23.startDrawingQuads();
//        //			var23.setColorRGBA_I(0, 255);
//        //			var23.addVertex(-var10, var11, var10);
//        //			var23.addVertex(var10, var11, var10);
//        //			var23.addVertex(var10, var12, var10);
//        //			var23.addVertex(-var10, var12, var10);
//        //			var23.addVertex(-var10, var12, -var10);
//        //			var23.addVertex(var10, var12, -var10);
//        //			var23.addVertex(var10, var11, -var10);
//        //			var23.addVertex(-var10, var11, -var10);
//        //			var23.addVertex(var10, var12, -var10);
//        //			var23.addVertex(var10, var12, var10);
//        //			var23.addVertex(var10, var11, var10);
//        //			var23.addVertex(var10, var11, -var10);
//        //			var23.addVertex(-var10, var11, -var10);
//        //			var23.addVertex(-var10, var11, var10);
//        //			var23.addVertex(-var10, var12, var10);
//        //			var23.addVertex(-var10, var12, -var10);
//        //			var23.addVertex(-var10, var12, -var10);
//        //			var23.addVertex(-var10, var12, var10);
//        //			var23.addVertex(var10, var12, var10);
//        //			var23.addVertex(var10, var12, -var10);
//        //			var23.draw();
//        //		}
//
//        RenderSystem.color3f(70F / 256F, 70F / 256F, 70F / 256F);
//
//        //		RenderSystem.pushMatrix();
//        //		RenderSystem.translatef(0.0F, -((float) (var25 - 16.0D)), 0.0F);
//        //		RenderSystem.callList(this.glSkyList2);
//        //		RenderSystem.popMatrix();
//        RenderSystem.enableRescaleNormal();
//        RenderSystem.enableTexture();
//        RenderSystem.depthMask(true);
//
//        RenderSystem.enable(GL11.GL_COLOR_MATERIAL);
//        RenderSystem.blendFunc(770, 771);
//        RenderSystem.disableBlend();
//    }
//
//    private void renderStars()
//    {
//        final Random var1 = new Random(10842L);
//        final Tessellator var2 = Tessellator.getInstance();
//        BufferBuilder worldRenderer = var2.getBuffer();
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//
//        for (int var3 = 0; var3 < (ConfigManagerCore.INSTANCE.moreStars.get() ? 35000 : 6000); ++var3)
//        {
//            double var4 = var1.nextFloat() * 2.0F - 1.0F;
//            double var6 = var1.nextFloat() * 2.0F - 1.0F;
//            double var8 = var1.nextFloat() * 2.0F - 1.0F;
//            final double var10 = 0.08F + var1.nextFloat() * 0.07F;
//            double var12 = var4 * var4 + var6 * var6 + var8 * var8;
//
//            if (var12 < 1.0D && var12 > 0.01D)
//            {
//                var12 = 1.0D / Math.sqrt(var12);
//                var4 *= var12;
//                var6 *= var12;
//                var8 *= var12;
//                final double pX = var4 * (ConfigManagerCore.INSTANCE.moreStars ? var1.nextDouble() * 75D + 65D : 80.0D);
//                final double pY = var6 * (ConfigManagerCore.INSTANCE.moreStars ? var1.nextDouble() * 75D + 65D : 80.0D);
//                final double pZ = var8 * (ConfigManagerCore.INSTANCE.moreStars ? var1.nextDouble() * 75D + 65D : 80.0D);
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
//                for (int i = 0; i < 4; ++i)
//                {
//                    final double i1 = ((i & 2) - 1) * var10;
//                    final double i2 = ((i + 1 & 2) - 1) * var10;
//                    final double var47 = i1 * var36 - i2 * var34;
//                    final double var49 = i2 * var36 + i1 * var34;
//                    final double var55 = -var47 * var30;
//                    final double dX = var55 * var22 - var49 * var24;
//                    final double dZ = var49 * var22 + var55 * var24;
//                    final double dY = var47 * var28;
//                    worldRenderer.pos(pX + dX, pY + dY, pZ + dZ).endVertex();
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
