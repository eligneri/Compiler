/*
 * This class is for ArrayEntries for Symbol Tables
 */
public class ArrayEntry extends SymbolTableEntry
{
    protected int upperBound;
    protected int lowerBound;

    public ArrayEntry(String n, int a, String t, int u, int l)
    {
        Name = n;
        Address = a;
        Type = t;
        upperBound = u;
        lowerBound = l;
        Array = true;
    }

    public ArrayEntry(String n)
    {
        Name = n;
        Array = true;
    }

    /*
     * GETTERS AND SETTERS
     */
    public void SetAddress(int address)
    {
        this.Address = address;
    }

    public void SetType(String type)
    {
        this.Type = type;
    }

    public void SetUpperBound(int upperBound)
    {
        this.upperBound = upperBound;
    }

    public void SetLowerBound(int lowerBound)
    {
        this.lowerBound = lowerBound;
    }

    public Integer GetUpperBound()
    {
        return upperBound;
    }

    public Integer GetLowerBound()
    {
        return lowerBound;
    }

    public String toString()
    {
        return "ArrayEntry: name=" + Name + " type=" + Type + " upperbound=" + upperBound + " lowerbound=" + lowerBound
                + " address=" + Address;
    }
}
