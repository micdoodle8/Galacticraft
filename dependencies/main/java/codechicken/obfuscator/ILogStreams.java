package codechicken.obfuscator;

import java.io.PrintStream;

public interface ILogStreams {
    PrintStream err();

    PrintStream out();
}
