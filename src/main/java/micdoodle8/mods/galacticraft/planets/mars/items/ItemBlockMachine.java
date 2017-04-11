package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMarsT2;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMachine extends ItemBlockDesc implements IHoldableItem
{
    public ItemBlockMachine(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public boolean placeBlockAt(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, IBlockState state)
    {
        int metaAt = itemStack.getItemDamage();
        
        //If it is a Cryogenic Chamber, check the space
        if (metaAt == BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
        {
            for (int y = 0; y < 3; y++)
            {
                Block blockAt = world.getBlockState(pos.add(0, y, 0)).getBlock();

                if (this.getBlock() == MarsBlocks.machine)
                {
                    if (!blockAt.getMaterial().isReplaceable())
                    {
                        if (world.isRemote)
                        {
                            FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                        }
                        return false;
                    }
                }
            }
        }
        return super.placeBlockAt(itemStack, player, world, pos, facing, hitX, hitY, hitZ, state);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        int index = 0;
        int typenum = itemstack.getItemDamage() & 12;

        if (this.getBlock() == MarsBlocks.machine)
        {
            if (typenum == BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
            {
                index = 2;
            }
            else if (typenum == BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
            {
                index = 1;
            }
        }
        else if (this.getBlock() == MarsBlocks.machineT2)
        {
            if (typenum == BlockMachineMarsT2.GAS_LIQUEFIER_META)
            {
                return "tile.mars_machine.4";
            }
            else if (typenum == BlockMachineMarsT2.METHANE_SYNTHESIZER_META)
            {
                return "tile.mars_machine.5";
            }
            else if (typenum == BlockMachineMarsT2.ELECTROLYZER_META)
            {
                return "tile.mars_machine.6";
            }
        }

        return this.getBlock().getUnlocalizedName() + "." + index;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getUnlocalizedName()
    {
        return this.getBlock().getUnlocalizedName() + ".0";
    }

    @Override
    public boolean shouldHoldLeftHandUp(EntityPlayer player)
    {
        ItemStack currentStack = player.getCurrentEquippedItem();

        return currentStack != null && this.getBlock() == MarsBlocks.machine && currentStack.getItemDamage() == BlockMachineMars.CRYOGENIC_CHAMBER_METADATA;

    }

    @Override
    public boolean shouldHoldRightHandUp(EntityPlayer player)
    {
        ItemStack currentStack = player.getCurrentEquippedItem();

        return currentStack != null && this.getBlock() == MarsBlocks.machine && currentStack.getItemDamage() == BlockMachineMars.CRYOGENIC_CHAMBER_METADATA;

    }

    @Override
    public boolean shouldCrouch(EntityPlayer player)
    {
        return false;
    }
}
