package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreEntityAstroOrb extends Entity
{
    public int xpColor;

    public int xpOrbAge = 0;
    public int field_70532_c;

    private int xpOrbHealth = 5;

    private int xpValue;

    private EntityPlayer closestPlayer;
    private int field_80002_g;

    public GCCoreEntityAstroOrb(World par1World, double par2, double par4, double par6, int par8)
    {
        super(par1World);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(par2, par4, par6);
        this.rotationYaw = (float)(Math.random() * 360.0D);
        this.motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.motionY = (double)((float)(Math.random() * 0.2D) * 2.0F);
        this.motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.xpValue = 1;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public GCCoreEntityAstroOrb(World par1World)
    {
        super(par1World);
        this.setSize(0.125F, 0.125F);
        this.yOffset = this.height / 2.0F;
    }

    protected void entityInit() {}

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1)
    {
        float var2 = 0.5F;

        if (var2 < 0.0F)
        {
            var2 = 0.0F;
        }

        if (var2 > 1.0F)
        {
            var2 = 1.0F;
        }

        int var3 = super.getBrightnessForRender(par1);
        int var4 = var3 & 255;
        int var5 = var3 >> 16 & 255;
        var4 += (int)(var2 * 15.0F * 16.0F);

        if (var4 > 240)
        {
            var4 = 240;
        }

        return var4 | var5 << 16;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.field_70532_c > 0)
        {
            --this.field_70532_c;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.029999999329447746D;

        if (this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) == Material.lava)
        {
            this.motionY = 0.20000000298023224D;
            this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.func_85030_a("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
        double var1 = 8.0D;

        if (this.field_80002_g < this.xpColor - 20 + this.entityId % 100)
        {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > var1 * var1)
            {
                this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, var1);
            }

            this.field_80002_g = this.xpColor;
        }

        if (this.closestPlayer != null)
        {
            double var3 = (this.closestPlayer.posX - this.posX) / var1;
            double var5 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / var1;
            double var7 = (this.closestPlayer.posZ - this.posZ) / var1;
            double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
            double var11 = 1.0D - var9;

            if (var11 > 0.0D)
            {
                var11 *= var11;
                this.motionX += var3 / var9 * var11 * 0.1D;
                this.motionY += var5 / var9 * var11 * 0.1D;
                this.motionZ += var7 / var9 * var11 * 0.1D;
            }
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        float var13 = 0.98F;

        if (this.onGround)
        {
            var13 = 0.58800006F;
            int var4 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

            if (var4 > 0)
            {
                var13 = Block.blocksList[var4].slipperiness * 0.98F;
            }
        }

        this.motionX *= (double)var13;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= (double)var13;

        if (this.onGround)
        {
            this.motionY *= -0.8999999761581421D;
        }

        ++this.xpColor;
        ++this.xpOrbAge;

        if (this.xpOrbAge >= 6000)
        {
            this.setDead();
        }
    }

    public boolean handleWaterMovement()
    {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
    }

    protected void dealFireDamage(int par1)
    {
        this.attackEntityFrom(DamageSource.inFire, par1);
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (this.func_85032_ar())
        {
            return false;
        }
        else
        {
            this.setBeenAttacked();
            this.xpOrbHealth -= par2;

            if (this.xpOrbHealth <= 0)
            {
                this.setDead();
            }

            return false;
        }
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("Health", (short)((byte)this.xpOrbHealth));
        par1NBTTagCompound.setShort("Age", (short)this.xpOrbAge);
        par1NBTTagCompound.setShort("Value", (short)this.xpValue);
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.xpOrbHealth = par1NBTTagCompound.getShort("Health") & 255;
        this.xpOrbAge = par1NBTTagCompound.getShort("Age");
        this.xpValue = par1NBTTagCompound.getShort("Value");
    }

    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (!this.worldObj.isRemote)
        {
            if (this.field_70532_c == 0 && par1EntityPlayer.xpCooldown == 0)
            {
                par1EntityPlayer.xpCooldown = 2;
                this.func_85030_a("random.orb", 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
                par1EntityPlayer.onItemPickup(this, 1);
                par1EntityPlayer.addExperience(this.xpValue);
                
                for (int i = 0; i < GalacticraftCore.gcPlayers.size(); i++)
                {
                	GCCoreEntityPlayer player = (GCCoreEntityPlayer) GalacticraftCore.gcPlayers.get(i);
                	
                	if (player.getPlayer().username == par1EntityPlayer.username)
                	{
                		player.addExperience(this.xpValue);
                	}
                }
                
                this.setDead();
            }
        }
    }

    public int getXpValue()
    {
        return this.xpValue;
    }

    @SideOnly(Side.CLIENT)
    public int getTextureByXP()
    {
        return this.xpValue >= 2477 ? 10 : (this.xpValue >= 1237 ? 9 : (this.xpValue >= 617 ? 8 : (this.xpValue >= 307 ? 7 : (this.xpValue >= 149 ? 6 : (this.xpValue >= 73 ? 5 : (this.xpValue >= 37 ? 4 : (this.xpValue >= 17 ? 3 : (this.xpValue >= 7 ? 2 : (this.xpValue >= 3 ? 1 : 0)))))))));
    }

    public static int getXPSplit(int par0)
    {
        return par0 >= 2477 ? 2477 : (par0 >= 1237 ? 1237 : (par0 >= 617 ? 617 : (par0 >= 307 ? 307 : (par0 >= 149 ? 149 : (par0 >= 73 ? 73 : (par0 >= 37 ? 37 : (par0 >= 17 ? 17 : (par0 >= 7 ? 7 : (par0 >= 3 ? 3 : 1)))))))));
    }

    public boolean canAttackWithItem()
    {
        return false;
    }
}
