//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
//import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
//import micdoodle8.mods.galacticraft.core.blocks.BlockSolar;
//import micdoodle8.mods.galacticraft.core.client.model.block.ModelSolarPanel;
//import micdoodle8.mods.galacticraft.core.client.objload.AdvancedModelLoader;
//import micdoodle8.mods.galacticraft.core.client.objload.IModelCustom;
//import micdoodle8.mods.galacticraft.core.tile.*;
//import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.WorldRenderer;
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//public class TileEntityMachineRenderer extends TileEntitySpecialRenderer
//{
//    private static final ResourceLocation oxygenTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/oxygenStorageModule.png");
//    private static final ResourceLocation oxygenOutput = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/machine_oxygen_output.png");
//    private static final ResourceLocation oxygenInput = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/machine_oxygen_input.png");
//    private static final ResourceLocation machineTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/machine.png");
//    private static final ResourceLocation machineSideTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/machine_side.png");
//    private static final ResourceLocation compressorTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/electric_compressor.png");
//    private static final ResourceLocation electricInput = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/machine_input.png");
//    private static final ResourceLocation circuitFabricator = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/circuit_fabricator.png");
//    private static final ResourceLocation oxygenBlockTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/oxygen_block.png");
//    private static final IModelCustom blockBasic = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/basicBlock.obj"));
//
//    @Override
//    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTickTime, int par9)
//    {
//        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
//        if (!state.getProperties().containsKey(BlockMachine2.FACING))
//        {
//            return;
//        }
//        EnumFacing facing = (EnumFacing)state.getValue(BlockMachine2.FACING);
//
//        // Texture file
//        GL11.glPushMatrix();
//        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
//
//        GL11.glRotatef(facing.getHorizontalIndex() * -90.0F, 0.0F, 1.0F, 0.0F);
//
//        if (tile.getBlockMetadata() >= BlockMachine2.CIRCUIT_FABRICATOR_METADATA && tile.getBlockMetadata() < BlockMachine2.CIRCUIT_FABRICATOR_METADATA + 4)
//        {
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(circuitFabricator);
//            blockBasic.renderPart("North");
//
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(machineSideTexture);
//            blockBasic.renderPart("South");
//            blockBasic.renderPart("East");
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(electricInput);
//            blockBasic.renderPart("West");
//
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(machineTexture);
//            blockBasic.renderPart("Up");
//            blockBasic.renderPart("Down");
//        }
//        else if (tile.getBlockMetadata() >= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA && tile.getBlockMetadata() < BlockMachine2.ELECTRIC_COMPRESSOR_METADATA + 4)
//        {
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(compressorTexture);
//            blockBasic.renderPart("North");
//
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(machineSideTexture);
//            blockBasic.renderPart("South");
//            blockBasic.renderPart("East");
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(electricInput);
//            blockBasic.renderPart("West");
//
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(machineTexture);
//            blockBasic.renderPart("Up");
//            blockBasic.renderPart("Down");
//        }
//        else if (tile.getBlockMetadata() >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA && tile.getBlockMetadata() < BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA + 4)
//        {
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(oxygenTexture);
//            blockBasic.renderPart("North");
//            blockBasic.renderPart("South");
//
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(oxygenOutput);
//            blockBasic.renderPart("East");
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(oxygenInput);
//            blockBasic.renderPart("West");
//
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(machineTexture);
//            blockBasic.renderPart("Up");
//            blockBasic.renderPart("Down");
//
//            TileEntityOxygenStorageModule storageModule = (TileEntityOxygenStorageModule) tile;
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(oxygenBlockTexture);
//
//            float percent = storageModule.getOxygenStored() / storageModule.getMaxOxygenStored();
//
//            for (int i = 0; i < 2; ++i)
//            {
//                GL11.glPushMatrix();
//
//                GL11.glRotatef(i * 180, 0.0F, 1.0F, 0.0F);
//
//                Tessellator tessellator = Tessellator.getInstance();
//                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
//                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
//                worldRenderer.normal(0.0F, 0.0F, 1.0F);
//                worldRenderer.pos(-0.188F, -0.25F + percent * 0.5F, -0.501F).tex(0.0F, percent).endVertex();
//                worldRenderer.pos(0.188F, -0.25F + percent * 0.5F, -0.501F).tex(1.0F, percent).endVertex();
//                worldRenderer.pos(0.188F, -0.25F, -0.501F).tex(1.0F, 0.0F).endVertex();
//                worldRenderer.pos(-0.188F, -0.25F, -0.501F).tex(0.0F, 0.0F).endVertex();
//                tessellator.draw();
//
//                GL11.glPopMatrix();
//            }
//        }
//
//        GL11.glPopMatrix();
//    }
//}
