package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityProjectileTNT;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderProjectileTNT extends Render
{
    private final RenderBlocks renderBlocks = new RenderBlocks();

    public RenderProjectileTNT()
    {
        this.shadowSize = 0.5F;
    }

    public void renderProjectileTNT(EntityProjectileTNT tnt, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4 + 0.5F, (float) par6);
        this.bindTexture(TextureMap.locationBlocksTexture);
        final Block var10 = Blocks.tnt;
        GL11.glDisable(GL11.GL_LIGHTING);
        if (var10 != null)
        {
            this.renderBlocks.setRenderBoundsFromBlock(var10);
            this.renderBlocks.renderBlockSandFalling(var10, tnt.worldObj, MathHelper.floor_double(tnt.posX), MathHelper.floor_double(tnt.posY), MathHelper.floor_double(tnt.posZ), 0);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderProjectileTNT((EntityProjectileTNT) par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }
}
