package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

/**
 * Do not reference this or test 'intance of' this in your code:
 * if PlayerAPI is installed, GCEntityPlayerMP will not be used.
 */
public class GCEntityPlayerMP extends EntityPlayerMP
{
    public GCEntityPlayerMP(MinecraftServer server, WorldServer world, GameProfile profile, PlayerInteractionManager interactionManager)
    {
        super(server, world, profile, interactionManager);
//        if (this.world != world)
//        {
//            GCPlayerStats.get(this).setStartDimension(WorldUtil.getDimensionName(this.world.provider));
//        }
    }

    //Server-only method
    @Override
    public void copyFrom(EntityPlayerMP oldPlayer, boolean keepInv)
    {
        super.copyFrom(oldPlayer, keepInv);
        GalacticraftCore.proxy.player.clonePlayer(this, oldPlayer, keepInv);
        TileEntityTelemetry.updateLinkedPlayer((EntityPlayerMP) oldPlayer, this);
    }

    @Override
    public void updateRidden()
    {
        GalacticraftCore.proxy.player.updateRiddenPre(this);
        super.updateRidden();
        GalacticraftCore.proxy.player.updateRiddenPost(this);
    }

    @Override
    public void dismountRidingEntity()
    {
        if (!GalacticraftCore.proxy.player.dismountEntity(this, this.getRidingEntity()))
        {
            super.dismountRidingEntity();
        }
    }

    @Override
    public void move(MoverType type, double x, double y, double z)
    {
        super.move(type, x, y, z);
        GalacticraftCore.proxy.player.move(this, type, x, y, z);
    }

    @Override
    public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn)
    {
        if (!GalacticraftCore.proxy.player.wakeUpPlayer(this, immediately, updateWorldFlag, setSpawn))
        {
            super.wakeUpPlayer(immediately, updateWorldFlag, setSpawn);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        par2 = GalacticraftCore.proxy.player.attackEntityFrom(this, par1DamageSource, par2);

        if (par2 == -1)
        {
            return false;
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

    @Override
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
        GalacticraftCore.proxy.player.knockBack(this, p_70653_1_, p_70653_2_, impulseX, impulseZ);
    }
    
    /*@Override
    public void setInPortal()
    {
    	if (!(this.world.provider instanceof IGalacticraftWorldProvider))
    	{
    		super.setInPortal();
    	}
    } TODO Fix disable of portal */
}
