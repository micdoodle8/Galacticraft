package codechicken.nei;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.util.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;

//import net.minecraft.entity.boss.BossStatus;

public class SpawnerRenderer implements IItemRenderer, IPerspectiveAwareModel {
    public static void load(ItemMobSpawner item) {
        ModelRegistryHelper.registerItemRenderer(item, new SpawnerRenderer());
    }

    public void renderItem(ItemStack stack) {
        int meta = stack.getItemDamage();

        if (meta == 0) {
            meta = ItemMobSpawner.idPig;
        }

        //String bossName = BossStatus.bossName;
        //int bossTimeout = BossStatus.statusBarTime;
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.theWorld;

        IBakedModel baseModel = mc.getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation("mob_spawner"));
        GlStateManager.pushMatrix();
        GlStateManager.translate(.5, .5, .5);
        GlStateManager.scale(2, 2, 2);
        mc.getRenderItem().renderItem(stack, baseModel);
        GlStateManager.popMatrix();

        try {
            Entity entity = ItemMobSpawner.getEntity(meta);
            entity.setWorld(world);
            float scale = 0.6F / Math.max(entity.height, entity.width);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5, 0.4, 0.5);
            GlStateManager.rotate((float) (ClientUtils.getRenderTime() * 10), 0, 1, 0);
            GlStateManager.rotate(-20, 1, 0, 0);
            GlStateManager.translate(0, -0.4, 0);
            GlStateManager.scale(scale, scale, scale);
            entity.setLocationAndAngles(0, 0, 0, 0, 0);
            mc.getRenderManager().doRenderEntity(entity, 0, 0, 0, 0, 0, false);
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();

            GlStateManager.enableRescaleNormal();
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        } catch (Exception e) {
            if (Tessellator.getInstance().getBuffer().isDrawing) {
                Tessellator.getInstance().draw();
            }
        }
        //BossStatus.bossName = bossName;
        //BossStatus.statusBarTime = bossTimeout;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        return new ArrayList<BakedQuad>();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return TextureUtils.getBlockTexture("mob_spawner");
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        return MapWrapper.handlePerspective(this, TransformUtils.DEFAULT_BLOCK.getTransforms(), cameraTransformType);
    }
}
