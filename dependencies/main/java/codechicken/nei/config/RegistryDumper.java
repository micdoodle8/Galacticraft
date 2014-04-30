package codechicken.nei.config;

import net.minecraft.util.RegistryNamespaced;

import java.util.LinkedList;

public abstract class RegistryDumper <T> extends DataDumper
{
    public RegistryDumper(String name) {
        super(name);
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        RegistryNamespaced registry = registry();

        for(T obj : (Iterable<T>)registry)
            list.add(dump(obj, registry.getIDForObject(obj), registry.getNameForObject(obj)));

        return list;
    }

    public abstract RegistryNamespaced registry();
    public abstract String[] dump(T obj, int id, String name);

    @Override
    public int modeCount() {
        return 1;
    }
}
