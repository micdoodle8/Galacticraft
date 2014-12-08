package micdoodle8.mods.galacticraft.planets.mars;

import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderBubble;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderTier1Rocket;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemRendererKey;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.client.SkyProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiGasLiquefier;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiMethaneSynthesizer;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiSlimeling;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiSlimelingFeed;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiTerraformer;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiWaterElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.client.render.block.BlockRendererCavernousVines;
import micdoodle8.mods.galacticraft.planets.mars.client.render.block.BlockRendererEgg;
import micdoodle8.mods.galacticraft.planets.mars.client.render.block.BlockRendererHydrogenPipe;
import micdoodle8.mods.galacticraft.planets.mars.client.render.block.BlockRendererMachine;
import micdoodle8.mods.galacticraft.planets.mars.client.render.block.BlockRendererTier2TreasureChest;
import micdoodle8.mods.galacticraft.planets.mars.client.render.entity.RenderCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.client.render.entity.RenderCreeperBoss;
import micdoodle8.mods.galacticraft.planets.mars.client.render.entity.RenderLandingBalloons;
import micdoodle8.mods.galacticraft.planets.mars.client.render.entity.RenderProjectileTNT;
import micdoodle8.mods.galacticraft.planets.mars.client.render.entity.RenderSlimeling;
import micdoodle8.mods.galacticraft.planets.mars.client.render.entity.RenderSludgeling;
import micdoodle8.mods.galacticraft.planets.mars.client.render.item.ItemRendererMachine;
import micdoodle8.mods.galacticraft.planets.mars.client.render.item.ItemRendererTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.client.render.tile.TileEntityCryogenicChamberRenderer;
import micdoodle8.mods.galacticraft.planets.mars.client.render.tile.TileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCreeperBoss;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityProjectileTNT;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySludgeling;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTerraformBubble;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityGasLiquefier;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTerraformer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityDropParticleFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MarsModuleClient implements IPlanetsModuleClient
{
    private static int vineRenderID;
    private static int eggRenderID;
    private static int treasureRenderID;
    private static int machineRenderID;
    private static int renderIdHydrogenPipe;

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(new TickHandlerClient());
        MarsModuleClient.vineRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockRendererCavernousVines(MarsModuleClient.vineRenderID));
        MarsModuleClient.eggRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockRendererEgg(MarsModuleClient.eggRenderID));
        MarsModuleClient.treasureRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockRendererTier2TreasureChest(MarsModuleClient.treasureRenderID));
        MarsModuleClient.machineRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockRendererMachine(MarsModuleClient.machineRenderID));
        MarsModuleClient.renderIdHydrogenPipe = RenderingRegistry.getNextAvailableRenderId();        
        RenderingRegistry.registerBlockHandler(new BlockRendererHydrogenPipe(MarsModuleClient.renderIdHydrogenPipe));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        IModelCustom chamberModel = AdvancedModelLoader.loadModel(new ResourceLocation(MarsModule.ASSET_PREFIX, "models/chamber.obj"));
        IModelCustom cargoRocketModel = AdvancedModelLoader.loadModel(new ResourceLocation(MarsModule.ASSET_PREFIX, "models/cargoRocket.obj"));

        // Tile Entity Renderers
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChestMars.class, new TileEntityTreasureChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCryogenicChamber.class, new TileEntityCryogenicChamberRenderer(chamberModel));

        // Entities
        RenderingRegistry.registerEntityRenderingHandler(EntitySludgeling.class, new RenderSludgeling());
        RenderingRegistry.registerEntityRenderingHandler(EntitySlimeling.class, new RenderSlimeling());
        RenderingRegistry.registerEntityRenderingHandler(EntityCreeperBoss.class, new RenderCreeperBoss());
        RenderingRegistry.registerEntityRenderingHandler(EntityTier2Rocket.class, new RenderTier1Rocket(new ModelTier2Rocket(), MarsModule.ASSET_PREFIX, "rocketT2"));
        RenderingRegistry.registerEntityRenderingHandler(EntityTerraformBubble.class, new RenderBubble(0.25F, 1.0F, 0.25F));
        RenderingRegistry.registerEntityRenderingHandler(EntityProjectileTNT.class, new RenderProjectileTNT());
        RenderingRegistry.registerEntityRenderingHandler(EntityLandingBalloons.class, new RenderLandingBalloons());
        RenderingRegistry.registerEntityRenderingHandler(EntityLandingBalloons.class, new RenderLandingBalloons());
        RenderingRegistry.registerEntityRenderingHandler(EntityCargoRocket.class, new RenderCargoRocket(cargoRocketModel));

        // Add Armor Renderer Prefix
        RenderingRegistry.addNewArmourRendererPrefix("desh");

        // Item Renderers
        MinecraftForgeClient.registerItemRenderer(MarsItems.spaceship, new ItemRendererTier2Rocket(cargoRocketModel));
        MinecraftForgeClient.registerItemRenderer(MarsItems.key, new ItemRendererKey(new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/model/treasure.png")));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MarsBlocks.machine), new ItemRendererMachine(chamberModel));
    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (side == Side.CLIENT)
        {
            TileEntity tile = world.getTileEntity(x, y, z);

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
    public int getBlockRenderID(Block block)
    {
        if (block == MarsBlocks.vine)
        {
            return MarsModuleClient.vineRenderID;
        }
        else if (block == MarsBlocks.hydrogenPipe)
        {
            return MarsModuleClient.renderIdHydrogenPipe;
        }
        else if (block == MarsBlocks.rock)
        {
            return MarsModuleClient.eggRenderID;
        }
        else if (block == MarsBlocks.machine || block == MarsBlocks.machineT2)
        {
            return MarsModuleClient.machineRenderID;
        }
        else if (block == MarsBlocks.tier2TreasureChest)
        {
            return MarsModuleClient.treasureRenderID;
        }

        return -1;
    }

    @Override
    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
        {
            final double dPosX = mc.renderViewEntity.posX - position.x;
            final double dPosY = mc.renderViewEntity.posY - position.y;
            final double dPosZ = mc.renderViewEntity.posZ - position.z;
            EntityFX particle = null;
            final double maxDistSqrd = 64.0D;

            if (dPosX * dPosX + dPosY * dPosY + dPosZ * dPosZ < maxDistSqrd * maxDistSqrd)
            {
                if (particleID.equals("sludgeDrip"))
                {
                    particle = new EntityDropParticleFX(mc.theWorld, position.x, position.y, position.z, Material.water);
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
