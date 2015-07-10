package micdoodle8.mods.galacticraft.core.client.render.entities;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderEntityFake extends Render
{
    public RenderEntityFake()
    {
        super(FMLClientHandler.instance().getClient().getRenderManager());
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
