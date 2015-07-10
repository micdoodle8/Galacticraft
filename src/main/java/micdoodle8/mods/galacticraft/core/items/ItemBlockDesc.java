package micdoodle8.mods.galacticraft.core.items;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvancedTile;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
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
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote) return;

    	//The player could be a FakePlayer made by another mod e.g. LogisticsPipes
    	if (player instanceof EntityPlayerSP)
    	{
	        if (this.getBlock() == GCBlocks.fuelLoader)
	        	ClientProxyCore.playerClientHandler.onBuild(4, (EntityPlayerSP) player);
	        else if (this.getBlock() == GCBlocks.fuelLoader)
	        	ClientProxyCore.playerClientHandler.onBuild(6, (EntityPlayerSP) player);
    	}
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean advanced)
    {
        if (this.getBlock() instanceof IBlockShiftDesc && ((IBlockShiftDesc) this.getBlock()).showDescription(stack.getItemDamage()))
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                info.addAll(FMLClientHandler.instance().getClient().fontRendererObj.listFormattedStringToWidth(((IBlockShiftDesc) this.getBlock()).getShiftDescription(stack.getItemDamage()), 150));
            }
            else
            {
                if (this.getBlock() instanceof BlockTileGC)
                {
                    TileEntity te = ((BlockTileGC) this.getBlock()).createTileEntity(null, getBlock().getStateFromMeta(stack.getItemDamage() & 12));
                    if (te instanceof TileBaseElectricBlock)
                    {
                        float powerDrawn = ((TileBaseElectricBlock) te).storage.getMaxExtract();
                        if (powerDrawn > 0)
                        {
                            info.add(EnumChatFormatting.GREEN + GCCoreUtil.translateWithFormat("itemDesc.powerdraw.name", EnergyDisplayHelper.getEnergyDisplayS(powerDrawn * 20)));
                        }
                    }
                }
                else if (this.getBlock() instanceof BlockAdvancedTile)
                {
                    TileEntity te = ((BlockAdvancedTile) this.getBlock()).createTileEntity(player.worldObj, getBlock().getStateFromMeta(stack.getItemDamage() & 12));
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
