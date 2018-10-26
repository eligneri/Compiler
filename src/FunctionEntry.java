import javax.lang.model.element.VariableElement;
import java.util.List;

public class FunctionEntry extends SymbolTableEntry {
    protected int numberOfParameters;
    protected List parameterInfo;
    protected VariableElement result;

    public FunctionEntry(String n, int p, List l, VariableElement r){
        name = n;
        numberOfParameters = p;
        parameterInfo = l;
        result = r;
        function = true;
    }

    public FunctionEntry(String n){
        name = n;
        function = true;
    }

    public String toString(){
        return "FunctionEntry: name=" + name + " numberofparameters=" + numberOfParameters + " parameterinfo=" +
                parameterInfo + " result=" + result;
    }
}
