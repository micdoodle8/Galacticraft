package micdoodle8.mods.galacticraft.planets.mars.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerTerraformer;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityTerraformer extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IBubbleProvider, IFluidHandlerWrapper
{
    private final int tankCapacity = 2000;
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank waterTank = new FluidTank(this.tankCapacity);
    public boolean active;
    public boolean lastActive;
    public static final int WATTS_PER_TICK = 1;
    private NonNullList<ItemStack> stacks = NonNullList.withSize(14, ItemStack.EMPTY);
    private ArrayList<BlockPos> terraformableBlocksList = new ArrayList<BlockPos>();
    private ArrayList<BlockPos> grassBlockList = new ArrayList<BlockPos>();
    private ArrayList<BlockPos> grownTreesList = new ArrayList<BlockPos>();
    @NetworkedField(targetSide = Side.CLIENT)
    public int terraformableBlocksListSize = 0; // used for server->client ease
    @NetworkedField(targetSide = Side.CLIENT)
    public int grassBlocksListSize = 0; // used for server->client ease
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean treesDisabled;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean grassDisabled;
    public final double MAX_SIZE = 15.0D;
    private int[] useCount = new int[2];
    private int saplingIndex = 6;
    public float bubbleSize;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean shouldRenderBubble = true;

    public TileEntityTerraformer()
    {
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 60 : 30);
    }

    public int getScaledWaterLevel(int i)
    {
        final double fuelLevel = this.waterTank.getFluid() == null ? 0 : this.waterTank.getFluid().amount;

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
    }

    public double getDistanceFromServer(double par1, double par3, double par5)
    {
        final double d3 = this.getPos().getX() + 0.5D - par1;
        final double d4 = this.getPos().getY() + 0.5D - par3;
        final double d5 = this.getPos().getZ() + 0.5D - par5;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    @Override
    public void update()
    {
        super.update();

//        if (this.terraformBubble == null)
//        {
//            if (!this.world.isRemote)
//            {
//                this.terraformBubble = new EntityTerraformBubble(this.world, new Vector3(this), this);
//                this.world.spawnEntity(this.terraformBubble);
//            }
//        }

        if (!this.world.isRemote)
        {
            final FluidStack liquid = FluidUtil.getFluidContained(this.stacks.get(0));
            if (FluidUtil.isFluidStrict(liquid, FluidRegistry.WATER.getName()))
            {
                FluidUtil.loadFromContainer(waterTank, FluidRegistry.WATER, this.stacks, 0, liquid.amount);
            }

            this.active = this.bubbleSize == this.MAX_SIZE && this.hasEnoughEnergyToRun && !this.getFirstBonemealStack().isEmpty() && this.waterTank.getFluid() != null && this.waterTank.getFluid().amount > 0;
        }

        if (!this.world.isRemote && (this.active != this.lastActive || this.ticks % 60 == 0))
        {
            this.terraformableBlocksList.clear();
            this.grassBlockList.clear();

            if (this.active)
            {
                int bubbleSize = (int) Math.ceil(this.bubbleSize);
                double bubbleSizeSq = this.bubbleSize;
                bubbleSizeSq *= bubbleSizeSq;
                boolean doGrass = !this.grassDisabled && !this.getFirstSeedStack().isEmpty();
                boolean doTrees = !this.treesDisabled && !this.getFirstSaplingStack().isEmpty();
                for (int x = this.getPos().getX() - bubbleSize; x < this.getPos().getX() + bubbleSize; x++)
                {
                    for (int y = this.getPos().getY() - bubbleSize; y < this.getPos().getY() + bubbleSize; y++)
                    {
                        for (int z = this.getPos().getZ() - bubbleSize; z < this.getPos().getZ() + bubbleSize; z++)
                        {
                            BlockPos pos = new BlockPos(x, y, z);
                            Block blockID = this.world.getBlockState(pos).getBlock();
                            if (blockID == null)
                            {
                                continue;
                            }

                            if (!(blockID.isAir(this.world.getBlockState(pos), this.world, pos)) && this.getDistanceFromServer(x, y, z) < bubbleSizeSq)
                            {
                                if (doGrass && blockID instanceof ITerraformableBlock && ((ITerraformableBlock) blockID).isTerraformable(this.world, pos))
                                {
                                    this.terraformableBlocksList.add(new BlockPos(x, y, z));
                                }
                                else if (doTrees)
                                {
                                    Block blockIDAbove = this.world.getBlockState(pos.up()).getBlock();
                                    if (blockID == Blocks.GRASS && blockIDAbove.isAir(this.world.getBlockState(pos.up()), this.world, pos.up()))
                                    {
                                        this.grassBlockList.add(new BlockPos(x, y, z));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!this.world.isRemote && this.terraformableBlocksList.size() > 0 && this.ticks % 15 == 0)
        {
            ArrayList<BlockPos> terraformableBlocks2 = new ArrayList<BlockPos>(this.terraformableBlocksList);

            int randomIndex = this.world.rand.nextInt(this.terraformableBlocksList.size());
            BlockPos vec = terraformableBlocks2.get(randomIndex);

            if (this.world.getBlockState(vec).getBlock() instanceof ITerraformableBlock)
            {
                Block id;

                switch (this.world.rand.nextInt(40))
                {
                case 0:
                    if (this.world.isBlockFullCube(new BlockPos(vec.getX() - 1, vec.getY(), vec.getZ())) && this.world.isBlockFullCube(new BlockPos(vec.getX() + 1, vec.getY(), vec.getZ())) && this.world.isBlockFullCube(new BlockPos(vec.getX(), vec.getY(), vec.getZ() - 1)) && this.world.isBlockFullCube(new BlockPos(vec.getX(), vec.getY(), vec.getZ() + 1)))
                    {
                        id = Blocks.FLOWING_WATER;
                    }
                    else
                    {
                        id = Blocks.GRASS;
                    }
                    break;
                default:
                    id = Blocks.GRASS;
                    break;
                }

                this.world.setBlockState(vec, id.getDefaultState());

                if (id == Blocks.GRASS)
                {
                    this.useCount[0]++;
                    this.waterTank.drain(1, true);
                    this.checkUsage(1);
                }
                else if (id == Blocks.FLOWING_WATER)
                {
                    this.checkUsage(2);
                }
            }

            this.terraformableBlocksList.remove(randomIndex);
        }

        if (!this.world.isRemote && !this.treesDisabled && this.grassBlockList.size() > 0 && this.ticks % 50 == 0)
        {
            int randomIndex = this.world.rand.nextInt(this.grassBlockList.size());
            BlockPos vecGrass = grassBlockList.get(randomIndex);

            if (this.world.getBlockState(vecGrass).getBlock() == Blocks.GRASS)
            {
                BlockPos vecSapling = vecGrass.add(0, 1, 0);
                ItemStack sapling = this.getFirstSaplingStack();
                boolean flag = false;

                //Attempt to prevent placement too close to other trees
                for (BlockPos testVec : this.grownTreesList)
                {
                    if (testVec.distanceSq(vecSapling) < 9)
                    {
                        flag = true;
                        break;
                    }
                }

                if (!flag && sapling != null)
                {
                    Block b = Block.getBlockFromItem(sapling.getItem());
                    this.world.setBlockState(vecSapling, b.getStateFromMeta(sapling.getItemDamage()), 3);
                    if (b instanceof BlockSapling)
                    {
                        if (this.world.getLightFromNeighbors(vecSapling) >= 9)
                        {
                            ((BlockSapling) b).grow(this.world, vecSapling, this.world.getBlockState(vecSapling), this.world.rand);
                            this.grownTreesList.add(new BlockPos(vecSapling.getX(), vecSapling.getY(), vecSapling.getZ()));
                        }
                    }
                    else if (b instanceof BlockBush)
                    {
                        if (this.world.getLightFromNeighbors(vecSapling) >= 5)
                        //Hammer the update tick a few times to try to get it to grow - it won't always
                        {
                            for (int j = 0; j < 12; j++)
                            {
                                if (this.world.getBlockState(vecSapling).getBlock() == b)
                                {
                                    ((BlockBush) b).updateTick(this.world, vecSapling, this.world.getBlockState(vecSapling), this.world.rand);
                                }
                                else
                                {
                                    this.grownTreesList.add(new BlockPos(vecSapling.getX(), vecSapling.getY(), vecSapling.getZ()));
                                    break;
                                }
                            }
                        }
                    }

                    this.useCount[1]++;
                    this.waterTank.drain(50, true);
                    this.checkUsage(0);
                }
            }

            this.grassBlockList.remove(randomIndex);
        }

        if (!this.world.isRemote)
        {
            this.terraformableBlocksListSize = this.terraformableBlocksList.size();
            this.grassBlocksListSize = this.grassBlockList.size();
        }

        if (this.hasEnoughEnergyToRun && (!this.grassDisabled || !this.treesDisabled))
        {
            this.bubbleSize = (float) Math.min(Math.max(0, this.bubbleSize + 0.1F), this.MAX_SIZE);
        }
        else
        {
            this.bubbleSize = (float) Math.min(Math.max(0, this.bubbleSize - 0.1F), this.MAX_SIZE);
        }

        this.lastActive = this.active;
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        if (!this.world.isRemote)
        {
            networkedList.add(this.bubbleSize);
        }
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        if (this.world.isRemote)
        {
            this.bubbleSize = dataStream.readFloat();
        }
    }

    private void checkUsage(int type)
    {
        ItemStack stack = null;

        if ((this.useCount[0] + this.useCount[1]) % 4 == 0)
        {
            stack = this.getFirstBonemealStack();

            if (!stack.isEmpty())
            {
                stack.shrink(1);
            }
        }

        switch (type)
        {
        case 0:
            stack = this.stacks.get(this.saplingIndex);

            if (!stack.isEmpty())
            {
                stack.shrink(1);
            }
            break;
        case 1:
            if (this.useCount[0] % 4 == 0)
            {
                stack = this.getFirstSeedStack();

                if (!stack.isEmpty())
                {
                    stack.shrink(1);
                }
            }
            break;
        case 2:
            this.waterTank.drain(50, true);
            break;
        }
    }

    private int getSelectiveStack(int start, int end)
    {
        for (int i = start; i < end; i++)
        {
            ItemStack stack = this.stacks.get(i);

            if (!stack.isEmpty())
            {
                return i;
            }
        }

        return -1;
    }

    private int getRandomStack(int start, int end)
    {
        int stackcount = 0;
        for (int i = start; i < end; i++)
        {
            if (!this.stacks.get(i).isEmpty())
            {
                stackcount++;
            }
        }

        if (stackcount == 0)
        {
            return -1;
        }

        int random = this.world.rand.nextInt(stackcount);
        for (int i = start; i < end; i++)
        {
            if (!this.stacks.get(i).isEmpty())
            {
                if (random == 0)
                {
                    return i;
                }
                random--;
            }
        }

        return -1;
    }

    public ItemStack getFirstBonemealStack()
    {
        int index = this.getSelectiveStack(2, 6);

        if (index != -1)
        {
            return this.stacks.get(index);
        }

        return ItemStack.EMPTY;
    }

    public ItemStack getFirstSaplingStack()
    {
        int index = this.getRandomStack(6, 10);

        if (index != -1)
        {
            this.saplingIndex = index;
            return this.stacks.get(index);
        }

        return ItemStack.EMPTY;
    }

    public ItemStack getFirstSeedStack()
    {
        int index = this.getSelectiveStack(10, 14);

        if (index != -1)
        {
            return this.stacks.get(index);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.stacks = this.readStandardItemsFromNBT(nbt);

        this.bubbleSize = nbt.getFloat("BubbleSize");
        this.useCount = nbt.getIntArray("UseCountArray");

        if (this.useCount.length == 0)
        {
            this.useCount = new int[2];
        }

        if (nbt.hasKey("waterTank"))
        {
            this.waterTank.readFromNBT(nbt.getCompoundTag("waterTank"));
        }

        if (nbt.hasKey("bubbleVisible"))
        {
            this.setBubbleVisible(nbt.getBoolean("bubbleVisible"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.writeStandardItemsToNBT(nbt, this.stacks);
        nbt.setFloat("BubbleSize", this.bubbleSize);
        nbt.setIntArray("UseCountArray", this.useCount);

        if (this.waterTank.getFluid() != null)
        {
            nbt.setTag("waterTank", this.waterTank.writeToNBT(new NBTTagCompound()));
        }

        nbt.setBoolean("bubbleVisible", this.shouldRenderBubble);
        return nbt;
    }

    @Override
    public NonNullList<ItemStack> getContainingItems()
    {
        return this.stacks;
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("container.tile_terraformer.name");
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return this.isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (slotID == 0)
        {
            return FluidUtil.isEmptyContainer(itemstack);
        }
        if (slotID == 1)
        {
            return ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0;
        }

        return false;
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        switch (slotID)
        {
        case 0:
            FluidStack stack = net.minecraftforge.fluids.FluidUtil.getFluidContained(itemstack);
            return stack != null && stack.getFluid() == FluidRegistry.WATER;
        case 1:
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        case 2:
        case 3:
        case 4:
        case 5:
            return itemstack.getItem() == Items.DYE && itemstack.getItemDamage() == 15;
        case 6:
        case 7:
        case 8:
        case 9:
            return ContainerTerraformer.isOnSaplingList(itemstack);
        case 10:
        case 11:
        case 12:
        case 13:
            return itemstack.getItem() == Items.WHEAT_SEEDS;
        }
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.grassDisabled || !this.treesDisabled;
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(1);
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown <= 0)
        {
            switch (index)
            {
            case 0:
                this.treesDisabled = !this.treesDisabled;
                break;
            case 1:
                this.grassDisabled = !this.grassDisabled;
                break;
            }

            this.disableCooldown = 10;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        switch (index)
        {
        case 0:
            return this.treesDisabled;
        case 1:
            return this.grassDisabled;
        }

        return false;
    }

//    @Override
//    public IBubble getBubble()
//    {
//        return this.terraformBubble;
//    }

    @Override
    public void setBubbleVisible(boolean shouldRender)
    {
        this.shouldRenderBubble = shouldRender;
    }

    @Override
    public double getPacketRange()
    {
        return 64.0F;
    }

    //Pipe handling
    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return (fluid == null || "water".equals(fluid.getName())) && from != this.getElectricInputDirection();
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        int used = 0;

        if (resource != null && this.canFill(from, resource.getFluid()))
        {
            used = this.waterTank.fill(resource, doFill);
        }

        return used;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        return new FluidTankInfo[] { new FluidTankInfo(this.waterTank) };
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(this.getPos().getX() - this.bubbleSize, this.getPos().getY() - this.bubbleSize, this.getPos().getZ() - this.bubbleSize, this.getPos().getX() + this.bubbleSize, this.getPos().getY() + this.bubbleSize, this.getPos().getZ() + this.bubbleSize);
    }

    @Override
    public float getBubbleSize()
    {
        return this.bubbleSize;
    }

    @Override
    public boolean getBubbleVisible()
    {
        return this.shouldRenderBubble;
    }

    @Override
    public EnumFacing getFront()
    {
        IBlockState state = this.world.getBlockState(getPos()); 
        if (state.getBlock() instanceof BlockMachineMars)
        {
            return state.getValue(BlockMachineMars.FACING);
        }
        return EnumFacing.NORTH;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return getFront().rotateY();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return (T) new FluidHandlerWrapper(this, facing);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        if (direction == null)
        {
            return false;
        } 
        if (type == NetworkType.POWER)
        {
            return direction == this.getElectricInputDirection();
        }
        if (type == NetworkType.FLUID)
        {
            return direction != this.getElectricInputDirection();
        }
        return false;
    }
}
