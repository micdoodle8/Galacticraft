package micdoodle8.mods.galacticraft.core.client.render.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelParaChest;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderParaChest extends Render
{
    private static final ResourceLocation parachestTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachest.png");

    private final ModelParaChest chestModel;

    public RenderParaChest()
    {
        this.shadowSize = 1F;
        this.chestModel = new ModelParaChest();
    }

    protected ResourceLocation func_110779_a(Entity par1EntityArrow)
    {
        return RenderParaChest.parachestTexture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110779_a(par1Entity);
    }

    public void doRenderParaChest(EntityParachest entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);

        this.bindEntityTexture(entity);

        if (!entity.isDead)
        {
            this.chestModel.renderAll();
        }
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderParaChest((EntityParachest) par1Entity, par2, par4, par6, par8, par9);
    }
}
