package micdoodle8.mods.galacticraft.planets.mars.tile;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.entities.IBubble;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTerraformBubble;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import cpw.mods.fml.relauncher.Side;

public class TileEntityTerraformer extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IBubbleProvider, IFluidHandler
{
    private final int tankCapacity = 2000;
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank waterTank = new FluidTank(this.tankCapacity);
    public boolean active;
    public boolean lastActive;
    public static final int WATTS_PER_TICK = 1;
    private ItemStack[] containingItems = new ItemStack[14];
    @NetworkedField(targetSide = Side.CLIENT)
    public EntityTerraformBubble terraformBubble;
    private ArrayList<BlockVec3> terraformableBlocksList = new ArrayList<BlockVec3>();
    private ArrayList<BlockVec3> grassBlockList = new ArrayList<BlockVec3>();
    private ArrayList<BlockVec3> grownTreesList = new ArrayList<BlockVec3>();
    @NetworkedField(targetSide = Side.CLIENT)
    public int terraformableBlocksListSize = 0; // used for server->client ease
    @NetworkedField(targetSide = Side.CLIENT)
    public int grassBlocksListSize = 0; // used for server->client ease
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean treesDisabled;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean grassDisabled;
    @NetworkedField(targetSide = Side.CLIENT)
    public float size;
    public final double MAX_SIZE = 15.0D;
    private int[] useCount = new int[2];
	private int saplingIndex = 6;

