package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityEvolvedSpider extends EntitySpider implements IEntityBreathable
{
    public EntityEvolvedSpider(World par1World)
    {
        super(par1World);
        this.setSize(1.4F, 0.9F);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(22.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(ConfigManagerCore.hardMode ? 1.2F : 1.0F);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(ConfigManagerCore.hardMode ? 4.0D : 2.0D);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    protected boolean isAIEnabled()
    {
        return false;
    }
    
    @Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData livingData)
	{
		livingData = super.onSpawnWithEgg(livingData);

		if (this.worldObj.rand.nextInt(100) == 0)
		{
			EntityEvolvedSkeleton skeleton = new EntityEvolvedSkeleton(this.worldObj);
			skeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
			skeleton.onSpawnWithEgg(null);
			this.worldObj.spawnEntityInWorld(skeleton);
			skeleton.mountEntity(this);
		}

        if (livingData == null)
        {
            livingData = new EntityEvolvedSpider.GroupData();

            if (this.worldObj.difficultySetting == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F * this.worldObj.func_147462_b(this.posX, this.posY, this.posZ))
            {
                ((EntityEvolvedSpider.GroupData)livingData).func_111104_a(this.worldObj.rand);
            }
        }

        if (livingData instanceof EntityEvolvedSpider.GroupData)
        {
            int i = ((EntityEvolvedSpider.GroupData)livingData).field_111105_a;

            if (i > 0 && Potion.potionTypes[i] != null)
            {
                this.addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
            }
        }

		return livingData;
	}
    
    @Override
    protected void jump()
    {
        this.motionY = 0.52D / WorldUtil.getGravityFactor(this);
        if (this.motionY < 0.26D) this.motionY = 0.26D;

        if (this.isPotionActive(Potion.jump))
        {
            this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
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

    @Override
    protected void dropRareDrop(int p_70600_1_)
    {
        switch (this.rand.nextInt(10))
        {
            case 0:
            case 1:
            case 9:
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
            	this.dropItem(Items.fermented_spider_eye, 1);
                break;
            case 6:
            	//Oxygen tank half empty or less
                this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
                break;
            case 7:
                this.dropItem(GCItems.oxygenGear, 1);
                break;
            case 8:
                this.dropItem(GCItems.oxygenConcentrator, 1);
                break;
        }
    }
}
