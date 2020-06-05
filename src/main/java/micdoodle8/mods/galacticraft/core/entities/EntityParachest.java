package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.fluid.GCFluidRegistry;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collections;

public class EntityParachest extends Entity implements IPacketReceiver
{
    public NonNullList<ItemStack> cargo;
    public int fuelLevel;
    private boolean placedChest;
    public DyeColor color = DyeColor.WHITE;

    public EntityParachest(EntityType<EntityParachest> type, World world, NonNullList<ItemStack> cargo, int fuelLevel)
    {
        super(type, world);
        this.cargo = NonNullList.withSize(cargo.size(), ItemStack.EMPTY);
        Collections.copy(this.cargo, cargo);
        this.placedChest = false;
        this.fuelLevel = fuelLevel;
    }

    public EntityParachest(EntityType<EntityParachest> type, World world)
    {
        super(type, world);
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    @Override
    protected void registerData()
    {
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        int size = 56;
        if (nbt.contains("CargoLength"))
        {
            size = nbt.getInt("CargoLength");
        }
        this.cargo = NonNullList.withSize(size, ItemStack.EMPTY);

        ItemStackHelper.loadAllItems(nbt, this.cargo);

        this.placedChest = nbt.getBoolean("placedChest");
        this.fuelLevel = nbt.getInt("FuelLevel");

        if (nbt.contains("color"))
        {
            this.color = DyeColor.values()[nbt.getInt("color")];
        }
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        if (world.isRemote) return;
        nbt.putInt("CargoLength", this.cargo.size());
        ItemStackHelper.saveAllItems(nbt, this.cargo);

        nbt.putBoolean("placedChest", this.placedChest);
        nbt.putInt("FuelLevel", this.fuelLevel);
        nbt.putInt("color", this.color.ordinal());
    }

    @Override
    public void tick()
    {
        if (!this.placedChest)
        {
            if (this.onGround && !this.world.isRemote)
            {
                for (int i = 0; i < 100; i++)
                {
                    final int x = MathHelper.floor(this.posX);
                    final int y = MathHelper.floor(this.posY);
                    final int z = MathHelper.floor(this.posZ);

                    if (tryPlaceAtPos(new BlockPos(x, y + i, z)))
                    {
                        return;
                    }
                }

                for (int size = 1; size < 5; ++size)
                {
                    for (int xOff = -size; xOff <= size; xOff++)
                    {
                        for (int yOff = -size; yOff <= size; yOff++)
                        {
                            for (int zOff = -size; zOff <= size; zOff++)
                            {
                                final int x = MathHelper.floor(this.posX) + xOff;
                                final int y = MathHelper.floor(this.posY) + yOff;
                                final int z = MathHelper.floor(this.posZ) + zOff;

                                if (tryPlaceAtPos(new BlockPos(x, y, z)))
                                {
                                    return;
                                }
                            }
                        }
                    }
                }

                if (this.cargo != null)
                {
                    for (final ItemStack stack : this.cargo)
                    {
                        final ItemEntity e = new ItemEntity(this.world, this.posX, this.posY, this.posZ, stack);
                        this.world.addEntity(e);
                    }
                }

                this.placedChest = true;
                this.remove();
            }
            else
            {
                this.setMotion(this.getMotion().add(0.0, -0.35, 0.0));
            }

            this.setMotion(0.0, this.getMotion().y, 0.0);
            this.move(MoverType.SELF, this.getMotion());
        }

        if (!this.world.isRemote && this.ticksExisted % 5 == 0)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new PacketDistributor.TargetPoint(this.posX, this.posY, this.posZ, 64.0, GCCoreUtil.getDimensionID(this.world)));
        }
    }

    private boolean tryPlaceAtPos(BlockPos pos)
    {
        BlockState state = this.world.getBlockState(pos);
        Block block = state.getBlock();

        if (block.getMaterial(state).isReplaceable())
        {
            if (this.placeChest(pos))
            {
                this.placedChest = true;
                this.remove();
                return true;
            }
        }
        return false;
    }

    private boolean placeChest(BlockPos pos)
    {
        if (this.world.setBlockState(pos, GCBlocks.parachest.getDefaultState(), 3))
        {
            if (this.cargo != null)
            {
                final TileEntity te = this.world.getTileEntity(pos);

                if (te instanceof TileEntityParaChest)
                {
                    final TileEntityParaChest chest = (TileEntityParaChest) te;

                    chest.inventory = NonNullList.withSize(this.cargo.size() + 1, ItemStack.EMPTY);
                    chest.color = this.color;

                    Collections.copy(chest.getInventory(), this.cargo);

                    chest.fuelTank.fill(new FluidStack(GCFluids.FUEL.getFluid(), this.fuelLevel), IFluidHandler.FluidAction.EXECUTE);
                }
                else
                {
                    for (ItemStack stack : this.cargo)
                    {
                        final ItemEntity e = new ItemEntity(this.world, this.posX, this.posY, this.posZ, stack);
                        this.world.addEntity(e);
                    }
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        if (!this.world.isRemote)
        {
            sendData.add(this.color.getId());
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.world.isRemote)
        {
            this.color = DyeColor.byId(buffer.readInt());
        }
    }
}
