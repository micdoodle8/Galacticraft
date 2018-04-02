package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.google.common.base.Function;

import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemModelCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCargoRocket extends Render<EntityCargoRocket>
{
    private ItemModelCargoRocket rocketModel;

    public RenderCargoRocket(RenderManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.5F;
    }

    private void updateModel()
    {
        if (rocketModel == null)
        {
            Function<ResourceLocation, TextureAtlasSprite> TEXTUREGETTER = input -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());

            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "rocket_cargo", "inventory");
            rocketModel = (ItemModelCargoRocket) FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().getModelManager().getModel(modelResourceLocation);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCargoRocket par1Entity)
    {
        return new ResourceLocation("missing");
    }

    @Override
    public void doRender(EntityCargoRocket entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
        GL11.glTranslatef((float) par2, (float) par4 + entity.getRenderOffsetY(), (float) par6);
        GL11.glScalef(0.4F, 0.4F, 0.4F);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);

        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.locationBlocksTexture);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        updateModel();

        ClientUtil.drawBakedModel(rocketModel);

        GL11.glPopMatrix();
        RenderHelper.enableStandardItemLighting();
    }
    
    @Override
    public boolean shouldRender(EntityCargoRocket rocket, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = rocket.getEntityBoundingBox();

        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
