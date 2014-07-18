package codechicken.nei;

public class ItemQuantityField extends TextField
{
    public ItemQuantityField(String ident)
    {
        super(ident);
        centered = true;
    }
    
    @Override
    public boolean isValid(String string)
    {
        if(string.equals(""))
            return true;
        
        try
        {
            return Integer.parseInt(string) >= 0;
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
    }
    
    public int intValue()
    {
        return intValue(text());
    }
    
    public int intValue(String s)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch(NumberFormatException nfe)
        {
            return 0;
        }
    }
    
    @Override
    public void loseFocus()
    {
        setText(Integer.toString(NEIClientConfig.getItemQuantity()));
    }

    @Override
    public void onTextChange(String oldText)
    {
        if(intValue(oldText) != intValue())//hacky recursion stopper
            NEIClientUtils.setItemQuantity(intValue());
    }
}
