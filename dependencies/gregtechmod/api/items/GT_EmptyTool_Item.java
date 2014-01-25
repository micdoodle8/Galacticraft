package gregtechmod.api.items;

import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GT_EmptyTool_Item extends GT_Tool_Item {
	public GT_EmptyTool_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aChargedGTID) {
		super(aID, aUnlocalized, aEnglish, "Empty. You need to recharge it.", aMaxDamage, 0, false, aChargedGTID, -1);
		try {
			Class.forName("codechicken.nei.api.API");
			codechicken.nei.api.API.hideItem(itemID);
		} catch(Throwable e) {/*Do nothing*/}
	}
	
	@Override
    public boolean hasContainerItem() {
        return false;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister aIconRegister) {
		//
    }
}