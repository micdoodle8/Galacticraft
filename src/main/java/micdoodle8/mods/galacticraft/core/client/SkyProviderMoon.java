//package micdoodle8.mods.galacticraft.core.client;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.dimension.DimensionMoon;
//import micdoodle8.mods.galacticraft.core.network.PacketSimple;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
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
//public class SkyProviderMoon implements IRenderHandler
//{
//    private static final ResourceLocation overworldTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/earth.png");
//    private static final ResourceLocation sunTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/planets/orbitalsun.png");
//
//    public int starGLCallList = GLAllocation.generateDisplayLists(3);
//    public int glSkyList;
//    public int glSkyList2;
//
//    public SkyProviderMoon()
//    {
//        RenderSystem.pushMatrix();
//        RenderSystem.NewList(this.starGLCallList, GL11.GL_COMPILE);
//        this.renderStars();
//        GL11.glEndList();
//        RenderSystem.popMatrix();
//        final Tessellator tessellator = Tessellator.getInstance();
//        this.glSkyList = this.starGLCallList + 1;
//        RenderSystem.NewList(this.glSkyList, GL11.GL_COMPILE);
//        final byte byte2 = 64;
//        final int i = 256 / byte2 + 2;
//        float f = 16F;
//        BufferBuilder worldRenderer = tessellator.getBuffer();
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
//        if (!ClientProxyCore.overworldTextureRequestSent)
//        {
//            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, GCCoreUtil.getDimensionType(mc.world), new Object[]{}));
//            ClientProxyCore.overworldTextureRequestSent = true;
//        }
//
//        DimensionMoon gcProvider = null;
//
//        if (world.getDimension() instanceof DimensionMoon)
//        {
//            gcProvider = (DimensionMoon) world.getDimension();
//        }
//
//        RenderSystem.disableTexture();
//        RenderSystem.disableRescaleNormal();
//        RenderSystem.color3f(1F, 1F, 1F);
//        final Tessellator var23 = Tessellator.getInstance();
//        RenderSystem.depthMask(false);
//        RenderSystem.enable(GL11.GL_FOG);
//        RenderSystem.color3f(0, 0, 0);
//        RenderSystem.callList(this.glSkyList);
//        RenderSystem.disable(GL11.GL_FOG);
//        RenderSystem.disableAlphaTest();
//        RenderSystem.enableBlend();
//        RenderSystem.blendFunc(770, 771);
//        RenderHelper.disableStandardItemLighting();
//        float var10;
//        float var11;
//        float var12;
//
//        float var20 = 0;
//
//        if (gcProvider != null)
//        {
//            var20 = gcProvider.getStarBrightness(partialTicks);
//        }
//
//        if (var20 > 0.0F)
//        {
//            RenderSystem.pushMatrix();
//            RenderSystem.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//            RenderSystem.rotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
//            RenderSystem.rotatef(-19.0F, 0, 1.0F, 0);
//            RenderSystem.color4f(1.0F, 1.0F, 1.0F, var20);
//            RenderSystem.callList(this.starGLCallList);
//            RenderSystem.popMatrix();
//        }
//
//        RenderSystem.enableTexture();
//        RenderSystem.blendFunc(770, 1);
//        RenderSystem.pushMatrix();
//
//        RenderSystem.popMatrix();
//
//        RenderSystem.pushMatrix();
//
//        RenderSystem.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 5F);
//        RenderSystem.rotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
//        RenderSystem.blendFunc(770, 771);
//        RenderSystem.disableTexture();
//        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
//        var12 = 20.0F / 3.5F;
//        BufferBuilder worldRenderer = var23.getBuffer();
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//        worldRenderer.pos(-var12, 99.9D, -var12).endVertex();
//        worldRenderer.pos(var12, 99.9D, -var12).endVertex();
//        worldRenderer.pos(var12, 99.9D, var12).endVertex();
//        worldRenderer.pos(-var12, 99.9D, var12).endVertex();
//        var23.draw();
//        RenderSystem.enableTexture();
//        RenderSystem.blendFunc(770, 1);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        var12 = 20.0F;
//        Minecraft.getInstance().textureManager.bindTexture(SkyProviderMoon.sunTexture);
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer.pos(-var12, 100.0D, -var12).tex(0.0D, 0.0D).endVertex();
//        worldRenderer.pos(var12, 100.0D, -var12).tex(1.0D, 0.0D).endVertex();
//        worldRenderer.pos(var12, 100.0D, var12).tex(1.0D, 1.0D).endVertex();
//        worldRenderer.pos(-var12, 100.0D, var12).tex(0.0D, 1.0D).endVertex();
//        var23.draw();
//
//        RenderSystem.popMatrix();
//
//        RenderSystem.pushMatrix();
//
//        RenderSystem.disableBlend();
//
//        // HOME:
//        var12 = 10.0F;
//        final float earthRotation = (float) (world.getSpawnPoint().getZ() - mc.player.getPosZ()) * 0.01F;
//        RenderSystem.scalef(0.6F, 0.6F, 0.6F);
//        RenderSystem.rotatef(earthRotation, 1.0F, 0.0F, 0.0F);
//        RenderSystem.rotatef(200F, 1.0F, 0.0F, 0.0F);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
//
//        if (ClientProxyCore.overworldTexturesValid)
//        {
//            RenderSystem.bindTexture(ClientProxyCore.overworldTextureClient.getGlTextureId());
//        }
//        else
//        {
//            // Overworld texture is 48x48 in a 64x64 .png file
//            Minecraft.getInstance().textureManager.bindTexture(SkyProviderMoon.overworldTexture);
//        }
//
//        world.getMoonPhase();
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer.pos(-var12, -100.0D, var12).tex(0D, 1.0D).endVertex();
//        worldRenderer.pos(var12, -100.0D, var12).tex(1.0D, 1.0D).endVertex();
//        worldRenderer.pos(var12, -100.0D, -var12).tex(1.0D, 0D).endVertex();
//        worldRenderer.pos(-var12, -100.0D, -var12).tex(0D, 0D).endVertex();
//        var23.draw();
//
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.disableBlend();
//        RenderSystem.enableAlphaTest();
//        RenderSystem.enable(GL11.GL_FOG);
//        RenderSystem.popMatrix();
//        RenderSystem.disableTexture();
//        RenderSystem.color3f(0.0F, 0.0F, 0.0F);
//        final double var25 = mc.player.getPosition().getY() - world.getHorizon();
//
//        if (var25 < 0.0D)
//        {
//            RenderSystem.pushMatrix();
//            RenderSystem.translatef(0.0F, 12.0F, 0.0F);
//            RenderSystem.callList(this.glSkyList2);
//            RenderSystem.popMatrix();
//            var10 = 1.0F;
//            var11 = -((float) (var25 + 65.0D));
//            var12 = -var10;
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
//            worldRenderer.pos(-var10, var11, var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(var10, var11, var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(var10, var12, var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(-var10, var12, var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(-var10, var12, -var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(var10, var12, -var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(var10, var11, -var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(-var10, var11, -var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(var10, var12, -var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(var10, var12, var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(var10, var11, var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(var10, var11, -var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(-var10, var11, -var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(-var10, var11, var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(-var10, var12, var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(-var10, var12, -var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(-var10, var12, -var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(-var10, var12, var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(var10, var12, var10).color(0, 0, 0, 1.0F).endVertex();
//            worldRenderer.pos(var10, var12, -var10).color(0, 0, 0, 1.0F).endVertex();
//            var23.draw();
//        }
//
//        RenderSystem.color3f(70F / 256F, 70F / 256F, 70F / 256F);
//
//        RenderSystem.pushMatrix();
//        RenderSystem.translatef(0.0F, -((float) (var25 - 16.0D)), 0.0F);
//        RenderSystem.callList(this.glSkyList2);
//        RenderSystem.popMatrix();
//        RenderSystem.enableRescaleNormal();
//        RenderSystem.enableTexture();
//        RenderSystem.depthMask(true);
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
//        for (int var3 = 0; var3 < (ConfigManagerCore.moreStars ? 20000 : 6000); ++var3)
//        {
//            double var4 = var1.nextFloat() * 2.0F - 1.0F;
//            double var6 = var1.nextFloat() * 2.0F - 1.0F;
//            double var8 = var1.nextFloat() * 2.0F - 1.0F;
//            final double var10 = 0.15F + var1.nextFloat() * 0.1F;
//            double var12 = var4 * var4 + var6 * var6 + var8 * var8;
//
//            if (var12 < 1.0D && var12 > 0.01D)
//            {
//                var12 = 1.0D / Math.sqrt(var12);
//                var4 *= var12;
//                var6 *= var12;
//                var8 *= var12;
//                final double var14 = var4 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 100D + 150D : 100.0D);
//                final double var16 = var6 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 100D + 150D : 100.0D);
//                final double var18 = var8 * (ConfigManagerCore.moreStars ? var1.nextDouble() * 100D + 150D : 100.0D);
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
