package codechicken.nei.config;

public class OptionIntegerField extends OptionTextField
{
    public int min;
    public int max;
    public OptionIntegerField(String name, int min, int max) {
        super(name);
        this.min = min;
        this.max = max;
    }

    public OptionIntegerField(String name) {
        this(name, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public boolean isValidInput(String s) {
        if(s.length() == 0)
            return true;

        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    public boolean isValidValue(String s) {
        if(s.length() == 0 || !isValidInput(s))
            return false;

        int i = Integer.parseInt(s);
        return i >= min && i <= max;
    }
}
