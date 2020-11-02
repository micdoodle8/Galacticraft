package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockEmergencyBox;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEmergencyBox;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntityEmergencyBoxRenderer extends TileEntityRenderer<TileEntityEmergencyBox>
{
    private static final float MASKSCALE = 3F;

    public TileEntityEmergencyBoxRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    public class Flap extends Model
    {
        ModelRenderer model;
        protected float angle;

        public Flap()
        {
            super(RenderType::getEntitySolid);
            this.angle = 0.0F;
            this.textureWidth = 32;
            this.textureHeight = 32;
            this.model = new ModelRenderer(this, 0, 0);
            this.model.addBox(-6F, -6F, 0F, 12, 6, 1);
            this.model.setRotationPoint(0F, 6F, -7F);
            this.model.setTextureSize(this.textureWidth, this.textureHeight);
            this.model.mirror = true;
        }

        private void setRotation(ModelRenderer model, float x, float y, float z)
        {
            model.rotateAngleX = x;
            model.rotateAngleY = y;
            model.rotateAngleZ = z;
        }

        @Override
        public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            this.setRotation(this.model, angle / Constants.RADIANS_TO_DEGREES, 0F, 0F);
            model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }

    public class Plinth extends Model
    {
        ModelRenderer model;

        public Plinth()
        {
            super(RenderType::getEntitySolid);
            this.textureWidth = 16;
            this.textureHeight = 16;
            this.model = new ModelRenderer(this, 0, 0);
            this.model.addBox(-6F, -7F, -6F, 12, 1, 12);
            this.model.setRotationPoint(0F, 0F, 0F);
            this.model.setTextureSize(this.textureWidth, this.textureHeight);
            this.model.mirror = true;
        }

        @Override
        public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }

        public void setHeight(float height)
        {
            this.model.setRotationPoint(0F, height, 0F);
        }

//        public void render(float height)
//        {
//            this.model.setRotationPoint(0F, height, 0F);
//            this.model.render(1F / 16F);
//        }
    }

    public class Mask extends Model
    {
        ModelRenderer model;

        public Mask()
        {
            super(RenderType::getEntityCutout);
            this.textureWidth = 128;
            this.textureHeight = 64;
            this.model = new ModelRenderer(this, 0, 0);
            this.model.addBox(-8.0F, -4F, -8.0F, 16, 16, 16, 1.0F);
            this.model.setRotationPoint(0F, 0F, 0F);
            this.model.setTextureSize(this.textureWidth, this.textureHeight);
            this.model.mirror = true;
        }

        @Override
        public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            matrixStackIn.push();
            float scale = 1.0F / MASKSCALE;
            matrixStackIn.scale(scale, scale, scale);
            model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.pop();
        }

        public void setHeight(float height)
        {
            this.model.setRotationPoint(0F, height * MASKSCALE, 0F);
        }

//        public void render(float height)
//        {
//            this.model.setRotationPoint(0F, height * MASKSCALE, 0F);
//            this.model.render(1F / 16F / MASKSCALE);
//        }
    }

    public class Tank extends Model
    {
        ModelRenderer model;

        public Tank()
        {
            super(RenderType::getEntitySolid);
            this.textureWidth = 128;
            this.textureHeight = 64;
            this.model = new ModelRenderer(this, 0, 0);
            this.model.setTextureOffset(4, 0);   // Green tank
            this.model.addBox(-1.5F, 0F, -1.5F, 3, 7, 3, 1.0F);
            this.model.setRotationPoint(0F, 0F, 0F);
            this.model.setTextureSize(this.textureWidth, this.textureHeight);
            this.model.mirror = true;
        }

        @Override
        public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            matrixStackIn.push();
            float scale = 1.0F / MASKSCALE;
            matrixStackIn.scale(scale, scale, scale);
            model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.pop();
        }

        public void setHeight(float height)
        {
            this.model.setRotationPoint(0F, height * MASKSCALE, 0F);
        }

