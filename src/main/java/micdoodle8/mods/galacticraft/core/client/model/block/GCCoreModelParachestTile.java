package micdoodle8.mods.galacticraft.core.client.model.block;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelRenderer;

/**
 * GCCoreModelParachestTile.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreModelParachestTile extends ModelChest
{
	public ModelRenderer keyParts[] = new ModelRenderer[3];

	public GCCoreModelParachestTile()
	{
		super();
		this.keyParts[0] = new ModelRenderer(this, 0, 1);
		this.keyParts[0].addBox(-1.0F, -6.0F, 0.0F, 1, 3, 1);
		this.keyParts[0].setRotationPoint(0F, 0F, 0F);
		this.keyParts[0].setTextureSize(32, 32);
		this.keyParts[0].mirror = true;
		this.keyParts[0].rotationPointX = 7.0F;
		this.keyParts[0].rotationPointY = 7.0F;
		this.keyParts[0].rotationPointZ = 7.5F;
		this.keyParts[1] = new ModelRenderer(this, 0, 1);
		this.keyParts[1].addBox(-1.0F, -5.5F, 0.0F, 4, 1, 1);
		this.keyParts[1].setRotationPoint(0F, 0F, 0F);
		this.keyParts[1].setTextureSize(32, 32);
		this.keyParts[1].mirror = true;
		this.keyParts[1].rotationPointX = 7.0F;
		this.keyParts[1].rotationPointY = 6.0F;
		this.keyParts[1].rotationPointZ = 7.5F;
		this.keyParts[2] = new ModelRenderer(this, 0, 1);
		this.keyParts[2].addBox(-1.0F, -6.0F, 0.0F, 1, 3, 1);
		this.keyParts[2].setRotationPoint(0F, 0F, 0F);
		this.keyParts[2].setTextureSize(32, 32);
		this.keyParts[2].mirror = true;
		this.keyParts[2].rotationPointX = 10.0F;
		this.keyParts[2].rotationPointY = 7.0F;
		this.keyParts[2].rotationPointZ = 7.5F;
	}

	public void renderAll(boolean lidUp)
	{
		if (lidUp)
		{
			for (ModelRenderer m : this.keyParts)
			{
				m.render(0.0625F);
			}
		}

		super.renderAll();
	}
}
