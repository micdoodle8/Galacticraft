package codechicken.lib.vec;

/**
 * Abstract supertype for any VectorN transformation
 * @param <Vector> The vector type
 * @param <Transformation> The transformation type
 */
public abstract class ITransformation<Vector, Transformation extends ITransformation>
{
    /**
     * Applies this transformation to vec
     */
    public abstract void apply(Vector vec);

    /**
     * @param point The point to apply this transformation around
     * @return Wraps this transformation in a translation to point and then back from point
     */
    public abstract Transformation at(Vector point);

    /**
     * Creates a TransformationList composed of this transformation followed by t
     * If this is a TransformationList, the transformation will be appended and this returned
     */
    public abstract Transformation with(Transformation t);

    /**
     * Returns a simplified transformation that performs this, followed by next. If such a transformation does not exist, returns null
     */
    public Transformation merge(Transformation next) {
        return null;
    }

    /**
     * Returns true if this transformation is redundant, eg. Scale(1, 1, 1), Translation(0, 0, 0) or Rotation(0, a, b, c)
     */
    public boolean isRedundant() {
        return false;
    }

    public abstract Transformation inverse();

    /**
     * Scala ++ operator
     */
    public Transformation $plus$plus(Transformation t) {
        return with(t);
    }
}
