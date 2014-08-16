package micdoodle8.mods.galacticraft.planets.asteroids.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;

public class AsteroidsEventHandler
{
    @SubscribeEvent
    public void onThermalArmorEvent(GCEntityPlayerMP.ThermalArmorEvent event)
    {
        if (event.armorStack == null)
        {
            event.setArmorAddResult(GCEntityPlayerMP.ThermalArmorEvent.ArmorAddResult.REMOVE);
            return;
        }

        if (event.armorStack.getItem() == AsteroidsItems.thermalPadding && event.armorStack.getItemDamage() == event.armorIndex)
        {
            event.setArmorAddResult(GCEntityPlayerMP.ThermalArmorEvent.ArmorAddResult.ADD);
            return;
        }

        event.setArmorAddResult(GCEntityPlayerMP.ThermalArmorEvent.ArmorAddResult.NOTHING);
    }
}
