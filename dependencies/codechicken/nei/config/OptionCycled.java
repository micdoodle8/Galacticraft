package codechicken.nei.config;

public class OptionCycled extends OptionButton
{
    public final int count;
    public final boolean prefixed;
    
    public OptionCycled(String name, int count, boolean prefixed)
    {
        super(name);
        this.count = count;
        this.prefixed = prefixed;
    }
    
    public OptionCycled(String name, int count)
    {
        this(name, count, false);
    }
    
    public int value()
    {
        return renderTag().getIntValue();
    }

    @Override
    public String getButtonText()
    {
        return translateN(name+"."+value());
    }
    
    @Override
    public String getPrefix()
    {
        return prefixed ? translateN(name) : null;
    }
    
    @Override
    public boolean onClick(int button)
    {
        if(renderDefault())
            return false;
        
        return cycle();
    }
    
    public boolean cycle()
    {
        int next = value();
        do
        {
            next=(next+1)%count;
        }
        while(!optionValid(next));
        
        if(next == value())
            return false;
        
        getTag().setIntValue(next);
        return true;
    }
    
    public boolean optionValid(int index)
    {
        return true;
    }
}
