package codechicken.lib.model.modelbase;

import codechicken.lib.render.CCModel;
import codechicken.lib.vec.uv.UV;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by covers1624 on 8/24/2016.
 * TODO, instead of just copying how vanillas works, create a wrapper that pulls data from existing ModelBases and allows some sort of Animation callback.
 */
public abstract class CCModelBase {

    public float swingProgress;
    public boolean isRiding;
    public boolean isChild = true;
    public List<CCModelRenderer> boxList = Lists.newArrayList();
    private final Map<String, UV> modelTextureMap = Maps.newHashMap();
    public int textureWidth = 64;
    public int textureHeight = 32;

    public CCModelRenderer getRandomModelBox(Random rand) {
        return this.boxList.get(rand.nextInt(this.boxList.size()));
    }

    protected void setTextureOffset(String partName, int x, int y) {
        this.modelTextureMap.put(partName, new UV(x, y));
    }

    public UV getTextureOffset(String partName) {
        return this.modelTextureMap.get(partName);
    }

    public static void copyModelAngles(CCModelRenderer source, CCModelRenderer dest) {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
        dest.rotationPointX = source.rotationPointX;
        dest.rotationPointY = source.rotationPointY;
        dest.rotationPointZ = source.rotationPointZ;
    }

    public void setModelAttributes(CCModelBase model) {
        this.swingProgress = model.swingProgress;
        this.isRiding = model.isRiding;
        this.isChild = model.isChild;
    }

    public static void renderModels(float scale, VertexBuffer buffer, TextureAtlasSprite sprite, CCModelRenderer... modelRenders) {
        LinkedList<CCModel> models = new LinkedList<CCModel>();
        for (CCModelRenderer modelRenderer : modelRenders) {
            models.add(modelRenderer.bake(scale));
        }
        CCModel model = CCModel.combine(models);

        //TODO
        //CCRenderState.bind(buffer);
        //boolean startDrawing = !CCRenderState.isDrawing();
        //if (startDrawing) {
        //    CCRenderState.startDrawing(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        //}
        //model.render();
        //if (startDrawing) {
        //    CCRenderState.draw();
        //}
    }
}
