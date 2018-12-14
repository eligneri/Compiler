import java.util.Hashtable;
import java.util.Enumeration;

/*
 * This class creates a symbol table based on a Hashtable
 */
public class SymbolTable
{
    private Hashtable<String, SymbolTableEntry> Table;

    public SymbolTable(int i)
    {
        Table = new Hashtable(i);
    }

    //lookup a value in the hashtable
    public SymbolTableEntry Lookup(String e)
    {
        return Table.get(e);
    }

    //put a value in the hashtable
    public void Insert(SymbolTableEntry e)
    {
        Table.put(e.Name, e);
    }

    public int Size()
    {
        return Table.size();
    }

    public void DumpTable()
    {
        Enumeration<SymbolTableEntry> e = Table.elements();
        while (e.hasMoreElements())
        {
            System.out.println(e.nextElement());
        }
    }
}
