package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import com.overminddl1.mods.NMT.NMTGlobal;
import com.overminddl1.mods.NMT.NMTModelRenderer;

public class GCCoreModelCelestialObject extends ModelBase
{
	public NMTModelRenderer bubble;

	public GCCoreModelCelestialObject()
	{
		this(0.0F);
	}

    public GCCoreModelCelestialObject(float par1)
    {
    	textureWidth = 128;
    	textureHeight = 128;

    	bubble = new NMTModelRenderer(this, 0, 0);
    	bubble.addBox(-10F, -10F, -10F, 20, 20, 20);
    	bubble.setRotationPoint(0F, 0F, 0F);
    	bubble.setTextureSize(128, 128);
    	bubble.mirror = true;
    	setRotation(bubble, 0F, (float) Math.PI, 0F);
    }

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

    @Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
    	this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
    	this.bubble.render(par7);
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
    	this.bubble.rotateAngleZ = (float) Math.PI;
    }
}
