package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.google.common.base.Function;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemModelGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderGrapple extends Render<EntityGrapple>
{
    private ItemModelGrapple grappleModel;

    public RenderGrapple(RenderManager manager)
    {
        super(manager);
    }

    private void updateModel()
    {
        if (grappleModel == null)
        {
            Function<ResourceLocation, TextureAtlasSprite> TEXTUREGETTER = input -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());

            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "grapple", "inventory");
            grappleModel = (ItemModelGrapple) FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().getModelManager().getModel(modelResourceLocation);
        }
    }

    @Override
    public void doRender(EntityGrapple grapple, double x, double y, double z, float par8, float partialTicks)
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();

        Vec3 vec3 = new Vec3(0.0D, -0.2D, 0.0D);
        EntityPlayer shootingEntity = grapple.getShootingEntity();

        if (shootingEntity != null && grapple.getPullingEntity())
        {
            double d3 = shootingEntity.prevPosX + (shootingEntity.posX - shootingEntity.prevPosX) * partialTicks + vec3.xCoord;
            double d4 = shootingEntity.prevPosY + (shootingEntity.posY - shootingEntity.prevPosY) * partialTicks + vec3.yCoord;
            double d5 = shootingEntity.prevPosZ + (shootingEntity.posZ - shootingEntity.prevPosZ) * partialTicks + vec3.zCoord;

            Tessellator tessellator = Tessellator.getInstance();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            tessellator.getWorldRenderer().begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            byte b2 = 16;

            double d14 = grapple.prevPosX + (grapple.posX - grapple.prevPosX) * partialTicks;
            double d8 = grapple.prevPosY + (grapple.posY - grapple.prevPosY) * partialTicks + 0.25D;
            double d10 = grapple.prevPosZ + (grapple.posZ - grapple.prevPosZ) * partialTicks;
            double d11 = (float) (d3 - d14);
            double d12 = (float) (d4 - d8);
            double d13 = (float) (d5 - d10);
            tessellator.getWorldRenderer().setTranslation(0, -0.2F, 0);

            for (int i = 0; i <= b2; ++i)
            {
                float f12 = (float) i / (float) b2;
                tessellator.getWorldRenderer().pos(x + d11 * f12, y + d12 * (f12 * f12 + f12) * 0.5D + 0.15D, z + d13 * f12).color(203.0F / 255.0F, 203.0F / 255.0F, 192.0F / 255.0F, 1.0F).endVertex();
            }

            tessellator.draw();
            tessellator.getWorldRenderer().setTranslation(0, 0, 0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glRotatef(grapple.prevRotationYaw + (grapple.rotationYaw - grapple.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(grapple.prevRotationPitch + (grapple.rotationPitch - grapple.prevRotationPitch) * partialTicks - 180, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(grapple.prevRotationRoll + (grapple.rotationRoll - grapple.prevRotationRoll) * partialTicks, 1.0F, 0.0F, 0.0F);

        updateModel();

        this.bindTexture(TextureMap.locationBlocksTexture);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        ClientUtil.drawBakedModel(grappleModel);

//        this.bindEntityTexture(grapple);
//        ItemRendererGrappleHook.modelGrapple.renderAll(); TODO

        GL11.glPopMatrix();
    }

//    protected ResourceLocation getEntityTexture(EntityGrapple grapple)
//    {
////        return ItemRendererGrappleHook.grappleTexture;
//    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGrapple entity)
    {
        return new ResourceLocation("missing");
    }
    
    @Override
    public boolean shouldRender(EntityGrapple entity, ICamera camera, double camX, double camY, double camZ)
    {
        return entity.isInRangeToRender3d(camX, camY, camZ);
    }
}
