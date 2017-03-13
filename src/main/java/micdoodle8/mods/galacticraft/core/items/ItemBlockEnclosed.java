package micdoodle8.mods.galacticraft.core.items;

//import java.lang.reflect.Method;
//
//import appeng.api.AEApi;
//import appeng.api.util.AEColor;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed.EnumEnclosedBlockType;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

        try
        {
            name = BlockEnclosed.EnumEnclosedBlockType.byMetadata(par1ItemStack.getItemDamage()).getName();
            name = name.substring(9, name.length()); // Remove "enclosed_"
        }
        catch (Exception e)
        {
            name = "null";
        }

        return this.getBlock().getUnlocalizedName() + "." + name;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        int metadata = this.getMetadata(stack.getItemDamage());
        if (metadata == EnumEnclosedBlockType.ME_CABLE.getMeta() && CompatibilityManager.isAppEngLoaded())
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (!block.isReplaceable(worldIn, pos))
            {
                pos = pos.offset(side);
            }

            if (stack.stackSize == 0)
            {
                return false;
            }
            else if (!playerIn.canPlayerEdit(pos, side, stack))
            {
                return false;
            }
            else if (worldIn.canBlockBePlaced(this.block, pos, false, side, (Entity)null, stack))
            {
                int i = this.getMetadata(stack.getMetadata());
                IBlockState iblockstate1 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, i, playerIn);

                if (placeBlockAt(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ, iblockstate1))
                {
                    worldIn.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
                    --stack.stackSize;

//                  ItemStack itemME = AEApi.instance().definitions().parts().cableGlass().stack(AEColor.Transparent, 1);
//                  itemME.stackSize = 2; //Fool AppEng into not destroying anything in the player inventory
//                  return AEApi.instance().partHelper().placeBus( itemME, pos, side, entityplayer, world );
//                  //Might be better to do appeng.parts.PartPlacement.place( is, pos, side, player, w, PartPlacement.PlaceType.INTERACT_SECOND_PASS, 0 );
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
            return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
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
//        if (damage == 4)
//        {
//            return 0;
//        }
//        if (damage == 0)
//        {
//            return 4;
//        }
        return damage;
    }
}
