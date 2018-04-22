package micdoodle8.mods.galacticraft.core.entities.player;

import java.util.LinkedList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.entities.EntityCelestialFake;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemArmorAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemArmorMars;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;

public class PlayerServer implements IPlayerServer
{
    private boolean updatingRidden = false;
    static List<EntityPlayer> noClipList = new LinkedList<>();

    @Override
    public void updateRiddenPre(EntityPlayerMP player)
    {
        this.updatingRidden = true;
    }

    @Override
    public void updateRiddenPost(EntityPlayerMP player)
    {
        this.updatingRidden = false;
    }

    @Override
    public boolean dismountEntity(EntityPlayerMP player, Entity par1Entity)
    {
        return updatingRidden && player.getRidingEntity() instanceof IIgnoreShift && ((IIgnoreShift) player.getRidingEntity()).shouldIgnoreShiftExit();

    }

    @Override
    public void move(EntityPlayerMP player, MoverType type, double x, double y, double z)
    {
        // If the player is on the moon, not airbourne and not riding anything
        if (player.world.provider instanceof WorldProviderMoon && !player.world.isRemote && player.getRidingEntity() == null)
        {
            GCPlayerHandler.updateFeet(player, x, z);
        }
    }

    @Override
    public boolean wakeUpPlayer(EntityPlayerMP player, boolean immediately, boolean updateWorldFlag, boolean setSpawn)
    {
        BlockPos c = player.bedLocation;

        if (c != null)
        {
            EventWakePlayer event = new EventWakePlayer(player, c, immediately, updateWorldFlag, setSpawn, false);
            MinecraftForge.EVENT_BUS.post(event);

            if (event.result == null || event.result == EntityPlayer.SleepResult.OK)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public float attackEntityFrom(EntityPlayerMP player, DamageSource par1DamageSource, float par2)
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
                if (player.world.provider instanceof WorldProviderAsteroids)
                {
                    if (player.posY > -120D)
                    {
                        return -1F;
                    }
                    if (player.posY > -180D)
                    {
                        par2 /= 2;
                    }
                }
            }
            else if (par1DamageSource == DamageSource.FALL || par1DamageSource == DamageSourceGC.spaceshipCrash)
            {
                int titaniumCount = 0;
                if (player.inventory != null)
                {
                    for (ItemStack armorPiece : player.getArmorInventoryList())
                    {
                        if (armorPiece != null && armorPiece.getItem() instanceof ItemArmorAsteroids)
                        {
                            titaniumCount++;
                        }
                    }
                }
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
    public void knockBack(EntityPlayerMP player, Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
        int deshCount = 0;
        if (player.inventory != null && GalacticraftCore.isPlanetsLoaded)
        {
            for (int i = 0; i < 4; i++)
            {
                for (ItemStack armorPiece : player.getArmorInventoryList())
                {
                    if (armorPiece != null && armorPiece.getItem() instanceof ItemArmorMars)
                    {
                        deshCount++;
                    }
                }
            }
        }

        if (player.getRNG().nextDouble() >= player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue())
        {
            player.isAirBorne = deshCount < 2;
            float f1 = MathHelper.sqrt(impulseX * impulseX + impulseZ * impulseZ);
            float f2 = 0.4F - deshCount * 0.05F;
            double d1 = 2.0D - deshCount * 0.15D;
            player.motionX /= d1;
            player.motionY /= d1;
            player.motionZ /= d1;
            player.motionX -= f2 * impulseX / f1;
            player.motionY += f2;
            player.motionZ -= f2 * impulseZ / f1;

            if (player.motionY > 0.4D)
            {
                player.motionY = 0.4D;
            }
        }
    }

    @Override
    public boolean isSpectator(EntityPlayerMP player)
    {
        return noClipList.contains(player);
    }

    @Override
    public void setNoClip(EntityPlayerMP player, boolean noClip)
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
