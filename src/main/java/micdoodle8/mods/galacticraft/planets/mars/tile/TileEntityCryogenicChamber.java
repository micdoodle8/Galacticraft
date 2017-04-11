package micdoodle8.mods.galacticraft.planets.mars.tile;

import java.util.LinkedList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityCryogenicChamber extends TileEntityMulti implements IMultiBlock
{
    public boolean isOccupied;
    private boolean initialised;

    public TileEntityCryogenicChamber()
    {
        super(null);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 3, getPos().getZ() + 2);
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        if (this.worldObj.isRemote)
        {
            return false;
        }

        EntityPlayer.SleepResult enumstatus = this.sleepInBedAt(entityPlayer, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());

        switch (enumstatus)
        {
        case OK:
            ((EntityPlayerMP) entityPlayer).connection.setPlayerLocation(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, entityPlayer.rotationYaw, entityPlayer.rotationPitch);
            GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_BEGIN_CRYOGENIC_SLEEP, GCCoreUtil.getDimensionID(entityPlayer.worldObj), new Object[] { this.getPos() }), (EntityPlayerMP) entityPlayer);
            return true;
        case NOT_POSSIBLE_NOW:
            GCPlayerStats stats = GCPlayerStats.get(entityPlayer);
            entityPlayer.addChatMessage(new TextComponentString(GCCoreUtil.translateWithFormat("gui.cryogenic.chat.cant_use", stats.getCryogenicChamberCooldown() / 20)));
            return false;
        default:
            return false;
        }
    }

    public EntityPlayer.SleepResult sleepInBedAt(EntityPlayer entityPlayer, int par1, int par2, int par3)
    {
        if (!this.worldObj.isRemote)
        {
            if (entityPlayer.isPlayerSleeping() || !entityPlayer.isEntityAlive())
            {
                return EntityPlayer.SleepResult.OTHER_PROBLEM;
            }

            if (this.worldObj.getBiome(new BlockPos(par1, par2, par3)) == Biomes.HELL)
            {
                return EntityPlayer.SleepResult.NOT_POSSIBLE_HERE;
            }

            GCPlayerStats stats = GCPlayerStats.get(entityPlayer);
            if (stats.getCryogenicChamberCooldown() > 0)
            {
                return EntityPlayer.SleepResult.NOT_POSSIBLE_NOW;
            }
        }

        if (entityPlayer.isRiding())
        {
            entityPlayer.dismountRidingEntity();
        }

        entityPlayer.setPosition(this.getPos().getX() + 0.5F, this.getPos().getY() + 1.9F, this.getPos().getZ() + 0.5F);

        entityPlayer.sleeping = true;
        entityPlayer.sleepTimer = 0;
        entityPlayer.bedLocation = new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
        entityPlayer.motionX = entityPlayer.motionZ = entityPlayer.motionY = 0.0D;

        if (!this.worldObj.isRemote)
        {
            this.worldObj.updateAllPlayersSleepingFlag();
        }

        return EntityPlayer.SleepResult.OK;
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return true;
//    }

    @Override
    public void update()
    {
        if (!this.initialised)
        {
            this.initialised = this.initialiseMultiTiles(this.getPos(), this.worldObj);
        }
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();

        List<BlockPos> positions = new LinkedList();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, EnumBlockMultiType.CRYO_CHAMBER);
    }
    
    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.worldObj.getHeight() - 1;

        for (int y = 1; y < 3; y++)
        {
            if (placedPosition.getY() + y > buildHeight)
            {
                return;
            }
            positions.add(new BlockPos(placedPosition.getX(), placedPosition.getY() + y, placedPosition.getZ()));
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();
        List<BlockPos> positions = new LinkedList();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            IBlockState stateAt = this.worldObj.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock && (EnumBlockMultiType) stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.CRYO_CHAMBER)
            {
                if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
                {
                    FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, this.worldObj.getBlockState(pos));
                }
                this.worldObj.destroyBlock(pos, false);
            }
        }
        this.worldObj.destroyBlock(thisBlock, true);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.isOccupied = nbt.getBoolean("IsChamberOccupied");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setBoolean("IsChamberOccupied", this.isOccupied);
        return nbt;
    }
}
