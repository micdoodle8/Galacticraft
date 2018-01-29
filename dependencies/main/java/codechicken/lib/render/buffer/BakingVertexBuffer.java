package codechicken.lib.render.buffer;

import codechicken.lib.math.MathHelper;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.VectorUtils;
import codechicken.lib.util.VertexDataUtils;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.uv.UV;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by covers1624 on 4/10/2016.
 * Creates a List of BakedQuads from a VertexBuffer. That's it really..
 * //TODO, support triangles & quadulate.
 */
public class BakingVertexBuffer extends VertexBuffer {

    private HashMap<Integer, TextureAtlasSprite> spriteMap;
    private boolean useSprites = true;
    private boolean useDiffuseLighting = true;

    private static ThreadLocal<BakingVertexBuffer> threadBuffers = new ThreadLocal<BakingVertexBuffer>() {
        @Override
        protected BakingVertexBuffer initialValue() {
            return new BakingVertexBuffer(0x200000);
        }
    };

    public static BakingVertexBuffer create() {
        return threadBuffers.get();
    }

    private BakingVertexBuffer(int bufferSizeIn) {
        super(bufferSizeIn);
    }

    @Override
    public void begin(int glMode, VertexFormat format) {
        if (glMode != 7) {
            throw new IllegalArgumentException("Unable to bake GL Mode, only Quads supported! To bake triangles pipe through CCQuad then quadulate.");
        }
        super.begin(glMode, format);
    }

    @Override
    public void reset() {
        spriteMap = new HashMap<Integer, TextureAtlasSprite>();
        useSprites = true;
        useDiffuseLighting = true;
        super.reset();
    }

    /**
     * Sets the sprite for a specific vertex.
     *
     * @param sprite The sprite to set.
     */
    public BakingVertexBuffer setSprite(TextureAtlasSprite sprite) {
        spriteMap.put(MathHelper.floor(getVertexCount() / 4D), sprite);
        return this;
    }

    /**
     * Sets the baker to ignore all sprite calculations and just use the missing icon.
     * This should only be used in cases where you know the quads are not going to be transformed by another mod at any point.
     */
    public BakingVertexBuffer ignoreSprites() {
        useSprites = false;
        return this;
    }

    /**
     * Sets the baker to use any set vertex range sprite, or calculate sprites for a given UV mapping.
     * This is enabled by default.
     */
    public BakingVertexBuffer useSprites() {
        useSprites = true;
        return this;
    }

    /**
     * Disables DiffuseLighting on quads.
     */
    public BakingVertexBuffer dissableDiffuseLighting() {
        useDiffuseLighting = false;
        return this;
    }

    /**
     * Enables DiffuseLighting on quads.
     * This is enabled by default.
     */
    public BakingVertexBuffer enableDiffuseLighting() {
        useDiffuseLighting = true;
        return this;
    }

    /**
     * Bakes the data inside the VertexBuffer to a baked quad.
     *
     * @return The list of quads baked.
     */
    public List<BakedQuad> bake() {
        if (isDrawing) {
            FMLLog.log("CodeChickenLib", Level.WARN, new IllegalStateException("Bake called before finishDrawing!"), "Someone is calling bake before finishDrawing!");
            finishDrawing();
        }
        State state = getVertexState();
        VertexFormat format = state.getVertexFormat();
        if (!format.hasUvOffset(0)) {
            throw new IllegalStateException("Unable to bake format that does not have UV mappings!");
        }
        int[] rawBuffer = Arrays.copyOf(state.getRawBuffer(), state.getRawBuffer().length);

        List<BakedQuad> quads = new LinkedList<BakedQuad>();
        TextureAtlasSprite sprite = TextureUtils.getMissingSprite();

        int curr = 0;
        int next = format.getNextOffset();
        int i = 0;
        while (rawBuffer.length >= next) {
            int[] quadData = Arrays.copyOfRange(rawBuffer, curr, next);
            Vector3 normal = new Vector3();
            if (format.hasNormal()) {
                //Grab first normal.
                float[] normalData = new float[4];
                LightUtil.unpack(quadData, normalData, format, 0, VertexDataUtils.getNormalElement(format));
                normal = Vector3.fromArray(normalData);
            } else {
                //No normal provided in format, so we calculate.
                float[][] posData = new float[4][4];
                for (int v = 0; v < 4; v++) {
                    LightUtil.unpack(quadData, posData[v], format, v, VertexDataUtils.getPositionElement(format));
                }
                normal.set(VectorUtils.calculateNormal(Vector3.fromArray(posData[0]), Vector3.fromArray(posData[1]), Vector3.fromArray(posData[3])));
            }
            if (useSprites) {
                //Attempt to get sprite for vertex.
                if (spriteMap.containsKey(i)) {
                    //Use provided sprite for vertex.
                    sprite = spriteMap.get(i);
                } else {
                    //Sprite not found for vertex, so we attempt to calculate the sprite.
                    float[] uvData = new float[4];
                    LightUtil.unpack(quadData, uvData, format, 0, VertexDataUtils.getUVElement(format));
                    UV uv = new UV(uvData[0], uvData[1]);
                    sprite = VertexDataUtils.getSpriteForUV(TextureUtils.getTextureMap(), uv);
                }
            }
            //Use normal to calculate facing.
            EnumFacing facing = VectorUtils.calcNormalSide(normal);
            if (facing == null) {
                facing = EnumFacing.UP;
            }
            BakedQuad quad = new BakedQuad(quadData, -1, facing, sprite, useDiffuseLighting, format);
            quads.add(quad);
            curr = next;
            next += format.getNextOffset();
            i++;
        }
        return ImmutableList.copyOf(quads);
    }
}
