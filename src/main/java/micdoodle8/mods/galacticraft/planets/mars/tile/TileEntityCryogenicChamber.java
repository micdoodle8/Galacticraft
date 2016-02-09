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
import net.minecraft.world.biome.BiomeGenBase;

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

            if (this.worldObj.getBiomeGenForCoords(par1, par3) == BiomeGenBase.hell)
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
        int buildHeight = this.worldObj.getHeight() - 1;

        for (int y = 0; y < 3; y++)
        {
            if (placedPosition.y + y > buildHeight) return;
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
        int fakeBlockCount = 0;

        for (int y = 0; y < 3; y++)
        {
            if (y == 0)
            {
                continue;
            }

            if (this.worldObj.getBlock(thisBlock.x, thisBlock.y + y, thisBlock.z) == GCBlocks.fakeBlock)
            {
                fakeBlockCount++;
            }
        }

        if (fakeBlockCount == 0)
        {
            return;
        }

        for (int y = 0; y < 3; y++)
        {
            if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
            {
                FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.x, thisBlock.y + y, thisBlock.z, MarsBlocks.machine, Block.getIdFromBlock(MarsBlocks.machine) >> 12 & 255);
            }
            this.worldObj.func_147480_a(thisBlock.x, thisBlock.y + y, thisBlock.z, true);
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
