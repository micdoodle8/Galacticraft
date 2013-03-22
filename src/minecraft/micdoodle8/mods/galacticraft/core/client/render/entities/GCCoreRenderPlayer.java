package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockBreathableAir;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelPlayer;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class GCCoreRenderPlayer extends RenderPlayer
{
	public GCCoreRenderPlayer()
	{
		super();
		this.mainModel = new GCCoreModelPlayer(0.0F);
        this.modelBipedMain = (GCCoreModelPlayer) this.mainModel;
        this.modelArmorChestplate = new GCCoreModelPlayer(1.0F);
        this.modelArmor =  new GCCoreModelPlayer(0.5F);
	}
	
	public ModelBiped getModel()
	{
		return this.modelBipedMain;
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

    private final ThreadDownloadImageData playerCloak = null;

    @Override
    public void renderSpecials(EntityPlayer var1, float var2)
    {
    	final String string = "http://www.micdoodle8.com/galacticraft/capes/" + StringUtils.stripControlCodes(var1.username) + ".png";

    	var1.cloakUrl = string;
    	
    	super.renderSpecials(var1, var2);
    }
}
