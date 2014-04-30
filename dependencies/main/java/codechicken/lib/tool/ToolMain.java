package codechicken.lib.tool;

import codechicken.lib.tool.module.ModuleQBConverter;

public class ToolMain
{
    public static interface Module {
        public void main(String[] args);
        public String name();
        public void printHelp();
    }

    public static Module[] modules = new Module[]{
            new ModuleQBConverter()
    };

    private static void printHelp() {
        System.out.println("Usage: [module] [args]");
        System.out.println("  Modules: ");
        for(Module m : modules)
            System.out.println("   - "+m.name());
        System.out.println("-h [module] for module help");
    }

    public static void main(String[] args) {
        if(args.length > 0) {
            for(Module m : modules)
                if(args[0].equals(m.name())) {
                    String[] args2 = new String[args.length-1];
                    System.arraycopy(args, 1, args2, 0, args2.length);
                    m.main(args2);
                    return;
                }
            if(args[0].equals("-h") && args.length >= 2) {
                for(Module m : modules)
                    if(args[1].equals(m.name())) {
                        m.printHelp();
                        return;
                    }
            }
        }
        printHelp();
    }
}