    public TileEntityTerraformer()
    {
        this.storage.setMaxExtract(45);
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
        final double d3 = this.xCoord + 0.5D - par1;
        final double d4 = this.yCoord + 0.5D - par3;
        final double d5 = this.zCoord + 0.5D - par5;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (this.terraformBubble == null)
        {
            if (!this.worldObj.isRemote)
            {
                this.terraformBubble = new EntityTerraformBubble(this.worldObj, new Vector3(this), this);
                this.terraformBubble.setPosition(this.xCoord, this.yCoord, this.zCoord);
                this.worldObj.spawnEntityInWorld(this.terraformBubble);
            }
        }

        if (!this.worldObj.isRemote)
        {
            if (this.containingItems[0] != null)
            {
                final FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(this.containingItems[0]);

                if (liquid != null && liquid.getFluid().getName().equals(FluidRegistry.WATER.getName()))
                {
                    if (this.waterTank.getFluid() == null || this.waterTank.getFluid().amount + liquid.amount <= this.waterTank.getCapacity())
                    {
                        this.waterTank.fill(liquid, true);

                        if (FluidContainerRegistry.isBucket(this.containingItems[0]) && FluidContainerRegistry.isFilledContainer(this.containingItems[0]))
                        {
                            this.containingItems[0] = new ItemStack(Items.bucket, this.containingItems[0].stackSize);
                        }
                        else
                        {
                            this.containingItems[0].stackSize--;

                            if (this.containingItems[0].stackSize == 0)
                            {
                                this.containingItems[0] = null;
                            }
                        }
                    }
                }
            }

            this.active = this.terraformBubble.getSize() == this.MAX_SIZE && this.hasEnoughEnergyToRun && this.getFirstBonemealStack() != null && this.waterTank.getFluid() != null && this.waterTank.getFluid().amount > 0;
        }

        if (!this.worldObj.isRemote && (this.active != this.lastActive || this.ticks % 60 == 0))
        {
            this.terraformableBlocksList.clear();
            this.grassBlockList.clear();

            if (this.active)
            {
                int bubbleSize = (int) Math.ceil(this.terraformBubble.getSize());
                double bubbleSizeSq = this.terraformBubble.getSize();
                bubbleSizeSq *= bubbleSizeSq;
                boolean doGrass = !this.grassDisabled && this.getFirstSeedStack() != null;
                boolean doTrees = !this.treesDisabled && this.getFirstSaplingStack() != null;
            	for (int x = this.xCoord - bubbleSize; x < this.xCoord + bubbleSize; x++)
                {
                    for (int y = this.yCoord - bubbleSize; y < this.yCoord + bubbleSize; y++)
                    {
                        for (int z = this.zCoord - bubbleSize; z < this.zCoord + bubbleSize; z++)
                        {
                            Block blockID = this.worldObj.getBlock(x, y, z);

                            if (!(blockID instanceof BlockAir) && this.getDistanceFromServer(x, y, z) < bubbleSizeSq)
                            {
                                if (doGrass && blockID instanceof ITerraformableBlock && ((ITerraformableBlock) blockID).isTerraformable(this.worldObj, x, y, z))
                                {
                                    this.terraformableBlocksList.add(new BlockVec3(x, y, z));
                                }
                                else if (doTrees)
                                {
                                    Block blockIDAbove = this.worldObj.getBlock(x, y + 1, z);
                                	if (blockID == Blocks.grass && blockIDAbove instanceof BlockAir)
	                                {
                                        this.grassBlockList.add(new BlockVec3(x, y, z));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!this.worldObj.isRemote && this.terraformableBlocksList.size() > 0 && this.ticks % 15 == 0)
        {
            ArrayList<BlockVec3> terraformableBlocks2 = new ArrayList<BlockVec3>(this.terraformableBlocksList);

            int randomIndex = this.worldObj.rand.nextInt(this.terraformableBlocksList.size());
            BlockVec3 vec = terraformableBlocks2.get(randomIndex);

            if (vec.getBlock(this.worldObj) instanceof ITerraformableBlock)
            {
	            Block id;
	
	            switch (this.worldObj.rand.nextInt(40))
	            {
	            case 0:
	                if (this.worldObj.func_147469_q(vec.x - 1, vec.y, vec.z) && this.worldObj.func_147469_q(vec.x + 1, vec.y, vec.z) && this.worldObj.func_147469_q(vec.x, vec.y, vec.z - 1) && this.worldObj.func_147469_q(vec.x, vec.y, vec.z + 1))
	                {
	                    id = Blocks.flowing_water;
	                }
	                else
	                {
	                    id = Blocks.grass;
	                }
	                break;
	            default:
	                id = Blocks.grass;
	                break;
	            }
	
	            this.worldObj.setBlock(vec.x, vec.y, vec.z, id);
	
	            if (id == Blocks.grass)
	            {
	                this.useCount[0]++;
	                this.waterTank.drain(1, true);
	                this.checkUsage(1);
	            }
	            else if (id == Blocks.flowing_water)
	            {
	                this.checkUsage(2);
	            }
            }

            this.terraformableBlocksList.remove(randomIndex);
        }

        if (!this.worldObj.isRemote && !this.treesDisabled && this.grassBlockList.size() > 0 && this.ticks % 50 == 0)
        {
            int randomIndex = this.worldObj.rand.nextInt(this.grassBlockList.size());
            BlockVec3 vecGrass = grassBlockList.get(randomIndex);

            if (vecGrass.getBlock(this.worldObj) == Blocks.grass)
            {
            	BlockVec3 vecSapling = vecGrass.translate(0,  1,  0);
            	ItemStack sapling = this.getFirstSaplingStack();
            	boolean flag = false;

            	//Attempt to prevent placement too close to other trees
            	for (BlockVec3 testVec : this.grownTreesList)
            	{
            		if (testVec.distanceSquared(vecSapling) < 9)
            		{
            			flag = true;
            			break;
            		}
            	}

            	if (!flag && sapling != null)
            	{
            		Block b = Block.getBlockFromItem(sapling.getItem());
            		this.worldObj.setBlock(vecSapling.x, vecSapling.y, vecSapling.z, b, sapling.getItemDamage(), 3);
            		if (b instanceof BlockSapling)
            		{
            			if (this.worldObj.getBlockLightValue(vecSapling.x, vecSapling.y, vecSapling.z) >= 9)
            			{
            				((BlockSapling)b).func_149878_d(this.worldObj, vecSapling.x, vecSapling.y, vecSapling.z, this.worldObj.rand);
            				this.grownTreesList.add(vecSapling.clone());
            			}
            		}
            		else if (b instanceof BlockBush)
            		{
            			if (this.worldObj.getBlockLightValue(vecSapling.x, vecSapling.y, vecSapling.z) >= 5)
            				//Hammer the update tick a few times to try to get it to grow - it won't always
            				for (int j = 0; j < 12; j++)
            				{
            					if (this.worldObj.getBlock(vecSapling.x, vecSapling.y, vecSapling.z) == b)
            						((BlockBush)b).updateTick(this.worldObj, vecSapling.x, vecSapling.y, vecSapling.z, this.worldObj.rand);
            					else
            					{
            						this.grownTreesList.add(vecSapling.clone());
            						break;
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

        if (!this.worldObj.isRemote)
        {
            this.terraformableBlocksListSize = this.terraformableBlocksList.size();
            this.grassBlocksListSize = this.grassBlockList.size();
        }

        if (this.hasEnoughEnergyToRun && (!this.grassDisabled || !this.treesDisabled))
        {
            this.size = (float) Math.min(Math.max(0, this.size + 0.1F), this.MAX_SIZE);
        }
        else
        {
            this.size = (float) Math.min(Math.max(0, this.size - 0.1F), this.MAX_SIZE);
        }

        this.lastActive = this.active;
    }

    private void checkUsage(int type)
    {
        ItemStack stack = null;

        if ((this.useCount[0] + this.useCount[1]) % 4 == 0)
        {
            stack = this.getFirstBonemealStack();

            if (stack != null)
            {
                stack.stackSize--;

                if (stack.stackSize <= 0)
                {
                    this.containingItems[this.getSelectiveStack(2, 6)] = null;
                }
            }
        }

        switch (type)
        {
        case 0:
            stack = this.containingItems[this.saplingIndex];

            if (stack != null)
            {
                stack.stackSize--;

                if (stack.stackSize <= 0)
                {
                    this.containingItems[this.saplingIndex] = null;
                }
            }
            break;
        case 1:
            if (this.useCount[0] % 4 == 0)
            {
                stack = this.getFirstSeedStack();

                if (stack != null)
                {
                    stack.stackSize--;

                    if (stack.stackSize <= 0)
                    {
                        this.containingItems[this.getSelectiveStack(10, 14)] = null;
                    }
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
            ItemStack stack = this.containingItems[i];

            if (stack != null)
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
            if (this.containingItems[i] != null)
            {
                stackcount++;
            }
        }
    	
    	if (stackcount == 0)
    		return -1;
    	
    	int random = this.worldObj.rand.nextInt(stackcount);
    	for (int i = start; i < end; i++)
        {
            if (this.containingItems[i] != null)
            {
                if (random == 0) return i;
                random --;
            }
        }
    	
    	return -1;
    }

    public ItemStack getFirstBonemealStack()
    {
        int index = this.getSelectiveStack(2, 6);

        if (index != -1)
        {
            return this.containingItems[index];
        }

        return null;
    }

    public ItemStack getFirstSaplingStack()
    {
        int index = this.getRandomStack(6, 10);

        if (index != -1)
        {
            this.saplingIndex = index;
        	return this.containingItems[index];
        }

        return null;
    }

    public ItemStack getFirstSeedStack()
    {
        int index = this.getSelectiveStack(10, 14);

        if (index != -1)
        {
            return this.containingItems[index];
        }

        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.containingItems = this.readStandardItemsFromNBT(nbt);

        this.size = nbt.getFloat("BubbleSize");
        this.useCount = nbt.getIntArray("UseCountArray");

        if (this.useCount.length == 0)
        {
            this.useCount = new int[2];
        }

        if (nbt.hasKey("waterTank"))
        {
            this.waterTank.readFromNBT(nbt.getCompoundTag("waterTank"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.writeStandardItemsToNBT(nbt);
        nbt.setFloat("BubbleSize", this.size);
        nbt.setIntArray("UseCountArray", this.useCount);

        if (this.waterTank.getFluid() != null)
        {
            nbt.setTag("waterTank", this.waterTank.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public ItemStack[] getContainingItems()
    {
        return this.containingItems;
    }

    @Override
    public String getInventoryName()
    {
        return GCCoreUtil.translate("container.tileTerraformer.name");
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 1 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
    {
        return this.isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
    {
        return slotID == 1;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return slotID == 1 && ItemElectricBase.isElectricItem(itemstack.getItem());
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.grassDisabled || !this.treesDisabled;
    }

    @Override
    public double getPacketRange()
    {
        return 320.0D;
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

    @Override
    public IBubble getBubble()
    {
        return this.terraformBubble;
    }

    @Override
    public void setBubbleVisible(boolean shouldRender)
    {
        if (this.terraformBubble == null)
        {
            return;
        }

        this.terraformBubble.setShouldRender(shouldRender);
    }
 
    //Pipe handling
    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return fluid != null && "water".equals(fluid.getName());
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int used = 0;

        if (resource != null && this.canFill(from, resource.getFluid()))
        {
            used = this.waterTank.fill(resource, doFill);
        }

        return used;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] { new FluidTankInfo(this.waterTank) };
    }
}
