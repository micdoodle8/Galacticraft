package micdoodle8.mods.galacticraft.planets.mars;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockBasicMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockCavernousVine;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.client.SkyProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.client.fx.EntityBacterialDripFX;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.*;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.client.render.entity.*;
import micdoodle8.mods.galacticraft.planets.mars.client.render.tile.TileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.entities.*;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class MarsModuleClient implements IPlanetsModuleClient
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        addVariants("mars",
                "galacticraftplanets:ore_copper_mars",
                "galacticraftplanets:ore_tin_mars",
                "galacticraftplanets:ore_desh_mars",
                "galacticraftplanets:ore_iron_mars",
                "galacticraftplanets:cobblestone",
                "galacticraftplanets:mars_surface",
                "galacticraftplanets:mars_middle",
                "galacticraftplanets:dungeon_brick",
                "galacticraftplanets:desh_block",
                "galacticraftplanets:mars_stone",
                "galacticraftplanets:dungeon_spawner");
        addVariants("cavernVines",
                "galacticraftplanets:vine_0",
                "galacticraftplanets:vine_1",
                "galacticraftplanets:vine_2");
        addVariants("itemBasicMars",
                "galacticraftplanets:rawDesh",
                "galacticraftplanets:deshStick",
                "galacticraftplanets:ingotDesh",
                "galacticraftplanets:reinforcedPlateT2",
                "galacticraftplanets:slimelingCargo",
                "galacticraftplanets:compressedDesh",
                "galacticraftplanets:fluidManip");
        addVariants("schematic",
                "galacticraftplanets:schematic_rocketT3",
                "galacticraftplanets:schematic_rocket_cargo",
                "galacticraftplanets:schematic_astroMiner");
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        MarsModuleClient.registerBlockRenderers();

        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 0, "rawDesh");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 1, "deshStick");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 2, "ingotDesh");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 3, "reinforcedPlateT2");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 4, "slimelingCargo");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 5, "compressedDesh");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 6, "fluidManip");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.key, 0, "key");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.schematic, 0, "schematic_rocketT3");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.schematic, 1, "schematic_rocket_cargo");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.schematic, 2, "schematic_astroMiner");

        MinecraftForge.EVENT_BUS.register(new TickHandlerClient());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
//            IModelCustom chamberModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/chamber.obj"));
//            IModelCustom cargoRocketModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/cargoRocket.obj"));
//
//        // Tile Entity Renderers
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChestMars.class, new TileEntityTreasureChestRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCryogenicChamber.class, new TileEntityCryogenicChamberRenderer(chamberModel));
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTerraformer.class, new TileEntityBubbleProviderRenderer(0.25F, 1.0F, 0.25F));
//
//        // Entities
//        RenderingRegistry.registerEntityRenderingHandler(EntitySludgeling.class, new RenderSludgeling());
//        RenderingRegistry.registerEntityRenderingHandler(EntitySlimeling.class, new RenderSlimeling());
//        RenderingRegistry.registerEntityRenderingHandler(EntityCreeperBoss.class, new RenderCreeperBoss());
//            RenderingRegistry.registerEntityRenderingHandler(EntityTier2Rocket.class, new RenderTier1Rocket(new ModelTier2Rocket(), GalacticraftPlanets.ASSET_PREFIX, "rocketT2"));
////        RenderingRegistry.registerEntityRenderingHandler(EntityTerraformBubble.class, new RenderBubble(0.25F, 1.0F, 0.25F));
//        RenderingRegistry.registerEntityRenderingHandler(EntityProjectileTNT.class, new RenderProjectileTNT());
//            RenderingRegistry.registerEntityRenderingHandler(EntityLandingBalloons.class, new RenderLandingBalloons());
//            RenderingRegistry.registerEntityRenderingHandler(EntityLandingBalloons.class, new RenderLandingBalloons());
//            RenderingRegistry.registerEntityRenderingHandler(EntityCargoRocket.class, new RenderCargoRocket(cargoRocketModel));

        // Add Armor Renderer Prefix
//        RenderingRegistry.addNewArmourRendererPrefix("desh");

        // Item Renderers
