package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.client.objload.AdvancedModelLoader;
import micdoodle8.mods.galacticraft.core.client.objload.IModelCustom;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBuggy extends Render
{
    private static final ResourceLocation buggyTextureBody = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/buggyMain.png");
    private static final ResourceLocation buggyTextureWheel = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/buggyWheels.png");
    private static final ResourceLocation buggyTextureStorage = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/buggyStorage.png");

    private final IModelCustom modelBuggy = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/buggy.obj"));
    private final IModelCustom modelBuggyWheelRight = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/buggyWheelRight.obj"));
    private final IModelCustom modelBuggyWheelLeft = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/buggyWheelLeft.obj"));

    public RenderBuggy()
    {
        super(FMLClientHandler.instance().getClient().getRenderManager());
        this.shadowSize = 2.0F;
    }

    protected ResourceLocation func_110779_a(EntityBuggy par1EntityArrow)
    {
        return RenderBuggy.buggyTextureBody;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110779_a((EntityBuggy) par1Entity);
    }

    public void renderBuggy(EntityBuggy entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
        GL11.glTranslatef((float) par2, (float) par4 - 2.5F, (float) par6);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(0.41F, 0.41F, 0.41F);
        this.bindTexture(RenderBuggy.buggyTextureWheel);

        float rotation = entity.wheelRotationX;

        // Front wheel covers
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 1.0F, -2.6F);
        GL11.glRotatef(entity.wheelRotationZ, 0, 1, 0);
        GL11.glTranslatef(1.4F, 0.0F, 0.0F);
        this.modelBuggyWheelRight.renderPart("WheelRightCover_Cover");
        GL11.glTranslatef(-2.8F, 0.0F, 0.0F);
        this.modelBuggyWheelLeft.renderPart("WheelLeftCover_Cover");
        GL11.glPopMatrix();

        // Back wheel covers
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 1.0F, 3.7F);
        GL11.glRotatef(-entity.wheelRotationZ, 0, 1, 0);
        GL11.glTranslatef(2.0F, 0.0F, 0.0F);
        this.modelBuggyWheelRight.renderPart("WheelRightCover_Cover");
        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
        this.modelBuggyWheelLeft.renderPart("WheelLeftCover_Cover");
        GL11.glPopMatrix();

        // Front wheels
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 1.0F, -2.7F);
        GL11.glRotatef(entity.wheelRotationZ, 0, 1, 0);
        GL11.glRotatef(rotation, 1, 0, 0);
        GL11.glTranslatef(1.4F, 0.0F, 0.0F);
        this.modelBuggyWheelRight.renderPart("WheelRight_Wheel");
        GL11.glTranslatef(-2.8F, 0.0F, 0.0F);
        this.modelBuggyWheelLeft.renderPart("WheelLeft_Wheel");
        GL11.glPopMatrix();

        // Back wheels
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 1.0F, 3.6F);
        GL11.glRotatef(-entity.wheelRotationZ, 0, 1, 0);
        GL11.glRotatef(rotation, 1, 0, 0);
        GL11.glTranslatef(2.0F, 0.0F, 0.0F);
        this.modelBuggyWheelRight.renderPart("WheelRight_Wheel");
        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
        this.modelBuggyWheelLeft.renderPart("WheelLeft_Wheel");
        GL11.glPopMatrix();

        this.bindTexture(RenderBuggy.buggyTextureBody);
        this.modelBuggy.renderPart("MainBody");

        // Radar Dish
        GL11.glPushMatrix();
        GL11.glTranslatef(-1.178F, 4.1F, -2.397F);
        GL11.glRotatef((float)Math.sin(entity.ticksExisted * 0.05) * 50.0F, 1, 0, 0);
        GL11.glRotatef((float)Math.cos(entity.ticksExisted * 0.1) * 50.0F, 0, 0, 1);
        this.modelBuggy.renderPart("RadarDish_Dish");
        GL11.glPopMatrix();

        this.bindTexture(RenderBuggy.buggyTextureStorage);

        if (entity.buggyType > 0)
        {
            this.modelBuggy.renderPart("CargoLeft");

            if (entity.buggyType > 1)
            {
                this.modelBuggy.renderPart("CargoMid");

                if (entity.buggyType > 2)
                {
                    this.modelBuggy.renderPart("CargoRight");
                }
            }
        }

        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderBuggy((EntityBuggy) par1Entity, par2, par4, par6, par8, par9);
    }
}
