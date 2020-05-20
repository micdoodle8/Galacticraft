package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import javax.annotation.Nullable;

public class ItemAstroMiner extends Item implements IHoldableItem, ISortableItem
{
    public ItemAstroMiner(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(assetName);
        //this.setTextureName("arrow");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemGroup getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    public ActionResultType onItemUseFirst(PlayerEntity playerIn, World worldIn, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, Hand hand)
    {
        TileEntity tile = null;

        if (playerIn == null)
        {
            return ActionResultType.PASS;
        }
        else
        {
            final Block id = worldIn.getBlockState(pos).getBlock();

            if (id == GCBlocks.fakeBlock)
            {
                tile = worldIn.getTileEntity(pos);

                if (tile instanceof TileEntityMulti)
                {
                    tile = ((TileEntityMulti) tile).getMainBlockTile();
                }
            }

            if (id == AsteroidBlocks.minerBaseFull)
            {
                tile = worldIn.getTileEntity(pos);
            }

            if (tile instanceof TileEntityMinerBase)
            {
                //Don't open GUI on client
                if (worldIn.isRemote)
                {
                    return ActionResultType.FAIL;
                }
                
                if (worldIn.provider instanceof WorldProviderSpaceStation)
                {
                    playerIn.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.message.astro_miner7.fail")));
                    return ActionResultType.FAIL;
                }

                if (((TileEntityMinerBase) tile).getLinkedMiner() != null)
                {
                    playerIn.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.message.astro_miner.fail")));
                    return ActionResultType.FAIL;
                }

                //Gives a chance for any loaded Astro Miner to link itself
                if (((TileEntityMinerBase) tile).ticks < 15)
                {
                    return ActionResultType.FAIL;
                }

                ServerPlayerEntity playerMP = (ServerPlayerEntity) playerIn;
                GCPlayerStats stats = GCPlayerStats.get(playerIn);

                int astroCount = stats.getAstroMinerCount();
                if (astroCount >= ConfigManagerAsteroids.astroMinerMax && (!playerIn.capabilities.isCreativeMode))
                {
                    playerIn.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.message.astro_miner2.fail")));
                    return ActionResultType.FAIL;
                }

                if (!((TileEntityMinerBase) tile).spawnMiner(playerMP))
                {
                    playerIn.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.message.astro_miner1.fail") + " " + GCCoreUtil.translate(EntityAstroMiner.blockingBlock.toString())));
                    return ActionResultType.FAIL;
                }

                if (!playerIn.capabilities.isCreativeMode)
                {
                    stats.setAstroMinerCount(stats.getAstroMinerCount() + 1);
                    playerIn.getHeldItem(hand).shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        //TODO
    }

    @Override
    public boolean shouldHoldLeftHandUp(PlayerEntity player)
    {
        return true;
    }

    @Override
    public boolean shouldHoldRightHandUp(PlayerEntity player)
    {
        return true;
    }

    @Override
    public boolean shouldCrouch(PlayerEntity player)
    {
        return true;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }
}
