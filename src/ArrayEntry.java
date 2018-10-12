public class ArrayEntry extends SymbolTableEntry {
    protected int address;
    protected String type;
    protected int upperBound;
    protected int lowerBound;

    public ArrayEntry(String n, int a, String t, int u, int l){
        name = n;
        address = a;
        type = t;
        upperBound = u;
        lowerBound = l;
    }

    public ArrayEntry(String n){
        name = n;
    }

    @Override
    public boolean isArray() {
        return true;
    }
}
