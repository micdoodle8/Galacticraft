package codechicken.lib.render.item;

import codechicken.lib.asm.ObfMapping;
import codechicken.lib.render.state.GlStateManagerHelper;
import codechicken.lib.util.ReflectionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

/**
 * Created by covers1624 on 17/10/2016.
 * TODO, Generify how this works. might be different from vanilla in the end but we should probably try and sniff off if the model is ours at an earlier date.
 * TODO, Some sort of registry over IMC for mods to add their own hooks to this.
 */
public class CCRenderItem extends RenderItem {

    private final RenderItem parent;
    private static CCRenderItem instance;
    private static boolean hasInit;

    //Because forge has this private.
    private static final Matrix4f flipX;

    static {
        flipX = new Matrix4f();
        flipX.setIdentity();
        flipX.m00 = -1;
    }

    public CCRenderItem(RenderItem renderItem) {
        super(renderItem.textureManager, renderItem.itemModelMesher.getModelManager(), renderItem.itemColors);
        ReflectionManager.setField(new ObfMapping("net/minecraft/client/renderer/RenderItem", "field_175059_m", ""), renderItem, renderItem.itemModelMesher);
        this.parent = renderItem;
    }

    public static void init() {
        if (!hasInit) {
            instance = new CCRenderItem(Minecraft.getMinecraft().getRenderItem());
            ObfMapping mapping = new ObfMapping("net/minecraft/client/Minecraft", "field_175621_X", "");
            ReflectionManager.setField(mapping, Minecraft.getMinecraft(), instance);
            hasInit = true;
        }
    }

    public static CCRenderItem instance() {
        init();
        return instance;
    }

