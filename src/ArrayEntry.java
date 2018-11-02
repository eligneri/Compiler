public class ArrayEntry extends SymbolTableEntry {
    //protected int address;
    //protected String type;
    protected int upperBound;
    protected int lowerBound;

    public ArrayEntry(String n, int a, String t, int u, int l){
        name = n;
        address = a;
        type = t;
        upperBound = u;
        lowerBound = l;
        array = true;
    }

    public ArrayEntry(String n){
        name = n;
        array = true;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public String toString(){
        return "ArrayEntry: name=" + name + " type=" + type + " upperbound=" + upperBound + " lowerbound=" + lowerBound
                + " address=" + address;
    }
}
