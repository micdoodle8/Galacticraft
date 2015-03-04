package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelAlienVillager;
import micdoodle8.mods.galacticraft.core.entities.EntityAlienVillager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderAlienVillager extends RenderLiving
{
    private static final ResourceLocation villagerTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/villager.png");

    protected ModelAlienVillager villagerModel;

    public RenderAlienVillager(RenderManager renderManager)
    {
        super(renderManager, new ModelAlienVillager(0.0F), 0.5F);
        this.addLayer(new LayerCustomHead(this.getAlienModel().villagerHead));
    }

    public ModelAlienVillager getAlienModel()
    {
        return (ModelAlienVillager)super.getMainModel();
    }

    protected ResourceLocation getEntityTexture(EntityAlienVillager entity)
    {
        return villagerTexture;
    }

    protected void preRenderCallback(EntityAlienVillager p_77041_1_, float p_77041_2_)
    {
        float f1 = 0.9375F;

        if (p_77041_1_.getGrowingAge() < 0)
        {
            f1 = (float)((double)f1 * 0.5D);
            this.shadowSize = 0.25F;
        }
        else
        {
            this.shadowSize = 0.5F;
        }

        GlStateManager.scale(f1, f1, f1);
    }

    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
    {
        this.preRenderCallback((EntityAlienVillager) p_77041_1_, p_77041_2_);
    }

    public ModelBase getMainModel()
    {
        return this.getAlienModel();
    }

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityAlienVillager)entity);
    }
}
