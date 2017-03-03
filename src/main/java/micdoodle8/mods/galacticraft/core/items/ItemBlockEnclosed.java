package micdoodle8.mods.galacticraft.core.items;

import java.lang.reflect.Method;

import appeng.api.AEApi;
import appeng.api.util.AEColor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed.EnumEnclosedBlock;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

@SuppressWarnings("unused")
public class ItemBlockEnclosed extends ItemBlockDesc
{
    public ItemBlockEnclosed(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        String name;

        switch (par1ItemStack.getItemDamage())
        {
        case 0:
            name = "null";
            break;
        case 1:
            name = "oxygenPipe";
            break;
        case 2:
            name = "copperCable";
            break;
        case 3:
            name = "goldCable";
            break;
        case 4:
            name = "hvCable";
            break;
        case 5:
            name = "glassFibreCable";
            break;
        case 6:
            name = "lvCable";
            break;
        case 13:
            name = "meCable";
            break;
        case 14:
            name = "aluminumWire";
            break;
        case 15:
            name = "aluminumWireHeavy";
            break;
        default:
        	//The BuildCraft pipes
            try
            {
                name = BlockEnclosed.getTypeFromMeta(par1ItemStack.getItemDamage()).getPipeType();
            }
            catch (Exception e)
            {
                name = "null";
            }
            break;
        }

        return this.field_150939_a.getUnlocalizedName() + "." + name;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float par8, float par9, float par10)
    {
        int metadata = this.getMetadata(itemstack.getItemDamage());
        if (metadata == EnumEnclosedBlock.ME_CABLE.getMetadata() && CompatibilityManager.isAppEngLoaded())
        {
        	int x = i;
        	int y = j;
        	int z = k;
        	Block block = world.getBlock(i, j, k);

        	if (block == Blocks.snow_layer && (world.getBlockMetadata(i, j, k) & 7) < 1)
        	{
        		side = 1;
        	}
        	else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, i, j, k))
        	{
        		if (side == 0)
        		{
        			j--;
        		}
        		if (side == 1)
        		{
        			j++;
        		}
        		if (side == 2)
        		{
        			k--;
        		}
        		if (side == 3)
        		{
        			k++;
        		}
        		if (side == 4)
        		{
        			i--;
        		}
        		if (side == 5)
        		{
        			i++;
        		}
        	}

        	if (itemstack.stackSize == 0)
        	{
        		return false;
        	}

        	if (!entityplayer.canPlayerEdit(i, j, k, side, itemstack))
        	{
        		return false;
        	}
        	else if (j == 255 && this.field_150939_a.getMaterial().isSolid())
        	{
        		return false;
        	}
        	else if (world.canPlaceEntityOnSide(block, i, j, k, false, side, entityplayer, itemstack))
        	{
        		int j1 = this.field_150939_a.onBlockPlaced(world, i, j, k, side, par8, par9, par10, metadata);

        		if (placeBlockAt(itemstack, entityplayer, world, i, j, k, side, par8, par9, par10, j1))
        		{
        			world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
        			--itemstack.stackSize;
        			
        			ItemStack itemME = AEApi.instance().definitions().parts().cableGlass().stack(AEColor.Transparent, 1);
        			itemME.stackSize = 2; //Fool AppEng into not destroying anything in the player inventory
        			return AEApi.instance().partHelper().placeBus( itemME, x, y, z, side, entityplayer, world );
        			//Might be better to do appeng.parts.PartPlacement.place( is, x, y, z, side, player, w, PartPlacement.PlaceType.INTERACT_SECOND_PASS, 0 );
        		}
        		return true;
        	}
        	else
        	{
        		return false;
        	}
        }
        else
        {
        	return super.onItemUse(itemstack, entityplayer, world, i, j, k, side, par8, par9, par10);
        }
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
        //TE_CONDUIT (item damage 0: currently unused) and HV_CABLE (item damage 4) have had to have swapped metadata in 1.7.10 because IC2's TileCable tile entity doesn't like a block with metadata 4
    	if (damage == 4) return 0;
        if (damage == 0) return 4;
    	return damage;
    }
}
