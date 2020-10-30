package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@OnlyIn(Dist.CLIENT)
public class TileEntityTreasureChestRenderer extends TileEntityRenderer<TileEntityTreasureChest>
{
    private final ResourceLocation texture;

    private final ModelRenderer singleLid;
    private final ModelRenderer singleBottom;
    private final ModelRenderer[] keyParts = new ModelRenderer[5];
    private final ModelRenderer latch;

    public TileEntityTreasureChestRenderer(TileEntityRendererDispatcher rendererDispatcherIn, ResourceLocation texture)
    {
        super(rendererDispatcherIn);
        this.texture = texture;
        singleBottom = new ModelRenderer(64, 64, 0, 19);
        singleBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        singleLid = new ModelRenderer(64, 64, 0, 0);
        singleLid.addBox(1.0F, 0.0F, -14.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        singleLid.rotationPointY = 9.0F;
        singleLid.rotationPointZ = 15.0F;
        keyParts[4] = new ModelRenderer(64, 64, 50, 43);
        keyParts[4].addBox(7F, 2F, -0.5F, 3, 1, 1);
        keyParts[4].setRotationPoint(0F, 0F, 0F);
        keyParts[4].setTextureSize(64, 64);
        keyParts[4].mirror = true;
        keyParts[3] = new ModelRenderer(64, 64, 39, 43);
        keyParts[3].addBox(6F, 1F, -0.5F, 4, 1, 1);
        keyParts[3].setRotationPoint(0F, 0F, 0F);
        keyParts[3].setTextureSize(64, 64);
        keyParts[3].mirror = true;
        keyParts[2] = new ModelRenderer(64, 64, 14, 43);
        keyParts[2].addBox(-0.5F, 0F, -0.5F, 11, 1, 1);
        keyParts[2].setRotationPoint(0F, 0F, 0F);
        keyParts[2].setTextureSize(64, 64);
        keyParts[2].mirror = true;
        keyParts[1] = new ModelRenderer(64, 64, 9, 43);
        keyParts[1].addBox(-1.5F, -0.5F, -0.5F, 1, 2, 1);
        keyParts[1].setRotationPoint(0F, 0F, 0F);
        keyParts[1].setTextureSize(64, 64);
        keyParts[1].mirror = true;
        keyParts[0] = new ModelRenderer(64, 64, 0, 43);
        keyParts[0].addBox(-4.5F, -1F, -0.5F, 3, 3, 1);
        keyParts[0].setRotationPoint(0F, 0F, 0F);
        keyParts[0].setTextureSize(64, 64);
        keyParts[0].mirror = true;
        latch = new ModelRenderer(64, 64, 0, 0).setTextureSize(64, 64);
        latch.addBox(-2.0F, -0.05F, -15.1F, 4, 4, 1, 0.0F);
        latch.rotationPointX = 8.0F;
        latch.rotationPointY = 7.0F;
        latch.rotationPointZ = 15.0F;
    }

    @Override
    public void render(TileEntityTreasureChest chest, float partialTicks, MatrixStack matStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
//        GL11.glPushMatrix();
        matStack.push();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
////        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
//        matStack.translate(0.0F, 1.0F, 1.0F);
//        matStack.scale(1.0F, -1.0F, -1.0F);
////        GL11.glScalef(1.0F, -1.0F, -1.0F);
//        matStack.translate(0.5F, 0.5F, 0.5F);
////        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        float f = chest.getBlockState().get(ChestBlock.FACING).getHorizontalAngle();
////        GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);
//        matStack.rotate(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), f, true));
////        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
//        matStack.translate(-0.5F, -0.5F, -0.5F);
        matStack.translate(0.5D, 0.5D, 0.5D);
        matStack.rotate(Vector3f.YP.rotationDegrees(-f));
        matStack.translate(-0.5D, -0.5D, -0.5D);
        float f1 = ((IChestLid) chest).getLidAngle(partialTicks);
        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        float lidRotation = (f1 * ((float) Math.PI / 2F));
        singleLid.rotateAngleX = lidRotation;

        RenderType renderType = RenderType.getEntitySolid(texture);
        IVertexBuilder builder = bufferIn.getBuffer(renderType);

        if (!chest.locked)
        {
            for (final ModelRenderer nmtmr : keyParts)
            {
                nmtmr.rotationPointX = 8.0F;
                nmtmr.rotationPointY = 7.0F;
                nmtmr.rotationPointZ = -2.0F;
                nmtmr.rotateAngleY = 3 * Constants.halfPI;
                nmtmr.rotateAngleX = lidRotation;
                nmtmr.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        latch.rotationPointX = 8.0F;
        latch.rotationPointY = 7.0F;
        latch.rotationPointZ = 15.0F;
        latch.rotateAngleX = 0;
        latch.rotateAngleY = 0;
        latch.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        singleLid.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        singleBottom.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//        singleLatch.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        GL11.glPopMatrix();
        matStack.pop();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
