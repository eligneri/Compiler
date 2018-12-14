import java.util.List;

/*
 * This class defines an entry to a symbol table. Extended by the different kinds of entries
 */
public class SymbolTableEntry
{
    protected String Name;
    protected String Type;
    protected Integer Address;

    protected Boolean Variable = false;
    protected Boolean Procedure = false;
    protected Boolean Function = false;
    protected Boolean FunctionResult = false;
    protected Boolean Parameter = false;
    protected Boolean Array = false;
    protected Boolean Reserved = false;

    public boolean IsProcedure()
    {
        return Procedure;
    }

    /*
     * Getters and Setters
     */
    public boolean GetVariable()
    {
        return Variable;
    }

    public boolean GetFunction()
    {
        return Function;
    }

    public boolean GetFunctionResult()
    {
        return FunctionResult;
    }

    public boolean GetParameter()
    {
        return Parameter;
    }

    public boolean GetArray()
    {
        return Array;
    }

    public boolean GetReserved()
    {
        return Reserved;
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + ": " + Name;
    }

    public void SetReserved(Boolean b)
    {
        Reserved = b;
    }

    public void SetParameter()
    {
        Parameter = true;
    }

    public void SetNumberOfParameters(int i)
    {
    }

    public Integer GetNumberOfParameters()
    {
        return null;
    }

    public void AddParameter(SymbolTableEntry e)
    {
    }

    public Integer GetUpperBound()
    {
        return null;
    }

    public Integer GetLowerBound()
    {
        return null;
    }

    public List GetParameterInfo()
    {
        return null;
    }

    public String GetType()
    {
        if (Type != null)
        {
            return Type;
        } else
        {
            return null;
        }
    }

    public Integer GetAddress()
    {
        if (Address != null)
        {
            return Address;
        } else
        {
            return null;
        }
    }

    public String GetName()
    {
        return Name;
    }

    public VariableEntry GetResult()
    {
        return null;
    }


}
