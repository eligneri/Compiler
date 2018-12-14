/*
 * This class is for VariableEntries for Symbol Tables
 */
public class VariableEntry extends SymbolTableEntry {

    public VariableEntry (String n, int a, String t){
        Name = n;
        Address = a;
        Type = t;
        Variable = true;
    }

    public VariableEntry(String n){
        Name = n;
        Variable = true;
    }

    /*
     * Setters
     */
    public void SetAddress(int address) {
        this.Address = address;
    }

    public void SetType(String type) {
        this.Type = type;
    }

    public void SetResult(){
        FunctionResult = true;}

    public String toString(){
        return "VariableEntry: name=" + Name + " type=" + Type + " address=" + Address;
    }
}
