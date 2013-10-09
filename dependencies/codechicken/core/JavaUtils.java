package codechicken.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.google.common.base.Function;

public class JavaUtils
{
    public static void processLines(File file, Function<String, Void> function)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null)
                function.apply(line);
            reader.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
