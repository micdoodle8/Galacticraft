//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.GCBlocks;
//import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
//import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
//import micdoodle8.mods.galacticraft.core.blocks.BlockMachineBase;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import net.minecraft.block.Block;
//import net.minecraft.client.entity.player.ClientPlayerEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemBlockMachine extends ItemBlockDesc
//{
//    public ItemBlockMachine(Block block)
//    {
//        super(block);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//    }
//
//    @Override
//    public int getMetadata(int damage)
//    {
//        return damage;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public Rarity getRarity(ItemStack par1ItemStack)
//    {
//        return ClientProxyCore.galacticraftItem;
//    }
//
//    @Override
//    public String getUnlocalizedName(ItemStack itemstack)
//    {
//        int index = 0;
//        int typenum = itemstack.getDamage() & 12;
//
//        if (this.getBlock() instanceof BlockMachineBase)
//        {
//            return ((BlockMachineBase) this.getBlock()).getUnlocalizedName(typenum);
//        }
//        return "tile.machine.0";
//    }
//
//    @Override
//    public void onCreated(ItemStack stack, World world, PlayerEntity player)
//    {
//        if (!world.isRemote)
//        {
//            return;
//        }
//
//        int typenum = stack.getDamage() & 12;
//
//        //The player could be a FakePlayer made by another mod e.g. LogisticsPipes
//        if (player instanceof ClientPlayerEntity)
//        {
//            if (this.getBlock() == GCBlocks.machineBase && typenum == BlockMachine.EnumMachineType.COMPRESSOR.getMetadata())
//            {
//                ClientProxyCore.playerClientHandler.onBuild(1, (ClientPlayerEntity) player);
//            }
//            else if (this.getBlock() == GCBlocks.machineBase2 && typenum == BlockMachine2.EnumMachineExtendedType.CIRCUIT_FABRICATOR.getMetadata())
//            {
//                ClientProxyCore.playerClientHandler.onBuild(2, (ClientPlayerEntity) player);
//            }
//        }
//    }
//
//    @Override
//    public String getUnlocalizedName()
//    {
//        return this.getBlock().getUnlocalizedName() + ".0";
//    }
//}
