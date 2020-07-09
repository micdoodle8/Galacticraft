//package micdoodle8.mods.galacticraft.core.client.render.entities;
//
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
//import micdoodle8.mods.galacticraft.core.client.model.ModelAlienVillager;
//import micdoodle8.mods.galacticraft.core.entities.EntityAlienVillager;
//import net.minecraft.client.renderer.entity.MobRenderer;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderAlienVillager extends MobRenderer<EntityAlienVillager>
//{
//    private static final ResourceLocation villagerTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/villager.png");
//    private boolean texSwitch;
//
//    protected ModelAlienVillager villagerModel;
//
//    public RenderAlienVillager(EntityRendererManager manager)
//    {
//        super(manager, new ModelAlienVillager(0.0F), 0.5F);
//        this.villagerModel = (ModelAlienVillager) this.mainModel;
//    }
//
//    @Override
//    protected void preRenderCallback(EntityAlienVillager villager, float par2)
//    {
//        float f1 = 0.9375F;
//
//        if (villager.getGrowingAge() < 0)
//        {
//            f1 = (float) (f1 * 0.5D);
//            this.shadowSize = 0.25F;
//        }
//        else
//        {
//            this.shadowSize = 0.5F;
//        }
//
//        if (texSwitch)
//        {
//            OverlaySensorGlasses.preRenderMobs();
//        }
//
//        GL11.glScalef(f1, f1, f1);
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(EntityAlienVillager par1Entity)
//    {
//        return texSwitch ? OverlaySensorGlasses.altTexture : RenderAlienVillager.villagerTexture;
//    }
//
//    @Override
//    public void doRender(EntityAlienVillager villager, double par2, double par4, double par6, float par8, float par9)
//    {
//        super.doRender(villager, par2, par4, par6, par8, par9);
//        if (OverlaySensorGlasses.overrideMobTexture())
//        {
//            texSwitch = true;
//            super.doRender(villager, par2, par4, par6, par8, par9);
//            texSwitch = false;
//            OverlaySensorGlasses.postRenderMobs();
//        }
//    }
//}
