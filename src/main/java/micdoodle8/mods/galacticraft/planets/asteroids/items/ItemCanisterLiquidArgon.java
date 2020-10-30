package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCanisterLiquidArgon extends ItemCanisterGeneric
{
//    protected IIcon[] icons = new IIcon[7];

    public ItemCanisterLiquidArgon(Item.Properties builder)
    {
        super(builder);
//        this.setAllowedFluid("liquidargon");
        //this.setTextureName(GalacticraftPlanets.TEXTURE_PREFIX + assetName);
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        for (int i = 0; i < this.icons.length; i++)
        {
            this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
        }
    }*/

//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        if (itemStack.getMaxDamage() - itemStack.getDamage() == 0)
//        {
//            return "item.emptyGasCanister";
//        }
//
//        if (itemStack.getDamage() == 1)
//        {
//            return "item.canister.liquidArgon.full";
//        }
//
//        return "item.canister.liquidArgon.partial";
//    }

    /*@Override
    public IIcon getIconFromDamage(int par1)
    {
        final int damage = 6 * par1 / this.getMaxDamage();

        if (this.icons.length > damage)
        {
            return this.icons[this.icons.length - damage - 1];
        }

        return super.getIconFromDamage(damage);
    }*/

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (par1ItemStack.getMaxDamage() - par1ItemStack.getDamage() > 0)
        {
            tooltip.add(new StringTextComponent(GCCoreUtil.translate("item.canister.liquid_argon") + ": " + (par1ItemStack.getMaxDamage() - par1ItemStack.getDamage())));
        }
    }
}
