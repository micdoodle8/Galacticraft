package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEvolvedSkeleton extends RenderBiped<EntityEvolvedSkeleton>
{
    private static final ResourceLocation skeletonTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/skeleton.png");
    private static final ResourceLocation powerTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/power.png");

    private final ModelEvolvedSkeleton model = new ModelEvolvedSkeleton(0.2F);
    private boolean texSwitch;

    public RenderEvolvedSkeleton(RenderManager manager)
    {
        super(manager, new ModelEvolvedSkeleton(), 0.6F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this)
        {
            @Override
            protected void initArmor()
            {
                this.modelLeggings = new ModelSkeleton(0.5F, true);
                this.modelArmor = new ModelSkeleton(1.0F, true);
            }
        });
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEvolvedSkeleton par1Entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedSkeleton.skeletonTexture;
    }

    @Override
    protected void preRenderCallback(EntityEvolvedSkeleton par1EntityLiving, float par2)
    {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void doRender(EntityEvolvedSkeleton entity, double par2, double par4, double par6, float par8, float par9)
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
    protected void applyRotations(EntityEvolvedSkeleton skellie, float pitch, float yaw, float partialTicks)
    {
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GL11.glTranslatef(0F, -skellie.height * 0.55F, 0F);
        GL11.glRotatef(skellie.getTumbleAngle(partialTicks), skellie.getTumbleAxisX(), 0F, skellie.getTumbleAxisZ());
        GL11.glTranslatef(0F, skellie.height * 0.55F, 0F);
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        super.applyRotations(skellie, pitch, yaw, partialTicks);
    }
}
