package codechicken.lib.block.property;

import com.google.common.base.Objects;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by covers1624 on 5/10/2016.
 * Used to store a TileEntity as an UnlistedProperty for the use of baking models, useful for anything to do with tile access dependent baking.
 */
public class TileEntityProperty implements IUnlistedProperty<TileEntity> {

    private String name;

    public TileEntityProperty(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(TileEntity value) {
        return true;
    }

    @Override
    public Class<TileEntity> getType() {
        return TileEntity.class;
    }

    @Override
    public String valueToString(TileEntity value) {
        Object worldID = value.hasWorldObj() ? value.getWorld().provider.getDimension() : "NoWorld!";
        return Objects.toStringHelper(value).add("position", value.getPos().toString()).add("worldID", worldID).toString();
    }
}
