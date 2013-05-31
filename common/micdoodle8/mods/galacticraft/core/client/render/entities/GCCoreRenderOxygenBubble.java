package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelOxygenBubble;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityOxygenBubble;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreRenderOxygenBubble extends Render
{
    private final GCCoreModelOxygenBubble oxygenBubbleModel = new GCCoreModelOxygenBubble();

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) d0, (float) d1, (float) d2);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture("/micdoodle8/mods/galacticraft/core/client/entities/bubble.png");

        GL11.glEnable(GL11.GL_BLEND);
        final float f10 = 0.1F;
        GL11.glColor4f(f10, f10, f10, 1.0F);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 1, 1, 0.75F);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glDepthMask(false);
        GL11.glScaled(((GCCoreEntityOxygenBubble) entity).getSize(), ((GCCoreEntityOxygenBubble) entity).getSize(), ((GCCoreEntityOxygenBubble) entity).getSize());

        this.oxygenBubbleModel.render(entity, (float) d0, (float) d1, (float) d2, 0, 0, 1.0F);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glDepthMask(true);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthFunc(GL11.GL_LEQUAL);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
