package micdoodle8.mods.galacticraft.planets.deepspace;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockBasicSpace.EnumBlockSpace;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockDeepStructure;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockFlooring.EnumBlockFlooring;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockInterior.EnumBlockInterior;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockSurface.EnumBlockSurface;
import micdoodle8.mods.galacticraft.planets.deepspace.client.ModelDeepStructural;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class DeepSpaceModuleClient implements IPlanetsModuleClient
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
    }

    @Override
    public void registerVariants()
    {
        ModelLoader.setCustomStateMapper(DeepSpaceBlocks.deepStructure, new StateMap.Builder().ignore(BlockDeepStructure.H).build());
        ModelLoader.setCustomStateMapper(DeepSpaceBlocks.deepWall, new StateMap.Builder().ignore(BlockDeepStructure.H).build());
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        ModelResourceLocation defaultLoc = new ModelResourceLocation(GalacticraftPlanets.ASSET_PREFIX + ":deep", "normal");
        event.getModelRegistry().putObject(defaultLoc, new ModelDeepStructural(false));
        defaultLoc = new ModelResourceLocation(GalacticraftPlanets.ASSET_PREFIX + ":deep_wall", "normal");
        event.getModelRegistry().putObject(defaultLoc, new ModelDeepStructural(true));
    }

    private void replaceModelDefault(ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState, String... variants)
    {
        ClientUtil.replaceModel(GalacticraftPlanets.ASSET_PREFIX, event, resLoc, objLoc, visibleGroups, clazz, parentState, variants);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void loadTextures(TextureStitchEvent.Pre event)
    {
    }

    private void registerTexture(TextureStitchEvent.Pre event, String texture)
    {
        event.getMap().registerSprite(new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/" + texture));
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        DeepSpaceModuleClient.registerBlockRenderers();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        addPlanetVariants(DeepSpaceBlocks.spaceBasic, EnumBlockSpace.values());
        addPlanetVariants(DeepSpaceBlocks.surface, EnumBlockSurface.values());
        addPlanetVariants(DeepSpaceBlocks.interior, EnumBlockInterior.values());
        addPlanetVariants(DeepSpaceBlocks.flooring, EnumBlockFlooring.values());
    }

    public static void registerBlockRenderers()
    {
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, DeepSpaceBlocks.deepStructure);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, DeepSpaceBlocks.deepWall);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, DeepSpaceBlocks.glassProtective);
        DeepSpaceModuleClient.registerSubBlocks(DeepSpaceBlocks.spaceBasic, EnumBlockSpace.values());
        DeepSpaceModuleClient.registerSubBlocks(DeepSpaceBlocks.surface, EnumBlockSurface.values());
        DeepSpaceModuleClient.registerSubBlocks(DeepSpaceBlocks.interior, EnumBlockInterior.values());
        DeepSpaceModuleClient.registerSubBlocks(DeepSpaceBlocks.flooring, EnumBlockFlooring.values());
    }
    
    private static void registerSubBlocks(Block b, IStringSerializable[] subBlocks)
    {
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, b, 0, b.getUnlocalizedName().substring(5));
        for (int i = 1; i < subBlocks.length; i++)
        {
            ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, b, i, subBlocks[i].getName());
        }
    }

    private void addPlanetVariants(String name, String... variants)
    {
        Item itemBlockVariants = GameRegistry.findItem(Constants.MOD_ID_PLANETS, name);
        ResourceLocation[] variants0 = new ResourceLocation[variants.length];
        for (int i = 0; i < variants.length; ++i)
        {
            variants0[i] = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + variants[i]);
        }
        ModelBakery.registerItemVariants(itemBlockVariants, variants0);
    }

    private void addPlanetVariants(Block b, IStringSerializable[] variants)
    {
        String name = b.getUnlocalizedName().substring(5);
        Item itemBlockVariants = GameRegistry.findItem(Constants.MOD_ID_PLANETS, name);
        ResourceLocation[] resources = new ResourceLocation[variants.length];
        resources[0] = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + name);
        for (int i = 1; i < variants.length; ++i)
        {
            resources[i] = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + variants[i].getName());
        }
        ModelBakery.registerItemVariants(itemBlockVariants, resources);
    }

    @Override
    public void getGuiIDs(List<Integer> idList)
    {
    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
    {
    }
}
