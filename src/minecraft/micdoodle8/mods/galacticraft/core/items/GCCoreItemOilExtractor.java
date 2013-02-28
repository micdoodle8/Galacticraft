package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;

public class GCCoreItemOilExtractor extends Item
{
	public GCCoreItemOilExtractor(int par1) 
	{
		super(par1);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
		this.setMaxStackSize(1);
	}

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        float var4 = 1.0F;
        double var5 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)var4;
        double var7 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par3EntityPlayer.yOffset + 2;
        double var9 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)var4;
        MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);

        if (var12 == null)
        {
            return par1ItemStack;
        }
        else
        {
            int var13 = var12.blockX;
            int var14 = var12.blockY + 1;
            int var15 = var12.blockZ;

            if (!par2World.canMineBlock(par3EntityPlayer, var13, var14, var15))
            {
                return par1ItemStack;
            }

            if ((par2World.getBlockId(var13, var14, var15) == GCCoreBlocks.crudeOilMoving.blockID || par2World.getBlockId(var13, var14, var15) == GCCoreBlocks.crudeOilStill.blockID) && par2World.getBlockMetadata(var13, var14, var15) == 0)
            {
            	if (openCanister(par3EntityPlayer) != null)
            	{
                    par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
            	}
            }
        }
        
        return par1ItemStack;
    }

    @Override
    public void onUsingItemTick(ItemStack par1ItemStack, EntityPlayer par3EntityPlayer, int count)
    {
        float var4 = 1.0F;
        double var5 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)var4;
        double var7 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par3EntityPlayer.yOffset + 2;
        double var9 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)var4;
        MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par3EntityPlayer.worldObj, par3EntityPlayer, false);

        if (var12 == null)
        {
            return;
        }
        else
        {
            int var13 = var12.blockX;
            int var14 = var12.blockY + 1;
            int var15 = var12.blockZ;

            if (!par3EntityPlayer.worldObj.canMineBlock(par3EntityPlayer, var13, var14, var15))
            {
                return;
            }

            if ((par3EntityPlayer.worldObj.getBlockId(var13, var14, var15) == GCCoreBlocks.crudeOilMoving.blockID || par3EntityPlayer.worldObj.getBlockId(var13, var14, var15) == GCCoreBlocks.crudeOilStill.blockID) && par3EntityPlayer.worldObj.getBlockMetadata(var13, var14, var15) == 0)
            {
            	if (openCanister(par3EntityPlayer) != null)
            	{
            		par3EntityPlayer.worldObj.setBlockMetadataWithNotify(var13, var14, var15, par3EntityPlayer.worldObj.getBlockMetadata(var13, var14, var15) + 1);
            		
                	ItemStack canister = this.openCanister(par3EntityPlayer);
                	
                	if (canister != null && count % 5 == 0 && canister.getItemDamage() > 5)
                	{
                		canister.setItemDamage(canister.getItemDamage() - 5);
                	}
            	}
            }
            else
            {
            	return;
            }
        }
    }
    
    private ItemStack openCanister(EntityPlayer player)
    {
    	for (ItemStack stack : player.inventory.mainInventory)
    	{
    		if (stack != null && stack.getItem() instanceof GCCoreItemOilCanister)
    		{
    			if ((stack.getMaxDamage() - stack.getItemDamage()) >= 0 && (stack.getMaxDamage() - stack.getItemDamage()) < 60)
    			{
    				return stack;
    			}
    		}
    	}
    	
    	return null;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

    @Override
    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return par1ItemStack;
    }

    public int getIconIndex(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
		int count2 = useRemaining / 2;
		
		switch (count2 % 5)
		{
		case 0:
			if (useRemaining == 0)
			{
				return 52;
			}
			return 56;
		case 1:
			return 55;
		case 2:
			return 54;
		case 3:
			return 53;
		case 4:
			return 52;
		}
		
		return 0;
    }

    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) 
    {
    	this.iconIndex = 52;
    }
    
	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/core/client/items/core.png";
	}
}
