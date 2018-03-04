package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityEvolvedEnderman extends EntityEnderman implements IEntityBreathable
{
    public EntityEvolvedEnderman(World world)
    {
        super(world);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        super.dropLoot(wasRecentlyHit, lootingModifier, source);
        if (wasRecentlyHit && this.rand.nextFloat() < 0.025F + (float)lootingModifier * 0.015F)
        {
            this.addRandomDrop();
        }
    }

    protected void addRandomDrop()
    {
        switch (this.rand.nextInt(10))
        {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            this.dropItem(Items.ENDER_PEARL, 1);
            break;
        case 6:
            //Oxygen tank half empty or less
            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
            break;
        case 7:
        case 8:
            this.dropItem(Items.ENDER_EYE, 1);
            break;
        case 9:
            this.dropItem(GCItems.oxygenConcentrator, 1);
            break;
        case 10:
            this.dropItem(GCItems.oxMask, 1);
            break;
        }
    }
}