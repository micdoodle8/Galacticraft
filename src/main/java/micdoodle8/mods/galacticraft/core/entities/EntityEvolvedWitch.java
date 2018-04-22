package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityEvolvedWitch extends EntityWitch implements IEntityBreathable
{
    public EntityEvolvedWitch(World world)
    {
        super(world);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    protected void addRandomDrop()
    {
        switch (this.rand.nextInt(10))
        {
        case 0:
        case 1:
        case 9:
            //Dehydrated carrot
            this.entityDropItem(new ItemStack(GCItems.foodItem, 1, 1), 0.0F);
            break;
        case 2:
        case 3:
            this.entityDropItem(new ItemStack(Blocks.GLOWSTONE, 1), 0.0F);
            break;
        case 4:
        case 5:
            this.entityDropItem(new ItemStack(GCItems.basicItem, 1, 20), 0.0F);
            break;
        case 6:
            //Oxygen tank half empty or less
            this.entityDropItem(new ItemStack(GCItems.oxTankHeavy, 1, 901 + this.rand.nextInt(900)), 0.0F);
            break;
        case 7:
            this.dropItem(GCItems.oxMask, 1);
            break;
        case 8:
            this.dropItem(GCItems.oxygenVent, 1);
            break;
        }
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
}