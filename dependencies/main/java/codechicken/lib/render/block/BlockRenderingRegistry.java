package codechicken.lib.render.block;

import codechicken.lib.model.SimpleBakedBlockModel;
import codechicken.lib.render.buffer.BakingVertexBuffer;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.ReflectionManager;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by covers1624 on 8/09/2016.
 * TODO Start phase out of this system.
 */
public class BlockRenderingRegistry {

    private static final Map<EnumBlockRenderType, ICCBlockRenderer> blockRendererList = new HashMap<EnumBlockRenderType, ICCBlockRenderer>();
    private static final ImmutableList<EnumBlockRenderType> vanillaRenderTypes = ImmutableList.copyOf(EnumBlockRenderType.values());

    private static boolean initialized = false;

    static {
        init();
    }

    public static void init() {
        if (!initialized) {
            Minecraft mc = Minecraft.getMinecraft();
            BlockRendererDispatcher parentDispatcher = mc.getBlockRendererDispatcher();
            CCBlockRendererDispatcher newDispatcher = new CCBlockRendererDispatcher(parentDispatcher, mc.getBlockColors());

            Field field = ReflectionHelper.findField(Minecraft.class, "blockRenderDispatcher", "field_175618_aM", "aR");
            ReflectionManager.set(field, mc, newDispatcher);

            TextureUtils.addIconRegister(newDispatcher);

            initialized = true;
        }
    }

    public static EnumBlockRenderType createRenderType(String name) {
        return EnumHelper.addEnum(EnumBlockRenderType.class, name, new Class[0]);
    }

    public static boolean canHandle(EnumBlockRenderType type) {
        return blockRendererList.containsKey(type);
    }

    public static void registerRenderer(EnumBlockRenderType type, ICCBlockRenderer renderer) {
        if (vanillaRenderTypes.contains(type)) {
            throw new IllegalArgumentException("Invalid EnumBlockRenderType! " + type.name());
        }
        if (blockRendererList.containsKey(type)) {
            throw new IllegalArgumentException("Unable to register duplicate render type!" + type.name());
        }
        blockRendererList.put(type, renderer);
    }

    static void renderBlockDamage(IBlockAccess world, BlockPos pos, IBlockState state, TextureAtlasSprite sprite) {
        ICCBlockRenderer renderer = blockRendererList.get(state.getRenderType());
        if (renderer != null) {
            state = state.getActualState(world, pos);
            //TODO This needs to be optimized, probably not the most efficient thing in the world..
            VertexBuffer parent = Tessellator.getInstance().getBuffer();
            BakingVertexBuffer buffer = BakingVertexBuffer.create();
            buffer.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
            buffer.begin(7, parent.getVertexFormat());
            renderer.handleRenderBlockDamage(world, pos, state, sprite, buffer);
            buffer.finishDrawing();
            BlockModelRenderer modelRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer();
            modelRenderer.renderModel(world, new SimpleBakedBlockModel(buffer.bake()), state, pos, parent, true);
        }
    }

    static boolean renderBlock(IBlockAccess world, BlockPos pos, IBlockState state, VertexBuffer buffer) {
        ICCBlockRenderer renderer = blockRendererList.get(state.getRenderType());
        if (renderer != null) {
            return renderer.renderBlock(world, pos, state, buffer);
        }
        return false;
    }

    static void renderBlockBrightness(IBlockState state, float brightness) {
        ICCBlockRenderer renderer = blockRendererList.get(state.getRenderType());
        if (renderer != null) {
            renderer.renderBrightness(state, brightness);
        }
    }

    static void registerTextures(TextureMap map) {
        for (ICCBlockRenderer renderer : blockRendererList.values()) {
            renderer.registerTextures(map);
        }
    }
}
