package micdoodle8.mods.galacticraft.core.util;

/**
 * Simple color enum for adding colors to in-game GUI strings of text.
 *
 * @author AidanBrady
 */
public enum EnumColor
{
    BLACK("\u00a70", "black", new int[] { 0, 0, 0 }),
    DARK_BLUE("\u00a71", "darkBlue", new int[] { 0, 0, 170 }),
    DARK_GREEN("\u00a72", "darkGreen", new int[] { 0, 170, 0 }),
    DARK_AQUA("\u00a73", "darkAqua", new int[] { 0, 170, 170 }),
    DARK_RED("\u00a74", "darkRed", new int[] { 170, 0, 0 }),
    PURPLE("\u00a75", "purple", new int[] { 170, 0, 170 }),
    ORANGE("\u00a76", "orange", new int[] { 255, 170, 0 }),
    GREY("\u00a77", "grey", new int[] { 170, 170, 170 }),
    DARK_GREY("\u00a78", "darkGrey", new int[] { 85, 85, 85 }),
    INDIGO("\u00a79", "indigo", new int[] { 85, 85, 255 }),
    BRIGHT_GREEN("\u00a7a", "brightGreen", new int[] { 85, 255, 85 }),
    AQUA("\u00a7b", "aqua", new int[] { 85, 255, 255 }),
    RED("\u00a7c", "red", new int[] { 255, 85, 85 }),
    PINK("\u00a7d", "pink", new int[] { 255, 85, 255 }),
    YELLOW("\u00a7e", "yellow", new int[] { 255, 255, 85 }),
    WHITE("\u00a7f", "white", new int[] { 255, 255, 255 });

    /**
     * The color code that will be displayed
     */
    private final String code;

    private final int[] rgbCode;

    /**
     * A friendly name of the color.
     */
    private final String unlocalizedName;

    private EnumColor(String s, String n, int[] rgb)
    {
        this.code = s;
        this.unlocalizedName = n;
        this.rgbCode = rgb;
    }
    
    public String getCode()
    {
    	return code;
    }

    public String getLocalizedName()
    {
        return GCCoreUtil.translate("color." + this.unlocalizedName);
    }

    public String getName()
    {
        return this.code + this.getLocalizedName();
    }

    public float getColor(int index)
    {
        return this.rgbCode[index] / 255F;
    }

    @Override
    public String toString()
    {
        return this.code;
    }
}
