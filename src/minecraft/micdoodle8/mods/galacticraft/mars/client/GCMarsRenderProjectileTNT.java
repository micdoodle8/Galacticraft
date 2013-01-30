package micdoodle8.mods.galacticraft.mars.client;

import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityProjectileTNT;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@SideOnly(Side.CLIENT)
public class GCMarsRenderProjectileTNT extends Render
{
    private final RenderBlocks renderBlocks = new RenderBlocks();

    public GCMarsRenderProjectileTNT()
    {
        this.shadowSize = 0.5F;
    }

    public void doRenderFallingSand(GCMarsEntityProjectileTNT par1EntityFallingSand, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        this.loadTexture("/terrain.png");
        final Block var10 = Block.blocksList[Block.tnt.blockID];
        final World var11 = par1EntityFallingSand.getWorld();
        GL11.glDisable(GL11.GL_LIGHTING);

        if (var10 == Block.dragonEgg)
        {
            this.renderBlocks.blockAccess = var11;
            final Tessellator var12 = Tessellator.instance;
            var12.startDrawingQuads();
            var12.setTranslation(-MathHelper.floor_double(par1EntityFallingSand.posX) - 0.5F, -MathHelper.floor_double(par1EntityFallingSand.posY) - 0.5F, -MathHelper.floor_double(par1EntityFallingSand.posZ) - 0.5F);
            this.renderBlocks.renderBlockByRenderType(var10, MathHelper.floor_double(par1EntityFallingSand.posX), MathHelper.floor_double(par1EntityFallingSand.posY), MathHelper.floor_double(par1EntityFallingSand.posZ));
            var12.setTranslation(0.0D, 0.0D, 0.0D);
            var12.draw();
        }
        else if (var10 != null)
        {
            this.renderBlocks.renderBlockSandFalling(var10, var11, MathHelper.floor_double(par1EntityFallingSand.posX), MathHelper.floor_double(par1EntityFallingSand.posY), MathHelper.floor_double(par1EntityFallingSand.posZ), 0);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    @Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderFallingSand((GCMarsEntityProjectileTNT)par1Entity, par2, par4, par6, par8, par9);
    }
}
