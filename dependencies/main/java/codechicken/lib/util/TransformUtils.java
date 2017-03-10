package codechicken.lib.util;

import codechicken.lib.render.CCModelState;
import codechicken.lib.vec.Matrix4;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.client.model.ForgeBlockStateV1.TRSRDeserializer;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.vecmath.Vector3f;
import java.util.EnumMap;

/**
 * Created by covers1624 on 5/16/2016.
 * This is mostly just extracted from the ForgeBlockStateV1.
 * Credits to Rain Warrior.
 * <p>
 * If you have an idea for another transform just make a pull request.
 */
public class TransformUtils {

    private static final TRSRTransformation flipX = new TRSRTransformation(null, null, new Vector3f(-1, 1, 1), null);

    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(TRSRTransformation.class, TRSRDeserializer.INSTANCE).create();

    public static final CCModelState DEFAULT_BLOCK;
    public static final CCModelState DEFAULT_ITEM;
    public static final CCModelState DEFAULT_TOOL;
    public static final CCModelState DEFAULT_BOW;
    public static final CCModelState DEFAULT_HANDHELD_ROD;

    static {
        TRSRTransformation thirdPerson = get(0, 2.5f, 0, 75, 45, 0, 0.375f);
        TRSRTransformation firstPerson;

        ImmutableMap.Builder<TransformType, TRSRTransformation> defaultBlockBuilder = ImmutableMap.builder();
        defaultBlockBuilder.put(TransformType.GUI, get(0, 0, 0, 30, 225, 0, 0.625f));
        defaultBlockBuilder.put(TransformType.GROUND, get(0, 3, 0, 0, 0, 0, 0.25f));
        defaultBlockBuilder.put(TransformType.FIXED, get(0, 0, 0, 0, 0, 0, 0.5f));
        defaultBlockBuilder.put(TransformType.THIRD_PERSON_RIGHT_HAND, thirdPerson);
        defaultBlockBuilder.put(TransformType.THIRD_PERSON_LEFT_HAND, leftify(thirdPerson));
        defaultBlockBuilder.put(TransformType.FIRST_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 45, 0, 0.4f));
        defaultBlockBuilder.put(TransformType.FIRST_PERSON_LEFT_HAND, get(0, 0, 0, 0, 225, 0, 0.4f));
        DEFAULT_BLOCK = new CCModelState(defaultBlockBuilder.build());

