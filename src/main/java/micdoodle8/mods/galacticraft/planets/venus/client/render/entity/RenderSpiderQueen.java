//package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.platform.GlStateManager;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
//import micdoodle8.mods.galacticraft.planets.venus.client.model.ModelSpiderQueen;
//import micdoodle8.mods.galacticraft.planets.venus.entities.EntitySpiderQueen;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.client.renderer.entity.MobRenderer;
//import net.minecraft.client.renderer.model.IBakedModel;
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.model.ModelLoader;
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderSpiderQueen extends MobRenderer<EntitySpiderQueen, ModelSpiderQueen>
//{
//    private static final ResourceLocation spiderTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/model/spider_queen.png");
//    private static IBakedModel webModel;
//
//    public RenderSpiderQueen(EntityRendererManager renderManager)
//    {
//        super(renderManager, new ModelSpiderQueen(), 1.0F);
//    }
//
//    public static void updateModels(ModelLoader modelLoader)
//    {
//        try
//        {
//            webModel = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "web.obj"), ImmutableList.of("Sphere"));
//        }
//        catch (Exception e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void preRenderCallback(EntitySpiderQueen entity, float partialTickTime)
//    {
//        if (entity.getBurrowedCount() >= 0)
//        {
//            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
//            GL11.glTranslatef(0.0F, entity.getHealth(), 0.0F);
//        }
//        GL11.glScalef(1.5F, 1.5F, 1.5F);
//        GL11.glRotatef((float) (Math.pow(entity.deathTicks, 2) / 5.0F + (Math.pow(entity.deathTicks, 2) / 5.0F - Math.pow(entity.deathTicks - 1, 2) / 5.0F) * partialTickTime), 0.0F, 1.0F, 0.0F);
//        super.preRenderCallback(entity, partialTickTime);
//    }
//
//    @Override
//    public void doRender(EntitySpiderQueen entity, double x, double y, double z, float entityYaw, float partialTicks)
//    {
//        super.doRender(entity, x, y, z, entityYaw, partialTicks);
//
//        GL11.glPushMatrix();
//        GL11.glTranslatef((float) x, (float) y + 0.8F, (float) z);
//        GL11.glScalef(1.4F, 1.5F, 1.4F);
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//
//        RenderHelper.disableStandardItemLighting();
//        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//
//        if (Minecraft.isAmbientOcclusionEnabled())
//        {
//            GlStateManager.shadeModel(GL11.GL_SMOOTH);
//        }
//        else
//        {
//            GlStateManager.shadeModel(GL11.GL_FLAT);
//        }
//
//        if (entity.getBurrowedCount() >= 0)
//        {
//            GL11.glDisable(GL11.GL_CULL_FACE);
//            ClientUtil.drawBakedModel(webModel);
//            GL11.glScalef(1.05F, 1.1F, 1.05F);
//            GL11.glRotatef(192.5F, 0.0F, 1.0F, 0.0F);
//            ClientUtil.drawBakedModel(webModel);
//            GL11.glEnable(GL11.GL_CULL_FACE);
//        }
//
//        RenderHelper.enableStandardItemLighting();
//
//        GL11.glPopMatrix();
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(EntitySpiderQueen juicer)
//    {
//        return spiderTexture;
//    }
//}
