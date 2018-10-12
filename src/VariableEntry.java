public class VariableEntry extends SymbolTableEntry {
    protected int address;
    protected String type;

    public VariableEntry (String n, int a, String t){
        name = n;
        address = a;
        type = t;
    }

    public VariableEntry(String n){
        name = n;
    }

    @Override
    public boolean isVariable() {
        return true;
    }
}
