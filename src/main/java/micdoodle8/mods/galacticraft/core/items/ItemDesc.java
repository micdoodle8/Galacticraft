package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

import javax.annotation.Nullable;

public abstract class ItemDesc extends Item implements IShiftDescription
{
    public ItemDesc(Properties properties)
    {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (this.showDescription(stack))
        {
            if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), 340))
            {
                List<String> descString = Minecraft.getInstance().fontRenderer.listFormattedStringToWidth(this.getShiftDescription(stack), 150);
                for (String string : descString)
                {
                    tooltip.add(new StringTextComponent(string));
                }
            }
            else
            {
                tooltip.add(new StringTextComponent(GCCoreUtil.translateWithFormat("item_desc.shift", Minecraft.getInstance().gameSettings.keyBindSneak.getLocalizedName())));
            }
        }
    }
}
