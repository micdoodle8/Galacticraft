package codechicken.lib.block.property.unlisted;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by covers1624 on 28/02/2017.
 */
public class UnlistedTileEntityProperty extends UnlistedPropertyBase<TileEntity> {

	public UnlistedTileEntityProperty(String name) {
		super(name);
	}

	@Override
	public Class<TileEntity> getType() {
		return TileEntity.class;
	}

	@Override
	public String valueToString(TileEntity value) {
		return value.toString();
	}
}
