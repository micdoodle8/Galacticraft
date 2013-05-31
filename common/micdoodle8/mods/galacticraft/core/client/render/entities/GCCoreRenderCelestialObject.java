//package micdoodle8.mods.galacticraft.core.client.render.entities;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelCelestialObject;
//import net.minecraft.client.renderer.OpenGlHelper;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.entity.Render;
//import net.minecraft.entity.Entity;
//
//import org.lwjgl.opengl.GL11;
//
//import cpw.mods.fml.client.FMLClientHandler;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//
///**
// * Copyright 2012-2013, micdoodle8
// *
// *  All rights reserved.
// *
// */
//@SideOnly(Side.CLIENT)
//public class GCCoreRenderCelestialObject extends Render
//{
//    private final GCCoreModelCelestialObject objectModel = new GCCoreModelCelestialObject();
//    private final String texture;
//    private final float size;
//
//    private static float lightmapLastX;
//    private static float lightmapLastY;
//
//    public GCCoreRenderCelestialObject(float size, String texture)
//    {
//    	this.size = size;
//    	this.texture = texture;
//    }
//
//	@Override
//	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
//	{
//      	GL11.glPushMatrix();
//	    GL11.glTranslatef((float)d0, (float)d1, (float)d2);
//        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
//        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
//        // <thanks value="MachineMuse|CanVox">
//        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
//        GL11.glDisable(GL11.GL_FOG);
//        lightmapLastX = OpenGlHelper.lastBrightnessX;
//        lightmapLastY = OpenGlHelper.lastBrightnessY;
//        RenderHelper.disableStandardItemLighting();
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
//		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
//        GL11.glScalef(GalacticraftCore.spaceScale * this.size / (50.0F), GalacticraftCore.spaceScale * this.size / (50.0F), GalacticraftCore.spaceScale * this.size / (50.0F));
//        this.objectModel.render(entity, (float)d0, (float)d1, (float)d2, 0, 0, 1.0F / 16.0F);
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapLastX, lightmapLastY);
//        GL11.glEnable(GL11.GL_FOG);
//        // </thanks>
//        GL11.glPopAttrib();
//        GL11.glPopMatrix();
//	}
//}
