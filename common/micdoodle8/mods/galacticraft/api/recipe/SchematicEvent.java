package micdoodle8.mods.galacticraft.api.recipe;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.Event;

/**
 * These events are used internally to perform actions when Galacticraft is
 * installed, without needing to include unnecessary classes. There is no need
 * to subscribe to these events.
 */
public abstract class SchematicEvent extends Event
{
	public ISchematicPage page;

	public SchematicEvent(ISchematicPage page)
	{
		this.page = page;
	}

	public static class Unlock extends SchematicEvent
	{
		public EntityPlayerMP player;

		public Unlock(EntityPlayerMP player, ISchematicPage page)
		{
			super(page);
			this.player = player;
		}
	}

	public static class FlipPage extends SchematicEvent
	{
		public int index;
		public int direction;

		public FlipPage(ISchematicPage page, int index, int direction)
		{
			super(page);
			this.index = index;
			this.direction = direction;
		}
	}
}
