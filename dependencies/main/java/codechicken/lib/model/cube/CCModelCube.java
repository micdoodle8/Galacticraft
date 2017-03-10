package codechicken.lib.model.cube;

import codechicken.lib.model.BakedModelProperties;
import codechicken.lib.model.bakedmodels.PerspectiveAwareLayeredModelWrapper;
import codechicken.lib.texture.TextureUtils;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by covers1624 on 19/11/2016.
 * TODO Document that this exists and what it does to jsons.
 */
public class CCModelCube implements IModel, IRetexturableModel, IModelSimpleProperties {

    public static String[] layerNames = { "solid", "cutout_mipped", "cutout", "translucent" };
    public static Map<String, BlockRenderLayer> nameToLayer = new HashMap<String, BlockRenderLayer>() {
        {
            put("solid", BlockRenderLayer.SOLID);
            put("cutout_mipped", BlockRenderLayer.CUTOUT_MIPPED);
            put("cutout", BlockRenderLayer.CUTOUT);
            put("translucent", BlockRenderLayer.TRANSLUCENT);
        }
    };

    public static CCModelCube INSTANCE = new CCModelCube();

    private final ImmutableMap<BlockRenderLayer, Map<EnumFacing, String>> layerFaceSpriteMap;
    private boolean isAO;
    private boolean gui3d;

    public CCModelCube() {
        this(new HashMap<BlockRenderLayer, Map<EnumFacing, String>>());
    }

    public CCModelCube(Map<BlockRenderLayer, Map<EnumFacing, String>> layerFaceSpriteMap) {
        this.layerFaceSpriteMap = ImmutableMap.copyOf(layerFaceSpriteMap);
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.singletonList(new ResourceLocation("minecraft:block/cube"));
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        List<ResourceLocation> textures = new ArrayList<ResourceLocation>();
        for (Entry<BlockRenderLayer, Map<EnumFacing, String>> layerEnrty : layerFaceSpriteMap.entrySet()) {
            for (Entry<EnumFacing, String> faceEntry : layerEnrty.getValue().entrySet()) {
                textures.add(new ResourceLocation(faceEntry.getValue()));
            }
        }
        return ImmutableList.copyOf(textures);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {

        Map<BlockRenderLayer, IBakedModel> layerModelMap = new HashMap<BlockRenderLayer, IBakedModel>();
        TextureAtlasSprite particle = TextureUtils.getMissingSprite();
        for (Entry<BlockRenderLayer, Map<EnumFacing, String>> layerEntry : layerFaceSpriteMap.entrySet()) {
            Map<String, String> kvTextures = new HashMap<String, String>();
            for (Entry<EnumFacing, String> faceEntry : layerEntry.getValue().entrySet()) {
                if (faceEntry.getKey() == null) {
                    kvTextures.put("particle", faceEntry.getValue());
                } else {
                    kvTextures.put(faceEntry.getKey().getName(), faceEntry.getValue());
                }
            }
            IModel vanillaModel = ModelLoaderRegistry.getModelOrLogError(new ResourceLocation("minecraft:block/cube"), "Unable to get vanilla model wrapper..");
            vanillaModel = ModelProcessingHelper.retexture(vanillaModel, ImmutableMap.copyOf(addMissing(kvTextures)));
            IBakedModel model = vanillaModel.bake(state, format, bakedTextureGetter);

            if (layerEntry.getKey() == BlockRenderLayer.SOLID) {
                particle = model.getParticleTexture();
            }

            layerModelMap.put(layerEntry.getKey(), model);
        }

        return new PerspectiveAwareLayeredModelWrapper(layerModelMap, state, new BakedModelProperties(isAO, gui3d, particle));
    }

    private static Map<String, String> addMissing(Map<String, String> textures) {
        Map<String, String> newTextures = new HashMap<String, String>(textures);
        for (EnumFacing face : EnumFacing.VALUES) {
            if (!textures.containsKey(face.getName())) {
                newTextures.put("#" + face.getName(), "");
            }
        }
        return newTextures;
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures) {
        Map<BlockRenderLayer, Map<EnumFacing, String>> layerFaceSpriteMap = new HashMap<BlockRenderLayer, Map<EnumFacing, String>>();

        for (Entry<String, String> entry : textures.entrySet()) {
            EnumFacing face = getFaceForKey(entry.getKey());
            BlockRenderLayer layer = getLayerForTexKey(entry.getKey());
            Map<EnumFacing, String> faceMap = layerFaceSpriteMap.get(layer);
            if (faceMap == null) {
                faceMap = new HashMap<EnumFacing, String>();
                layerFaceSpriteMap.put(layer, faceMap);
            }
            faceMap.put(face, entry.getValue());
        }

        return new CCModelCube(layerFaceSpriteMap);
    }

    private static BlockRenderLayer getLayerForTexKey(String key) {
        String layerName = "solid";
        for (String l : layerNames) {
            if (key.endsWith(l)) {
                layerName = l;
            }
        }
        return nameToLayer.get(layerName);
    }

    public static EnumFacing getFaceForKey(String key) {
        String faceName = key;
        if (key.contains("_")) {
            faceName = key.substring(0, key.indexOf("_"));
        }
        return EnumFacing.byName(faceName);
    }

    @Override
    public IModel smoothLighting(boolean value) {
        this.isAO = value;
        return this;
    }

    @Override
    public IModel gui3d(boolean value) {
        this.gui3d = value;
        return this;
    }
}
