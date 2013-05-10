package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import com.overminddl1.mods.NMT.NMTGlobal;
import com.overminddl1.mods.NMT.NMTModelRenderer;

public class GCCoreModelOxygenBubble extends ModelBase
{
	public NMTModelRenderer bubble;
	public NMTModelRenderer bubble2;

	public GCCoreModelOxygenBubble()
	{
		this(0.0F);
	}

    public GCCoreModelOxygenBubble(float par1)
    {
    	this.textureWidth = 512 * 4;
		this.textureHeight = 512 * 32;
		this.bubble = new NMTModelRenderer(this, 40, 20);
		this.bubble.addSphere(0.0F, 0.0F, 0.0F, 1.0F, 12, 12, this.textureWidth, this.textureHeight, true, NMTGlobal.NMT_SPHERE_SINUSOIDAL);
		this.bubble.setRotationPoint(0F, 0F, 0F);
		this.bubble.setTextureSize(128, 64);
		this.bubble.mirror = true;
		this.setRotation(this.bubble, 0F, 0F, 0F);
		this.bubble2 = new NMTModelRenderer(this, 40, 20);
		this.bubble2.addSphere(0.0F, 0.0F, 0.0F, -1.0F, 12, 12, this.textureWidth, this.textureHeight, true, NMTGlobal.NMT_SPHERE_SINUSOIDAL);
		this.bubble2.setRotationPoint(0F, 0F, 0F);
		this.bubble2.setTextureSize(128, 64);
		this.setRotation(this.bubble2, 0F, 0F, 0F);
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
    	this.bubble2.render(par7);
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {

    }
}
