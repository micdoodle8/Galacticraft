package micdoodle8.mods.galacticraft.core.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ClientUtil
{
    /**
     * Use getClientTimeTotal() now.
     */
    @Deprecated
    public static long getMilliseconds()
    {
        return getClientTimeTotal();
    }

    public static long getClientTimeTotal()
    {
        return (long) (Minecraft.getInstance().world.getGameTime() * 66.666666666666);
    }

//    public static void addVariant(String modID, String name, String... variants)
//    {
////        Item itemBlockVariants = GameRegistry.findItem(modID, name);
//        Item itemBlockVariants = Item.REGISTRY.getObject(new ResourceLocation(modID, name));
//        ResourceLocation[] variants0 = new ResourceLocation[variants.length];
//        for (int i = 0; i < variants.length; ++i)
//        {
//            variants0[i] = new ResourceLocation(modID + ":" + variants[i]);
//        }
//        ModelBakery.registerItemVariants(itemBlockVariants, variants0);
//    }

//    public static void registerBlockJson(String texturePrefix, Block block)
//    {
//        registerBlockJson(texturePrefix, block, 0, block.getUnlocalizedName().substring(5));
//    }
//
//    public static void registerBlockJson(String texturePrefix, Block block, int meta, String name)
//    {
////        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(texturePrefix + name, "inventory"));
//        Minecraft.getInstance().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(texturePrefix + name, "inventory"));
//    }

//    public static void registerItemJson(String texturePrefix, Item item)
//    {
//        registerItemJson(texturePrefix, item, 0, item.getUnlocalizedName().substring(5));
//    }
//
//    public static void registerItemJson(String texturePrefix, Item item, String name)
//    {
////        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(texturePrefix + name, "inventory"));
//        Minecraft.getInstance().getItemRenderer().getItemModelMesher().register(item, new ModelResourceLocation(texturePrefix + name, "inventory"));
//    }

//    public static ScaledResolution getScaledRes(Minecraft minecraft, int width, int height)
//    {
//        return new ScaledResolution(minecraft);
////        return VersionUtil.getScaledRes(minecraft, width, height);
//    }

    public static FlagData updateFlagData(String playerName, boolean sendPacket)
    {
        SpaceRace race = SpaceRaceManager.getSpaceRaceFromPlayer(playerName);

        if (race != null)
        {
            return race.getFlagData();
        }
        else if (!ClientProxyCore.flagRequestsSent.contains(playerName) && sendPacket)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_REQUEST_FLAG_DATA, GCCoreUtil.getDimensionType(Minecraft.getInstance().world), new Object[]{playerName}));
            ClientProxyCore.flagRequestsSent.add(playerName);
        }

        return FlagData.DEFAULT;
    }

    public static Vector3 updateTeamColor(String playerName, boolean sendPacket)
    {
        SpaceRace race = SpaceRaceManager.getSpaceRaceFromPlayer(playerName);

        if (race != null)
        {
            return race.getTeamColor();
        }
        else if (!ClientProxyCore.flagRequestsSent.contains(playerName) && sendPacket)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_REQUEST_FLAG_DATA, GCCoreUtil.getDimensionType(Minecraft.getInstance().world), new Object[]{playerName}));
            ClientProxyCore.flagRequestsSent.add(playerName);
        }

        return new Vector3(1, 1, 1);
    }

