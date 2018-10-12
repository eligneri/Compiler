import java.util.Hashtable;
import java.util.Enumeration;

public class SymbolTable {
    private Hashtable<String, SymbolTableEntry> table;

    public SymbolTable(int i){
        table = new Hashtable(i);
    }

    public SymbolTableEntry lookup(SymbolTableEntry e){
        return table.get(e.name);
    }

    public void insert(SymbolTableEntry e){
        table.put(e.name, e);
    }

    public int size(){
        return table.size();
    }

    public void dumpTable() {
        Enumeration<SymbolTableEntry> e = table.elements();
        while(e.hasMoreElements()){
            System.out.println(e.nextElement());
        }
    }
}
