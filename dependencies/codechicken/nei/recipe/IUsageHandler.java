package codechicken.nei.recipe;


/**
 * 
 * Implement this to show custom recipes in the usage viewer.
 * See the default vanilla ones for more info.
 *
 */
public interface IUsageHandler extends IRecipeHandler
{
    /**
     * 
     * @param inputId A String identifier representing the type of ingredients used. Eg. {"item", "fuel"}
     * @param ingredients Objects representing the ingredients that matching recipes must contain.
     * @return An instance of {@link IUsageHandler} configured with a list of recipes that contain matching input
     */
    public IUsageHandler getUsageHandler(String inputId, Object... ingredients);
}
