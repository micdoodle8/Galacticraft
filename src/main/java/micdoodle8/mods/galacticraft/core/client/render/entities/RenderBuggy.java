package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBuggy extends Render<EntityBuggy>
{
    private OBJModel.OBJBakedModel mainModel;
    private OBJModel.OBJBakedModel radarDish;
    private OBJModel.OBJBakedModel wheelLeftCover;
    private OBJModel.OBJBakedModel wheelRight;
    private OBJModel.OBJBakedModel wheelLeft;
    private OBJModel.OBJBakedModel wheelRightCover;
    private OBJModel.OBJBakedModel cargoLeft;
    private OBJModel.OBJBakedModel cargoMid;
    private OBJModel.OBJBakedModel cargoRight;

    private void updateModels()
    {
        if (this.mainModel == null)
        {
            try
            {
                IModel model = OBJLoaderGC.instance.loadModel(new ResourceLocation(Constants.ASSET_PREFIX, "buggy.obj"));
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

                this.mainModel = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("MainBody"), false), DefaultVertexFormats.ITEM, spriteFunction);
                this.radarDish = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("RadarDish_Dish"), false), DefaultVertexFormats.ITEM, spriteFunction);
                this.wheelLeftCover = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Left_Cover"), false), DefaultVertexFormats.ITEM, spriteFunction);
                this.wheelRight = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Right"), false), DefaultVertexFormats.ITEM, spriteFunction);
                this.wheelLeft = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Left"), false), DefaultVertexFormats.ITEM, spriteFunction);
                this.wheelRightCover = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Right_Cover"), false), DefaultVertexFormats.ITEM, spriteFunction);
                this.cargoLeft = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("CargoLeft"), false), DefaultVertexFormats.ITEM, spriteFunction);
                this.cargoMid = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("CargoMid"), false), DefaultVertexFormats.ITEM, spriteFunction);
                this.cargoRight = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("CargoRight"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public RenderBuggy(RenderManager manager)
    {
        super(manager);
        this.shadowSize = 1.0F;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBuggy entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void doRender(EntityBuggy entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        GlStateManager.disableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-pitch, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(0.41F, 0.41F, 0.41F);

        this.updateModels();
        this.bindEntityTexture(entity);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        // Front wheels
        GlStateManager.pushMatrix();
        float dZ = -2.727F;
        float dY = 0.976F;
        float dX = 1.25F;
        float rotation = entity.wheelRotationX;
        GlStateManager.translate(dX, dY, dZ);
        GlStateManager.rotate(entity.wheelRotationZ, 0, 1, 0);
        ClientUtil.drawBakedModel(this.wheelRightCover);
        GlStateManager.rotate(rotation, 1, 0, 0);
        ClientUtil.drawBakedModel(this.wheelRight);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(-dX, dY, dZ);
        GlStateManager.rotate(entity.wheelRotationZ, 0, 1, 0);
        ClientUtil.drawBakedModel(this.wheelLeftCover);
        GlStateManager.rotate(rotation, 1, 0, 0);
        ClientUtil.drawBakedModel(this.wheelLeft);
        GlStateManager.popMatrix();

        // Back wheels
        GlStateManager.pushMatrix();
        dX = 1.9F;
        dZ = -dZ;
        GlStateManager.translate(dX, dY, dZ);
        GlStateManager.rotate(-entity.wheelRotationZ, 0, 1, 0);
        GlStateManager.rotate(rotation, 1, 0, 0);
        ClientUtil.drawBakedModel(this.wheelRight);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(-dX, dY, dZ);
        GlStateManager.rotate(-entity.wheelRotationZ, 0, 1, 0);
        GlStateManager.rotate(rotation, 1, 0, 0);
        ClientUtil.drawBakedModel(this.wheelLeft);
        GlStateManager.popMatrix();

        ClientUtil.drawBakedModel(this.mainModel);

        // Radar Dish
        GlStateManager.pushMatrix();
        GlStateManager.translate(-1.178F, 4.1F, -2.397F);
        int ticks = entity.ticksExisted + entity.getEntityId() * 10000;
        GlStateManager.rotate((float) Math.sin(ticks * 0.05) * 50.0F, 1, 0, 0);
        GlStateManager.rotate((float) Math.cos(ticks * 0.1) * 50.0F, 0, 0, 1);
        ClientUtil.drawBakedModel(this.radarDish);
        GlStateManager.popMatrix();

        if (entity.buggyType > 0)
        {
            ClientUtil.drawBakedModel(this.cargoLeft);

            if (entity.buggyType > 1)
            {
                ClientUtil.drawBakedModel(this.cargoMid);

                if (entity.buggyType > 2)
                {
                    ClientUtil.drawBakedModel(this.cargoRight);
                }
            }
        }

        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }
    
    @Override
    public boolean shouldRender(EntityBuggy buggy, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = buggy.getEntityBoundingBox().grow(2D, 1D, 2D);
        return buggy.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