//        MinecraftForgeClient.registerItemRenderer(MarsItems.spaceship, new ItemRendererTier2Rocket(cargoRocketModel));
//        MinecraftForgeClient.registerItemRenderer(MarsItems.key, new ItemRendererKey(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/model/treasure.png")));
//        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MarsBlocks.machine), new ItemRendererMachine(chamberModel));
    }

    public static void registerBlockRenderers()
    {
        for (BlockBasicMars.EnumBlockBasic blockBasic : BlockBasicMars.EnumBlockBasic.values())
        {
            if (blockBasic != BlockBasicMars.EnumBlockBasic.DUNGEON_SPAWNER)
            {
                ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.marsBlock, blockBasic.getMeta(), blockBasic.getName());
            }
        }

        for (BlockCavernousVine.EnumVineType vineType : BlockCavernousVine.EnumVineType.values())
        {
            ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.vine, vineType.getMeta(), vineType.getName());
        }
    }

    private void addVariants(String name, String... variants)
    {
        Item itemBlockVariants = GameRegistry.findItem(Constants.MOD_ID_PLANETS, name);
        ModelBakery.addVariantName(itemBlockVariants, variants);
    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (side == Side.CLIENT)
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

            if (ID == GuiIdsPlanets.MACHINE_MARS)
            {
                if (tile instanceof TileEntityTerraformer)
                {
                    return new GuiTerraformer(player.inventory, (TileEntityTerraformer) tile);
                }
                else if (tile instanceof TileEntityLaunchController)
                {
                    return new GuiLaunchController(player.inventory, (TileEntityLaunchController) tile);
                }
                else if (tile instanceof TileEntityElectrolyzer)
                {
                    return new GuiWaterElectrolyzer(player.inventory, (TileEntityElectrolyzer) tile);
                }
                else if (tile instanceof TileEntityGasLiquefier)
                {
                    return new GuiGasLiquefier(player.inventory, (TileEntityGasLiquefier) tile);
                }
                else if (tile instanceof TileEntityMethaneSynthesizer)
                {
                    return new GuiMethaneSynthesizer(player.inventory, (TileEntityMethaneSynthesizer) tile);
                }
            }
        }

        return null;
    }

    @Override
    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
        {
            final double dPosX = mc.getRenderViewEntity().posX - position.x;
            final double dPosY = mc.getRenderViewEntity().posY - position.y;
            final double dPosZ = mc.getRenderViewEntity().posZ - position.z;
            EntityFX particle = null;
            final double maxDistSqrd = 64.0D;

            if (dPosX * dPosX + dPosY * dPosY + dPosZ * dPosZ < maxDistSqrd * maxDistSqrd)
            {
                if (particleID.equals("sludgeDrip"))
                {
//                    particle = new EntityDropParticleFX(mc.theWorld, position.x, position.y, position.z, Material.water); TODO
                }
    			else if (particleID.equals("bacterialDrip"))
    			{
    				particle = new EntityBacterialDripFX(mc.theWorld, position.x, position.y, position.z);
    			}
            }

            if (particle != null)
            {
                particle.prevPosX = particle.posX;
                particle.prevPosY = particle.posY;
                particle.prevPosZ = particle.posZ;
                mc.effectRenderer.addEffect(particle);
            }
        }
    }

    @Override
    public void getGuiIDs(List<Integer> idList)
    {
        idList.add(GuiIdsPlanets.MACHINE_MARS);
    }

    public static void openSlimelingGui(EntitySlimeling slimeling, int gui)
    {
        switch (gui)
        {
        case 0:
            FMLClientHandler.instance().getClient().displayGuiScreen(new GuiSlimeling(slimeling));
            break;
        case 1:
            FMLClientHandler.instance().getClient().displayGuiScreen(new GuiSlimelingFeed(slimeling));
            break;
        }
    }

    public static class TickHandlerClient
    {
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void onClientTick(ClientTickEvent event)
        {
            final Minecraft minecraft = FMLClientHandler.instance().getClient();

            final WorldClient world = minecraft.theWorld;

            if (world != null)
            {
                if (world.provider instanceof WorldProviderMars)
                {
                    if (world.provider.getSkyRenderer() == null)
                    {
                        world.provider.setSkyRenderer(new SkyProviderMars((IGalacticraftWorldProvider) world.provider));
                    }

                    if (world.provider.getCloudRenderer() == null)
                    {
                        world.provider.setCloudRenderer(new CloudRenderer());
                    }
                }
            }
        }
    }
}
