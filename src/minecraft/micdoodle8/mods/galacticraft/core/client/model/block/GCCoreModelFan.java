package micdoodle8.mods.galacticraft.core.client.model.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class GCCoreModelFan extends ModelBase
{
	public ModelRenderer[][] fans = new ModelRenderer[4][3];
	public ModelRenderer center;

	public GCCoreModelFan()
	{
		this(0.0F);
	}
	
	public GCCoreModelFan(float par1)
	{
		this.center = new ModelRenderer(this, 0, 0);
		this.center.addBox(-1.5F, -1F, -1.5F, 3, 2, 3);
		this.center.setRotationPoint(0F, 0F, 0F);
		this.center.setTextureSize(64, 32);
		this.center.mirror = true;
		this.setRotation(this.center, 0F, 0F, 0F);

		this.fans[0][0] = new ModelRenderer(this, 14, 3);
		this.fans[0][0].addBox(-1.5F, -0.5F, -6.5F, 2, 1, 5);
		this.fans[0][0].setRotationPoint(0F, 0F, 0F);
		this.fans[0][0].setTextureSize(64, 32);
		this.fans[0][0].mirror = true;
		this.setRotation(this.fans[0][0], 0F, 0F, 0F);
		this.fans[0][1] = new ModelRenderer(this, 28, 2);
		this.fans[0][1].addBox(0.5F, -0.5F, -6.5F, 1, 1, 4);
		this.fans[0][1].setRotationPoint(0F, 0F, 0F);
		this.fans[0][1].setTextureSize(64, 32);
		this.fans[0][1].mirror = true;
		this.setRotation(this.fans[0][1], 0F, 0F, 0F);
		this.fans[0][2] = new ModelRenderer(this, 38, 2);
		this.fans[0][2].addBox(1.5F, -0.5F, -6.5F, 1, 1, 2);
		this.fans[0][2].setRotationPoint(0F, 0F, 0F);
		this.fans[0][2].setTextureSize(64, 32);
		this.fans[0][2].mirror = true;
		this.setRotation(this.fans[0][2], 0F, 0F, 0F);

		this.fans[1][0] = new ModelRenderer(this, 14, 0);
		this.fans[1][0].addBox(1.5F, -0.5F, -1.5F, 5, 1, 2);
		this.fans[1][0].setRotationPoint(0F, 0F, 0F);
		this.fans[1][0].setTextureSize(64, 32);
		this.fans[1][0].mirror = true;
		this.setRotation(this.fans[1][0], 0F, 0F, 0F);
		this.fans[1][1] = new ModelRenderer(this, 28, 0);
		this.fans[1][1].addBox(2.5F, -0.5F, 0.5F, 4, 1, 1);
		this.fans[1][1].setRotationPoint(0F, 0F, 0F);
		this.fans[1][1].setTextureSize(64, 32);
		this.fans[1][1].mirror = true;
		this.setRotation(this.fans[1][1], 0F, 0F, 0F);
		this.fans[1][2] = new ModelRenderer(this, 38, 0);
		this.fans[1][2].addBox(4.5F, -0.5F, 1.5F, 2, 1, 1);
		this.fans[1][2].setRotationPoint(0F, 0F, 0F);
		this.fans[1][2].setTextureSize(64, 32);
		this.fans[1][2].mirror = true;
		this.setRotation(this.fans[1][2], 0F, 0F, 0F);

		this.fans[2][0] = new ModelRenderer(this, 14, 0);
		this.fans[2][0].addBox(-6.5F, -0.5F, -0.5F, 5, 1, 2);
		this.fans[2][0].setRotationPoint(0F, 0F, 0F);
		this.fans[2][0].setTextureSize(64, 32);
		this.fans[2][0].mirror = true;
		this.setRotation(this.fans[2][0], 0F, 0F, 0F);
		this.fans[2][1] = new ModelRenderer(this, 28, 0);
		this.fans[2][1].addBox(-6.5F, -0.5F, -1.5F, 4, 1, 1);
		this.fans[2][1].setRotationPoint(0F, 0F, 0F);
		this.fans[2][1].setTextureSize(64, 32);
		this.fans[2][1].mirror = true;
		this.setRotation(this.fans[2][1], 0F, 0F, 0F);
		this.fans[2][2] = new ModelRenderer(this, 38, 0);
		this.fans[2][2].addBox(-6.5F, -0.5F, -2.5F, 2, 1, 1);
		this.fans[2][2].setRotationPoint(0F, 0F, 0F);
		this.fans[2][2].setTextureSize(64, 32);
		this.fans[2][2].mirror = true;
		this.setRotation(this.fans[2][2], 0F, 0F, 0F);

		this.fans[3][0] = new ModelRenderer(this, 14, 3);
		this.fans[3][0].addBox(-0.5F, -0.5F, 1.5F, 2, 1, 5);
		this.fans[3][0].setRotationPoint(0F, 0F, 0F);
		this.fans[3][0].setTextureSize(64, 32);
		this.fans[3][0].mirror = true;
		this.setRotation(this.fans[3][0], 0F, 0F, 0F);
		this.fans[3][1] = new ModelRenderer(this, 28, 2);
		this.fans[3][1].addBox(-1.5F, -0.5F, 2.5F, 1, 1, 4);
		this.fans[3][1].setRotationPoint(0F, 0F, 0F);
		this.fans[3][1].setTextureSize(64, 32);
		this.fans[3][1].mirror = true;
		this.setRotation(this.fans[3][1], 0F, 0F, 0F);
		this.fans[3][2] = new ModelRenderer(this, 38, 2);
		this.fans[3][2].addBox(-2.5F, -0.5F, 4.5F, 1, 1, 2);
		this.fans[3][2].setRotationPoint(0F, 0F, 0F);
		this.fans[3][2].setTextureSize(64, 32);
		this.fans[3][2].mirror = true;
		this.setRotation(this.fans[3][2], 0F, 0F, 0F);
	}
	  
	public void renderAll()
	{
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				this.fans[i][j].render(0.0625F);
			}
		}
		
		this.center.render(0.0625F);
	}
	  
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
