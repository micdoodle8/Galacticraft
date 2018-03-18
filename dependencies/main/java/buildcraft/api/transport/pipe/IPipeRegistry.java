package buildcraft.api.transport.pipe;

import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public interface IPipeRegistry {
    PipeDefinition getDefinition(ResourceLocation identifier);

    void registerPipe(PipeDefinition definition);

    /** Maps the given {@link PipeDefinition} to an {@link IItemPipe}. This acts exactly akin to
     * {@link Map#put(Object, Object)}. */
    void setItemForPipe(PipeDefinition definition, @Nullable IItemPipe item);

    IItemPipe getItemForPipe(PipeDefinition definition);

    /** Creates an {@link IItemPipe} for the given {@link PipeDefinition}. If the {@link PipeDefinition} has been
     * registered with {@link #registerPipe(PipeDefinition)} then it will also be registered with
     * {@link #setItemForPipe(PipeDefinition, IItemPipe)}. The returned item will be automatically registered with
     * forge. */
    IItemPipe createItemForPipe(PipeDefinition definition);

    /** Identical to {@link #createItemForPipe(PipeDefinition)}, but doesn't require registering tags with buildcraft
     * lib in order to register.
     * 
     * @param postCreate A function to call in order to setup the {@link Item#setRegistryName(ResourceLocation)} and
     *            {@link Item#setUnlocalizedName(String)}. */
    IItemPipe createUnnamedItemForPipe(PipeDefinition definition, Consumer<Item> postCreate);

    Iterable<PipeDefinition> getAllRegisteredPipes();
}
