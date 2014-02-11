package micdoodle8.mods.galacticraft.core.client.model;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.FMLClientHandler;

/**
 * GCCoreModelParaChest.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreModelParaChest extends ModelChest
{
	private static final ResourceLocation grayParachuteTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/parachute/gray.png");

	public ModelRenderer[] parachute = new ModelRenderer[3];
	public ModelRenderer[] parachuteStrings = new ModelRenderer[4];

	public GCCoreModelParaChest()
	{
		this(0.0F);
	}

	public GCCoreModelParaChest(float par1)
	{
		super();

		this.parachute[0] = new ModelRenderer(this, 0, 0).setTextureSize(512, 256);
		this.parachute[0].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, par1);
		this.parachute[0].setRotationPoint(15.0F, 4.0F, 0.0F);
		this.parachute[1] = new ModelRenderer(this, 0, 42).setTextureSize(512, 256);
		this.parachute[1].addBox(-20.0F, -45.0F, -20.0F, 40, 2, 40, par1);
		this.parachute[1].setRotationPoint(0.0F, 0.0F, 0.0F);
		this.parachute[2] = new ModelRenderer(this, 0, 0).setTextureSize(512, 256);
		this.parachute[2].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, par1);
		this.parachute[2].setRotationPoint(11F, -11, 0.0F);

		this.parachuteStrings[0] = new ModelRenderer(this, 100, 0).setTextureSize(512, 256);
		this.parachuteStrings[0].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
		this.parachuteStrings[0].setRotationPoint(0.0F, 0.0F, 0.0F);
		this.parachuteStrings[1] = new ModelRenderer(this, 100, 0).setTextureSize(512, 256);
		this.parachuteStrings[1].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
		this.parachuteStrings[1].setRotationPoint(0.0F, 0.0F, 0.0F);
		this.parachuteStrings[2] = new ModelRenderer(this, 100, 0).setTextureSize(512, 256);
		this.parachuteStrings[2].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
		this.parachuteStrings[2].setRotationPoint(0.0F, 0.0F, 0.0F);
		this.parachuteStrings[3] = new ModelRenderer(this, 100, 0).setTextureSize(512, 256);
		this.parachuteStrings[3].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
		this.parachuteStrings[3].setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	@Override
	public void renderAll()
	{
		super.renderAll();

		FMLClientHandler.instance().getClient().renderEngine.bindTexture(GCCoreModelParaChest.grayParachuteTexture);

		this.chestLid.rotateAngleX = (float) Math.PI;
		this.chestBelow.rotateAngleX = (float) Math.PI;
		this.chestKnob.rotateAngleX = (float) Math.PI;

		this.chestLid.rotationPointX = 2.0F;
		this.chestLid.rotationPointY = 7.0F;
		this.chestLid.rotationPointZ = -6.0F;
		this.chestKnob.rotationPointX = 9.0F;
		this.chestKnob.rotationPointY = 7.0F;
		this.chestKnob.rotationPointZ = -6.0F;
		this.chestBelow.rotationPointX = 2.0F;
		this.chestBelow.rotationPointY = 8.0F;
		this.chestBelow.rotationPointZ = 8.0F;

		int i;

		for (i = 0; i < this.parachute.length; i++)
		{
			this.parachute[i].render(0.0625F);
		}

		for (i = 0; i < this.parachuteStrings.length; i++)
		{
			this.parachuteStrings[i].render(0.0625F);
		}

		this.parachute[0].rotateAngleY = (float) (0 * (Math.PI / 180F));
		this.parachute[2].rotateAngleY = (float) -(0 * (Math.PI / 180F));
		this.parachuteStrings[0].rotateAngleY = (float) (0 * (Math.PI / 180F));
		this.parachuteStrings[1].rotateAngleY = (float) (0 * (Math.PI / 180F));
		this.parachuteStrings[2].rotateAngleY = (float) -(0 * (Math.PI / 180F));
		this.parachuteStrings[3].rotateAngleY = (float) -(0 * (Math.PI / 180F));

		this.parachute[0].setRotationPoint(-5.85F, -11.0F, 2.0F);
		this.parachute[1].setRotationPoint(9F, -7F, 2.0F);
		this.parachute[2].setRotationPoint(-2.15F, 4.0F, 2.0F);
		this.parachute[0].rotateAngleZ = (float) (210F * (Math.PI / 180F));
		this.parachute[1].rotateAngleZ = (float) (180F * (Math.PI / 180F));
		this.parachute[2].rotateAngleZ = (float) -(210F * (Math.PI / 180F));
		this.parachuteStrings[0].rotateAngleZ = (float) ((155F + 180F) * (Math.PI / 180F));
		this.parachuteStrings[0].rotateAngleX = (float) (23F * (Math.PI / 180F));
		this.parachuteStrings[0].setRotationPoint(9.0F, 3.0F, 2.0F);
		this.parachuteStrings[1].rotateAngleZ = (float) ((155F + 180F) * (Math.PI / 180F));
		this.parachuteStrings[1].rotateAngleX = (float) -(23F * (Math.PI / 180F));
		this.parachuteStrings[1].setRotationPoint(9.0F, 3.0F, 2.0F);

		this.parachuteStrings[2].rotateAngleZ = (float) -((155F + 180F) * (Math.PI / 180F));
		this.parachuteStrings[2].rotateAngleX = (float) (23F * (Math.PI / 180F));
		this.parachuteStrings[2].setRotationPoint(9.0F, 3.0F, 2.0F);
		this.parachuteStrings[3].rotateAngleZ = (float) -((155F + 180F) * (Math.PI / 180F));
		this.parachuteStrings[3].rotateAngleX = (float) -(23F * (Math.PI / 180F));
		this.parachuteStrings[3].setRotationPoint(9.0F, 3.0F, 2.0F);
	}

	public void renderParachute()
	{
	}
}
