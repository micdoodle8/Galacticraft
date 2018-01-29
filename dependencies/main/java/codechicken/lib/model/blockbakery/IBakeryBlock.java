package codechicken.lib.model.blockbakery;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by covers1624 on 28/10/2016.
 * TODO Document.
 * TODO, Refactor, IBakeryBlock > IBakeryProvider
 * TODO, Item and block implementation support.
 */
public interface IBakeryBlock {

    @SideOnly (Side.CLIENT)
        //TODO ICustomBakery
    ICustomBlockBakery getCustomBakery();

}
