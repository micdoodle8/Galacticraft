package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemBlockLandingPad extends ItemBlockDesc
{
	public ItemBlockLandingPad(Block block)
	{
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		String name = "";

		switch (par1ItemStack.getItemDamage())
		{
		case 0:
			name = "landingPad";
			break;
		case 1:
			name = "buggyFueler";
			break;
		case 2:
			name = "cargoPad";
			break;
		}

		return this.field_150939_a.getUnlocalizedName() + "." + name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
}
