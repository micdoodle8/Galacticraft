package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntitySpaceChicken extends EntityChicken implements IEntityBreathable
{
    public EntitySpaceChicken(World world)
    {
        super(world);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
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
            this.dropItem(Items.feather, 1);
            break;
        case 6:
            //Oxygen tank half empty or less
            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
            break;
        case 7:
        case 8:
            this.dropItem(Items.ender_eye, 1);
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
