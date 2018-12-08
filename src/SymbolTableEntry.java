import java.util.List;
public class SymbolTableEntry {
    protected String name;
    protected String type;
    protected Integer address;

    protected Boolean variable = false;
    protected Boolean procedure = false;
    protected Boolean function = false;
    protected Boolean functionResult = false;
    protected Boolean parameter = false;
    protected Boolean array = false;
    protected Boolean reserved = false;

    public boolean isVariable() {
        return variable;
    }

    public boolean isProcedure() {
        return procedure;
    }

    public boolean isFunction() {
        return function;
    }

    public boolean isFunctionResult() {
        return functionResult;
    }

    public boolean isParameter() {
        return parameter;
    }

    public boolean isArray() {
        return array;
    }

    public boolean isReserved() {
        return reserved;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ": " + name;
    }

    public void setReserved(Boolean b) {
        reserved = b;
    }

    public void setParameter(){parameter = true;}
    public void setNumberOfParameters(int i){}
    public Integer getNumberOfParameters(){return null;}
    public void addParameter(SymbolTableEntry e){}

    public Integer getUpperBound(){return null;}
    public Integer getLowerBound(){return null;}

    public List getParameterInfo(){return null;}
    public String getType() {
        if (type != null) {
            return type;
        } else {
            return null;
        }
    }

    public Integer getAddress() {
        if (address != null) {
            return address;
        } else {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public VariableEntry getResult() {
        return null;
    }


}
