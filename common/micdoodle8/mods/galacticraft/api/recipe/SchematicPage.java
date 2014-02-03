package micdoodle8.mods.galacticraft.api.recipe;

/**
 * Reference implementation of {@link ISchematicPage}. Use/extend this or
 * implement your own.
 */
public abstract class SchematicPage implements ISchematicPage
{
	@Override
	public int compareTo(ISchematicPage o)
	{
		if (this.getPageID() > o.getPageID())
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}
}
