package micdoodle8.mods.galacticraft.mars.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GCMarsModelSpaceshipTier2 extends ModelBase
{
	// Roof, Floor, Bottom
	ModelRenderer[] inside = new ModelRenderer[3];
	// 4 sets of 5 fin parts
	ModelRenderer[][] fins = new ModelRenderer[4][5];
	// 8 parts to the top of the rocket, last is the long center piece
	ModelRenderer[] top = new ModelRenderer[8];
	// 4 sets of 3 booster parts (attachment, main, top)
	ModelRenderer[][] boosters = new ModelRenderer[4][3];
	// 3 booster base parts, starting from bottom
	ModelRenderer[] base = new ModelRenderer[3];
	// 7 side pars, first four are the front, then right, and so on
	ModelRenderer[] sides = new ModelRenderer[7];
	
	public GCMarsModelSpaceshipTier2()
	{
		this(0.0F);
	}
	
	public GCMarsModelSpaceshipTier2(float var1)
	{
		textureWidth = 256;
		textureHeight = 256;
		
		inside[0] = new ModelRenderer(this, 0, 59);
		inside[0].addBox(-9F, -57F, -9F, 18, 1, 18, var1);
		inside[0].setRotationPoint(0F, 23F, 0F);
		inside[0].setTextureSize(256, 256);
		inside[0].mirror = true;
		setStartingAngles(inside[0], 0F, 0F, 0F);
		inside[1] = new ModelRenderer(this, 0, 78);
		inside[1].addBox(-8.5F, -16F, -8.5F, 17, 1, 17, var1);
		inside[1].setRotationPoint(0F, 23F, 0F);
		inside[1].setTextureSize(256, 256);
		inside[1].mirror = true;
		setStartingAngles(inside[1], 0F, 0F, 0F);
		inside[2] = new ModelRenderer(this, 0, 40);
		inside[2].addBox(-9F, -4F, -9F, 18, 1, 18, var1);
		inside[2].setRotationPoint(0F, 23F, 0F);
		inside[2].setTextureSize(256, 256);
		inside[2].mirror = true;
		setStartingAngles(inside[2], 0F, 0F, 0F);

		fins[0][1] = new ModelRenderer(this, 66, 0);
		fins[0][1].addBox(-1F, -9F, -19.4F, 2, 8, 2, var1);
		fins[0][1].setRotationPoint(0F, 24F, 0F);
		fins[0][1].setTextureSize(256, 256);
		fins[0][1].mirror = true;
		setStartingAngles(fins[0][1], 0F, 0.7853982F, 0F);
		fins[0][2] = new ModelRenderer(this, 66, 0);
		fins[0][2].addBox(-1F, -12F, -17.4F, 2, 8, 2, var1);
		fins[0][2].setRotationPoint(0F, 24F, 0F);
		fins[0][2].setTextureSize(256, 256);
		fins[0][2].mirror = true;
		setStartingAngles(fins[0][2], 0F, 0.7853982F, 0F);
		fins[0][3] = new ModelRenderer(this, 66, 0);
		fins[0][3].addBox(-1F, -14F, -15.4F, 2, 8, 2, var1);
		fins[0][3].setRotationPoint(0F, 24F, 0F);
		fins[0][3].setTextureSize(256, 256);
		fins[0][3].mirror = true;
		setStartingAngles(fins[0][3], 0F, 0.7853982F, 0F);
		fins[0][4] = new ModelRenderer(this, 66, 0);
		fins[0][4].addBox(-1F, -15F, -13.5F, 2, 8, 2, var1);
		fins[0][4].setRotationPoint(0F, 24F, 0F);
		fins[0][4].setTextureSize(256, 256);
		fins[0][4].mirror = true;
		setStartingAngles(fins[0][4], 0F, 0.7853982F, 0F);
		
		fins[0][0] = new ModelRenderer(this, 60, 0);
		fins[0][0].addBox(-1F, -14F, -20.4F, 2, 15, 1, var1);
		fins[0][0].setRotationPoint(0F, 24F, 0F);
		fins[0][0].setTextureSize(256, 256);
		fins[0][0].mirror = true;
		setStartingAngles(fins[0][0], 0F, 0.7853982F, 0F);

		fins[1][0] = new ModelRenderer(this, 74, 0);
		fins[1][0].addBox(-20.4F, -14F, -1F, 1, 15, 2, var1);
		fins[1][0].setRotationPoint(0F, 24F, 0F);
		fins[1][0].setTextureSize(256, 256);
		fins[1][0].mirror = true;
		setStartingAngles(fins[1][0], 0F, 0.7853982F, 0F);
		fins[1][1] = new ModelRenderer(this, 66, 0);
		fins[1][1].addBox(-19.4F, -9F, -1F, 2, 8, 2, var1);
		fins[1][1].setRotationPoint(0F, 24F, 0F);
		fins[1][1].setTextureSize(256, 256);
		fins[1][1].mirror = true;
		setStartingAngles(fins[1][1], 0F, 0.7853982F, 0F);
		fins[1][2] = new ModelRenderer(this, 66, 0);
		fins[1][2].addBox(-17.4F, -12F, -1F, 2, 8, 2, var1);
		fins[1][2].setRotationPoint(0F, 24F, 0F);
		fins[1][2].setTextureSize(256, 256);
		fins[1][2].mirror = true;
		setStartingAngles(fins[1][2], 0F, 0.7853982F, 0F);
		fins[1][3] = new ModelRenderer(this, 66, 0);
		fins[1][3].addBox(-15.4F, -14F, -1F, 2, 8, 2, var1);
		fins[1][3].setRotationPoint(0F, 24F, 0F);
		fins[1][3].setTextureSize(256, 256);
		fins[1][3].mirror = true;
		setStartingAngles(fins[1][3], 0F, 0.7853982F, 0F);
		fins[1][4] = new ModelRenderer(this, 66, 0);
		fins[1][4].addBox(-13.5F, -15F, -1F, 2, 8, 2, var1);
		fins[1][4].setRotationPoint(0F, 24F, 0F);
		fins[1][4].setTextureSize(256, 256);
		fins[1][4].mirror = true;
		setStartingAngles(fins[1][4], 0F, 0.7853982F, 0F);

		fins[2][0] = new ModelRenderer(this, 60, 0);
		fins[2][0].addBox(-1F, -14F, 19.5F, 2, 15, 1, var1);
		fins[2][0].setRotationPoint(0F, 24F, 0F);
		fins[2][0].setTextureSize(256, 256);
		fins[2][0].mirror = true;
		setStartingAngles(fins[2][0], 0F, 0.7853982F, 0F);
		fins[2][1] = new ModelRenderer(this, 66, 0);
		fins[2][1].addBox(-1F, -9F, 17.5F, 2, 8, 2, var1);
		fins[2][1].setRotationPoint(0F, 24F, 0F);
		fins[2][1].setTextureSize(256, 256);
		fins[2][1].mirror = true;
		setStartingAngles(fins[2][1], 0F, 0.7853982F, 0F);
		fins[2][2] = new ModelRenderer(this, 66, 0);
		fins[2][2].addBox(-1F, -12F, 15.5F, 2, 8, 2, var1);
		fins[2][2].setRotationPoint(0F, 24F, 0F);
		fins[2][2].setTextureSize(256, 256);
		fins[2][2].mirror = true;
		setStartingAngles(fins[2][2], 0F, 0.7853982F, 0F);
		fins[2][3] = new ModelRenderer(this, 66, 0);
		fins[2][3].addBox(-1F, -14F, 13.5F, 2, 8, 2, var1);
		fins[2][3].setRotationPoint(0F, 24F, 0F);
		fins[2][3].setTextureSize(256, 256);
		fins[2][3].mirror = true;
		setStartingAngles(fins[2][3], 0F, 0.7853982F, 0F);
		fins[2][4] = new ModelRenderer(this, 66, 0);
		fins[2][4].addBox(-1F, -15F, 11.6F, 2, 8, 2, var1);
		fins[2][4].setRotationPoint(0F, 24F, 0F);
		fins[2][4].setTextureSize(256, 256);
		fins[2][4].mirror = true;
		setStartingAngles(fins[2][4], 0F, 0.7853982F, 0F);

		fins[3][0] = new ModelRenderer(this, 74, 0);
		fins[3][0].addBox(19.5F, -14F, -1F, 1, 15, 2, var1);
		fins[3][0].setRotationPoint(0F, 24F, 0F);
		fins[3][0].setTextureSize(256, 256);
		fins[3][0].mirror = true;
		setStartingAngles(fins[3][0], 0F, 0.7853982F, 0F);
		fins[3][1] = new ModelRenderer(this, 66, 0);
		fins[3][1].addBox(17.5F, -9F, -1F, 2, 8, 2, var1);
		fins[3][1].setRotationPoint(0F, 24F, 0F);
		fins[3][1].setTextureSize(256, 256);
		fins[3][1].mirror = true;
		setStartingAngles(fins[3][1], 0F, 0.7853982F, 0F);
		fins[3][2] = new ModelRenderer(this, 66, 0);
		fins[3][2].addBox(15.5F, -12F, -1F, 2, 8, 2, var1);
		fins[3][2].setRotationPoint(0F, 24F, 0F);
		fins[3][2].setTextureSize(256, 256);
		fins[3][2].mirror = true;
		setStartingAngles(fins[3][2], 0F, 0.7853982F, 0F);
		fins[3][3] = new ModelRenderer(this, 66, 0);
		fins[3][3].addBox(13.5F, -14F, -1F, 2, 8, 2, var1);
		fins[3][3].setRotationPoint(0F, 24F, 0F);
		fins[3][3].setTextureSize(256, 256);
		fins[3][3].mirror = true;
		setStartingAngles(fins[3][3], 0F, 0.7853982F, 0F);
		fins[3][4] = new ModelRenderer(this, 66, 0);
		fins[3][4].addBox(11.6F, -15F, -1F, 2, 8, 2, var1);
		fins[3][4].setRotationPoint(0F, 24F, 0F);
		fins[3][4].setTextureSize(256, 256);
		fins[3][4].mirror = true;
		setStartingAngles(fins[3][4], 0F, 0.7853982F, 0F);
		
		top[0] = new ModelRenderer(this, 192, 60);
		top[0].addBox(-8F, -60F, -8F, 16, 2, 16, var1);
		top[0].setRotationPoint(0F, 24F, 0F);
		top[0].setTextureSize(256, 256);
		top[0].mirror = true;
		setStartingAngles(top[0], 0F, 0F, 0F);
		top[1] = new ModelRenderer(this, 200, 78);
		top[1].addBox(-7F, -62F, -7F, 14, 2, 14, var1);
		top[1].setRotationPoint(0F, 24F, 0F);
		top[1].setTextureSize(256, 256);
		top[1].mirror = true;
		setStartingAngles(top[1], 0F, 0F, 0F);
		top[2] = new ModelRenderer(this, 208, 94);
		top[2].addBox(-6F, -64F, -6F, 12, 2, 12, var1);
		top[2].setRotationPoint(0F, 24F, 0F);
		top[2].setTextureSize(256, 256);
		top[2].mirror = true;
		setStartingAngles(top[2], 0F, 0F, 0F);
		top[3] = new ModelRenderer(this, 216, 108);
		top[3].addBox(-5F, -66F, -5F, 10, 2, 10, var1);
		top[3].setRotationPoint(0F, 24F, 0F);
		top[3].setTextureSize(256, 256);
		top[3].mirror = true;
		setStartingAngles(top[3], 0F, 0F, 0F);
		top[4] = new ModelRenderer(this, 224, 120);
		top[4].addBox(-4F, -68F, -4F, 8, 2, 8, var1);
		top[4].setRotationPoint(0F, 24F, 0F);
		top[4].setTextureSize(256, 256);
		top[4].mirror = true;
		setStartingAngles(top[4], 0F, 0F, 0F);
		top[5] = new ModelRenderer(this, 232, 130);
		top[5].addBox(-3F, -70F, -3F, 6, 2, 6, var1);
		top[5].setRotationPoint(0F, 24F, 0F);
		top[5].setTextureSize(256, 256);
		top[5].mirror = true;
		setStartingAngles(top[5], 0F, 0F, 0F);
		top[6] = new ModelRenderer(this, 240, 138);
		top[6].addBox(-2F, -72F, -2F, 4, 2, 4, var1);
		top[6].setRotationPoint(0F, 24F, 0F);
		top[6].setTextureSize(256, 256);
		top[6].mirror = true;
		setStartingAngles(top[6], 0F, 0F, 0F);
		top[7] = new ModelRenderer(this, 248, 144);
		top[7].addBox(-1F, -88F, -1F, 2, 18, 2, var1);
		top[7].setRotationPoint(0F, 24F, 0F);
		top[7].setTextureSize(256, 256);
		top[7].mirror = true;
		setStartingAngles(top[7], 0F, 0F, 0F);
		
		base[0] = new ModelRenderer(this, 0, 0);
		base[0].addBox(-7F, -1F, -7F, 14, 1, 14, var1);
		base[0].setRotationPoint(0F, 24F, 0F);
		base[0].setTextureSize(256, 256);
		base[0].mirror = true;
		setStartingAngles(base[0], 0F, 0F, 0F);
		base[1] = new ModelRenderer(this, 0, 15);
		base[1].addBox(-6F, -2F, -6F, 12, 1, 12, var1);
		base[1].setRotationPoint(0F, 24F, 0F);
		base[1].setTextureSize(256, 256);
		base[1].mirror = true;
		setStartingAngles(base[1], 0F, 0F, 0F);
		base[2] = new ModelRenderer(this, 0, 28);
		base[2].addBox(-5F, -4F, -5F, 10, 2, 10, var1);
		base[2].setRotationPoint(0F, 24F, 0F);
		base[2].setTextureSize(256, 256);
		base[2].mirror = true;
		setStartingAngles(base[2], 0F, 0F, 0F);
		
		sides[0] = new ModelRenderer(this, 85, 0);
		sides[0].addBox(-3.9F, -58F, -8.9F, 8, 17, 1, var1);
		sides[0].setRotationPoint(0F, 24F, 0F);
		sides[0].setTextureSize(256, 256);
		sides[0].mirror = true;
		setStartingAngles(sides[0], 0F, 0F, 0F);
		sides[1] = new ModelRenderer(this, 103, 0);
		sides[1].addBox(3.9F, -58F, -8.9F, 5, 54, 1, var1);
		sides[1].setRotationPoint(0F, 24F, 0F);
		sides[1].setTextureSize(256, 256);
		sides[1].mirror = true;
		setStartingAngles(sides[1], 0F, 0F, 0F);
		sides[2] = new ModelRenderer(this, 85, 18);
		sides[2].addBox(-3.9F, -34F, -8.9F, 8, 30, 1, var1);
		sides[2].setRotationPoint(0F, 24F, 0F);
		sides[2].setTextureSize(256, 256);
		sides[2].mirror = true;
		setStartingAngles(sides[2], 0F, 0F, 0F);
		sides[3] = new ModelRenderer(this, 103, 55);
		sides[3].addBox(-8.9F, -58F, -8.9F, 5, 54, 1, var1);
		sides[3].setRotationPoint(0F, 24F, 0F);
		sides[3].setTextureSize(256, 256);
		sides[3].mirror = true;
		setStartingAngles(sides[3], 0F, 0F, 0F);

		sides[4] = new ModelRenderer(this, 120, 0);
		sides[4].addBox(-8.9F, -58F, -7.9F, 1, 54, 16, var1);
		sides[4].setRotationPoint(0F, 24F, 0F);
		sides[4].setTextureSize(256, 256);
		sides[4].mirror = true;
		setStartingAngles(sides[4], 0F, 0F, 0F);
		sides[5] = new ModelRenderer(this, 120, 141);
		sides[5].addBox(-8.9F, -58F, 8.1F, 17, 54, 1, var1);
		sides[5].setRotationPoint(0F, 24F, 0F);
		sides[5].setTextureSize(256, 256);
		sides[5].mirror = true;
		setStartingAngles(sides[5], 0F, 0F, 0F);
		sides[6] = new ModelRenderer(this, 119, 70);
		sides[6].addBox(8.1F, -58F, -7.9F, 1, 54, 17, var1);
		sides[6].setRotationPoint(0F, 24F, 0F);
		sides[6].setTextureSize(256, 256);
		sides[6].mirror = true;
		setStartingAngles(sides[6], 0F, 0F, 0F);
		
		boosters[0][0] = new ModelRenderer(this, 154, 19);
		boosters[0][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, var1);
		boosters[0][0].setRotationPoint(0F, 24F, 0F);
		boosters[0][0].setTextureSize(256, 256);
		boosters[0][0].mirror = true;
		setStartingAngles(boosters[0][0], 0F, -1.570796F, 0F);
		boosters[0][1] = new ModelRenderer(this, 154, 6);
		boosters[0][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, var1);
		boosters[0][1].setRotationPoint(0F, 24F, 0F);
		boosters[0][1].setTextureSize(256, 256);
		boosters[0][1].mirror = true;
		setStartingAngles(boosters[0][1], 0F, -1.570796F, 0F);
		boosters[0][2] = new ModelRenderer(this, 154, 0);
		boosters[0][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, var1);
		boosters[0][2].setRotationPoint(0F, 24F, 0F);
		boosters[0][2].setTextureSize(256, 256);
		boosters[0][2].mirror = true;
		setStartingAngles(boosters[0][2], 0F, -1.570796F, 0F);
		
		boosters[1][0] = new ModelRenderer(this, 154, 19);
		boosters[1][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, var1);
		boosters[1][0].setRotationPoint(0F, 24F, 0F);
		boosters[1][0].setTextureSize(256, 256);
		boosters[1][0].mirror = true;
		setStartingAngles(boosters[1][0], 0F, 0F, 0F);
		boosters[1][1] = new ModelRenderer(this, 154, 6);
		boosters[1][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, var1);
		boosters[1][1].setRotationPoint(0F, 24F, 0F);
		boosters[1][1].setTextureSize(256, 256);
		boosters[1][1].mirror = true;
		setStartingAngles(boosters[1][1], 0F, 0F, 0F);
		boosters[1][2] = new ModelRenderer(this, 154, 0);
		boosters[1][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, var1);
		boosters[1][2].setRotationPoint(0F, 24F, 0F);
		boosters[1][2].setTextureSize(256, 256);
		boosters[1][2].mirror = true;
		setStartingAngles(boosters[1][2], 0F, 0F, 0F);
		
		boosters[2][0] = new ModelRenderer(this, 154, 19);
		boosters[2][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, var1);
		boosters[2][0].setRotationPoint(0F, 24F, 0F);
		boosters[2][0].setTextureSize(256, 256);
		boosters[2][0].mirror = true;
		setStartingAngles(boosters[2][0], 0F, 1.570796F, 0F);
		boosters[2][1] = new ModelRenderer(this, 154, 6);
		boosters[2][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, var1);
		boosters[2][1].setRotationPoint(0F, 24F, 0F);
		boosters[2][1].setTextureSize(256, 256);
		boosters[2][1].mirror = true;
		setStartingAngles(boosters[2][1], 0F, 1.570796F, 0F);
		boosters[2][2] = new ModelRenderer(this, 154, 0);
		boosters[2][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, var1);
		boosters[2][2].setRotationPoint(0F, 24F, 0F);
		boosters[2][2].setTextureSize(256, 256);
		boosters[2][2].mirror = true;
		setStartingAngles(boosters[2][2], 0F, 1.570796F, 0F);
		
		boosters[3][0] = new ModelRenderer(this, 154, 19);
		boosters[3][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, var1);
		boosters[3][0].setRotationPoint(0F, 24F, 0F);
		boosters[3][0].setTextureSize(256, 256);
		boosters[3][0].mirror = true;
		setStartingAngles(boosters[3][0], 0F, 3.141593F, 0F);
		boosters[3][1] = new ModelRenderer(this, 154, 6);
		boosters[3][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, var1);
		boosters[3][1].setRotationPoint(0F, 24F, 0F);
		boosters[3][1].setTextureSize(256, 256);
		boosters[3][1].mirror = true;
		setStartingAngles(boosters[3][1], 0F, 3.141593F, 0F);
		boosters[3][2] = new ModelRenderer(this, 154, 0);
		boosters[3][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, var1);
		boosters[3][2].setRotationPoint(0F, 24F, 0F);
		boosters[3][2].setTextureSize(256, 256);
		boosters[3][2].mirror = true;
		setStartingAngles(boosters[3][2], 0F, 3.141593F, 0F);
	}
	
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		for (ModelRenderer model : this.inside)
		{
			model.render(par7);
		}

		for (ModelRenderer model : this.top)
		{
			model.render(par7);
		}
		
		for (ModelRenderer model : this.base)
		{
			model.render(par7);
		}

		for (ModelRenderer model : this.sides)
		{
			model.render(par7);
		}
		
		int var1 = 0;
		int var2 = 0;
		
		for (var1 = 0; var1 < this.fins.length; var1++)
		{
			for (var2 = 0; var2 < this.fins[var1].length; var2++)
			{
				this.fins[var1][var2].render(par7);
			}
		}
		
		for (var1 = 0; var1 < this.boosters.length; var1++)
		{
			for (var2 = 0; var2 < this.boosters[var1].length; var2++)
			{
				this.boosters[var1][var2].render(par7);
			}
		}
	}

	private void setStartingAngles(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity)
	{
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
	}
}
