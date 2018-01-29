package codechicken.lib.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.IStringSerializable;

/**
 * Created by covers1624 on 20/11/2016.
 */
public interface IType extends IStringSerializable {

    IProperty<?> getTypeProperty();

    int meta();

}
