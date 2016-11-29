package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMeteorChunk extends ModelBase
{
    private ModelRenderer[] boxes = new ModelRenderer[6];

    public ModelMeteorChunk()
    {
        textureWidth = 16;
        textureHeight = 16;

        boxes[0] = new ModelRenderer(this, 0, 0);
        boxes[0].addBox(0F, 0F, 0F, 1, 8, 6);
        boxes[0].setRotationPoint(4F, 0.0F, -5F);
        boxes[0].setTextureSize(16, 16);
        setRotation(boxes[0], -0F, -0F, -0F);
        boxes[0].mirror = false;
        boxes[1] = new ModelRenderer(this, 0, 0);
        boxes[1].addBox(0F, 0F, 0F, 5, 8, 1);
        boxes[1].setRotationPoint(-3F, 1.0F, -7F);
        boxes[1].setTextureSize(16, 16);
        setRotation(boxes[1], -0F, -0F, -0F);
        boxes[1].mirror = false;
        boxes[2] = new ModelRenderer(this, 0, 0);
        boxes[2].addBox(0F, 0F, 0F, 1, 8, 6);
        boxes[2].setRotationPoint(-6F, -1.0F, -5F);
        boxes[2].setTextureSize(16, 16);
        setRotation(boxes[2], -0F, -0F, -0F);
        boxes[2].mirror = false;
        boxes[3] = new ModelRenderer(this, 0, 0);
        boxes[3].addBox(0F, 0F, 0F, 6, 1, 5);
        boxes[3].setRotationPoint(-3F, -3.0F, -4F);
        boxes[3].setTextureSize(16, 16);
        setRotation(boxes[3], -0F, -0F, -0F);
        boxes[3].mirror = false;
        boxes[4] = new ModelRenderer(this, 0, 0);
        boxes[4].addBox(0F, 0F, 0F, 5, 8, 1);
        boxes[4].setRotationPoint(-3F, 0.0F, 3F);
        boxes[4].setTextureSize(16, 16);
        setRotation(boxes[4], -0F, -0F, -0F);
        boxes[4].mirror = false;
        boxes[5] = new ModelRenderer(this, 0, 0);
        boxes[5].addBox(0F, 0F, 0F, 9, 12, 9);
        boxes[5].setRotationPoint(-5F, -2.0F, -6F);
        boxes[5].setTextureSize(16, 16);
        setRotation(boxes[5], -0F, -0F, -0F);
        boxes[5].mirror = false;
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        for (ModelRenderer mr : boxes)
        {
            mr.render(f5);
        }
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
