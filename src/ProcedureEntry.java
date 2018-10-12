import java.util.List;

public class ProcedureEntry extends SymbolTableEntry {
    protected int numberofParameters;
    protected List parameterInfo;

    public ProcedureEntry(String n, int p, List l){
        name = n;
        numberofParameters =p;
        parameterInfo = l;
    }

    public ProcedureEntry(String n, int p){
        name = n;
        numberofParameters = p;
    }

    @Override
    public boolean isProcedure() {
        return true;
    }
}
