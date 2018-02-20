package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockWalkway;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

public class AsteroidBlocks
{
    public static Block blockWalkway;
    public static Block blockBasic;
    //	public static Block machineFrame;
    public static Block beamReflector;
    public static Block beamReceiver;
    public static Block shortRangeTelepad;
    public static Block fakeTelepad;
    public static Block blockDenseIce;
    public static Block blockMinerBase;
    public static Block minerBaseFull;
    public static Block spaceWart;

    public static void initBlocks()
    {
        AsteroidBlocks.blockWalkway = new BlockWalkway("walkway");
        AsteroidBlocks.blockBasic = new BlockBasicAsteroids("asteroids_block");
        AsteroidBlocks.beamReflector = new BlockBeamReflector("beam_reflector");
        AsteroidBlocks.beamReceiver = new BlockBeamReceiver("beam_receiver");
        AsteroidBlocks.shortRangeTelepad = new BlockShortRangeTelepad("telepad_short");
        AsteroidBlocks.fakeTelepad = new BlockTelepadFake("telepad_fake");
        AsteroidBlocks.blockDenseIce = new BlockIceAsteroids("dense_ice");
        AsteroidBlocks.blockMinerBase = new BlockMinerBase("miner_base");
        AsteroidBlocks.minerBaseFull = new BlockMinerBaseFull("miner_base_full");
        AsteroidBlocks.spaceWart = new BlockSpaceWart("spacewart");
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemClass)
    {
        String name = block.getUnlocalizedName().substring(5);
        GCCoreUtil.registerGalacticraftBlock(name, block);
        GameRegistry.registerBlock(block, itemClass, name);
        if (GCCoreUtil.getEffectiveSide() == Side.CLIENT)
        {
            GCBlocks.registerSorted(block);
        }
    }

    public static void registerBlocks()
    {
        registerBlock(AsteroidBlocks.blockBasic, ItemBlockAsteroids.class);
        registerBlock(AsteroidBlocks.blockWalkway, ItemBlockWalkway.class);
        registerBlock(AsteroidBlocks.beamReflector, ItemBlockDesc.class);
        registerBlock(AsteroidBlocks.beamReceiver, ItemBlockDesc.class);
        registerBlock(AsteroidBlocks.shortRangeTelepad, ItemBlockShortRangeTelepad.class);
        registerBlock(AsteroidBlocks.fakeTelepad, null);
        registerBlock(AsteroidBlocks.blockDenseIce, ItemBlockGC.class);
        registerBlock(AsteroidBlocks.blockMinerBase, ItemBlockDesc.class);
        registerBlock(AsteroidBlocks.minerBaseFull, null);
        registerBlock(AsteroidBlocks.spaceWart, null);
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
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 0);   //Rock
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 1);   //Rock
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 2);   //Rock
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 2, 3);   //Aluminium
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 3, 4);   //Ilmenite
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 2, 5);   //Iron
    }

    public static void oreDictRegistration()
    {
        OreDictionary.registerOre("oreAluminum", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
        OreDictionary.registerOre("oreAluminium", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
        OreDictionary.registerOre("oreNaturalAluminum", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
        OreDictionary.registerOre("oreIlmenite", new ItemStack(AsteroidBlocks.blockBasic, 1, 4));
        OreDictionary.registerOre("oreIron", new ItemStack(AsteroidBlocks.blockBasic, 1, 5));

        OreDictionary.registerOre("blockTitanium", new ItemStack(AsteroidBlocks.blockBasic, 1, 7));
    }
}
