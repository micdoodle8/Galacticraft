package micdoodle8.mods.galacticraft.core.items;

import java.util.logging.Level;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockEnclosed;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockEnclosed.EnumEnclosedBlock;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Property;
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftTransport;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.Pipe;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
        case 2:
            name = "copperCable";
            break;
        default:
            name = GCCoreBlockEnclosed.getTypeFromMeta(par1ItemStack.getItemDamage()).getPipeClass();
            break;
        }

        return Block.blocksList[this.getBlockID()].getUnlocalizedName() + "." + name;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float par8, float par9, float par10)
    {
        EnumEnclosedBlock type = GCCoreBlockEnclosed.getTypeFromMeta(itemstack.getItemDamage());

        if (type != null && type.getPipeClass() != null)
        {
            int blockID = GCCoreBlocks.enclosedWire.blockID;
            Block block = BuildCraftTransport.genericPipeBlock;

            int id = world.getBlockId(i, j, k);

            if (id == Block.snow.blockID)
            {
                side = 1;
            }
            else if (id != Block.vine.blockID && id != Block.tallGrass.blockID && id != Block.deadBush.blockID && (Block.blocksList[id] == null || !Block.blocksList[id].isBlockReplaceable(world, i, j, k)))
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
            
            if (entityplayer.canCurrentToolHarvestBlock(i, j, k) && world.canPlaceEntityOnSide(blockID, i, j, k, false, side, entityplayer, itemstack))
            {
                String name = Character.toLowerCase(type.getPipeClass().charAt(0)) + type.getPipeClass().substring(1);
                // -1 is safe since they will have already been set
                Property prop = BuildCraftCore.mainConfiguration.getItem(name + ".id", -1);
                int pipeID = prop.getInt(-1);
                Pipe pipe = BlockGenericPipe.createPipe(pipeID + 256);
                
                if (pipe == null)
                {
                    BuildCraftCore.bcLog.log(Level.WARNING, "Pipe failed to create during placement at {0},{1},{2}", new Object[] { i, j, k });
                    return true;
                }
                
                if (BlockGenericPipe.placePipe(pipe, world, i, j, k, blockID, type.getMetadata()))
                {

                    Block.blocksList[blockID].onBlockPlacedBy(world, i, j, k, entityplayer, itemstack);
                    world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                    itemstack.stackSize--;
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
        return damage;
    }
}
