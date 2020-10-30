package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IKeyable;
import micdoodle8.mods.galacticraft.core.Annotations;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleSidedInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TileEntityTreasureChest extends TileEntityAdvanced implements ITickableTileEntity, IInventory, IKeyable, ISidedInventory, IChestLid, INamedContainerProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.treasureChestTier1)
    public static TileEntityType<TileEntityTreasureChest> TYPE;

    public boolean adjacentChestChecked;
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;
    private AxisAlignedBB renderAABB;

    protected ResourceLocation lootTable;
    protected long lootTableSeed;

    @Annotations.NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean locked = true;
    @Annotations.NetworkedField(targetSide = LogicalSide.CLIENT)
    public int tier = 1;

    public TileEntityTreasureChest()
    {
        this(TYPE, 1);
    }

    public TileEntityTreasureChest(TileEntityType<?> type, int tier)
    {
        super(type);
        this.tier = tier;
        inventory = NonNullList.withSize(27, ItemStack.EMPTY);
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.locked = nbt.getBoolean("isLocked");
        this.tier = nbt.getInt("tier");

        checkLootAndRead(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putBoolean("isLocked", this.locked);
        nbt.putInt("tier", this.tier);

        checkLootAndWrite(nbt);

        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Override
    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }

    /**
     * Updates the JList with a new model.
     */
    @Override
    public void tick()
    {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;
        float f;

        if (this.locked)
        {
            this.numPlayersUsing = 0;
        }

        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            f = 5.0F;
            List list = this.world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB((float) i - f, (float) j - f, (float) k - f, (float) (i + 1) + f, (float) (j + 1) + f, (float) (k + 1) + f));
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                PlayerEntity entityplayer = (PlayerEntity) iterator.next();

                if (entityplayer.openContainer instanceof ChestContainer)
                {
                    IInventory iinventory = ((ChestContainer) entityplayer.openContainer).getLowerChestInventory();

                    if (iinventory == this || iinventory instanceof DoubleSidedInventory && ((DoubleSidedInventory) iinventory).isPartOfLargeChest(this))
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        f = 0.1F;
        double d2;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
        {
            double d1 = (double) i + 0.5D;
            d2 = (double) k + 0.5D;

            this.world.playSound(null, d1, (double) j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (((this.numPlayersUsing == 0 || this.locked) && this.lidAngle > 0.0F) || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f1 = this.lidAngle;

            if (this.numPlayersUsing == 0 || this.locked)
            {
                this.lidAngle -= f;
            }
            else
            {
                this.lidAngle += f;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float f2 = 0.5F;

            if (this.lidAngle < f2 && f1 >= f2)
            {
                d2 = (double) i + 0.5D;
                double d0 = (double) k + 0.5D;

                this.world.playSound(null, d2, (double) j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }

        super.tick();
    }

    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void openInventory(PlayerEntity player)
    {
        if (!player.isSpectator())
        {
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockState().getBlock(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockState().getBlock());
            this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockState().getBlock());
        }
    }

    @Override
    public void closeInventory(PlayerEntity player)
    {
        if (!player.isSpectator())
        {
//            --this.numPlayersUsing;
//            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
//            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
//            this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void remove()
    {
        super.remove();
        this.updateContainingBlockInfo();
    }

    public String getGuiID()
    {
        return "minecraft:chest";
    }

//    public Container createContainer(PlayerInventory playerInventory, PlayerEntity playerIn)
//    {
//        return new ChestContainer(playerInventory, this, playerIn);
//    }

//    @Override
//    public int getField(int id)
//    {
//        return 0;
//    }
//
//    @Override
//    public void setField(int id, int value)
//    {
//    }

//    @Override
//    public int getFieldCount()
//    {
//        return 0;
//    }

    @Override
    public double getPacketRange()
    {
        return 20.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    @Override
    public int getTierOfKeyRequired()
    {
        return this.tier;
    }

    @Override
    public boolean onValidKeyActivated(PlayerEntity player, ItemStack key, Direction face)
    {
        if (this.locked)
        {
            this.locked = false;

            if (this.world.isRemote)
            {
                // player.playSound("galacticraft.player.unlockchest", 1.0F,
                // 1.0F);
            }
            else
            {
                if (!player.abilities.isCreativeMode)
                {
                    player.inventory.getCurrentItem().shrink(1);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onActivatedWithoutKey(PlayerEntity player, Direction face)
    {
        if (this.locked)
        {
            if (player.world.isRemote)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_ON_FAILED_CHEST_UNLOCK, GCCoreUtil.getDimensionType(this.world), new Object[]{this.getTierOfKeyRequired()}));
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean canBreak()
    {
        return false;
    }

    public static TileEntityTreasureChest findClosest(Entity entity, int tier)
    {
        double distance = Double.MAX_VALUE;
        TileEntityTreasureChest chest = null;
        for (final TileEntity tile : entity.world.loadedTileEntityList)
        {
            if (tile instanceof TileEntityTreasureChest && ((TileEntityTreasureChest) tile).getTierOfKeyRequired() == tier)
            {
                double dist = entity.getDistanceSq(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5);
                if (dist < distance)
                {
                    distance = dist;
                    chest = (TileEntityTreasureChest) tile;
                }
            }
        }

        if (chest != null)
        {
            GCLog.debug("Found chest to generate boss loot in: " + chest.pos);
        }
        else
        {
            GCLog.debug("Could not find chest to generate boss loot in!");
        }

        return chest;
    }

//    protected boolean checkLootAndRead(CompoundNBT compound)
//    {
//        if (compound.contains("LootTable", 8))
//        {
//            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
//            this.lootTableSeed = compound.getLong("LootTableSeed");
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }

//    protected boolean checkLootAndWrite(CompoundNBT compound)
//    {
//        if (this.lootTable != null)
//        {
//            compound.putString("LootTable", this.lootTable.toString());
//
//            if (this.lootTableSeed != 0L)
//            {
//                compound.putLong("LootTableSeed", this.lootTableSeed);
//            }
//
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }

//    public void fillWithLoot(PlayerEntity player)
//    {
//        if (this.lootTable != null)
//        {
//            LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);
//            this.lootTable = null;
//            Random random;
//
//            if (this.lootTableSeed == 0L)
//            {
//                random = new Random();
//            }
//            else
//            {
//                random = new Random(this.lootTableSeed);
//            }
//
//            LootContext.Builder builder = new LootContext.Builder((ServerWorld) this.world);
//
//            if (player != null)
//            {
//                builder.withLuck(player.getLuck());
//            }
//
//            loottable.fillInventory(this, random, builder.build());
//        }
//    }

    public ResourceLocation getLootTable()
    {
        return this.lootTable;
    }

//    public void setLootTable(ResourceLocation lootTable, long lootTableSeed)
//    {
//        this.lootTable = lootTable;
//        this.lootTableSeed = lootTableSeed;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(pos, pos.add(1, 2, 1));
        }
        return this.renderAABB;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_MEDIUM;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction)
    {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        return false;
    }

    public static void setLootTable(IBlockReader reader, Random rand, BlockPos p_195479_2_, ResourceLocation lootTableIn)
    {
        TileEntity tileentity = reader.getTileEntity(p_195479_2_);
        if (tileentity instanceof LockableLootTileEntity)
        {
            ((LockableLootTileEntity) tileentity).setLootTable(lootTableIn, rand.nextLong());
        }

    }

    protected boolean checkLootAndRead(CompoundNBT compound)
    {
        if (compound.contains("LootTable", 8))
        {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean checkLootAndWrite(CompoundNBT compound)
    {
        if (this.lootTable == null)
        {
            return false;
        }
        else
        {
            compound.putString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L)
            {
                compound.putLong("LootTableSeed", this.lootTableSeed);
            }

            return true;
        }
    }

    public void fillWithLoot(@Nullable PlayerEntity player)
    {
        if (this.lootTable != null && this.world.getServer() != null)
        {
            LootTable loottable = this.world.getServer().getLootTableManager().getLootTableFromLocation(this.lootTable);
            this.lootTable = null;
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) this.world)).withParameter(LootParameters.POSITION, new BlockPos(this.pos)).withSeed(this.lootTableSeed);
            if (player != null)
            {
                lootcontext$builder.withLuck(player.getLuck()).withParameter(LootParameters.THIS_ENTITY, player);
            }

            loottable.fillInventory(this, lootcontext$builder.build(LootParameterSets.CHEST));
        }

    }

    public void setLootTable(ResourceLocation lootTableIn, long seedIn)
    {
        this.lootTable = lootTableIn;
        this.lootTableSeed = seedIn;
    }

    /**
     * Returns the stack in the given slot.
     */
    @Override
    public ItemStack getStackInSlot(int index)
    {
        this.fillWithLoot(null);
        return this.inventory.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        this.fillWithLoot(null);
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventory, index, count);
        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        this.fillWithLoot(null);
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.fillWithLoot(null);
        this.inventory.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return !(player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clear()
    {
        this.inventory.clear();
    }

    @Override
    public ChestContainer createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        this.fillWithLoot(playerInv.player);
        return ChestContainer.createGeneric9X3(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.treasure_chest");
    }

    @Override
    public float getLidAngle(float partialTicks)
    {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }
}