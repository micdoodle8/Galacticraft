package micdoodle8.mods.galacticraft.core.blocks;

import appeng.api.AEApi;
import appeng.api.parts.IPartHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenPipe;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class BlockEnclosed extends BlockContainer implements IPartialSealableBlock, ITileEntityProvider, ItemBlockDesc.IBlockShiftDesc
{
    private IIcon[] enclosedIcons;
    public static Item[] pipeItemsBC = new Item[6];
    public static BlockContainer blockPipeBC = null;
    public static Method onBlockNeighbourChangeIC2 = null;

    public enum EnumEnclosedBlock
    {
        TE_CONDUIT(4, 2, null, "enclosed_te_conduit"), //CURRENTLY UNUSED
        OXYGEN_PIPE(1, -1, null, "enclosed_oxygen_pipe"),
        IC2_COPPER_CABLE(2, 0, null, "enclosed_copper_cable"),
        IC2_GOLD_CABLE(3, 3, null, "enclosed_gold_cable"),
        IC2_HV_CABLE(0, 6, null, "enclosed_hv_cable"),
        IC2_GLASS_FIBRE_CABLE(5, 9, null, "enclosed_glassfibre_cable"),
        IC2_LV_CABLE(6, 13, null, "enclosed_lv_cable"),
        BC_ITEM_STONEPIPE(7, -1, "PipeItemsStone", "enclosed_itempipe_stone"),
        BC_ITEM_COBBLESTONEPIPE(8, -1, "PipeItemsCobblestone", "enclosed_itempipe_cobblestone"),
        BC_FLUIDS_STONEPIPE(9, -1, "PipeFluidsStone", "enclosed_liquidpipe_stone"),
        BC_FLUIDS_COBBLESTONEPIPE(10, -1, "PipeFluidsCobblestone", "enclosed_liquidpipe_cobblestone"),
        BC_POWER_STONEPIPE(11, -1, "PipePowerStone", "enclosed_powerpipe_stone"),
        BC_POWER_GOLDPIPE(12, -1, "PipePowerGold", "enclosed_powerpipe_gold"),
        ME_CABLE(13, -1, null, "enclosed_me_cable"),
        ALUMINUM_WIRE(14, -1, null, "enclosed_aluminum_wire"),
        ALUMINUM_WIRE_HEAVY(15, -1, null, "enclosed_heavy_aluminum_wire");

        int metadata;
        int subMeta;
        String pipeType;
        String texture;

        EnumEnclosedBlock(int metadata, int subMeta, String pipeTypeBC, String texture)
        {
            this.metadata = metadata;
            this.subMeta = subMeta;
            this.pipeType = pipeTypeBC;
            this.texture = texture;
        }

        public int getMetadata()
        {
            return this.metadata;
        }

        public int getSubMetaValue()
        {
            return this.subMeta;
        }

        public String getPipeType()
        {
            return this.pipeType;
        }

        public String getTexture()
        {
            return this.texture;
        }
    }

    public static EnumEnclosedBlock getTypeFromMeta(int metadata)
    {
        for (EnumEnclosedBlock type : EnumEnclosedBlock.values())
        {
            if (type.getMetadata() == metadata)
            {
                return type;
            }
        }

        return null;
    }

    public BlockEnclosed(String assetName)
    {
        super(Material.clay);
        this.setResistance(0.2F);
        this.setHardness(0.4f);
        this.setStepSound(Block.soundTypeStone);
        this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setBlockName(assetName);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.ALUMINUM_WIRE.getMetadata()));
        par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.ALUMINUM_WIRE_HEAVY.getMetadata()));
        par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.OXYGEN_PIPE.getMetadata()));

        if (CompatibilityManager.isTELoaded())
        {
            // par3List.add(new ItemStack(par1, 1, 0));
        }

        if (CompatibilityManager.isIc2Loaded())
        {
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_COPPER_CABLE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_GOLD_CABLE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, 4));  //Damage value not same as metadata for HV_CABLE
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_GLASS_FIBRE_CABLE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_LV_CABLE.getMetadata()));
        }

        if (CompatibilityManager.isBCraftTransportLoaded())
        {
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_ITEM_COBBLESTONEPIPE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_ITEM_STONEPIPE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_FLUIDS_COBBLESTONEPIPE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_FLUIDS_STONEPIPE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_POWER_STONEPIPE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata()));
        }

        if (CompatibilityManager.isAppEngLoaded())
        {
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.ME_CABLE.getMetadata()));
        }
    }

    public static void initialiseBC()
    {
    	for (int i = 0; i < 6; i++)
    	{
    		try {
    			Class<?> clazzBC = Class.forName("buildcraft.BuildCraftTransport");
    			String pipeName = EnumEnclosedBlock.values()[i+7].getPipeType();
    			pipeName = pipeName.substring(0, 1).toLowerCase() + pipeName.substring(1);
    			pipeItemsBC[i] = (Item) clazzBC.getField(pipeName).get(null);
    		}
    		catch (Exception e) { e.printStackTrace(); }
        }
	}

	@Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int meta)
    {
        if (meta == 4) meta = 0;  //Deal with item rendering for the HV block
    	return meta >= this.enclosedIcons.length ? this.blockIcon : this.enclosedIcons[meta];
    }

    @Override
    public int damageDropped(int meta)
    {
        //TE_CONDUIT and HV_CABLE have had to have swapped metadata in 1.7.10 because IC2's TileCable tile entity doesn't like a block with metadata 4
    	if (meta == 0) return 4;
        if (meta == 4) return 0;
    	return meta;
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.enclosedIcons = new IIcon[16];

        for (int i = 0; i < EnumEnclosedBlock.values().length; i++)
        {
            this.enclosedIcons[EnumEnclosedBlock.values()[i].getMetadata()] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + EnumEnclosedBlock.values()[i].getTexture());
        }

        this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "" + EnumEnclosedBlock.OXYGEN_PIPE.getTexture());
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        final TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (metadata == EnumEnclosedBlock.TE_CONDUIT.getMetadata())
        {
            super.onNeighborBlockChange(world, x, y, z, block);
        }
        else if (metadata == EnumEnclosedBlock.OXYGEN_PIPE.getMetadata())
        {
            super.onNeighborBlockChange(world, x, y, z, block);

            if (tileEntity instanceof INetworkConnection)
            {
                ((INetworkConnection) tileEntity).refresh();
            }
        }
        else if (metadata <= 6)
        {
        	super.onNeighborBlockChange(world, x, y, z, block);
        	if (CompatibilityManager.isIc2Loaded() && tileEntity != null)
            {
                try
                {
                	onBlockNeighbourChangeIC2.invoke(tileEntity);
                	return;
                }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
        else if (metadata <= 12)
        {
            if (CompatibilityManager.isBCraftTransportLoaded())
            {
            	if (blockPipeBC != null)
            	{
            		try {
            			blockPipeBC.onNeighborBlockChange(world, x, y, z, block);
            		}
            		catch (Exception e) { e.printStackTrace(); }
            		return;
            	}
            }

            super.onNeighborBlockChange(world, x, y, z, block);
        }
        else if (metadata <= EnumEnclosedBlock.ME_CABLE.getMetadata())
        {
            super.onNeighborBlockChange(world, x, y, z, block);
            if (CompatibilityManager.isAppEngLoaded())
            {
                world.markBlockForUpdate(x, y, z);
            }
        }
        else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE.getMetadata())
        {
            super.onNeighborBlockChange(world, x, y, z, block);
            if (tileEntity instanceof IConductor)
            {
                ((IConductor) tileEntity).refresh();
            }
        }
        else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE_HEAVY.getMetadata())
        {
            super.onNeighborBlockChange(world, x, y, z, block);
            if (tileEntity instanceof IConductor)
            {
                ((IConductor) tileEntity).refresh();
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
    	if (metadata == EnumEnclosedBlock.TE_CONDUIT.getMetadata())
        {
    		//TODO
        }
        else if (metadata == EnumEnclosedBlock.OXYGEN_PIPE.getMetadata())
        {
            return new TileEntityOxygenPipe();
        }
        else if (metadata <= 6)
        {
            if (CompatibilityManager.isIc2Loaded())
            {
                try
                {
                    Class<?> clazz = Class.forName("ic2.core.block.wiring.TileEntityCable");
                    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                    Constructor<?> constructor = null;

                    for (Constructor<?> constructor2 : constructors)
                    {
                        constructor = constructor2;

                        if (constructor.getGenericParameterTypes().length == 1)
                        {
                            break;
                        }
                    }

                    constructor.setAccessible(true);

                    return (TileEntity) constructor.newInstance((short) BlockEnclosed.getTypeFromMeta(metadata).getSubMetaValue());
                }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
        else if (metadata <= 12)
        {
            if (CompatibilityManager.isBCraftTransportLoaded())
            {
            	try
            	{
            		return blockPipeBC.createNewTileEntity(world, 0);
            	}
            	catch (Exception e) { e.printStackTrace(); }
            }
        }
        else if (metadata <= EnumEnclosedBlock.ME_CABLE.getMetadata())
        {
            if (CompatibilityManager.isAppEngLoaded())
            {
            	//Api.INSTANCE.partHelper().getCombinedInstance( TileCableBus.class.getName() )
            	try
                {
                    IPartHelper apiPart = AEApi.instance().partHelper();
                    Class<?> clazzApiPart = Class.forName("appeng.core.api.ApiPart");
                    Class clazz = (Class) clazzApiPart.getDeclaredMethod("getCombinedInstance", String.class).invoke(apiPart, "appeng.tile.networking.TileCableBus");
                    //Needs to be: appeng.parts.layers.LayerITileStorageMonitorable_TileCableBus
                    return (TileEntity) clazz.newInstance();
                }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
        else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE.getMetadata())
        {
            return new TileEntityAluminumWire(1);
        }
        else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE_HEAVY.getMetadata())
        {
            return new TileEntityAluminumWire(2);
        }
   	
        return null;
    }

    @Override
    public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction)
    {
        return true;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
    
    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int metadata)
    {
        if (metadata >= EnumEnclosedBlock.BC_ITEM_STONEPIPE.getMetadata() && metadata <= EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata())
        {
        	EnumEnclosedBlock type = BlockEnclosed.getTypeFromMeta(metadata);
        	if (CompatibilityManager.isBCraftTransportLoaded() && type != null && type.getPipeType() != null)
            {
                BlockEnclosed.initialiseBCPipe(world, x, y, z, metadata);
            }
        }   	
    }

	public static void initialiseBCPipe(World world, int i, int j, int k, int metadata)
	{
    	try
        {
            //------
            //This section makes these three calls to initialise the TileEntity:
            //	Pipe pipe = BlockGenericPipe.createPipe(Item);
            //  tilePipe.initialize(pipe);
            //	and optionally: tilePipe.sendUpdateToClient();

            Item pipeItem = pipeItemsBC[metadata-7];
            Class<?> clazzBlockPipe = CompatibilityManager.classBCBlockGenericPipe;
            TileEntity tilePipe = world.getTileEntity(i, j, k);
            Class<?> clazzTilePipe = tilePipe.getClass();

            if (CompatibilityManager.methodBCBlockPipe_createPipe != null)
            {
                Object pipe = CompatibilityManager.methodBCBlockPipe_createPipe.invoke(null, pipeItem);
                Method initializePipe = null;
                for (Method m : clazzTilePipe.getMethods())
                {
                    if (m.getName().equals("initialize") && m.getParameterTypes().length == 1)
                    {
                        initializePipe = m;
                        break;
                    }
                }
                if (initializePipe != null)
                {
                    initializePipe.invoke(tilePipe, pipe);

                    //Legacy compatibility: TileGenericPipe.sendUpdateToClient() is not in recent BC versions
                    Method m = null;
                    try
                    {
                    	m = clazzTilePipe.getMethod("sendUpdateToClient");                  
                    }
                    catch (Exception e) { }
                    if (m != null) m.invoke(tilePipe);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
}