    @Override
    public void renderItem(ItemStack stack, IBakedModel model) {
        if (stack != null && model instanceof IItemRenderer) {
            IItemRenderer renderer = (IItemRenderer) model;
            boolean shouldHandleRender = true;
            try {//Catch AME's from new method.
                shouldHandleRender = true;//renderer.shouldHandleRender(stack);
            } catch (AbstractMethodError ignored) {
            }
            if (shouldHandleRender) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                GlStateManagerHelper.pushState();
                renderer.renderItem(stack);
                GlStateManagerHelper.popState();
                GlStateManager.popMatrix();
                return;
            }
        }
        parent.renderItem(stack, model);
    }

    private IBakedModel handleTransforms(ItemStack stack, IBakedModel model, TransformType transformType, boolean isLeftHand) {
        if (model instanceof IMatrixTransform) {
            ((IMatrixTransform) model).getTransform(transformType, isLeftHand).glApply();
        } else if (model instanceof IGLTransform) {
            ((IGLTransform) model).applyTransforms(transformType, isLeftHand);
        } else if (model instanceof IPerspectiveAwareModel) {
            model = ForgeHooksClient.handleCameraTransforms(model, transformType, isLeftHand);
        } else if (model instanceof IStackPerspectiveAwareModel) {
            Pair<? extends IBakedModel, Matrix4f> pair = ((IStackPerspectiveAwareModel) model).handlePerspective(stack, transformType);

            if (pair.getRight() != null) {
                Matrix4f matrix = new Matrix4f(pair.getRight());
                if (isLeftHand) {
                    matrix.mul(flipX, matrix);
                    matrix.mul(matrix, flipX);
                }
                ForgeHooksClient.multiplyCurrentGlMatrix(matrix);
            }
            return pair.getLeft();
        }
        return model;
    }

    private boolean isValidModel(IBakedModel model) {
        return model instanceof IItemRenderer || model instanceof IGLTransform || model instanceof IMatrixTransform || model instanceof IStackPerspectiveAwareModel;
    }

    @Override
    public void renderItemModel(ItemStack stack, IBakedModel bakedModel, TransformType transform, boolean leftHanded) {
        if (stack.getItem() != null) {
            if (isValidModel(bakedModel)) {
                this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.pushMatrix();

                bakedModel = handleTransforms(stack, bakedModel, transform, leftHanded);

                this.renderItem(stack, bakedModel);
                GlStateManager.cullFace(GlStateManager.CullFace.BACK);
                GlStateManager.popMatrix();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
                this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
            } else {
                parent.zLevel = this.zLevel;
                parent.renderItemModel(stack, bakedModel, transform, leftHanded);
            }
        }
    }

    @Override
    public void renderItemModelIntoGUI(ItemStack stack, int x, int y, IBakedModel bakedModel) {
        if (isValidModel(bakedModel)) {
            GlStateManager.pushMatrix();
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.setupGuiTransform(x, y, bakedModel.isGui3d());

            bakedModel = handleTransforms(stack, bakedModel, ItemCameraTransforms.TransformType.GUI, false);

            this.renderItem(stack, bakedModel);
            GlStateManager.disableAlpha();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        } else {
            parent.zLevel = this.zLevel;
            parent.renderItemModelIntoGUI(stack, x, y, bakedModel);
        }
    }

    // region Other Overrides

    @Override
    public void renderItem(ItemStack stack, TransformType cameraTransformType) {
        if (stack != null) {
            IBakedModel bakedModel = this.getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null);
            if (isValidModel(bakedModel)) {
                this.renderItemModel(stack, bakedModel, cameraTransformType, false);
            }
            parent.zLevel = this.zLevel;
            parent.renderItem(stack, cameraTransformType);
        }
    }

    @Override
    public void renderItem(ItemStack stack, EntityLivingBase livingBase, TransformType transform, boolean leftHanded) {
        if (stack != null && livingBase != null && stack.getItem() != null) {
            IBakedModel bakedModel = this.getItemModelWithOverrides(stack, livingBase.worldObj, livingBase);
            if (isValidModel(bakedModel)) {
                this.renderItemModel(stack, bakedModel, transform, leftHanded);
            } else {
                parent.zLevel = this.zLevel;
                parent.renderItem(stack, livingBase, transform, leftHanded);
            }
        }
    }

    @Override
    public void renderItemIntoGUI(ItemStack stack, int x, int y) {
        IBakedModel bakedModel = this.getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null);
        if (isValidModel(bakedModel)) {
            this.renderItemModelIntoGUI(stack, x, y, bakedModel);
        } else {
            parent.zLevel = this.zLevel;
            parent.renderItemIntoGUI(stack, x, y);
        }
    }

    @Override
    public void renderItemAndEffectIntoGUI(ItemStack stack, int xPosition, int yPosition) {
        this.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().thePlayer, stack, xPosition, yPosition);
    }

    @Override
    public void renderItemAndEffectIntoGUI(@Nullable EntityLivingBase livingBase, final ItemStack stack, int x, int y) {
        if (stack != null && stack.getItem() != null) {
            try {

                IBakedModel model = this.getItemModelWithOverrides(stack, (World) null, livingBase);
                if (isValidModel(model)) {
                    this.zLevel += 50.0F;
                    this.renderItemModelIntoGUI(stack, x, y, model);
                    this.zLevel -= 50.0F;
                } else {
                    parent.zLevel = this.zLevel;
                    parent.renderItemAndEffectIntoGUI(livingBase, stack, x, y);
                }

            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", new ICrashReportDetail<String>() {
                    public String call() throws Exception {
                        return String.valueOf((Object) stack.getItem());
                    }
                });
                crashreportcategory.setDetail("Item Aux", new ICrashReportDetail<String>() {
                    public String call() throws Exception {
                        return String.valueOf(stack.getMetadata());
                    }
                });
                crashreportcategory.setDetail("Item NBT", new ICrashReportDetail<String>() {
                    public String call() throws Exception {
                        return String.valueOf((Object) stack.getTagCompound());
                    }
                });
                crashreportcategory.setDetail("Item Foil", new ICrashReportDetail<String>() {
                    public String call() throws Exception {
                        return String.valueOf(stack.hasEffect());
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    // endregion

    // region parentOverrides
    @Override
    public void registerItems() {
        //We don't want to register any more items as we are just a wrapper.
    }

    @Override
    public void registerItem(Item item, int subType, String identifier) {
        //Pass this through because why not.
        parent.registerItem(item, subType, identifier);
    }

    @Override
    public void isNotRenderingEffectsInGUI(boolean isNot) {
        parent.isNotRenderingEffectsInGUI(isNot);
    }

    @Override
    public ItemModelMesher getItemModelMesher() {
        return parent.getItemModelMesher();
    }

    @Override
    public boolean shouldRenderItemIn3D(ItemStack stack) {
        return parent.shouldRenderItemIn3D(stack);
    }

    @Override
    public IBakedModel getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn) {
        return parent.getItemModelWithOverrides(stack, worldIn, entitylivingbaseIn);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        parent.onResourceManagerReload(resourceManager);
    }

    //endregion
}
