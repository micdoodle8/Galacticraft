package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import universalelectricity.components.common.tileentity.TileEntityCopperWire;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;

public class GCCoreItemBlockEnclosedBlock extends ItemBlock
{
	public GCCoreItemBlockEnclosedBlock(int id)
	{
		super(id);
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
    		name = "copperWire";
    		break;
    	case 1:
    		name = "oxygenPipe";
    		break;
    	}

		return Block.blocksList[this.getBlockID()].getUnlocalizedName() + "." + name;
    }

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (par1ItemStack.getItemDamage() == 0)
		{
			par3List.add("Resistance: " + ElectricityDisplay.getDisplay(TileEntityCopperWire.RESISTANCE, ElectricUnit.RESISTANCE));
			par3List.add("Max Amps: " + ElectricityDisplay.getDisplay(TileEntityCopperWire.MAX_AMPS, ElectricUnit.AMPERE));
		}
	}
}
