package micdoodle8.mods.galacticraft.planets.mars.tile;

import com.mojang.datafixers.util.Either;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFake;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Unit;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedList;
import java.util.List;

public class TileEntityCryogenicChamber extends TileEntityFake implements IMultiBlock
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.cryoChamber)
    public static TileEntityType<TileEntityCryogenicChamber> TYPE;

    public boolean isOccupied;
    private boolean initialised;

    public TileEntityCryogenicChamber()
    {
        super(TYPE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 3, getPos().getZ() + 2);
    }

    @Override
    public boolean onActivated(PlayerEntity entityPlayer)
    {
        if (this.world.isRemote)
        {
            return false;
        }

        Either<PlayerEntity.SleepResult, Unit> enumstatus = this.sleepInBedAt(entityPlayer, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());

        enumstatus.ifLeft((result) -> {
            ((ServerPlayerEntity) entityPlayer).connection.setPlayerLocation(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, entityPlayer.rotationYaw, entityPlayer.rotationPitch);
            GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_BEGIN_CRYOGENIC_SLEEP, GCCoreUtil.getDimensionID(entityPlayer.world), new Object[] { this.getPos() }), (ServerPlayerEntity) entityPlayer);
        });

        enumstatus.ifRight((result) -> {
            GCPlayerStats stats = GCPlayerStats.get(entityPlayer);
            entityPlayer.sendMessage(new StringTextComponent(GCCoreUtil.translateWithFormat("gui.cryogenic.chat.cant_use", stats.getCryogenicChamberCooldown() / 20)));
        });

        return enumstatus.left().isPresent();
    }

    public Either<PlayerEntity.SleepResult, Unit> sleepInBedAt(PlayerEntity entityPlayer, int par1, int par2, int par3)
    {
        if (!this.world.isRemote)
        {
            if (entityPlayer.isSleeping() || !entityPlayer.isAlive())
            {
                return Either.left(PlayerEntity.SleepResult.OTHER_PROBLEM);
            }

            if (this.world.getBiome(new BlockPos(par1, par2, par3)) == Biomes.NETHER)
            {
                return Either.left(PlayerEntity.SleepResult.NOT_POSSIBLE_HERE);
            }

            GCPlayerStats stats = GCPlayerStats.get(entityPlayer);
            if (stats.getCryogenicChamberCooldown() > 0)
            {
                return Either.left(PlayerEntity.SleepResult.NOT_POSSIBLE_NOW);
            }
        }

        if (entityPlayer.getRidingEntity() != null)
        {
            entityPlayer.stopRiding();
        }

        entityPlayer.setPosition(this.getPos().getX() + 0.5F, this.getPos().getY() + 1.9F, this.getPos().getZ() + 0.5F);

        entityPlayer.startSleeping(new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));
//        entityPlayer.sleeping = true;
        entityPlayer.sleepTimer = 0;
//        entityPlayer.bedLocation = new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
        entityPlayer.setMotion(0.0, 0.0, 0.0);

        if (!this.world.isRemote)
        {
            ((ServerWorld) this.world).updateAllPlayersSleepingFlag();
        }

        return Either.right(Unit.INSTANCE);
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return true;
//    }

    @Override
    public void tick()
    {
        if (!this.initialised)
        {
            this.initialised = this.initialiseMultiTiles(this.getPos(), this.world);
        }
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();

        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }
    
    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.CRYO_CHAMBER;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.world.getHeight() - 1;

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
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            BlockState stateAt = this.world.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock && stateAt.get(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.CRYO_CHAMBER)
            {
                if (this.world.isRemote && this.world.rand.nextDouble() < 0.1D)
                {
                    Minecraft.getInstance().particles.addBlockDestroyEffects(pos, this.world.getBlockState(pos));
                }
                this.world.destroyBlock(pos, false);
            }
        }
        this.world.destroyBlock(thisBlock, true);
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.isOccupied = nbt.getBoolean("IsChamberOccupied");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putBoolean("IsChamberOccupied", this.isOccupied);
        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }
}
