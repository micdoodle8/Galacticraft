package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedZombie;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderEvolvedZombie extends AbstractZombieRenderer<EntityEvolvedZombie, ModelEvolvedZombie>
{
    private static final ResourceLocation zombieTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/zombie.png");
    private static final ResourceLocation powerTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/power.png");

    private boolean texSwitch;

    public RenderEvolvedZombie(EntityRendererManager manager)
    {
        super(manager, new ModelEvolvedZombie(0.0F, false, true), new ModelEvolvedZombie(0.5F, false, true), new ModelEvolvedZombie(1.0F, false, true));
//        LayerRenderer layerrenderer = (LayerRenderer) this.layerRenderers.get(0);
        this.addLayer(new HeldItemLayer(this));
//        BipedArmorLayer layerbipedarmor = new BipedArmorLayer(this)
//        {
//            @Override
//            protected void initArmor()
//            {
//                this.modelLeggings = new ZombieModel(0.5F, true);
//                this.modelArmor = new ZombieModel(1.0F, true);
//            }
//        };
//        this.addLayer(layerbipedarmor);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEvolvedZombie par1Entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedZombie.zombieTexture;
    }

    @Override
    protected void preRenderCallback(EntityEvolvedZombie zombie, float par2)
    {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void doRender(EntityEvolvedZombie entity, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(entity, par2, par4, par6, par8, par9);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.doRender(entity, par2, par4, par6, par8, par9);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }

    @Override
    protected void applyRotations(EntityEvolvedZombie zombie, float pitch, float yaw, float partialTicks)
    {
        GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
        GL11.glTranslatef(0F, -zombie.getHeight() * 0.55F, 0F);
        GL11.glRotatef(zombie.getTumbleAngle(partialTicks), zombie.getTumbleAxisX(), 0F, zombie.getTumbleAxisZ());
        GL11.glTranslatef(0F, zombie.getHeight() * 0.55F, 0F);
        GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
        super.applyRotations(zombie, pitch, yaw, partialTicks);
    }
}
