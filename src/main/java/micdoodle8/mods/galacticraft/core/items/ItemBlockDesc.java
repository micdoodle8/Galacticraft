package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvancedTile;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;

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
    @SideOnly(Side.CLIENT)
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
                if (this.field_150939_a instanceof BlockTileGC)
                {
                    TileEntity te = ((BlockTileGC) this.field_150939_a).createTileEntity(null, stack.getItemDamage() & 12);
                    if (te instanceof TileBaseElectricBlock && !(te instanceof TileEntityEnergyStorageModule))
                    {
                        float powerDrawn = ((TileBaseElectricBlock) te).storage.getMaxExtract();
                        if (powerDrawn > 0)
                        {
                            info.add(EnumChatFormatting.GREEN + GCCoreUtil.translateWithFormat("itemDesc.powerdraw.name", EnergyDisplayHelper.getEnergyDisplayS(powerDrawn * 20)));
                        }
                    }
                }
                else if (this.field_150939_a instanceof BlockAdvancedTile)
                {
                    TileEntity te = ((BlockAdvancedTile) this.field_150939_a).createTileEntity(player.worldObj, stack.getItemDamage() & 12);
                    if (te instanceof TileBaseElectricBlock)
                    {
                        float powerDrawn = ((TileBaseElectricBlock) te).storage.getMaxExtract();
                        if (powerDrawn > 0)
                        {
                            info.add(EnumChatFormatting.GREEN + GCCoreUtil.translateWithFormat("itemDesc.powerdraw.name", EnergyDisplayHelper.getEnergyDisplayS(powerDrawn * 20)));
                        }
                    }
                }
                info.add(GCCoreUtil.translateWithFormat("itemDesc.shift.name", GameSettings.getKeyDisplayString(FMLClientHandler.instance().getClient().gameSettings.keyBindSneak.getKeyCode())));
            }
        }
    }
}
