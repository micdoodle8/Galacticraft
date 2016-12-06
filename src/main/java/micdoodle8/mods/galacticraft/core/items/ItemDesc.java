package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class ItemDesc extends Item implements IShiftDescription
{
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> info, boolean advanced)
    {
        if (this.showDescription(stack.getItemDamage()))
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                info.addAll(FMLClientHandler.instance().getClient().fontRendererObj.listFormattedStringToWidth(this.getShiftDescription(stack.getItemDamage()), 150));
            }
            else
            {
                info.add(GCCoreUtil.translateWithFormat("item_desc.shift.name", GameSettings.getKeyDisplayString(FMLClientHandler.instance().getClient().gameSettings.keyBindSneak.getKeyCode())));
            }
        }
    }
}
