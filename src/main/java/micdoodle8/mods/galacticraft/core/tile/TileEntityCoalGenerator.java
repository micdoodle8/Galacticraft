package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCoalGenerator;
import micdoodle8.mods.galacticraft.core.inventory.ContainerElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;

public class TileEntityCoalGenerator extends TileBaseUniversalElectricalSource implements IInventoryDefaults, ISidedInventory, IConnector, INamedContainerProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.coalGenerator)
    public static TileEntityType<TileEntityCoalGenerator> TYPE;

    //New energy rates:
    //
    //Tier 1 machine typically consumes 600 gJ/s = 30 gJ/t

    //Coal generator on max heat can power up to 4 Tier 1 machines
    //(fewer if one of them is an Electric Furnace)
    //Basic solar gen in full sun can power 1 Tier 1 machine

    //1 lump of coal is equivalent to 38400 gJ
    //because on max heat it produces 120 gJ/t over 320 ticks

    //Below the min_generate, all heat is wasted
    //At max generate, 100% efficient conversion coal energy -> electric makes 120 gJ/t
    public static final int MAX_GENERATE_GJ_PER_TICK = 150;
    public static final int MIN_GENERATE_GJ_PER_TICK = 30;

    private static final float BASE_ACCELERATION = 0.3f;

    public float prevGenerateWatts = 0;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public float heatGJperTick = 0;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int itemCookTime = 0;

    public TileEntityCoalGenerator()
    {
        super(TYPE);
        this.storage.setMaxExtract(TileEntityCoalGenerator.MAX_GENERATE_GJ_PER_TICK - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK);
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        if (!this.world.isRemote && this.heatGJperTick - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK > 0)
        {
            this.receiveEnergyGC(null, (this.heatGJperTick - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK), false);
        }

        super.tick();

        if (!this.world.isRemote)
        {
            if (this.itemCookTime > 0)
            {
                this.itemCookTime--;

                this.heatGJperTick = Math.min(this.heatGJperTick + Math.max(this.heatGJperTick * 0.005F, TileEntityCoalGenerator.BASE_ACCELERATION), TileEntityCoalGenerator.MAX_GENERATE_GJ_PER_TICK);
            }

            if (this.itemCookTime <= 0 && !this.getInventory().get(0).isEmpty())
            {
                if (this.getInventory().get(0).getItem() == Items.COAL && this.getInventory().get(0).getCount() > 0)
                {
                    this.itemCookTime = 320;
                    this.decrStackSize(0, 1);
                }
                else if (this.getInventory().get(0).getItem() == Item.getItemFromBlock(Blocks.COAL_BLOCK) && this.getInventory().get(0).getCount() > 0)
                {
                    this.itemCookTime = 320 * 10;
                    this.decrStackSize(0, 1);
                }
            }

            this.produce();

            if (this.itemCookTime <= 0)
            {
                this.heatGJperTick = Math.max(this.heatGJperTick - 0.3F, 0);
            }

            this.heatGJperTick = Math.min(Math.max(this.heatGJperTick, 0.0F), this.getMaxEnergyStoredGC());
        }
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.itemCookTime = nbt.getInt("itemCookTime");
        this.heatGJperTick = nbt.getInt("generateRateInt");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("itemCookTime", this.itemCookTime);
        nbt.putFloat("generateRate", this.heatGJperTick);

        return nbt;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return itemstack.getItem() == Items.COAL || itemstack.getItem() == Item.getItemFromBlock(Blocks.COAL_BLOCK);
    }

//    @Override
//    public int[] getAccessibleSlotsFromSide(int var1)
//    {
//        return new int[] { 0 };
//    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0};
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction direction)
    {
        return slotID == 0;
    }

//    @Override
//    public boolean canInsertItem(int slotID, ItemStack itemstack, int j)
//    {
//        return this.isItemValidForSlot(slotID, itemstack);
//    }
//
//    @Override
//    public boolean canExtractItem(int slotID, ItemStack itemstack, int j)
//    {
//        return slotID == 0;
//    }

    @Override
    public float receiveElectricity(Direction from, float energy, int tier, boolean doReceive)
    {
        return 0;
    }

	/*
    @Override
	public float getRequest(EnumFacing direction)
	{
		return 0;
	}
	*/

    @Override
    public EnumSet<Direction> getElectricalInputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    @Override
    public EnumSet<Direction> getElectricalOutputDirections()
    {
        return EnumSet.of(this.getElectricOutputDirection());
    }

    public Direction getFront()
    {
        return BlockMachineBase.getFront(this.world.getBlockState(getPos()));
    }

    @Override
    public Direction getElectricOutputDirection()
    {
        return getFront().rotateY();
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return direction == this.getElectricOutputDirection();
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerCoalGenerator(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.coal_generator");
    }
}
