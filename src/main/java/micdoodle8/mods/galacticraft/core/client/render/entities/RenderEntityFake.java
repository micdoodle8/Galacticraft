package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.entities.EntityCelestialFake;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderEntityFake extends EntityRenderer<EntityCelestialFake>
{
    public RenderEntityFake(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCelestialFake par1Entity)
    {
        return null;
    }

    @Override
    public void doRender(EntityCelestialFake par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
    }

    @Override
    public boolean shouldRender(EntityCelestialFake lander, ICamera camera, double camX, double camY, double camZ)
    {
        return false;
    }
}
