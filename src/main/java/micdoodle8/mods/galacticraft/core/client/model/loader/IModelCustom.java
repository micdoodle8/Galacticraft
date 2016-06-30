package micdoodle8.mods.galacticraft.core.client.model.loader;

public interface IModelCustom {
    String getType();
    void renderAll();
    void renderPart(String partName);
}
