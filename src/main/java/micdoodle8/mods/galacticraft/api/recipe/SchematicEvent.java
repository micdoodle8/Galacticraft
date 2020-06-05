package micdoodle8.mods.galacticraft.api.recipe;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Event;

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
        public ServerPlayerEntity player;

        public Unlock(ServerPlayerEntity player, ISchematicPage page)
        {
            super(page);
            this.player = player;
        }
    }

    public static class FlipPage extends SchematicEvent
    {
        public int index;
        public int direction;
        public Screen currentGui;

        public FlipPage(Screen cs, ISchematicPage page, int index, int direction)
        {
            super(page);
            this.currentGui = cs;
            this.index = index;
            this.direction = direction;
        }
    }
}
