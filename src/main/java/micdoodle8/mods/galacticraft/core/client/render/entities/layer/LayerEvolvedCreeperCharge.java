package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerEvolvedCreeperCharge implements LayerRenderer<EntityEvolvedCreeper>
{
    private final RenderEvolvedCreeper render;
    private final ModelEvolvedCreeper creeperModel = new ModelEvolvedCreeper(2.0F);

    public LayerEvolvedCreeperCharge(RenderEvolvedCreeper render)
    {
        this.render = render;
    }

    @Override
    public void doRenderLayer(EntityEvolvedCreeper entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (entity.getPowered())
        {
            boolean flag = entity.isInvisible();
            GlStateManager.depthMask(!flag);
            this.render.bindTexture(new ResourceLocation("textures/entity/creeper/creeper_armor.png"));
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = entity.ticksExisted + partialTicks;
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            float f1 = 0.5F;
            GlStateManager.color(f1, f1, f1, 1.0F);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(1, 1);
            this.creeperModel.setModelAttributes(this.render.getMainModel());
            this.creeperModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(flag);
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}