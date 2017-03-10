package codechicken.lib.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Level;

/**
 * Created by covers1624 on 3/27/2016.
 */
public class BlockMultiTile extends Block implements ITileEntityProvider {

    @SuppressWarnings ("unchecked")
    private Class<? extends TileEntity>[] tileEntityMap = new Class[16];

    public BlockMultiTile(Material material) {
        super(material);
    }

    public void registerSubItemAndTile(int meta, String name, Class<? extends TileEntity> clazz) {
        GameRegistry.registerTileEntity(clazz, Loader.instance().activeModContainer().getModId() + ":" + name);
        addSubItemAndTile(meta, name, clazz);
    }

    public void addSubItemAndTile(int meta, String name, Class<? extends TileEntity> clazz) {
        addTileEntityMapping(meta, clazz);
        setItemName(meta, name);
    }

    public void setItemName(int meta, String name) {
        Item item = Item.getItemFromBlock(this);
        ((ItemBlockMultiType) item).registerSubItem(meta, name);
    }

    public void addTileEntityMapping(int meta, Class<? extends TileEntity> clazz) {
        this.tileEntityMap[meta] = clazz;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        Class<? extends TileEntity> clazz = tileEntityMap[meta];
        try {
            return clazz.getDeclaredConstructor(new Class[0]).newInstance();
        } catch (Exception e) {
            FMLLog.log("CodeChicken Lib", Level.FATAL, "Unable to create tile with meta of %s", meta);
            return null;
        }
    }
}
