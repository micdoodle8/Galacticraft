package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import java.util.List;

import javax.annotation.Nullable;

public class ItemVolcanicPickaxe extends ItemPickaxe implements ISortableItem, IShiftDescription
{
    public ItemVolcanicPickaxe(String assetName)
    {
        super(VenusItems.TOOL_VOLCANIC);
        this.setUnlocalizedName(assetName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> info, ITooltipFlag flagIn)
    {
        if (this.showDescription(stack.getItemDamage()))
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                info.addAll(FMLClientHandler.instance().getClient().fontRenderer.listFormattedStringToWidth(this.getShiftDescription(stack.getItemDamage()), 150));
            }
            else
            {
                info.add(GCCoreUtil.translateWithFormat("item_desc.shift.name", GameSettings.getKeyDisplayString(FMLClientHandler.instance().getClient().gameSettings.keyBindSneak.getKeyCode())));
            }
        }
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.TOOLS;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        boolean ret = super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);

        if (!(entityLiving instanceof EntityPlayer) || worldIn.isRemote)
        {
            return ret;
        }

        EntityPlayer player = (EntityPlayer) entityLiving;
        EnumFacing facing = entityLiving.getHorizontalFacing();

        if (entityLiving.rotationPitch < -45.0F)
        {
            facing = EnumFacing.UP;
        }
        else if (entityLiving.rotationPitch > 45.0F)
        {
            facing = EnumFacing.DOWN;
        }

        boolean yAxis = facing.getAxis() == EnumFacing.Axis.Y;
        boolean xAxis = facing.getAxis() == EnumFacing.Axis.X;
        
        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1 && !stack.isEmpty(); ++j)
            {
                if (i == 0 && j == 0)
                {
                    continue;
                }

                BlockPos pos1;
                if (yAxis)
                {
                    pos1 = pos.add(i, 0, j);
                }
                else if (xAxis)
                {
                    pos1 = pos.add(0, i, j);
                }
                else
                {
                    pos1 = pos.add(i, j, 0);
                }

                //:Replicate logic of PlayerInteractionManager.tryHarvestBlock(pos1)
                IBlockState state1 = worldIn.getBlockState(pos1);
                float f = state1.getBlockHardness(worldIn, pos1);
                if (f >= 0F)
                {
                    BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(worldIn, pos1, state1, player);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (!event.isCanceled())
                    {
                        Block block = state1.getBlock(); 
                        if ((block instanceof BlockCommandBlock || block instanceof BlockStructure) && !player.canUseCommandBlock())
                        {
                            worldIn.notifyBlockUpdate(pos1, state1, state1, 3);
                            continue;
                        }
                        TileEntity tileentity = worldIn.getTileEntity(pos1);
                        if (tileentity != null)
                        {
                            Packet<?> pkt = tileentity.getUpdatePacket();
                            if (pkt != null)
                            {
                                ((EntityPlayerMP)player).connection.sendPacket(pkt);
                            }
                        }
    
                        boolean canHarvest = block.canHarvestBlock(worldIn, pos1, player);
                        boolean destroyed = block.removedByPlayer(state1, worldIn, pos1, player, canHarvest);
                        if (destroyed)
                        {
                            block.onBlockDestroyedByPlayer(worldIn, pos1, state1);
                        }
                        if (canHarvest && destroyed)
                        {
                            block.harvestBlock(worldIn, player, pos1, state1, tileentity, stack);
                            stack.damageItem(1, player);
                        }
                    }
                }
            }
        }

        return ret;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate("item.volcanic_pickaxe.description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
