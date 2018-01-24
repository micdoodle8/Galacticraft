package micdoodle8.mods.galacticraft.planets.asteroids.event;

import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler.ThermalArmorEvent;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AsteroidsEventHandler
{
    @SubscribeEvent
    public void onThermalArmorEvent(ThermalArmorEvent event)
    {
        if (event.armorStack.isEmpty())
        {
            event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.REMOVE);
        }
        else if (event.armorStack.getItem() == AsteroidsItems.thermalPadding && event.armorStack.getItemDamage() == event.armorIndex)
        {
            event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.ADD);
        }
    }
}
