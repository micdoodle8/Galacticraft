package micdoodle8.mods.galacticraft.mars.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * GCMarsModelSpaceshipTier2.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
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
		this.textureWidth = 256;
		this.textureHeight = 256;

		this.inside[0] = new ModelRenderer(this, 0, 59);
		this.inside[0].addBox(-9F, -57F, -9F, 18, 1, 18, var1);
		this.inside[0].setRotationPoint(0F, 23F, 0F);
		this.inside[0].setTextureSize(256, 256);
		this.inside[0].mirror = true;
		this.setStartingAngles(this.inside[0], 0F, 0F, 0F);
		this.inside[1] = new ModelRenderer(this, 0, 78);
		this.inside[1].addBox(-8.5F, -16F, -8.5F, 17, 1, 17, var1);
		this.inside[1].setRotationPoint(0F, 23F, 0F);
		this.inside[1].setTextureSize(256, 256);
		this.inside[1].mirror = true;
		this.setStartingAngles(this.inside[1], 0F, 0F, 0F);
		this.inside[2] = new ModelRenderer(this, 0, 40);
		this.inside[2].addBox(-9F, -4F, -9F, 18, 1, 18, var1);
		this.inside[2].setRotationPoint(0F, 23F, 0F);
		this.inside[2].setTextureSize(256, 256);
		this.inside[2].mirror = true;
		this.setStartingAngles(this.inside[2], 0F, 0F, 0F);

		this.fins[0][1] = new ModelRenderer(this, 66, 0);
		this.fins[0][1].addBox(-1F, -9F, -19.4F, 2, 8, 2, var1);
		this.fins[0][1].setRotationPoint(0F, 24F, 0F);
		this.fins[0][1].setTextureSize(256, 256);
		this.fins[0][1].mirror = true;
		this.setStartingAngles(this.fins[0][1], 0F, 0.7853982F, 0F);
		this.fins[0][2] = new ModelRenderer(this, 66, 0);
		this.fins[0][2].addBox(-1F, -12F, -17.4F, 2, 8, 2, var1);
		this.fins[0][2].setRotationPoint(0F, 24F, 0F);
		this.fins[0][2].setTextureSize(256, 256);
		this.fins[0][2].mirror = true;
		this.setStartingAngles(this.fins[0][2], 0F, 0.7853982F, 0F);
		this.fins[0][3] = new ModelRenderer(this, 66, 0);
		this.fins[0][3].addBox(-1F, -14F, -15.4F, 2, 8, 2, var1);
		this.fins[0][3].setRotationPoint(0F, 24F, 0F);
		this.fins[0][3].setTextureSize(256, 256);
		this.fins[0][3].mirror = true;
		this.setStartingAngles(this.fins[0][3], 0F, 0.7853982F, 0F);
		this.fins[0][4] = new ModelRenderer(this, 66, 0);
		this.fins[0][4].addBox(-1F, -15F, -13.5F, 2, 8, 2, var1);
		this.fins[0][4].setRotationPoint(0F, 24F, 0F);
		this.fins[0][4].setTextureSize(256, 256);
		this.fins[0][4].mirror = true;
		this.setStartingAngles(this.fins[0][4], 0F, 0.7853982F, 0F);

		this.fins[0][0] = new ModelRenderer(this, 60, 0);
		this.fins[0][0].addBox(-1F, -14F, -20.4F, 2, 15, 1, var1);
		this.fins[0][0].setRotationPoint(0F, 24F, 0F);
		this.fins[0][0].setTextureSize(256, 256);
		this.fins[0][0].mirror = true;
		this.setStartingAngles(this.fins[0][0], 0F, 0.7853982F, 0F);

		this.fins[1][0] = new ModelRenderer(this, 74, 0);
		this.fins[1][0].addBox(-20.4F, -14F, -1F, 1, 15, 2, var1);
		this.fins[1][0].setRotationPoint(0F, 24F, 0F);
		this.fins[1][0].setTextureSize(256, 256);
		this.fins[1][0].mirror = true;
		this.setStartingAngles(this.fins[1][0], 0F, 0.7853982F, 0F);
		this.fins[1][1] = new ModelRenderer(this, 66, 0);
		this.fins[1][1].addBox(-19.4F, -9F, -1F, 2, 8, 2, var1);
		this.fins[1][1].setRotationPoint(0F, 24F, 0F);
		this.fins[1][1].setTextureSize(256, 256);
		this.fins[1][1].mirror = true;
		this.setStartingAngles(this.fins[1][1], 0F, 0.7853982F, 0F);
		this.fins[1][2] = new ModelRenderer(this, 66, 0);
		this.fins[1][2].addBox(-17.4F, -12F, -1F, 2, 8, 2, var1);
		this.fins[1][2].setRotationPoint(0F, 24F, 0F);
		this.fins[1][2].setTextureSize(256, 256);
		this.fins[1][2].mirror = true;
		this.setStartingAngles(this.fins[1][2], 0F, 0.7853982F, 0F);
		this.fins[1][3] = new ModelRenderer(this, 66, 0);
		this.fins[1][3].addBox(-15.4F, -14F, -1F, 2, 8, 2, var1);
		this.fins[1][3].setRotationPoint(0F, 24F, 0F);
		this.fins[1][3].setTextureSize(256, 256);
		this.fins[1][3].mirror = true;
		this.setStartingAngles(this.fins[1][3], 0F, 0.7853982F, 0F);
		this.fins[1][4] = new ModelRenderer(this, 66, 0);
		this.fins[1][4].addBox(-13.5F, -15F, -1F, 2, 8, 2, var1);
		this.fins[1][4].setRotationPoint(0F, 24F, 0F);
		this.fins[1][4].setTextureSize(256, 256);
		this.fins[1][4].mirror = true;
		this.setStartingAngles(this.fins[1][4], 0F, 0.7853982F, 0F);

		this.fins[2][0] = new ModelRenderer(this, 60, 0);
		this.fins[2][0].addBox(-1F, -14F, 19.5F, 2, 15, 1, var1);
		this.fins[2][0].setRotationPoint(0F, 24F, 0F);
		this.fins[2][0].setTextureSize(256, 256);
		this.fins[2][0].mirror = true;
		this.setStartingAngles(this.fins[2][0], 0F, 0.7853982F, 0F);
		this.fins[2][1] = new ModelRenderer(this, 66, 0);
		this.fins[2][1].addBox(-1F, -9F, 17.5F, 2, 8, 2, var1);
		this.fins[2][1].setRotationPoint(0F, 24F, 0F);
		this.fins[2][1].setTextureSize(256, 256);
		this.fins[2][1].mirror = true;
		this.setStartingAngles(this.fins[2][1], 0F, 0.7853982F, 0F);
		this.fins[2][2] = new ModelRenderer(this, 66, 0);
		this.fins[2][2].addBox(-1F, -12F, 15.5F, 2, 8, 2, var1);
		this.fins[2][2].setRotationPoint(0F, 24F, 0F);
		this.fins[2][2].setTextureSize(256, 256);
		this.fins[2][2].mirror = true;
		this.setStartingAngles(this.fins[2][2], 0F, 0.7853982F, 0F);
		this.fins[2][3] = new ModelRenderer(this, 66, 0);
		this.fins[2][3].addBox(-1F, -14F, 13.5F, 2, 8, 2, var1);
		this.fins[2][3].setRotationPoint(0F, 24F, 0F);
		this.fins[2][3].setTextureSize(256, 256);
		this.fins[2][3].mirror = true;
		this.setStartingAngles(this.fins[2][3], 0F, 0.7853982F, 0F);
		this.fins[2][4] = new ModelRenderer(this, 66, 0);
		this.fins[2][4].addBox(-1F, -15F, 11.6F, 2, 8, 2, var1);
		this.fins[2][4].setRotationPoint(0F, 24F, 0F);
		this.fins[2][4].setTextureSize(256, 256);
		this.fins[2][4].mirror = true;
		this.setStartingAngles(this.fins[2][4], 0F, 0.7853982F, 0F);

		this.fins[3][0] = new ModelRenderer(this, 74, 0);
		this.fins[3][0].addBox(19.5F, -14F, -1F, 1, 15, 2, var1);
		this.fins[3][0].setRotationPoint(0F, 24F, 0F);
		this.fins[3][0].setTextureSize(256, 256);
		this.fins[3][0].mirror = true;
		this.setStartingAngles(this.fins[3][0], 0F, 0.7853982F, 0F);
		this.fins[3][1] = new ModelRenderer(this, 66, 0);
		this.fins[3][1].addBox(17.5F, -9F, -1F, 2, 8, 2, var1);
		this.fins[3][1].setRotationPoint(0F, 24F, 0F);
		this.fins[3][1].setTextureSize(256, 256);
		this.fins[3][1].mirror = true;
		this.setStartingAngles(this.fins[3][1], 0F, 0.7853982F, 0F);
		this.fins[3][2] = new ModelRenderer(this, 66, 0);
		this.fins[3][2].addBox(15.5F, -12F, -1F, 2, 8, 2, var1);
		this.fins[3][2].setRotationPoint(0F, 24F, 0F);
		this.fins[3][2].setTextureSize(256, 256);
		this.fins[3][2].mirror = true;
		this.setStartingAngles(this.fins[3][2], 0F, 0.7853982F, 0F);
		this.fins[3][3] = new ModelRenderer(this, 66, 0);
		this.fins[3][3].addBox(13.5F, -14F, -1F, 2, 8, 2, var1);
		this.fins[3][3].setRotationPoint(0F, 24F, 0F);
		this.fins[3][3].setTextureSize(256, 256);
		this.fins[3][3].mirror = true;
		this.setStartingAngles(this.fins[3][3], 0F, 0.7853982F, 0F);
		this.fins[3][4] = new ModelRenderer(this, 66, 0);
		this.fins[3][4].addBox(11.6F, -15F, -1F, 2, 8, 2, var1);
		this.fins[3][4].setRotationPoint(0F, 24F, 0F);
		this.fins[3][4].setTextureSize(256, 256);
		this.fins[3][4].mirror = true;
		this.setStartingAngles(this.fins[3][4], 0F, 0.7853982F, 0F);

		this.top[0] = new ModelRenderer(this, 192, 60);
		this.top[0].addBox(-8F, -60F, -8F, 16, 2, 16, var1);
		this.top[0].setRotationPoint(0F, 24F, 0F);
		this.top[0].setTextureSize(256, 256);
		this.top[0].mirror = true;
		this.setStartingAngles(this.top[0], 0F, 0F, 0F);
		this.top[1] = new ModelRenderer(this, 200, 78);
		this.top[1].addBox(-7F, -62F, -7F, 14, 2, 14, var1);
		this.top[1].setRotationPoint(0F, 24F, 0F);
		this.top[1].setTextureSize(256, 256);
		this.top[1].mirror = true;
		this.setStartingAngles(this.top[1], 0F, 0F, 0F);
		this.top[2] = new ModelRenderer(this, 208, 94);
		this.top[2].addBox(-6F, -64F, -6F, 12, 2, 12, var1);
		this.top[2].setRotationPoint(0F, 24F, 0F);
		this.top[2].setTextureSize(256, 256);
		this.top[2].mirror = true;
		this.setStartingAngles(this.top[2], 0F, 0F, 0F);
		this.top[3] = new ModelRenderer(this, 216, 108);
		this.top[3].addBox(-5F, -66F, -5F, 10, 2, 10, var1);
		this.top[3].setRotationPoint(0F, 24F, 0F);
		this.top[3].setTextureSize(256, 256);
		this.top[3].mirror = true;
		this.setStartingAngles(this.top[3], 0F, 0F, 0F);
		this.top[4] = new ModelRenderer(this, 224, 120);
		this.top[4].addBox(-4F, -68F, -4F, 8, 2, 8, var1);
		this.top[4].setRotationPoint(0F, 24F, 0F);
		this.top[4].setTextureSize(256, 256);
		this.top[4].mirror = true;
		this.setStartingAngles(this.top[4], 0F, 0F, 0F);
		this.top[5] = new ModelRenderer(this, 232, 130);
		this.top[5].addBox(-3F, -70F, -3F, 6, 2, 6, var1);
		this.top[5].setRotationPoint(0F, 24F, 0F);
		this.top[5].setTextureSize(256, 256);
		this.top[5].mirror = true;
		this.setStartingAngles(this.top[5], 0F, 0F, 0F);
		this.top[6] = new ModelRenderer(this, 240, 138);
		this.top[6].addBox(-2F, -72F, -2F, 4, 2, 4, var1);
		this.top[6].setRotationPoint(0F, 24F, 0F);
		this.top[6].setTextureSize(256, 256);
		this.top[6].mirror = true;
		this.setStartingAngles(this.top[6], 0F, 0F, 0F);
		this.top[7] = new ModelRenderer(this, 248, 144);
		this.top[7].addBox(-1F, -88F, -1F, 2, 18, 2, var1);
		this.top[7].setRotationPoint(0F, 24F, 0F);
		this.top[7].setTextureSize(256, 256);
		this.top[7].mirror = true;
		this.setStartingAngles(this.top[7], 0F, 0F, 0F);

		this.base[0] = new ModelRenderer(this, 0, 0);
		this.base[0].addBox(-7F, -1F, -7F, 14, 1, 14, var1);
		this.base[0].setRotationPoint(0F, 24F, 0F);
		this.base[0].setTextureSize(256, 256);
		this.base[0].mirror = true;
		this.setStartingAngles(this.base[0], 0F, 0F, 0F);
		this.base[1] = new ModelRenderer(this, 0, 15);
		this.base[1].addBox(-6F, -2F, -6F, 12, 1, 12, var1);
		this.base[1].setRotationPoint(0F, 24F, 0F);
		this.base[1].setTextureSize(256, 256);
		this.base[1].mirror = true;
		this.setStartingAngles(this.base[1], 0F, 0F, 0F);
		this.base[2] = new ModelRenderer(this, 0, 28);
		this.base[2].addBox(-5F, -4F, -5F, 10, 2, 10, var1);
		this.base[2].setRotationPoint(0F, 24F, 0F);
		this.base[2].setTextureSize(256, 256);
		this.base[2].mirror = true;
		this.setStartingAngles(this.base[2], 0F, 0F, 0F);

		this.sides[0] = new ModelRenderer(this, 85, 0);
		this.sides[0].addBox(-3.9F, -58F, -8.9F, 8, 17, 1, var1);
		this.sides[0].setRotationPoint(0F, 24F, 0F);
		this.sides[0].setTextureSize(256, 256);
		this.sides[0].mirror = true;
		this.setStartingAngles(this.sides[0], 0F, 0F, 0F);
		this.sides[1] = new ModelRenderer(this, 103, 0);
		this.sides[1].addBox(3.9F, -58F, -8.9F, 5, 54, 1, var1);
		this.sides[1].setRotationPoint(0F, 24F, 0F);
		this.sides[1].setTextureSize(256, 256);
		this.sides[1].mirror = true;
		this.setStartingAngles(this.sides[1], 0F, 0F, 0F);
		this.sides[2] = new ModelRenderer(this, 85, 18);
		this.sides[2].addBox(-3.9F, -34F, -8.9F, 8, 30, 1, var1);
		this.sides[2].setRotationPoint(0F, 24F, 0F);
		this.sides[2].setTextureSize(256, 256);
		this.sides[2].mirror = true;
		this.setStartingAngles(this.sides[2], 0F, 0F, 0F);
		this.sides[3] = new ModelRenderer(this, 103, 55);
		this.sides[3].addBox(-8.9F, -58F, -8.9F, 5, 54, 1, var1);
		this.sides[3].setRotationPoint(0F, 24F, 0F);
		this.sides[3].setTextureSize(256, 256);
		this.sides[3].mirror = true;
		this.setStartingAngles(this.sides[3], 0F, 0F, 0F);

		this.sides[4] = new ModelRenderer(this, 120, 0);
		this.sides[4].addBox(-8.9F, -58F, -7.9F, 1, 54, 16, var1);
		this.sides[4].setRotationPoint(0F, 24F, 0F);
		this.sides[4].setTextureSize(256, 256);
		this.sides[4].mirror = true;
		this.setStartingAngles(this.sides[4], 0F, 0F, 0F);
		this.sides[5] = new ModelRenderer(this, 120, 141);
		this.sides[5].addBox(-8.9F, -58F, 8.1F, 17, 54, 1, var1);
		this.sides[5].setRotationPoint(0F, 24F, 0F);
		this.sides[5].setTextureSize(256, 256);
		this.sides[5].mirror = true;
		this.setStartingAngles(this.sides[5], 0F, 0F, 0F);
		this.sides[6] = new ModelRenderer(this, 119, 70);
		this.sides[6].addBox(8.1F, -58F, -7.9F, 1, 54, 17, var1);
		this.sides[6].setRotationPoint(0F, 24F, 0F);
		this.sides[6].setTextureSize(256, 256);
		this.sides[6].mirror = true;
		this.setStartingAngles(this.sides[6], 0F, 0F, 0F);

		this.boosters[0][0] = new ModelRenderer(this, 154, 19);
		this.boosters[0][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, var1);
		this.boosters[0][0].setRotationPoint(0F, 24F, 0F);
		this.boosters[0][0].setTextureSize(256, 256);
		this.boosters[0][0].mirror = true;
		this.setStartingAngles(this.boosters[0][0], 0F, -1.570796F, 0F);
		this.boosters[0][1] = new ModelRenderer(this, 154, 6);
		this.boosters[0][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, var1);
		this.boosters[0][1].setRotationPoint(0F, 24F, 0F);
		this.boosters[0][1].setTextureSize(256, 256);
		this.boosters[0][1].mirror = true;
		this.setStartingAngles(this.boosters[0][1], 0F, -1.570796F, 0F);
		this.boosters[0][2] = new ModelRenderer(this, 154, 0);
		this.boosters[0][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, var1);
		this.boosters[0][2].setRotationPoint(0F, 24F, 0F);
		this.boosters[0][2].setTextureSize(256, 256);
		this.boosters[0][2].mirror = true;
		this.setStartingAngles(this.boosters[0][2], 0F, -1.570796F, 0F);

		this.boosters[1][0] = new ModelRenderer(this, 154, 19);
		this.boosters[1][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, var1);
		this.boosters[1][0].setRotationPoint(0F, 24F, 0F);
		this.boosters[1][0].setTextureSize(256, 256);
		this.boosters[1][0].mirror = true;
		this.setStartingAngles(this.boosters[1][0], 0F, 0F, 0F);
		this.boosters[1][1] = new ModelRenderer(this, 154, 6);
		this.boosters[1][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, var1);
		this.boosters[1][1].setRotationPoint(0F, 24F, 0F);
		this.boosters[1][1].setTextureSize(256, 256);
		this.boosters[1][1].mirror = true;
		this.setStartingAngles(this.boosters[1][1], 0F, 0F, 0F);
		this.boosters[1][2] = new ModelRenderer(this, 154, 0);
		this.boosters[1][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, var1);
		this.boosters[1][2].setRotationPoint(0F, 24F, 0F);
		this.boosters[1][2].setTextureSize(256, 256);
		this.boosters[1][2].mirror = true;
		this.setStartingAngles(this.boosters[1][2], 0F, 0F, 0F);

		this.boosters[2][0] = new ModelRenderer(this, 154, 19);
		this.boosters[2][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, var1);
		this.boosters[2][0].setRotationPoint(0F, 24F, 0F);
		this.boosters[2][0].setTextureSize(256, 256);
		this.boosters[2][0].mirror = true;
		this.setStartingAngles(this.boosters[2][0], 0F, 1.570796F, 0F);
		this.boosters[2][1] = new ModelRenderer(this, 154, 6);
		this.boosters[2][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, var1);
		this.boosters[2][1].setRotationPoint(0F, 24F, 0F);
		this.boosters[2][1].setTextureSize(256, 256);
		this.boosters[2][1].mirror = true;
		this.setStartingAngles(this.boosters[2][1], 0F, 1.570796F, 0F);
		this.boosters[2][2] = new ModelRenderer(this, 154, 0);
		this.boosters[2][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, var1);
		this.boosters[2][2].setRotationPoint(0F, 24F, 0F);
		this.boosters[2][2].setTextureSize(256, 256);
		this.boosters[2][2].mirror = true;
		this.setStartingAngles(this.boosters[2][2], 0F, 1.570796F, 0F);

		this.boosters[3][0] = new ModelRenderer(this, 154, 19);
		this.boosters[3][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, var1);
		this.boosters[3][0].setRotationPoint(0F, 24F, 0F);
		this.boosters[3][0].setTextureSize(256, 256);
		this.boosters[3][0].mirror = true;
		this.setStartingAngles(this.boosters[3][0], 0F, 3.141593F, 0F);
		this.boosters[3][1] = new ModelRenderer(this, 154, 6);
		this.boosters[3][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, var1);
		this.boosters[3][1].setRotationPoint(0F, 24F, 0F);
		this.boosters[3][1].setTextureSize(256, 256);
		this.boosters[3][1].mirror = true;
		this.setStartingAngles(this.boosters[3][1], 0F, 3.141593F, 0F);
		this.boosters[3][2] = new ModelRenderer(this, 154, 0);
		this.boosters[3][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, var1);
		this.boosters[3][2].setRotationPoint(0F, 24F, 0F);
		this.boosters[3][2].setTextureSize(256, 256);
		this.boosters[3][2].mirror = true;
		this.setStartingAngles(this.boosters[3][2], 0F, 3.141593F, 0F);
	}

	@Override
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		for (final ModelRenderer model : this.inside)
		{
			model.render(par7);
		}

		for (final ModelRenderer model : this.top)
		{
			model.render(par7);
		}

		for (final ModelRenderer model : this.base)
		{
			model.render(par7);
		}

		for (final ModelRenderer model : this.sides)
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
