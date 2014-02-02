package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

/**
 * GCCoreModelOxygenBubble.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreModelOxygenBubble extends ModelBase
{
	IModelCustom sphere;

	public GCCoreModelOxygenBubble()
	{
		this(0.0F);
	}

	public GCCoreModelOxygenBubble(float par1)
	{
		this.sphere = AdvancedModelLoader.loadModel("/assets/galacticraftcore/models/sphere.obj");
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
		this.sphere.renderAll();
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
	{
		;
	}
}
