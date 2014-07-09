package micdoodle8.mods.galacticraft.planets.mars.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
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
		int metadata = 0;

		if (itemstack.getItemDamage() >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
		{
			metadata = 2;
		}
		else if (itemstack.getItemDamage() >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
			metadata = 1;
		}

		return this.field_150939_a.getUnlocalizedName() + "." + metadata;
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
