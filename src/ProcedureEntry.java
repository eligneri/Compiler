import java.util.LinkedList;
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
        parameterInfo = new LinkedList();
    }

    public ProcedureEntry(String n){
        name = n;
        parameterInfo = new LinkedList();
        procedure = true;
    }

    public boolean isProcedure(){return true;}



    public void addParameter(SymbolTableEntry e){
        parameterInfo.add(e);
    }

    public void setNumberOfParameters(int i){
        numberofParameters = i;
    }


    public Integer getNumberOfParameters(){return numberofParameters;}

    public String toString(){
        return "ProcedureEntry: name=" + name + " numberofparameters=" + numberofParameters + " parameterinfo=" +
                parameterInfo;
    }
}
