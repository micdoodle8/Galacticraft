package codechicken.lib.item.filtering;

/**
 * Defines a class that can obtain filters, Really only used in NEI but here because reasons.
 */
public interface IItemFilterProvider {

    /**
     * Provides an item filter. May be called from any thread. Returned filter should only reference objects with immutable state.
     */
    IItemFilter getFilter();

}
