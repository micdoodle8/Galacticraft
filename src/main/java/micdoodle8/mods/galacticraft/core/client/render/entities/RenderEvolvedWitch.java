package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedWitch;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerHeldItemEvolvedWitch;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEvolvedWitch extends RenderLiving<EntityEvolvedWitch>
{
    private static final ResourceLocation witchTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/evolved_witch.png");
    private boolean texSwitch;

    public RenderEvolvedWitch(RenderManager manager)
    {
        super(manager, new ModelEvolvedWitch(), 0.5F);
        this.addLayer(new LayerHeldItemEvolvedWitch(this));
    }

    @Override
    public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    @Override
    public void doRender(EntityEvolvedWitch entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        ((ModelEvolvedWitch)this.mainModel).field_82900_g = entity.getHeldItem() != null;
        texSwitch = false;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    protected void preRenderCallback(EntityEvolvedWitch entity, float partialTickTime)
    {
        float f1 = 0.9375F;
        GlStateManager.scale(f1, f1, f1);
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEvolvedWitch entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedWitch.witchTexture;
    }
}