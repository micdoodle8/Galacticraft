package gregtechmod.api.enums;

public class MaterialStack implements Cloneable {
	public long mAmount;
	public Materials mMaterial;

	public MaterialStack(Materials aMaterial, long aAmount) {
		mMaterial = aMaterial==null?Materials._NULL:aMaterial;
		mAmount = aAmount;
	}
	
	public MaterialStack copy(long aAmount) {
		return new MaterialStack(mMaterial, aAmount);
	}
	
	@Override
	public MaterialStack clone() {
		return new MaterialStack(mMaterial, mAmount);
	}
	
	@Override
	public boolean equals(Object aObject) {
		if (aObject == this) return true;
		if (aObject == null) return false;
		if (aObject instanceof Materials) return aObject == mMaterial;
		if (aObject instanceof MaterialStack) return ((MaterialStack)aObject).mMaterial == mMaterial && (mAmount < 0 || ((MaterialStack)aObject).mAmount < 0 || ((MaterialStack)aObject).mAmount == mAmount);
		return false;
	}
	
	@Override
	public String toString() {
		return (mMaterial.mMaterialList.size() > 1 && mAmount > 1 ? "(" : "") + mMaterial.getToolTip() + (mMaterial.mMaterialList.size() > 1 && mAmount > 1 ? ")" : "") + (mAmount > 1 ? mAmount : "");
	}

	@Override
	public int hashCode() {
		return mMaterial.hashCode();
	}
}