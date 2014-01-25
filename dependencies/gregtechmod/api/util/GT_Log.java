package gregtechmod.api.util;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * Just a simple Logging Function. If on Server, then this will point to System.out and System.err
 */
public class GT_Log {
	public static PrintStream out = System.out;
	public static PrintStream err = System.err;
	public static PrintStream ore = new LogBuffer();
    public static File mLogFile;
    public static File mOreDictLogFile;
    
    public static class LogBuffer extends PrintStream {
        public final List<String> mBufferedOreDictLog = new ArrayList<String>();
        
		public LogBuffer() {
			super(new OutputStream() {@Override public void write(int arg0) {/*Do nothing*/}});
		}
		
		@Override
		public void println(String aString) {
			mBufferedOreDictLog.add(aString);
		}
    }
}