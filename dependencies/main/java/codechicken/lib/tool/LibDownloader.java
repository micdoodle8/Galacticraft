package codechicken.lib.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class LibDownloader {
    private static String[] libs = new String[] { "org/ow2/asm/asm-debug-all/5.0.3/asm-debug-all-5.0.3.jar", "com/google/guava/guava/14.0/guava-14.0.jar", "net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar", "org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar", "org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar" };
    private static File libDir = new File("lib");

    private static ByteBuffer downloadBuffer = ByteBuffer.allocateDirect(1 << 23);

    public static void load() {
        if (!libDir.exists()) {
            libDir.mkdir();
        }
        if (!libDir.isDirectory()) {
            throw new RuntimeException("/lib is not a directory");
        }

        List<String> missing = checkExists();
        for (String lib : missing) {
            download(lib);
        }
        addPaths(libs);
    }

    private static void addPaths(String[] libs) {
        try {
            URLClassLoader cl = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method m_addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            m_addURL.setAccessible(true);
            for (String lib : libs) {
                m_addURL.invoke(cl, new File(libDir, fileName(lib)).toURI().toURL());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to add libraries to classpath", e);
        }
    }

    private static void download(String lib) {
        File libFile = new File(libDir, fileName(lib));
        try {
            URL libDownload = new URL("http://repo1.maven.org/maven2/" + lib);
            URLConnection connection = libDownload.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "CodeChickenLib Downloader");
            int sizeGuess = connection.getContentLength();
            download(connection.getInputStream(), sizeGuess, libFile);
        } catch (Exception e) {
            libFile.delete();
            throw new RuntimeException("A download error occured", e);
        }
    }

    private static void download(InputStream is, int sizeGuess, File target) throws Exception {
        String name = target.getName();
        if (sizeGuess > downloadBuffer.capacity()) {
            throw new Exception(String.format("The file %s is too large to be downloaded", name));
        }

        downloadBuffer.clear();

        int bytesRead, fullLength = 0;

        System.out.format("Downloading lib %s", name);
        byte[] smallBuffer = new byte[1024];
        while ((bytesRead = is.read(smallBuffer)) >= 0) {
            downloadBuffer.put(smallBuffer, 0, bytesRead);
            fullLength += bytesRead;
            System.out.format("\rDownloading lib %s %d%%", name, (int) (fullLength * 100 / sizeGuess));
        }
        System.out.format("\rDownloaded lib %s         \n", name);
        is.close();
        downloadBuffer.limit(fullLength);

        if (!target.exists()) {
            target.createNewFile();
        }

        downloadBuffer.position(0);
        FileOutputStream fos = new FileOutputStream(target);
        fos.getChannel().write(downloadBuffer);
        fos.close();
    }

    private static String fileName(String lib) {
        return lib.replaceAll(".+/", "");
    }

    private static List<String> checkExists() {
        LinkedList<String> list = new LinkedList<String>();
        for (String lib : libs) {
            File file = new File(libDir, fileName(lib));
            if (!file.exists()) {
                list.add(lib);
            }
        }
        return list;
    }

}
