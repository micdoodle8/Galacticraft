package micdoodle8.mods.galacticraft.core.client.render.tile;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class GCCoreTileEntityLandingPadRenderer extends TileEntitySpecialRenderer
{
	private HMModelStarForge model;
	
	public GCCoreTileEntityLandingPadRenderer()
	{
		this.model = new HMModelStarForge();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity var1, double par2, double par4, double par6, float var8)
	{
		this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/refinery.png");

		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.5F, (float)par6 + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);
//		this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
		GL11.glPopMatrix();
	}
	
	public class HMModelStarForge extends ModelBase
	{
		ModelRenderer NETHERSTAR_1_ROT;
		ModelRenderer NETHERSTAR_2_ROT;
		ModelRenderer NETHERSTAR_3_ROT;
		ModelRenderer NETHERSTAR_4_ROT;
		ModelRenderer TOP_1;
		ModelRenderer TOP_2;
		ModelRenderer TOP_4;
		ModelRenderer TOP_3;
		ModelRenderer TOP_5;
		ModelRenderer TOP_6;
		ModelRenderer CHANNEL_1;
		ModelRenderer DEPOSIT_2;
		ModelRenderer CHANNEL_2;
		ModelRenderer DEPOSIT_1;
		ModelRenderer DEPOSIT_3;
		ModelRenderer CHANNEL_3;
		ModelRenderer CHANNEL_4;
		ModelRenderer DEPOSIT_4;
		ModelRenderer SUPPORT_1;
		ModelRenderer SUPPORT_2;
		ModelRenderer SUPPORT_3;
		ModelRenderer SUPPORT_4;
		ModelRenderer SUPPORT_5;
		ModelRenderer SUPPORT_6;
		ModelRenderer BASE_1;
		ModelRenderer BASE_2;
		ModelRenderer BASE_3;
		ModelRenderer BASE_4;
		ModelRenderer BASE_5;
		ModelRenderer BASE_6;
		
		public HMModelStarForge()
		{
			textureWidth = 128;
			textureHeight = 128;
			
			NETHERSTAR_1_ROT = new ModelRenderer(this, 11, 26);
			NETHERSTAR_1_ROT.addBox(-3F, -3F, -3F, 6, 6, 6);
			NETHERSTAR_1_ROT.setRotationPoint(0F, 15F, 0F);
			NETHERSTAR_1_ROT.setTextureSize(128, 128);
			NETHERSTAR_1_ROT.mirror = true;
			setRotation(NETHERSTAR_1_ROT, 0.7853982F, 0.7853982F, 0.7853982F);
			NETHERSTAR_2_ROT = new ModelRenderer(this, 11, 26);
			NETHERSTAR_2_ROT.addBox(-3F, -3F, -3F, 6, 6, 6);
			NETHERSTAR_2_ROT.setRotationPoint(0F, 15F, 0F);
			NETHERSTAR_2_ROT.setTextureSize(128, 128);
			NETHERSTAR_2_ROT.mirror = true;
			setRotation(NETHERSTAR_2_ROT, -0.7853982F, -0.7853982F, -0.7853982F);
			NETHERSTAR_3_ROT = new ModelRenderer(this, 11, 26);
			NETHERSTAR_3_ROT.addBox(-3F, -3F, -3F, 6, 6, 6);
			NETHERSTAR_3_ROT.setRotationPoint(0F, 15F, 0F);
			NETHERSTAR_3_ROT.setTextureSize(128, 128);
			NETHERSTAR_3_ROT.mirror = true;
			setRotation(NETHERSTAR_3_ROT, 2.356194F, 2.356194F, 2.356194F);
			NETHERSTAR_4_ROT = new ModelRenderer(this, 11, 26);
			NETHERSTAR_4_ROT.addBox(-3F, -3F, -3F, 6, 6, 6);
			NETHERSTAR_4_ROT.setRotationPoint(0F, 15F, 0F);
			NETHERSTAR_4_ROT.setTextureSize(128, 128);
			NETHERSTAR_4_ROT.mirror = true;
			setRotation(NETHERSTAR_4_ROT, -2.356194F, -2.356194F, -2.356194F);
			TOP_1 = new ModelRenderer(this, 45, 4);
			TOP_1.addBox(-5F, 0F, -9F, 10, 1, 5);
			TOP_1.setRotationPoint(0F, 9F, 0F);
			TOP_1.setTextureSize(128, 128);
			TOP_1.mirror = true;
			setRotation(TOP_1, 0F, 0F, 0F);
			TOP_2 = new ModelRenderer(this, 45, 4);
			TOP_2.addBox(-5F, 0F, -9F, 10, 1, 5);
			TOP_2.setRotationPoint(0F, 9F, 0F);
			TOP_2.setTextureSize(128, 128);
			TOP_2.mirror = true;
			setRotation(TOP_2, 0F, 1.047198F, 0F);
			TOP_4 = new ModelRenderer(this, 45, 4);
			TOP_4.addBox(-5F, 0F, -9F, 10, 1, 5);
			TOP_4.setRotationPoint(0F, 9F, 0F);
			TOP_4.setTextureSize(128, 128);
			TOP_4.mirror = true;
			setRotation(TOP_4, 0F, 2.094395F, 0F);
			TOP_3 = new ModelRenderer(this, 45, 4);
			TOP_3.addBox(-5F, 0F, 4F, 10, 1, 5);
			TOP_3.setRotationPoint(0F, 9F, 0F);
			TOP_3.setTextureSize(128, 128);
			TOP_3.mirror = true;
			setRotation(TOP_3, 0F, 0F, 0F);
			TOP_5 = new ModelRenderer(this, 45, 4);
			TOP_5.addBox(-5F, 0F, 4F, 10, 1, 5);
			TOP_5.setRotationPoint(0F, 9F, 0F);
			TOP_5.setTextureSize(128, 128);
			TOP_5.mirror = true;
			setRotation(TOP_5, 0F, 2.094395F, 0F);
			TOP_6 = new ModelRenderer(this, 45, 4);
			TOP_6.addBox(-5F, 0F, 4F, 10, 1, 5);
			TOP_6.setRotationPoint(0F, 9F, 0F);
			TOP_6.setTextureSize(128, 128);
			TOP_6.mirror = true;
			setRotation(TOP_6, 0F, 1.047198F, 0F);
			CHANNEL_1 = new ModelRenderer(this, 0, 14);
			CHANNEL_1.addBox(-3F, 0F, 9F, 6, 1, 10);
			CHANNEL_1.setRotationPoint(0F, 19F, 0F);
			CHANNEL_1.setTextureSize(128, 128);
			CHANNEL_1.mirror = true;
			setRotation(CHANNEL_1, -0.1570796F, 1.047198F, 0F);
			DEPOSIT_2 = new ModelRenderer(this, 41, 12);
			DEPOSIT_2.addBox(-4F, 0F, 15F, 8, 2, 10);
			DEPOSIT_2.setRotationPoint(0F, 22F, 0F);
			DEPOSIT_2.setTextureSize(128, 128);
			DEPOSIT_2.mirror = true;
			setRotation(DEPOSIT_2, 0F, 2.094395F, 0F);
			CHANNEL_2 = new ModelRenderer(this, 0, 14);
			CHANNEL_2.addBox(-3F, 0F, 9F, 6, 1, 10);
			CHANNEL_2.setRotationPoint(0F, 19F, 0F);
			CHANNEL_2.setTextureSize(128, 128);
			CHANNEL_2.mirror = true;
			setRotation(CHANNEL_2, -0.1570796F, 2.094395F, 0F);
			DEPOSIT_1 = new ModelRenderer(this, 41, 12);
			DEPOSIT_1.addBox(-4F, 0F, 15F, 8, 2, 10);
			DEPOSIT_1.setRotationPoint(0F, 22F, 0F);
			DEPOSIT_1.setTextureSize(128, 128);
			DEPOSIT_1.mirror = true;
			setRotation(DEPOSIT_1, 0F, 1.047198F, 0F);
			DEPOSIT_3 = new ModelRenderer(this, 41, 12);
			DEPOSIT_3.addBox(-4F, 0F, 15F, 8, 2, 10);
			DEPOSIT_3.setRotationPoint(0F, 22F, 0F);
			DEPOSIT_3.setTextureSize(128, 128);
			DEPOSIT_3.mirror = true;
			setRotation(DEPOSIT_3, 0F, -1.047198F, 0F);
			CHANNEL_3 = new ModelRenderer(this, 0, 14);
			CHANNEL_3.addBox(-3F, 0F, 9F, 6, 1, 10);
			CHANNEL_3.setRotationPoint(0F, 19F, 0F);
			CHANNEL_3.setTextureSize(128, 128);
			CHANNEL_3.mirror = true;
			setRotation(CHANNEL_3, -0.1570796F, -1.047198F, 0F);
			CHANNEL_4 = new ModelRenderer(this, 0, 14);
			CHANNEL_4.addBox(-3F, 0F, 9F, 6, 1, 10);
			CHANNEL_4.setRotationPoint(0F, 19F, 0F);
			CHANNEL_4.setTextureSize(128, 128);
			CHANNEL_4.mirror = true;
			setRotation(CHANNEL_4, -0.1570796F, -2.094395F, 0F);
			DEPOSIT_4 = new ModelRenderer(this, 41, 12);
			DEPOSIT_4.addBox(-4F, 0F, 15F, 8, 2, 10);
			DEPOSIT_4.setRotationPoint(0F, 22F, 0F);
			DEPOSIT_4.setTextureSize(128, 128);
			DEPOSIT_4.mirror = true;
			setRotation(DEPOSIT_4, 0F, -2.094395F, 0F);
			SUPPORT_1 = new ModelRenderer(this, 0, 26);
			SUPPORT_1.addBox(-1F, 0F, 9F, 2, 16, 2);
			SUPPORT_1.setRotationPoint(0F, 8F, 0F);
			SUPPORT_1.setTextureSize(128, 128);
			SUPPORT_1.mirror = true;
			setRotation(SUPPORT_1, 0F, 0.5235988F, 0F);
			SUPPORT_2 = new ModelRenderer(this, 0, 26);
			SUPPORT_2.addBox(-1F, 0F, 9F, 2, 16, 2);
			SUPPORT_2.setRotationPoint(0F, 8F, 0F);
			SUPPORT_2.setTextureSize(128, 128);
			SUPPORT_2.mirror = true;
			setRotation(SUPPORT_2, 0F, 1.570796F, 0F);
			SUPPORT_3 = new ModelRenderer(this, 0, 26);
			SUPPORT_3.addBox(-1F, 0F, 9F, 2, 16, 2);
			SUPPORT_3.setRotationPoint(0F, 8F, 0F);
			SUPPORT_3.setTextureSize(128, 128);
			SUPPORT_3.mirror = true;
			setRotation(SUPPORT_3, 0F, -2.617994F, 0F);
			SUPPORT_4 = new ModelRenderer(this, 0, 26);
			SUPPORT_4.addBox(-1F, 0F, 9F, 2, 16, 2);
			SUPPORT_4.setRotationPoint(0F, 8F, 0F);
			SUPPORT_4.setTextureSize(128, 128);
			SUPPORT_4.mirror = true;
			setRotation(SUPPORT_4, 0F, 2.617994F, 0F);
			SUPPORT_5 = new ModelRenderer(this, 0, 26);
			SUPPORT_5.addBox(-1F, 0F, 9F, 2, 16, 2);
			SUPPORT_5.setRotationPoint(0F, 8F, 0F);
			SUPPORT_5.setTextureSize(128, 128);
			SUPPORT_5.mirror = true;
			setRotation(SUPPORT_5, 0F, -0.5235988F, 0F);
			SUPPORT_6 = new ModelRenderer(this, 0, 26);
			SUPPORT_6.addBox(-1F, 0F, 9F, 2, 16, 2);
			SUPPORT_6.setRotationPoint(0F, 8F, 0F);
			SUPPORT_6.setTextureSize(128, 128);
			SUPPORT_6.mirror = true;
			setRotation(SUPPORT_6, 0F, -1.570796F, 0F);
			BASE_1 = new ModelRenderer(this, 0, 0);
			BASE_1.addBox(-5F, 0F, 0F, 10, 4, 9);
			BASE_1.setRotationPoint(0F, 20F, 0F);
			BASE_1.setTextureSize(128, 128);
			BASE_1.mirror = true;
			setRotation(BASE_1, 0F, 3.141593F, 0F);
			BASE_2 = new ModelRenderer(this, 0, 0);
			BASE_2.addBox(-5F, 0F, 0F, 10, 4, 9);
			BASE_2.setRotationPoint(0F, 20F, 0F);
			BASE_2.setTextureSize(128, 128);
			BASE_2.mirror = true;
			setRotation(BASE_2, 0F, -2.094395F, 0F);
			BASE_3 = new ModelRenderer(this, 0, 0);
			BASE_3.addBox(-5F, 0F, 0F, 10, 4, 9);
			BASE_3.setRotationPoint(0F, 20F, 0F);
			BASE_3.setTextureSize(128, 128);
			BASE_3.mirror = true;
			setRotation(BASE_3, 0F, -1.047198F, 0F);
			BASE_4 = new ModelRenderer(this, 0, 0);
			BASE_4.addBox(-5F, 0F, 0F, 10, 4, 9);
			BASE_4.setRotationPoint(0F, 20F, 0F);
			BASE_4.setTextureSize(128, 128);
			BASE_4.mirror = true;
			setRotation(BASE_4, 0F, 2.094395F, 0F);
			BASE_5 = new ModelRenderer(this, 0, 0);
			BASE_5.addBox(-5F, 0F, 0F, 10, 4, 9);
			BASE_5.setRotationPoint(0F, 20F, 0F);
			BASE_5.setTextureSize(128, 128);
			BASE_5.mirror = true;
			setRotation(BASE_5, 0F, 1.047198F, 0F);
			BASE_6 = new ModelRenderer(this, 0, 0);
			BASE_6.addBox(-5F, 0F, 0F, 10, 4, 9);
			BASE_6.setRotationPoint(0F, 20F, 0F);
			BASE_6.setTextureSize(128, 128);
			BASE_6.mirror = true;
			setRotation(BASE_6, 0F, 0F, 0F);
		}
		
		@Override
		public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
		{
			super.render(entity, f, f1, f2, f3, f4, f5);
			this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
			this.NETHERSTAR_1_ROT.render(0.0625F);
			this.NETHERSTAR_2_ROT.render(0.0625F);
			this.NETHERSTAR_3_ROT.render(0.0625F);
			this.NETHERSTAR_4_ROT.render(0.0625F);
			this.TOP_1.render(0.0625F);
			this.TOP_2.render(0.0625F);
			this.TOP_4.render(0.0625F);
			this.TOP_3.render(0.0625F);
			this.TOP_5.render(0.0625F);
			this.TOP_6.render(0.0625F);
			this.CHANNEL_1.render(0.0625F);
			this.DEPOSIT_2.render(0.0625F);
			this.CHANNEL_2.render(0.0625F);
			this.DEPOSIT_1.render(0.0625F);
			this.DEPOSIT_3.render(0.0625F);
			this.CHANNEL_3.render(0.0625F);
			this.CHANNEL_4.render(0.0625F);
			this.DEPOSIT_4.render(0.0625F);
			this.SUPPORT_1.render(0.0625F);
			this.SUPPORT_2.render(0.0625F);
			this.SUPPORT_3.render(0.0625F);
			this.SUPPORT_4.render(0.0625F);
			this.SUPPORT_5.render(0.0625F);
			this.SUPPORT_6.render(0.0625F);
			this.BASE_1.render(0.0625F);
			this.BASE_2.render(0.0625F);
			this.BASE_3.render(0.0625F);
			this.BASE_4.render(0.0625F);
			this.BASE_5.render(0.0625F);
			this.BASE_6.render(0.0625F);
			
		}
		
		private void setRotation(ModelRenderer model, float x, float y, float z)
		{
			model.rotateAngleX = x;
			model.rotateAngleY = y;
			model.rotateAngleZ = z;
			
		}
		
		@Override
		public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
		{
			super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
			
		}
		
	}
}
