package codechicken.core.commands;

import codechicken.core.commands.CoreCommand;
import net.minecraft.world.ChunkPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;

public abstract class PlayerCommand extends CoreCommand
{    
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1)
    {
        if(!super.canCommandSenderUseCommand(var1))
            return false;
        return var1 instanceof EntityPlayer;
    }
    
    
    @Override
    public void handleCommand(String command, String playername, String[] args, WCommandSender listener)
    {
        EntityPlayerMP player = (EntityPlayerMP)listener.wrapped;
        handleCommand(getWorld(player), player, args);
    }
    
    public abstract void handleCommand(WorldServer world, EntityPlayerMP player, String[] args);
    
    public ChunkPosition getPlayerLookingAtBlock(EntityPlayerMP player, float reach)
    {
        Vec3 vec3d = Vec3.createVectorHelper(player.posX, (player.posY + 1.6200000000000001D) - player.yOffset, player.posZ);
        Vec3 vec3d1 = player.getLook(1.0F);
        Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * reach, vec3d1.yCoord * reach, vec3d1.zCoord * reach);
        MovingObjectPosition hit = player.worldObj.clip(vec3d, vec3d2);
        if(hit == null || hit.typeOfHit != EnumMovingObjectType.TILE)
        {
            return null;
        }
        
        return new ChunkPosition(hit.blockX, hit.blockY, hit.blockZ);
    }
    
    public Entity getPlayerLookingAtEntity(EntityPlayerMP player, float reach)
    {
        Vec3 vec3d = Vec3.createVectorHelper(player.posX, (player.posY + 1.6200000000000001D) - player.yOffset, player.posZ);
        Vec3 vec3d1 = player.getLook(1.0F);
        Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * reach, vec3d1.yCoord * reach, vec3d1.zCoord * reach);
        MovingObjectPosition hit = player.worldObj.clip(vec3d, vec3d2);
        if(hit == null || hit.typeOfHit != EnumMovingObjectType.ENTITY)
        {
            return null;
        }
        
        return hit.entityHit;
    }
}
