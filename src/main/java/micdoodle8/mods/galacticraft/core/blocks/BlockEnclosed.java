package micdoodle8.mods.galacticraft.core.blocks;

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

    public enum EnumEnclosedBlock
    {
        TE_CONDUIT(0, 2, null, "enclosed_te_conduit"),
        OXYGEN_PIPE(1, -1, null, "enclosed_oxygen_pipe"),
        IC2_COPPER_CABLE(2, 0, null, "enclosed_copper_cable"),
        IC2_GOLD_CABLE(3, 3, null, "enclosed_gold_cable"),
        IC2_HV_CABLE(4, 6, null, "enclosed_hv_cable"),
        IC2_GLASS_FIBRE_CABLE(5, 9, null, "enclosed_glassfibre_cable"),
        IC2_LV_CABLE(6, 10, null, "enclosed_lv_cable"),
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
        String pipeClass;
        String texture;

        EnumEnclosedBlock(int metadata, int subMeta, String pipeClass, String texture)
        {
            this.metadata = metadata;
            this.subMeta = subMeta;
            this.pipeClass = pipeClass;
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

        public String getPipeClass()
        {
            return this.pipeClass;
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
            // par3List.add(new ItemStack(par1, 1,
            // EnumEnclosedBlock.TE_CONDUIT.getMetadata()));
        }

        if (CompatibilityManager.isIc2Loaded())
        {
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_COPPER_CABLE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_GOLD_CABLE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_HV_CABLE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_GLASS_FIBRE_CABLE.getMetadata()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_LV_CABLE.getMetadata()));
        }

        if (CompatibilityManager.isBCraftLoaded())
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

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
        return par2 >= this.enclosedIcons.length ? this.blockIcon : this.enclosedIcons[par2];
    }

    @Override
    public int damageDropped(int meta)
    {
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
        super.onNeighborBlockChange(world, x, y, z, block);

        int metadata = world.getBlockMetadata(x, y, z);
        final TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (metadata == EnumEnclosedBlock.TE_CONDUIT.getMetadata())
        {

        }
        else if (metadata > 0 && metadata <= EnumEnclosedBlock.OXYGEN_PIPE.getMetadata())
        {
            if (tileEntity instanceof INetworkConnection)
            {
                ((INetworkConnection) tileEntity).refresh();
            }
        }
        else if (metadata <= EnumEnclosedBlock.IC2_LV_CABLE.getMetadata())
        {
            if (CompatibilityManager.isIc2Loaded() && tileEntity != null)
            {
                try
                {
                    Class<?> clazz = Class.forName("ic2.core.block.wiring.TileEntityCable");

                    if (clazz != null && clazz.isInstance(tileEntity))
                    {
                        try
                        {
                            Method method = clazz.getMethod("onNeighborBlockChange");
                            method.invoke(tileEntity);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (metadata <= EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata())
        {
            if (CompatibilityManager.isBCraftLoaded())
            {
                try
                {
                    Class<?> clazzPipe = Class.forName("buildcraft.transport.Pipe");
                    Class<?> clazzPipeTile = Class.forName("buildcraft.transport.TileGenericPipe");
                    Class<?> clazzPipeBlock = Class.forName("buildcraft.transport.BlockGenericPipe");

                    Object pipe = CompatibilityManager.methodBCBlockPipe_getPipe.invoke(null, world, x, y, z);
                    Method isValid = clazzPipeBlock.getMethod("isValid", clazzPipe);
                    Boolean valid = (Boolean) isValid.invoke(null, pipe);

                    if (valid)
                    {
                        Method schedule = clazzPipeTile.getMethod("scheduleNeighborChange");
                        Object container = clazzPipe.getField("container").get(pipe);
                        schedule.invoke(container);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (metadata <= EnumEnclosedBlock.ME_CABLE.getMetadata())
        {
            if (CompatibilityManager.isAppEngLoaded())
            {
                world.markBlockForUpdate(x, y, z);
            }
        }
        else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE.getMetadata())
        {
            if (tileEntity instanceof IConductor)
            {
                ((IConductor) tileEntity).refresh();
            }
        }
        else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE_HEAVY.getMetadata())
        {
            if (tileEntity instanceof IConductor)
            {
                ((IConductor) tileEntity).refresh();
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        TileEntity returnTile = null;
    	
    	if (metadata == EnumEnclosedBlock.TE_CONDUIT.getMetadata())
        {
    		//TODO
        }
        else if (metadata > 0 && metadata <= EnumEnclosedBlock.OXYGEN_PIPE.getMetadata())
        {
            returnTile = new TileEntityOxygenPipe();
        }
        else if (metadata <= EnumEnclosedBlock.IC2_LV_CABLE.getMetadata())
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

                    returnTile = (TileEntity) constructor.newInstance((short) BlockEnclosed.getTypeFromMeta(metadata).getSubMetaValue());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (metadata <= EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata())
        {
            if (CompatibilityManager.isBCraftLoaded())
            {
                try
                {
                    Class<?> clazzTilePipe = Class.forName("buildcraft.transport.TileGenericPipe");
                    Constructor<?>[] constructors = clazzTilePipe.getDeclaredConstructors();
                    Constructor<?> constructor = null;

                    for (Constructor<?> constructor2 : constructors)
                    {
                        constructor = constructor2;

                        if (constructor.getGenericParameterTypes().length == 0)
                        {
                            break;
                        }
                    }

                    constructor.setAccessible(true);

                    TileEntity tilePipe = (TileEntity) constructor.newInstance();

                    returnTile = tilePipe;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (metadata <= EnumEnclosedBlock.ME_CABLE.getMetadata())
        {
            if (CompatibilityManager.isAppEngLoaded())
            {
                try
                {
                    Class<?> clazz = Class.forName("appeng.me.tile.TileCable");
                    returnTile = (TileEntity) clazz.newInstance();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE.getMetadata())
        {
            returnTile = new TileEntityAluminumWire(1);
        }
        else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE_HEAVY.getMetadata())
        {
            returnTile = new TileEntityAluminumWire(2);
        }
   	
        return returnTile;
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
}
