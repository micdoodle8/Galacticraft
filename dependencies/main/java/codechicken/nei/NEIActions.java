package codechicken.nei;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class NEIActions
{
    public static final int protocol = 0;
    
    public static HashMap<String, NEIActions> nameActionMap = new HashMap<String, NEIActions>();
    public static HashSet<String> canDisable = new HashSet<String>();

    public static void addAction(String name, String base, boolean smpreq)
    {
        NEIActions action = new NEIActions(name, base, smpreq);
        nameActionMap.put(name, action);
    }
    
    private static void addAction(String name, String base)
    {
        addAction(name, base, false);
    }

    public static void addAction(String name)
    {
        addAction(name, name);
    }

    private static void addAction(String name, boolean smpreq)
    {
        addAction(name, name, smpreq);
    }
    
    public static String base(String name)
    {
        NEIActions action = nameActionMap.get(name);
        return action == null ? name : action.base;
    }

    public static boolean smpRequired(String name)
    {
        return nameActionMap.get(name).smpreq;
    }
    
    public static void init()
    {
        addAction("time");
        addAction("dawn", "time");
        addAction("noon", "time");
        addAction("dusk", "time");
        addAction("midnight", "time");
        addAction("creative");
        addAction("creative+", true);
        addAction("adventure", "creative");
        addAction("rain");
        addAction("item");
        addAction("heal");
        addAction("delete", true);
        addAction("magnet", true);
        addAction("enchant", true);
        addAction("potion", true);
        addAction("itemnbt", "item", true);

        canDisable.add("dawn");
        canDisable.add("noon");
        canDisable.add("dusk");
        canDisable.add("midnight");
        canDisable.add("rain");
    }
    
    public static final String[] timeZones = new String[]{"dawn", "noon", "dusk", "midnight"};
    public static final String[] gameModes = new String[]{"survival", "creative", "creative+", "adventure"};

    public String name;
    public String base;
    public boolean smpreq;
    
    public NEIActions(String name, String base, boolean smpreq)
    {
        this.name = name;
        this.base = base;
        this.smpreq = smpreq;
    }

    public static List<String> baseActions()
    {
        List<String> list = new LinkedList<String>();
        for(NEIActions a : nameActionMap.values())
            if(a.base.equals(a.name))
                list.add(a.name);
        return list;
    }
}
