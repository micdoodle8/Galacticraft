package codechicken.lib.render.uv;

import java.util.ArrayList;
import java.util.Iterator;

public class UVTransformationList extends UVTransformation
{
    private ArrayList<UVTransformation> transformations = new ArrayList<UVTransformation>();

    public UVTransformationList(UVTransformation... transforms)
    {
        for(UVTransformation t : transforms)
            if(t instanceof UVTransformationList)
                transformations.addAll(((UVTransformationList)t).transformations);
            else
                transformations.add(t);

        compact();
    }

    @Override
    public void apply(UV uv)
    {
        for(int i = 0; i < transformations.size(); i++)
            transformations.get(i).apply(uv);
    }

    @Override
    public UVTransformationList with(UVTransformation t)
    {
        if(t.isRedundant())
            return this;

        if(t instanceof UVTransformationList)
            transformations.addAll(((UVTransformationList)t).transformations);
        else
            transformations.add(t);

        compact();
        return this;
    }

    public UVTransformationList prepend(UVTransformation t)
    {
        if(t.isRedundant())
            return this;

        if(t instanceof UVTransformationList)
            transformations.addAll(0, ((UVTransformationList)t).transformations);
        else
            transformations.add(0, t);

        compact();
        return this;
    }

    private void compact() {
        ArrayList<UVTransformation> newList = new ArrayList<UVTransformation>(transformations.size());
        Iterator<UVTransformation> iterator = transformations.iterator();
        UVTransformation prev = null;
        while(iterator.hasNext()) {
            UVTransformation t = iterator.next();
            if(t.isRedundant())
                continue;

            if(prev != null) {
                UVTransformation m = prev.merge(t);
                if(m == null)
                    newList.add(prev);
                else if(m.isRedundant())
                    t = null;
                else
                    t = m;
            }
            prev = t;
        }
        if(prev != null)
            newList.add(prev);

        if(newList.size() < transformations.size())
            transformations = newList;
    }

    @Override
    public boolean isRedundant() {
        return transformations.size() == 0;
    }

    @Override
    public UVTransformation inverse()
    {
        UVTransformationList rev = new UVTransformationList();
        for(int i = transformations.size()-1; i >= 0; i--)
            rev.with(transformations.get(i).inverse());
        return rev;
    }

    @Override
    public String toString()
    {
        String s = "";
        for(UVTransformation t : transformations)
            s+="\n"+t.toString();
        return s.trim();
    }
}
