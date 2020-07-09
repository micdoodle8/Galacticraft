package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.item.Item;

/**
 * Used to register canisters with partially filled textures
 */
public class PartialCanister
{
    private final Item item;
    private final String modID;
    private final String baseName;
    private final int textureCount;

    public PartialCanister(Item item, String modID, String baseName, int textureCount)
    {
        this.item = item;
        this.modID = modID;
        this.baseName = baseName;
        this.textureCount = textureCount;
    }

    public Item getItem()
    {
        return item;
    }

    public String getModID()
    {
        return modID;
    }

    public String getBaseName()
    {
        return baseName;
    }

    public int getTextureCount()
    {
        return textureCount;
    }
}
