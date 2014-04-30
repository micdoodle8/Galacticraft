package codechicken.core.fluid;

import codechicken.core.CommonUtils;
import codechicken.lib.inventory.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class FluidUtils
{
    public static int B = FluidContainerRegistry.BUCKET_VOLUME;
    public static FluidStack water = new FluidStack(FluidRegistry.WATER, 1000);
    public static FluidStack lava = new FluidStack(FluidRegistry.LAVA, 1000);

    public static boolean fillTankWithContainer(IFluidHandler tank, EntityPlayer player)
    {
        ItemStack stack = player.getCurrentEquippedItem();
        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(stack);

        if(liquid == null)
            return false;

        if(tank.fill(ForgeDirection.UNKNOWN, liquid, false) != liquid.amount && !player.capabilities.isCreativeMode)
            return false;
        
        tank.fill(ForgeDirection.UNKNOWN, liquid, true);
        
        if(!player.capabilities.isCreativeMode)
            InventoryUtils.consumeItem(player.inventory, player.inventory.currentItem);

        player.inventoryContainer.detectAndSendChanges();
        return true;
    }

    public static boolean emptyTankIntoContainer(IFluidHandler tank, EntityPlayer player, FluidStack tankLiquid)
    {
        ItemStack stack = player.getCurrentEquippedItem();

        if(!FluidContainerRegistry.isEmptyContainer(stack))
            return false;
        
        ItemStack filled = FluidContainerRegistry.fillFluidContainer(tankLiquid, stack);
        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(filled);

        if(liquid == null || filled == null)
            return false;
        
        tank.drain(ForgeDirection.UNKNOWN, liquid.amount, true);

        if(!player.capabilities.isCreativeMode)
        {
            if(stack.stackSize == 1)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, filled);
            else if(player.inventory.addItemStackToInventory(filled))
                stack.stackSize--;
            else
                return false;
        }
        
        player.inventoryContainer.detectAndSendChanges();        
        return true;
    }

    public static FluidStack copy(FluidStack liquid, int quantity)
    {
        liquid = liquid.copy();
        liquid.amount = quantity;
        return liquid;
    }

    public static FluidStack read(NBTTagCompound tag)
    {
        FluidStack stack = FluidStack.loadFluidStackFromNBT(tag);
        return stack != null ? stack : new FluidStack(0, 0);
    }
    
    public static NBTTagCompound write(FluidStack fluid, NBTTagCompound tag)
    {
        return fluid == null || fluid.getFluid() == null ? new NBTTagCompound() : fluid.writeToNBT(new NBTTagCompound());
    }

    public static int getLuminosity(FluidStack stack, double density)
    {
        Fluid fluid = stack.getFluid();
        if(fluid == null)
            return 0;
        int light = fluid.getLuminosity(stack);
        if(fluid.isGaseous())
            light=(int)(light*density);
        return light;
    }
}
