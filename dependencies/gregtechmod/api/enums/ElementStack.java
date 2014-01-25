package gregtechmod.api.enums;

public class ElementStack implements Cloneable {
	public int mAmount;
	public Element mElement;
	
	public ElementStack(Element aElement, int aAmount) {
		mElement = aElement==null?Element._NULL:aElement;
		mAmount = aAmount;
	}
	
	public ElementStack copy(int aAmount) {
		return new ElementStack(mElement, aAmount);
	}
	
	@Override
	public ElementStack clone() {
		return new ElementStack(mElement, mAmount);
	}
	
	@Override
	public boolean equals(Object aObject) {
		if (aObject == this) return true;
		if (aObject == null) return false;
		if (aObject instanceof Element) return aObject == mElement;
		if (aObject instanceof ElementStack) return ((ElementStack)aObject).mElement == mElement && (mAmount < 0 || ((ElementStack)aObject).mAmount < 0 || ((ElementStack)aObject).mAmount == mAmount);
		return false;
	}
	
	@Override
	public String toString() {
		return mElement.toString()+mAmount;
	}

	@Override
	public int hashCode() {
		return mElement.hashCode();
	}
}