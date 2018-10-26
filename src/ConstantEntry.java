public class ConstantEntry extends SymbolTableEntry{
    protected String type;

    public ConstantEntry(String n, String t){
        name = n;
        type = t;
    }

    public ConstantEntry (String n){
        name = n;
    }

    public String toString(){
        return "ConstantEntry: name=" + name + " type=" + type;
    }
}