//        public void render(float height)
//        {
//            this.model.setRotationPoint(0F, height * MASKSCALE, 0F);
//            this.model.render(1F / 16F / MASKSCALE);
//        }
    }

    public class Pack extends Model
    {
        ModelRenderer model;

        public Pack()
        {
            super(RenderType::getEntitySolid);
            this.textureWidth = 256;
            this.textureHeight = 256;
            this.model = new ModelRenderer(this, 0, 0);
            this.model.setTextureOffset(50, 50);
            this.model.addBox(-6F, -11F, -10F, 12, 1, 20, 1.0F);
            this.model.setRotationPoint(0F, 0F, 0F);
            this.model.setTextureSize(this.textureWidth, this.textureHeight);
            this.model.mirror = true;
        }

        @Override
        public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            matrixStackIn.push();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.pop();
        }

        public void setHeight(float height)
        {
            this.model.setRotationPoint(0F, height * 2F, 0F);
        }
    }

    private static final ResourceLocation boxTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/emergency_box.png");
    private static final ResourceLocation flapTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/emergency_box_flap.png");
    private static final ResourceLocation packTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/red.png");
    private static final ResourceLocation oxygenMaskTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/oxygen.png");
    private static final ResourceLocation oxygenTankTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/player.png");
    private final Flap flapA = new Flap();
    private final Flap flapB = new Flap();
    private final Flap flapC = new Flap();
    private final Flap flapD = new Flap();
    private final Plinth plat = new Plinth();
    private final Mask mask = new Mask();
    private final Tank tank = new Tank();
    private final Pack pack = new Pack();

    @Override
    public void render(TileEntityEmergencyBox emergencyBox, float partialTicks, MatrixStack matStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockState state = emergencyBox.getBlockState();
        if (!(state.getBlock() instanceof BlockEmergencyBox))
        {
            return;
        }
//        GlStateManager.pushMatrix();
        matStack.push();
//        GlStateManager.translatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        matStack.translate(0.5F, 0.5F, 0.5F);

        flapA.angle = emergencyBox.getAngleA(partialTicks);
        flapB.angle = emergencyBox.getAngleB(partialTicks);
        flapC.angle = emergencyBox.getAngleC(partialTicks);
        flapD.angle = emergencyBox.getAngleD(partialTicks);
        float height = Math.max(Math.max(flapA.angle, flapB.angle), Math.max(flapC.angle, flapD.angle)) / 90F;

        if (height > 0F && state.getBlock() == GCBlocks.emergencyBoxKit)
        {
//            GlStateManager.pushMatrix();
            matStack.push();
            RenderType renderType = RenderType.getEntitySolid(packTexture);
            IVertexBuilder builder = bufferIn.getBuffer(renderType);
            this.pack.setHeight(height);
            this.pack.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//            GlStateManager.rotatef(180F, 1F, 0F, 0F);
            matStack.rotate(new Quaternion(Vector3f.XP, 180.0F, true));
//            GlStateManager.translatef(0.0F, 0.0F, -0.07F);
            matStack.translate(0.0F, 0.0F, -0.07F);
            renderType = RenderType.getEntityCutout(oxygenMaskTexture);
            builder = bufferIn.getBuffer(renderType);
            this.mask.setHeight(-height);
            this.mask.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            renderType = RenderType.getEntitySolid(oxygenTankTexture);
            builder = bufferIn.getBuffer(renderType);
            this.mask.setHeight(-height);
//            this.mask.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//            GlStateManager.translatef(0.1F, 0.11F, 0.3F);
            matStack.translate(0.1F, 0.11F, 0.3F);
            this.tank.setHeight(-height);
            this.tank.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//            GlStateManager.translatef(-0.2F, 0F, 0F);
            matStack.translate(-0.2F, 0.0F, 0.0F);
            this.tank.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//            GlStateManager.popMatrix();
            matStack.pop();
        }

        RenderType renderType = RenderType.getEntitySolid(boxTexture);
        IVertexBuilder builder = bufferIn.getBuffer(renderType);
        this.plat.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        renderType = RenderType.getEntitySolid(flapTexture);
        builder = bufferIn.getBuffer(renderType);
        this.flapA.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//        GlStateManager.rotatef(90F, 0, 1F, 0F);
        matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapB.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapC.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapD.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.rotate(new Quaternion(Vector3f.XP, 180.0F, true));
        this.flapB.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapA.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapD.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapC.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//        GlStateManager.popMatrix();
        matStack.pop();
    }
}
