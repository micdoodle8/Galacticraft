package codechicken.nei.config;

import java.util.LinkedList;

public abstract class ArrayDumper<T> extends DataDumper
{
    public ArrayDumper(String name) {
        super(name);
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        T[] array = array();
        for (int i = 0; i < array.length; i++) {
            T obj = array[i];
            if (obj == null) {
                if (mode == 1 || mode == 2)
                    list.add(new String[]{Integer.toString(i), null, null, null, null});
            } else {
                if (mode == 0 || mode == 2)
                    list.add(dump(obj, i));
            }
        }
        return list;
    }

    public abstract T[] array();

    public abstract String[] dump(T obj, int id);
}