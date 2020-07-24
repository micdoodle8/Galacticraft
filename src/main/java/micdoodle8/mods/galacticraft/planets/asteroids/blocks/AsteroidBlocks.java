package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.DecoBlock;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_PLANETS)
public class AsteroidBlocks
{
    @ObjectHolder(AsteroidBlockNames.blockWalkway)
    public static Block blockWalkway;
    @ObjectHolder(AsteroidBlockNames.blockWalkwayFluid)
    public static Block blockWalkwayFluid;
    @ObjectHolder(AsteroidBlockNames.blockWalkwayWire)
    public static Block blockWalkwayWire;
    //	public static Block machineFrame;
    @ObjectHolder(AsteroidBlockNames.beamReflector)
    public static Block beamReflector;
    @ObjectHolder(AsteroidBlockNames.beamReceiver)
    public static Block beamReceiver;
    @ObjectHolder(AsteroidBlockNames.shortRangeTelepad)
    public static Block shortRangeTelepad;
    @ObjectHolder(AsteroidBlockNames.fakeTelepad)
    public static Block fakeTelepad;
    @ObjectHolder(AsteroidBlockNames.blockDenseIce)
    public static Block blockDenseIce;
    @ObjectHolder(AsteroidBlockNames.blockMinerBase)
    public static Block blockMinerBase;
    @ObjectHolder(AsteroidBlockNames.minerBaseFull)
    public static Block minerBaseFull;
    @ObjectHolder(AsteroidBlockNames.spaceWart)
    public static Block spaceWart;
    @ObjectHolder(AsteroidBlockNames.rock0)
    public static Block rock0;
    @ObjectHolder(AsteroidBlockNames.rock1)
    public static Block rock1;
    @ObjectHolder(AsteroidBlockNames.rock2)
    public static Block rock2;
    @ObjectHolder(AsteroidBlockNames.oreAluminum)
    public static Block oreAluminum;
    @ObjectHolder(AsteroidBlockNames.oreIlmenite)
    public static Block oreIlmenite;
    @ObjectHolder(AsteroidBlockNames.oreIron)
    public static Block oreIron;
    @ObjectHolder(AsteroidBlockNames.asteroidDeco)
    public static Block asteroidDeco;
    @ObjectHolder(AsteroidBlockNames.titaniumBlock)
    public static Block titaniumBlock;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt)
    {
        IForgeRegistry<Block> r = evt.getRegistry();

        Block.Properties builder = Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F).sound(SoundType.METAL);
        register(r, new BlockWalkway(builder), AsteroidBlockNames.blockWalkway);
        register(r, new BlockWalkway(builder), AsteroidBlockNames.blockWalkwayFluid);
        register(r, new BlockWalkway(builder), AsteroidBlockNames.blockWalkwayWire);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F);
        register(r, new BlockAsteroidRock(builder), AsteroidBlockNames.rock0);
        register(r, new BlockAsteroidRock(builder), AsteroidBlockNames.rock1);
        register(r, new BlockAsteroidRock(builder), AsteroidBlockNames.rock2);
        register(r, new OreBlockAsteroids(builder), AsteroidBlockNames.oreAluminum);
        register(r, new OreBlockAsteroids(builder), AsteroidBlockNames.oreIlmenite);
        register(r, new OreBlockAsteroids(builder), AsteroidBlockNames.oreIron);
        register(r, new DecoBlock(builder), AsteroidBlockNames.asteroidDeco);
        register(r, new DecoBlock(builder), AsteroidBlockNames.titaniumBlock);

        builder = Block.Properties.create(Material.IRON).sound(SoundType.METAL);
        register(r, new BlockBeamReflector(builder), AsteroidBlockNames.beamReflector);
        register(r, new BlockBeamReceiver(builder), AsteroidBlockNames.beamReceiver);

        builder = Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3.0F);
        register(r, new BlockShortRangeTelepad(builder), AsteroidBlockNames.shortRangeTelepad);

        builder = Block.Properties.create(GCBlocks.machine).sound(SoundType.METAL).hardnessAndResistance(100000.0F);
        register(r, new BlockTelepadFake(builder), AsteroidBlockNames.fakeTelepad);

        builder = Block.Properties.create(Material.ICE).sound(SoundType.GLASS).hardnessAndResistance(0.5F).slipperiness(0.98F);
        register(r, new BlockIceAsteroids(builder), AsteroidBlockNames.blockDenseIce);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F).sound(SoundType.METAL);
        register(r, new BlockMinerBase(builder), AsteroidBlockNames.blockMinerBase);
        builder = builder.hardnessAndResistance(3.0F, 35.0F);
        register(r, new BlockMinerBaseFull(builder), AsteroidBlockNames.minerBaseFull);

        builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().sound(SoundType.NETHER_WART);
        register(r, new BlockSpaceWart(builder), AsteroidBlockNames.spaceWart);
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name)
    {
        reg.register(thing.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        register(reg, thing, new ResourceLocation(Constants.MOD_ID_PLANETS, name));
    }

//    public static void registerBlocks()
//    {
//        registerBlock(AsteroidBlocks.blockBasic, ItemBlockAsteroids.class);
//        registerBlock(AsteroidBlocks.blockWalkway, ItemBlockWalkway.class);
//        registerBlock(AsteroidBlocks.beamReflector, ItemBlockDesc.class);
//        registerBlock(AsteroidBlocks.beamReceiver, ItemBlockDesc.class);
//        registerBlock(AsteroidBlocks.shortRangeTelepad, ItemBlockShortRangeTelepad.class);
//        registerBlock(AsteroidBlocks.fakeTelepad, null);
//        registerBlock(AsteroidBlocks.blockDenseIce, ItemBlockGC.class);
//        registerBlock(AsteroidBlocks.blockMinerBase, ItemBlockDesc.class);
//        registerBlock(AsteroidBlocks.minerBaseFull, null);
//        registerBlock(AsteroidBlocks.spaceWart, null);
//    }

//    public static void setHarvestLevels()
//    {
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 0);   //Rock
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 1);   //Rock
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 2);   //Rock
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 2, 3);   //Aluminium
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 3, 4);   //Ilmenite
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 2, 5);   //Iron
//    }
//
//    public static void oreDictRegistration()
//    {
//        OreDictionary.registerOre("oreAluminum", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
//        OreDictionary.registerOre("oreAluminium", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
//        OreDictionary.registerOre("oreNaturalAluminum", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
//        OreDictionary.registerOre("oreIlmenite", new ItemStack(AsteroidBlocks.blockBasic, 1, 4));
//        OreDictionary.registerOre("oreIron", new ItemStack(AsteroidBlocks.blockBasic, 1, 5));
//
//        OreDictionary.registerOre("blockTitanium", new ItemStack(AsteroidBlocks.blockBasic, 1, 7));
//    }

    @SubscribeEvent
    public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt)
    {
        IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();

        register(r, TileEntityType.Builder.create(TileEntityBeamReceiver::new, beamReceiver).build(null), AsteroidBlockNames.beamReceiver);
        register(r, TileEntityType.Builder.create(TileEntityBeamReflector::new, beamReflector).build(null), AsteroidBlockNames.beamReflector);
        register(r, TileEntityType.Builder.create(TileEntityMinerBaseSingle::new, blockMinerBase).build(null), AsteroidBlockNames.blockMinerBase);
        register(r, TileEntityType.Builder.create(TileEntityMinerBase::new, minerBaseFull).build(null), AsteroidBlockNames.minerBaseFull);
        register(r, TileEntityType.Builder.create(TileEntityShortRangeTelepad::new, shortRangeTelepad).build(null), AsteroidBlockNames.shortRangeTelepad);
        register(r, TileEntityType.Builder.create(TileEntityTelepadFake::new, fakeTelepad).build(null), AsteroidBlockNames.fakeTelepad);
    }
}
