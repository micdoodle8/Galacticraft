package micdoodle8.mods.galacticraft.core.client.fx;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLTextureFX;

public class GCCoreTextureCrudeOilFlowFX extends FMLTextureFX {

	public GCCoreTextureCrudeOilFlowFX()
	{
		super(GCCoreBlocks.crudeOilMoving.blockIndexInTexture + 1);
		this.tileSize = 2;
	}

	@Override
	protected void setup()
	{
		super.setup();
		this.field_1138_g = new float[this.tileSizeSquare];
		this.field_1137_h = new float[this.tileSizeSquare];
		this.field_1136_i = new float[this.tileSizeSquare];
		this.field_1135_j = new float[this.tileSizeSquare];
		this.field_1134_k = 0;
	}

	@Override
	public void bindImage(RenderEngine renderengine)
	{
		GL11.glBindTexture(3553, renderengine.getTexture(GCCoreBlocks.crudeOilMoving.getTextureFile()));
	}

	@Override
	public void onTick()
	{
		this.field_1134_k += 0.1F;
		for (int i = 0; i < this.tileSizeBase; i++)
		{
			for (int k = 0; k < this.tileSizeBase; k++)
			{
				float f = 0.0F;

				for (int j1 = k - 2; j1 <= k; j1++)
				{
					final int k1 = i & this.tileSizeMask;
					final int i2 = j1 & this.tileSizeMask;
					f += this.field_1138_g[k1 + i2 * this.tileSizeBase];
				}

				this.field_1137_h[i + k * this.tileSizeBase] = f / 3.2F + this.field_1136_i[i + k * this.tileSizeBase] * 0.8F;
			}
		}

		for (int j = 0; j < this.tileSizeBase; j++)
		{
			for (int l = 0; l < this.tileSizeBase; l++)
			{
				this.field_1136_i[j + l * this.tileSizeBase] += this.field_1135_j[j + l * this.tileSizeBase] * 0.05F;

				if (this.field_1136_i[j + l * this.tileSizeBase] < 0.0F)
				{
					this.field_1136_i[j + l * this.tileSizeBase] = 0.0F;
				}

				this.field_1135_j[j + l * this.tileSizeBase] -= 0.3F;

				if (Math.random() < 0.20000000000000001D)
				{
					this.field_1135_j[j + l * this.tileSizeBase] = 0.5F;
				}
			}
		}

		final float af[] = this.field_1137_h;
		this.field_1137_h = this.field_1138_g;
		this.field_1138_g = af;

		for (int i1 = 0; i1 < this.tileSizeSquare; i1++)
		{
			float f1 = this.field_1138_g[i1 - MathHelper.floor_float(this.field_1134_k) * this.tileSizeBase & this.tileSizeSquareMask];

			if (f1 > 1.0F)
			{
				f1 = 1.0F;
			}

			if (f1 < 0.0F)
			{
				f1 = 0.0F;
			}

			final float f2 = f1 * f1;
			int l1 = (int) (10F + f2 * 22F);
			int j2 = (int) (50F + f2 * 64F);
			int k2 = 255;

			if (this.anaglyphEnabled)
			{
				final int i3 = (l1 * 30 + j2 * 59 + k2 * 11) / 100;
				final int j3 = (l1 * 30 + j2 * 70) / 100;
				final int k3 = (l1 * 30 + k2 * 70) / 100;
				l1 = i3;
				j2 = j3;
				k2 = k3;
			}

			// imageData[i1 * 4 + 0] = (byte)l1;
			// imageData[i1 * 4 + 1] = (byte)j2;
			// imageData[i1 * 4 + 2] = (byte) k2;
			// imageData[i1 * 4 + 3] = /*(byte)l2*/(byte)255;

			this.imageData[i1 * 4 + 0] = (byte) l1;
			this.imageData[i1 * 4 + 1] = (byte) l1;
			this.imageData[i1 * 4 + 2] = (byte) l1;
			this.imageData[i1 * 4 + 3] = /* (byte)l2 */(byte) 255;
		}

	}

	protected float field_1138_g[];
	protected float field_1137_h[];
	protected float field_1136_i[];
	protected float field_1135_j[];
	private float field_1134_k;
}
