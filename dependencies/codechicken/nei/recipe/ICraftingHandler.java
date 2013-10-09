package codechicken.nei.recipe;

/**
 * 
 * Implement this to show custom recipes in the usage viewer.
 * See the default vanilla ones for more info.
 *
 */
public interface ICraftingHandler extends IRecipeHandler
{
    /**
     * 
     * @param outputId A String identifier representing the type of output produced. Eg. {"item", "fuel"}
     * @param results Objects representing the results that matching recipes must produce.
     * @return An instance of {@link ICraftingHandler} configured with a list of recipes that produce matching output.
     */
    public ICraftingHandler getRecipeHandler(String outputId, Object... results);
}
