package micdoodle8.mods.galacticraft.planets.venus.event;

import micdoodle8.mods.galacticraft.api.entity.ILaserTrackableFast;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

public class EventHandlerVenus
{
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.side == Side.SERVER && event.phase == Phase.START)
        {
            ArrayList<Entity> list = new ArrayList<>(event.world.loadedEntityList);
            for (Entity e : list)
            {
                if (e.ticksExisted % 20 == 1 && e instanceof EntityLivingBase)
                {
                    if (event.world.isMaterialInBB(e.getEntityBoundingBox().grow(-0.1D, -0.4D, -0.1D), VenusModule.acidMaterial))
                    {
                        e.attackEntityFrom(DamageSourceGC.acid, 3.0F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onThermalArmorEvent(GCPlayerHandler.ThermalArmorEvent event)
    {
        if (event.armorStack.isEmpty())
        {
            event.setArmorAddResult(GCPlayerHandler.ThermalArmorEvent.ArmorAddResult.REMOVE);
        }
        else if (event.armorStack.getItem() == VenusItems.thermalPaddingTier2 && event.armorStack.getItemDamage() == event.armorIndex)
        {
            event.setArmorAddResult(GCPlayerHandler.ThermalArmorEvent.ArmorAddResult.ADD);
        }
    }

    @SubscribeEvent
    public void onEntitySpawned(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof ILaserTrackableFast)
        {
            for (TileEntity tile : event.getEntity().getEntityWorld().loadedTileEntityList)
            {
                if (tile instanceof TileEntityLaserTurret)
                {
                    ((TileEntityLaserTurret) tile).trackEntity(event.getEntity());
                }
            }
        }
    }
}
