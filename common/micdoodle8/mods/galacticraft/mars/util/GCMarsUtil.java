package micdoodle8.mods.galacticraft.mars.util;

import java.util.HashMap;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerParachest;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.recipe.GCCoreNasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityLandingBalloons;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsContainerSlimeling;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class GCMarsUtil
{
    public static void addCraftingRecipes()
    {
        HashMap<Integer, ItemStack> input = new HashMap<Integer, ItemStack>();
        input.put(1, new ItemStack(GCCoreItems.rocketNoseCone));
        input.put(2, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
        input.put(3, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
        input.put(4, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
        input.put(5, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
        input.put(6, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
        input.put(7, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
        input.put(8, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
        input.put(9, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
        input.put(10, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
        input.put(11, new ItemStack(GCMarsItems.marsItemBasic, 1, 3));
        input.put(12, new ItemStack(GCCoreItems.rocketEngine, 1, 1));
        input.put(13, new ItemStack(GCCoreItems.rocketFins));
        input.put(14, new ItemStack(GCCoreItems.rocketFins));
        input.put(15, new ItemStack(GCCoreItems.rocketEngine));
        input.put(16, new ItemStack(GCCoreItems.rocketEngine, 1, 1));
        input.put(17, new ItemStack(GCCoreItems.rocketFins));
        input.put(18, new ItemStack(GCCoreItems.rocketFins));
        input.put(19, null);
        input.put(20, null);
        input.put(21, null);
        GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 0), input);

        HashMap<Integer, ItemStack> input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Block.chest));
        input2.put(20, null);
        input2.put(21, null);
        GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, null);
        input2.put(20, new ItemStack(Block.chest));
        input2.put(21, null);
        GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, null);
        input2.put(20, null);
        input2.put(21, new ItemStack(Block.chest));
        GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Block.chest));
        input2.put(20, new ItemStack(Block.chest));
        input2.put(21, null);
        GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Block.chest));
        input2.put(20, null);
        input2.put(21, new ItemStack(Block.chest));
        GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, null);
        input2.put(20, new ItemStack(Block.chest));
        input2.put(21, new ItemStack(Block.chest));
        GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Block.chest));
        input2.put(20, new ItemStack(Block.chest));
        input2.put(21, new ItemStack(Block.chest));
        GCMarsUtil.addRocketBenchT2Recipe(new ItemStack(GCMarsItems.spaceship, 1, 3), input2);
    }

    public static void addRocketBenchT2Recipe(ItemStack result, HashMap<Integer, ItemStack> input)
    {
        GalacticraftRegistry.addT2RocketRecipe(new GCCoreNasaWorkbenchRecipe(result, input));
    }

    public static void addSmeltingRecipes()
    {
        FurnaceRecipes.smelting().addSmelting(GCMarsItems.marsItemBasic.itemID, 0, new ItemStack(GCMarsItems.marsItemBasic, 1, 2), 0.2F);
        FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.marsBlock.blockID, 4, new ItemStack(GCMarsBlocks.marsBlock, 1, 9), 0.0F);
        
        if (OreDictionary.getOres("ingotCopper").size() > 0)
        {
            FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.marsBlock.blockID, 0, OreDictionary.getOres("ingotCopper").get(0), 1.0F);
        }

        if (OreDictionary.getOres("ingotTin").size() > 0)
        {
            FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.marsBlock.blockID, 1, OreDictionary.getOres("ingotTin").get(0), 1.0F);
        }

        FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.marsBlock.blockID, 2, new ItemStack(GCMarsItems.marsItemBasic, 1, 2), 0.2F);
        FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.marsBlock.blockID, 3, new ItemStack(Item.ingotIron), 0.2F);
    }

    public static void openParachestInv(EntityPlayerMP player, GCMarsEntityLandingBalloons landerInv)
    {
        player.incrementWindowID();
        player.closeContainer();
        int windowId = player.currentWindowId;
        player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 28, new Object[] { windowId, 1, landerInv.entityId }));
        player.openContainer = new GCCoreContainerParachest(player.inventory, landerInv);
        player.openContainer.windowId = windowId;
        player.openContainer.addCraftingToCrafters(player);
    }
    
    public static void openSlimelingInventory(EntityPlayerMP player, GCMarsEntitySlimeling slimeling)
    {
        player.incrementWindowID();
        player.closeContainer();
        int windowId = player.currentWindowId;
        player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 0, new Object[] { windowId, 0, slimeling.entityId }));
        player.openContainer = new GCMarsContainerSlimeling(player.inventory, slimeling);
        player.openContainer.windowId = windowId;
        player.openContainer.addCraftingToCrafters(player);
    }
}
