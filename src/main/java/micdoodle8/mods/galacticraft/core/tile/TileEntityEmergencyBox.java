package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.*;
import micdoodle8.mods.galacticraft.core.blocks.BlockEmergencyBox;
import micdoodle8.mods.galacticraft.core.items.ItemEmergencyKit;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TileEntityEmergencyBox extends TileEntity implements ITickableTileEntity, IPacketReceiver
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.emergencyBox)
    public static TileEntityType<TileEntityEmergencyBox> TYPE;

    private static final float SPEED = 4F;
    public float angleA = 0F;
    public float angleB = 0F;
    public float angleC = 0F;
    public float angleD = 0F;
    public float lastAngleA = 0F;
    public float lastAngleB = 0F;
    public float lastAngleC = 0F;
    public float lastAngleD = 0F;

    private boolean openN = false;
    private boolean openW = false;
    private boolean openS = false;
    private boolean openE = false;
    private int cooldown = 0;

    private final HashSet<BlockVec3> airToRestore = new HashSet<>();
    private boolean activated = false;
    private Vec3d vec3Centre;
    private Vec3d thisVec3;
    private AxisAlignedBB mobsAABB;

    public TileEntityEmergencyBox()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.activated)
        {
            this.activated = true;
            this.setLightBlocks();
            this.thisVec3 = new Vec3d(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
            this.vec3Centre = new Vec3d(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D);
            this.mobsAABB = new AxisAlignedBB(this.pos.getX() - 14, this.pos.getY() - 7, this.pos.getZ() - 14, this.pos.getX() + 14, this.pos.getY() + 7, this.pos.getZ() + 14);
        }

        if (this.world.isRemote)
        {
            if (this.openN && this.angleA < 90F)
            {
                this.lastAngleA = this.angleA;
                this.angleA += SPEED;
                if (this.angleA > 90F)
                {
                    this.angleA = 90F;
                }
            }
            if (this.openW && this.angleB < 90F)
            {
                this.lastAngleB = this.angleB;
                this.angleB += SPEED;
                if (this.angleB > 90F)
                {
                    this.angleB = 90F;
                }
            }
            if (this.openS && this.angleC < 90F)
            {
                this.lastAngleC = this.angleC;
                this.angleC += SPEED;
                if (this.angleC > 90F)
                {
                    this.angleC = 90F;
                }
            }
            if (this.openE && this.angleD < 90F)
            {
                this.lastAngleD = this.angleD;
                this.angleD += SPEED;
                if (this.angleD > 90F)
                {
                    this.angleD = 90F;
                }
            }

            if (!this.openN && this.angleA > 0F)
            {
                this.lastAngleA = this.angleA;
                this.angleA -= SPEED;
                if (this.angleA < 0F)
                {
                    this.angleA = 0F;
                }
            }
            if (!this.openW && this.angleB > 0F)
            {
                this.lastAngleB = this.angleB;
                this.angleB -= SPEED;
                if (this.angleB < 0F)
                {
                    this.angleB = 0F;
                }
            }
            if (!this.openS && this.angleC > 0F)
            {
                this.lastAngleC = this.angleC;
                this.angleC -= SPEED;
                if (this.angleC < 0F)
                {
                    this.angleC = 0F;
                }
            }
            if (!this.openE && this.angleD > 0F)
            {
                this.lastAngleD = this.angleD;
                this.angleD -= SPEED;
                if (this.angleD < 0F)
                {
                    this.angleD = 0F;
                }
            }
        }
        else
        {
            if (this.cooldown > 0)
            {
                this.cooldown--;
            }

            boolean updateRequired = false;
            if (this.openN)
            {
                boolean clash = false;
                BlockPos testPos = this.pos.north(1);
                BlockState bs = this.world.getBlockState(testPos);
                if (!(bs.getBlock() instanceof AirBlock))
                {
                    VoxelShape neighbour = bs.getShape(this.world, testPos);
                    if (neighbour == VoxelShapes.fullCube())
                    {
                        clash = true;
                    }
                    VoxelShape check = VoxelShapes.create(0.125D, 0.125D, 11 / 16D, 0.875D, 0.875D, 1D);
                    if (neighbour != VoxelShapes.empty())
                    {
                        clash = check.getBoundingBox().intersects(neighbour.getBoundingBox());
                    }
                }
                if (clash)
                {
                    this.openN = false;
                    updateRequired = true;
                }
            }
            if (this.openS)
            {
                boolean clash = false;
                BlockPos testPos = this.pos.south(1);
                BlockState bs = this.world.getBlockState(testPos);
                if (!(bs.getBlock() instanceof AirBlock))
                {
                    VoxelShape neighbour = bs.getShape(this.world, testPos);
                    if (neighbour == VoxelShapes.fullCube())
                    {
                        clash = true;
                    }
                    VoxelShape check = VoxelShapes.create(0.125D, 0.125D, 0D, 0.875D, 0.875D, 5 / 16D);
                    if (neighbour != VoxelShapes.empty())
                    {
                        clash = check.getBoundingBox().intersects(neighbour.getBoundingBox());
                    }
                }
                if (clash)
                {
                    this.openS = false;
                    updateRequired = true;
                }
            }
            if (this.openW)
            {
                boolean clash = false;
                BlockPos testPos = this.pos.west(1);
                BlockState bs = this.world.getBlockState(testPos);
                if (!(bs.getBlock() instanceof AirBlock))
                {
                    VoxelShape neighbour = bs.getShape(this.world, testPos);
                    if (neighbour == VoxelShapes.fullCube())
                    {
                        clash = true;
                    }
                    VoxelShape check = VoxelShapes.create(11 / 16D, 0.125D, 0.125D, 1D, 0.875D, 0.875D);
                    if (neighbour != VoxelShapes.empty())
                    {
                        clash = check.getBoundingBox().intersects(neighbour.getBoundingBox());
                    }
                }
                if (clash)
                {
                    this.openW = false;
                    updateRequired = true;
                }
            }
            if (this.openE)
            {
                boolean clash = false;
                BlockPos testPos = this.pos.east(1);
                BlockState bs = this.world.getBlockState(testPos);
                if (!(bs.getBlock() instanceof AirBlock))
                {
                    VoxelShape neighbour = bs.getShape(this.world, testPos);
                    if (neighbour == VoxelShapes.fullCube())
                    {
                        clash = true;
                    }
                    VoxelShape check = VoxelShapes.create(0D, 0.125D, 0.125D, 5 / 16D, 0.875D, 0.875D);
                    if (neighbour != VoxelShapes.empty())
                    {
                        clash = check.getBoundingBox().intersects(neighbour.getBoundingBox());
                    }
                }
                if (clash)
                {
                    this.openE = false;
                    updateRequired = true;
                }
            }

            if (updateRequired)
            {
                this.updateClients();
            }

            if (this.world.rand.nextInt(15) == 0)
            {
                this.scareMobs();
            }
        }
    }

    private void scareMobs()
    {
        List<Entity> moblist = this.world.getEntitiesInAABBexcluding(null, mobsAABB, (entity) -> entity instanceof IMob);
        if (!moblist.isEmpty())
        {
            for (Entity entry : moblist)
            {
                if (!(entry instanceof CreatureEntity && entry instanceof IEntityBreathable))
                {
                    continue;
                }
                CreatureEntity mob = (CreatureEntity) entry;
                PathNavigator nav = mob.getNavigator();

                Vec3d vecNewTarget = RandomPositionGenerator.findRandomTargetBlockAwayFrom(mob, 12, 5, vec3Centre);
                if (vecNewTarget == null)
                {
                    vecNewTarget = RandomPositionGenerator.findRandomTargetBlockAwayFrom(mob, 14, 7, vec3Centre);
                    if (vecNewTarget == null)
                    {
                        continue;
                    }
                }
                double distanceNew = vecNewTarget.squareDistanceTo(thisVec3);
                double distanceCurrent = thisVec3.squareDistanceTo(new Vec3d(mob.getPosX(), mob.getPosY(), mob.getPosZ()));
                if (distanceNew > distanceCurrent)
                {
                    Vec3d vecOldTarget = null;
                    if (nav.getPath() != null && !nav.getPath().isFinished())
                    {
                        vecOldTarget = nav.getPath().getPosition(mob);
                    }
                    if (vecOldTarget == null || distanceCurrent > vecOldTarget.squareDistanceTo(thisVec3))
                    {
                        nav.tryMoveToXYZ(vecNewTarget.x, vecNewTarget.y, vecNewTarget.z, 1.1D);
                    }
                }
            }
        }
    }

    public float getAngleA(float f)
    {
        float result = this.angleA + (this.angleA - this.lastAngleA) * f;
        if (result > 90F)
        {
            result = 90F;
        }
        if (result < 0F)
        {
            result = 0F;
        }
        return result;
    }

    public float getAngleB(float f)
    {
        float result = this.angleB + (this.angleB - this.lastAngleB) * f;
        if (result > 90F)
        {
            result = 90F;
        }
        if (result < 0F)
        {
            result = 0F;
        }
        return result;
    }

    public float getAngleC(float f)
    {
        float result = this.angleC + (this.angleC - this.lastAngleC) * f;
        if (result > 90F)
        {
            result = 90F;
        }
        if (result < 0F)
        {
            result = 0F;
        }
        return result;
    }

    public float getAngleD(float f)
    {
        float result = this.angleD + (this.angleD - this.lastAngleD) * f;
        if (result > 90F)
        {
            result = 90F;
        }
        if (result < 0F)
        {
            result = 0F;
        }
        return result;
    }

    public void click(PlayerEntity player, Direction side, boolean kitted)
    {
        switch (side)
        {
            case NORTH:
                if (this.openN && this.cooldown == 0)
                {
                    if (kitted)
                    {
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(GCItems.emergencyKit), 0);
                        this.world.setBlockState(this.pos, GCBlocks.emergencyBox.getDefaultState(), 3);
                        break;
                    }
                    else
                    {
                        ItemStack stack = player.inventory.getCurrentItem();
                        if (!stack.isEmpty() && stack.getItem() instanceof ItemEmergencyKit)
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                            this.world.setBlockState(this.pos, GCBlocks.emergencyBox.getDefaultState().with(BlockEmergencyBox.KIT, true), 3);
                            this.openW = false;
                            this.openS = false;
                            this.openE = false;
                        }
                    }
                }
                this.cooldown = 12;
                this.openN = !this.openN;
                this.updateClients();
                break;
            case WEST:
                if (this.openW && this.cooldown == 0)
                {
                    if (kitted)
                    {
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(GCItems.emergencyKit), 0);
                        this.world.setBlockState(this.pos, GCBlocks.emergencyBox.getDefaultState(), 3);
                        break;
                    }
                    else
                    {
                        ItemStack stack = player.inventory.getCurrentItem();
                        if (!stack.isEmpty() && stack.getItem() instanceof ItemEmergencyKit)
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                            this.world.setBlockState(this.pos, GCBlocks.emergencyBox.getDefaultState().with(BlockEmergencyBox.KIT, true), 3);
                            this.openN = false;
                            this.openS = false;
                            this.openE = false;
                        }
                    }
                }
                this.cooldown = 12;
                this.openW = !this.openW;
                this.updateClients();
                break;
            case SOUTH:
                if (this.openS && this.cooldown == 0)
                {
                    if (kitted)
                    {
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(GCItems.emergencyKit), 0);
                        this.world.setBlockState(this.pos, GCBlocks.emergencyBox.getDefaultState(), 3);
                        break;
                    }
                    else
                    {
                        ItemStack stack = player.inventory.getCurrentItem();
                        if (!stack.isEmpty() && stack.getItem() instanceof ItemEmergencyKit)
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                            this.world.setBlockState(this.pos, GCBlocks.emergencyBox.getDefaultState().with(BlockEmergencyBox.KIT, true), 3);
                            this.openN = false;
                            this.openW = false;
                            this.openE = false;
                        }
                    }
                }
                this.cooldown = 12;
                this.openS = !this.openS;
                this.updateClients();
                break;
            case EAST:
                if (this.openE && this.cooldown == 0)
                {
                    if (kitted)
                    {
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(GCItems.emergencyKit), 0);
                        this.world.setBlockState(this.pos, GCBlocks.emergencyBox.getDefaultState(), 3);
                        break;
                    }
                    else
                    {
                        ItemStack stack = player.inventory.getCurrentItem();
                        if (!stack.isEmpty() && stack.getItem() instanceof ItemEmergencyKit)
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                            this.world.setBlockState(this.pos, GCBlocks.emergencyBox.getDefaultState().with(BlockEmergencyBox.KIT, true), 3);
                            this.openN = false;
                            this.openW = false;
                            this.openS = false;
                        }
                    }
                }
                this.cooldown = 12;
                this.openE = !this.openE;
                this.updateClients();
                break;
            default:
        }
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        int data = nbt.getInt("open");
        this.openN = (data & 1) == 1;
        this.openW = (data & 2) == 2;
        this.openS = (data & 4) == 4;
        this.openE = (data & 8) == 8;
        if (GCCoreUtil.getEffectiveSide() == LogicalSide.SERVER)
        {
            this.airToRestore.clear();
            ListNBT airBlocks = nbt.getList("air", 10);
            if (airBlocks.size() > 0)
            {
                for (int j = airBlocks.size() - 1; j >= 0; j--)
                {
                    CompoundNBT tag1 = airBlocks.getCompound(j);
                    this.airToRestore.add(BlockVec3.read(tag1));
                }
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        int data = (this.openN ? 1 : 0) + (this.openW ? 2 : 0) + (this.openS ? 4 : 0) + (this.openE ? 8 : 0);
        nbt.putInt("open", data);

        ListNBT airBlocks = new ListNBT();
        for (BlockVec3 vec : this.airToRestore)
        {
            CompoundNBT tag = new CompoundNBT();
            vec.write(tag);
            airBlocks.add(tag);
        }
        nbt.put("air", airBlocks);
        return nbt;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        if (this.world.isRemote)
        {
            return;
        }

        int data = (this.openN ? 1 : 0) + (this.openW ? 2 : 0) + (this.openS ? 4 : 0) + (this.openE ? 8 : 0);
        sendData.add((byte) data);
        sendData.addAll(this.airToRestore);
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.world.isRemote)
        {
            try
            {
                int data = buffer.readByte();
                this.openN = (data & 1) == 1;
                this.openW = (data & 2) == 2;
                this.openS = (data & 4) == 4;
                this.openE = (data & 8) == 8;
                while (buffer.readableBytes() >= 12)
                {
                    int x = buffer.readInt();
                    int y = buffer.readInt();
                    int z = buffer.readInt();
                    this.airToRestore.add(new BlockVec3(x, y, z));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoad()
    {
        if (this.world.isRemote)
        {
            //Request any networked information from server on first client tick
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
    }

    private void updateClients()
    {
        GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new PacketDistributor.TargetPoint(getPos().getX(), getPos().getY(), getPos().getZ(), 128, GCCoreUtil.getDimensionType(this.world)));
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState)
//    {
//        return oldState.getBlock() != newState.getBlock();
//    }

    private void brightenAir(BlockPos blockpos, BlockState newState)
    {
        Chunk chunk = this.world.getChunkAt(blockpos);
        BlockState oldState = chunk.setBlockState(blockpos, newState, false);
        if (this.world.isRemote && oldState != null)
        {
            this.world.markAndNotifyBlock(blockpos, chunk, oldState, newState, 2);
        }
        //No block tick on server - not necessary for changing air to air (also must not trigger a sealer edge check!)
        this.airToRestore.add(new BlockVec3(blockpos));
//        this.world.checkLightFor(LightType.BLOCK, blockpos); TODO Lighting
        this.markDirty();
    }

    private void setDarkerAir(BlockVec3 vec)
    {
        BlockPos blockpos = vec.toBlockPos();
        Block b = this.world.getBlockState(blockpos).getBlock();
        BlockState newState;
        if (b == GCBlocks.brightAir)
        {
            newState = Blocks.AIR.getDefaultState();
        }
        else if (b == GCBlocks.brightBreatheableAir)
        {
            newState = GCBlocks.breatheableAir.getDefaultState();
        }
        else
        {
            return;
        }

        Chunk chunk = this.world.getChunkAt(blockpos);
        BlockState oldState = chunk.setBlockState(blockpos, newState, false);
        if (this.world.isRemote && oldState != null)
        {
            this.world.markAndNotifyBlock(blockpos, chunk, oldState, newState, 2);
        }
//        this.world.checkLightFor(LightType.BLOCK, blockpos);  TODO Lighting
    }

    private void revertAir()
    {
        if (this.airToRestore.isEmpty())
        {
            return;
        }

        int index = 0;
        for (BlockVec3 vec : this.airToRestore)
        {
            this.setDarkerAir(vec);
        }
        this.airToRestore.clear();
        this.markDirty();
    }

    @Override
    public void remove()
    {
        this.revertAir();
        super.remove();
    }

    private void setLightBlocks()
    {
        this.setLightBlock(new BlockPos(this.pos.getX() - 6, this.pos.getY(), this.pos.getZ()));
        this.setLightBlock(new BlockPos(this.pos.getX() + 6, this.pos.getY(), this.pos.getZ()));
        this.setLightBlock(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 6));
        this.setLightBlock(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 6));
        this.setLightBlock(new BlockPos(this.pos.getX() - 5, this.pos.getY(), this.pos.getZ() - 5));
        this.setLightBlock(new BlockPos(this.pos.getX() - 5, this.pos.getY(), this.pos.getZ() + 5));
        this.setLightBlock(new BlockPos(this.pos.getX() + 5, this.pos.getY(), this.pos.getZ() + 5));
        this.setLightBlock(new BlockPos(this.pos.getX() + 5, this.pos.getY(), this.pos.getZ() - 5));
    }

    private boolean setLightBlock(BlockPos blockPos)
    {
        BlockState bs = this.world.getBlockState(blockPos);
        if (bs.getBlock() == Blocks.AIR)
        {
            this.brightenAir(blockPos, GCBlocks.brightAir.getDefaultState());
            return true;
        }
        else if (bs.getBlock() == GCBlocks.breatheableAir)
        {
            this.brightenAir(blockPos, GCBlocks.brightBreatheableAir.getDefaultState());
            return true;
        }

        blockPos = blockPos.up(1);
        bs = this.world.getBlockState(blockPos);
        if (bs.getBlock() == Blocks.AIR)
        {
            this.brightenAir(blockPos, GCBlocks.brightAir.getDefaultState());
            return true;
        }
        else if (bs.getBlock() == GCBlocks.breatheableAir)
        {
            this.brightenAir(blockPos, GCBlocks.brightBreatheableAir.getDefaultState());
            return true;
        }

        return false;
    }
}
