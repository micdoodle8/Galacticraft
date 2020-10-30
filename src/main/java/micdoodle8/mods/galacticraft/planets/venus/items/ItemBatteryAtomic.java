package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBatteryAtomic extends ItemElectricBase implements ISortable
{
    public ItemBatteryAtomic(Item.Properties properties)
    {
        super(properties);
    }

    @Override
    protected void setMaxTransfer()
    {
        this.transferMax = 7;
    }

    @Override
    public int getTierGC(ItemStack itemStack)
    {
        return 2;
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.infinite_item.desc")));
        tooltip.add(new StringTextComponent(EnumColor.ORANGE + GCCoreUtil.translate("gui.message.low_energy_output")));
    }

    @Override
    public float getElectricityStored(ItemStack itemStack)
    {
        return this.getMaxElectricityStored(itemStack);
    }

    @Override
    public void setElectricity(ItemStack itemStack, float joules)
    {
    }

    @Override
    public float getMaxElectricityStored(ItemStack itemStack)
    {
        return Float.POSITIVE_INFINITY;
    }

    @Override
    public float getTransfer(ItemStack itemStack)
    {
        return 0.0F;
    }

    @Override
    public float recharge(ItemStack theItem, float energy, boolean doReceive)
    {
        return 0F;
    }

    @Override
    public float discharge(ItemStack theItem, float energy, boolean doTransfer)
    {
        return super.discharge(theItem, energy, doTransfer);
    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> par3List)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            par3List.add(new ItemStack(this, 1, 0));
//        }
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
