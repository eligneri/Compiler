public class SymbolTableEntry {
    protected String name;

    public boolean isVariable(){
        return false;
    }
    public boolean isProcedure(){
        return false;
    }
    public boolean isFunction(){
        return false;
    }
    public boolean isFunctionResult(){
        return false;
    }
    public boolean isParameter(){
        return false;
    }
    public boolean isArray(){
        return false;
    }
    public boolean isReserved(){
        return false;
    }


    public String toString(){
        return this.getClass().getSimpleName() + ": " + name;
    }

}
