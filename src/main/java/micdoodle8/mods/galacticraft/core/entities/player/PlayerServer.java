package micdoodle8.mods.galacticraft.core.entities.player;

import cpw.mods.fml.common.Loader;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemArmorAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemArmorMars;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;

public class PlayerServer implements IPlayerServer
{
    boolean updatingRidden = false;

    @Override
    public void clonePlayer(EntityPlayerMP player, EntityPlayer oldPlayer, boolean keepInv)
    {
        if (oldPlayer instanceof EntityPlayerMP)
        {
            GCPlayerStats.get(player).copyFrom(GCEntityPlayerMP.getPlayerStats((EntityPlayerMP) oldPlayer), keepInv || player.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"));
        }
    }

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
    public boolean mountEntity(EntityPlayerMP player, Entity par1Entity)
    {
        if (updatingRidden && player.ridingEntity instanceof IIgnoreShift && ((IIgnoreShift) player.ridingEntity).shouldIgnoreShiftExit())
        {
            return true;
        }

        return false;
    }

    @Override
    public void moveEntity(EntityPlayerMP player, double par1, double par3, double par5)
    {
        // If the player is on the moon, not airbourne and not riding anything
        if (player.worldObj.provider instanceof WorldProviderMoon && !player.worldObj.isRemote && player.ridingEntity == null)
        {
            GCPlayerHandler.updateFeet(player, par1, par5);
        }
    }

    @Override
    public boolean wakeUpPlayer(EntityPlayerMP player, boolean par1, boolean par2, boolean par3)
    {
        return this.wakeUpPlayer(player, par1, par2, par3, false);
    }

    @Override
    public float attackEntityFrom(EntityPlayerMP player, DamageSource par1DamageSource, float par2)
    {
        if (Loader.isModLoaded(Constants.MOD_ID_PLANETS))
        {
            if (par1DamageSource == DamageSource.outOfWorld)
            {
                if (player.worldObj.provider instanceof WorldProviderAsteroids)
                {
                    if (player.posY > -120D) return -1;
                    if (player.posY > -180D) par2 /= 2;
                }
            }
            else if (par1DamageSource == DamageSource.fall || par1DamageSource == DamageSourceGC.spaceshipCrash)
            {
                int titaniumCount = 0;
                if (player.inventory != null)
                {
                    for (int i = 0; i < 4; i++)
                    {
                        ItemStack armorPiece = player.getCurrentArmor(i);
                        if (armorPiece != null && armorPiece.getItem() instanceof ItemArmorAsteroids)
                        {
                            titaniumCount++;
                        }
                    }
                }
                if (titaniumCount == 4) titaniumCount = 5;
                par2 *= (1 - 0.15D * titaniumCount);
            }
        }

        return par2;
    }

    @Override
    public void knockBack(EntityPlayerMP player, Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
        int deshCount = 0;
        if (player.inventory != null && Loader.isModLoaded(Constants.MOD_ID_PLANETS))
        {
            for (int i = 0; i < 4; i++)
            {
                ItemStack armorPiece = player.getCurrentArmor(i);
                if (armorPiece != null && armorPiece.getItem() instanceof ItemArmorMars)
                {
                    deshCount++;
                }
            }
        }

        if (player.getRNG().nextDouble() >= player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue())
        {
            player.isAirBorne = deshCount < 2;
            float f1 = MathHelper.sqrt_double(impulseX * impulseX + impulseZ * impulseZ);
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

    public boolean wakeUpPlayer(EntityPlayerMP player, boolean par1, boolean par2, boolean par3, boolean bypass)
    {
        ChunkCoordinates c = player.playerLocation;

        if (c != null)
        {
            EventWakePlayer event = new EventWakePlayer(player, c.posX, c.posY, c.posZ, par1, par2, par3, bypass);
            MinecraftForge.EVENT_BUS.post(event);

            if (bypass || event.result == null || event.result == EntityPlayer.EnumStatus.OK)
            {
                return false;
            }
        }

        return true;
    }
}
