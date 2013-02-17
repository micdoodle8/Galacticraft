package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.API.GCBlockBreathableAir;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraft.src.RenderPlayerBase;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

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
}
