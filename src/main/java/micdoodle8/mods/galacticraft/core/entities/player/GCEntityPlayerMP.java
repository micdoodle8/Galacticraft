package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;

/**
 * This class provides various hooks which are missing from Forge or don't quite do what we need.
 * </p>
 * Do not reference this or test 'instance of' this in your code:
 * if PlayerAPI is installed, GCEntityPlayerMP will not be used.
 */
public class GCEntityPlayerMP extends ServerPlayerEntity
{
    public GCEntityPlayerMP(MinecraftServer server, ServerWorld world, GameProfile profile, PlayerInteractionManager interactionManager)
    {
        super(server, world, profile, interactionManager);
//        if (this.world != world)
//        {
//            GCPlayerStats.get(this).setStartDimension(WorldUtil.getDimensionName(this.world.getDimension()));
//        }
    }

    @Override
    public void updateRidden()
    {
        GalacticraftCore.proxy.player.updateRiddenPre(this);
        super.updateRidden();
        GalacticraftCore.proxy.player.updateRiddenPost(this);
    }

    @Override
    public void stopRiding()
    {
        if (!GalacticraftCore.proxy.player.dismountEntity(this, this.getRidingEntity()))
        {
            super.stopRiding();
        }
    }

    @Override
    public void move(MoverType type, Vec3d pos)
    {
        super.move(type, pos);
        GalacticraftCore.proxy.player.move(this, type, pos);
    }

    @Override
    public void wakeUp()
    {
//    @Override
//    public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn)
//    {
//        if (GalacticraftCore.proxy.player.wakeUpPlayer(this, immediately, updateWorldFlag, setSpawn))
//        {
//            return;
//        } TODO Cryo chamber
        super.wakeUp();
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        par2 = GalacticraftCore.proxy.player.attackEntityFrom(this, par1DamageSource, par2);
        return par2 != -1F && super.attackEntityFrom(par1DamageSource, par2);
    }

    @Override
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
        GalacticraftCore.proxy.player.knockBack(this, p_70653_1_, p_70653_2_, impulseX, impulseZ);
    }

    @Override
    public boolean isSpectator()
    {
        return GalacticraftCore.proxy.player.isSpectator(this) || super.isSpectator();
    }

    /*@Override
    public void setInPortal()
    {
    	if (!(this.world.getDimension() instanceof IGalacticraftWorldProvider))
    	{
    		super.setInPortal();
    	}
    } TODO Fix disable of portal */
}
