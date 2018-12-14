import java.util.LinkedList;
import java.util.List;

/*
 * This class is for ProcedureEntries for Symbol Tables
 */
public class ProcedureEntry extends SymbolTableEntry
{
    protected int NumberOfParameters;
    protected List ParameterInfo;


    public ProcedureEntry(String n, int p)
    {
        Name = n;
        NumberOfParameters = p;
        Procedure = true;
        ParameterInfo = new LinkedList();
    }

    public ProcedureEntry(String n)
    {
        Name = n;
        ParameterInfo = new LinkedList();
        Procedure = true;
    }

    public boolean IsProcedure()
    {
        return true;
    }

    //Adds a parameter to parameter info
    public void AddParameter(SymbolTableEntry e)
    {
        ParameterInfo.add(e);
    }

    /*
     * Getters and Setters
     */
    public void SetNumberOfParameters(int i)
    {
        NumberOfParameters = i;
    }


    public Integer GetNumberOfParameters()
    {
        return NumberOfParameters;
    }

    public String toString()
    {
        return "ProcedureEntry: name=" + Name + " numberofparameters=" + NumberOfParameters + " parameterinfo=" +
                ParameterInfo;
    }
}
