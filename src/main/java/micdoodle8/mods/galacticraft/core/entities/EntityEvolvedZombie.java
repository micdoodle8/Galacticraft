package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityEvolvedZombie extends EntityZombie implements IEntityBreathable
{
    private int conversionTime = 0;

    public EntityEvolvedZombie(World par1World)
    {
        super(par1World);
        this.setSize(0.6F, 1.95F);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    public IAttribute getReinforcementsAttribute()
    {
        return EntityZombie.SPAWN_REINFORCEMENTS_CHANCE;
    }

    @Override
    protected void jump()
    {
        this.motionY = 0.48D / WorldUtil.getGravityFactor(this);
        if (this.motionY < 0.24D)
        {
            this.motionY = 0.24D;
        }

        if (this.isPotionActive(MobEffects.JUMP_BOOST))
        {
            this.motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
        }

        if (this.isSprinting())
        {
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }

    protected void addRandomDrop()
    {
        switch (this.rand.nextInt(16))
        {
        case 0:
        case 1:
        case 2:
            //Dehydrated carrot
            this.entityDropItem(new ItemStack(GCItems.basicItem, 1, 16), 0.0F);
            break;
        case 3:
        case 4:
            this.dropItem(GCItems.meteoricIronRaw, 1);
            break;
        case 5:
        case 6:
            //Dehydrated potato
            this.entityDropItem(new ItemStack(GCItems.basicItem, 1, 18), 0.0F);
            break;
        case 7:
        case 8:
            //Oxygen tank half empty or less
            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
            break;
        case 9:
            this.dropItem(GCItems.oxMask, 1);
            break;
        case 10:
            this.dropItem(GCItems.oxygenVent, 1);
            break;
        case 11:
        case 12:
            this.dropItem(Items.CARROT, 1);
            break;
        case 13:
        case 14:
        case 15:
            if (ConfigManagerCore.challengeMode || ConfigManagerCore.challengeMobDropsAndSpawning) this.dropItem(Items.MELON_SEEDS, 1);
            break;
        }
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        super.dropFewItems(wasRecentlyHit, lootingModifier);
        Item item = this.getDropItem();

        //Less rotten flesh than vanilla
        int j = this.rand.nextInt(2);

        if (item != null)
        {
            if (lootingModifier > 0)
            {
                j += this.rand.nextInt(lootingModifier + 1);
            }

            for (int k = 0; k < j; ++k)
            {
                this.dropItem(item, 1);
            }
        }

        //Drop copper ingot as semi-rare drop if player hit and if dropping rotten flesh (50% chance)
        if (wasRecentlyHit && (ConfigManagerCore.challengeMode || ConfigManagerCore.challengeMobDropsAndSpawning) && j > 0 && this.rand.nextInt(6) == 0)
            this.entityDropItem(new ItemStack(GCItems.basicItem, 1, 3), 0.0F);

        if (this.recentlyHit > 0 && this.rand.nextFloat() < 0.025F + (float)lootingModifier * 0.01F)
        {
            this.addRandomDrop();
        }
    }
}
