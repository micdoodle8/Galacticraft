package codechicken.lib.tool.module;

import codechicken.lib.tool.ToolMain;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.IOException;

public abstract class JOptModule implements ToolMain.Module
{
    OptionParser parser = new OptionParser();

    @Override
    public void main(String[] args) {
        OptionSet options;
        try
        {
            options = parser.parse(args);
        }
        catch(OptionException ex)
        {
            System.err.println(ex.getLocalizedMessage());
            System.exit(-1);
            return;
        }

        try
        {
            main(parser, options);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    protected abstract void main(OptionParser parser, OptionSet options);

    @Override
    public void printHelp() {
        try {
            parser.printHelpOn(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
