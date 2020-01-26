package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineBase;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMachine extends ItemBlockDesc
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
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        int index = 0;
        int typenum = itemstack.getItemDamage() & 12;

        if (this.getBlock() instanceof BlockMachineBase)
        {
            return ((BlockMachineBase) this.getBlock()).getUnlocalizedName(typenum);
        }
        return "tile.machine.0";
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            return;
        }

        int typenum = stack.getItemDamage() & 12;

        //The player could be a FakePlayer made by another mod e.g. LogisticsPipes
        if (player instanceof EntityPlayerSP)
        {
            if (this.getBlock() == GCBlocks.machineBase && typenum == BlockMachine.EnumMachineType.COMPRESSOR.getMetadata())
            {
                ClientProxyCore.playerClientHandler.onBuild(1, (EntityPlayerSP) player);
            }
            else if (this.getBlock() == GCBlocks.machineBase2 && typenum == BlockMachine2.EnumMachineExtendedType.CIRCUIT_FABRICATOR.getMetadata())
            {
                ClientProxyCore.playerClientHandler.onBuild(2, (EntityPlayerSP) player);
            }
        }
    }

    @Override
    public String getUnlocalizedName()
    {
        return this.getBlock().getUnlocalizedName() + ".0";
    }
}
