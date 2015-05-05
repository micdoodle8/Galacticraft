package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import java.util.ArrayList;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
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
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderAstroMiner extends Render
{
	private static final float LSIZE = 0.12F;
	private static final float RETRACTIONSPEED = 0.02F;
	private static ResourceLocation scanTexture;
    private RenderBlocks blockRenderer = new RenderBlocks();
    private float spin;
    private float lastPartTime;

    private ResourceLocation modelTexture;
    private ResourceLocation modelTextureFX;
    private ResourceLocation modelTextureOff;
    protected IModelCustom modelObj;
    protected IModelCustom modellaser1;
    protected IModelCustom modellaser2;
    protected IModelCustom modellaser3;
    protected IModelCustom modellasergl;
    protected IModelCustom modellasergr;

    private final NoiseModule wobbleX;
    private final NoiseModule wobbleY;
    private final NoiseModule wobbleZ;
    private final NoiseModule wobbleXX;
    private final NoiseModule wobbleYY;
    private final NoiseModule wobbleZZ;

    public RenderAstroMiner()
    {
    	this.modelObj = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/astroMiner.obj"));
    	this.modellaser1 = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/astroMinerLaserFront.obj"));
    	this.modellaser2 = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/astroMinerLaserBottom.obj"));
    	this.modellaser3 = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/astroMinerLaserCenter.obj"));
    	this.modellasergl = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/astroMinerLeftGuard.obj"));
    	this.modellasergr = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/astroMinerRightGuard.obj"));
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
        int ais = ((EntityAstroMiner)entity).AIstate;
        boolean active = ais > EntityAstroMiner.AISTATE_ATBASE;

        float time = astroMiner.ticksExisted + partialTickTime;
        float sinOfTheTime = (MathHelper.sin(time / 4) + 1F)/4F + 0.5F;
        float wx = active ? this.wobbleX.getNoise(time) + this.wobbleXX.getNoise(time) : 0F;        
        float wy = active ? this.wobbleY.getNoise(time) + this.wobbleYY.getNoise(time) : 0F;
        float wz = active ? this.wobbleZ.getNoise(time) + this.wobbleZZ.getNoise(time) : 0F;
        
        float partTime = partialTickTime - lastPartTime;
        lastPartTime = partialTickTime;
        while (partTime < 0) partTime += 1F;

//      RenderHelper.enableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        final float rotPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTickTime;
        final float rotYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTickTime;

        GL11.glTranslatef((float)x, (float)y + 1.4F, (float)z);
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
        GL11.glTranslatef(0F, -0.35F, 0.2F);
        GL11.glScalef(0.05F, 0.05F, 0.05F);
        GL11.glTranslatef(wx, wy, wz);

        if (active)
        {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.modelTexture);
	        this.modelObj.renderAllExcept("Hoverpad_Front_Left_Top", "Hoverpad_Front_Right_Top", "Hoverpad_Front_Left_Bottom", "Hoverpad_Front_Right_Bottom", "Hoverpad_Rear_Right", "Hoverpad_Rear_Left", "Hoverpad_Heavy_Right", "Hoverpad_Heavy_Left", "Hoverpad_Heavy_Rear", "Hoverpad_Front_Left_Top_Glow", "Hoverpad_Front_Right_Top_Glow", "Hoverpad_Front_Left_Bottom_Glow", "Hoverpad_Front_Right_Bottom_Glow", "Hoverpad_Rear_Right_Glow", "Hoverpad_Rear_Left_Glow", "Hoverpad_Heavy___Glow002", "Hoverpad_Heavy___Glow001", "Hoverpad_Heavy___Glow003");

	        renderLaserModel(astroMiner.retraction);
	        
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
		        
		        int removeCount = 0;
		        int afterglowCount = 0;
		        GL11.glPopMatrix();
		        GL11.glPushMatrix();
		        GL11.glTranslatef((float)(x - astroMiner.posX), (float)(y - astroMiner.posY), (float)(z - astroMiner.posZ));
		        for (Integer blockTime : new ArrayList<Integer>(astroMiner.laserTimes))
		        {
		        	if (blockTime < astroMiner.ticksExisted - 19) removeCount++;
		        	else if (blockTime < astroMiner.ticksExisted - 3) afterglowCount++;
		        }
		        if (removeCount > 0) astroMiner.removeLaserBlocks(removeCount);
		        int count = 0;
		        for (BlockVec3 blockLaser : new ArrayList<BlockVec3>(astroMiner.laserBlocks))
		        {
		        	if (count < afterglowCount)
		        	{
			        	int fade = astroMiner.ticksExisted - astroMiner.laserTimes.get(count) - 8;
			        	if (fade < 0) fade = 0;
		        		doAfterGlow(blockLaser, fade);
		        	}
		        	else doLaser(astroMiner, blockLaser);
		        	count ++;
		        }
		        if (astroMiner.retraction > 0F) astroMiner.retraction -= RETRACTIONSPEED;
		        GL11.glPopMatrix();
	        }
	        else
	        {
		        if (astroMiner.retraction < 1F) astroMiner.retraction += RETRACTIONSPEED;
		        GL11.glPopMatrix();
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
	        renderLaserModel(astroMiner.retraction);
	        if (astroMiner.retraction < 1F) astroMiner.retraction += RETRACTIONSPEED;
            GL11.glPopMatrix();
        }
    }

	private void doAfterGlow(BlockVec3 blockLaser, int level)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(blockLaser.x, blockLaser.y, blockLaser.z);
        final Tessellator tess = Tessellator.instance;
        GL11.glColor4f(1.0F, 0.7F, 0.7F, 0.016667F * (12 - level));
        float cA = -0.01F;
        float cB = 1.01F;
        tess.startDrawingQuads();
        tess.addVertexWithUV(cA, cB, cA, 0D, 1D);
        tess.addVertexWithUV(cB, cB, cA, 1D, 1D);
        tess.addVertexWithUV(cB, cB, cB, 1D, 0D);
        tess.addVertexWithUV(cA, cB, cB, 0D, 0D);
        tess.draw();
        tess.startDrawingQuads();
        tess.addVertexWithUV(cA, cA, cA, 0D, 0D);
        tess.addVertexWithUV(cA, cA, cB, 0D, 1D);
        tess.addVertexWithUV(cB, cA, cB, 1D, 1D);
        tess.addVertexWithUV(cB, cA, cA, 1D, 0D);
        tess.draw();
        tess.startDrawingQuads();
        tess.addVertexWithUV(cA, cA, cA, 1D, 0D);
        tess.addVertexWithUV(cA, cB, cA, 0D, 0D);
        tess.addVertexWithUV(cA, cB, cB, 0D, 1D);
        tess.addVertexWithUV(cA, cA, cB, 1D, 1D);
        tess.draw();
        tess.startDrawingQuads();
        tess.addVertexWithUV(cB, cA, cA, 1D, 1D);
        tess.addVertexWithUV(cB, cA, cB, 1D, 0D);
        tess.addVertexWithUV(cB, cB, cB, 0D, 0D);
        tess.addVertexWithUV(cB, cB, cA, 0D, 1D);
        tess.draw();   	
        tess.startDrawingQuads();
        tess.addVertexWithUV(cA, cA, cA, 1D, 0D);
        tess.addVertexWithUV(1F, cA, cA, 0D, 0D);
        tess.addVertexWithUV(1F, 1F, cA, 0D, 1D);
        tess.addVertexWithUV(cA, 1F, cA, 1D, 1D);
        tess.draw();
        tess.startDrawingQuads();
        tess.addVertexWithUV(1F, cA, 1F, 1D, 1D);
        tess.addVertexWithUV(cA, cA, 1F, 1D, 0D);
        tess.addVertexWithUV(cA, 1F, 1F, 0D, 0D);
        tess.addVertexWithUV(1F, 1F, 1F, 0D, 1D);
        tess.draw();   	
        
    	
        GL11.glPopMatrix();
	}

    private void doLaser(EntityAstroMiner entity, BlockVec3 blockLaser)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(blockLaser.x, blockLaser.y, blockLaser.z);
        final Tessellator tess = Tessellator.instance;
        GL11.glColor4f(1.0F, 0.7F, 0.7F, 0.2F);
        float cA = -0.01F;
        float cB = 1.01F;
        tess.startDrawingQuads(); 
        tess.addVertexWithUV(cA, cB, cA, 0D, 1D);
        tess.addVertexWithUV(cB, cB, cA, 1D, 1D);
        tess.addVertexWithUV(cB, cB, cB, 1D, 0D);
        tess.addVertexWithUV(cA, cB, cB, 0D, 0D);
        tess.draw();
        tess.startDrawingQuads();
        tess.addVertexWithUV(cA, cA, cA, 0D, 0D);
        tess.addVertexWithUV(cA, cA, cB, 0D, 1D);
        tess.addVertexWithUV(cB, cA, cB, 1D, 1D);
        tess.addVertexWithUV(cB, cA, cA, 1D, 0D);
        tess.draw();
        tess.startDrawingQuads();
        tess.addVertexWithUV(cA, cA, cA, 1D, 0D);
        tess.addVertexWithUV(cA, cB, cA, 0D, 0D);
        tess.addVertexWithUV(cA, cB, cB, 0D, 1D);
        tess.addVertexWithUV(cA, cA, cB, 1D, 1D);
        tess.draw();
        tess.startDrawingQuads();
        tess.addVertexWithUV(cB, cA, cA, 1D, 1D);
        tess.addVertexWithUV(cB, cA, cB, 1D, 0D);
        tess.addVertexWithUV(cB, cB, cB, 0D, 0D);
        tess.addVertexWithUV(cB, cB, cA, 0D, 1D);
        tess.draw();   	
        tess.startDrawingQuads();
        tess.addVertexWithUV(cA, cA, cA, 1D, 0D);
        tess.addVertexWithUV(1F, cA, cA, 0D, 0D);
        tess.addVertexWithUV(1F, 1F, cA, 0D, 1D);
        tess.addVertexWithUV(cA, 1F, cA, 1D, 1D);
        tess.draw();
        tess.startDrawingQuads();
        tess.addVertexWithUV(1F, cA, 1F, 1D, 1D);
        tess.addVertexWithUV(cA, cA, 1F, 1D, 0D);
        tess.addVertexWithUV(cA, 1F, 1F, 0D, 0D);
        tess.addVertexWithUV(1F, 1F, 1F, 0D, 1D);
        tess.draw();
        
        GL11.glColor4f(1.0F, 0.79F, 0.79F, 0.17F);
        float bb = 1.75F;
        float cc = 0.6F;
        float radiansYaw = entity.rotationYaw * 0.017453292F;
        float radiansPitch = entity.rotationPitch * 0.017453292F / 4F;
        float mainLaserX = bb * MathHelper.sin(radiansYaw) * MathHelper.cos(radiansPitch);
        float mainLaserY = cc + bb * MathHelper.sin(radiansPitch);
        float mainLaserZ = bb * MathHelper.cos(radiansYaw) * MathHelper.cos(radiansPitch);
        
        mainLaserX += entity.posX - blockLaser.x;
        mainLaserY += entity.posY - blockLaser.y;
        mainLaserZ += entity.posZ - blockLaser.z;
        
        float xD = (mainLaserX - 0.5F);
        float yD = (mainLaserY - 0.5F);
        float zD = (mainLaserZ - 0.5F);
        float xDa = Math.abs(xD);
        float yDa = Math.abs(yD);
        float zDa = Math.abs(zD);
        
        float xx, yy, zz;
        
        if (entity.facing > 3)
        {
            xx = ((xD < 0) ? cA : cB);
            drawLaserX(mainLaserX, mainLaserY, mainLaserZ, xx, 0.5F, 0.5F);
        }
        else if (entity.facing < 2)
        {
            yy = ((yD < 0) ? cA : cB);
            drawLaserY(mainLaserX, mainLaserY, mainLaserZ, 0.5F, yy, 0.5F);
        }
        else
        {
            zz = ((zD < 0) ? cA : cB);
            drawLaserZ(mainLaserX, mainLaserY, mainLaserZ, 0.5F, 0.5F, zz);
        }
    	
        GL11.glPopMatrix();
   	}
    
    private void drawLaserX(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertex(x1, y1 - 0.01F, z1 - 0.01F);
        tess.addVertex(x2, y2 - LSIZE, z2 - LSIZE);
        tess.addVertex(x2, y2 + LSIZE, z2 - LSIZE);
        tess.addVertex(x1, y1 + 0.01F, z1 - 0.01F);
        tess.draw();   	
        tess.startDrawingQuads();
        tess.addVertex(x1, y1 - 0.01F, z1 + 0.01F);
        tess.addVertex(x2, y2 - LSIZE, z2 + LSIZE);
        tess.addVertex(x2, y2 + LSIZE, z2 + LSIZE);
        tess.addVertex(x1, y1 + 0.01F, z1 + 0.01F);
        tess.draw();   	
        tess.startDrawingQuads();
        tess.addVertex(x1, y1 - 0.01F, z1 - 0.01F);
        tess.addVertex(x2, y2 - LSIZE, z2 - LSIZE);
        tess.addVertex(x2, y2 - LSIZE, z2 + LSIZE);
        tess.addVertex(x1, y1 - 0.01F, z1 + 0.01F);
        tess.draw();   	
        tess.startDrawingQuads();
        tess.addVertex(x1, y1 + 0.01F, z1 + 0.01F);
        tess.addVertex(x2, y2 + LSIZE, z2 + LSIZE);
        tess.addVertex(x2, y2 + LSIZE, z2 - LSIZE);
        tess.addVertex(x1, y1 + 0.01F, z1 - 0.01F);
        tess.draw();
    }

    private void drawLaserY(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertex(x1 - 0.01F, y1, z1 - 0.01F);
        tess.addVertex(x2 - LSIZE, y2, z2 - LSIZE);
        tess.addVertex(x2 + LSIZE, y2, z2 - LSIZE);
        tess.addVertex(x1 + 0.01F, y1, z1 - 0.01F);
        tess.draw();   	
        tess.startDrawingQuads();
        tess.addVertex(x1 - 0.01F, y1, z1 + 0.01F);
        tess.addVertex(x2 - LSIZE, y2, z2 + LSIZE);
        tess.addVertex(x2 + LSIZE, y2, z2 + LSIZE);
        tess.addVertex(x1 + 0.01F, y1, z1 + 0.01F);
        tess.draw();   	
        tess.startDrawingQuads();
        tess.addVertex(x1 - 0.01F, y1, z1 - 0.01F);
        tess.addVertex(x2 - LSIZE, y2, z2 - LSIZE);
        tess.addVertex(x2 - LSIZE, y2, z2 + LSIZE);
        tess.addVertex(x1 - 0.01F, y1, z1 + 0.01F);
        tess.draw();   	
        tess.startDrawingQuads();
        tess.addVertex(x1 + 0.01F, y1, z1 + 0.01F);
        tess.addVertex(x2 + LSIZE, y2, z2 + LSIZE);
        tess.addVertex(x2 + LSIZE, y2, z2 - LSIZE);
        tess.addVertex(x1 + 0.01F, y1, z1 - 0.01F);
        tess.draw();
    }

    private void drawLaserZ(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertex(x1 - 0.01F, y1 - 0.01F, z1);
        tess.addVertex(x2 - LSIZE, y2 - LSIZE, z2);
        tess.addVertex(x2 - LSIZE, y2 + LSIZE, z2);
        tess.addVertex(x1 - 0.01F, y1 + 0.01F, z1);
        tess.draw();   	
        tess.startDrawingQuads();
        tess.addVertex(x1 + 0.01F, y1 - 0.01F, z1);
        tess.addVertex(x2 + LSIZE, y2 - LSIZE, z2);
        tess.addVertex(x2 + LSIZE, y2 + LSIZE, z2);
        tess.addVertex(x1 + 0.01F, y1 + 0.01F, z1);
        tess.draw();   	
        tess.startDrawingQuads();
        tess.addVertex(x1 - 0.01F, y1 - 0.01F, z1);
        tess.addVertex(x2 - LSIZE, y2 - LSIZE, z2);
        tess.addVertex(x2 + LSIZE, y2 - LSIZE, z2);
        tess.addVertex(x1 + 0.01F, y1 - 0.01F, z1);
        tess.draw();   	
        tess.startDrawingQuads();
        tess.addVertex(x1, y1 + 0.01F, z1 + 0.01F);
        tess.addVertex(x2, y2 + LSIZE, z2 + LSIZE);
        tess.addVertex(x2, y2 + LSIZE, z2 - LSIZE);
        tess.addVertex(x1, y1 + 0.01F, z1 - 0.01F);
        tess.draw();
    }

    private void renderLaserModel(float retraction)
    {
    	float laserretraction = retraction / 0.8F;
    	if (laserretraction > 1F) laserretraction = 1F;
    	float guardmovement = (retraction - 0.6F) / 0.4F;
    	if (guardmovement < 0F) guardmovement = 0F;
    	GL11.glPushMatrix();       
    	float zadjust = laserretraction * 5F;
    	float yadjust = zadjust;
    	if (yadjust > 0.938F)
    	{	
    		yadjust = 0.938F;
    		zadjust = (zadjust - yadjust) * 2F + yadjust;
    	}
        GL11.glTranslatef(0F, yadjust, zadjust);
	    this.modellaser1.renderAll();
	    this.modellaser2.renderAll();
        GL11.glPopMatrix();
	    this.modellaser3.renderAll();
        GL11.glPushMatrix();
        GL11.glTranslatef(guardmovement, 0F, 0F);
	    this.modellasergl.renderAll();
        GL11.glTranslatef(-2 * guardmovement, 0F, 0F);
	    this.modellasergr.renderAll();
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.modelTextureOff;
    }
}
