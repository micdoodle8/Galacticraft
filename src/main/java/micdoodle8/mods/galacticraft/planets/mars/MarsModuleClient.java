package micdoodle8.mods.galacticraft.planets.mars;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.client.objload.AdvancedModelLoader;
import micdoodle8.mods.galacticraft.core.client.objload.IModelCustom;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderBubble;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderTier1Rocket;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.client.SkyProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.*;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.client.render.entity.*;
import micdoodle8.mods.galacticraft.planets.mars.client.render.tile.TileEntityCryogenicChamberRenderer;
import micdoodle8.mods.galacticraft.planets.mars.client.render.tile.TileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.entities.*;
import micdoodle8.mods.galacticraft.planets.mars.tile.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

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
//        MarsModuleClient.vineRenderID = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(new BlockRendererCavernousVines(MarsModuleClient.vineRenderID));
//        MarsModuleClient.eggRenderID = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(new BlockRendererEgg(MarsModuleClient.eggRenderID));
//        MarsModuleClient.treasureRenderID = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(new BlockRendererTier2TreasureChest(MarsModuleClient.treasureRenderID));
//        MarsModuleClient.machineRenderID = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(new BlockRendererMachine(MarsModuleClient.machineRenderID));
//        MarsModuleClient.renderIdHydrogenPipe = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(new BlockRendererHydrogenPipe(MarsModuleClient.renderIdHydrogenPipe));
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
//        RenderingRegistry.addNewArmourRendererPrefix("desh");

        // Item Renderers
//        MinecraftForgeClient.registerItemRenderer(MarsItems.spaceship, new ItemRendererTier2Rocket(cargoRocketModel));
//        MinecraftForgeClient.registerItemRenderer(MarsItems.key, new ItemRendererKey(new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/model/treasure.png")));
//        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MarsBlocks.machine), new ItemRendererMachine(chamberModel));
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
