package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelMeteor;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMeteor extends Render<EntityMeteor>
{
    private static final ResourceLocation meteorTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/meteor.png");

    private final ModelMeteor modelMeteor;

    public RenderMeteor(RenderManager manager)
    {
        super(manager);
        this.shadowSize = 1F;
        this.modelMeteor = new ModelMeteor();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMeteor par1Entity)
    {
        return RenderMeteor.meteorTexture;
    }

    @Override
    public void doRender(EntityMeteor meteor, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glRotatef(par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(par8, 1.0F, 0.0F, 0.0F);
        final float f = meteor.getSize();
        GL11.glScalef(f / 2, f / 2, f / 2);
        this.bindEntityTexture(meteor);
        this.modelMeteor.render(meteor, 0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.1F);
        GL11.glPopMatrix();
    }
}
