package micdoodle8.mods.galacticraft.core.entities;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLLog;

public class GCCoreEntityWorm extends EntityMob
{
    public GCCoreEntityWorm(World par1World)
    {
        super(par1World);
        this.texture = "/micdoodle8/mods/galacticraft/core/client/entities/worm.png";
        this.setSize(2F, 2F);
        this.moveSpeed = 1F / 15F;
        this.noClip = true;
    }
    
    protected void entityInit()
    {
    	super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf(this.rand.nextInt(4)));
    }
    
    @Override
	public AxisAlignedBB getCollisionBox(Entity var1)
    {
        return var1.boundingBox;
    }

    @Override
	public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    public int getMaxHealth()
    {
        return 80;
    }

    protected String getLivingSound()
    {
        return "mob.silverfish.say";
    }

    protected String getHurtSound()
    {
        return "mob.silverfish.hit";
    }

    protected String getDeathSound()
    {
        return "mob.silverfish.kill";
    }

    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.silverfish.step", 0.15F, 1.0F);
    }

    protected int getDropItemId()
    {
        return 0;
    }

    @Override
	public boolean canBeCollidedWith()
    {
        return true;
    }

    public void onUpdate()
    {
        super.onUpdate();
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
    }

    public void onLivingUpdate()
    {
    	if (!this.worldObj.isRemote && this.rand.nextInt(150) == 0)
    	{
    		this.setRotationIndex(this.getRotationIndex() + 1);
    	}

		this.rotationYaw = (float) (this.getRotationIndex() % 4) * 90F + 45F;
    	
//    	if (this.worldObj.isBlockSolidOnSide((int)this.posX, (int)this.posY - 3, (int)this.posZ, ForgeDirection.UP))
//    	{
//    		
//    	}
//    	else
//    	{
//    	}
		
		int xOffset = 0;
		int zOffset = 0;
		
		switch (this.getRotationIndex() % 4)
		{
		case 0:
			xOffset = -5;
			break;
		case 1:
			zOffset = -5;
			break;
		case 2:
			xOffset = 5;
			break;
		case 3:
			zOffset = 5;
			break;
		}
    	
    	for (int i = -1; i < 2; i++)
    	{
    		for (int j = -1; j < 2; j++)
    		{
            	for (int k = -1; k < 2; k++)
            	{
            		int id = this.worldObj.getBlockId(MathHelper.floor_double(this.posX + xOffset + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ + zOffset + k));
            		
            		if (id > 0 && id != Block.waterMoving.blockID && id != Block.waterStill.blockID)
            		{
                		Block block = Block.blocksList[id];
                		
                		if (block != null)
                		{
                    		block.dropBlockAsItemWithChance(worldObj, MathHelper.floor_double(this.posX + xOffset + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ + zOffset + k), worldObj.getBlockMetadata(MathHelper.floor_double(this.posX + xOffset + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ + zOffset + k)), 1F, 0);
                		}

                        if (this.worldObj.setBlockAndMetadataWithUpdate(MathHelper.floor_double(this.posX + xOffset + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ + zOffset + k), 0, 0, this.worldObj.isRemote))
                        {
                            this.worldObj.notifyBlocksOfNeighborChange(MathHelper.floor_double(this.posX + xOffset + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ + zOffset + k), 0);
                        }
            		}
            	}
    		}
    	}
    	
    	for (int i = -1; i < 2; i++)
    	{
    		for (int j = -1; j < 2; j++)
    		{
            	for (int k = -1; k < 2; k++)
            	{
            		int id = this.worldObj.getBlockId(MathHelper.floor_double(this.posX - (xOffset / 6) + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ - (zOffset / 6) + k));

            		if (id > 0 && id != Block.waterMoving.blockID && id != Block.waterStill.blockID)
            		{
                		Block block = Block.blocksList[id];
                		
                		if (block != null)
                		{
                    		block.dropBlockAsItemWithChance(worldObj, MathHelper.floor_double(this.posX - (xOffset / 6) + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ - (zOffset / 6) + k), worldObj.getBlockMetadata(MathHelper.floor_double(this.posX - (xOffset / 6) + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ - (zOffset / 6) + k)), 1F, 0);
                		}

                        if (this.worldObj.setBlockAndMetadataWithUpdate(MathHelper.floor_double(this.posX - (xOffset / 6) + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ - (zOffset / 6) + k), 0, 0, this.worldObj.isRemote))
                        {
                            this.worldObj.notifyBlocksOfNeighborChange(MathHelper.floor_double(this.posX - (xOffset / 6) + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ - (zOffset / 6) + k), 0);
                        }
            		}
            	}
    		}
    	}
    	
    	int id = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 3), MathHelper.floor_double(this.posZ));

    	if (id == Block.waterMoving.blockID || id == Block.waterStill.blockID || id == 0)
    	{
    		this.motionY -= 0.062D;
    	}

        List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)MathHelper.floor_double(this.posX) - 2 + xOffset, (double)MathHelper.floor_double(this.posY) - 2, (double)MathHelper.floor_double(this.posZ) - 2 + zOffset, (double)MathHelper.floor_double(this.posX) + 2 + xOffset, (double)MathHelper.floor_double(this.posY) + 2, (double)MathHelper.floor_double(this.posZ) + 2 + zOffset));

        for (int var11 = 0; var11 < var9.size(); ++var11)
        {
            Entity var32 = (Entity)var9.get(var11);
            
            if (var32 != null && !(var32 instanceof EntityItem))
            {
            	var32.attackEntityFrom(DamageSource.cactus, 1);
            }
        }
        
        this.motionY *= 0.95;

        this.motionX = -(this.moveSpeed * Math.cos((((this.getRotationIndex() % 4) + 1) * (90F) - 90F) * Math.PI / 180.0D));
        this.motionZ = -(this.moveSpeed * Math.sin((((this.getRotationIndex() % 4) + 1) * (90F) - 90F) * Math.PI / 180.0D));
        
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }
    
    protected boolean isValidLightLevel()
    {
        return true;
    }

    public boolean getCanSpawnHere()
    {
        if (super.getCanSpawnHere())
        {
            EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
            return var1 == null;
        }
        else
        {
            return false;
        }
    }
    
    public int getAttackStrength(Entity par1Entity)
    {
        return 1;
    }
    
    public int getRotationIndex()
    {
    	return this.dataWatcher.getWatchableObjectInt(16);
    }
    
    public void setRotationIndex(int i)
    {
    	this.dataWatcher.updateObject(16, Integer.valueOf(i));
    }
}
