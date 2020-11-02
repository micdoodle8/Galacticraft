package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenDistributor;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProviderColored;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenDistributor;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

public class TileEntityOxygenDistributor extends TileEntityOxygen implements IBubbleProviderColored, INamedContainerProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.oxygenDistributor)
    public static TileEntityType<TileEntityOxygenDistributor> TYPE;

    public boolean active;
    public boolean lastActive;

    public static HashSet<BlockVec3Dim> loadedTiles = new HashSet<>();
    public float bubbleSize;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean shouldRenderBubble = true;

    public TileEntityOxygenDistributor()
    {
        super(TYPE, 6000, 8);
//        this.oxygenBubble = null;
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public void onLoad()
    {
        if (!this.world.isRemote)
        {
            TileEntityOxygenDistributor.loadedTiles.add(new BlockVec3Dim(this));
        }
    }

    @Override
    public void onChunkUnloaded()
    {
        if (!this.world.isRemote)
        {
            TileEntityOxygenDistributor.loadedTiles.remove(new BlockVec3Dim(this));
        }
        super.onChunkUnloaded();
    }

    @Override
    public void remove()
    {
        if (!this.world.isRemote/* && this.oxygenBubble != null*/)
        {
            int bubbleR = MathHelper.ceil(bubbleSize);
            int bubbleR2 = (int) (bubbleSize * bubbleSize);
            final int xMin = this.getPos().getX() - bubbleR;
            final int xMax = this.getPos().getX() + bubbleR;
            final int yMin = this.getPos().getY() - bubbleR;
            final int yMax = this.getPos().getY() + bubbleR;
            final int zMin = this.getPos().getZ() - bubbleR;
            final int zMax = this.getPos().getZ() + bubbleR;
            for (int x = xMin; x < xMax; x++)
            {
                for (int z = zMin; z < zMax; z++)
                {
                    //Doing y as the inner loop allows BlockVec3 to cache the chunks more efficiently
                    for (int y = yMin; y < yMax; y++)
                    {
                        BlockState state = new BlockVec3(x, y, z).getBlockState(this.world);

                        if (state != null && state.getBlock() instanceof IOxygenReliantBlock && this.getDistanceFromServer(x, y, z) <= bubbleR2)
                        {
                            this.world.getPendingBlockTicks().scheduleTick(new BlockPos(x, y, z), state.getBlock(), 0);
                        }
                    }
                }
            }
//        	this.oxygenBubble.remove();
            TileEntityOxygenDistributor.loadedTiles.remove(new BlockVec3Dim(this));
        }

        super.remove();
    }

    @Override
    public double getPacketRange()
    {
        return 64.0F;
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        if (!this.world.isRemote && !this.isRemoved())
        {
//            networkedList.add(this.oxygenBubble != null);
//            if (this.oxygenBubble != null)
//            {
//                networkedList.add(this.oxygenBubble.getEntityId());
//            }
            if (this.world.getServer().isDedicatedServer())
            {
                networkedList.add(loadedTiles.size());
                //TODO: Limit this to ones in the same dimension as this tile?
                for (BlockVec3Dim distributor : loadedTiles)
                {
                    if (distributor == null)
                    {
                        networkedList.add(-1);
                        networkedList.add(-1);
                        networkedList.add(-1);
                        networkedList.add("null");
                    }
                    else
                    {
                        networkedList.add(distributor.x);
                        networkedList.add(distributor.y);
                        networkedList.add(distributor.z);
                        networkedList.add(distributor.dim.getRegistryName().toString());
                    }
                }
            }
            else
            //Signal integrated server, do not clear loadedTiles
            {
                networkedList.add(-1);
            }
            networkedList.add(this.bubbleSize);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(this.getPos().getX() - this.bubbleSize, this.getPos().getY() - this.bubbleSize, this.getPos().getZ() - this.bubbleSize, this.getPos().getX() + this.bubbleSize, this.getPos().getY() + this.bubbleSize, this.getPos().getZ() + this.bubbleSize);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_LONG;
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        if (this.world.isRemote)
        {
//            if (dataStream.readBoolean())
//            {
//                this.oxygenBubble = (EntityBubble) worldObj.getEntityByID(dataStream.readInt());
//            }
            int size = dataStream.readInt();
            if (size >= 0)
            {
                loadedTiles.clear();
                for (int i = 0; i < size; ++i)
                {
                    int i1 = dataStream.readInt();
                    int i2 = dataStream.readInt();
                    int i3 = dataStream.readInt();
                    String str = NetworkUtil.readUTF8String(dataStream);
                    if (str == null || str == "null")
                    {
                        continue;
                    }
                    DimensionType i4 = DimensionType.byName(new ResourceLocation(str));
                    if (i1 == -1 && i2 == -1 && i3 == -1 && i4 == null)
                    {
                        continue;
                    }
                    loadedTiles.add(new BlockVec3Dim(i1, i2, i3, i4));
                }
            }
            this.bubbleSize = dataStream.readFloat();
        }
    }

    public int getDistanceFromServer(int par1, int par3, int par5)
    {
        final int d3 = this.getPos().getX() - par1;
        final int d4 = this.getPos().getY() - par3;
        final int d5 = this.getPos().getZ() - par5;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    @Override
    public void tick()
    {
        if (!this.world.isRemote)
        {
            ItemStack oxygenItemStack = this.getStackInSlot(1);
            if (oxygenItemStack != null && oxygenItemStack.getItem() instanceof IItemOxygenSupply)
            {
                IItemOxygenSupply oxygenItem = (IItemOxygenSupply) oxygenItemStack.getItem();
                int oxygenDraw = (int) Math.floor(Math.min(this.oxygenPerTick * 2.5F, this.getMaxOxygenStored() - this.getOxygenStored()));
                this.setOxygenStored(getOxygenStored() + oxygenItem.discharge(oxygenItemStack, oxygenDraw));
                if (this.getOxygenStored() > this.getMaxOxygenStored())
                {
                    this.setOxygenStored(this.getOxygenStored());
                }
            }
        }

        super.tick();

        if (!this.world.isRemote)
        {
            if (this.getEnergyStoredGC() > 0.0F && this.hasEnoughEnergyToRun && this.getOxygenStored() > this.oxygenPerTick)
            {
                this.bubbleSize += 0.01F;
            }
            else
            {
                this.bubbleSize -= 0.1F;
            }

            this.bubbleSize = Math.min(Math.max(this.bubbleSize, 0.0F), 10.0F);
        }

//        if (!hasValidBubble && !this.world.isRemote && (this.oxygenBubble == null || this.ticks < 25))
//        {
//            //Check it's a world without a breathable atmosphere
//        	if (this.oxygenBubble == null && this.world.getDimension() instanceof IGalacticraftWorldProvider && !((IGalacticraftWorldProvider)this.world.getDimension()).hasBreathableAtmosphere())
//            {
//                this.oxygenBubble = new EntityBubble(this.world, new Vector3(this), this);
//                this.hasValidBubble = true;
//                this.world.addEntity(this.oxygenBubble);
//            }
//        }

        if (!this.world.isRemote/* && this.oxygenBubble != null*/)
        {
            this.active = bubbleSize >= 1 && this.hasEnoughEnergyToRun && this.getOxygenStored() > this.oxygenPerTick;

            if (this.ticks % (this.active ? 20 : 4) == 0)
            {
                double size = bubbleSize;
                int bubbleR = MathHelper.floor(size) + 4;
                int bubbleR2 = (int) (size * size);
                for (int x = this.getPos().getX() - bubbleR; x <= this.getPos().getX() + bubbleR; x++)
                {
                    for (int y = this.getPos().getY() - bubbleR; y <= this.getPos().getY() + bubbleR; y++)
                    {
                        for (int z = this.getPos().getZ() - bubbleR; z <= this.getPos().getZ() + bubbleR; z++)
                        {
                            BlockPos pos = new BlockPos(x, y, z);
                            BlockState state = this.world.getBlockState(pos);
                            Block block = state.getBlock();

                            if (block instanceof IOxygenReliantBlock)
                            {
                                if (this.getDistanceFromServer(x, y, z) <= bubbleR2)
                                {
                                    ((IOxygenReliantBlock) block).onOxygenAdded(this.world, pos, state);
                                }
                                else
                                {
                                    //Do not necessarily extinguish it - it might be inside another oxygen system
                                    this.world.getPendingBlockTicks().scheduleTick(pos, block, 0);
                                }
                            }
                        }
                    }
                }
            }
        }

        this.lastActive = this.active;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);

        if (nbt.contains("bubbleVisible"))
        {
            this.setBubbleVisible(nbt.getBoolean("bubbleVisible"));
        }

        if (nbt.contains("bubbleSize"))
        {
            this.bubbleSize = nbt.getFloat("bubbleSize");
        }
//        this.hasValidBubble = nbt.getBoolean("hasValidBubble");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);

        nbt.putBoolean("bubbleVisible", this.shouldRenderBubble);
        nbt.putFloat("bubbleSize", this.bubbleSize);
//        nbt.putBoolean("hasValidBubble", this.hasValidBubble);
        return nbt;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, Direction side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return ItemElectricBase.isElectricItemCharged(itemstack);
            case 1:
                return itemstack.getDamage() < itemstack.getItem().getMaxDamage();
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction side)
    {
        switch (slotID)
        {
        case 0:
            return ItemElectricBase.isElectricItemEmpty(itemstack);
        case 1:
            return FluidUtil.isEmptyContainer(itemstack);
        default:
            return false;
        }
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        if (itemstack.isEmpty())
        {
            return false;
        }
        if (slotID == 0)
        {
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        }
        if (slotID == 1)
        {
            return itemstack.getItem() instanceof IItemOxygenSupply;
        }
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.getOxygenStored() > this.oxygenPerTick;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.world.getBlockState(getPos());
        if (state.getBlock() instanceof BlockOxygenDistributor)
        {
            return state.get(BlockOxygenDistributor.FACING);
        }
        return Direction.NORTH;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return getFront().rotateY();
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return this.hasEnoughEnergyToRun;
    }

    @Override
    public EnumSet<Direction> getOxygenInputDirections()
    {
        return EnumSet.of(this.getElectricInputDirection().getOpposite());
    }

    @Override
    public EnumSet<Direction> getOxygenOutputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

//    @Override
//    public IBubble getBubble()
//    {
//        return this.oxygenBubble;
//    }

    public boolean inBubble(double pX, double pY, double pZ)
    {
        double r = bubbleSize;
        r *= r;
        double d3 = this.getPos().getX() + 0.5D - pX;
        d3 *= d3;
        if (d3 > r)
        {
            return false;
        }
        double d4 = this.getPos().getZ() + 0.5D - pZ;
        d4 *= d4;
        if (d3 + d4 > r)
        {
            return false;
        }
        double d5 = this.getPos().getY() + 0.5D - pY;
        return d3 + d4 + d5 * d5 < r;
    }

    @Override
    public void setBubbleVisible(boolean shouldRender)
    {
        this.shouldRenderBubble = shouldRender;
//        this.oxygenBubble.setShouldRender(shouldRender);
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
    public Vector3 getColor()
    {
        return new Vector3(0.125F, 0.125F, 0.5F);
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerOxygenDistributor(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.oxygen_distributor");
    }
}
