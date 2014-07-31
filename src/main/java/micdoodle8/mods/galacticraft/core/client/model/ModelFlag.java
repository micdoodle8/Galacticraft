package micdoodle8.mods.galacticraft.core.client.model;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelFlag extends ModelBase
{
	ModelRenderer base;
	ModelRenderer pole;

	public ModelFlag()
	{
		this.textureWidth = 128;
		this.textureHeight = 64;
		this.base = new ModelRenderer(this, 4, 0);
		this.base.addBox(-1.5F, 0F, -1.5F, 3, 1, 3);
		this.base.setRotationPoint(0F, 23F, 0F);
		this.base.setTextureSize(128, 64);
		this.base.mirror = true;
		this.setRotation(this.base, 0F, 0F, 0F);
		this.pole = new ModelRenderer(this, 0, 0);
		this.pole.addBox(-0.5F, -40F, -0.5F, 1, 40, 1);
		this.pole.setRotationPoint(0F, 23F, 0F);
		this.pole.setTextureSize(128, 64);
		this.pole.mirror = true;
		this.setRotation(this.pole, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);

		if (entity instanceof EntityFlag)
		{
			EntityFlag flag = (EntityFlag) entity;
			this.renderPole(flag, f5);
			this.renderFlag(flag, flag.ticksExisted);
		}
	}

	public void renderPole(Entity entity, float f5)
	{
		this.base.render(f5);
		this.pole.render(f5);
	}

	public void renderFlag(EntityFlag entity, float ticks)
	{
		if (entity.flagData != null)
		{
			GL11.glPushMatrix();

			GL11.glScalef(0.5F, 0.5F, 0.5F);
			GL11.glTranslatef(0.0F, -1.1F, 0.0F);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_CULL_FACE);

            for (int i = 0; i < entity.flagData.getWidth(); i++) {
                for (int j = 0; j < entity.flagData.getHeight(); j++) {
                    GL11.glPushMatrix();
                    GL11.glTranslatef(0, -1.0F, 0);
                    float offset = 0.0F;
                    float offsetAhead = 0.0F;

                    if (!(entity.worldObj.provider instanceof IGalacticraftWorldProvider))
                    {
                        offset = (float) (Math.sin(ticks / 2.0F + i * 50 + 3) / 25.0F) * i / 30.0F;
                        offsetAhead = (float) (Math.sin(ticks / 2.0F + (i + 1) * 50 + 3) / 25.0F) * (i + 1) / 30.0F;
                    }

                    Vector3 col = entity.flagData.getColorAt(i, j);
                    GL11.glColor3f(col.floatX(), col.floatY(), col.floatZ());

                    Tessellator tess = Tessellator.instance;
                    tess.startDrawing(GL11.GL_TRIANGLES);
                    tess.addVertex(i / 24.0F + 0.0 / 24.0F, j / 24.0F + 0.0 / 24.0F + offset, offset);
                    tess.addVertex(i / 24.0F + 0.0 / 24.0F, j / 24.0F + 1.0 / 24.0F + offset, offset);
                    tess.addVertex(i / 24.0F + 1.0 / 24.0F, j / 24.0F + 1.0 / 24.0F + offsetAhead, offsetAhead);

                    tess.addVertex(i / 24.0F + 0.0 / 24.0F, j / 24.0F + 0.0 / 24.0F + offset, offset);
                    tess.addVertex(i / 24.0F + 1.0 / 24.0F, j / 24.0F + 1.0 / 24.0F + offsetAhead, offsetAhead);
                    tess.addVertex(i / 24.0F + 1.0 / 24.0F, j / 24.0F + 0.0 / 24.0F + offsetAhead, offsetAhead);
                    tess.draw();

                    GL11.glColor3f(1, 1, 1);
                    GL11.glPopMatrix();
                }
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_CULL_FACE);

			GL11.glPopMatrix();
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
