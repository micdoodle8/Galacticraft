package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemBlockDesc extends ItemBlockGC
{
    public static interface IBlockShiftDesc
    {
        String getShiftDescription(int meta);

        boolean showDescription(int meta);
    }

    public ItemBlockDesc(Block block)
    {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean advanced)
    {
        if (this.field_150939_a instanceof IBlockShiftDesc && ((IBlockShiftDesc) this.field_150939_a).showDescription(stack.getItemDamage()))
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                info.addAll(FMLClientHandler.instance().getClient().fontRenderer.listFormattedStringToWidth(((IBlockShiftDesc) this.field_150939_a).getShiftDescription(stack.getItemDamage()), 150));
            }
            else
            {
                info.add(GCCoreUtil.translateWithFormat("itemDesc.shift.name", Keyboard.getKeyName(FMLClientHandler.instance().getClient().gameSettings.keyBindSneak.getKeyCode())));
            }
        }
    }
}
