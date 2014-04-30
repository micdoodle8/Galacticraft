package codechicken.lib.vec;

@SuppressWarnings("serial")
public class IrreversibleTransformationException extends RuntimeException
{
    public ITransformation t;
    
    public IrreversibleTransformationException(ITransformation t)
    {
        this.t = t;
    }
    
    @Override
    public String getMessage()
    {
        return "The following transformation is irreversible:\n"+t;
    }
}
