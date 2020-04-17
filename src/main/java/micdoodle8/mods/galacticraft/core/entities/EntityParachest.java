package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.ArrayList;
import java.util.Collections;

public class EntityParachest extends Entity implements IPacketReceiver
{
    public NonNullList<ItemStack> cargo;
    public int fuelLevel;
    private boolean placedChest;
    public EnumDyeColor color = EnumDyeColor.WHITE;

    public EntityParachest(World world, NonNullList<ItemStack> cargo, int fuelLevel)
    {
        this(world);
        this.cargo = NonNullList.withSize(cargo.size(), ItemStack.EMPTY);
        Collections.copy(this.cargo, cargo);
        this.placedChest = false;
        this.fuelLevel = fuelLevel;
    }

    public EntityParachest(World world)
    {
        super(world);
        this.setSize(1.0F, 1.0F);
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        int size = 56;
        if (nbt.hasKey("CargoLength"))
        {
            size = nbt.getInteger("CargoLength");
        }
        this.cargo = NonNullList.withSize(size, ItemStack.EMPTY);

        ItemStackHelper.loadAllItems(nbt, this.cargo);

        this.placedChest = nbt.getBoolean("placedChest");
        this.fuelLevel = nbt.getInteger("FuelLevel");

        if (nbt.hasKey("color"))
        {
            this.color = EnumDyeColor.byDyeDamage(nbt.getInteger("color"));
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        if (world.isRemote) return;
        nbt.setInteger("CargoLength", this.cargo.size());
        ItemStackHelper.saveAllItems(nbt, this.cargo);

        nbt.setBoolean("placedChest", this.placedChest);
        nbt.setInteger("FuelLevel", this.fuelLevel);
        nbt.setInteger("color", this.color.getDyeDamage());
    }

    @Override
    public void onUpdate()
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
                        final EntityItem e = new EntityItem(this.world, this.posX, this.posY, this.posZ, stack);
                        this.world.spawnEntity(e);
                    }
                }

                this.placedChest = true;
                this.setDead();
            }
            else
            {
                this.motionY = -0.35;
            }

            this.move(MoverType.SELF, 0, this.motionY, 0);
        }

        if (!this.world.isRemote && this.ticksExisted % 5 == 0)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new NetworkRegistry.TargetPoint(GCCoreUtil.getDimensionID(this.world), this.posX, this.posY, this.posZ, 64.0));
        }
    }

    private boolean tryPlaceAtPos(BlockPos pos)
    {
        IBlockState state = this.world.getBlockState(pos);
        Block block = state.getBlock();

        if (block.getMaterial(state).isReplaceable())
        {
            if (this.placeChest(pos))
            {
                this.placedChest = true;
                this.setDead();
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

                    chest.fuelTank.fill(FluidRegistry.getFluidStack(GCFluids.fluidFuel.getName().toLowerCase(), this.fuelLevel), true);
                }
                else
                {
                    for (ItemStack stack : this.cargo)
                    {
                        final EntityItem e = new EntityItem(this.world, this.posX, this.posY, this.posZ, stack);
                        this.world.spawnEntity(e);
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
            sendData.add(this.color.getDyeDamage());
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.world.isRemote)
        {
            this.color = EnumDyeColor.byDyeDamage(buffer.readInt());
        }
    }
}
