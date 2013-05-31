package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class GCCoreEntityWormPart extends Entity
{
    public final IEntityMultiPart entityWormObj;
    public final String name;

    public GCCoreEntityWormPart(IEntityMultiPart par1, String par2, float par3, float par4)
    {
        super(par1.getWorld());
        this.setSize(par3, par4);
        this.entityWormObj = par1;
        this.name = par2;
    }

	@Override
	protected void entityInit(){}

	@Override
	protected void readEntityFromNBT(NBTTagCompound var1){}

	@Override
	protected void writeEntityToNBT(NBTTagCompound var1){}

	@Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

	@Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        return this.isEntityInvulnerable() ? false : this.entityWormObj.attackEntityFromPart(this, par1DamageSource, par2);
    }

	@Override
    public boolean isEntityEqual(Entity par1Entity)
    {
        return this == par1Entity || this.entityWormObj == par1Entity;
    }
}
