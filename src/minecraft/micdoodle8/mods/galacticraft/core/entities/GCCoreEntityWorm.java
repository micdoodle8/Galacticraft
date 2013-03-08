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

public class GCCoreEntityWorm extends EntityMob implements IEntityMultiPart
{
    public GCCoreEntityWormPart[] wormPartArray;

    public GCCoreEntityWormPart wormPartHead;
    public GCCoreEntityWormPart wormPartBody1;
    public GCCoreEntityWormPart wormPartBody2;
    public GCCoreEntityWormPart wormPartBody3;
    public GCCoreEntityWormPart wormPartBody4;
    public GCCoreEntityWormPart wormPartBody5;
    public GCCoreEntityWormPart wormPartBody6;
    public GCCoreEntityWormPart wormPartBody7;
    public GCCoreEntityWormPart wormPartBody8;
    public GCCoreEntityWormPart wormPartBody9;

    public GCCoreEntityWorm(World par1World)
    {
        super(par1World);
        this.wormPartArray = new GCCoreEntityWormPart[]
        {
        		this.wormPartHead = new GCCoreEntityWormPart(this, "head", 2.0F, 2.0F),
				this.wormPartBody1 = new GCCoreEntityWormPart(this, "body", 8.0F, 8.0F),
				this.wormPartBody2 = new GCCoreEntityWormPart(this, "body", 4.0F, 4.0F),
				this.wormPartBody3 = new GCCoreEntityWormPart(this, "body", 4.0F, 4.0F),
				this.wormPartBody4 = new GCCoreEntityWormPart(this, "body", 4.0F, 4.0F),
				this.wormPartBody5 = new GCCoreEntityWormPart(this, "body", 4.0F, 4.0F),
				this.wormPartBody6 = new GCCoreEntityWormPart(this, "body", 4.0F, 4.0F),
				this.wormPartBody7 = new GCCoreEntityWormPart(this, "body", 4.0F, 4.0F),
				this.wormPartBody8 = new GCCoreEntityWormPart(this, "body", 4.0F, 4.0F),
				this.wormPartBody9 = new GCCoreEntityWormPart(this, "body", 4.0F, 4.0F)
        };
        this.texture = "/micdoodle8/mods/galacticraft/core/client/entities/worm.png";
        this.setSize(2F, 2F);
        this.moveSpeed = 1F / 15F;
        this.noClip = true;
        this.ignoreFrustumCheck = true;
    }

    @Override
	protected void entityInit()
    {
    	super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf(this.rand.nextInt(4)));
        this.dataWatcher.addObject(17, Integer.valueOf(0));
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

    @Override
	public int getMaxHealth()
    {
        return 80;
    }

    @Override
	protected String getLivingSound()
    {
        return "mob.silverfish.say";
    }

    @Override
	protected String getHurtSound()
    {
        return "mob.silverfish.hit";
    }

    @Override
	protected String getDeathSound()
    {
        return "mob.silverfish.kill";
    }

    @Override
	protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.silverfish.step", 0.15F, 1.0F);
    }

    @Override
    public void setPosition(double par1, double par3, double par5)
    {
        this.posX = par1;
        this.posY = par3;
        this.posZ = par5;
        final float var7 = this.width / 2.0F;
        final float var8 = this.height;
        this.boundingBox.setBounds(par1 - var7, par3 - this.yOffset + this.ySize, par5 - var7, par1 + var7, par3 - this.yOffset + this.ySize + var8, par5 + var7);
    }

    @Override
	protected int getDropItemId()
    {
        return 0;
    }

    @Override
	public void onUpdate()
    {
        super.onUpdate();
    }

    @Override
	protected void updateEntityActionState()
    {
        super.updateEntityActionState();
    }

    @Override
	public void onLivingUpdate()
    {
    	if (!this.worldObj.isRemote && this.rand.nextInt(150) == 0)
    	{
    		this.turn();
    	}

		this.rotationYaw = this.getRotationIndex() % 4 * 90F + 45F;

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
			xOffset = -4;
			break;
		case 1:
			zOffset = -4;
			break;
		case 2:
			xOffset = 4;
			break;
		case 3:
			zOffset = 4;
			break;
		}

