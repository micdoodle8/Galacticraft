package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityFake extends Render
{
    public RenderEntityFake()
    {
    }

    protected ResourceLocation func_110779_a(EntityMeteor entity)
    {
        return null;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return null;
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
    }
}
