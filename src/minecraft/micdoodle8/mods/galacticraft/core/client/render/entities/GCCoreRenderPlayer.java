package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockBreathableAir;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraft.src.RenderPlayerBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class GCCoreRenderPlayer extends RenderPlayerBase
{
	public GCCoreRenderPlayer(RenderPlayerAPI var1)
	{
		super(var1);
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

                    if (var12 != null && var12 instanceof GCCoreBlockBreathableAir)
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
        final RenderEngine var3 = RenderManager.instance.renderEngine;
        final int var4 = var3.getTextureForDownloadableImage(par1Str, par2Str);

        if (var4 >= 0)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
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
    	final String string = "http://www.micdoodle8.com/galacticraft/capes/" + StringUtils.stripControlCodes(var1.username) + ".png";

    	if (this.playerCloak == null)
    	{
    		this.playerCloak = RenderManager.instance.renderEngine.obtainImageData(string, new ImageBufferDownload());
    	}

        final ModelBiped model = ObfuscationReflectionHelper.getPrivateValue(RenderPlayer.class, this.renderPlayer, 0);

        if (this.loadDownloadableImageTexture(string, (String)null) && !var1.getHasActivePotion() && !var1.getHideCape())
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            final double var3 = var1.field_71091_bM + (var1.field_71094_bP - var1.field_71091_bM) * var2 - (var1.prevPosX + (var1.posX - var1.prevPosX) * var2);
            final double var5 = var1.field_71096_bN + (var1.field_71095_bQ - var1.field_71096_bN) * var2 - (var1.prevPosY + (var1.posY - var1.prevPosY) * var2);
            final double var7 = var1.field_71097_bO + (var1.field_71085_bR - var1.field_71097_bO) * var2 - (var1.prevPosZ + (var1.posZ - var1.prevPosZ) * var2);
            final float var9 = var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var2;
            final double var10 = MathHelper.sin(var9 * (float)Math.PI / 180.0F);
            final double var12 = -MathHelper.cos(var9 * (float)Math.PI / 180.0F);
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
            final float var16 = (float)(var3 * var12 - var7 * var10) * 100.0F;

            if (var15 < 0.0F)
            {
                var15 = 0.0F;
            }

            final float var17 = var1.prevCameraYaw + (var1.cameraYaw - var1.prevCameraYaw) * var2;
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
