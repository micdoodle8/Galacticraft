package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.blocks.GCBlockBreathableAir;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraft.src.RenderPlayerBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class GCCoreRenderPlayer extends RenderPlayerBase
{
	public GCCoreRenderPlayer(RenderPlayerAPI var1)
	{
		super(var1);
	}

	@Override
    public void doRenderShadowAndFire(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
    	if (this.isAABBInBreathableAirBlock())
    	{
            if (var1.canRenderOnFire())
            {
            	this.renderEntityOnFire(var1, var2, var4, var6, var9);
            }
    	}
    	else
    	{
    		super.doRenderShadowAndFire(var1, var2, var4, var6, var8, var9);
    	}
    }

    private void renderEntityOnFire(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        final int var9 = Block.fire.blockIndexInTexture;
        final int var10 = (var9 & 15) << 4;
        final int var11 = var9 & 240;
        float var12 = var10 / 256.0F;
        float var13 = (var10 + 15.99F) / 256.0F;
        float var14 = var11 / 256.0F;
        float var15 = (var11 + 15.99F) / 256.0F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        final float var16 = par1Entity.width * 1.4F;
        GL11.glScalef(var16, var16, var16);
        this.loadTexture("/terrain.png");
        final Tessellator var17 = Tessellator.instance;
        float var18 = 0.5F;
        final float var19 = 0.0F;
        float var20 = par1Entity.height / var16;
        float var21 = (float)(par1Entity.posY - par1Entity.boundingBox.minY);
        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.0F, -0.3F + (int)var20 * 0.02F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var22 = 0.0F;
        int var23 = 0;
        var17.startDrawingQuads();

        while (var20 > 0.0F)
        {
            if (var23 % 2 == 0)
            {
                var12 = var10 / 256.0F;
                var13 = (var10 + 15.99F) / 256.0F;
                var14 = var11 / 256.0F;
                var15 = (var11 + 15.99F) / 256.0F;
            }
            else
            {
                var12 = var10 / 256.0F;
                var13 = (var10 + 15.99F) / 256.0F;
                var14 = (var11 + 16) / 256.0F;
                var15 = ((var11 + 16) + 15.99F) / 256.0F;
            }

            if (var23 / 2 % 2 == 0)
            {
                final float var24 = var13;
                var13 = var12;
                var12 = var24;
            }

            var17.addVertexWithUV((var18 - var19), (0.0F - var21), var22, var13, var15);
            var17.addVertexWithUV((-var18 - var19), (0.0F - var21), var22, var12, var15);
            var17.addVertexWithUV((-var18 - var19), (1.4F - var21), var22, var12, var14);
            var17.addVertexWithUV((var18 - var19), (1.4F - var21), var22, var13, var14);
            var20 -= 0.45F;
            var21 -= 0.45F;
            var18 *= 0.9F;
            var22 += 0.03F;
            ++var23;
        }

        var17.draw();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public boolean isAABBInBreathableAirBlock()
    {
        final int var3 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.minX);
        final int var4 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.maxX + 1.0D);
        final int var5 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.minY);
        final int var6 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.maxY + 1.0D);
        final int var7 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.minZ);
        final int var8 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.maxZ + 1.0D);

        for (int var9 = var3; var9 < var4; ++var9)
        {
            for (int var10 = var5; var10 < var6; ++var10)
            {
                for (int var11 = var7; var11 < var8; ++var11)
                {
                    final Block var12 = Block.blocksList[FMLClientHandler.instance().getClient().thePlayer.worldObj.getBlockId(var9, var10, var11)];

                    if (var12 != null && var12 instanceof GCBlockBreathableAir)
                    {
                        final int var13 = FMLClientHandler.instance().getClient().thePlayer.worldObj.getBlockMetadata(var9, var10, var11);
                        double var14 = var10 + 1;

                        if (var13 < 8)
                        {
                            var14 = var10 + 1 - var13 / 8.0D;
                        }

                        if (var14 >= FMLClientHandler.instance().getClient().thePlayer.boundingBox.minY)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean loadDownloadableImageTexture(String par1Str, String par2Str)
    {
        RenderEngine var3 = RenderManager.instance.renderEngine;
        int var4 = var3.getTextureForDownloadableImage(par1Str, par2Str);

        if (var4 >= 0)
        {
            var3.bindTexture(var4);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    private ThreadDownloadImageData playerCloak = null;

    @Override
    public void renderSpecialCloak(EntityPlayer var1, float var2)
    {
    	String string = "http://www.micdoodle8.com/galacticraft/capes/" + StringUtils.stripControlCodes(var1.username) + ".png";

    	if (playerCloak == null)
    	{
    		playerCloak = RenderManager.instance.renderEngine.obtainImageData(string, new ImageBufferDownload());
    	}
        
        ModelBiped model = ObfuscationReflectionHelper.getPrivateValue(RenderPlayer.class, this.renderPlayer, 0);
        
        if (this.loadDownloadableImageTexture(string, (String)null) && !var1.getHasActivePotion() && !var1.getHideCape())
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double var3 = var1.field_71091_bM + (var1.field_71094_bP - var1.field_71091_bM) * (double)var2 - (var1.prevPosX + (var1.posX - var1.prevPosX) * (double)var2);
            double var5 = var1.field_71096_bN + (var1.field_71095_bQ - var1.field_71096_bN) * (double)var2 - (var1.prevPosY + (var1.posY - var1.prevPosY) * (double)var2);
            double var7 = var1.field_71097_bO + (var1.field_71085_bR - var1.field_71097_bO) * (double)var2 - (var1.prevPosZ + (var1.posZ - var1.prevPosZ) * (double)var2);
            float var9 = var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var2;
            double var10 = (double)MathHelper.sin(var9 * (float)Math.PI / 180.0F);
            double var12 = (double)(-MathHelper.cos(var9 * (float)Math.PI / 180.0F));
            float var14 = (float)var5 * 10.0F;

            if (var14 < -6.0F)
            {
                var14 = -6.0F;
            }

            if (var14 > 32.0F)
            {
                var14 = 32.0F;
            }

            float var15 = (float)(var3 * var10 + var7 * var12) * 100.0F;
            float var16 = (float)(var3 * var12 - var7 * var10) * 100.0F;

            if (var15 < 0.0F)
            {
                var15 = 0.0F;
            }

            float var17 = var1.prevCameraYaw + (var1.cameraYaw - var1.prevCameraYaw) * var2;
            var14 += MathHelper.sin((var1.prevDistanceWalkedModified + (var1.distanceWalkedModified - var1.prevDistanceWalkedModified) * var2) * 6.0F) * 32.0F * var17;

            if (var1.isSneaking())
            {
                var14 += 25.0F;
            }

            GL11.glRotatef(6.0F + var15 / 2.0F + var14, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var16 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-var16 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            
        	model.bipedCloak.render(0.0625F);
        	
            GL11.glPopMatrix();
        }
        else
        {
        	super.renderSpecialCloak(var1, var2);
        }
    }
}
