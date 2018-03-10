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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
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
        if (mainModel == null)
        {
            try
            {
                IModel model = OBJLoaderGC.instance.loadModel(new ResourceLocation(Constants.ASSET_PREFIX, "buggy.obj"));
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

                mainModel = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("MainBody"), false), DefaultVertexFormats.ITEM, spriteFunction);
                radarDish = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("RadarDish_Dish"), false), DefaultVertexFormats.ITEM, spriteFunction);
                wheelLeftCover = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Left_Cover"), false), DefaultVertexFormats.ITEM, spriteFunction);
                wheelRight = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Right"), false), DefaultVertexFormats.ITEM, spriteFunction);
                wheelLeft = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Left"), false), DefaultVertexFormats.ITEM, spriteFunction);
                wheelRightCover = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Right_Cover"), false), DefaultVertexFormats.ITEM, spriteFunction);
                cargoLeft = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("CargoLeft"), false), DefaultVertexFormats.ITEM, spriteFunction);
                cargoMid = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("CargoMid"), false), DefaultVertexFormats.ITEM, spriteFunction);
                cargoRight = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("CargoRight"), false), DefaultVertexFormats.ITEM, spriteFunction);
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
    protected ResourceLocation getEntityTexture(EntityBuggy par1Entity)
    {
        return new ResourceLocation("missing");
    }

    @Override
    public void doRender(EntityBuggy entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(0.41F, 0.41F, 0.41F);

        this.updateModels();

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

        float rotation = entity.wheelRotationX;

        // Front wheels
        GL11.glPushMatrix();
        float dZ = -2.727F;
        float dY = 0.976F;
        float dX = 1.25F;
        GL11.glTranslatef(dX, dY, dZ);
        GL11.glRotatef(entity.wheelRotationZ, 0, 1, 0);
        ClientUtil.drawBakedModel(wheelRightCover);
        GL11.glRotatef(rotation, 1, 0, 0);
        ClientUtil.drawBakedModel(wheelRight);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(-dX, dY, dZ);
        GL11.glRotatef(entity.wheelRotationZ, 0, 1, 0);
        ClientUtil.drawBakedModel(wheelLeftCover);
        GL11.glRotatef(rotation, 1, 0, 0);
        ClientUtil.drawBakedModel(wheelLeft);
        GL11.glPopMatrix();

        // Back wheels
        GL11.glPushMatrix();
        dX = 1.9F;
        dZ = -dZ;
        GL11.glTranslatef(dX, dY, dZ);
        GL11.glRotatef(-entity.wheelRotationZ, 0, 1, 0);
        GL11.glRotatef(rotation, 1, 0, 0);
        ClientUtil.drawBakedModel(wheelRight);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(-dX, dY, dZ);
        GL11.glRotatef(-entity.wheelRotationZ, 0, 1, 0);
        GL11.glRotatef(rotation, 1, 0, 0);
        ClientUtil.drawBakedModel(wheelLeft);
        GL11.glPopMatrix();

        ClientUtil.drawBakedModel(mainModel);

        // Radar Dish
        GL11.glPushMatrix();
        GL11.glTranslatef(-1.178F, 4.1F, -2.397F);
        int ticks = entity.ticksExisted + entity.getEntityId() * 10000;
        GL11.glRotatef((float) Math.sin(ticks * 0.05) * 50.0F, 1, 0, 0);
        GL11.glRotatef((float) Math.cos(ticks * 0.1) * 50.0F, 0, 0, 1);
        ClientUtil.drawBakedModel(radarDish);
        GL11.glPopMatrix();

        if (entity.buggyType > 0)
        {
            ClientUtil.drawBakedModel(cargoLeft);

            if (entity.buggyType > 1)
            {
                ClientUtil.drawBakedModel(cargoMid);

                if (entity.buggyType > 2)
                {
                    ClientUtil.drawBakedModel(cargoRight);
                }
            }
        }

        GL11.glPopMatrix();
        RenderHelper.enableStandardItemLighting();
    }
    
    @Override
    public boolean shouldRender(EntityBuggy buggy, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = buggy.getEntityBoundingBox().expand(2D, 1D, 2D);
        return buggy.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
