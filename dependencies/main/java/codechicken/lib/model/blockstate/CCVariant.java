package codechicken.lib.model.blockstate;

import codechicken.lib.util.Copyable;
import codechicken.lib.util.TransformUtils;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Optional;
import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by covers1624 on 18/11/2016.
 */
public class CCVariant implements Copyable<CCVariant> {

    protected ResourceLocation model;
    protected Optional<IModelState> state = Optional.absent();
    protected Optional<Boolean> uvLock = Optional.absent();
    protected Optional<Boolean> smooth = Optional.absent();
    protected Optional<Boolean> gui3d = Optional.absent();
    protected Optional<Integer> weight = Optional.absent();
    protected Map<String, String> textures = new HashMap<String, String>();
    protected Map<String, String> customData = new HashMap<String, String>();
    protected Map<String, Map<String, CCVariant>> subVariants = new LinkedHashMap<String, Map<String, CCVariant>>();

    public CCVariant() {
    }

    public CCVariant(CCVariant variant) {
        this.model = variant.model;
        this.state = variant.state;
        this.uvLock = variant.uvLock;
        this.smooth = variant.smooth;
        this.gui3d = variant.gui3d;
        this.weight = variant.weight;
        this.textures = new HashMap<String, String>(variant.textures);
        this.customData = new HashMap<String, String>(customData);
        this.subVariants = new LinkedHashMap<String, Map<String, CCVariant>>(variant.subVariants);
    }

    public CCVariant with(CCVariant other) {
        if (this.model == null || other.model != null) {
            this.model = other.model;
        }

        if (other.state.isPresent()) {
            this.state = other.state;
        }
        if (other.uvLock.isPresent()) {
            this.uvLock = other.uvLock;
        }
        if (other.smooth.isPresent()) {
            this.smooth = other.smooth;
        }
        if (other.gui3d.isPresent()) {
            this.gui3d = other.gui3d;
        }
        if (other.weight.isPresent()) {
            this.weight = other.weight;
        }
        HashMap<String, String> newTextures = new HashMap<String, String>();
        newTextures.putAll(textures);
        newTextures.putAll(other.textures);
        this.textures = new LinkedHashMap<String, String>(newTextures);
        HashMap<String, String> newCustomData = new HashMap<String, String>();
        newCustomData.putAll(customData);
        newCustomData.putAll(other.customData);
        this.customData = new LinkedHashMap<String, String>(newCustomData);
        return this;
    }

    public List<String> getPossibleVariantNames() {
        List<String> variantNames = new ArrayList<String>();
        for (String variantName : subVariants.keySet()) {
            variantNames.add(variantName);
            for (CCVariant subVariant : subVariants.get(variantName).values()) {
                variantNames.addAll(subVariant.getPossibleVariantNames());
            }
        }
        return variantNames;
    }

    public List<String> getPossibleVariantValues(String variant) {
        List<String> variantValues = new ArrayList<String>();
        for (String variantName : subVariants.keySet()) {
            if (variantName.equals(variant) && subVariants.containsKey(variant)) {
                variantValues.addAll(subVariants.get(variant).keySet());
            }

            for (CCVariant subVariant : subVariants.get(variantName).values()) {
                variantValues.addAll(subVariant.getPossibleVariantValues(variant));
            }
        }
        return variantValues;
    }

    public CCVariant applySubOverrides(CCVariant parent, Map<String, String> kvArray) {
        CCVariant finalVariant = parent;
        for (Entry<String, String> entry : kvArray.entrySet()) {
            for (Entry<String, Map<String, CCVariant>> variantsEntry : subVariants.entrySet()) {
                if (entry.getKey().equals(variantsEntry.getKey())) {
                    Map<String, CCVariant> variantMap = variantsEntry.getValue();
                    if (variantMap.containsKey(entry.getValue())) {
                        finalVariant = finalVariant.with(variantMap.get(entry.getValue()));
                    }
                }
            }
        }

        for (Entry<String, String> entry : kvArray.entrySet()) {
            for (Entry<String, Map<String, CCVariant>> variantsEntry : subVariants.entrySet()) {
                if (entry.getKey().equals(variantsEntry.getKey())) {
                    Map<String, CCVariant> variantMap = variantsEntry.getValue();
                    if (variantMap.containsKey(entry.getValue())) {
                        finalVariant = variantMap.get(entry.getValue()).applySubOverrides(finalVariant, kvArray);
                    }
                }
            }
        }
        return finalVariant;
    }

    @Override
    public CCVariant copy() {
        return new CCVariant(this);
    }

    public static class Deserializer implements JsonDeserializer<CCVariant> {

        public static ResourceLocation getBlockLocation(String location) {
            ResourceLocation tmp = new ResourceLocation(location);
            return new ResourceLocation(tmp.getResourceDomain(), "block/" + tmp.getResourcePath());
        }

        @Override
        public CCVariant deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            CCVariant variant = new CCVariant();
            JsonObject json = element.getAsJsonObject();

            if (json.has("model")) {
                JsonElement modelElement = json.get("model");
                if (modelElement.isJsonNull()) {
                    variant.model = null;
                } else {
                    variant.model = getBlockLocation(modelElement.getAsString());
                }
            }

            if (json.has("textures")) {
                for (Entry<String, JsonElement> entry : json.get("textures").getAsJsonObject().entrySet()) {
                    if (entry.getValue().isJsonNull()) {
                        variant.textures.put(entry.getKey(), "");
                    } else {
                        variant.textures.put(entry.getKey(), entry.getValue().getAsString());
                    }
                }
            }

            variant.state = TransformUtils.parseFromJson(json);

            if (json.has("uvlock")) {
                variant.uvLock = Optional.of(JsonUtils.getBoolean(json, "uvlock"));
            }

            if (json.has("smooth_lighting")) {
                variant.smooth = Optional.of(JsonUtils.getBoolean(json, "smooth_lighting"));
            }

            if (json.has("gui3d")) {
                variant.gui3d = Optional.of(JsonUtils.getBoolean(json, "gui3d"));
            }

            if (json.has("weight")) {
                variant.weight = Optional.of(JsonUtils.getInt(json, "weight"));
            }

            if (json.has("variants")) {
                variant.subVariants.putAll(CCBlockStateLoader.parseVariants(json.getAsJsonObject("variants")));
            }

            if (json.has("custom")) {
                for (Entry<String, JsonElement> e : json.get("custom").getAsJsonObject().entrySet()) {
                    if (e.getValue().isJsonNull()) {
                        variant.customData.put(e.getKey(), null);
                    } else {
                        variant.customData.put(e.getKey(), e.getValue().toString());
                    }
                }
            }

            return variant;
        }
    }

    @Override
    public String toString() {
        ToStringHelper helper = Objects.toStringHelper("CCVariant");
        helper.add("Model", model);
        helper.add("IModelState", state);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        helper.add("Textures:", gson.toJson(textures, Map.class));
        return helper.toString();
    }
}
