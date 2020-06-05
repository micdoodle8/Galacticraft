package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.block.Block;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Rarity;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import java.util.List;

import javax.annotation.Nullable;

public class ItemVolcanicPickaxe extends PickaxeItem implements ISortableItem, IShiftDescription
{
    public ItemVolcanicPickaxe(Item.Properties builder)
    {
        super(VenusItems.TOOL_VOLCANIC);
        this.setUnlocalizedName(assetName);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> info, ITooltipFlag flagIn)
    {
        if (this.showDescription(stack.getItemDamage()))
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                info.addAll(Minecraft.getInstance().fontRenderer.listFormattedStringToWidth(this.getShiftDescription(stack.getItemDamage()), 150));
            }
            else
            {
                info.add(GCCoreUtil.translateWithFormat("item_desc.shift.name", GameSettings.getKeyDisplayString(Minecraft.getInstance().gameSettings.keyBindSneak.getKeyCode())));
            }
        }
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
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.TOOLS;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        boolean ret = super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);

        if (!(entityLiving instanceof PlayerEntity) || worldIn.isRemote)
        {
            return ret;
        }

        PlayerEntity player = (PlayerEntity) entityLiving;
        Direction facing = entityLiving.getHorizontalFacing();

        if (entityLiving.rotationPitch < -45.0F)
        {
            facing = Direction.UP;
        }
        else if (entityLiving.rotationPitch > 45.0F)
        {
            facing = Direction.DOWN;
        }

        boolean yAxis = facing.getAxis() == Direction.Axis.Y;
        boolean xAxis = facing.getAxis() == Direction.Axis.X;
        
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
                BlockState state1 = worldIn.getBlockState(pos1);
                float f = state1.getBlockHardness(worldIn, pos1);
                if (f >= 0F)
                {
                    BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(worldIn, pos1, state1, player);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (!event.isCanceled())
                    {
                        Block block = state1.getBlock(); 
                        if ((block instanceof CommandBlockBlock || block instanceof StructureBlock) && !player.canUseCommandBlock())
                        {
                            worldIn.notifyBlockUpdate(pos1, state1, state1, 3);
                            continue;
                        }
                        TileEntity tileentity = worldIn.getTileEntity(pos1);
                        if (tileentity != null)
                        {
                            IPacket<?> pkt = tileentity.getUpdatePacket();
                            if (pkt != null)
                            {
                                ((ServerPlayerEntity)player).connection.sendPacket(pkt);
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
