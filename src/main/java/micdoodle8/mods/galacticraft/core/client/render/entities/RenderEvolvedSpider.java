//package micdoodle8.mods.galacticraft.core.client.render.entities;
//
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
//import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSpider;
//import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.client.renderer.entity.MobRenderer;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderEvolvedSpider extends MobRenderer<EntityEvolvedSpider, ModelEvolvedSpider>
//{
//    private static final ResourceLocation spiderTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/spider.png");
//    private boolean texSwitch;
//
//    public RenderEvolvedSpider(EntityRendererManager manager)
//    {
//        super(manager, new ModelEvolvedSpider(), 1.0F);
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(EntityEvolvedSpider spider)
//    {
//        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedSpider.spiderTexture;
//    }
//
//    @Override
//    protected void preRenderCallback(EntityEvolvedSpider spider, float par2)
//    {
//        GL11.glScalef(1.2F, 1.2F, 1.2F);
//        if (texSwitch)
//        {
//            GL11.glTranslatef(0.0F, -0.03F, 0.0F);
//            OverlaySensorGlasses.preRenderMobs();
//        }
//    }
//
//    @Override
//    public void doRender(EntityEvolvedSpider spider, double par2, double par4, double par6, float par8, float par9)
//    {
//        super.doRender(spider, par2, par4, par6, par8, par9);
//        if (OverlaySensorGlasses.overrideMobTexture())
//        {
//            texSwitch = true;
//            super.doRender(spider, par2, par4, par6, par8, par9);
//            texSwitch = false;
//            OverlaySensorGlasses.postRenderMobs();
//        }
//    }
//
//    @Override
//    protected float getDeathMaxRotation(EntityEvolvedSpider spider)
//    {
//        return 180.0F;
//    }
//}
