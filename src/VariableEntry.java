public class VariableEntry extends SymbolTableEntry {
    //protected int address;
    //protected String type;

    public VariableEntry (String n, int a, String t){
        name = n;
        address = a;
        type = t;
        variable = true;
    }

    public VariableEntry(String n){
        name = n;
        variable = true;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setResult(){functionResult = true;}

    public String toString(){
        return "VariableEntry: name=" + name + " type=" + type + " address=" + address;
    }
}
