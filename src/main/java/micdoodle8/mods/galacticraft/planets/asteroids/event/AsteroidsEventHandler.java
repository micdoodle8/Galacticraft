package micdoodle8.mods.galacticraft.planets.asteroids.event;

import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler.ThermalArmorEvent;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AsteroidsEventHandler
{
    @SubscribeEvent
    public void onThermalArmorEvent(ThermalArmorEvent event)
    {
        if (event.armorStack == null)
        {
            event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.REMOVE);
            return;
        }

        if (event.armorStack.getItem() == AsteroidsItems.thermalPadding && event.armorStack.getItemDamage() == event.armorIndex)
        {
            event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.ADD);
            return;
        }

        event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.NOTHING);
    }
}
