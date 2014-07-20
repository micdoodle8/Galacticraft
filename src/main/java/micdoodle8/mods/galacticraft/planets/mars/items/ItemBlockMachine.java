package micdoodle8.mods.galacticraft.planets.mars.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMarsT2;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMachine extends ItemBlock implements IHoldableItem
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
	public String getUnlocalizedName(ItemStack itemstack)
	{
		int index = 0;
		int typenum = itemstack.getItemDamage() & 12;

		if (this.field_150939_a == MarsBlocks.machine)
		{
			if (typenum == BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
			{
				index = 2;
			}
			else if (typenum == BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
			{
				index = 1;
			}
		} else 	if (this.field_150939_a == MarsBlocks.machineT2)
		{
			if (typenum == BlockMachineMarsT2.GAS_LIQUEFIER)
			{
				return "tile.marsMachine.4";
			}
		}
		
		return this.field_150939_a.getUnlocalizedName() + "." + index;
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
		return this.field_150939_a.getUnlocalizedName() + ".0";
	}

	@Override
	public boolean shouldHoldLeftHandUp(EntityPlayer player)
	{
		ItemStack currentStack = player.getCurrentEquippedItem();

        return currentStack != null && currentStack.getItemDamage() >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA && currentStack.getItemDamage() < BlockMachineMars.LAUNCH_CONTROLLER_METADATA;

    }

	@Override
	public boolean shouldHoldRightHandUp(EntityPlayer player)
	{
		ItemStack currentStack = player.getCurrentEquippedItem();

        return currentStack != null && currentStack.getItemDamage() >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA && currentStack.getItemDamage() < BlockMachineMars.LAUNCH_CONTROLLER_METADATA;

    }

	@Override
	public boolean shouldCrouch(EntityPlayer player)
	{
		return false;
	}
}
