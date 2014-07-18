package codechicken.nei.config;

public class OptionToggleButton extends OptionButton
{
    public final boolean prefixed;

    public OptionToggleButton(String name, boolean prefixed) {
        super(name);
        this.prefixed = prefixed;
    }

    public OptionToggleButton(String name) {
        this(name, false);
    }

    public boolean state() {
        return renderTag().getBooleanValue();
    }

    public String getButtonText() {
        return translateN(name + (state() ? ".true" : ".false"));
    }

    @Override
    public String getPrefix() {
        return prefixed ? translateN(name) : null;
    }

    @Override
    public boolean onClick(int button) {
        if (defaulting())
            return false;

        getTag().setBooleanValue(!state());
        return true;
    }
}
