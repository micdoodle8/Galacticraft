package micdoodle8.mods.galacticraft.core.client;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * GCCoreSkyProviderOrbit.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSkyProviderOrbit extends IRenderHandler
{
	private static final ResourceLocation moonTexture = new ResourceLocation("textures/environment/moon_phases.png");
	private static final ResourceLocation sunTexture = new ResourceLocation("textures/environment/sun.png");

	public int starGLCallList = GLAllocation.generateDisplayLists(3);
	public int glSkyList;
	public int glSkyList2;
	private final ResourceLocation planetToRender;
	private final boolean renderMoon;
	private final boolean renderSun;

	public GCCoreSkyProviderOrbit(ResourceLocation planet, boolean renderMoon, boolean renderSun)
	{
		this.planetToRender = planet;
		this.renderMoon = renderMoon;
		this.renderSun = renderSun;
		GL11.glPushMatrix();
		GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
		this.renderStars();
		GL11.glEndList();
		GL11.glPopMatrix();
		final Tessellator tessellator = Tessellator.instance;
		this.glSkyList = this.starGLCallList + 1;
		GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
		final byte byte2 = 64;
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
		final float var20 = 400.0F;

		// if (this.minecraft.thePlayer.ridingEntity != null)
		{
			// var20 = (float) (this.minecraft.thePlayer.posY - 200.0F);
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		final Vec3 var2 = this.minecraft.theWorld.getSkyColor(this.minecraft.renderViewEntity, partialTicks);
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
		final Tessellator var23 = Tessellator.instance;
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glColor3f(var3, var4, var5);
		GL11.glCallList(this.glSkyList);
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

		if (var24 != null)
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glPushMatrix();
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(MathHelper.sin(this.minecraft.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
			var8 = var24[0];
			var9 = var24[1];
			var10 = var24[2];
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
			var23.setColorRGBA_F(var8, var9, var10, var24[3]);
			var23.addVertex(0.0D, 100.0D, 0.0D);
			final byte var26 = 16;
			var23.setColorRGBA_F(var24[0], var24[1], var24[2], 0.0F);

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
		if (this.renderSun)
		{
			var12 = 30.0F;
			this.minecraft.renderEngine.bindTexture(GCCoreSkyProviderOrbit.sunTexture);
			var23.startDrawingQuads();
			var23.addVertexWithUV(-var12, 100.0D, -var12, 0.0D, 0.0D);
			var23.addVertexWithUV(var12, 100.0D, -var12, 1.0D, 0.0D);
			var23.addVertexWithUV(var12, 100.0D, var12, 1.0D, 1.0D);
			var23.addVertexWithUV(-var12, 100.0D, var12, 0.0D, 1.0D);
			var23.draw();
		}

		if (this.renderMoon)
		{
			var12 = 40.0F;
			this.minecraft.renderEngine.bindTexture(GCCoreSkyProviderOrbit.moonTexture);
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
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		// if (var20 > 0.0F)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glCallList(this.starGLCallList);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(0.0F, 0.0F, 0.0F);

		double var25 = 0.0D;

		// if (this.minecraft.thePlayer.ridingEntity != null)
		{
			var25 = this.minecraft.thePlayer.posY - 64;

			if (var25 < 0.0D)
			{
				// GL11.glPushMatrix();
				// GL11.glTranslatef(0.0F, 12.0F, 0.0F);
				// GL11.glCallList(this.glSkyList2);
				// GL11.glPopMatrix();
				// var10 = 1.0F;
				// var11 = -((float)(var25 + 65.0D));
				// var12 = -var10;
				// var23.startDrawingQuads();
				// var23.setColorRGBA_I(0, 255);
				// var23.addVertex(-var10, var11, var10);
				// var23.addVertex(var10, var11, var10);
				// var23.addVertex(var10, var12, var10);
				// var23.addVertex(-var10, var12, var10);
				// var23.addVertex(-var10, var12, -var10);
				// var23.addVertex(var10, var12, -var10);
				// var23.addVertex(var10, var11, -var10);
				// var23.addVertex(-var10, var11, -var10);
				// var23.addVertex(var10, var12, -var10);
				// var23.addVertex(var10, var12, var10);
				// var23.addVertex(var10, var11, var10);
				// var23.addVertex(var10, var11, -var10);
				// var23.addVertex(-var10, var11, -var10);
				// var23.addVertex(-var10, var11, var10);
				// var23.addVertex(-var10, var12, var10);
				// var23.addVertex(-var10, var12, -var10);
				// var23.addVertex(-var10, var12, -var10);
				// var23.addVertex(-var10, var12, var10);
				// var23.addVertex(var10, var12, var10);
				// var23.addVertex(var10, var12, -var10);
				// var23.draw();
			}

			if (var20 > 0.0F)
			{
				if (this.planetToRender != null)
				{
					GL11.glPushMatrix();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glTranslatef(0.0F, -var20 / 10, 0.0F);
					float scale = 100 * (0.25F - var20 / 10000.0F);
					scale = Math.max(scale, 0.2F);
					GL11.glScalef(scale, 0.0F, scale);
					GL11.glTranslatef(0.0F, -var20, 0.0F);
					this.minecraft.renderEngine.bindTexture(this.planetToRender);

					var10 = 1.0F;
					var12 = -1.0F;

					final float alpha = 0.5F;
					GL11.glColor4f(Math.min(alpha, 1.0F), Math.min(alpha, 1.0F), Math.min(alpha, 1.0F), Math.min(alpha, 1.0F));
					var23.startDrawingQuads();
					var23.addVertexWithUV(-var10, 0, var10, 0.0F, 1.0F);
					var23.addVertexWithUV(var10, 0, var10, 1.0F, 1.0F);
					var23.addVertexWithUV(var10, 0, -var10, 1.0F, 0.0F);
					var23.addVertexWithUV(-var10, 0, -var10, 0.0F, 0.0F);
					var23.addVertexWithUV(-var10, 0, var10, 0.0F, 1.0F);
					var23.addVertexWithUV(var10, 0, var10, 1.0F, 1.0F);
					var23.addVertexWithUV(var10, 0, -var10, 1.0F, 0.0F);
					var23.addVertexWithUV(-var10, 0, -var10, 0.0F, 0.0F);
					var23.addVertexWithUV(-var10, -50, -var10, 0.0F, 0.0F);
					var23.draw();
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glPopMatrix();
				}
			}
		}

		if (this.minecraft.theWorld.provider.isSkyColored())
		{
			GL11.glColor3f(0.0f, 0.0f, 0.0f);
		}
		else
		{
			GL11.glColor3f(var3, var4, var5);
		}
		GL11.glColor3f(0.0f, 0.0f, 0.0f);

		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, -((float) (var25 - 16.0D)), 0.0F);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(true);
	}

	private void renderStars()
	{
		final Random var1 = new Random(10842L);
		final Tessellator var2 = Tessellator.instance;
		var2.startDrawingQuads();

		for (int var3 = 0; var3 < (GCCoreConfigManager.moreStars ? 20000 : 6000); ++var3)
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
				final double var14 = var4 * (GCCoreConfigManager.moreStars ? var1.nextDouble() * 100D + 150D : 100.0D);
				final double var16 = var6 * (GCCoreConfigManager.moreStars ? var1.nextDouble() * 100D + 150D : 100.0D);
				final double var18 = var8 * (GCCoreConfigManager.moreStars ? var1.nextDouble() * 100D + 150D : 100.0D);
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
