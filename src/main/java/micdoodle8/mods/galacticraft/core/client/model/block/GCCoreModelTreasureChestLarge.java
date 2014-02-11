package micdoodle8.mods.galacticraft.core.client.model.block;

import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.model.ModelRenderer;

/**
 * GCCoreModelTreasureChestLarge.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreModelTreasureChestLarge extends ModelLargeChest
{
	public ModelRenderer keyParts[] = new ModelRenderer[6];

	public GCCoreModelTreasureChestLarge()
	{
		super();
		this.textureWidth = 128;
		this.textureHeight = 64;
		this.keyParts[4] = new ModelRenderer(this, 50, 43);
		this.keyParts[4].addBox(7F, 2F, -0.5F, 3, 1, 1);
		this.keyParts[4].setRotationPoint(0F, 0F, 0F);
		this.keyParts[4].setTextureSize(128, 64);
		this.keyParts[4].mirror = true;
		this.keyParts[3] = new ModelRenderer(this, 39, 43);
		this.keyParts[3].addBox(6F, 1F, -0.5F, 4, 1, 1);
		this.keyParts[3].setRotationPoint(0F, 0F, 0F);
		this.keyParts[3].setTextureSize(128, 64);
		this.keyParts[3].mirror = true;
		this.keyParts[2] = new ModelRenderer(this, 14, 43);
		this.keyParts[2].addBox(-0.5F, 0F, -0.5F, 11, 1, 1);
		this.keyParts[2].setRotationPoint(0F, 0F, 0F);
		this.keyParts[2].setTextureSize(128, 64);
		this.keyParts[2].mirror = true;
		this.keyParts[1] = new ModelRenderer(this, 9, 43);
		this.keyParts[1].addBox(-1.5F, -0.5F, -0.5F, 1, 2, 1);
		this.keyParts[1].setRotationPoint(0F, 0F, 0F);
		this.keyParts[1].setTextureSize(128, 64);
		this.keyParts[1].mirror = true;
		this.keyParts[0] = new ModelRenderer(this, 0, 43);
		this.keyParts[0].addBox(-4.5F, -1F, -0.5F, 3, 3, 1);
		this.keyParts[0].setRotationPoint(0F, 0F, 0F);
		this.keyParts[0].setTextureSize(128, 64);
		this.keyParts[0].mirror = true;
		this.keyParts[5] = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
		this.keyParts[5].addBox(-2.0F, -2.05F, -15.1F, 4, 4, 1, 0.0F);
		this.keyParts[5].rotationPointX = 8.0F;
		this.keyParts[5].rotationPointY = 7.0F;
		this.keyParts[5].rotationPointZ = 15.0F;
	}

	public void renderAll(boolean withKey)
	{
		if (withKey)
		{
			for (final ModelRenderer nmtmr : this.keyParts)
			{
				if (!nmtmr.equals(this.keyParts[5]))
				{
					nmtmr.rotationPointX = 16.0F;
					nmtmr.rotationPointY = 7.0F;
					nmtmr.rotationPointZ = -2.0F;
					nmtmr.rotateAngleY = (float) (3 * Math.PI / 2);
					nmtmr.rotateAngleX = -this.chestLid.rotateAngleX;
					nmtmr.render(0.0625F);
				}
			}
		}

		this.keyParts[5].rotationPointX = 16.0F;
		this.keyParts[5].rotationPointY = 7.0F;
		this.keyParts[5].rotationPointZ = 15.0F;
		this.keyParts[5].rotateAngleX = 0;
		this.keyParts[5].rotateAngleY = 0;
		this.keyParts[5].render(0.0625F);

		super.renderAll();
	}
}
