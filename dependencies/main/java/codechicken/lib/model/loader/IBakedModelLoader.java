package codechicken.lib.model.loader;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by covers1624 on 7/25/2016.
 * <p/>
 * This defines a sub bakery of CCBakedModelLoader
 * See EnderStorage's EnderStorageBakedModelProvider for a "Default" implementation.
 */
@Deprecated
public interface IBakedModelLoader {

    /**
     * This defines a Mod Key provider.
     */
    @Deprecated
    interface IModKeyProvider {

        /**
         * Gets the mod to operate for.
         *
         * @return The mod id used in your Item.setRegistryName() CAPS SENSITIVE.
         */
        String getMod();

        /**
         * Creates a unique key for your item that you can use to create models for.
         * Return something like "key1=value1,key2=value2,key3=value3" but can realistically be what ever you want.
         *
         * @param stack Stack to create a key for.
         * @return The key created, null if the item is not supported.
         */
        String createKey(ItemStack stack);

        /**
         * Creates a unique key for your BlockState that you can use to create models for.
         * Return something like "key1=value1,key2=value2,key3=value3" but can realistically be what ever you want.
         * <p>
         * This should be used to create a key for the BlockState, handle the face culling in the model you return!
         *
         * @param state IBlockState to create a key for.
         * @return The key created, null if IBlockState is not supported.
         */
        String createKey(IBlockState state);
    }

    /**
     * Used to associate a IModKeyProvider with a IBakedModelLoader.
     *
     * @return Your key provider.
     */
    IModKeyProvider createKeyProvider();

    /**
     * Any textures you want added to the texture map.
     *
     * @param builder The Builder to add textures to.
     */
    void addTextures(ImmutableList.Builder<ResourceLocation> builder);

    /**
     * Uses the key provided from the IModKeyProvider to bake a model from your unique key.
     * Form your key you should have some sort of indication of what exactly you are baking.
     * <p/>
     * If it is a "In World" model then it can just be a simple wrapper as the override model just calls getQuads.
     *
     * @param key Your Unique key.
     * @return The baked model created, null if it cannot for some reason.
     */
    IBakedModel bakeModel(String key);

}
