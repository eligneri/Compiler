import java.util.List;

public class ProcedureEntry extends SymbolTableEntry {
    protected int numberofParameters;
    protected List parameterInfo;

    public ProcedureEntry(String n, int p, List l){
        name = n;
        numberofParameters =p;
        parameterInfo = l;
        procedure = true;
    }

    public ProcedureEntry(String n, int p){
        name = n;
        numberofParameters = p;
        procedure = true;
    }

    public String toString(){
        return "ProcedureEntry: name=" + name + " numberofparameters=" + numberofParameters + " parameterinfo=" +
                parameterInfo;
    }
}
