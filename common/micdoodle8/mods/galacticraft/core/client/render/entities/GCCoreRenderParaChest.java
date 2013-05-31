package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelParaChest;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityParaChest;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreRenderParaChest extends Render
{
    private final GCCoreModelParaChest chestModel;

    public GCCoreRenderParaChest()
    {
        this.shadowSize = 1F;
        this.chestModel = new GCCoreModelParaChest();
    }

    public void doRenderParaChest(GCCoreEntityParaChest par1EntityFallingSand, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        this.loadTexture("/item/chest.png");
        if (!par1EntityFallingSand.isDead)
        {
            this.chestModel.renderAll();
        }
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method,
     * always casting down its argument and then handing it off to a worker
     * function which does the actual work. In all probabilty, the class Render
     * is generic (Render<T extends Entity) and this method has signature public
     * void doRender(T entity, double d, double d1, double d2, float f, float
     * f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderParaChest((GCCoreEntityParaChest) par1Entity, par2, par4, par6, par8, par9);
    }
}
