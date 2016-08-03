package codechicken.lib.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;

public class ModelRegistryHelper {
    private static List<Pair<ModelResourceLocation, IBakedModel>> registerModels = new LinkedList<Pair<ModelResourceLocation, IBakedModel>>();

    static {
        MinecraftForge.EVENT_BUS.register(new ModelRegistryHelper());
    }

    public static void register(ModelResourceLocation location, IBakedModel model) {
        registerModels.add(new ImmutablePair<ModelResourceLocation, IBakedModel>(location, model));
    }

    public static void registerItemModel(Item item, ResourceLocation location) {
        registerItemModel(item, 0, location);
    }

    public static void registerItemModel(Item item, int meta, ResourceLocation location) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(location, "inventory"));
    }

    public static void registerItemMesher(Item item, ItemMeshDefinition mesher) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, mesher);
    }

    public static void registerItemRenderer(Item item, IItemRenderer renderer, ResourceLocation location) {
        final ModelResourceLocation modelLoc = new ModelResourceLocation(location, "inventory");
        register(modelLoc, renderer);
        registerItemMesher(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return modelLoc;
            }
        });
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        for (Pair<ModelResourceLocation, IBakedModel> pair : registerModels) {
            event.modelRegistry.putObject(pair.getKey(), pair.getValue());
        }
    }
}
