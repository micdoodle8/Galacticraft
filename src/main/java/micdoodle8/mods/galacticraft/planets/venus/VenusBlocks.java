package micdoodle8.mods.galacticraft.planets.venus;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockBasicVenus;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockBossSpawnerVenus;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockSpout;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockTier3TreasureChest;
import micdoodle8.mods.galacticraft.planets.venus.items.ItemBlockBasicVenus;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class VenusBlocks
{
    public static Block venusBlock;
    public static Block spout;
    public static Block bossSpawner;
    public static Block treasureChestTier3;

    public static void initBlocks()
    {
        VenusBlocks.venusBlock = new BlockBasicVenus("venus");
        VenusBlocks.spout = new BlockSpout("spout");
        VenusBlocks.bossSpawner = new BlockBossSpawnerVenus("boss_spawner_venus");
        VenusBlocks.treasureChestTier3 = new BlockTier3TreasureChest("treasure_t3");

        GCBlocks.hiddenBlocks.add(VenusBlocks.bossSpawner);

        VenusBlocks.registerBlocks();
        VenusBlocks.setHarvestLevels();
        VenusBlocks.oreDictRegistration();
    }

    private static void setHarvestLevel(Block block, String toolClass, int level, int meta)
    {
        block.setHarvestLevel(toolClass, level, block.getStateFromMeta(meta));
    }

    private static void setHarvestLevel(Block block, String toolClass, int level)
    {
        block.setHarvestLevel(toolClass, level);
    }

    public static void setHarvestLevels()
    {
        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 0);
        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 1);
        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 2);
        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 3);
        setHarvestLevel(VenusBlocks.spout, "pickaxe", 1);
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemClass)
    {
        String name = block.getUnlocalizedName().substring(5);
        GCCoreUtil.registerGalacticraftBlock(name, block);
        GameRegistry.registerBlock(block, itemClass, name);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            GCBlocks.registerSorted(block);
        }
    }

    public static void registerBlocks()
    {
        registerBlock(VenusBlocks.venusBlock, ItemBlockBasicVenus.class);
        registerBlock(VenusBlocks.spout, ItemBlockGC.class);
        registerBlock(VenusBlocks.bossSpawner, ItemBlockGC.class);
        registerBlock(VenusBlocks.treasureChestTier3, ItemBlockDesc.class);
    }

    public static void oreDictRegistration()
    {
    }
}
