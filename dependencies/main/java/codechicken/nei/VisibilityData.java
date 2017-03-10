package codechicken.nei;

public class VisibilityData {
    public boolean showUtilityButtons = true;
    public boolean showStateButtons = true;
    /**
     * Item panel and associated buttons
     */
    public boolean showItemPanel = true;
    /**
     * Item and search section
     */
    @Deprecated
    public boolean showItemSection = true;
    /**
     * Dropdown and Item search field
     */
    public boolean showSearchSection = true;
    /**
     * All widgets except options button
     */
    public boolean showWidgets = true;
    /**
     * The entire NEI interface, aka hidden
     */
    public boolean showNEI = true;
    public boolean enableDeleteMode = true;

    public void translateDependancies() {
        if (!showNEI) {
            showWidgets = false;
        }
        if (!showWidgets) {
            showSearchSection = showItemPanel = showUtilityButtons = showStateButtons = false;
        }
//        JEIIntegrationManager.pushChanges(this);

        //if (!showItemSection){
        //    showSearchSection = showItemPanel = false;
        //} else {
        //    showSearchSection = showItemPanel = true;
        //}
    }
}
