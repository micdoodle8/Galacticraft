package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEvolvedSpider extends RenderLiving<EntityEvolvedSpider>
{
    private static final ResourceLocation spiderTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/spider.png");

    public RenderEvolvedSpider(RenderManager manager)
    {
        super(manager, new ModelEvolvedSpider(), 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEvolvedSpider par1Entity)
    {
        return RenderEvolvedSpider.spiderTexture;
    }

    @Override
    protected void preRenderCallback(EntityEvolvedSpider par1EntityLiving, float par2)
    {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
    }

    @Override
    protected float getDeathMaxRotation(EntityEvolvedSpider par1EntityLiving)
    {
        return 180.0F;
    }
}
