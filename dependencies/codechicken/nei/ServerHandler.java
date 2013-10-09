package codechicken.nei;

import java.util.EnumSet;
import java.util.List;


import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import codechicken.core.CommonUtils;
import codechicken.lib.packet.PacketCustom;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ServerHandler implements ITickHandler, IPlayerTracker 
{
    private static ServerHandler instance;
    
    public static void load() 
    {
        instance = new ServerHandler();
        
        PacketCustom.assignHandler(NEICPH.channel, 0, 255, new NEISPH());    
        TickRegistry.registerTickHandler(instance, Side.SERVER);
        GameRegistry.registerPlayerTracker(instance);

        NEIActions.init();
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) 
    {
        if(type.contains(TickType.WORLD))
        {
            processDisabledProperties((World)tickData[0]);
        }
        if(type.contains(TickType.WORLDLOAD))
        {
            NEIServerConfig.load((World)tickData[0]);
        }
        if(type.contains(TickType.PLAYER))
        {
            EntityPlayerMP player = (EntityPlayerMP)tickData[0];
            PlayerSave save = NEIServerConfig.forPlayer(player.username);
            if(save == null)
                return;
            updateMagneticPlayer(player, save);
            save.updateOpChange(player);
            save.save();
        }
    }
    

    private void processDisabledProperties(World world)
    {        
        NEIServerUtils.advanceDisabledTimes(world);
        if(NEIServerUtils.isRaining(world) && NEIServerConfig.isActionDisabled(CommonUtils.getDimension(world), "rain"))
            NEIServerUtils.toggleRaining(world, false);
    }
    
    private void updateMagneticPlayer(EntityPlayerMP player, PlayerSave save)
    {
        if(!save.isActionEnabled("magnet") || player.isDead)
            return;
        
        float distancexz = 16;
        float distancey = 8;
        double maxspeedxz = 0.5;
        double maxspeedy = 0.5;
        double speedxz = 0.05;
        double speedy = 0.07;
        List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(distancexz, distancey, distancexz));
        for(EntityItem item : items)
        {
            if(item.delayBeforeCanPickup > 0)continue;
            if(!NEIServerUtils.canItemFitInInventory(player, item.getEntityItem()))continue;
            if(item.delayBeforeCanPickup == 0)
            {
                NEISPH.sendAddMagneticItemTo(player, item);
            }
            
            double dx = player.posX - item.posX;
            double dy = player.posY+player.getEyeHeight() - item.posY;
            double dz = player.posZ - item.posZ;
            double absxz = Math.sqrt(dx*dx+dz*dz);
            double absy = Math.abs(dy);
            if(absxz > distancexz)
            {
                continue;
            }
            if(absxz < 1)
            {
                item.onCollideWithPlayer(player);
            }
            
            if(absxz > 1)
            {
                dx /= absxz;
                dz /= absxz;
            }
            
            if(absy > 1)
            {
                dy /= absy;
            }

            double vx = item.motionX + speedxz*dx;
            double vy = item.motionY + speedy*dy;
            double vz = item.motionZ + speedxz*dz;
            
            double absvxz = Math.sqrt(vx*vx+vz*vz);
            double absvy = Math.abs(vy);
            
            double rationspeedxz = absvxz / maxspeedxz;
            if(rationspeedxz > 1)
            {
                vx/=rationspeedxz;
                vz/=rationspeedxz;
            }
            
            double rationspeedy = absvy / maxspeedy;
            if(rationspeedy > 1)
            {
                vy/=rationspeedy;
            }
            
            item.motionX = vx;
            item.motionY = vy;
            item.motionZ = vz;
        }
    }
    
    public void tickEnd(EnumSet<TickType> type, Object... tickData) 
    {}

    @Override
    public EnumSet<TickType> ticks() 
    {
        return EnumSet.of(TickType.WORLD, TickType.PLAYER, TickType.WORLDLOAD);
    }

    @Override
    public String getLabel() 
    {
        return "NEI Server";
    }

    @Override
    public void onPlayerLogin(EntityPlayer player) 
    {
        NEIServerConfig.loadPlayer(player);
        NEISPH.sendHasServerSideTo((EntityPlayerMP) player);
    }

    @Override
    public void onPlayerLogout(EntityPlayer player) 
    {
        NEIServerConfig.unloadPlayer(player);
    }

    @Override
    public void onPlayerChangedDimension(EntityPlayer player) 
    {
        NEISPH.sendHasServerSideTo((EntityPlayerMP) player);
    }

    @Override
    public void onPlayerRespawn(EntityPlayer player)
    {
        NEISPH.sendHasServerSideTo((EntityPlayerMP) player);        
    }
}
