package codechicken.nei.config;

import net.minecraft.util.registry.RegistryNamespaced;

import java.util.LinkedList;

@Deprecated// Use ForgeRegistryDumper.
public abstract class RegistryDumper<T> extends DataDumper {
    public RegistryDumper(String name) {
        super(name);
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        RegistryNamespaced registry = registry();

        for (T obj : (Iterable<T>) registry) {
            list.add(dump(obj, registry.getIDForObject(obj), registry.getNameForObject(obj).toString()));
        }

        return list;
    }

    public abstract RegistryNamespaced registry();

    public abstract String[] dump(T obj, int id, String name);

    @Override
    public int modeCount() {
        return 1;
    }
}
