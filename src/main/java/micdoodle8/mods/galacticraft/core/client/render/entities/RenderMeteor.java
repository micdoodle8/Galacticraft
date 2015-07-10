package micdoodle8.mods.galacticraft.core.client.render.entities;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelMeteor;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMeteor extends Render
{
    private static final ResourceLocation meteorTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/meteor.png");

    private final ModelMeteor modelMeteor;

    public RenderMeteor()
    {
        super(FMLClientHandler.instance().getClient().getRenderManager());
        this.shadowSize = 1F;
        this.modelMeteor = new ModelMeteor();
    }

    protected ResourceLocation func_110779_a(EntityMeteor entity)
    {
        return RenderMeteor.meteorTexture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110779_a((EntityMeteor) par1Entity);
    }

    public void doRenderMeteor(EntityMeteor entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glRotatef(par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(par8, 1.0F, 0.0F, 0.0F);
        final float f = entity.getSize();
        GL11.glScalef(f / 2, f / 2, f / 2);
        this.bindEntityTexture(entity);
        this.modelMeteor.render(entity, 0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.1F);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderMeteor((EntityMeteor) par1Entity, par2, par4, par6, par8, par9);
    }
}
