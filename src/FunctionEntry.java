import java.util.LinkedList;
import java.util.List;

/*
 * This class is for FunctionEntries for Symbol Tables
 */
public class FunctionEntry extends SymbolTableEntry
{
    protected int numberOfParameters;
    protected List parameterInfo;
    protected VariableEntry result;

    public FunctionEntry(String n, VariableEntry r)
    {
        Name = n;
        result = r;
        Function = true;
        parameterInfo = new LinkedList();
    }

    //Adds a parameter to the parameter list
    public void AddParameter(SymbolTableEntry e)
    {
        parameterInfo.add(e);
    }

    /*
     * Getters and Setters
     */
    public void SetType(String s)
    {
        Type = s;
    }

    public void setResultType(String s)
    {
        result.SetType(s);
    }

    public void SetNumberOfParameters(int i)
    {
        numberOfParameters = i;
    }

    public Integer GetNumberOfParameters()
    {
        return numberOfParameters;
    }

    public List GetParameterInfo()
    {
        return parameterInfo;
    }

    public VariableEntry GetResult()
    {
        return result;
    }

    public String toString()
    {
        return "FunctionEntry: name=" + Name + " numberofparameters=" + numberOfParameters + " parameterinfo=" +
                parameterInfo + " result=" + result;
    }
}