//    	for (int i = -1; i < 2; i++)
//    	{
//    		for (int j = -1; j < 2; j++)
//    		{
//            	for (int k = -1; k < 2; k++)
//            	{
//            		int id = this.worldObj.getBlockId(MathHelper.floor_double(this.posX + xOffset + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ + zOffset + k));
//
//            		if (id > 0 && id != Block.waterMoving.blockID && id != Block.waterStill.blockID && id != Block.bedrock.blockID)
//            		{
//                		Block block = Block.blocksList[id];
//
//                		if (block != null)
//                		{
//                    		block.dropBlockAsItemWithChance(worldObj, MathHelper.floor_double(this.posX + xOffset + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ + zOffset + k), worldObj.getBlockMetadata(MathHelper.floor_double(this.posX + xOffset + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ + zOffset + k)), 1F, 0);
//                		}
//
//                        if (this.worldObj.setBlockAndMetadataWithUpdate(MathHelper.floor_double(this.posX + xOffset + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ + zOffset + k), 0, 0, this.worldObj.isRemote))
//                        {
//                            this.worldObj.notifyBlocksOfNeighborChange(MathHelper.floor_double(this.posX + xOffset + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ + zOffset + k), 0);
//                        }
//            		}
//
//            		if (id == Block.bedrock.blockID)
//            		{
//            			turn();
//            		}
//            	}
//    		}
//    	}
//
//    	for (int i = -1; i < 2; i++)
//    	{
//    		for (int j = -1; j < 2; j++)
//    		{
//            	for (int k = -1; k < 2; k++)
//            	{
//            		int id = this.worldObj.getBlockId(MathHelper.floor_double(this.posX - (xOffset / 6) + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ - (zOffset / 6) + k));
//
//            		if (id > 0 && id != Block.waterMoving.blockID && id != Block.waterStill.blockID && id != Block.bedrock.blockID)
//            		{
//                		Block block = Block.blocksList[id];
//
//                		if (block != null)
//                		{
//                    		block.dropBlockAsItemWithChance(worldObj, MathHelper.floor_double(this.posX - (xOffset / 6) + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ - (zOffset / 6) + k), worldObj.getBlockMetadata(MathHelper.floor_double(this.posX - (xOffset / 6) + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ - (zOffset / 6) + k)), 1F, 0);
//                		}
//
//                        if (this.worldObj.setBlockAndMetadataWithUpdate(MathHelper.floor_double(this.posX - (xOffset / 6) + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ - (zOffset / 6) + k), 0, 0, this.worldObj.isRemote))
//                        {
//                            this.worldObj.notifyBlocksOfNeighborChange(MathHelper.floor_double(this.posX - (xOffset / 6) + i), MathHelper.floor_double(this.posY + j), MathHelper.floor_double(this.posZ - (zOffset / 6) + k), 0);
//                        }
//            		}
//            	}
//    		}


        final float var5 = this.rotationYaw * (float)Math.PI / 180.0F;
        MathHelper.sin(var5);
        MathHelper.cos(var5);

		this.wormPartHead.onUpdate();
        this.wormPartHead.setLocationAndAngles(this.posX + xOffset, this.posY - 0.5, this.posZ + zOffset, 0.0F, 0.0F);

    	final int id = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 3), MathHelper.floor_double(this.posZ));

    	if (id == Block.waterMoving.blockID || id == Block.waterStill.blockID || id == 0)
    	{
    		this.motionY -= 0.062D;
    	}

        final List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)MathHelper.floor_double(this.posX) - 2 + xOffset, (double)MathHelper.floor_double(this.posY) - 2, (double)MathHelper.floor_double(this.posZ) - 2 + zOffset, (double)MathHelper.floor_double(this.posX) + 2 + xOffset, (double)MathHelper.floor_double(this.posY) + 2, (double)MathHelper.floor_double(this.posZ) + 2 + zOffset));

        for (int var11 = 0; var11 < var9.size(); ++var11)
        {
            final Entity var32 = (Entity)var9.get(var11);

            if (var32 != null && !(var32 instanceof EntityItem))
            {
            	var32.attackEntityFrom(DamageSource.cactus, 1);
            }
        }

        this.motionY *= 0.95;

        this.motionX = -(this.moveSpeed * Math.cos(((this.getRotationIndex() % 4 + 1) * 90F - 90F) * Math.PI / 180.0D));
        this.motionZ = -(this.moveSpeed * Math.sin(((this.getRotationIndex() % 4 + 1) * 90F - 90F) * Math.PI / 180.0D));

        if (this.getSlowed() == 1)
        {
            this.moveEntity(this.motionX * 0.800000011920929D, this.motionY * 0.800000011920929D, this.motionZ * 0.800000011920929D);
        }
        else
        {
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
        }

        if (!this.worldObj.isRemote)
        {
            this.setSlowed(this.destroyBlocksInAABB(this.wormPartHead.boundingBox) | this.destroyBlocksInAABB(this.wormPartBody1.boundingBox));
        }
    }

    @Override
	protected boolean isValidLightLevel()
    {
        return true;
    }

    @Override
	public boolean getCanSpawnHere()
    {
        if (super.getCanSpawnHere())
        {
            final EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
            return var1 == null;
        }
        else
        {
            return false;
        }
    }

    @Override
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

    public int getSlowed()
    {
    	return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setSlowed(boolean b)
    {
    	if (b)
    	{
    		this.setSlowed(1);
    	}
    	else
    	{
    		this.setSlowed(0);
    	}
    }

    public void setSlowed(int i)
    {
    	this.dataWatcher.updateObject(17, Integer.valueOf(i));
    }

    private void turn()
    {
		this.setRotationIndex(this.getRotationIndex() + 1);
    }

	@Override
	public World getWorld()
	{
        return this.worldObj;
	}

	@Override
    public Entity[] getParts()
    {
        return this.wormPartArray;
    }

	@Override
    public boolean canBeCollidedWith()
    {
        return false;
    }

	@Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        return false;
    }

	@Override
	public boolean attackEntityFromPart(GCCoreEntityWormPart var1, DamageSource par2DamageSource, int par3)
	{
        if (var1 != this.wormPartHead)
        {
            par3 = par3 / 4 + 1;
        }

        if (par2DamageSource.getEntity() instanceof EntityPlayer || par2DamageSource == DamageSource.explosion)
        {
        	super.attackEntityFrom(par2DamageSource, par3);
        }

        return true;
	}

    private boolean destroyBlocksInAABB(AxisAlignedBB par1AxisAlignedBB)
    {
        final int var2 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        final int var3 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        final int var4 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        final int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.maxY);
        final int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxZ);
        boolean var8 = false;
        boolean var9 = false;

        for (int var10 = var2; var10 <= var5; ++var10)
        {
            for (int var11 = var3; var11 <= var6; ++var11)
            {
                for (int var12 = var4; var12 <= var7; ++var12)
                {
                    final int var13 = this.worldObj.getBlockId(var10, var11, var12);
                    final Block block = Block.blocksList[var13];

                    if (block != null)
                    {
                        if (block.canDragonDestroy(this.worldObj, var10, var11, var12))
                        {
                            var9 = true;
                            this.worldObj.setBlockWithNotify(var10, var11, var12, 0);
                        }
                        else
                        {
                            var8 = true;
                        }
                    }
                }
            }
        }

        if (var9)
        {
            final double var16 = par1AxisAlignedBB.minX + (par1AxisAlignedBB.maxX - par1AxisAlignedBB.minX) * this.rand.nextFloat();
            final double var17 = par1AxisAlignedBB.minY + (par1AxisAlignedBB.maxY - par1AxisAlignedBB.minY) * this.rand.nextFloat();
            final double var14 = par1AxisAlignedBB.minZ + (par1AxisAlignedBB.maxZ - par1AxisAlignedBB.minZ) * this.rand.nextFloat();
            this.worldObj.spawnParticle("largeexplode", var16, var17, var14, 0.0D, 0.0D, 0.0D);
        }

        return var8;
    }
}
