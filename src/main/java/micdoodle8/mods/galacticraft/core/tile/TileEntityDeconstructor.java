package micdoodle8.mods.galacticraft.core.tile;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class TileEntityDeconstructor extends TileBaseElectricBlock implements IInventoryDefaults, ISidedInventory, IMachineSides
{
    public static final float SALVAGE_CHANCE = 0.75F;
    public static final int PROCESS_TIME_REQUIRED_BASE = 250;
    public int processTimeRequired = PROCESS_TIME_REQUIRED_BASE;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = 0;
    private ItemStack producingStack = ItemStack.EMPTY;
    private long ticks;

    private NonNullList<ItemStack> stacks = NonNullList.withSize(11, ItemStack.EMPTY);
    
    public static List<ItemStack> salvageable = new LinkedList<>();
    public static List<INasaWorkbenchRecipe> knownRecipes = new LinkedList<>();
    private int recursiveCount;
    
    static
    {
        initialiseItemList();
        initialiseRecipeList();
    }
    
    private static void initialiseItemList()
    {
        if (GalacticraftCore.isPlanetsLoaded)
        {
            addSalvage(new ItemStack(AsteroidsItems.basicItem, 1, 5));  //T3 plate
            addSalvage(new ItemStack(AsteroidsItems.basicItem, 1, 6));  //Compressed titanium
            addSalvage(new ItemStack(AsteroidsItems.basicItem, 1, 0));  //Titanium ingot
            addSalvage(new ItemStack(MarsItems.marsItemBasic, 1, 3));  //T2 plate
            addSalvage(new ItemStack(MarsItems.marsItemBasic, 1, 5));  //Compressed desh
            addSalvage(new ItemStack(MarsItems.marsItemBasic, 1, 2));  //Desh ingot
        }
        addSalvage(new ItemStack(GCItems.flagPole));
        addSalvage(new ItemStack(GCItems.heavyPlatingTier1));
        addSalvage(new ItemStack(GCItems.itemBasicMoon, 1, 1));  //Compressed meteoric iron
        addSalvage(new ItemStack(GCItems.itemBasicMoon, 1, 0));  //Meteoric iron ingot
        addSalvage(new ItemStack(GCItems.basicItem, 1, 9));  //Compressed steel
        addSalvage(new ItemStack(GCItems.basicItem, 1, 10));  //Compressed bronze
        addSalvage(new ItemStack(GCItems.basicItem, 1, 6));
        addSalvage(new ItemStack(GCItems.basicItem, 1, 7));
        addSalvage(new ItemStack(GCItems.basicItem, 1, 8));
        addSalvage(new ItemStack(GCItems.basicItem, 1, 11));  //Compressed iron
        addSalvage(new ItemStack(GCItems.basicItem, 1, 3));
        addSalvage(new ItemStack(GCItems.basicItem, 1, 4));
        addSalvage(new ItemStack(GCItems.basicItem, 1, 5));
        addSalvage(new ItemStack(Items.IRON_INGOT));
        addSalvage(new ItemStack(Items.GOLD_INGOT));
        addSalvage(new ItemStack(Items.GOLD_NUGGET));
    }

    private static void initialiseRecipeList()
    {
        knownRecipes.addAll(GalacticraftRegistry.getRocketT1Recipes());
        knownRecipes.addAll(GalacticraftRegistry.getBuggyBenchRecipes());
        if (GalacticraftCore.isPlanetsLoaded)
        {
            knownRecipes.addAll(GalacticraftRegistry.getRocketT2Recipes());
            knownRecipes.addAll(GalacticraftRegistry.getCargoRocketRecipes());
            knownRecipes.addAll(GalacticraftRegistry.getRocketT3Recipes());
            knownRecipes.addAll(GalacticraftRegistry.getAstroMinerRecipes());
        }
    }

    public TileEntityDeconstructor()
    {
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 90 : 75);
        this.setTierGC(2);
    }

    public static void addSalvage(ItemStack itemStack)
    {
        for (ItemStack inList : salvageable)
        {
            if (ItemStack.areItemsEqual(inList, itemStack))
                return;
        }
        salvageable.add(itemStack.copy());
    }

    public static boolean isSalvage(ItemStack stack)
    {
        for (ItemStack inList : salvageable)
        {
            if (ItemStack.areItemsEqual(inList, stack))
                return true;
        }
        return false;
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            boolean updateInv = false;

            if (this.hasEnoughEnergyToRun)
            {
                if (this.canDeconstruct())
                {
                    ++this.processTicks;

                    if ((this.processTicks * 5) % this.processTimeRequired == 5)
                    {
                        this.world.playSound(null, this.getPos(), GCSounds.deconstructor, SoundCategory.BLOCKS, 0.25F, this.world.rand.nextFloat() * 0.04F + 0.38F);
                    }

                    if (this.processTicks >= this.processTimeRequired)
                    {
                        this.processTicks = 0;
                        this.deconstruct();
                        updateInv = true;
                    }
                }
                else
                {
                    this.processTicks = 0;
                }
            }
            else
            {
                this.processTicks = 0;
            }

            if (updateInv)
            {
                this.markDirty();
            }
        }

        this.ticks++;
    }

    private boolean canDeconstruct()
    {
        return !this.stacks.get(1).isEmpty();
    }

    public void deconstruct()
    {
        List<ItemStack> ingredients = new LinkedList<>();
        ingredients.add(new ItemStack(this.stacks.get(1).getItem(), 1, this.stacks.get(1).getItemDamage()));
        this.recursiveCount = 0;
        List<ItemStack> salvaged = this.getSalvageable(ingredients);
        salvaged = this.squashList(salvaged);
        salvaged = this.randomChanceList(salvaged);
        if (salvaged != null)
        {
            for (ItemStack output: salvaged)
            {
                this.addToOutputMatrix(output);
            }
        }
        this.decrStackSize(1, 1);
    }

    private List<ItemStack> getSalvageable(List<ItemStack> ingredients)
    {
        if (ingredients == null || ingredients.isEmpty())
        {
            return null;
        }
        if (this.recursiveCount++ > 10)
        {
            this.recursiveCount = 0;
            return null;
        }

        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack stack : ingredients)
        {
            if (isSalvage(stack))
            {
                ret.add(stack);
            }
            else
            {
                GCLog.debug("Trying to " + this.recursiveCount + " break down " + stack.toString());
                List<ItemStack> ingredients2 = this.getIngredients(stack);
                List<ItemStack> recursive = this.getSalvageable(ingredients2);
                if (recursive != null && !recursive.isEmpty())
                {
                    ret.addAll(recursive);
                }
            }
        }
        this.recursiveCount--;
        return ret;
    }

    private List<ItemStack> squashList(List<ItemStack> ingredients)
    {
        if (ingredients == null || ingredients.isEmpty())
        {
            return null;
        }
        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack stack : ingredients)
        {
            boolean matched = false;
            for (ItemStack stack1 : ret)
            {
                if (ItemStack.areItemsEqual(stack, stack1))
                {
                    matched = true;
                    stack1.grow(stack.getCount());
                    break;
                }
            }
            if (!matched)
            {
                ret.add(stack);
            }
        }
        return ret;
    }

    private List<ItemStack> randomChanceList(List<ItemStack> ingredients)
    {
        if (ingredients == null || ingredients.isEmpty())
        {
            return null;
        }
        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack stack : ingredients)
        {
            int count = stack.getCount();
            int result = 0;
            for (int i = 0; i < count; i++)
            {
                if (this.world.rand.nextFloat() < SALVAGE_CHANCE)
                {
                    result++;
                }
            }
            if (result > 0)
            {
                stack.setCount(result);
                ret.add(stack);
            }
        }
        return ret;
    }

    private List<ItemStack> getIngredients(ItemStack stack)
    {
        for (INasaWorkbenchRecipe recipe : knownRecipes)
        {
            ItemStack test = (ItemStack) recipe.getRecipeOutput();
            if (ItemStack.areItemsEqual(test, stack) && test.getCount() == 1)
            {
                return toItemStackList(recipe.getRecipeInput().values());
            }
        }
        List<IRecipe> standardRecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe recipe : standardRecipes)
        {
            ItemStack test = recipe.getRecipeOutput();
            if (ItemStack.areItemsEqual(test, stack) && test.getCount() == 1)
            {
                if (recipe instanceof ShapedRecipes)
                {
                    return expandRecipeInputs(Arrays.asList(((ShapedRecipes) recipe).recipeItems));
                }
                else if (recipe instanceof ShapelessRecipes)
                {
                    return expandRecipeInputs(Arrays.asList(((ShapelessRecipes) recipe).recipeItems));
                }
                else if (recipe instanceof ShapedOreRecipe)
                {
                    return expandRecipeInputs(Arrays.asList(((ShapedOreRecipe) recipe).getInput()));
                }
                else if (recipe instanceof ShapelessOreRecipe)
                {
                    return expandRecipeInputs(Arrays.asList(((ShapelessOreRecipe) recipe).getInput()));
                }
            }
        }
        return null;
    }
    
    private List<ItemStack> expandRecipeInputs(List<?> inputs)
    {
        List<ItemStack> ret = new LinkedList<>();
        for (Object input : inputs)
        {
            ItemStack toAdd = parseRecipeInput(input);
            if (toAdd != null && !toAdd.isEmpty())
            {
                ret.add(toAdd);
            }
        }
        return ret;
    }
    
    private ItemStack parseRecipeInput(Object input)
    {
        if (input instanceof ItemStack)
        {
            ItemStack stack = (ItemStack) input;
            if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
            {
                return new ItemStack(stack.getItem(), stack.getCount(), 0);
            }
            else
            {
                return stack.copy();
            }
        }
        else if (input instanceof String)
        {
            List<ItemStack> stacks = OreDictionary.getOres((String) input);
            if (stacks.isEmpty())
            {
                return null;
            }
            for (ItemStack stack : stacks)
            {
                if (isSalvage(stack));
                {
                    return stack.copy();
                }
            }
            return stacks.get(0).copy();
        }
        else if (input instanceof Iterable)
        {
            for (Object obj : (Iterable) input)
            {
                ItemStack ret = parseRecipeInput(obj);
                if (ret != null)
                {
                    return ret;
                }
            }
        }
        
        return null;
    }

    private List<ItemStack> toItemStackList(Collection<ItemStack> inputs)
    {
        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack o : inputs)
        {
            if (o != null && !o.isEmpty())
            {
                ret.add(o.copy());
            }
        }
        return ret;
    }

    private void addToOutputMatrix(ItemStack stack)
    {
        for (int i = 2; i < 11; i++)
        {
            if (this.stacks.get(i).isEmpty())
            {
                this.stacks.set(i, stack);
                return;
            }
            if (!(ItemStack.areItemsEqual(stack, this.stacks.get(i))))
            {
                continue;
            }
            int size = this.stacks.get(i).getCount();
            if (size + stack.getCount() < this.getInventoryStackLimit())
            {
                this.stacks.get(i).grow(stack.getCount());
                return;
            }
            this.stacks.get(i).setCount(this.getInventoryStackLimit());
            stack.shrink(this.getInventoryStackLimit() - size);
        }
        GCCoreUtil.spawnItem(this.world, this.getPos(), stack);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.processTicks = par1NBTTagCompound.getInteger("smeltingTicks");
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < var2.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = var2.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.stacks.size())
            {
                this.stacks.set(j, new ItemStack(nbttagcompound));
            }
        }
        
        this.readMachineSidesFromNBT(par1NBTTagCompound);  //Needed by IMachineSides
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("smeltingTicks", this.processTicks);
        NBTTagList var2 = new NBTTagList();
        int var3;

        for (var3 = 0; var3 < this.stacks.size(); ++var3)
        {
            if (!this.stacks.get(var3).isEmpty())
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.stacks.get(var3).writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        nbt.setTag("Items", var2);

        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides
        return nbt;
    }

    @Override
    public int getSizeInventory()
    {
        return this.stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
    	return this.stacks.get(par1);
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (!this.stacks.get(par1).isEmpty())
        {
            ItemStack var3;

            if (this.stacks.get(par1).getCount() <= par2)
            {
                var3 = this.stacks.get(par1);
                this.stacks.set(par1, ItemStack.EMPTY);
                this.markDirty();
                return var3;
            }
            else
            {
                var3 = this.stacks.get(par1).splitStack(par2);

                if (this.stacks.get(par1).isEmpty())
                {
                    this.stacks.set(par1, ItemStack.EMPTY);
                }

                this.markDirty();
                return var3;
            }
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (par1 >= this.stacks.size())
        {
        	return ItemStack.EMPTY;
        }

        if (!this.stacks.get(par1).isEmpty())
        {
            ItemStack var2 = this.stacks.get(par1);
            this.stacks.set(par1, ItemStack.EMPTY);
            this.markDirty();
            return var2;
        }
        else
        {
        	return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack stack)
    {
        if (par1 < this.stacks.size())
        {
            this.stacks.set(par1, stack);

            if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
            {
                stack.setCount(this.getInventoryStackLimit());
            }
        }
        this.markDirty();
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("tile.machine2.10.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return this.world.getTileEntity(this.getPos()) == this && entityplayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        if (slotID == 0)
        {
            return itemStack != null && !itemStack.isEmpty() && ItemElectricBase.isElectricItem(itemStack.getItem());
        }
        
        return slotID == 1;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.DOWN)
        {
            return new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        }
        
        return new int[] { 1 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack par2ItemStack, EnumFacing par3)
    {
        return this.isItemValidForSlot(slotID, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack par2ItemStack, EnumFacing par3)
    {
        return slotID >= 2;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.processTicks > 0;
    }

    @Override
    public EnumFacing getFront()
    {
        IBlockState state = this.world.getBlockState(getPos()); 
        if (state.getBlock() instanceof BlockMachine2)
        {
            return state.getValue(BlockMachine2.FACING);
        }
        return EnumFacing.NORTH;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case RIGHT:
            return getFront().rotateYCCW();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return EnumFacing.UP;
        case BOTTOM:
            return EnumFacing.DOWN;
        case LEFT:
        default:
            return getFront().rotateY();
        }
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly 
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        return new MachineSide[] { MachineSide.ELECTRIC_IN };
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[] { Face.LEFT };
    }
    
    private MachineSidePack[] machineSides;

    @Override
    public MachineSidePack[] getAllMachineSides()
    {
        if (this.machineSides == null)
        {
            this.initialiseSides();
        }

        return this.machineSides;
    }

    @Override
    public void setupMachineSides(int length)
    {
        this.machineSides = new MachineSidePack[length];
    }
    
    @Override
    public void onLoad()
    {
        this.clientOnLoad();
    }
    
    @Override
    public IMachineSidesProperties getConfigurationType()
    {
        return BlockMachine2.MACHINESIDES_RENDERTYPE;
    }
    //------------------END OF IMachineSides implementation
}
