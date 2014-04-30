package codechicken.lib.tool.module;

import codechicken.lib.render.QBImporter;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;

import static java.util.Arrays.asList;

public class ModuleQBConverter extends JOptModule
{
    public ModuleQBConverter() {
        parser.acceptsAll(asList("?", "h", "help"), "Show the help");
        parser.acceptsAll(asList("i", "input"), "comma separated list of paths to models (.qb or directories)")
                .withRequiredArg().ofType(File.class).withValuesSeparatedBy(',').required();
        parser.acceptsAll(asList("o", "out"), "Output Directory")
                .withRequiredArg().ofType(File.class);
        parser.acceptsAll(asList("o2", "textureplanes"), "2nd level optimisation. Merges coplanar polygons. Increases texture size");
        parser.acceptsAll(asList("s", "squaretextures"), "Produce square textures");
        parser.acceptsAll(asList("t", "mergetextures"), "Use the same texture for all models");
        parser.acceptsAll(asList("r", "scalemc"), "Resize model to mc standard (shrink by factor of 16)");
    }

    protected void main(OptionParser parser, OptionSet options) {
        int flags = 0;
        if(options.has("o2")) flags |= QBImporter.TEXTUREPLANES;
        if(options.has("s")) flags |= QBImporter.SQUARETEXTURE;
        if(options.has("t")) flags |= QBImporter.MERGETEXTURES;
        if(options.has("r")) flags |= QBImporter.SCALEMC;

        File[] input = options.valuesOf("input").toArray(new File[0]);
        File[] outDir = new File[input.length];
        if(options.has("out")) {
            File output = (File)options.valueOf("out");
            if(output.isFile())
                throw new RuntimeException("Output Path is not a directory");
            if(!output.exists())
                output.mkdirs();

            for(int i = 0; i < input.length; i++)
                outDir[i] = output;
        } else {
            for(int i = 0; i < input.length; i++)
                outDir[i] = input[i].isDirectory() ? input[i] : input[i].getParentFile();
        }

        for(int i = 0; i < input.length; i++) {
            File file = input[i];
            if(file.isDirectory()) {
                for(File file2 : file.listFiles())
                    if(file2.getName().endsWith(".qb"))
                        convert(file2, outDir[i], flags);
            }
            else
                convert(file, outDir[i], flags);
        }
    }

    private void convert(File in, File outDir, int flags) {
        System.out.println("Converting: "+in.getName());
        QBImporter.RasterisedModel m = QBImporter.loadQB(in).toRasterisedModel(flags);
        m.export(new File(outDir, in.getName().replace(".qb", ".obj")), outDir);
    }

    @Override
    public String name() {
        return "QBConverter";
    }
}
