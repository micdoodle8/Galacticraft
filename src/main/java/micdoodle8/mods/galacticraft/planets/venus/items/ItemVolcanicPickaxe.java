package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

import java.util.List;

import javax.annotation.Nullable;

public class ItemVolcanicPickaxe extends PickaxeItem implements ISortable, IShiftDescription
{
    public ItemVolcanicPickaxe(Item.Properties builder)
    {
        super(EnumItemTierVenus.VOLCANIC_TOOL, 1, -2.8F, builder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
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
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.TOOLS;
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
                                ((ServerPlayerEntity) player).connection.sendPacket(pkt);
                            }
                        }

                        boolean canHarvest = block.canHarvestBlock(state1, worldIn, pos1, player);
                        boolean destroyed = block.removedByPlayer(state1, worldIn, pos1, player, canHarvest, worldIn.getFluidState(pos1));
                        if (destroyed)
                        {
                            block.onPlayerDestroy(worldIn, pos1, state1);
                        }
                        if (canHarvest && destroyed)
                        {
                            block.harvestBlock(worldIn, player, pos1, state1, tileentity, stack);
                            stack.damageItem(1, player, (e) ->
                            {
                            });
                        }
                    }
                }
            }
        }

        return ret;
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }
}
