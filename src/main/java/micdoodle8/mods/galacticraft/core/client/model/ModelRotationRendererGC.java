package micdoodle8.mods.galacticraft.core.client.model;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.smart.render.ModelRotationRenderer;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Method;

/**
 *  If Smart Moving is installed, this is used by ModelPlayerBaseGC as the ModelRenderer
 *  - see ModelPlayerBaseGC.createModelRenderer()
 *  
 *  This renders the player equipment, there is one of these renderers for each type of equipment.
 *  Smart Moving will call this.doRender() when the corresponding player body part is being drawn.
 *  Most GC equipment is rendered when the body is drawn; Oxygen Mask and Frequency Module are rendered when the head is drawn.
 *  Smart Moving handles all relevant transformations so that the position will match the Smart Moving model.  
 * 
 * @author User
 *
 */
public class ModelRotationRendererGC extends ModelRotationRenderer
{
    private int type;
 
    public ModelRotationRendererGC(ModelBase modelBase, int i, int j, ModelRenderer baseRenderer, int type)
    {
        super(modelBase, i, j, (ModelRotationRenderer)baseRenderer);
        this.type = type;
        ModelPlayerBaseGC.frequencyModule = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/frequencyModule.obj"));
    }

    @Override
    public boolean preRender(float f)
    {
        boolean b = super.preRender(f);

        if (ModelPlayerBaseGC.currentGearData == null)
        {
            return false;
        }

        if (b)
        {
            switch (type)
            {
            case 0:
                return ModelPlayerBaseGC.currentGearData.getMask() > -1;
            case 1:
                return ModelPlayerBaseGC.currentGearData.getParachute() != null;
            case 2:
            	return ModelPlayerBaseGC.currentGearData.getGear() > -1;
            case 3: // Left Green
                return ModelPlayerBaseGC.currentGearData.getLeftTank() == 0;
            case 4: // Right Green
                return ModelPlayerBaseGC.currentGearData.getRightTank() == 0;
            case 5: // Left Orange
                return ModelPlayerBaseGC.currentGearData.getLeftTank() == 1;
            case 6: // Right Orange
                return ModelPlayerBaseGC.currentGearData.getRightTank() == 1;
            case 7: // Left Red
                return ModelPlayerBaseGC.currentGearData.getLeftTank() == 2;
            case 8: // Right Red
                return ModelPlayerBaseGC.currentGearData.getRightTank() == 2;
            case 9:
                return ModelPlayerBaseGC.currentGearData.getFrequencyModule() > -1;
            }
        }

        return b;
    }

    private static RenderPlayer playerRenderer;
    private Method getEntityTextureMethod;

    @Override
    public void doRender(float f, boolean useParentTransformations)
    {
    	if (this.preRender(f))
        {
    		int saveTex = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
    		
    		switch (type)
            {
            case 0:
            	FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerGC.oxygenMaskTexture);
                break;
            case 1:
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerBaseGC.currentGearData.getParachute());
                break;
            case 9:
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerGC.frequencyModuleTexture);
                break;
            default:
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerGC.playerTexture);
                break;
            }

            if (type != 9)
            {
            	super.doRender(f, useParentTransformations);               	
            }
            else
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerGC.frequencyModuleTexture);
                GL11.glPushMatrix();
                GL11.glRotatef(180, 1, 0, 0);
                GL11.glScalef(0.3F, 0.3F, 0.3F);
                GL11.glTranslatef(-1.1F, 1.2F, 0);
                ModelPlayerBaseGC.frequencyModule.renderPart("Main");
                GL11.glTranslatef(0, 1.2F, 0);
                GL11.glRotatef((float) (Math.sin(ModelPlayerBaseGC.playerRendering.ticksExisted * 0.05) * 50.0F), 1, 0, 0);
                GL11.glRotatef((float) (Math.cos(ModelPlayerBaseGC.playerRendering.ticksExisted * 0.1) * 50.0F), 0, 1, 0);
                GL11.glTranslatef(0, -1.2F, 0);
                ModelPlayerBaseGC.frequencyModule.renderPart("Radar");
                GL11.glPopMatrix();
            }

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, saveTex);
        }
    }
}

