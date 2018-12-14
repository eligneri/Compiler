/*
 * This class is for ConstantEntries for Symbol Tables
 */
public class ConstantEntry extends SymbolTableEntry
{

    public ConstantEntry(String n, String t)
    {
        Name = n;
        Type = t;
    }

    public String toString()
    {
        return "ConstantEntry: name=" + Name + " type=" + Type;
    }
}
