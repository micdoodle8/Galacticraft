package codechicken.lib.config;

import java.io.*;

public class ConfigFile extends ConfigTagParent
{
    public static final byte[] crlf = new byte[]{0xD, 0xA};

    public File file;
    private boolean loading;

    public ConfigFile(File file) {
        newlinemode = 2;
        load(file);
    }

    protected ConfigFile() {}

    protected void load(File file) {
        try {
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.file = file;
        loadConfig();
    }

    protected void loadConfig() {
        loading = true;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));

            while (true) {
                reader.mark(2000);
                String line = reader.readLine();
                if (line != null && line.startsWith("#")) {
                    if (comment == null || comment.equals(""))
                        comment = line.substring(1);
                    else
                        comment = comment + "\n" + line.substring(1);
                } else {
                    reader.reset();
                    break;
                }
            }
            loadChildren(reader);
            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loading = false;
    }

    @Override
    public ConfigFile setComment(String header) {
        super.setComment(header);
        return this;
    }

    @Override
    public ConfigFile setSortMode(int mode) {
        super.setSortMode(mode);
        return this;
    }

    @Override
    public String getNameQualifier() {
        return "";
    }

    public static String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        return line == null ? null : line.replace("\t", "");
    }

    public static void writeLine(PrintWriter writer, String line, int tabs) {
        for (int i = 0; i < tabs; i++)
            writer.print('\t');

        writer.println(line);
    }

    public void saveConfig() {
        if (loading)
            return;

        PrintWriter writer;
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        writeComment(writer, 0);
        ConfigFile.writeLine(writer, "", 0);
        saveTagTree(writer, 0, "");
        writer.flush();
        writer.close();
    }

    public boolean isLoading() {
        return loading;
    }
}
