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
    }

    public FunctionEntry(String n){
        name = n;
    }

    @Override
    public boolean isFunction() {
        return true;
    }
}
