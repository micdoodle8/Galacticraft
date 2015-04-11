package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDish;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;

public class TileEntityDishRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation textureSupport = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/telesupport.png");
    private static final ResourceLocation textureFork = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/telefork.png");
    private static final ResourceLocation textureDish = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/teledish.png");
    private static final IModelCustom modelSupport = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/telesupport.obj"));
    private static final IModelCustom modelFork = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/telefork.obj"));
    private static final IModelCustom modelDish = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/teledish.obj"));
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;

    @Override
    public void renderTileEntityAt(TileEntity var1, double par2, double par4, double par6, float partialTickTime)
    {
        TileEntityDish dish = (TileEntityDish) var1;
        float time = (dish.ticks + partialTickTime) % 1440F;
        final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
        
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glTranslatef(1.0F, 1.0F, 1.0F);
        GL11.glScalef(2.0F, 2.0F, 2.0F);

        this.renderEngine.bindTexture(textureSupport);
        modelSupport.renderAll();
        GL11.glRotatef(time / 4, 0, -1, 0);
        this.renderEngine.bindTexture(textureFork);
        modelFork.renderAll();

//        float celestialAngle = (dish.getWorldObj().getCelestialAngle(1.0F) - 0.784690560F) * 360.0F;
//        float celestialAngle2 = dish.getWorldObj().getCelestialAngle(1.0F) * 360.0F;

        GL11.glTranslatef(0.0F, 2.3F, 0.0F);
        GL11.glRotatef((MathHelper.sin(time / 144) + 1.0F) * 22.5F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.0F, -2.3F, 0.0F);

        this.renderEngine.bindTexture(textureDish);
        modelDish.renderAll();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
