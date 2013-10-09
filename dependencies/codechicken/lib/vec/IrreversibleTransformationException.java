package codechicken.lib.vec;

@SuppressWarnings("serial")
public class IrreversibleTransformationException extends RuntimeException
{
    public Transformation t;
    
    public IrreversibleTransformationException(Transformation t)
    {
        this.t = t;
    }
    
    @Override
    public String getMessage()
    {
        return "The following transformation is irreversible:\n"+t;
    }
}
