import javax.lang.model.element.VariableElement;
import java.util.LinkedList;
import java.util.List;

public class FunctionEntry extends SymbolTableEntry {
    protected int numberOfParameters;
    protected List parameterInfo;
    protected VariableEntry result;

    public FunctionEntry(String n, int p, List l, VariableEntry r){
        name = n;
        numberOfParameters = p;
        parameterInfo = l;
        result = r;
        function = true;
    }

    public FunctionEntry(String n, VariableEntry r){
        name = n;
        result = r;
        function = true;
        parameterInfo = new LinkedList();
    }

    public void setType(String s){
        type = s;
    }

    public void setResultType(String s){
        result.setType(s);
    }

    public void setNumberOfParameters(int i){
        numberOfParameters = i;
    }

    public Integer getNumberOfParameters(){return numberOfParameters;}

    public List getParameterInfo(){return parameterInfo;}

    public void addParameter(SymbolTableEntry e){
        parameterInfo.add(e);
    }

    public VariableEntry getResult(){
        return result;
    }

    public String toString(){
        return "FunctionEntry: name=" + name + " numberofparameters=" + numberOfParameters + " parameterinfo=" +
                parameterInfo + " result=" + result;
    }
}