        thirdPerson = get(0, 3, 1, 0, 0, 0, 0.55f);
        firstPerson = get(1.13f, 3.2f, 1.13f, 0, -90, 25, 0.68f);
        ImmutableMap.Builder<TransformType, TRSRTransformation> defaultItemBuilder = ImmutableMap.builder();
        defaultItemBuilder.put(TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.5f));
        defaultItemBuilder.put(TransformType.HEAD, get(0, 13, 7, 0, 180, 0, 1));
        defaultItemBuilder.put(TransformType.THIRD_PERSON_RIGHT_HAND, thirdPerson);
        defaultItemBuilder.put(TransformType.THIRD_PERSON_LEFT_HAND, leftify(thirdPerson));
        defaultItemBuilder.put(TransformType.FIRST_PERSON_RIGHT_HAND, firstPerson);
        defaultItemBuilder.put(TransformType.FIRST_PERSON_LEFT_HAND, leftify(firstPerson));
        DEFAULT_ITEM = new CCModelState(defaultItemBuilder.build());

        ImmutableMap.Builder<TransformType, TRSRTransformation> defaultToolBuilder = ImmutableMap.builder();
        defaultToolBuilder.put(TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.5f));
        defaultToolBuilder.put(TransformType.THIRD_PERSON_RIGHT_HAND, get(0, 4, 0.5F, 0, -90, 55, 0.85F));
        defaultToolBuilder.put(TransformType.THIRD_PERSON_LEFT_HAND, get(0, 4, 0.5f, 0, 90, -55, 0.85f));
        defaultToolBuilder.put(TransformType.FIRST_PERSON_RIGHT_HAND, get(1.13f, 3.2f, 1.13f, 0, -90, 25, 0.68f));
        defaultToolBuilder.put(TransformType.FIRST_PERSON_LEFT_HAND, get(1.13f, 3.2f, 1.13f, 0, 90, -25, 0.68f));
        DEFAULT_TOOL = new CCModelState(defaultToolBuilder.build());

        ImmutableMap.Builder<TransformType, TRSRTransformation> defaultBowBuilder = ImmutableMap.builder();
        defaultBowBuilder.put(TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.5f));
        defaultBowBuilder.put(TransformType.THIRD_PERSON_RIGHT_HAND, get(-1F, -2F, 2.5F, -80, 260, -40, 0.9F));
        defaultBowBuilder.put(TransformType.THIRD_PERSON_LEFT_HAND, get(-1F, -2F, 2.5F, -80, -280, 40, 0.9f));
        defaultBowBuilder.put(TransformType.FIRST_PERSON_RIGHT_HAND, get(1.13F, 3.2F, 1.13F, 0, -90, 25, 0.68f));
        defaultBowBuilder.put(TransformType.FIRST_PERSON_LEFT_HAND, get(1.13f, 3.2f, 1.13f, 0, 90, -25, 0.68f));
        DEFAULT_BOW = new CCModelState(defaultBowBuilder.build());

        ImmutableMap.Builder<TransformType, TRSRTransformation> defaultRodBuilder = ImmutableMap.builder();
        defaultRodBuilder.put(TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.5f));
        defaultRodBuilder.put(TransformType.THIRD_PERSON_RIGHT_HAND, get(0F, 4F, 2.5F, 0, 90, 55, 0.85F));
        defaultRodBuilder.put(TransformType.THIRD_PERSON_LEFT_HAND, get(0F, 4F, 2.5F, 0, -90, -55, 0.85f));
        defaultRodBuilder.put(TransformType.FIRST_PERSON_RIGHT_HAND, get(0F, 1.6F, 0.8F, 0, 90, 25, 0.68f));
        defaultRodBuilder.put(TransformType.FIRST_PERSON_LEFT_HAND, get(0F, 1.6F, 0.8F, 0, -90, -25, 0.68f));
        DEFAULT_HANDHELD_ROD = new CCModelState(defaultRodBuilder.build());
    }

    public static TRSRTransformation get(float tx, float ty, float tz, float rx, float ry, float rz, float s) {
        return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new Vector3f(tx / 16, ty / 16, tz / 16), TRSRTransformation.quatFromXYZDegrees(new Vector3f(rx, ry, rz)), new Vector3f(s, s, s), null));
    }

    public static TRSRTransformation leftify(TRSRTransformation transform) {
        return TRSRTransformation.blockCenterToCorner(flipX.compose(TRSRTransformation.blockCornerToCenter(transform)).compose(flipX));
    }

    /**
     * Reimplementation from ForgeBlockStateV1's variant Deserializer.
     *
     * @param json The Json that contains either ModelRotation x,y,TRSRTransforms or CCL defaults.
     * @return A IModelState.
     */
    public static Optional<IModelState> parseFromJson(JsonObject json) {
        Optional<IModelState> ret = Optional.absent();
        if (json.has("x") || json.has("y")) {
            int x = JsonUtils.getInt(json, "x", 0);
            int y = JsonUtils.getInt(json, "y", 0);
            ModelRotation rot = ModelRotation.getModelRotation(x, y);
            ret = Optional.<IModelState>of(new TRSRTransformation(rot));
            if (!ret.isPresent()) {
                throw new JsonParseException("Invalid BlockModelRotation x: " + x + " y: " + y);
            }
        }
        if (json.has("transform")) {
            JsonElement transformElement = json.get("transform");
            if (transformElement.isJsonPrimitive() && transformElement.getAsJsonPrimitive().isString()) {
                String transform = transformElement.getAsString();
                if (transform.equals("identity")) {
                    ret = Optional.<IModelState>of(TRSRTransformation.identity());
                } else if (transform.equals("ccl:default-block")) {
                    ret = Optional.<IModelState>of(DEFAULT_BLOCK);
                } else if (transform.equals("ccl:default-item")) {
                    ret = Optional.<IModelState>of(DEFAULT_ITEM);
                } else if (transform.equals("ccl:default-tool")) {
                    ret = Optional.<IModelState>of(DEFAULT_TOOL);
                } else if (transform.equals("ccl:default-bow")) {
                    ret = Optional.<IModelState>of(DEFAULT_BOW);
                } else if (transform.equals("ccl:default-handheld-rod")) {
                    ret = Optional.<IModelState>of(DEFAULT_HANDHELD_ROD);
                }
            } else if (!transformElement.isJsonObject()) {
                try {
                    TRSRTransformation base = GSON.fromJson(transformElement, TRSRTransformation.class);
                    ret = Optional.<IModelState>of(TRSRTransformation.blockCenterToCorner(base));
                } catch (JsonParseException e) {
                    throw new JsonParseException("transform: expected a string, object or valid base transformation, got: " + transformElement);
                }
            } else {
                JsonObject transform = transformElement.getAsJsonObject();
                EnumMap<TransformType, TRSRTransformation> transforms = Maps.newEnumMap(TransformType.class);
                if (transform.has("thirdperson")) {
                    TRSRTransformation t = GSON.fromJson(transform.get("thirdperson"), TRSRTransformation.class);
                    transform.remove("thirdperson");
                    transforms.put(TransformType.THIRD_PERSON_RIGHT_HAND, TRSRTransformation.blockCenterToCorner(t));
                }
                if (transform.has("thirdperson_righthand")) {
                    TRSRTransformation t = GSON.fromJson(transform.get("thirdperson_righthand"), TRSRTransformation.class);
                    transform.remove("thirdperson_righthand");
                    transforms.put(TransformType.THIRD_PERSON_RIGHT_HAND, TRSRTransformation.blockCenterToCorner(t));
                }
                if (transform.has("thirdperson_lefthand")) {
                    TRSRTransformation t = GSON.fromJson(transform.get("thirdperson_lefthand"), TRSRTransformation.class);
                    transform.remove("thirdperson_lefthand");
                    transforms.put(TransformType.THIRD_PERSON_LEFT_HAND, TRSRTransformation.blockCenterToCorner(t));
                }
                if (transform.has("firstperson")) {
                    TRSRTransformation t = GSON.fromJson(transform.get("firstperson"), TRSRTransformation.class);
                    transform.remove("firstperson");
                    transforms.put(TransformType.FIRST_PERSON_RIGHT_HAND, TRSRTransformation.blockCenterToCorner(t));
                }
                if (transform.has("firstperson_righthand")) {
                    TRSRTransformation t = GSON.fromJson(transform.get("firstperson_righthand"), TRSRTransformation.class);
                    transform.remove("firstperson_righthand");
                    transforms.put(TransformType.FIRST_PERSON_RIGHT_HAND, TRSRTransformation.blockCenterToCorner(t));
                }
                if (transform.has("firstperson_lefthand")) {
                    TRSRTransformation t = GSON.fromJson(transform.get("firstperson_lefthand"), TRSRTransformation.class);
                    transform.remove("firstperson_lefthand");
                    transforms.put(TransformType.FIRST_PERSON_LEFT_HAND, TRSRTransformation.blockCenterToCorner(t));
                }
                if (transform.has("head")) {
                    TRSRTransformation t = GSON.fromJson(transform.get("head"), TRSRTransformation.class);
                    transform.remove("head");
                    transforms.put(TransformType.HEAD, TRSRTransformation.blockCenterToCorner(t));
                }
                if (transform.has("gui")) {
                    TRSRTransformation t = GSON.fromJson(transform.get("gui"), TRSRTransformation.class);
                    transform.remove("gui");
                    transforms.put(TransformType.GUI, TRSRTransformation.blockCenterToCorner(t));
                }
                if (transform.has("ground")) {
                    TRSRTransformation t = GSON.fromJson(transform.get("ground"), TRSRTransformation.class);
                    transform.remove("ground");
                    transforms.put(TransformType.GROUND, TRSRTransformation.blockCenterToCorner(t));
                }
                if (transform.has("fixed")) {
                    TRSRTransformation t = GSON.fromJson(transform.get("fixed"), TRSRTransformation.class);
                    transform.remove("fixed");
                    transforms.put(TransformType.FIXED, TRSRTransformation.blockCenterToCorner(t));
                }
                int k = transform.entrySet().size();
                if (transform.has("matrix")) {
                    k--;
                }
                if (transform.has("translation")) {
                    k--;
                }
                if (transform.has("rotation")) {
                    k--;
                }
                if (transform.has("scale")) {
                    k--;
                }
                if (transform.has("post-rotation")) {
                    k--;
                }
                if (k > 0) {
                    throw new JsonParseException("transform: allowed keys: 'thirdperson', 'firstperson', 'gui', 'head', 'matrix', 'translation', 'rotation', 'scale', 'post-rotation'");
                }
                TRSRTransformation base = TRSRTransformation.identity();
                if (!transform.entrySet().isEmpty()) {
                    base = GSON.fromJson(transform, TRSRTransformation.class);
                    base = TRSRTransformation.blockCenterToCorner(base);
                }
                IModelState state;
                if (transforms.isEmpty()) {
                    state = base;
                } else {
                    state = new CCModelState(Maps.immutableEnumMap(transforms), Optional.of(base));
                }
                ret = Optional.of(state);
            }
        }
        return ret;
    }

    public static TRSRTransformation fromMatrix4(Matrix4 matrix4) {
        return new TRSRTransformation(matrix4.toMatrix4f());
    }
}
