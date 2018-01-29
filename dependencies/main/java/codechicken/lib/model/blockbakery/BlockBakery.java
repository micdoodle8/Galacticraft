package codechicken.lib.model.blockbakery;

import codechicken.lib.model.BakedModelProperties;
import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.model.ModelRegistryHelper.IModelBakeCallback;
import codechicken.lib.model.PerspectiveAwareModelProperties;
import codechicken.lib.model.bakedmodels.PerspectiveAwareBakedModel;
import codechicken.lib.model.bakedmodels.PerspectiveAwareLayeredModel;
import codechicken.lib.model.bakery.PlanarFaceBakery;
import codechicken.lib.texture.IItemBlockTextureProvider;
import codechicken.lib.texture.IWorldBlockTextureProvider;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * Created by covers1624 on 25/10/2016.
 */
//TODO, We need a hook for CCRenderItem to allow switching the model being rendered on ItemOverrideList.handleItemState, possibly an interface.
//TODO, BlockBakery > CachedModelBakery? dunno.
@SideOnly (Side.CLIENT)
public class BlockBakery implements IResourceManagerReloadListener {

    private static boolean DEBUG = Boolean.parseBoolean(System.getProperty("ccl.debugBakeryLogging"));

    private static Cache<String, IBakedModel> keyModelCache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).build();

    private static Map<Item, IItemStackKeyGenerator> itemKeyGeneratorMap = new HashMap<Item, IItemStackKeyGenerator>();
    private static Map<Block, IBlockStateKeyGenerator> blockKeyGeneratorMap = new HashMap<Block, IBlockStateKeyGenerator>();
    private static IBakedModel missingModel;

    public static final IBlockStateKeyGenerator defaultBlockKeyGenerator = new IBlockStateKeyGenerator() {
        @Override
        public String generateKey(IExtendedBlockState state) {
            if (state.getBlock() instanceof IWorldBlockTextureProvider) {
                Map<BlockRenderLayer, Map<EnumFacing, TextureAtlasSprite>> layerFaceSpriteMap = state.getValue(BlockBakeryProperties.LAYER_FACE_SPRITE_MAP);
                StringBuilder builder = new StringBuilder(state.getBlock().getRegistryName() + ",");
                for (Entry<BlockRenderLayer, Map<EnumFacing, TextureAtlasSprite>> layerEntry : layerFaceSpriteMap.entrySet()) {
                    builder.append(layerEntry.getKey().toString()).append(",");
                    for (Entry<EnumFacing, TextureAtlasSprite> faceSpriteEntry : layerEntry.getValue().entrySet()) {
                        builder.append(faceSpriteEntry.getKey()).append(",").append(faceSpriteEntry.getValue().getIconName()).append(",");
                    }
                }
                return builder.toString();
            }
            return state.getBlock().getRegistryName().toString() + "|" + state.getBlock().getMetaFromState(state);
        }
    };

    public static final IItemStackKeyGenerator defaultItemKeyGenerator = new IItemStackKeyGenerator() {
        @Override
        public String generateKey(ItemStack stack) {
            return stack.getItem().getRegistryName().toString() + "|" + stack.getMetadata();
        }
    };

    public static void init() {
        TextureUtils.registerReloadListener(new BlockBakery());
        ModelRegistryHelper.registerCallback(new IModelBakeCallback() {
            @Override
            public void onModelBake(IRegistry<ModelResourceLocation, IBakedModel> modelRegistry) {
                missingModel = ModelLoaderRegistry.getMissingModel().bake(TransformUtils.DEFAULT_BLOCK, DefaultVertexFormats.ITEM, TextureUtils.bakedTextureGetter);
            }
        });
    }

    public static IBlockStateKeyGenerator getKeyGenerator(Block block) {
        if (blockKeyGeneratorMap.containsKey(block)) {
            return blockKeyGeneratorMap.get(block);
        }
        return defaultBlockKeyGenerator;
    }

    public static IItemStackKeyGenerator getKeyGenerator(Item item) {
        if (itemKeyGeneratorMap.containsKey(item)) {
            return itemKeyGeneratorMap.get(item);
        }
        return defaultItemKeyGenerator;
    }

    public static void registerBlockKeyGenerator(Block block, IBlockStateKeyGenerator generator) {
        if (blockKeyGeneratorMap.containsKey(block)) {
            throw new IllegalArgumentException("Unable to register IBlockStateKeyGenerator as one is already registered for block:" + block.getRegistryName());
        }
        blockKeyGeneratorMap.put(block, generator);
    }

    public static void registerItemKeyGenerator(Item item, IItemStackKeyGenerator generator) {
        if (itemKeyGeneratorMap.containsKey(item)) {
            throw new IllegalArgumentException("Unable to register IItemStackKeyGenerator as one is already registered for item: " + item.getRegistryName());
        }
        itemKeyGeneratorMap.put(item, generator);
    }

    //TODO, Move this to pass IExtendedBlockState, IBlockAccess, BlockPos. Leave tile usage to
    public static IBlockState handleExtendedState(IExtendedBlockState state, TileEntity tileEntity) {
        Block block = state.getBlock();

        if (block instanceof IBakeryBlock) {
            return ((IBakeryBlock) block).getCustomBakery().handleState(state, tileEntity);
        } else if (block instanceof IWorldBlockTextureProvider) {
            IWorldBlockTextureProvider provider = ((IWorldBlockTextureProvider) block);
            Map<BlockRenderLayer, Map<EnumFacing, TextureAtlasSprite>> layerFaceSpriteMap = new HashMap<BlockRenderLayer, Map<EnumFacing, TextureAtlasSprite>>();
            for (BlockRenderLayer layer : BlockRenderLayer.values()) {
                if (block.canRenderInLayer(state, layer)) {
                    Map<EnumFacing, TextureAtlasSprite> faceSpriteMap = new HashMap<EnumFacing, TextureAtlasSprite>();
                    for (EnumFacing face : EnumFacing.VALUES) {
                        TextureAtlasSprite sprite = provider.getTexture(face, state, layer, tileEntity.getWorld(), tileEntity.getPos());
                        if (sprite != null) {
                            faceSpriteMap.put(face, sprite);
                        }
                    }
                    layerFaceSpriteMap.put(layer, faceSpriteMap);
                }
            }
            state = state.withProperty(BlockBakeryProperties.LAYER_FACE_SPRITE_MAP, layerFaceSpriteMap);
        }
        return state;
    }

    public static IBakedModel getCachedItemModel(ItemStack stack) {
        IBakedModel model;
        IItemStackKeyGenerator generator = getKeyGenerator(stack.getItem());
        String key = generator.generateKey(stack);
        model = keyModelCache.getIfPresent(key);
        if (model == null) {
            model = generateItemModel(stack);
            if (DEBUG) {
                FMLLog.info("Baking item model: " + key);
            }
            if (model != missingModel) {
                keyModelCache.put(key, model);
            }
        }
        return model;

    }

    public static IBakedModel generateItemModel(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock) {
            Block block = Block.getBlockFromItem(item);
            if (block instanceof IBakeryBlock) {
                ICustomBlockBakery bakery = ((IBakeryBlock) block).getCustomBakery();
                List<BakedQuad> generalQuads = new LinkedList<BakedQuad>();
                Map<EnumFacing, List<BakedQuad>> faceQuads = new HashMap<EnumFacing, List<BakedQuad>>();
                generalQuads.addAll(bakery.bakeItemQuads(null, stack));

                for (EnumFacing face : EnumFacing.VALUES) {
                    List<BakedQuad> quads = new LinkedList<BakedQuad>();

                    quads.addAll(PlanarFaceBakery.shadeQuadFaces(bakery.bakeItemQuads(face, stack)));

                    faceQuads.put(face, quads);
                }

                BakedModelProperties properties = new BakedModelProperties(true, true, null);
                return new PerspectiveAwareBakedModel(faceQuads, generalQuads, TransformUtils.DEFAULT_BLOCK, properties);

            } else if (block instanceof IItemBlockTextureProvider) {
                IItemBlockTextureProvider provider = ((IItemBlockTextureProvider) block);
                Map<EnumFacing, List<BakedQuad>> faceQuadMap = new HashMap<EnumFacing, List<BakedQuad>>();
                for (EnumFacing face : EnumFacing.VALUES) {
                    List<BakedQuad> faceQuads = new LinkedList<BakedQuad>();

                    faceQuads.addAll(PlanarFaceBakery.shadeQuadFaces(PlanarFaceBakery.bakeFace(face, provider.getTexture(face, stack), DefaultVertexFormats.ITEM)));

                    faceQuadMap.put(face, faceQuads);
                }
                BakedModelProperties properties = new BakedModelProperties(true, true, null);
                return new PerspectiveAwareBakedModel(faceQuadMap, TransformUtils.DEFAULT_BLOCK, properties);
            }
        } else {
            if (item instanceof IBakeryItem) {
                IItemBakery bakery = ((IBakeryItem) item).getBakery();
                List<BakedQuad> generalQuads = new LinkedList<BakedQuad>();
                Map<EnumFacing, List<BakedQuad>> faceQuads = new HashMap<EnumFacing, List<BakedQuad>>();
                generalQuads.addAll(bakery.bakeItemQuads(null, stack));

                for (EnumFacing face : EnumFacing.VALUES) {
                    List<BakedQuad> quads = new LinkedList<BakedQuad>();

                    quads.addAll(bakery.bakeItemQuads(face, stack));

                    faceQuads.put(face, quads);
                }

                PerspectiveAwareModelProperties bakeryProperties = PerspectiveAwareModelProperties.DEFAULT_ITEM;

                try {
                    bakeryProperties = bakery.getModelProperties(stack);
                } catch (Exception ignored) {
                }

                BakedModelProperties properties = bakeryProperties.getProperties();
                return new PerspectiveAwareBakedModel(faceQuads, generalQuads, bakeryProperties.getModelState(), properties);
            }
        }
        return missingModel;
    }

    public static IBakedModel getCachedModel(IExtendedBlockState state) {
        IBakedModel model;
        IBlockStateKeyGenerator keyGenerator = getKeyGenerator(state.getBlock());
        String key = keyGenerator.generateKey(state);
        model = keyModelCache.getIfPresent(key);
        if (model == null) {
            model = generateModel(state);
            if (DEBUG) {
                FMLLog.info("Baking block model: " + key);
            }
            if (model != missingModel) {
                keyModelCache.put(key, model);
            }
        }
        return model;
    }

    public static IBakedModel generateModel(IExtendedBlockState state) {
        if (state.getBlock() instanceof IBakeryBlock) {
            ICustomBlockBakery bakery = ((IBakeryBlock) state.getBlock()).getCustomBakery();
            if (bakery instanceof ISimpleBlockBakery) {
                ISimpleBlockBakery simpleBakery = ((ISimpleBlockBakery) bakery);
                List<BakedQuad> generalQuads = new LinkedList<BakedQuad>();
                Map<EnumFacing, List<BakedQuad>> faceQuads = new HashMap<EnumFacing, List<BakedQuad>>();
                generalQuads.addAll(simpleBakery.bakeQuads(null, state));

                for (EnumFacing face : EnumFacing.VALUES) {
                    List<BakedQuad> quads = new LinkedList<BakedQuad>();

                    quads.addAll(simpleBakery.bakeQuads(face, state));

                    faceQuads.put(face, quads);
                }
                BakedModelProperties properties = new BakedModelProperties(true, true, null);
                return new PerspectiveAwareBakedModel(faceQuads, generalQuads, TransformUtils.DEFAULT_BLOCK, properties);
            }
            if (bakery instanceof ILayeredBlockBakery) {
                ILayeredBlockBakery layeredBakery = ((ILayeredBlockBakery) bakery);
                Map<BlockRenderLayer, Map<EnumFacing, List<BakedQuad>>> layerFaceQuadMap = new HashMap<BlockRenderLayer, Map<EnumFacing, List<BakedQuad>>>();
                Map<BlockRenderLayer, List<BakedQuad>> layerGeneralQuads = new HashMap<BlockRenderLayer, List<BakedQuad>>();
                for (BlockRenderLayer layer : BlockRenderLayer.values()) {
                    if (state.getBlock().canRenderInLayer(state, layer)) {
                        LinkedList<BakedQuad> quads = new LinkedList<BakedQuad>();
                        quads.addAll(layeredBakery.bakeLayerFace(null, layer, state));
                        layerGeneralQuads.put(layer, quads);
                    }
                }

                for (BlockRenderLayer layer : BlockRenderLayer.values()) {
                    if (state.getBlock().canRenderInLayer(state, layer)) {
                        Map<EnumFacing, List<BakedQuad>> faceQuadMap = new HashMap<EnumFacing, List<BakedQuad>>();
                        for (EnumFacing face : EnumFacing.VALUES) {
                            List<BakedQuad> quads = new LinkedList<BakedQuad>();
                            quads.addAll(layeredBakery.bakeLayerFace(face, layer, state));
                            faceQuadMap.put(face, quads);
                        }
                        layerFaceQuadMap.put(layer, faceQuadMap);
                    }
                }
                BakedModelProperties properties = new BakedModelProperties(true, true, null);
                return new PerspectiveAwareLayeredModel(layerFaceQuadMap, layerGeneralQuads, TransformUtils.DEFAULT_BLOCK, properties);
            }
        }
        if (state.getBlock() instanceof IWorldBlockTextureProvider) {
            Map<BlockRenderLayer, Map<EnumFacing, List<BakedQuad>>> layerFaceQuadMap = generateLayerFaceQuadMap(state);
            BakedModelProperties properties = new BakedModelProperties(true, true, null);
            return new PerspectiveAwareLayeredModel(layerFaceQuadMap, TransformUtils.DEFAULT_BLOCK, properties);
        } /*else if (state.getBlock() instanceof IBlockTextureProvider) {
            IBlockTextureProvider provider = ((IBlockTextureProvider) state.getBlock());
            int meta = state.getBlock().getMetaFromState(state);

            Map<EnumFacing, List<BakedQuad>> quadFaceMap = new HashMap<EnumFacing, List<BakedQuad>>();
            for (EnumFacing face : EnumFacing.VALUES) {
                LinkedList<BakedQuad> quads = new LinkedList<BakedQuad>();

                quads.add(PlanarFaceBakery.bakeFace(face, provider.getTexture(face, meta)));

                quadFaceMap.put(face, quads);
            }
            BakedModelProperties properties = new BakedModelProperties(true, true, null);
            return new PerspectiveAwareBakedModel(quadFaceMap, TransformUtils.DEFAULT_BLOCK, properties);

        }*/
        return missingModel;
    }

    public static Map<BlockRenderLayer, Map<EnumFacing, List<BakedQuad>>> generateLayerFaceQuadMap(IExtendedBlockState state) {
        Map<BlockRenderLayer, Map<EnumFacing, TextureAtlasSprite>> layerFaceSpriteMap = state.getValue(BlockBakeryProperties.LAYER_FACE_SPRITE_MAP);
        Map<BlockRenderLayer, Map<EnumFacing, List<BakedQuad>>> layerFaceQuadMap = new HashMap<BlockRenderLayer, Map<EnumFacing, List<BakedQuad>>>();
        for (BlockRenderLayer layer : layerFaceSpriteMap.keySet()) {
            Map<EnumFacing, TextureAtlasSprite> faceSpriteMap = layerFaceSpriteMap.get(layer);
            Map<EnumFacing, List<BakedQuad>> faceQuadMap = new HashMap<EnumFacing, List<BakedQuad>>();
            for (EnumFacing face : faceSpriteMap.keySet()) {
                List<BakedQuad> quads = new LinkedList<BakedQuad>();
                quads.add(PlanarFaceBakery.bakeFace(face, faceSpriteMap.get(face)));
                faceQuadMap.put(face, quads);
            }
            layerFaceQuadMap.put(layer, faceQuadMap);
        }
        return layerFaceQuadMap;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        nukeModelCache();
    }

    public static void nukeModelCache() {
        keyModelCache.invalidateAll();
    }
}
