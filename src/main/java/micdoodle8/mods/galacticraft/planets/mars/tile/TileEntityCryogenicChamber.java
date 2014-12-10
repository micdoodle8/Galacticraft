package micdoodle8.mods.galacticraft.planets.mars.tile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;

public class TileEntityCryogenicChamber extends TileEntityMulti implements IMultiBlock
{
    public boolean isOccupied;

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
    	return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord, zCoord - 1, xCoord + 2, yCoord + 3, zCoord + 2);
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        if (this.worldObj.isRemote)
        {
            return false;
        }

        EnumStatus enumstatus = this.sleepInBedAt(entityPlayer, this.xCoord, this.yCoord, this.zCoord);

        switch (enumstatus)
        {
        case OK:
            ((EntityPlayerMP) entityPlayer).playerNetServerHandler.setPlayerLocation(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, entityPlayer.rotationYaw, entityPlayer.rotationPitch);
            GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_BEGIN_CRYOGENIC_SLEEP, new Object[] { this.xCoord, this.yCoord, this.zCoord }), (EntityPlayerMP) entityPlayer);
            return true;
        case NOT_POSSIBLE_NOW:
            entityPlayer.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("gui.cryogenic.chat.cantUse", GCPlayerStats.get((EntityPlayerMP) entityPlayer).cryogenicChamberCooldown / 20)));
            return false;
        default:
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

            if (GCPlayerStats.get((EntityPlayerMP) entityPlayer).cryogenicChamberCooldown > 0)
            {
                return EnumStatus.NOT_POSSIBLE_NOW;
            }
        }

        if (entityPlayer.isRiding())
        {
            entityPlayer.mountEntity((Entity) null);
        }

        entityPlayer.setPosition(this.xCoord + 0.5F, this.yCoord + 1.9F, this.zCoord + 0.5F);

        entityPlayer.sleeping = true;
        entityPlayer.sleepTimer = 0;
        entityPlayer.playerLocation = new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
        entityPlayer.motionX = entityPlayer.motionZ = entityPlayer.motionY = 0.0D;

        if (!this.worldObj.isRemote)
        {
            this.worldObj.updateAllPlayersSleepingFlag();
        }

        return EnumStatus.OK;
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
    }

    @Override
    public void onCreate(BlockVec3 placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();

        for (int y = 0; y < 3; y++)
        {
            final BlockVec3 vecToAdd = new BlockVec3(placedPosition.x, placedPosition.y + y, placedPosition.z);

            if (!vecToAdd.equals(placedPosition))
            {
                ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 5);
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockVec3 thisBlock = new BlockVec3(this);

        int x1 = 0;
        int x2 = 0;
        int z1 = 0;
        int z2 = 0;

        switch (this.getBlockMetadata() - BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
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

        int fakeBlockCount = 0;
        for (int x = x1; x <= x2; x++)
        {
            for (int z = z1; z <= z2; z++)
            {
                for (int y = 0; y < 4; y++)
                {
                    if (x == 0 && y == 0 && z == 0)
                    {
                        continue;
                    }

                    if (this.worldObj.getBlock(thisBlock.x + x, thisBlock.y + y, thisBlock.z + z) == GCBlocks.fakeBlock)
                    {
                        fakeBlockCount++;
                    }
                }
            }
        }

        if (fakeBlockCount == 0)
        {
            return;
        }

        for (int x = x1; x <= x2; x++)
        {
            for (int z = z1; z <= z2; z++)
            {
                for (int y = 0; y < 4; y++)
                {
                    if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
                    {
                        FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.x + x, thisBlock.y + y, thisBlock.z + z, MarsBlocks.machine, Block.getIdFromBlock(MarsBlocks.machine) >> 12 & 255);
                    }
                    this.worldObj.func_147480_a(thisBlock.x + x, thisBlock.y + y, thisBlock.z + z, x == 0 && z == 0);
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