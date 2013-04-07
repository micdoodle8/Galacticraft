package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.client.FMLClientHandler;

public class GCCoreModelLanderChest extends ModelChest
{
	public GCCoreModelLanderChest()
	{
		this(0.0F);
	}

	public GCCoreModelLanderChest(float par1)
	{
		super();
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		
		FMLClientHandler.instance().getClient().renderEngine.bindTexture("/micdoodle8/mods/galacticraft/core/client/entities/parachute/gray.png");

    	this.chestLid.rotationPointX = 2.0F;
        this.chestLid.rotationPointY = 7.0F;
        this.chestLid.rotationPointZ = -6.0F;
    	this.chestKnob.rotationPointX = 9.0F;
        this.chestKnob.rotationPointY = 7.0F;
        this.chestKnob.rotationPointZ = -6.0F;
        this.chestBelow.rotationPointX = 2.0F;
        this.chestBelow.rotationPointY = 8.0F;
        this.chestBelow.rotationPointZ = 8.0F;

    	this.chestLid.rotateAngleX = (float) Math.PI;
    	this.chestBelow.rotateAngleX = (float) Math.PI;
    	this.chestKnob.rotateAngleX = (float) Math.PI;
        this.chestLid.render(f5);
        this.chestKnob.render(f5);
        this.chestBelow.render(f5);
    }
    
	@Override
    public void renderAll()
    {
    }
}
