package micdoodle8.mods.galacticraft.core.items;

import java.lang.reflect.Method;

import appeng.api.AEApi;
import appeng.api.util.AEColor;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed.EnumEnclosedBlockType;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
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
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        int metadata = this.getMetadata(itemstack.getItemDamage());
        if (metadata == EnumEnclosedBlockType.ME_CABLE.getMeta() && CompatibilityManager.isAppEngLoaded())
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            BlockPos origPos = pos;

            if (!block.isReplaceable(worldIn, pos))
            {
                pos = pos.offset(side);
            }

            if (itemstack.getCount() == 0)
            {
                return EnumActionResult.FAIL;
            }
            else if (!playerIn.canPlayerEdit(pos, side, itemstack))
            {
                return EnumActionResult.FAIL;
            }
            else if (worldIn.mayPlace(this.block, pos, false, side, null))
            {
                int i = this.getMetadata(itemstack.getMetadata());
                IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, side, hitX, hitY, hitZ, i, playerIn);

                if (placeBlockAt(itemstack, playerIn, worldIn, pos, side, hitX, hitY, hitZ, iblockstate1))
                {
                    SoundType soundType = this.getBlock().getSoundType(iblockstate, worldIn, pos, playerIn);
                    worldIn.playSound(playerIn, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, soundType.getPlaceSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                    itemstack.shrink(1);

                    ItemStack itemME = AEApi.instance().definitions().parts().cableGlass().stack(AEColor.TRANSPARENT, 1);
                    itemME.setCount(2); //Fool AppEng into not destroying anything in the player inventory
                    AEApi.instance().partHelper().placeBus( itemME, origPos, side, playerIn, hand, worldIn );
                    //Emulate appeng.parts.PartPlacement.place( is, pos, side, player, w, PartPlacement.PlaceType.INTERACT_SECOND_PASS, 0 );
                    try
                    {
                        Class clazzpp = Class.forName("appeng.parts.PartPlacement");
                        Class enumPlaceType = Class.forName("appeng.parts.PartPlacement$PlaceType");
                        Method methPl = clazzpp.getMethod("place", ItemStack.class, BlockPos.class, EnumFacing.class, EntityPlayer.class, EnumHand.class, World.class, enumPlaceType, int.class);
                        methPl.invoke(null, itemME, origPos, side, playerIn, hand, worldIn, enumPlaceType.getEnumConstants()[2], 0 );
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                return EnumActionResult.SUCCESS;
            }
            else
            {
                return EnumActionResult.FAIL;
            }
        }
        else
        {
            return super.onItemUse(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ);
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
