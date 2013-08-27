package micdoodle8.mods.galacticraft.mars.tile;

import java.util.List;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlockMachine;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMarsTileEntityCryogenicChamber extends TileEntityMulti implements IMultiBlock
{
    public boolean isOccupied;
    
    public GCMarsTileEntityCryogenicChamber()
    {
        super(GalacticraftCore.CHANNELENTITIES);
    }
    
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        EnumStatus enumstatus = sleepInBedAt(entityPlayer, this.xCoord, this.yCoord, this.zCoord);
        
        if (enumstatus == EnumStatus.OK)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public EnumStatus sleepInBedAt(EntityPlayer entityPlayer, int par1, int par2, int par3)
    {
        if (!this.worldObj.isRemote)
        {
            if (entityPlayer.isPlayerSleeping() || !entityPlayer.isEntityAlive())
            {
                return EnumStatus.OTHER_PROBLEM;
            }

            if (!this.worldObj.provider.isSurfaceWorld())
            {
                return EnumStatus.NOT_POSSIBLE_HERE;
            }

            if (Math.abs(entityPlayer.posX - (double)par1) > 3.0D || Math.abs(entityPlayer.posY - (double)par2) > 2.0D || Math.abs(entityPlayer.posZ - (double)par3) > 3.0D)
            {
                return EnumStatus.TOO_FAR_AWAY;
            }

            double d0 = 8.0D;
            double d1 = 5.0D;
            @SuppressWarnings("unchecked")
            List<EntityMob> list = this.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getAABBPool().getAABB((double)par1 - d0, (double)par2 - d1, (double)par3 - d0, (double)par1 + d0, (double)par2 + d1, (double)par3 + d0));

            if (!list.isEmpty())
            {
                return EnumStatus.NOT_SAFE;
            }
        }

        if (entityPlayer.isRiding())
        {
            entityPlayer.mountEntity((Entity)null);
        }

        if (this.worldObj.blockExists(par1, par2, par3))
        {
            int l = this.worldObj.getBlockMetadata(par1, par2, par3);
            int i1 = BlockBed.getDirection(l);
            Block block = Block.blocksList[worldObj.getBlockId(par1, par2, par3)];
            if (block != null)
            {
                i1 = block.getBedDirection(worldObj, par1, par2, par3);
            }
            float f = 0.5F;
            float f1 = 0.5F;

            switch (i1)
            {
                case 0:
                    f1 = 0.9F;
                    break;
                case 1:
                    f = 0.1F;
                    break;
                case 2:
                    f1 = 0.1F;
                    break;
                case 3:
                    f = 0.9F;
            }

            entityPlayer.setPosition((double)((float)par1 + f), (double)((float)par2 + 0.9375F), (double)((float)par3 + f1));
        }
        else
        {
            entityPlayer.setPosition((double)((float)par1 + 0.5F), (double)((float)par2 + 0.9375F), (double)((float)par3 + 0.5F));
        }

        entityPlayer.sleeping = true;
        entityPlayer.sleepTimer = 0;
        entityPlayer.playerLocation = new ChunkCoordinates(par1, par2, par3);
        entityPlayer.motionX = entityPlayer.motionZ = entityPlayer.motionY = 0.0D;

        if (!this.worldObj.isRemote)
        {
            this.worldObj.updateAllPlayersSleepingFlag();
        }

        return EnumStatus.OK;
    }

    @Override
    public void onCreate(Vector3 placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        
        int x1 = 0;
        int x2 = 0;
        int z1 = 0;
        int z2 = 0;
        
        switch (this.getBlockMetadata() - GCMarsBlockMachine.CRYOGENIC_CHAMBER_METADATA)
        {
        case 0:
            x1 = 0;
            x2 = 0;
            z1 = -1;
            z2 = 1;
            break;
        case 1:
            x1 = 0;
            x2 = 0;
            z1 = -1;
            z2 = 1;
            break;
        case 2:
            x1 = -1;
            x2 = 1;
            z1 = 0;
            z2 = 0;
            break;
        case 3:
            x1 = -1;
            x2 = 1;
            z1 = 0;
            z2 = 0;
            break;
        }

        for (int x = x1; x <= x2; x++)
        {
            for (int z = z1; z <= z2; z++)
            {
                for (int y = 0; y < 4; y++)
                {
                    final Vector3 vecToAdd = Vector3.add(placedPosition, new Vector3(x, y, z));

                    if (!vecToAdd.equals(placedPosition))
                    {
                        GCCoreBlocks.dummyBlock.makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 4);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final Vector3 thisBlock = new Vector3(this);

        int x1 = 0;
        int x2 = 0;
        int z1 = 0;
        int z2 = 0;

        switch (this.getBlockMetadata() - GCMarsBlockMachine.CRYOGENIC_CHAMBER_METADATA)
        {
        case 0:
            x1 = 0;
            x2 = 0;
            z1 = -1;
            z2 = 1;
            break;
        case 1:
            x1 = 0;
            x2 = 0;
            z1 = -1;
            z2 = 1;
            break;
        case 2:
            x1 = -1;
            x2 = 1;
            z1 = 0;
            z2 = 0;
            break;
        case 3:
            x1 = -1;
            x2 = 1;
            z1 = 0;
            z2 = 0;
            break;
        }
        
        for (int x = x1; x <= x2; x++)
        {
            for (int z = z1; z <= z2; z++)
            {
                for (int y = 0; y < 4; y++)
                {
                    if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
                    {
                        FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, GCMarsBlocks.machine.blockID & 4095, GCMarsBlocks.machine.blockID >> 12 & 255);
                    }
                    this.worldObj.setBlock(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, 0, 0, 3);
                }
            }
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.isOccupied = nbt.getBoolean("IsChamberOccupied");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setBoolean("IsChamberOccupied", this.isOccupied);
    }
}