//    public static void replaceModel(String modid, ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState, String... variants)
//    {
//        OBJModel model;
//        try
//        {
//            model = (OBJModel) OBJLoaderGC.instance.loadModel(new ResourceLocation(modid, objLoc));
//        }
//        catch (Exception e)
//        {
//            throw new RuntimeException(e);
//        }
//
//
//        Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getInstance().getTextureMapBlocks().getAtlasSprite(location.toString());
//        IBakedModel newModelBase = model.bake(new OBJModel.OBJState(visibleGroups, false, parentState), DefaultVertexFormats.ITEM, spriteFunction);
//        IBakedModel newModelAlt = null;
//        if (variants.length == 0)
//        {
//            variants = new String[] { "inventory" };
//        }
//        else if (variants.length > 1 || !variants[0].equals("inventory"))
//        {
//            newModelAlt = model.bake(new OBJModel.OBJState(visibleGroups, false, TRSRTransformation.identity()), DefaultVertexFormats.ITEM, spriteFunction);
//        }
//
//        for (String variant : variants)
//        {
//            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(modid + ":" + resLoc, variant);
//            IBakedModel object = event.getModelRegistry().getObject(modelResourceLocation);
//            if (object != null)
//            {
//                IBakedModel newModel = variant.equals("inventory") ? newModelBase : newModelAlt;
//                if (clazz != null)
//                {
//                    try
//                    {
//                        newModel = clazz.getConstructor(IBakedModel.class).newInstance(newModel);
//                    } catch (Exception e)
//                    {
//                        GCLog.severe("ItemModel constructor problem for " + modelResourceLocation);
//                        e.printStackTrace();
//                    }
//                }
//                event.getModelRegistry().putObject(modelResourceLocation, newModel);
//            }
//        } TODO models
//    }

//    public static OBJModel.OBJBakedModel modelFromOBJ(ModelLoader loader, ResourceLocation loc) throws IOException
//    {
//        return modelFromOBJ(loader, loc, ImmutableList.of("main"));
//    }
//
//    public static OBJModel.OBJBakedModel modelFromOBJ(ModelLoader loader, ResourceLocation loc, List<String> visibleGroups) throws IOException
//    {
//        return modelFromOBJ(loader, loc, visibleGroups, TRSRTransformation.identity(), ImmutableMap.of());
//    }
//
//    public static OBJModel.OBJBakedModel modelFromOBJ(ModelLoader loader, ResourceLocation loc, List<String> visibleGroups, IModelState parentState, ImmutableMap<String, String> customData) throws IOException
//    {
//        OBJModel.ModelSettings settings = new OBJModel.ModelSettings(loc, true, false, false, true, null);
//        OBJModel model = OBJLoader.INSTANCE.loadModel(settings);
//        java.util.function.Function<ResourceLocation, TextureAtlasSprite> textureGetter;
//        textureGetter = location -> Minecraft.getInstance().getAtlasSpriteGetter(location).apply(location);
//
//        IBakedModel bakedModel = model.bake(model.owner, bakery, ModelLoader.defaultTextureGetter(), model.originalTransform, model.getOverrides(), new ResourceLocation("forge:bucket_override"));
//        return (OBJModel) model.bake(loader, textureGetter, new BasicState(new OBJModel.OBJState(visibleGroups, false, parentState), false), DefaultVertexFormats.ITEM);
//    }

    public static void drawBakedModel(IBakedModel model, IRenderTypeBuffer buffer, MatrixStack mat, int light)
    {
        RenderType renderType = RenderType.getEntityTranslucent(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        IVertexBuilder builder = buffer.getBuffer(renderType);
        Random random = new Random();
        random.setSeed(42);

        MatrixStack.Entry last = mat.getLast();
        for (BakedQuad bakedquad : model.getQuads(null, null, random))
        {
            builder.addVertexData(last, bakedquad, 1.0F, 1.0F, 1.0F, 1.0F, light, OverlayTexture.NO_OVERLAY);
        }
        ((IRenderTypeBuffer.Impl) buffer).finish(renderType);
    }

    public static void drawBakedModelColored(IBakedModel model, IRenderTypeBuffer buffer, MatrixStack mat, int light, float r, float g, float b, float a)
    {
        RenderType renderType = RenderType.getEntityTranslucent(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        IVertexBuilder builder = buffer.getBuffer(renderType);
        Random random = new Random();
        random.setSeed(42);

        MatrixStack.Entry last = mat.getLast();
        for (BakedQuad bakedquad : model.getQuads(null, null, random))
        {
            builder.addVertexData(last, bakedquad, 1.0F, 1.0F, 1.0F, 1.0F, light, OverlayTexture.NO_OVERLAY);
        }
        ((IRenderTypeBuffer.Impl) buffer).finish(renderType);
    }
//
    public static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
        dest.rotationPointX = source.rotationPointX;
        dest.rotationPointY = source.rotationPointY;
        dest.rotationPointZ = source.rotationPointZ;
    }
}
