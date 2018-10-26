public class SymbolTableEntry {
    protected String name;

    protected Boolean variable = false;
    protected Boolean procedure = false;
    protected Boolean function = false;
    protected Boolean functionResult = false;
    protected Boolean parameter = false;
    protected Boolean array = false;
    protected Boolean reserved = false;

    public boolean isVariable(){
        return variable;
    }
    public boolean isProcedure(){
        return procedure;
    }
    public boolean isFunction(){
        return function;
    }
    public boolean isFunctionResult(){
        return functionResult;
    }
    public boolean isParameter(){
        return parameter;
    }
    public boolean isArray(){
        return array;
    }
    public boolean isReserved(){
        return reserved;
    }

    public String toString(){
        return this.getClass().getSimpleName() + ": " + name;
    }

    public void setReserved(Boolean b){
        reserved = b;
    }
}
