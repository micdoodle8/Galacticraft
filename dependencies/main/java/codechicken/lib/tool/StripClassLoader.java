package codechicken.lib.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class StripClassLoader extends URLClassLoader
{
    public StripClassLoader() {
        super(new URL[0], StripClassLoader.class.getClassLoader());
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if(!name.startsWith("codechicken.lib"))
            return super.loadClass(name);

        try {
            String resName = name.replace('.', '/')+".class";
            InputStream res = getResourceAsStream(resName);
            if(res == null)
                throw new ClassNotFoundException("Could not find resource: "+resName);
            byte[] bytes = readFully(res);
            bytes = transform(bytes);
            return defineClass(name, bytes, 0, bytes.length);

        } catch(IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    public static byte[] readFully(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int read;
        byte[] data = new byte[16384];
        while ((read = is.read(data, 0, data.length)) > 0)
            buffer.write(data, 0, read);

        return buffer.toByteArray();
    }

    private byte[] transform(byte[] bytes) {

        bytes = MCStripTransformer.transform(bytes);
        return bytes;
    }
}
