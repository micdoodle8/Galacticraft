package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.DimensionMoon;
import micdoodle8.mods.galacticraft.core.entities.EntityCelestialFake;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedList;
import java.util.List;

public class PlayerServer implements IPlayerServer
{
    private boolean updatingRidden = false;
    static List<PlayerEntity> noClipList = new LinkedList<>();

    @Override
    public void updateRiddenPre(ServerPlayerEntity player)
    {
        this.updatingRidden = true;
    }

    @Override
    public void updateRiddenPost(ServerPlayerEntity player)
    {
        this.updatingRidden = false;
    }

    @Override
    public boolean dismountEntity(ServerPlayerEntity player, Entity par1Entity)
    {
        return updatingRidden && player.getRidingEntity() instanceof IIgnoreShift && ((IIgnoreShift) player.getRidingEntity()).shouldIgnoreShiftExit();

    }

    @Override
    public void move(ServerPlayerEntity player, MoverType type, Vec3d motion)
    {
        // If the player is on the moon, not airbourne and not riding anything
        if (player.world.getDimension() instanceof DimensionMoon && !player.world.isRemote && player.getRidingEntity() == null)
        {
            GCPlayerHandler.updateFeet(player, motion.x, motion.z);
        }
    }

//    @Override
//    public boolean wakeUpPlayer(ServerPlayerEntity player, boolean immediately, boolean updateWorldFlag, boolean setSpawn)
//    {
//        BlockPos c = player.bedLocation;
//
//        if (c != null)
//        {
//            EventWakePlayer event = new EventWakePlayer(player, c, immediately, updateWorldFlag, setSpawn, false);
//            MinecraftForge.EVENT_BUS.post(event);
//
//            if (event.result == null || event.result == PlayerEntity.SleepResult.OK)
//            {
//                return false;
//            }
//        }
//
//        return true; TODO Cryo chamber
//    }

    @Override
    public float attackEntityFrom(ServerPlayerEntity player, DamageSource par1DamageSource, float par2)
    {
        //No damage while in Celestial Selection screen
        if (player.getRidingEntity() instanceof EntityCelestialFake)
        {
            return -1F;
        }

        if (GalacticraftCore.isPlanetsLoaded)
        {
            if (par1DamageSource == DamageSource.OUT_OF_WORLD)
            {
//                if (player.world.getDimension() instanceof WorldProviderAsteroids)
//                {
//                    if (player.posY > -120D)
//                    {
//                        return -1F;
//                    }
//                    if (player.posY > -180D)
//                    {
//                        par2 /= 2;
//                    }
//                } TODO Planets
            }
            else if (par1DamageSource == DamageSource.FALL || par1DamageSource == DamageSourceGC.spaceshipCrash)
            {
                int titaniumCount = 0;
//                if (player.inventory != null)
//                {
//                    for (ItemStack armorPiece : player.getArmorInventoryList())
//                    {
//                        if (armorPiece != null && armorPiece.getItem() instanceof ItemArmorAsteroids)
//                        {
//                            titaniumCount++;
//                        }
//                    }
//                } TODO Planets
                if (titaniumCount == 4)
                {
                    titaniumCount = 5;
                }
                par2 *= (1 - 0.15D * titaniumCount);
            }
        }

        return (par2 == -1F) ? -0.9999F : par2;
    }

    @Override
    public void knockBack(ServerPlayerEntity player, Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
        int deshCount = 0;
//        if (player.inventory != null && GalacticraftCore.isPlanetsLoaded)
//        {
//            for (int i = 0; i < 4; i++)
//            {
//                for (ItemStack armorPiece : player.getArmorInventoryList())
//                {
//                    if (armorPiece != null && armorPiece.getItem() instanceof ItemArmorMars)
//                    {
//                        deshCount++;
//                    }
//                }
//            }
//        } TODO Planets

        if (player.getRNG().nextDouble() >= player.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getValue())
        {
            player.isAirBorne = deshCount < 2;
            float f1 = MathHelper.sqrt(impulseX * impulseX + impulseZ * impulseZ);
            float f2 = 0.4F - deshCount * 0.05F;
            double d1 = 2.0D - deshCount * 0.15D;
            player.setMotion(player.getMotion().mul(1.0 / d1, 1.0 / d1, 1.0 / d1));
            player.setMotion(player.getMotion().add(-f2 * impulseX / f1, f2, -f2 * impulseZ / f1));

            if (player.getMotion().y > 0.4D)
            {
                player.setMotion(player.getMotion().x, 0.4, player.getMotion().z);
            }
        }
    }

    @Override
    public boolean isSpectator(ServerPlayerEntity player)
    {
        return noClipList.contains(player);
    }

    @Override
    public void setNoClip(ServerPlayerEntity player, boolean noClip)
    {
        if (noClip)
        {
            if (!noClipList.contains(player))
                noClipList.add(player);
        }
        else
        {
            noClipList.remove(player);
        }
    }
}
