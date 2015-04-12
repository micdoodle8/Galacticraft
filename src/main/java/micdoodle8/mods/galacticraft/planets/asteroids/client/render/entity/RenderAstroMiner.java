package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderAstroMiner extends Render
{
	private static ResourceLocation scanTexture;
    private RenderBlocks blockRenderer = new RenderBlocks();
    private float spin;
    private float lastPartTime;

    private ResourceLocation modelTexture;
    private ResourceLocation modelTextureFX;
    private ResourceLocation modelTextureOff;
    protected IModelCustom modelObj;

    private final NoiseModule wobbleX;
    private final NoiseModule wobbleY;
    private final NoiseModule wobbleZ;
    private final NoiseModule wobbleXX;
    private final NoiseModule wobbleYY;
    private final NoiseModule wobbleZZ;

    public RenderAstroMiner()
    {
    	this.modelObj = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/astroMiner.obj"));
        this.modelTexture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/astroMiner.png");
        this.modelTextureFX = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/astroMinerFX.png");
        this.modelTextureOff = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/astroMiner_off.png");
        this.scanTexture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/misc/gradient.png");
        this.shadowSize = 2F;
        
        Random rand = new Random();
        this.wobbleX = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleX.amplitude = 0.5F;
        this.wobbleX.frequencyX = 0.025F;

        this.wobbleY = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleY.amplitude = 0.6F;
        this.wobbleY.frequencyX = 0.025F;

        this.wobbleZ = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleZ.amplitude = 0.1F;
        this.wobbleZ.frequencyX = 0.025F;

        this.wobbleXX = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleXX.amplitude = 0.1F;
        this.wobbleXX.frequencyX = 0.8F;

        this.wobbleYY = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleYY.amplitude = 0.15F;
        this.wobbleYY.frequencyX = 0.8F;

        this.wobbleZZ = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleZZ.amplitude = 0.04F;
        this.wobbleZZ.frequencyX = 0.8F;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f, float partialTickTime)
    {
        EntityAstroMiner astroMiner = (EntityAstroMiner) entity;
        float time = astroMiner.ticksExisted + partialTickTime;
        float sinOfTheTime = (MathHelper.sin(time / 4) + 1F)/4F + 0.5F;
        float wx = this.wobbleX.getNoise(time) + this.wobbleXX.getNoise(time);        
        float wy = this.wobbleY.getNoise(time) + this.wobbleYY.getNoise(time);
        float wz = this.wobbleZ.getNoise(time) + this.wobbleZZ.getNoise(time);
        
        float partTime = partialTickTime - lastPartTime;
        lastPartTime = partialTickTime;
        while (partTime < 0) partTime += 1F;

        GL11.glPushMatrix();
        final float rotPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTickTime;
        final float rotYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTickTime;

        /*
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        RenderGlobal.drawOutlinedBoundingBox(asteroid.boundingBox, 16777215);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
         */
        GL11.glTranslatef((float)x, (float)y + 1.55F, (float)z);
        float partBlock;
        switch (astroMiner.facing)
        {
        case 0:
        	partBlock = (float) (astroMiner.posY % 1D);
        	break;
        case 1:
        	partBlock = 1F - (float) (astroMiner.posY % 1D);
        	break;
        case 2:
        	partBlock = (float) (astroMiner.posZ % 1D);
        	break;
        case 3:
        	partBlock = 1F - (float) (astroMiner.posZ % 1D);
        	break;
        case 4:
        	partBlock = (float) (astroMiner.posX % 1D);
        	break;
        case 5:
        	partBlock = 1F - (float) (astroMiner.posX % 1D);
        	break;
        default:
        	partBlock = 0F;
        }
        partBlock /= 0.06F;
        
//        else if (rotPitch > 0F)
//        {
//            GL11.glTranslatef(-0.65F, -0.65F, 0);
//        	GL11.glRotatef(rotPitch, 0, 0, 1);
//            GL11.glTranslatef(0.65F, 0.65F, 0);
//        }
        GL11.glRotatef(rotYaw + 180F, 0, 1, 0);
        if (rotPitch != 0F)
        {
            GL11.glTranslatef(-0.65F, -0.65F, 0);
        	GL11.glRotatef(rotPitch / 4F, 1, 0, 0);
            GL11.glTranslatef(0.65F, 0.65F, 0);
        }
        GL11.glTranslatef(0F, -0.35F, 0.25F);
        GL11.glScalef(0.05F, 0.05F, 0.05F);
        GL11.glTranslatef(wx, wy, wz);

        int ais = ((EntityAstroMiner)entity).AIstate;
        boolean active = ais > EntityAstroMiner.AISTATE_ATBASE;

        if (active)
        {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.modelTexture);
	        this.modelObj.renderAllExcept("Hoverpad_Front_Left_Top", "Hoverpad_Front_Right_Top", "Hoverpad_Front_Left_Bottom", "Hoverpad_Front_Right_Bottom", "Hoverpad_Rear_Right", "Hoverpad_Rear_Left", "Hoverpad_Heavy_Right", "Hoverpad_Heavy_Left", "Hoverpad_Heavy_Rear", "Hoverpad_Front_Left_Top_Glow", "Hoverpad_Front_Right_Top_Glow", "Hoverpad_Front_Left_Bottom_Glow", "Hoverpad_Front_Right_Bottom_Glow", "Hoverpad_Rear_Right_Glow", "Hoverpad_Rear_Left_Glow", "Hoverpad_Heavy___Glow002", "Hoverpad_Heavy___Glow001", "Hoverpad_Heavy___Glow003");
	
	        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
	        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
	        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glColor4f(sinOfTheTime, sinOfTheTime, sinOfTheTime, 1.0F);
	        this.modelObj.renderOnly("Hoverpad_Front_Left_Top", "Hoverpad_Front_Right_Top", "Hoverpad_Front_Left_Bottom", "Hoverpad_Front_Right_Bottom", "Hoverpad_Rear_Right", "Hoverpad_Rear_Left", "Hoverpad_Heavy_Right", "Hoverpad_Heavy_Left", "Hoverpad_Heavy_Rear");
	
	        FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.modelTextureFX);
	        GL11.glDisable(GL11.GL_CULL_FACE);
	        GL11.glDisable(GL11.GL_ALPHA_TEST);
	        GL11.glDepthMask(false);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	        GL11.glColor4f(sinOfTheTime, sinOfTheTime, sinOfTheTime, 0.6F);
	        this.modelObj.renderOnly("Hoverpad_Front_Left_Top_Glow", "Hoverpad_Front_Right_Top_Glow", "Hoverpad_Front_Left_Bottom_Glow", "Hoverpad_Front_Right_Bottom_Glow", "Hoverpad_Rear_Right_Glow", "Hoverpad_Rear_Left_Glow", "Hoverpad_Heavy___Glow002", "Hoverpad_Heavy___Glow001", "Hoverpad_Heavy___Glow003");

	        if (ais < EntityAstroMiner.AISTATE_DOCKING)
	        {
	        	FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.scanTexture);
		        final Tessellator tess = Tessellator.instance;
		        GL11.glColor4f(0, 0.6F, 1.0F, 0.2F);
		        tess.startDrawingQuads(); 
		        tess.addVertexWithUV(15.6F, -0.6F, -20F, 0D, 0D);
		        tess.addVertexWithUV(37.8F, 31.4F, -45F - partBlock, 1D, 0D);
		        tess.addVertexWithUV(37.8F, -32.6F, -45F - partBlock, 1D, 1D);
		        tess.addVertexWithUV(15.6F, -0.7F, -20F, 0D, 1D);
		        tess.draw();   	
		        tess.startDrawingQuads(); 
		        tess.addVertexWithUV(-15.6F, -0.6F, -20F, 0D, 0D);
		        tess.addVertexWithUV(-37.8F, 31.4F, -45F - partBlock, 1D, 0D);
		        tess.addVertexWithUV(-37.8F, -32.6F, -45F - partBlock, 1D, 1D);
		        tess.addVertexWithUV(-15.6F, -0.7F, -20F, 0D, 1D);
		        tess.draw();
	        }
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glEnable(GL11.GL_CULL_FACE);
	        GL11.glEnable(GL11.GL_ALPHA_TEST);
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glDepthMask(true);
	        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        }
        else
        {
            this.bindEntityTexture(astroMiner);
            this.modelObj.renderAllExcept("Hoverpad_Front_Left_Top_Glow", "Hoverpad_Front_Right_Top_Glow", "Hoverpad_Front_Left_Bottom_Glow", "Hoverpad_Front_Right_Bottom_Glow", "Hoverpad_Rear_Right_Glow", "Hoverpad_Rear_Left_Glow", "Hoverpad_Heavy___Glow002", "Hoverpad_Heavy___Glow001", "Hoverpad_Heavy___Glow003");
        }
        
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.modelTextureOff;
    }
}
