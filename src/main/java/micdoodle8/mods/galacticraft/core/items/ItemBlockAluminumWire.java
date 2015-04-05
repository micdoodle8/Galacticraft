package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.blocks.BlockAluminumWire;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemBlockAluminumWire extends ItemBlockDesc
{
    public ItemBlockAluminumWire(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        if (par1 == 1)
        	return GCBlocks.aluminumWireHeavy.getIcon(0, 0);
    	return this.field_150939_a.getIcon(0, par1);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        String name = "";
        
        if (this.field_150939_a == GCBlocks.aluminumWireHeavy)
        	name = BlockAluminumWire.names[1];
        
        else
        {
	        switch (par1ItemStack.getItemDamage())
	        {
	        case 0:
	            name = BlockAluminumWire.names[0];
	            break;
	        case 1:
	            name = BlockAluminumWire.names[1];
		    	NBTTagCompound tag = new NBTTagCompound();
				tag.setShort("id", (short)Item.getIdFromItem(new ItemStack(GCBlocks.aluminumWireHeavy).getItem()));
		        tag.setByte("Count", (byte)par1ItemStack.stackSize);
		        tag.setShort("Damage", (short)0);
		        par1ItemStack.readFromNBT(tag);
	            break;
	        default:
	            name = "null";
	            break;
	        }
        }
        
        return "tile." + name;
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

    //Special version to ensure Heavy Aluminum Wire block gets placed in world
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
    	//Substitute Heavy Aluminum Wire
    	if (metadata == 1)
    	{
    		if (!world.setBlock(x, y, z, GCBlocks.aluminumWireHeavy, 0, 3))
    		{
    			return false;
    		}

    		if (world.getBlock(x, y, z) == GCBlocks.aluminumWireHeavy)
    		{
    			GCBlocks.aluminumWireHeavy.onBlockPlacedBy(world, x, y, z, player, stack);
    			GCBlocks.aluminumWireHeavy.onPostBlockPlaced(world, x, y, z, 0);
    		}

    		return true;
    	}

    	if (!world.setBlock(x, y, z, field_150939_a, metadata, 3))
    	{
    		return false;
    	}

    	if (world.getBlock(x, y, z) == field_150939_a)
    	{
    		field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
    		field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
    	}

    	return true;
    }

    @Override
    public int getItemStackLimit(ItemStack par1ItemStack)
    {
    	if (par1ItemStack.getItemDamage() == 1)
    	{
	    	NBTTagCompound tag = new NBTTagCompound();
			tag.setShort("id", (short)Item.getIdFromItem(new ItemStack(GCBlocks.aluminumWireHeavy).getItem()));
	        tag.setByte("Count", (byte)par1ItemStack.stackSize);
	        tag.setShort("Damage", (short)0);
	        par1ItemStack.readFromNBT(tag);
    	}
    	return super.getItemStackLimit(par1ItemStack);
    }
}
