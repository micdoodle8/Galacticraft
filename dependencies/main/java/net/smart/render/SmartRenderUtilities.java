package net.smart.render;

public abstract class SmartRenderUtilities
{
	public static final float Whole = (float)Math.PI * 2F;
	public static final float Half = (float)Math.PI;
	public static final float Quarter = Half / 2F;
	public static final float Eighth = Quarter / 2F;
	public static final float Sixteenth = Eighth / 2F;
	public static final float Thirtytwoth = Sixteenth / 2F;
	public static final float Sixtyfourth = Thirtytwoth / 2F;

	public static final float RadiantToAngle = 360F / Whole;

	public static float getHorizontalCollisionangle(boolean isCollidedPositiveX, boolean isCollidedNegativeX, boolean isCollidedPositiveZ, boolean isCollidedNegativeZ)
	{
		if(isCollidedPositiveX)
			if(isCollidedNegativeX)
				if(isCollidedPositiveZ)
					if(isCollidedNegativeZ)
						;
					else
						return 90F;
				else
					if(isCollidedNegativeZ)
						return 270F;
					else
						;
			else
				if(isCollidedPositiveZ)
					if(isCollidedNegativeZ)
						return 0F;
					else
						return 45F;
				else
					if(isCollidedNegativeZ)
						return 315F;
					else
						return 0F;
		else
			if(isCollidedNegativeX)
				if(isCollidedPositiveZ)
					if(isCollidedNegativeZ)
						return 180F;
					else
						return 135F;
				else
					if(isCollidedNegativeZ)
						return 225F;
					else
						return 180F;
			else
				if(isCollidedPositiveZ)
					if(isCollidedNegativeZ)
						;
					else
						return 90F;
				else
					if(isCollidedNegativeZ)
						return 270F;
					else
						;
	
		return Float.NaN;
	}

	public static float getAngle(double x, double y)
	{
		if(x == 0)
		{
			if(y == 0)
				return Float.NaN;
			if(y < 0)
				return 270;
			return 90;
		}

		if(y == 0)
		{
			if(x < 0)
				return 180;
			return 0;
		}

		float angle = (float)Math.atan(y / x) * RadiantToAngle;
		if(x < 0)
			return 180F + angle;
		if(y < 0 && x > 0)
			return 360F + angle;
		return angle;
	}
}